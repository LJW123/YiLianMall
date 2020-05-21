package com.yilian.networkingmodule.retrofitutil;

import android.content.Context;

import com.yilian.mylibrary.CommonUtils;

/**
 * Created by Ray_L_Pain on 2017/8/11 0011.
 */

public class AppVersion {

    public static String getAppVersion(Context mContext) {
        return "android_" + CommonUtils.getAppVersion(mContext)+",ios_0";
    }
}
