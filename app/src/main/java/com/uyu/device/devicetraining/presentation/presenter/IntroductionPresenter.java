package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.ContentUseCase;
import com.uyu.device.devicetraining.presentation.RawProvider;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2015/11/30.
 */
@PerActivity
public class IntroductionPresenter {
    private final Context context;
    protected final TtsEngine ttsEngine;

    @Inject
    public IntroductionPresenter(Context context
            , TtsEngine ttsEngine) {
        this.context = context;
        this.ttsEngine = ttsEngine;
    }

    public interface IntroductionPresenterListener{
        void showSpeakIntroStatus(boolean isOnSpeakIntroStatus);
    }

    private IntroductionPresenterListener presenterListener;

    public void setItemListener(IntroductionPresenterListener presenterListener) {
        this.presenterListener = presenterListener;
    }

    public boolean isOnSpeakIntroStatus = true;
    private final int IntervalTime = 30;

    public void setSpeakIntroStatus(boolean isOnSpeakIntroStatus){
        this.isOnSpeakIntroStatus = isOnSpeakIntroStatus;
        if(isOnSpeakIntroStatus){
            startIntroduce();
        }else{
            stopIntroduce();
        }
        if(presenterListener!=null){
            presenterListener.showSpeakIntroStatus(this.isOnSpeakIntroStatus);
        }
    }

    /**
     * 切换说话状态
     */
    public void changeSpeakIntroStatus() {
        isOnSpeakIntroStatus = !isOnSpeakIntroStatus;
        SharePreferenceTool.setSharePreferenceValueBoolean(context,SharePreferenceTool.PREF_INTRODUCTION_SPEAK_ON,isOnSpeakIntroStatus);
        setSpeakIntroStatus(isOnSpeakIntroStatus);
    }

    public void startIntroduce(){
        stopTimmer();
        startTimer();
    }

    public void stopIntroduce(){
        stopTimmer();
        ttsEngine.stopSpeaking();
    }

    private Observable<Long> observable = null;
    private Subscriber<Long> subscriber = null;
    private Subscription subscription = null;

    private void startTimer(){
        Timber.d("startTimer");
        observable = Observable.interval(0,IntervalTime, TimeUnit.SECONDS);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                introduce();
            }
        };

        subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    private void stopTimmer(){
        Timber.d("stopTimmer");
        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        if(subscriber!=null && !subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }
        subscription = null;
        subscriber = null;
        observable = null;
    }

    private void introduce(){
        if(isOnSpeakIntroStatus) {
            ttsEngine.stopSpeaking();
            ttsEngine.startSpeaking(RawProvider.intro_content);
        }
    }
}
