package com.uyu.device.devicetraining.data.motor;

/**
 * Created by windern on 2015/12/8.
 */
public enum EnumMotorNum {
    SCREEN(1,"屏幕"),
    BAFFLE(2,"挡板"),
    PUPIL(3,"瞳距"),
    RIGHT(4,"右边"),
    LEFT(5,"左边"),
    BUTTON(6,"按键");

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
    private EnumMotorNum(int value, String name) {
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

    //我们可以重写toString()，不可以自己重写valueOf()，当我们重写toString()方法时，valueOf()会自动重写，不用我们理会。
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
