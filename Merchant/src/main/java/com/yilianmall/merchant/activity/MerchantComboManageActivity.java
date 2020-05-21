package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mylibrary.widget.SlidingTabLayout;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.fragment.MerchantDeliveryComboListFragment;
import com.yilianmall.merchant.fragment.MerchantDineInComboListFragment;

public class MerchantComboManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private FrameLayout v3Layout;
    private SlidingTabLayout slidingTab;
    private ViewPager viewPager;

    private final String[] titles = {
            "配送订单", "到店消费订单"
    };
    private final Fragment[] fragments = {
            new MerchantDeliveryComboListFragment(), new MerchantDineInComboListFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_combo_manage);
        initView();
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("套餐管理");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
//        v3Share.setImageResource(R.mipmap.merchant_ic_mall_scanning);
//        v3Share.setVisibility(View.VISIBLE);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
//        tvRight2.setText("筛选");
//        tvRight2.setVisibility(View.VISIBLE);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ComoboListAdapter(getSupportFragmentManager()));
        slidingTab.setViewPager(viewPager);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {

        }
    }

    class ComoboListAdapter extends FragmentPagerAdapter {

        public ComoboListAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
