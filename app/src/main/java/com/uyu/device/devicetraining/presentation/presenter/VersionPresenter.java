package com.uyu.device.devicetraining.presentation.presenter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;

import com.google.gson.Gson;
import com.uyu.device.devicetraining.R;
import com.uyu.device.devicetraining.data.entity.DeviceInfo;
import com.uyu.device.devicetraining.data.entity.ModelApiResult;
import com.uyu.device.devicetraining.data.entity.other.HardwareVersion;
import com.uyu.device.devicetraining.data.entity.other.SoftwareVersion;
import com.uyu.device.devicetraining.data.net.api.ServiceConfig;
import com.uyu.device.devicetraining.data.repository.sharepreference.SharePreferenceTool;
import com.uyu.device.devicetraining.domain.interactor.LoginUseCase;
import com.uyu.device.devicetraining.presentation.internal.di.PerActivity;
import com.uyu.device.devicetraining.presentation.util.ToastUtil;
import com.uyu.device.devicetraining.presentation.view.activity.WelcomeActivity;
import com.uyu.device.devicetraining.presentation.view.widget.ShowUpgradeView;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by windern on 2016/5/12.
 */
@PerActivity
public class VersionPresenter {
    public interface VersionPresenterListener{
        void installSoftware(File file);
        void silentInstallSoftware(File file);
        void showUpgradeView(int status);
    }

    private VersionPresenterListener mListener;

    public void setmListener(VersionPresenterListener mListener) {
        this.mListener = mListener;
    }

    private final LoginUseCase loginUseCase;
    private final Context context;

    /**
     * 联网失效时间
     */
    public static final int TIMEOUT = 10000;
    private final static String SoftwareDownloadFileName = "DeviceTraining_update.apk";
    private final static String HardwareDownloadFileName = "DeviceHardware_update.ubin";

    private NotificationManager notificationManager;
    private int notification_id = 0;
    private PendingIntent pendingIntent;

    @Inject
    public VersionPresenter(@Named("loginUseCase") LoginUseCase loginUseCase, Context context) {
        this.loginUseCase = loginUseCase;
        this.context = context;
    }

    /**
     * 检查版本
     */
    public void checkVersion(){
        checkSoftwareVersion();
//        checkHardwareVersion();
    }

    public void checkSoftwareVersion(String downloadUrl,int versionCode){
        try {
            if(versionCode!=0) {
                int localVersionCode = getVersionCode();
                if (localVersionCode < versionCode) {
                    updateSoftware(downloadUrl,versionCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新软件
     */
    public void updateSoftware(String downloadUrl,int versionCode) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLongToast(context,"SD卡不可用，没法下载新的安装包，请手动下载");
        }else{
            mListener.showUpgradeView(1);
            Observable.just(1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .flatMap(deviceEntity -> {
                        String softwareLocalDownloadPath = Environment.getExternalStorageDirectory()
                                + File.separator + SoftwareDownloadFileName;
                        try {
                            File file = getFileFromServer(downloadUrl,softwareLocalDownloadPath);
                            return Observable.just(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Observable.error(new Exception("下载失败"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        // 弹出通知提示下载完成
                        notificationBuilder.setContentText("软件下载完成").setProgress(0, 0, false);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");

                        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                        notificationBuilder.setContentIntent(pendingIntent);
                        notificationManager.notify(0, notificationBuilder.build());

                        mListener.showUpgradeView(0);
                        installSofware(file);
                    }, throwable -> {
                        mListener.showUpgradeView(0);
                        Timber.d("updateSoftware fail:%s", throwable.getMessage());
                    });
        }
    }


    /**
     * 检查软件版本
     */
    public void checkSoftwareVersion(){
        String token = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_TOKEN);
        loginUseCase.getNowSoftwareVersion(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->{
                    if(result.getCode()==0){
                        SoftwareVersion softwareVersion = result.getData();
                        try {
                            if(softwareVersion!=null) {
                                int localVersionCode = getVersionCode();
                                if (localVersionCode < softwareVersion.getVersionCode()) {
                                    updateSoftware(softwareVersion);
                                }
                            }
                        }catch (Exception e){
                            //网络异常，需要重新连接，隔1s尝试重连
                            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(aLong -> {
                                        checkSoftwareVersion();
                                    });
                        }
                    }else{
                        //网络异常，需要重新连接，隔1s尝试重连
                        Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    checkSoftwareVersion();
                                });
                    }
                },throwable -> {
                    //网络异常，需要重新连接，隔1s尝试重连
                    Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                checkSoftwareVersion();
                            });
                });
    }

    /**
     * 更新软件
     * @param softwareVersion
     */
    public void updateSoftware(SoftwareVersion softwareVersion) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLongToast(context,"SD卡不可用，没法下载新的安装包，请手动下载");
        }else{
            mListener.showUpgradeView(1);
            Observable.just(1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .flatMap(deviceEntity -> {
                        String softwareLocalDownloadPath = Environment.getExternalStorageDirectory()
                                + File.separator + SoftwareDownloadFileName;
                        String downloadUrl = ServiceConfig.DOMAIN + "/" + softwareVersion.getPath();
                        try {
                            File file = getFileFromServer(downloadUrl,softwareLocalDownloadPath);
                            return Observable.just(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Observable.error(new Exception("下载失败"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        // 弹出通知提示下载完成
                        notificationBuilder.setContentText("软件下载完成").setProgress(0, 0, false);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");

                        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                        notificationBuilder.setContentIntent(pendingIntent);
                        notificationManager.notify(0, notificationBuilder.build());

                        installSofware(file);
                        mListener.showUpgradeView(0);
                    }, throwable -> {
                        mListener.showUpgradeView(0);
                        Timber.d("updateSoftware fail:%s", throwable.getMessage());
                    });
        }
    }

    /**
     * 安装apk
     *
     * @param file
     */
    protected void installSofware(File file) {
        Process process = null;
        OutputStream os = null;
        if(mListener!=null){
            try {
                process = Runtime.getRuntime().exec("su");
                os = process.getOutputStream();
                os.write("exit\n".getBytes());
                os.flush();
                int value = process.waitFor();
                if(value == 0){// 拥有root权限
                    // 静默安装
                    mListener.silentInstallSoftware(file);
                }else{// 没有root权限
                    // 普通安装
                    mListener.installSoftware(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(os != null){
                        os.close();
                    }
                    process.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 检查硬件版本
     */
    public void checkHardwareVersion(){
        int hardwareVersionCode = 1;
        String token = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_TOKEN);
        loginUseCase.getNowHardwareVersion(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->{
                    if(result.getCode()==0){
                        HardwareVersion hardwareVersion = result.getData();
                        try {
                            if(hardwareVersion!=null) {
                                if (hardwareVersionCode < hardwareVersion.getVersionCode()) {
                                    updateHardware(hardwareVersion);
                                }
                            }
                        }catch (Exception e){
                            checkHardwareVersion();
                        }
                    }else{
                        checkHardwareVersion();
                    }
                },throwable -> {
                    checkHardwareVersion();
                });
    }

    /**
     * 更新软件
     * @param hardwareVersion
     */
    public void updateHardware(HardwareVersion hardwareVersion) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLongToast(context,"SD卡不可用，没法下载新的安装包，请手动下载");
        }else{
            Observable.just(1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .flatMap(deviceEntity -> {
                        String hardwareLocalDownloadPath = Environment.getExternalStorageDirectory()
                                + File.separator + HardwareDownloadFileName;
                        String downloadUrl = ServiceConfig.DOMAIN + "/" + hardwareVersion.getPath();
                        try {
                            File file = getFileFromServer(downloadUrl,hardwareLocalDownloadPath);
                            return Observable.just(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Observable.error(new Exception("下载失败"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        // 弹出通知提示下载完成
                        notificationBuilder.setContentText("下载完成").setProgress(0, 0, false);
                        notificationManager.notify(notification_id,
                                notificationBuilder.build());

                        installHardware(file);
                    }, throwable -> {
                        Timber.d("updateSoftware fail:%s", throwable.getMessage());
                    });
        }
    }

    /**
     * 安装硬件软件
     * @param file
     */
    protected void installHardware(File file) {
        Timber.d("安装硬件软件");
    }

    private Notification.Builder notificationBuilder;

    public File getFileFromServer(String downloadUrl,String localPath) throws Exception {
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(
                        context.getString(R.string.app_name) + "-" + "新版本下载提醒")
                .setContentText("新版本下载中。。。");
        notificationBuilder.setAutoCancel(true);

        int down_step = 5;// 提示step
        int totalSize;// 文件总大小
        int updateCount = 0;// 已经上传的文件大小

        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            //设置为true时，本地正常ngnix不正常，设置成false就可以了
            conn.setDoOutput(false);

            totalSize = conn.getContentLength();
            // 获取到文件的大小
            // pd.setMax(totalSize);
            InputStream is = conn.getInputStream();
            File file = new File(localPath);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;

                /**
                 * 每次增张5%
                 */
                if (updateCount == 0
                        || (total * 100 / totalSize - down_step) >= updateCount) {
                    updateCount += down_step;
                    // 改变通知栏
                    notificationBuilder.setContentText(
                            "新版本下载中。。。" + String.valueOf(updateCount) + "%")
                            .setProgress(100, updateCount, false);
                    notificationManager.notify(notification_id,
                            notificationBuilder.build());
                }
            }

            fos.close();
            bis.close();
            is.close();

            return file;
        } else {
            return null;
        }
    }

    /**
     * 获取当前程序的版本号
     *
     * @return
     * @throws Exception
     */
    public int getVersionCode() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        int localVersionCode = packInfo.versionCode;
        return localVersionCode;
    }

    public void updateDeviceInfo(){
        String deviceId = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_ID);
        String token = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_TOKEN);
        String deviceActualInfo = SharePreferenceTool.getSharePreferenceValue(context,SharePreferenceTool.PREF_DEVICE_ACTUAL_INFO);
        Gson gson = new Gson();
        DeviceInfo deviceInfoPref = gson.fromJson(deviceActualInfo,DeviceInfo.class);
    }
}
