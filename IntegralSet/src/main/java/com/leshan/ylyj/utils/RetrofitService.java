package com.leshan.ylyj.utils;/**
 * Created by  on 2017/3/3 0003.
 */

import com.leshan.ylyj.view.activity.bankmodel.JRegionModel;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.UploadImageEnity;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rxfamily.entity.JBankModel;

/**
 * Created by  on 2017/3/3 0003.
 */
public interface RetrofitService {

    @POST("account.php")
    Call<BaseEntity> bindCard(@Query("c") String c, @Query("token") String token, @Query("device_index") String deviceIndex, @Query("card_bank") String bankName, @Query("card_holder") String userName,
                              @Query("card_number") String cardNum, @Query("card_branch") String cardBranch, @Query("province_id") String provinceId, @Query("city_id") String cityId,
                              @Query("county_id") String countyId, @Query("phone") String phone, @Query("sms_code") String code);

    //获取银行列表
    @POST("account.php")
    Call<JBankModel> getBankList(@Query("c") String c);

    //获取地区列表
    @POST("account.php")
    Call<JRegionModel> getRegion(@Query("c") String c);

    //获取班定银行卡时的短信验证码
    @POST("account.php")
    Call<BaseEntity> getSmsCode(@Query("c") String c, @Query("token") String token, @Query("device_index") String device_index, @Query("phone") String phone, @Query("verify_type") String verify_type,
                                @Query("verify_code") String verify_code, @Query("voice") String voice, @Query("type") String type);


    /**
     * 绑定对工卡-上传资质照片
     *
     * @param image
     * @return
     */
    @Multipart
    @POST("mall.php")
    Call<UploadImageEnity> uploadFilePublic(@Query("c") String c, @Query("token") String token, @Query("device_index") String deviceIndex, @Part MultipartBody.Part image);
}