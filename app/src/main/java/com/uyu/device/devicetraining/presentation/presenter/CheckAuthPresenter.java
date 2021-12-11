package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.uyu.device.devicetraining.data.emqtt.Mqtt;
import com.uyu.device.devicetraining.data.emqtt.MqttConfiguration;
import com.uyu.device.devicetraining.data.emqtt.MqttListener;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessageType;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.DeviceInfoTool;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.LoginUseCase;
import com.uyu.device.devicetraining.domain.util.AccountManager;
import com.uyu.device.devicetraining.presentation.adapter.MqttAdatper;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.util.DateUtil;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2015/11/30.
 */
@PerActivity
public class CheckAuthPresenter implements MqttAdatper{
    public interface CheckAuthPresenterListener extends MqttAdatper{
        //mqtt掉线
        void mqttLost();
        //mqtt连接成功
        void mqttConnectSuccess();
        //mqtt连接失败
        void mqttConnectFail();
        //网络异常
        void networkException();
        //token校验失败
        void checkAuthFail();
        //去登录
        void go2Login();
    }

    private CheckAuthPresenterListener mListener;

    public void setmListener(CheckAuthPresenterListener mListener) {
        this.mListener = mListener;
    }

    private final LoginUseCase loginUseCase;
    private final Context context;

    private final static String TAG = "CheckAuthPresenter";

    private TrainEmqttMessage finishTrainEmqttMessage = null;

    @Inject
    public CheckAuthPresenter(@Named("loginUseCase") LoginUseCase loginUseCase, Context context) {
        this.loginUseCase = loginUseCase;
        this.context = context;
    }

    /**
     * 检查验证
     */
    public void checkAuth(){
        SharePreferenceTool.setEmqttStatus(context, false);

        Timber.d("checkAuth");
        //duid里面存的也是蓝牙，登录失败后，重新选择蓝牙
        String duid = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DUID);
        String token = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_TOKEN);
        if(duid.equals("") || token.equals("")){
            checkAuthFail();
        }else{
            loginUseCase.checkAuth(duid, token)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(apiResult -> {
                        if (apiResult.getCode() == 0) {
                            connectEmqtt();
                        } else {
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN, "");
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_DUID, "");
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_CONNECT_BLUTTOOTH_UID, "");

                            checkAuthFail();
                        }
                    },throwable -> {
                        networkException();
                    });
        }
    }

    /**
     * 登录
     */
    private void go2Login() {
        if(mListener!=null) {
            mListener.go2Login();
        }
    }

    /**
     * 登录失败
     */
    private void checkAuthFail(){
        if(mListener!=null) {
            mListener.checkAuthFail();
        }
    }

    /**
     * 成功了以后
     * 启动emqtt进行订阅
     */
    private void connectEmqtt(){
        String device_id = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_ID);
        String device_token = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_TOKEN);

        MqttListener mqttListener = createMqttListener();
        MqttConfiguration config = new MqttConfiguration.Builder(context,mqttListener)
                .setBroker(ServiceConfig.BROKER_URL_SSL)
                .setUid(device_id)
                .setToken(device_token)
                .setFirstLogin(0)
                .setLink("link")
                .setBrief("brief")
                .setTimestamp(System.currentTimeMillis() / 1000)
                .setDeviceName(Build.DEVICE)
                .setTopicFilters(MqttConfiguration.TOPIC_GLOBAL_PUSH)
                .build();
        Mqtt.getInstance().connectionMqtt(config);
    }

    private MqttListener mqttListener = new MqttListener() {
        @Override
        public void connectSuccess() {
            SharePreferenceTool.setEmqttStatus(context, true);

            if(finishTrainEmqttMessage !=null){
                Mqtt.getInstance().sendMsg(finishTrainEmqttMessage);
                finishTrainEmqttMessage = null;
            }

            mqttConnectSuccess();
        }

        @Override
        public void connectFail(String failMsg) {
            SharePreferenceTool.setEmqttStatus(context, false);

            mqttConnectFail();
        }


        //在断开连接时调用
        @Override
        public void connectionLost(Throwable arg0) {
            Timber.d("------->connectionLost:%s",System.currentTimeMillis());
            //检查断开原因

            mqttLost();
        }

        //接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Timber.d("------->deliveryComplete:token:%s",token);
        }

        //接收已经预订的发布
        @Override
        public void messageArrived(String topic, MqttMessage message) {
//                try {
            Timber.d("topic:%s",topic);
            //需要根据message内容
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String strate = DateUtil.format(date);
            Timber.d("------->messageArrived:%s:%s",strate,message.toString());

            EmqttMessage emqttMessage = EmqttMessage.convert(message.toString());
            receiveMessage(emqttMessage);
        }
    };

    /**
     * 创建emqtt的监听器
     * @return
     */
    public MqttListener createMqttListener(){
        return mqttListener;
    }

    protected void showMsg(String strmsg){
        Observable.just(strmsg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg->{
                    ToastUtil.showShortToast(context,msg);
                });
    }

    @Override
    public void receiveMessage(EmqttMessage emqttMessage) {
        Observable.just(emqttMessage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg->{
                    if(mListener!=null) {
                        mListener.receiveMessage(msg);
                    }
                });
    }

    @Override
    public void sendMessage(EmqttMessage emqttMessage) {
        boolean emqttStatus = SharePreferenceTool.getEmqttStatus(context);
        //不能直接给系统发消息
        if(emqttMessage instanceof TrainEmqttMessage) {
            if (!emqttStatus && ((TrainEmqttMessage)emqttMessage).getMsg().getTmt() == TrainMessageType.FINISH) {
                //如果emqtt没有连接上，同时这时结束了训练，需要缓存这条结束的指令
                finishTrainEmqttMessage = (TrainEmqttMessage)emqttMessage;
            } else {
                Mqtt.getInstance().sendMsg((TrainEmqttMessage)emqttMessage);
            }
        }
    }

    public void mqttLost() {
        if(mListener!=null){
            mListener.mqttLost();
        }
    }

    public void mqttConnectSuccess() {
        if(mListener!=null){
            mListener.mqttConnectSuccess();
        }
    }

    public void mqttConnectFail() {
        if(mListener!=null){
            mListener.mqttConnectFail();
        }
    }

    public void networkException(){
        if(mListener!=null){
            mListener.networkException();
        }
    }
}
