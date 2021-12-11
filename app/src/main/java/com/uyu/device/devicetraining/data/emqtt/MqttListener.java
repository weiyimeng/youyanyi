package com.uyu.device.devicetraining.data.emqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;

/**
 * Created by windern on 2015/12/2.
 */
public interface MqttListener extends MqttCallback {
    void connectSuccess();
    void connectFail(String failMsg);
}
