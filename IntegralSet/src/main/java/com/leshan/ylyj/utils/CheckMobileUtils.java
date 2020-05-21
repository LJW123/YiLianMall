package com.leshan.ylyj.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class CheckMobileUtils {
    //正则校验手机号
    public static final String CM = "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}|(1705)\\\\d{7}$";
    public static final String CU = "^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\\\d{7}$";
    public static final String CT = "^((133)|(153)|(177)|(173)|(175)|(170)|(18[0,1,9]))\\d{8}$";

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(CM, mobile) || Pattern.matches(CU, mobile) || Pattern.matches(CT, mobile);
    }

    /**
     * 加密手机号
     */
    public static String isEncryption(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return "手机号获取失败";
    }
}
