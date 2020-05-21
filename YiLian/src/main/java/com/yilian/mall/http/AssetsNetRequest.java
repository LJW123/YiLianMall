package com.yilian.mall.http;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.mall.entity.AssetsRecordList;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

/**
 * Created by yukun on 2016/3/27.
 */
public class AssetsNetRequest extends BaseNetRequest {

    private  String URL;

    public AssetsNetRequest(Context context) {
        super(context);
        URL= Ip.getURL(context) + "mall.php";
    }

    /**{
     *
     * @param c  lebi 乐享币记录;lefen 乐分币记录;coupon 抵扣券记录
     * @param type 0 充值记录;1 消费记录;2 领奖励记录
     * @param page
     * @param callBack
     */
    public void assetsRecord(String c,String type, int page, RequestCallBack<AssetsRecordList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/"+c+"_log");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", String.valueOf(page));
        if(c.equals("voucher")){
            type=String.valueOf(Integer.valueOf(type)+1);
        }
        params.addBodyParameter("type", type);
        params.addBodyParameter("count", "20");
        postRequest(URL, params, AssetsRecordList.class, callBack);
    }

    public void shoppingRecord(int page, RequestCallBack<AssetsRecordList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/consumer_integral_income");
        params.addBodyParameter(Constants.SPKEY_DEVICE_INDEX, RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", String.valueOf(page));
        postRequest(URL, params, AssetsRecordList.class, callBack);
    }



}
