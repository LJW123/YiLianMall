package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.OrderRoutingAdapter;
import com.yilian.mall.adapter.layoutManager.FullyLinearLayoutManager;
import com.yilian.mall.entity.OrderRouting;
import com.yilian.mall.utils.StringFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class OrderRoutingActivity extends BaseActivity {

    @ViewInject(R.id.rv)
    private RecyclerView rv;

    @ViewInject(R.id.tv)
    private TextView tv;

    @ViewInject(R.id.tv_back)
    private TextView tvBack;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_routing);


        ViewUtils.inject(this);
        rv.setAdapter(new OrderRoutingAdapter(mContext, initData()));
        rv.setLayoutManager(new FullyLinearLayoutManager(mContext));
        
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        tvBack.setText(title);
    }

    private ArrayList<OrderRouting> initData() {
        ArrayList<OrderRouting> list = new ArrayList<>();
        String timeStamp;

        timeStamp = getIntent().getStringExtra("confirmTime");
        if (!timeStamp.equals("")&&!timeStamp.equals("0")) {
            list.add(new OrderRouting("确认收货", StringFormat.formatDate(timeStamp, "yyyy-MM-dd"),
                    StringFormat.formatDate(timeStamp, "HH:mm:ss")));
            if (TextUtils.isEmpty(tv.getText().toString()))
                tv.setText("确认收货");
        }
        timeStamp = getIntent().getStringExtra("deliveryTime");
        if (!timeStamp.equals("")&&!timeStamp.equals("0")) {
            list.add(new OrderRouting("发货成功", StringFormat.formatDate(timeStamp, "yyyy-MM-dd"),
                    StringFormat.formatDate(timeStamp, "HH:mm:ss")));
            if (TextUtils.isEmpty(tv.getText().toString()))
                tv.setText("发货成功");
        }
        timeStamp = getIntent().getStringExtra("waitTime");
        if (!timeStamp.equals("")&&!timeStamp.equals("0")) {
            list.add(new OrderRouting("等待发货", "", ""));
            if (TextUtils.isEmpty(tv.getText().toString()))
                tv.setText("等待发货");
        }
        timeStamp = getIntent().getStringExtra("paymentTime");
        if (!timeStamp.equals("")&&!timeStamp.equals("0")) {
            list.add(new OrderRouting("付款成功", StringFormat.formatDate(timeStamp, "yyyy-MM-dd"),
                    StringFormat.formatDate(timeStamp, "HH:mm:ss")));
            if (TextUtils.isEmpty(tv.getText().toString()))
                tv.setText("付款成功");
        }
        timeStamp = getIntent().getStringExtra("orderTime");
        if (!timeStamp.equals("")&&!timeStamp.equals("0")) {
            list.add(new OrderRouting("下单成功", StringFormat.formatDate(timeStamp, "yyyy-MM-dd"),
                    StringFormat.formatDate(timeStamp, "HH:mm:ss")));
            if (TextUtils.isEmpty(tv.getText().toString()))
                tv.setText("下单成功");
        }

        Logger.i(list.size() + "");

        return list;

    }

}
