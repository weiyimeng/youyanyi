package com.uyu.device.devicetraining.data.entity.type;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.AppProvider;

/**
 * Created by windern on 2015/12/8.
 */
public enum EnumGlassType {
    Zheng(AppProvider.getApplication().getString(R.string.positive), 0, "辨认", 0xff0000ff), Fu(AppProvider.getApplication().getString(R.string.negative), 1, "清晰", 0xffff0000);

    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private int value;
    /**
     * 按钮提示
     */
    private String btnShowName;
    /**
     * 颜色
     */
    private int color;

    /**
     * 构造方法
     */
    private EnumGlassType(String name, int value, String btnShowName, int color) {
        this.name = name;
        this.value = value;
        this.btnShowName = btnShowName;
        this.color = color;
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
     * 获取按钮提示
     *
     * @return
     */
    public String getBtnShowName() {
        return btnShowName;
    }

    /**
     * 获取颜色
     * @return
     */
    public int getColor(){
        return color;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
