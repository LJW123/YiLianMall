package com.yilian.mall;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.entity.RedPacketWhetherEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.mall.MyApplication.sp;

/**
 * Created by  on 2017/4/14 0014.
 * 各种登录方式登录成功后的逻辑处理
 * 包括登录状态存储，APPtoken存储，获取用户信息，阿里客服信息获取，融云token的获取，属于手机号快捷或密码登录的还要存储手机号码
 */

public class LoginSuccessFlow {

    private static volatile LoginSuccessFlow loginSuccessFlow;

    private LoginSuccessFlow() {
    }

    public static LoginSuccessFlow getInstance() {
        if(loginSuccessFlow==null){
            synchronized (LoginSuccessFlow.class){
                if(loginSuccessFlow==null){
                    loginSuccessFlow = new LoginSuccessFlow();
                }
            }
        }
        return loginSuccessFlow;
    }

    /**
     * 登录成功后的逻辑处理
     *
     * @param loginEntity 登录接口返回的entity
     * @param mContext
     * @param phone       手机号码登录时传递手机号码，其他登录传递空字符串
     */
    public void loginsuccessFlow(LoginEntity loginEntity, Context mContext, String phone) {
        Logger.i("第二次存入的token:"+loginEntity.token);
        PreferenceUtils.writeStrConfig(Constants.SPKEY_TOKEN, loginEntity.token, mContext);
        PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, true, mContext);
        if (!TextUtils.isEmpty(phone)) {
            PreferenceUtils.writeStrConfig(Constants.SPKEY_PHONE, phone, mContext);
        }
        //赠送抵扣券大于0则代表新用户，等于0则代表老用户
        PreferenceUtils.writeStrConfig("given_coupon", String.valueOf(loginEntity.givenCoupon), mContext);
        if (loginEntity.givenCoupon > 0) {
            mContext.sendBroadcast(new Intent("com.yilian.isNewUser"));
        }
        RequestOftenKey.getUserInfor(mContext, sp);
        RequestOftenKey.getUserRecordCounts(mContext,sp);

        //登录成功后检测是否需要弹出奖励框
//        isShowRedWindow(mContext);
    }

    public void isShowRedWindow(Context mContext) {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                RetrofitUtils2.getInstance(mContext).whetherShowRedDialog(new Callback<RedPacketWhetherEntity>() {
                    @Override
                    public void onResponse(Call<RedPacketWhetherEntity> call, Response<RedPacketWhetherEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                RedPacketWhetherEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        boolean isShow;
                                        String redPacketId = "";
                                        switch (entity.isAlert) {
                                            case "0":
                                                isShow = false;
                                                break;
                                            case "1":
                                                isShow = true;
                                                redPacketId = entity.cashBonusId;
                                                break;
                                            default:
                                                isShow = false;
                                                break;
                                        }
                                        PreferenceUtils.writeBoolConfig(Constants.RED_PACKET_SHOW, isShow, mContext);
                                        PreferenceUtils.writeStrConfig(Constants.RED_PACKET_ID, redPacketId, mContext);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RedPacketWhetherEntity> call, Throwable t) {
                    }
                });
            }
        });
    }
}
