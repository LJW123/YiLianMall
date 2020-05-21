package com.yilian.mall.http;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.Merchant;
import com.yilian.mall.entity.MerchantEntity;
import com.yilian.mall.entity.MerchantPraiseEntity;
import com.yilian.mall.entity.Nearby;
import com.yilian.mall.entity.Search;
import com.yilian.mall.entity.ShopsConsumeEntity;
import com.yilian.mall.entity.ShopsPresentEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Ip;

public class NearbyNetRequest extends BaseNetRequest {
	public  String URL;

	public NearbyNetRequest(Context mContext) {
		super(mContext);
		URL=Ip.getURL(mContext)+"nearby.php";
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取附近商家列表 ,兑换中心列表
	 * @param page 页码，默认0
	 * @param industryTop 联盟商家行业顶级分类，默认0表示全部分类   兑换中心为 0
	 * @param industrySon /联盟商家行业二级分类，默认0   兑换中心为 0
	 * @param type 商家类型0表示全部，1表示仅联盟商家，2表示该地区所有兑换中心  3表示该省所有兑换中心
	 * @param callBack
	 */
	public void getNearbyList(int page,String industryTop,String industrySon,String type,String cityId,String countyId,String provinceId,RequestCallBack<Nearby> callBack) {
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "list");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("industry_top", industryTop);
		params.addBodyParameter("industry_son", industrySon);
		params.addBodyParameter("page", Integer.toString(page));
		params.addBodyParameter("type", type);
		params.addBodyParameter("county", countyId);
		params.addBodyParameter("city", cityId);
		params.addBodyParameter("province",provinceId);
		
		postRequest(URL, params, Nearby.class, callBack);
	}

	/**
	 * 搜索附件商家
	 * deviceIndex
	 * @param city 用户定位城市ID，必须是具体的城市ID
	 * @param county 用户定位区县id,默认0
	 * @param keyword 关键字
	 * @param cls
	 * @param callBack
	 */
    public void searchNearbyMerchants(String city, String county, String keyword, String lat, String lng, Class<Search> cls, RequestCallBack<Search> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "search");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("city", city);
        params.addBodyParameter("county", county);
        params.addBodyParameter("keyword", keyword);
        params.addBodyParameter("lat", lat);
        params.addBodyParameter("lng", lng);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 获取商家详细资料
     * @param merchantType 1代表联盟商家，2代表兑换中心
     * @param merchantId merchant_type为1时代表联盟商家id，为2时代表兑换中心门面id
	 * @param callBack
	 */
	public void getMerchantDetails(String merchantType,String merchantId,RequestCallBack<Merchant> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "details");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("merchant_type", merchantType);
		params.addBodyParameter("merchant_id", merchantId);
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		
		postRequest(URL, params,Merchant.class, callBack);
	}

	/**
	 * 点赞
	 * @param merchantId 商家唯一 id
	 * @param callBack
     */
	public void clickPraise(String merchantId,RequestCallBack<MerchantPraiseEntity> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "merchant_praise");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("merchant_id", merchantId);
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		Logger.i("merchant_id:" + merchantId);
		Logger.i("merchant_id:" + RequestOftenKey.getDeviceIndex(mContext));
		Logger.i("merchant_id:" + RequestOftenKey.getToken(mContext));
		postRequest(URL, params,MerchantPraiseEntity.class, callBack);
	}

	/**
	 * 获得商家赠送记录
	 * @param merchantId
	 * @param page
	 * @param callBack
     */
	public void getShopsPresentDetail(String merchantId,int page ,RequestCallBack<ShopsPresentEntity> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "merchant_give_lefen");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("page", String.valueOf(page));
		params.addBodyParameter("count", "20");
		params.addBodyParameter("merchant_id", merchantId);
		postRequest(URL, params,ShopsPresentEntity.class, callBack);
	}

	/**
	 * 获得消费者在商家消费记录记录
	 * @param merchantId
	 * @param page
	 * @param callBack
	 */
	public void getShopsConsumeDetail(String merchantId,int page ,RequestCallBack<ShopsConsumeEntity> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "consumer_expend_lebi");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("page", String.valueOf(page));
		params.addBodyParameter("count", "20");
		params.addBodyParameter("merchant_id", merchantId);
		postRequest(URL, params,ShopsConsumeEntity.class, callBack);
	}

	/**
	 * 兑换中心详情
	 * @param page//分页
	 * @param shop_index//兑换中心分店唯一ID
	 * @param callBack
	 */
	public void getMerchantDetail(String recommend,int page ,String shop_index,RequestCallBack<MerchantEntity> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "filiale_v2");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("page", String.valueOf(page));
		params.addBodyParameter("recommend", recommend);
		params.addBodyParameter("count", "20");
		params.addBodyParameter("shop_index", shop_index);

		Logger.i(RequestOftenKey.getDeviceIndex(mContext)+ "=="+RequestOftenKey.getToken(mContext)+"=="+shop_index);
		postRequest(URL, params, MerchantEntity.class, callBack);
	}
}
