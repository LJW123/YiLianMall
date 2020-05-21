package com.yilian.mall.ctrip.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.mobileimexternal.ui.fundamental.widget.CircleImageView;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.InfoWinAdapter;
import com.yilian.mall.ctrip.bean.MarketBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.alibaba.mobileim.YWChannel.getResources;
import static com.vondear.rxtools.view.RxToast.doubleClickExit;
import static com.vondear.rxtools.view.RxToast.showToast;

/**
 * 作者：马铁超 on 2018/9/21 17:23
 */

public class MarkOverlayUtil {
    // 标记上一次点击的marker
    private static Marker lastClickMarker;
    private static Marker currentMarker;
    //    存储所有Marker
    static List<Marker> mAllMarker = new ArrayList<>();
    //    存储所有坐标点
    private static List<com.amap.api.maps.model.LatLng> pointList = new ArrayList<com.amap.api.maps.model.LatLng>();
    private static Marker centerMarker;
    private static com.amap.api.maps.model.LatLng centerPoint;

    //  设置中心点marker
    public static void setCurrentPositionMark(Context context, AMap aMap, double latitude, double longitude) {
        View markerView = LayoutInflater.from(context).inflate(R.layout.layout_custom_marker, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewUtil.convertViewToBitmap(markerView));
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iv_current_position));
        centerMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(new LatLng(latitude, longitude))
                .icon(bitmapDescriptor)
                .title("当前位置")
//                .zIndex(-1)
                .visible(true));
//         自定义 infoWindow adapter
        AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View view = initView();
                return view;
            }

            @NonNull
            private View initView() {
                View infoWindow = LayoutInflater.from(context).inflate(R.layout.layout_map_currentmarker, null);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        };

        aMap.setInfoWindowAdapter(infoWindowAdapter);
        centerMarker.showInfoWindow();
        //        currentMarker.setObject("curruntPosition");
        centerPoint = new com.amap.api.maps.model.LatLng(latitude, longitude);
    }


    /**
     * 根据传进来的经纬度绘制marker
     * 如果设置自定义infoWindow 必须设置 title 可以为空 不设置  infoWindow 不显示
     * @param aMap
     * @param latitude
     * @param longitude
     */
    public static void setHotelInfoMark( AMap aMap, double latitude, double longitude) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iv_current_position));
        centerMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(new LatLng(latitude, longitude))
                .icon(bitmapDescriptor)
                .zIndex(-1)
                .title("")
                .visible(true));
        centerPoint = new com.amap.api.maps.model.LatLng(latitude, longitude);
        centerMarker.showInfoWindow();
    }


    //删除指定Marker
    private void clearMarkers(AMap aMap, String title) {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            if (marker.getObject() instanceof String && marker.getObject().equals(title)) {
                marker.remove();//移除当前Marker
            }
        }
        aMap.invalidate();//刷新地图
    }


    //  添加多个marker
    public static void addMarkerList(List<MarketBean> marketList, AMap aMap, LatLngBounds.Builder boundsBuilder) {
        Logger.e("marketList.toString():  " + marketList.toString());
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.iv_nearby_hotel);
        Marker marker;
        for (int i = 0; i < marketList.size(); i++) {
            marker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .position(new LatLng(marketList.get(i).getLatitude(), marketList.get(i).getLongitude()))
                    .icon(bitmapDescriptor)
//                    .title(marketList.get(i).getTitle())
                    .draggable(true)
                    .visible(true));
            double lat = marketList.get(i).getLatitude();
            double lng = marketList.get(i).getLongitude();
            LatLng latLng = marker.getPosition();
            marker.setObject(marketList.get(i).getTitle());
            mAllMarker.add(marker);
            boundsBuilder.include(marker.getPosition());//绘制Marker  //把所有点都include进去（LatLng类型）
            pointList.add(new com.amap.api.maps.model.LatLng(marketList.get(i).getLatitude(), marketList.get(i).getLongitude()));
        }
        if (aMap != null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 15));//第二个参数为四周留空宽度
        }
    }

    /**
     * 清除所有Marker
     */

    public static void clearAllMarker() {
        if (mAllMarker.size() > 0) {
            for (Marker marker : mAllMarker) {
                marker.remove();
            }
            mAllMarker.clear();
        }
    }


    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中。
     */
    public void zoomToSpan(AMap aMap, LatLngBounds.Builder boundsBuilder) {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null) {
                return;
            }
            centerMarker.setVisible(false);
            com.amap.api.maps.model.LatLngBounds bounds = getLatLngBounds(centerPoint, pointList);
//            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }


    //根据中心点和自定义内容获取缩放bounds
    private com.amap.api.maps.model.LatLngBounds getLatLngBounds(com.amap.api.maps.model.LatLng centerpoint, List<com.amap.api.maps.model.LatLng> pointList) {
        com.amap.api.maps.model.LatLngBounds.Builder b = com.amap.api.maps.model.LatLngBounds.builder();
        if (centerpoint != null) {
            for (int i = 0; i < pointList.size(); i++) {
                com.amap.api.maps.model.LatLng p = pointList.get(i);
                com.amap.api.maps.model.LatLng p1 = new com.amap.api.maps.model.LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    //  设置当前选中marker
    public static void resetMarkers(Marker marker, AMap aMap) {
        String hotelTitle = null;
        try {
            if (marker.getObject() != null) {
                hotelTitle = marker.getObject().toString();
                if (lastClickMarker == null) {
                    lastClickMarker = marker;
                } else {
                    resetLastMark(lastClickMarker);
                    lastClickMarker = marker;
                }
            }
            changeMarkerImg(marker, hotelTitle, false);
            aMap.invalidate();
        } catch (Exception e) {
            Logger.i("ChangeMarkerException==", e);
            showToast("出现错误，请刷新地图");
        }
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude)));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }


    // 将之前点击的marker 状态还原
    private static void resetLastMark(Marker lastMarker) {
        try {
            String lastUseRegcode = lastMarker.getObject().toString();
            if (lastMarker != null) {
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.iv_nearby_hotel);
                lastMarker.setIcon(bitmapDescriptor);
            }
//            changeMarkerImg(lastClickMarker, lastUseRegcode, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Marker changeMarkerImg(Marker marker, final String useRegcode, boolean showLarge) {//获取原设置参数
        BitmapDescriptor bitmapDescriptor = showLarge ? BitmapDescriptorFactory.fromResource(R.mipmap.iv_nearby_hotel) : BitmapDescriptorFactory.fromResource(R.mipmap.iv_hotel_select);
        marker.setIcon(bitmapDescriptor);
        marker.setVisible(true);
        for (int i = 0; i < mAllMarker.size(); i++) {
            if (mAllMarker.get(i).getPosition() == marker.getPosition()) {
                mAllMarker.get(i).setZIndex(0);
            } else {
                mAllMarker.get(i).setZIndex(-1);
            }
        }
        return marker;
    }


    // 判断当前选中酒店与地图上的坐标一致时更新marker图标
    public static void filtrateMarker(LatLng latLng, AMap aMap) {
        for (int i = 0; i < mAllMarker.size(); i++) {
            LatLng itemLatLng = mAllMarker.get(i).getPosition();
            if (itemLatLng.latitude == latLng.latitude && itemLatLng.longitude == itemLatLng.longitude) {
                currentMarker = mAllMarker.get(i);
            }
        }
        if (currentMarker != null) {
            resetMarkers(currentMarker, aMap);
        }
    }
}
