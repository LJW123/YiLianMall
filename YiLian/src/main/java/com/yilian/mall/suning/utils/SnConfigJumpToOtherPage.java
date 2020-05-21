package com.yilian.mall.suning.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mylibrary.Constants;

/**
 * 苏宁通用跳转
 *
 * @author Created by Zg on 2018/7/19.
 */

public class SnConfigJumpToOtherPage {
    /**
     * 0.app内web页面(不需要登录) (content : "url")
     * <p>
     * 1.app内web页面(需要登录) (content : "url")
     * <p>
     * 2.外部浏览器打开web页面(不需要登录) (content : "url")
     * <p>
     * 3.品牌精选(content : "")
     * <p>
     * 4.品牌商品列表(content : "品牌名称")
     * <p>
     * 5.商品详情(content : "商品id (sku)")
     * <p>
     * 6.商品分类(content : "")
     */
    private static SharedPreferences sp;
    private static Context mContext;
    private static SnConfigJumpToOtherPage jumpToOtherPage;
    String clsName = "";

    private SnConfigJumpToOtherPage(Context context) {
        mContext = context;
        sp = context.getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
    }

    public static synchronized SnConfigJumpToOtherPage getInstance(Context context) {
        if (jumpToOtherPage == null) {
            jumpToOtherPage = new SnConfigJumpToOtherPage(context);
        }
        return jumpToOtherPage;
    }

    public void jumpToOtherPage(int type, String contents) {

        try {
            String[] content = contents.split(",");
            Intent intent = new Intent();
            switch (type) {
                case 0:
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                    intent.putExtra(Constants.SPKEY_URL, content[0]);
                    break;
                case 1:
                    if (isLogin()) {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                        intent.putExtra(Constants.SPKEY_URL, content[0]);
                    } else {
                        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                    }
                    break;
                case 3:
                    //跳转到京东首页的品牌精选
                    JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_BRAND_SELECTED, null);
                    break;
                case 2:
                    //打开外部浏览器
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(content[0]));
                    break;
                case 4:
                    JumpSnActivityUtils.toSnBrandGoodsList(mContext, content[0]);
                    break;
                case 5:
                    //跳转京东商品详情 content[0]skuId
                    JumpSnActivityUtils.toSnGoodsDetailActivity(mContext, content[0]);
                    break;
                case 6:
                    //跳转到京东首页的分类
                    JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_CLASSIFY, null);
                    break;
                default:
                    break;
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static boolean isLogin() {
        return sp.getBoolean(Constants.SPKEY_STATE, false);
    }
}
