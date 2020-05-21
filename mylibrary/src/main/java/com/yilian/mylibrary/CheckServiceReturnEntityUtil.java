package com.yilian.mylibrary;

import android.content.Context;

/**
 * Created by  on 2017/5/25 0025.
 */

public class CheckServiceReturnEntityUtil {
    /**
     * 检查服务器返回的实体类是否为空
     * @param mContext
     * @param entity
     * @return 空返回false  不为空返回TRUE
     */
    public static boolean checkServiceReturnEntity(Context mContext, Object entity) {
        if (entity == null) {
            ToastUtil.showMessage(mContext, "服务器繁忙,请稍后再试");
            return false;
        }
        return true;
    }
}
