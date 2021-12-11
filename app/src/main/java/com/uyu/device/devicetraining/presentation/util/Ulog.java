package com.uyu.device.devicetraining.presentation.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by windern on 2015/12/2.
 */
public class Ulog {

    public static void d(String tag,String msg) {
        if (ConstantValues.DEBUG) {
            Log.d(tag, msg);
        }
    }
    public static void d(String tag,String msg,Throwable e) {
        if (ConstantValues.DEBUG) {
            Log.d(tag, msg,e);
        }
    }
    public static void i(String tag,String msg) {
        if (ConstantValues.DEBUG) {
            Log.i(tag, msg);
        }
    }
    public static void v(String tag,String msg) {
        if (ConstantValues.DEBUG) {
            Log.v(tag, msg);
        }
    }
    public static void e(String tag,String msg) {
        if (ConstantValues.DEBUG) {
            Log.e(tag, msg);
        }
    }
    public static void e(String tag,String msg,Throwable e) {
        if (ConstantValues.DEBUG) {
            Log.e(tag, msg,e);
        }
    }
    public static void w(String tag,String msg) {
        if (ConstantValues.DEBUG) {
            Log.w(tag, msg);
        }
    }
    public static void w(String tag,String msg,Throwable e) {
        if (ConstantValues.DEBUG) {
            Log.w(tag, msg,e);
        }
    }

    /**
     * 打印文件到 Android/Data/log/*
     * @param tag
     * @param msg
     */
    public static void f(String tag, String msg) {
        try {
            if (ConstantValues.DEBUG) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File logpath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Android/log/");
                    if(!logpath.exists()){
                        logpath.mkdirs();
                    }
                    String filename;
                    SimpleDateFormat format =new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                    if(TextUtils.isEmpty(tag)){
                        filename = format.format(new Date());
                    }else{
                        filename = tag;
                    }
                    String path = logpath.getAbsolutePath() +"/"+filename+".txt";
                    FileWriter fw = new FileWriter(new File(path));
                    fw.write(msg);
                    fw.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
