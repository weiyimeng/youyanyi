package com.uyu.device.devicetraining.data.entity.config.trial;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.RawProvider;

/**
 * Created by windern on 2015/12/11.
 */
public class FracturedRulerConfig {
    /**
     * 裂隙尺最大等级
     */
    public static final int Max_Level = 8;
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
    public static final String Keeping_Tip = "请继续保持，直至提示休息，如果不能保持，请再次按下确认键";
    public static final String Finish_Tip = "请先休息，等待视光师准备完毕";

    public static final int Fusing_Tip_Resid = RawProvider.fractured_ruler_fusing_tip;
    public static final int Keeping_Tip_Resid = RawProvider.fractured_ruler_keeping_tip;
    public static final int Finish_Tip_Resid = RawProvider.fractured_ruler_finish_tip;

    /**
     * 挡板的位置(单位：mm)
     */
    private static float[] arrayBaffleLocation = {
            260,
            246, 223,
            203, 186,
            171, 156,
            142, 130,
            120, 110
    };

    /**
     * 图片间距（单位：mm）
     */
    private static float[] arrayIntervalSpace = {
            20,
            30,40,
            50,60,
            70,80,
            90,100,
            110,120
    };

    public static float getIntervalSpace(int level){
        if(level<= arrayIntervalSpace.length-1){
            return arrayIntervalSpace[level];
        }else{
            return arrayIntervalSpace[arrayIntervalSpace.length-1];
        }
    }

    public static float getBaffleLocation(int level){
        if(level<=arrayBaffleLocation.length-1){
            return arrayBaffleLocation[level];
        }else{
            return arrayBaffleLocation[arrayBaffleLocation.length-1];
        }
    }

    public static int[] arrayTransformLevel = {1,3,5,7,8,9,10,11};
    public static int getTransformLevel(int nowLevel){
        return arrayTransformLevel[nowLevel-1];
    }
}
