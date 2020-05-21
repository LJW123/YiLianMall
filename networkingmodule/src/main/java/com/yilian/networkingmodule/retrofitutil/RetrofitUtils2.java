package com.yilian.networkingmodule.retrofitutil;/**
 * Created by  on 2017/3/3 0003.
 */


import android.content.Context;

import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.NetworkUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.Utils;
import com.yilian.networkingmodule.BuildConfig;
import com.yilian.networkingmodule.entity.ActEvaDetailEntity;
import com.yilian.networkingmodule.entity.ActGoodsEvaListEntity;
import com.yilian.networkingmodule.entity.ActInventIntegralChangeListEntity;
import com.yilian.networkingmodule.entity.ActInventMyRcordEntity;
import com.yilian.networkingmodule.entity.ActInventSubMemberListEntity;
import com.yilian.networkingmodule.entity.ActMyRecordEntity;
import com.yilian.networkingmodule.entity.ActRuleEntity;
import com.yilian.networkingmodule.entity.AfterSaleBtnClickResultEntity;
import com.yilian.networkingmodule.entity.BarCodeSearchEntity;
import com.yilian.networkingmodule.entity.BranchListEntity;
import com.yilian.networkingmodule.entity.CalculateEntity;
import com.yilian.networkingmodule.entity.ChartsEntity;
import com.yilian.networkingmodule.entity.ClassifyListV2Entity;
import com.yilian.networkingmodule.entity.ExpressInfoEntity;
import com.yilian.networkingmodule.entity.ExpressListEntity;
import com.yilian.networkingmodule.entity.GALLuckyPrizeEntity;
import com.yilian.networkingmodule.entity.GALMakeOrderEntity;
import com.yilian.networkingmodule.entity.GALRecordEntity;
import com.yilian.networkingmodule.entity.GALZanEntity;
import com.yilian.networkingmodule.entity.GuessInfoEntity;
import com.yilian.networkingmodule.entity.GuessListEntity;
import com.yilian.networkingmodule.entity.LuckyInfoEntity;
import com.yilian.networkingmodule.entity.LuckyNumLogListEntity;
import com.yilian.networkingmodule.entity.LuckyPayEntity;
import com.yilian.networkingmodule.entity.LuckyRecordListEntity;
import com.yilian.networkingmodule.entity.LuckyShowListInfoEntity;
import com.yilian.networkingmodule.entity.LuckySnathNewsList;
import com.yilian.networkingmodule.entity.MerchantAuthInfoEntity;
import com.yilian.networkingmodule.entity.MerchantCartListEntity;
import com.yilian.networkingmodule.entity.MerchantMakeOrderEntity;
import com.yilian.networkingmodule.entity.MerchantManagerDetailEntity;
import com.yilian.networkingmodule.entity.MerchantManagerListEntity;
import com.yilian.networkingmodule.entity.MerchantOrderEntity;
import com.yilian.networkingmodule.entity.MerchantOrderListEntity;
import com.yilian.networkingmodule.entity.MerchantShopQrCodeEntity;
import com.yilian.networkingmodule.entity.MineIconsEntity;
import com.yilian.networkingmodule.entity.MineMallOrderEntity;
import com.yilian.networkingmodule.entity.OfflineTransferCardInfoDatailEntity;
import com.yilian.networkingmodule.entity.OfflineTransferCardInfoEntity;
import com.yilian.networkingmodule.entity.OrderNewListEntity;
import com.yilian.networkingmodule.entity.ParadiseEntity;
import com.yilian.networkingmodule.entity.PerformanceEntity;
import com.yilian.networkingmodule.entity.RedPacketDateEntity;
import com.yilian.networkingmodule.entity.RedPacketFragmentHeadEntity;
import com.yilian.networkingmodule.entity.RedPacketIndexEntity;
import com.yilian.networkingmodule.entity.RedPacketOneKeyRemoveEntity;
import com.yilian.networkingmodule.entity.RedPacketOpenOneEntity;
import com.yilian.networkingmodule.entity.RedPacketWhetherEntity;
import com.yilian.networkingmodule.entity.RemoveRedRecordEntity;
import com.yilian.networkingmodule.entity.ScOrderListEntity;
import com.yilian.networkingmodule.entity.SnatchAwardDetailsEntity;
import com.yilian.networkingmodule.entity.SnatchAwardListEntity;
import com.yilian.networkingmodule.entity.SnatchShowListEntity;
import com.yilian.networkingmodule.entity.SupplierDetailEntity;
import com.yilian.networkingmodule.entity.SupplierListEntity;
import com.yilian.networkingmodule.entity.SystemNewListEntity;
import com.yilian.networkingmodule.entity.TransferIntegralEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by  on 2017/3/3 0003.
 */
public class RetrofitUtils2 {

    private static volatile RetrofitUtils2 retrofitUtils;
    private static RetrofitService retrofitService;
    private String deviceIndex;
    private String token;

    private RetrofitUtils2() {

    }

    public static RetrofitUtils2 getInstance(final Context context) {
        if (retrofitUtils == null) {
            synchronized (RetrofitUtils2.class) {
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
                    httpClient.addInterceptor(logging);  // <-- this is the important line!
                    httpClient.addInterceptor(logging)  // <-- this is the important line!
//            网络不可用拦截器
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Utils.init(context);
                                    boolean connected = NetworkUtils.isConnected();
                                    if (connected) {
                                        return chain.proceed(chain.request());
                                    } else {
                                        throw new NoNetworkException();
                                    }
                                }
                            })
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
                    retrofitUtils = new RetrofitUtils2();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Ip.getURL(context))
                            .addConverterFactory(MyGsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    retrofitService = retrofit.create(RetrofitService.class);
                }
            }
        }

        return retrofitUtils;
    }


    public RetrofitUtils2 setDeviceIndex(String deviceIndex) {
        this.deviceIndex = deviceIndex;
        return retrofitUtils;
    }

    public RetrofitUtils2 setToken(String token) {
        this.token = token;
        return retrofitUtils;
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
     * 奖券转赠
     *
     * @param bcPhone
     * @param amount
     * @param platform
     * @param
     */
    public void getTransferIntegrals(String bcPhone, String amount, String platform, String pwd, String mark, Callback<TransferIntegralEntity> callback) {
        retrofitService.getTransferIntegrals("transfer_integral", token, deviceIndex, bcPhone, amount, platform, pwd, mark).enqueue(callback);
    }

    /**
     * 益豆转赠
     * @param bcPhone
     * @param amount
     * @param platform
     * @param pwd
     * @param mark
     * @param callback
     */
    public void getTransferCalculatedStress(String bcPhone, String amount, String platform, String pwd, String mark, Callback<TransferIntegralEntity> callback) {
        retrofitService.getTransferCalculatedStress("transfer_bean", token, deviceIndex, bcPhone, amount, platform, pwd, mark).enqueue(callback);
    }



    /**
     * 获取商家认证资料 处理回显图片
     *
     * @param callback
     */
    public void getMerchantAuthInfo(Callback<MerchantAuthInfoEntity> callback) {
        retrofitService.getMerchantAuthInfo("merchantAuthInfo", deviceIndex, token).enqueue(callback);
    }

    /**
     * 我的业绩
     */
    public void getAchievementInfo(String userId, Callback<PerformanceEntity> callback) {
        retrofitService.getAchievementInfo("achievementInfo", userId).enqueue(callback);
    }

    /**
     * 奖券购、易划算分类列表
     */
    public void getClassifyListV2(String useType, Callback<ClassifyListV2Entity> callback) {
        retrofitService.getClassifyListV2("classify_list_v2", "0", useType).enqueue(callback);
    }

    /**
     * 获取排行榜数据
     */
    public void getChartsEntity(Callback<ChartsEntity> callback) {
        retrofitService.getChartsEntity("appindex_charts", token, deviceIndex).enqueue(callback);
    }

    /**
     * 条形码模糊搜索列表
     *
     * @param goodsCode
     * @param callback
     */
    public void getBarCodeSearchEntity(String goodsCode, Callback<BarCodeSearchEntity> callback) {
        retrofitService.getBarCodeSearchEntity("sc_search_goods_list", deviceIndex, token, goodsCode).enqueue(callback);
    }

    /**
     * 商家售卖商品 购物车列表
     */
    public void getMerchantCartList(Callback<MerchantCartListEntity> callback) {
        retrofitService.getMerchantCartList("sc_cart_list", deviceIndex, token).enqueue(callback);
    }

    /**
     * 更新购物车数量
     */
    public void modifyMerchantCartList(String cartId, String count, Callback<HttpResultBean> callback) {
        retrofitService.modifyMerchantCartList("sc_modify_cart", deviceIndex, token, cartId, count).enqueue(callback);
    }

    /**
     * 删除购物车商品
     */
    public void delCartGoods(String cartId, Callback<HttpResultBean> callback) {
        retrofitService.delMerchantGoods("sc_del_cart", deviceIndex, token, cartId).enqueue(callback);
    }

    /**
     * merchant下订单
     */
    public void merchantMakeOrder(String cardIdList, String phone, String smsCodeStr, Callback<MerchantMakeOrderEntity> callback) {
        retrofitService.merchantMakeOrder("sc_make_order", deviceIndex, token, cardIdList, phone, smsCodeStr).enqueue(callback);
    }

    /**
     * 二维码付款 二维码生成
     */
    public void merchantQrCode(String orderIndex, Callback<MerchantShopQrCodeEntity> callback) {
        retrofitService.merchantQrCode("sc_qr_code_img", deviceIndex, token, orderIndex).enqueue(callback);
    }

    /**
     * 商超零售现金付款
     */
    public void merchantCashPay(String pwd, String orderIndex, Callback<HttpResultBean> callback) {
        retrofitService.merchantCashPay("sc_retail_cash_pay", deviceIndex, token, pwd, orderIndex).enqueue(callback);
    }

    /**
     * 模糊查询数据添加购物车
     *
     * @param goodsCode
     * @param count
     * @param callback
     */
    public void scGoodsAddCart(String goodsCode, String count, Callback<HttpResultBean> callback) {
        retrofitService.scGoodsAddCart("sc_goods_add_cart", deviceIndex, token, goodsCode, count).enqueue(callback);
    }

    /**
     * 获取商超的订单列表
     *
     * @param page
     * @param callback
     */
    public void getScOrderData(String page, Callback<ScOrderListEntity> callback) {
        retrofitService.getScOrderData("sc_order_list", deviceIndex, token, page, "30").enqueue(callback);
    }

    /**
     * 清空购物车
     */
    public void clearMerchantShopList(Callback<HttpResultBean> callback) {
        retrofitService.clearMerchantShopList("sc_clear_cart", deviceIndex, token).enqueue(callback);
    }

    /**
     * 获取系统消息列表
     *
     * @param page
     * @param callback
     */
    public void getSystemNewList(String page, Callback<SystemNewListEntity> callback) {
        retrofitService.getSystemNewList("system_new_list", deviceIndex, token, page, "30").enqueue(callback);
    }

    /**
     * 获取消息列表
     *
     * @param page
     * @param callback
     */
    public void getOrderNewList(String page, Callback<OrderNewListEntity> callback) {
        retrofitService.getOrderNewList("order_new_list", deviceIndex, token, page, "30").enqueue(callback);
    }

    /**
     * 线下转账记录
     */
    public void transferList(String page, String count, Callback<OfflineTransferCardInfoEntity> callback) {
        retrofitService.transferList("transfer_list", deviceIndex, token, page, count).enqueue(callback);
    }

    /**
     * 线下转账
     */
    public void userTransfer(String amount, String remark, String cardName, String cardNum, String cardBank, String cardVoucher, Callback<HttpResultBean> callback) {
        retrofitService.userTransfer("user_transfer", deviceIndex, token, amount, remark, cardName, cardNum, cardBank, cardVoucher).enqueue(callback);
    }

    /**
     * 银行卡信息
     */
    public void cardInfo1(String type, String transId, Callback<OfflineTransferCardInfoEntity> callback) {
        retrofitService.cardInfo1("card_info", deviceIndex, token, type, transId).enqueue(callback);
    }

    public void cardInfo2(String type, String transId, Callback<OfflineTransferCardInfoDatailEntity> callback) {
        retrofitService.cardInfo2("card_info", deviceIndex, token, type, transId).enqueue(callback);
    }

    /**
     * 获取商家中心订单列表
     *
     * @param orderType
     * @param page
     * @param callback
     */
    public void getMerchantCenterOrderList(int orderType, int page, Callback<MerchantOrderListEntity> callback) {
        retrofitService.getOrderList("supplier_mallorder_list", deviceIndex, token, String.valueOf(page), String.valueOf(orderType), "30").enqueue(callback);
    }

    /**
     * 备货
     *
     * @param orderId
     * @param callback
     */
    public void orderExport(String orderId, Callback<HttpResultBean> callback) {
        retrofitService.orderExport("order_export", deviceIndex, token, orderId).enqueue(callback);
    }

    /**
     * 订单详情
     *
     * @param orderIndex
     * @param callback
     */
    public void getMallOrderData(String orderIndex, Callback<MerchantOrderEntity> callback) {
        retrofitService.getMallOrderData("supplier_mallorder_info", deviceIndex, token, orderIndex).enqueue(callback);
    }

    /**
     * 取消备货
     *
     * @param orderId
     * @param callback
     */
    public void cancelOrderExprot(String orderId, Callback<HttpResultBean> callback) {
        retrofitService.cancelOrderExport("cancel_order_exports", deviceIndex, token, orderId).enqueue(callback);
    }

    /**
     * 发货
     *
     * @param orderIndex      短的订单的唯一标识
     * @param orderGoodsIndex 订单下商品的自增id 对应列表中goods_list 中的order_goods_index（如果合并发货多个商品id用，号分割）
     * @param expressCompany  快递公司的名称 yunda
     * @param expressNumber   快递单号 123456789
     * @param callback
     */
    public void merchantOrderSend(String orderIndex, String orderGoodsIndex, String expressCompany,
                                  String expressNumber, Callback<HttpResultBean> callback) {
        retrofitService.orderSend2("order_sends", token, deviceIndex, orderIndex, orderGoodsIndex, expressCompany, expressNumber).enqueue(callback);
    }

    /**
     * 获取物流信息
     *
     * @param callback
     */
    public void getExpressList(Callback<ExpressListEntity> callback) {
        retrofitService.getExpressList("express_list_v1").enqueue(callback);
    }

//    /**
//     * 修改物流信息
//     * @param parcelIndex
//     * @param expressCompany
//     * @param expressNumber
//     * @param callback
//     */
//    public void updateParcelInfo(String parcelIndex,String expressCompany,String expressNumber,Callback<HttpResultBean>callback){
//        retrofitService.updateParcelInfo("update_parcel_info",deviceIndex,token,parcelIndex,expressCompany,expressNumber).enqueue(callback);
//    }

    /**
     * 获取售后列表界面
     *
     * @param page
     * @param type
     * @param callback
     */
    public void getSupplierList(String page, String type, Callback<SupplierListEntity> callback) {
        retrofitService.getSupplierList("supplier_service_list", deviceIndex, token, page, String.valueOf(Constants.PAGE_COUNT), type).enqueue(callback);
    }

    /**
     * 获取售后详情数据
     *
     * @param serviceIndex
     * @param type
     * @param callback
     */
    public void getSupplierDetailsData(String serviceIndex, String type, Callback<SupplierDetailEntity> callback) {
        retrofitService.getSupplierDetailData("supplier_service_info", deviceIndex, token, serviceIndex, type).enqueue(callback);
    }

    /**
     * 售后待审核订单的审核/拒绝
     *
     * @param checkIndex
     * @param status
     * @param reason
     * @param serviceType
     * @param callback
     */
    public void supplierServiceCheck(String checkIndex, String status, String reason, String serviceType, Callback<AfterSaleBtnClickResultEntity> callback) {
        retrofitService.supplierRefuse("supplier_service_check", deviceIndex, token, checkIndex, status, reason, serviceType).enqueue(callback);
    }

    /**
     * 商品管理-商品列表
     */
    public void getMerchantManagerList(String page, String count, String keyWord, Callback<MerchantManagerListEntity> callback) {
        retrofitService.getMerchantManagerList("goods_downuplist", token, deviceIndex, page, count, keyWord).enqueue(callback);
    }

    /**
     * 商品管理-商品上下架专区
     */
    public void getGoodsDownUp(String goodsId, String statusStr, Callback<HttpResultBean> callback) {
        retrofitService.getGoodsDownUp("get_goods_downup", token, deviceIndex, goodsId, statusStr).enqueue(callback);
    }

    /**
     * 商品管理-商品详情
     */
    public void goodsDownUpInfo(String goodsId, Callback<MerchantManagerDetailEntity> callback) {
        retrofitService.goodsDownUpInfo("goods_downup_info", token, deviceIndex, goodsId).enqueue(callback);
    }


    /**
     * 换货 返修订单中确认收货功能
     *
     * @param barterId
     * @param callback
     */
    public void barterExpressReceive(String barterId, Callback<AfterSaleBtnClickResultEntity> callback) {
        retrofitService.barterExpressReceive("supplier_barter_express_receive", deviceIndex, token, barterId).enqueue(callback);
    }

    /**
     * 确认修复 备货功能
     *
     * @param barterId
     * @param callback
     */
    public void barterStock(String barterId, Callback<AfterSaleBtnClickResultEntity> callback) {
        retrofitService.barterStock("supplier_barter_stock", token, deviceIndex, barterId).enqueue(callback);
    }

    /**
     * 售后的发货功能
     *
     * @param barterId
     * @param expressId
     * @param expressNum
     * @param callback
     */
    public void barterExpress(String barterId, String expressId, String expressNum, Callback<AfterSaleBtnClickResultEntity> callback) {
        retrofitService.barterExpress("supplier_barter_express", deviceIndex, token, barterId, expressId, expressNum).enqueue(callback);
    }

    /**
     * 退货的确认收货
     *
     * @param refundId
     * @param callback
     */
    public void refundExpressReceive(String refundId, Callback<AfterSaleBtnClickResultEntity> callback) {
        retrofitService.refundExpressReceive("supplier_refund_express_receive", deviceIndex, token, refundId).enqueue(callback);
    }

    /**
     * 退货中的退款功能
     *
     * @param refundIndex
     * @param callback
     */
    public void refundMoneyBack(String refundIndex, Callback<HttpResultBean> callback) {
        retrofitService.refundMoneyBack("supplier_refund_money_back", deviceIndex, token, refundIndex).enqueue(callback);
    }

    /**
     * 大家猜活动列表
     */
    public void guessList(String status, String page, String count, Callback<GuessListEntity> callback) {
        retrofitService.guessList("guess_list", deviceIndex, token, status, page, count).enqueue(callback);
    }

    /**
     * 大家猜我参与的
     */
    public void guessListSelf(String page, String count, Callback<GuessListEntity> callback) {
        retrofitService.guessListSelf("guess_list_self", deviceIndex, token, page, count).enqueue(callback);
    }

    /**
     * 大家猜结果
     */
    public void guessInfoSelf(String id, String page, String count, Callback<GuessInfoEntity> callback) {
        retrofitService.guessInfo("guess_has_prize", deviceIndex, token, id, page, count).enqueue(callback);
    }

    /**
     * 大家猜-我猜过的结果
     */
    public void guessHasPrize(String id, String page, String count, Callback<GuessInfoEntity> callback) {
        retrofitService.guessHasPrize("guess_info_self", deviceIndex, token, id, page, count).enqueue(callback);
    }

    /**
     * 获取支行列表
     */
    public void getBranchBankList(String provinceId, String cityId, String bankId, String searchStr, Callback<BranchListEntity> callback) {
        retrofitService.getBranchBankList("get_branch_bank_list", provinceId, cityId, bankId, searchStr).enqueue(callback);
    }

    /**
     * 获取游戏活动列表
     */
    public void getActiviesLists(Callback<ParadiseEntity> callback) {
        retrofitService.getActiviesLists("getActivesLists", deviceIndex, token).enqueue(callback);
    }

    /**
     * 中奖记录
     */
    public void getSnatchAwardList(int page, String userId, Callback<SnatchAwardListEntity> callback) {
        retrofitService.getSnatchAwardList("snatch/snatch_award_log", deviceIndex, token,
                userId, String.valueOf(page), String.valueOf(Constants.PAGE_COUNT)).enqueue(callback);
    }

    /**
     * 获取晒单列表
     */
    /**
     * @param type        0 某个活动所有记录 1我的夺宝晒单记录 2所有的晒单纪录
     * @param page
     * @param snatchIndex 0 所有记录 ，其他情况本活动的相关晒单记录
     * @param callback
     */
    public void getSnatchShowList(String type, int page, String snatchIndex, Callback<SnatchShowListEntity> callback) {
        retrofitService.snatchShowList("snatch/snatch_show_list", deviceIndex, token, type, snatchIndex,
                String.valueOf(page), String.valueOf(Constants.PAGE_COUNT)).enqueue(callback);
    }

    /**
     * 晒单提交
     */
    public void snatchShowCommit(String activityId, String content, String img, Callback<HttpResultBean> callback) {
        retrofitService.snatchShow("snatch/snatch_show", deviceIndex, token, activityId, content, img).enqueue(callback);
    }

    /**
     * 中奖详情
     */
    public void snatchAwardDetails(String activityId, Callback<SnatchAwardDetailsEntity> callback) {
        retrofitService.snatchAwardDetails("snatch/snatch_award_info", deviceIndex, token, activityId).enqueue(callback);
    }

    /**
     * 参与记录
     */
    public void snatchJoinList(String type, String userId, int page, Callback<SnatchAwardListEntity> callback) {
        retrofitService.snatchJoinList("snatch/snatch_join_log", deviceIndex, token, type, userId, String.valueOf(page),
                String.valueOf(Constants.PAGE_COUNT)).enqueue(callback);
    }

    /**
     * 设置收货地址
     */
    public void snatchSetAddress(String activityId, String addressId, Callback<HttpResultBean> callback) {
        retrofitService.snatchSetAddress("snatch/snatch_set_address", deviceIndex, token, activityId, addressId).enqueue(callback);
    }

    /**
     * 时间详情
     */
    public void snatchCalculate(String snatchIndex, Callback<CalculateEntity> callback) {
        retrofitService.snatchCalculate("snatch/snatch_calculate", deviceIndex, token, snatchIndex).enqueue(callback);
    }

    /**
     * 确认收货
     */
    public void snatchConfirmGoods(String activityId, Callback<HttpResultBean> callback) {
        retrofitService.snatchConfirmGoods("snatch/snatch_confirm_goods", deviceIndex, token, activityId).enqueue(callback);
    }

    /**
     * 幸运购-活动详情
     *
     * @param id
     * @param page
     * @param count
     * @param type     0正常获取详情 ， 值为1 表示查询最新一期活动详情
     * @param callback
     */
    public void luckyInfo(String id, String page, String count, String type, String lat, String lng, Callback<LuckyInfoEntity> callback) {
        retrofitService.luckyInfo("snatch/snatch_info_v2", deviceIndex, token, id, page, count, type, lat, lng).enqueue(callback);
    }

    /**
     * 幸运购-参加活动
     */
    public void luckyPayOrder(String id, String count, String lat, String lng, Callback<LuckyPayEntity> callback) {
        retrofitService.luckyPayOrder("snatch/snatch_order", deviceIndex, token, id, count, lat, lng).enqueue(callback);
    }

    /**
     * 幸运购-往期回顾列表
     */
    public void luckyRecordList(String id, String page, String count, Callback<LuckyRecordListEntity> callback) {
        retrofitService.luckyRecordList("snatch/snatch_list_record", deviceIndex, token, id, page, count).enqueue(callback);
    }

    /**
     * 幸运购-夺宝记录
     */
    public void luckyNumLogList(String activityId, String userId, Callback<LuckyNumLogListEntity> callback) {
        retrofitService.luckyNumLogList("snatch/snatch_num_log", deviceIndex, token, activityId, userId).enqueue(callback);
    }

    /**
     * 幸运购-夺宝记录-带page count
     */
    public void luckyNumLogListWithPageAndCount(String activityId, String userId, String page, String count, Callback<LuckyNumLogListEntity> callback) {
        retrofitService.luckyNumLogListWithPageAndCount("snatch/snatch_num_log", deviceIndex, token, activityId, userId, page, count).enqueue(callback);
    }

    /**
     * 幸运购-夺宝记录-带page count type
     */
    public void luckyNumLogListWithPageAndCountAndType(String activityId, String userId, String page, String count, String type, Callback<LuckyNumLogListEntity> callback) {
        retrofitService.luckyNumLogListWithPageAndCountAndType("snatch/snatch_num_log", deviceIndex, token, activityId, userId, page, count, type).enqueue(callback);
    }

    /**
     * 幸运购-晒单详情
     */
    public void luckyShowListInfo(String id, Callback<LuckyShowListInfoEntity> callback) {
        retrofitService.luckyShowListInfo("snatch/snatch_show_list_info", deviceIndex, token, id).enqueue(callback);
    }

    /**
     * 碰运气操作
     */
    public void galLuckyPrize(String id, Callback<GALLuckyPrizeEntity> callback) {
        retrofitService.galLuckyPrize("guessPriceAndLucky/luckyPrize", deviceIndex, token, id).enqueue(callback);
    }

    /**
     * 碰运气下订单
     */
    public void galMakerOrder(String orderId, String sku, String addressId, String remark, Callback<GALMakeOrderEntity> callback) {
        retrofitService.galMakerOrder("guessPriceAndLucky/make_order", orderId, sku, addressId, remark).enqueue(callback);
    }

    /**
     * 猜价格操作
     */
    public void galGuessPrize(String id, String type, String price, Callback<GALLuckyPrizeEntity> callback) {
        retrofitService.galGuessPrize("guessPriceAndLucky/guessPrize", id, type, price).enqueue(callback);
    }

    /**
     * 猜价格-碰运气历史战绩
     */
    public void galRecordList(String goodsId, String type, String page, String count, Callback<GALRecordEntity> callback) {
        retrofitService.galRecordList("guessPriceAndLucky/recordList", goodsId, type, page, count).enqueue(callback);
    }

    /**
     * 猜价格-碰运气点赞操作
     */
    public void setPraise(String id, String type, String praiseType, Callback<GALZanEntity> callback) {
        retrofitService.setPraise("guessPriceAndLucky/setPraise", id, type, praiseType).enqueue(callback);
    }

    /**
     * 猜价格-碰运气我的战绩
     */
    public void galMyRecordList(String type, String page, String count, Callback<ActMyRecordEntity> callback) {
        retrofitService.galMyRecordList("guessPriceAndLucky/selRecordList", type, page, count).enqueue(callback);
    }

    /**
     * 猜价格-碰运气评论详情
     */
    public void galEvaDetail(String id, String page, String count, Callback<ActEvaDetailEntity> callback) {
        retrofitService.galEvaDetail("guessPriceAndLucky/commentInfo", id, page, count).enqueue(callback);
    }

    /**
     * 猜价格-碰运气回复评论
     */
    public void galReplyComment(String id, String content, Callback<HttpResultBean> callback) {
        retrofitService.galReplyComment("guessPriceAndLucky/replyComment", id, content).enqueue(callback);
    }

    /**
     * 猜价格-碰运气确认收货
     */
    public void galConfirmOrder(String id, Callback<HttpResultBean> callback) {
        retrofitService.galConfirmOrder("guessPriceAndLucky/confirmOrder", id).enqueue(callback);
    }

    /**
     * 猜价格-碰运气添加评论
     */
    public void galSetComment(String index, String anonymity, String score, String content, String images, Callback<HttpResultBean> callback) {
        retrofitService.galSetComment("guessPriceAndLucky/setComment", index, anonymity, score, content, images).enqueue(callback);
    }

    /**
     * 猜价格-碰运气商品详情下方-评论列表
     */
    public void galCommentList(String page, String goodsId, Callback<ActGoodsEvaListEntity> callback) {
        retrofitService.galCommentList("guessPriceAndLucky/commentList", page, goodsId).enqueue(callback);
    }

    /**
     * 猜价格-碰运气规则
     */
    public void galRule(String actType, Callback<ActRuleEntity> callback) {
        retrofitService.galRule("guessPriceAndLucky/actRule", actType).enqueue(callback);
    }

    /**
     * 打卡-邀请好友-我的战绩-战绩
     */
    public void actInventMyRecord(Callback<ActInventMyRcordEntity> callback) {
        retrofitService.actInventMyRecord("myRecord").enqueue(callback);
    }

    /**
     * 打卡-邀请好友-我的战绩-排行榜
     */
    public void actInventIntegralChangeList(String page, String count, Callback<ActInventIntegralChangeListEntity> callback) {
        retrofitService.actInventIntegralChangeList("integralChangeList", page, count).enqueue(callback);
    }

    /**
     * 打卡-邀请好友-我的战绩-好友列表
     */
    public void actGetSubMember(String page, String count, Callback<ActInventSubMemberListEntity> callback) {
        retrofitService.actGetSubMember("getSubMember", page, count).enqueue(callback);
    }

    /**
     * 幸运购-首页中奖快报更多列表
     */
    public void luckySnatchPrizeList(String page, String count, Callback<LuckySnathNewsList> callback) {
        retrofitService.luckySnatchPrizeList("get_snatch_prize_list", page, count).enqueue(callback);
    }

    /**
     * 幸运购-首页发货快报更多列表
     */
    public void luckySnatchSendList(String page, String count, Callback<LuckySnathNewsList> callback) {
        retrofitService.luckySnatchSendList("get_snatch_prize_delivery", page, count).enqueue(callback);
    }

    /**
     * 拆奖励记录
     */
    public void removeRedPacketReocrds(String page, String count, Callback<RemoveRedRecordEntity> callback) {
        retrofitService.removeRedPacketReocrds("remove_red_packet_records", page, count).enqueue(callback);
    }

    /**
     * 消费指数
     */
    public void redPacketIndex(Callback<RedPacketIndexEntity> callback) {
        retrofitService.redPacketIndex("red_packet_index_number").enqueue(callback);
    }

    /**
     * app首页是否弹奖励窗
     */
    public void whetherShowRedDialog(Callback<RedPacketWhetherEntity> callback) {
        retrofitService.whetherShowRedDialog("snatch/snatch_alerts").enqueue(callback);
    }

    /**
     * app首页领奖励
     */
    public void openOneRedPacket(String id, Callback<RedPacketOpenOneEntity> callback) {
        retrofitService.openOneRedPacket("snatch/snatch_open_one", id).enqueue(callback);
    }

    /**
     * 奖励-每个月的领取记录
     */
    public void getRedDate(String year, String month, Callback<RedPacketDateEntity> callback) {
        retrofitService.getRedDate("red_packet_monthly_records", year, month).enqueue(callback);
    }

    /**
     * 奖励Fragment头部数据
     */
    public void getRedHead(Callback<RedPacketFragmentHeadEntity> callback) {
        retrofitService.getRedHead("red_packet_index").enqueue(callback);
    }

    /**
     * 单个奖励激活
     */
    public void activityRedPacket(String id, Callback<HttpResultBean> callback) {
        retrofitService.activityRedPacket("activate_red_packet", id).enqueue(callback);
    }

    /**
     * 一键拆奖励
     */
    public void oneKeyRemoveRedPacket(String type, Callback<RedPacketOneKeyRemoveEntity> callback) {
        retrofitService.oneKeyRemoveRedPacket("onekey_remove_red_packet", type).enqueue(callback);
    }

    /**
     * app首页弹窗是否弹过上报
     */
    public void openOneRedPop(String id, Callback<HttpResultBean> callback) {
        retrofitService.openOneRedPop("snatch/snatch_alert_pop", id).enqueue(callback);
    }

    /**
     * 个人中心所有icons的图片和文字信息
     * 这里ios需要传version字段 android不用  2017-12-15需求
     * 2018年1月27日版本从personalCenter升级为personal_center
     */
    public void mineIconsMessage(Callback<MineIconsEntity> callback) {
        retrofitService.mineIconsMessage("personal_center").enqueue(callback);
    }

    /**
     * 2018-2-5 版个人中心
     * val 大于5 游戏中心 否则 益联之家
     */
    public void newMineIconsMessage(String val, Callback<MineIconsEntity> callback) {
        retrofitService.mineIconsMessage("account/personalCenter").enqueue(callback);
    }


    /**
     * 个人中心-线上商城角标
     */
    public void mineCornerMessage(String userId, Callback<MineMallOrderEntity> callback) {
        retrofitService.mineCornerMessage("SyncOrderInfo", userId).enqueue(callback);
    }

    /**
     * 获取快递信息
     */
    public void getExpressInfo(String expressNum, Callback<ExpressInfoEntity> callback) {
        retrofitService.getExpressInfo("snatch/getExpressInfo", expressNum).enqueue(callback);
    }

}
