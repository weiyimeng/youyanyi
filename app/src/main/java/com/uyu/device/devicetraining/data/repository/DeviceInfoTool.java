package com.uyu.device.devicetraining.data.repository;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by windern on 2016/5/12.
 */
public class DeviceInfoTool {
    /**
     * 获取手机/pad唯一编码
     * 现在用的是可打电话的imei
     * @param context
     * @return
     */
    public static String getDuid(Context context){
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager)context.getSystemService( Context.TELEPHONY_SERVICE );

        /*
         * getDeviceId() function Returns the unique device ID.
         * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
         */
        String imeistring = telephonyManager.getDeviceId();
        return imeistring;
    }
}
