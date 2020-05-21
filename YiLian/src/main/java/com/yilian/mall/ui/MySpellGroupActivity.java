package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseFragmentViewPagerAdapter;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.MySpellGroupErrorFragment;
import com.yilian.mall.ui.fragment.MySpellGroupJoinFragment;
import com.yilian.mall.ui.fragment.MySpellGroupSucceessFragment;
import com.yilian.mall.ui.fragment.MySpellGroupTotalFragment;
import com.yilian.mall.ui.fragment.MySpellGroupWinPrizeFragment;
import com.yilian.mall.widgets.SlidingTabLayout;

import java.util.ArrayList;

/**
 * 我的拼团
 */
public class MySpellGroupActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private SlidingTabLayout tablayout;
    private ViewPager viewpager;
    private String[] titls = {"全部", "拼团中", "拼团成功", "已中奖", "拼团失败"};
    private ArrayList<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spell_group);
        if (!isLogin()){
         startActivity(new Intent(this, LeFenPhoneLoginActivity.class));
        }
        initFragment();
        initView();
    }

    private void initFragment() {

        fragments = new ArrayList<>();
        fragments.add(new MySpellGroupTotalFragment());
        fragments.add(new MySpellGroupJoinFragment());
        fragments.add(new MySpellGroupSucceessFragment());
        fragments.add(new MySpellGroupWinPrizeFragment());
        fragments.add(new MySpellGroupErrorFragment());
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("我的拼团");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.INVISIBLE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tablayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new BaseFragmentViewPagerAdapter(getSupportFragmentManager(),fragments,titls));
        tablayout.setViewPager(viewpager);


        v3Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
        }
    }
}
