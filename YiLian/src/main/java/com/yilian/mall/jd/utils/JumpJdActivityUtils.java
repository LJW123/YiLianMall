package com.yilian.mall.jd.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.yilian.mall.enums.GoodsFeedbackType;
import com.yilian.mall.jd.activity.JDAfterSaleApplyForActivity;
import com.yilian.mall.jd.activity.JDAfterSaleDetailsActivity;
import com.yilian.mall.jd.activity.JDAfterSaleTrackActivity;
import com.yilian.mall.jd.activity.JDCashDeskActivity2;
import com.yilian.mall.jd.activity.JDCommitOrderActivity;
import com.yilian.mall.jd.activity.JDEditShippingAddressActivity;
import com.yilian.mall.jd.activity.JDEditShippingAddressListActivity;
import com.yilian.mall.jd.activity.JDGoodsFeedbackActivity;
import com.yilian.mall.jd.activity.JDOrderDetailsActivity;
import com.yilian.mall.jd.activity.JDOrderLogisticsActivity;
import com.yilian.mall.jd.activity.JDOrderReceivingSuccessActivity;
import com.yilian.mall.jd.activity.JDPaySuccessActivity;
import com.yilian.mall.jd.activity.JdBrandGoodsListActivity;
import com.yilian.mall.jd.activity.JdGoodsSearchActivity;
import com.yilian.mall.jd.activity.JdThirdClassifyGoodsListActivity;
import com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity;
import com.yilian.mall.jd.enums.JdShoppingCartType;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleDetialEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.JDPayInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnPayInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.yilian.mall.jd.activity.JDCommitOrderActivity.TAG_GOODS;
import static com.yilian.mall.jd.activity.JDCommitOrderActivity.TAG_SHIPPING_ADDRESS_INFO;
import static com.yilian.mall.jd.activity.JDEditShippingAddressActivity.ADD_ADDRESS_REQUEST_CODE;
import static com.yilian.mall.jd.activity.JDEditShippingAddressListActivity.GET_JD_ADDRESS;
import static com.yilian.mall.jd.activity.JDPaySuccessActivity.PAY_SIDE_TAG;

/**
 * 用于activity跳转等
 * Created by Zg on 2018/5/22
 */
public class JumpJdActivityUtils {

    /**
     * 跳转JD商品详情
     */
    public static void toGoodsDetail(Context mContext, String skuID) {
        JumpToOtherPageUtil.getInstance().jumToJdGoodsDetail(mContext, skuID, JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON);
    }


    /**
     * 跳转JD商品详情 问题反馈
     */
    public static void toJDGoodsFeedback(Context mContext, String goods_sku, String goods_name, String goods_img) {
        if (isLogin(mContext)) {
            toGoodsFeedbackByType(mContext, goods_sku, goods_name, goods_img, GoodsFeedbackType.GOODS_FEEDBACK_JD);
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
     * 商品意见反馈
     *
     * @param mContext
     * @param goods_sku
     * @param goods_name
     * @param goods_img
     * @param goodsFeedbackType 反馈的商品 类型
     */
    public static void toGoodsFeedbackByType(Context mContext, String goods_sku, String goods_name, String goods_img, GoodsFeedbackType goodsFeedbackType) {
        Intent intent = new Intent(mContext, JDGoodsFeedbackActivity.class);
        intent.putExtra("goods_sku", goods_sku);
        intent.putExtra("goods_name", goods_name);
        intent.putExtra("goods_img", goods_img);
        intent.putExtra("goodsFeedbackType", goodsFeedbackType);
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
     * 跳转JD地址编辑页面
     *
     * @param activity
     */
    public static void toJDEditAddressActivity(Activity activity, @Nullable JDShippingAddressInfoEntity.DataBean dataBean) {
        Intent intent = new Intent(activity, JDEditShippingAddressActivity.class);
        intent.putExtra(JDEditShippingAddressActivity.TAG, dataBean);
        activity.startActivityForResult(intent, ADD_ADDRESS_REQUEST_CODE);

    }

    /**
     * 跳转JD收货地址列表管理界面
     *
     * @param activity
     */
    public static void toJDShippingAddressListActivityForReuslt(Activity activity) {
        Intent intent = new Intent(activity, JDEditShippingAddressListActivity.class);
        activity.startActivityForResult(intent, GET_JD_ADDRESS);
    }

    /**
     * 跳转JD收货地址列表管理界面
     *
     * @param activity
     */
    public static void toJDShippingAddressListActivity(android.support.v4.app.Fragment activity) {
        Intent intent = new Intent(activity.getContext(), JDEditShippingAddressListActivity.class);
        activity.startActivityForResult(intent, GET_JD_ADDRESS);
    }

    /**
     * 跳转提交订单界面
     */
    public static void toJDCommitOrderActivity(Context context, @Nullable JDShippingAddressInfoEntity.DataBean shippingAddressInfoEntity,
                                               JDCommitOrderEntity jdCommitOrderEntity, JdShoppingCartType jdShoppingCartType, @JumpToOtherPageUtil.JDGoodsType int jdType) {
        ArrayList<JDCommitOrderEntity> jdCommitOrderEntities = new ArrayList<>();
        jdCommitOrderEntities.add(jdCommitOrderEntity);
        toJDCommitOrderActivity(context, shippingAddressInfoEntity, jdCommitOrderEntities, jdShoppingCartType, jdType);
    }

    /**
     * 跳转提交订单界面
     */
    public static void toJDCommitOrderActivity(Context context, @Nullable JDShippingAddressInfoEntity.DataBean shippingAddressInfoEntity,
                                               ArrayList<JDCommitOrderEntity> jdCommitOrderEntities, JdShoppingCartType jdShoppingCartType, @JumpToOtherPageUtil.JDGoodsType int jdType) {
        Intent intent = new Intent(context, JDCommitOrderActivity.class);
        intent.putExtra(TAG_SHIPPING_ADDRESS_INFO, shippingAddressInfoEntity);
        intent.putExtra(TAG_GOODS, jdCommitOrderEntities);
        intent.putExtra("jdShoppingCartType", jdShoppingCartType);
        intent.putExtra("jdType", jdType);
        context.startActivity(intent);
    }

    /**
     * 跳转JD订单详情
     *
     * @param jdOrderId 京东订单号
     */
    public static void toJDOrderDetails(Context mContext, String jdOrderId) {
        Intent intent = new Intent(mContext, JDOrderDetailsActivity.class);
        intent.putExtra("jdOrderId", jdOrderId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转 确认收货成功
     */
    public static void toJDOrderReceivingSuccess(Context mContext) {
        Intent intent = new Intent(mContext, JDOrderReceivingSuccessActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转JD售后申请页面
     *
     * @param jdOrderId 京东订单号
     * @param skuId     商品id
     * @param skuNum    商品数量
     */
    public static void toJDAfterSaleApplyFor(Context mContext, String jdOrderId, String skuId, String skuNum) {
        Intent intent = new Intent(mContext, JDAfterSaleApplyForActivity.class);
        intent.putExtra("jdOrderId", jdOrderId);
        intent.putExtra("skuId", skuId);
        intent.putExtra("skuNum", skuNum);
        mContext.startActivity(intent);
    }

    /**
     * 跳转JD 京东售后申请 详情页
     *
     * @param afsServiceId 售后服务单号
     * @param skuId        售后商品id
     * @param skuNum       售后商品数量
     */
    public static void toJDAfterSaleDetails(Context mContext, String afsServiceId, String skuId, String skuNum) {
        Intent intent = new Intent(mContext, JDAfterSaleDetailsActivity.class);
        intent.putExtra("afsServiceId", afsServiceId);
        intent.putExtra("skuId", skuId);
        intent.putExtra("skuNum", skuNum);
        mContext.startActivity(intent);
    }

    /**
     * 跳转JD 京东售后申请 服务单追踪
     *
     * @param serviceTrackInfoDTOsList 售后服务详情中 服务单追踪信息
     */
    public static void toJDAfterSaleTrack(Context mContext, List<JDAfterSaleDetialEntity.serviceTrackInfoDTOs> serviceTrackInfoDTOsList) {
        Intent intent = new Intent(mContext, JDAfterSaleTrackActivity.class);
        intent.putExtra("serviceTrackInfoDTOsList", (Serializable) serviceTrackInfoDTOsList);
        mContext.startActivity(intent);
    }

    /**
     * 跳转JD订单物流
     *
     * @param jdOrderId 京东订单号
     */
    public static void toJDOrderLogistics(Context mContext, String jdOrderId) {
        Intent intent = new Intent(mContext, JDOrderLogisticsActivity.class);
        intent.putExtra("jdOrderId", jdOrderId);
        mContext.startActivity(intent);
    }

    /**
     * 跳转京东品牌商品列表
     *
     * @param mContext
     * @param brandName 品牌名称
     */
    public static void toJdBrandGoodsListActivity(Context mContext, String brandName) {
//        Intent intent = new Intent(mContext, JdBrandGoodsListActivity.class);
//        intent.putExtra("brand_name", brandName);
//        mContext.startActivity(intent);
        JumpToOtherPageUtil.getInstance().jumToJdBrandGoodsList(mContext, brandName);
    }

    /**
     * 跳转京东第三级类表
     *
     * @param mContext
     * @param catId    三级分类id
     */
    public static void toJdThirdClassifyGoodsListActivity(Context mContext, String catId, String title) {
        Intent intent = new Intent(mContext, JdThirdClassifyGoodsListActivity.class);
        intent.putExtra("catId", catId);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到商品搜索页面
     *
     * @param mContext
     */
    public static void toJdGoodsSearchActivity(Context mContext) {
        Intent intent = new Intent(mContext, JdGoodsSearchActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到首页
     *
     * @param index     当前fragment的tab角标
     * @param skuString 购物车sku，多个sku以逗号分隔并且
     */
    public static void toJdHomePageActivity(Context mContext, int index, String skuString) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.jd.activity.JdHomePageActivity"));
        intent.putExtra("index", index);
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
        Intent intent = new Intent(context, JDCashDeskActivity2.class);
        intent.putExtra(JDCashDeskActivity2.TAG, jdCommitOrderSuccessEntity);
        context.startActivity(intent);
    }

    /**
     * 跳转支付成功页面
     *
     * @param context
     * @param jdPayInfoEntity
     */
    public static void toJDJDPaySuccessActivity(Context context, HttpResultBean jdPayInfoEntity, JDPaySuccessActivity.PaySide paySide) {
        Intent intent = new Intent(context, JDPaySuccessActivity.class);
        switch (paySide) {
            case Card_JD:
            case JD:
                intent.putExtra(JDPaySuccessActivity.TAG, (JDPayInfoEntity) jdPayInfoEntity);
                break;
            case SN:
                intent.putExtra(JDPaySuccessActivity.TAG, (SnPayInfoEntity) jdPayInfoEntity);
                break;
            default:
                break;
        }
        intent.putExtra(PAY_SIDE_TAG, paySide);
        context.startActivity(intent);
    }

}
