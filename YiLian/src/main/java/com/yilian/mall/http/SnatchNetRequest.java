package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.MySnatchEntity;
import com.yilian.mall.entity.SnatchDetailsEntity;
import com.yilian.mall.entity.SnatchListEntity;
import com.yilian.mall.entity.SnatchMyPartEntity;
import com.yilian.mall.entity.SnatchPartEntity;
import com.yilian.mall.entity.SnatchPartRecord;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * Created by Administrator on 2016/4/5.
 */
public class SnatchNetRequest extends BaseNetRequest {

    private  String URL;
    public SnatchNetRequest(Context context) {
        super(context);
        URL= Ip.getURL(context) + "activity.php";
    }


    //夺宝列表
    public void snatchListNet(String token,int type,int page, RequestCallBack<SnatchListEntity> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "activity_snatch_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type",type+"");//1进行中 2已揭晓 0我参与的
        params.addBodyParameter("page",page+"");//分页
        postRequest(URL, params, SnatchListEntity.class, callBack);

    }

    //我的夺宝
    public void MySnatchNet(int type,int page, RequestCallBack<MySnatchEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "activity_snatch_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type",type+"");//1进行中 2已揭晓 0我参与的  3 我的夺宝
        params.addBodyParameter("page",page+"");//分页
        postRequest(URL, params, MySnatchEntity.class, callBack);
    }

    //设置中奖地址
    public void winAddressNet(String activityId,String addressId,RequestCallBack<BaseEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","activity_snatch_set_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activityId);//活动id
        params.addBodyParameter("address_id",addressId);//幸运数字
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    //夺宝详情进行中
    public void snatchingNet(String activityId,int page,RequestCallBack<SnatchPartEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","activity_snatch_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activityId);//活动id
        params.addBodyParameter("page",page+"");//分页
        postRequest(URL,params,SnatchPartEntity.class,callBack);
    }

    //我的记录
    public void MyPartNet(String token,String activityId,String status,RequestCallBack<SnatchMyPartEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","activity_snatch_my_log");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activityId);//活动id
        params.addBodyParameter("status",status+"");//活动状态 1进行中 2已揭晓
        postRequest(URL, params, SnatchMyPartEntity.class, callBack);
    }

    //夺宝详情
    public void snatchDetailNet(String activityId,RequestCallBack<SnatchDetailsEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","activity_snatch_user_log");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activityId);//活动id
        postRequest(URL, params, SnatchDetailsEntity.class, callBack);
    }

    //夺宝详情夺宝数字详情
    public void partRecordNet(String activityId,String luckNumber,RequestCallBack<SnatchPartRecord> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","activity_snatch_num_log");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activityId);//活动id
        params.addBodyParameter("luck_number",luckNumber);//幸运数字
        postRequest(URL, params, SnatchPartRecord.class, callBack);
    }

    //参加夺宝
    public void SnatchPayNet(String activity_id,String luck_number,String pwd,RequestCallBack<BaseEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "activity_snatch");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id",activity_id);//活动id
        params.addBodyParameter("luck_number",luck_number);//幸运数字
        params.addBodyParameter("pwd",pwd);//支付密码

        postRequest(URL, params,BaseEntity.class, callBack);
    }

    //设置中奖地址
    public void AddressNet(String activityId,String addressId,RequestCallBack<BaseEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c", "activity_snatch_set_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id",activityId);//活动id
        params.addBodyParameter("address_id",addressId);//用户收货地址

        postRequest(URL, params,BaseEntity.class, callBack);
    }
}
