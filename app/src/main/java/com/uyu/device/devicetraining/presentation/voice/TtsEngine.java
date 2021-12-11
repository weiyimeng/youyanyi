package com.uyu.device.devicetraining.presentation.voice;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by windern on 2015/12/11.
 */
public class TtsEngine {

    private static final String TAG = "讯飞语音" ;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    //上下文
    private Context context;

    //初始化监听
    private InitListener mTtsInitListener;
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;

    SynthesizerListener mSyntListener;

    public TtsEngine(Context context) {
        this.context = context;
    }

    /**
     * 开始合成
     * @param string
     */
    public void startSpeaking(String string){
        stopSpeaking();
        mTts.startSpeaking(string,mSyntListener);
    }


    /**
     * 停止说话
     */
    public void stopSpeaking(){
        if(mTts != null){
            mTts.stopSpeaking();
        }
    }

    public void release() {
        context = null;
        if(mTts != null){
            mTts.stopSpeaking();
            mTts.destroy();
        }
    }

    public Context getContext() {
        return context;
    }

    public void setup(Context context) {
        this.context = context;

        Log.d(TAG, "装载配置");
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);

        mTtsInitListener = new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d(TAG, "InitListener init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    Log.e(TAG, "err code = " + code);
                } else {
                    // 初始化成功，之后可以调用startSpeaking方法
                    // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                    // 正确的做法是将onCreate中的startSpeaking调用移至这里
                    Log.i(TAG, "success code = " + code);
                }
            }
        };

        mSyntListener = new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                Log.d(TAG,"开始");
            }
            @Override
            public void onSpeakPaused() {
                Log.d(TAG,"暂停");
            }
            @Override
            public void onSpeakResumed() {
                Log.d(TAG,"继续");
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
                mPercentForBuffering = percent;
                Log.d(TAG,"缓冲: " + mPercentForBuffering );
            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                // 播放进度
                mPercentForPlaying = percent;
                Log.d(TAG,"播放: " + mPercentForPlaying );
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                //播放完成
                if(speechError != null){
                    Log.d(TAG,speechError.getPlainDescription(true));
                }

                Log.d(TAG,"播放完成");
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
                // 若使用本地能力，会话id为null
                //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                //		Log.d(TAG, "session id =" + sid);
                //	}
            }
        };
    }
}
