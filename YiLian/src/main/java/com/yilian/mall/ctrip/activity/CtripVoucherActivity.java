package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderDetailEntity;

import java.text.SimpleDateFormat;

/**
 * 携程 入住凭证
 *
 * @author Created by Zg on 2018/9/20.
 */

public class CtripVoucherActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;
    private TextView tvTitle;

    private TextView tvHotelName;
    private TextView tvPerson;
    private TextView tvCheckInTime;
    private TextView tvCheckOutTime;
    private TextView tvHouseType;
    private TextView tcRoomNum;
    private TextView tvMealsNum;

    private CtripOrderDetailEntity.DataBean orderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_voucher);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
            }
        });

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);

        tvHotelName = (TextView) findViewById(R.id.tv_hotel_name);
        tvPerson = (TextView) findViewById(R.id.tv_person);
        tvCheckInTime = (TextView) findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = (TextView) findViewById(R.id.tv_check_out_time);
        tvHouseType = (TextView) findViewById(R.id.tv_house_type);
        tcRoomNum = (TextView) findViewById(R.id.tc_room_num);
        tvMealsNum = (TextView) findViewById(R.id.tv_meals_num);
    }

    public void initData() {
        tvTitle.setText("入住凭证");

        orderData = (CtripOrderDetailEntity.DataBean) getIntent().getSerializableExtra("orderData");

        tvHotelName.setText(orderData.hotelName);
        tvPerson.setText(orderData.ContactName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tvCheckInTime.setText(sdf.format(orderData.TimeSpanStart * 1000));
        tvCheckOutTime.setText(sdf.format(orderData.TimeSpanEnd * 1000));
        tvHouseType.setText(orderData.RoomName);
        tcRoomNum.setText(String.format("%s间", orderData.NumberOfUnits));
        tvMealsNum.setText(String.format("%s份", orderData.Breakfast * Integer.valueOf(orderData.NumberOfUnits)));

        varyViewUtils.showDataView();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            default:
                break;
        }
    }
}
