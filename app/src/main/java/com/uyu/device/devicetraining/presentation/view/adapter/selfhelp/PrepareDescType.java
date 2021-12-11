package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

/**
 * Created by windern on 2016/1/8.
 */
public enum PrepareDescType {
    NORMAL(0, "普通"),
    SELECT(1, "选择"),
    CONTENT(2, "内容"),

    //翻转拍
    EYELEVEL(3, "级别"),
    TEXTSIZE(4, "字体"),

    //用户下载的内容
    USERCONTENT(5,"用户下载的内容");

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
     PrepareDescType(int value, String name) {
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
