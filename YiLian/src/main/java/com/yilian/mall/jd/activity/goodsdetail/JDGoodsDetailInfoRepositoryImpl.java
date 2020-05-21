package com.yilian.mall.jd.activity.goodsdetail;

import com.orhanobut.logger.Logger;
import com.yilian.mall.MyApplication;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.HashMap;

import rx.Observable;

/**
 * @author Created by  on 2018/5/23.
 */

public class JDGoodsDetailInfoRepositoryImpl implements JDGoodsDetailInfoRepository {
    public HashMap<String, String> params = new HashMap<>();
    public String paramsKey[] = new String[]{"sku", "lat", "lng","jdType"};

    public void setParams(String sku, String lat, String lng,String jdType) {
        Logger.i("setParams: sku:"+sku+" lat:"+lat +"  lng:"+lng+"  jdType:"+jdType);
        params.put(paramsKey[0], sku);
        params.put(paramsKey[1], lat);
        params.put(paramsKey[2], lng);
        params.put(paramsKey[3], jdType);
    }

    @Override
    public Observable<JDGoodsDetailInfoEntity> getJDGoodsDetailInfo() {
        String sku = params.get("sku");
        Logger.i("JD商品详情 sku:" + sku);
        String sku1 = params.get(paramsKey[0]);

        return RetrofitUtils3.getRetrofitService(MyApplication.getContextObject())
                .getJDGoodsDetailInfo("jd_goods/jd_goods_detail",
                        sku1, params.get(paramsKey[1]), params.get(paramsKey[2]),params.get(paramsKey[3]));

    }
}
