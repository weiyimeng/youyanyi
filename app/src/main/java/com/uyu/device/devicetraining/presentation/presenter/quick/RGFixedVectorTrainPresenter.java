package com.uyu.device.devicetraining.presentation.presenter.quick;

import android.content.Context;
import android.util.Log;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.RGFixedVectorConfig;
import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.RGFixedVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.RGFixedVectorTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.RGFixedVectorTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumColorType;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnFailListener;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.presenter.TrainPresenter;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevelConfiguration;
import com.uyu.device.devicetraining.presentation.view.widget.RedGreenFusionPicLevel;

import javax.inject.Inject;
import javax.inject.Named;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class RGFixedVectorTrainPresenter extends TrainPresenter<RGFixedVectorTrainPres,RGFixedVectorTrainBack,RGFixedVectorTrainNormal> implements OnFinishResultListener,OnFailListener {
    /**
     * 构造函数
     */

    @Inject
    public RGFixedVectorTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface RGFixedVectorListener{

    }

    private RGFixedVectorListener itemListener;

    public void setItemListener(RGFixedVectorListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * 每个项目都要独立实现的
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
        trainBack = new RGFixedVectorTrainBack();
        trainNomal = new RGFixedVectorTrainNormal();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        trainBack.setTrainingType(trainPres.getTrainingType());

        nowLevel=1;
        nowLevel = trainPres.getStartDifficulty();
        nowLevel = getLowLevel(nowLevel);

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
        //讲最好的结果关赋值给resultLevel
        if(resultLevel<nowLevel){
            resultLevel = nowLevel;
        }
        //防止训练过程中出现，刚开始立刻结束的情况，会出现-1
        if(resultLevel<1){
            resultLevel=1;
        }
        trainBack.setResultDifficulty(resultLevel - 1);

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createRGFixedVector(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createRGFixedVector(token, trainBack)
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
    public void pressEnter() {
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
            //通过总关数小并且返回次数小
            if(passLevelCount<RGFixedVectorConfig.Need_Train_Total_Level_Count && backCount<RGFixedVectorConfig.Back_Max_Time){
                backCount++;

                //讲最好的结果关赋值给resultLevel
                if(resultLevel<nowLevel){
                    resultLevel = nowLevel;
                }

                nowLevel = getLowLevel(nowLevel);
                refreshNowLevelShow();
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
     * 单独的部分
     */

    private int nowLevel = 1;
    private int failTime = 0;
    private RedGreenFusionPicLevel fusionPicLevel;
    /**
     * 过关的关数
     */
    private int passLevelCount = 0;
    /**
     * 返回的次数
     */
    private int backCount = 0;
    /**
     * 结果关数，取最好的关数
     */
    private int resultLevel = 0;

    public void setFusionPicLevel(RedGreenFusionPicLevel fusionPicLevel) {
        this.fusionPicLevel = fusionPicLevel;
        this.fusionPicLevel.setResultListener(this);
        this.fusionPicLevel.setFailListener(this);
    }

    private void passLevel(){
        passLevelCount++;

        if(nowLevel== RGFixedVectorConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else {
//            showMsg("完成了" + nowLevel + "级");
            nowLevel++;
            refreshNowLevelShow();
        }
    }

    private void refreshNowLevelShow(){
        failTime = 0;

        EnumFusionTrain fusionTrain = trainPres.getTrainingType();

        String formatImageName = "%s_%s_%s";
        String redImageName = String.format(formatImageName,EnumTrainItem.R_G_FIXED_VECTOR.getName(), EnumColorType.RED.getName().toLowerCase(),nowLevel);
        String greenImageName = String.format(formatImageName, EnumTrainItem.R_G_FIXED_VECTOR.getName(), EnumColorType.GREEN.getName().toLowerCase(), nowLevel);

        int redPicResId = context.getResources().getIdentifier(redImageName, "drawable" , context.getPackageName());
        int greenPicResId = context.getResources().getIdentifier(greenImageName, "drawable" , context.getPackageName());

        if(redPicResId==0 || greenPicResId==0 ){
            //成对看的，一个没有两个都默认
            redPicResId = R.drawable.r_g_fixed_vector_red_default;
            greenPicResId = R.drawable.r_g_fixed_vector_green_default;
        }

        float intervalSpaceMM = RGFixedVectorConfig.getIntervalSpace(nowLevel - 1);

        String tip = context.getString(R.string.current_level)+String.valueOf(nowLevel);
        tflistener.setItemTip(tip);
        fusionPicLevel.setParamas(fusionTrain,intervalSpaceMM,redPicResId,RGFixedVectorConfig.redAphla,greenPicResId,RGFixedVectorConfig.greenAphla);


        FusionPicLevelConfiguration configuration = fusionPicLevel.getConfiguration();

        configuration.fusingTime =
                nowLevel==1 && backCount < 1
                        ? StereoscopeConfig.Fusing_Max_Time_First
                        : StereoscopeConfig.Fusing_Max_Time;

        fusionPicLevel.startLevel();

        sendNowInfo();
    }

    /**
     * 降低的关数
     * @param level
     */
    public int getLowLevel(int level){
        int lowLevel = level-2;
        if(lowLevel<1){
            lowLevel = 1;
        }
        return lowLevel;
    }
}
