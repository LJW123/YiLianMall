package com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.yilian.mall.clean.BaseRepository;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * @author Created by  on 2018/5/23.
 */

public class SnGoodsDetailInfoRepositoryImpl extends BaseRepository implements SnGoodsDetailInfoRepository {
    public HashMap<String, String> params = new HashMap<>();
    private Map<String, String> mParams;

    public SnGoodsDetailInfoRepositoryImpl(Context context) {
        super(context);
    }

    public void setParams(Map<String, String> params) {
        mParams = params;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Observable<SnGoodsDetailInfoEntity> getSnGoodsDetailInfo() {
        String sku = params.get("sku");
        Logger.i("JD商品详情 sku:" + sku);
        if (mContext==null) {
            throw new IllegalArgumentException("context can not be null");
        }
        return RetrofitUtils3.getRetrofitService(mContext).getSuningGoodsInfo(mParams);

    }


}
