package com.leshan.ylyj.presenter;

import android.content.Context;

import com.leshan.ylyj.base.BasePresenter;
import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.model.DrivingLicenseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by @author zhaiyaohua on 2018/1/22 0022.
 */

public class DringLicensePrensent extends BasePresenter {

    private DrivingLicenseModel model;

    public DringLicensePrensent(Context context, BaseView view) {
        attachView(view);
        model = new DrivingLicenseModel(context);
    }
    public DringLicensePrensent(Context context, BaseView view,int flag) {
        this.mFlag=flag;
        attachView(view);
        model = new DrivingLicenseModel(context);
    }

    /**
     * 阿里认证行驶证
     * @param body
     * @param subscriber
     */
    public void dringLicense(RequestBody body,Subscriber<ResponseBody> subscriber){
        Subscription subscription= model.drivingEntityObservable(body).subscribe(subscriber);
        addSubscription(subscription);
    }

    /**
     * 上传行驶证
     * @param certificates
     */
    public void uploadDrivingCertificates(MultipartBody.Part certificates){
        Subscription subscription= model.uploadDrivingCertificates(certificates).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 添加行驶证
     * @param map
     */
    public void addDringCard(Map<String,String> map){
        Subscription subscription= model.addDrivingEntity(map).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 获取认证用户信息
     * @param context
     */
    public void getAuthenticationInfoVisiable(Context context){
        Subscription subscription= model.getAuthenticationInfoVisiable(context).subscribe(observer);
        addSubscription(subscription);
    }

}
