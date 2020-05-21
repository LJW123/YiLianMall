package com.yilian.mall.ctrip.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.activity.MapSearchHotelInfoActivity;
import com.yilian.mall.ctrip.bean.MapHotelInfoBean;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;

import java.util.ArrayList;

/**
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context context;
    private LatLng latLng;
    private TextView tvHotelTitle, tvHotelInfo, tvGrade, tvReserve;
    String title = "", info = "", grade = "",checkInTime,checkOutTime,hotelId;
    MapSearchHotelInfoActivity infoActivity;

    public InfoWinAdapter(Context mContext, CtripHotelDetailEntity infoBean) {
        context = mContext;
        title = infoBean.data.hotelName;
        ArrayList<CtripHotelDetailEntity.DataBean.DescriptionsBean> descriptions = infoBean.data.descriptions;
        if (descriptions != null && descriptions.size() > 0) {
            info = descriptions.get(0).text;
        }
        grade =  infoBean.data.starRating;
        hotelId = infoBean.data.hotelID;
        infoActivity = (MapSearchHotelInfoActivity) mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
    }

    @NonNull
    private View initView() {
        View infoWindow = LayoutInflater.from(context).inflate(R.layout.layout_infowindow, null);
        tvHotelTitle = (TextView) infoWindow.findViewById(R.id.tv_hotel_title);
        tvHotelInfo = (TextView) infoWindow.findViewById(R.id.tv_hotel_info);
        tvGrade = (TextView) infoWindow.findViewById(R.id.tv_grade);
        tvReserve = (TextView) infoWindow.findViewById(R.id.tv_reserve);
        tvHotelTitle.setText(title);
        tvHotelInfo.setText(info);
        tvGrade.setText(grade+"分");
        tvReserve.setOnClickListener(this);
        return infoWindow;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//          房间预订
            case R.id.tv_reserve:
                JumpCtripActivityUtils.toCtripHotelDetail(context, hotelId,"","");
                infoActivity.finish();
                break;
        }
    }

}
