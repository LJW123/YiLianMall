package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.KeyWord;
import com.yilian.mall.entity.NGotDetailEntity;
import com.yilian.mall.entity.NGotRecordEntity;
import com.yilian.mall.entity.OneBuyCategoryListEntity;
import com.yilian.mall.entity.OneBuyDetailsEntity;
import com.yilian.mall.entity.OneBuyEntity;
import com.yilian.mall.entity.OneBuyFromGoingToAnnouncedEntity;
import com.yilian.mall.entity.OneBuyGoodsListEntity;
import com.yilian.mall.entity.OneBuyJoinRecordAnnouncedEntity;
import com.yilian.mall.entity.OneBuyJoinRecordGoingEntity;
import com.yilian.mall.entity.OneBuyJoinRecordShowPrize;
import com.yilian.mall.entity.OneBuyNewestAnnouncedEntity;
import com.yilian.mall.entity.OneBuyParticipation;
import com.yilian.mall.entity.OneBuyRecommendEntity;
import com.yilian.mall.entity.OneBuyRotateAwards;
import com.yilian.mall.entity.OneBuyShowPrizeEntity;
import com.yilian.mall.entity.ReViewEntity;
import com.yilian.mall.entity.RotateImageList;
import com.yilian.mall.entity.ShareRecord64Entity;
import com.yilian.mall.entity.ShareRecordEntity;
import com.yilian.mall.entity.UserInfoEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;

/**
 * Created by lefenandroid on 16/5/24.
 */
public class OneBuyNetRequest extends BaseNetRequest {

    public  String URL;

    public OneBuyNetRequest(Context context) {
        super(context);
        URL= Ip.getURL(context)+"mall.php";
    }



    /**
     * 获取首页轮播图URl
     * @param callBack
     */
    public void getOneBuyBanner(RequestCallBack<RotateImageList> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_banner");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, RotateImageList.class, callBack);
    }

    /**
     * 获取幸运购 循环中奖信息
     * @param callBack
     */
    public void oneBuyRotateAwards(RequestCallBack<OneBuyRotateAwards> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/activity_snatch_news_notice");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, OneBuyRotateAwards.class, callBack);
    }
    /**
     * 晒单
     * @param activity_id
     * @param content
     * @param img
     * @param callBack
     */
    public void snatchShare(String activity_id,String content,String img,RequestCallBack<BaseEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_show");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activity_id);//活动期号
        params.addBodyParameter("content", content);//评价内容
        params.addBodyParameter("img", img);//逗号分隔
        postRequest(URL, params, BaseEntity.class, callBack);
    }
    /**
     *  获取 幸运购活动列表
     * @param local  0 本地 1全国
     * @param type 0 所有 1 1元分区 10 10元分区 100 100元分区 以此类推
     * @param status 1 进行中 2 待揭晓 3 已揭晓
     * @param sort 排序 人气 最新 进度 总须人数 已逗号间隔如("0,1,1,0"),0升序 1降序
     * @param page 分页
     * @param city 城市id
     * @param callBack
     */
    public void oneBuyGoodsList(String local, String type, String status, String sort,
                                int page, String city,String country, RequestCallBack<OneBuyGoodsListEntity> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("local", local);
        params.addBodyParameter("type", status);
        params.addBodyParameter("type_code", type);
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("city", city);
        params.addBodyParameter("country",country );
        params.addBodyParameter("count", "20");
        postRequest(URL, params, OneBuyGoodsListEntity.class, callBack);
    }



    /**
     * 获取 幸运购活动详情
     * @param id
     * @param page
     * @param callBack
     */
    public void loadOneBuyActivityDetails(String id,int page,int type,RequestCallBack<OneBuyDetailsEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("snatch_index", id);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", "20");
        params.addBodyParameter("type",String.valueOf(type));
        postRequest(URL, params, OneBuyDetailsEntity.class, callBack);

    }

    /**
     * 获取幸运购晒单列表
     * @param type 0:所有记录  1:我的夺宝晒单记录
     * @param page 页数
     * @param callBack
     */
    public void getOneBuyShowPrizeList(String type,int page,String snatchIndex,RequestCallBack<OneBuyShowPrizeEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_show_list");
        params.addBodyParameter("device_index",RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("count","20");
        params.addBodyParameter("type",type);
        params.addBodyParameter("snatch_index",snatchIndex);
        params.addBodyParameter("page",String.valueOf(page));
        postRequest(URL,params,OneBuyShowPrizeEntity.class,callBack);
    }

    /**
     * 获取幸运购晒单列表
     * @param type 0:所有记录  1:我的夺宝晒单记录
     * @param page 页数
     * @param callBack
     */
    public void getOneBuyJoinRecordShowPrizeList(String type,int page,RequestCallBack<OneBuyJoinRecordShowPrize> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_show_list");
        params.addBodyParameter("device_index",RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("count","20");
        params.addBodyParameter("type",type);
        params.addBodyParameter("page",String.valueOf(page));
        postRequest(URL,params,OneBuyJoinRecordShowPrize.class,callBack);
    }

    /**
     * 获取玩家参与幸运购记录
     * @param type 1：进行中   2：已揭晓  3：全部
     * @param uerId
     * @param page
     * @param callBack
     */
    public void getOneBuyJoinRecordList(String type,String uerId,int page, RequestCallBack<OneBuyJoinRecordGoingEntity> callBack){

        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_join_log");
        params.addBodyParameter("device_index",RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("count","20");
        params.addBodyParameter("type",type);
        params.addBodyParameter("page",String.valueOf(page));
        params.addBodyParameter("user_id",uerId);
        postRequest(URL,params,OneBuyJoinRecordGoingEntity.class,callBack);


    }
    /**
     * 获取玩家参与幸运购的已揭晓记录
     * @param type 1：进行中   2：已揭晓  3：全部
     * @param uerId
     * @param page
     * @param callBack
     */
    public void getOneBuyJoinRecordAnnouncedList(String type, String uerId, int page, RequestCallBack<OneBuyJoinRecordAnnouncedEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_join_log");
        params.addBodyParameter("device_index",RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        params.addBodyParameter("count","20");
        params.addBodyParameter("type",type);
        params.addBodyParameter("page",String.valueOf(page));
        params.addBodyParameter("user_id",uerId);
        postRequest(URL,params,OneBuyJoinRecordAnnouncedEntity.class,callBack);

    }

    /**
     * 往期回顾列表
     * @param activity_id 活动Id
     * @param page 分页
     * @param callBack
     */
    public void reViewList(String activity_id,int page,RequestCallBack<ReViewEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_list_record");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activity_id);
        params.addBodyParameter("page", page+"");
        params.addBodyParameter("count", 20+"");//一页加载数量

        postRequest(URL, params, ReViewEntity.class, callBack);
    }

    /**
     * 中奖记录
     * @param user_id //0 代表查看自己的中奖 其他情况代表查看用户
     * @param page
     * @param callBack
     */
    public void nGotRecord(String user_id,int page,RequestCallBack<NGotRecordEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_award_log");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("user_id", user_id);
        params.addBodyParameter("page", page+"");
        params.addBodyParameter("count", 20 + "");//一页加载数量
        postRequest(URL, params, NGotRecordEntity.class, callBack);
    }

    /**
     * 参与幸运购活动
     * @param callBack
     */
    public void participationOneBuy(String activityId, String count,RequestCallBack<OneBuyParticipation> callBack) {
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_order");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activityId);
        params.addBodyParameter("count", count);

        postRequest(URL, params, OneBuyParticipation.class, callBack);
    }

    /**
     * 夺宝中奖收货接口
     * @param activity_id
     * @param address_id
     * @param callBack
     */
    public void addressNet(String activity_id,String address_id,RequestCallBack<BaseEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_set_address");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activity_id);
        params.addBodyParameter("address_id", address_id);
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 确认收货
     * @param activity_id
     * @param callBack
     */
    public void confirm(String activity_id,RequestCallBack<BaseEntity> callBack ){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_confirm_goods");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activity_id);
        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 夺宝记录中用户信息
     * @param user_id
     * @param callBack
     */
    public void userInfo(String user_id,RequestCallBack<UserInfoEntity> callBack ){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_user_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("user_id", user_id);
        postRequest(URL, params, UserInfoEntity.class, callBack);
    }

    /**
     * 中奖详情
     * @param activity_id
     * @param callBack
     */
    public void nsnatchDetail(String activity_id,RequestCallBack<NGotDetailEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_award_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activity_id);
        postRequest(URL, params, NGotDetailEntity.class, callBack);
    }

    /**
     * 参与记录
     * @param type //1进行中 2 已揭晓 0全部
     * @param user_id //0 代表查看自己的中奖 其他情况代表查看用户
     * @param page
     * @param callBack
     */
    public void nJoinRecord(int type,String user_id,int page,RequestCallBack<NGotRecordEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_join_log");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type",type+"");
        params.addBodyParameter("user_id", user_id);
        params.addBodyParameter("page", page+"");
        params.addBodyParameter("count", 20 + "");//一页加载数量
        postRequest(URL, params, NGotRecordEntity.class, callBack);
    }

    /**
     * 晒单图片上传接口
     * @param file
     * @param callBack
     */
    public void uploadimg(String file,RequestCallBack<ShareRecordEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/upload_goods_img");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("file", file);//头像图片文件内容
        postRequest(URL, params, ShareRecordEntity.class, callBack);
    }

    /**
     *  base64方式上传图片
     * @param image_data //图片数据base64编码
     * @param callBack
     */
    public void upload64img(String image_data,RequestCallBack<ShareRecord64Entity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/upload_image_data");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("image_name", "");
        params.addBodyParameter("image_type", "image/png");
        params.addBodyParameter("image_size", image_data.length()+"");
        params.addBodyParameter("image_data", image_data);
        postRequest(URL, params, ShareRecord64Entity.class, callBack);
    }


    /**
     * 中奖详情
     * @param activity_id
     * @param callBack
     */
    public void nSnatchDetail(String activity_id,RequestCallBack<NGotDetailEntity> callBack){
        RequestParams params=new RequestParams();
        params.addBodyParameter("c","snatch/snatch_award_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("activity_id", activity_id);
        postRequest(URL, params, NGotDetailEntity.class, callBack);
    }

    /**
     * 推荐夺宝列表
     * @param callBack
     */
    public void getRecommendList(RequestCallBack<OneBuyRecommendEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_recommend_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", "0");
        params.addBodyParameter("count", "20");
        postRequest(URL,params, OneBuyRecommendEntity.class,callBack);
    }

    /**
     * 获取 热词
     * @param callBack
     */
    public void getHotWord(RequestCallBack<KeyWord> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_hot_word");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL,params, KeyWord.class,callBack);
        Logger.i("获取热词"+"device_index "+RequestOftenKey.getDeviceIndex(mContext)+"token"+RequestOftenKey.getToken(mContext));
    }

    /**
     * 搜索接口
     * @param keyWord 搜索关键字
     * @param callBack
     */
    public void search(String keyWord,RequestCallBack<OneBuyGoodsListEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_search");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", "0");
        params.addBodyParameter("key_word", keyWord);
        postRequest(URL,params, OneBuyGoodsListEntity.class,callBack);
    }

    /**
     * 获取从即将揭晓到已揭晓的数据
     * @param announcedTime
     * @param callBack
     */
    public void getDataFromGoingToAnnounced (String announcedTime,RequestCallBack<OneBuyFromGoingToAnnouncedEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_announce");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("snatch_announce_time", announcedTime);
        postRequest(URL,params, OneBuyFromGoingToAnnouncedEntity.class,callBack);
    }

    /**
     * 即将揭晓到揭晓过度的奖品开奖状态
     * @param snatchIssue
     * @param callBack
     */
    public void getStatus(String snatchIssue,RequestCallBack<BaseEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_poll");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("snatch_issue",snatchIssue);
        postRequest(URL,params,BaseEntity.class,callBack);
    }


    /**
     * 获取全局已揭晓列表
     * @param page
     * @param callBack
     */
    public void getNewestAnnouncedList(int page , RequestCallBack<OneBuyNewestAnnouncedEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_new_award");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count","20");
        postRequest(URL,params, OneBuyNewestAnnouncedEntity.class,callBack);
    }

    /**
     * 新版幸运购获取分类图标及轮播图
     * @param callBack
     */
    public void getOneBuyData(RequestCallBack<OneBuyEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatchindex");
        params.addBodyParameter("device_index",RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token",RequestOftenKey.getToken(mContext));
        postRequest(URL, params, OneBuyEntity.class, callBack);
    }

    /**
     * 分享幸运购中奖，获取奖励
     * @param activityId
     */
    public void oneBuyPrizeShareWxOrQZone(String activityId,RequestCallBack<BaseEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_share");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("snatch_index",activityId);
        postRequest(URL,params,BaseEntity.class,callBack);
    }

    /**
     * 获取幸运购分类列表数据
     * @param callBack
     */
    public void getOneBuyCategroyList(RequestCallBack<OneBuyCategoryListEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_class");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL,params,OneBuyCategoryListEntity.class,callBack);

    }

    /**
     * 根据分类获取商品列表信息
     * @param page
     * @param type
     * @param callBack
     */
    public void getOneBuyCategory(int page,String type,RequestCallBack<OneBuyGoodsListEntity> callBack){
        RequestParams params = new RequestParams();
        params.addBodyParameter("c","snatch/snatch_classify");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("page",String.valueOf(page));
        params.addBodyParameter("count","20");
        params.addBodyParameter("type",type);
        postRequest(URL,params,OneBuyGoodsListEntity.class,callBack);
    }
}
