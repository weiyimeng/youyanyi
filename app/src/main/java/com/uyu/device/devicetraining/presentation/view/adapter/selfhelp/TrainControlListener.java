package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

/**
 * Created by windern on 2016/1/9.
 */
public interface TrainControlListener {
    //停止训练
    void onStopTrain();

    //重新发送消息
    void onsendMessageAgain();
    //暂停训练
    void onTrainningPause();
    //继续
    void onTrainResume();
}
