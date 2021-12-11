package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.RawProvider;
import com.uyu.device.devicetraining.presentation.localvoice.TtsEngine;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jwc on 2016/7/6.
 */
public class ShowResultView extends LinearLayout {

    Context mContext;

    TtsEngine mTtsEngine;

    public ShowResultView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public ShowResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public ShowResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_show_result,this,true);
        ButterKnife.bind(view);

        if(mTtsEngine == null) {
            mTtsEngine = new TtsEngine(mContext);
        }
    }
    public void voicePrompt(){
        /**
         * 开始语音播报
         */
        if(mTtsEngine != null){
            mTtsEngine.startSpeaking(RawProvider.show_result);
        }
    }

    @OnClick({R.id.bt_ok})
    public void onClick(View view){
        exit();
    }

    public void exit(){
        /**
         * 停止语音播报
         */
        if(mTtsEngine != null){
            mTtsEngine.stopSpeaking();
            mTtsEngine.release();
        }
        ((WelcomeActivity)mContext).finishAllTrain();
        ((WelcomeActivity)mContext).banner_bar_view.hiddenTime();
    }
}
