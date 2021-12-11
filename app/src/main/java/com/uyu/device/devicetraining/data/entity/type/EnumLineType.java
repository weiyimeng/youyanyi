package com.uyu.device.devicetraining.data.entity.type;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.AppProvider;

/**
 * Created by windern on 2015/12/8.
 */
public enum EnumLineType {
    STRAIGHT(0, AppProvider.getApplication().getString(R.string.straight_line)),
    CURVE(1,AppProvider.getApplication().getString(R.string.dotted_line)),
    DASHED(2,AppProvider.getApplication().getString(R.string.curve));

    /**
     * 值
     */
    private int value;
    /**
     * 名称
     */
    private String name;


    /*
     * 构造方法
     */
     EnumLineType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 获取名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 获取值
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
