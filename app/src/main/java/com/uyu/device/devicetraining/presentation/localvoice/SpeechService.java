package com.uyu.device.devicetraining.presentation.localvoice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import rx.subjects.PublishSubject;

/**
 * Created by windern on 2016/1/12.
 */
public class SpeechService extends Service{
    public static final String ACTION = "com.uyu.device.devicetraining.presentation.voice.SpeechService";

    public MediaPlayer mPlayer;
    private Handler handler;

    public static class SpeechServiceBinder extends Binder {

        private SpeechService mService = null;

        public SpeechServiceBinder(SpeechService service) {
            mService = service;
        }

        public SpeechService getService () {
            return mService;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SpeechServiceBinder(this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void startSpeech(int voiceResId){
        mPlayer = MediaPlayer.create(getApplicationContext(), voiceResId);
        if(mPlayer!=null) {
            mPlayer.start();
        }
    }

    public PublishSubject<Boolean> startSpeechSubject(int voiceResId){
        final PublishSubject<Boolean> subject = PublishSubject.create();
        mPlayer = MediaPlayer.create(getApplicationContext(), voiceResId);
        if(mPlayer!=null) {
            mPlayer.start();
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                subject.onNext(true);
            }
        });
        return subject;
    }

    public void stopSpeech(){
        if(mPlayer!=null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        handler = null;
    }
}
