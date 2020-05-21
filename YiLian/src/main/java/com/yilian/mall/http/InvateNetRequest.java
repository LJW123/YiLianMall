package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.InvateEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * Created by Administrator on 2016/3/28.
 */
public class InvateNetRequest extends BaseNetRequest {

    private  String URL;
    public InvateNetRequest(Context context) {
        super(context);
        URL= Ip.getURL(context)+"activity.php";
    }

    public void invateNet(RequestCallBack<InvateEntity> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "share");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        postRequest(URL, params, InvateEntity.class, callBack);
    }

}
