package com.leshan.ylyj.model;

import android.content.Context;

import com.leshan.ylyj.basemodel.BaseModel;
import com.leshan.ylyj.baseurl.BaseUrl;

import java.util.Map;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.entity.AddDriverLicenseEntity;
import rxfamily.entity.BaseEntity;
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
import rxfamily.entity.InquireRecordEntity;
import rxfamily.entity.MyCreditEntity;
import rxfamily.entity.MyDriverLicenseEntity;
import rxfamily.entity.MyEmailEntity;
import rxfamily.entity.MyEmailInfoEntity;
import rxfamily.entity.PublicBenefitHomeBageEntity;
import rxfamily.entity.QueryDriverLicenseEntity;
import rxfamily.entity.QueryDrivingEntity;
import rxfamily.entity.SchoolNameEntity;
import rxfamily.entity.ShareCreditEntity;
import rxfamily.entity.ShareSucceedEntity;
import rxfamily.entity.ShortcutDonateEntity;
import rxfamily.entity.SubmitRecordEntity;
import rxfamily.entity.UploadDriverImgEntity;
import rxfamily.entity.UploadInformationEntity;

/**
 * 信用相关接口
 * Created by Administrator on 2018/1/16 0016.
 */

public class CreditModel extends BaseModel {

    public CreditModel(Context mContex) {
        super(mContex);
    }

    /**
     * @return
     * @net 获取信用首页数据
     */
    public Observable getHomePage() {
        String url = BaseUrl.BASE_URL_1 + "v1/page/userCredits.do";
        Observable<MyCreditEntity> observable = service.getHomePage(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 修改信用协议同意与否
     */
    public Observable setAgreement(String isAgree) {
        String url = BaseUrl.BASE_URL_1 + "v1/page/userCredits/agreement.do";
        Observable<CreditProtocolsEntity> observable = service.setAgreement(url,isAgree) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 发送验证码至email
     *
     * @param email
     * @return
     */
    public Observable getEmailCode(String email) {
        String url = BaseUrl.BASE_URL_1 + "v1/sendEmailCheckCode.do";
        Observable<MyEmailEntity> observable = service.getEmailCode(url, email)//@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 添加email提交保存
     *
     * @param email
     * @param checkCode
     * @return
     */
    public Observable addEmail(String email, String checkCode) {
        String url = BaseUrl.BASE_URL_1 + "v1/email.do";
        Observable<CommitEmailEntity> observable = service.addEmail(url, email, checkCode)//@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 查询邮箱信息
     *
     * @return
     */
    public Observable queryEmailInfo() {
        String url = BaseUrl.BASE_URL_1 + "v1/email.do";
        Observable<MyEmailInfoEntity> observable = service.queryEmailInfo(url)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 删除邮箱
     *
     * @return
     */
    public Observable deleteEmail() {
        String url = BaseUrl.BASE_URL_1 + "v1/email.do";
        Observable<MyEmailEntity> observable = service.deleteEmail(url)//@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 上传驾驶证图片
     */
    public Observable uploadDriverImg(MultipartBody.Part image) {
        String url = BaseUrl.BASE_URL_1 + "v1/driverLicense/uploadDriverImage.do";
        Observable<UploadDriverImgEntity> observable = service.uploadDriverImg(url, image)//@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 添加驾驶证
     *
     * @param map
     * @return
     */
    public Observable addDriver(Map<String, String> map) {
        String url = BaseUrl.BASE_URL_1 + "v1/driverLicense/saveDriverLices.do";
        Observable<AddDriverLicenseEntity> observable = service.addDriver(url, map)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 查询驾驶证信息
     *
     * @return
     */
    public Observable queryDriverLicenseInfo() {
        String url = BaseUrl.BASE_URL_1 + "v1/driverLicense/findDriverLicenseByUserId.do";
        Observable<QueryDriverLicenseEntity> observable = service.queryDriverLicenseInfo(url)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 确认修改驾驶证信息
     *
     * @param map
     * @return
     */
    public Observable updateDriver(Map<String, String> map) {
        String url = BaseUrl.BASE_URL_1 + "v1/driverLicense/updateDriverLicenseById.do";
        Observable<MyDriverLicenseEntity> observable = service.updateDriver(url, map)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }


    /**
     * 删除驾驶证信息
     */
    public Observable deleteDriverLincenseInfo(String id) {
        String url = BaseUrl.BASE_URL_1 + "v1/driverLicense/removeDriverLicenseById.do";
        Observable<BaseEntity> observable = service.deleteDriverLincenseInfo(url, id)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 添加行驶证信息
     * carNo 车牌号    name 姓名    sex  性别   nationality 国籍    address 地址   birthday 出生日期 initialDate 初次领证日期 brandType 车牌型号 registerDate 注册日期 issueDate发证日期  engineNo 发动机编号 vin 车辆识别代号  positive 证件照片主页 back 证件照片副页
     */
    public Observable addDrivingInfo(String carNo, String name, String sex, String nationality, String address, String birthday, String initialDate, String brandType, String registerDate, String issueDate, String engineNo, String vin, String positive, String back) {
        String url = BaseUrl.BASE_URL_1 + "v1/vehicleLicense/saveVehicleLicense.do";
        Observable<AddDriverLicenseEntity> observable = service.addDrivingInfo(url, carNo, name, sex, nationality, address, birthday, initialDate, brandType, registerDate, issueDate, engineNo, vin, positive, back)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 查询行驶证信息
     */
    public Observable queryDrivingLicenseInfo() {
        String url = BaseUrl.BASE_URL_1 + "v1/vehicleLicense/findVehicleLicenseByUserId.do";
        Observable<QueryDrivingEntity> observable = service.queryDrivingLicenseInfo(url)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 确认修改行驶证 updateDriving
     */
    public Observable updateDriving(Map<String, String> map) {
        String url = BaseUrl.BASE_URL_1 + "v1/vehicleLicense/updateVehicleLicenseById.do";
//        String url="http://192.168.1.43/npm/api/v1/vehicleLicense/updateVehicleLicenseById.do";
        Observable<MyDriverLicenseEntity> observable = service.updateDriving(url, map)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * 删除行驶证信息
     */
    public Observable deleteDrivingInfo(String id) {
        String url = BaseUrl.BASE_URL_1 + "v1/vehicleLicense/removeVehicleLicenseById.do";
        Observable<DeleteDrivingInfo> observable = service.deleteDrivingInfo(url, id)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 获取信用等级数据
     */
    public Observable getCreditRating() {
        String url = BaseUrl.BASE_URL_1 + "v1/creditLevels.do";
        Observable<CreditRatingEntity> observable = service.getCreditRating(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 获取学历地区
     */
    public Observable getEducationRegion() {
        String url = BaseUrl.BASE_URL_1 + "v1/areas/listAreaByParentCode.do";
        Observable<EducationRegionEntity> observable = service.getEducationRegion(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 获取学历院校地区
     */
    public Observable getSchlloName(String provinceid) {
        String url = BaseUrl.BASE_URL_1 + "v1/school/listSchoolByAreaCode.do";
        Observable<SchoolNameEntity> observable = service.getSchoolName(url, provinceid) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 提交学历
     */
    public Observable getSubmitRecord(String areaName, String areaId, String collegeName, String collegeId, int currentStatus, int education, String enrolTime, String graduateTime) {
        String url = BaseUrl.BASE_URL_1 + "v1/education/saveEducation.do";
        Observable<SubmitRecordEntity> observable = service.getSubmitRecord(url, areaName, areaId, collegeName, collegeId, currentStatus, education, enrolTime, graduateTime) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 提交学历
     */
    public Observable getSubmitRecords(Map<String, String> map) {
        String url = BaseUrl.BASE_URL_1 + "v1/education/saveEducation.do";
        Observable<SubmitRecordEntity> observable = service.getSubmitRecords(url, map)
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 查询学历
     */
    public Observable getInquireRecord() {
        String url = BaseUrl.BASE_URL_1 + "v1/education/findEducationById.do";
        Observable<InquireRecordEntity> observable = service.getInquireRecord(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 信用个人信息查询上传信息
     */
    public Observable getUploadInformation() {
        String url = BaseUrl.BASE_URL_1 + "v1/personal//findPersinalByUserId.do";
        Observable<UploadInformationEntity> observable = service.getUploadInformation(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 删除学历
     */
    public Observable getDeleteRecord(String id) {
        String url = BaseUrl.BASE_URL_1 + "v1/education/removeEducation.do";
        Observable<DeleteRecordEntity> observable = service.getDeleteRecord(url, id) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 查询信用纬度
     */
    public Observable getDimensionality() {
        String url = BaseUrl.BASE_URL_1 + "v1/page/userCredits/dimensional.do";
        Observable<DimensionalityEntity> observable = service.getDimensionality(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 查询信用分享主题列表
     */
    public Observable getShareCredit() {
        String url = BaseUrl.BASE_URL_1 + "v1/creditShareThemes.do";
        Observable<ShareCreditEntity> observable = service.getShareCredit(url) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 查询信用分享成功回调
     */
    public Observable getShareSucceed(String themeID, String credit, String shareApp) {
        String url = BaseUrl.BASE_URL_1 + "v1/creditShareThemes.do";
        Observable<ShareSucceedEntity> observable = service.getShareSucceed(url, themeID, credit, shareApp) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 查询信用界面公益项目首页列表
     */
    public Observable getPublicBenefitHomeBage(String page, String count) {
        String url = BaseUrl.BASE_URL_1 + "v1/publicWelfareProject/publicWelfareProjectList.do";
        Observable<PublicBenefitHomeBageEntity> observable = service.getPublicBenefitHomeBage(url, page, count) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 查询信用界面公益项目快捷捐赠弹窗
     */
    public Observable getShortcutDonate(String typeVal) {
        String url = BaseUrl.BASE_URL_1 + "v1/publicWelfareProject/quickAndProjectDonation.do";
        Observable<ShortcutDonateEntity> observable = service.getShortcutDonate(url, typeVal) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 查询信用界面公益项目快捷捐赠弹窗（点击捐赠）
     */
    public Observable getDonate(String project_id, String donation_integral, String is_anonymous) {
        String url = BaseUrl.BASE_URL_1 + "v1/personalDetails/userDonationintegral.do";
        Observable<DonateEntity> observable = service.getDonate(url, project_id, donation_integral, is_anonymous) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

    /**
     * @return
     * @net 信用管理 信用守护
     */
    public Observable<CreditGuardEntity> getCreditGuard() {
        Observable<CreditGuardEntity> observable = service.getCreditGuard("userAuth/credit_guard")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * @return
     * @net 信用管理 信用足迹
     */
    public Observable getCreditFootmark(String page, String count) {
        String url = BaseUrl.BASE_URL_1 + "v1/creditFoot/findCreditFoot.do";
        Observable<CreditFootmarkEntity> observable = service.getCreditFootmark(url, page, count) //@net 调用网络请求的方法
                .subscribeOn(Schedulers.io())//@net 切换网络请求在io线程
                .observeOn(AndroidSchedulers.mainThread());//@net 切换网络请求回调在主线程
        return observable;
    }

}
