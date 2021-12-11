package com.uyu.device.devicetraining.presentation.view.adapter;

/**
 * Created by windern on 2015/12/10.
 */
public interface PicLevelLister {
    void changeToStartStatus();
    void changeToTrainStatus();
    void changeTo(int nowLevel,int nowPic);
}
