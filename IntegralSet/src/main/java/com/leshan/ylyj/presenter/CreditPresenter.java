package com.leshan.ylyj.presenter;

import android.content.Context;

import com.leshan.ylyj.base.BasePresenter;
import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.model.CreditModel;

import java.util.Map;

import okhttp3.MultipartBody;
import rx.Subscription;

/**
 * 信用相关接口
 * Created by Administrator on 2018/1/16 0016.
 */

public class CreditPresenter extends BasePresenter {
    private CreditModel modle;

    /**
     * presenter关联具体的modle
     *
     * @param context
     */
    public CreditPresenter(Context context, BaseView view) {
        attachView(view);
        modle = new CreditModel(context);
    }

    /**
     * @net 获取公益的爱心数量
     * @net 获取信用首页数据
     * 执行网络请求并订阅观察者observer
     */
    public void getHomePage() {
        Subscription subscription = modle.getHomePage().subscribe(observer);
        addSubscription(subscription);
    }
    /**
     * 修改信用协议同意与否
     */
    public void setAgreement(String isAgree) {
        Subscription subscription = modle.setAgreement(isAgree).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 发送验证码至email
     *
     * @param email
     */
    public void getEmailCode(String email) {
        Subscription subscribe = modle.getEmailCode(email).subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 添加email提交保存
     */
    public void addEmail(String email, String checkCode) {
        Subscription subscribe = modle.addEmail(email, checkCode).subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 查询邮箱信息
     */
    public void queryEmailInfo() {
        Subscription subscribe = modle.queryEmailInfo().subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 删除邮箱
     */
    public void deleteEmail() {
        Subscription subscribe = modle.deleteEmail().subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 上传驾驶证图片
     */
    public void uploadDriverImg(MultipartBody.Part image) {
        Subscription subscribe = modle.uploadDriverImg(image).subscribe(observer);
        addSubscription(subscribe);
    }
//
//    /**
//     * 添加驾驶证信息
//     */
//    public void addDriverLicenseInfo(String certificateNo, String name, String nationality, String address, String birthday, String initialDate, String carType, String effectiveDate, String expiryDate, String fileNo, String positive, String back) {
//        Subscription subscribe = modle.addDriverLicenseInfo(certificateNo, name, nationality, address, birthday, initialDate, carType, effectiveDate, expiryDate, fileNo, positive, back).subscribe(observer);
//        addSubscription(subscribe);
//    }

    /**
     * 添加驾驶证
     */
    public void addDriver(Map<String, String> map) {
        Subscription subscribe = modle.addDriver(map).subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 查询驾驶证信息
     */
    public void queryDriverLicenseInfo() {
        Subscription subscribe = modle.queryDriverLicenseInfo().subscribe(observer);
        addSubscription(subscribe);
    }
    /**
     * 确认修改驾驶证
     */
    public void updateDriver(Map<String, String> map) {
        Subscription subscribe = modle.updateDriver(map).subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 删除驾驶证信息
     */
    public void deleteDriverLincenseInfo(String id) {
        Subscription subscribe = modle.deleteDriverLincenseInfo(id).subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 添加行驶证信息
     */
    public void addDrivingInfo(String carNo, String name, String sex, String nationality, String address, String birthday, String initialDate, String brandType, String registerDate, String issueDate, String engineNo, String vin, String positive, String back) {
        Subscription subscribe = modle.addDrivingInfo(carNo, name, sex, nationality, address, birthday, initialDate, brandType, registerDate, issueDate, engineNo, vin, positive, back).subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * 查询行驶证信息
     */
    public void queryDrivingLicenseInfo() {
        Subscription subscribe = modle.queryDrivingLicenseInfo().subscribe(observer);
        addSubscription(subscribe);
    }


    /**
     * 确认修改行驶证信息
     */
    public void  updateDriving(Map<String, String> map){
        Subscription subscribe = modle.updateDriving(map).subscribe(observer);
        addSubscription(subscribe);
    }
    /**
     * 删除行驶证信息
     */
    public void deleteDrivingInfo(String id) {
        Subscription subscribe = modle.deleteDrivingInfo(id).subscribe(observer);
        addSubscription(subscribe);
    }

    /**
     * @net 获取信用首页数据
     * 执行网络请求并订阅观察者observer
     */
    public void getCreditRating() {
        Subscription subscription = modle.getCreditRating().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 获取学历地区
     * 执行网络请求并订阅观察者observer
     */
    public void getEducationRegion() {
        Subscription subscription = modle.getEducationRegion().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 获取学历院校地区
     * 执行网络请求并订阅观察者observer
     */
    public void getSchoolName(String provinceid) {
        Subscription subscription = modle.getSchlloName(provinceid).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 提交学历
     * 执行网络请求并订阅观察者observer
     */
    public void getSubmitRecord(String areaName, String areaId, String collegeName, String collegeId, int currentStatus, int education, String enrolTime, String graduateTime) {
        Subscription subscription = modle.getSubmitRecord(areaName, areaId, collegeName, collegeId, currentStatus, education, enrolTime, graduateTime).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 提交学历
     * 执行网络请求并订阅观察者observer
     */
    public void getSubmitRecords(Map<String, String> map) {
        Subscription subscription = modle.getSubmitRecords(map).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 查询学历
     * 执行网络请求并订阅观察者observer
     */
    public void getInquireRecord() {
        Subscription subscription = modle.getInquireRecord().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 信用个人信息查询上传信息
     * 执行网络请求并订阅观察者observer
     */
    public void getUploadInformation() {
        Subscription subscription = modle.getUploadInformation().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 删除学历
     * 执行网络请求并订阅观察者observer
     */
    public void getDeleteRecord(String id) {
        Subscription subscription = modle.getDeleteRecord(id).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 查询信用纬度
     * 执行网络请求并订阅观察者observer
     */
    public void getDimensionality() {
        Subscription subscription = modle.getDimensionality().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 查询信用分享主题列表
     * 执行网络请求并订阅观察者observer
     */
    public void getShareCredit() {
        Subscription subscription = modle.getShareCredit().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 查询信用分享成功回调
     * 执行网络请求并订阅观察者observer
     */
    public void getShareSucceed(String themeID, String credit, String shareApp) {
        Subscription subscription = modle.getShareSucceed(themeID, credit, shareApp).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 查询信用界面公益项目首页列表
     * 执行网络请求并订阅观察者observer
     */
    public void getPublicBenefitHomeBage(String page, String count) {
        Subscription subscription = modle.getPublicBenefitHomeBage(page, count).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 查询信用界面公益项目快捷捐赠弹窗
     * 执行网络请求并订阅观察者observer
     */
    public void getShortcutDonate(String typeVal) {
        Subscription subscription = modle.getShortcutDonate(typeVal).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 查询信用界面公益项目快捷捐赠弹窗（点击捐赠）
     * 执行网络请求并订阅观察者observer
     */
    public void getDonate(String project_id, String donation_integral, String is_anonymous) {
        Subscription subscription = modle.getDonate(project_id, donation_integral, is_anonymous).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * @net 信用守护
     * 执行网络请求并订阅观察者observer
     */
    public void getCreditGuard() {
        Subscription subscription = modle.getCreditGuard().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 信用足迹
     * 执行网络请求并订阅观察者observer
     */
    public void getCreditFootmark(String page,String count) {
        Subscription subscription=modle.getCreditFootmark(page,count).subscribe(observer);
        addSubscription(subscription);
    }
}
