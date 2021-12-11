package com.uyu.device.devicetraining.presentation.voice;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import timber.log.Timber;

/**
 * Created by windern on 2015/12/21.
 */
public class AsrEngine {
    private Context context;

    private String grammarId;
    private SpeechRecognizer mAsr;

    public AsrEngine(Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setup(){
        Timber.d("setup");
        // 在线命令词识别， 启用终端级语法
        // ABNF语法示例
        String mCloudGrammar ="#ABNF 1.0 UTF-8;\nlanguage zh-CN;\nmode voice;\nroot $main;\n$main = $order;\n$order = 停止|继续|开始|第一条|第二条|第三条|第四条|第五条|第六条|第七条|第八条|第九条|第十条|第十一条|第12条|一样|不一样|都清楚|都不清楚|左边|右边|变大|变小;";
        // 1.创建SpeechRecognizer对象
        mAsr = SpeechRecognizer.createRecognizer(context, null);
        // 2.构建语法文件
        mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        int ret = mAsr.buildGrammar("abnf", mCloudGrammar, mGrammarListener);
        if (ret != ErrorCode.SUCCESS){
            Timber.d("语法构建失败,错误码：%s",ret);
        }else{
            Timber.d("语法构建成功");
        }
    }

    public void startListening(){
        Timber.d("startListening");
        int ret = mAsr.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            Timber.d("识别失败,错误码: %s",ret);
        }
    }

    public void stopListening(){
        mAsr.stopListening();
    }

    public void release(){
        context = null;
        if(mAsr != null){
            mAsr.stopListening();
            mAsr.destroy();
        }
    }

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        // 音量变化
        public void onVolumeChanged(int volume, byte[] data) {}
        // 返回结果
        public void onResult(final RecognizerResult result, boolean isLast) {
            Timber.d("RecognizerResult:%s",result.getResultString());

            startListening();
        }
        // 开始说话
        public void onEndOfSpeech() {
            Timber.d("onEndOfSpeech 检测到尾端点,不再接受语音输入！");
        }
        // 结束说话
        public void onBeginOfSpeech() {
            Timber.d("onBeginOfSpeech 开始检测");
        }
        // 错误回调
        public void onError(SpeechError error) {
            Timber.d("onError:error:%s",error);
            //tips 10118(您没有说话)
            final int code = error.getErrorCode();
            if(code == 10118){
                Timber.d("onError: 设备准备ok 用户未说话:10118");
                startListening(); //不能让语音识别停止
            }
        }
        // 事件回调
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}
    };

    //构建语法监听器
    private GrammarListener mGrammarListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String grammarId, SpeechError error) {
            if(error == null){
                Timber.d("onBuildFinish:grammarId:%s",grammarId);
                if(!TextUtils.isEmpty(grammarId)){
                    //构建语法成功，请保存grammarId用于识别
                    setGrammarId(grammarId);
                }else{
                    Timber.d("语法构建失败,错误码：%s",error.getErrorCode());
                }
            }
        }
    };

    public String getGrammarId() {
        return grammarId;
    }

    public void setGrammarId(String grammarId) {
        this.grammarId = grammarId;

        // 3.设置参数
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, this.grammarId);
    }
}
