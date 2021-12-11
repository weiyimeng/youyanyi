package com.uyu.device.devicetraining.data.entity.type;

/**
 * Created by windern on 2016/1/7.
 */
public enum EnumColorType {
    GREEN(0, "green", "绿色"),
    RED(1, "red", "红");

    /**
     * 值
     */
    private int value;
    /**
     * 名称
     */
    private String name;
    /**
     * 显示名称
     */
    private String showName;

    /*
     * 构造方法
     */
    private EnumColorType(int value, String name, String showName) {
        this.value = value;
        this.name = name;
        this.showName = showName;
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

    public String getShowName() {
        return showName;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
