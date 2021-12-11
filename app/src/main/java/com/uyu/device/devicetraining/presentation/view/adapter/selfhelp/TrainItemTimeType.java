package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

/**
 * Created by windern on 2016/1/4.
 */
public enum TrainItemTimeType {
    PREPARE(20,"开始准备中"),
    TRAINING(21,"训练中"),
    PAUSING(22,"暂停中"),
    ONE_END(23,"一次结束调整中"),
    ALL_END(24,"全部结束"),
    ALL_PRES(25, "显示所有处方"),
    DEVICE_OVERALL_PREPARE(26,"设备整体准备"),
    WELCOME(27,"欢迎页面"),
    SHOWRESULT(28,"显示结果页面");

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
    private TrainItemTimeType(int value, String name) {
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
