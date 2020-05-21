package com.yilian.mall.http;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.AfterSale;
import com.yilian.mall.entity.BarterAfterSaleRouing;
import com.yilian.mall.entity.BarterOrderInfo;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.RefundAfterSaleRouing;
import com.yilian.mall.entity.RefundOrderInfo;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

/**
 * Created by 刘玉坤 on 2016/2/26.
 */
public class AfterSaleNetRequest extends BaseNetRequest {

    private  String URL;

    public AfterSaleNetRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "mall.php";
    }

    /**
     * 获取售后列表数据
     *
     * @param page     数据页数。
     * @param callBack
     */
    public void afterSaleList(int page, RequestCallBack<AfterSale> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/service_order_list");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", String.valueOf(page));

        postRequest(URL, params, AfterSale.class, callBack);
    }

    /**
     * 获取退货订单信息
     * @param orderId
     * @param callBack
     */
    public void refundOrderInfo(String orderId,String filialeId, RequestCallBack<RefundOrderInfo> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/refund_order_info");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("refund_order", orderId);
        params.addBodyParameter("filiale_id", filialeId);
        Logger.i(RequestOftenKey.getToken(mContext) + "---" + RequestOftenKey.getDeviceIndex(mContext));

        postRequest(URL, params, RefundOrderInfo.class, callBack);
    }


    public void barterOrderInfo(String orderId, String filialeId,RequestCallBack<BarterOrderInfo> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/barter_order_info");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("barter_order", orderId);
        params.addBodyParameter("filiale_id", filialeId);
        postRequest(URL, params, BarterOrderInfo.class, callBack);
    }

    /**
     * 取消售后
     * @param orderId
     * @param type
     * @param callBack
     */
    public void cancel(String orderId,int type,RequestCallBack<BaseEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/cancle_service");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("type",String.valueOf(type));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", orderId);
        params.addBodyParameter("cancel_reason","已解决");

        postRequest(URL, params, BaseEntity.class, callBack);

    }

    /**
     * 重新申请售后
     * @param orderId
     * @param type
     * @param callBack
     */
    public void recreate(String orderId,int type,RequestCallBack<BaseEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/recreate_service_order");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("service_type",String.valueOf(type));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("service_order_id", orderId);

        postRequest(URL, params, BaseEntity.class, callBack);
    }


    /**
     *查询退货订单状态
     * @param orderId 订单编号
     * @param callBack
     */
    public void refundOrderStatus(String orderId,RequestCallBack<RefundAfterSaleRouing> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/refund_order_status");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("refund_order", orderId);

        postRequest(URL, params, RefundAfterSaleRouing.class, callBack);

    }

    /**
     * 查询换货返修订单状态
     * @param orderId 订单编号
     * @param callBack
     */
    public void barterOrderStatus(String orderId,RequestCallBack<BarterAfterSaleRouing> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/barter_order_status");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("barter_order", orderId);
        postRequest(URL, params, BarterAfterSaleRouing.class, callBack);
    }

    /**
     * 提交物流单号
     * @param orderType 0 退货 1 换货维修
     * @param orderId 订单编号
     * @param expressName 物流公司
     * @param expressId 物流编号
     * @param callBack
     */
    public void setExpress(int orderType, String orderId, String expressName, String expressId,RequestCallBack<BaseEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/set_express");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_type", String.valueOf(orderType));
        params.addBodyParameter("order_id", orderId);
        params.addBodyParameter("express_com",expressName);
        params.addBodyParameter("express_num",expressId);
        postRequest(URL, params, BaseEntity.class, callBack);

    }

    /**
     * 售后订单确定解决
     */
    public void confirmBarterOrder(String orderId,RequestCallBack<BaseEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/confirm_barter_order");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", orderId);
        postRequest(URL, params, BaseEntity.class, callBack);
    }

}
