package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.ActivityList;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.DailyId;
import com.yilian.mall.entity.DailyTurntable;
import com.yilian.mall.entity.GetActivityInfoResult;
import com.yilian.mall.entity.GetWinnerListResult;
import com.yilian.mall.entity.GuessWinnerInfo;
import com.yilian.mall.entity.GuessedNumberEntity;
import com.yilian.mall.entity.GuessingNumberEntity;
import com.yilian.mall.entity.JPLocation;
import com.yilian.mall.entity.JPTurnTableEntity;
import com.yilian.mall.entity.ScrapeEntity;
import com.yilian.mall.entity.ShakePrize;
import com.yilian.mall.entity.ShakeResult;
import com.yilian.mall.entity.ShakeWinnersList;
import com.yilian.mall.entity.TurnTablePrize;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;

/**
 * 活动相关请求
 * @author Administrator
 */
public class ActivityNetRequest extends BaseNetRequest {
	
	private  String URL;

	public ActivityNetRequest(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		URL=Ip.getURL(mContext) + "activity.php";
	}

	/**
	 * 获取每日大转盘
	 * @param callBack
	 */
	public void getDailyId(RequestCallBack<DailyId> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "daily_id");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		postRequest(URL, params, DailyId.class, callBack);
	}

	/**
	 * 参加免費抽獎
	 * @param activityIndex 活动id
	 * @param play 参与次数
	 * @param callBack getLotteryFreeActivityResult
     */
	public void getLotteryFree(String activityIndex,String play,RequestCallBack<DailyTurntable> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "daily_turntable");
		params.addBodyParameter("activity_index", activityIndex);
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("play", play);
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		postRequest(URL, params, DailyTurntable.class, callBack);
	}


	/**
	 * 获取活动详细信息
	 * @param activityIndex 活动id
	 * @param callBack
	 */
	public void getActivityInfo(String activityIndex, RequestCallBack<GetActivityInfoResult> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "details");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("activity_index", activityIndex);
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		postRequest(URL, params, GetActivityInfoResult.class, callBack);
	}

	/**
	 * 参加摇一摇活动
	 * 

	 * @param activityIndex 活动id
	 * @param callBack
	 */
	public void getShakeActivityResult(String activityIndex,RequestCallBack<ShakeResult> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "shake");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("activity_index", activityIndex);
		postRequest(URL, params, ShakeResult.class, callBack);
	}
	/**
	 * 参加大转盘活动
	 * @param activityIndex 活动id
	 * @param callBack
	 */
	public void getTurnTableActivityResult(String activityIndex,RequestCallBack<TurnTablePrize> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "turntable");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("activity_index", activityIndex);
		
		postRequest(URL, params, TurnTablePrize.class, callBack);
	}
	
	/**
	 *  中奖信息
	 * @param activitIndex 活动id
	 * @param callBack
	 */
	public void getShakeWinners(String activitIndex,RequestCallBack<ShakeWinnersList> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "shake_winners");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("activity_index", activitIndex);
		
		postRequest(URL, params, ShakeWinnersList.class, callBack);
	}
	
	/**
	 * 奖品列表
	 * @param activitIndex 活动id
	 * @param callBack
	 */
	public void getShakePrize(String activitIndex,RequestCallBack<ShakePrize> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "shake_prize");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("activity_index", activitIndex);
		postRequest(URL, params, ShakePrize.class, callBack);
	}
	
	/**
	 * 获取活动列表
	 * @param type 活动类型，1转盘、4代表摇一摇、2代表大家猜、3代表刮刮乐，0代表全部
	 * @param cityId 用户定位城市ID
	 * @param countyId 用户定位区县id
	 * @param page 页码，默认0
	 */
	public void getActivityList(String type,String cityId,String countyId,int page,Class<ActivityList> cls,RequestCallBack<ActivityList> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "list_v2");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("type", type);
		params.addBodyParameter("city", cityId);
		params.addBodyParameter("county", countyId);
		params.addBodyParameter("page", Integer.toString(page));
		
		postRequest(URL, params, cls, callBack);
	}

	/**
	 * 新版本获取活动列表
     * @param callBack
     */
	public void getActivityListV3(String type,String cityId,String countyId,int page,RequestCallBack<JPLocation> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "list_v2");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("type", type);
		params.addBodyParameter("city", cityId);
		params.addBodyParameter("county", countyId);
		params.addBodyParameter("page", Integer.toString(page));
		Logger.i("2017-2-16:" + RequestOftenKey.getDeviceIndex(mContext));
		Logger.i("2017-2-16:" + RequestOftenKey.getToken(mContext));
		Logger.i("2017-2-16:" + type);
		Logger.i("2017-2-16:" + cityId);
		Logger.i("2017-2-16:" + countyId);
		Logger.i("2017-2-16:" + Integer.toString(page));

		postRequest(URL, params, JPLocation.class, callBack);
	}
	
	/**
	 * 参加大家猜活动
	 * @param activityIndex 活动唯一id号
	 * @param guessFigure 猜测的数字
	 * @param callBack
	 */
	public void getGuess(String activityIndex,String guessFigure,RequestCallBack<GuessingNumberEntity>callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c","guess");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("guess_figure",guessFigure);
		params.addBodyParameter("activity_index", activityIndex);
		postRequest(URL, params, GuessingNumberEntity.class, callBack);
	}
	
	/**
	 *获取用户自己猜过的数字列表
	 * @param activityIndex 活动唯一id号
	 * @param callBack
	 */
	public void getGuessFigureList(String activityIndex,RequestCallBack<GuessedNumberEntity>callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c","guess_figure_list");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("activity_index",activityIndex);
		postRequest(URL, params, GuessedNumberEntity.class, callBack);
	}
	
	/**
	 * 参加刮刮乐活动
	 * @param activityIndex 活动id
	 * @param callBack
	 */
	public void getScrapeActivityResult(String activityIndex,RequestCallBack<ScrapeEntity> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "Scrape");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("activity_index", activityIndex);
		
		postRequest(URL, params, ScrapeEntity.class, callBack);
	}
	
	/**
	 * 获取摇一摇活动的中奖记录
	 * @param activityIndex
	 * @param page
	 * @param callBack
	 */
	public void getShakeWinnerList(String activityIndex,int page,RequestCallBack<GetWinnerListResult> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "shake_winners_v2");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("page", Integer.toString(page));
		params.addBodyParameter("activity_index", activityIndex);
		
		postRequest(URL, params, GetWinnerListResult.class, callBack);
	}
	
	/**
	 * 获取刮刮卡活动的中奖记录
	 * @param activityIndex
	 * @param callBack
	 */
	public void getScrapeWinnerList(String activityIndex,int page,RequestCallBack<GetWinnerListResult> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "scrape_winners");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("page", Integer.toString(page));
		params.addBodyParameter("activity_index", activityIndex);
		
		postRequest(URL, params, GetWinnerListResult.class, callBack);
	}
	
	/**
	 * 获取大转盘活动的中奖记录
	 * @param activityIndex
	 * @param callBack
	 */
	public void getTurntableWinnerList(String activityIndex,int page,RequestCallBack<GetWinnerListResult> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "turntable_winners");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("page", Integer.toString(page));
		params.addBodyParameter("activity_index", activityIndex);
		
		postRequest(URL, params, GetWinnerListResult.class, callBack);
	}
	
	/**
	 * 获取中奖大家猜活动中奖用户
	 * @param activityIndex
	 * @param callBack
	 */
	public void getGuessWinner(String activityIndex,RequestCallBack<GuessWinnerInfo> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "guess_winner");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("activity_index", activityIndex);

		postRequest(URL, params, GuessWinnerInfo.class, callBack);
	}

	public void isShake (String activityIndex,String city,String county,double lat,double lng,RequestCallBack<BaseEntity> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "is_shake");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("activity_index", activityIndex);
		params.addBodyParameter("city", city);
		params.addBodyParameter("county", county);
		params.addBodyParameter("lat", String.valueOf(lat));
		params.addBodyParameter("lng", String.valueOf(lng));

		postRequest(URL, params, BaseEntity.class, callBack);
	}


	/**
	 * 签到中的大转盘
	 */
	public void signTurnTable(String type, RequestCallBack<JPTurnTableEntity> callBack){
		RequestParams params = new RequestParams();
		params.addBodyParameter("c", "daily_turntable_v3");
		params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
		params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
		params.addBodyParameter("play", type);

		postRequest(URL, params, JPTurnTableEntity.class, callBack);
	}
}
