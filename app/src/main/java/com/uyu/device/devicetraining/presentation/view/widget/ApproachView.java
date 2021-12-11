package com.uyu.device.devicetraining.presentation.view.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.ApproachConfig;
import com.uyu.device.devicetraining.data.motor.BackMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessage;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.data.motor.ControlType;
import com.uyu.device.devicetraining.data.motor.DistanceHandler;
import com.uyu.device.devicetraining.domain.motor.BaffleMotorController;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.ScreenMotorController;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.domain.motor.TurntableMotorController;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.type.EnumApproachStatus;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.util.ApproachRail;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.adapter.OnLocationChangeListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 2015/12/19.
 */
public class ApproachView extends RelativeLayout implements OnLocationChangeListener{
    private Context context;
    private View view;

    @Bind(R.id.tv_location)
    TextView tv_location;

    protected OnFinishResultListener resultListener;
    protected TtsEngine ttsEngine;

    private EnumApproachStatus status = EnumApproachStatus.INIT;
    private double criticalLocation = ApproachConfig.MaxDistance;

    private ApproachRail approachRail = new ApproachRail();

    /**
     * 开始超时时间-直接到头了还没按下，就算最好
     */
    private static int startOverTime = 30*1000;

    public ApproachView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public ApproachView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public ApproachView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

    /**
     * 初始化布局
     */
    public void initLayout(){
        view = LayoutInflater.from(context).inflate(R.layout.layout_approach_view, this,
                true);
        ButterKnife.bind(this, view);

        approachRail.setListener(this);
        approachRail.setApproachView(this);
    }

    public void setResultListener(OnFinishResultListener resultListener) {
        this.resultListener = resultListener;
    }

    /**
     * 是否开始以后移动
     */
    private boolean isStartMove = false;
    public void pressEnter(){
        if(status==EnumApproachStatus.START){
            //只有提示完成，开始移动后才接受开始后的按键
            if(isStartMove) {
                approachRail.stopMove();
                approachRail.changeBackDirection();

                status = EnumApproachStatus.TWO_SPLIT;
                speakTip(status);
            }
        }else if(status==EnumApproachStatus.TWO_SPLIT){
            stopTwoSplitKeepTime();

            status = EnumApproachStatus.ONE_KEEP;
            speakTip(status);
        }else if(status==EnumApproachStatus.ONE_KEEP){
            stopKeepTime();

            status = EnumApproachStatus.TWO_MOVE;
            speakTip(status);
        }else if(status==EnumApproachStatus.TWO_MOVE){
            approachRail.stopMove();

            status = EnumApproachStatus.ONE_KEEP;
            speakTip(status);
        }
    }

    public void start(){
        criticalLocation = ApproachConfig.MaxDistance;
        onLocationChange(ApproachConfig.MaxDistance);
        approachRail.reset();

        restart();
    }

    private void restart(){
        resetInitStatus();

        status = EnumApproachStatus.START;
        speakTip(status);
    }

    private void speakTip(EnumApproachStatus nowStatus){
        ttsEngine.stopSpeaking();
        ttsEngine.startSpeaking(nowStatus.getTipResid());

        if(nowStatus==EnumApproachStatus.START){
            speakStartHandler.postDelayed(speakStartRunnable,nowStatus.getAfterTime());
        }else if(nowStatus==EnumApproachStatus.TWO_SPLIT){
            speakTwoSplitHandler.postDelayed(speakTwoSplitRunnable,nowStatus.getAfterTime());
        }else if(nowStatus==EnumApproachStatus.ONE_KEEP){
            speakOneKeepHandler.postDelayed(speakOneKeepRunnable,nowStatus.getAfterTime());
        }else if(nowStatus==EnumApproachStatus.TWO_MOVE){
            speakTwoMoveHandler.postDelayed(speakTwoMoveRunnable,nowStatus.getAfterTime());
        }
    }

    protected Handler speakStartHandler = new Handler();
    protected Runnable speakStartRunnable = new Runnable() {
        @Override
        public void run() {
            isStartMove = true;
            approachRail.startMove();
        }
    };
    protected void stopSpeakStartHandler(){
        speakStartHandler.removeCallbacks(speakStartRunnable);
    }

    protected Handler speakTwoSplitHandler = new Handler();
    protected Runnable speakTwoSplitRunnable = new Runnable() {
        @Override
        public void run() {
            startTwoSplitKeepTime();
        }
    };
    protected void stopSpeakTwoSplitHandler(){
        speakTwoSplitHandler.removeCallbacks(speakTwoSplitRunnable);
    }

    protected Handler speakOneKeepHandler = new Handler();
    protected Runnable speakOneKeepRunnable = new Runnable() {
        @Override
        public void run() {
            startKeepTime();
        }
    };
    protected void stopSpeakOneKeepHandler(){
        speakOneKeepHandler.removeCallbacks(speakOneKeepRunnable);
    }

    protected Handler speakTwoMoveHandler = new Handler();
    protected Runnable speakTwoMoveRunnable = new Runnable() {
        @Override
        public void run() {
            approachRail.startMove();
        }
    };
    protected void stopSpeakTwoMoveHandler(){
        speakTwoMoveHandler.removeCallbacks(speakTwoMoveRunnable);
    }

    protected Handler keepTimeHandler = new Handler();
    protected Runnable keepTimeRunnable = new Runnable() {
        @Override
        public void run() {
            finishTrain();
        }
    };
    protected void startKeepTime(){
        keepTimeHandler.postDelayed(keepTimeRunnable, ApproachConfig.KeepTime);
    }
    private void stopKeepTime(){
        keepTimeHandler.removeCallbacks(keepTimeRunnable);
    }

    protected Handler twoSplitKeepTimeHandler = new Handler();
    protected Runnable twoSplitKeepTimeRunnable = new Runnable() {
        @Override
        public void run() {
            twoSplitOverTime();
        }
    };
    protected void startTwoSplitKeepTime(){
        twoSplitKeepTimeHandler.postDelayed(twoSplitKeepTimeRunnable, ApproachConfig.KeepTime);
    }
    private void stopTwoSplitKeepTime(){
        twoSplitKeepTimeHandler.removeCallbacks(twoSplitKeepTimeRunnable);
    }
    private void twoSplitOverTime(){
        status = EnumApproachStatus.TWO_MOVE;
        speakTip(status);
    }

    private void finishTrain(){
        ControlMessageSet messageSet = TrainUseCase.createBaffleStop();
        //MotorBus.getInstance().sendMessageSet(messageSet);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .flatMap(result -> {
                    ControlMessageSet messageSetBaffleGet = TrainUseCase.createBaffleGet();
                    return MotorBus.getInstance().sendMessageSet(messageSetBaffleGet);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(backmessageSet -> {
                    getLocationBack(backmessageSet);
                });

//        ControlMessageSet messageSetBaffleGet = TrainUseCase.createBaffleGet();
//        messageSetBaffleGet.setHandler(handlerGetLocationBack);
//        MotorBus.getInstance().sendMessageSet(messageSetBaffleGet);
    }

    @Override
    public void onLocationChange(double location) {
        //criticalLocation = location;
        tv_location.setText(String.valueOf(location));
    }

    public double getCriticalLocation() {
        return criticalLocation;
    }

    /**
     * 恢复到初始状态
     */
    private void resetInitStatus() {
        if(ttsEngine==null){
            ttsEngine = new TtsEngine(getContext());
        }
        ttsEngine.stopSpeaking();
        stopSpeakStartHandler();
        stopSpeakTwoSplitHandler();
        stopSpeakOneKeepHandler();
        stopSpeakTwoMoveHandler();
        stopKeepTime();
        status = EnumApproachStatus.INIT;
        isStartMove = false;
        //approachRail.stopMove();
    }

    /**
     * 结束以后初始化显示的东西
     */
    public void initViewAfterFinish() {
        resetInitStatus();
    }

    public void getLocationBack(ControlMessageSet messageSet){
        boolean getSuccess = false;
        if(messageSet.getList().size()>0){
            ControlMessage controlMessage = messageSet.getList().get(0);
            BackMessage backMessage = controlMessage.getBackMessage();
            if(controlMessage.getControlType()== ControlType.GET && backMessage!=null){
                int motorDistance = backMessage.getValue();
                Timber.d("get back motorDistance:%s",motorDistance);
                criticalLocation = DistanceHandler.computeBaffleRealDistanceInApproach(motorDistance);
                getSuccess = true;
            }
        }
        if(getSuccess){
            status = EnumApproachStatus.END;
            speakTip(status);
            endGoOnMove();
        }else{
            //如果获取失败，重新获取一遍
            finishTrain();
        }
    }

    protected Handler handlerGetLocationBack = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            getLocationBack(ControlMessageSet.convert(msg));
        }
    };

    /**
     * 结束后继续移动
     */
    public void endGoOnMove(){
        //approachRail.startMove();

        //直接移动到350停止之后结束
        int baffleLocation = DistanceHandler.computeBaffleVirtualDistanceInApproach(ApproachConfig.MaxDistance);

        ControlMessageSet messageSet = new ControlMessageSet();
        MotorBus motorBus = MotorBus.getInstance();
        BaffleMotorController baffleMotCtrl = motorBus.baffleMotCtrl;
        ControlMessage baffleMessage = baffleMotCtrl.setLocation(baffleLocation);
        messageSet.addMessage(baffleMessage);

        PublishSubject<ControlMessageSet> publishSubject = MotorBus.getInstance().sendMessageSet(messageSet);
        publishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    overMaxStop();
                });
    }


    /**
     * 原点停住
     */
    public void zeroStop(){
        approachRail.stopMove();
        approachRail.changeBackDirection();

        status = EnumApproachStatus.ONE_KEEP;
        speakTip(status);
    }

    /**
     * 超过最远距离停住
     */
    public void overMaxStop(){
        if(status == EnumApproachStatus.TWO_MOVE){
            //最远端停止
            approachRail.stopMove();

            //两个移动的到头，需要直接结束，默认就是最远距离
            criticalLocation = ApproachConfig.MaxDistance;
            resultListener.onFinishResult(EnumResultType.SUCCESS);
        }else if(status==EnumApproachStatus.END){
            //前面取完数据了，直接结束
            resultListener.onFinishResult(EnumResultType.SUCCESS);
        }
    }
}
