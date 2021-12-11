package com.uyu.device.devicetraining.data.entity.type;

/**
 * Created by windern on 2016/1/7.
 */
public enum EnumShowContentType {
    NORMAL(0,"普通"),
    WORD(1,"词语（以空格分开）"),
    SENTENCE(1,"诗词（以逗号和句号分开）");

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
    private EnumShowContentType(int value, String name) {
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
