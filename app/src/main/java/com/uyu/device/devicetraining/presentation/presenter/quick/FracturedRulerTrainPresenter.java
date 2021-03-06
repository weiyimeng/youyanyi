package com.uyu.device.devicetraining.presentation.presenter.quick;

import android.content.Context;
import android.util.Log;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.FracturedRulerConfig;
import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
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
public class FracturedRulerTrainPresenter extends TrainPresenter<FracturedRulerTrainPres,FracturedRulerTrainBack,FracturedRulerTrainNormal> implements OnFinishResultListener,OnFailListener {
    /**
     * ????????????
     */

    @Inject
    public FracturedRulerTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * ???????????????????????????listener
     */

    public interface FracturedRulerListener{

    }

    private FracturedRulerListener itemListener;

    public void setItemListener(FracturedRulerListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * ?????????????????????????????????
     */

    @Override
    public void initStartStatus() {
        nowLevel = 1;
        failTime=0;
        passLevelCount = 0;
        backCount = 0;
        resultLevel = 1;
        super.initStartStatus();
    }

    @Override
    protected void newTrainBack() {
        trainBack = new FracturedRulerTrainBack();
        trainNomal = new FracturedRulerTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        trainBack.setTrainingType(trainPres.getTrainingType());

        nowLevel=1;
        nowLevel = trainPres.getStartDifficulty();
        if(nowLevel<1){
            nowLevel = 1;
        }

        passLevelCount = 0;
        backCount = 0;
        resultLevel = 1;

        refreshNowLevelShow();
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
        //??????????????????????????????resultLevel
        if(resultLevel<nowLevel){
            resultLevel = nowLevel;
        }
        //????????????????????????????????????????????????????????????????????????-1
        if(resultLevel==0){
            resultLevel=1;
        }
        trainBack.setResultDifficulty(resultLevel - 1);

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
     * ?????????????????????????????????????????????
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
     * ????????????????????????
     */

    @Override
    public void onFinishResult(EnumResultType resultType) {
        if(resultType==EnumResultType.SUCCESS){
            passLevel();
        }else if(resultType==EnumResultType.FAIL){
            //???????????????????????????????????????
            if(passLevelCount<FracturedRulerConfig.Need_Train_Total_Level_Count && backCount<FracturedRulerConfig.Back_Max_Time){
                backCount++;

                //??????????????????????????????resultLevel
                if(resultLevel<nowLevel){
                    resultLevel = nowLevel;
                }

                nowLevel = getLowLevel(nowLevel);
                change2Level(nowLevel);
            }else{
                finishTrain();
            }
        }
    }

    @Override
    public void onFail(int failTime) {
        this.failTime = failTime;
        sendNowInfo();
    }

    /**
     * ???????????????
     */

    private int nowLevel = 1;
    private int failTime = 0;
    private FusionPicLevel fusionPicLevel;
    /**
     * ???????????????
     */
    private int passLevelCount = 0;
    /**
     * ???????????????
     */
    private int backCount = 0;
    /**
     * ?????????????????????????????????
     */
    private int resultLevel = 1;


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
            nowLevel++;
            change2Level(nowLevel);
        }
    }

    /**
     * ????????????????????????
     * @param nowLevel
     */
    private void change2Level(int nowLevel){
        this.nowLevel = nowLevel;

        deviceAdjust();
    }

    private void changeToNext(){
        if(nowLevel== FracturedRulerConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else{
            nowLevel++;

            refreshNowLevelShow();
        }
    }

    private void refreshNowLevelShow(){
        failTime = 0;

        String formatImageName = "%s_%s_%s";
        String leftImageName = String.format(formatImageName,EnumTrainItem.FRACTURED_RULER.getName(),"left",nowLevel);
        String rightImageName = String.format(formatImageName, EnumTrainItem.FRACTURED_RULER.getName(),"right",nowLevel);

        int leftPicResId = context.getResources().getIdentifier(leftImageName, "drawable" , context.getPackageName());
        int rightPicResId = context.getResources().getIdentifier(rightImageName, "drawable" , context.getPackageName());

        if(leftPicResId==0 || rightPicResId==0){
            //??????????????????????????????????????????
            leftPicResId = R.drawable.fractured_ruler_left_default;
            rightPicResId = R.drawable.fractured_ruler_right_default;
        }

        float intervalSpaceMM = FracturedRulerConfig.getIntervalSpace(nowLevel - 1);

        String tip = trainPres.getTrainingType().getName()+"-"+String.valueOf(nowLevel);
        tflistener.setItemTip(tip);
        fusionPicLevel.setParamas(intervalSpaceMM, leftPicResId, rightPicResId);


        FusionPicLevelConfiguration configuration = fusionPicLevel.getConfiguration();

        configuration.fusingTime =
                nowLevel == 1 && backCount < 1
                        ? StereoscopeConfig.Fusing_Max_Time_First
                        : StereoscopeConfig.Fusing_Max_Time;

        fusionPicLevel.startLevel();

        sendNowInfo();
    }

    /**
     * ???????????????
     * @param level
     */
    public int getLowLevel(int level){
        int lowLevel = level-2;
        if(lowLevel<1){
            lowLevel = 1;
        }
        return lowLevel;
    }

    /**
     * ????????????
     */
    public void deviceAdjust(){
        ttsEngine.startSpeaking(RawProvider.wait_adjust_device);
        trainStatus = TrainStatus.ADJUST;
        tflistener.changeToStatus(trainStatus);

        //???????????????????????????
        ControlMessageSet messageSet = TrainUseCase.createFracturedRulerPrepare(nowLevel-1);
        //messageSet.setHandler(handlerDeviceAdjustFinish);
        //MotorBus.getInstance().sendMessageSet(messageSet);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .flatMap(result -> ttsEngine.startSpeakingSubject(RawProvider.adjust_device_finish))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    trainStatus = TrainStatus.TRAINING;
                    tflistener.changeToStatus(trainStatus);

                    refreshNowLevelShow();
                });
    }
}
