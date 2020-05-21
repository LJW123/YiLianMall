package com.yilian.mall.utils;

import android.content.Context;

import com.yilian.mylibrary.*;

/**
 * 用于 判断用户是否登录
 * <p>
 * Created by Zg on 2018/7/21
 */
public class IsLoginUtils {

    /**
     * 判断是否登录
     */
    public static boolean isLogin(Context mContext) {
        if (com.yilian.mylibrary.PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, mContext, false)) {
            return true;
        } else {
            JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(mContext);
            return false;
        }
    }

}
