package com.yilian.mylibrary;

import android.content.Context;

/**
 * @author Created by  on 2018/1/27.
 *         刷新个人中心数据
 */

public class RefreshPersonCenter {
    /**
     * 回到个人中心时刷新
     *
     * @param context
     */
    public static void refresh(Context context) {
        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
    }
}
