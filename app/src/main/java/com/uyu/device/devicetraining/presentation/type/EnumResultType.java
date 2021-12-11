package com.uyu.device.devicetraining.presentation.type;

/**
 * Created by windern on 2015/12/11.
 */
public enum EnumResultType {
    SUCCESS(0,"成功"), FAIL(1,"失败");

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
    private EnumResultType(int value,String name) {
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
