package app.update.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by banxinyu on 2016/10/14.
 */
public class HttpUtils {

    /**
     * 发送Get请求
     * @param path 请求地址
     * @return 请求结果的封装
     */
    public static HttpEntity get(String path) {
        L.d(path);
        HttpEntity httpEntity = new HttpEntity(0,0,null);
        try {
            URL url =new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setRequestMethod("GET");
            conn.connect();
            httpEntity.code = conn.getResponseCode();
            httpEntity.contentLength = conn.getContentLength();
            httpEntity.inputStream = conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpEntity;
    }

    /**
     * 检查是否是WIFI连接
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


}
