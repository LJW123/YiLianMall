package com.yilian.mall.http;

import android.content.Context;

import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.FlagshipList;
import com.yilian.mall.entity.JPFlagshipEntity;
import com.yilian.mall.entity.LeFenHome;
import com.yilian.mall.utils.RequestOftenKey;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;

/**
 * Created by Ray_L_Pain on 2016/10/19 0019.
 */

public class LeFenHomeRequest extends BaseNetRequest {

    private String URL;

    public LeFenHomeRequest(Context context) {
        super(context);
        URL = Ip.getURL(context) + "nearby.php";
    }

    /**
     * 获取乐分之家
     *
     * @param type     0兑换中心详情 1分店
     * @param id       兑换中心 id或者分店id
     * @param page
     * @param callBack
     */
    public void getLeFenHomeMsg(String type, String id, String page, RequestCallBack<LeFenHome> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "filiale_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", type);
        params.addBodyParameter("id", id);
        params.addBodyParameter("page", page);
        params.addBodyParameter("count", "20");
        Logger.i("请求参数：type:" + type + "  id:" + id);
        postRequest(URL, params, LeFenHome.class, callBack);
    }

    /**
     * 获取本地旗舰店列表
     *
     * @param id
     * @param callBack
     */
    public void getFlagshipList(String type, String id, int page, int count, RequestCallBack<FlagshipList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "supplier_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("type", type);
        params.addBodyParameter("id", id);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("count", String.valueOf(count));
        Logger.i("本地旗舰店列表数据请求参数： type:" + type + "  id:" + id + "device  " + RequestOftenKey.getDeviceIndex(mContext) + "  token " + RequestOftenKey.getToken(mContext) + " page " + page);
        postRequest(URL, params, FlagshipList.class, callBack);
    }

    /**
     * 获取旗舰店详情信息
     */
    public void getFlagship(String id, RequestCallBack<JPFlagshipEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "supplier_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("id", id);
        postRequest(URL, params, JPFlagshipEntity.class, callBack);
        Logger.i("请求旗舰店详情参数：" + RequestOftenKey.getDeviceIndex(mContext));
        Logger.i("请求旗舰店详情参数：" + RequestOftenKey.getToken(mContext));
        Logger.i("请求旗舰店详情参数：" + id);
    }
}
