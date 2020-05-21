package com.yilian.mylibrary;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PhoneInfor {
	
	/**
	 * 手机系统sdk版本
	 */
	private static int mSDKVersion = -1;

	/**
	 * app versionName
	 */
	private static String mAPPVersionName;

	/**
	 * app versionCode
	 */
	private static int mAPPVersionCode=-1;

	/**
	 * 手机IMEI
	 */
	private static String mIMEI;
	
	/**
	 * 手机IMSI
	 */
	private static String mIMSI;
	
	/**
	 * 操作系统名称
	 */
	private static String mOSVersion;
	
	/**
	 * 设备型号
	 */
	private static String mDeviceModel;
	
	/**
	 * 设备品牌
	 */
	private static String mDeviceBrand;
	
	/**
	 * cpu型号
	 */
	private static String mCpuModel;
	

	public static String getInfo(){

		return Build.MODEL;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static long getTotalMemory(Context c) {
		// memInfo.totalMem not supported in pre-Jelly Bean APIs.
			ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
			ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
			am.getMemoryInfo(memInfo);
				return memInfo.totalMem;
	}


	/**
	 *
	 * @description 获取系统版本号
	 * @return int
	 */
	public static int getSDKVersion() {
		if (mSDKVersion == -1) {
			mSDKVersion = Build.VERSION.SDK_INT;
		}
		return mSDKVersion;
	}

	/**
	 *
	 * @description 获取app versionName
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 *             String
	 */
	public static String getAppVersion(Context context)
			throws NameNotFoundException {
		if (TextUtils.isEmpty(mAPPVersionName)) {
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			mAPPVersionName = packInfo.versionName;
		}
		return mAPPVersionName;
	}

	/**
	 *
	 * @description:获取app versionCode
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 * int
	 */
	public static int getAPPVersionCode(Context context)
			throws NameNotFoundException {
		if (mAPPVersionCode == -1) {
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			mAPPVersionCode = packInfo.versionCode;
		}
		return mAPPVersionCode;
	}

    /**
     *
     * @description  获取手机IMEI Requires Permission: READ_PHONE_STATE
     * @param context
     * @return
     * String
     */
    public static String getIMEI(Context context) {
		if (TextUtils.isEmpty(mIMEI)) {
			mIMEI = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		}
		return mIMEI;
	}

    /**
     *
     * @description  获取手机IMSI Requires Permission: READ_PHONE_STATE
     * @param context
     * @return
     * String
     */
    public static String getIMSI(Context context) {
		if (TextUtils.isEmpty(mIMSI)) {
			mIMSI = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		}
		return mIMSI;
	}

    /**
     *
     * @description:获取手机系统版本
     * @param context
     * @return
     * String
     */
    public static String getOSVersion() {
 		if (TextUtils.isEmpty(mOSVersion)) {
 			mOSVersion = Build.VERSION.RELEASE;
 		}
 		return mOSVersion;
 	}

    /**
     *
     * @description:获取设备名称
     * @return
     * String
     */
    public static String getDeviceModel()
    {
    	if (TextUtils.isEmpty(mDeviceModel)) {
    		mDeviceModel = Build.MODEL;
 		}
 		return mDeviceModel;
    }

    /**
     *
     * @description:获取设备品牌
     * @return
     * String
     */
    public static String getDeviceBrand() {
    	if (TextUtils.isEmpty(mDeviceBrand)) {
    		mDeviceBrand = Build.BRAND;
 		}
 		return mDeviceBrand;
	}
    
    /**
     * 获取cpu型号
     * @return
     */
    public static String getCpuModel() {
    	if (TextUtils.isEmpty(mCpuModel)) {
    	    String model="";
    	    String[] arrayOfString;  
    	    try {  
    	    	//proc/cpuinfo文件中第一行是CPU的型号，第二行是CPU的频率，可以通过读文件，读取这些数据！
    	        FileReader fr = new FileReader("/proc/cpuinfo");  
    	        BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
    	        String str = localBufferedReader.readLine();  
    	        arrayOfString = str.split("\\s+");  
    	        for (int i = 2; i < arrayOfString.length; i++) {  
    	            model= model+arrayOfString[i] + " ";  
    	        }  
    	        localBufferedReader.close();  
    	    } catch (IOException e) {
    	    	
    	    }
    	    mCpuModel=model;
 		}
 		return mCpuModel;
	}
}