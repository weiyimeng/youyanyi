package com.uyu.device.devicetraining.presentation.util;

/**
 * Created by windern on 2015/12/8.
 */
public class MyConfig {
    /**
     * 官网地址
     */
    public final static String OfficialWebsite = "http://www.uyu.com";

    /**
     * 图表显示最大的列数
     */
    public final static int MaxChartColumnNum = 8;
    /**
     * 用眼超标的时间-单位分钟
     */
    public final static int EyeUseOverStandardMinute = 30;
    /**
     * 用眼有效的时间-单位毫秒
     */
    public final static int EyeUseValidTime = 30*1000;
    /**
     * 训练时间超时重新开始时间
     */
    public final static int TrainingOverStartTime = 30*60*1000;
    //public final static int TrainingOverStartTime = 20*1000;

    /**
     * 阅读训练切换时间
     */
    public final static int TrainingReadingSwitchSecond = 8;

    /**
     * 扫描间隔时间（ms）
     */
    public final static long ScanIntervalTime = 400;

    /**
     * 最大超时记录数
     */
    public static final int MaxOverCount = 6;

//    public final static String QQZoneAppId = "801564140";
//    public final static String QQZoneAppKey = "06b60f75b1736ff7f3c85a5448685d30";

//    public final static String QQZoneAppId = "1104607834";
//    public final static String QQZoneAppKey = "hg3QRswsYGPAaSLc";

    //移动应用
    public final static String QQZoneAppId = "1104607656";
    public final static String QQZoneAppKey = "jMYCbHSXiXwfGNux";

    //微信
    public final static String WechatAppId = "wx0a6e12cea2ab210b";
    public final static String WechatAppSecret = "8b069a435282f2b84c008dac0377ade2";


}
