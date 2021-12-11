package com.uyu.device.devicetraining.presentation.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.presentation.adapter.MqttAdatper;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.CheckAuthPresenter;
import com.uyu.device.devicetraining.presentation.presenter.LoginPresenter;
import com.uyu.device.devicetraining.presentation.presenter.WelcomePresenter;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windern on 2016/5/17.
 */
public class CheckAuthFragment extends BaseFragment implements CheckAuthPresenter.CheckAuthPresenterListener{
    @Inject
    CheckAuthPresenter checkAuthPresenter;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_check_auth, container, true);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        checkAuthPresenter.setmListener(this);
    }

    public void showNetworkExceptionDialog(){
        Dialog alertDialog = new AlertDialog.Builder(getActivity()).
                setTitle((R.string.prompt)).
                setMessage(R.string.network_timeout).
                setIcon(R.mipmap.ic_launcher).
                setPositiveButton(R.string.reconnect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkAuth();
                    }
                }).
                create();
        alertDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        alertDialog.show();
    }

    public void checkAuth(){
        checkAuthPresenter.checkAuth();
    }

    @Override
    public void networkException() {
        ((WelcomeActivity)getActivity()).networkException();
    }

    @Override
    public void checkAuthFail() {
        ((WelcomeActivity)getActivity()).checkAuthFail();
    }

    @Override
    public void go2Login() {
        ((WelcomeActivity)getActivity()).go2Login();
    }

    @Override
    public void receiveMessage(EmqttMessage emqttMessage) {
        ((WelcomeActivity)getActivity()).receiveMessage(emqttMessage);
    }

    @Override
    public void sendMessage(EmqttMessage emqttMessage) {
        checkAuthPresenter.sendMessage(emqttMessage);
    }

    @Override
    public void mqttLost() {
        ((WelcomeActivity)getActivity()).mqttLost();
    }

    @Override
    public void mqttConnectSuccess() {
        ((WelcomeActivity)getActivity()).mqttConnectSuccess();
    }

    @Override
    public void mqttConnectFail() {
        ((WelcomeActivity)getActivity()).mqttConnectFail();
    }
}
