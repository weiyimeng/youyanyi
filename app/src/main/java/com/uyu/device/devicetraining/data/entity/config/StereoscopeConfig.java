package com.uyu.device.devicetraining.data.entity.config;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.trainpres.StereoscopeTrainPres;
import com.uyu.device.devicetraining.data.entity.type.EnumFusionTrain;
import com.uyu.device.devicetraining.data.motor.DistanceHandler;
import com.uyu.device.devicetraining.presentation.RawProvider;

/**
 * Created by windern on 2015/12/11.
 */
public class StereoscopeConfig {
    /**
     * 立体镜最大等级
     */
    public static final int Max_Level = 16;
    /**
     * 融合上最大时间
     */
    public static final int Fusing_Max_Time_First = 40 * 1000;
    public static final int Fusing_Max_Time = 15 * 1000;
    //public static final int Fusing_Max_Time = 20*1000;
    /**
     * 保持最大时间
     */
    public static final int Keeping_Max_Time = 10 * 1000;
    /**
     * 失败最大次数
     */
    public static final int Fail_Max_Time = 1;
    /**
     * 返回的次数
     */
    public static final int Back_Max_Time = 2;

    /**
     * 默认平板距离
     */
    public static final int DefaultScreenLocation = 200;

    /**
     * 保持最大时间-简单关即奇数关
     */
    public static final int Keeping_Max_Time_Easy = 3 * 1000;
    /**
     * 保持最大时间-难关即偶数关
     */
    public static final int Keeping_Max_Time_Hard = 8 * 1000;
    /**
     * 需要训练的总关数
     */
    public static final int Need_Train_Total_Level_Count = 20;
//    public static final int Need_Train_Total_Level_Count = 4;

    //定制pad
    public static final int Pic_Width_MM = 148;
    public static final int Pic_Height_MM = 68;

    /**
     * 屏幕默认真实距离200mm，对应0级
     * 往后加一级，距离加10mm
     */
    public static final int Screen_Default_Real_Distance = 200;
    /**
     * pad屏幕最低档的位置等级
     */
    public static final int Screen_Pos_Min_Level = 0;
    /**
     * pad屏幕最高档的位置等级
     */
    public static final int Screen_Pos_Max_Level = 10;

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
            88, 81,
            86, 73,
            86, 68,
            84, 61,
            86, 56,
            86, 49,
            84, 38,
            85, 31
    };
    private static float[] arrayIntervalSpaceBI = {
            86, 80,
            86, 90,
            86, 94,
            86, 98,
            86, 102,
            86, 106,
            86, 110,
            86, 112
    };

    public static float getIntervalSpace(EnumFusionTrain fusionTrain, int level) {
        if (fusionTrain == EnumFusionTrain.BO) {
            if (level <= arrayIntervalSpaceBO.length - 1) {
                return arrayIntervalSpaceBO[level];
            } else {
                return arrayIntervalSpaceBO[arrayIntervalSpaceBO.length - 1];
            }
        } else {
            if (level <= arrayIntervalSpaceBI.length - 1) {
                return arrayIntervalSpaceBI[level];
            } else {
                return arrayIntervalSpaceBI[arrayIntervalSpaceBI.length - 1];
            }
        }
    }

    /**
     * 获取屏幕电机的位置
     *
     * @param screenPosLevel
     * @return
     */
    public static int getMotorScreenLocation(int screenPosLevel) {
        int realDistance = Screen_Default_Real_Distance;
        //在等级范围内的计算距离，否则0级默认的
        if (screenPosLevel >= Screen_Pos_Min_Level && screenPosLevel <= Screen_Pos_Max_Level) {
            realDistance = Screen_Default_Real_Distance + screenPosLevel * 10;
        }
        int motorScreenLocation = DistanceHandler.computeMotorScreenDistance(realDistance);
        return motorScreenLocation;
    }
}
