package com.uyu.device.devicetraining.data.repository.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.uyu.device.devicetraining.data.entity.EnumUserTrainMode;
import com.uyu.device.devicetraining.data.entity.trainpres.ReversalTrainPres;

/**
 * Created by windern on 2015/12/1.
 */
public class SharePreferenceTool {
    public static final String PREF_DEVICE_INFO = "pref_device_info";
    public static final String PREF_DEVICE_ID = "pref_device_id";
    public static final String PREF_DEVICE_TOKEN = "pref_device_token";
    public static final String PREF_MERCHANT_ID = "pref_merchant_id";
    public static final String PREF_DEVICE_NAME = "pref_device_name";
    public static final String PREF_DEVICE_ALIAS = "pref_device_alias";
    public static final String PREF_PRODUCT_UID = "pref_product_uid";
    public static final String PREF_EMQTT_STATUS = "pref_emqtt_status";
    public static final String PREF_USER_TRAIN_MODE = "pref_user_train_mode";
    public static final String PREF_CONNECT_BLUTTOOTH_UID = "pref_connect_bluetooth_uid";
    public static final String PREF_DEVICE_ACTUAL_INFO = "pref_device_actual_info";

    public static final String PREF_INTRODUCTION_SPEAK_ON = "pref_introduction_speak_on";

    public static final String PREF_DUID = "pref_duid";

    public static final String PREF_NOW_TRAIN_USER_ID = "pref_now_train_user_id";
    public static final String PREF_NOW_TRAIN_USER_UPLOADIDS = "pref_now_train_user_uploadids";

    /**
     * 存储翻转拍最后的处方，用于同一个用户训练恢复状态
     */
    public static final String PREF_LAST_REVERSAL_PRES = "pref_last_reversal_pres";

    public static void setSharePreferenceValue(Context context,String key,String value){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor pref_editor = pref.edit();
        pref_editor.putString(key, value);
        pref_editor.commit();
    }

    public static String getSharePreferenceValue(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        return pref.getString(key,"");
    }

    public static void setSharePreferenceValueBoolean(Context context,String key,boolean value){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor pref_editor = pref.edit();
        pref_editor.putBoolean(key, value);
        pref_editor.commit();
    }

    public static boolean getSharePreferenceValueBoolean(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static void setSharePreferenceValueInt(Context context,String key,int value){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor pref_editor = pref.edit();
        pref_editor.putInt(key, value);
        pref_editor.commit();
    }

    public static int getSharePreferenceValueInt(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    /**
     * 设置emqtt状态
     * @param context
     * @param value
     */
    public static void setEmqttStatus(Context context,boolean value){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor pref_editor = pref.edit();
        pref_editor.putBoolean(PREF_EMQTT_STATUS, value);
        pref_editor.commit();
    }

    /**
     * 获取emqtt状态
     * @param context
     */
    public static boolean getEmqttStatus(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_EMQTT_STATUS,false);
    }

    /**
     * 获取当前训练类型
     * @return
     */
    public static EnumUserTrainMode getUserTrainMode(Context context){
        EnumUserTrainMode userTrainMode = EnumUserTrainMode.SELFHELP;
        String userTrainModeValue = SharePreferenceTool.getSharePreferenceValue(context, SharePreferenceTool.PREF_USER_TRAIN_MODE);
        if(!userTrainModeValue.equals("")){
            userTrainMode = EnumUserTrainMode.valueOf(userTrainModeValue);
        }
        return userTrainMode;
    }

    /**
     * 设置最后的翻转拍处方
     * @param context
     * @param reversalTrainPres
     */
    public static void setLastReversalTrainPres(Context context,ReversalTrainPres reversalTrainPres){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor pref_editor = pref.edit();
        String jsonStr = "";
        if(reversalTrainPres!=null){
            Gson gson = new Gson();
            jsonStr = gson.toJson(reversalTrainPres);
        }
        pref_editor.putString(PREF_LAST_REVERSAL_PRES, jsonStr);
        pref_editor.commit();
    }

    /**
     * 获取最后的翻转拍处方
     * @param context
     * @return
     */
    public static ReversalTrainPres getLastReversalTrainPres(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_DEVICE_INFO, Context.MODE_PRIVATE);
        String jsonStr = pref.getString(PREF_LAST_REVERSAL_PRES,"");
        ReversalTrainPres reversalTrainPres = null;
        if(!jsonStr.equals("")){
            Gson gson = new Gson();
            reversalTrainPres = gson.fromJson(jsonStr,ReversalTrainPres.class);
        }
        return reversalTrainPres;
    }
}
