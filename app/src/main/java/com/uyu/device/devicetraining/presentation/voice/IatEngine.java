package com.uyu.device.devicetraining.presentation.voice;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.uyu.device.devicetraining.presentation.util.BaseModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 语音识别
 */
public class IatEngine extends BaseModule {

    protected Context context;

    private static String TAG = "bzy";

    // 语音听写对象
    private SpeechRecognizer mIat;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    public IatEngine(Context context) {
        super(context);
        setup(context);
    }

    private ResultListener mResultListener;

    public void setListener(ResultListener listener){
        this.mResultListener = listener;
    }

    //初始化语音听写对象的回调
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, code == ErrorCode.SUCCESS ? "initListener Success: " + code : " InitListener Fail: " + code);
        }
    };


    @Override
    public void setup(Context context) {
        super.setup(context);
        // 初始化识别无UI识别对象 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
    }

    @Override
    public void release() {
        super.release();
        if(mIat != null){
            mIat.stopListening();
            mIat.cancel();
            mIat.destroy();
        }
    }

    /**
     * 开始语音听写
     */
    public void startRecognize(){
        mIatResults.clear();
        setParam();
        int code = mIat.startListening(mRecognizerListener);
        Log.d(TAG, code == ErrorCode.SUCCESS ? "startListening success code: " + code : "startListener err code: " + code);

    }

    /**
     *  停止听写
     */
    public void stop(){
        mIat.stopListening();
        Log.d(TAG, "stop 停止听写");
    }

    /**
     * 清空历史数据
     */
    public void cleanResult(){
        mIatResults.clear();
    }

    /**
     * 取消听写
     */
    public void cancel(){
        mIat.cancel();
        Log.d(TAG, "cancel 取消听写");
    }


    /**
     * 听写监听
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            Log.d(TAG, "onError :录音设备准备完毕,可以说话了");
        }

        @Override
        public void onError(SpeechError error) {
            //tips 10118(您没有说话)
            final int code = error.getErrorCode();
            if(code == 10118){
                Log.d(TAG, "onError: 设备准备ok 用户未说话:10118");
                startRecognize(); //不能让语音识别停止
            }
            Log.d(TAG, "onError: " + error.getPlainDescription(true));
            mResultListener.onResult(false, "error");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech 检测到尾端点,不再接受语音输入！");
            startRecognize(); //不能让语音识别停止
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, "onResult "+results.getResultString()+" isLast: "+isLast);
            parserResult(results);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //nothing
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//             以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//             若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d(TAG, "session id =" + sid);
            }
        }
    };

    private void parserResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = "";
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        String result = resultBuffer.toString();
        Log.d(TAG,result);
        if(mResultListener != null)
            mResultListener.onResult(true,resultBuffer.toString());
    }


    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "jsonaa");

        String lag = "mandarin";//mSharedPreferences.getString("iat_language_preference", "mandarin");

        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }


        //前端点超时 开始录入音频后，音频前面部份最长静音时长。
        mIat.setParameter(SpeechConstant.VAD_BOS, String.valueOf(1000*60*60) /*String.valueOf(Integer.MAX_VALUE)*//*mSharedPreferences.getString("iat_vadbos_preference", "4000")*/);

        //后端点超时 是否必须设置：否 开始录入音频后，音频后面部份最长静音时长。
        mIat.setParameter(SpeechConstant.VAD_EOS, String.valueOf(1000*60*60) /*String.valueOf(Integer.MAX_VALUE)*//*mSharedPreferences.getString("iat_vadeos_preference", "1000")*/);

        //通过此参数，设置听写文本结果是否含标点符号。0表示不带标点，1则表示带标点。
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA, "0"/*mSharedPreferences.getString("iat_dwa_preference", "0")*/);
    }


    public interface ResultListener{
        //识别成功 //返回值
        void onResult(boolean isSuccess,String retvalue);
    }
}
