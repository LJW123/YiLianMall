package com.leshan.ylyj.view.activity.healthmodel;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.adapter.HealthAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.bean.HealthBean;
import com.leshan.ylyj.customview.CountNumberView;
import com.leshan.ylyj.customview.FullyLinearLayoutManager;
import com.leshan.ylyj.utils.SkipUtils;
import com.yilian.mylibrary.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的健康
 */
public class MyHealthActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout back_ll;

    //健康额度
    private CountNumberView integral_total_tv;

    private RecyclerView help_rv;
    private List<HealthBean> healthBeans;
    private HealthAdapter adapter;
    private LinearLayout apply_money_ll;
    private LinearLayout up_money_ll;
    private LinearLayout my_family_ll;
    private LinearLayout health_result_ll;
    private ImageView back_iv;

    private LinearLayout rightLayout;
    private TextView tvHealthFruit;
    private TextView tvMyFamily;
    private TextView tvHelpCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_health);

        initView();
        initListener();
        initData();

        StatusBarUtils.setStatusBarColor(MyHealthActivity.this, R.color.color_act_green, true);
    }

    @Override
    protected void initView() {
        back_ll = findViewById(R.id.back_ll);//返回键

        rightLayout = findViewById(R.id.layout_right);
        tvHealthFruit = findViewById(R.id.tv_health_fruit);
        tvMyFamily = findViewById(R.id.tv_my_family);
        tvHelpCount = findViewById(R.id.tv_help_count);

        integral_total_tv = findViewById(R.id.integral_total_tv);//健康额度
        help_rv = findViewById(R.id.help_rv);
        help_rv.setLayoutManager(new FullyLinearLayoutManager(this));
        help_rv.setNestedScrollingEnabled(false);
        help_rv.setFocusable(false);//去掉焦点
        health_result_ll = findViewById(R.id.health_result_ll);
        my_family_ll = findViewById(R.id.my_family_ll);
        up_money_ll = findViewById(R.id.up_money_ll);
        apply_money_ll = findViewById(R.id.apply_money_ll);
        back_iv = findViewById(R.id.back_iv);
    }

    @Override
    protected void initListener() {
        back_ll.setOnClickListener(this);
        rightLayout.setOnClickListener(this);
        health_result_ll.setOnClickListener(this);
        my_family_ll.setOnClickListener(this);
        up_money_ll.setOnClickListener(this);
        apply_money_ll.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        integral_total_tv.showNumberWithAnimation(183000, CountNumberView.INTREGEX);

        healthBeans = new ArrayList<>();
        healthBeans.add(new HealthBean(R.mipmap.zzzz_icon2, "白血病患儿", "50种大病", "出生135天"));
        healthBeans.add(new HealthBean(R.mipmap.zzzz_icon2, "白血病患儿", "50种大病", "出生135天"));
        healthBeans.add(new HealthBean(R.mipmap.zzzz_icon2, "白血病患儿", "50种大病", "出生135天"));
        healthBeans.add(new HealthBean(R.mipmap.zzzz_icon2, "白血病患儿", "50种大病", "出生135天"));
        healthBeans.add(new HealthBean(R.mipmap.zzzz_icon2, "白血病患儿", "50种大病", "出生135天"));
        healthBeans.add(new HealthBean(R.mipmap.zzzz_icon2, "白血病患儿", "50种大病", "出生135天"));
        healthBeans.add(new HealthBean(R.mipmap.zzzz_icon2, "白血病患儿", "50种大病", "出生135天"));
        adapter = new HealthAdapter(R.layout.item_health_layout);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                view.findViewById(R.id.add_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        });
        help_rv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.health_result_ll) {
            SkipUtils.toHealthFruit(this);
        } else if (i == R.id.my_family_ll) {
            SkipUtils.toMyFamily(this);
        } else if (i == R.id.apply_money_ll) {
        } else if (i == R.id.back_ll) {
            finish();
        }
    }
}
