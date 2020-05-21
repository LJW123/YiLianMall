package com.leshan.ylyj.api;


import android.support.annotation.Nullable;

import com.yilian.mylibrary.Constants;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
import rxfamily.entity.AddDriverLicenseEntity;
import rxfamily.entity.AddDrivingEntity;
import rxfamily.entity.AuthInfoEntity;
import rxfamily.entity.AuthenticationInfoEntity;
import rxfamily.entity.BankInfoEntity;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.BindPersonBankCardSuccessEntity;
import rxfamily.entity.BranchBankEntity;
import rxfamily.entity.CardPrivateDetailsInfo;
import rxfamily.entity.CardPublicDetailsInfo;
import rxfamily.entity.CheckBankCard4ElementEntity;
import rxfamily.entity.CommitEmailEntity;
import rxfamily.entity.CreditFootmarkEntity;
import rxfamily.entity.CreditGuardEntity;
import rxfamily.entity.CreditProtocolsEntity;
import rxfamily.entity.CreditRatingEntity;
import rxfamily.entity.DeleteDrivingInfo;
import rxfamily.entity.DeleteRecordEntity;
import rxfamily.entity.DimensionalityEntity;
import rxfamily.entity.DonateEntity;
import rxfamily.entity.EducationRegionEntity;
import rxfamily.entity.HealthFriendRobAllCountBean;
import rxfamily.entity.HealthFriendRobListBean;
import rxfamily.entity.HealthFruitListBean;
import rxfamily.entity.HealthMyFamilyBean;
import rxfamily.entity.HelpOtherDetailBean;
import rxfamily.entity.HelpOtherFindBAIBean;
import rxfamily.entity.InquireRecordEntity;
import rxfamily.entity.IntegralRegionEntity;
import rxfamily.entity.MyCreditEntity;
import rxfamily.entity.MyDriverLicenseEntity;
import rxfamily.entity.MyEmailEntity;
import rxfamily.entity.MyEmailInfoEntity;
import rxfamily.entity.MyHealthFruitBean;
import rxfamily.entity.MyHealthListBean;
import rxfamily.entity.MyHealthTopMsgBean;
import rxfamily.entity.MyPurseEntity;
import rxfamily.entity.PublicBenefitHomeBageEntity;
import rxfamily.entity.PublicCardDetailsEntity;
import rxfamily.entity.PublicCardDetailsEntityV2;
import rxfamily.entity.QueryDriverLicenseEntity;
import rxfamily.entity.QueryDrivingEntity;
import rxfamily.entity.SchoolNameEntity;
import rxfamily.entity.ShareCreditEntity;
import rxfamily.entity.ShareSucceedEntity;
import rxfamily.entity.ShortcutDonateEntity;
import rxfamily.entity.SubmitRecordEntity;
import rxfamily.entity.TransactionRecordEntity;
import rxfamily.entity.TwoTypeEntity;
import rxfamily.entity.UploadDriverImgEntity;
import rxfamily.entity.UploadDrivingImgEntity;
import rxfamily.entity.UploadInformationEntity;
import rxfamily.entity.WeatherBean;
import rxfamily.entity.WelfareExpericeEntity;
import rxfamily.entity.WelfareGrowthSystem;
import rxfamily.entity.WelfareListEntity;
import rxfamily.entity.WelfareLoveEntity;
import rxfamily.entity.WelfareLoveOperationEntity;
import rxfamily.entity.WelfarePersonInfoEntity;
import rxfamily.entity.WelfareShareDataEntivity;


/**
 * 存放所有的网络请求
 *
 * @author ASUS
 */

public interface APIService {


    String C = "c";

    @GET("mall.php")
    Observable<BaseEntity> getMemberGroupNotice(@Query(C) String c);

    /**
     * 我的奖励
     *
     * @return
     */
    @GET("account.php")
    Observable<rxfamily.entity.MyBalanceEntity> getMoneyData(@Query(C) String c);

    /**
     * @return
     * @net 获取信用首页信息
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<MyCreditEntity> getHomePage(@Url String url);

    /**
     * 修改信用协议同意与否
     *
     * @return
     * @net 获取信用首页信息
     * 当BASE_URL发生变化时使用注解
     */
    @POST
    Observable<CreditProtocolsEntity> setAgreement(@Url String url, @Query("isAgree") String isAgree);

    /**
     * 发送验证码至email
     *
     * @param url
     * @return
     */
    @GET
    Observable<MyEmailEntity> getEmailCode(@Url String url, @Query("email") String email);

    /**
     * 添加email提交保存
     *
     * @param url
     * @param email
     * @param checkCode
     * @return
     */
    @PUT
    Observable<CommitEmailEntity> addEmail(@Url String url, @Query("email") String email, @Query("checkCode") String checkCode);

    /**
     * 查询邮箱信息
     *
     * @param url
     * @return
     */
    @GET
    Observable<MyEmailInfoEntity> queryEmailInfo(@Url String url);

    /**
     * 删除邮箱
     *
     * @param url
     * @return
     */
    @DELETE
    Observable<MyEmailEntity> deleteEmail(@Url String url);

    /**
     * 上传驾驶证图片
     *
     * @param url
     * @return
     */
    @Multipart
    @POST
    Observable<UploadDriverImgEntity> uploadDriverImg(@Url String url, @Part MultipartBody.Part image);

    /**
     * 添加驾驶证
     */
    @FormUrlEncoded
    @POST
    Observable<AddDriverLicenseEntity> addDriver(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 查询驾驶证信息
     *
     * @param url
     * @return
     */

    @GET
    Observable<QueryDriverLicenseEntity> queryDriverLicenseInfo(@Url String url);

    /**
     * 确认修改驾驶证
     */
    @FormUrlEncoded
    @POST
    Observable<MyDriverLicenseEntity> updateDriver(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 删除驾驶证信息
     */
    @DELETE
    Observable<BaseEntity> deleteDriverLincenseInfo(@Url String url, @Query("id") String id);

    /**
     * 添加行驶证信息
     * carNo 车牌号             name 姓名                sex  性别                   nationality 国籍                                 address 地址                  birthday 出生日期                      initialDate 初次领证日期                   brandType 车牌型号                   registerDate 注册日期            issueDate发证日期                           engineNo 发动机编号                vin 车辆识别代号           positive 证件照片主页          back 证件照片副页
     */
    @POST
    Observable<AddDriverLicenseEntity> addDrivingInfo(@Url String url, @Query("carNo") String carNo, @Query("name") String name, @Query("sex") String sex, @Query("nationality") String nationality, @Query("address") String address, @Query("birthday") String birthday, @Query("initialDate") String initialDate, @Query("brandType") String brandType, @Query("registerDate") String registerDate, @Query("issueDate") String issueDate, @Query("engineNo") String engineNo, @Query("vin") String vin, @Query("positive") String positive, @Query("back") String back);

    /**
     * 查询行驶证信息
     */
    @GET
    Observable<QueryDrivingEntity> queryDrivingLicenseInfo(@Url String url);

    /**
     * 确认行驶证信息
     */
    @FormUrlEncoded
    @POST
    Observable<MyDriverLicenseEntity> updateDriving(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 删除行驶证信息
     */
    @DELETE
    Observable<DeleteDrivingInfo> deleteDrivingInfo(@Url String url, @Query("id") String id);

    /**
     * @return
     * @net 获取了解信用的信用等级
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<CreditRatingEntity> getCreditRating(@Url String url);

    /**
     * @return
     * @net 获取学历地区列表
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<EducationRegionEntity> getEducationRegion(@Url String url);

    /**
     * @return
     * @net 获取学历院校地区列表
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<SchoolNameEntity> getSchoolName(@Url String url, @Query("areaCodeProvice") String areaCodeProvice);

    /**
     * @return
     * @net 获取提交学历
     * 当BASE_URL发生变化时使用注解
     */
    @FormUrlEncoded
    @POST
    Observable<SubmitRecordEntity> getSubmitRecord(@Url String url, @Query("areaName") String areaName, @Query("areaId") String areaId, @Query("collegeName") String collegeName, @Query("collegeId") String collegeId, @Query("currentStatus") int currentStatus, @Query("education") int education, @Query("enrolTime") String enrolTime, @Query("graduateTime") String graduateTime);

    @FormUrlEncoded
    @POST
    Observable<SubmitRecordEntity> getSubmitRecords(@Url String url, @FieldMap Map<String, String> map);

    /**
     * @return
     * @net 查询学历
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<InquireRecordEntity> getInquireRecord(@Url String url);

    /**
     * @return
     * @net 查询学历
     * 当BASE_URL发生变化时使用注解
     */
    @DELETE
    Observable<DeleteRecordEntity> getDeleteRecord(@Url String url, @Query("id") String id);

    /**
     * @return
     * @net 查询信用纬度
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<DimensionalityEntity> getDimensionality(@Url String url);

    /**
     * @return
     * @net 查询信用分享主题列表
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<ShareCreditEntity> getShareCredit(@Url String url);

    /**
     * @return
     * @net 查询信用分享成功回调
     * 当BASE_URL发生变化时使用注解
     */
    @POST
    Observable<ShareSucceedEntity> getShareSucceed(@Url String url, @Query("themeID") String themeID, @Query("credit") String credit, @Query("shareApp") String shareApp);

    /**
     * @return
     * @net 信用个人信息查询上传信息
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<UploadInformationEntity> getUploadInformation(@Url String url);

    /**
     * @return
     * @net 查询信用界面公益项目首页列表
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<PublicBenefitHomeBageEntity> getPublicBenefitHomeBage(@Url String url, @Query("page") String page, @Query("count") String count);

    /**
     * @return
     * @net 查询信用界面公益项目快捷捐赠弹窗
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<ShortcutDonateEntity> getShortcutDonate(@Url String url, @Query("typeVal") String typeVal);

    /**
     * @return
     * @net 查询信用界面公益项目快捷捐赠弹窗（点击捐赠）
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<DonateEntity> getDonate(@Url String url, @Query("project_id") String project_id, @Query("donation_integral") String donation_integral, @Query("is_anonymous") String is_anonymous);

    /**
     * 信用管理 信用守护
     */
    @GET("mall.php")
    Observable<CreditGuardEntity> getCreditGuard(@Query(C) String c);

    /**
     * 信用管理 ，信用足迹
     */
    @GET
    Observable<CreditFootmarkEntity> getCreditFootmark(@Url String url, @Query("page") String page, @Query("count") String count);

    /**
     * 获取公益列表接口
     *
     * @param
     * @return
     */
    @GET
    Observable<WelfareListEntity> getWelfareList(@Url String url, @Query("count") int count, @Query("page") int pager);

    /**
     * @return
     * @net 获取公益爱心数量
     * 当BASE_URL发生变化时使用注解
     */
    @GET
    Observable<WelfareLoveEntity> getWelfareLoveNum(@Url String url);


    /**
     * 获取公益个人详情
     *
     * @return
     */
    @GET
    Observable<WelfarePersonInfoEntity> getWelfarePersonalInfo(@Url String url);


    /**
     * 获取公益个人详情页面经验值
     *
     * @return
     */
    @GET
    Observable<WelfareExpericeEntity> getWelfareExperience(@Url String url);

    /**
     * 获取公益成长体系
     *
     * @return
     */
    @GET
    Observable<WelfareGrowthSystem> getWelfareGrowthSystem(@Url String url);

    /**
     * 获取公益如何获取经验值
     *
     * @return
     */
    @GET
    Observable<WelfareLoveOperationEntity> getWelfareOper(@Url String url);

    /**
     * 提交公益留言
     *
     * @param url
     * @param id
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<BaseEntity> submitWelfareLoveMsg(@Url String url, @Field("RECORD_ID") String recordId, @Field("PROJECT_ID") String id, @Field("CONTENT") String content);

    /**
     * 获取公益分享的内容
     *
     * @return
     */
    @GET
    Observable<WelfareShareDataEntivity> getWelfareShareData(@Url String url, @Query("project_id") String proId);

    /**
     * 分享成功之后调用
     *
     * @param url
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<BaseEntity> getWelfareShareResult(@Url String url, @FieldMap Map<String, String> id);

    /**
     * 健康-我的健康
     */
    @GET
    Observable<MyHealthTopMsgBean> getMyHealthTopMsg(@Url String url);


    /**
     * 健康-互助列表
     */
    @GET
    Observable<MyHealthListBean> getMyHealthList(@Url String url, @Query("page") String page, @Query("count") String count);

    /**
     * 获取天气信息
     */
    @GET
    Observable<WeatherBean> getWeather(@Url String url, @Query("location") String location);

    /**
     * 健康-健康果-数量
     */
    @GET
    Observable<MyHealthFruitBean> getHealthFruitCount(@Url String url);

    /**
     * 集分宝首页数据
     */
    @GET("mall.php")
    Observable<rxfamily.entity.IntegralTreasureEntity> getIntegralTreasureData(@Query(C) String c);


    /**
     * 设置自动转入
     */
    @GET("mall.php")
    Observable<rxfamily.entity.BaseEntity> setRetainedIntegra(@Query(C) String c, @Query("type") String type, @Query("amount") String amount);

    /**
     * 明细
     */
    @GET("mall.php")
    Observable<rxfamily.entity.IntegralRecordEntity> getRecord(@Query(C) String c, @Query("page") String page, @Query("count") String count, @Query("date") String date, @Query("type") String type);

    /**
     * 累计兑换明细记录
     */
    @GET("mall.php")
    Observable<rxfamily.entity.IntegralExchangeRecordEntity> getExchangeRecord(@Query(C) String c, @Query("page") String page, @Query("count") String count, @Query("date") String date);

    /**
     * 转入
     */
    @GET("mall.php")
    Observable<rxfamily.entity.IntegralStatusEntity> setShiftTo(@Query(C) String c, @Query("amount") String amount);

    /**
     * 转出
     */
    @GET("mall.php")
    Observable<rxfamily.entity.IntegralStatusEntity> setTurnOut(@Query(C) String c, @Query("amount") String amount);

    /**
     * 7天奖券指数
     */
    @GET("mall.php")
    Observable<rxfamily.entity.ExchangeIndexRecordEntity> getExchangeIndexRecord(@Query(C) String c);

    /**
     * 健康-健康果-动态列表
     */
    @GET
    Observable<HealthFruitListBean> getHealthFruitList(@Url String url, @Query("page") String page, @Query("count") String count);

    /**
     * 健康-健康果-兑换
     */
    @POST
    Observable<BaseEntity> exchangeFruit(@Url String url, @Query("apples") String apples);

    /**
     * 健康-好友抢果-已抢总数
     */
    @GET
    Observable<HealthFriendRobAllCountBean> getFriendRobAllCount(@Url String url);

    /**
     * 健康-好友抢果-可抢列表
     */
    @GET
    Observable<HealthFriendRobListBean> getFriendRobList(@Url String url, @Query("page") String page, @Query("count") String count);

    /**
     * 健康-好友抢果-抢
     */
    @GET
    Observable<BaseEntity> healthRobFriend(@Url String url, @Query("friend_id") String friendId, @Query("apples") String apples);


    /**
     * 健康-互助计划-详情
     */
    @GET
    Observable<HelpOtherDetailBean> helpOtherDetail(@Url String url, @Query("projectId") String projectId);

    /**
     * 健康-互助计划-根据姓名查询生日和身份证号码
     */
    @GET
    Observable<HelpOtherFindBAIBean> helpOtherFindBAI(@Url String url, @Query("name") String name);

    /**
     * 健康-互助计划-加入
     */
    @POST
    Observable<BaseEntity> helpOtherJoin(@Url String url, @Query("projectId") String projectId, @Nullable @Query("joinNum") String num, @Query("TAG_ID") String tagId, @Nullable @Query("name") String name,
                                         @Nullable @Query("idNum") String idNum, @Nullable @Query("birthday") String birthday, @Nullable @Query("joinId") String joinId);

    /**
     * 健康-我的家庭-列表
     */
    @POST
    Observable<HealthMyFamilyBean> getMyFamilyList(@Url String url);


    /**
     * 校验银行卡信息
     */
    @GET("mall.php")
    Observable<BankInfoEntity> addBankInfo(@Query(C) String c, @Query("card_number") String card);


    /**
     * 上传资质照片
     */
    @POST("mall.php")
    Observable<MyPurseEntity> upLoadPic(@Query(C) String c);


    /**
     * 对公卡详情
     */
    @POST("mall.php")
    Observable<PublicCardDetailsEntityV2> getPublicDetailsInfo(@Query(C) String c, @Query("card_index") String card_index);

    /**
     * 个人银行卡详情
     *
     * @param url
     * @param cardIndex
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<CardPrivateDetailsInfo> getPrivateCardDetailsInfo(@Url String url, @Field("card_index") String cardIndex);

    /**
     * 解除银行卡
     *
     * @param ur
     * @param card_index
     * @return
     */
    @GET
    Observable<BaseEntity> bankCardRelieved(@Url String ur, @Query("card_index") String card_index);

    /**
     * 获取对公卡详细信息
     *
     * @param url
     * @param cardIndex
     * @return
     */

    @GET
    Observable<CardPublicDetailsInfo> getPublicCardDetailsInfo(@Url String url, @Query("card_index") String cardIndex);


    /**
     * 提现银行卡列表
     */
    @POST("mall.php")
    Observable<MyPurseEntity> withDrawalCards(@Query(C) String c);

    /**
     * 提现默认银行卡列表
     */
    @POST("mall.php")
    Observable<MyPurseEntity> withDrawalDefaultCards(@Query(C) String c);


    /**
     * 我的银行卡
     */
    @POST("mall.php")
    Observable<BaseEntity> getCards(@Query(C) String c);


    /**
     * 我的钱包
     */
    @POST("mall.php")
    Observable<MyPurseEntity> getMyPurseInfo(@Query(C) String c);

    /**
     * 交易明细
     */
    @POST("mall.php")
    Observable<TransactionRecordEntity> getTransactionRecordPresenter(@Query(C) String c, @Query("page") String page, @Query("count") String count, @Query("date") String date);

    /**
     * 发送短信默认接口
     */
    @POST("mall.php")
    Observable<BaseEntity> sendSMS(@Query(C) String c, @Query("phone") String phone, @Query("verify_type") String verify_type, @Query("type") String type, @Query("voice") String voice, @Query("verify_code") String verify_code);

    /**
     * 验证验证码
     */
    @POST("mall.php")
    Observable<BaseEntity> checkCode(@Query(C) String c, @Query("phone") String phone, @Query("SMS_code") String SMS_code);

    /**
     * 绑定私有卡
     *
     * @param c
     * @param bankCardNumber
     * @param phone
     * @param provinceId
     * @param cityId
     * @param service_phone  客服电话 根据卡号查询银行卡信息接口返回的字段
     * @param branchBankName
     * @return
     */
    @GET("mall.php")
    Observable<BindPersonBankCardSuccessEntity> addPersonalBankCardsInfo(@Query(C) String c, @Query("card_number") String bankCardNumber,
                                                                         @Query("obligate_phone") String phone, @Query("province") String provinceId,
                                                                         @Query("city") String cityId, @Query("service_phone") String service_phone,
                                                                         @Query("branch_bank") String branchBankName, @Query("sms_code") String smsCode);

    @GET("mall.php")
    Observable<BindPersonBankCardSuccessEntity> addPersonalBankCardsInfo(@QueryMap Map<String, String> map);

    /**
     * 可支持的银行卡列表
     */
    @POST("mall.php")
    Observable<TwoTypeEntity> getSupportCards(@Query(C) String c, @Query("type") String type);

    /**
     * 解绑银行卡
     */
    @POST("mall.php")
    Observable<BaseEntity> unBundlingCard(@Query(C) String c, @Query("card_index") String card_index);

    /**
     * 绑定对公卡
     */
    @GET("mall.php")
    Observable<BaseEntity> addPublicBankCardsInfo(@Query(C) String c, @Query("card_number") String card, @Query("bank_name") String bank_name, @Query("obligate_phone") String obligate_phone, @Query("branch_bank") String branch_bank, @Query("card_holder") String card_holder, @Query("tax_code") String tax_code, @Query("province") String province, @Query("city") String city, @Query("company_address") String company_address, @Query("cert_image") String cert_image, @Query("sms_code") String sms_code,@Query("card_index")String cardIndex);

    /**
     * 识别行驶证
     *
     * @param url
     * @param body
     * @return
     */
    @Headers({"Authorization: APPCODE " + Constants.OCR_VEHICLE,
            "Content-Type:application/json; charset=UTF-8"})
    @POST
    Observable<ResponseBody> getDistinguishDrivingLicense(@Url String url, @Body RequestBody body);

    /**
     * 上传行驶证
     *
     * @param url
     * @return
     */
    @Multipart
    @POST
    Observable<UploadDrivingImgEntity> uploadDrivingCertificates(@Url String url, @Part MultipartBody.Part certificates);

    /**
     * 添加行驶证
     *
     * @param url
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<AddDrivingEntity> addDriving(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 认证信息回显
     *
     * @return
     */
    @GET("mall.php")
    Observable<AuthenticationInfoEntity> getAuthenticationInfoVisiable(@Query(C) String c);

    /**
     * 银行卡四元素校验
     */
    @GET("mall.php")
    Observable<CheckBankCard4ElementEntity> addBankCardsInfo(@Query(C) String c, @Query("card_number") String card, @Query("phone") String phone);

    /**
     * 私有卡详情
     */
    @POST("mall.php")
    Observable<PublicCardDetailsEntity> getMyPersonalDetailsInfo(@Query(C) String c, @Query("card_index") String card_index);

    /**
     * 获取省市县
     *
     * @param c
     * @return
     */
    @GET("account.php")
    Observable<IntegralRegionEntity> getRegion(@Query(C) String c);

    /**
     * 获取支行信息
     *
     * @param c
     * @return
     */
    @POST("account.php")
    Observable<BranchBankEntity> getBranchBankName(@Query(C) String c, @Query("province_id") String provinceId,
                                                   @Query("city_id") String cityId, @Query("search_val") String keyword, @Query("bank_id") String bankId);

    /**
     * 银行卡四元素校验
     *
     * @param c
     * @param bankCardNumber
     * @param userName
     * @param idCardNumber   身份证号
     * @param phone
     * @return
     */
    @GET("mall.php")
    Observable<CheckBankCard4ElementEntity> checkBankCard4Element(@Query(C) String c, String bankCardNumber, String userName, String idCardNumber, String phone);

    /**
     * 获取实名认证信息
     *
     * @param c
     * @return
     */
    @GET("mall.php")
    Observable<AuthInfoEntity> getAuthInfo(@Query(C) String c);
}
