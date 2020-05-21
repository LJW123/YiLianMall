package com.yilian.mall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.GetUserInfoEntity;
import com.yilian.mall.entity.UserRecordGatherEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.http.UserdataNetRequest;
import com.yilian.mylibrary.Constants;

public class RequestOftenKey {


    /**
     * 网络请求常用Key
     */

    public static String getDeviceIndex(Context context) {
        return PreferenceUtils.readStrConfig(Constants.SPKEY_DEVICE_INDEX, context);
    }


    /**
     * 加盐值
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        String token = PreferenceUtils.readStrConfig(Constants.SPKEY_TOKEN, context, "0");
        if (TextUtils.isEmpty(token)) {
            token="0";
        }
        Long aLong = Long.valueOf(token);
        if (aLong == 0L) {//如果不加盐值的token为0时，则返回0，不再增加盐值
            return "0";
        }
        String salt = PreferenceUtils.readStrConfig(Constants.SPKEY_SERVER_SALT, context, "0");
        String s = String.valueOf(aLong + Long.valueOf(salt));

        return s;
    }


    /**
     * 不加盐值
     *
     * @param context
     * @return
     */
    public static String gettoken(Context context) {
        String s = PreferenceUtils.readStrConfig(Constants.SPKEY_TOKEN, context);
        Logger.i("返回的不加盐token：" + s);
        return s;
    }

    ;

    public static String getServerSalt(Context context) {
        return PreferenceUtils.readStrConfig(Constants.SPKEY_SERVER_SALT, context);
    }

    public static void getUserInfor(final Context context, final SharedPreferences sp) {

        if (sp.getBoolean(Constants.SPKEY_STATE, false)) {
            new AccountNetRequest(context).getUserInfo(new RequestCallBack<GetUserInfoEntity>() {
                @Override
                public void onSuccess(ResponseInfo<GetUserInfoEntity> responseInfo) {
                    switch (responseInfo.result.code) {
                        case 1:

                            Gson gson = new Gson();
                            String s = gson.toJson(responseInfo.result);
                            String substring = s.substring(1, s.length() - 1);
                            int begin = substring.indexOf("{");
                            int end = substring.indexOf("}");
                            substring = substring.substring(begin, end + 1);
                            Logger.i("get_userInfo接口获取的用户信息：" + substring);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString(Constants.SPKEY_USER_INFO, substring);//把个人信息json字符串存储下来，以便web通过JS调用
                            GetUserInfoEntity.UserInfo userInfo = responseInfo.result.userInfo;
                            edit.putString(Constants.MERCHANT_APPLY_ID, userInfo.merchantApplyId);
                            edit.putString(Constants.MERCHANT_CHECK_STATUS, userInfo.checkStatus);
                            edit.putString(Constants.MERCHANT_REFUSE_REASON, userInfo.refundReason);
                            edit.putString(Constants.SPKEY_PHONE, userInfo.phone);
                            edit.putString("name", userInfo.name);
                            edit.putString(Constants.USER_NAME, userInfo.name);
                            edit.putString("sex", userInfo.sex);
                            edit.putString("integral", userInfo.integral);
                            edit.putString("provinceId", userInfo.province);
                            edit.putString("cityId", userInfo.city);
                            edit.putString("district", userInfo.district);
                            edit.putString("photo", userInfo.photo);
                            edit.putString("card_index", userInfo.cardIndex);
                            edit.putString("card_name", userInfo.cardName);
                            edit.putBoolean(Constants.PAY_PASSWORD, userInfo.payPassword);
                            edit.putBoolean("login_password", userInfo.loginPassword);
                            edit.putString("lebi", userInfo.lebi);
                            edit.putString("coupon", userInfo.coupon);
                            edit.putString("provinceName", userInfo.provinceName);
                            edit.putString("provinceId", userInfo.province);
                            edit.putString("cityName", userInfo.cityName);
                            edit.putString("cityId", userInfo.city);
                            edit.putString("districtId", userInfo.district);
                            edit.putString("districtName", userInfo.districtName);
                            edit.putString("yyCardUrl", responseInfo.result.cardUrl);
                            edit.putString("voucher", userInfo.voucher);
                            edit.putString("weChatIsBind", userInfo.weChatIsBind);
                            edit.putString("zhiFuBaoIsBind", userInfo.zhiFuBaoIsBind);
                            edit.putString(Constants.USERURL, responseInfo.result.url);
                            edit.putString(Constants.JWT, userInfo.jwt);
                            edit.putString(Constants.JWT_EXPIRES_IN, userInfo.jwtExpiresIn);
                            edit.putString(Constants.STATE_OF_MIND, userInfo.stateOfMind);
                            edit.putString(Constants.IS_CERT, userInfo.isCert);
                            if ("1".equals(userInfo.voucherAuth)) {//有零购券权限
                                edit.putBoolean(Constants.VOUCHERAUTH, true);
                            } else {//无零购券权限
                                edit.putBoolean(Constants.VOUCHERAUTH, false);
                            }
                            edit.putBoolean(com.yilian.mylibrary.Constants.HAS_MESSAGE, "0".equals(userInfo.hasNews) ? false : true);
                            Logger.i("存储的推荐人手机号码：" + userInfo.superiorPhone);
                            edit.putString(Constants.REFERRER_PHONE, userInfo.superiorPhone);//推荐人手机号码
                            edit.apply();
                            PushManager.getInstance().bindAlias(context, userInfo.userId);//绑定个推别名
                            Logger.i("userId 获取---" + userInfo.userId);
                            PreferenceUtils.writeStrConfig(Constants.USER_ID, userInfo.userId, context);

                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });

        }
    }


    /**
     * 获取用户各项记录数量,获取用户信息接口get_userInfo获取信息不完整，补充user_base_data接口，期待有一天，这两个接口合并
     */

    public static void getUserRecordCounts(Context mContext, SharedPreferences sp) {
        UserdataNetRequest userDataRequest = new UserdataNetRequest(mContext);
        userDataRequest.getUserRecordGather(UserRecordGatherEntity.class, new RequestCallBack<UserRecordGatherEntity>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<UserRecordGatherEntity> arg0) {

                UserRecordGatherEntity result = arg0.result;
                Logger.json(result.toString());
                switch (result.code) {
                    case 1:
                        SharedPreferences.Editor edit = sp.edit();
                        //先设置再获取，已获取实时数据
                        edit.putString("phone", result.phone);
                        edit.putString("name", result.name);
                        edit.putString("availableMoney", result.availableLebi);
                        edit.putBoolean("state", true);
                        edit.putString("photo", result.photo);
                        edit.putString("integral", result.balance);
                        edit.putFloat("income", result.income);
                        edit.putString("memberCount", result.memberCount);
                        edit.putString("lebi", result.lebi);//奖励
                        edit.putString("coupon", result.coupon);//兑换券
                        edit.putString("voucher", result.voucher);//零购券
                        edit.putBoolean(com.yilian.mylibrary.Constants.HAS_MESSAGE, "0".equals(result.hasNews) ? false : true);
                        edit.putString(Constants.JIFEN, result.integral);//奖券
                        edit.putString(Constants.MERCHANT_LEV, result.lev);
                        Logger.i("result,Lev  " + result.lev);
                        edit.putString(Constants.LEV_NAME, result.levName);
                        edit.putString(Constants.MERCHANT_ID, result.merchantId);
                        edit.putString(Constants.AGENT_ID, result.agentId);
                        edit.putString(Constants.AGENT_URL, result.agentUrl);
                        edit.putString(Constants.REQUIREDCASH, result.requiredCash);
                        edit.putString(Constants.QYURL, result.qyUrl);
                        edit.putString(Constants.MER_STATUS, result.merStatus);
                        edit.putString(Constants.MER_REFUSE_REASON, result.refuse);
                        edit.putString(Constants.AGENT_STATUS, result.agentApply);
                        edit.putString(Constants.AGENT_REGION, result.agentRegion);
                        edit.putString(Constants.YI_DOU_BAO, result.yiDouBao);
                        edit.putString(Constants.YI_DOU_BAO_URL, result.yiDouBaoUrl);
                        edit.putString(Constants.YI_DOU_JUMP, result.yiDouJump);
                        Logger.i("存储的yiDouBaoUrl：" + result.yiDouBaoUrl);
                        edit.apply();

                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        });
    }
}
