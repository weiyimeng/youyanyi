package com.uyu.device.devicetraining.presentation.view.adapter.selfhelp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.uyu.device.devicetraining.data.config.AppConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class FileUtils {

    /**
     * @return
     */
    public static String getAppPath() {
        String appPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            appPath = Environment.getExternalStorageDirectory() + File.separator + AppConfig.ROOT_CACHE;
        } else {
            appPath = Environment.getDataDirectory() + File.separator + AppConfig.SYSTEM_CACHE;
        }
        File file = new File(appPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return appPath;
    }

    /**
     * @return
     */
    public static File getImageCache() {
        String imageCache = getAppPath() + File.separator + AppConfig.IMAGE_CACHE;
        File imageFile = new File(imageCache);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        return imageFile;

    }

    /**
     * 保存用户头像
     *
     * @param head
     */
    public static void setPicToView(Bitmap head) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream outputStream = null;
        String fileName = FileUtils.getImageCache().getAbsolutePath() + "head.jpg";//图片名字
        try {
            outputStream = new FileOutputStream(fileName);
            head.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 裁剪图片
     *
     * @param data
     */
    public static void cropPhoto(Uri data, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, AppConfig.CUT_PIC);
    }

    /**
     * 生成二维码图片
     *
     * @param message
     * @return
     */
    public static Bitmap bitMapGenerator(Context context, String message) {
        if (message == null) {
            throw new NullPointerException("message is not null");
        }
        Bitmap bitmap;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int windowWidth = windowManager.getDefaultDisplay().getWidth();
        int windowHeight = windowManager.getDefaultDisplay().getHeight();

        QRCodeWriter writer = new QRCodeWriter();
        try {
            int width = windowWidth / 2 + 10;
            int height = windowWidth / 2 + 50;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            //提高容错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            BitMatrix encode = writer.encode(message, BarcodeFormat.QR_CODE, width, height, hints);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (encode.get(i, j)) {
                        bitmap.setPixel(i, j, Color.BLACK);
                    } else {
                        bitmap.setPixel(i, j, Color.WHITE);
                    }
                }
            }
           /* Bitmap ic = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            Rect s = new Rect(0, 0, ic.getWidth(), ic.getHeight());

            Rect dst = new Rect(width/2,width/2,height/2,height/2);
            new Canvas(bitmap).drawBitmap(ic, s, dst, null);*/
            return bitmap;
        } catch (Exception e) {

        }
        return null;
    }


    public static int getWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int windowWidth = windowManager.getDefaultDisplay().getWidth();
        int windowHeight = windowManager.getDefaultDisplay().getHeight();
        return windowWidth;
    }
}