package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.RecommendAppEntity;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * Created by  on 2016/9/8 0008.
 * 获取推荐应用
 */
public class RecommendNetRequest extends BaseNetRequest {

    public  String URL;
    public RecommendNetRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "mall.php";
    }

    /**
     * 获取推荐应用
     * type  app类型 0 android 1 ios
     */
    public  void getRecommendApp(RequestCallBack<RecommendAppEntity> callBack){

        RequestParams params = new RequestParams();

        params.addBodyParameter("c","recommended_app");
        params.addBodyParameter("type","0");
        postRequest(URL,params, RecommendAppEntity.class,callBack);
    }
}
