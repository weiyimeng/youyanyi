package com.uyu.device.devicetraining.presentation.util;

import android.os.Handler;
import android.os.Message;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.ApproachConfig;
import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.motor.BackMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.ControlType;
import com.uyu.device.devicetraining.data.motor.DirectionType;
import com.uyu.device.devicetraining.data.motor.DistanceHandler;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.view.adapter.OnLocationChangeListener;
import com.uyu.device.devicetraining.presentation.view.widget.ApproachView;

import java.util.concurrent.TimeUnit;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 2015/12/28.
 */
public class ApproachRail {
    public double location = ApproachConfig.MaxDistance;
    public int maxLocation = ApproachConfig.MaxDistance-DistanceHandler.ApproachNeedleOriginDistance;
    public int minLocation = ApproachConfig.MinDistance-DistanceHandler.ApproachNeedleOriginDistance;

    private static int forwardSpeedLevel = 5;
    private static int backwardSpeedLevel = 3;

    /**
     * 方向
     */
    private DirectionType direction = DirectionType.BACKWARD;
    /**
     * 速度级别
     */
    private int speedLevel = forwardSpeedLevel;


    private int moveTime = 1000;

    private OnLocationChangeListener listener;
    private ApproachView approachView;

    private boolean isMove = false;
    /**
     * 计时间单位
     */
    private static final long msUnit = 100;
    private static final long MaxTimeCount = 300;
    /**
     * 运动的最长的毫秒时间
     */
    private static final long moveMaxMs = MaxTimeCount*msUnit;
    /**
     * 已用时间次数
     */
    private long hasUseTimeCount = 0;
    /**
     * 预期是时间次数
     */
    private long expectedUseTimeCount= MaxTimeCount;

    public void setListener(OnLocationChangeListener listener) {
        this.listener = listener;
    }

    public void setApproachView(ApproachView approachView) {
        this.approachView = approachView;
    }

    /**
     * 复位
     */
    public void reset(){
        location = ApproachConfig.MaxDistance;
        direction = DirectionType.BACKWARD;
        speedLevel = forwardSpeedLevel;

        hasUseTimeCount = 0;
        expectedUseTimeCount = MaxTimeCount;
    }

    /**
     * 运动中时间计时
     */
    private Subscription moveSubscription;

    @DebugLog
    public void startMove(){
        Timber.d("startMove");
        ControlMessageSet messageSet = TrainUseCase.createBaffleMove(direction, speedLevel);
        //MotorBus.getInstance().sendMessageSet(messageSet);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                });

        isMove = true;
//        startAskPos();
        moveSubscription = Observable.interval(msUnit, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timeCount -> {
                    hasUseTimeCount++;
                    if(hasUseTimeCount==expectedUseTimeCount){
                        if(direction==DirectionType.BACKWARD){
                            zeroStop();
                        }else if(direction==DirectionType.FORWARD){
                            overMaxStop();
                        }
                    }
                });
    }

    @DebugLog
    public void stopMove(){
        Timber.d("stopMove");
        ControlMessageSet messageSet = TrainUseCase.createBaffleStop();
        //MotorBus.getInstance().sendMessageSet(messageSet);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                });

        isMove = false;
//        stopAskPos();

        if(moveSubscription!=null && !moveSubscription.isUnsubscribed()){
            moveSubscription.unsubscribe();
            moveSubscription = null;
        }
    }

    /**
     * 结束之后的移动，直接移动到停止
     */
    public void endMove(){
        ControlMessageSet messageSet = TrainUseCase.createBaffleMove(direction, speedLevel);
        //MotorBus.getInstance().sendMessageSet(messageSet);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                });
    }

    public void changeBackDirection(){
        direction = DirectionType.FORWARD;
        speedLevel = backwardSpeedLevel;

        expectedUseTimeCount = hasUseTimeCount;
        hasUseTimeCount = 0;
    }

    private void zeroStop(){
        approachView.zeroStop();
    }

    private void overMaxStop(){
        approachView.overMaxStop();
    }
}
