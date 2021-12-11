package com.uyu.device.devicetraining.presentation.localvoice;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.uyu.device.devicetraining.presentation.AndroidApplication;

import rx.subjects.PublishSubject;

/**
 * Created by windern on 2015/12/11.
 */
public class TtsEngine {

    private static final String TAG = "本地语音提示" ;

    //上下文
    private Context context;

    private SpeechService speechService;

    public TtsEngine(Context context) {
        this.context = context;
    }

    /**
     * 播报提示
     * @param voiceResid
     */
    public void startSpeaking(int voiceResid){
        if(speechService==null){
            speechService = ((AndroidApplication)context.getApplicationContext()).getSpeechService();
        }
        stopSpeaking();
        speechService.startSpeech(voiceResid);
    }

    /**
     * 播报提示
     * @param voiceResid
     */
    public PublishSubject<Boolean> startSpeakingSubject(int voiceResid){
        if(speechService==null){
            speechService = ((AndroidApplication)context.getApplicationContext()).getSpeechService();
        }
        stopSpeaking();
        return speechService.startSpeechSubject(voiceResid);
    }

    /**
     * 停止说话
     */
    public void stopSpeaking(){
        if(speechService!=null) {
            speechService.stopSpeech();
        }
    }

    public Context getContext() {
        return context;
    }

    public void release() {
        context = null;
        if(speechService != null){
            speechService.stopSpeech();
        }
    }
}
