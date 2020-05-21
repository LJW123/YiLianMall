package com.yilian.mall.shoppingcard.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity;
import com.yilian.mall.shoppingcard.activity.CardCommodityDetailActivity;
import com.yilian.mall.shoppingcard.activity.CardDetailActivity;
import com.yilian.mall.shoppingcard.activity.CardJdBrandListActivity;
import com.yilian.mall.shoppingcard.activity.CardJdCashDeskActivity;
import com.yilian.mall.shoppingcard.activity.CardJdShoppingListActivity;
import com.yilian.mall.shoppingcard.activity.CardMyCardActivity;
import com.yilian.mall.shoppingcard.activity.CardTypeFiltrateActivity;
import com.yilian.mall.ui.V3MoneyDetailActivity;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;

import javax.annotation.Nullable;

/**
 * 作者：马铁超 on 2018/11/14 19:19
 * 购物卡板块跳转页面工具类
 */

public class JumpCardActivityUtils {
    /**
     * 判断是否登录
     */
    public static boolean isLogin(Context context) {
        return PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, context, false);
    }


    /**
     * 跳转到首页
     *
     * @param mContext
     */
    public static void toCardHomePage(Context mContext) {
        JumpToOtherPageUtil.getInstance().jumToCardHomePage(mContext);
    }

    /**
     * 跳转 购物卡 JD商品详情
     */
    public static void toGoodsDetail(Context mContext, String skuID) {
        AppManager.getInstance().killActivity(JDGoodsDetailActivity.class);
        JumpToOtherPageUtil.getInstance().jumToJdGoodsDetail(mContext, skuID, JumpToOtherPageUtil.JD_GOODS_TYPE_CARD);
    }

    /**
     * 跳转其他应用 某个页面
     *
     * @param activity
     * @param packageName 包名
     * @param className   完整类名路径
     *  知道要跳转应用的包名、类名
     *  跳转页面需设置 android:exported="true"
     */
    public static void toOtherAppActivity(Activity activity, String packageName, String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName componentName = new ComponentName(packageName, className);
        intent.setComponent(componentName);
        activity.startActivity(intent);
    }


    /**
     * 跳转购物卡明细页面
     *
     * @param mContext
     */


    public static void toCardDetailActivity(Context mContext) {
        Intent intent = new Intent(mContext, CardDetailActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转购物卡明细详情
     *
     * @param context
     * @param type
     */
    public static void toV3MoneyDetailActivity(Context context, int type, String img, String orderId) {
        Intent intent = new Intent(context, V3MoneyDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("img", img);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    /**
     * 跳转我的购物卡页面
     *
     * @param context
     * @param cardNumber 卡号 传null代表未开卡
     */
    public static void toCardMyShoppingCardActivity(Context context,@Nullable  String cardNumber) {
        Intent intent = new Intent(context, CardMyCardActivity.class);
        intent.putExtra(CardMyCardActivity.TAG_CARD_NUMBER,cardNumber);
        context.startActivity(intent);
    }

    /**
     * 跳转 购物卡 京东商品列表
     */
    public static void toCardJdGoodsList(Context mContext) {
        JumpToOtherPageUtil.getInstance().jumToCardJdGoodsList(mContext, "", "");
    }


    /**
     * 购物卡 京东品牌列表
     *
     * @param context
     */
    public static void toCardJdBrandList(Context context) {
        Intent intent = new Intent(context, CardJdBrandListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转到首页
     *
     * @param mContext
     * @param brandName 品牌名称
     */
    public static void toCardJdBrandGoodsList(Context mContext, String brandName) {
        JumpToOtherPageUtil.getInstance().jumToCardJdBrandGoodsList(mContext, brandName);
    }


    /**
     * 购物卡 京东购物清单
     *
     * @param mContext
     * @param skuString 购物车sku，多个sku以逗号分隔并且
     */
    public static void toCardJdShoppingList(Context mContext, @Nullable String skuString) {
        AppManager.getInstance().killActivity(CardJdShoppingListActivity.class);
        Intent intent = new Intent(mContext, CardJdShoppingListActivity.class);
        intent.putExtra("sku_string", skuString);
        mContext.startActivity(intent);
    }

    /**
     * 跳转收银台
     *
     * @param context
     * @param jdCommitOrderSuccessEntity
     */
    public static void toJDCashDeskActivity(Context context, JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity) {
        Intent intent = new Intent(context, CardJdCashDeskActivity.class);
        intent.putExtra(CardJdCashDeskActivity.TAG, jdCommitOrderSuccessEntity);
        context.startActivity(intent);
    }

    /**
     * 跳转购物卡类型筛选
     *
     * @param activity
     */
    public static void toCardTypeFiltrateActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CardTypeFiltrateActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转购物卡商品详情
     * @param context
     * @param goodsId 商品id
     */
    public static void toCardCommodityDetailActivity(Context context,String goodsId){
      Intent intent = new Intent(context,CardCommodityDetailActivity.class);
      intent.putExtra("goods_id",goodsId);
      context.startActivity(intent);
    }

}
