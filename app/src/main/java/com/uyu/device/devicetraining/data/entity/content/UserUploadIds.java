package com.uyu.device.devicetraining.data.entity.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by windern on 2016/5/25.
 */
public class UserUploadIds{
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("ids")
    @Expose
    public List<Integer> ids = new ArrayList<Integer>();
}
