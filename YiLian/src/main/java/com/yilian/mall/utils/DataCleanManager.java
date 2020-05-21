package com.yilian.mall.utils;

import android.content.Context;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;

public class DataCleanManager {
	
	 /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param mContext */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param mContext */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases/db"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * mContext
     */
    public static void cleanSharedPreference(Context context) {
        Logger.i("tag", "cleanSharedPreference");
        deleteFilesByDirectory(new File("/data/data/com.yilian.mall/shared_prefs.xml"));
    }

    /** * 按名字清除本应用数据库 * * @param mContext * @param dbName */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param mContext */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * mContext
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /** * 清除本应用所有的数据 * * @param mContext * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /** 
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件夹，将不做处理 
     * @param directory 
     * */
    private static void deleteFilesByDirectory(File directory) {
        Logger.i("tag", "delete - deleteFilesByDirectory");
        if (directory != null && directory.exists() && directory.isDirectory()) {
            Logger.i("tag", "delete - if" + directory);
            for (File item : directory.listFiles()) {
                Logger.i("tag", "delete - for " + item.getName());
                if (item.getName().equals("express")) {
                    Logger.i("tag", "delete - for " + item.getName());
                    item.delete();
				}
            	
            }
        }
    }

}
