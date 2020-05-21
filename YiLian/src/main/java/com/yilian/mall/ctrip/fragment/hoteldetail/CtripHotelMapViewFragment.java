package com.yilian.mall.ctrip.fragment.hoteldetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.JPBaseFragment;

/**
 * @author xiaofo on 2018/9/15.
 */

@SuppressLint("ValidFragment")
public class CtripHotelMapViewFragment extends JPBaseFragment implements AMap.OnMapClickListener{
    public static final String TAG_LAT_LNG = "tag_lat_lng";
    private MapClickInterFace listener;
    String  cityId;
    Context context;

    @SuppressLint("ValidFragment")
    public CtripHotelMapViewFragment( Context context,String cityId) {
         this.cityId = cityId;
         this.context = context;
    }

    public static CtripHotelMapViewFragment getInstance(LatLng latLng ,CtripHotelMapViewFragment ctripHotelMapViewFragment) {
//        CtripHotelMapViewFragment ctripHotelMapViewFragment = new CtripHotelMapViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAG_LAT_LNG, latLng);
        ctripHotelMapViewFragment.setArguments(args);
        return ctripHotelMapViewFragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapView mapView = new MapView(getContext());
        mapView.onCreate(savedInstanceState);
        mapView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Bundle arguments = getArguments();
        LatLng latLng = arguments.getParcelable(TAG_LAT_LNG);
        AMap map = mapView.getMap();
        map.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(com.amap.api.maps2d.model.BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory.decodeResource(getResources(), R.mipmap.icon_ctrip_map_view_mark)
                        ))
                        .draggable(true)
        );
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        uiSettings.setZoomGesturesEnabled(false);
        map.moveCamera(CameraUpdateFactory.zoomTo(17));
        map.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        map.setOnMapClickListener(this);
        return mapView;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        ToastUtil.showMessage(context,"onClick");
        listener.onMapClick(context, cityId);
    }


    public interface MapClickInterFace {
        void onMapClick(Context context, String cityId);
    }

    public void setOnMapClickListener(MapClickInterFace myItemClickListener) {
        this.listener = myItemClickListener;
    }
}
