package app.update.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by banxinyu on 2016/10/17.
 */
public class PackageUtils {

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        String packageName = "";
        try {
            packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取应用程序图标
     * @param context
     * @return
     */
    public static Bitmap getIconBitmap(Context context) {
        Bitmap bitmap = null;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(getPackageName(context), 0);

            BitmapDrawable bd = (BitmapDrawable) info.loadIcon(pm);
            bitmap = bd.getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
