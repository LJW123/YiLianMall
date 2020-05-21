/*
 * @description
 * NaviLocater.java
 * @author Administrator create at 2014年11月8日下午12:15:56
 */
package com.yilian.mall.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;
import com.yilian.mall.MyApplication;

/**
 * @description: 提供定位导航位置搜索等操作
 */
public class NaviLocater {
    
    private Context mContext;
//    private LocationManagerProxy mLocationManagerProxy;

    private static NaviLocater sNaviLocater;
    
    private NaviLocater() {
        super();
        this.mContext = MyApplication.getInstance();
    }
    
    public static synchronized NaviLocater getInstance() {
        if (sNaviLocater==null) {
            sNaviLocater=new NaviLocater();
            return sNaviLocater;
        }
        return sNaviLocater;
    }
    

    /**
     * 
     * @description:进行单次定位
     * @param listener
     * void
     */
    public void activateSingleLocate(AMapLocationListener listener) {
//        if (mLocationManagerProxy==null) {
//            mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);
//        }
//        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 10, listener);
    }
    
    
    /**
     * 
     * @description:激活定时定位,30s定位一次
     * @param listener 在 requestLocationData 条件满足时触发的监听
     * @author Administrator create at 2014年11月6日下午2:48:23
     */
    public void activateLocate(AMapLocationListener listener) {
//        if (mLocationManagerProxy == null) {
//            mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用destroy()方法
//            // 其中如果间隔时间为-1，则定位只定一次
//            // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
//            mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 30 * 1000, 10, listener);
//        }
    }
    
    /**
     * 
     * @description:停止定时定位
     * @param listener 在 requestLocationData 条件满足时触发的监听
     * @return void
     * @author Administrator create at 2014年11月10日下午5:13:44
     */
    public void deactivateLocate(AMapLocationListener listener) {
//        // TODO Auto-generated method stub
//        if (mLocationManagerProxy != null) {
//            mLocationManagerProxy.removeUpdates(listener);
//            mLocationManagerProxy.destroy();
//        }
//        mLocationManagerProxy = null;
    }

}
