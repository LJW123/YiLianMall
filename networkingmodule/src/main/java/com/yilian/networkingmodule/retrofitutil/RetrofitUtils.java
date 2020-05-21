package com.yilian.networkingmodule.retrofitutil;/**
 * Created by  on 2017/3/3 0003.
 */


import android.content.Context;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JsPayClass;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.BuildConfig;
import com.yilian.networkingmodule.entity.AliPayAuthorInfoEntity;
import com.yilian.networkingmodule.entity.AliPayUserIdEntity;
import com.yilian.networkingmodule.entity.AssetsRecordList;
import com.yilian.networkingmodule.entity.BalanceScreenEntity;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CategoryHeaderEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.FavoriteEntity;
import com.yilian.networkingmodule.entity.GoodsListEntity;
import com.yilian.networkingmodule.entity.IndustryEntity;
import com.yilian.networkingmodule.entity.JPFragmentGoodEntity;
import com.yilian.networkingmodule.entity.JPMainHeaderEntity;
import com.yilian.networkingmodule.entity.JPShopEntity;
import com.yilian.networkingmodule.entity.LimitBuyMakeOrderEntity;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.entity.MTHomePageEntity;
import com.yilian.networkingmodule.entity.MemberRelationSystemEntity;
import com.yilian.networkingmodule.entity.MerchantAddressInfo;
import com.yilian.networkingmodule.entity.MerchantCashPayEntity;
import com.yilian.networkingmodule.entity.MerchantCenterInfo;
import com.yilian.networkingmodule.entity.MerchantData;
import com.yilian.networkingmodule.entity.MerchantDiscountChangeRecordEntity;
import com.yilian.networkingmodule.entity.MerchantDiscountEntity;
import com.yilian.networkingmodule.entity.MerchantMoneyRecordEntity;
import com.yilian.networkingmodule.entity.MerchantOffLineOrderEntity;
import com.yilian.networkingmodule.entity.MerchantPayEntity;
import com.yilian.networkingmodule.entity.MerchantPhotoAlbum;
import com.yilian.networkingmodule.entity.MerchantPhotoAlbumName;
import com.yilian.networkingmodule.entity.MerchantQrCodeEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.MyGroupEntity;
import com.yilian.networkingmodule.entity.MyGroupsListEntity;
import com.yilian.networkingmodule.entity.PayTypeEntity;
import com.yilian.networkingmodule.entity.PaymentIndexEntity;
import com.yilian.networkingmodule.entity.PrizeGetResultEntity;
import com.yilian.networkingmodule.entity.RecommendInfoEntity;
import com.yilian.networkingmodule.entity.RegionEntity;
import com.yilian.networkingmodule.entity.RegisterDevice;
import com.yilian.networkingmodule.entity.RetailPayEntity;
import com.yilian.networkingmodule.entity.SearchCommodityEntity;
import com.yilian.networkingmodule.entity.SearchListEntity;
import com.yilian.networkingmodule.entity.SpellGroupDetailsEntity;
import com.yilian.networkingmodule.entity.SpellGroupOrderEntity;
import com.yilian.networkingmodule.entity.SpellGroupOrderInfoEntity;
import com.yilian.networkingmodule.entity.SpellGroupShareListEntity;
import com.yilian.networkingmodule.entity.ThirdPartyNewUserEntity;
import com.yilian.networkingmodule.entity.TransferIntegralEntity;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.entity.UserBaseDataEntity;
import com.yilian.networkingmodule.entity.UserInfoEntity;
import com.yilian.networkingmodule.entity.WeiXinInfoEntity;
import com.yilian.networkingmodule.entity.WeiXinLoginEntity;
import com.yilian.networkingmodule.entity.WeiXinOrderEntity;
import com.yilian.networkingmodule.entity.WheelOfFortuneAwardListEntity;
import com.yilian.networkingmodule.entity.WheelOfFortunePrizeListEntity;
import com.yilian.networkingmodule.entity.WheelOfFortuneResultEntity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by  on 2017/3/3 0003.
 */
public class RetrofitUtils {

    private static volatile RetrofitUtils retrofitUtils;
    private static RetrofitService retrofitService;
    private String deviceIndex;
    private String token;

    private RetrofitUtils() {

    }

    public static RetrofitUtils getInstance(final Context context) {
        if (retrofitUtils == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofitUtils == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
                    if (BuildConfig.DEBUG) {
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    } else {
                        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
                    }
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.connectTimeout(30, TimeUnit.SECONDS); //超时链接的时间
                    httpClient.readTimeout(30, TimeUnit.SECONDS);
                    httpClient.writeTimeout(30, TimeUnit.SECONDS);
// add your other interceptors …
// add logging as last interceptor
                    httpClient.addInterceptor(logging)
                            //                    统一参数拦截器
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request oldRequest = chain.request();
                                    HttpUrl.Builder builder = oldRequest.url().newBuilder()
                                            .scheme(oldRequest.url().scheme())
                                            .host(oldRequest.url().host())
                                            .addQueryParameter("token", RequestOftenKey.getToken(context))
                                            .addQueryParameter("device_index", RequestOftenKey.getDeviceIndex(context));
                                    Request newRequest = oldRequest.newBuilder().method(oldRequest.method(), oldRequest.body())
                                            .url(builder.build())
                                            .build();
                                    return chain.proceed(newRequest);
                                }
                            });
                    retrofitUtils = new RetrofitUtils();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Ip.getURL(context))
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    retrofitService = retrofit.create(RetrofitService.class);
                }
            }
        }

        return retrofitUtils;
    }


    public RetrofitUtils setDeviceIndex(String deviceIndex) {
        this.deviceIndex = deviceIndex;
        return retrofitUtils;
    }

    public RetrofitUtils setToken(String token) {
        this.token = token;
        return retrofitUtils;
    }

    /**
     * 注册设备
     *
     * @param uuid        设备的唯一ID
     * @param osVersion   操作系统版本
     * @param deviceBrand 设备品牌
     * @param deviceModel 设备型号
     * @param deviceRam   设备内存型号或大小
     * @return
     */
    public void registerDevice(String uuid, String osVersion, int scrWidth, int scrHeight, String deviceBrand, String deviceModel, String deviceRam, Callback<RegisterDevice> callback) {
        retrofitService.registerDevice("register_new_device", uuid, "0", osVersion, "0", scrWidth + "×" + scrHeight, deviceBrand, deviceModel, "0", deviceRam)
                .enqueue(callback);
    }

    /**
     * 账号密码登录
     */
    public void loginByPassWord(String account, String pwd, Callback<LoginEntity> callback) {
        Call<LoginEntity> login_by_password = retrofitService.login("login_by_password", deviceIndex, account, pwd);
        login_by_password.enqueue(callback);
    }

    /**
     * 检测第三方账号是否是新用户
     *
     * @param oauthId
     * @return
     */
    public void checkThirdPartyRecord(String oauthId, Callback<ThirdPartyNewUserEntity> callback) {
        Call<ThirdPartyNewUserEntity> newuser = retrofitService.checkThirdPartyRecord("newrecord", deviceIndex, oauthId);
        newuser.enqueue(callback);
    }

    /**
     * 第三方登录（第三方登录后，通过第三方返回的ID类信息，获取token）
     *
     * @param oauthId  微信是unionid（移动端和微信端的openId不同，不能使用）,支付宝是授权商户的userId
     * @param type     0微信 1支付宝
     * @param appType  微信端传2 客户端可不传或传1 这里选择传1
     * @param userNick 用户昵称
     * @param userHead 用户头像地址
     * @param openId   微信open_id 只在微信登陆时候传
     * @param newUser  如果该第三方账号是老账号，不与手机号绑定，该字段传空字符串，如果该第三方账号是新账号，可以选择是否绑定号码，绑定时该字段传"1"，不绑定时传"2"
     * @param phone    绑定的手机号
     * @param smsCode  验证码
     */
    public void loginByThirdParyt(String oauthId, String type, String appType, String userNick, String userHead, String openId, String newUser, String phone, String smsCode, Callback<LoginEntity> callback) {
        retrofit2.Call<LoginEntity> login_by_third = retrofitService.loginByThirdParyt("login_by_third", deviceIndex, oauthId, type, appType,
                userNick, userHead, openId, newUser, phone, smsCode);
        login_by_third.enqueue(callback);
    }

    /**
     * 会员体系判断
     *
     * @param phone
     * @return
     */
    public void getMemberRelationSystem(String phone, Callback<MemberRelationSystemEntity> callback) {
        Call<MemberRelationSystemEntity> member_relation_system = retrofitService.getMemberRelationSystem("member_relation_system", deviceIndex, token, phone);
        member_relation_system.enqueue(callback);
    }

    /**
     * 快捷登录
     *
     * @param phone   手机号码
     * @param smsCode 短信验证码
     * @param sign    推荐人手机号
     * @return
     */
    public void loginBySmsCode(String phone, String smsCode, String sign, Callback<LoginEntity> callback) {
        Call<LoginEntity> login_by_quick = retrofitService.loginBySmsCode("login_by_quick", deviceIndex, phone, smsCode, sign);
        login_by_quick.enqueue(callback);
    }

    /**
     * @param phone      用户填写的手机号
     * @param verifyType 请求验证码的类型，0表示注册验证，1表示快捷登录验证，2表示账号操作安全验证 3表示未登录 4绑定手机号
     * @param verifyCode 图形验证码
     * @param voice      null(即请求不带该参数)发送短信验证码  1发送语音验证码
     * @param type       短信验证码type类型 type = 1 快捷登陆 type = 2 修改绑定手机号 type = 3 重置支付密码 type = 4 找回登录密码
     *                   type = 5 领奖励申请 type = 6 绑定银行卡 type = 7 设置支付密码 type = 8 绑定手机号 type=9 设置支付密码
     * @return
     */
    public void getSmsCode(String phone, String verifyType, String verifyCode, String voice, String type, Callback<BaseEntity> callback) {
        Call<BaseEntity> get_sms_code_v2 = retrofitService.getSmsCode("get_SMS_code_v2", deviceIndex, token, phone, verifyType, verifyCode, voice, type);
        get_sms_code_v2.enqueue(callback);
    }


    /**
     * 判断手机号是否已经存在
     *
     * @param phone
     * @return
     */
    public void checkUser(String phone, Callback<CheckUserEntity> callback) {
        Call<CheckUserEntity> check_user = retrofitService.checkUser("check_user", phone);
        check_user.enqueue(callback);
    }

    /**
     * 检验 验证码是否正确
     *
     * @param phone
     * @param smsCode
     * @return
     */
    public void checkSmsCode(String phone, String smsCode, Callback<BaseEntity> callback) {
        Call<BaseEntity> verify_sms_code = retrofitService.checkSmsCode2("verify_SMS_code", phone, smsCode);
        verify_sms_code.enqueue(callback);
    }

    /**
     * 重新设置登录密码
     *
     * @param account
     * @param loginPassword
     * @return
     */
    public void resetLoginPassword(String account, String loginPassword, Callback<BaseEntity> callback) {
        Call<BaseEntity> reset_login_password_ios = retrofitService.resetLoginPassword("reset_login_password_ios", account, loginPassword);
        reset_login_password_ios.enqueue(callback);
    }

    /**
     * 微信登录时获取微信返回的一些信息
     * String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
     * + Constants.APP_ID
     * + "&secret="
     * + Constants.WEIXIN_APPSECRET
     * + "&code="
     * + code
     * + "&grant_type=authorization_code";
     *
     * @return
     */
    public void getWeiXinLoginEntity(String appId, String secret, String code, String grantType, Callback<WeiXinLoginEntity> callback) {
        Call<WeiXinLoginEntity> weiXinLoginEntity = retrofitService.getWeiXinLoginEntity("https://api.weixin.qq.com/sns/oauth2/access_token", appId, secret, code, grantType);
        weiXinLoginEntity.enqueue(callback);
    }

    /**
     * 用微信返回的access_token和openid通过微信官方提供的接口换取的微信用户信息
     * String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
     *
     * @return
     */
    public void getWeiXinUserInfo(String accessToken, String openId, Callback<WeiXinInfoEntity> callback) {
        Call<WeiXinInfoEntity> weiXinUserInfo = retrofitService.getWeiXinUserInfo("https://api.weixin.qq.com/sns/userinfo", accessToken, openId);
        weiXinUserInfo.enqueue(callback);
    }

    /**
     * 已登录账号绑定其他第三方账号
     *
     * @param account  绑定手机号时传递空字符串 绑定微信传递微信的unionID 绑定支付宝传递支付宝的userId
     * @param type     绑定类型 0手机 1微信 2支付宝
     * @param userNick 第三方账号的 用户昵称 , 没有就传空
     * @param userHead 第三方账号的 用户头像地址 ,没有就传空
     * @param openId   绑定微信时传递openID 绑定手机和支付宝时传空
     * @return
     */
    public void bindThirdPartyAccount(String account, String type, String userNick, String userHead, String openId, String smsCode, String sign, Callback<BaseEntity> callback) {
        Call<BaseEntity> bind_account = retrofitService.bindThirdPartyAccount("bind_account", deviceIndex, token, account, type, userNick, userHead, openId, smsCode, sign);
        bind_account.enqueue(callback);
    }

    /**
     * 获取支付宝快捷登陆infoStr接口
     *
     * @return
     */
    public void getAliPayAuthorInfo(Callback<AliPayAuthorInfoEntity> callback) {
        Call<AliPayAuthorInfoEntity> alipay_oauth_request = retrofitService.getAliPayAuthorInfo("alipay_oauth_request");
        alipay_oauth_request.enqueue(callback);
    }

    /**
     * 支付宝授权登录后获取用户支付宝的userId
     *
     * @param source
     * @param scope
     * @param authCode
     * @return
     */
    public void getAliPayUserId(String appId, String source, String scope, String authCode, Callback<AliPayUserIdEntity> callback) {
        Call<AliPayUserIdEntity> alipay_oauth_login = retrofitService.getAliPayUserId("alipay_oauth_login", appId, source, scope, authCode);
        alipay_oauth_login.enqueue(callback);
    }
    /**
     * 登录阿里客服
     * 该方法必须在获取token，并将token存储到本地sp后使用
     * 多出登录操作，Callback中执行的操作是相同的，所以Callback不作为参数传递过来，而是直接在该方法中new
     *
     * @throws Exception
     */
//    public void getAliAccountInfo() throws Exception {
//
//
//        if (context == null) {
//            throw new Exception("RetrofitUtils mContext is null");
//        } else {
//            Call<AliLoginUserInfo> loginAliIMCall = retrofitService.getAliAccountInfo("chat/user_add", deviceIndex, token);
//            loginAliIMCall.enqueue(new Callback<AliLoginUserInfo>() {
//                @Override
//                public void onResponse(Call<AliLoginUserInfo> call, Response<AliLoginUserInfo> response) {
//                    AliLoginUserInfo body = response.body();
////                    Logger.i("Login ali info:" + body.toString());
//                    if (CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
//                        AliLoginUserInfo.DataBean data = body.data;
//                        String password = data.password;
//                        String userid = data.userid;//ALi userId
//                        Logger.i("登录获取ALIuserid:" + userid + "登录获取ALIpassword:" + password);
////                                将Ali UserId存储为sp值，以便后续使用
//                        sp.edit().putString(Constants.ALI_USER_ID, userid).commit();
//                        //此对象获取到后，保存为全局对象，供APP使用
//                        //此对象跟用户相关，如果切换了用户，需要重新获取
//                        YWIMKit mIMKit = YWAPI.getIMKitInstance(userid, Constants.ALI_APP_KEY);
//                        if (mIMKit != null) {
//                            LeFenMallApplication.getInstance().setYWIMKit(mIMKit);
//                            Logger.i("不空");
//                        } else {
//                            Logger.i("空啊空");
//                        }
//                        /**
//                         * 登录阿里
//                         */
//                        IYWLoginService loginService = null;
//                        try {
//                            loginService = LeFenMallApplication.getInstance().getYWIMKit().getLoginService();
//                        } catch (Exception e) {
//                            Logger.i("ali loginByPassWord " + e.getMessage());
//                            e.printStackTrace();
//                        }
//                        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
//                        loginService.loginByPassWord(loginParam, new IWxCallback() {
//                            @Override
//                            public void onSuccess(Object... arg0) {
//                                Logger.i("Ali loginByPassWord success:" + arg0.toString());
//                            }
//
//                            @Override
//                            public void onProgress(int arg0) {
//                                // TODO Auto-generated method stub
//                            }
//
//                            @Override
//                            public void onError(int errCode, String description) {
//                                //如果登录失败，errCode为错误码,description是错误的具体描述信息
//                                Logger.i("Ali loginByPassWord error" + errCode + "   detail info:" + description);
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AliLoginUserInfo> call, Throwable t) {
//                    Logger.i("Login aLi error" + t.getMessage());
//                }
//            });
//        }
//    }

    /**
     * 退出阿里客服账号
     */
//    public void logOutAli() {
//        try {
//            LeFenMallApplication.getInstance().getYWIMKit().getLoginService().logout(new IWxCallback() {
//                @Override
//                public void onSuccess(Object... objects) {
//                    Logger.i("ali logout success");
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    Logger.i("ali logout error + " + s);
//                }
//
//                @Override
//                public void onProgress(int i) {
//
//                }
//            });
//        } catch (Exception e) {
//            Logger.i("ali logout " + e.getMessage());
//            e.printStackTrace();
//        }
//    }


    /**
     * 获取用户信息
     */
//    public void getUserInfo(Callback<T> callback) {
//        Call<T> get_userInfo = retrofitService.getUserInfo("get_userInfo", deviceIndex, token);
//        get_userInfo.enqueue(callback);
//    }
//
//    /**
//     * 获取客服信息
//     *
//     * @param type 1商品详情，2商城订单详情，3商家套餐订单详情，4商家详情，5旗舰店详情
//     * @param num  type=1时商品id和兑换中心id用'+'分割如（1314+25）或者（1312+0）,其余type时不用拼接，订单id为短的
//     * @return
//     */
//    public Call<AliCustomerServiceInfo> getAliCustomerServiceInfo(String type, String num) {
//        Call<AliCustomerServiceInfo> aliCustomerServiceInfo = retrofitService.getAliCustomerServiceInfo("chat/service_type", deviceIndex, token, type, num);
//        Logger.i("获取阿里客服信息参数：type:" + type + "  num" + num + "  deviceIndex:" + deviceIndex + "  token:" + token);
//        return aliCustomerServiceInfo;
//    }
//
//    /**
//     * 获取仿美团版本首页信息
//     *
//     * @param cityId   用户定位城市ID，必须是具体的城市ID
//     * @param countyId 用户定位区县id
//     * @param callback
//     */
//    public void getLeFenMTHomePageData(String cityId, String countyId, Callback<LeFenMTHomePageEntity> callback) {
//        retrofitService.getLeFenMTHomePageData("appindex_v4", deviceIndex, token, cityId, countyId).enqueue(callback);
//    }
//
//    /**
//     * 获取附近商家列表 ,兑换中心列表,借口调用更换了c的值返回值多增加一个是否配送
//     *
//     * @param page        页码，默认0
//     * @param industryTop 联盟商家行业顶级分类，默认0表示全部分类   兑换中心为 0
//     * @param industrySon /联盟商家行业二级分类，默认0   兑换中心为 0
//     * @param callback
//     */
//    public void getNearByShops(int page, int count, String industryTop, String industrySon, String cityId, String countyId, Callback<ShopsEntity> callback) {
//        retrofitService.getNearByShops("package/merchant_list", deviceIndex, token,
//                String.valueOf(page), String.valueOf(count), industryTop, industrySon, cityId, countyId).enqueue(callback);
//    }
//
//    /**
//     * 获取限时抢购商品信息列表
//     *
//     * @param cityId   用户定位城市ID，必须是具体的城市ID
//     * @param countyId 用户定位区县id
//     * @param round    0进行时场次 1上一个抢购场次 2下一场
//     * @param page
//     * @param count
//     * @return
//     */
//    public void getFlashSaleList(String cityId, String countyId, String round, String page, String count, Callback<FlashSaleEntity> callback) {
//        retrofitService.getFlashSaleList("activity/flash_sale", deviceIndex, token, cityId, countyId, round, page, count).enqueue(callback);
//    }
//
//    /**
//     * 搜索附近商家
//     *
//     * @param cityId   用户定位城市ID，必须是具体的城市ID
//     * @param countyId 用户定位区县id,默认0
//     * @param keyword  关键字
//     * @param callback
//     */
//    public void searchNearByShops(String cityId, String countyId, String keyword, Callback<ShopsEntity> callback) {
//        retrofitService.searchNearByShops("search", deviceIndex, cityId, countyId, keyword).enqueue(callback);
//    }
//
//    /**
//     * 支付限时抢购商品
//     *
//     * @param goodsId 抢购商品id
//     * @param payPwd  支付密码
//     * @return
//     */
//    public void payForFlashSale(String goodsId, String payPwd, Callback<FlashSalePayResult> callback) {
//        retrofitService.payForFlashSale("activity/pay_order", deviceIndex, token, goodsId, payPwd).enqueue(callback);
//    }

    /**
     * 获取大转盘获奖名单列表
     *
     * @param type  0个人用户中奖列表，1轮播中奖列表
     * @param round 0未领取,1已兑换,2已失效。 type=0 传此参数 否则传null
     * @param page  type=0 传此参数 否则传null
     * @param count type=0 传此参数 否则传null
     * @return
     */
    public void getWheelOfFortuneAwardList(String type, String round, String page, String count, Callback<WheelOfFortuneAwardListEntity> callback) {
        Logger.i("device_index  " + deviceIndex + "  token " + token);
        Call<WheelOfFortuneAwardListEntity> wheelOfFortunePrizeList = retrofitService.getWheelOfFortuneAwardList("activity/prize_turn_list", deviceIndex, token, type, round, page, count);
        wheelOfFortunePrizeList.enqueue(callback);
    }

    /**
     * 获取大转盘奖品列表
     *
     * @return
     */
    public void getWheelOfFortunePrizeList(Callback<WheelOfFortunePrizeListEntity> callback) {
        Call<WheelOfFortunePrizeListEntity> wheelOfFortunePrizeList = retrofitService.getWheelOfFortunePrizeList("activity/turn_goods_list", deviceIndex, token);
        wheelOfFortunePrizeList.enqueue(callback);
    }

    /**
     * 获取大转盘抽奖结果
     *
     * @return
     */
    public void getWheelOfFortuneResult(String version, Callback<WheelOfFortuneResultEntity> callback) {
        Call<WheelOfFortuneResultEntity> wheelOfFortuneResult = retrofitService.getWheelOfFortuneResult("activity/join_turn", deviceIndex, token, version);
        wheelOfFortuneResult.enqueue(callback);
    }

    /**
     * 确认领取中将物品
     *
     * @param goods_id
     * @param order_remark
     * @param address_id
     * @param order_id
     * @param sku_index
     */
    public void getPrizeGoodsResult(String goods_id, String order_remark, String address_id, String order_id, String sku_index, Callback<PrizeGetResultEntity> callback) {
        Call<PrizeGetResultEntity> getPrizeResult = retrofitService.getPrizeGoodsResult("activity/make_turn_order", deviceIndex, token, goods_id, order_remark,
                address_id, order_id, sku_index);
        getPrizeResult.enqueue(callback);
    }

    /**
     * 获取商家相册的标题头
     *
     * @param merchant_id
     * @param callback
     */
    public void getMerchantPackageAlbumTitleName(String merchant_id, Callback<MerchantPhotoAlbumName> callback) {
        retrofitService.getMerchantPackageAlbumTitleName("package/merchantPackageAlbumTitle", deviceIndex, token, merchant_id).enqueue(callback);
    }

    /**
     * 获取商家的相册
     *
     * @param page
     * @param count
     * @param merchant_id
     * @param group_id
     * @param callback
     */
    public void getMerchantPackageAlbum(String page, String count, String merchant_id, String group_id, Callback<MerchantPhotoAlbum> callback) {
        retrofitService.getMerchantPackageAlbum("package/merchantPackageAlbum", deviceIndex, token, page, count, merchant_id, group_id).enqueue(callback);
    }

    /**
     * 本地限时购配送支付
     *
     * @param goods_id
     * @param address_id
     * @param callback
     */
    public void activityPaySendOrder(String goods_id, String address_id, String payPwd, Callback<PaymentIndexEntity> callback) {
        retrofitService.paySendOrder("activity/paySendOrder", deviceIndex, token, goods_id, address_id, payPwd).enqueue(callback);
    }

    /**
     * 本地限时购配送第三方支付 还在调试
     *
     * @param goods_id
     * @param express_id
     * @param callback
     */
    public void rechargePaySendOrder(String goods_id, String express_id, Callback<PaymentIndexEntity> callback) {
        retrofitService.rechargPaySendOrder("recharge/paySendOrder", deviceIndex, token, goods_id, express_id).enqueue(callback);
    }

    /**
     * 本地限时购的生成预支付的订单
     *
     * @param goods_id
     * @param express_id
     */
    public void rechargLimitBuyMakeOrder(String goods_id, String express_id, Callback<LimitBuyMakeOrderEntity> callBack) {
        retrofitService.rechargLimitBuyMakeOrder("activity/limitBuyMakeOrder", deviceIndex, token, goods_id, express_id).enqueue(callBack);
    }

    /**
     * 获取拼团详情数据
     *
     * @param index
     * @param callback
     */
    public void getSpellGroupDetailsData(String index, Callback<SpellGroupDetailsEntity> callback) {
        retrofitService.getSpellGroupsDetailsInfo("groups/groupsInfo", deviceIndex, token, index).enqueue(callback);
    }

    /**
     * 获取我的拼团列表数据
     *
     * @param type
     * @param page
     * @param callback
     */
    public void getMySpellGroupsListData(String type, String page, Callback<MyGroupsListEntity> callback) {
        retrofitService.getMyGroupsListData("groups/groups", deviceIndex, token, type, page, "30").enqueue(callback);
    }

    /**
     * 获取拼团的分享头像
     *
     * @param groupId
     * @param callback
     */
    public void getSpellGroupShareData(String groupId, String orderId, Callback<SpellGroupShareListEntity> callback) {
        retrofitService.getSpellGroupShareData("groups/groupsShare", deviceIndex, token, groupId, orderId).enqueue(callback);
    }

    /**
     * 获取拼团下订单的orderId
     *
     * @param activity_id
     * @param index
     * @param express_id
     * @param type
     * @param goods_id
     * @param goods_sku
     * @param goods_count
     * @param order_remark
     */
    public void getSpellGroupOrderData(String activity_id, String index, String express_id, String type, String goods_id,
                                       String goods_sku, String goods_count, String order_remark, Callback<SpellGroupOrderEntity> callBack) {
        retrofitService.getSpellGroupOrderData("groups/groupsOrder", deviceIndex, token, activity_id, index, express_id,
                type, goods_id, goods_sku, goods_count, order_remark).enqueue(callBack);
    }

    /**
     * 获取拼团的订单奖励支付结果
     *
     * @param order_id
     * @param pay_pwd
     * @param callback
     */
    public void getSpellGoupOrderPayData(String order_id, String pay_pwd, Callback<SpellGroupOrderEntity> callback) {
        retrofitService.getSpellGroupOrderPay("groups/groupsOrderPay", deviceIndex, token, order_id, pay_pwd).enqueue(callback);
    }

    /**
     * 获取拼团的订单详情数据
     *
     * @param orderId
     * @param callback
     */
    public void getSpellGroupOrderInfoData(String orderId, Callback<SpellGroupOrderInfoEntity> callback) {
        retrofitService.getSpellGroupOrderInfoData("groups/orderDetail", deviceIndex, token, orderId).enqueue(callback);
    }


    /**
     * 注册账号
     *
     * @param phone
     * @param smsCode
     * @param referrer
     */
    public void getRegisterAccount(String phone, String smsCode, String password, String referrer, Callback<LoginEntity> callback) {
        retrofitService.getRegisterAccount("register_account", phone, password, deviceIndex, smsCode, referrer).enqueue(callback);
    }

    /**
     * 验证推荐人信息
     *
     * @param phone
     * @param callback
     */
    public void verifyRecommendInfo(String phone, Callback<RecommendInfoEntity> callback) {
        retrofitService.verifyRecommendInfo("member_relation_system", phone).enqueue(callback);
    }


    /**
     * 获取用户的信息
     *
     * @param callback
     */
    public void getUserInfo(Callback<UserInfoEntity> callback) {
        retrofitService.getUserInfo("get_userInfo", deviceIndex, token).enqueue(callback);
    }

    /**
     * 快捷登录
     *
     * @param phone
     * @param smsCode
     * @param callback
     */
    public void loginByQuick(String phone, String smsCode, Callback<LoginEntity> callback) {
        retrofitService.loginByQuick("login_by_quick", deviceIndex, phone, smsCode).enqueue(callback);
    }

    /**
     * 获取我的奖励信息
     *
     * @param callback
     */
    public void getMyBalance(Callback<MyBalanceEntity2> callback) {
        retrofitService.getMyBalanceData("get_integral_balance", deviceIndex, token).enqueue(callback);
    }

    /**
     * 获取我的奖励/奖券的使用获取记录
     *
     * @param page
     * @param type     0 奖励 1 奖券
     * @param callback
     */
    public void getMyAsstsRecordList(String page, String type, String screenType, String startTime, String endTime, Callback<AssetsRecordList> callback) {
        retrofitService.getMyAssetsRecordList("user_balance_change", deviceIndex, token, page, type, screenType, startTime, endTime).enqueue(callback);
    }

    /**
     * 获取我的信息数据
     *
     * @param callback
     */
    public void getUserBaseData(Callback<UserBaseDataEntity> callback) {
        retrofitService.getBaseUserData("user_base_data_v1", deviceIndex, token).enqueue(callback);
    }

    /**
     * 上传文件
     *
     * @param image
     * @param callback
     */
    public void uploadFile(MultipartBody.Part image, Callback<UploadImageEnity> callback) {
        retrofitService.uploadFile("img/upload_goods_img", token, deviceIndex, image).enqueue(callback);
    }

    /**
     * 上传头像
     *
     * @param image
     * @param callback
     */
    public void uploadHeadImage(MultipartBody.Part image, Callback<UploadImageEnity> callback) {
        retrofitService.upLoadHeadImage("upload_head_image", deviceIndex, token, image).enqueue(callback);
    }

    /**
     * 上传头像-商家 2018-2-8
     *
     * @param image
     * @param callback
     */
    public void uploadHeadImageMerchant(MultipartBody.Part image, Callback<UploadImageEnity> callback) {
        retrofitService.upLoadHeadImageMerchant("supplier/set_supplier_icon", deviceIndex, token, image).enqueue(callback);
    }

    /**
     * 提交认证资料上传加水印
     *
     * @param image
     * @param callback
     */
    public void AddressuploadFile(MultipartBody.Part image, Callback<UploadImageEnity> callback) {
        retrofitService.uploadFile("address/upload_goods_img", token, deviceIndex, image).enqueue(callback);
    }


    /**
     * 获取省市县三级列表
     *
     * @param callback
     */
    public void getRegion(Callback<RegionEntity> callback) {
        retrofitService.getCityList("getRegion").enqueue(callback);
    }

    /**
     * 获取支付列表
     *
     * @param callback
     */
    public void getPayTypeList(Callback<PayTypeEntity> callback) {
        retrofitService.getPayType("recharge/paytype_v3", deviceIndex, token).enqueue(callback);
    }

    /**
     * 商家缴费/续费的奖励支付
     *
     * @param orderId
     * @param payPwd
     * @param callback
     */
    public void getMerchantCashPay(String orderId, String payPwd, Callback<MerchantCashPayEntity> callback) {
        retrofitService.getMerchantCashPay("merchant_cash_pay", deviceIndex, token, orderId, payPwd).enqueue(callback);
    }

    /**
     * 商家缴费或续费下订单
     * 生成订单
     *
     * @param merchantId
     * @param merchantType   要成为的商家类型 0个体商家，1实体商家
     * @param merchantStatus 0缴费 1续费
     * @param callback
     */
    public void merchantPayOrder(String merchantId, String merchantType, String merchantStatus, Callback<MerchantPayEntity> callback) {
        retrofitService.merchantPay("merchant_pay", token, deviceIndex, merchantId, merchantType, merchantStatus).enqueue(callback);
    }

    /**
     * 商家提交待认证资料
     *
     * @param merchantId
     * @param merchantType
     * @param cardFront
     * @param cardReverse
     * @param cardHold
     * @param businessLincence
     * @param companyLogo
     * @param cardId
     * @param cardName
     * @param callback
     */
    public void submitMerchantAuth(String merchantId, String merchantType, String cardFront, String cardReverse, String cardHold,
                                   String businessLincence, String companyLogo, String cardId, String cardName, Callback<BaseEntity> callback) {
        retrofitService.submitMerchantAuth("submit_merchant_auth", deviceIndex, token, merchantId, merchantType, cardFront, cardReverse, cardHold,
                businessLincence, companyLogo, cardId, cardName, "").enqueue(callback);
    }

    /**
     * 支付宝微信的请求预充值订单
     *
     * @param payType    支付方式 1支付宝 2微信 3 微信公众号 4 网银
     * @param type       1商城订单支付,2商家入驻或续费,3商家扫码支付 4 线下交易 5奖励充值 19对应商超的支付
     * @param paymentFee
     * @param orderIndex 订单ID，由于该接口是生成订单，还没有订单ID，此处固定传0
     * @param callback
     */
    public void rechargeOrder(String payType, String type, String paymentFee, String orderIndex, String merchantIncome,Callback<JsPayClass> callback) {
        retrofitService.rechargeOrder("recharge/recharge_order", deviceIndex,
                token, payType, paymentFee, "0", type, orderIndex,merchantIncome).enqueue(callback);
    }

    /**
     * 支付结果
     *
     * @param orderId
     * @param type
     * @param callback
     */
    public void getPayResult(String orderId, String type, Callback<MerchantCashPayEntity> callback) {
        retrofitService.getPayResult("pay_info", deviceIndex, token, orderId, type).enqueue(callback);
    }

    /**
     * 生成微信支付的订单
     *
     * @param fee
     * @param paymentIndex
     * @param callback
     */
    public void getWeiXinOrder(String fee, String paymentIndex, Callback<WeiXinOrderEntity> callback) {
        retrofitService.WeiXinOrder(deviceIndex, token, fee, paymentIndex).enqueue(callback);
    }

    /**
     * 获取商家中心数据
     *
     * @param callback
     * @param appVersion 新增字段：零售商超的icon显示兼容老版本
     */
    public void getMerchantCenterInfo(String appVersion, Callback<MerchantCenterInfo> callback) {
        retrofitService.getMerchantCenterInfo("user_merchant_center", deviceIndex, token, appVersion).enqueue(callback);
    }

    /**
     * 获取商家收款二维码信息
     *
     * @param c merchant/happyBeanQrCodeImg 赠送益豆二维码  merchant/payQrCodeImg 赠送奖券二维码
     * @return
     */
    public void getMerchantQrCodeInfo(String c, Callback<MerchantQrCodeEntity> callback) {
        retrofitService.getMerchantPayQrCodeInfo(c, token, deviceIndex).enqueue(callback);
    }

    /**
     * 提交商家门店资料
     *
     * @param merchantId
     * @param merchantImage
     * @param merchantName
     * @param merchantAlbums
     * @param merchantContacts
     * @param merchantPhone
     * @param openTime
     * @param closeTime
     * @param merchantRecommend 简介
     * @param parentIndustryId
     * @param sonIndustryId
     * @param keywords          关键词 非必填
     * @return
     */
    public void setMerchantData(String merchantId, String merchantImage, String merchantName, String merchantAlbums, String merchantContacts, String merchantPhone,
                                String openTime, String closeTime, String merchantRecommend, String parentIndustryId, String sonIndustryId, String keywords, Callback<BaseEntity> callback) {
        retrofitService.setMerchantData("merchant_submit_info", deviceIndex, token, merchantId, merchantImage, merchantName, merchantAlbums,
                merchantContacts, merchantPhone, openTime, closeTime, merchantRecommend, parentIndustryId, sonIndustryId, keywords).enqueue(callback);
    }

    /**
     * 获取商家门店资料
     *
     * @param merchantId
     * @param callback
     */
    public void getMerchantData(String merchantId, Callback<MerchantData> callback) {
        retrofitService.getMerchantData("merchant_info", deviceIndex, token, merchantId).enqueue(callback);
    }

    /**
     * 获取行业分类
     *
     * @param callback
     */
    public void getIndustryList(Callback<IndustryEntity> callback) {
        retrofitService.getIndustryList("getIndustryList").enqueue(callback);
    }

    /**
     * 设置商家店铺地址
     *
     * @param merchantId
     * @param provinceId
     * @param cityId
     * @param countyId
     * @param address
     * @param longitude
     * @param latitude
     * @return
     */
    public void setMerchantAddress(String merchantId, String provinceId, String cityId, String countyId, String address, String longitude, String latitude, Callback<BaseEntity> callback) {
        retrofitService.setMerchantAddress("merchant/merchant_address_set", deviceIndex, token, merchantId, provinceId, cityId, countyId, address, longitude, latitude).enqueue(callback);
    }

    /**
     * 获取商家店铺地址信息
     *
     * @param callback
     */
    public void getMerchantAddressInfo(String merchantId, Callback<MerchantAddressInfo> callback) {
        retrofitService.getMerchantAddressInfo("merchant/get_merchant_address", deviceIndex, token, merchantId).enqueue(callback);
    }

    /**
     * 设置商家折扣
     *
     * @param merchantId
     * @param merchantDiscount 商家折扣 0-10
     * @param callback
     */
    public void setMerchantDiscount(String merchantId, String merchantDiscount, String pwd, Callback<BaseEntity> callback) {
        retrofitService.setMerchantDiscount("setMerchantPercent", deviceIndex, token, merchantId, merchantDiscount, pwd).enqueue(callback);
    }

    /**
     * 获取商家折扣
     *
     * @param merchantId
     * @param callback
     */
    public void getMerchantDiscountInfo(String merchantId, Callback<MerchantDiscountEntity> callback) {
        retrofitService.getMerchantDiscount("getMerSimpleInfo", deviceIndex, token, merchantId).enqueue(callback);
    }

    /**
     * 线下现金交易
     *
     * @param totalCash
     * @param phone
     * @param payPwd
     * @param callback
     * @param remark    备注信息
     * @param merchantIncome 商家营收金额
     */
    public void merchantOffLinePay(String totalCash, String phone, String payPwd, String remark, String merchantIncome,Callback<BaseEntity> callback) {
        retrofitService.merchantOffLinePay("merchant_line_pay", deviceIndex, token, totalCash, phone, payPwd, remark,merchantIncome).enqueue(callback);
    }

    /**
     * 获取商家收款记录
     *
     * @param page
     * @param callback
     */
    public void getMerchantMoneyRecord(int page, Callback<MerchantMoneyRecordEntity> callback) {
        retrofitService.getMerchantMoneyRecord("merchantPaymentRecord", deviceIndex, token, String.valueOf(page), "20").enqueue(callback);
    }

    /**
     * 获取商家折扣变更记录
     *
     * @param merchantId
     * @param page
     * @param callback
     */
    public void getMerchantDiscountChangeRecord(String merchantId, int page, Callback<MerchantDiscountChangeRecordEntity> callback) {
        retrofitService.getMerchantDiscountChangeRecord("getMerchantPercentChange", deviceIndex, token, merchantId, String.valueOf(page), "20").enqueue(callback);
    }

    /**
     * 获取我的团队信息
     *
     * @param callback
     */
    public void getMyGroupData(Callback<MyGroupEntity> callback) {
        retrofitService.getMyGroupData("account/mineGroup_v1", token, deviceIndex).enqueue(callback);
    }

    /**
     * 奖券转赠
     *
     * @param bcPhone
     * @param amount
     * @param platform
     * @param callback
     */
    public void getTransferIntegral(String bcPhone, String amount, String platform, String pwd, Callback<TransferIntegralEntity> callback) {
        retrofitService.getTransferIntegral("transfer_integral", token, deviceIndex, bcPhone, amount, platform, pwd).enqueue(callback);
    }

    /**
     * 提交服务中心申请
     *
     * @param provinceId 省ID 不能为空
     * @param cityId     市ID 可以为空
     * @param countyId   县ID 可以为空
     * @param remark     备注 必填
     * @param callback
     */
    public void applyAgent(String provinceId, @Nullable String cityId, @Nullable String countyId, @Nullable String remark, Callback<BaseEntity> callback) {
        retrofitService.applyAgent("agent/agent_application", deviceIndex, token, provinceId, cityId, countyId, remark).enqueue(callback);
    }


    /**
     * 生成线下交易预支付订单，使用第三方支付时使用
     *
     * @param totalMoney 该笔订单总钱数
     * @param phone      在该商家消费的消费者手机号
     * @return
     */
    public void getMerchantOffLineDealOrder(String totalMoney, String phone, String remark, Callback<MerchantOffLineOrderEntity> callback) {
        retrofitService.getMerchantOffLineDealOrder("merchantLinePayOrder", deviceIndex, token, totalMoney, phone, remark).enqueue(callback);
    }

    /**
     * 获取奖励奖券变更记录筛选的条件
     *
     * @param type
     * @param callback
     */
    public void getScreenType(String type, Callback<BalanceScreenEntity> callback) {
        retrofitService.getScreenType("getScreenType", deviceIndex, token, type).enqueue(callback);
    }

    /**
     * 奖励领奖励记录
     *
     * @param page
     * @param startTime
     * @param endTime
     * @param callback
     */
    public void getCashChange(String page, String startTime, String endTime, Callback<AssetsRecordList> callback) {
        retrofitService.getCashChange("user_withdraw_cash_change", deviceIndex, token, page, startTime, endTime).enqueue(callback);
    }

    /**
     * 申请立即发奖励 发奖励后订单不可申请售后
     *
     * @param orderIndex
     * @param callback
     */
    public void applyIntegral(String orderIndex, Callback<BaseEntity> callback) {
        retrofitService.applyIntegral("order/apply_integral", deviceIndex, token, orderIndex).enqueue(callback);
    }

    /**
     * 易划算商品列表
     */
    public void getYhsList(String page, String count, String classId, Callback<GoodsListEntity> callback) {
        retrofitService.getMallList("yi_goods_list", page, count, classId).enqueue(callback);
    }

    /**
     * 奖券购商品列表
     */
    public void getJfgList(String page, String count, String classId, Callback<GoodsListEntity> callback) {
        retrofitService.getMallList("integral_goods_list", page, count, classId).enqueue(callback);
    }

    /**
     * 易划算搜索列表
     * 奖券购搜索列表
     */
    public void getSearchList(String keyWord, String page, String type, Callback<SearchListEntity> callback) {
        retrofitService.getSearchList("goods/search_list", deviceIndex, token, keyWord, page, type).enqueue(callback);
    }

    /**
     * 商品搜索列表 type = 3
     */
    public void getSearchCommodityList(String keyWord, String page, String type, Callback<SearchCommodityEntity> callback) {
        retrofitService.getSearchCommodityList("goods/search_list", deviceIndex, token, keyWord, page, type).enqueue(callback);
    }

    /**
     * 获取分类商品列表
     *
     * @param sort
     * @param classId   分类type
     * @param page
     * @param count
     * @param filialeId
     * @return
     */
    public void getCategoryGoodsList(String sort, String classId, int page, int count, String filialeId, Callback<JPFragmentGoodEntity> callback) {
        retrofitService.getCategoryGoodsList("goods/mallgoods_global_class", deviceIndex, token, sort, classId, String.valueOf(page), String.valueOf(count), filialeId).enqueue(callback);
    }

    /**
     * 获取主分类商品列表
     *
     * @param filialeId 兑换中心id（type=6时传0,type=4时传旗舰店id）
     * @param type      0默认只显示兑换中心商品， 2本地生鲜(除以上参数外还需要传county,city,province)
     *                  3本地精品默认按照本地旗舰店所有商品的销量倒序排列切换到价格后默认从高到底，再点切换从低到高(0,0,0)
     *                  4旗舰店商品 5本地新品 6首页商品
     * @param sort      0,0,0,0无sort排序的时候默认传0(不排序商品默认传0, 有价格升降序时 0升序1降序) 只看有货的时候
     *                  （1） 可以联动例如（0,1,0,1有货的时候价格降序 0001 有货的时候价格升序 ）无sort
     * @param page
     * @param count
     * @return
     */
    public void getMainCategoryGoodsList(String filialeId, int type, String sort, int page, int count, Callback<JPFragmentGoodEntity> callback) {
        retrofitService.getMainCategoryGoodsList("goods/multi_mallgoods_list", deviceIndex, token, filialeId, String.valueOf(type), sort, String.valueOf(page), String.valueOf(count)).enqueue(callback);
    }

    /**
     * 获取分类头部数据
     *
     * @param classId
     * @param callback
     */
    public void getCategoryHeaderData(String classId, Callback<CategoryHeaderEntity> callback) {
        retrofitService.getCategoryHeaderData("goods/goods_class_index", deviceIndex, token, classId).enqueue(callback);
    }

    /**
     * 获取店铺列表
     *
     * @param type  0 全国供货商 1 本地供货商
     * @param id    type是1时传兑换中心id
     * @param page
     * @param count
     * @return
     */
    public void getRecommendShopsList(String type, String id, int page, int count, Callback<JPShopEntity> callback) {
        retrofitService.getRecommendShopsList("supplier_list", deviceIndex, token, type, id, String.valueOf(page), String.valueOf(count)).enqueue(callback);
    }

    /**
     * 获取主分类头部数据
     *
     * @param callback
     */
    public void getMainCategoryHeaderData(String appVersion, Callback<JPMainHeaderEntity> callback) {
        retrofitService.getMainCategoryHeaderData("appindex_v3", deviceIndex, token, appVersion).enqueue(callback);
    }

    /**
     * 获取仿美团版本首页信息
     *
     * @param cityId   用户定位城市ID，必须是具体的城市ID
     * @param countyId 用户定位区县id
     * @param callback flag 从0开始 如果升级则手动+1   当前是0
     */
    public void getMTHomePageData(String cityId, String countyId, String appVersion, String gameShow, Callback<MTHomePageEntity> callback) {
        retrofitService.getMTHomePageData("appindex_v10", deviceIndex, token,
                cityId, countyId, appVersion, gameShow, 0).enqueue(callback);
    }

    /**
     * 商超的银联支付宝下订单
     *
     * @param totalCash  消费总金额
     * @param profitCash 让利总金额
     * @param isCash     是否使用奖励
     * @param phone      手机号
     * @param remark     备注
     * @param callback
     */
    public void getRetailPayOrderIndex(String totalCash, String profitCash, String isCash, String phone, String remark, Callback<RetailPayEntity> callback) {
        retrofitService.getRetailPayOrderIndex("shop_retail_third_pay", deviceIndex, token, totalCash, profitCash, isCash, phone, remark).enqueue(callback);
    }

    /**
     * 商超奖励支付（不需要订单）
     *
     * @param payPwd
     * @param totalCash
     * @param profitCash
     * @param phone
     * @param remark
     * @param callback
     */
    public void retailCashPay(String payPwd, String totalCash, String profitCash, String phone, String remark, Callback<BaseEntity> callback) {
        retrofitService.retailCashPay("shop_retail_cash_pay", deviceIndex, token, payPwd, totalCash, profitCash, phone, remark).enqueue(callback);
    }

    /**
     * 获取商超的支付扫描二维码
     *
     * @param totalCash
     * @param profitCash
     * @param merchantId
     * @param remark
     * @param callback
     */
    public void getShopRetailQrCodeImg(String totalCash, String profitCash, String merchantId, String remark, Callback<UploadImageEnity> callback) {
        retrofitService.getShopRetailQrCodeImg("merchant/shopRetailQrCodeImg", deviceIndex, token, totalCash, profitCash, merchantId, remark).enqueue(callback);
    }

    /**
     * 下载语言包
     *
     * @param callback
     */
    public void getLanguagePackage(Callback<ResponseBody> callback) {
        retrofitService.getLanguagePackage("http://img.yilian.lefenmall.com/configs/yilian.json").enqueue(callback);
    }


    /**
     * 获取收藏数据
     *
     * @param type     0商品 1旗舰店 2商家 3套餐
     * @param callback
     */
    public void getFavoriteData(String type, Callback<FavoriteEntity> callback) {
        retrofitService.getFavoriteData("package/collect_list_v2", deviceIndex, token, type).enqueue(callback);
    }

    /**
     * 取消收藏
     *
     * @param collectId
     * @param callback
     */
    public void cancelFavorite(String collectId, Callback<BaseEntity> callback) {
        retrofitService.cancelFavorite("goods_collect_cancel_more", deviceIndex, token, collectId).enqueue(callback);
    }


}
