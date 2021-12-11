package com.uyu.device.devicetraining.presentation.adapter;

import android.content.DialogInterface;
import android.view.KeyEvent;

import com.uyu.device.devicetraining.data.entity.message.TrainStatus;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;

/**
 * Created by windern on 2015/12/14.
 */
public interface TrainFragmentListener extends MqttAdatper,SelfhelpPresenterListener{
    void changeToStatus(TrainStatus trainStatus);
    boolean onKeyDown(int keyCode, KeyEvent event);
    void showPostDialog(String title,String content);
    void hidePostDialog();
    void showNetworkExceptionDialog(DialogInterface.OnClickListener retryClickListener);

    void prepare();
    void startTrain();
    void finishTrain();
    void finishTrain(TrainBack trainBack);
    void pauseTrain();
    void resumeTrain();

    void setItemTitle(String itemTitle);
    void setItemTip(String itemTip);
}
