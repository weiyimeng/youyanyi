package com.uyu.device.devicetraining.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uyu.device.devicetraining.data.entity.trainback.TrainBack;

/**
 * Created by windern on 2015/12/8.
 */
public class TrainBackApiResult <T extends TrainBack> extends ApiResult {
    @SerializedName("data")
    @Expose
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
