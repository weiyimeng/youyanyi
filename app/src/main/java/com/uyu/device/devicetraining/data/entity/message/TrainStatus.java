package com.uyu.device.devicetraining.data.entity.message;

/**
 * Created by windern on 2015/12/5.
 */
public enum TrainStatus {
    WELCOME(0,"等待准备开始"),
    PREPARING(1,"准备中"),
    TRAINING(2,"正在训练"),
    PAUSING(3,"等待"),
    WAITING(4,"等待下一步"),
    FINISHING(5,"正在结束"),
    ADJUST(6,"设备调整中");

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
    private TrainStatus(int value,String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 获取值
     * @return
     */
    public int getValue(){
        return value;
    }

    /**
     * 获取名称
     * @return
     */
    public String getName(){
        return name;
    }
}
