package com.yilian.mall.http;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.CartCount;
import com.yilian.mall.entity.ShoppingCartList;
import com.yilian.mall.entity.ShoppingCartListEntity2;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Ip;

/**
 * Created by 刘玉坤 on 2016/1/22.
 */
public class ShoppingCartNetRequest extends BaseNetRequest {

    private  String URL;
    private String token;

    public ShoppingCartNetRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "mall.php";
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 商品添加购物车
     *
     * @param goodsId    商品 id
     * @param goodsCount 商品数量
     * @param goodsSku   商品 sku 用 ; 隔开   如：0:0;0:9
     * @param goodsNorms 商品 sku 描述 用 , 隔开 如：白色,39码
     * @param callBack
     */
    public void addShoppingCart(String goodsId, String goodsCount, String goodsSku, String goodsNorms,
                                String regionId, String filialeId, String supplierId, String type,
                                RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "cart/add_cart_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        Logger.i(RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("token == " + token);
        params.addBodyParameter("token", token);
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("goods_count", goodsCount);
        params.addBodyParameter("goods_sku", goodsSku);
        params.addBodyParameter("goods_norms", goodsNorms);
        params.addBodyParameter("region_id", regionId);
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("supplier_id", supplierId);
        params.addBodyParameter("type", type);//该值固定为1

        Logger.i("device_index " + RequestOftenKey.getDeviceIndex(mContext) + " token " + token + " goods_id " + goodsId + " goods_count " + goodsCount + " goodsSku " + goodsSku + " goodsNorms " + goodsNorms + " regionId " + regionId + " filialeId "
                + filialeId + " supplierId " + supplierId);

        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 删除购物车商品
     *
     * @param cartId   购物车id拼接字符串例:"123,12"  0代表全部删
     * @param callBack
     */
    public void deleteShoppingCartGoods(String cartId, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "cart/delete_cart");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        params.addBodyParameter("cart_id", cartId);

        postRequest(URL, params, BaseEntity.class, callBack);

    }

    /**
     * 购物车列表
     *
     * @param callBack
     */
    public void getShoppingCartList(RequestCallBack<ShoppingCartList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "cart/cart_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        Logger.i("token == " + token);
        postRequest(URL, params, ShoppingCartList.class, callBack);
    }

    /**
     * 新版购物车列表
     *
     * @param callBack
     */
    public void getShoppingCartList2(RequestCallBack<ShoppingCartListEntity2> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "cart/cart_list_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        Logger.i("URL  " + URL + "  DEVICEINDE " + RequestOftenKey.getDeviceIndex(mContext) + " TOKEN " + token);
        postRequest(URL, params, ShoppingCartListEntity2.class, callBack);

    }


    /**
     * 修改购物车商品数量
     *
     * @param cartId   购物车 id
     * @param count    修改后商品数量
     * @param callBack
     */
    public void modifyShoppingCartQuantity(String cartId, String count,
                                           RequestCallBack<ShoppingCartList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "cart/update_cart_quantity");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        Logger.i("token == " + token);
        params.addBodyParameter("cart_id", cartId);
        params.addBodyParameter("count", count);

        postRequest(URL, params, ShoppingCartList.class, callBack);
    }

    /**
     * 新版 修改购物车商品数量
     *
     * @param cartId   购物车 id
     * @param count    修改后商品数量
     * @param callBack
     */
    public void modifyShoppingCartQuantity2(String cartId, String count,
                                            RequestCallBack<ShoppingCartListEntity2> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "cart/update_cart_quantity");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        Logger.i("token == " + token);
        params.addBodyParameter("cart_id", cartId);
        params.addBodyParameter("count", count);

        postRequest(URL, params, ShoppingCartListEntity2.class, callBack);
    }

    /**
     * 获取购物车商品数量
     *
     * @param callBack
     */
    public void cartCount(RequestCallBack<CartCount> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "cart/cart_count");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);

        postRequest(URL, params, CartCount.class, callBack);
    }

    /**
     * 收藏商品
     *
     * @param goodsId
     * @param callBack
     */
    public void collect(String goodsId, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_collect_add_more");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("收藏商品参数：" + RequestOftenKey.getDeviceIndex(mContext) + "  token:" + token + "  goods_id:" + goodsId);
        params.addBodyParameter("token", token);
        params.addBodyParameter("goods_id", goodsId);

        postRequest(URL, params, BaseEntity.class, callBack);

    }

    /**
     * V2收藏商品
     *
     * @param goodsIds    商品ID以","拼接的字符串 如"1,2,3"
     * @param filialeIds  兑换中心id以","拼接的字符串 如"1,2,3"
     * @param supplierIds 旗舰店id以","拼接的字符串 如"1,2,3"
     * @param type        区分是什么类型的商品
     * @param callBack
     */
    public void collectv2(String goodsIds, String filialeIds, String supplierIds, String type, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_collect_add_more_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("收藏商品参数：" + RequestOftenKey.getDeviceIndex(mContext) + "  token:" + token + "  goods_id:" + goodsIds);
        params.addBodyParameter("token", token);
        params.addBodyParameter("goods_id", goodsIds);
        params.addBodyParameter("type", type);
        params.addBodyParameter("filiale_ids", filialeIds);
        params.addBodyParameter("supplier_ids", supplierIds);

        postRequest(URL, params, BaseEntity.class, callBack);

    }


}
