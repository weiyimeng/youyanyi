package com.uyu.device.devicetraining.presentation.presenter.trial;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.trial.FracturedRulerConfig;
import com.uyu.device.devicetraining.data.entity.config.trial.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessageType;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.FracturedRulerTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialFracturedRulerTrainBack;
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
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.presenter.TrainPresenter;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevel;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;

import javax.inject.Inject;
import javax.inject.Named;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class TrialFracturedRulerTrainPresenter extends TrialTrainPresenter<FracturedRulerTrainPres,TrialFracturedRulerTrainBack,FracturedRulerTrainNormal> implements OnFinishResultListener,OnFailListener {
    /**
     * 构造函数
     */

    @Inject
    public TrialFracturedRulerTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
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
        trainBack = new TrialFracturedRulerTrainBack();
        trainNomal = new FracturedRulerTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        trainBack.setTrainingType(trainPres.getTrainingType());

        nowLevel=0;
        failTime=0;
        changeToNext();
    }

    @Override
    public void startTrain(FracturedRulerTrainPres trainPres) {
        super.startTrain(trainPres);

        trainBack.setTrainingType(trainPres.getTrainingType());

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

        trainBack.computeScore();

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
//        trainBackUseCase.createFracturedRuler(token, trainBack)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
//        trainBackUseCase.createFracturedRuler(token, trainBack)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CallBackPostTrainingBack(isUpdateReception));
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

    private int nowLevel = 0;
    private int failTime = 0;
    private FusionPicLevel fusionPicLevel;

    public void setFusionPicLevel(FusionPicLevel fusionPicLevel) {
        this.fusionPicLevel = fusionPicLevel;
        this.fusionPicLevel.setResultListener(this);
        this.fusionPicLevel.setFailListener(this);
    }

    private void passLevel(){
        if(nowLevel== com.uyu.device.devicetraining.data.entity.config.FracturedRulerConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else {
            trainStatus = TrainStatus.WAITING;

            deviceAdjust();
        }
    }

    private void changeToNext(){
        if(nowLevel == FracturedRulerConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else{
            nowLevel++;

            changePic();
        }
    }


    private void changePic(){
        int transformLevel = FracturedRulerConfig.getTransformLevel(nowLevel);

        failTime = 0;

        String formatImageName = "%s_%s_%s";
        String leftImageName = String.format(formatImageName,EnumTrainItem.FRACTURED_RULER.getName(),"left",transformLevel);
        String rightImageName = String.format(formatImageName, EnumTrainItem.FRACTURED_RULER.getName(),"right",transformLevel);

        int leftPicResId = context.getResources().getIdentifier(leftImageName, "drawable" , context.getPackageName());
        int rightPicResId = context.getResources().getIdentifier(rightImageName, "drawable" , context.getPackageName());

        if(leftPicResId==0 || rightPicResId==0){
            //成对看的，一个没有两个都默认
            leftPicResId = R.drawable.fractured_ruler_left_default;
            rightPicResId = R.drawable.fractured_ruler_right_default;
        }

        float intervalSpaceMM = FracturedRulerConfig.getIntervalSpace(transformLevel - 1);

        String tip = trainPres.getTrainingType().getName()+"-"+String.valueOf(nowLevel);
        tflistener.setItemTip(tip);
        fusionPicLevel.setParamas(intervalSpaceMM, leftPicResId, rightPicResId);

        FusionPicLevelConfiguration configuration = fusionPicLevel.getConfiguration();

        configuration.fusingTime =
                nowLevel == 1
                        ? StereoscopeConfig.Fusing_Max_Time_First
                        : StereoscopeConfig.Fusing_Max_Time;

        fusionPicLevel.startLevel();

        sendNowInfo();
    }

    /**
     * 设备调整
     */
    public void deviceAdjust(){
        int transformLevel = FracturedRulerConfig.getTransformLevel(nowLevel);

        ttsEngine.startSpeaking(RawProvider.wait_adjust_device);
        //获取下一级的等级的命令，nowlevel是+1了的
        ControlMessageSet messageSet = TrainUseCase.createFracturedRulerPrepare(transformLevel+1-1);
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
}
