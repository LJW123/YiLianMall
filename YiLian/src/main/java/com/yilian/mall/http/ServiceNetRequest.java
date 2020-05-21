package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.Feedback;
import com.yilian.mall.entity.FqaActivityArrayList;
import com.yilian.mall.entity.GetPrizeVoucherListResult;
import com.yilian.mall.entity.Get_newest_version_info;
import com.yilian.mall.entity.LexinNewsListBean;
import com.yilian.mall.entity.ObtainIntegral;
import com.yilian.mall.entity.PrizeVoucherDetailBean;
import com.yilian.mall.entity.RecordExchangeJifenListBean;
import com.yilian.mall.entity.RecordObtainJifenListBean;
import com.yilian.mall.entity.RecordPaticipateActivityListBean;
import com.yilian.mall.entity.RotateImageList;
import com.yilian.mall.entity.UseVoucherEntity;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 基础系统服务相关请求
 *
 * @author Administrator
 */
public class ServiceNetRequest extends BaseNetRequest {
    public  String URL;

    public ServiceNetRequest(Context mContext) {
        super(mContext);
        URL = Ip.getURL(mContext) + "service.php";
        // TODO Auto-generated constructor stub
    }

    /**
     * 获取新版本功能说明
     *
     * @param callBack
     */
    public void getNewVersionInfo(RequestCallBack<Get_newest_version_info> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_newest_version_info");
        params.addBodyParameter("os_type", "0");

        postRequest(URL, params, Get_newest_version_info.class, callBack);
    }

    /**
     * 获取反馈类型列表
     *
     * @param cls
     * @param callBack
     */
    public void getFeedBackTypeList(Class<Feedback> cls, RequestCallBack<Feedback> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_feedback_type");
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 提交意见反馈
     * device_index 客户端运行设备的唯一自增编号
     * token 客户端登录凭证(token值+盐做数学运算)
     *
     * @param type     反馈类型
     * @param content  反馈内容
     * @param contact  联系方式
     * @param cls
     * @param callBack
     */
    public void submitFeedback(String type, String content, String contact, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "submit_feedback");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));//客户端登录凭证(token值+盐做数学运算)
        params.addBodyParameter("type", type);//反馈类型
        params.addBodyParameter("content", content);//反馈内容
        params.addBodyParameter("contact", contact);//联系方式

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 提交意见反馈
     * device_index 客户端运行设备的唯一自增编号
     * token 客户端登录凭证(token值+盐做数学运算)
     *
     * @param type     反馈类型
     * @param content  反馈内容
     * @param contact  联系方式
     * @param cls
     * @param callBack
     */
    public void submitFeedback_v2(String type, String content, String contact, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "submit_feedback_v2");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));//客户端运行设备的唯一自增编号
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));//客户端登录凭证(token值+盐做数学运算)
        params.addBodyParameter("type", type);//反馈类型
        params.addBodyParameter("content", content);//反馈内容
        params.addBodyParameter("contact", contact);//联系方式

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 获取首页轮播图URl
     *
     * @param deviceIndex 设备编号
     * @param city        用户定位城市ID，必须是具体的城市ID
     * @param county      用户定位区县id,默认0
     * @param cls
     * @param callBack
     */
    public void getHomePageRotationImages(String deviceIndex, String city, String county, Class<RotateImageList> cls, RequestCallBack<RotateImageList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "view_pager_v2");
        params.addBodyParameter("device_index", deviceIndex);//客户端运行设备的唯一自增编号
        params.addBodyParameter("city", city);
        params.addBodyParameter("county", county);//客户端登录凭证(token值+盐做数学运算)

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 中奖凭证列表
     * deviceIndex
     * token
     *
     * @param cls
     * @param callBack
     */
    public void getPrizeVoucherList(Class<GetPrizeVoucherListResult> cls, RequestCallBack<GetPrizeVoucherListResult> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "prize_voucher_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 获取单个中奖凭证信息
     *
     * @param deviceIndex  设备编号
     * @param token        用户登录凭证(token值+盐做数学运算)
     * @param prizeVoucher 奖品凭证编号
     * @param cls
     * @param callBack
     */
    public void getPrizeVoucherInfo(String deviceIndex, String token, String prizeVoucher, Class<PrizeVoucherDetailBean> cls, RequestCallBack<PrizeVoucherDetailBean> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "prize_voucher_info");
        params.addBodyParameter("device_index", deviceIndex);
        params.addBodyParameter("token", token);
        params.addBodyParameter("prize_voucher", prizeVoucher);

        postRequest(URL, params, cls, callBack);
    }

    public void useVoucher(String voucherIndex, RequestCallBack<UseVoucherEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "prize_use_page");
        params.addBodyParameter("voucher_index", voucherIndex);
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, UseVoucherEntity.class, callBack);
    }

    /**
     * 获取兑换中心的赠送奖券
     * deviceIndex
     * token
     *
     * @param givenIndex 赠送奖券活动编号
     * @param filialeId  兑换中心ID
     * @param cls
     * @param callBack
     */
    public void getFilialeObtainScore(String givenIndex, String filialeId, Class<ObtainIntegral> cls, RequestCallBack<ObtainIntegral> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "obtain_integral");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("given_index", givenIndex);
        params.addBodyParameter("filiale_id", filialeId);
        postRequest(URL, params, cls, callBack);
    }

    public void debugLog(File file, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "debug_log");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("log_title", "log");
        params.addBodyParameter("log_content", "1");
        params.addBodyParameter("log_platform", "安卓");

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 常见问题
     * deviceIndex 设备编号
     *
     * @param cls
     * @param callBack
     */
    public void getFQA(Class<FqaActivityArrayList> cls, RequestCallBack<FqaActivityArrayList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "FAQ_list");
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 乐分获得记录
     *
     * @param cls
     * @param callBack
     */
    public void getJifenobtainRecordList(Class<RecordObtainJifenListBean> cls,
                                         RequestCallBack<RecordObtainJifenListBean> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "obtain_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 乐分兑换记录
     *
     * @param cls
     * @param callBack
     */
    public void getJifenExchangeRecordList(Class<RecordExchangeJifenListBean> cls,
                                           RequestCallBack<RecordExchangeJifenListBean> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "exchange_record");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 活动参与记录
     *
     * @param cls
     * @param callBack
     */
    public void getPaticipateActivityRecordList(Class<RecordPaticipateActivityListBean> cls,
                                                RequestCallBack<RecordPaticipateActivityListBean> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "participation_record");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 新闻列表
     *
     * @param cls
     * @param cityId   城市id
     * @param countyId 区县id
     * @param page
     * @param callBack
     */
    public void getNewsList(Class<LexinNewsListBean> cls, int page, String token, String cityId, String countyId, RequestCallBack<LexinNewsListBean> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "news_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", token);
        params.addBodyParameter("city", cityId);
        params.addBodyParameter("county", countyId);
        params.addBodyParameter("page", Integer.toString(page));
        postRequest(URL, params, cls, callBack);

    }

    /**
     * 获取新闻的html内容
     *
     * @param news_index 新闻数据id
     * @param callBack
     */
    public void getNewsContent(String news_index, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "news_content");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("news_index", news_index);
        postRequest(URL, params, callBack);

    }
}
