package com.uyu.device.devicetraining.presentation.presenter.quick;

import android.content.Context;
import android.util.Log;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.ReversalConfig;
import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.message.ControlsetTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.StereoscopeTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.StereoscopeTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.EnumMotorNum;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.PupilMotorController;
import com.uyu.device.devicetraining.domain.motor.ScreenMotorController;
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

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class StereoscopeTrainPresenter extends TrainPresenter<StereoscopeTrainPres, StereoscopeTrainBack, StereoscopeTrainNormal> implements OnFinishResultListener, OnFailListener {
    /**
     * ????????????
     */

    @Inject
    public StereoscopeTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase, contentUseCase, context, ttsEngine);
    }

    /**
     * ???????????????????????????listener
     */

    public interface StereoscopeListener {

    }

    private StereoscopeListener itemListener;

    public void setItemListener(StereoscopeListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * ?????????????????????????????????
     */

    @Override
    public void initStartStatus() {
        nowLevel = 1;
        failTime = 0;
        passLevelCount = 0;
        backCount = 0;
        resultLevel = 1;
        super.initStartStatus();
    }

    @Override
    protected void newTrainBack() {
        trainBack = new StereoscopeTrainBack();
        trainNomal = new StereoscopeTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        trainBack.setTrainingType(trainPres.getTrainingType());

        nowLevel = 1;
        nowLevel = trainPres.getStartDifficulty();

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
        if (resultLevel < nowLevel) {
            resultLevel = nowLevel;
        }
        //????????????????????????????????????????????????????????????????????????-1
        if (resultLevel == 0) {
            resultLevel = 1;
        }
        trainBack.setResultDifficulty(resultLevel - 1);

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createStereoscope(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createStereoscope(token, trainBack)
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
    public void pressEnter() {
        if (trainStatus == TrainStatus.WELCOME) {
            showMsg(context.getString(R.string.training_has_not_yet_begun_please_wait));
        } else if (trainStatus == TrainStatus.WAITING) {
            showMsg(context.getString(R.string.the_training_is_not_ready_please_wait));
        } else if (trainStatus == TrainStatus.TRAINING) {
            fusionPicLevel.confirm();
        } else if (trainStatus == TrainStatus.PAUSING) {
            showMsg(context.getString(R.string.pause_status_please_wait));
        }
    }

    /**
     * ????????????????????????
     */

    @Override
    public void onFinishResult(EnumResultType resultType) {
        if (resultType == EnumResultType.SUCCESS) {
            passLevel();
        } else if (resultType == EnumResultType.FAIL) {
            //???????????????????????????????????????
            if (passLevelCount < StereoscopeConfig.Need_Train_Total_Level_Count && backCount < StereoscopeConfig.Back_Max_Time) {
                //??????????????????????????????resultLevel
                if (resultLevel < nowLevel) {
                    resultLevel = nowLevel;
                }

                nowLevel = getLowLevel(nowLevel);
                backCount++;
                refreshNowLevelShow();

            } else {
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

    /**
     * ?????????????????????????????????????????????
     * ???????????????????????????????????????
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
    private int resultLevel = 0;

    public void setFusionPicLevel(FusionPicLevel fusionPicLevel) {
        this.fusionPicLevel = fusionPicLevel;
        this.fusionPicLevel.setResultListener(this);
        this.fusionPicLevel.setFailListener(this);
    }

    private void passLevel() {
        passLevelCount++;

        if (nowLevel == StereoscopeConfig.Max_Level) {
            nowLevel++;
            finishTrain();
        } else {
//            showMsg("?????????" + nowLevel + "???");
            nowLevel++;
            refreshNowLevelShow();
        }
    }

    /**
     * ????????????????????????
     */
    private void refreshNowLevelShow() {
        failTime = 0;

        EnumFusionTrain fusionTrain = trainPres.getTrainingType();

        String formatImageName = "%s_%s_%s_%s";
        String leftImageName = String.format(formatImageName, EnumTrainItem.STEREOSCOPE.getName(), fusionTrain.getName().toLowerCase(), "left", nowLevel);
        String rightImageName = String.format(formatImageName, EnumTrainItem.STEREOSCOPE.getName(), fusionTrain.getName().toLowerCase(), "right", nowLevel);

        int leftPicResId = context.getResources().getIdentifier(leftImageName, "drawable", context.getPackageName());
        int rightPicResId = context.getResources().getIdentifier(rightImageName, "drawable", context.getPackageName());

        if (leftPicResId == 0 || rightPicResId == 0) {
            //??????????????????????????????????????????
            if (fusionTrain == EnumFusionTrain.BO) {
                leftPicResId = R.drawable.stereoscope_bo_left_default;
                rightPicResId = R.drawable.stereoscope_bo_right_default;
            } else {
                leftPicResId = R.drawable.stereoscope_bi_left_default;
                rightPicResId = R.drawable.stereoscope_bi_right_default;
            }
        }

        float intervalSpaceMM = StereoscopeConfig.getIntervalSpace(fusionTrain, nowLevel - 1);

        String tip = trainPres.getTrainingType().getName() + "-" + String.valueOf(nowLevel);
        tflistener.setItemTip(tip);

        fusionPicLevel.setParamas(intervalSpaceMM, leftPicResId, rightPicResId);

        //??????????????????????????????
        FusionPicLevelConfiguration configuration = fusionPicLevel.getConfiguration();
        configuration.keepingTime = StereoscopeConfig.Keeping_Max_Time_Easy;

        configuration.fusingTime =
                nowLevel == 1 && backCount < 1
                        ? StereoscopeConfig.Fusing_Max_Time_First
                        : StereoscopeConfig.Fusing_Max_Time;
        if (nowLevel % 2 == 0) {
            configuration.keepingTime = StereoscopeConfig.Keeping_Max_Time_Hard;
        }


        fusionPicLevel.startLevel();

        sendNowInfo();
    }

    /**
     * ???????????????
     *
     * @param level
     */
    public int getLowLevel(int level) {
        int lowLevel = level - 4;

        if (lowLevel < 1) {
            lowLevel = 1;
        } else {
            if (lowLevel % 2 == 0) {
                lowLevel--;
            }
        }
        return lowLevel;
    }

    @Override
    public boolean isCanHandleControlsetMessage() {
        return true;
    }

    @Override
    public boolean checkValue(ControlsetTrainMessageContent controlsetTrainMessageContent) {
        boolean isCheckPass = false;
        if (controlsetTrainMessageContent.getMotorNum() == EnumMotorNum.SCREEN.getValue()
                && controlsetTrainMessageContent.getValue() >= StereoscopeConfig.Screen_Pos_Min_Level
                && controlsetTrainMessageContent.getValue() <= StereoscopeConfig.Screen_Pos_Max_Level) {
            //??????????????????????????????????????????
            isCheckPass = true;
        }
        return isCheckPass;
    }

    @Override
    public ControlMessageSet transfer(ControlsetTrainMessageContent controlsetTrainMessageContent) {
        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();

        int screenLocation = StereoscopeConfig.getMotorScreenLocation(controlsetTrainMessageContent.getValue());
        ScreenMotorController screenMotCtrl = motorBus.screenMotCtrl;
        ControlMessage screenMessage = screenMotCtrl.setLocation(screenLocation);
        messageSet.addMessage(screenMessage);

        return messageSet;
    }

    @Override
    public void sendControlMotorMsg(ControlMessageSet messageSet) {
        super.sendControlMotorMsg(messageSet);
    }
}
