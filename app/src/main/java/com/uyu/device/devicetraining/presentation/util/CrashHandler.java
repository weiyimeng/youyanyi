package com.uyu.device.devicetraining.presentation.util;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.content.CrashInfo;
import com.uyu.device.devicetraining.data.net.api.ApiService;
import com.uyu.device.devicetraining.data.net.api.ServiceFactory;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by banzhenyu on 2016/01/30
 * 捕获未处理的异常
 */

public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    private ApiService apiService;

    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * CrashHandler实例
     */
    private static CrashHandler instance = new CrashHandler();

    /**
     * App
     */
    private Context mContext;

    private CrashHandler() {
        apiService = ServiceFactory.create(ServiceFactory.TYPE_OBJECT);
    }

    /**
     * 懒汉
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    /**
     * 注册一个异常的追踪者
     *
     * @param ctx
     */
    public void regiestExceptionTracker(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    /**
     * 上传错误日志
     * @param content
     */
    private void uploadLog(String content){
        String tk = SharePreferenceTool.getSharePreferenceValue(mContext,SharePreferenceTool.PREF_DEVICE_TOKEN);
        String deviceId = SharePreferenceTool.getSharePreferenceValue(mContext,SharePreferenceTool.PREF_DEVICE_ID);
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(mContext.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        CrashInfo crashInfo = new CrashInfo();
        crashInfo.device_id = Integer.valueOf(deviceId);
        crashInfo.content = content;
        if(info != null) {
            crashInfo.version_code = info.versionCode;
            crashInfo.version_name = info.versionName;
            apiService.uploadLog(tk,crashInfo)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ModelApiResult<CrashInfo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("aaa","===e===="+e.toString());
                        }

                        @Override
                        public void onNext(ModelApiResult<CrashInfo> crashInfoModelApiResult) {
                            Log.d("aaa","====code====="+crashInfoModelApiResult.getCode());
                        }
                    });
        }
    }

    /**
     * 处理未捕获的异常
     *
     * @param thread 当前线程
     * @param ex     异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("<html><body>");
            sb.append("<h1 style=\"font-family:KaiTi;text-align:center;vertical-align:middle;width:100%;height:120px;background-color:#009688;color:white;font-size:60px;\">ERROR</h1>");
            sb.append("<h2>User data</h2>");//http://chris.banes.me/content/images/2014/11/materialdesign-goals-swirlanddot_large_xhdpi.png
            sb.append("<div>");
            sb.append("Android版本号:" + Build.VERSION.SDK_INT + "<br>");
            sb.append("手机厂商:" + Build.MANUFACTURER + "<br>");
            sb.append("手机型号:" + ":" + Build.MODEL + "<br>");
            TelephonyManager phoneMgr = (TelephonyManager) (mContext.getSystemService(Context.TELEPHONY_SERVICE));
            //sb.append("手机号码: " + phoneMgr.getLine1Number() + "<br>");
            sb.append("国际识别码: " + phoneMgr.getDeviceId() + "<br>");
            sb.append("</div>");
            sb.append("<h2>内存信息</h2>");
            sb.append("<div>");
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            am.getMemoryInfo(memoryInfo);
            Field[] fields1 = ActivityManager.MemoryInfo.class.getDeclaredFields();
            for (Field field : fields1) {
                try {
                    field.setAccessible(true);
                    sb.append(field.getName().toLowerCase(Locale.US) + ":\t" + field.get(memoryInfo).toString());
                    sb.append("<br>");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sb.append("</div>");
            sb.append("<h2>系统信息</h2>");
            sb.append("<div>");
            Field[] field2 = Build.class.getDeclaredFields();
            for (Field field : field2) {
                try {
                    field.setAccessible(true);
                    sb.append(field.getName().toLowerCase(Locale.US) + ":\t" + field.get(null).toString());
                    sb.append("<br>");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sb.append("</div>");
            sb.append("<h2>线程信息</h2>");
            sb.append("<div>");
            sb.append("Thread ID: " + thread.getId());
            sb.append("Thread name : " + thread.getName());
            sb.append("Thread SimpleName : " + thread.getClass().getSimpleName());
            sb.append("</div>");

            sb.append("<h2>异常信息</h2>");
            sb.append("<div>");
            sb.append(ThrowableUtils.getExceptionBody(ex));
            sb.append("</div>");
            sb.append("</body></html>");

            uploadLog(sb.toString());

            //保留保存本地文件的，防止网络不好，丢失
            //save exception info 2 file
            File file = new File(getCrash());
            FileWriter writer = null;
            try {
                if (file != null) {
                    writer = new FileWriter(file);
                    writer.write(sb.toString());
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //重新打开主视图
        Intent intent = new Intent(mContext, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        Process.killProcess(Process.myPid());
    }


    /**
     * 获取一个存放崩溃日志的路径
     * 如果错误日志数量大于100个将会清除所有文件
     *
     * @return 本次崩溃日志保存的文件路径
     */
    private String getCrash() {
        StringBuilder builder = new StringBuilder();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File canWriteDirectory = Environment.getExternalStorageDirectory();
            if(!canWriteDirectory.canWrite()){
                canWriteDirectory =mContext .getExternalFilesDir(null);
            }
            builder.append(canWriteDirectory + "/.crash/");


        }
        File file = new File(builder.toString());
        Log.d("canWriteDirectory",builder.toString());
        if (!file.exists()) {
            file.mkdirs();
        } else {
            if (file.list().length > 100) {
                //clear crashs
                final File files[] = file.listFiles();
                for (int i = files.length - 1; i > -1; i--) {
                    files[i].delete();
                }
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            builder.append(formatter.format(curDate));
            builder.append(".html");
        }
        return builder.toString();
    }
}
