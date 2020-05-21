package com.yilian.mall.http;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.ActivityDetailEntity;
import com.yilian.mall.entity.AnswerListEntity;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.FlashSaleDetailsEntity;
import com.yilian.mall.entity.FlashSaleEntity;
import com.yilian.mall.entity.JPCategoryHeaderEntity;
import com.yilian.mall.entity.JPCommodityDetail;
import com.yilian.mall.entity.JPFragmentGoodEntity;
import com.yilian.mall.entity.JPGoodsClassfiyEntity;
import com.yilian.mall.entity.JPLevel1CategoryEntity;
import com.yilian.mall.entity.JPLocalHeaderEntity;
import com.yilian.mall.entity.JPMainHeaderEntity;
import com.yilian.mall.entity.JPMyFavoriteGoodsEntity;
import com.yilian.mall.entity.JPMyFavoriteMerchantEntity;
import com.yilian.mall.entity.JPMyFavoriteStorEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.JPShopEntity;
import com.yilian.mall.entity.JPSignBeforeEntity;
import com.yilian.mall.entity.JPSignGVEntity;
import com.yilian.mall.entity.JPSignInEntity;
import com.yilian.mall.entity.JPSubClassfiyGoodsEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

/**
 * Created by liuyuqi on 2016/10/18 0018.
 * 新版商城 网络请求
 */

public class JPNetRequest extends BaseNetRequest {
    public String URL;

    public JPNetRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "mall.php";
    }


    /**
     * 请求商品分类数据
     *
     * @param callBack class_id "0"
     *                 type 0是大分类，1是大分类下对应的二级分类
     */
    public void getGoodsClassList(RequestCallBack<JPGoodsClassfiyEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "classify_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("class_id", "0");
        params.addBodyParameter("type", "1");
        postRequest(URL, params, JPGoodsClassfiyEntity.class, callBack);
    }

    /**
     * 获取一级分类的类别名称
     *
     * @param callBack
     */
    public void getJPLevel1Category(RequestCallBack<JPLevel1CategoryEntity> callBack) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "classify_list");
        params.addBodyParameter("device_index", "0");
        params.addBodyParameter("token", "0");
        params.addBodyParameter("class_id", "0");//class_id为0，获取全部一级分类，该数据中不包括第一个分类“上新”，所以请求到的集合应该在第一个位置添加“上新”
        params.addBodyParameter("type", "1");
        postRequest(URL, params, JPLevel1CategoryEntity.class, callBack);
    }

    /**
     * 获取首页头部数据 包括轮播、五个图标、运营图片（有显示和不显示两种状态）、活动图标（左边一个 右上一个 右下一个）
     */
    public void getJPMainHeaderData(RequestCallBack<JPMainHeaderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "appindex_v3");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        Logger.i("获取主页头部信息toke:  " + RequestOftenKey.getToken(mContext));
        postRequest(URL, params, JPMainHeaderEntity.class, callBack);
    }

    /**
     * 获取主页商家推荐信息
     * type 0 全国供货商 1 本地供货商
     */
    public void getMainRecommendData(int page, RequestCallBack<JPShopEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "supplier_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "0");
        params.addBodyParameter("id", "0");
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "30");
        Logger.i("请求首页商家数据时的page:" + page);
        postRequest(Ip.getURL(mContext) + "nearby.php", params, JPShopEntity.class, callBack);
    }

    /**
     * 获取一级分类界面头部数据 包括轮播、一行四列的图标
     */
    public void getJPCategoryData(String classId, RequestCallBack<JPCategoryHeaderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/goods_class_index");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("class_id", classId);
        Logger.i("获取二级分类头部数据参数;" + classId);
        postRequest(URL, params, JPCategoryHeaderEntity.class, callBack);
    }


    /**
     * 获取一级分类界面推荐商品
     *
     * @param classId  分类ID
     * @param page
     * @param callBack
     */
    public void getCategoryGoodsData(String classId, int page, RequestCallBack<JPFragmentGoodEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_global_class");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("sort", "0000");
        params.addBodyParameter("class_id", classId);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "30");
        Logger.i("class_id  " + classId + "  uRL  " + URL);
        postRequest(URL, params, JPFragmentGoodEntity.class, callBack);
    }

    /**
     * 请求小分类数据 web跳转
     *
     * @param page     当前的界面数 默认是从0开始的
     * @param count    当前界面请求服务器返回的数据的数量数
     * @param sort     0000默认 //排序 第一位的0默认是默认 第二位的是价格
     * @param class_id
     */
    public void getJpGoodsList(String sort, String class_id, String filiale_id, int page, int count, RequestCallBack<JPSubClassfiyGoodsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_global_class");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("class_id", class_id);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", String.valueOf(count));
        params.addBodyParameter("filiale_id", filiale_id);
        Logger.i("sort:" + sort + "filiale_id:" + filiale_id + "  class_id:" + class_id);
        postRequest(URL, params, JPSubClassfiyGoodsEntity.class, callBack);
    }

    /**
     * 获取乐分币商品信息
     */
    public void getLeFenGoodsList(String sort, int page, int count, RequestCallBack<JPSubClassfiyGoodsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_lefen_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", String.valueOf(count));
        Logger.i("乐分区商品参数：  sort:" + sort + "  page:" + page + " count:" + count + " ");
        postRequest(URL, params, JPSubClassfiyGoodsEntity.class, callBack);
    }

    /**
     * 获取新版v3商品详情
     */
    public void getV3MallGoodInfo(String filialeId, String goodsId, String type, RequestCallBack<JPCommodityDetail> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgood_info_v3");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("type", type);
        Logger.i("新版商品详情请求参数：  URL  " + URL + " filialeid  " + filialeId + "   goods_id:" + goodsId + " token  " + RequestOftenKey.getToken(mContext) + " deviceIndex " + RequestOftenKey.getDeviceIndex(mContext) + " type " + type);
        postRequest(URL, params, JPCommodityDetail.class, callBack);
    }

    /**
     * 获取猜价格-碰运气商品详情
     */
    public void getActivityGoodInfo(String activityId, String activityType, String goodsId, RequestCallBack<ActivityDetailEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "guessPriceAndLucky/actInfo");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("act_id", activityId);
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("act_type", activityType);
        Logger.i("url-" + URL + "?c=guessPriceAndLucky/actInfo" + "&device_index=" + RequestOftenKey.getDeviceIndex(mContext) + "&token=" + RequestOftenKey.getToken(mContext) + "&act_id=" + activityId + "&goods_id=" + goodsId + "&act_type=" + activityType);
        postRequest(URL, params, ActivityDetailEntity.class, callBack);
    }

    /**
     * 新版收藏
     * @param id
     * @param collect_type
     * @param filiale_id
     * @param type
     * @param callBack
     */
    public void collectV3(String id, String collect_type, String filiale_id, @Nullable String type, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("c", "goods_collect_add_v2");
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("filiale_id", filiale_id);
        params.addBodyParameter("id", id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("collect_type", collect_type);
        Logger.i("收藏参数：URL  " + URL + "   device_index:" + RequestOftenKey.getDeviceIndex(mContext) + "  token:" + RequestOftenKey.getToken(mContext)
                + "  filiale_id:" + filiale_id + "   id:" + id + "  collect_tye:" + collect_type + "  type " + String.valueOf(type) + " URL " + URL);
        postRequest(URL, params, BaseEntity.class, callBack);
    }


    /**
     * 新版取消收藏
     */
    public void cancelCollectV3(String id, String collect_type, String filiale_id, @Nullable String type, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_collect_cancel_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("filiale_id", filiale_id);
        params.addBodyParameter("id", id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("collect_type", collect_type);
        Logger.i("URL  " + URL + " filiale_id  " + filiale_id + " id  " + id + " collect_type " + collect_type);
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 获取本地页面头部信息，包括轮播、图标、三个活动
     *
     * @param cityId
     * @param countyId
     * @param callBack
     * @param sort     0,0,0,0 默认上架顺序升序 ，价格升序 ， 销量升序， 有无库存全部//0不参与排序1参与排序
     */
    public void getJPLocalHeaderData(String cityId, String countyId, String sort, int page, RequestCallBack<JPLocalHeaderEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "filiale_index");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("city", cityId);
        params.addBodyParameter("county", countyId);
        params.addBodyParameter("count", "20");
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("page", String.valueOf(page));
        Logger.i("请求本地商品数据参数：cityId" + cityId + "  countyId" + countyId + " ");
        postRequest(Ip.getURL(mContext) + "nearby.php", params, JPLocalHeaderEntity.class, callBack);
    }

    /**
     * 获取新版商城商品信息
     *
     * @param filialeId 兑换中心id（type=2时传商品分类id,type=6时传0）
     * @param type      0默认只显示兑换中心商品， 1兑换中心和旗舰店商品 2本地生鲜(除以上参数外还需要传county,city,county)
     *                  3本地精品默认按照本地旗舰店所有商品的销量倒序排列切换到价格后默认从高到底，再点切换从低到高(0,0,0) 4旗舰店商品 6首页商品
     * @param sort      (不排序商品默认传0)排序时根据实际情况确定该字符串长度
     * @param page      分页
     * @param callBack
     */
    public void getJPGoodsData(String filialeId, String type, String sort, int page, RequestCallBack<JPFragmentGoodEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/multi_mallgoods_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("filiale_id", filialeId);
        params.addBodyParameter("type", type);
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", Constants.PAGE_COUNT + "");
        Logger.i("旗舰店详情里的排序参数;  filialeId" + filialeId + "  type" + type + "   sort" + sort + "   page" + page);
        postRequest(URL, params, JPFragmentGoodEntity.class, callBack);
    }

    /**
     * 获取店铺商品列表
     *
     * @param supplierId
     * @param sort
     * @param page
     * @param callBack
     */
    public void getJPGoodsDatas(String supplierId, String sort, int page, int count,RequestCallBack<JPFragmentGoodEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/supplier_goods_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("supplier_id", supplierId);
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", count+"");
        postRequest(URL, params, JPFragmentGoodEntity.class, callBack);
    }

    /**
     * 签到之前  type = 0
     */
    public void signBefore(String type, RequestCallBack<JPSignBeforeEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "sign/user_sign");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", type);

        postRequest(URL, params, JPSignBeforeEntity.class, callBack);
    }


    /**
     * 请求我的收藏的商品数据
     *
     * @param callBack class_id "0"
     *                 0商品 1旗舰店 2商家 3套餐
     */
    public void getMyFavoriteList(RequestCallBack<JPMyFavoriteGoodsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/collect_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("getMyFavoriteList", "device_index" + RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        Logger.i("getMyFavoriteList", "token" + RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "0");
        postRequest(URL, params, JPMyFavoriteGoodsEntity.class, callBack);
    }

    /**
     * 签到 type = 1
     */
    public void signIn(String type, RequestCallBack<JPSignInEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "sign/user_sign");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", type);

        postRequest(URL, params, JPSignInEntity.class, callBack);
    }

    /**
     * 请求我的收藏的旗舰店数据
     *
     * @param callBack 0商品 1旗舰店 2商家 3套餐
     */
    public void getMyFavoriteStorList(RequestCallBack<JPMyFavoriteStorEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/collect_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
//        params.addBodyParameter("token", "3409563168400740314");
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
        params.addBodyParameter("c", "goods/collect_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "2");
        postRequest(URL, params, JPMyFavoriteMerchantEntity.class, callBack);
    }

    /**
     * 取消收藏数据请求
     *
     * @param callBack class_id "0"
     *                 0商品 1旗舰店 2商家 3套餐
     */
    public void getCancleFavoriteist(String collect_id, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods_collect_cancel_more");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("collect_id", collect_id);

        postRequest(URL, params, BaseEntity.class, callBack);
    }


    /**
     * 请求本地精品推荐
     * sort  默认排序
     * 0 商品
     * 0 价格 默认高到低 010 000低到高
     * 0 销量
     * filiale_id  兑换中心id（type=2时传商品分类id,type=6时传0）
     *
     * @param callBack
     */
    public void getNativeRecommendList(String filiale_id, String sort, int page, int count, RequestCallBack<JPSubClassfiyGoodsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/multi_mallgoods_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("filiale_id", String.valueOf(filiale_id));
        params.addBodyParameter("type", "3");
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", String.valueOf(count));
        postRequest(URL, params, JPSubClassfiyGoodsEntity.class, callBack);
    }

    /**
     * 签到里的为您推荐
     */
    public void signGridView(RequestCallBack<JPSignGVEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/mallgoods_recommend");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, JPSignGVEntity.class, callBack);
    }


    /**
     * 分享
     *
     * @param type     类型// 1=>旗舰店分享supplier_id; 2=>商品详情分享goods_id,filiale_id;
     *                 3=>商家详情分享merchant_id;4=>本地商城分享filiale_id;
     *                 5=>web页面分享(iOS); 6=>本地旗舰店列表分享filiale_id;
     *                 7=>乐分币 乐享币 现金券奖励分享; 8=>乐友及其领奖励分享;
     *                 9=>邀请有奖分享 10=>其他未提到的页面;
     *                 11=>套餐分享package_id; 12=>限时购分享data传goods_id 分享内容 标题:乐分商城给您送礼啦! 内容:注册就送50元 默认logo图片url 微信#领取礼品页面url;
     *                 13=>大转盘抽奖分享（data传prize_id）22 京东商品详情
    type = 23 苏宁商品详情
     * @param id       数据id type=>2 时 data = goods_id,filiale_id ;其他type只传id
     * @param callBack
     */
    public void getShareUrl(String type, String id, RequestCallBack<JPShareEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "share_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", type);
        params.addBodyParameter("data", id);
        postRequest(URL, params, JPShareEntity.class, callBack);
    }

    public void getNearbyGoodsList(String city_id, String county_id, int page, String sort, RequestCallBack<JPSubClassfiyGoodsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "goods/nearby_goods");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("city", city_id);
        params.addBodyParameter("county", county_id);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "30");
        params.addBodyParameter("sort", sort);
        postRequest(URL, params, JPSubClassfiyGoodsEntity.class, callBack);

        Logger.i("sort    " + sort);
        Logger.i("page    " + page);
        Logger.i("city_id    " + city_id);
        Logger.i("county_id    " + county_id);
        Logger.i("device_index    " + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("token    " + RequestOftenKey.getToken(mContext));
    }

    /**
     * 获取显示抢购的数据
     *
     * @param city   用户定位的城市的ID,必须是具体的城市ID
     * @param county 用户定位的区县id
     * @param round  0 进行时场次 1 上一场抢购场次 2下一场
     * @param page
     */
    public void getFlashSaleList(String city, String county, String round, int page, String type, String belong, RequestCallBack<FlashSaleEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "activity/flash_sale");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("city", city);
        params.addBodyParameter("county", county);
        params.addBodyParameter("round", round);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "30");
        params.addBodyParameter("type", type);
        params.addBodyParameter("belong", belong);
        postRequest(URL, params, FlashSaleEntity.class, callBack);


        Logger.i("device_index     " + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("token     " + RequestOftenKey.getToken(mContext));
        Logger.i("city     " + city);
        Logger.i("city     " + city);
        Logger.i("county     " + county);
        Logger.i("round     " + round);
        Logger.i("page     " + page);

    }


    public void getFlashSaleDetailsData(String goods_id, RequestCallBack<FlashSaleDetailsEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "activity/flash_goods");
        params.addBodyParameter("goods_id", goods_id);
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, FlashSaleDetailsEntity.class, callBack);


        Logger.i(" goods_id  " + goods_id);
        Logger.i(" device_index  " + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i(" token  " + RequestOftenKey.getToken(mContext));
    }

    /**
     * 咨询问答-提问
     */
    public void askAndAnswer(String goodsId, String questionStr, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "askAndAnswer");
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("question_info", questionStr);
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 咨询问答-列表
     */
    public void askAndAnswerList(String goodsId, int page, RequestCallBack<AnswerListEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "askAndAnswerList");
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, AnswerListEntity.class, callBack);
    }
}
