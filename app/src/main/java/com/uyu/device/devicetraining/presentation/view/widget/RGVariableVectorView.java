package com.uyu.device.devicetraining.presentation.view.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.config.RGVariableVectorConfig;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.presentation.adapter.OnFinishResultListener;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.type.EnumApproachStatus;
import com.uyu.device.devicetraining.presentation.type.EnumFusionTimeType;
import com.uyu.device.devicetraining.presentation.type.EnumRGVariableVectorStatus;
import com.uyu.device.devicetraining.presentation.type.EnumResultType;
import com.uyu.device.devicetraining.presentation.util.Util;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 2015/12/19.
 */
public class RGVariableVectorView extends RelativeLayout{
    private Context context;
    private View view;

    /**
     * 融合训练类型-集合散开
     */
    private EnumFusionTrain fusionTrain = EnumFusionTrain.BO;

    @Bind(R.id.iv_pic_left)
    ImageView iv_pic_left;

    @Bind(R.id.iv_pic_right)
    ImageView iv_pic_right;

    /**
     * 返回结果监听器
     */
    protected OnFinishResultListener resultListener;
    /**
     * 播放语音引擎
     */
    protected TtsEngine ttsEngine;

    /**
     * 失败次数
     */
    protected int failTime = 0;
    /**
     * 移动的阶段，两种速度
     */
    private int sectionIndex = 0;
    /**
     * 移动距离的素组
     */
    private float[] arrayMoveDistance;
    /**
     * 移动的像素
     */
    private float movePx = 0;

    /**
     * 结束时移动的距离
     */
    private float endMoveDistance = 0;
    /**
     * 动画
     */
    private ObjectAnimator anim = null;
    /**
     * 动画总时间
     */
    private long animTotalTime = 0;

    /**
     * 状态
     */
    private EnumRGVariableVectorStatus status = EnumRGVariableVectorStatus.START;

    /**
     * 移动失败次数
     */
    private int moveFailTime = 0;

    private RGVariableConfiguration configuration;

    public RGVariableVectorView(Context context) {
        super(context);
        this.context = context;

        initLayout();
    }

    public RGVariableVectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initLayout();
    }

    public RGVariableVectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initLayout();
    }

    /**
     * 初始化布局
     */
    public void initLayout(){
        view = LayoutInflater.from(context).inflate(R.layout.layout_rgvv_view, this,
                true);
        ButterKnife.bind(this, view);
    }

    public void setFusionTrain(EnumFusionTrain fusionTrain){
        this.fusionTrain = fusionTrain;
        if(this.fusionTrain==EnumFusionTrain.BO){
            iv_pic_left.setImageResource(R.drawable.rgvv_green);
            iv_pic_left.setAlpha(RGVariableVectorConfig.greenAphla);

            iv_pic_right.setImageResource(R.drawable.rgvv_red);
            iv_pic_right.setAlpha(RGVariableVectorConfig.redAphla);
        }else{
            iv_pic_left.setImageResource(R.drawable.rgvv_red);
            iv_pic_left.setAlpha(RGVariableVectorConfig.redAphla);

            iv_pic_right.setImageResource(R.drawable.rgvv_green);
            iv_pic_right.setAlpha(RGVariableVectorConfig.greenAphla);
        }
    }


    public void setResultListener(OnFinishResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public float getMovePx() {
        return movePx;
    }

    public void setMovePx(float movePx) {
        this.movePx = movePx;
    }

    /**
     * 获取最好成绩
     * @return
     */
    public float getMaxMoveDistance() {
        float maxMoveDistance = 0;
        for(int i=0;i<arrayMoveDistance.length;i++){
            if(arrayMoveDistance[i]>maxMoveDistance){
                maxMoveDistance = arrayMoveDistance[i];
            }
        }
        return maxMoveDistance;
    }

    /**
     * 恢复到初始状态
     */
    private void resetInitStatus(){
        if(ttsEngine==null){
            ttsEngine = new TtsEngine(getContext());
        }
        stopMove();
        cancelListenTwoSplit();
        cancelListenExtraKeep();

        status = EnumRGVariableVectorStatus.START;
        sectionIndex=0;
        movePx = 0;
        moveFailTime = 0;
        iv_pic_right.setTranslationX(0);
        iv_pic_left.setTranslationX(0);
    }

    /**
     * 开始此关
     */
    public void startTrain(){
        failTime = 0;
        arrayMoveDistance = new float[RGVariableVectorConfig.Fail_Max_Time];
        restartSubTrain();
    }

    /**
     * 重新开始
     */
    private void restartSubTrain(){
        resetInitStatus();
        speakTip(EnumRGVariableVectorStatus.START);
    }

    /**
     * 按键
     */
    public void confirm(){
        ViewToast.getInstance(configuration.extraEndWaitSecond-2).removeTime();
        if(status==EnumRGVariableVectorStatus.START){
            startMove();

            status = EnumRGVariableVectorStatus.ONE_MOVE;
            speakTip(status);
        }else if(status==EnumRGVariableVectorStatus.ONE_MOVE){
            pauseMove();

            status = EnumRGVariableVectorStatus.TWO_KEEP;
            speakTip(status);
            startListenTwoSplit();

            moveFailTime++;
            if(moveFailTime>=RGVariableVectorConfig.Move_Fail_Max_Time){
                finishResult(EnumResultType.FAIL);
            }
        }else if(status==EnumRGVariableVectorStatus.TWO_KEEP){
            cancelListenTwoSplit();
            resumeMove();

            status = EnumRGVariableVectorStatus.ONE_MOVE;
            speakTip(status);
        }
    }

    /**
     * 每个状态-播放语音
     * @param nowStatus
     */
    private void speakTip(EnumRGVariableVectorStatus nowStatus){
        ttsEngine.stopSpeaking();
        ttsEngine.startSpeakingSubject(nowStatus.getTipResid());
    }

    /**
     * 结束
     * @param resultType
     */
    protected void finishResult(EnumResultType resultType){
        if(resultType==EnumResultType.SUCCESS){
            float movemm = Util.convertmm(movePx,context);
            //实际间距乘2
            arrayMoveDistance[failTime] = movemm*2;
            resultListener.onFinishResult(EnumResultType.SUCCESS);
        }else if(resultType==EnumResultType.FAIL){
            float movemm = Util.convertmm(movePx,context);
            //实际间距乘2
            arrayMoveDistance[failTime] = movemm*2;
            Timber.d("movePx:%s", movePx);
            Timber.d("movemm:%s",movemm);

            failTime++;
            if(failTime == RGVariableVectorConfig.Fail_Max_Time){
                resultListener.onFinishResult(EnumResultType.FAIL);
            }else{
                restartSubTrain();
            }
        }
    }

    /**
     * 初始化动画参数
     */
    public void initMoveAnim(){
        animTotalTime = RGVariableVectorConfig.arraySectionSpeedTime[sectionIndex]
                *RGVariableVectorConfig.arraySection[sectionIndex];
        mAnimationTime = 0;
        Timber.d("totalTime:%s", animTotalTime);

        //移动1mm
        float nextMoveDistance = Util.convertpx(RGVariableVectorConfig.arraySection[sectionIndex], context);
        endMoveDistance = movePx+nextMoveDistance;
        Timber.d("nextMoveDistance:" + nextMoveDistance);

        anim = ObjectAnimator.ofFloat(this,"movePx",endMoveDistance);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(animTotalTime);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float nowMovePx = (Float) animation.getAnimatedValue();
                //Timber.d("anim:nowMovePx:%s", nowMovePx);

                iv_pic_left.setTranslationX(-nowMovePx);
                iv_pic_right.setTranslationX(nowMovePx);
            }
        });
    }

    /**
     * 动画自己结束
     */
    private void animEnd(){
        movePx = endMoveDistance;
        finishSection();
    }

    /**
     * 结束移动的某一阶段
     */
    private void finishSection(){
        sectionIndex++;
        if(sectionIndex>=RGVariableVectorConfig.arraySection.length){
            //开始监听额外的时间
            startListenExtraKeep();;
        }else{
            startMove();
        }
    }

    /**
     * 结束以后初始化显示的东西
     */
    public void initViewAfterFinish() {
        resetInitStatus();
    }

    //监听动画自动结束

    private Observable<Long> observableAnimAutoEnd = null;
    private Subscriber<Long> subscriberAnimAutoEnd = null;
    private Subscription subscriptionAnimAutoEnd = null;

    /**
     * 开始监听动画自动结束
     */
    public void startListenAnimAutoEnd(long animEndTime){
        observableAnimAutoEnd = Observable.timer(animEndTime, TimeUnit.MILLISECONDS);
        subscriberAnimAutoEnd = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                animEnd();
            }
        };

        subscriptionAnimAutoEnd = observableAnimAutoEnd.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberAnimAutoEnd);
    }

    /**
     * 取消监听动画自动结束
     */
    public void cancelListenAnimAutoEnd(){
        if(subscriptionAnimAutoEnd!=null && !subscriptionAnimAutoEnd.isUnsubscribed()){
            subscriptionAnimAutoEnd.unsubscribe();
        }
        if(subscriberAnimAutoEnd!=null && !subscriberAnimAutoEnd.isUnsubscribed()){
            subscriberAnimAutoEnd.unsubscribe();
        }
        subscriptionAnimAutoEnd = null;
        subscriberAnimAutoEnd = null;
        observableAnimAutoEnd = null;
    }

    //额外保持的监听

    private Observable<Long> observableExtra = null;
    private Subscriber<Long> subscriberExtra = null;
    private Subscription subscriptionExtra = null;

    /**
     * 开始额外保持时间的监听
     */
    public void startListenExtraKeep(){
        ViewToast.getInstance(configuration.extraEndWaitSecond-2).showTime(context, Gravity.TOP,0,120);
        observableExtra = Observable.timer(configuration.extraEndWaitSecond, TimeUnit.SECONDS);
        subscriberExtra = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                finishResult(EnumResultType.SUCCESS);
            }
        };

        subscriptionExtra = observableExtra.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberExtra);
    }

    /**
     * 取消额外保持时间的监听
     */
    public void cancelListenExtraKeep(){
        if(subscriptionExtra!=null && !subscriptionExtra.isUnsubscribed()){
            subscriptionExtra.unsubscribe();
        }
        if(subscriberExtra!=null && !subscriberExtra.isUnsubscribed()){
            subscriberExtra.unsubscribe();
        }
        subscriptionExtra = null;
        subscriberExtra = null;
        observableExtra = null;
    }

    //两个分开的监听-最大时间后失败结束

    private Observable<Long> observableTwoSplit = null;
    private Subscriber<Long> subscriberTwoSplit = null;
    private Subscription subscriptionTwoSplit = null;

    /**
     * 开始额外保持时间的监听
     */
    public void startListenTwoSplit(){
        observableTwoSplit = Observable.timer(configuration.twoSplitMaxSecond, TimeUnit.SECONDS);
        subscriberTwoSplit = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                finishResult(EnumResultType.FAIL);
            }
        };

        subscriptionTwoSplit = observableTwoSplit.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberTwoSplit);
    }

    /**
     * 取消额外保持时间的监听
     */
    public void cancelListenTwoSplit(){
        if(subscriptionTwoSplit!=null && !subscriptionTwoSplit.isUnsubscribed()){
            subscriptionTwoSplit.unsubscribe();
        }
        if(subscriberTwoSplit!=null && !subscriberTwoSplit.isUnsubscribed()){
            subscriberTwoSplit.unsubscribe();
        }
        subscriptionTwoSplit = null;
        subscriberTwoSplit = null;
        observableTwoSplit = null;
    }

    //动画的部分

    //实现动画可以暂停的方法，pause只能在api19以上使用
    private long mAnimationTime=0;

    private void stopAnimation() {
        if(anim != null) {
            mAnimationTime = anim.getCurrentPlayTime();
            anim.cancel();
        }
    }

    private void playAnimation() {
        if (anim != null) {
            anim.start();
            anim.setCurrentPlayTime(mAnimationTime);
            startListenAnimAutoEnd(animTotalTime-mAnimationTime);
        }
    }

    /**
     * 开始移动
     */
    public void startMove(){
        initMoveAnim();
        anim.start();
        startListenAnimAutoEnd(animTotalTime-mAnimationTime);
    }

    /**
     * 暂停移动
     */
    public void pauseMove(){
        stopAnimation();
        cancelListenAnimAutoEnd();
    }

    /**
     * 恢复移动
     */
    public void resumeMove(){
        playAnimation();
    }

    /**
     * 结束移动
     */
    public void stopMove(){
        cancelListenAnimAutoEnd();
        if(anim!=null) {
            anim.cancel();
            anim.removeAllListeners();
            anim.removeAllUpdateListeners();
            anim = null;
        }
    }

    /**
     * 暂停训练的消息
     */
    public void pauseTrain() {
        resetInitStatus();
    }

    /**
     * 恢复训练的消息
     */
    public void resumeTrain() {
        startTrain();
    }

    public void setConfiguration(RGVariableConfiguration configuration) {
        this.configuration = configuration;
    }
}
