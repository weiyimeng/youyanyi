package com.uyu.device.devicetraining.presentation.presenter.trial;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.RGVariableVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainback.trial.TrialRGVariableVectorTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.TrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.RGVariableVectorTrainPres;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.presenter.TrainPresenter;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.view.widget.RGVariableVectorView;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Named;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class TrialRGVariableVectorTrainPresenter extends TrialTrainPresenter<RGVariableVectorTrainPres,TrialRGVariableVectorTrainBack,TrainNormal> implements OnFinishResultListener {
    /**
     * 构造函数
     */

    @Inject
    public TrialRGVariableVectorTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface RGVariableVectorListener{

    }

    private RGVariableVectorListener itemListener;

    public void setItemListener(RGVariableVectorListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * 每个项目都要独立实现的
     */

    @Override
    public void initStartStatus() {
        super.initStartStatus();
    }

    @Override
    protected void newTrainBack() {
        trainBack = new TrialRGVariableVectorTrainBack();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        trainBack.setTrainingType(trainPres.getTrainingType());

        rgVariableVectorView.setFusionTrain(trainPres.getTrainingType());
        rgVariableVectorView.startTrain();
    }

    @Override
    public void startTrain(RGVariableVectorTrainPres trainPres) {
        super.startTrain(trainPres);

        trainBack.setTrainingType(trainPres.getTrainingType());

        rgVariableVectorView.setFusionTrain(trainPres.getTrainingType());
        rgVariableVectorView.startTrain();
    }

    @Override
    public void sendNowInfo() {

    }

    @Override
    public void finishTrainViewStatus() {
        rgVariableVectorView.initViewAfterFinish();
    }

    @Override
    public void prepareFinishData() {
        double maxMoveDistance = rgVariableVectorView.getMaxMoveDistance();
        //精确保留两位小数
        BigDecimal bd = new BigDecimal(maxMoveDistance);
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        trainBack.setCriticalLocation(bd.doubleValue());

        trainBack.computeScore();

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
//        trainBackUseCase.createRGVariableVector(token, trainBack)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
//        trainBackUseCase.createRGVariableVector(token, trainBack)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CallBackPostTrainingBack(isUpdateReception));
    }

    @Override
    public void pauseTrain() {
        super.pauseTrain();

        rgVariableVectorView.pauseTrain();
    }

    @Override
    public void resumeTrain() {
        super.resumeTrain();

        rgVariableVectorView.resumeTrain();
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
            rgVariableVectorView.confirm();
        }else if(trainStatus==TrainStatus.PAUSING){
            showMsg(context.getString(R.string.pause_status_please_wait));
        }
    }

    /**
     * 单独的实现的接口
     */

    @Override
    public void onFinishResult(EnumResultType resultType) {
        finishTrain();
//        if(resultType==EnumResultType.SUCCESS){
//            passLevel();
//        }else if(resultType==EnumResultType.FAIL){
//            finishTrain();
//        }
    }

    /**
     * 单独的部分
     */

    private RGVariableVectorView rgVariableVectorView;

    public void setRgVariableVectorView(RGVariableVectorView rgVariableVectorView) {
        this.rgVariableVectorView = rgVariableVectorView;
        this.rgVariableVectorView.setResultListener(this);
    }
}
