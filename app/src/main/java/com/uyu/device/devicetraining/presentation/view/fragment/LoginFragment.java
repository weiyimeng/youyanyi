package com.uyu.device.devicetraining.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.presentation.internal.di.components.UserComponent;
import com.uyu.device.devicetraining.presentation.presenter.LoginPresenter;
import com.uyu.device.devicetraining.presentation.presenter.ShowCodePresenter;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windern on 2016/5/17.
 */
public class LoginFragment extends BaseFragment implements LoginPresenter.LoginPresenterListener{
    @Inject
    LoginPresenter loginPresenter;

    @Bind(R.id.et_device_user_name)
    EditText et_device_user_name;

    @Bind(R.id.et_device_password)
    EditText et_device_password;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_login, container, true);
        ButterKnife.bind(this, fragmentView);

        refreshDeviceUserName();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    protected void initialize() {
        this.getComponent(UserComponent.class).inject(this);
        loginPresenter.setPresenterListener(this);
    }

    public void refreshDeviceUserName(){
        String duid = SharePreferenceTool.getSharePreferenceValue(getActivity(),SharePreferenceTool.PREF_DUID);
        et_device_user_name.setText(duid);
    }

    @OnClick(R.id.btn_login)
    public void login(){
        String deviceUserName = et_device_user_name.getText().toString();
        String devicePassword = et_device_password.getText().toString();
        if(deviceUserName.equals("") || devicePassword.equals("")){
            ToastUtil.showShortToast(getActivity(),"账户和密码不能为空");
        }else{
            loginPresenter.login(deviceUserName,devicePassword);
        }
    }

    @Override
    public void loginSuccess() {
        ((WelcomeActivity)getActivity()).loginSuccess();
    }

    public void loginDirect(String duid){
        loginPresenter.login(duid,duid);
    }
}
