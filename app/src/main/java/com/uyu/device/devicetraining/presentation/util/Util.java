package com.uyu.device.devicetraining.presentation.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.uyu.device.devicetraining.presentation.model.VisionCardLevel;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import timber.log.Timber;

/**
 * Created by windern on 2015/12/8.
 */
public class Util {
	/**
	 * pad是不是特殊的
	 */
	private static boolean isSpecial = false;

	/**
	 * 300px对应mm数-迅为
	 */
	private static float px300tomm_Xunwei = 57.42f;

	/**
	 * 300px对应mm数-菜心
	 */
	private static float px300tomm_Caixin = 47.75f;

	/**
	 * 300px对应mm数
	 */
	private static float px300tomm = px300tomm_Caixin;

	/**
	 * 像素标准-300
	 */
	private static float pxStandard300 = 300;

	/**
	 * 获取手机/pad唯一编码
	 * 现在用的是可打电话的imei
	 * @param context
	 * @return
     */
	public static String getDuid(Context context){
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager)context.getSystemService( Context.TELEPHONY_SERVICE );

		/*
		 * getDeviceId() function Returns the unique device ID.
		 * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
		 */
		String imeistring = telephonyManager.getDeviceId();
		return imeistring;
	}

	/**
	 * 得到本机ip地址
	 */
	public static String getLocalHostIp()
	{
		String ipaddress = "";
		try
		{
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements())
			{
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements())
				{
					InetAddress ip = inet.nextElement();
//                    if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress()))
					if (!ip.isLoopbackAddress())
					{
						ipaddress = ip.getHostAddress();
						return ipaddress;
					}
				}

			}
		}
		catch (SocketException e)
		{
			Timber.e("获取本地ip地址失败");
			e.printStackTrace();
		}
		return ipaddress;

	}

	/**
	 * 是否联网
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 视力卡级别
	 */
	public static VisionCardLevel[] arrayVisionCardLevels = {
//		new VisionCardLevel("1级",40,6),
//		new VisionCardLevel("2级",35,9),
//		new VisionCardLevel("3级",30,12),
//		new VisionCardLevel("4级",25,15),
//		new VisionCardLevel("5级",20,18),
//		new VisionCardLevel("6级",15,21),
//		new VisionCardLevel("7级",10,24),
//		new VisionCardLevel("8级",9,27),
//		new VisionCardLevel("9级",8,30),
//		new VisionCardLevel("10级",7,35),
//		new VisionCardLevel("11级",6,40),
//		new VisionCardLevel("12级",5,45),
//		new VisionCardLevel("13级",4.5f,50),
//		new VisionCardLevel("14级",4,55),
//		new VisionCardLevel("15级",3.5f,60),
//		new VisionCardLevel("16级",3,65),
//		new VisionCardLevel("17级",2.5f,70),
//		new VisionCardLevel("18级",2,75),
//		new VisionCardLevel("19级",1.5f,80),
//		new VisionCardLevel("20级",1.2f,85),
//		new VisionCardLevel("21级",1,90)
			new VisionCardLevel("1级",25F),
			new VisionCardLevel("2级",20F),
			new VisionCardLevel("3级",15F),
			new VisionCardLevel("4级",10F),
			new VisionCardLevel("5级",9F),
			new VisionCardLevel("6级",8F),
			new VisionCardLevel("7级",7F),
			new VisionCardLevel("8级",6F),
			new VisionCardLevel("9级",5F),
			new VisionCardLevel("10级",4.5F),
			new VisionCardLevel("11级",4.0F),
			new VisionCardLevel("12级",3.5F),
			new VisionCardLevel("13级",3.0f),
			new VisionCardLevel("14级",2.5F),
			new VisionCardLevel("15级",2.0F),
			new VisionCardLevel("16级",1.5F)
	};

	/**
	 * 单个随机字符的列表
	 */
	private static ArrayList<String> arrayListRandomLetter = null;
	/**
	 * 单个随机字符的列表
	 */
	private static ArrayList<String> arrayListRandomNumber = null;
	/**
	 * 获取随机字符
	 *
	 * @param textLength
	 *            随机字符的长度
	 * @param spaceCount
	 *            中间空格的个数
	 * @return
	 */
	public static String getRandomText(int textLength, int spaceCount) {
		String splitStr = "";
		int hasCount = 0;
		while (hasCount < spaceCount) {
			splitStr += " ";
			hasCount++;
		}
		String randomText = "";
		// List<String> listRandomText = new ArrayList<String>();
		for (int i = 0; i < textLength; i++) {
			// listRandomText.add(getOneRandomText());
			randomText += getOneRandomText();
			if (i < textLength - 1) {
				randomText += splitStr;
			}
		}
		return randomText;
	}

	/**
	 * 获取随机字符
	 *
	 * @param textLength
	 *            随机字符的长度
	 * @param spaceCount
	 *            中间空格的个数
	 * @param everytextLength
	 *            每个字符串的字母数
	 * @return
	 */
	public static String getRandomText(int textLength, int spaceCount,
									   int everytextLength) {
		String splitStr = "";
		int hasCount = 0;
		while (hasCount < spaceCount) {
			splitStr += " ";
			hasCount++;
		}
		String randomText = "";
		for (int i = 0; i < textLength; i++) {
			randomText += getOneRandomText(everytextLength);
			if (i < textLength - 1) {
				randomText += splitStr;
			}
		}
		return randomText;
	}

	/**
	 * 获取一个随机字符
	 *
	 * @param length
	 *            ，字符的长度
	 * @return
	 */
	public static String getOneRandomText(int length) {
		ArrayList<String> arrayList = getListRandomLetter();
		int randomLength = arrayList.size();
		String str = "";
		int randomIndex = 0;
		for (int i = 0; i < length; i++) {
			randomIndex = (int) (Math.random() * randomLength);
			str += arrayList.get(randomIndex);
		}
		return str;
	}

	/**
	 * 获取一个随机字符
	 *
	 * @return
	 */
	public static String getOneRandomText() {
		ArrayList<String> arrayList = getListRandomLetter();
		int randomLength = arrayList.size();
		int randomIndex = (int) (Math.random() * randomLength);
		return arrayList.get(randomIndex);
	}

	/**
	 * 获取单个随机字符的列表
	 *
	 * @return
	 */
	public static ArrayList<String> getListRandomLetter() {
		if (arrayListRandomLetter == null) {
			String whole = "abcdefghijklmnopqrstuvwxyz";
			String upperWhole = whole.toUpperCase();
			String[] arrayWhole = upperWhole.split("");
			List<String> list = Arrays.asList(arrayWhole);
			arrayListRandomLetter = new ArrayList<String>(list);
			arrayListRandomLetter.remove(0);
		}
		return arrayListRandomLetter;
	}
	/**
	 * 获取单个随机数字的列表
	 *
	 * @return
	 */
	public static ArrayList<String> getListRandomNumber() {
		if (arrayListRandomNumber == null) {
			String whole = "0123456789";
			String upperWhole = whole.toUpperCase();
			String[] arrayWhole = upperWhole.split("");
			List<String> list = Arrays.asList(arrayWhole);
			arrayListRandomNumber = new ArrayList<String>(list);
			arrayListRandomNumber.remove(0);
		}
		return arrayListRandomNumber;
	}

	/**
	 * 获取一个数字
	 *
	 * @param length
	 *            ，字符的长度
	 * @return
	 */
	public static String getOneRandomNumber(int length) {
		ArrayList<String> arrayList = getListRandomNumber();
		int randomLength = arrayList.size();
		String str = "";
		int randomIndex = 0;
		for (int i = 0; i < length; i++) {
			randomIndex = (int) (Math.random() * randomLength);
			str += arrayList.get(randomIndex);
		}
		return str;
	}

	/**
	 * 获取随机不重复的数字，从0开始，不包含最大数
	 * @param length 长度
	 * @param maxNum 最大数
	 * @return
	 */
	public static HashSet<Integer> getRandomNumNoRepeat(int length,int maxNum){
		HashSet<Integer> hashSet = new HashSet<Integer>();
		Random random = new Random();
		while(hashSet.size()<length){
			int randomInt = random.nextInt(maxNum);
			if(!hashSet.contains(randomInt)){
				hashSet.add(randomInt);
			}
		}
		return hashSet;
	}

	/**
	 * mm转成px
	 * @param mmvalue
	 * @param context
	 * @return
	 */
	public static float convertpx(float mmvalue,Context context){
		float px = mmvalue/px300tomm*pxStandard300;
		return px;
	}

	/**
	 * px转成mm
	 * @param pxvalue
	 * @param context
	 * @return
	 */
	public static float convertmm(float pxvalue,Context context){
		float mm = pxvalue/pxStandard300*px300tomm;
		return mm;
	}

	/**
	 * mm转成px
	 * @param mmvalue
	 * @param context
	 * @return
	 */
	private static float convertpxNormal(float mmvalue,Context context){
		Resources r;

		if (context == null)
			r = Resources.getSystem();
		else
			r = context.getResources();

//		DisplayMetrics metrics = r.getDisplayMetrics();
//		float cx = mmvalue * metrics.xdpi * (1.0f/25.4f);
//		float cxd = mmvalue * metrics.density * (1.0f/25.4f);
//		float cxdd = mmvalue * metrics.densityDpi * (1.0f/25.4f);
//		Timber.d("cx:%s",cx);

		float pxvalue = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_MM, mmvalue, r.getDisplayMetrics());

		return pxvalue;
	}

	/**
	 * px转成mm
	 * @param pxvalue
	 * @param context
	 * @return
	 */
	private static float convertmmNormal(float pxvalue,Context context){
		float onemm_px = convertpx(1,context);
		float mmvalue = pxvalue/onemm_px;
		return mmvalue;
	}

	/**
	 * mm转成px
	 * @param mmvalue
	 * @param context
	 * @return
	 */
	private static float convertpxInPad(float mmvalue,Context context){
		float pxvalue = mmvalue*930/178;

		return pxvalue;
	}

	/**
	 * px转成mm
	 * @param pxvalue
	 * @param context
	 * @return
	 */
	private static float convertmmInPad(float pxvalue,Context context){
		float mmvalue = pxvalue*178/930;
		return mmvalue;
	}

	/**
	 * 获取图片
	 * @param imageName
	 * @param widthmm
	 * @param heightmm
	 * @param context
	 * @return
	 */
	public static Bitmap getBitmap(String imageName,float widthmm,float heightmm,Context context){
		Resources resources = context.getResources();

		float mm2in = 25.4f;

		// 图片显示物理尺寸，长度单位是mm
		float pic_x_mm = widthmm;
		float pic_y_mm = heightmm;

		DisplayMetrics dm = resources.getDisplayMetrics();

		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;

		float pic_x_change_px =  pic_x_mm * xdpi * (1.0f/25.4f);
		float pic_y_change_px =  pic_y_mm * ydpi * (1.0f/25.4f);

		ApplicationInfo appInfo = context.getApplicationInfo();
		int resID = resources.getIdentifier(imageName, "drawable",
				appInfo.packageName);
		Bitmap bm = BitmapFactory.decodeResource(resources, resID);

		int bmpWidth = bm.getWidth();
		int bmpHeight = bm.getHeight();
		/* 计算这次要放大的比例 */
		float scaleWidth = pic_x_change_px / bmpWidth;
		float scaleHeight = pic_y_change_px / bmpHeight;
		/* 产生reSize后的Bitmap对象 */
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizeBm = Bitmap.createBitmap(bm, 0, 0, bmpWidth, bmpHeight,
				matrix, true);

		return resizeBm;
	}
}
