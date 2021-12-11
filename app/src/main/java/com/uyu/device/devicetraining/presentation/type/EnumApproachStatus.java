package com.uyu.device.devicetraining.presentation.type;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.RawProvider;

/**
 * Created by windern on 2015/12/28.
 */
public enum EnumApproachStatus {
//    START(0,"开始","双眼紧盯引导棒的尖部，当尖部变成两个时，大声说出“两个”",6*1000),
//    MAX_DISTANCE(1,"最大距离","双眼用力，尖部可以看成一个，大声说“一个”。合不成一个，请再次说“两个”，直至合成一个时，说“一个”",12*1000),
//    ADJUST_DISTANCE(1,"微调","合不成一个，请再次说“两个”，直至合成一个时，说“一个”",6*1000);
    INIT(0,"初始化状态","初始化状态", R.raw.approach_start_tip,5*1000),
    START(1,"开始","双眼紧盯引导棒的尖部，当尖部变成两个时，按下确认键", RawProvider.approach_start_tip,5*1000),
    TWO_SPLIT(2,"两个移动","变成一个按下确认键，", RawProvider.approach_two_move_tip,2*1000),
    ONE_KEEP(3,"一个保持","变成一个坚持10秒，如果变成两个按下确认键", RawProvider.approach_one_keep_tip,4*1000),
    TWO_MOVE(4,"两个移动","变成一个按下确认键，", RawProvider.approach_two_move_tip,2*1000),
    END(5,"结束","此项训练结束，", RawProvider.approach_end_move_tip,1*1000);

    /**
     * 值
     */
    private int value;
    /**
     * 名称
     */
    private String name;
    /**
     * 提示
     */
    private String tip;
    /**
     * 提示语音
     */
    private int tipResid = 0;
    /**
     * 提示结束后时间
     */
    private int afterTime = 1000;

    /*
     * 构造方法
     */
    private EnumApproachStatus(int value,String name,String tip,int tipResid,int afterTime) {
        this.value = value;
        this.name = name;
        this.tip = tip;
        this.tipResid = tipResid;
        this.afterTime = afterTime;
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

    /**
     * 获取提示
     * @return
     */
    public String getTip() {
        return tip;
    }

    /**
     * 获取提示语音
     * @return
     */
    public int getTipResid() {
        return tipResid;
    }

    /**
     * 获取之后的时间
     * @return
     */
    public int getAfterTime() {
        return afterTime;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
