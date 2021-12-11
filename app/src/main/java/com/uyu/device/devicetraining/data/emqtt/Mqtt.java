package com.uyu.device.devicetraining.data.emqtt;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.message.TrainEmqttMessage;
import com.uyu.device.devicetraining.presentation.util.Ulog;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Mqtt {

    private static final String TAG = "Mqtt";
    //Mqtt客户端配置
    private MqttConfiguration configuration;
    //单利
    private static Mqtt instance;
    //Mqtt客户端
    private MqttClient client;
    //接口回掉中不运行断开连接,使用handler通讯
    private Handler handler = null;

    //构造
    private Mqtt() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        try {
                            if (client.isConnected()) {
                                client.disconnect();
                                Log.d(TAG, "Handler Message--->disconnect" + client.isConnected());
                            }
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        };
    }

    public static Mqtt getInstance() {
        if (instance == null) {
            synchronized (Mqtt.class) {
                if (instance == null) {
                    instance = new Mqtt();

                }
            }
        }
        return instance;
    }

    /**
     * 注册消息推送
     */
    public synchronized void connectionMqtt(final MqttConfiguration configuration) {
        //配置参数信息
        this.configuration = configuration;
        this.configuration.printf();
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(configuration.broker, configuration.token, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(configuration.uid);
            options.setPassword(configuration.token.toCharArray());
            options.setSocketFactory(SslUtility.getInstance().getSocketFactory(R.raw.raw_key_file, "Iuyu365"));
            client.setTimeToWait(configuration.timeToWait);

            Ulog.d("LoginActivity", "-------->clientid: " + configuration.token + " ,username: " + configuration.uid + " ,pwd:" + new String(configuration.token.toCharArray()));

            // 连接
            client.connect(options);

            // 删除 连接成功后需要向主题”TOPIC_GLOBAL_CONNECTED” publish 上面内容
            //client.publish("TOPIC_GLOBAL_CONNECTED", new MqttMessage(createPublish(null).toString().getBytes()));

            // 订阅相关内容(产品相关) TOPIC_GLOBAL_PUSH
            client.subscribe(configuration.topicFilters);
            // 订阅自己的Token(排他) token((uuid+loginname+timestamp)md5加密)
            client.subscribe(configuration.token);

            client.setCallback(configuration.mqttListener);

            configuration.mqttListener.connectSuccess();
        } catch (MqttException me) {
            me.printStackTrace();
            configuration.mqttListener.connectFail(me.getMessage());
        }

    }

    /**
     * 断开mqtt连接
     */
    public synchronized void disconnectMqtt() {
        if (client != null) {
            try {
                //configuration.reset();
                //configuration = null;
                client.disconnect();
                Log.d(TAG, "断开长连接:" + client.isConnected());
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建服务器的推送消息
     *
     * @param firstLogin 登陆标志 1.登陆 2.切换到后台又回到主界面
     * @return
     */
    private String createPublish(@Nullable String firstLogin) {
        JSONObject object = new JSONObject();
        try {
            object.put("fl", firstLogin == null ? configuration.firstLogin : firstLogin);
            object.put("ln", configuration.loginName);
            object.put("ts", String.valueOf(System.currentTimeMillis() / 1000));
            object.put("dm", configuration.deviceName);
            object.put("token", configuration.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    /**
     * 向服务器推送一个注销的消息
     *
     * @param disconnectMqtt 断开长连接 true
     */
    public void publishLogout(boolean disconnectMqtt) {
        try {
            client.publish("TOPIC_GLOBAL_CONNECTED", new MqttMessage(createPublish(String.valueOf(2)).toString().getBytes()));
            if (disconnectMqtt) {
                disconnectMqtt();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(TrainEmqttMessage msg){
        Observable.just(msg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strmsg->{
                    try {
                        //确保消息仅被送达一次
                        client.publish("transmit:message:", msg.toJson().getBytes(), 2, false);
                        Log.d("mqtt send msg:", msg.toJson());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                });
    }
}