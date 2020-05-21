package com.yilian.mylibrary;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;

import com.yilian.mylibrary.activity.ImageBrowseActivity;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 跳转到其他页面的工具类
 *
 * @author Created by  on 2018/3/22.
 */

public class JumpToOtherPageUtil {
    /**
     * 京东商品类型限制 类型 0普通京东商品
     */
    public static final int JD_GOODS_TYPE_COMMON = 0;
    /**
     * 京东商品类型限制 类型 1购物卡京东商
     */
    public static final int JD_GOODS_TYPE_CARD = 1;
    private static volatile JumpToOtherPageUtil singleInstance;

    private JumpToOtherPageUtil() {
    }

    public static JumpToOtherPageUtil getInstance() {
        synchronized (JumpToOtherPageUtil.class) {
            if (singleInstance == null) {
                singleInstance = new JumpToOtherPageUtil();
            }
        }
        return singleInstance;
    }

    /**
     * 跳转到web页面
     *
     * @param mContext
     * @param url
     */
    public void jumpToWebViewActivity(Context mContext, String url, boolean isRecharge) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
        intent.putExtra(Constants.SPKEY_URL, url);
        intent.putExtra("isRecharge", isRecharge);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到 图片浏览界面
     *
     * @param mContext
     * @param imgList   图片集合
     * @param position  当前图片位置
     * @param canDelete 是否可删除
     */
    public void jumpToImageBrowse(Context mContext, List<String> imgList, int position, boolean canDelete) {
        Intent intent = new Intent(mContext, ImageBrowseActivity.class);
        intent.putExtra("canDelete", canDelete);
        intent.putExtra("imgList", (Serializable) imgList);
        intent.putExtra("position", position);
        mContext.startActivity(intent);
    }

    /**
     * 跳转找回密码界面
     *
     * @param mContext
     */
    public void jumpToInitialPayActivity(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.InitialPayActivity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 申请领奖励,
     *
     * @param context
     */
    public void jumpToJTakeCashActivity(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, "com.yilian.mall.ui.JTakeCashActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转乐分登录页面
     *
     * @param mContext
     */
    public void jumpToLeFenPhoneLoginActivity(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 跳转商品详情
     *
     * @param mContext
     * @param classify
     * @param goodsId  商品类别 0 普通商品 1yhs 2jfg
     */
    public void jumpToJPNewCommDetailActivity(Context mContext, String classify, String goodsId) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPNewCommDetailActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("goods_id", goodsId);
        intent.putExtra("classify", classify);
        mContext.startActivity(intent);
    }

    /**
     * 跳转扫描二维码界面
     */
    public void jumToMipcaActivityCapture(Context mContext, Intent intent) {
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MipcaActivityCapture"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 设置密码
     *
     * @param mContext
     */
    public void jumpToRetrievePasswordActivity(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.RetrievePasswordActivity"));
        intent.putExtra("forget", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", "设置密码");
        intent.putExtra("verifyType", 3);
        mContext.startActivity(intent);
    }

    /**
     * 商家我的营收
     *
     * @param mContext
     */
    public void jumpToMerchantMyRevneue(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilianmall.merchant.activity.MerchantMyRevenueActivity"));
        mContext.startActivity(intent);
    }

    /**
     * 跳转商家缴费/续费界面
     *
     * @param context
     * @param merchantPayType 缴费类型 {@link MerchantEnterType}
     */
    public void jumpToMerchantPayActivity(Context context, @MerchantEnterType.EnterType String merchantPayType) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, "com.yilianmall.merchant.activity.MerchantPayActivity"));
        intent.putExtra("merchantPayType", merchantPayType);
        context.startActivity(intent);
    }

    /**
     * 跳转 京东政企品牌商品列表
     *
     * @param brandName  品牌名称
     */
    public void jumToJdBrandGoodsList(Context mContext, String brandName) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.jd.activity.JdBrandGoodsListActivity"));
        intent.putExtra("brand_name", brandName);
        mContext.startActivity(intent);
    }

    /**
     * 跳转JD商品详情
     *
     * @param skuID  京东 skuID
     * @param jdType 0普通京东商品  1购物卡京东商品
     */
    public void jumToJdGoodsDetail(Context mContext, String skuID, @JDGoodsType int jdType) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity"));
        intent.putExtra("skuID", skuID);
        intent.putExtra("jdType", jdType);
        mContext.startActivity(intent);
    }


    /**
     * 跳转 购物卡首页
     */
    public void jumToCardHomePage(Context mContext) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.shoppingcard.activity.CardHomePageActivity"));
        mContext.startActivity(intent);
    }

    /**
     * 跳转 购物卡 京东商品列表
     */
    public void jumToCardJdGoodsList(Context mContext, String catId, String title) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.shoppingcard.activity.CardJdGoodsListActivity"));
        intent.putExtra("catId", catId);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 购物卡 京东政企品牌商品列表
     *
     * @param brandName  品牌名称
     */
    public void jumToCardJdBrandGoodsList(Context mContext, String brandName) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.shoppingcard.activity.CardJdBrandGoodsListActivity"));
        intent.putExtra("brand_name", brandName);
        mContext.startActivity(intent);
    }

    /**
     * 京东商品类型限制 类型 0普通京东商品 1购物卡京东商
     *
     * @author Created by Zg on 2018/11/16.
     */
    @IntDef({JD_GOODS_TYPE_COMMON, JD_GOODS_TYPE_CARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface JDGoodsType {
    }


}
