package com.yilian.mall.utils;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.LanguagePackageEntity;
import com.yilian.mylibrary.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ray_L_Pain on 2017/8/3 0003.\
 * 语言包的 下载 使用
 */

public class LanguagePackageUtils {

    public static void createFile(String url) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OutputStream output = null;
                try {
                    String pathName = Environment.getExternalStorageDirectory() + "/" + Constants.FILE_PATH + "/" + Constants.FILE_NAME; //文件储存路径
                    URL mURL = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
                    Logger.i("2017年3月1日 20:03:44---" + pathName);
                    File file = new File(pathName);
                    InputStream input = conn.getInputStream();
                    if (!file.exists()) {
                        String dir = Environment.getExternalStorageDirectory() + "/" + Constants.FILE_NAME;
                        new File(dir).mkdir();
                    }
                    output = new FileOutputStream(file);
                    //读取
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = input.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    output.close();
                    input.close();
                    Logger.i("---语言包---" + "下载---成功---");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public static String getFile() {
        try {
            //文件储存路径
            String pathName = Environment.getExternalStorageDirectory() + "/" + Constants.FILE_PATH + "/" + Constants.FILE_NAME;
            File file = new File(pathName);
            FileInputStream inStream = new FileInputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int len = 0;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            outStream.close();
            inStream.close();
            String str = new String(data).trim();
            Logger.i("---语言包---" + "获取---成功---");
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseJsonWithGson(String jsonStr, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonStr, type);
        return result;
    }

    public static String getIp(Context context, Gson gson) {
        return gson.fromJson(PreferenceUtils.getLanguaePakcage(context), LanguagePackageEntity.class).ip;
    }

    /**
     * 自定义名称文件下载
     * @param url
     * @param fileName
     */
    public static void createFile(String url,String fileName) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OutputStream output = null;
                try {
                    String pathName = Environment.getExternalStorageDirectory() + "/" + Constants.FILE_PATH + "/" +fileName; //文件储存路径
                    URL mURL = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
                    Logger.i("2017年3月1日 20:03:44---" + pathName);
                    File file = new File(pathName);
                    InputStream input = conn.getInputStream();
                    if (!file.exists()) {
                        String dir = Environment.getExternalStorageDirectory() + "/" +fileName;
                        new File(dir).mkdir();
                    }
                    output = new FileOutputStream(file);
                    //读取
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = input.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    output.close();
                    input.close();
                    Logger.i("---语言包---" + "下载---成功---");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public static String getFile(String fileName) {
        try {
            //文件储存路径
            String pathName = Environment.getExternalStorageDirectory() + "/" + Constants.FILE_PATH + "/" + fileName;
            File file = new File(pathName);
            FileInputStream inStream = new FileInputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int len = 0;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            outStream.close();
            inStream.close();
            String str = new String(data).trim();
            Logger.i("---语言包---" + "获取---成功---");
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
