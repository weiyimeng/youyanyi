package com.uyu.device.devicetraining.data.entity;

/**
 * Created by windern on 2015/12/8.
 */
public enum EnumUserTrainMode {
    CONTROL(0,"控制模式"), SELFHELP(1,"自助模式");

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
    private EnumUserTrainMode(int value, String name) {
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
}
