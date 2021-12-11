package com.uyu.device.devicetraining.presentation.adapter;

import com.uyu.device.devicetraining.data.entity.message.PresTrainMessageContent;

/**
 * Created by windern on 2015/12/15.
 */
public interface TrainPresenterListener extends MqttAdatper{
    void startTrain(PresTrainMessageContent presTrainMessageContent);
    void initStartStatus();
    void sendNowInfo();
    void prepareFinishData();
    void finishTrain();
    void receiveFinishServerMessage();
    void postData();
    void postData(boolean isUpdateReception);
    void sendFinishMsg();
    void sendNormalMsg();
    void pauseTrain();
    void resumeTrain();
    void pressEnter();
    void pressUp();
    void pressDown();
}
