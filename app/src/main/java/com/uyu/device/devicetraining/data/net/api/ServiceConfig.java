package com.uyu.device.devicetraining.data.net.api;

/**
 * Created by windern on 2015/12/2.
 */
public class ServiceConfig {
    public static final String DOMAIN = "http://app.uyu.com";
    public static String BROKER_URL_SSL = "ssl://app.uyu.com:8883";
    public static final String OTHER_DOMAIN = "http://mall.uyu.com";
//    public static final String DOMAIN = "http://test.uyu.com";
//    public static String BROKER_URL_SSL = "ssl://test.uyu.com:8883";
//    public static final String OTHER_DOMAIN = "http://test.uyu.com";

    public static final String API_URL = DOMAIN + "/device/api/v1/";
    public static final String RESUME_TRAIN_URL = OTHER_DOMAIN + "/api/device/unlockTrain?duid=%s&tk=";

    /**
     * 网络失败重试时间
     */
    public static final int RETRY_SECOND = 2;

    /**
     * 检测更新
     */
    public static final String CHEACK_VERSION = "http://app.uyu.com:3001/check/version/checkVersion?";
}
