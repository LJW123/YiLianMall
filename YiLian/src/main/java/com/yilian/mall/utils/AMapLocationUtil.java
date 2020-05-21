package com.yilian.mall.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.Location;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by  on 2017/7/7 0007.
 * 定位工具类
 */

public class AMapLocationUtil {
    private Context mContext;
    AccountNetRequest request;
    private AMapLocationSuccessListener aMapLocationSuccessListener;

    private AMapLocationUtil() {

    }

    static AMapLocationUtil aMapLocationUtil;

    public static AMapLocationUtil getInstance() {
        if (aMapLocationUtil == null) {
            synchronized (AMapLocationUtil.class) {
                if (aMapLocationUtil == null) {
                    aMapLocationUtil = new AMapLocationUtil();
                }
            }
        }
        return aMapLocationUtil;
    }

    public void startLocation() {
//启动定位

        mLocationClient.startLocation();
        Logger.i("定位log", "定位开始");
    }

    public AMapLocationUtil location(AMapLocationSuccessListener aMapLocationSuccessListener) {

        this.aMapLocationSuccessListener = aMapLocationSuccessListener;
        return this;
    }

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public AMapLocationUtil initLocation(Context context) {
        this.mContext = context;
//        请在主线程中声明AMapLocationClient类对象，需要传Context类型的参数。推荐用getApplicationConext()方法获取全进程有效的context。
        //初始化定位
        mLocationClient = new

                AMapLocationClient(mContext.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //获取一次定位结果：
//该方法默认为false。
        mLocationOption.setOnceLocation(true);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        return this;
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {

                    if (request == null) {
                        request = new AccountNetRequest(mContext);
                    }
                    request.location(String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getLongitude()), new RequestCallBack<Location>() {
                        @Override
                        public void onSuccess(ResponseInfo<Location> responseInfo) {
                            Location result = responseInfo.result;
                            if (result.code == 1) {
                                Location.location location = result.location;
//                                定位成功后的操作放在监听器中进行，根据需求 可以把定位地址信息是否替换本地的地址信息
//                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_CITY_ID,location.cityId,mContext);
//                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_CITY_NAME,location.cityName,mContext);
//                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_ID,location.countyId,mContext);
//                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME,location.countyName,mContext);
//                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_ID,location.provinceId,mContext);
//                                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_NAME,location.provinceName,mContext);
                                aMapLocationSuccessListener.amapLocationSuccessListener(aMapLocation, location);
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            aMapLocationSuccessListener.amapLocationFailureListener( e, s);
                        }
                    });
                    PreferenceUtils.writeStrConfig(Constants.SELF_LOCATION_LAT, String.valueOf(aMapLocation.getLatitude()), mContext);
                    PreferenceUtils.writeStrConfig(Constants.SELF_LOCATION_LNG, String.valueOf(aMapLocation.getLongitude()), mContext);
//                    PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, aMapLocation.getDistrict(), mContext);
                    Logger.i("定位log", "定位成功，存储到常量中的定位经纬度： SELF_LOCATION_LAT:" + aMapLocation.getLatitude() + "  SELF_LOCATION_LNG:" + aMapLocation.getLongitude());
                    Intent intent = new Intent();
                    intent.putExtra("aMapLocation", aMapLocation);
                    intent.setAction("com.yilian.mall.aMapLocation");
//                    sendBroadcast(intent);
//可在其中解析amapLocation获取相应内容。
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i("aMapLocation", "" + aMapLocation.getLocationType());
                    aMapLocation.getLatitude();//获取纬度
                    Log.i("aMapLocation", "纬度" + aMapLocation.getLatitude());

                    aMapLocation.getLongitude();//获取经度
                    Log.i("aMapLocation", "经度" + aMapLocation.getLongitude());
                    aMapLocation.getAccuracy();//获取精度信息
                    Log.i("aMapLocation", "精度信息" + aMapLocation.getAccuracy());
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i("aMapLocation", "地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。：：" + aMapLocation.getAddress());
                    aMapLocation.getCountry();//国家信息
                    Log.i("aMapLocation", "国家信息" + aMapLocation.getCountry());
                    aMapLocation.getProvince();//省信息
                    Log.i("aMapLocation", "省信息" + aMapLocation.getProvince());
                    aMapLocation.getCity();//城市信息
                    Log.i("aMapLocation", "城市信息" + aMapLocation.getCity());
                    aMapLocation.getDistrict();//城区信息
                    Log.i("aMapLocation", "城区信息" + aMapLocation.getDistrict());
                    aMapLocation.getStreet();//街道信息
                    Log.i("aMapLocation", "街道信息" + aMapLocation.getStreet());
                    aMapLocation.getStreetNum();//街道门牌号信息
                    Log.i("aMapLocation", "街道门牌号信息" + aMapLocation.getStreetNum());
                    aMapLocation.getCityCode();//城市编码
                    Log.i("aMapLocation", "城市编码" + aMapLocation.getCityCode());
                    aMapLocation.getAdCode();//地区编码
                    Log.i("aMapLocation", "地区编码" + aMapLocation.getAdCode());
                    aMapLocation.getAoiName();//获取当前定位点的AOI信息
                    Log.i("aMapLocation", "获取当前定位点的AOI信息" + aMapLocation.getAoiName());
                    aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    Log.i("aMapLocation", "获取当前室内定位的建筑物Id" + aMapLocation.getBuildingId());
                    aMapLocation.getFloor();//获取当前室内定位的楼层
                    Log.i("aMapLocation", "获取当前室内定位的楼层" + aMapLocation.getFloor());
                    aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                    Log.i("aMapLocation", "获取GPS的当前状态" + aMapLocation.getGpsAccuracyStatus());
                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    String format = df.format(date);
                    Log.i("aMapLocation", "定位时间：" + format);


                } else {
                    //定位失败时，可通过ErrCode（v3Back）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };


}
