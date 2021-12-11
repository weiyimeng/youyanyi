package app.update.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by banxinyu on 2016/10/14.
 */
public class HttpEntity {

    /**
     * 调用Api相应码
     */
    public int code;

    /**
     * 数据的长度
     */
    public int contentLength;

    /**
     * 返回值
     */
    public InputStream inputStream;

    public HttpEntity(int code,int contentLength, InputStream inputStream) {
        this.code = code;
        this.contentLength = contentLength;
        this.inputStream = inputStream;
    }

    /**
     * Http响应流转字符串
     * @return
     * @throws IOException
     */
    public String entityToString() throws IOException {
        if (code == HttpURLConnection.HTTP_OK){
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            String result = baos.toString();
            if (inputStream != null){
                inputStream.close();
            }
            return result;
        }
        return null;
    }

    /**
     * Http响应流保存为文件
     * @param filePath
     */
    public void entityToFile( HttpEntity entity,String filePath) throws IOException {
        if (entity.code == HttpURLConnection.HTTP_OK){
            FileOutputStream os = new FileOutputStream(filePath);
            byte[] buffer = new byte[2048];
            int len;
            while ((len = entity.inputStream.read(buffer)) > 0){
                os.write(buffer);
                os.flush();
            }
            os.close();
            if (inputStream != null){
                inputStream.close();
            }
        }
    }
}
