package com.yilian.networkingmodule.retrofitutil;/**
 * Created by  on 2017/3/3 0003.
 */

import android.support.annotation.Nullable;

import com.yilian.mylibrary.JsPayClass;
import com.yilian.mylibrary.pay.ThirdPayForType;
import com.yilian.networkingmodule.entity.*;
import com.yilian.networkingmodule.entity.ctrip.CtripAddressTransformEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripBookRoomOrderEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripCheckBookable;
import com.yilian.networkingmodule.entity.ctrip.CtripHomeTop;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelFilterEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelListEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripKeywordBySearchEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripKeywordEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderListEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripPayInfoEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityByDistrictsEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripSiteCityEntity;
import com.yilian.networkingmodule.entity.jd.FeedbackTypeEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplicationRecordListEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForDataEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForListEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForSubmitEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleDetialEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleUpdEntity;
import com.yilian.networkingmodule.entity.jd.JDAreaStringData;
import com.yilian.networkingmodule.entity.jd.JDCashPayResult;
import com.yilian.networkingmodule.entity.jd.JDCheckAvailableAfterSaleEntity;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntities;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.JDFreightEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsListEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntities;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntity;
import com.yilian.networkingmodule.entity.jd.JDHomePageTop;
import com.yilian.networkingmodule.entity.jd.JDIsAreaRestrict;
import com.yilian.networkingmodule.entity.jd.JDOrderDetailEntity;
import com.yilian.networkingmodule.entity.jd.JDOrderListEntity;
import com.yilian.networkingmodule.entity.jd.JDOrderLogisticsEntity;
import com.yilian.networkingmodule.entity.jd.JDPayInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressList;
import com.yilian.networkingmodule.entity.jd.JDThirdPayPreRechargeOrderEntity;
import com.yilian.networkingmodule.entity.jd.JdBrandGoodsListEntity;
import com.yilian.networkingmodule.entity.jd.JdDefaultAddressListEntity;
import com.yilian.networkingmodule.entity.jd.JdGoodsBrandSelectedEntity;
import com.yilian.networkingmodule.entity.jd.JdGoodsClassifyEntity;
import com.yilian.networkingmodule.entity.jd.JdHomePageNoticeEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarGoodsCountsEntity;
import com.yilian.networkingmodule.entity.shoppingcard.CardChangeDetailEntity;
import com.yilian.networkingmodule.entity.shoppingcard.CardHomeHeaderEntity;
import com.yilian.networkingmodule.entity.shoppingcard.CardTypeFiltrateEntity;
import com.yilian.networkingmodule.entity.shoppingcard.MyCardInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplicationRecordListEntity;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplyForListEntity;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleDetialEntity;
import com.yilian.networkingmodule.entity.suning.SnBrandGoodsListEntity;
import com.yilian.networkingmodule.entity.suning.SnCheckGoodsStock;
import com.yilian.networkingmodule.entity.suning.SnCheckStockEntity;
import com.yilian.networkingmodule.entity.suning.SnCommitOrderEntity;
import com.yilian.networkingmodule.entity.suning.SnFreightEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsBrandSelectedEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsClassifyEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsListEntity;
import com.yilian.networkingmodule.entity.suning.SnHomePageNoticeEntity;
import com.yilian.networkingmodule.entity.suning.SnHomePageTop;
import com.yilian.networkingmodule.entity.suning.SnOrderDetailEntity;
import com.yilian.networkingmodule.entity.suning.SnOrderListEntity;
import com.yilian.networkingmodule.entity.suning.SnOrderLogisticsEntity;
import com.yilian.networkingmodule.entity.suning.SnPayInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnShoppingCarEntity;
import com.yilian.networkingmodule.entity.suning.SnShoppingCartCountEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.math.BigInteger;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

//import com.yilian.networkingmodule.entity.suning.SnOrderDetailEntity;

/**
 * Created by  on 2017/3/3 0003.
 */
public interface RetrofitService<T> {

    String DEVICE_INDEX = "device_index";
    String TOKEN = "token";
    String C = "c";

    /**
     * 我的营收
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<MerchantMyRevenueEntity> getMerchantMyRevenueEntity(@Query(C) String c);

    /**
     * 注册设备
     *
     * @param c
     * @param uuid        设备的唯一ID
     * @param osType      设备操作系统类型,0代表安卓，1代表IOS
     * @param osVersion   操作系统版本
     * @param screenSize  设备屏幕尺寸
     * @param screenDpi   设备分辨率
     * @param deviceBrand 设备品牌
     * @param deviceModel 设备型号
     * @param deviceCpu   设备cpu型号
     * @param deviceRam   设备内存型号或大小
     * @return
     */
    @GET("account.php")
    Call<RegisterDevice> registerDevice(@Query(C) String c, @Query("uuid") String uuid, @Query
            ("os_type") String osType, @Query("os_version") String osVersion,
                                        @Query("screen_size") String screenSize, @Query
                                                ("screen_dpi") String screenDpi, @Query
                                                ("device_brand")
                                                String deviceBrand,
                                        @Query("device_model") String deviceModel, @Query
                                                ("device_cpu") String deviceCpu, @Query
                                                ("device_ram")
                                                String deviceRam);

    /**
     * 登录阿里百川客服系统
     *
     * @param deviceIndex
     * @param token
     * @return
     */
    @GET("mall.php")
    Call<T> getAliAccountInfo(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex, @Query
            ("token") String token);

    /**
     * 登录
     *
     * @param c
     * @param deviceIndex
     * @param accouont
     * @param pwd
     * @return
     */
    @GET("account.php")
    Call<LoginEntity> login(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                            @Query("account") String accouont, @Query("login_password") String pwd);

    /**
     * 获取用户信息
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @GET("account.php")
    Call<UserInfoEntity> getUserInfo(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                     @Query("token") String token);

    /**
     * 获取客服信息
     * 用于打开客服对话界面
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param type        1商品详情，2商城订单详情，3商家套餐订单详情，4商家详情，5旗舰店详情
     * @param num         type=1时商品id和兑换中心id以英文逗号分割如（1314,25）或者（1312,0）,其余type时不用拼接，订单id为短的
     * @return
     */
    @GET("mall.php")
    Call<T> getAliCustomerServiceInfo
    (@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex, @Query("token") String token,
     @Query("type") String type, @Nullable @Query("num") String num);

    /**
     * 获取附近商家列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @param industryTop 联盟商家行业顶级分类，默认0表示全部分类
     * @param industrySon 联盟商家行业二级分类，默认0
     * @param cityId      用户定位城市ID，必须是具体的城市ID
     * @param countyId    用户定位区县id,默认0
     * @return
     */
    @POST("mall.php")
    Call<T> getNearByShops(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex, @Query
            ("token") String token,
                           @Query("page") String page, @Query("count") String count, @Query
                                   ("industry_top") String industryTop, @Query("industry_son")
                                   String
                                   industrySon,
                           @Query("city") String cityId, @Query("county") String countyId);

    /**
     * 获取限时抢购商品信息列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param cityId      用户定位城市ID，必须是具体的城市ID
     * @param countyId    用户定位区县id
     * @param round       0进行时场次 1上一个抢购场次 2下一场
     * @param page
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<T> getFlashSaleList(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex, @Query
            ("token") String token
            , @Query("city") String cityId, @Query("county") String countyId, @Query("round")
                                     String round, @Query("page") String page, @Query("count")
                                     String count);

    /**
     * 搜索附近商家
     *
     * @param c
     * @param deviceIndex
     * @param city
     * @param county      用户定位区县id,默认0
     * @param keyword     关键字
     * @return
     */
    @POST("nearby.php")
    Call<T> searchNearByShops(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex, @Query
            ("city") String city, @Query("county") String county, @Query("keyword") String keyword);

    /**
     * 支付限时抢购商品
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param goodsId     抢购商品id
     * @param payPwd      支付密码
     * @return
     */
    @POST("mall.php")
    Call<T> payForFlashSale(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex, @Query
            ("token") String token,
                            @Query("goods_id") String goodsId, @Query("pay_pwd") String payPwd);

    /**
     * 检测第三方账号是否是新用户
     *
     * @param c
     * @param deviceIndex
     * @param oauthId
     * @return
     */
    @POST("account.php")
    Call<ThirdPartyNewUserEntity> checkThirdPartyRecord(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query("oauth_id") String oauthId);

    /**
     * 第三方登录（第三方登录后，通过第三方返回的ID类信息，获取token）
     *
     * @param c
     * @param deviceIndex
     * @param oauthId     微信是unionid（移动端和微信端的openId不同，不能使用）,支付宝是授权商户的userId
     * @param type        0微信 1支付宝
     * @param appType     微信端传2 客户端可不传或传1 这里选择传1
     * @param userNick    用户昵称
     * @param openId      微信openId 只有在微信登录时才传递
     * @param userHead    用户头像地址
     * @return
     */
    @POST("account.php")
    Call<LoginEntity> loginByThirdParyt(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                        @Query("oauth_id") String oauthId, @Query("type") String
                                                type, @Query("app_type") String appType,
                                        @Query("user_nick") String userNick, @Query("user_head")
                                                String userHead, @Query("open_id") String openId,
                                        @Query("newuser") String newUser, @Query("phone") String
                                                phone, @Query("SMS_code") String smsCode);

    /**
     * 微信登录时获取微信返回的一些信息 如access_token何openid
     * *    String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
     * + Constants.APP_ID
     * + "&secret="
     * + Constants.WEIXIN_APPSECRET
     * + "&code="
     * + code
     * + "&grant_type=authorization_code";
     *
     * @param getWeChatLoginEntityUrl 微信官方提供的获取用户微信账户信息的接口
     * @return
     */
    @POST
    Call<WeiXinLoginEntity> getWeiXinLoginEntity(@Url String getWeChatLoginEntityUrl, @Query
            ("appid") String appId, @Query("secret") String secret, @Query("code") String code,
                                                 @Query("grant_type") String grantType);

    /**
     * 用微信返回的access_token和openid通过微信官方提供的接口换取的微信用户信息
     * String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token +
     * "&openid=" + openid;
     *
     * @param getWeiXinUserInfoUrl
     * @return
     */
    @POST
    Call<WeiXinInfoEntity> getWeiXinUserInfo(@Url String getWeiXinUserInfoUrl, @Query
            ("access_token") String accessToken, @Query("openid") String openId);

    /**
     * 已登录账号绑定其他第三方账号
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param account     绑定手机号时传递空字符串 绑定微信传递微信的unionID 绑定支付宝传递支付宝的userId
     * @param type        绑定类型 0手机 1微信 2支付宝
     * @param userNick    第三方账号的 用户昵称 , 没有就传空
     * @param userHead    第三方账号的 用户头像地址 ,没有就传空
     * @param openId      绑定微信时传递openID 绑定手机和支付宝时传空
     * @return
     */
    @POST("account.php")
    Call<BaseEntity> bindThirdPartyAccount(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query("token") String token,
                                           @Query("account") String account, @Query("type")
                                                   String type, @Query("user_nick") String userNick,
                                           @Query("user_head") String userHead, @Query("open_id")
                                                   String openId, @Query("SMS_code") String smsCode,
                                           @Query("sign") @Nullable String sign);

    /**
     * 获取支付宝快捷登陆infoStr接口
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Call<AliPayAuthorInfoEntity> getAliPayAuthorInfo(@Query(C) String c);

    /**
     * 支付宝授权登录后获取用户支付宝的userId
     *
     * @param c
     * @param appid
     * @param source
     * @param scope
     * @param authCode
     * @return
     */
    @GET("account.php")
    Call<AliPayUserIdEntity> getAliPayUserId(@Query(C) String c, @Query("appid") String appid,
                                             @Query("soucre") String source, @Query("scope")
                                                     String scope, @Query("auth_code") String
                                                     authCode);

    /**
     * 会员体系判断
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param phone
     * @return
     */
    @POST("mall.php")
    Call<MemberRelationSystemEntity> getMemberRelationSystem(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token, @Query("phone") String
                                                                     phone);

    /**
     * 快捷登录
     *
     * @param c
     * @param deviceIndex
     * @param phone       手机号码
     * @param smsCode     短信验证码
     * @param sign        推荐人手机号
     * @return
     */
    @POST("account.php")
    Call<LoginEntity> loginBySmsCode(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                     @Query("phone") String phone, @Query("SMS_code") String
                                             smsCode, @Query("sign") String sign);

    /**
     * @param c
     * @param deviceIndex
     * @param phone       用户填写的手机号
     * @param verifyType  请求验证码的类型，0表示注册验证，1表示快捷登录验证，2表示账号操作安全验证 3表示未登录 4绑定手机号
     * @param verifyCode  图形验证码
     * @param voice       null(即请求不带该参数)发送短信验证码  1发送语音验证码
     * @param type        短信验证码type类型 type = 1 快捷登陆 type = 2 修改绑定手机号 type = 3 重置支付密码 type = 4
     *                    找回登录密码type = 5 领奖励申请 type = 6 绑定银行卡 type = 7 设置支付密码 type = 8 绑定手机号
     *                    type=9 设置支付密码
     * @return
     */
    @POST("account.php")
    Call<BaseEntity> getSmsCode(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                @Query(TOKEN) String token, @Query("phone") String phone, @Query
                                        ("verify_type") String
                                        verifyType,
                                @Query("verify_code") String verifyCode, @Query("voice")
                                @Nullable String voice, @Query("type") String type);

    /**
     * 判断手机号是否已经存在
     *
     * @param c
     * @param phone
     * @return
     */
    @POST("account.php")
    Call<CheckUserEntity> checkUser(@Query(C) String c, @Query("phone") String phone);

    /**
     * 检验 验证码是否正确
     *
     * @param c
     * @param phone
     * @param smsCode
     * @return
     */
    @POST("account.php")
    Call<BaseEntity> checkSmsCode2(@Query(C) String c, @Query("phone") String phone, @Query
            ("SMS_code") String smsCode);

    /**
     * 重新设置登录密码
     *
     * @param c
     * @param account
     * @param loginPassword
     * @return
     */
    @POST("account.php")
    Call<BaseEntity> resetLoginPassword(@Query(C) String c, @Query("account") String account,
                                        @Query("login_password") String loginPassword);

    /**
     * 获取大转盘获奖名单列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param type        0个人用户中奖列表，1轮播中奖列表
     * @param round       0未领取,1已兑换,2已失效 type=0 传此参数
     * @param page        type=0 传此参数
     * @param count       type=0 传此参数
     * @return
     */
    @POST("mall.php")
    Call<WheelOfFortuneAwardListEntity> getWheelOfFortuneAwardList(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                                   @Query("type") String type,
                                                                   @Query("round") @Nullable
                                                                           String round, @Query
                                                                           ("page")
                                                                   @Nullable String page, @Query
                                                                           ("count") @Nullable
                                                                           String
                                                                           count);

    /**
     * 获取大转盘奖品列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<WheelOfFortunePrizeListEntity> getWheelOfFortunePrizeList(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token);

    /**
     * 获取大转盘抽奖结果
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<WheelOfFortuneResultEntity> getWheelOfFortuneResult(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token, @Query("version") String
                                                                     version);

    /**
     * 获取大转盘中奖物品确认领取物品结果
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param goods_id
     * @param order_remark
     * @param address_id
     * @param order_id
     * @param sku_index
     * @return
     */
    @POST("mall.php")
    Call<PrizeGetResultEntity> getPrizeGoodsResult(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("goods_id") String goods_id,
                                                   @Query("order_remark") String order_remark,
                                                   @Query("address_id") String address_id, @Query
                                                           ("order_id") String order_id, @Query
                                                           ("sku_index") String sku_index);

    /**
     * 获取商家相册的标题头
     *
     * @param c
     * @param device_index
     * @param token
     * @param merchant_id
     * @return
     */

    @POST("mall.php")
    Call<MerchantPhotoAlbumName> getMerchantPackageAlbumTitleName(@Query(C) String c, @Query
            (DEVICE_INDEX) String device_index, @Query(TOKEN) String token, @Query("merchant_id")
                                                                          String
                                                                          merchant_id);

    /**
     * 获取商家对 相册
     *
     * @param c
     * @param device_index
     * @param token
     * @param page
     * @param count
     * @param merchant_id
     * @param group_id
     * @return
     */
    @POST("mall.php")
    Call<MerchantPhotoAlbum> getMerchantPackageAlbum(@Query(C) String c, @Query(DEVICE_INDEX)
            String device_index, @Query(TOKEN) String token, @Query("page") String page,
                                                     @Query("count") String count, @Query
                                                             ("merchant_id") String merchant_id,
                                                     @Query
                                                             ("group_id") String group_id);

    /**
     * 本地限时购配送支付
     *
     * @param c
     * @param device_index
     * @param token
     * @param goods_id
     * @param address_id
     * @return
     */
    @POST("mall.php")
    Call<PaymentIndexEntity> paySendOrder(@Query(C) String c, @Query(DEVICE_INDEX) String
            device_index, @Query(TOKEN) String token, @Query("goods_id") String goods_id,
                                          @Query("address_id") String address_id, @Query
                                                  ("pay_pwd") String pay_pwd);

    /**
     * 本地限时购配送第三方支付
     *
     * @param c
     * @param device_index
     * @param token
     * @param goods_id
     * @param express_id
     * @return
     */
    @POST("mall.php")
    Call<PaymentIndexEntity> rechargPaySendOrder(@Query(C) String c, @Query(DEVICE_INDEX) String
            device_index, @Query(TOKEN) String token, @Query("goods_id") String goods_id,
                                                 @Query("express_id") String express_id);

    /**
     * 本地限时购生成预支付订单
     *
     * @param c
     * @param device_index
     * @param token
     * @param goods_id
     * @param express_id
     * @return
     */
    @POST("mall.php")
    Call<LimitBuyMakeOrderEntity> rechargLimitBuyMakeOrder(@Query(C) String c, @Query
            (DEVICE_INDEX) String device_index, @Query(TOKEN) String token,
                                                           @Query("goods_id") String goods_id,
                                                           @Query("express_id") String express_id);

    /**
     * 获取拼团详情数据
     *
     * @param c
     * @param device_index
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<SpellGroupDetailsEntity> getSpellGroupsDetailsInfo(@Query(C) String c, @Query
            (DEVICE_INDEX) String device_index, @Query(TOKEN) String token, @Query("index")
                                                                    String index);

    /**
     * 获取我的拼团列表数据
     *
     * @param c
     * @param device_index
     * @param token
     * @param type
     * @param page
     * @param count
     * @return
     */

    @POST("mall.php")
    Call<MyGroupsListEntity> getMyGroupsListData(@Query(C) String c, @Query(DEVICE_INDEX) String
            device_index, @Query(TOKEN) String token, @Query("type") String type,
                                                 @Query("page") String page, @Query("count")
                                                         String count);

    /**
     * 获取拼团分享的头像数据
     *
     * @param device_index
     * @param token
     * @param group_id
     * @return
     */
    @POST("mall.php")
    Call<SpellGroupShareListEntity> getSpellGroupShareData(@Query(C) String c, @Query
            (DEVICE_INDEX) String device_index, @Query(TOKEN) String token,
                                                           @Query("group_id") String group_id,
                                                           @Query("order_id") String order_id);

    /**
     * 拼团的下订单
     *
     * @param c
     * @param device_index
     * @param token
     * @param activityId
     * @param index
     * @param expressId
     * @param type
     * @param goodsId
     * @param goodsSku
     * @param goodsCount
     * @param orderRemark
     * @return
     */
    @POST("mall.php")
    Call<SpellGroupOrderEntity> getSpellGroupOrderData(@Query(C) String c, @Query(DEVICE_INDEX)
            String device_index, @Query(TOKEN) String token,
                                                       @Query("activity_id") String activityId,
                                                       @Query("index") String index, @Query
                                                               ("express_id") String expressId,
                                                       @Query("type") String type, @Query
                                                               ("goods_id") String goodsId, @Query
                                                               ("goods_sku") String goodsSku, @Query
                                                               ("goods_count") String goodsCount,
                                                       @Query("order_remark") String orderRemark);

    /**
     * 拼团订单的约支付
     *
     * @param c
     * @param device_index
     * @param token
     * @param orderid
     * @param payPwd
     * @return
     */
    @POST("mall.php")
    Call<SpellGroupOrderEntity> getSpellGroupOrderPay(@Query(C) String c, @Query(DEVICE_INDEX)
            String device_index, @Query(TOKEN) String token,
                                                      @Query("order_id") String orderid, @Query
                                                              ("pay_pwd") String payPwd);

    /**
     * 获取拼团的订单详情数据
     *
     * @param c
     * @param device_index
     * @param token
     * @param index
     * @return
     */
    @POST("mall.php")
    Call<SpellGroupOrderInfoEntity> getSpellGroupOrderInfoData(@Query(C) String c, @Query
            (DEVICE_INDEX) String device_index, @Query(TOKEN) String token, @Query("order_id")
                                                                       String
                                                                       index);

    /**
     * 注册时验证短息验证码，并注册账号
     *
     * @param c
     * @param device_index
     * @param phone
     * @param smsCode
     * @param referrer
     * @return
     */
    @POST("account.php")
    Call<LoginEntity> getRegisterAccount(@Query(C) String c, @Query("phone") String phone, @Query
            ("login_password") String password, @Query(DEVICE_INDEX) String device_index,
                                         @Query("SMS_code") String smsCode, @Query("referrer")
                                                 String referrer);

    /**
     * 验证推荐人
     *
     * @param c
     * @param phone
     * @return
     */
    @POST("mall.php")
    Call<RecommendInfoEntity> verifyRecommendInfo(@Query(C) String c, @Query("phone") String phone);

    /**
     * 快捷登录
     *
     * @return
     */
    @POST("account.php")
    Call<LoginEntity> loginByQuick(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                   @Query("phone") String phone, @Query("SMS_code") String smsCode);

    /**
     * 我的奖励
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("account.php")
    Call<MyBalanceEntity2> getMyBalanceData(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token);

    /**
     * 我的奖励
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<MyBalanceEntity2> getMoneyData(@Query(C) String c);

    /**
     * 获取第三方支付方式
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<PayTypeEntity> getPayTypeList(@Query(C) String c);

    /**
     * 我的区块益豆
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<MyBalanceBeanEntity> getBeanData(@Query(C) String c);

    /**
     * 兑换券
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ExchangeTicketEntity> getExchangeTicket(@Query(C) String c);

    /**
     * 待核销兑换券
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<VerificationExchangeTicketEntity> getVerificationExchangeTicket(@Query(C) String c);

    /**
     * 兑换券申请核销
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> applyVerification(@Query(C) String c, @Query("amount") String amount);

    /**
     * 商家天使首页
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<AngelBalanceEntity> getAngelBalance(@Query(C) String c);

    /**
     * 商家提取天使
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<HttpResultBean> angelToBalance(@Query(C) String c);

    /**
     * 商家天使变更明细
     *
     * @param c
     * @param page
     * @param count
     * @param date
     * @return
     */

    @GET("mall.php")
    Observable<ExchangeTicketRecordEntity> getExtractAngelRecord(@Query(C) String c, @Query("page") int page, @Query("count") int count, @Query("date") String date);

    /**
     * 商家天使变更明细详情
     *
     * @param c
     * @param id 记录的自增id上个页面的id值
     * @return
     */
    @GET("mall.php")
    Observable<V3MoneyDetailEntity> getAngelChangeDetail(@Query(C) String c, @Query("id") String id);

    /**
     * 提取区块益豆
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<TakeLedouSuccessEntity> takeBean(@Query(C) String c, @Query("charge_rate") String
            rate, @Query("bean_amount") String amount, @Query("address") String address);

    /**
     * 奖券 菜单
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<V3MoneyMenuEntity> getV3MoneyMenu(@Query(C) String c);

    /**
     * 获取我的奖励使用获取记录
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param type
     * @return
     */
    @POST("mall.php")
    Call<AssetsRecordList> getMyAssetsRecordList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("page") String page,
                                                 @Query("type") String type, @Query
                                                         ("screen_type") String screenType, @Query
                                                         ("start_time") String startTime, @Query
                                                         ("end_time") String endTime);


    /**
     * 获取我的信息的数据
     */
    @POST("userdata.php")
    Call<UserBaseDataEntity> getBaseUserData(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token);

    /**
     * 支付列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<PayTypeEntity> getPayType(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                   @Query(TOKEN) String token);

    /**
     * 商家缴费续费下订单
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param merchantId
     * @param merchantType
     * @return
     */
    @POST("mall.php")
    Call<MerchantPayEntity> merchantPay(@Query(C) String c, @Query(TOKEN) String token, @Query
            (DEVICE_INDEX) String deviceIndex,
                                        @Query("merchant_id") String merchantId, @Query
                                                ("merchant_type") String merchantType, @Query
                                                ("merchant_pay_type") String merchantStatus);

    /**
     * 商家缴费货续费的奖励支付
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderIds
     * @param payPwd
     * @return
     */
    @POST("mall.php")
    Call<MerchantCashPayEntity> getMerchantCashPay(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                   @Query("order_index") String orderIds, @Query
                                                           ("pay_pwd") String payPwd);

    /**
     * 商家提交认证资料上传
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param file
     * @return
     */
    @POST("mall.php")
    Call<UploadMerchantImgEntity> uploadMerchantImg(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex,
                                                    @Query(TOKEN) String token, @Query("file")
                                                            String file);

    /**
     * 商家提交认证资料
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param merchantId
     * @param merchantType
     * @param cardFront
     * @param cardReverse
     * @param cardHold
     * @param businessLincence
     * @param companyLogo
     * @param cardId
     * @param cardName
     * @param cardDate
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> submitMerchantAuth(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                        @Query("merchant_id") String merchantId, @Query
                                                ("merchant_type") String merchantType,
                                        @Query("card_front") String cardFront, @Query
                                                ("card_reverse") String cardReverse, @Query
                                                ("card_hold")
                                                String cardHold,
                                        @Query("business_licence") String businessLincence,
                                        @Query("company_logo") String companyLogo, @Query
                                                ("card_id") String cardId,
                                        @Query("card_name") String cardName, @Query("card_date")
                                                String cardDate);

    /**
     * 支付宝微信的请求预充值订单
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param payType
     * @param paymentFee     支付总价
     * @param consumerOpenid 微信唯一Id,app情况传0
     * @param type           支付的type
     * @param orderId        对于支付类型充值需要穿
     * @param merchantIncome 商家营收款
     * @return
     */
    @POST("mall.php")
    Call<JsPayClass> rechargeOrder(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                   @Query(TOKEN) String token, @Query("pay_type") String payType,
                                   @Query("payment_fee") String paymentFee, @Query
                                           ("consumer_openid") String consumerOpenid, @Query("type")
                                           String type,
                                   @Query("order_index") String orderId, @Query("moneys") String merchantIncome);

    /**
     * 支付结果
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderId
     * @param type
     * @return
     */
    @POST("payment.php")
    Call<MerchantCashPayEntity> getPayResult(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                             @Query("order_index") String orderId, @Query
                                                     ("type") String type);

    /**
     * 生成微信支付订单接口（老接口）
     *
     * @param deviceIdndex
     * @param token
     * @param fee
     * @param paymentIndex
     * @return
     */
    @POST("pay/wxmake_order.php")
    Call<WeiXinOrderEntity> WeiXinOrder(@Query(DEVICE_INDEX) String deviceIdndex, @Query(TOKEN)
            String token, @Query("fee") String fee, @Query("payment_index") String paymentIndex);

    /**
     * 上传图片
     *
     * @param image
     * @return
     */
    @Multipart
    @POST("mall.php")
    Call<UploadImageEnity> uploadFile(@Query(C) String c, @Query(TOKEN) String token, @Query
            (DEVICE_INDEX) String deviceIndex, @Part MultipartBody.Part image);

    /**
     * 上传头像
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param image
     * @return
     */
    @Multipart
    @POST("account.php")
    Call<UploadImageEnity> upLoadHeadImage(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Part MultipartBody.Part image);

    /**
     * 上传头像-商家 2018-2-8
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param image
     * @return
     */
    @Multipart
    @POST("mall.php")
    Call<UploadImageEnity> upLoadHeadImageMerchant(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Part MultipartBody.Part image);

    /**
     * 获取省市县三级列表
     *
     * @param c
     * @return
     */
    @POST("account.php")
    Call<RegionEntity> getCityList(@Query(C) String c);

    /**
     * 提交认证资料上传图片加水印
     *
     * @param image
     * @return
     */
    @Multipart
    @POST("mall.php")
    Call<UploadImageEnity> AddressuploadFile(@Query("c") String c, @Query("token") String token,
                                             @Query("device_index") String deviceIndex, @Part
                                                     MultipartBody.Part image);

    /**
     * 获取商家中心数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("userdata.php")
    Call<MerchantCenterInfo> getMerchantCenterInfo(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                   @Query("app_version") String appVersion);

    /**
     * 获取商家收款二维码信息
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @return
     */
    @POST("mall.php")
    Call<MerchantQrCodeEntity> getMerchantPayQrCodeInfo(@Query(C) String c, @Query(TOKEN) String
            token, @Query(DEVICE_INDEX) String deviceIndex);

    /**
     * 提交商家门店资料
     *
     * @param c
     * @param deviceIndex
     * @param token
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
    @POST("mall.php")
    Call<BaseEntity> setMerchantData(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                     @Query(TOKEN) String token,
                                     @Query("merchant_id") String merchantId, @Query
                                             ("merchant_image") String merchantImage, @Query
                                             ("merchant_name") String merchantName,
                                     @Query("merchant_album") String merchantAlbums, @Query
                                             ("merchant_contacts") String merchantContacts,
                                     @Query("merchant_tel") String merchantPhone, @Query
                                             ("open_time") String openTime, @Query("close_time")
                                             String
                                             closeTime, @Query("merchant_desp") String
                                             merchantRecommend,
                                     @Query("merchant_industry_parent") String parentIndustryId,
                                     @Query("merchant_industry") String sonIndustryId, @Query
                                             ("keywords") String keywords);

    /**
     * 获取商家门店资料
     */
    @POST("mall.php")
    Call<MerchantData> getMerchantData(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("merchant_id") String merchantId);

    /**
     * 获取行业分类
     *
     * @param c
     * @return
     */
    @POST("mall.php")
    Call<IndustryEntity> getIndustryList(@Query(C) String c);

    /**
     * 设置商家店铺地址
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param merchantId
     * @param provinceId
     * @param cityId
     * @param countyId
     * @param address
     * @param longitude
     * @param latitude
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> setMerchantAddress(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("merchant_id") String merchantId,
                                        @Query("province_id") String provinceId, @Query
                                                ("city_id") String cityId, @Query("county_id")
                                                String
                                                countyId, @Query("address") String address,
                                        @Query("longitude") String longitude, @Query("latitude")
                                                String latitude);

    /**
     * 获取商家店铺位置信息
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<MerchantAddressInfo> getMerchantAddressInfo(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("merchant_id") String
                                                             merchantId);

    /**
     * 设置商家折扣
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param merchantId
     * @param merchantDiscount 商家折扣 0-10
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> setMerchantDiscount(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("merchant_id") String merchantId,
                                         @Query("merchant_percent") String merchantDiscount,
                                         @Query("payPwd") String pwd);

    /**
     * 获取商家折扣信息
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param merchantId
     * @return
     */
    @POST("mall.php")
    Call<MerchantDiscountEntity> getMerchantDiscount(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                     @Query("merchant_id") String merchantId);

    /**
     * 线下现金交易
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param totalCash
     * @param phone
     * @param payPwd
     * @param merchantIncome 商家营收金额
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> merchantOffLinePay(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                        @Query(TOKEN) String token, @Query("total_cash") String totalCash,
                                        @Query("phone") String phone, @Query("pay_pwd") String payPwd,
                                        @Nullable @Query("remark") String remark, @Query("moneys") String merchantIncome);

    /**
     * 获取商家收款记录
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<MerchantMoneyRecordEntity> getMerchantMoneyRecord(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                           @Query("page") String page, @Query
                                                                   ("count") String count);

    /**
     * 获取商家折扣变更记录
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param merchantId
     * @param page
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<MerchantDiscountChangeRecordEntity> getMerchantDiscountChangeRecord(@Query(C) String c,
                                                                             @Query(DEVICE_INDEX)
                                                                                     String
                                                                                     deviceIndex,
                                                                             @Query(TOKEN)
                                                                                     String token,
                                                                             @Query
                                                                                     ("merchant_id")
                                                                                     String
                                                                                     merchantId,
                                                                             @Query("page")
                                                                                     String page,
                                                                             @Query
                                                                                     ("count")
                                                                                     String
                                                                                     count);

    /**
     * 获取我的团队信息
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @return
     */
    @POST("mall.php")
    Call<MyGroupEntity> getMyGroupData(@Query(C) String c, @Query(TOKEN) String token, @Query
            (DEVICE_INDEX) String deviceIndex);

    /**
     * 申请成为服务中心
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param provinceId
     * @param cityId
     * @param countyId
     * @param remark      理由
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> applyAgent(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                @Query(TOKEN) String token,
                                @Nullable @Query("province_id") String provinceId, @Nullable
                                @Query("city_id") String cityId, @Nullable @Query("county_id")
                                        String countyId, @Nullable @Query("remark") String remark);

    /**
     * 奖券转赠
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param bcPhone
     * @param amount
     * @param platform
     * @return
     */
    @POST("service.php")
    Call<TransferIntegralEntity> getTransferIntegral(@Query(C) String c, @Query(TOKEN) String
            token, @Query(DEVICE_INDEX) String deviceIndex, @Query("be_phone") String bcPhone,
                                                     @Query("amount") String amount, @Query
                                                             ("platform") String platform, @Query
                                                             ("pwd")
                                                             String pwd);

    /**
     * 奖券转赠
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param bcPhone
     * @param amount
     * @param platform
     * @return
     */
    @POST("service.php")
    Call<TransferIntegralEntity> getTransferIntegrals(@Query(C) String c, @Query(TOKEN) String
            token, @Query(DEVICE_INDEX) String deviceIndex, @Query("be_phone") String bcPhone,
                                                      @Query("amount") String amount, @Query
                                                              ("platform") String platform,
                                                      @Query("pwd")
                                                              String pwd, @Query("remark") String
                                                              mark);

    @POST("service.php")
    Call<TransferIntegralEntity> getTransferCalculatedStress(@Query(C) String c, @Query(TOKEN) String
            token, @Query(DEVICE_INDEX) String deviceIndex, @Query("be_phone") String bcPhone,
                                                             @Query("amount") String amount, @Query
                                                                     ("platform") String platform,
                                                             @Query("pwd")
                                                                     String pwd, @Query("remark") String
                                                                     mark);


    /**
     * @param c
     * @param token
     * @param deviceIndex
     * @param newpay
     * @return
     */
    @POST("mall.php")
    Call<PayTypeListEntity> getPayTypeList(@Query(C) String c, @Query(TOKEN) String token, @Query
            (DEVICE_INDEX) String deviceIndex, @Query("newpay") String newpay);

    /**
     * 生成线下交易预支付订单，使用第三方支付时使用
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param totalMoney  该笔订单总钱数
     * @param phone       在该商家消费的消费者手机号
     * @return
     */
    @POST("mall.php")
    Call<MerchantOffLineOrderEntity> getMerchantOffLineDealOrder(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                                 @Query("total_cash") String
                                                                         totalMoney, @Query("phone")
                                                                         String phone, @Nullable
                                                                 @Query
                                                                         ("remark") String remark);

    /**
     * 获取奖励奖券变更记录筛选类型
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param type
     * @return
     */
    @POST("mall.php")
    Call<BalanceScreenEntity> getScreenType(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("type") String type);

    /**
     * 奖励领奖励记录
     *
     * @return
     */
    @POST("mall.php")
    Call<AssetsRecordList> getCashChange(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("page") String page,
                                         @Query("start_time") String startTime, @Query
                                                 ("end_time") String endTime);

    /**
     * 申请订单立即发奖励，发奖励后不可申请售后
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderIndex
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> applyIntegral(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                   @Query(TOKEN) String token, @Query("order_index") String
                                           orderIndex);


    /**
     * 易划算商品列表
     * 奖券购商品列表
     */
    @POST("mall.php")
    Call<GoodsListEntity> getMallList(@Query(C) String c, @Query("page") String page, @Query
            ("count") String count, @Query("class_id") String classId);


    /**
     * 易划算搜索列表
     * 奖券购搜索列表
     */
    @POST("mall.php")
    Call<SearchListEntity> getSearchList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("keyword") String keyWord, @Query
                                                 ("page")
                                                 String page, @Query("type") String type);

    /**
     * 商品搜索列表 type = 3
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param keyWord
     * @param page
     * @param type
     * @return
     */
    @POST("mall.php")
    Call<SearchCommodityEntity> getSearchCommodityList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("keyword") String keyWord, @Query
                                                               ("page")
                                                               String page, @Query("type") String type);


    /**
     * 获取分类商品列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param sort
     * @param classId     分类type
     * @param page
     * @param count
     * @param filialeId
     * @return
     */
    @POST("mall.php")
    Call<JPFragmentGoodEntity> getCategoryGoodsList(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                    @Query("sort") String sort, @Query
                                                            ("class_id") String classId, @Query
                                                            ("page")
                                                            String page, @Query("count") String
                                                            count,
                                                    @Nullable @Query("filiale_id") String
                                                            filialeId);

    /**
     * 获取主分类商品列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param filialeId   兑换中心id（type=6时传0,type=4时传旗舰店id）
     * @param type        0默认只显示兑换中心商品， 2本地生鲜(除以上参数外还需要传county,city,province)
     *                    3本地精品默认按照本地旗舰店所有商品的销量倒序排列切换到价格后默认从高到底，再点切换从低到高(0,0,0)
     *                    4旗舰店商品 5本地新品 6首页商品
     * @param sort        0,0,0,0无sort排序的时候默认传0(不排序商品默认传0, 有价格升降序时 0升序1降序) 只看有货的时候
     *                    （1） 可以联动例如（0,1,0,1有货的时候价格降序 0001 有货的时候价格升序 ）无sort
     * @param page
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<JPFragmentGoodEntity> getMainCategoryGoodsList(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                        @Query("filiale_id") String filialeId,
                                                        @Query("type") String type, @Query
                                                                ("sort") String sort, @Query("page")
                                                                String page,
                                                        @Query("count") String count);

    /**
     * 获取分类头部数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param classId
     * @return
     */
    @POST("mall.php")
    Call<CategoryHeaderEntity> getCategoryHeaderData(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                     @Query("class_id") String classId);

    /**
     * 获取店铺列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param type        0 全国供货商 1 本地供货商
     * @param id          type是1时传兑换中心id
     * @param page
     * @param count
     * @return
     */
    @POST("nearby.php")
    Call<JPShopEntity> getRecommendShopsList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("type") String type,
                                             @Query("id") String id, @Query("page") String page,
                                             @Query("count") String count);

    /**
     * 获取主分类头部数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<JPMainHeaderEntity> getMainCategoryHeaderData(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("app_version") String
                                                               appVersion);

    /**
     * 获取仿美团首页数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param cityId      用户定位城市ID，必须是具体的城市ID
     * @param countyId    用户定位区县id
     * @param flag        从0开始 如果升级则手动+1
     * @return
     */
    @POST("mall.php")
    Call<MTHomePageEntity> getMTHomePageData(@Query("c") String c, @Query("device_index") String
            deviceIndex, @Query("token") String token,
                                             @Query("city") String cityId, @Query("county")
                                                     String countyId, @Query("app_version") String
                                                     appVersion,
                                             @Nullable @Query("game_show") String gameShow,
                                             @Query("flag") int flag);

    /**
     * 商超的银联支付宝下订单
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param totalCash
     * @param profitCash
     * @param isCash
     * @param phone
     * @param remark
     * @return
     */
    @POST("mall.php")
    Call<RetailPayEntity> getRetailPayOrderIndex(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                                 @Query("total_cash") String totalCash, @Query
                                                         ("profit_cash") String profitCash, @Query
                                                         ("isCash") String isCash,
                                                 @Query("phone") String phone, @Query("remark")
                                                         String remark);

    /**
     * 商超奖励支付
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param payPwd
     * @param totalCash
     * @param profitCash
     * @param phone
     * @param remark
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> retailCashPay(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                   @Query(TOKEN) String token, @Query("pay_pwd") String payPwd,
                                   @Query("total_cash") String totalCash, @Query("profit_cash")
                                           String profitCash, @Query("phone") String phone, @Query
                                           ("remark") String remark);

    /**
     * 商超的获取扫码支付二维码
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param totalCash
     * @param profitCash
     * @param merchantId
     * @param remark
     * @return
     */
    @POST("mall.php")
    Call<UploadImageEnity> getShopRetailQrCodeImg(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("total_cash") String totalCash,
                                                  @Query("profit_cash") String profitCash, @Query
                                                          ("merchant_id") String merchantId, @Query
                                                          ("remark") String remark);

    @GET
    Call<ResponseBody> getLanguagePackage(@Url String fileUrl);

    /**
     * 获取商家套餐订单管理界面 套餐订单列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param type        1到店消费订单 2 配送套餐列表
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<ComboOrderListDeliveryEntity> getManagerComboOrderList(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                                      @Query("type") String type,
                                                                      @Query("page") String
                                                                              page, @Query("count")
                                                                              String count);

    /**
     * 获取商家套餐订单管理界面 套餐订单列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param type
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<ComboOrderListDineInEntity> getManagerComboOrderDineInList(@Query(C) String c,
                                                                          @Query(DEVICE_INDEX)
                                                                                  String
                                                                                  deviceIndex,
                                                                          @Query(TOKEN) String
                                                                                  token,
                                                                          @Query("type") String
                                                                                  type, @Query
                                                                                  ("page")
                                                                                  String page,
                                                                          @Query
                                                                                  ("count")
                                                                                  String count);

    /**
     * 获取收藏数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<FavoriteEntity> getFavoriteData(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("type") String type);

    /**
     * 获取商家认证资料
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<MerchantAuthInfoEntity> getMerchantAuthInfo(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token);

    @GET("mall.php")
    Observable<ComboDetailEntity> getComboDetail(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("order_id") String orderId);

    /**
     * 处理订单
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderId
     * @param newStatus   更新后的状态 2接单，3配送 4配送完成
     * @return
     */
    @GET("mall.php")
    Observable<UpdateComboStatusEntity> updateComboStatus(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                          @Query("orderId") String orderId,
                                                          @Query("status") String newStatus);


    /**
     * 我的业绩
     */
    @POST("mall.php")
    Call<PerformanceEntity> getAchievementInfo(@Query(C) String c, @Query("user_id") String userId);

    /**
     * 取消收藏
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param collectId
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> cancelFavorite(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                    @Query(TOKEN) String token, @Query("collect_id") String
                                            collectId);

    /**
     * 易划算/奖券购分类列表
     */
    @POST("mall.php")
    Call<ClassifyListV2Entity> getClassifyListV2(@Query(C) String c, @Query("type") String type,
                                                 @Query("use_type") String useType);

    /**
     * 获取游戏列表
     *
     * @param page
     * @param count
     * @param type  1 Android 2 iOS
     * @return
     */
    @GET("mall.php")
    Observable<GameListEntity> getGameList(@Query(C) String c, @Query("page") String page, @Query
            ("count") String count, @Query("type") String type);

    /**
     * 获取游戏列表界面头部banner数据
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<GameListHeaderEntity> getGameListHeaderData(@Query(C) String c);

    /**
     * 获取游戏历史记录列表
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<BaseHistoryEntity> getGameHistoryList(@Query(C) String c, @Query(TOKEN) String
            token, @Query(DEVICE_INDEX) String deviceIndex, @Query("page") String page, @Query
                                                             ("count")
                                                             String count);

    /**
     * 获取排行榜内容
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @return
     */
    @POST("mall.php")
    Call<ChartsEntity> getChartsEntity(@Query(C) String c, @Query(TOKEN) String token, @Query
            (DEVICE_INDEX) String deviceIndex);

    /**
     * 条形码的模糊搜索列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param goodsCode
     * @return
     */
    @POST("mall.php")
    Call<BarCodeSearchEntity> getBarCodeSearchEntity(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("goods_code") String goodsCode);

    /**
     * merchant购物车列表
     */
    @POST("mall.php")
    Call<MerchantCartListEntity> getMerchantCartList(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token);

    /**
     * merchant更新购物车
     */
    @POST("mall.php")
    Call<HttpResultBean> modifyMerchantCartList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("cart_index") String cartIndex, @Query
                                                        ("count") String count);

    /**
     * merchant删除购物车
     */
    @POST("mall.php")
    Call<HttpResultBean> delMerchantGoods(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("cart_index") String cartIndex);

    /**
     * merchant下订单
     */
    @POST("mall.php")
    Call<MerchantMakeOrderEntity> merchantMakeOrder(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                    @Query("cart_list") String cartList, @Query
                                                            ("phone") String phone, @Query
                                                            ("SMS_code")
                                                            String smsCode);

    /**
     * 二维码付款二维码生成
     */
    @POST("mall.php")
    Call<MerchantShopQrCodeEntity> merchantQrCode(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("order_index") String orderIndex);

    /**
     * 根据条码获取商品信息
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param goodsCode
     * @return
     */
    @GET("mall.php")
    Observable<BarCodeScanResultEntity> getBarCodeScanResult(@Query(C) String c, @Query(TOKEN)
            String token, @Query(DEVICE_INDEX) String deviceIndex, @Query("goods_code") String
                                                                     goodsCode);

    /**
     * 模糊查询加入购物车
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param goodsCode
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<HttpResultBean> scGoodsAddCart(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                        @Query("goods_code") String goodsCode, @Query("count")
                                                String count);

    /**
     * 将条码对应商品加入购物车
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param goodsCode
     * @param count
     * @return
     */
    @POST("mall.php")
    Observable<HttpResultBean> addGoodsToShoppingCart(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                      @Query("goods_code") String goodsCode,
                                                      @Query("count") String count);

    /**
     * 获取商超的订单列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<ScOrderListEntity> getScOrderData(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                           @Query("page") String page, @Query("count") String
                                                   count);

    /**
     * 商超零售现金付款
     */
    @POST("mall.php")
    Call<HttpResultBean> merchantCashPay(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                         @Query("pay_pwd") String pwd, @Query("order_index")
                                                 String orderIndex);

    /**
     * 清空merchant购物车列表
     */
    @POST("mall.php")
    Call<HttpResultBean> clearMerchantShopList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token);

    /**
     * 获取订单列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<OrderNewListEntity> getOrderNewList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                             @Query("page") String page, @Query("count") String
                                                     count);

    /**
     * 线下转账记录
     */
    @POST("mall.php")
    Call<OfflineTransferCardInfoEntity> transferList(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                     @Query("page") String page, @Query("count")
                                                             String count);

    /**
     * 获取系统消息列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @return
     */
    @POST("mall.php")
    Call<SystemNewListEntity> getSystemNewList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                               @Query("page") String page, @Query("count") String
                                                       count);

    /**
     * 线下转账
     */
    @POST("mall.php")
    Call<HttpResultBean> userTransfer(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                      @Query("amount") String amount, @Query("remark") @Nullable
                                              String remark, @Query("card_name") String cardName,
                                      @Query("card_number") String cardNumber, @Query
                                              ("card_bank") String cardBank, @Query("card_vouchar")
                                              String cardVoucher);

    /**
     * 银行卡信息
     */
    @POST("mall.php")
    Call<OfflineTransferCardInfoEntity> cardInfo1(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                                  @Query("type") String type, @Nullable @Query
            ("trans_id") String transId);

    @POST("mall.php")
    Call<OfflineTransferCardInfoDatailEntity> cardInfo2(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                        @Query("type") String type, @Nullable
                                                        @Query("trans_id") String transId);

    /**
     * 获取强制公告，该公告显示后，用户不能对APP进行任何操作
     *
     * @param c
     * @param appVersion
     * @return
     */
    @GET("account.php")
    Observable<ForceNotice> getForceNotice(@Query(C) String c, @Query("app_version") String
            appVersion);

    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    /**
     * 套餐外面订单 确认收货（该接口其实是改变套餐外面订单状态的接口）
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderId     长的套餐订单的订单号
     * @param status      4 代表配送完成的状态（目前只传4）
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> confirmIMerMealOrder(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("orderId") String orderId, @Query
                                                            ("status") String status);

    /**
     * 商家主动取消套餐
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderId
     * @return
     */
    @GET("mall.php")
    Observable<CancelComboEntity> cancelComboOrder(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                   @Query("orderId") String orderId);

    /**
     * 获取订单列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param orderType
     * @return
     */
    @POST("mall.php")
    Call<MerchantOrderListEntity> getOrderList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                               @Query("page") String page, @Query("order_type")
                                                       String orderType, @Query("count") String
                                                       count);

    /**
     * 备货
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderId
     * @return
     */
    @POST("mall.php")
    Call<HttpResultBean> orderExport(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                     @Query(TOKEN) String token,
                                     @Query("order_id") String orderId);

    /**
     * 订单详情
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderIndex
     * @return
     */
    @POST("mall.php")
    Call<MerchantOrderEntity> getMallOrderData(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                               @Query("order_index") String orderIndex);

    /**
     * 取消备货
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderId
     * @return
     */
    @POST("mall.php")
    Call<HttpResultBean> cancelOrderExport(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                           @Query("order_id") String orderId);

    /**
     * 商家发货
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param orderIndex
     * @param parcelIndex
     * @param expressCompany
     * @param expressNumber
     * @return
     */
    @POST("mall.php")
    Observable<MerchantDeliverGoods> orderSend(@Query(C) String c, @Query(TOKEN) String token,
                                               @Query(DEVICE_INDEX) String deviceIndex,
                                               @Query("order_index") String orderIndex, @Query
                                                       ("order_goods_index") String parcelIndex,
                                               @Query("express_company") String expressCompany,
                                               @Query("express_number") String expressNumber);

    /**
     * 商家发货
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param orderIndex
     * @param parcelIndex
     * @param expressCompany
     * @param expressNumber
     * @return
     */
    @POST("mall.php")
    Call<HttpResultBean> orderSend2(@Query(C) String c, @Query(TOKEN) String token, @Query
            (DEVICE_INDEX) String deviceIndex,
                                    @Query("order_index") String orderIndex, @Query
                                            ("order_goods_index") String parcelIndex,
                                    @Query("express_company") String expressCompany, @Query
                                            ("express_number") String expressNumber);

    /**
     * 获取物流信息
     *
     * @param c
     * @return
     */
    @POST("mall.php")
    Call<ExpressListEntity> getExpressList(@Query(C) String c);

    /**
     * 修改物流信息
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param parcelIndex
     * @param expressCompany
     * @param expressNumber
     * @return
     */
    @POST("mall.php")
    Observable<HttpResultBean> updateParcelInfo(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                                @Query("parcel_index") String parcelIndex, @Query
                                                        ("express_company") String expressCompany,
                                                @Query("express_number") String expressNumber);

    /**
     * 售后列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @param type
     * @return
     */
    @POST("mall.php")
    Call<SupplierListEntity> getSupplierList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                             @Query("page") String page, @Query("count") String
                                                     count, @Query("type") String type);

    /**
     * 售后订单详情
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param serviceIndex
     * @param type
     * @return
     */
    @POST("mall.php")
    Call<SupplierDetailEntity> getSupplierDetailData(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                     @Query("service_index") String serviceIndex,
                                                     @Query("type") String type);

    /**
     * 待审核订单的审核/拒绝
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param checkIndex
     * @param status
     * @param reason
     * @param serviceType
     * @return
     */
    @POST("mall.php")
    Call<AfterSaleBtnClickResultEntity> supplierRefuse(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("check_index") String checkIndex,
                                                       @Query("status") String status, @Query
                                                               ("reason") String reason, @Query
                                                               ("service_type") String serviceType);

    /**
     * 商品管理-商品列表
     */
    @POST("mall.php")
    Call<MerchantManagerListEntity> getMerchantManagerList(@Query(C) String c, @Query(TOKEN)
            String token, @Query(DEVICE_INDEX) String deviceIndex
            , @Query("page") String page, @Query("count") String count, @Query("keyword") String
                                                                   keyWord);

    /**
     * 获取商家待处理订单数量
     *
     * @return
     */
    @GET("mall.php")
    Observable<MerchantOrderNumber> getMerchantOrderNumber(@Query(C) String c, @Query(TOKEN)
            String token, @Query(DEVICE_INDEX) String deviceIndex);

    /**
     * 获取商家待处理订单数量
     *
     * @return
     */
    @GET("mall.php")
    Observable<MerchantAfterSaleOrderNumber> getMerchantAfterSaleOrderNumber(@Query(C) String c,
                                                                             @Query(TOKEN) String
                                                                                     token, @Query
                                                                                     (DEVICE_INDEX)
                                                                                     String
                                                                                     deviceIndex);

    /**
     * 换货返修 订单中确认收货功能
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param barterId
     * @return
     */
    @POST("mall.php")
    Call<AfterSaleBtnClickResultEntity> barterExpressReceive(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                             @Query("barter_id") String barterId);

    /**
     * 商品管理-商品上下架专区
     */
    @POST("mall.php")
    Call<HttpResultBean> getGoodsDownUp(@Query(C) String c, @Query(TOKEN) String token, @Query
            (DEVICE_INDEX) String deviceIndex
            , @Query("goods_id") String goodsId, @Query("status_str") String statusStr);

    /**
     * 商品管理-商品详情
     */
    @POST("mall.php")
    Call<MerchantManagerDetailEntity> goodsDownUpInfo(@Query(C) String c, @Query(TOKEN) String
            token, @Query(DEVICE_INDEX) String deviceIndex, @Query("goods_id") String goodsId);

    /**
     * 确认修复 备货功能
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param barterId
     * @return
     */
    @POST("mall.php")
    Call<AfterSaleBtnClickResultEntity> barterStock(@Query(C) String c, @Query(TOKEN) String
            token, @Query(DEVICE_INDEX) String deviceIndex,
                                                    @Query("barter_id") String barterId);

    /**
     * 换货返修的发货功能
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param barterId
     * @param expressId
     * @param expressNum
     * @return
     */
    @POST("mall.php")
    Call<AfterSaleBtnClickResultEntity> barterExpress(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                      @Query("barter_id") String barterId, @Query
                                                              ("express_id") String expressId,
                                                      @Query
                                                              ("express_num") String expressNum);

    /**
     * 退货中的 确认收货
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param refundId
     * @return
     */
    @POST("mall.php")
    Call<AfterSaleBtnClickResultEntity> refundExpressReceive(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token, @Query("refund_id")
                                                                     String
                                                                     refundId);

    /**
     * 退款功能
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param refundIndex
     * @return
     */
    @POST("mall.php")
    Call<HttpResultBean> refundMoneyBack(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("refund_index") String refundIndex);

    /**
     * 套餐添加申请发奖励功能
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param orderId     套餐订单 长的订单号
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> packageApplyIntegral(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("order_id") String orderId);

    /**
     * 大家猜活动列表
     */
    @GET("mall.php")
    Call<GuessListEntity> guessList(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                    @Query(TOKEN) String token, @Query("status") String status,
                                    @Query("page") String page, @Query("count") String count);

    /**
     * 大家猜我参与的
     */
    @GET("mall.php")
    Call<GuessListEntity> guessListSelf(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                        @Query("page") String page, @Query("count") String count);

    /**
     * 大家猜结果
     */
    @GET("mall.php")
    Call<GuessInfoEntity> guessInfo(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                    @Query(TOKEN) String token,
                                    @Query("activity_id") String id, @Query("page") String page,
                                    @Query("count") String count);

    /**
     * 大家猜我猜过的结果
     */
    @GET("mall.php")
    Call<GuessInfoEntity> guessHasPrize(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                        @Query("activity_id") String id, @Query("page") String
                                                page, @Query("count") String count);

    /**
     * 获取支行列表
     */
    @GET("account.php")
    Call<BranchListEntity> getBranchBankList(@Query(C) String c, @Query("province_id") String
            provinceId, @Query("city_id") String cityId, @Query("bank_id") String bankId, @Query
                                                     ("search_val") String searchVal);

    /**
     * 获取活动列表
     */
    @GET("mall.php")
    Call<ParadiseEntity> getActiviesLists(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token);

    /**
     * 中奖记录
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Call<SnatchAwardListEntity> getSnatchAwardList(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                   @Query("user_id") String userId, @Query
                                                           ("page") String page, @Query("count")
                                                           String
                                                           count);

    /**
     * 幸运购-活动详情
     */
    @GET("mall.php")
    Call<LuckyInfoEntity> luckyInfo(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                    @Query(TOKEN) String token, @Query("snatch_index") String id,
                                    @Query("page") String page, @Query("count") String count,
                                    @Query("type") String type, @Query("lat") String lat, @Query
                                            ("lng") String lng);

    /**
     * 幸运购-参加活动
     */
    @GET("mall.php")
    Call<LuckyPayEntity> luckyPayOrder(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                       @Query("activity_id") String id, @Query("count") String
                                               count, @Query("lat") String lat, @Query("lng")
                                               String lng);

    /**
     * 幸运购-往期回顾列表
     */
    @GET("mall.php")
    Call<LuckyRecordListEntity> luckyRecordList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                                @Query("activity_id") String id, @Query("page")
                                                        String page, @Query("count") String count);

    /**
     * 晒单提交
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param activityId
     * @param content
     * @param img
     * @return
     */
    @GET("mall.php")
    Call<HttpResultBean> snatchShow(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                    @Query(TOKEN) String token,
                                    @Query("activity_id") String activityId, @Query("content")
                                            String content, @Query("img") String img);

    /**
     * 晒单列表
     */
    @GET("mall.php")
    Call<SnatchShowListEntity> snatchShowList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                              @Query("type") String type, @Query("snatch_index")
                                                      String snatchIndex, @Query("page") String
                                                      page,
                                              @Query("count") String count);

    /**
     * 中奖详情
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param activityId
     * @return
     */
    @GET("mall.php")
    Call<SnatchAwardDetailsEntity> snatchAwardDetails(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                      @Query("activity_id") String activityId);

    /**
     * 参与记录
     */
    @GET("mall.php")
    Call<SnatchAwardListEntity> snatchJoinList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                               @Query("type") String type, @Query("user_id")
                                                       String userId, @Query("page") String page,
                                               @Query
                                                       ("count") String count);

    /**
     * 幸运购设置地址
     */
    @GET("mall.php")
    Call<HttpResultBean> snatchSetAddress(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                          @Query("activity_id") String activityId, @Query
                                                  ("address_id") String addressId);

    /**
     * 计算时间详情
     */
    @GET("mall.php")
    Call<CalculateEntity> snatchCalculate(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                          @Query("snatch_index") String snatchIndex);

    /**
     * 计算时间详情
     */
    @GET("mall.php")
    Call<HttpResultBean> snatchConfirmGoods(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                            @Query("activity_id") String activityId);

    /**
     * 获取幸运购首页头部数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @GET("mall.php")
    Observable<LuckyMainHeaderEntity> getLuckyMainHeaderData(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token);

    /**
     * 获取幸运购活动列表
     *
     * @param c
     * @param token
     * @param sort  排序 人气 最新 进度 总须人数 已逗号间隔如("0,1,1,0"),0升序 1降序
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<LuckyGoodsListEntity> getLuckyGoodsListData(@Query(C) String c, @Query(TOKEN)
            String token, @Query(DEVICE_INDEX) String deviceIndex,
                                                           @Query("sort") String sort, @Query
                                                                   ("page") String page, @Query
                                                                   ("count")
                                                                   String count);

    /**
     * 获取幸运购活动全部列表
     *
     * @param c
     * @param token
     * @param deviceIndex
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<LuckyGoodsListEntity> getAllLuckyGoodsListData(@Query(C) String c, @Query(TOKEN)
            String token, @Query(DEVICE_INDEX) String deviceIndex,
                                                              @Query("page") int page, @Query
                                                                      ("count") int count);

    /**
     * 幸运购-夺宝记录
     */
    @GET("mall.php")
    Call<LuckyNumLogListEntity> luckyNumLogList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                                @Query("snatch_index") String id, @Query
                                                        ("user_id") String userId);

    /**
     * 幸运购-夺宝记录
     */
    @GET("mall.php")
    Call<LuckyNumLogListEntity> luckyNumLogListWithPageAndCount(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                                @Query("snatch_index") String id,
                                                                @Query("user_id") String userId,
                                                                @Query("page") String page,
                                                                @Query("count") String count);

    /**
     * 幸运购-夺宝记录
     */
    @GET("mall.php")
    Call<LuckyNumLogListEntity> luckyNumLogListWithPageAndCountAndType(@Query(C) String c, @Query
            (DEVICE_INDEX) String deviceIndex, @Query(TOKEN) String token,
                                                                       @Query("snatch_index")
                                                                               String id, @Query
                                                                               ("user_id") String
                                                                               userId,
                                                                       @Query("page") String
                                                                               page, @Query("count")
                                                                               String count, @Query
                                                                               ("type") String
                                                                               type);

    /**
     * 幸运购-晒单详情
     */
    @GET("mall.php")
    Call<LuckyShowListInfoEntity> luckyShowListInfo(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token, @Query("comment_index") String id);

    /**
     * 获取幸运购活动列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param keyWord
     * @param page
     * @param couont
     * @return
     */
    @GET("mall.php")
    Observable<LuckyGoodsListEntity> getPrizeList(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                                  @Query("key_word") String keyWord, @Query
                                                          ("page") String page, @Query("count")
                                                          String
                                                          couont);

    /**
     * 获取幸运购最近揭晓列表
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<LuckyAwardListEntity> getLuckyAwardList(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token,
                                                       @Query("page") String page, @Query
                                                               ("count") int count);

    /**
     * 统计推送数据
     *
     * @param c
     * @param pushId 推送消息中的push_id 如果push_id 不为0 则调用该接口
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> countPushInfo(@Query(C) String c, @Query("push_id") String pushId);

    /**
     * 获取会员幸运购参与记录
     *
     * @param c
     * @param userId 用户id
     * @param page
     * @param count
     * @param type   1 参与记录 2中奖记录 3晒单记录
     * @return
     */
    @GET("mall.php")
    Observable<LuckyMemberJoinRecordEntity> getMemberLuckyJoinRecord(@Query(C) String c, @Query
            ("user_id") String userId, @Query("page") int page, @Query("count") int count, @Query
                                                                             ("type")
                                                                             int type);

    /**
     * 获取会员幸运购中奖记录
     *
     * @param c
     * @param userId 用户id
     * @param page
     * @param count
     * @param type   2中奖记录
     * @return
     */
    @GET("mall.php")
    Observable<LuckyMemberPrizeRecordEntity> getMemberLuckyPrizeRecord(@Query(C) String c, @Query
            ("user_id") String userId, @Query("page") int page, @Query("count") int count, @Query
                                                                               ("type")
                                                                               int type);

    /**
     * 获取会员幸运购晒单记录
     *
     * @param c
     * @param userId 用户id
     * @param page
     * @param count
     * @param type   3晒单记录
     * @return
     */
    @GET("mall.php")
    Observable<LuckyMemberShowPrizeRecordEntity> getMemberLuckyShowPrizeRecord(@Query(C) String
                                                                                       c, @Query
                                                                                       ("user_id") String userId, @Query("page") int page, @Query("count") int count,
                                                                               @Query("type") int
                                                                                       type);

    @GET("mall.php")
    Observable<LuckyWinPushPopWindowEntity> getLuckyWinPushPopWindowData(@Query(C) String c,
                                                                         @Query("snatch_index")
                                                                                 String
                                                                                 snatchIndex);

    /**
     * 碰运气操作
     */
    @GET("mall.php")
    Call<GALLuckyPrizeEntity> galLuckyPrize(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token,
                                            @Query("act_id") String activityId);

    /**
     * 碰运气下订单
     */
    @GET("mall.php")
    Call<GALMakeOrderEntity> galMakerOrder(@Query(C) String c, @Query("order_id") String orderId,
                                           @Query("goods_sku") String goodsSku, @Query
                                                   ("address_id") String addressId, @Query
                                                   ("order_remark") @Nullable String remark);

    /**
     * 猜价格操作
     */
    @GET("mall.php")
    Call<GALLuckyPrizeEntity> galGuessPrize(@Query(C) String c, @Query("act_id") String actId,
                                            @Query("luck_price_type") String type, @Query
                                                    ("luck_price") String luckyPrice);

    /**
     * 猜价格-碰运气历史战绩
     */
    @GET("mall.php")
    Call<GALRecordEntity> galRecordList(@Query(C) String c, @Query("goods_id") String goodsId,
                                        @Query("act_type") String type, @Query("page") String
                                                page, @Query("count") String count);

    /**
     * 猜价格-碰运气点赞操作
     */
    @GET("mall.php")
    Call<GALZanEntity> setPraise(@Query(C) String c, @Query("order_index") String id, @Query
            ("type") String type, @Query("praise_type") String praiseType);

    /**
     * 猜价格-碰运气我的战绩
     */
    @GET("mall.php")
    Call<ActMyRecordEntity> galMyRecordList(@Query(C) String c, @Query("act_type") String type,
                                            @Query("page") String page, @Query("count") String
                                                    count);

    /**
     * 猜价格-碰运气评论详情
     */
    @GET("mall.php")
    Call<ActEvaDetailEntity> galEvaDetail(@Query(C) String c, @Query("comment_index") String
            comment_index, @Query("page") String page, @Query("count") String count);

    /**
     * 猜价格-碰运气回复评论
     */
    @GET("mall.php")
    Call<HttpResultBean> galReplyComment(@Query(C) String c, @Query("comment_index") String
            comment_index, @Query("comment_content") String comment_content);

    /**
     * 猜价格-碰运气确认收货
     */
    @GET("mall.php")
    Call<HttpResultBean> galConfirmOrder(@Query(C) String c, @Query("order_index") String
            comment_index);

    /**
     * 猜价格-碰运气添加评论
     */
    @GET("mall.php")
    Call<HttpResultBean> galSetComment(@Query(C) String c, @Query("order_index") String
            comment_index, @Query("comment_anonymity") String anonymity, @Query("comment_score")
                                               String
                                               score,
                                       @Query("comment_content") String content, @Query
                                               ("comment_images") String images);

    /**
     * 猜价格-碰运气商品详情下方-评论列表
     */
    @GET("mall.php")
    Call<ActGoodsEvaListEntity> galCommentList(@Query(C) String c, @Query("page") String page,
                                               @Query("goods_id") String goodsId);

    /**
     * 猜价格-碰运气规则
     */
    @GET("mall.php")
    Call<ActRuleEntity> galRule(@Query(C) String c, @Query("act_type") String actType);

    /**
     * 打卡-邀请好友-我的战绩
     */
    @GET("mall.php")
    Call<ActInventMyRcordEntity> actInventMyRecord(@Query(C) String c);

    /**
     * 打卡-邀请好友-我的战绩
     */
    @GET("mall.php")
    Call<ActInventIntegralChangeListEntity> actInventIntegralChangeList(@Query(C) String c,
                                                                        @Query("page") String
                                                                                page, @Query
                                                                                ("count")
                                                                                String count);

    /**
     * /**
     * app每次启动时调用
     *
     * @param appVersionCode versionCode
     * @param gapTime        本次启动间隔时间（当前时间-上次退出时间,单位：秒）
     *                       osType //设备操作系统类型,0代表安卓，1代表IOS
     *                       token （token+gap_time做数学运算）（没有登录可以没有或值为空字符串）
     * @param pushId         客户端当前的推送id
     * @param callBack
     */
    //    public void appStart(String appVersionCode,String gapTime, String pushId,
    // Class<Start_app> cls, RequestCallBack<Start_app> callBack) {
    //
    //        RequestParams params = new RequestParams();
    //        params.addBodyParameter("c", "start_app");
    //        params.addBodyParameter("token", RequestOftenKey.gettoken(mContext));//此处使用不加盐值的token
    //        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
    //        params.addBodyParameter("os_type", "0");
    //        params.addBodyParameter("push_id", pushId);
    //        params.addBodyParameter("device_token", "0");
    //        params.addBodyParameter("gap_time", gapTime);
    //        params.addBodyParameter("app_version", appVersionCode);
    //
    //        Logger.i("startApp  参数  token "+ RequestOftenKey.gettoken(mContext)+" device_index
    // "+RequestOftenKey.getDeviceIndex(mContext)
    //                +"  ostype :0 "+"   push_id   "+pushId+"  gapTime  "+gapTime+"  appversion
    // "+appVersionCode);
    //        postRequest(URL, params, cls, callBack);
    //    }
    //
    //     */

    /**
     * 打卡-邀请好友-我的战绩-好友列表
     */
    @GET("mall.php")
    Call<ActInventSubMemberListEntity> actGetSubMember(@Query(C) String c, @Query("page") String
            page, @Query("count") String count);

    /**
     * @param c
     * @param deviceIndex
     * @param token       此处使用不加盐值的token
     * @param osType
     * @param pushId
     * @param deviceToken
     * @param gapTime
     * @param appVersion
     * @return
     */
    @GET("account.php")
    Observable<StartAppEntity> startApp(@Query(C) String c, @Query(DEVICE_INDEX) String
            deviceIndex, @Query(TOKEN) String token, @Query("os_type") String osType,
                                        @Query("push_id") String pushId, @Query("device_token")
                                                String deviceToken, @Query("gap_time") String
                                                gapTime,
                                        @Query("app_version") String appVersion);

    /**
     * 获取活动首页签到数据
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ActSignInData> getSignData(@Query(C) String c);

    /**
     * 获取活动首页打卡数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @GET("mall.php")
    Observable<ActHomeClockEntity> getHomeClockData(@Query(C) String c, @Query(DEVICE_INDEX)
            String deviceIndex, @Query(TOKEN) String token);

    /**
     * 获取押话题数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @return
     */
    @GET("mall.php")
    Observable<TopicEntity> getTopic(@Query(C) String c, @Query(DEVICE_INDEX) String deviceIndex,
                                     @Query(TOKEN) String token);

    /**
     * 押注
     *
     * @param c
     * @param betId 押话题活动的id
     * @param red   押注红方的注数
     * @param blue  押注蓝方的注数
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> betTopic(@Query(C) String c, @Query("bet_id") String betId, @Query
            ("red") int red, @Query("blue") int blue);

    /**
     * 获取打卡活动页面数据
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ActClockHomePageEntity> getClockHomePageData(@Query(C) String c);

    /**
     * 参与打卡挑战
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> clockInChallenge(@Query(C) String c, @Query("clockinId") String
            clockInId);

    /**
     * 活动首页 签到
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ActSignInSuccessEntity> signIn(@Query(C) String c);

    /**
     * 活动首页了解更多数据
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ActKnowMoreEntity> getActKnowMoreData(@Query(C) String c);

    /**
     * 打卡
     *
     * @param c
     * @param clockInId
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> clockIn(@Query(C) String c, @Query("clockinId") String clockInId);

    /**
     * 获取我的打卡战绩
     *
     * @param c
     * @param page
     * @param couont
     * @return
     */
    @GET("mall.php")
    Observable<ActClockInMyRecordEntity> getClockInMyRecord(@Query(C) String c, @Query("page")
            int page, @Query("count") int couont);

    /**
     * 获取打卡活动全局排名前十
     *
     * @param c
     * @param clockInId
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<ActClockInTopTenEntity> getActClockInTopTenData(@Query(C) String c, @Query
            ("clockinId") String clockInId, @Query("page") int page, @Query("count") int count);

    /**
     * 对打卡排行榜进行点赞
     *
     * @param c
     * @param recordId
     * @param type
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> clockInRecordPraise(@Query(C) String c, @Query("record_id") String
            recordId, @Query("type") int type);

    /**
     * 求赏
     *
     * @param c
     * @param recordId
     * @param rewardIntegral
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> clockInRecordAskReward(@Query(C) String c, @Query("record_id")
            String recordId, @Query("reward_integral") String rewardIntegral);

    /**
     * 获取打卡排行前十名个人打卡记录
     *
     * @param c
     * @param consumerId
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<ActClockInTopTenPersonRecordEntity> getClockInTopTenPersonRecord(@Query(C) String
                                                                                        c, @Query
                                                                                        ("consumer_id") String consumerId, @Query("page") int page, @Query("count") int count);

    @GET("mall.php")
    Call<LuckySnathNewsList> luckySnatchPrizeList(@Query(C) String c, @Query("page") String page,
                                                  @Query("count") String count);

    @GET("mall.php")
    Call<LuckySnathNewsList> luckySnatchSendList(@Query(C) String c, @Query("page") String page,
                                                 @Query("count") String count);

    @GET("mall.php")
    Call<RemoveRedRecordEntity> removeRedPacketReocrds(@Query(C) String c, @Query("page") String
            page, @Query("count") String count);

    @GET("mall.php")
    Call<RedPacketIndexEntity> redPacketIndex(@Query(C) String c);

    /**
     * app首页是否弹奖励窗
     */
    @GET("mall.php")
    Call<RedPacketWhetherEntity> whetherShowRedDialog(@Query(C) String c);

    /**
     * app首页领奖励
     */
    @GET("mall.php")
    Call<RedPacketOpenOneEntity> openOneRedPacket(@Query(C) String c, @Query("cash_bonus_id")
            String id);

    /**
     * 奖励-每个月的领取记录
     */
    @GET("mall.php")
    Call<RedPacketDateEntity> getRedDate(@Query(C) String c, @Query("year") String year, @Query
            ("month") String month);

    /**
     * 奖励Fragment头部数据
     */
    @GET("mall.php")
    Call<RedPacketFragmentHeadEntity> getRedHead(@Query(C) String c);

    /**
     * 单个奖励激活
     */
    @GET("mall.php")
    Call<HttpResultBean> activityRedPacket(@Query(C) String c, @Query("id") String id);

    /**
     * 一键拆奖励
     */
    @GET("mall.php")
    Call<RedPacketOneKeyRemoveEntity> oneKeyRemoveRedPacket(@Query(C) String c, @Query("type")
            String type);

    /**
     * app首页弹窗是否弹过上报
     */
    @GET("mall.php")
    Call<HttpResultBean> openOneRedPop(@Query(C) String c, @Query("cash_bonus_id") String id);

    /**
     * 获取我的hong
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<StealMyRedpackgets> getMyRedPackgetsResult(@Query(C) String c, @Query("lat")
            String lat, @Query("lng") String lng);

    /**
     * 抢更多的奖励接口
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<StealMoreRedpackgs> getMoreRedPackgetsResult(@Query(C) String c, @Query("page")
            int page, @Query("city_id") String cityId, @Query("lat") String lat,
                                                            @Query("lng") String lng, @Query
                                                                    ("count") int count);

    /**
     * 被偷奖励详情
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<StealMyRedpackgetsDetails> getMyRedPackgetsDetails(@Query(C) String c, @Query
            ("page") int page, @Query("merchant_id") String id, @Query("count") int count);

    /**
     * 查看更多状态
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<StealMoreTrends> getStealRedPackgesMoreTrends(@Query(C) String c, @Query("page")
            int page, @Query("count") int count);

    /**
     * 获取推送状态
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<StealRedPackgesPushStatus> getStealRedPackgesPushStatus(@Query(C) String c);

    /**
     * 设置推送状态
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> setStealRedPackgesPushStatus(@Query(C) String c, @Query("type")
            int type);

    /**
     * 发送奖励
     * 总金额是type--1
     * 单个金额是type--2
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> sendRedPackges(@Query(C) String c, @Query("type") int type, @Query
            ("amount") double amount, @Query("count") String count);

    /**
     * 获取奖励记录
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<SendPackgeRecorder> getRedPackgeRecorder(@Query(C) String c, @Query("page") int
            page, @Query("id") String id, @Query("count") int count);

    /**
     * 获取奖励总额
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<SendRedTotals> getRedPackgeTotalMomey(@Query(C) String c, @Query("page") int page,
                                                     @Query("count") int count);

    /**
     * 获取奖励记录详情
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<SendRedStealedDetails> getStealedRedDetails(@Query(C) String c, @Query("id")
            String id);

    /**
     * 获取本地夺宝列表
     *
     * @param c
     * @param sort
     * @param page
     * @param count
     * @param merchantId
     * @param lat
     * @param lng
     * @return
     */
    @GET("mall.php")
    Observable<LuckyGoodsListEntity> getLocalLuckyData(@Query(C) String c, @Query("sort") String
            sort, @Query("page") int page, @Query("count") int count
            , @Query("merchant_id") String merchantId, @Query("lat") String lat, @Query("lng")
                                                               String lng);

    /**
     * 获取通讯录接口
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ContactSperson> getContactsPerson(@Query(C) String c);

    @POST("mall.php")
    Observable<MemberGiftEntity1> getGiveManInfo(@Query(C) String c, @Query("phone") String phone);

    /**
     * 个人中心所有icons的图片和文字信息
     */
    @GET("mall.php")
    Call<MineIconsEntity> mineIconsMessage(@Query(C) String c);

    /**
     * 2018-2-5 版个人中心
     */
    @GET("mall.php")
    Call<MineIconsEntity> newMineIconsMessage(@Query(C) String c, @Query("val") String val);

    /**
     * 个人中心-线上商城角标
     */
    @GET("mall.php")
    Call<MineMallOrderEntity> mineCornerMessage(@Query(C) String c, @Query("user_id") String
            userId);

    /**
     * 获取快递信息
     */
    @GET("mall.php")
    Call<ExpressInfoEntity> getExpressInfo(@Query(C) String c, @Query("number") String number);

    /**
     * 获取轮播数据
     *
     * @param c
     * @return 6转增成功页面，7我的奖券页面
     */
    @GET("mall.php")
    Observable<BannerDataEntity> getJumpBanner(@Query(C) String c, @Query("type") int type,
                                               @Query("flg") int flg);

    /**
     * 获取我的团队界面轮播信息
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<MemberGroupNotice> getMemberGroupNotice(@Query(C) String c);

    /**
     * 获取区块益豆明细
     */
    @GET("mall.php")
    Observable<BeanEntity> getBeansListData(@Query(C) String c, @Query("date") String date,
                                            @Query("count") int count, @Query("page") int pager);

    /**
     * 获取区块益豆明细详情
     */
    @GET("account.php")
    Observable<V3MoneyDetailEntity> getBeansListDataDetails(@Query(C) String c, @Query("id")
            String id);

    /**
     * 获取奖励、奖券的变更记录列表数据
     *
     * @param c
     * @param page
     * @param type       0 奖励 1 奖券
     * @param filterType 对应所传的筛选类型type数值
     * @param date       筛选时间
     * @return
     */
    @GET("mall.php")
    Observable<MoneyListEntity> getMoneyListData(@Query(C) String c, @Query("page") int page,
                                                 @Query("count") int count, @Query("date") String
                                                         date, @Query("type") int type,
                                                 @Nullable @Query("screen_type") String filterType);

    /**
     * 营收列表
     *
     * @param c
     * @param page
     * @param count
     * @param date
     * @param filterType
     * @return
     */

    @GET("mall.php")
    Observable<MoneyListEntity> getMerchantRevneueList(@Query(C) String c, @Query("page") int page,
                                                       @Query("count") int count, @Query("date") String date,
                                                       @Nullable @Query("screen_type") String filterType);

    /**
     * 兑换券收支明细
     *
     * @param c
     * @param page
     * @param count
     * @param date
     * @param isAgency 是否是服务中心明细 0不是（普通会员明细），1是
     * @return
     */

    @GET("mall.php")
    Observable<ExchangeTicketRecordEntity> getxchangeTicketRecord(@Query(C) String c,
                                                                  @Query("page") int page, @Query("count") int count, @Query("date") String date, @Query("is_agency") String isAgency);

    /**
     * 获取奖励、奖券单笔详情
     * 只适用于领取奖励进度详情
     *
     * @param c
     * @param type 0 余额 1奖券
     * @param id   记录的自增id上个页面的id值
     * @return
     */
    @GET("mall.php")
    Observable<V3MoneyDetailWithDrawEntity> getMoneyWithDrawDetailData(@Query(C) String c, @Query
            ("type") int type, @Query("id") String id);

    /**
     * 营收提取明细详情
     *
     * @param c
     * @param id
     * @return
     */
    @GET("mall.php")
    Observable<V3MoneyDetailWithDrawEntity> getRevenueDetails(@Query(C) String c, @Query("id") String id);

    /**
     * 获取奖励、奖券单笔详情
     *
     * @param c
     * @param type 0 余额 1奖券
     * @param id   记录的自增id上个页面的id值
     * @return
     */
    @GET("{path}")
    Observable<V3MoneyDetailEntity> getMoneyDetailData(@Path("path") String path, @Query(C) String c, @Query("type") int
            type, @Query("id") String id);

    /**
     * 获取奖券、奖励的往来列表数据
     *
     * @param c
     * @param page
     * @param count
     * @param id
     * @param type
     * @return
     */
    @GET("mall.php")
    Observable<V3MoneyDealingsEntity> getMoneyDealingsData(@Query(C) String c, @Query("page") int
            page,
                                                           @Query("count") int count, @Query
                                                                   ("id") String id, @Query
                                                                   ("type") int
                                                                   type);

    /**
     * 从奖券和益豆转赠获取奖券往来列表数据
     *
     * @param c
     * @param page
     * @param count
     * @param id
     * @return
     */
    @GET("mall.php")
    Observable<V3MoneyDealingsEntity> getMoneyDealingsDataFromGive(@Query(C) String c, @Query
            ("page") int page,
                                                                   @Query("count") int count,
                                                                   @Query("be_userid") String id);

    /**
     * 好友打卡排名
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ActClockRankingEntity> getClockRanking(@Query(C) String c, @Query("clockinId") int
            id, @Query("page") int page, @Query("count") int count);

    /**
     * 大家对他的评价列表
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ActCommentRankEntity> getCommentRanking(@Query(C) String c, @Query("record_id")
            String id);

    /**
     * 评论功能
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> subComments(@Query(C) String c, @Query("record_id") String id,
                                           @Query("content") String content);

    /**
     * 对评论点赞
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> praiseComments(@Query(C) String c, @Query("comment_id") String id,
                                              @Query("type") int type);

    /**
     * 提示有人赏
     *
     * @param c
     * @param id
     * @return
     */
    @GET("mall.php")
    Observable<ActSeekRewardEntity> seekReward(@Query(C) String c, @Query("reward_id") String id);

    /**
     * 通过求赏
     * 1--通过
     * 2--拒绝
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> checkSeekReward(@Query(C) String c, @Query("reward_id") String id,
                                               @Query("type") int type);

    /**
     * V5线下商家-推荐商家列表
     */
    @GET("mall.php")
    Observable<V5MerchantListEntity> v5MerchantList(@Query(C) String c, @Query("merchant_id")
            String id, @Query("page") String page, @Query("count") String count, @Query("lat")
                                                            String
                                                            lat, @Query("lng") String lng);

    /**
     * V5线下商家-详情
     */
    @GET("mall.php")
    Observable<V5MerchantDetailEntity> v5MerchantDetail(@Query(C) String c, @Query("merchant_id")
            String id, @Query("lat") String lat, @Query("lng") String lng);

    /**
     * 语音播报开关
     *
     * @param c
     * @param type 0 开启 1关闭
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> setVoiceOnOff(@Query(C) String c, @Query("type") int type);

    /**
     * V5线下商家-套餐
     */
    @GET("mall.php")
    Observable<V5ComboDetailEntity> v5ComboDetail(@Query(C) String c, @Query("package_id") String
            id);

    /**
     * 打赏记录接口
     *
     * @param c
     * @param page
     * @param count
     * @param type
     * @return
     */

    @GET("mall.php")
    Observable<ActRewardRecorderEntity> getRewardRecorder(@Query(C) String c, @Query("page") int
            page, @Query("count") int count, @Query("type") String type);

    /**
     * 获取线上商城首页 banner数据是否更新接口
     *
     * @param c
     * @return
     */

    @GET("service.php")
    Observable<OnlineMallUpData> getMallBannerUpDate(@Query(C) String c, @Query("flg") int flg);

    /**
     * 获取线上商城首页活动的数据
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<OnlineMallActivityEntity> getOnlineMallActivity(@Query(C) String c);

    /**
     * 获取线上商城首页为您推荐数据
     *
     * @param c
     * @param page
     * @param count
     * @return
     */
    @GET("mall.php")
    Observable<OnlineMallRecommendGoodsEntity> getOnlineMallRecommendGoodsList(@Query(C) String
                                                                                       c, @Query
                                                                                       ("page")
                                                                                       int page,
                                                                               @Query
                                                                                       ("count")
                                                                                       int count);

    @GET
    Observable<ResponseBody> getYiDouBaoIndex(@Url String yiDouBaoIndexUrl);

    /**
     * 运营活动加入后-我的银行卡列表
     */
    @GET("mall.php")
    Observable<CardMyBankCardsListEntity> getMyBankCardsList(@Query(C) String c);

    /**
     * 设置默认银行卡
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> setCashBankCard(@Query(C) String c, @Query("card_index") String
            cardIndex);

    /**
     * 运营活动加入后-我的银行卡列表
     */
    @GET("mall.php")
    Observable<CardMyBankCardsListEntity> getUserBankCardsList(@Query(C) String c);

    /**
     * 设置昵称
     *
     * @param c
     * @param nickName
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> setNickName(@Query(C) String c, @Query("name") String nickName);

    /**
     * 设置个性签名
     *
     * @param c
     * @param stateOfMind
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> setStateOfMind(@Query(C) String c, @Query("signature") String
            stateOfMind);

    /**
     * 获取用户基本信息
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<BasicInformationEntity> getBasicInformation(@Query(C) String c);

    /**
     * 设置用户基本信息
     *
     * @param c
     * @param sex
     * @param age
     * @param birth
     * @param bloodType
     * @param school
     * @param company
     * @param jobId
     * @param jobName
     * @param homeProvince
     * @param homeCity
     * @param homeCounty
     * @param locationProvince
     * @param locationCity
     * @param currentCounty
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> setBasicInformation(@Query(C) String c,
                                                   @Query("sex") String sex, @Query("age") String
                                                           age,
                                                   @Query("birthday") String birth, @Query
                                                           ("blood_type") String bloodType,
                                                   @Query("school") String school, @Query
                                                           ("company") String company,
                                                   @Query("profession") String jobId, @Query
                                                           ("profession_name") String jobName,
                                                   @Query("home_province") String homeProvince,
                                                   @Query("home_city") String homeCity, @Query
                                                           ("home_county") String homeCounty,
                                                   @Query("current_province") String
                                                           locationProvince, @Query("current_city")
                                                           String locationCity, @Query
                                                           ("current_county")
                                                           String currentCounty
    );

    /**
     * 获取职业分类数据
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ProfessionEntity> getProfessionData(@Query(C) String c);

    /**
     * 获取省市县
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<RegionEntity2> getRegion(@Query(C) String c);

    /**
     * 根据经纬度获取系统自己维护的位置信息
     *
     * @param c
     * @param lat
     * @param lng
     * @return
     */
    @GET("mall.php")
    Observable<Location> getLocation(@Query(C) String c, @Query("lat") String lat, @Query("lng")
            String lng);

    /**
     * 新版获取短信
     *
     * @param c
     * @param phone
     * @param verifyType 1需要校验图形验证码 2不需要校验图形验证码 其它为登录状态下获取短信验证码
     * @param type       1快捷登录 2修改绑定手机号 3重置支付密码 4找回登录密码 5提现申请 6绑定银行卡 7设置支付密码
     *                   8绑定手机号 9设置登录密码 10设置商户绑定微信 11审核打款成功 12绑定支付宝账号 13短信注册
     *                   14商家资料已审核拒绝 15商家资料已审核通过
     * @param voice      如果是语音验证码 需要传该字段
     * @param verifyCode 如果有图形验证码 需要传该字段
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> getSmsCodeV2(@Query(C) String c, @Query("phone") String phone,
                                            @Query("verify_type") int verifyType,
                                            @Query("type") int type, @Nullable @Query("voice")
                                                    String voice,
                                            @Nullable @Query("verify_code") String verifyCode);

    /**
     * 发送验证码前是否弹图形验证码
     *
     * @return
     */
    @GET("mall.php")
    Observable<IsShowImgCode> getShowImgCode(@Query(C) String c);

    @GET("mall.php")
    Observable<HttpResultBean> checkSmsCode(@Query(C) String c, @Query("phone") String phone,
                                            @Query("SMS_code") String smsCode);

    /**
     * 实名认证 银行卡四元素通过之后 根据验证码添加认证
     *
     * @param c
     * @param cardNumber   银行卡号
     * @param userName     姓名
     * @param bankName     银行名称
     * @param phone        手机号
     * @param idCard       身份证号
     * @param servicePhone 银行卡信息接口（c=userAuth/bank_card_info）返回该字段
     * @param smsCode      短信验证码
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> addAuth(@Query(C) String c, @Query("card_number") String cardNumber,
                                       @Query("user_name") String userName,
                                       @Query("bank_name") String bankName, @Query("phone") String phone,
                                       @Query("ID_card") String idCard, @Query("service_phone") String servicePhone,
                                       @Query("sms_code") String smsCode);

    /**
     * 身份证/姓名校验 也是实名认证校验
     *
     * @param c
     * @param userName
     * @param idCard
     * @param smsCode
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> addAuthV2(@Query(C) String c, @Query("user_name") String userName,
                                         @Query("ID_card") String idCard, @Query("SMS_code") String smsCode);

    /**
     * 根据银行卡号 获取该卡所属银行信息
     *
     * @param c
     * @param bankCardNumber
     * @return
     */
    @GET("mall.php")
    Observable<BankCardEntity> getBankCardInfo(@Query(C) String c, @Query("card_number") String
            bankCardNumber);

    /**
     * 检查银行卡四元素是否匹配
     *
     * @param c
     * @param cardNumber 银行卡号
     * @param userName   姓名
     * @param idCard     身份证号
     * @param phone      手机号
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> checkBankCard4Element(@Query(C) String c, @Query("card_number")
            String cardNumber,
                                                     @Query("user_name") String userName, @Query
                                                             ("ID_card") String idCard,
                                                     @Query("phone") String phone);

    @GET("mall.php")
    Observable<CerResultEntity> getCerResult(@Query(C) String c);

    /**
     * 上传身份证照片
     *
     * @param c
     * @param image
     * @return
     */
    @Multipart
    @POST("mall.php")
    Observable<UploadImageEnity> uploadImage(@Query(C) String c, @Part MultipartBody.Part image);

    @GET("mall.php")
    Observable<HttpResultBean> addIdCardInfo(@Query(C) String c, @Query("card_front") String
            faceUrl,
                                             @Query("card_behind") String backUrl, @Query
                                                     ("valid_time") String validTime);

    /**
     * 识别驾驶证信息
     */
    @POST("http://dm-52.data.aliyun.com/rest/160601/ocr/ocr_driver_license.json")
    Observable<ResponseBody> checkDriverCard(@HeaderMap Map<String, String> headers, @Body
            RequestBody body);

    @POST("http://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json")
    Observable<ResponseBody> checkIdCard(@HeaderMap Map<String, String> headers, @Body
            RequestBody body);

    /**
     * @param c
     * @param type 0私有的 1对公的
     * @return
     */
    @GET("mall.php")
    Observable<SupportBankListEntity> getSupportBankList(@Query(C) String c, @Query("type") int
            type);

    /**
     * 新版获取默认提现银行卡
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<TakeCashEntity2> getTakeCashDefaultCard(@Query(C) String c);

    /**
     * 实名认证后的设置支付密码接口
     */
    @GET("mall.php")
    Observable<HttpResultBean> setPasswordAfterCer(@Query(C) String c, @Query("new_pay_password")
            String password);

    /**
     * 商家个人中心-信息
     */
    @GET("mall.php")
    Observable<MerchantCenterMsgEntity> getMerchantCenter(@Query(C) String c);

    /**
     * 商家个人中心-店铺管理
     */
    @GET("mall.php")
    Observable<MerchantManageEntity> getMerchantManage(@Query(C) String c);

    /**
     * 商家个人中心-信息修改中的信息
     */
    @GET("mall.php")
    Observable<MerchantCenterInfoEntity> getMerchantInfo(@Query(C) String c);

    /**
     * 商家个人中心-更改店铺名字
     */
    @GET("mall.php")
    Observable<HttpResultBean> setMerchantName(@Query(C) String c, @Query("supplier_name") String
            name);

    /**
     * 商家个人中心-获取商家发货地址
     */
    @GET("mall.php")
    Observable<MerchantAddressListEntity> getMerchantAddressList(@Query(C) String c);

    /**
     * 信用通-上部信息
     */
    @GET
    Observable<MerchantXytTopEntity> xytTopMsg(@Url String url, @Query("shopID") String shopID,
                                               @Query("type") String type);

    /**
     * 信用通-底部信息
     */
    @GET
    Observable<MerchantXytEntity> xytMsg(@Url String url, @Query("shopID") String shopID, @Query
            ("type") String type);

    /**
     * 获取经营分类
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<MerchantEnterCategory3Entity> getMerchantCategoryData(@Query(C) String c, @Query
            ("type") int type);

    /**
     * @param c
     * @param shopType    店铺类型 0线下 1线上
     * @param certType    认证类型 2个人 3个体户 4企业
     * @param shopName    店铺名称
     * @param shopAddress 详细地址
     * @param lon         地理位置经度
     * @param lat         地理位置维度
     * @param classifyId  商品分类id,多个逗号分隔
     * @return
     */
    @GET("mall.php")
    Observable<AddMerchantInfoEntity> addMerchantInfo(@Query(C) String c, @Query("id") String
            merchantId, @Query("shop_type") int shopType,
                                                      @Query("entity_type") int certType, @Query
                                                              ("shop_name") String shopName, @Query
                                                              ("shop_address") String shopAddress,
                                                      @Query("merchant_longitude") double lon,
                                                      @Query("merchant_latitude") double lat,
                                                      @Query("classify_id") String classifyId);

    /**
     * 获取区块益豆数据
     *
     * @param c
     * @param page
     * @param count
     * @param beanType 0 首页区块益豆数据 5专区区块益豆数据
     * @param sort     0000默认1000 最新 0100价格升 0200价格降 0010销量 0001有货  null 不排序
     * @return
     */
    @GET("mall.php")
    Observable<LeDouHomePageDataEntity> getLeDouHomePageData(@Query(C) String c, @Query("page")
            int page, @Query("count") int count,
                                                             @Query("bean_type") int beanType,
                                                             @Nullable @Query("sort") String sort,
                                                             @Nullable @Query("class_id") String
                                                                     classId);

    /**
     * 提取区块益豆到乐淘天使
     *
     * @param beanAmount 区块益豆提取数量
     * @param address    钱包地址
     * @return
     */
    @GET("account.php")
    Observable<ExtractLeTaoAngleEntity> extractLeDou(@Query(C) String c,
                                                     @Query("bean_amount") String beanAmount, @Query("address") String address);

    /**
     * 提取益豆到钱包
     *
     * @param c
     * @param beanAmount
     * @param address
     * @return
     */
    @GET("account.php")
    Observable<ExtractLeTaoAngleEntity> extractLeDouToPurse(@Query(C) String c,
                                                            @Query("bean_amount") String beanAmount, @Query("address") String address);

    /**
     * 重新提取益豆
     *
     * @param id
     * @param address
     * @return
     */
    @GET("account.php")
    Observable<HttpResultBean> reExtractLeDou(@Query(C) String c, @Query("id") String id, @Query("address") String address);

    /**
     * 获取首页专区数据
     *
     * @return
     */
    @GET("account.php")
    Observable<HomePageAreaEntity> getHomePageAreaData(@Query(C) String c);

    /**
     * 获取仿美团版本首页信息
     *
     * @param cityId   用户定位城市ID，必须是具体的城市ID
     * @param countyId 用户定位区县id
     * @param flag     flag 从0开始 如果升级则手动+1   当前是0
     */
    @GET("mall.php")
    Observable<MTHomePageEntity> getHomePageHeaderData(@Query(C) String c,
                                                       @Query("city") String cityId, @Query("county")
                                                               String countyId, @Query("app_version") String
                                                               appVersion,
                                                       @Nullable @Query("game_show") String gameShow,
                                                       @Query("flag") int flag);

    /**
     * 获取购物车数据
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<ShoppingCartListEntity2> getShoppingCartList(@Query(C) String c);

    @GET("mall.php")
    Observable<ShoppingCartListEntity2> modifyShoppingCartNum(@Query(C) String c, @Query("cart_id") String cartId,
                                                              @Query("count") String count);

    /**
     * 获取商品详情
     *
     * @param c
     * @param filialeId
     * @param goodsId
     * @param goodsType
     * @return
     */
    @GET("mall.php")
    Observable<JPCommodityDetail> getV3MallGoodInfo(@Query(C) String c, @Query("filiale_id") String filialeId,
                                                    @Query("goods_id") String goodsId, @Query("type") String goodsType);

    /**
     * 从商品详情下订单获取运费
     *
     * @return
     */
    @GET("mall.php")
    Observable<MallGoodsExpressEntity> getGoodDetailExpressV3(@Query(C) String c, @Query("goods_count") String goodsCount,
                                                              @Query("goods_sku") String goodsSKU, @Query("filiale_id") String filialeId,
                                                              @Query("address_id") String addressId,
                                                              @Query("supplier_id") String supplierId, @Query("goods_id") String goodsId,
                                                              @Query("type") String type);

    /**
     * 从购物车下单获取物流费用
     *
     * @param cartList
     * @param addressId
     */
    @GET("mall.php")
    Observable<MallGoodsExpressEntity> getCartExpressV3(@Query(C) String c, @Query("cart_list") String cartList,
                                                        @Query("address_id") String addressId);

    @GET("mall.php")
    Observable<HttpResultBean> collectV3(@Query(C) String c, @Query("filiale_id") String filialeId, @Query("id") String id,
                                         @Query("type") String type, @Query("collect_type") String collectType);

    /**
     * 请求商品分类数据
     *
     * @param classId "0"
     *                type 0是大分类，1是大分类下对应的二级分类
     */
    @GET("mall.php")
    Observable<JPGoodsClassfiyEntity> getGoodsClassList(@Query(C) String c, @Query("class_id") String classId, @Query("type") String type);

    /**
     * 获取京东首页为您推荐列表数据
     *
     * @param c
     * @param page
     * @param count
     * @return
     */
    @GET("jd.php")
    Observable<JDGoodsListEntity> getJDGoodsListData(@Query(C) String c, @Query("page") int page, @Query("count") int count);

    /**
     * 京东首页列表头部
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<JDHomePageTop> getJdHomePageTop(@Query(C) String c);


    /**
     * 苏宁首页列表头部
     *
     * @param c
     * @return
     */
    @GET("suning.php")
    Observable<SnHomePageTop> getSnHomePageTop(@Query(C) String c);

    /**
     * 获取京东首页通知
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<JdHomePageNoticeEntity> getJdHomePageNotice(@Query(C) String c);

    /**
     * 首页提示消息
     *
     * @param c
     * @return
     */
    @GET("suning.php")
    Observable<SnHomePageNoticeEntity> getSnHomePageNotice(@Query(C) String c);

    /**
     * 京东首页分类接口
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<JdGoodsClassifyEntity> getJDGoodsClassify(@Query(C) String c);

    /**
     * 苏宁首页分类接口
     *
     * @param c
     * @return
     */
    @GET("suning.php")
    Observable<SnGoodsClassifyEntity> getSnGoodsClassify(@Query(C) String c);


    /**
     * 获取京东订单列表
     *
     * @param c
     * @param type  0全部 1待付款 2待收货 3已完成 4已取消
     * @param page  页数
     * @param count 页面条数
     * @return
     */
    @GET("jd.php")
    Observable<JDOrderListEntity> getJDOrderList(@Query(C) String c, @Query("type") int type, @Query("page") int page, @Query("count") int count);

    /**
     * 京东再次购买
     *
     * @param c
     * @param goods_counts 逗号分隔多个商品数量
     * @param goods_skus   逗号分隔多个商品sku 和商品数量一一对应
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> setJDBuyAgain(@Query(C) String c, @Query("goods_counts") String goods_counts, @Query("goods_skus") String goods_skus);

    /**
     * 获取京东订单详情
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @return
     */
    @GET("jd.php")
    Observable<JDOrderDetailEntity> getJDOrderetails(@Query(C) String c, @Query("jdOrderId") String jdOrderId);

    /**
     * 京东政企查询物流信息
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @return
     */
    @GET("jd.php")
    Observable<JDOrderLogisticsEntity> getJDOrderLogistics(@Query(C) String c, @Query("jdOrderId") String jdOrderId);

    /**
     * 京东政企删除订单
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> jdDeleteOrder(@Query(C) String c, @Query("jdOrderId") String jdOrderId);

    /**
     * 京东政企判断订单中的商品是否可以申请售后
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @param skuId
     * @return
     */
    @GET("jd.php")
    Observable<JDCheckAvailableAfterSaleEntity> jdCheckAvailableAftersaler(@Query(C) String c, @Query("jdOrderId") String jdOrderId, @Query("skuId") String skuId);

    /**
     * 京东政企确认收货接口
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> jdConfirmGoods(@Query(C) String c, @Query("jdOrderId") String jdOrderId);

    /**
     * 取消订单
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> jdCancleOrder(@Query(C) String c, @Query("jdOrderId") String jdOrderId);

    /**
     * 申请结算益豆
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> jdApplyOrderSettle(@Query(C) String c, @Query("order_index") String jdOrderId);

    /**
     * 获取京东商品详情
     *
     * @param c
     * @param sku
     * @param lat
     * @param lng
     * @param jdType 类型 0普通京东商品 1购物卡京东商品
     * @return
     */
    @GET("jd.php")
    Observable<JDGoodsDetailInfoEntity> getJDGoodsDetailInfo(@Query(C) String c,
                                                             @Query("sku") String sku, @Query("lat") String lat, @Query("lng") String lng, @Query("jd_type") String jdType);

    /**
     * 京东政企意见反馈类型
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<FeedbackTypeEntity> getJDFeedbackType(@Query(C) String c);

    /**
     * 京东政企意见反馈类型
     *
     * @param c
     * @param type       反馈类型 汉字
     * @param content    反馈内容
     * @param img        图片 逗号分隔 全路径
     * @param contact    反馈手机号
     * @param goods_sku  商品sku
     * @param goods_name 商品名称
     * @param goods_img  商品主图 全路径
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> setJDFeedback(@Query(C) String c, @Query("type") String type, @Query("content") String content, @Query("img") String img, @Query("contact") String contact, @Query("goods_sku") String goods_sku, @Query("goods_name") String goods_name, @Query("goods_img") String goods_img);

    /**
     * 获取京东申请售后列表
     *
     * @param c
     * @param page  页数
     * @param count 页面条数
     * @return
     */
    @GET("jd.php")
    Observable<JDAfterSaleApplyForListEntity> getJDAfterSaleApplyForList(@Query(C) String c, @Query("page") int page, @Query("count") int count);

    /**
     * 获取京东售后申请记录
     *
     * @param c
     * @param page  页数
     * @param count 页面条数
     * @return
     */
    @GET("jd.php")
    Observable<JDAfterSaleApplicationRecordListEntity> getJDAfterSaleApplicationRecordList(@Query(C) String c, @Query("page") int page, @Query("count") int count);

    /**
     * 京东售后申请填写
     *
     * @param c
     * @param jdOrderId 京东订单号
     * @param skuId
     * @return
     */
    @GET("jd.php")
    Observable<JDAfterSaleApplyForDataEntity> getJDAfterSaleApplyForData(@Query(C) String c, @Query("jdOrderId") String jdOrderId, @Query("skuId") String skuId);

    /**
     * 京东售后记录详情
     *
     * @param c
     * @param afsServiceId 售后服务单号
     * @param skuId        售后商品id
     * @param skuNum       售后商品数量
     * @return
     */
    @GET("jd.php")
    Observable<JDAfterSaleDetialEntity> getJDServiceDetial(@Query(C) String c, @Query("afsServiceId") String afsServiceId, @Query("skuId") String skuId, @Query("skuNum") String skuNum);

    /**
     * 京东售后记录详情
     *
     * @param c
     * @param afsServiceId   售后服务单号
     * @param expressCompany 发运公司: 圆通快递、申通快递、韵达快递、中 通快递、宅急送、EMS、顺丰快递
     * @param expressCode    发运单号
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> updateSendNum(@Query(C) String c, @Query("afsServiceId") String afsServiceId, @Query("expressCompany") String expressCompany, @Query("expressCode") String expressCode);

    /**
     * 京东政企取消售后订单
     *
     * @param c
     * @param afsServiceId 售后服务单号
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> jsAftersaleCancel(@Query(C) String c, @Query("afsServiceId") String afsServiceId);

    /**
     * 京东政企取消售后订单
     *
     * @param c
     * @param afsServiceId 售后服务单号
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> jdDeleteServiceOrder(@Query(C) String c, @Query("afsServiceId") String afsServiceId);

    /**
     * 京东申请售后
     *
     * @param c
     * @param jdOrderId   京东订单号
     * @param skuId       商品id
     * @param skuNum      商品数量
     * @param serviceType //退货(10)、换货(20)、维修(30)
     * @param describe    描述
     * @param img         图片 逗号分隔
     * @param returnType  4 上门取件 7 客户送货40 客户发货
     * @param address     地址
     * @param name        用户
     * @param mobile      手机号
     * @param provinceId  省ID
     * @return
     */
    @GET("jd.php")
    Observable<JDAfterSaleApplyForSubmitEntity> setJdAftersaleAction(@Query(C) String c, @Query("jdOrderId") String jdOrderId, @Query("skuId") String skuId, @Query("skuNum") String skuNum,
                                                                     @Query("serviceType") String serviceType, @Query("describe") String describe, @Query("img") String img, @Query("returnType") String returnType,
                                                                     @Query("address") String address, @Query("name") String name, @Query("mobile") String mobile, @Query("provinceId") String provinceId);

    /**
     * 京东更新售后订单
     *
     * @param c
     * @param id 京东申请售后(setJdAftersaleAction（）) 成功返回值
     * @return
     */
    @GET("jd.php")
    Observable<JDAfterSaleUpdEntity> getJdAftersaleUpd(@Query(C) String c, @Query("id") String id);

    /**
     * 获取京东首页品牌精选列表
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<JdGoodsBrandSelectedEntity> getBrandSelectdList(@Query(C) String c, @Query("page") int page, @Query("count") int count);

    /**
     * 苏宁品牌精选
     *
     * @param c
     * @param page
     * @param count
     * @return
     */
    @GET("suning.php")
    Observable<SnGoodsBrandSelectedEntity> getSnBrandSelectdList(@Query(C) String c, @Query("page") int page, @Query("count") int count);

    /**
     * 获取京东省/市/区县/镇地址列表
     *
     * @param c
     * @param type:  1 一级地址 2 二级地址 3 三级地址 4 四级地址 当type=1时areaId=0
     * @param areaId 地址id
     * @return
     */
    @GET("jd.php")
    Observable<JDAreaStringData> getJDAreaStringData(@Query(C) String c, @Query("type") String type, @Query("areaId") String areaId);

    /**
     * @param type       0 添加 1更新
     * @param addressId  添加地址是传0 否则传地址id
     * @param provinceId
     * @param province
     * @param cityId
     * @param city
     * @param countyId
     * @param county
     * @param townId     没有情况下传0
     * @param town       没有情况下传空字符串
     * @param name
     * @param mobile
     * @param address
     * @param isDefault  是否默认 添加默认为0 更新根据地址情况传原值
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<HttpResultBean> updateJDAddress(@Query(C) String c,
                                               @Field("type") int type, @Field("address_id") String addressId, @Field("provinceId") String provinceId,
                                               @Field("province") String province, @Field("cityId") String cityId, @Field("city") String city, @Field("countyId") String countyId,
                                               @Field("county") String county, @Field("townId") String townId, @Field("town") String town, @Field("name") String name,
                                               @Field("mobile") String mobile, @Field("address") String address, @Field("default_address") int isDefault);

    /**
     * 获取用户JD收货地址列表
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<JDShippingAddressList> getJDShippingAddressList(@Query(C) String c);

    /**
     * 设置JD默认收货地址
     *
     * @param c
     * @param addressId
     * @param defaultAddress 0 取消默认 1设置默认
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<HttpResultBean> setJDDefaultShippingAddress(@Query(C) String c,
                                                           @Field("address_id") String addressId, @Field("default_address") int defaultAddress);

    /**
     * 从商品详情下单获取运费
     *
     * @param c
     * @param skuId
     * @param count
     * @param addressId
     * @return
     */
    @GET("jd.php")
    Observable<JDFreightEntity> getJDFreightData(@Query(C) String c, @Query("sku") String skuId, @Query("num") int count, @Query("user_address_id") String addressId);

    /**
     * 从购物车下单获取运费
     *
     * @param c
     * @param cartList  购物车列表 1,2多个以逗号分隔
     * @param addressId
     * @return
     */
    @GET("jd.php")
    Observable<JDFreightEntity> getJDFreightDataFromShoppingCart(@Query(C) String c, @Query("cart_list") String cartList,
                                                                 @Query("user_address_id") String addressId);

    /**
     * 提交JD订单
     *
     * @param c
     * @param skuId     商品id
     * @param count     商品数量
     * @param addressId 用户地址id
     * @param remark    订单备注
     * @param price     商品协议价
     * @param imgPath   商品主图地址(服务器在详情中返回的图片地址第一条,服务器返回什么,这里传递什么 不做处理)
     * @param jdType    0普通专区商品下单   1购物卡专区商品下单
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDCommitOrderSuccessEntity> commitJDOrder(@Query(C) String c, @Field("sku") String skuId,
                                                         @Field("num") int count, @Field("user_address_id") String addressId,
                                                         @Field("remark") String remark, @Field("price") String price,
                                                         @Field("pic") String imgPath, @Field("is_quan") String useDaiGouQuan,
                                                         @Field("jd_type") int jdType);

    /**
     * 购物车提交JD订单
     *
     * @param c
     * @param cartList  购物车列表 1,2多个以逗号分隔
     * @param addressId 用户地址id
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDCommitOrderSuccessEntity> commitJDOrderShoppingCart(@Query(C) String c, @Field("cart_list") String cartList,
                                                                     @Field("user_address_id") String addressId, @Field("is_quan") String useDaiGouQuan);

    /**
     * @param c
     * @param page
     * @param count
     * @param brandName 品牌名
     * @param sort      默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy   asc升序 desc降序
     * @return
     */
    @GET("jd.php")
    Observable<JdBrandGoodsListEntity> getJdBrandGoodsList(@Query(C) String c, @Query("page") int page, @Query("count") int count,
                                                           @Query("brandName") String brandName,
                                                           @Query("sort") String sort,
                                                           @Query("order_by") String orderBy);

    /**
     * 京东政企三级分类商品列表
     *
     * @param c
     * @param page
     * @param count
     * @param catId   三级分类id
     * @param sort    默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy asc升序 desc降序
     * @return
     */
    @GET("jd.php")
    Observable<JdBrandGoodsListEntity> getJdThirdClassifyGoodsList(@Query(C) String c, @Query("page") int page, @Query("count") int count,
                                                                   @Query("catId") String catId,
                                                                   @Query("sort") String sort,
                                                                   @Query("order_by") String orderBy);

    /**
     * 京东政企商品搜索
     *
     * @param c
     * @param page
     * @param count
     * @param search_val 搜索关键字
     * @param sort       默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy    asc升序 desc降序
     * @return
     */
    @GET("jd.php")
    Observable<JdBrandGoodsListEntity> searchJdGoods(@Query(C) String c, @Query("page") int page, @Query("count") int count,
                                                     @Query("search_val") String search_val,
                                                     @Query("sort") String sort,
                                                     @Query("order_by") String orderBy);

    /**
     * 获取用户金额
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<MyBalanceEntity> getUserMoney(@Query(C) String c);

    /**
     * JD 使用奖励支付
     *
     * @param orderIndex
     * @param payPwd
     * @param merchantIncomePayMoney 营收额支付金额 单位分
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDCashPayResult> jdPay(@Query(C) String c,
                                      @Field("order_index") String orderIndex,
                                      @Field("pay_pwd") String payPwd,
                                      @Field("moneys") String merchantIncomePayMoney);

    /**
     * JD第三方预充值
     *
     * @param fee        充值金额 单位元
     * @param payType    1支付宝 2微信 3微信公共账号 4网银
     * @param orderIndex
     * @param type       商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11     京东购物卡订单 15
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDThirdPayPreRechargeOrderEntity> jdPreRecharge(@Query(C) String c, @Field("payment_fee") String fee,
                                                               @Field("pay_type") String payType, @Field("order_index") String orderIndex,
                                                               @Field("type") String type, @Field("moneys") String merchantIncomePayMoney);


    /**
     * 删除收货地址
     *
     * @param addressId
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<HttpResultBean> deleteJdShippingAddress(@Query(C) String c, @Field("address_id") String addressId);

    /**
     * 提交订单前检查库存
     *
     * @param c
     * @param skuId
     * @param count
     * @param addressId
     * @param jdType    0普通专区商品下单   1购物卡专区商品下单
     * @return
     */
    @GET("jd.php")
    Observable<JDGoodsStoreEntity> checkJDStore(@Query(C) String c, @Query("sku") String skuId, @Query("num") int count,
                                                @Query("user_address_id") String addressId, @Query("jd_type") int jdType);

    /**
     * 购物车提交订单前检查库存
     *
     * @param c
     * @param cartList  购物车列表 1,2多个以逗号分隔
     * @param addressId
     * @return
     */
    @GET("jd.php")
    Observable<JDGoodsStoreEntities> checkJDStoreShoppingCart(@Query(C) String c, @Query("cart_list") String cartList,
                                                              @Query("user_address_id") String addressId);

    /**
     * 提交订单前检测价格是否改变
     * @param jdType    0普通专区商品下单   1购物卡专区商品下单
     */
    @GET("jd.php")
    Observable<JDCheckPriceEntity> checkJDPrice(@Query(C) String c, @Query("sku") String skuId, @Query("jd_type") int jdType);

    @GET("jd.php")
    /**
     * 从购物车提交订单前检测价格是否改变
     */
    Observable<JDCheckPriceEntities> checkJDPriceShoppingCart(@Query(C) String c, @Query("cart_list") String cartList);

    /**
     * 检查是否支持在该地区销售
     *
     * @param c
     * @param skuIds
     * @param provinceId
     * @param cityId
     * @param countyId
     * @param townId
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDIsAreaRestrict> checkJDAreaSupportSell(@Query(C) String c, @Field("skuIds") String skuIds, @Field("province") String provinceId,
                                                        @Field("city") String cityId, @Field("county") String countyId, @Field("town") String townId);

    /**
     * 获取京东购物车列表
     *
     * @return
     */
    @GET("jd.php")
    Observable<JdShoppingCarEntity> getJdShoppingCarList(@Query(C) String c);

    /**
     * 删除购物车中的商品
     *
     * @param c
     * @param cartIndex
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> delGoodsFromJdShoppingCar(@Query(C) String c, @Query("cart_index") String cartIndex);

    /**
     * 京东购物车更新
     *
     * @param c
     * @param goodsCount
     * @param goodsSku
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> updateJdShoppingCarGoodsCounts(@Query(C) String c, @Query("goods_count") String goodsCount, @Query("goods_sku") String goodsSku);

    /**
     * 京东购物车数量
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<JdShoppingCarGoodsCountsEntity> getJdShoppingCarGoodsCounts(@Query(C) String c);

    /**
     * 京东添加商品到购物车
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> addJdGoodsToShoppingCar(@Query(C) String c, @Query("goods_count") String goodsCount, @Query("goods_sku") String goodsSku);


    /**
     * 提取营收接口
     *
     * @param c
     * @param cash_amount
     * @param card_index
     * @return
     */
    @POST("account.php")
    Observable<MerchantCashSuccessEntity> takeCash(@Query("c") String c, @Query("cash_amount") String cash_amount,
                                                   @Query("card_index") String card_index);

    /**
     * 修改提现订单
     *
     * @param c
     * @param orderId
     * @param cardNum
     * @param cardBank
     * @param branchBank
     * @param cardHolder
     * @param bankCode
     * @param province
     * @param city
     * @param type
     * @return
     */
    @GET("account.php")
    Observable<HttpResultBean> modifyExtractOrder(@Query(C) String c, @Query("order_id") String orderId,
                                                  @Query("card_number") String cardNum, @Query("card_bank") String cardBank,
                                                  @Query("branch_bank") String branchBank, @Query("card_holder") String cardHolder,
                                                  @Query("bank_code") String bankCode, @Query("province") String province,
                                                  @Query("city") String city,
                                                  @Query("type") int type);

    @GET("jd.php")
    Observable<JdDefaultAddressListEntity> getJdDefaultShippingAddress(@Query(C) String c);

    /**
     * 不使用第三方支付
     *
     * @param c
     * @param orderId        支付订单ID
     * @param payPwd         支付密码
     * @param merchantIncome 商家营收款
     * @return
     */
    @FormUrlEncoded
    @POST("mall.php")
    Observable<PayOkEntity> payCash(@Field(C) String c, @Field("order_index") String orderId, @Field("pay_pwd") String payPwd,
                                    @Field("moneys") String merchantIncome);

    /**
     * 生成第三方支付预支付订单
     *
     * @param c
     * @param payType        //1支付宝 2微信 3微信公共账号 4网银
     * @param paymentFee     //支付总价 单位元
     * @param consumerOpenid //微信唯一id app情况传0
     * @param type           //0正常充值 1乐享币直充
     * @param orderId        订单号
     * @param merchantIncome 商家营收款 单位分
     * @return
     */
    @POST("mall.php")
    Observable<JsPayClass> createGoodsThirdPreparePayOrder(@Query(C) String c,
                                                           @Query("pay_type") String payType,
                                                           @Query("payment_fee") String paymentFee,
                                                           @Query("consumer_openid") String consumerOpenid,
                                                           @Query("type") String type,
                                                           @Query("order_index") String orderId,
                                                           @Query("moneys") String merchantIncome);

    /**
     * 获取支付结果
     *
     * @param c
     * @param orderId 支付订单号
     * @param type    商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @return
     */
    @GET("payment.php")
    Observable<GoodsChargeForPayResultEntity> getPayResult(@Query(C) String c, @Query("order_index") String orderId,
                                                           @Query("type") String type);


    /**
     * 获取JD支付结果
     *
     * @param orderIndex 益联短ID
     * @param type       商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @return
     */
    @GET("payment.php")
    Observable<JDPayInfoEntity> getJdPayInfo(@Query(C) String c, @Query("order_index") String orderIndex, @ThirdPayForType.PayType @Query("type") String type);


    /**
     * 苏宁政企意见反馈类型
     *
     * @param c
     * @return
     */
    @GET("suning.php")
    Observable<FeedbackTypeEntity> getSnFeedbackType(@Query(C) String c);

    /**
     * 苏宁政企意见反馈类型
     *
     * @param c
     * @param type       反馈类型 汉字
     * @param content    反馈内容
     * @param img        图片 逗号分隔 全路径
     * @param contact    反馈手机号
     * @param goods_sku  商品sku
     * @param goods_name 商品名称
     * @param goods_img  商品主图 全路径
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> setSnFeedback(@Query(C) String c, @Query("type") String type, @Query("content") String content, @Query("img") String img, @Query("contact") String contact, @Query("goods_sku") String goods_sku, @Query("goods_name") String goods_name, @Query("goods_img") String goods_img);

    /**
     * 苏宁政企品牌商品列表
     *
     * @param c
     * @param page
     * @param count
     * @param brand   品牌名
     * @param sort    默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy asc升序 desc降序
     * @return
     */
    @GET("suning.php")
    Observable<SnBrandGoodsListEntity> getSnBrandGoodsList(@Query(C) String c, @Query("page") int page, @Query("count") int count,
                                                           @Query("brand") String brand,
                                                           @Query("sort") String sort,
                                                           @Query("order_by") String orderBy);

    /**
     * 苏宁政企搜索接口
     *
     * @param c
     * @param page
     * @param count
     * @param search_val 搜索关键字
     * @param sort       默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy    asc升序 desc降序
     * @return
     */
    @GET("suning.php")
    Observable<SnBrandGoodsListEntity> searchSnGoods(@Query(C) String c, @Query("page") int page, @Query("count") int count,
                                                     @Query("search_val") String search_val,
                                                     @Query("sort") String sort,
                                                     @Query("order_by") String orderBy);

    /**
     * 苏宁政企三级分类商品列表
     *
     * @param c
     * @param page
     * @param count
     * @param catId   三级分类id
     * @param sort    默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy asc升序 desc降序
     * @return
     */
    @GET("suning.php")
    Observable<SnBrandGoodsListEntity> getSnThirdClassifyGoodsList(@Query(C) String c,
                                                                   @Query("page") int page, @Query("count") int count,
                                                                   @Query("catId") String catId,
                                                                   @Query("sort") String sort,
                                                                   @Query("order_by") String orderBy);

    /**
     * 精选推荐
     *
     * @param c
     * @param page
     * @param count
     * @return
     */
    @GET("suning.php")
    Observable<SnGoodsListEntity> getSnGoodsListData(@Query(C) String c, @Query("page") int page, @Query("count") int count);

    /**
     * 苏宁购物车更新
     *
     * @param c
     * @param goodsCount
     * @param goodsSku
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> updateSnShoppingCarGoodsCounts(@Query(C) String c, @Query("goods_count") String goodsCount, @Query("goods_sku") String goodsSku);

    /**
     * 获取苏宁购物车列表
     *
     * @return
     */
    @GET("suning.php")
    Observable<SnShoppingCarEntity> getSnShoppingCarList(@Query(C) String c);

    /**
     * 删除购物车中的商品
     *
     * @param c
     * @param cartIndex
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> delGoodsFromSnShoppingCar(@Query(C) String c, @Query("cart_index") String cartIndex);

    /**
     * 获取苏宁默认收货地址
     *
     * @param c
     * @return
     */
    @GET("suning.php")
    Observable<SnShippingAddressInfoEntity> getSnShippingAddress(@Query(C) String c);

    /**
     * 获取苏宁商品详情信息
     *
     * @param map
     * @return
     */
    @GET("suning.php")
    Observable<SnGoodsDetailInfoEntity> getSuningGoodsInfo(@QueryMap Map<String, String> map);

    /**
     * 获取苏宁省市县地址
     *
     * @param level
     * @param id
     * @return
     */
    @GET("suning.php")
    Observable<SnSelectorAddressData> selectorSnAddress(@Query(C) String c, @Query("level") String level, @Query("id") String id);

    /**
     * 添加或更新苏宁地址信息
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("suning.php")
    Observable<HttpResultBean> updateSnShippingAddress(@FieldMap Map<String, String> params);

    /**
     * 删除地址
     *
     * @param c
     * @param addressId
     * @return
     */
    @FormUrlEncoded
    @POST("suning.php")
    Observable<HttpResultBean> deleteSnShippingAddress(@Field(C) String c, @Field("address_id") String addressId);

    /**
     * 检查商品在当前地区是否有货
     *
     * @param c
     * @param skuId
     * @param cityId
     * @param countyId
     * @return
     */
    @GET("suning.php")
    Observable<SnCheckStockEntity> checkStock(@Query(C) String c, @Query("skuId") String skuId, @Query("cityId") String cityId,
                                              @Query("countyId") String countyId);

    /**
     * 苏宁获取运费
     *
     * @param c
     * @param skuIdAndNum   格式 skuId:num
     * @param detailAddress
     * @param cityId
     * @param countyId
     * @return
     */
    @GET("suning.php")
    Observable<SnFreightEntity> getSnFreight(@Query(C) String c, @Query("skuId") String skuIdAndNum, @Query("addrDetail") String detailAddress,
                                             @Query("cityId") String cityId, @Query("countyId") String countyId);

    /**
     * 苏宁加入购物车
     *
     * @param c
     * @param goodsCount
     * @param skuId
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> putInShoppingCart(@Query(C) String c, @Query("goods_count") String goodsCount, @Query("goods_sku") String skuId);

    @GET("suning.php")
    Observable<SnShoppingCartCountEntity> getSnShoppingCartCount(@Query(C) String c);

    /**
     * 苏宁政企购物车添加(多个商品)
     *
     * @param c
     * @param goodsCounts
     * @param goodsSkus
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> putInShoppingCartMore(@Query(C) String c, @Query("goods_counts") String goodsCounts, @Query("goods_skus") String goodsSkus);


    /**
     * 苏宁政企删除订单
     *
     * @param c
     * @param snOrderId 苏宁订单号
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> snDeleteOrder(@Query(C) String c, @Query("sn_order_id") String snOrderId);

    /**
     * 苏宁政企取消订单
     *
     * @param c
     * @param snOrderId 苏宁订单号
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> snCancleOrder(@Query(C) String c, @Query("sn_order_id") String snOrderId);

    /**
     * 苏宁政企确认收货接口
     *
     * @param c
     * @param snOrderId 苏宁订单号
     * @param skuIds    厂送苏宁商品id 多个逗号分隔 自营商品不需要确认收货
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> snConfirmGoods(@Query(C) String c, @Query("sn_order_id") String snOrderId, @Query("skuIds") String skuIds);


    /**
     * 获取京东订单列表
     *
     * @param c
     * @param type  0全部 1待付款 2待收货 3已完成 4已取消
     * @param page  页数
     * @param count 页面条数
     * @return
     */
    @GET("suning.php")
    Observable<SnOrderListEntity> getSnOrderList(@Query(C) String c, @Query("type") int type, @Query("page") int page, @Query("count") int count);

    /**
     * 苏宁提交订单
     *
     * @param addressId       地址ID
     * @param cartList        购物车ID 逗号分隔 商品详情直接下单传空字符串
     * @param daiGouQuanMoney 代购券金额
     * @param goodsSkuId      商品ID 购物车下单传空字符串
     * @param goodsCount      商品数量 购物车下单传空字符串
     * @return
     */
    @FormUrlEncoded
    @POST("suning.php")
    Observable<SnCommitOrderEntity> snCommitOrder(@Field(C) String c,
                                                  @Field("address_id") String addressId, @Field("cart_list") String cartList,
                                                  @Field("coupon") String daiGouQuanMoney, @Field("skuId") String goodsSkuId,
                                                  @Field("num") String goodsCount);

    /**
     * 苏宁第三方预充值
     *
     * @param fee        充值金额 单位元
     * @param payType    1支付宝 2微信 3微信公共账号 4网银
     * @param orderIndex
     * @param type       商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @return
     */
    @FormUrlEncoded
    @POST("suning.php")
    Observable<JDThirdPayPreRechargeOrderEntity> snPreRecharge(@Query(C) String c, @Field("payment_fee") String fee,
                                                               @Field("pay_type") String payType, @Field("order_index") String orderIndex,
                                                               @Field("type") String type, @Field("moneys") String merchantIncomePayMoney);

    /**
     * 获取订单详情
     *
     * @param c
     * @param snOrderId 京东订单号
     * @return
     */
    @GET("suning.php")
    Observable<SnOrderDetailEntity> getSnOrderetails(@Query(C) String c, @Query("sn_order_id") String snOrderId);

    /**
     * 检查商品库存
     *
     * @param c
     * @param skuIds
     * @param cityId
     * @return
     */
    @GET("suning.php")
    Observable<SnCheckGoodsStock> checkGoodsStock(@Query(C) String c, @Query("skuId") String skuIds, @Query("cityId") String cityId);

    /**
     * 苏宁 申请结算益豆
     *
     * @param c
     * @param orderIndex 苏宁订单号
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> snApplyOrderSettle(@Query(C) String c, @Query("order_index") String orderIndex);

    /**
     * 苏宁政企查询物流信息
     *
     * @param c
     * @param orderId     苏宁订单号
     * @param orderItemId 苏宁订单行号
     * @param skuId       商品id
     * @return
     */
    @GET("suning.php")
    Observable<SnOrderLogisticsEntity> getSnOrderLogistics(@Query(C) String c, @Query("orderId") String orderId, @Query("orderItemId") String orderItemId, @Query("skuId") String skuId);

    /**
     * 获取苏宁申请售后列表
     *
     * @param c
     * @param page   页数
     * @param count  页面条数
     * @param search 搜索内容
     * @return
     */
    @GET("suning.php")
    Observable<SnAfterSaleApplyForListEntity> getSnAfterSaleApplyForList(@Query(C) String c, @Query("page") int page, @Query("count") int count, @Query("search") String search);

    /**
     * 获取苏宁售后申请记录
     *
     * @param c
     * @param page   页数
     * @param count  页面条数
     * @param search 搜索内容
     * @return
     */
    @GET("suning.php")
    Observable<SnAfterSaleApplicationRecordListEntity> getSnAfterSaleApplicationRecordList(@Query(C) String c,
                                                                                           @Query("page") int page,
                                                                                           @Query("count") int count,
                                                                                           @Query("search") String search);

    /**
     * 苏宁售后记录详情
     *
     * @param c
     * @param id 售后服务单号
     * @return
     */
    @GET("suning.php")
    Observable<SnAfterSaleDetialEntity> getSnServiceDetial(@Query(C) String c, @Query("id") String id);


    /**
     * 获取分享内容
     *
     * @param c
     * @param shareType {@link com.yilian.mylibrary.activity.ShareType}
     * @param data      type=>2 时 data = goods_id,filiale_id ;其他type只传id
     * @return
     */
    @GET("mall.php")
    Observable<ShareEntity> getShareContent(@Query(C) String c, @Query("type") int shareType, @Nullable @Query("data") String data);

    /**
     * 苏宁申请售后
     *
     * @param c
     * @param skus        skuId
     * @param num         数量
     * @param sn_order_id 订单ID
     * @return
     */
    @GET("suning.php")
    Observable<HttpResultBean> setSnAftersaleAction(@Query(C) String c, @Query("skus") String skus, @Query("num") String num, @Query("sn_order_id") String sn_order_id);


    /**
     * JD 使用奖励支付
     *
     * @param orderIndex
     * @param payPwd
     * @param merchantIncomePayMoney 营收额支付金额 单位分
     * @return
     */
    @FormUrlEncoded
    @POST("suning.php")
    Observable<JDCashPayResult> snPay(@Query(C) String c,
                                      @Field("order_index") String orderIndex,
                                      @Field("pay_pwd") String payPwd,
                                      @Field("moneys") String merchantIncomePayMoney);

    /**
     * 获取JD支付结果
     *
     * @param orderIndex 益联短ID
     * @param type       商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @return
     */
    @GET("payment.php")
    Observable<SnPayInfoEntity> getSnPayInfo(@Query(C) String c, @Query("order_index") String orderIndex, @ThirdPayForType.PayType @Query("type") String type);

    /**
     * 获取商家收券二维码
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<MerchantExchangeQrCodeEntity> getMerchantExchangeQrCode(@Query(C) String c);

    /**
     * 获取商家信息
     *
     * @param c
     * @param merchantId
     * @return
     */
    @GET("mall.php")
    Observable<MerchantExchangeEntity> getMerchantInfo(@Query(C) String c, @Query("mer_user_id") String merchantId);

    /**
     * 向商家支付兑换券
     *
     * @param c
     * @param merchantId
     * @param exchange
     * @param pwd
     * @return
     */
    @GET("mall.php")
    Observable<PayToMerchantExchangeEntity> payExchangeToMerchant(@Query(C) String c, @Query("mer_user_id") String merchantId,
                                                                  @Query("quan") BigInteger exchange, @Query("pay_pwd") String pwd);

    /**
     * 兑换券兑换商品支付时使用支付密码支付
     *
     * @param orderId  订单id字符串，多个订单逗号隔开，例如：11，12，13
     * @param password
     * @param exchange 使用营收款金额，没有或者没有使用，传0，单位：分
     * @return
     */
    @FormUrlEncoded
    @POST("mall.php")
    Observable<ExchangePayByPasswordSuccessEntity> exchangePayByPassword(@Field(C) String c,
                                                                         @Field("order_index") String orderId,
                                                                         @Field("pay_pwd") String password,
                                                                         @Field("moneys") String exchange);

    /**
     * 转赠
     *
     * @param c
     * @param bcPhone  受赠手机
     * @param amount   转赠金额
     * @param platform 移动端传0
     * @param pwd      密码
     * @param mark     备注
     * @return
     */
    @GET("mall.php")
    Observable<TransferIntegralEntity> transfer(@Query(C) String c, @Query("be_phone") String bcPhone,
                                                @Query("amount") String amount, @Query("platform") String platform,
                                                @Query("pwd") String pwd, @Query("remark") String mark);

    /**
     * 携程 首页控制
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripAddressTransformEntity> getAddressTransform(@Query(C) String c, @Query("provincename") String provincename, @Query("cityname") String cityname);

    /**
     * 携程 首页控制
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripHomeTop> getCtripHomeIndex(@Query(C) String c);

    /**
     * 携程 猜你喜欢
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripHotelListEntity> getMaybeLike(@Query(C) String c, @Query("cityid") String cityId, @Query("countryid") String countryId, @Query("gdLat") String gdLat, @Query("gdLng") String gdLng, @Query("page") int page, @Query("count") int count);

    /**
     * 携程 城市列表
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripSiteCityEntity> getCtripCity(@Query(C) String c);

    /**
     * 携程 城市列表 搜索
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripSiteCityByDistrictsEntity> getCtripCityBySearch(@Query(C) String c, @Query("keyWord") String keyWord);


    /**
     * 携程 区县列表
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripSiteCityByDistrictsEntity> getCtripCityByDistricts(@Query(C) String c, @Query("cityid") String cityId);

    /**
     * 携程 搜索界面品牌/行政区域/商圈接口
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripKeywordEntity> getSearchInfo(@Query(C) String c, @Query("cityid") String cityId);

    /**
     * 携程 搜索界面品牌/行政区域/商圈接口
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripKeywordBySearchEntity> getKeywordBySearch(@Query(C) String c, @Query("cityid") String cityId, @Query("keyword") String keyword);

    /**
     * 获取携程酒店详情
     *
     * @param c
     * @param hotelId   酒店ID
     * @param startDate 入住时间 格式：2018-08-12
     * @param endDate   离开时间 格式：2018-08-14
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripHotelDetailEntity> getCtripHotelDetail(@Query(C) String c,
                                                           @Query("HotelID") String hotelId,
                                                           @Query("Start") String startDate,
                                                           @Query("End") String endDate);

    /**
     * 获取携程房间筛选条件
     *
     * @param c
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripHotelFilterEntity> getCtripHotelFilter(@Query(C) String c);

    /**
     *获取酒店详情筛选数据
     *
     * @param c
     * @param HotelID
     * @param Start  入住时间
     * @param End 离开时间
     * @param RoomBedInfos 床id
     * @param NumberOfBreakfast 早餐默认0不选择 -1含早餐 1单餐 2双餐
     * @param WirelessBroadnet 是否无线 默认0不选择 2免费无线
     * @param WiredBroadnet 是否有线 默认0不选择 2免费有线
     * @param minPrice 最小价格
     * @param maxPrice 最大价格
     * @param IsInstantConfirm 是否立即确认 默认0 选择 传1
     * @param CancelPolicyInfos 免费取消 默认-1 选择 传0
     * @return
     */
    @GET("xiecheng.php")
    Observable<Hotel_Rooms_FiltrateBean> getHotel_Rooms_FiltrateData(@Query(C) String c,
                                                                     @Query("HotelID") String HotelID,
                                                                     @Query("Start") String Start,
                                                                     @Query("End") String End,
                                                                     @Query("RoomBedInfos") String RoomBedInfos,
                                                                     @Query("NumberOfBreakfast") String NumberOfBreakfast,
                                                                     @Query("WirelessBroadnet") String WirelessBroadnet,
                                                                     @Query("WiredBroadnet") String WiredBroadnet,
                                                                     @Query("minPrice") String minPrice,
                                                                     @Query("maxPrice") String maxPrice,
                                                                     @Query("IsInstantConfirm")String IsInstantConfirm,
                                                                     @Query("CancelPolicyInfos")String CancelPolicyInfos);

    //    搜索筛选条件
    @GET("xiecheng.php")
    Observable<SearchFilterBean> getSarchCriteria(@Query(C) String c, @Query("cityid") String cityid);

    //    酒店筛选列表
    @GET("xiecheng.php")
    Observable<CtripHotelListEntity> getHotelList(@Query(C) String c, @Query("cityid") String cityid,
                                                  @Query("comsort") String comsort,
                                                  @Query("distance") String distance,
                                                  @Query("zoneid") String zoneid,
                                                  @Query("locationid") String locationid,
                                                  @Query("minprice") String minprice,
                                                  @Query("maxprice") String maxprice,
                                                  @Query("starlevel") String starlevel,
                                                  @Query("themeid") String themeid,
                                                  @Query("brandid") String brandid,
                                                  @Query("facilityid") String facilityid,
                                                  @Query("bedid") String bedid,
                                                  @Query("grade") String grade,
                                                  @Query("keyword") String keyword,
                                                  @Query("gdLat") String gdLat,
                                                  @Query("gdLng") String gdLng,
                                                  @Query("page") int page,
                                                  @Query("count") String count);

    /**
     * 检查房间是否可以预定
     *
     * @param c
     * @param hotelId
     * @param roomId
     * @param startDate        入住时间
     * @param endDate          离开时间
     * @param peopleCount      人数
     * @param bookRoomCount    房间数
     * @param laterArrivalTime 最晚到店时间
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripCheckBookable> getRoomBookableStatus(@Query(C) String c, @Query("HotelCode") String hotelId,
                                                         @Query("RoomID") String roomId,
                                                         @Query("Start") String startDate, @Query("End") String endDate,
                                                         @Query("Count") String peopleCount, @Query("Quantity") String bookRoomCount,
                                                         @Query("LateArrivalTime") String laterArrivalTime);

    /**
     * 预定房间
     *
     * @param c
     * @param roomCount       预定房间数
     * @param roomId
     * @param hotelId
     * @param enterNames      入住人姓名 多个姓名英文逗号分隔
     * @param contactName     联系人姓名
     * @param contactPhone    联系人电话
     * @param lateArrivalTime 最晚抵店时间
     * @param arrivalTime     最早抵店时间
     * @param startDate       入住时间 例如：2018-01-08
     * @param endDate         离店时间 例如：2018-01-09
     * @param peopleCount     总共入住人数
     * @param exclusiveAmount 税前总价
     * @param inclusiveAmount 税后总价
     * @param IsHourlyRoom 0普通 1钟点房
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripBookRoomOrderEntity> commitBookRoomOrder(@Query(C) String c, @Query("NumberOfUnits") int roomCount,
                                                             @Query("RoomID") String roomId, @Query("HotelCode") String hotelId,
                                                             @Query("Name") String enterNames, @Query("ContactName") String contactName,
                                                             @Query("PhoneNumber") String contactPhone, @Query("LateArrivalTime") String lateArrivalTime,
                                                             @Query("ArrivalTime") String arrivalTime, @Query("Start") String startDate,
                                                             @Query("End") String endDate, @Query("Count") int peopleCount,
                                                             @Query("ExclusiveAmount") String exclusiveAmount,
                                                             @Query("InclusiveAmount") String inclusiveAmount,
                                                             @Query("IsHourlyRoom") int IsHourlyRoom);

    /**
     * 携程订单列表
     *
     * @param c
     * @param type  类型 Uncommitted-未提交; Process-确认中; Confirm-已确认; Cancel-已取消; Success-已消费 0-全部
     * @param page  页数
     * @param count 页面条数
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripOrderListEntity> getCtripOrderList(@Query(C) String c, @Query("type") String type, @Query("page") int page, @Query("count") int count);

    /**
     * 获取订单详情
     *
     * @param c
     * @param ResID_Value 携程订单号
     * @return
     */
    @GET("xiecheng.php")
    Observable<CtripOrderDetailEntity> getCtripOrderDetails(@Query(C) String c, @Query("ResID_Value") String ResID_Value);

    /**
     * 携程删除订单
     *
     * @param c
     * @param ResID_Value 携程订单号
     * @return
     */
    @GET("xiecheng.php")
    Observable<HttpResultBean> deleteCtripOrder(@Query(C) String c, @Query("ResID_Value") String ResID_Value);

    /**
     * 携程取消订单
     *
     * @param c
     * @param ResID_Value 携程订单号
     * @param reason 取消原因
     * @return
     */
    @GET("xiecheng.php")
    Observable<HttpResultBean> cancelCtripOrder(@Query(C) String c, @Query("ResID_Value") String ResID_Value,@Query("reason") String reason);

    /**
     * 携程 第三方预充值
     *
     * @param fee        充值金额 单位元
     * @param payType    1支付宝 2微信 3微信公共账号 4网银
     * @param orderIndex
     * @param type       商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @return
     */
    @FormUrlEncoded
    @POST("xiecheng.php")
    Observable<JDThirdPayPreRechargeOrderEntity> ctripPreRecharge(@Query(C) String c, @Field("payment_fee") String fee,
                                                               @Field("pay_type") String payType, @Field("order_index") String orderIndex,
                                                               @Field("type") String type, @Field("moneys") String merchantIncomePayMoney);

    /**
     * 携程 使用奖励支付
     *
     * @param orderIndex
     * @param payPwd
     * @param merchantIncomePayMoney 营收额支付金额 单位分
     * @return
     */
    @FormUrlEncoded
    @POST("xiecheng.php")
    Observable<JDCashPayResult> ctripPay(@Query(C) String c,
                                      @Field("order_index") String orderIndex,
                                      @Field("pay_pwd") String payPwd,
                                      @Field("moneys") String merchantIncomePayMoney);

    /**
     * 获取 携程 支付结果
     *
     * @param orderIndex 益联短ID
     * @param type       商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11   携程支付 14
     * @return
     */
    @GET("payment.php")
    Observable<CtripPayInfoEntity> getCtripPayInfo(@Query(C) String c, @Query("order_index") String orderIndex, @ThirdPayForType.PayType @Query("type") String type);

    /**
     * 携程 删除订单
     *
     * @param c
     * @param resID_Value 苏宁订单号
     * @return
     */
    @GET("xiecheng.php")
    Observable<HttpResultBean> ctripDeleteOrder(@Query(C) String c, @Query("ResID_Value") String resID_Value);
    /**
     * 获取购物卡信息
     *
     * @param c
     * @param cardNumber 卡号
     * @return
     */
    @GET("mall.php")
    Observable<MyCardInfoEntity> getCardInfo(@Query(C) String c, @Query("hpl_shop_card") String cardNumber);


    /**
     * 购物卡保存收货地址
     *
     * @param c
     * @param address 选择的地址格式：**省id,市id,县id ** id没有传 0 例如： 11,159,0
     * @return
     */
    @GET("mall.php")
    Observable<HttpResultBean> saveAddress(@Query(C) String c, @Query("address") String address);

    /**
     * 购物卡变更明细列表
     *
     * @param c
     * @param date 没有日期筛选就写0
     * @return
     */
    @GET("mall.php")
    Observable<CardChangeDetailEntity> getCardChangeDetail(@Query(C) String c, @Query("page") int page, @Query("count") int count, @Query("date") String date, @Query("type") String type);


    /**
     * 购物卡类型筛选
     *
     * @param c
     * @param type 购物卡类型筛选获取的type=2 不可更改 获取成功后客户端将接口返回到 type值 做参数
     * @return
     */
    @GET("mall.php")
    Observable<CardTypeFiltrateEntity> getCardTypeFiltrate(@Query(C) String c, @Query("type") String type);


    /**
     * 购物卡 首页列表头部
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<CardHomeHeaderEntity> getCardHomePageHeader(@Query(C) String c);

    /**
     * 购物卡 好货推荐
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<JDGoodsListEntity> getCardHomePageListData(@Query(C) String c, @Query("page") int page, @Query("count") int count);

    /**
     * 购物卡  京东清单 数量
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<JdShoppingCarGoodsCountsEntity> getCardJdShoppingCarGoodsCounts(@Query(C) String c);


    /**
     * 购物卡 京东添加商品到购物清单
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<HttpResultBean> addCardJdGoodsToShoppingCar(@Query(C) String c, @Query("goods_count") String goodsCount, @Query("goods_sku") String goodsSku);


    /**
     * 购物卡  京东政企三级分类商品列表
     *
     * @param c
     * @param page
     * @param count
     * @param catId   三级分类id
     * @param sort    默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy asc升序 desc降序
     * @param state   是否选择有货 0否 1是
     * @return
     */
    @GET("mall.php")
    Observable<JdBrandGoodsListEntity> getCardJdThirdClassifyGoodsList(@Query(C) String c, @Query("page") int page, @Query("count") int count,
                                                                       @Query("catId") String catId,
                                                                       @Query("sort") String sort,
                                                                       @Query("order_by") String orderBy,
                                                                       @Query("state") String state);

    /**
     * 购物卡 京东 品牌商品列表
     *
     * @param c
     * @param page
     * @param count
     * @param brand   品牌名
     * @param sort    默认id 价格jdPrice 益豆return_bean 销量sale
     * @param orderBy asc升序 desc降序
     * @return
     */
    @GET("mall.php")
    Observable<JdBrandGoodsListEntity> getCardJdBrandGoodsList(@Query(C) String c, @Query("page") int page, @Query("count") int count,
                                                               @Query("brandName") String brand,
                                                               @Query("sort") String sort,
                                                               @Query("order_by") String orderBy,
                                                               @Query("state") String state);

    /**
     * 购物卡  获取京东品牌精选列表
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<JdGoodsBrandSelectedEntity> getCardJdBrandList(@Query(C) String c, @Query("page") int page, @Query("count") int count);


    /**
     * 购物卡 购物清单 提交JD订单
     *
     * @param c
     * @param cartList  购物车列表 1,2多个以逗号分隔
     * @param addressId 用户地址id
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDCommitOrderSuccessEntity> commitCardJDOrderShoppingList(@Query(C) String c, @Field("cart_list") String cartList,
                                                                     @Field("user_address_id") String addressId, @Field("is_quan") String useDaiGouQuan);


    /**
     * 购物卡 JD第三方预充值
     *
     * @param fee        充值金额 单位元
     * @param payType    1支付宝 2微信 3微信公共账号 4网银
     * @param orderIndex
     * @param type       商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11     京东购物卡订单 15
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDThirdPayPreRechargeOrderEntity> cardJdPreRecharge(@Query(C) String c, @Field("payment_fee") String fee,
                                                               @Field("pay_type") String payType, @Field("order_index") String orderIndex,
                                                               @Field("type") String type, @Field("card_amount") String cardmount);

    /**
     * 购物卡  JD 使用购物卡支付
     *
     * @param orderIndex
     * @param payPwd
     * @return
     */
    @FormUrlEncoded
    @POST("jd.php")
    Observable<JDCashPayResult> cardJdPay(@Query(C) String c,
                                      @Field("order_index") String orderIndex,
                                      @Field("pay_pwd") String payPwd);

    /**
     * 我的购物卡 余额
     *
     * @param c
     * @return
     */
    @GET("jd.php")
    Observable<MyCardEntity> getCardBalance(@Query(C) String c);

    /**
     * 购物卡  判断当前登陆用户是否关联购物卡
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<CheckShopCardRelEntity> checkShopCardRel(@Query(C) String c);
    /**
     * 获取京东省/市/区县/镇地址列表
     *
     * @param c
     * @param level 层级 1省级2市级3县级
     * @param regionId 上一级的id 省级传0
     * @return
     */
    @GET("mall.php")
    Observable<JDAreaStringData> getShppingCardAreaStringData(@Query(C) String c,
                                                              @Query("level") String level,
                                                              @Query("region_id") String regionId);

}
