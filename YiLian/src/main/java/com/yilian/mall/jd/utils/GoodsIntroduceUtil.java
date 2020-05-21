package com.yilian.mall.jd.utils;

import android.text.TextUtils;

/**
 * @author Created by  on 2018/5/29.
 */

public class GoodsIntroduceUtil {
   public static String getIntroduce(String appintroduce, String introduce) {
        if (!TextUtils.isEmpty(appintroduce)) {
            if (!TextUtils.isEmpty(appintroduce.trim())) {
                return appintroduce.trim();
            }
        }
        if (!TextUtils.isEmpty(introduce)) {
            if (TextUtils.isEmpty(introduce.trim())) {
                return introduce.trim();
            }
        }
        return "暂无详情";
    }
}
