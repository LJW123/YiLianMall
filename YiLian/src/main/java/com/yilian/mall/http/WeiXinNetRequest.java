package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.WeiXinInfoEntity;
import com.yilian.mall.entity.WeiXinLoginEntity;
import com.yilian.mall.entity.WeiXinOrderEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/3/31.
 */
public class WeiXinNetRequest extends BaseNetRequest {


    private  String URL;

    public WeiXinNetRequest(Context context) {
        super(context);
        URL= Ip.getURL(context)+"pay/wxmake_order.php";
    }

    public void WeiXinOrder(String a, String b,RequestCallBack<WeiXinOrderEntity> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("fee",a);//支付金额 单位元
        params.addBodyParameter("paymentIndex",b);//充值订单编号
        postRequest(URL, params, WeiXinOrderEntity.class, callBack);
    }

    public void WeiXinLogin(String url,RequestCallBack<WeiXinLoginEntity> callBack){
        RequestParams params=new RequestParams();

        postRequest(url, params, WeiXinLoginEntity.class, callBack);
    }

    public void WeiXinInfo(String url,RequestCallBack<WeiXinInfoEntity> callBack){
        RequestParams params=new RequestParams();

        postRequest(url, params, WeiXinInfoEntity.class, callBack);
    }
    /**
     * 客户端 openId 更新
     * @param openId
     * @param unionid
     * @param nickname
     * @param headimgurl
     * @param callBack
     */
    public void updateOpenid(String openId, String unionid,String nickname,String headimgurl,RequestCallBack<BaseEntity> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "account/update_openid");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        Logger.i("更新客户端OpenID使用token:"+RequestOftenKey.getToken(mContext));
        params.addBodyParameter("openid",openId);
        params.addBodyParameter("unionid",unionid);
        params.addBodyParameter("nickname",nickname);
        params.addBodyParameter("headimgurl",headimgurl);
        postRequest(Ip.getURL(mContext)+"mall.php", params, BaseEntity.class, callBack);
    }
}
