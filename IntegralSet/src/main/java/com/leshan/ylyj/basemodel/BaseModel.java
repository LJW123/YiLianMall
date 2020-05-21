package com.leshan.ylyj.basemodel;


import android.content.Context;

import com.leshan.ylyj.api.APIService;
import com.leshan.ylyj.view.activity.BaseFragment;
import com.yilian.mylibrary.Ip;

import rxfamily.net.HttpService;

/**
 * @author ASUS
 */
public abstract class BaseModel {

    public HttpService httpService;
    public APIService service;


    public BaseModel(Context mContext) {
//        getToken(mContex);
//        httpService = HttpService.getInstance(mContex, Ip.getURL(mContex), false);
        httpService = HttpService.getInstance(mContext, Ip.getURL(mContext), false);
        service = httpService.getHttpService().create(APIService.class);
    }

    public BaseModel(Context mContext,boolean has_cache) {
//        getToken(mContext);
        httpService = HttpService.getInstance(mContext,Ip.getURL(mContext), has_cache);
        service = httpService.getHttpService().create(APIService.class);
    }


    public BaseModel(BaseFragment mContext) {
        httpService = HttpService.getInstance(mContext.getActivity(), Ip.getURL(mContext.getActivity()), false);
        service = httpService.getHttpService().create(APIService.class);
    }

    /**
     * 获取网络请求公共的参数
     *
     * @param context
     */
//    private void getToken(Context context){
//        this.token= PreferenceUtils.readStrConfig(Constants.SPKEY_TOKEN,context);
//        this.deviceIndex= PreferenceUtils.readStrConfig(Constants.SPKEY_DEVICE_INDEX,context);
//    }
}
