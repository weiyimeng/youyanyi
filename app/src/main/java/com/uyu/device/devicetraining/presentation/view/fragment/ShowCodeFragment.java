package com.uyu.device.devicetraining.presentation.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.motor.ControlMessageSet;
import com.uyu.device.devicetraining.domain.motor.MotorBus;
import com.uyu.device.devicetraining.domain.motor.TrainUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.ShowCodePresenter;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by windern on 2015/11/30.
 */
public class ShowCodeFragment extends BaseFragment implements ShowCodePresenter.ShowCodeListener {
    @Inject
    ShowCodePresenter showCodePresenter;

    @Bind(R.id.view_qrcode)
    ImageView view_qrcode;

    @Bind(R.id.tv_msg)
    TextView tv_msg;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_show_code, container, true);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        showCodePresenter.setItemListener(this);
    }

    @Override
    public void setQrcode(Bitmap bitmap) {
        view_qrcode.setImageBitmap(bitmap);
    }

    public void getQrcode(){
        showCodePresenter.getQrcode();
        startTimer();
    }

    @Override
    public void hidenQRCode() {
        view_qrcode.setVisibility(View.GONE);
    }

    @Override
    public void showQRCode() {
        view_qrcode.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMsg(String msg) {
        tv_msg.setText(msg);
        tv_msg.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidenMsg() {
        tv_msg.setVisibility(View.GONE);
    }

    /**
     * 在当前页面计时
     * 到时间会介绍页
     */
    public void startTimer(){
        showCodePresenter.startTimer();
    }

    @Override
    public void inNowPageTimeOut(){
        ((WelcomeActivity)getActivity()).inNowPageTimeOut();
    }

    /**
     * 开始刷新二维码
     */
    public void startChangeQrcode(){
        showCodePresenter.startChangeQrcode();
    }

    public void stopChangeQrcode(){
        showCodePresenter.stopChangeQrcode();
    }
}
