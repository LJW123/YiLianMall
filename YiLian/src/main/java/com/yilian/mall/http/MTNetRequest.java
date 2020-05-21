package com.yilian.mall.http;/**
 * Created by  on 2016/12/7 0007.
 */

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.ComboListEntity;
import com.yilian.mall.entity.JPMyFavoriteGoodsEntity;
import com.yilian.mall.entity.JPMyFavoriteMerchantEntity;
import com.yilian.mall.entity.JPMyFavoriteStorEntity;
import com.yilian.mall.entity.MTComboDetailsEntity;
import com.yilian.mall.entity.MTComboSearchEntity;
import com.yilian.mall.entity.MTEntity;
import com.yilian.mall.entity.MTMerchantCommendEntity;
import com.yilian.mall.entity.MTMerchantDetailEntity;
import com.yilian.mall.entity.MTOrderEntity;
import com.yilian.mall.entity.MTOrderListEntity;
import com.yilian.mall.entity.MTPackageCommentListEntity;
import com.yilian.mall.entity.MTPaySuccessEntity;
import com.yilian.mall.entity.MTRefundDetailEntity;
import com.yilian.mall.entity.MTRefundStatusEntity;
import com.yilian.mall.entity.MTSubmitOrderEntity;
import com.yilian.mall.entity.MtMyFavoriteComboEntity;
import com.yilian.mall.entity.Nearby;
import com.yilian.mall.entity.UpLoadIdCardEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

/**
 * Created by  on 2016/12/7 0007.
 */
public class MTNetRequest extends BaseNetRequest {
    public  String URL;

    public MTNetRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "mall.php";
    }

    /**
     * 获取评论内容
     *
     * @param packageId  套餐ID
     * @param merchantId 商户ID
     * @param type       评论类型 0 全部 1 有图片的评论
     * @param page
     * @param callBack
     */
    public void getMTPackageCommentAllList(String packageId, String merchantId, String type, int page, RequestCallBack<MTPackageCommentListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/package_comment_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("package_id", packageId);
        params.addBodyParameter("merchant_id", merchantId);
        params.addBodyParameter("type", type);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "30");
        Logger.i("获取评论内容参数：  package_id:" + packageId + "  merchant_id:" + merchantId + "  type:" + type);
        postRequest(URL, params, MTPackageCommentListEntity.class, callBack);
    }

    /**
     * 获取附近商家列表 ,兑换中心列表,借口调用更换了c的值返回值多增加一个是否配送
     *
     * @param page        页码，默认0
     * @param industryTop 联盟商家行业顶级分类，默认0表示全部分类   兑换中心为 0
     * @param industrySon /联盟商家行业二级分类，默认0   兑换中心为 0
     * @param callBack
     */
    public void getNewNearMerchantList(int page, String industryTop, String industrySon, String cityId,
                                       String countyId, String hasPackage, String lat, String lng,
                                       RequestCallBack<Nearby> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/merchant_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("industry_top", industryTop);
        params.addBodyParameter("industry_son", industrySon);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("county", countyId);
        params.addBodyParameter("city", cityId);
        params.addBodyParameter("count", String.valueOf(Constants.PAGE_COUNT));
        params.addBodyParameter("has_package", hasPackage);
        params.addBodyParameter("lat", lat);
        params.addBodyParameter("lng", lng);
        Logger.i("package/merchant_list-  industryTop：" + industryTop + "  industrySon:" + industrySon + "  countyId:" + countyId + "  cityId:" + cityId + "  hasPackage:" + hasPackage + "  lat" + lat + "  lng:" + lng);
        postRequest(URL, params, Nearby.class, callBack);
    }

    /**
     * 获取套餐列表
     *
     * @param page        页码，默认0
     * @param industryTop 联盟商家行业顶级分类，默认0表示全部分类   兑换中心为 0
     * @param industrySon /联盟商家行业二级分类，默认0   兑换中心为 0
     * @param callBack
     */
    public void getPackageList(int page, String industryTop, String industrySon, String cityId, String countyId, RequestCallBack<ComboListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/package_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("industry_top", industryTop);
        params.addBodyParameter("industry_son", industrySon);
        params.addBodyParameter("page", Integer.toString(page));
        params.addBodyParameter("count", "20");
        params.addBodyParameter("county", countyId);
        params.addBodyParameter("city", cityId);
        postRequest(URL, params, ComboListEntity.class, callBack);
    }

    /**
     * 获取商家详情的数据
     *
     * @param merchant_id
     * @param callBack
     */
    public void getMerchantDetailList(String merchant_id, String lat, String lng, RequestCallBack<MTMerchantDetailEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/merchant_details_v2");
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("merchant_id", merchant_id);
        params.addBodyParameter("lat", lat);
        params.addBodyParameter("lng", lng);
        Logger.i("商家详情的参数" + "token" + RequestOftenKey.getToken(mContext) + " device_index  " + RequestOftenKey.getDeviceIndex(mContext) + " merchant_id" + merchant_id);
        postRequest(URL, params, MTMerchantDetailEntity.class, callBack);
    }

    /**
     * 商家推荐数据
     *
     * @param merchant_id
     * @param callBack
     */
    public void getMerchantRecommendList(String merchant_id, int page, String lat, String lng, RequestCallBack<MTMerchantCommendEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/merchant_recommend");
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("merchant_id", merchant_id);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "30");
        params.addBodyParameter("lat", lat);
        params.addBodyParameter("lng", lng);
        Logger.i("请求附近推荐商家参数：URL"+URL+" DEVICE_INDEX"+RequestOftenKey.getDeviceIndex(mContext)+"  TOKEN  "+RequestOftenKey.getToken(mContext)+"  merchantId:" + merchant_id);
        postRequest(URL, params, MTMerchantCommendEntity.class, callBack);

    }

    /**
     * 套餐详情数据
     *
     * @param package_id
     * @param callBack
     */
    public void getComboDetailsList(String package_id, RequestCallBack<MTComboDetailsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/package_details");
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("package_id", package_id);
//        params.addBodyParameter("package_id","5006");
        Logger.i("请求商品详情参数:  token:" + RequestOftenKey.getToken(mContext) + "  deviceIndex:" + RequestOftenKey.getDeviceIndex(mContext) + "  packageId:" + package_id);
        postRequest(URL, params, MTComboDetailsEntity.class, callBack);

    }


    /**
     * 获取外卖订单列表
     */
    public void getMTOrderList(int type, int page, RequestCallBack<MTOrderListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/order_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", type + "");
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("count", "20");
        Logger.i("获取外卖订单借口参数   URL "+ URL+"  device_index = "+RequestOftenKey.getDeviceIndex(mContext)+"  token  =  "
                +RequestOftenKey.getToken(mContext)+" type  "+type+"  page    "+page+"  count  == 20 ");
        postRequest(URL, params, MTOrderListEntity.class, callBack);
    }

    /**
     * 获取外卖订单详情
     */
    public void getMTOrder(String id, RequestCallBack<MTOrderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/order_details");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", id);
        Logger.i("URL  "+URL+" deviceIndex  "+RequestOftenKey.getDeviceIndex(mContext)+"  token  "+RequestOftenKey.getToken(mContext)+"  orderId   "+id);
        postRequest(URL, params, MTOrderEntity.class, callBack);
    }

    /**
     * 团购转配送
     * @param id
     * @param count
     * @param address_id
     * @param type  0获取配送费 1支付配送费
     * @param pwd
     * @param callBack
     */
    public void switchMT(String id, String count, String address_id, String type, String pwd, RequestCallBack<MTEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/translate_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", id);
        params.addBodyParameter("count", count);
        params.addBodyParameter("address_id", address_id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("pay_pwd", pwd);
        Logger.i("团购转配送参数 device_index:" + RequestOftenKey.getDeviceIndex(mContext) + "   token " + RequestOftenKey.getToken(mContext) +
                "   order_id  " + id + "   count  " + count + "   address_id " + address_id + "  type  " + type + "  pay_pwd" + pwd);
        postRequest(URL, params, MTEntity.class, callBack);
    }

    /**
     * 外卖退款详情
     */
    public void comboRefundDetail(String orderId, RequestCallBack<MTRefundDetailEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/refund_details");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", orderId);
        Logger.i("2016-12-20:" + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("2016-12-20:" + RequestOftenKey.getToken(mContext));

        postRequest(URL, params, MTRefundDetailEntity.class, callBack);
    }

    /**
     * 外卖退款
     */
    public void comboRefund(String orderId, String code, String reasonMsg, String msg, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/refund_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", orderId);
        params.addBodyParameter("code", code);
        params.addBodyParameter("reason_msg", reasonMsg);
        params.addBodyParameter("msg", msg);

        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 外卖退款追踪
     */
    public void comboRefundStatus(String orderId, RequestCallBack<MTRefundStatusEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/refund_status");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", orderId);

        postRequest(URL, params, MTRefundStatusEntity.class, callBack);
    }


    /**
     * 提交订单配送不配送
     */
    public void getSubmitOrderList(String package_id, String address_id, String count, RequestCallBack<MTSubmitOrderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/submit_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("package_id", package_id);
        params.addBodyParameter("address_id", address_id);
        params.addBodyParameter("count", count);
        Logger.i("提交订单请求借口参数  device_index " + RequestOftenKey.getDeviceIndex(mContext) + "Token " + RequestOftenKey.getToken(mContext) +
                "package_id" + package_id + "  address_id  " + address_id + " count " + count);
        postRequest(URL, params, MTSubmitOrderEntity.class, callBack);
    }

    /**
     * 发表套餐评论
     *
     * @param orderId          订单ID
     * @param totalScore       总体评分  10-50 共5个档
     * @param environmentScore 环境评分  10-50 共5个档
     * @param serviceScore     服务评分  10-50 共5个档
     * @param commentContent   评论内容
     * @param imgUrls          晒图的图片的URL地址 多个用逗号分开
     * @param callBack
     */
    public void commitPackageComment(String orderId, String totalScore, String environmentScore, String serviceScore, String commentContent, String imgUrls, RequestCallBack<BaseEntity> callBack) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/create_comment");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", orderId);
        params.addBodyParameter("evaluate", totalScore);
        params.addBodyParameter("environment", environmentScore);
        params.addBodyParameter("service", serviceScore);
        params.addBodyParameter("comment", commentContent);
        params.addBodyParameter("image", imgUrls);

        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 套餐搜索的结果
     *
     * @param city     城市id
     * @param county   用户定位的城市id
     * @param key_word 搜索关键字
     * @param callBack
     */
    public void getComboSerachList(String city, String county, String key_word, RequestCallBack<MTComboSearchEntity> callBack) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/merchant_search_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("city", city);
        params.addBodyParameter("county", county);
        params.addBodyParameter("key_word", key_word);
        Logger.i("查询商家或套餐参数： city:" + city + "  county:" + county + "  key_word:" + key_word);
        postRequest(URL, params, MTComboSearchEntity.class, callBack);
    }


    /**
     * 取消套餐
     */
    public void cancelOrder(String orderId, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/cancel_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", orderId);

        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 套餐支付
     *
     * @param packageOrderId
     * @param payPwd
     * @param callBack
     */
    public void payPackage(String packageOrderId, String payPwd, RequestCallBack<MTPaySuccessEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/pay_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", packageOrderId);
        params.addBodyParameter("pay_pwd", payPwd);
        Logger.i("2017年8月15日 14:47:37-" + URL);
        Logger.i("2017年8月15日 14:47:37-" + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("2017年8月15日 14:47:37-" + RequestOftenKey.getToken(mContext));
        Logger.i("2017年8月15日 14:47:37-" + packageOrderId);
        Logger.i("2017年8月15日 14:47:37-" + payPwd);
        postRequest(URL, params, MTPaySuccessEntity.class, callBack);
    }


    /**
     * V2版本商品收藏列表
     */
    public void getMtFavoriteGoodsList(RequestCallBack<JPMyFavoriteGoodsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/collect_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "0");

        postRequest(URL, params, JPMyFavoriteGoodsEntity.class, callBack);
    }

    /**
     * 请求我的收藏的旗舰店数据
     *
     * @param callBack 0商品 1旗舰店 2商家 3套餐
     */
    public void getMyFavoriteStorList(RequestCallBack<JPMyFavoriteStorEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/collect_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "1");
        postRequest(URL, params, JPMyFavoriteStorEntity.class, callBack);
    }

    /**
     * 请求我的收藏的商家数据
     *
     * @param callBack class_id "0"
     *                 0商品 1旗舰店 2商家 3套餐
     */
    public void getMyFavoriteMerchantList(RequestCallBack<JPMyFavoriteMerchantEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/collect_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "2");
        postRequest(URL, params, JPMyFavoriteMerchantEntity.class, callBack);
    }

    /**
     * 请求我的收藏的套餐数据
     *
     * @param callBack 0商品 1旗舰店 2商家 3套餐
     */
    public void getMyFavoriteComboList(RequestCallBack<MtMyFavoriteComboEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/collect_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "3");
        Logger.i("套餐收藏的接口参数  URL   "+URL+" deviceIndex  "+RequestOftenKey.getDeviceIndex(mContext)+"  token  "+RequestOftenKey.getToken(mContext)+"  type ==  3 ");
        postRequest(URL, params, MtMyFavoriteComboEntity.class, callBack);
    }

    /**
     *  以base64上传全球购身份证
     * @param imageName  图片的名字包括后缀名
     * @param imageSize  图片数据长度（图片的base64的字段值的长度）
     * @param imageData  图片的base64字符串
     * @param callBack
     */
    public void upLoadIDCardImagData(String imageName, int imageSize, String imageData, RequestCallBack<UpLoadIdCardEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "address/upload_image_data");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("image_role", "card");
        params.addBodyParameter("image_name",imageName );
        params.addBodyParameter("image_type", "image/png");
        params.addBodyParameter("image_size", String.valueOf(imageSize));
        params.addBodyParameter("image_data", imageData);
        postRequest(URL, params, UpLoadIdCardEntity.class, callBack);
        Logger.i("加水印 device_index "+RequestOftenKey.getDeviceIndex(mContext));
        Logger.i(" token "+RequestOftenKey.getToken(mContext));
        Logger.i("imageName     "+imageName);
        Logger.i(" imageSize    "+imageSize);
        Logger.i("image_type    "+"image/png");
        //Logger.i(" imageData    "+imageData);
    }

    /**
     *  保存全球购收货人信息
     * @param callBack
     */
    public void saveGolbalContactInfo(String image_front, String iamge_reserve, String idCard, String name, String user_addressid, RequestCallBack<UpLoadIdCardEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "address/global_contact_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("image_front", image_front);
        params.addBodyParameter("image_reserve", iamge_reserve);
        params.addBodyParameter("id_card", idCard);
        params.addBodyParameter("name",name );
        params.addBodyParameter("user_address_id", user_addressid);
        postRequest(URL, params, UpLoadIdCardEntity.class, callBack);
        Logger.i("保存 device_index "+RequestOftenKey.getDeviceIndex(mContext));
        Logger.i(" token "+RequestOftenKey.getToken(mContext));
        Logger.i("image_front  "+image_front);
        Logger.i(" image_reserve  "+image_front);
        Logger.i(" id_card    "+idCard);
        Logger.i("name  "+name);
        Logger.i("user_address_id  "+user_addressid);
        Logger.i("image_front  "+image_front);
    }

}
