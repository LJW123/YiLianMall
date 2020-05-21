package com.yilian.mall.suning.utils;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.yilian.mall.enums.GoodsFeedbackType;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.suning.activity.SnAfterSaleApplyForActivity;
import com.yilian.mall.suning.activity.SnAfterSaleDetailsActivity;
import com.yilian.mall.suning.activity.SnAfterSaleSearchActivity;
import com.yilian.mall.suning.activity.SnBrandGoodsListActivity;
import com.yilian.mall.suning.activity.SnCashDeskActivity;
import com.yilian.mall.suning.activity.SnCommitOrderActivity;
import com.yilian.mall.suning.activity.SnCommitOrderMultiGoodsList;
import com.yilian.mall.suning.activity.SnGoodsSearchActivity;
import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.activity.SnOrderDetailsActivity;
import com.yilian.mall.suning.activity.SnOrderLogisticsActivity;
import com.yilian.mall.suning.activity.SnThirdClassifyGoodsListActivity;
import com.yilian.mall.suning.activity.goodsdetail.SnGoodsDetailActivity;
import com.yilian.mall.suning.activity.shippingaddress.SnEditShippingAddressActivity;
import com.yilian.mall.suning.activity.shippingaddress.SnShippingAddressListActivity;
import com.yilian.mall.suning.module.SuNingCommitOrderGoodsModule;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.suning.SnCommitOrderEntity;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;

import java.util.ArrayList;

import static com.yilian.mall.suning.activity.shippingaddress.SnEditShippingAddressActivity.TAG_EDIT_STATUS;
import static com.yilian.mall.suning.activity.shippingaddress.SnEditShippingAddressActivity.TAG_OLD_SHIPPING_ADDRESS_INFO;
import static com.yilian.mall.suning.activity.shippingaddress.SnShippingAddressListActivity.CHECKED_ADDRESS_ID;

/**
 * 用于 苏宁 activity跳转等
 * Created by Zg on 2018/7/14
 */
public class JumpSnActivityUtils {

    /**
     * 跳转SN商品详情 问题反馈
     */
    public static void toSnGoodsFeedback(Context mContext, String goods_sku, String goods_name, String goods_img) {
        if (isLogin(mContext)) {
            JumpJdActivityUtils.toGoodsFeedbackByType(mContext, goods_sku, goods_name, goods_img, GoodsFeedbackType.GOODS_FEEDBACK_SN);
        } else {
            JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(mContext);
        }
    }

    /**
     * 判断是否登录
     */
    public static boolean isLogin(Context context) {
        return PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, context, false);
    }

    /**
     * 跳转苏宁第三级类表
     *
     * @param mContext
     * @param catId    三级分类id
     */
    public static void toSnThirdClassifyGoodsList(Context mContext, String catId, String title) {
        Intent intent = new Intent(mContext, SnThirdClassifyGoodsListActivity.class);
        intent.putExtra("catId", catId);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到商品搜索页面
     *
     * @param mContext
     */
    public static void toSnGoodsSearch(Context mContext) {
        Intent intent = new Intent(mContext, SnGoodsSearchActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转苏宁订单详情
     *
     * @param snOrderId 苏宁订单号
     */
    public static void toSnOrderDetails(Context mContext, String snOrderId) {
        Intent intent = new Intent(mContext, SnOrderDetailsActivity.class);
        intent.putExtra("snOrderId", snOrderId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转苏宁品牌商品列表
     *
     * @param mContext
     * @param brandName 品牌名称
     */
    public static void toSnBrandGoodsList(Context mContext, String brandName) {
        Intent intent = new Intent(mContext, SnBrandGoodsListActivity.class);
        intent.putExtra("brand_name", brandName);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到首页
     *
     * @param snHomeTab 当前fragment的tab角标
     * @param skuString 购物车sku，多个sku以逗号分隔并且
     */
    public static void toSnHomePageActivity(Context mContext, @SnHomePageActivity.SnHomeTab int snHomeTab, @Nullable String skuString) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.suning.activity.SnHomePageActivity"));
        intent.putExtra("index", snHomeTab);
        intent.putExtra("sku_string", skuString);
        mContext.startActivity(intent);
    }

    /**
     * 跳转至App内部web页面
     *
     * @param mContext
     * @param url
     */
    public static void toWebViewActivity(Context mContext, String url) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
        intent.putExtra(Constants.SPKEY_URL, url);
        mContext.startActivity(intent);
    }

    /**
     * 跳转苏宁商品详情
     *
     * @param context
     * @param skuId
     */
    public static void toSnGoodsDetailActivity(Context context, String skuId) {
        Intent intent = new Intent(context, SnGoodsDetailActivity.class);
        intent.putExtra(SnGoodsDetailActivity.TAG, skuId);
        context.startActivity(intent);

    }

    /**
     * 跳转苏宁提交订单
     *
     * @param context
     * @param suNingCommitOrderGoodsModule
     */
    public static void toSnCommitOrderActivity(Context context, @Nullable SnShippingAddressInfoEntity.DataBean shippingAddress,
                                               SuNingCommitOrderGoodsModule suNingCommitOrderGoodsModule, SnCommitOrderActivity.FromeType fromeType) {
        ArrayList<SuNingCommitOrderGoodsModule> suNingCommitOrderGoodsModules = new ArrayList<>();
        suNingCommitOrderGoodsModules.add(suNingCommitOrderGoodsModule);
        toSnCommitOrderActivity(context, shippingAddress, suNingCommitOrderGoodsModules, fromeType);
    }

    /**
     * 跳转苏宁提交订单
     *
     * @param context
     * @param suNingCommitOrderGoodsModules
     */
    public static void toSnCommitOrderActivity(Context context, @Nullable SnShippingAddressInfoEntity.DataBean shippingAddress,
                                               ArrayList<SuNingCommitOrderGoodsModule> suNingCommitOrderGoodsModules, SnCommitOrderActivity.FromeType fromeType) {
        Intent intent = new Intent(context, SnCommitOrderActivity.class);
        intent.putExtra(SnCommitOrderActivity.TAG_ADDRESS, shippingAddress);
        intent.putExtra(SnCommitOrderActivity.TAG, suNingCommitOrderGoodsModules);
        intent.putExtra(SnCommitOrderActivity.TAG_FROM_TYPE, fromeType);
        context.startActivity(intent);
    }

    /**
     * 苏宁地址列表
     *
     * @param fragment
     */
    public static void toShippingAddressListActivitiy(android.support.v4.app.Fragment fragment) {
        toShippingAddressListActivitiy(fragment, null);
    }

    /**
     * 苏宁地址列表
     *
     * @param fragment
     */
    public static void toShippingAddressListActivitiy(android.support.v4.app.Fragment fragment, String addressId) {
        if (isLogin(fragment.getContext())) {
            fragment.startActivityForResult(
                    getShippingAddressListActivityIntent(fragment.getContext(), addressId), SnShippingAddressListActivity.REQUEST_CODE);
        } else {
            JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(fragment.getContext());
        }

    }

    /**
     * 获取跳转苏宁地址列表的intent
     *
     * @param context
     * @param addressId
     * @return
     */
    private static Intent getShippingAddressListActivityIntent(Context context, String addressId) {
        Intent intent = new Intent(context, SnShippingAddressListActivity.class);
        intent.putExtra(CHECKED_ADDRESS_ID, addressId);
        return intent;
    }

    /**
     * 苏宁地址列表
     *
     * @param activity
     */
    public static void toShippingAddressListActivitiy(Activity activity) {
        toShippingAddressListActivitiy(activity, null);
    }

    /**
     * 苏宁地址列表
     *
     * @param activity
     */
    public static void toShippingAddressListActivitiy(Activity activity, String addressId) {
        if (isLogin(activity)) {
            activity.startActivityForResult(
                    getShippingAddressListActivityIntent(activity, addressId), SnShippingAddressListActivity.REQUEST_CODE);
        } else {
            JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(activity);
        }

    }

    /**
     * 编辑/新增地址
     *
     * @param context
     * @param editType
     */
    public static void toSnEditShippingAddressActivity(Activity context, SnEditShippingAddressActivity.EditType editType) {
        Intent intent = new Intent(context, SnEditShippingAddressActivity.class);
        intent.putExtra(TAG_EDIT_STATUS, editType);
        context.startActivityForResult(intent, SnEditShippingAddressActivity.REQUEST_CODE_EDIT);
    }

    /**
     * 编辑/新增地址
     *
     * @param context
     * @param editType
     */
    public static void toSnEditShippingAddressActivity(Activity context,
                                                       SnEditShippingAddressActivity.EditType editType,
                                                       SnShippingAddressInfoEntity.DataBean dataBean) {
        Intent intent = new Intent(context, SnEditShippingAddressActivity.class);
        intent.putExtra(TAG_OLD_SHIPPING_ADDRESS_INFO, dataBean);
        intent.putExtra(TAG_EDIT_STATUS, editType);
        context.startActivityForResult(intent, SnEditShippingAddressActivity.REQUEST_CODE_EDIT);
    }

    /**
     * 跳转苏宁收银台
     *
     * @param context
     * @param snCommitOrderEntity
     */
    public static void toSnCashDeskActivity(Context context, SnCommitOrderEntity snCommitOrderEntity) {
        Intent intent = new Intent(context, SnCashDeskActivity.class);
        intent.putExtra(SnCashDeskActivity.TAG, snCommitOrderEntity);
        context.startActivity(intent);
    }

    /**
     * 跳转苏宁订单物流
     *
     * @param snOrderId   苏宁订单号
     * @param orderItemId 苏宁订单行号
     * @param skuId       商品id
     */
    public static void toSnOrderLogistics(Context mContext, String snOrderId, String orderItemId, String skuId) {
        Intent intent = new Intent(mContext, SnOrderLogisticsActivity.class);
        intent.putExtra("snOrderId", snOrderId);
        intent.putExtra("orderItemId", orderItemId);
        intent.putExtra("skuId", skuId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 苏宁售后申请 详情页
     *
     * @param id 列表的ID
     */
    public static void toSnAfterSaleDetails(Context mContext, String id) {
        Intent intent = new Intent(mContext, SnAfterSaleDetailsActivity.class);
        intent.putExtra("id", id);
        mContext.startActivity(intent);
    }

    /**
     * 跳转苏宁售后申请页面
     *
     * @param snOrderId    京东订单号
     * @param orderSnPrice 订单金额
     * @param coupon       订单使用的代购券
     * @param skuId        商品id
     * @param skuPic       商品图片
     * @param skuName      商品名
     * @param goodsPrice   商品单价
     * @param goodsNum     商品数量
     */
    public static void toSnAfterSaleApplyFor(Context mContext, String snOrderId, String orderSnPrice, String coupon, String skuId, String skuPic, String skuName, String goodsPrice, String goodsNum) {
        Intent intent = new Intent(mContext, SnAfterSaleApplyForActivity.class);
        intent.putExtra("snOrderId", snOrderId);
        intent.putExtra("orderSnPrice", orderSnPrice);
//        intent.putExtra("coupon", coupon);
        intent.putExtra("coupon", "0");
        intent.putExtra("skuId", skuId);
        intent.putExtra("skuPic", skuPic);
        intent.putExtra("skuName", skuName);
        intent.putExtra("goodsPrice", goodsPrice);
        intent.putExtra("goodsNum", goodsNum);
        mContext.startActivity(intent);
    }

    /**
     * 苏宁售后订单查询页面
     *
     * @param context
     * @param afterSaleType {@link SnAfterSaleSearchActivity.AfterSaleType}
     */
    public static void toSnAfterSearchActivity(Context context, SnAfterSaleSearchActivity.AfterSaleType afterSaleType) {
        Intent intent = new Intent(context, SnAfterSaleSearchActivity.class);
        intent.putExtra(SnAfterSaleSearchActivity.TAG, afterSaleType);
        context.startActivity(intent);
    }

    /**
     * 跳转苏宁提交订单商品清单
     *
     * @param context
     * @param datas
     */
    public static void toSnCommitOrderMultiGoodsList(Context context, ArrayList<SuNingCommitOrderGoodsModule> datas,
                                                     int goodsCount) {
        Intent intent = new Intent(context, SnCommitOrderMultiGoodsList.class);
        intent.putExtra(SnCommitOrderMultiGoodsList.TAG, datas);
        intent.putExtra(SnCommitOrderMultiGoodsList.TAG_GOODS_COUNT, goodsCount);
        context.startActivity(intent);
    }
}
