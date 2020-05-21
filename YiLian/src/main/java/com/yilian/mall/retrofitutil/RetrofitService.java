package com.yilian.mall.retrofitutil;/**
 * Created by  on 2017/3/3 0003.
 */

import android.support.annotation.Nullable;

import com.yilian.mall.entity.AliCustomerServiceInfo;
import com.yilian.mall.entity.AliLoginUserInfo;
import com.yilian.mall.entity.BankInfoModel;
import com.yilian.mall.entity.JBankModel;
import com.yilian.mall.entity.CashSuccessModel;
import com.yilian.mall.entity.FlashSaleEntity;
import com.yilian.mall.entity.FlashSalePayResult;
import com.yilian.mall.entity.GetUserInfoEntity;
import com.yilian.mall.entity.JRegionModel;
import com.yilian.mall.entity.LeFenMTHomePageEntity;
import com.yilian.mall.entity.MallFlashGoodsEntity;
import com.yilian.mall.entity.MallOrderListEntity;
import com.yilian.mall.entity.NoticeModel;
import com.yilian.mall.entity.RegisterAccount;
import com.yilian.mall.entity.ShopsEntity;
import com.yilian.mall.entity.SpellGroupListEntity;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.MallFlashSalePayOrderEntity;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by  on 2017/3/3 0003.
 */
public interface RetrofitService {
    /**
     * 登录阿里百川客服系统
     *
     * @param deviceIndex
     * @param token
     * @return
     */
    @GET("mall.php")
    Call<AliLoginUserInfo> getAliAccountInfo(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token);

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
    Call<RegisterAccount> login(@Query("c") String c, @Query("device_index") String deviceIndex,
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
    Call<GetUserInfoEntity> getUserInfo(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token);

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
    Call<AliCustomerServiceInfo> getAliCustomerServiceInfo
    (@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token,
     @Query("type") String type, @Nullable @Query("num") String num);

    /**
     * 获取仿美团首页数据
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param cityId      用户定位城市ID，必须是具体的城市ID
     * @param countyId    用户定位区县id
     * @return
     */
    @POST("mall.php")
    Call<LeFenMTHomePageEntity> getLeFenMTHomePageData(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token,
                                                       @Query("city") String cityId, @Query("county") String countyId, @Query("app_version") String appVersion);

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
     * @param latitude    纬度
     * @param longitude   经度
     * @return
     */
    @POST("mall.php")
    Call<ShopsEntity> getNearByShops(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token,
                                     @Query("page") String page, @Query("count") String count, @Query("industry_top") String industryTop, @Query("industry_son") String industrySon,
                                     @Query("city") String cityId, @Query("county") String countyId, @Query("lat") String latitude, @Query("lng") String longitude);

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
     * @param type        1首页列表  0 内部列表
     * @return
     */
    @POST("mall.php")
    Call<FlashSaleEntity> getFlashSaleList(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token
            , @Query("city") String cityId, @Query("county") String countyId, @Query("round") String round, @Query("page") String page, @Query("count") String count, @Query("type") String type);

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
    Call<ShopsEntity> searchNearByShops(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("city") String city,
                                        @Query("county") String county, @Query("keyword") String keyword, @Query("lat") String lat, @Query("lng") String lng);

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
    Call<FlashSalePayResult> payForFlashSale(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token,
                                             @Query("goods_id") String goodsId, @Query("pay_pwd") String payPwd);

    /**
     * 总部限时抢购详情
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param goodsId
     * @return
     */
    @POST("mall.php")
    Call<MallFlashGoodsEntity> getMallDetailFlashSale(@Query("c") String c, @Query("device_index") String deviceIndex,
                                                      @Query("token") String token, @Query("goods_id") String goodsId);

    /**
     * 总部限时购下单
     *
     * @param c
     * @param deviceIndex
     * @param token
     * @param goods_id
     * @param express_price
     * @param address_id
     * @param order_remark
     * @return
     */
    @POST("mall.php")
    Call<MallFlashSalePayOrderEntity> flashSalePayOrder(@Query("c") String c, @Query("device_index") String deviceIndex, @Query("token") String token, @Query("goods_id") String goods_id,
                                                        @Query("express_price") String express_price, @Query("address_id") String address_id, @Query("order_remark") String order_remark);

    /**
     * 总部限时购支付
     *
     * @param c
     * @param device_index
     * @param token
     * @param order_id
     * @param pay_pwd
     * @return
     */
    @POST("mall.php")
    Call<MallFlashSalePayOrderEntity> flashSaleLimitPay(@Query("c") String c, @Query("device_index") String device_index, @Query("token") String token,
                                                        @Query("order_id") String order_id, @Query("pay_pwd") String pay_pwd);

    /**
     * 总部限时购商品检测库存
     *
     * @param c
     * @param device_index
     * @param token
     * @param order_id
     * @return
     */
    @POST("mall.php")
    Call<BaseEntity> flashSaleCheckInventory(@Query("c") String c, @Query("device_index") String device_index, @Query("token") String token, @Query("order_id") String order_id);

    /**
     * 获取拼团列表的banner图
     *
     * @param c
     * @param device_index
     * @param token
     * @return
     */
    @POST("mall.php")
    Call<SpellGroupListEntity> getSpellGroupListContent(@Query("c") String c, @Query("device_index") String device_index, @Query("token") String token,
                                                        @Query("page") String page, @Query("count") String count);


    /**
     * 上传图片
     *
     * @param image
     * @return
     */
    @Multipart
    @POST("mall.php")
    Call<UploadImageEnity> uploadFile(@Query("c") String c, @Query("token") String token, @Query("device_index") String deviceIndex, @Part MultipartBody.Part image);



    /**
     * 领奖励的信息
     */
    @POST("account.php")
    Call<BankInfoModel> getBinkInfo(@Query("c") String c, @Query("token") String token, @Query("device_index") String deviceIndex);


    /**
     * 绑定银行卡
     *
     * @return
     */
    @POST("account.php")
    Call<BaseEntity> bindCard(@Query("c") String c, @Query("token") String token, @Query("device_index") String deviceIndex, @Query("card_bank") String bankName, @Query("card_holder") String userName,
                              @Query("card_number") String cardNum, @Query("card_branch") String cardBranch, @Query("province_id") String provinceId, @Query("city_id") String cityId,
                              @Query("county_id") String countyId, @Query("phone") String phone, @Query("sms_code") String code);

    //获取银行列表
    @POST("account.php")
    Call<JBankModel> getBankList(@Query("c") String c);

    //获取地区列表
    @POST("account.php")
    Call<JRegionModel> getRegion(@Query("c") String c);

    //获取班定银行卡时的短信验证码
    @POST("account.php")
    Call<BaseEntity> getSmsCode(@Query("c") String c, @Query("token") String token, @Query("device_index") String device_index, @Query("phone") String phone, @Query("verify_type") String verify_type,
                                @Query("verify_code") String verify_code, @Query("voice") String voice, @Query("type") String type);

    //领奖励
    @POST("account.php")
    Call<CashSuccessModel> takeCash(@Query("c") String c, @Query("token") String token, @Query("device_index") String device_index, @Query("cash_amount") String cash_amount,
                                    @Query("card_index") String card_index);

    //快报列表
    @POST("mall.php")
    Call<NoticeModel> getNoticeList(@Query("c") String c, @Query("page") String page, @Query("count") String count);

    @POST("mall.php")
    Call<MallOrderListEntity> getMallOrderList(@Query("c") String c, @Query("device_index") String device_index, @Query("token") String token,
                                               @Query("order_type") String orderType, @Query("page") String page);


}