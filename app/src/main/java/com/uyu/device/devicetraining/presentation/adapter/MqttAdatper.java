package com.uyu.device.devicetraining.presentation.adapter;

import com.uyu.device.devicetraining.data.entity.message.EmqttMessage;

/**
 * Created by windern on 2015/12/7.
 */
public interface MqttAdatper {
    void receiveMessage(EmqttMessage emqttMessage);
    void sendMessage(EmqttMessage emqttMessage);
}
