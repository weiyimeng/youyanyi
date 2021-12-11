package com.uyu.device.devicetraining.data.entity.config.trial;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.RawProvider;

/**
 * Created by windern on 2015/12/11.
 */
public class RGFixedVectorConfig {
    /**
     * 红绿固定最大等级
     */
    public static final int Max_Level = 9;
    /**
     * 融合上最大时间
     */
    public static final int Fusing_Max_Time = 20*1000;
    //public static final int Fusing_Max_Time = 20*1000;
    /**
     * 保持最大时间
     */
    public static final int Keeping_Max_Time = 6*1000;
    /**
     * 失败最大次数
     */
//    public static final int Fail_Max_Time = 1;
    public static final int Fail_Max_Time = 1;

    public static final String Fusing_Tip = "将左右两幅图合成一副保持住，同时按下确认键";
    public static final String Keeping_Tip = "请继续保持，直至图像换成其他内容，如果不能保持，请再次按下确认键";
    public static final String Finish_Tip = "该关结束";

    public static final int Fusing_Tip_Resid = RawProvider.stereoscope_fusing_tip;
    public static final int Keeping_Tip_Resid = RawProvider.stereoscope_keeping_tip;
    public static final int Finish_Tip_Resid = RawProvider.finish_tip;

    /**
     * 红色透明度
     */
    public static final float redAphla = 0.4f;
    /**
     * 绿色透明度
     */
    public static final float greenAphla = 0.4f;

    /**
     * 图片间距（单位：mm）
     */
    private static float[] arrayIntervalSpace = {
            4,8,
            12,16,
            24,32,
            40,48,
            56,64,
            72,80,
            88,96,
            104,112,
            120
    };

    public static float getIntervalSpace(int level){
        if(level<= arrayIntervalSpace.length-1){
            return arrayIntervalSpace[level];
        }else{
            return arrayIntervalSpace[arrayIntervalSpace.length-1];
        }
    }

    public static int[] arrayTransformLevel = {2,4,6,8,10,12,14,16,17};
    public static int getTransformLevel(int nowLevel){
        return arrayTransformLevel[nowLevel-1];
    }
}
