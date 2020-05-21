package com.yilian.mall.http;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.imentity.IMGroupInfoEntity;
import com.yilian.mall.entity.imentity.IMGroupMembersInfoEntity;
import com.yilian.mall.entity.imentity.IMMemberInfoEntity;
import com.yilian.mall.entity.imentity.IMTokenEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Ip;

/**
 * author XiuRenLi on 2016/8/16  PRAY NO BUG
 */

public class IMNetRequest extends BaseNetRequest {
    private  String URL;

    public IMNetRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "im.php";
    }

    /**
     * 获取融云Token
     * @param callBack
     */
    public void getIMToken(RequestCallBack<IMTokenEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","get_rc_token");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));//加盐值的token
        Logger.i("获取融云IMToken的参数:"+"device_index="+RequestOftenKey.getDeviceIndex(mContext)+"   IMToken="+RequestOftenKey.getToken(mContext));
        postRequest(URL,params, IMTokenEntity.class,callBack);
    }

    /**
     * 获取群组信息，返回值包括 乐友群ID 和 上级的乐友群ID
     * @param type  类型  1获取我参与的乐友群 2获取我的乐友群 3获取上级下级群组
     * @param callBack
     */
    public void getIMGroupInfo(String type,String userid,RequestCallBack<IMGroupInfoEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","get_group");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));//加盐值的token
//        String userid = mContext.getSharedPreferences("UserInfor", 0).getString(Constants.SPKEY_IM_USERID, "");
        params.addBodyParameter("user_id", userid);
        params.addBodyParameter("type",type);
        postRequest(URL,params, IMGroupInfoEntity.class,callBack);
    }

    /**
     * 获取群成员信息
     * @param groupID  群ID
     * @param offset 偏移量
     * @param count 每次取得数量
     * @param callBack
     */
    public void getIMGroupMemberInfo(String groupID, String offset, String count, RequestCallBack<IMGroupMembersInfoEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","get_group_members");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));//加盐值的token
        params.addBodyParameter("group_id",groupID);
        params.addBodyParameter("offset",offset);
        params.addBodyParameter("count",count);
        postRequest(URL,params,IMGroupMembersInfoEntity.class,callBack);
    }

    /**
     * 更新用户信息  修改昵称和更换头像之后调用  通知融云用户修改了信息
     * @param callBack
     */
    public void refreshUserInfo(RequestCallBack<BaseEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","refresh_user");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));//加盐值的token
        postRequest(URL,params, BaseEntity.class,callBack);
    }

    /**
     * 根据用户ID获取用户详情，没有会员关系限制
     * @param userId
     * @param callBack
     */
    public void getMemberInfo(String userId,RequestCallBack<IMMemberInfoEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","account/member_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));//加盐值的token
        params.addBodyParameter("user_id",userId);//加盐值的token
        postRequest(Ip.getURL(mContext)+"mall.php",params, IMMemberInfoEntity.class,callBack);
    }

}
