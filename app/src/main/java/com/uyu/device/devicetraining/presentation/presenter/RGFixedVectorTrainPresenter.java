package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.config.RGFixedVectorConfig;
import com.uyu.device.devicetraining.data.entity.config.StereoscopeConfig;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.RGFixedVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.RGFixedVectorTrainNormal;
import com.uyu.device.devicetraining.data.entity.trainnormal.TrainNormal;
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
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.view.widget.RedGreenFusionPicLevel;
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
public class RGFixedVectorTrainPresenter extends TrainPresenter<RGFixedVectorTrainPres,RGFixedVectorTrainBack,RGFixedVectorTrainNormal> implements OnFinishResultListener,OnFailListener {
    /**
     * ????????????
     */

    @Inject
    public RGFixedVectorTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * ???????????????????????????listener
     */

    public interface RGFixedVectorListener{

    }

    private RGFixedVectorListener itemListener;

    public void setItemListener(RGFixedVectorListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * ?????????????????????????????????
     */

    @Override
    public void initStartStatus() {
        nowLevel = 0;
        failTime=0;
        super.initStartStatus();
    }

    @Override
    protected void newTrainBack() {
        trainBack = new RGFixedVectorTrainBack();
        trainBack.setTrainingType(trainPres.getTrainingType());
        trainNomal = new RGFixedVectorTrainNormal();
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
        //????????????????????????????????????????????????????????????????????????-1
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
     * ?????????????????????????????????????????????
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
     * ????????????????????????
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
     * ???????????????
     */

    private int nowLevel = 0;
    private int failTime = 0;
    private RedGreenFusionPicLevel fusionPicLevel;

    public void setFusionPicLevel(RedGreenFusionPicLevel fusionPicLevel) {
        this.fusionPicLevel = fusionPicLevel;
        this.fusionPicLevel.setResultListener(this);
        this.fusionPicLevel.setFailListener(this);
    }

    private void passLevel(){
        if(nowLevel== RGFixedVectorConfig.Max_Level){
            nowLevel++;
            finishTrain();
        }else {
//            showMsg("?????????" + nowLevel + "???");

            changeToNext();
        }
    }

    private void changeToNext(){
        nowLevel++;
        failTime = 0;

        EnumFusionTrain fusionTrain = trainPres.getTrainingType();

        String formatImageName = "%s_%s_%s";
        String redImageName = String.format(formatImageName,EnumTrainItem.R_G_FIXED_VECTOR.getName(), EnumColorType.RED.getName().toLowerCase(),nowLevel);
        String greenImageName = String.format(formatImageName, EnumTrainItem.R_G_FIXED_VECTOR.getName(), EnumColorType.GREEN.getName().toLowerCase(), nowLevel);

        int redPicResId = context.getResources().getIdentifier(redImageName, "drawable" , context.getPackageName());
        int greenPicResId = context.getResources().getIdentifier(greenImageName, "drawable" , context.getPackageName());

        if(redPicResId==0 || greenPicResId==0 ){
            //??????????????????????????????????????????
            redPicResId = R.drawable.r_g_fixed_vector_red_default;
            greenPicResId = R.drawable.r_g_fixed_vector_green_default;
        }

        float intervalSpaceMM = RGFixedVectorConfig.getIntervalSpace(nowLevel - 1);

        String tip = context.getString(R.string.current_level)+String.valueOf(nowLevel);
        tflistener.setItemTip(tip);
        fusionPicLevel.setParamas(fusionTrain,intervalSpaceMM,redPicResId,RGFixedVectorConfig.redAphla,greenPicResId,RGFixedVectorConfig.greenAphla);

        fusionPicLevel.startLevel();

        sendNowInfo();
    }
}
