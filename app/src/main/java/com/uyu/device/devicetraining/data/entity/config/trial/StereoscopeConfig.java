package com.uyu.device.devicetraining.data.entity.config.trial;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.presentation.RawProvider;

/**
 * Created by windern on 2015/12/11.
 */
public class StereoscopeConfig {
    /**
     * 立体镜最大等级
     */
    public static final int Max_Level = 8;
    /**
     * 融合上最大时间
     */
    public static final int Fusing_Max_Time_First = 40*1000;
    public static final int Fusing_Max_Time = 20*1000;
    //public static final int Fusing_Max_Time = 20*1000;
    /**
     * 保持最大时间
     */
    public static final int Keeping_Max_Time = 6*1000;
    /**
     * 失败最大次数
     */
    //public static final int Fail_Max_Time = 1;
    public static final int Fail_Max_Time = 1;

    /**
     * 默认平板距离
     */
    public static final int DefaultScreenLocation = 200;

    //定制pad
    public static final int Pic_Width_MM = 148;
    public static final int Pic_Height_MM = 68;

    //白色区域-在其他正常比例的pad上
//    public static final int Pic_Width_MM = 133;
//    public static final int Pic_Height_MM = 67;
    //所有的
//    public static final int Pic_Width_MM = 178;
//    public static final int Pic_Height_MM = 82;

    public static final String Fusing_Tip = "将左右两幅图合成一副保持住，同时按下确认键";
    public static final String Keeping_Tip = "请继续保持，直至图像换成其他内容，如果不能保持，请再次按下确认键";
    public static final String Finish_Tip = "该关结束";

    public static final int Fusing_Tip_Resid = RawProvider.stereoscope_fusing_tip;
    public static final int Keeping_Tip_Resid = RawProvider.stereoscope_keeping_tip;
    public static final int Finish_Tip_Resid = RawProvider.finish_tip;

    private static float[] arrayIntervalSpaceBO = {
            88,81,
            86,73,
            86,68,
            84,61,
            86,56,
            86,49,
            84,38,
            85,31
    };
    private static float[] arrayIntervalSpaceBI = {
            86,80,
            86,90,
            86,94,
            86,98,
            86,102,
            86,106,
            86,110,
            86,112
    };

    public static float getIntervalSpace(EnumFusionTrain fusionTrain, int level){
        if(fusionTrain== EnumFusionTrain.BO){
            if(level<= arrayIntervalSpaceBO.length-1){
                return arrayIntervalSpaceBO[level];
            }else{
                return arrayIntervalSpaceBO[arrayIntervalSpaceBO.length-1];
            }
        }else{
            if(level<= arrayIntervalSpaceBI.length-1){
                return arrayIntervalSpaceBI[level];
            }else{
                return arrayIntervalSpaceBI[arrayIntervalSpaceBI.length-1];
            }
        }
    }

    public static int getTransformLevel(int nowLevel){
        return nowLevel*2;
    }
}
