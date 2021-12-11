package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.StereoscopeTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.StereoscopeTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.entity.type.EnumTrainItem;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnFailListener;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.view.widget.FusionPicLevel;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class StereoscopeTrainPresenter extends TrainPresenter<StereoscopeTrainPres,StereoscopeTrainBack,StereoscopeTrainNormal> implements OnFinishResultListener,OnFailListener {
    /**
     * 构造函数
     */

    @Inject
    public StereoscopeTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface StereoscopeListener{

    }

    private StereoscopeListener itemListener;

    public void setItemListener(StereoscopeListener itemListener) {
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
        trainBack = new StereoscopeTrainBack();
        trainBack.setTrainingType(trainPres.getTrainingType());
        trainNomal = new StereoscopeTrainNormal();
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
        if(nowLevel== StereoscopeConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else {
//            showMsg("完成了" + nowLevel + "级");

            changeToNext();
        }
    }

    private void changeToNext(){
        nowLevel++;
        failTime = 0;

        EnumFusionTrain fusionTrain = trainPres.getTrainingType();

        String formatImageName = "%s_%s_%s_%s";
        String leftImageName = String.format(formatImageName,EnumTrainItem.STEREOSCOPE.getName(),fusionTrain.getName().toLowerCase(),"left",nowLevel);
        String rightImageName = String.format(formatImageName,EnumTrainItem.STEREOSCOPE.getName(),fusionTrain.getName().toLowerCase(),"right",nowLevel);

        int leftPicResId = context.getResources().getIdentifier(leftImageName, "drawable" , context.getPackageName());
        int rightPicResId = context.getResources().getIdentifier(rightImageName, "drawable" , context.getPackageName());

        if(leftPicResId==0 || rightPicResId==0){
            //成对看的，一个没有两个都默认
            if(fusionTrain==EnumFusionTrain.BO){
                leftPicResId = R.drawable.stereoscope_bo_left_default;
                rightPicResId = R.drawable.stereoscope_bo_right_default;
            }else{
                leftPicResId = R.drawable.stereoscope_bi_left_default;
                rightPicResId = R.drawable.stereoscope_bi_right_default;
            }
        }

        float intervalSpaceMM = StereoscopeConfig.getIntervalSpace(fusionTrain, nowLevel - 1);

        String tip = trainPres.getTrainingType().getName()+"-"+String.valueOf(nowLevel);
        tflistener.setItemTip(tip);

        fusionPicLevel.setParamas(intervalSpaceMM, leftPicResId, rightPicResId);

        fusionPicLevel.startLevel();

        sendNowInfo();
    }
}
