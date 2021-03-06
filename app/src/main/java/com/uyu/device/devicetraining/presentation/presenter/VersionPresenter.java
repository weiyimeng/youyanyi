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
     * ??????????????????
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
     * ????????????
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
     * ????????????
     */
    public void updateSoftware(String downloadUrl,int versionCode) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLongToast(context,"SD????????????????????????????????????????????????????????????");
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
                            return Observable.error(new Exception("????????????"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        // ??????????????????????????????
                        notificationBuilder.setContentText("??????????????????").setProgress(0, 0, false);
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
     * ??????????????????
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
                            //???????????????????????????????????????1s????????????
                            Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(aLong -> {
                                        checkSoftwareVersion();
                                    });
                        }
                    }else{
                        //???????????????????????????????????????1s????????????
                        Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    checkSoftwareVersion();
                                });
                    }
                },throwable -> {
                    //???????????????????????????????????????1s????????????
                    Observable.timer(ServiceConfig.RETRY_SECOND, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                checkSoftwareVersion();
                            });
                });
    }

    /**
     * ????????????
     * @param softwareVersion
     */
    public void updateSoftware(SoftwareVersion softwareVersion) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLongToast(context,"SD????????????????????????????????????????????????????????????");
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
                            return Observable.error(new Exception("????????????"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        // ??????????????????????????????
                        notificationBuilder.setContentText("??????????????????").setProgress(0, 0, false);
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
     * ??????apk
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
                if(value == 0){// ??????root??????
                    // ????????????
                    mListener.silentInstallSoftware(file);
                }else{// ??????root??????
                    // ????????????
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
     * ??????????????????
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
     * ????????????
     * @param hardwareVersion
     */
    public void updateHardware(HardwareVersion hardwareVersion) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showLongToast(context,"SD????????????????????????????????????????????????????????????");
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
                            return Observable.error(new Exception("????????????"));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        // ??????????????????????????????
                        notificationBuilder.setContentText("????????????").setProgress(0, 0, false);
                        notificationManager.notify(notification_id,
                                notificationBuilder.build());

                        installHardware(file);
                    }, throwable -> {
                        Timber.d("updateSoftware fail:%s", throwable.getMessage());
                    });
        }
    }

    /**
     * ??????????????????
     * @param file
     */
    protected void installHardware(File file) {
        Timber.d("??????????????????");
    }

    private Notification.Builder notificationBuilder;

    public File getFileFromServer(String downloadUrl,String localPath) throws Exception {
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(
                        context.getString(R.string.app_name) + "-" + "?????????????????????")
                .setContentText("???????????????????????????");
        notificationBuilder.setAutoCancel(true);

        int down_step = 5;// ??????step
        int totalSize;// ???????????????
        int updateCount = 0;// ???????????????????????????

        // ?????????????????????????????????sdcard????????????????????????????????????
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            //?????????true??????????????????ngnix?????????????????????false????????????
            conn.setDoOutput(false);

            totalSize = conn.getContentLength();
            // ????????????????????????
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
                 * ????????????5%
                 */
                if (updateCount == 0
                        || (total * 100 / totalSize - down_step) >= updateCount) {
                    updateCount += down_step;
                    // ???????????????
                    notificationBuilder.setContentText(
                            "???????????????????????????" + String.valueOf(updateCount) + "%")
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
     * ??????????????????????????????
     *
     * @return
     * @throws Exception
     */
    public int getVersionCode() throws Exception {
        // ??????packagemanager?????????
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()???????????????????????????0???????????????????????????
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
