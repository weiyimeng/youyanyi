package com.uyu.device.devicetraining.data.entity.config;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.RawProvider;

/**
 * Created by windern on 2015/12/11.
 */
public class RGVariableVectorConfig {
    /**
     * 移动的最大距离
     */
    public static final int Max_Distance = 60;
    /**
     * 融合上最大时间
     */
    public static final int Fusing_Max_Time = 3*60*1000;
//    public static final int Fusing_Max_Time = 20*1000;
    /**
     * 失败最大次数
     */
    public static final int Fail_Max_Time = 1;
//    public static final int Fail_Max_Time = 3;

    /**
     * 移动失败的最大次数
     */
    public static final int Move_Fail_Max_Time = 2;

    /**
     * 速度的区间范围mm
     */
    //public static final int[] arraySection = {20,40,60};
    public static final int[] arraySection = {40,20};
    /**
     * 移动1mm需要的时间ms
     */
    //public static final int[] arraySectionSpeedTime = {400,500,1000};
    public static final int[] arraySectionSpeedTime = {800,1000};

    /**
     * 到终点等待时间
     */
    public static final int ExtraEndWaitSecond = 20;

    /**
     * 两个分开最大的时间
     */
    public static final int TwoSplitMaxSecond = 20;
//    public static final int TwoSplitMaxSecond = 10;

    public static final String Fusing_Tip = "按下确认键后开始，保持图中图像为一个，当变成两个时，再次按下确认键";
    public static final String Keeping_Tip = "请继续保持，当保持不住时按下确认键";
    public static final String Finish_Tip = "该关结束";

    public static final int Fusing_Tip_Resid = RawProvider.r_g_variable_vector_fusing_tip;
    public static final int Keeping_Tip_Resid = RawProvider.r_g_variable_vector_keeping_tip;
    public static final int Finish_Tip_Resid = RawProvider.finish_tip;

    /**
     * 红色透明度
     */
    public static final float redAphla = 0.5f;
    /**
     * 绿色透明度
     */
    public static final float greenAphla = 0.5f;

    /**
     * 获取时间
     * @param mm
     * @return
     */
    public static int getSpeedTime(int mm){
        int sectionIndex = 0;
        int i=0;
        while(i<arraySection.length && mm>arraySection[i]){
            i++;
        }
        sectionIndex = i;
        if(sectionIndex>=arraySection.length){
            sectionIndex = arraySection.length-1;
        }
        return arraySectionSpeedTime[sectionIndex];
    }
}
