package com.uyu.device.devicetraining.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by windern on 2015/12/1.
 */
public class TokenEntity {
    @SerializedName("uid")
    @Expose
    private int uid;
    @SerializedName("tk")
    @Expose
    private String tk;

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
