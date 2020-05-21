package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.PrizeAlreadyConversionFragment;
import com.yilian.mall.ui.fragment.PrizeAlreadyDefeatedFragment;
import com.yilian.mall.ui.fragment.PrizeNoGetFragment;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.yilian.mylibrary.BaseFragmentActivity;
import com.yilian.networkingmodule.entity.WheelOfFortuneAwardListEntity;

import java.util.ArrayList;

public class PrizeGoodsListActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private SlidingTabLayout slidingTab;
    private ViewPager viewpager;
    private String[] titles = {"未领取", "已兑换", "已失效"};
    private ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> prizeList = new ArrayList();
    private android.content.Context mContext;
    private String round;
    private MyAdapter vpAdapter;
    private ArrayList<BaseFragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_prize);
        initFragments();
        initView();
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(new PrizeNoGetFragment());//未领取
        fragments.add(new PrizeAlreadyConversionFragment());//已兑换
        fragments.add(new PrizeAlreadyDefeatedFragment());//已失效

    }

    private void initView() {

        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setVisibility(View.VISIBLE);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("中奖物品");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.GONE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        slidingTab = (SlidingTabLayout) findViewById(R.id.slidingTab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        if (vpAdapter==null){
            vpAdapter = new MyAdapter(getSupportFragmentManager(), fragments);
        }
        viewpager.setAdapter(vpAdapter);
        slidingTab.setViewPager(viewpager);

        v3Back.setOnClickListener(this);
    }

    class MyAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> fragments;

        public MyAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
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
