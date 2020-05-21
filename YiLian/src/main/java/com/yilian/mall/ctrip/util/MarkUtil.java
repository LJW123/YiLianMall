package com.yilian.mall.ctrip.util;

import android.graphics.BitmapFactory;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.bean.MarketBean;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.mobileim.YWChannel.getResources;

/**
 * 作者：马铁超 on 2018/9/21 17:23
 */

public class MarkUtil {
    //    存储所有Marker
    static List<Marker> mAllMarker = new ArrayList<>();

    public static void setCurrentPositionMark(AMap aMap, double latitude, double longitude) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iv_current_position));
        Marker marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(new LatLng(latitude, longitude))
                .icon(bitmapDescriptor)
                .title("当前位置")
                .visible(true));
    }

    //删除指定Marker
   /* private void clearMarkers(AMap aMap) {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            if (marker.getObject() instanceof xxx) {
                marker.remove();//移除当前Marker
            }
        }
        aMap.invalidate();//刷新地图
    }*/


    //  添加多个marker
    public static void addMarkerList(List<MarketBean> marketList, AMap aMap, LatLngBounds.Builder boundsBuilder) {
        Logger.e("marketList.toString():  " + marketList.toString());
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.iv_nearby_hotel);
        for (int i = 0; i < marketList.size(); i++) {
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(new LatLng(marketList.get(i).getLatitude(), marketList.get(i).getLongitude()))
                    .icon(bitmapDescriptor)
                    .title(marketList.get(i).getTitle())
                    .visible(true));
            mAllMarker.add(marker);
            boundsBuilder.include(marker.getPosition());//绘制Marker  //把所有点都include进去（LatLng类型）
        }
        if (aMap != null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 15));//第二个参数为四周留空宽度
        }
    }

    /**
     * 清除所有Marker
     */
    public static void clearAllMarker() {
        for (Marker marker : mAllMarker) {
            marker.remove();
        }
        mAllMarker.clear();
    }
}
