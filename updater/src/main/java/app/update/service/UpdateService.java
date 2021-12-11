package app.update.service;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.update.define.AnalyzeCallBack;
import app.update.model.VersionInfo;
import app.update.utils.HttpEntity;
import app.update.utils.HttpUtils;
import app.update.utils.L;

/**
 * Created by banxinyu on 2016/10/14.
 */
public class UpdateService implements IUpdate {

    @Override
    public void getVersionInfo(final String url, final AnalyzeCallBack<VersionInfo> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpEntity httpEntity = HttpUtils.get(url);
                if (httpEntity.code == 200) {
                    try {
                        VersionInfo info = new VersionInfo();
                        JSONObject object = new JSONObject(httpEntity.entityToString());
                        if(object.getInt("code") == 0 && object.getInt("status") == 1){
                            //发现新版本
                            info.setUpdate(true);
                            JSONObject data = object.getJSONObject("data");
                            info.setAppName(data.getString("app_name"));
                            info.setPackageName(data.getString("package_name"));
                            info.setVersionCode(data.getInt("version_code"));
                            info.setVersionName(data.getString("version_name"));
                            info.setUpdateInfo(data.getString("update_info"));
                            info.setChannelId(data.getString("channel_id"));
                            info.setApkSize((float) data.getDouble("apk_size"));
                            info.setDownloadURL(data.getString("download_url"));
                            info.setIsForce(data.getInt("isforce") == 1);
                            callback.onAnalyzeCallBack(info);
                        }else{
                            L.d("暂无新版本!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onAnalyzeErrorCallBack(e);
                    }
                }else{
                    L.d("getVersionInfo Http Ex(code): "+httpEntity.code);
                }
            }
        }).start();
    }

    @Override
    public void downloadApk(final String url, final String savePath, final AnalyzeCallBack<Integer> analyzeCallBack) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpEntity httpEntity = HttpUtils.get(url);
                if (httpEntity.code == 200) {

                    InputStream inputStream = httpEntity.inputStream;
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(savePath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    byte[] buffer = new byte[2048];
                    long total = 0;
                    int len;
                    try {
                        while ((len = inputStream.read(buffer)) != -1) {
                            total += len;
                            outputStream.write(buffer, 0, len);
                            int progress = (int) (total * 100 / httpEntity.contentLength);

                            if(progress % 4 == 0){ //4秒更新一次通知,太频繁会影响下载速度
                                analyzeCallBack.onAnalyzeCallBack(progress);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        analyzeCallBack.onAnalyzeErrorCallBack(e);
                    } finally {
                        if (outputStream != null){
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

}
