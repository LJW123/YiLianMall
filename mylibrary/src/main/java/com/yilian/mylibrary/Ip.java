package com.yilian.mylibrary;


import android.content.Context;
import android.text.TextUtils;

public class Ip {
    /**
     * 正式服 上线IP
     */
//    public static String BASE_PAY_URL = "https://app.i1170.com/";
//        ................................................................................................
    /**
     * 测试服 测试IP
     */
    public static String BASE_PAY_URL = "http://test.i1170.com/";
//            ................................................................................................
    /**
     * 2017.12.12 新增压测IP
     */
//    public static String BASE_PAY_URL = "https://b.i1170.com/";
    //        ................................................................................................
    /**
     * 静态web（/http下的）界面上传OSS地址
     */
    public static String BASE_DEFAULT_WEB_URL = "https://img.yilian.lefenmall.com/";
    //测试
//    public static String URL = BASE_PAY_URL + "app/";
    //线上
//    public static String URL = getURL(LibApplication.getContextObject()) + "app/";
//    public static String HELP_URL = BASE_DEFAULT_WEB_URL + "help/";
//    public static String WECHAT = BASE_PAY_URL + "wechat/";
    /**
     * 分享使用URL
     */
    public static final String SHARE_URL = "http://www.lefenmall.com/wechat/m/appShare.php?sign=0";

    /**
     * 获取baseUrl
     */
    public static String getBaseURL(Context context) {
        if (TextUtils.isEmpty(PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context))) {
            return BASE_PAY_URL;
        } else {
            return PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context);
        }
    }

    /**
     * 获取带app的url
     */
    public static String getURL(Context context) {
        if (TextUtils.isEmpty(PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context))) {
            return BASE_PAY_URL + "app/";
        } else {
            return PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context) + "app/";
        }
    }

    public static String getJavaUrl(Context context) {
        if (TextUtils.isEmpty(PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context))) {
            return BASE_PAY_URL + "npm/api/";
        } else {
            return PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context) + "npm/api/";
        }
    }


    /**
     * 获取带wechat的url
     */
    public static String getWechatURL(Context context) {
        if (TextUtils.isEmpty(PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context))) {
            return BASE_PAY_URL + "wechat/";
        } else {
            return PreferenceUtils.readStrConfig(Constants.LANGUAGE_PACKAGE_IP, context) + "wechat/";
        }
    }

    /**
     * 获取带help的url
     */
    public static String getHelpURL() {
        return BASE_DEFAULT_WEB_URL + "help/";
    }

    /**
     * 获取带file的url
     *
     * @return
     */
    public static String getFileUrl() {
        return BASE_DEFAULT_WEB_URL + "file/";
    }
}


