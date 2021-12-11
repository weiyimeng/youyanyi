package app.update.service;

import app.update.define.AnalyzeCallBack;
import app.update.model.VersionInfo;

/**
 * Created by banxinyu on 2016/10/14.
 */
public interface IUpdate {

    /**
     * 获取服务器上最新apk的版本信息
     */
    void getVersionInfo(String url, AnalyzeCallBack<VersionInfo> analyzeCallBack);

    /**
     * 下载文件
     * @param url
     * @param analyzeCallBack
     */
    void downloadApk(String url,final String savePath,AnalyzeCallBack<Integer> analyzeCallBack);

}
