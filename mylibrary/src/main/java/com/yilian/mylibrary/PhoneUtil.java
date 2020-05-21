package com.yilian.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by  on 2017/10/11 0011.
 */

public class PhoneUtil {
    public static final int PHONE_LENGTH = 11;

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param tel 电话号码
     */
    public static void call(String tel, Context context) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel)));
    }

    /**
     * 格式化11位手机号码 中间四位变为*
     *
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneMiddle4Asterisk(String phoneNumber) {
        if (phoneNumber != null) {
            if (phoneNumber.length() == PHONE_LENGTH) {
                return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
            }
            return phoneNumber;
        }
        return phoneNumber;

    }

    /**
     * 电话号码加横杆132-0000-0000
     *
     * @param mobile
     */
    public static String formatPhone(String mobile) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = mobile.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            buffer.append(chars[i]);
            if (i == 2 || i == 6) {
                if (!"-".equals(chars[i])) {
                    buffer.append("-");
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 直接拨打电话
     *
     * @param mobile
     */
    public static void callDirectly(String mobile, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }
}
