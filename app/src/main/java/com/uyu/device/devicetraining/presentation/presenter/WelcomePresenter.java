package com.uyu.device.devicetraining.presentation.presenter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.emqtt.Mqtt;
import com.uyu.device.devicetraining.data.emqtt.MqttConfiguration;
import com.uyu.device.devicetraining.data.emqtt.MqttListener;
import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
import com.uyu.device.devicetraining.data.entity.message.TrainMessageType;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.DeviceInfoTool;
import com.uyu.device.devicetraining.data.repository.property.PropertyTool;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.LoginUseCase;
import com.uyu.device.devicetraining.domain.util.AccountManager;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.util.DateUtil;
import com.uyu.device.devicetraining.presentation.adapter.MqttAdatper;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2015/11/30.
 */
@PerActivity
public class WelcomePresenter implements MqttAdatper{
    public interface WelcomePresenterListener{
        //连接并校验成功
        void onConnectNetworkSuccess();
        //网络异常
        void networkException();
        //去登录
        void go2Login();
    }

    private WelcomePresenterListener mListener;

    public void setmListener(WelcomePresenterListener mListener) {
        this.mListener = mListener;
    }

    private final LoginUseCase loginUseCase;
    private final Context context;

    private final static String TAG = "WelcomePresenter";

    private MqttAdatper mqttAdatper;
    private TrainEmqttMessage finishTrainEmqttMessage = null;

    @Inject
    public WelcomePresenter(@Named("loginUseCase") LoginUseCase loginUseCase,Context context) {
        this.loginUseCase = loginUseCase;
        this.context = context;
    }

    private final int IntervalTime = 10;
    private boolean isOnCheckAuth = false;

    public void startCheckAuth(){
        SharePreferenceTool.setEmqttStatus(context, false);

        stopTimmer();
        startTimer();
    }

    private Observable<Long> observable = null;
    private Subscriber<Long> subscriber = null;
    private Subscription subscription = null;

    private void startTimer(){
        Timber.d("startTimer");
        observable = Observable.interval(0,IntervalTime, TimeUnit.SECONDS);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                Timber.d("onNext:%s",aLong);
                boolean emqttStatus = SharePreferenceTool.getEmqttStatus(context);
                if(!emqttStatus){
                    Timber.d("isOnCheckAuth:%s",isOnCheckAuth);
                    if(!isOnCheckAuth) {
                        checkAuthBegin();
                        checkAuth();
                    }
                }else{
                    stopTimmer();
                }
            }
        };

        subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    private void stopTimmer(){
        Timber.d("stopTimmer");
        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        if(subscriber!=null && !subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }
        subscription = null;
        subscriber = null;
        observable = null;
    }

    public void setMqttAdatper(MqttAdatper mqttAdatper){
        this.mqttAdatper = mqttAdatper;
    }

    private void checkAuthBegin(){
        isOnCheckAuth = true;
    }

    private void checkAuthEnd(){
        isOnCheckAuth = false;
    }

    /**
     * 检查验证
     */
    private void checkAuth(){
        Timber.d("checkAuth");
        String token = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_TOKEN);
        if(token.equals("")){
            login();
        }else{
            String duid = DeviceInfoTool.getDuid(context);

            loginUseCase.checkAuth(duid, token)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(apiResult -> {
                        if (apiResult.getCode() == 0) {
                            Timber.d("checkAuth success");
                            showMsg(context.getString(R.string.token_verification_suc));
                            connectEmqtt();
                        } else {
                            //验证失败重新登录
                            Timber.d("token验证失败：%s", apiResult.getMessage());
                            showMsg(context.getString(R.string.token_verification_fail)+"code：" + apiResult.getMessage());
                            SharePreferenceTool.setSharePreferenceValue(context, SharePreferenceTool.PREF_DEVICE_TOKEN, "");
                            checkAuthFail();
                        }
                    },throwable -> {
                        Timber.d("checkAuth fail:%s", throwable.getMessage());
                        //error的时候说明网不好，不需要重新登录
                        showMsg(context.getString(R.string.token_verification_fail)+"error：" + throwable.getMessage());
                        checkAuthEnd();
                    });
        }
    }

    private int count = 1;

    /**
     * 登录
     */
    private void login() {
        try {
            String duid = DeviceInfoTool.getDuid(context);
            //SharePreferenceTool.setSharePreferenceValue(context,SharePreferenceTool.PREF_PRODUCT_UID,productUid);
            String encodePassword = AccountManager.encodePassword(duid);

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

                            connectEmqtt();
                        }else{
                            Timber.d("失败：获取token失败");
                            checkAuthEnd();
                            showMsg(context.getString(R.string.get_token_fail));
                        }
                    }, throwable -> {
                        checkAuthEnd();
                        Timber.d("getToken fail:%s", throwable.getMessage());
                        showMsg(context.getString(R.string.fail) + throwable.getMessage());
                    });
        }catch (Exception e){
            checkAuthEnd();
            showMsg(context.getString(R.string.fail) + e.getMessage());
        }
    }

    /**
     * 登录失败
     */
    private void checkAuthFail(){
        login();
    }

    /**
     * emqtt收到消息的处理handler
     */
    private Handler rmsghandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String msgString = bundle.getString("msg");

            EmqttMessage emqttMessage = EmqttMessage.convert(msgString);
            receiveMessage(emqttMessage);
        }
    };

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

    /**
     * 创建emqtt的监听器
     * @return
     */
    public MqttListener createMqttListener(){
        MqttListener mqttListener = new MqttListener() {
            @Override
            public void connectSuccess() {
                SharePreferenceTool.setEmqttStatus(context, true);
                checkAuthEnd();
                showMsg(context.getString(R.string.connection_success));

                if(finishTrainEmqttMessage !=null){
                    Mqtt.getInstance().sendMsg(finishTrainEmqttMessage);
                    finishTrainEmqttMessage = null;
                }

                if(mListener!=null){
                    mListener.onConnectNetworkSuccess();
                }
            }

            @Override
            public void connectFail(String failMsg) {
                showMsg(context.getString(R.string.connection_fail)+failMsg);
                Timber.d("fail msg:%s",failMsg);
                SharePreferenceTool.setEmqttStatus(context, false);
                checkAuthEnd();
            }


            //在断开连接时调用
            @Override
            public void connectionLost(Throwable arg0) {
                Timber.d("------->connectionLost:%s",System.currentTimeMillis());
                //检查断开原因

                //重新验证连接emqtt
                startCheckAuth();
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
                //Toast.makeText(context,"messageArrived："+message.toString(),Toast.LENGTH_LONG).show();

                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putString("msg", message.toString());
                msg.setData(b);
                rmsghandler.sendMessage(msg);
                //handler.sendMessage(msg);

//                } catch (Exception e) {
//                    Log.e(TAG, "----------------------->data is bad");
//                }
            }
        };
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
        mqttAdatper.receiveMessage(emqttMessage);
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
}
