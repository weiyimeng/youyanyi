package app.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

import app.update.define.AnalyzeCallBack;
import app.update.model.VersionInfo;
import app.update.service.IUpdate;
import app.update.service.UpdateService;
import app.update.utils.HttpUtils;
import app.update.utils.L;
import app.update.widget.UpdateDialog;
import update.app.updatelib.R;

/**
 * Created by banxinyu on 2016/10/12.
 * 更新
 */
public class UpdateAgent {

    /**
     * 视图
     */
    private Context mContext;

    /**
     * 用来更新视图
     */
    private Handler mUiHandler;

    /**
     * 仅允许WIFI网络下更细
     */
    public boolean onlyWifi = false;

    /**
     * 更新的网络地址
     */
    public String mUpdateURL = "";

    /**
     * 临时Apk文件下载路径
     */
    public String mSavePath = "";

    /**
     * 安装Apk后清理Apk文件
     */
    public boolean cleanMode = false;

    /**
     * 应用程序图标
     */
    public int appIcon;

    /**
     * 通知ID
     */
    public int notification_id = 1010010;

    /**
     * 更新逻辑
     */
    private IUpdate mUpdateServer;

    /**
     * 通知栏相关
     */
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    public UpdateAgent(Context context) {
        this.mContext = context;
        this.mUpdateServer = new UpdateService();
        this.mUiHandler = new Handler(this.mContext.getMainLooper());
    }

    public UpdateAgent setUpdateOnlyWifi(boolean onlyWifi) {
        this.onlyWifi = onlyWifi;
        return this;
    }

    public UpdateAgent setUpdateURL(String updateURL) {
        this.mUpdateURL = updateURL;
        return this;
    }

    public UpdateAgent setCleanMode(boolean cleanMode) {
        this.cleanMode = cleanMode;
        return this;
    }

    public UpdateAgent setSavePath(String path) {
        this.mSavePath = path;
        return this;
    }

    public UpdateAgent setAppIcon(int resource) {
        this.appIcon = resource;
        return this;
    }

    /**
     * 初始化更新操作
     */
    private void initialization(){
     File f = new File(mSavePath);
        if (f.isFile() && f.exists()){
            f.delete();
        }
    }

    /**
     * 更新包下载完成释放资源
     */
    private void onDestoryUpdate() {
        this.mUiHandler = null;
        this.mContext = null;
        this.mNotificationManager.cancel(notification_id);
    }

    /**
     * 调用这个函数开启更新功能
     */
    public void update() {
        if (onlyWifi && !HttpUtils.isWifi(mContext)) {
            return;
        }

        initialization();
        this.mUpdateServer.getVersionInfo(this.mUpdateURL, new AnalyzeCallBack<VersionInfo>() {

            @Override
            public void onAnalyzeCallBack(final VersionInfo versionInfo) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (versionInfo.isUpdate()) {
                            final UpdateDialog dialog = new UpdateDialog(mContext);
                            dialog.setMessage(versionInfo.getUpdateInfo());
                            dialog.setCancelable(false);
                            dialog.setForce(versionInfo.isForce());
                            dialog.setCancelButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            dialog.setUpgradeButton("升级", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    //显示通知,显示下载进度
                                    Toast.makeText(mContext, mContext.getString(R.string.downloading), Toast.LENGTH_LONG).show();
                                    showDownloadNotify();

                                    startDownload(versionInfo.getDownloadURL(), true);
                                }
                            });
                            dialog.show();
                        }
                    }
                });
            }

            @Override
            public void onAnalyzeErrorCallBack(Exception e) {
                L.d("版本更新发生异常: " + e.getMessage());
            }

        });
    }

    /**
     * 调用这个函数开始下载Apk文件
     *
     * @param apkDownloadURL
     */
    public void startDownload(String apkDownloadURL, final boolean autoInstall) {
        final String percent = mContext.getString(R.string.percent);
        final String prefixDownload = mContext.getString(R.string.prefix_download);

        this.mUpdateServer.downloadApk(apkDownloadURL, this.mSavePath, new AnalyzeCallBack<Integer>() {
            @Override
            public void onAnalyzeCallBack(final Integer progress) {
                mBuilder.setContentTitle(prefixDownload + progress + percent);
                mBuilder.setProgress(100, progress, false);
                mNotificationManager.notify(notification_id, mBuilder.build());

                if (progress == 100 && autoInstall) {
                    Uri uri = Uri.fromFile(new File(mSavePath));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                    //所有操作结束
                    onDestoryUpdate();
                }
            }

            @Override
            public void onAnalyzeErrorCallBack(Exception e) {
                L.d("下载出现错误: " + e.toString());
            }
        });
    }

    public void showDownloadNotify() {
        mBuilder = new NotificationCompat.Builder(mContext)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setSmallIcon(this.appIcon)
                .setContentTitle(mContext.getString(R.string.prefix_download) + 0 + mContext.getString(R.string.percent))
                .setProgress(100, 0, false);
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notification_id, mBuilder.build());
    }

}
