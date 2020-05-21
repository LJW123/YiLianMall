package com.yilian.mall.retrofitutil;/**
 * Created by  on 2017/3/3 0003.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.AliCustomerServiceInfo;
import com.yilian.mall.entity.BankInfoModel;
import com.yilian.mall.entity.CashSuccessModel;
import com.yilian.mall.entity.FlashSaleEntity;
import com.yilian.mall.entity.FlashSalePayResult;
import com.yilian.mall.entity.GetUserInfoEntity;
import com.yilian.mall.entity.JBankModel;
import com.yilian.mall.entity.JRegionModel;
import com.yilian.mall.entity.MallFlashGoodsEntity;
import com.yilian.mall.entity.MallOrderListEntity;
import com.yilian.mall.entity.NoticeModel;
import com.yilian.mall.entity.RegisterAccount;
import com.yilian.mall.entity.ShopsEntity;
import com.yilian.mall.entity.SpellGroupListEntity;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.networkingmodule.BuildConfig;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.MallFlashSalePayOrderEntity;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by  on 2017/3/3 0003.
 */
public class RetrofitUtils {

    private static RetrofitUtils retrofitUtils;
    private Context context;
    private static Retrofit retrofit;
    private static RetrofitService retrofitService;
    private SharedPreferences sp;
    private String deviceIndex;
    private String token;

    private RetrofitUtils() {

    }

    public static RetrofitUtils getInstance(Context context) {
        if (retrofitUtils == null || retrofitService == null) {
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
//                httpClient.connectTimeout(5, TimeUnit.SECONDS);
// add your other interceptors …
// add logging as last interceptor
                    httpClient.addInterceptor(logging);  // <-- this is the important line!
                    retrofitUtils = new RetrofitUtils();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Ip.getURL(context))
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                }
                if (retrofitService == null) {
                    retrofitService = retrofit.create(RetrofitService.class);
                }
            }
        }
        return retrofitUtils;
    }

    public RetrofitUtils setContext(Context context) throws Exception {
        if (context == null) {
            throw new Exception("RetrofitUtils mContext is null");
        }
        this.context = context;
        sp = context.getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
        deviceIndex = RequestOftenKey.getDeviceIndex(context);
        token = RequestOftenKey.getToken(context);
        return retrofitUtils;
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
//            Logger.i("获取百川信息使用的token："+token+"   RequestOftenToken:"+ com.yilian.mylibrary.RequestOftenKey.getToken(context));
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
//                            MyApplication.getInstance().setYWIMKit(mIMKit);
//                            Logger.i("不空");
//                        } else {
//                            Logger.i("空啊空");
//                        }
//                        /**
//                         * 登录阿里
//                         */
//                        IYWLoginService loginService = null;
//                        try {
//                            loginService = MyApplication.getInstance().getYWIMKit().getLoginService();
//                        } catch (Exception e) {
//                            Logger.i("ali loginByPassWord " + e.getMessage());
//                            e.printStackTrace();
//                        }
//                        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
//                        loginService.login(loginParam, new IWxCallback() {
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
//            MyApplication.getInstance().getYWIMKit().getLoginService().logout(new IWxCallback() {
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
     * 账号密码登录
     */
    public void login(String account, String pwd, Callback<RegisterAccount> callback) {
        Call<RegisterAccount> login_by_password = retrofitService.login("login_by_password", deviceIndex, account, pwd);
        login_by_password.enqueue(callback);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(Callback<GetUserInfoEntity> callback) {
        Call<GetUserInfoEntity> get_userInfo = retrofitService.getUserInfo("get_userInfo", deviceIndex, token);
        get_userInfo.enqueue(callback);
    }

    /**
     * 获取客服信息
     *
     * @param type 1商品详情，2商城订单详情，3商家套餐订单详情，4商家详情，5旗舰店详情
     * @param num  type=1时商品id和兑换中心id用'+'分割如（1314+25）或者（1312+0）,其余type时不用拼接，订单id为短的
     * @return
     */
    public Call<AliCustomerServiceInfo> getAliCustomerServiceInfo(String type, String num) {
        Call<AliCustomerServiceInfo> aliCustomerServiceInfo = retrofitService.getAliCustomerServiceInfo("chat/service_type", deviceIndex, token, type, num);
        Logger.i("获取阿里客服信息参数：type:" + type + "  num" + num + "  deviceIndex:" + deviceIndex + "  token:" + token);
        return aliCustomerServiceInfo;
    }


    /**
     * 获取附近商家列表 ,兑换中心列表,借口调用更换了c的值返回值多增加一个是否配送
     *
     * @param page        页码，默认0
     * @param industryTop 联盟商家行业顶级分类，默认0表示全部分类   兑换中心为 0
     * @param industrySon /联盟商家行业二级分类，默认0   兑换中心为 0
     * @param callback
     */
    public void getNearByShops(int page, int count, String industryTop, String industrySon, String cityId, String countyId, String latitude, String longitude, Callback<ShopsEntity> callback) {
        retrofitService.getNearByShops("package/merchant_list_v2", deviceIndex, token,
                String.valueOf(page), String.valueOf(count), industryTop, industrySon, cityId, countyId, latitude, longitude).enqueue(callback);
    }

    /**
     * 获取限时抢购商品信息列表
     *
     * @param cityId   用户定位城市ID，必须是具体的城市ID
     * @param countyId 用户定位区县id
     * @param round    0进行时场次 1上一个抢购场次 2下一场
     * @param page
     * @param count
     * @return
     */
    public void getFlashSaleList(String cityId, String countyId, String round, String page, String count, String type, Callback<FlashSaleEntity> callback) {
        retrofitService.getFlashSaleList("activity/flash_sale", deviceIndex, token, cityId, countyId, round, page, count, type).enqueue(callback);
    }

    /**
     * 搜索附近商家
     *最多返回10个
     * @param cityId   用户定位城市ID，必须是具体的城市ID
     * @param countyId 用户定位区县id,默认0
     * @param keyword  关键字
     * @param callback
     */
    public void searchNearByShops(String cityId, String countyId, String keyword, String lat, String lng, Callback<ShopsEntity> callback) {
        retrofitService.searchNearByShops("search", deviceIndex, cityId, countyId, keyword, lat, lng).enqueue(callback);
    }

    /**
     * 支付限时抢购商品
     *
     * @param goodsId 抢购商品id
     * @param payPwd  支付密码
     * @return
     */
    public void payForFlashSale(String goodsId, String payPwd, Callback<FlashSalePayResult> callback) {
        retrofitService.payForFlashSale("activity/pay_order", deviceIndex, token, goodsId, payPwd).enqueue(callback);
    }

    /**
     * 获取总部限时购详情信息
     *
     * @param goodsId
     * @param callback
     */
    public void getMallDetailFlashSale(String goodsId, Callback<MallFlashGoodsEntity> callback) {
        retrofitService.getMallDetailFlashSale("activity/mall_flash_goods", deviceIndex, token, goodsId).enqueue(callback);
    }

    /**
     * 总部限时购下单
     *
     * @param goods_id
     * @param express_price
     * @param address_id
     * @param order_remark
     */
    public void flashSalePayOrder(String goods_id, String express_price, String address_id, String order_remark, Callback<MallFlashSalePayOrderEntity> callback) {
        retrofitService.flashSalePayOrder("activity/mall_pay_order", deviceIndex, token, goods_id, express_price, address_id, order_remark).enqueue(callback);
    }

    /**
     * 总部限时抢购支付
     *
     * @param order_id
     * @param pay_pwd
     * @param callback
     */
    public void flashSaleLiminPay(String order_id, String pay_pwd, Callback<MallFlashSalePayOrderEntity> callback) {
        retrofitService.flashSaleLimitPay("activity/mall_limit_pay", deviceIndex, token, order_id, pay_pwd).enqueue(callback);
    }

    /**
     * 总部限时抢购检测库存
     *
     * @param order_id
     * @param callback
     */
    public void flashSaleCheckInvoentory(String order_id, Callback<BaseEntity> callback) {
        retrofitService.flashSaleCheckInventory("activity/check_inventory", deviceIndex, token, order_id).enqueue(callback);
    }

    /**
     * 获取拼团列表的内容
     *
     * @param callback
     */
    public void getSpellGroupListContent(String page, String count, Callback<SpellGroupListEntity> callback) {
        Logger.i("PAGE  " + page + " " + callback.toString());
        retrofitService.getSpellGroupListContent("groups/groupsList", deviceIndex, token, page, count).enqueue(callback);
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

    public void getBinkInfo(Callback<BankInfoModel> callback) {
        retrofitService.getBinkInfo("get_bank_info", token, deviceIndex).enqueue(callback);
    }

    public void bindCard(String bankName, String userName, String cardNum, String cardBranch, String provinceId, String cityId, String countyId,
                         String phone, String code, Callback<BaseEntity> callback) {
        retrofitService.bindCard("add_bank_card", token, deviceIndex, bankName, userName, cardNum, cardBranch, provinceId, cityId, countyId, phone, code).enqueue(callback);
    }

    public void getBankList(Callback<JBankModel> callback) {
        retrofitService.getBankList("get_bank_list").enqueue(callback);
    }

    public void getRegion(Callback<JRegionModel> callback) {
        retrofitService.getRegion("getRegion").enqueue(callback);
    }

    public void getSmsCode(String phone, String verify_type, String verify_code, String voice, String type, Callback<BaseEntity> callback) {
        retrofitService.getSmsCode("get_SMS_code_v2", token, deviceIndex, phone, verify_type, verify_code, voice, type).enqueue(callback);
    }


    //2018年1月27日 接口升级为cashToBalance_v2
    public void takeCash(String cash_amount, String card_index, Callback<CashSuccessModel> callback) {
        retrofitService.takeCash("cashToBalance_v2", token, deviceIndex, cash_amount, card_index).enqueue(callback);
    }

    public void getNoticeList(String page, String count, Callback<NoticeModel> callback) {
        retrofitService.getNoticeList("new_list", page, count).enqueue(callback);
    }

    /**
     * 获取订单列表  订单类型 0 全部订单 1代付款 2已付款 3已完成 4待评价
     *
     * @param orderType
     * @param page
     * @param callback
     */
    public void getMallOrderList(String orderType, String page, Callback<MallOrderListEntity> callback) {
        retrofitService.getMallOrderList("order/mallorder_list_v2", deviceIndex, token, orderType, page).enqueue(callback);
    }

}
