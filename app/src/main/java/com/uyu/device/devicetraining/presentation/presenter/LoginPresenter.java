package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.DeviceEntity;
import com.uyu.device.devicetraining.data.repository.DeviceInfoTool;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.LoginUseCase;
import com.uyu.device.devicetraining.domain.util.AccountManager;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2016/5/17.
 */
public class LoginPresenter {
    public interface LoginPresenterListener{
        void loginSuccess();
    }

    private LoginPresenterListener presenterListener;

    public void setPresenterListener(LoginPresenterListener presenterListener) {
        this.presenterListener = presenterListener;
    }

    private final LoginUseCase loginUseCase;
    private final Context context;

    @Inject
    public LoginPresenter(@Named("loginUseCase") LoginUseCase loginUseCase, Context context) {
        this.loginUseCase = loginUseCase;
        this.context = context;
    }
    /**
     * 登录
     */
    public void login(String deviceUserName,String password) {
        try {
            String duid = deviceUserName;
            String encodePassword = AccountManager.encodePassword(password);

            SharePreferenceTool.setSharePreferenceValue(context,SharePreferenceTool.PREF_DUID,duid);

            // IO 线程，由 subscribeOn() 指定
            // subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的
            // observeOn() 控制的是它后面的线程
            loginUseCase.login(duid, encodePassword)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(deviceEntity -> {
                        if (deviceEntity.getId() != 0 && deviceEntity.getCode() != null && (!deviceEntity.getCode().equals(""))) {
                            SharePreferenceTool.setSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_ID,String.valueOf(deviceEntity.getId()));
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_MERCHANT_ID, String.valueOf(deviceEntity.getMerchant_id()));
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_NAME, String.valueOf(deviceEntity.getName()));
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_ALIAS, String.valueOf(deviceEntity.getAlias()));
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_PRODUCT_UID, String.valueOf(deviceEntity.getProduct_uid()));

                            return loginUseCase.getToken(duid, deviceEntity.getCode());
                        } else {
                            //直接返回一个执行error的observable
                            return Observable.error(new Exception("登录失败"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tokenEntity -> {
                        if(tokenEntity.getTk()!=null && (!tokenEntity.getTk().equals(""))) {
                            Timber.d("getToken success");

                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN, String.valueOf(tokenEntity.getTk()));

                            showMsg(context.getString(R.string.success) + tokenEntity.getTk());

                            if(presenterListener!=null){
                                presenterListener.loginSuccess();
                            }
                        }else{
                            Timber.d("失败：获取token失败");
                            showMsg(context.getString(R.string.get_token_fail));
                        }
                    }, throwable -> {
                        Timber.d("getToken fail:%s", throwable.getMessage());
                        showMsg(context.getString(R.string.fail)+"：" + throwable.getMessage());
                    });
        }catch (Exception e){
            showMsg(context.getString(R.string.fail)+"：" + e.getMessage());
        }
    }

    protected void showMsg(String strmsg){
        Observable.just(strmsg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg->{
                    ToastUtil.showShortToast(context,msg);
                });
    }
}
