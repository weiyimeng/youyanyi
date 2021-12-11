package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.TrainBackApiResult;
import com.uyu.device.devicetraining.data.entity.config.ApproachConfig;
import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.ApproachTrainBack;
import com.uyu.device.devicetraining.data.entity.trainnormal.TrainNormal;
import com.uyu.device.devicetraining.data.entity.trainpres.ApproachTrainPres;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.domain.interactor.TrainBackUseCase;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.view.widget.ApproachView;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;


/**
 * 训练流程：
 * 训练开始后，提示合成分成两个按键
 * （1）如果一直没有按到原点，就结束后退到350mm处；
 * （2）按键停止，提示合成一个后按键
 * （2-1）合成一个后按键，走合成一个的逻辑
 * （2-2）如果保持了10s中还没有合成一个按键，则往后移动走，变成两个的逻辑
 * （2-3）如果保持10s钟，结束推到350mm处；
 * （2-4）如果10s内按键，提示合成一个后按键
 * （2-4-1）按键停止，循环步骤2，但道理肯定会按停止
 * （2-4-2）如果一直不按键，会碰撞到最远点的平板，结束
 *
 * 实现思路：
 * 发现没法开始运动后判断是否到达原点或碰撞到最远点的平板
 * 只能通过时间去判断，如果在运动过程中，10s没按，就算finish结束了，再走逻辑推到350mm处结束
 */

/**
 * Created by windern on 2015/12/4.
 */
@PerActivity
public class ApproachTrainPresenter extends TrainPresenter<ApproachTrainPres,ApproachTrainBack,TrainNormal> implements OnFinishResultListener {
    /**
     * 构造函数
     */

    @Inject
    public ApproachTrainPresenter(@Named("trainBackUseCase") TrainBackUseCase trainBackUseCase
            , @Named("contentUseCase") ContentUseCase contentUseCase
            , Context context
            , TtsEngine ttsEngine) {
        super(trainBackUseCase,contentUseCase, context, ttsEngine);
    }

    /**
     * 每个项目单独默认的listener
     */

    public interface ApproachListener{

    }

    private ApproachListener itemListener;

    public void setItemListener(ApproachListener itemListener) {
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
        trainBack = new ApproachTrainBack();
    }

    @Override
    public void startTrain(PresTrainMessageContent presTrainMessageContent) {
        super.startTrain(presTrainMessageContent);

        approachView.start();
    }

    @Override
    public void sendNowInfo() {

    }

    @Override
    public void finishTrainViewStatus() {
        approachView.initViewAfterFinish();
        //结束以后重置所有电机
        //finishDeviceResetAll();
    }

    @Override
    public void prepareFinishData() {
        trainBack.setCriticalLocation(approachView.getCriticalLocation());
        trainBack.setRealDwellTime(10);
        trainBack.setEyeChangeType(1);

        super.prepareFinishData();
    }

    @Override
    public void postData() {
        super.postData();
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createApproach(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack());
    }

    @Override
    public void postData(boolean isUpdateReception) {
        super.postData(isUpdateReception);
        String token = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN);
        trainBackUseCase.createApproach(token, trainBack)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CallBackPostTrainingBack(isUpdateReception));
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
            Calendar now = Calendar.getInstance();
            long nowTime = now.getTime().getTime();

            //去抖
            if(nowTime-pressEnterTime>ApproachConfig.ShakeTime){
                realPressEnter();
            }
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
    }

    /**
     * 单独的部分
     */

    private long pressEnterTime = 0;

    private ApproachView approachView;

    public void setApproachView(ApproachView approachView) {
        this.approachView = approachView;
        this.approachView.setResultListener(this);
    }

    public void realPressEnter(){
        approachView.pressEnter();
    }

    /**
     * 设备电机也重置
     */
    protected void finishDeviceResetAll() {
        ControlMessageSet messageSet = TrainUseCase.reset();

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                });
    }
}
