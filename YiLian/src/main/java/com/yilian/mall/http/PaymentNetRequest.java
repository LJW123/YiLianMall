package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.LebiPayResult;
import com.yilian.mall.entity.MTConsumerProfit;
import com.yilian.mall.entity.PayContent;
import com.yilian.mall.entity.PayCouponResult;
import com.yilian.mall.entity.PayTypeListEntity;
import com.yilian.mall.entity.Payment;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;


/**
 * 支付相关请求
 * @author Administrator
 *
 */
public class PaymentNetRequest extends BaseNetRequest {
	public  String URL;

	public PaymentNetRequest(Context mContext) {
		super(mContext);
		URL=Ip.getURL(mContext)+"payment.php";
		// TODO Auto-generated constructor stub
	}

	/**
	 * 奖券支付请求
	 * @param orderId 要支付的订单编号
	 * @param payPwd 用户支付密码(16位md5值+盐的16位md5值)
	 * @param cls
	 * @param callBack
	 */
	public void scorePay(String orderId,String payPwd,Class<Payment> cls,RequestCallBack<Payment> callBack) {
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "lefen");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("order_id", orderId);
		params.addBodyParameter("pay_pwd", payPwd);
		
		postRequest(URL, params, cls, callBack);
	}
	
	/**
	 * 乐币支付给联盟商家
	 * @param merchantId 商家ID
	 * @param money 支付乐币金额（单位：分，小数点后两位）
	 * @param pwd 用户支付密码(16位md5值+盐的16位md5值)
	 * @param cls
	 * @param callBack
	 */
	public void lebiPayMerchant(String merchantId,String money,String paymentCoupon,String employeeId,String pwd,Class<LebiPayResult> cls,RequestCallBack<LebiPayResult> callBack) {
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "merchant_lebi");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("merchant_id", merchantId);
		params.addBodyParameter("payment_money", money);
		params.addBodyParameter("payment_coupon", paymentCoupon);
		params.addBodyParameter("employee", employeeId);
		params.addBodyParameter("pay_pwd", pwd);
		params.addBodyParameter("type", "0");//区别微信和移动端，移动端传递0

        Logger.i("店内消费支付参数: deviceIndex:"+RequestOftenKey.getDeviceIndex(mContext)+" token: "+ RequestOftenKey.getToken(mContext) +"  merchant_id:"+merchantId+"  paymentMoney:"+money+"  paymentCoupon:"+paymentCoupon+"  employee:"+employeeId+"  payPwd:"+pwd);
        postRequest(URL, params, cls, callBack);
	}
	
	/**
	 * 乐币支付给兑换中心
	 * @param filialeId 兑换中心ID
	 * @param orderId 订单编号
	 * @param money 支付乐币金额（单位：分，小数点后两位）
	 * @param pwd 用户支付密码(16位md5值+盐的16位md5值)
	 * @param cls
	 * @param callBack
	 */
	public void lebiPayFiliale(String filialeId,String orderId,String money,String pwd,Class<LebiPayResult> cls,RequestCallBack<LebiPayResult> callBack) {
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "filiale_lebi");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("filiale_id", filialeId);
		params.addBodyParameter("order_id", orderId);
		params.addBodyParameter("payment_money", money);
		params.addBodyParameter("pay_pwd", pwd);
		
		postRequest(URL, params, cls, callBack);
	}

	/**
	 * 乐券支付给兑换中心
	 * @param orderId
	 * @param pwd
	 * @param cls
	 * @param callBack
	 */
	public void payCoupon(String orderId,String pwd,Class<PayCouponResult> cls,RequestCallBack<PayCouponResult> callBack){
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "pay_coupon");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("order_id", orderId);
		params.addBodyParameter("pay_pwd", pwd);
		postRequest(URL, params, cls, callBack);
	}

	/**
	 * 获取支付内容
	 * @param c 请求借口类型
	 * @param orderId 订单id
	 * @param callBack
     */
	public void payContent(String c,String orderId, RequestCallBack<PayContent> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c",c);
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("order_id", orderId);
		postRequest(URL, params, PayContent.class, callBack);
	}

	/**
	 * 获取支付方式列表 该方法废弃  使用RetrofitUtil中的getPayTypeList
	 * @param callBack
     */
	@Deprecated
	public void getPayTypeList(RequestCallBack<PayTypeListEntity> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c","recharge/paytype_v3");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("newpay","1");
		postRequest(Ip.getURL(mContext)+"mall.php",params,PayTypeListEntity.class,callBack);
	}

	/**
	 * 分润金额  --- 店内消费/扫码支付给商家/扫码支付给兑换中心
	 */
	public void getConsumerProfit(String orderId, RequestCallBack<MTConsumerProfit> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c","consumer_profit");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.gettoken(mContext));
		params.addBodyParameter("order_id", orderId);
		postRequest(URL, params, MTConsumerProfit.class, callBack);


		Logger.i("2017-1-17:" + RequestOftenKey.getDeviceIndex(mContext));
		Logger.i("2017-1-17:" + RequestOftenKey.gettoken(mContext));
		Logger.i("2017-1-17:" + orderId);
	}

}
