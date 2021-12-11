package com.uyu.device.devicetraining.presentation.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.IntroductionPresenter;
import com.uyu.device.devicetraining.presentation.presenter.ShowCodePresenter;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windern on 2015/11/30.
 */
public class IntroductionFragment extends BaseFragment implements IntroductionPresenter.IntroductionPresenterListener {
    @Inject
    IntroductionPresenter introductionPresenter;

    @Bind(R.id.iv_volume)
    ImageView iv_volume;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_introduction, container, true);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        introductionPresenter.setItemListener(this);
        refreshStatusAndView();
    }

    /**
     * 刷新状态和界面
     */
    public void refreshStatusAndView(){
        boolean isSpeakOn = SharePreferenceTool.getSharePreferenceValueBoolean(getActivity(),SharePreferenceTool.PREF_INTRODUCTION_SPEAK_ON);
        introductionPresenter.setSpeakIntroStatus(isSpeakOn);
    }

    @OnClick(R.id.iv_volume)
    public void changeSpeakIntroStatus(){
        introductionPresenter.changeSpeakIntroStatus();
    }

    @Override
    public void showSpeakIntroStatus(boolean isOnSpeakIntroStatus) {
        if(isOnSpeakIntroStatus){
            iv_volume.setImageResource(R.drawable.volume_open);
        }else{
            iv_volume.setImageResource(R.drawable.volume_close);
        }
    }

    @OnClick(R.id.ll)
    public void afterIntroductionStart(){
        ((WelcomeActivity)getActivity()).afterIntroductionStart();
    }

    public void changeDeviceStatusStopSpeak(){
        introductionPresenter.stopIntroduce();
    }
}
