package com.yilian.mylibrary;

import android.content.Context;
import android.text.TextUtils;

public class RequestOftenKey {


    /**
     * 网络请求常用Key
     */

    public static String getDeviceIndex(Context context) {
//        Logger.i("getDeviceIndex  FFFFFF "+PreferenceUtils.readStrConfig(Constants.SPKEY_DEVICE_INDEX, context));
        return PreferenceUtils.readStrConfig(Constants.SPKEY_DEVICE_INDEX, context);
    }

    ;

    /**
     * 加盐值
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        String token = PreferenceUtils.readStrConfig(Constants.SPKEY_TOKEN, context, "0");
//        Logger.i("生成加盐token使用的token:"+token);
        if (TextUtils.isEmpty(token)) {//解决异常情况存储token为空字符串，导致转换为long类型错误问题 bugly上报地址：https://bugly.qq.com/v2/crash-reporting/crashes/ca5b40bb7f/4370?pid=1
            token = "0";
        }
        Long aLong = Long.valueOf(token);
//        Logger.i("返回的token1: "+aLong);
        if (aLong == 0L) {//如果不加盐值的token为0时，则返回0，不再增加盐值
            return "0";
        }
        String salt = PreferenceUtils.readStrConfig(Constants.SPKEY_SERVER_SALT, context,"0");
//        Logger.i("获取token时使用的salt:"+salt);
        String s = String.valueOf(aLong + Long.valueOf(salt));

//        Logger.i("返回的token2: "+s);
        return s;
    }

    ;

    /**
     * 不加盐值
     *
     * @param context
     * @return
     */
    public static String gettoken(Context context) {
        return PreferenceUtils.readStrConfig(Constants.SPKEY_TOKEN, context);
    }

    public static String getServerSalt(Context context) {
        return PreferenceUtils.readStrConfig(Constants.SPKEY_SERVER_SALT, context);
    }
}
