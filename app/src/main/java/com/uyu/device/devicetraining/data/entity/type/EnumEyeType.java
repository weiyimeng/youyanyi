package com.uyu.device.devicetraining.data.entity.type;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.AppProvider;

/**
 * Created by windern on 2015/12/8.
 */
public enum EnumEyeType {
    LEFT(AppProvider.getApplication().getString(R.string.left), 0, 50), RIGHT(AppProvider.getApplication().getString(R.string.right), 1, 50), DOUBLE(AppProvider.getApplication().getString(R.string.double_), 2, 40);

    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private int value;
    /**
     * 标准基数
     */
    private int standardCount = 1;

    /*
     * 构造方法
     */
    private EnumEyeType(String name, int value, int standardCount) {
        this.name = name;
        this.value = value;
        this.standardCount = standardCount;
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

    /**
     * 获取训练个数的基数
     *
     * @return
     */
    public int getStandardCount() {
        return standardCount;
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
