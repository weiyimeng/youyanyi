package com.uyu.device.devicetraining.data.emqtt;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by banzhenyu on 2015.09.01.0001.
 * mqtt配置项
 */
public class MqttConfiguration {

    private static final String TAG = "MainActivity";

    public static String TOPIC_GLOBAL_PUSH =  "TOPIC_GLOBAL_PUSH";

    //上下文
    public Context context;
    //连接超时时间
    public long  timeToWait = 5000;
    //服务器地址
    public String broker;
    //用户名
    public String loginName;
    //登录成功后返回的ID
    public String uid;
    //用户密码
    public String  passWord;
    //第一登陆传1
    public int firstLogin;
    //图片地址
    public String link;
    //时间戳
    public String timestamp;
    //摘要信息
    public String brief;
    //唯一标识,一次生成以后都不变
    public String token;
    //设备名字
    public String deviceName;
    //订阅的内容
    public String [] topicFilters;
    //订阅的监听器
    public MqttListener mqttListener;

    private MqttConfiguration(final Builder builder) {
        Log.d(TAG,"MqttConfiguration");
        this.context = builder.context;
        this.timeToWait = builder.timeToWait;
        this.broker = builder.broker;
        this.loginName = builder.loginName;
        this.uid = builder.uid;
        this.passWord = builder.passWord;
        this.firstLogin = builder.firstLogin;
        this.link = builder.link;
        this.timestamp = builder.timestamp;
        this.brief = builder.brief;
        this.token = builder.token;
        this.deviceName = builder.deviceName;
        this.topicFilters = builder.topicFilters;
        this.mqttListener = builder.mqttListener;
    }

    public void reset() {
        //TOD
    }

    public static class Builder {

        private Context context;
        private long  timeToWait;
        private String broker;
        private String loginName;
        private String  passWord;
        private int firstLogin;
        private String link;
        private String timestamp;
        private String brief;
        private String token;
        public String deviceName;
        private String [] topicFilters;
        public String uid;
        public MqttListener mqttListener;

        public Builder(Context context,MqttListener mqttListener) {
            this.context = context;
            this.mqttListener = mqttListener;
        }

        public Builder setLoginName(String loginName) {
            this.loginName = loginName;
            return this;
        }

        public Builder setPassWord(String passWord) {
            this.passWord = passWord;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setBroker(String broker) {
            this.broker = broker;
            return this;
        }

        public Builder setFirstLogin(int firstLogin) {
            this.firstLogin = firstLogin;
            return this;
        }

        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        public Builder setBrief(String brief) {
            this.brief = brief;
            return this;
        }

        public Builder setDeviceName(String deviceName) {
            this.deviceName = deviceName;
            return this;
        }


        public Builder setTopicFilters(String ...topicFilters){
            this.topicFilters = topicFilters;
            return this;
        }

        public Builder setTimestamp(long timestamp){
            this.timestamp = String.valueOf(timestamp);
            return this;
        }

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        /** Builds configured {@link MqttConfiguration} object */
        public MqttConfiguration build() {
            Log.d(TAG,"BUILD");
            initEmptyFieldsWithDefaultValues();
            Log.d(TAG, toString());
            return new MqttConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            //超时时间是5秒
            if(timeToWait == 0){
                timeToWait = 5000;
            }

        }
    }

    public void printf(){
        System.err.println("Builder{" +
                "mContext=" + context +
                ", timeToWait=" + timeToWait +
                ", broker='" + broker + '\'' +
                ", loginName='" + loginName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", firstLogin=" + firstLogin +
                ", link='" + link + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", brief='" + brief + '\'' +
                ", token='" + token + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", topicFilters=" + Arrays.toString(topicFilters) +
                ", uid='" + uid + '\'' +
                '}');
    }
}
