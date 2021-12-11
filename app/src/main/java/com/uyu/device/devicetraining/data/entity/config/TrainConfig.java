package com.uyu.device.devicetraining.data.entity.config;

/**
 * Created by windern on 2016/1/9.
 */
public class TrainConfig {
    /**
     * 去抖时间
     */
    public final static long ShakeTime = 100;
    /**
     * 默认瞳距
     */
    public final static int PupilDefaultDistance = 640;
    /**
    * 按钮去抖时间
    */
    public final static long ButtonPressShakeTime = 1000;
    /**
     * 没有操作训练暂停的时间
     */
    public final static int NoOptTrainLockTime = 3*60;
    /**
     * 没有操作训练结束的时间
     */
    public final static int NoOptTrainFinishTime = 4*60;
    /**
     * 二维码显示时间，超时自动返回介绍页
     */
    public final static int CodeShowTime = 60;
    /**
     * 单位1
     */
    public final static int UnitOne = 1;
}
