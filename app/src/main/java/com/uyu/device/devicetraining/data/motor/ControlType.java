package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/1/19.
 */
public enum ControlType {
    RESET(0,"复位"),
    SET(1,"指定"),
    GET(2,"获取"),
    MOVE(3,"移动"),
    STOP(4,"停止"),
    CHARGE(5,"充电管理");

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
    private ControlType(int value, String name) {
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
