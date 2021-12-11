package com.uyu.device.devicetraining.data.repository.property;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by windern on 2015/12/1.
 */
public class PropertyTool {
    /**
     * 位置
     */
    private final static String PropertiesName = "device.properties";

    public static String getProductUid(Context context){
        Properties props = new Properties();
        try {
            //方法一：通过activity中的context攻取setting.properties的FileInputStream
            InputStream in = context.getAssets().open(PropertiesName);
            //方法二：通过class获取setting.properties的FileInputStream
            //InputStream in = PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
            props.load(in);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return props.getProperty("product_uid");
    }

    public static String getDeviceid(Context context){
        Properties props = new Properties();
        try {
            //方法一：通过activity中的context攻取setting.properties的FileInputStream
            InputStream in = context.getAssets().open(PropertiesName);
            //方法二：通过class获取setting.properties的FileInputStream
            //InputStream in = PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
            props.load(in);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return props.getProperty("device_id");
    }
}
