package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.JPFavoriteGooodsFragment;
import com.yilian.mall.ui.fragment.JPFavoriteMerchantFragment;
import com.yilian.mall.ui.fragment.JPFavoriteStorFragment;
import com.yilian.mall.ui.fragment.MTFavoriteComboFragment;
import com.yilian.mall.widgets.SlidingTabLayout;

import java.util.ArrayList;


/**
 * Created by liuyuqi on 2016/10/21 0021.
 * 本地活动/我的收藏
 */

public class JPMyFavoriteActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final String[] titles = {"商品", "店铺", "本地商家","套餐"};
    public boolean isChecked = false;
    private ImageView iv_back;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private FavoriteAdapter favoriteAdapter;
    private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private TextView tv_compile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_activity_native_exercise);
        initfragment();
        initView();
        initListener();
    }

    private void initListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                JPMyFavoriteActivity.this.isChecked = true;
                compile();
            }
        });

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tl_3);
        viewPager = (ViewPager) findViewById(R.id.vp);
        if (favoriteAdapter == null) {
            favoriteAdapter = new FavoriteAdapter(getSupportFragmentManager(), fragments);
        }
        viewPager.setAdapter(favoriteAdapter);
        tabLayout.setViewPager(viewPager);
        iv_back.setOnClickListener(this);
        tv_compile = (TextView) findViewById(R.id.tv_compile);
        tv_compile.setOnClickListener(this);

    }

    private void initfragment() {
        fragments.clear();
        fragments.add(new JPFavoriteGooodsFragment());
        fragments.add(new JPFavoriteStorFragment());
        fragments.add(new JPFavoriteMerchantFragment());
        fragments.add(new MTFavoriteComboFragment());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_compile:
                compile();
                break;
        }
    }

    //编辑所有列表的状态改为可选状态
    public void compile() {
        isChecked = !isChecked;
        if (isChecked) {
            tv_compile.setTextColor(getResources().getColor(R.color.red));
            tv_compile.setText("完成");
        } else {
            tv_compile.setTextColor(getResources().getColor(R.color.color_h1));
            tv_compile.setText("编辑");
        }
        switchFragment();
    }

    private void switchFragment() {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem == 0) {
            JPFavoriteGooodsFragment fragment = (JPFavoriteGooodsFragment) fragments.get(currentItem);
            fragment.initActivirtyState(isChecked);
        } else if (currentItem == 1) {
            JPFavoriteStorFragment fragment = (JPFavoriteStorFragment) fragments.get(currentItem);
            fragment.initActivirtyState(isChecked);
        } else if (currentItem == 2) {
            JPFavoriteMerchantFragment fragment = (JPFavoriteMerchantFragment) fragments.get(currentItem);
            fragment.initActivirtyState(isChecked);
        } else if (currentItem == 3) {
            MTFavoriteComboFragment fragment = (MTFavoriteComboFragment) fragments.get(currentItem);
            fragment.initActivirtyState(isChecked);
        }
    }

    class FavoriteAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> fragments;

        public FavoriteAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
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


}
