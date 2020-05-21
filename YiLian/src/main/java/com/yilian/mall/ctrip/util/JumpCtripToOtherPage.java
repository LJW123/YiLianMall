package com.yilian.mall.ctrip.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mylibrary.Constants;

/**
 * 携程 通用跳转
 *
 * @author Created by Zg on 2018/8/27.
 */

public class JumpCtripToOtherPage {
    /**
     * app内web页面(不需要登录) (content : "url")
     */
    private static SharedPreferences sp;
    private static Context mContext;
    private static JumpCtripToOtherPage jumpToOtherPage;


    private JumpCtripToOtherPage(Context context) {
        mContext = context;
        sp = context.getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
    }

    public static synchronized JumpCtripToOtherPage getInstance(Context context) {
        if (jumpToOtherPage == null) {
            jumpToOtherPage = new JumpCtripToOtherPage(context);
        }
        return jumpToOtherPage;
    }

    public void jumpToOtherPage(String contents) {
        try {
            String[] content = contents.split(",");
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, content[0]);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
