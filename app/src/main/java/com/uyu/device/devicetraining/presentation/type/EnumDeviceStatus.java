package com.uyu.device.devicetraining.presentation.type;

/**
 * Created by windern on 2015/12/11.
 */
public enum EnumDeviceStatus {
    WELCOME(0,"欢迎页"),
    TRAINING(1,"训练"),
    LOGIN(2,"登录"),
    CHECK_AUTH(3,"联网验证"),
    INTRODUCTION(4,"介绍"),
    LOCK(5,"锁屏"),
    TRAINING_TRIAL(5,"体验训练");

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
    private EnumDeviceStatus(int value, String name) {
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
