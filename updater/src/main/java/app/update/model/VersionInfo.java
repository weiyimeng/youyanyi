package app.update.model;

/**
 * Created by banxinyu on 2016/10/12.
 */
public class VersionInfo {

    /**
     * 主键(DB)
     */
    private int id;

    /**
     * 应用名
     */
    private String app_name;

    /**
     * 包名
     */
    private String package_name;

    /**
     * 版本号码
     */
    private int version_code;

    /**
     * 版本名
     */
    private String version_name;

    /**
     * 更新信息
     */
    private String update_info;

    /**
     * 渠道号(如果不存在多渠道打包传0即可)
     */
    private String channel_id;

    /**
     * 文件大小(M为单位)
     */
    private float apk_size;

    /**
     * 下载地址
     */
    private String download_url;

    /**
     * 是否强制更新
     */
    private boolean isforce;

    private boolean isUpdate;

    public VersionInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return app_name;
    }

    public void setAppName(String app_name) {
        this.app_name = app_name;
    }

    public String getPackageName() {
        return package_name;
    }

    public void setPackageName(String package_name) {
        this.package_name = package_name;
    }

    public int getVersionCode() {
        return version_code;
    }

    public void setVersionCode(int version_code) {
        this.version_code = version_code;
    }

    public String getVersionName() {
        return version_name;
    }

    public void setVersionName(String version_name) {
        this.version_name = version_name;
    }

    public String getUpdateInfo() {
        return update_info;
    }

    public void setUpdateInfo(String update_info) {
        this.update_info = update_info;
    }

    public String getChannelId() {
        return channel_id;
    }

    public void setChannelId(String channel_id) {
        this.channel_id = channel_id;
    }

    public float getApkSize() {
        return apk_size;
    }

    public void setApkSize(float apk_size) {
        this.apk_size = apk_size;
    }

    public String getDownloadURL() {
        return download_url;
    }

    public void setDownloadURL(String download_url) {
        this.download_url = download_url;
    }

    public boolean isForce() {
        return isforce;
    }

    public void setIsForce(boolean force) {
        this.isforce = force;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
