package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.FracturedRulerConfig;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.PassTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessageType;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.FracturedRulerTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.FracturedRulerTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.FracturedRulerTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.RawProvider;
import com.uyu.device.devicetraining.presentation.adapter.OnFailListener;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevel;

import javax.inject.Inject;
import javax.inject.Named;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class FracturedRulerTrainPresenter extends TrainPresenter<FracturedRulerTrainPres,FracturedRulerTrainBack,FracturedRulerTrainNormal> implements OnFinishResultListener,OnFailListener {
    /**
     * 构造函数
     */

    @Inject
    public FracturedRulerTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface FracturedRulerListener{

    }

    private FracturedRulerListener itemListener;

    public void setItemListener(FracturedRulerListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * 每个项目都要独立实现的
     */

    @Override
    public void initStartStatus() {
        nowLevel = 0;
        failTime=0;
        super.initStartStatus();
    }

    @Override
    protected void newTrainBack() {
        trainBack = new FracturedRulerTrainBack();
        trainBack.setTrainingType(trainPres.getTrainingType());

        trainNomal = new FracturedRulerTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        nowLevel=0;
        failTime=0;
        changeToNext();
    }

    @Override
    public void sendNowInfo() {
        trainNomal.setNowLevel(nowLevel);
        trainNomal.setFailTime(failTime);

        super.sendNowInfo();
    }

    @Override
    public void finishTrainViewStatus() {
        fusionPicLevel.initViewAfterFinish();
    }

    @Override
    public void prepareFinishData() {
        //防止训练过程中出现，刚开始立刻结束的情况，会出现-1
        if(nowLevel==0){
            nowLevel=1;
        }
        trainBack.setResultDifficulty(nowLevel - 1);

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createFracturedRuler(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createFracturedRuler(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack(isUpdateReception));
    }

    @Override
    public void pauseTrain() {
        super.pauseTrain();

        fusionPicLevel.pauseTrain();
    }

    @Override
    public void resumeTrain() {
        super.resumeTrain();

        fusionPicLevel.resumeTrain();
    }

    /**
     * 每个项目额外可覆盖的父类的方法
     */

    @Override
    public void receiveOptMsg(TrainEmqttMessage trainEmqttMessage) {
        super.receiveOptMsg(trainEmqttMessage);
        if(trainEmqttMessage.getMsg().getTmt()== TrainMessageType.READY){
            showMsg(context.getString(R.string.receive_continue_instructions));
            changeToNext();
            trainStatus = TrainStatus.TRAINING;
        }
    }

    @Override
    public void pressEnter(){
        if(trainStatus==TrainStatus.WELCOME){
            showMsg(context.getString(R.string.training_has_not_yet_begun_please_wait));
        } else if (trainStatus==TrainStatus.WAITING){
            showMsg(context.getString(R.string.the_training_is_not_ready_please_wait));
        } else if(trainStatus==TrainStatus.TRAINING){
            fusionPicLevel.confirm();
        }else if(trainStatus==TrainStatus.PAUSING){
            showMsg(context.getString(R.string.pause_status_please_wait));
        }
    }

    /**
     * 单独的实现的接口
     */

    @Override
    public void onFinishResult(EnumResultType resultType) {
        if(resultType==EnumResultType.SUCCESS){
            passLevel();
        }else if(resultType==EnumResultType.FAIL){
            finishTrain();
        }
    }

    @Override
    public void onFail(int failTime) {
        this.failTime = failTime;
        sendNowInfo();
    }

    /**
     * 单独的部分
     */

    /**
     * 目前的等级，从0开始算的话是+1了的
     */
    private int nowLevel = 0;
    private int failTime = 0;
    private FusionPicLevel fusionPicLevel;

    public void setFusionPicLevel(FusionPicLevel fusionPicLevel) {
        this.fusionPicLevel = fusionPicLevel;
        this.fusionPicLevel.setResultListener(this);
        this.fusionPicLevel.setFailListener(this);
    }

    private void passLevel(){
        if(nowLevel== FracturedRulerConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else {
            showMsg(context.getString(R.string.complete) + nowLevel + context.getString(R.string.level)+"，"+context.getString(R.string.wait_for_the_next_optometrist));
            trainStatus = TrainStatus.WAITING;

            deviceAdjust();

//            EmqttMessage emqttMessage = new EmqttMessage();
//            emqttMessage.setStp(lastEmqttMessage.getRtp());
//            emqttMessage.setSid(lastEmqttMessage.getRid());
//            emqttMessage.setRtp(lastEmqttMessage.getStp());
//            emqttMessage.setRid(lastEmqttMessage.getSid());
//
//            TrainMessage trainMessage = new TrainMessage();
//            trainMessage.setTmt(TrainMessageType.PASS);
//
//            PassTrainMessageContent passTrainMessageContent = new PassTrainMessageContent();
//            passTrainMessageContent.setPassLevel(nowLevel);
//            trainMessage.setCt(passTrainMessageContent);
//
//            emqttMessage.setMsg(trainMessage);
//            sendMessage(emqttMessage);
            //准备完成的消息不用发了，电机就调节了
//        changeToNext();
//        if(nowLevel== TrainPresConfig.FracturedRuler_MaxLevel){
//            nowLevel++;
//            finishTrain();
//        }else {
//            showMsg("完成了" + nowLevel + "级，等待视光师下一步");
//            trainStatus = TrainStatus.WAITING;
//
//            TrainEmqttMessage trainEmqttMessage = new TrainEmqttMessage();
//            trainEmqttMessage.setStp(lastTrainEmqttMessage.getRtp());
//            trainEmqttMessage.setSid(lastTrainEmqttMessage.getRid());
//            trainEmqttMessage.setRtp(lastTrainEmqttMessage.getStp());
//            trainEmqttMessage.setRid(lastTrainEmqttMessage.getSid());
//
//            TrainMessage trainMessage = new TrainMessage();
//            trainMessage.setTmt(TrainMessageType.PASS);
//
//            PassTrainMessageContent passTrainMessageContent = new PassTrainMessageContent();
//            passTrainMessageContent.setPassLevel(nowLevel);
//            trainMessage.setCt(passTrainMessageContent);
//
//            trainEmqttMessage.setMsg(trainMessage);
//            sendMessage(trainEmqttMessage);
        }
    }

    private void changeToNext(){
        if(nowLevel== FracturedRulerConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else{
            nowLevel++;

            changePic();
        }
    }

    private void changePic(){
        failTime = 0;

        String formatImageName = "%s_%s_%s";
        String leftImageName = String.format(formatImageName,EnumTrainItem.FRACTURED_RULER.getName(),"left",nowLevel);
        String rightImageName = String.format(formatImageName, EnumTrainItem.FRACTURED_RULER.getName(),"right",nowLevel);

        int leftPicResId = context.getResources().getIdentifier(leftImageName, "drawable" , context.getPackageName());
        int rightPicResId = context.getResources().getIdentifier(rightImageName, "drawable" , context.getPackageName());

        if(leftPicResId==0 || rightPicResId==0){
            //成对看的，一个没有两个都默认
            leftPicResId = R.drawable.fractured_ruler_left_default;
            rightPicResId = R.drawable.fractured_ruler_right_default;
        }

        float intervalSpaceMM = FracturedRulerConfig.getIntervalSpace(nowLevel - 1);

        String tip = trainPres.getTrainingType().getName()+"-"+String.valueOf(nowLevel);
        tflistener.setItemTip(tip);
        fusionPicLevel.setParamas(intervalSpaceMM, leftPicResId, rightPicResId);

        fusionPicLevel.startLevel();

        sendNowInfo();
    }

    /**
     * 设备调整
     */
    public void deviceAdjust(){
        ttsEngine.startSpeaking(RawProvider.wait_adjust_device);
        //获取下一级的等级的命令，nowlevel是+1了的
        ControlMessageSet messageSet = TrainUseCase.createFracturedRulerPrepare(nowLevel+1-1);
        //messageSet.setHandler(handlerDeviceAdjustFinish);
        //MotorBus.getInstance().sendMessageSet(messageSet);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .flatMap(result -> ttsEngine.startSpeakingSubject(RawProvider.adjust_device_finish))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    changeToNext();
                    trainStatus = TrainStatus.TRAINING;
                });
    }

//    private Handler handlerDeviceAdjustFinish = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            ttsEngine.startSpeaking(R.raw.adjust_device_finish,handlerDeviceAdjustVoiceOver);
//        }
//    };
//
//    private Handler handlerDeviceAdjustVoiceOver = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            changeToNext();
//            trainStatus = TrainStatus.TRAINING;
//        }
//    };
}
