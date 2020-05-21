package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.BankMain;
import com.yilian.mall.entity.BankProfitEntity;
import com.yilian.mall.entity.BankProfitRateEntity;
import com.yilian.mall.entity.RollOutEntity;
import com.yilian.mall.entity.RollOutSuccessEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * Created by Administrator on 2016/6/28.
 */
public class BankRequest extends BaseNetRequest {
    private  String URL;

    public BankRequest(Context context) {
        super(context);
        URL= Ip.getURL(context) + "mall.php";
    }

    /**
     * 获取累计领奖励
     * @param callBack
     */
    public void getTotalProfit(RequestCallBack<BankProfitEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","lefenbao/bao_sum_percentage");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL,params, BankProfitEntity.class,callBack);
    }
    /**
     *获取年化领奖励率
     */
    public void getProfitRate(RequestCallBack<BankProfitRateEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","lefenbao/bao_year_percentage");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL,params,BankProfitRateEntity.class,callBack);
    }
    /**
     * 转出
     * @param callBack
     */
    public void RollOutAviable( RequestCallBack<RollOutEntity> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "lefenbao/bao_avible");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));

        postRequest(URL, params, RollOutEntity.class, callBack);

    }

    /**
     * 转出成功
     * @param lebi //转到奖励中货币数量
     * @param pwd //用户支付密码
     * @param callBack
     */
    public void RollOutSucceccAviable(String lebi,String pwd, RequestCallBack<RollOutSuccessEntity> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "lefenbao/bao_lebi_transfer");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("pwd", pwd);
        params.addBodyParameter("lebi",lebi);

        postRequest(URL, params, RollOutSuccessEntity.class, callBack);

    }

    public void BankMain(int page, RequestCallBack<BankMain> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "lefenbao/bao_main");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count","20");

        postRequest(URL, params, BankMain.class, callBack);
    }
}
