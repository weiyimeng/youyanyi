package com.uyu.device.devicetraining.presentation.util;

import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.presentation.AppProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by windern on 2015/12/3.
 */
public class DateUtil {

    public static String FORMAT_SHORT = "yyyy-MM-dd";
    public static String FORMAT_MINUTE= "yyyy-MM-dd HH:mm";
    public static String FORMAT_HOUR= "yyyy-MM-dd HH";
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    public static String FORMAT_FULL_US = "yyyy-MM-dd hh:mm:ss.SSS";
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    public static String FORMAT_NOS = "yyyy-MM-dd HH:mm";
    public static String FORMAT_EASY = "MM-dd HH:mm";

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 默认日期格式
     */
    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 获取当前日期,根据用户指定格式
     */
    public static String getNow(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *得到毫秒值
     * @param strDate
     * @return
     */
    public static long getTime(String strDate){
        return parse(strDate).getTime();
    }

    /**
     * 获取日期年份
     * @param date
     *            日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }
    /**
     * 判断两个时间是否是10分钟之内
     * @param date
     * @param lastDate
     * @return
     */
    public static boolean isInTenMinute(Date date, Date lastDate){
        long delta = date.getTime() - lastDate.getTime();
        boolean is = false;
        if(-6000L *10 < delta && delta < 60000L * 10){
            is = true;
        }
        return is;
    }

    /**
     * 时间级别-毫秒
     */
    public static final int TimeLevel_Millisecond = 0;
    /**
     * 时间级别-秒
     */
    public static final int TimeLevel_Second = 1;
    /**
     * 时间级别-分
     */
    public static final int TimeLevel_Minute = 2;
    /**
     * 时间级别-时
     */
    public static final int TimeLevel_Hour = 3;
    /**
     * 时间级别-天
     */
    public static final int TimeLevel_Day = 4;

    /**
     * 将时间转换成0"毫秒",1"秒",2"分",3"时",4"天"
     * @param TimeMillis 时间
     * @param timeLevel 时间的级别，级别，0从毫秒开始，1从秒开始
     * @param level 显示的级别 级别，0从毫秒开始，1从秒开始
     * @return
     */
    public static String showDate(long TimeMillis,int timeLevel,int level){
        String strDate = "";
        //一次除以的数，得到余数就是该单位的数
        int[] devideTime = {1000,60,60,24};
        String[] devideTimeUnit = {AppProvider.getApplication().getString(R.string.millisecond),AppProvider.getApplication().getString(R.string.seconds),AppProvider.getApplication().getString(R.string.minutes),AppProvider.getApplication().getString(R.string.hour),AppProvider.getApplication().getString(R.string.day)};
        int i=timeLevel;
        long quotient = TimeMillis;
        //现将时间转到需要显示的那个时间的级别
        while(i<level){
            quotient = quotient/devideTime[i];
            i++;
        }

        //余数，当前需要显示的数字
        long remainder = quotient%devideTime[i];
        //商，需要一直除下去的
        quotient = quotient/devideTime[i];
        strDate = String.valueOf(remainder)+devideTimeUnit[i] + strDate;
        //如果商数大于0，则需要继续；如果到小时以后，则剩下的都是天
        while(quotient>0 && i<devideTime.length){
            i++;
            //先取余数，再取商，否则逻辑就不对了，取得的余数也不对了
            remainder = quotient%devideTime[i];
            quotient = quotient/devideTime[i];
            strDate = String.valueOf(remainder)+devideTimeUnit[i] + strDate;
        }
        //如果商数大于0，则一定是到小时结束了，则剩下的都是天了
        if(quotient>0){
            strDate = String.valueOf(remainder)+devideTimeUnit[i+1] + strDate;
        }
        return strDate;
    }
}
