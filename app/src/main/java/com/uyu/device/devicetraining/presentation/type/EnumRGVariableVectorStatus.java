package com.uyu.device.devicetraining.presentation.type;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.RawProvider;

/**
 * Created by windern on 2015/12/28.
 */
public enum EnumRGVariableVectorStatus {
    START(0,"开始","当图片变成两个时，请按键。", RawProvider.r_g_variable_vector_start_tip),
    ONE_MOVE(1,"一个保持","请坚持，变成两个，请按键。", RawProvider.r_g_variable_vector_one_keep_tip),
    TWO_KEEP(2,"两个移动","变成一个请按键。", RawProvider.r_g_variable_vector_two_move_tip),
    FINISH(3,"结束","此项训练结束。", RawProvider.r_g_variable_vector_finish_tip),
    MAX_KEEP(3,"最远坚持","请坚持，变成两个，请按键。", RawProvider.r_g_variable_vector_one_keep_tip);

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

    /*
     * 构造方法
     */
    private EnumRGVariableVectorStatus(int value, String name,String tip, int tipResid) {
        this.value = value;
        this.name = name;
        this.tip = tip;
        this.tipResid = tipResid;
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
}
