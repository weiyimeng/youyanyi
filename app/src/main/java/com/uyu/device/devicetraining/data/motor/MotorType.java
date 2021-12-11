package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2015/12/8.
 */
public enum MotorType {
    LINE(0,"直线"),
    GAP(1,"间距"),
    TURNTABLE(2,"转盘"),
    BUTTON(3,"按键"),
    CHARGE(4,"充电");

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
    private MotorType(int value, String name) {
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
