package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mall.utils.LefenHttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class BaseNetRequest {

	private static final String TAG = "BaseNetRequest";
	protected Context mContext;
	
	public BaseNetRequest(Context context) {
		super();
		this.mContext = context;
	}

	/**
	 * 共用的get请求。
	 */
	protected <T>  void getRequest(String url ,RequestParams params,Class<T> cls,RequestCallBack<T> callBack){
		LefenHttpUtils.get(mContext, cls, url, null, params, callBack);
	}
	
	/**
	 * 共用的post请求。
	 */
	protected <T> void postRequest(String URL ,RequestParams params,Class<T> cls,RequestCallBack<T> callBack){
		LefenHttpUtils.post(mContext, cls, URL, null, params, callBack);
	}

	/**
	 * 共用的post请求。
	 */
	protected <T> void postRequest(String URL ,RequestParams params,RequestCallBack<String> callBack){
		LefenHttpUtils.post(mContext, URL,  params, callBack);
	}
}
