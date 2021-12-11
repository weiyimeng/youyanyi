package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2016/1/26.
 */
public enum DirectionType {
    BACKWARD(0,"后退方向"),
    FORWARD(1,"前进方向");

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
    private DirectionType(int value, String name) {
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
