package com.yilian.mall.http;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.ADEntity;
import com.yilian.mall.entity.AfterSale;
import com.yilian.mall.entity.AreaInfoList;
import com.yilian.mall.entity.BarterOrderInfo;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.CreateServiceOrderEntity;
import com.yilian.mall.entity.CreateServiceOrderParameter;
import com.yilian.mall.entity.DeleteOrderEntity;
import com.yilian.mall.entity.EvaluateList;
import com.yilian.mall.entity.GetGoodInfoResult;
import com.yilian.mall.entity.GetGoodInfoResultV2;
import com.yilian.mall.entity.GoodsChargeForPayResultEntity;
import com.yilian.mall.entity.GoodsCollectList;
import com.yilian.mall.entity.GoodsList;
import com.yilian.mall.entity.JPLogisticsEntity;
import com.yilian.mall.entity.JPMallGoodsListEntity;
import com.yilian.mall.entity.KeyWord;
import com.yilian.mall.entity.MTShareRedBag;
import com.yilian.mall.entity.MainActivityEntity;
import com.yilian.mall.entity.MainFansIncomeEntity;
import com.yilian.mall.entity.MakeMallShoppingOrderEntity;
import com.yilian.mall.entity.MakeMallorderEntity;
import com.yilian.mall.entity.MallCancelMallorderEntity;
import com.yilian.mall.entity.MallCategoryHotListEntity;
import com.yilian.mall.entity.MallCategoryListEntity;
import com.yilian.mall.entity.MallConfirmMallorderEntity;
import com.yilian.mall.entity.MallExpressListEntity;
import com.yilian.mall.entity.MallGoodsExpressEntity;
import com.yilian.mall.entity.MallGoodsListEntity;
import com.yilian.mall.entity.MallOrderInfoEntity;
import com.yilian.mall.entity.MallOrderListEntity;
import com.yilian.mall.entity.MallProviderEntity;
import com.yilian.mall.entity.NoEvaluateList;
import com.yilian.networkingmodule.entity.PayOkEntity;
import com.yilian.mall.entity.PerPaymentOrderEntity;
import com.yilian.mall.entity.RefundOrderInfoEntity;
import com.yilian.mall.entity.RotateImageList;
import com.yilian.mall.entity.ServiceOrderListEntity;
import com.yilian.mall.entity.UploadImageData;
import com.yilian.mall.entity.UserAddressListEntity;
import com.yilian.mall.entity.UserDefaultAddressEntity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.retrofitutil.AppVersion;


public class MallNetRequest extends BaseNetRequest {


    private String URL;

    public MallNetRequest(Context mContext) {
        super(mContext);
        URL = Ip.getURL(mContext) + "mall.php";
        // TODO Auto-generated constructor stub

    }

    /**
     * 收藏商品
     *
     * @param goodsId  商品id
     * @param callBack
     */
    public void goodsCollect(String goodsId, String coolectType, String filialeId, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_collect_add");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("collect_type", coolectType);
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 取消收藏商品
     *
     * @param deviceIndex 设备编号
     * @param token       用户登录凭证
     * @param goodsId     商品id
     * @param cls
     * @param callBack
     */
    public void goodsCancelCollect(String deviceIndex, String token, String goodsId, String goodsFiliale, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_collect_cancel");
        params.addBodyParameter("device_index", deviceIndex);
        params.addBodyParameter("token", token);
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("filiale_id", goodsFiliale);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 用户商品收藏列表
     *
     * @param deviceIndex 设备编号
     * @param cls
     * @param callBack
     */
    public void getGoodsCollectList(String deviceIndex, Class<GoodsCollectList> cls, RequestCallBack<GoodsCollectList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_collect_list");
        params.addBodyParameter("device_index", deviceIndex);
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 获取兑换中心分区商品列表
     *
     * @param filialeId 兑换中心ID
     * @param goodType  商品分区，1表示乐换，2表示乐选，3表示乐购
     * @param cls
     * @param callBack
     */
    public void getFilialeGoodList(String filialeId, int goodType, Class<GoodsList> cls, RequestCallBack<GoodsList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "filiale_goods_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("goods_type", Integer.toString(goodType));
        postRequest(URL, params, cls, callBack);
    }


    /**
     * 2
     * 收货地址列表查询
     *
     * @param cls
     * @param callBack "device_index":"",//app安装设备编号
     *                 "token":"",//客户端登录token
     */
    public void userAddressList(Class<UserAddressListEntity> cls, RequestCallBack<UserAddressListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "address/user_address_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 3
     * 收货地址编辑/新建
     * {
     * "device_index":"",//app安装设备编号
     * "token":"",//客户端登录token
     * "address_id":"",//收货地址id，0表示新建，大于零表示修改对应id的地址
     * "province":"",//省
     * "province_name":"",//省
     * "city":"",//市
     * "city_name":"",//市
     * "county":"",//区或县
     * "county_name":"",//区或县
     * "address":"",//详细地址id
     * "phone":"",//电话id
     * "contacts":"",//收货人
     * "default_address":"",//0不是默认 1默认
     * }
     */
    public void editeUserAddress(UserAddressLists userAddressLists, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "address/edit_user_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("address_id", userAddressLists.address_id);
        params.addBodyParameter("province_id", userAddressLists.province_id);
        params.addBodyParameter("province_name", userAddressLists.province_name);
        params.addBodyParameter("city_id", userAddressLists.city_id);
        params.addBodyParameter("city_name", userAddressLists.city_name);
        params.addBodyParameter("county_id", userAddressLists.county_id);
        params.addBodyParameter("county_name", userAddressLists.county_name);
        params.addBodyParameter("address", userAddressLists.address);
        params.addBodyParameter("phone", userAddressLists.phone);
        params.addBodyParameter("contacts", userAddressLists.contacts);
        params.addBodyParameter("default_address", userAddressLists.default_address + "");
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 3
     * MT 版本收货地址编辑/新建
     * {
     * "device_index":"",//app安装设备编号
     * "token":"",//客户端登录token
     * "address_id":"",//收货地址id，0表示新建，大于零表示修改对应id的地址
     * "province":"",//省
     * "province_name":"",//省
     * "city":"",//市
     * "city_name":"",//市
     * "county":"",//区或县
     * "county_name":"",//区或县
     * "address":"",//详细地址id
     * "phone":"",//电话id
     * "contacts":"",//收货人
     * "default_address":"",//0不是默认 1默认
     * }
     */
    public void editeMTUserAddress(UserAddressLists userAddressLists, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "package/edit_user_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("address_id", userAddressLists.address_id);
        params.addBodyParameter("detailed_address", userAddressLists.address);
        params.addBodyParameter("phone", userAddressLists.phone);
        params.addBodyParameter("contacts", userAddressLists.contacts);
        params.addBodyParameter("default_address", userAddressLists.default_address + "");
        params.addBodyParameter("full_address", userAddressLists.fullAddress);

        params.addBodyParameter("lng", userAddressLists.longitude);
        params.addBodyParameter("lat", userAddressLists.latitude);
        Logger.i("编辑收货地址参数: deviceIndex:" + RequestOftenKey.getDeviceIndex(mContext) + "  token:" + RequestOftenKey.gettoken(mContext) + "  addressInfo:" + userAddressLists.toString());
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 4
     * 收货地址删除
     *
     * @param address_id 收货地址id
     * @param cls
     * @param callBack
     */
    public void deleteUserAddress(String address_id, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "address/delete_user_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("address_id", address_id);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 5
     * 收货地址设为默认
     *
     * @param address_id 收货地址id
     * @param cls
     * @param callBack
     */
    public void defaultUserAddress(String address_id, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "address/default_user_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("address_id", address_id);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 6
     * 获取用户默认地址
     *
     * @param cls
     * @param callBack
     */
    public void getDefaultUserAddress(Class<UserDefaultAddressEntity> cls, RequestCallBack<UserDefaultAddressEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "address/get_default_user_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
        Logger.i("获取用的默认地址   device_index" + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("获取用的默认地址   token" + RequestOftenKey.getToken(mContext));
    }

    /**
     * 7
     * 商品列表
     * "device_index":"",//app安装设备编号
     *
     * @param token//客户端登录token 没有登录时为0
     * @param goodsType         // 0表示所有区域，3乐送区（送区）4买区
     * @param comprehensive     // 0默认 1人气 2评论
     * @param sales//销量         按销量排序 1升序 2降序 0参数不排序 默认按照综合排序
     * @param price//价格         按价格排序 1升序 2降序 0参数不排序 默认按照综合排序
     * @param page//页数，0默认第一页
     * @param cls
     * @param callBack
     */
    public void mallGoodsList(String token, String goodsType, String comprehensive, String sales, String price, int page, Class<MallGoodsListEntity> cls, RequestCallBack<MallGoodsListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        params.addBodyParameter("comprehensive", comprehensive);
        params.addBodyParameter("sales", sales);
        params.addBodyParameter("price", price);
        params.addBodyParameter("page", Integer.toString(page));
        params.addBodyParameter("goods_type", goodsType);
        postRequest(URL, params, cls, callBack);
    }

    public void mallGoodsClassList(String goodsType, int cls, int comprehensive, int sales, int price, int page, RequestCallBack<MallGoodsListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_class");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("comprehensive", String.valueOf(comprehensive));
        params.addBodyParameter("sales", String.valueOf(sales));
        params.addBodyParameter("price", String.valueOf(price));
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("goods_type", goodsType);
        params.addBodyParameter("class", String.valueOf(cls));

        postRequest(URL, params, MallGoodsListEntity.class, callBack);
    }


    /**
     * 8,9
     * 获取商品详情
     * goods_id 商品id
     * code 验证码
     * filiale_id:
     * 兑换中心id(只有兑换中心商品时传id,其他商品传0)
     */
    public void getGoodsInfo(String filiale_id, String goodsId, RequestCallBack<GetGoodInfoResult> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgood_info_v3");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("filiale_id", filiale_id);
        postRequest(URL, params, GetGoodInfoResult.class, callBack);
    }

    /**
     * 获取商品详情
     */
    public void getGoodsInfo(String goodsId, Class<GetGoodInfoResultV2> cls, RequestCallBack<GetGoodInfoResultV2> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_info_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_id", goodsId);
        postRequest(URL, params, cls, callBack);
    }


    /**
     * 15
     * 搜索商品列表
     * "device_index":"",//设备编号
     *
     * @param keyword  关键字
     * @param page     页数，0默认第一页
     * @param cls
     * @param callBack
     */
    public void mallSearchList(String type, String keyword, int page, Class<MallGoodsListEntity> cls, RequestCallBack<MallGoodsListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/search_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("keyword", keyword);
        params.addBodyParameter("page", Integer.toString(page));
        params.addBodyParameter("type", type);
        postRequest(URL, params, cls, callBack);

    }


    /**
     * x新版商城搜索
     * "device_index":"",//设备编号
     *
     * @param keyword  关键字
     * @param page     页数，0默认第一页
     * @param cls
     * @param callBack
     */
    public void mallGoodsSearchList(String keyword, int page, Class<JPMallGoodsListEntity> cls, RequestCallBack<JPMallGoodsListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/search_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("keyword", keyword);
        params.addBodyParameter("page", String.valueOf(page));
        Logger.i(" URL " + URL + " DEVICEINDEX " + RequestOftenKey.getDeviceIndex(mContext) + " TOKEN " + RequestOftenKey.getToken(mContext) + " keyword " + keyword + " page  " + page);
        postRequest(URL, params, cls, callBack);

    }

    /**
     * 16
     * 商城轮播图
     *
     * @param callBack
     */
    public void mallBanner(RequestCallBack<RotateImageList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "banner/mall_banner");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        postRequest(URL, params, RotateImageList.class, callBack);
    }

    /**
     * 17
     * 从商品详情下单
     * "device_index":"",//app安装设备编号
     * "token":"",//客户端登录token
     * "goods_id":"",//商品id
     * "goods_count":"",//商品数量
     * "goods_sku":"",//商品规格相关信息，格式（1:2;5:6;3:7)），：分号前后分别代表属性和属性值
     * "order_remark":"",//订单备注
     * "user_address_id":"",//用户收货地址编号
     * "express_id":"",//配送方式模板
     */
    public void makeMallOrder(String regionId, String goods_id, int goods_count, String goods_sku,
                              String order_remark, String user_address_id, String express_id, double expressPrice,
                              String type, String filiale_id, String supplier_id, RequestCallBack<MakeMallorderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/make_mallorder_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_id", goods_id);
        params.addBodyParameter("goods_count", goods_count + "");
        params.addBodyParameter("goods_sku", goods_sku);
        params.addBodyParameter("order_remark", order_remark);
        params.addBodyParameter("user_address_id", user_address_id);
        params.addBodyParameter("express_id", express_id);
        params.addBodyParameter("express_price", String.valueOf(expressPrice));
        params.addBodyParameter("type", type);//支付方式0乐享币支付1乐享币加抵扣券
        params.addBodyParameter("filiale_id", filiale_id);
        params.addBodyParameter("region_id", regionId);
        params.addBodyParameter("supplier_id", supplier_id);
        postRequest(URL, params, MakeMallorderEntity.class, callBack);
    }

    /**
     * 17
     * JP 版本从商品详情下单
     * "device_index":"",//app安装设备编号
     * "token":"",//客户端登录token
     * region_id: 省分id(兑换中心或供货商传地区id，总部商品默认传0)
     * "goods_id":"",//商品id
     * "goods_count":"",//商品数量
     * "goods_sku":"",//商品规格相关信息，格式（1:2;5:6;3:7)），：分号前后分别代表属性和属性值
     * "order_remark":"",//订单备注
     * "user_address_id":"",//用户收货地址编号
     * "express_id":"",//配送方式模板
     * @param isUseDiGouQuan 0不使用代购券  1使用代购券
     */
    public void makeMallOrderV3(String isUseDiGouQuan,String regionId, String goods_id, int goods_count, String goods_sku,
                                String order_remark, String user_address_id, String express_id, double expressPrice,
                                String type, String filiale_id, String supplier_id, String typeFromDetail, RequestCallBack<MakeMallorderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/make_mallorder_v4");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_id", goods_id);
        params.addBodyParameter("goods_count", goods_count + "");
        params.addBodyParameter("goods_sku", goods_sku);
        params.addBodyParameter("order_remark", order_remark);
        params.addBodyParameter("user_address_id", user_address_id);
        params.addBodyParameter("express_id", express_id);
        params.addBodyParameter("express_price", String.valueOf(expressPrice));
        params.addBodyParameter("type", type);//支付方式0乐享币支付1乐享币加抵扣券
        params.addBodyParameter("filiale_id", filiale_id);
        params.addBodyParameter("region_id", regionId);
        params.addBodyParameter("supplier_id", supplier_id);
        params.addBodyParameter("type", typeFromDetail);
        params.addBodyParameter("is_quan",isUseDiGouQuan);
        Logger.i("商品详情提交订单，从提交订单到收银台参数：  goods_id" + goods_id + "  goods_sku:" + goods_sku);
        postRequest(URL, params, MakeMallorderEntity.class, callBack);
    }


    /**
     * 18
     * 购物车下单
     * "device_index":"",//app安装设备编号
     * "token":"",//客户端登录token 
     *
     * @param cart_list       //购物车id拼接的字符串 例"1,33" 
     * @param order_remark    ,订单备注
     * @param user_address_id //用户收货地址编号
     * @param express_id      //配送方式模板
     * @param callBack
     */
    public void makeMallorderCartV3(String isUseDaiGouQuan,String cart_list, String order_remark, String user_address_id, String express_id, double expressPrice, RequestCallBack<MakeMallShoppingOrderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/make_mallorder_from_cart_v4");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("cart_list", cart_list);
        params.addBodyParameter("order_remark", order_remark);
        params.addBodyParameter("user_address_id", user_address_id);
        params.addBodyParameter("express_id", express_id);
        params.addBodyParameter("express_price", String.valueOf(expressPrice));
        params.addBodyParameter("is_quan",isUseDaiGouQuan);
        postRequest(URL, params, MakeMallShoppingOrderEntity.class, callBack);
        Logger.i("购物车下单参数：device_index:"+RequestOftenKey.getDeviceIndex(mContext)+"  token:"+RequestOftenKey.getToken(mContext)+"  cart_list:"+cart_list+"  order_remark:"+order_remark+
        "  user_address_id:"+user_address_id+"  express_id:"+express_id+"  express_price:"+String.valueOf(expressPrice));
    }

    /**
     * 配送方式列表
     *
     * @param type     0 商品   1 购物车
     * @param goodsId  商品id， 购物车  1,2  用 “,” 拼接购物车id
     * @param callBack
     */
    public void expressList(String addressId, String type, String goodsId, RequestCallBack<MallExpressListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "express_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", type);
        params.addBodyParameter("id", goodsId);
        params.addBodyParameter("address_id", addressId);

        Logger.i(addressId + "---" + type + "---" + goodsId);
        postRequest(URL, params, MallExpressListEntity.class, callBack);
    }

    /**
     * 从商品详情下单获取物流费用
     *
     * @param goodsCount
     * @param goodsSku
     * @param filialeId
     * @param addressId
     * @param supplierId
     * @param callBack
     */
    public void getGoodDetailExpress(String goodsId, String goodsCount, String goodsSku, String filialeId, String addressId, String supplierId, RequestCallBack<MallGoodsExpressEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_express");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_count", goodsCount);
        params.addBodyParameter("goods_sku", goodsSku);
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("address_id", addressId);
        params.addBodyParameter("supplier_id", supplierId);
        params.addBodyParameter("goods_id", goodsId);

        postRequest(URL, params, MallGoodsExpressEntity.class, callBack);
    }

    /**
     * JP版从商品详情下单获取物流费用
     *
     * @param goodsCount
     * @param goodsSku
     * @param filialeId
     * @param addressId
     * @param supplierId
     * @param callBack
     * @param type       限时购调用时候需传1 不是限时购下单不传该字段
     */
    public void getGoodDetailExpressV3(String goodsId, String goodsCount, String goodsSku, String filialeId, String addressId, String supplierId, @Nullable String type, RequestCallBack<MallGoodsExpressEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_express_v3");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_count", goodsCount);
        params.addBodyParameter("goods_sku", goodsSku);
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("address_id", addressId);
        params.addBodyParameter("supplier_id", supplierId);
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("type", type);

        postRequest(URL, params, MallGoodsExpressEntity.class, callBack);
    }

    /**
     * 从购物车下单获取物流费用
     *
     * @param cartList
     * @param addressId
     * @param callBack
     */
    public void getCartExpress(String cartList, String addressId, RequestCallBack<MallGoodsExpressEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_cart_express");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("cart_list", cartList);
        params.addBodyParameter("address_id", addressId);
        Logger.i("购物车提交订单提交参数：" + "  cartList:" + cartList + "   addressId:" + addressId + "  device_index" + RequestOftenKey.getDeviceIndex(mContext) +
                "  token:" + RequestOftenKey.getToken(mContext));
        postRequest(URL, params, MallGoodsExpressEntity.class, callBack);
    }

    /**
     * JP版从购物车下单获取物流费用
     *
     * @param cartList
     * @param addressId
     * @param callBack
     */
    public void getCartExpressV3(String cartList, String addressId, RequestCallBack<MallGoodsExpressEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_cart_express_v3");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("cart_list", cartList);
        params.addBodyParameter("address_id", addressId);
        Logger.i("购物车提交订单提交参数：" + "  cartList:" + cartList + "   addressId:" + addressId + "  device_index" + RequestOftenKey.getDeviceIndex(mContext) +
                "  token:" + RequestOftenKey.getToken(mContext));
        postRequest(URL, params, MallGoodsExpressEntity.class, callBack);
    }

    /**
     * 配送方式名单
     */
    public void expressListV1(RequestCallBack<MallExpressListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "express_list_v1");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, MallExpressListEntity.class, callBack);
    }

    /**
     * 21
     * 取消订单
     *
     * @param order_index 订单编号
     *                    order_cancel_reason       取消订单原因
     * @param cls
     * @param callBack
     */
    public void cancelMallorder(String order_index, String order_cancel_reason, Class<MallCancelMallorderEntity> cls, RequestCallBack<MallCancelMallorderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/cancel_mallorder");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", order_index);
        params.addBodyParameter("order_cancel_reason", order_cancel_reason);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 21
     * 新版取消订单
     *
     * @param order_index 订单编号
     *                    order_cancel_reason       取消订单原因
     * @param cls
     * @param callBack
     */
    public void cancelMallorder2(String order_index, String order_cancel_reason, Class<MallCancelMallorderEntity> cls, RequestCallBack<MallCancelMallorderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/cancel_mallorder_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", order_index);
        params.addBodyParameter("order_cancel_reason", order_cancel_reason);
        postRequest(URL, params, cls, callBack);

        Logger.i("  device_index  " + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("  token  " + RequestOftenKey.getToken(mContext));
        Logger.i("  order_index  " + order_index);
        Logger.i("  order_cancel_reason  " + order_cancel_reason);

    }

    /**
     * 22
     * 确认收货
     *
     * @param orderId  订单编号
     * @param callBack
     */
    public void confirmMallOrder(String orderId, RequestCallBack<MallConfirmMallorderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/confirm_mallorder");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", orderId);
        postRequest(URL, params, MallConfirmMallorderEntity.class, callBack);
    }

    /**
     * 23
     * 取得全部订单列表
     *
     * @param order_type //订单类型 0 全部订单 1代付款 2已付款 3已完成 4待评价
     * @param page       //分页 默认是0
     * @param cls
     * @param callBack
     */
    public void mallorderList(int order_type, int page, Class<MallOrderListEntity> cls, RequestCallBack<MallOrderListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/mallorder_list_v1");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_type", Integer.toString(order_type));
        params.addBodyParameter("page", Integer.toString(page));
        postRequest(URL, params, cls, callBack);
        Logger.i("mallorderListtype " + order_type + "\npage  " + page + "\ntoken " + RequestOftenKey.getToken(mContext) + "\ndeviceIndex  " + RequestOftenKey.getDeviceIndex(mContext) + " \n type  " + order_type);
    }

    public void noEvaluate(int page, RequestCallBack<NoEvaluateList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "comment/uncomment_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", Integer.toString(page));
        postRequest(URL, params, NoEvaluateList.class, callBack);
    }


    /**
     * 24
     * 取得总部商品订单详情
     *
     * @param order_index 订单id
     * @param cls
     * @param callBack
     */
    public void mallOrderInfo(String order_index, Class<MallOrderInfoEntity> cls, RequestCallBack<MallOrderInfoEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/mallorder_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", order_index);
        postRequest(URL, params, cls, callBack);
        Logger.i("URL  "+URL+"  device_index  "+RequestOftenKey.getDeviceIndex(mContext)+" token "+
                RequestOftenKey.getToken(mContext)+"  orderIndex  "+order_index);
    }

    /**
     * 25
     * 申请售后
     *
     * @param parameters
     * @param callBack
     */
    public void createServiceOrder(CreateServiceOrderParameter parameters, RequestCallBack<CreateServiceOrderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/create_service_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", parameters.order_index);
        params.addBodyParameter("order_goods_index", parameters.order_goods_index);
//        params.addBodyParameter("order_goods_sku", parameters.order_goods_sku);
        params.addBodyParameter("order_goods_norms", parameters.order_goods_norms);
        params.addBodyParameter("service_type", parameters.service_type + "");
        params.addBodyParameter("service_total_price", parameters.service_total_price + "");
        params.addBodyParameter("service_goods_count", parameters.service_goods_count + "");
        params.addBodyParameter("service_remark", parameters.service_remark);
        params.addBodyParameter("service_images", parameters.service_images);
        params.addBodyParameter("barter_type", parameters.barter_type + "");
        postRequest(URL, params, CreateServiceOrderEntity.class, callBack);
    }

    /**
     * 25
     * 新版申请售后
     *
     * @param parameters
     * @param callBack
     */
    public void createServiceOrder2(CreateServiceOrderParameter parameters, RequestCallBack<CreateServiceOrderEntity> callBack) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("c", "order/create_service_order_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", parameters.order_index);
        params.addBodyParameter("order_goods_index", parameters.order_goods_index);
//        params.addBodyParameter("order_goods_sku", parameters.order_goods_sku);
        params.addBodyParameter("order_goods_norms", parameters.order_goods_norms);
        params.addBodyParameter("service_type", parameters.service_type + "");
        params.addBodyParameter("service_total_price", parameters.service_total_price + "");
        params.addBodyParameter("service_goods_count", parameters.service_goods_count + "");
        params.addBodyParameter("service_remark", parameters.service_remark);
        params.addBodyParameter("service_images", parameters.service_images);
        params.addBodyParameter("barter_type", parameters.barter_type + "");
        Logger.i("申请售后参数:" + parameters.toString());
        postRequest(URL, params, CreateServiceOrderEntity.class, callBack);
    }

    /**
     * 26
     * 查询售后列表
     *
     * @param page
     * @param cls
     * @param callBack
     */
    public void serviceOrderList(int page, Class<ServiceOrderListEntity> cls, RequestCallBack<ServiceOrderListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/service_order_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", page + "");
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 27
     * 根据商品名称或者订单号查询售后列表
     *
     * @param keyWord  查询关键字
     * @param page
     * @param callBack
     */
    public void keyWordAfterSaleOrderList(String keyWord, int page, RequestCallBack<AfterSale> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/select_service_order_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("key_word", keyWord);
        params.addBodyParameter("page", page + "");
        postRequest(URL, params, AfterSale.class, callBack);
    }

    /**
     * 28
     * 查询换货返修订单
     * 订单号
     *
     * @param barter_order
     * @param cls
     * @param callBack
     */
    public void barterOrderInfo(String barter_order, Class<BarterOrderInfo> cls, RequestCallBack<BarterOrderInfo> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/barter_order_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("barter_order", barter_order);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 29
     * 查询退货订单
     * 订单号
     *
     * @param refund_order
     * @param cls
     * @param callBack
     */
    public void refundOrderInfo(String refund_order, Class<RefundOrderInfoEntity> cls, RequestCallBack<RefundOrderInfoEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/refund_order_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("refund_order", refund_order);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 30
     * 售后订单确认解决
     *
     * @param order_id  订单号
     * @param apy_passw 支付密码
     * @param cls
     * @param callBack
     */
    public void confirmBarterOrder(String order_id, String apy_passw, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/confirm_barter_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("apy_passw", apy_passw);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 31
     * 提交物流单号
     *
     * @param order_type  //0 退货 1换货或维修
     * @param order_id    //订单编号
     * @param express_com //物流公司
     * @param express_num //物流编号
     * @param callBack
     */
    public void setExpress(int order_type, String order_id, String express_com, String express_num, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/set_express");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_type", Integer.toString(order_type));
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("express_com", express_com);
        params.addBodyParameter("express_num", express_num);
        postRequest(URL, params, BaseEntity.class, callBack);

        Logger.i("2016-12-28:" + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("2016-12-28:" + RequestOftenKey.getToken(mContext));
        Logger.i("2016-12-28:" + Integer.toString(order_type));
        Logger.i("2016-12-28:" + order_id);
        Logger.i("2016-12-28:" + express_com);
        Logger.i("2016-12-28:" + express_num);
    }

    /**
     * 获取评价信息
     *
     * @param goodsId  商品 id
     * @param page     页数
     * @param callBack
     */
    public void getEvaluateList(String goodsId, String filialeId, int page, RequestCallBack<EvaluateList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "comment/goods_comment");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("page", String.valueOf(page));
        postRequest(URL, params, EvaluateList.class, callBack);
    }

    /**
     * @param orderId         订单编号
     * @param orderGoodsIndex 商品订单规格自增编号
     * @param goodsId         评价商品id
     * @param anonymity       是否匿名评论 0正常 1匿名
     * @param score           评价分数
     * @param content         评价内容
     * @param images          评价图片
     * @param callBack
     */
    public void Evaluate(String orderId, String orderGoodsIndex, String goodsId, String anonymity,
                         float score, String content, String images, RequestCallBack<BaseEntity> callBack) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "comment/set_comment");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("comment_score", String.valueOf(score));
        params.addBodyParameter("order_id", orderId);
        params.addBodyParameter("order_goods_index", orderGoodsIndex);
        params.addBodyParameter("comment_anonymity", anonymity);
        params.addBodyParameter("comment_content", content);
        params.addBodyParameter("comment_images", images);
        postRequest(URL, params, BaseEntity.class, callBack);
    }


    /**
     * 44
     * 奖励支付
     *
     * @param payPwd   支付密码
     * @param orderId
     * @param callBack
     */
    public void payBalance(String payPwd, String orderId, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "pay/pay_balance");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("pay_pwd", payPwd);
        params.addBodyParameter("order_id", orderId);
        postRequest(URL, params, BaseEntity.class, callBack);
    }


    public void uploadGoodsImg(String imgData, RequestCallBack<UploadImageData> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "img/upload_image_data");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("image_data", imgData);
        params.addBodyParameter("image_role", "goods");
        params.addBodyParameter("image_name", "");
        params.addBodyParameter("image_type", "image/png");
        params.addBodyParameter("image_size", imgData.length() + "");

        Logger.i("2017年8月21日 10:49:20-" + URL);
        Logger.i("2017年8月21日 10:49:20-" + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("2017年8月21日 10:49:20-" + RequestOftenKey.getToken(mContext));
        Logger.i("2017年8月21日 10:49:20-" + imgData);
        Logger.i("2017年8月21日 10:49:20-" + imgData.length() + "");

        postRequest(URL, params, UploadImageData.class, callBack);

    }

    /**
     * 获取总领奖励及总人数
     *
     * @param callBack
     */
    public void getIncome(RequestCallBack<MainFansIncomeEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/member_count");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, MainFansIncomeEntity.class, callBack);
    }

    /**
     * 搜索城市
     *
     * @param cityName 城市名 关键字
     * @param callBack
     */
    public void selectCity(String cityName, RequestCallBack<AreaInfoList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "location/select_cities");
        params.addBodyParameter("name", cityName);

        postRequest(URL, params, AreaInfoList.class, callBack);
    }

    public void likeGoods(String token, int page, RequestCallBack<MallGoodsListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_provide");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        params.addBodyParameter("page", String.valueOf(page));
        postRequest(URL, params, MallGoodsListEntity.class, callBack);
    }

    /**
     * 绑定微信
     *
     * @param openid
     * @param unionid
     * @param nickname
     * @param headimgurl
     * @param callBack
     */
    public void bindWX(String openid, String unionid, String nickname, String headimgurl, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/update_openid");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("unionid", unionid);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("headimgurl", headimgurl);
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 获取首页数据
     *
     * @param callBack
     */
    public void getMainData(RequestCallBack<MainActivityEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "appindex");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, MainActivityEntity.class, callBack);
    }

    /**
     * 获取广告数据
     */
    public void getADData(RequestCallBack<ADEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "appAdv_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("app_version", AppVersion.getAppVersion(mContext));
        postRequest(URL, params, ADEntity.class, callBack);
    }

    /**
     * 获取省分公司商品分类列表
     *
     * @param callBack
     */
    public void mallcategory(String region_id, RequestCallBack<MallCategoryListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_classify");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("region_id", region_id);

        postRequest(URL, params, MallCategoryListEntity.class, callBack);
    }

    /**
     * 热销商品，分类商品，旗舰店商品列表
     *
     * @param region_id     //  省份id
     * @param comprehensive // 0默认 1人气 2评论
     * @param sales         //销量 按销量排序 1升序 2降序 0参数不排序 默认按照综合排序
     * @param price         //价格 按价格排序 1升序 2降序 0参数不排序 默认按照综合排序
     * @param page          //页数，0默认第一页
     * @param supplier_id   //品牌旗舰店id(不是必需参数)
     */
    public void mallcategorylist(String classId, String region_id, String supplier_id, int comprehensive, int sales, int price, int page, RequestCallBack<MallCategoryHotListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("comprehensive", comprehensive + "");
        params.addBodyParameter("sales", sales + "");
        params.addBodyParameter("price", price + "");
        params.addBodyParameter("page", Integer.toString(page));
        params.addBodyParameter("class_id", classId);
        params.addBodyParameter("region_id", region_id); //正式用
        params.addBodyParameter("supplier_id", supplier_id);
        Logger.i("classID" + classId + "  " + RequestOftenKey.getDeviceIndex(mContext) + "==" + RequestOftenKey.getToken(mContext) +
                "==" + comprehensive + "==" + sales + "==" + price + "==" + region_id + "==" + supplier_id);
        postRequest(URL, params, MallCategoryHotListEntity.class, callBack);
    }

    /**
     * 品牌旗舰店列表
     *
     * @param region_id
     * @param page
     * @param callBack
     */

    public void mallProvider(String region_id, int page, RequestCallBack<MallProviderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_brand_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("region_id", region_id); //正式用
//        params.addBodyParameter("region_id", "11");//测试用
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "20");
        params.addBodyParameter("city_id", mContext.getSharedPreferences("UserInfor", 0).getString(Constants.SPKEY_SELECT_CITY_ID, ""));
        postRequest(URL, params, MallProviderEntity.class, callBack);
    }

    /**
     * 搜索热词
     *
     * @param callBack
     */
    public void mallHotKeyWord(String region_id, RequestCallBack<KeyWord> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/search_hot_word");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("region_id", region_id); //正式用
        postRequest(URL, params, KeyWord.class, callBack);
        Logger.i("商品的热词" + "region_id ：" + region_id);
    }

    /**
     * 搜索结果
     */
    public void mallSearch(String region_id, String keyword, RequestCallBack<MallGoodsListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_search");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("region_id", region_id); //正式用
//        params.addBodyParameter("region_id", "11");//测试用
        params.addBodyParameter("keyword", keyword);
        postRequest(URL, params, MallGoodsListEntity.class, callBack);
    }

    /**
     * 新商城搜索结果
     */
    public void mallGoodsSearch(String region_id, String keyword, RequestCallBack<JPMallGoodsListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_search");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("region_id", region_id); //正式用
        params.addBodyParameter("keyword", keyword);
        postRequest(URL, params, JPMallGoodsListEntity.class, callBack);
        Logger.i("搜索的结果参数" + params.toString());
    }

    /**
     * 支付
     *
     * @param orderIds
     * @param payPwd
     * @param callBack
     */
    public void CashDeskPayGoods(String orderIds, String payPwd, RequestCallBack<PayOkEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "pay/pay_balance_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", orderIds);
        params.addBodyParameter("pay_pwd", payPwd);
        params.addBodyParameter("moneys","0");
        Logger.i("Url  " + URL + " orderindex " + orderIds + "  paypwd " + payPwd + " token  " + RequestOftenKey.getToken(mContext) + " device_index " + RequestOftenKey.getDeviceIndex(mContext));
        postRequest(URL, params, PayOkEntity.class, callBack);
    }

    /**
     * 支付结果  （一般在奖励不足进行支付的情况下进行调用，奖励不足时，进行充值支付，充值成功后，由后台完成订单结算，所以在支付成功回调中回调该接口）
     *
     * @param orderIds 订单id 多个的用英文，分割
     * @param type     商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @param callBack
     */
    public void getPayResult(String orderIds, String type, RequestCallBack<GoodsChargeForPayResultEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "pay_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", orderIds);
        params.addBodyParameter("type", type);
        Logger.i("获取支付结果参数：device_index:" + RequestOftenKey.getDeviceIndex(mContext) + " token:" + RequestOftenKey.getToken(mContext) + " orderIndex:" + orderIds + " type:" + type);
        postRequest(Ip.getURL(mContext) + "payment.php", params, GoodsChargeForPayResultEntity.class, callBack);
    }

    /**
     * v3版订单中查看物流
     */
    public void checkLogistics(String id, RequestCallBack<JPLogisticsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "express_name");
        params.addBodyParameter("express_id", id);
        postRequest(URL, params, JPLogisticsEntity.class, callBack);

    }


    /**
     * 生成预支付订单
     *
     * @param cash          订单需要支付多少金额
     * @param paymentCoupon 订单需要支付多少抵扣券
     * @param merId         商家ID
     * @param type          App传0
     * @param employee      员工ID
     * @param callBack
     */
    public void getPerPaymentOrderId(String cash, String paymentCoupon, String merId, String type, String employee, RequestCallBack<PerPaymentOrderEntity> callBack) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "recharge/recharge_mer");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("cash", cash);
        params.addBodyParameter("payment_coupon", paymentCoupon);
        params.addBodyParameter("mer_id", merId);
        params.addBodyParameter("type", type);
        params.addBodyParameter("employee", employee);
        Logger.i("生成预支付订单参数  device:" + RequestOftenKey.getDeviceIndex(mContext)
                + "  token:" + RequestOftenKey.getToken(mContext)
                + " cash" + cash + "   payment_coupon:" + paymentCoupon + "  mer_id:" + merId + "  type:" + type + "   employee:" + employee);
        postRequest(URL, params, PerPaymentOrderEntity.class, callBack);
    }

    /**
     * 订单评论成功回调
     */
    public void getShareRedBag(RequestCallBack<MTShareRedBag> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "activity/share_redbag");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, MTShareRedBag.class, callBack);
    }

    /**
     * 删除订单
     *
     * @param orderId  订单ID(包括商品订单和套餐订单，只能是取消的订单才能删除，其他状态订单不能删除)
     * @param type     1：商城订单
     * @param callBack
     */
    public void deleteOrder(String orderId, String type, RequestCallBack<DeleteOrderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "order/delete_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("order_index", orderId);
        params.addBodyParameter("type", type);
        postRequest(URL, params, DeleteOrderEntity.class, callBack);
    }
}
