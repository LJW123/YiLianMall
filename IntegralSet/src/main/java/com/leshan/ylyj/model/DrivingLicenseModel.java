package com.leshan.ylyj.model;

import android.content.Context;

import com.leshan.ylyj.basemodel.BaseModel;
import com.leshan.ylyj.baseurl.BaseUrl;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.entity.AddDrivingEntity;
import rxfamily.entity.AuthenticationInfoEntity;
import rxfamily.entity.UploadDrivingImgEntity;

/**
 * @author zhaiyaohua on 2018/1/22 0022.
 */

public class DrivingLicenseModel extends BaseModel {
    public DrivingLicenseModel(Context mContex) {
        super(mContex);
    }

    /**
     * 阿里行驶证认证
     * @param body
     * @return
     */
    public Observable<ResponseBody> drivingEntityObservable(RequestBody body){
        String url="http://dm-53.data.aliyun.com/rest/160601/ocr/ocr_vehicle.json";
        Observable observable=service.getDistinguishDrivingLicense(url,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 上传行驶证
     * @param certificates
     * @return
     */
    public Observable<UploadDrivingImgEntity> uploadDrivingCertificates(MultipartBody.Part certificates){
        String url = BaseUrl.BASE_URL_1 + "v1/driverLicense/uploadDriverImage.do";
        Observable observable=service.uploadDrivingCertificates(url,certificates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 添加行驶证
     * @param map
     * @return
     */
    public Observable<AddDrivingEntity> addDrivingEntity(Map<String,String> map){
        String url = BaseUrl.BASE_URL_1 + "v1/vehicleLicense/saveVehicleLicense.do";
        Observable observable=service.addDriving(url,map)
               .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    /**
     * 认证信息回显
     * @param context
     * @return
     */
    public Observable<AuthenticationInfoEntity> getAuthenticationInfoVisiable(Context context){
        Observable observable=service.getAuthenticationInfoVisiable("get_auth_info")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}
