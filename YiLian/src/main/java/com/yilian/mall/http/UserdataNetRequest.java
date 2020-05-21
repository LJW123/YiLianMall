package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.CouponExpendListBean;
import com.yilian.mall.entity.CouponGainListBean;
import com.yilian.mall.entity.RecordLebipayListBean;
import com.yilian.mall.entity.RecordLegouListBean;
import com.yilian.mall.entity.UserRecordGatherEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;

/**
 * 我的 记录部分联网方法
 * 
 * @author Administrator
 */
public class UserdataNetRequest extends BaseNetRequest {

	private  String URL;

	public UserdataNetRequest(Context context) {
		super(context);
		URL = Ip.getURL(context) + "userdata.php";
	}


	/**
	 * 乐购记录（人民币支付）
	 * 
	 * @param cls
	 * @param callBack
	 */
	public void getLegouRecordList(Class<RecordLegouListBean> cls, RequestCallBack<RecordLegouListBean> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "legou_list");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		postRequest(URL, params, cls, callBack);
	} 

	/**
	 * 乐币支付记录
	 * 
	 * @param cls
	 * @param callBack
	 */
	public void getLebipayRecordList(Class<RecordLebipayListBean> cls,
			RequestCallBack<RecordLebipayListBean> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "lebi_list");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		postRequest(URL, params, cls, callBack);
	}

    /**
     * 获取用户各项记录数量
     *
     * @param cls
     * @param callBack
     */
    public void getUserRecordGather(Class<UserRecordGatherEntity> cls, RequestCallBack<UserRecordGatherEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "user_base_data_v1");
        params.addBodyParameter("app_versions", "3");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        Logger.i("请求各项记录参数：deviceIndex:" + RequestOftenKey.getDeviceIndex(mContext) + "  token:" + RequestOftenKey.getToken(mContext));
		Logger.i("请求各项记录参数api: https://yilian.lefenmall.net/app/userdata.php?c=user_base_data_v1&app_versions=3&device_index=" + RequestOftenKey.getDeviceIndex(mContext)
				+ "&token=" + RequestOftenKey.getToken(mContext));
		postRequest(URL, params, cls, callBack);
	}


	/**
	 * 乐享币获得记录
	 * @param cls
	 * @param callBack
	 */
	public void getCouponGainList(Class<CouponGainListBean> cls, RequestCallBack<CouponGainListBean> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "coupon_gain_list");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		postRequest(URL, params, cls,callBack);
	}

	/**
	 * 乐享币兑换记录
	 * @param cls
	 * @param callBack
	 */
	public void getCouponExpendList(Class<CouponExpendListBean> cls, RequestCallBack<CouponExpendListBean> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "coupon_expend_list");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		postRequest(URL, params, cls,callBack);
	}
}
