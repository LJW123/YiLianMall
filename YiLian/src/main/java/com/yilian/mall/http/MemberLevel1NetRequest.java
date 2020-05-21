package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.MyIncome;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;


/**
 * Created by Administrator on 2016/3/29.
 */
public class MemberLevel1NetRequest extends BaseNetRequest {

    private  String URL;

    public MemberLevel1NetRequest(Context context) {
        super(context);
        URL= Ip.getURL(context)+"mall.php";
    }

    public void MyIncomeNet(RequestCallBack<MyIncome> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "account/member_center");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("lev","");

        postRequest(URL, params, MyIncome.class, callBack);
    }
}
