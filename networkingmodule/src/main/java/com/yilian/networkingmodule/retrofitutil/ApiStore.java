package com.yilian.networkingmodule.retrofitutil;

import com.yilian.networkingmodule.entity.MerchantManagerListEntity;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ray_L_Pain on 2017/9/27 0027.
 * 接口商店
 */

public interface ApiStore {

    /**
     * 商品管理-商品列表
     */
    @POST("mall.php")
    Observable<MerchantManagerListEntity> getMerchantManagerList(@Query("c") String c, @Query("token") String token, @Query("device_index") String deviceIndex
    , @Query("page") String page, @Query("count") String count, @Query("keyword") String keyWord);





}
