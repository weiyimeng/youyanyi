package com.uyu.device.devicetraining.data.entity.message;

/**
 * Created by windern on 2015/12/5.
 */
public enum TrainMessageType {

    START(0,"开始"),
    PAUSE(1,"暂停"),
    RESUME(2,"继续"),
    READY(3,"准备好"),
    PASS(4,"通过下一个"),
    FINISH(5,"结束"),
    NORMAL(6,"普通消息"),
    EXECUTED(7,"执行完成"),
    STOP(8,"立即结束"),
    EXIT(9,"退出结束所有训练"),
    CONNECT(10,"连接绑定上设备"),
    CONTROLSET(11,"控制设置电机"),
    RESTLENS(12,"重置翻转拍镜片");

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
    private TrainMessageType(int value,String name) {
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
