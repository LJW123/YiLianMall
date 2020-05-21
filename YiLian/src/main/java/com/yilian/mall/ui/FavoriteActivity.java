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

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.FavoriteComboFragment2;
import com.yilian.mall.ui.fragment.FavoriteGooodsFragment2;
import com.yilian.mall.ui.fragment.FavoriteMerchantFragment2;
import com.yilian.mall.ui.fragment.FavoriteStorFragment2;
import com.yilian.mall.widgets.SlidingTabLayout;



/**
 * 重构收藏界面
 */
public class FavoriteActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private SlidingTabLayout tabLayout;
    private ViewPager viewpager;
    private TextView tvRefresh;
//    private String[] titles = new String[]{"商品", "店铺", "本地商家", "套餐"};//目前支持这个四种
    private String[] titles = new String[]{"商品", "店铺", "本地商家"};//目前支持这个四种
    private boolean isComplie = false;
    private FavoriteStorFragment2 storeFragment2;
    private FavoriteMerchantFragment2 merchantFragment2;
    private FavoriteComboFragment2 comboFragment2;
    private FavoriteGooodsFragment2 gooodsFragment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initView();
        initListener();
    }

    private void initListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isComplie=true;
                complie();
            }
        });
    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("我的收藏");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("编辑");
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new FavoriteVpAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(viewpager);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
    }

    class FavoriteVpAdapter extends FragmentPagerAdapter {

        public FavoriteVpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return initFragment(position);
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

    private BaseFragment initFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                if (gooodsFragment2 == null) {
                    gooodsFragment2 = new FavoriteGooodsFragment2();
                }
                fragment = gooodsFragment2;
                break;
            case 1:
                if (storeFragment2 == null) {
                    storeFragment2 = new FavoriteStorFragment2();
                }
                fragment = storeFragment2;
                break;
            case 2:
                if (merchantFragment2 == null) {
                    merchantFragment2 = new FavoriteMerchantFragment2();
                }
                fragment = merchantFragment2;
                break;
            case 3:
                if (comboFragment2 == null) {
                    comboFragment2 = new FavoriteComboFragment2();
                }
                fragment = comboFragment2;
                break;
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                complie();
                break;
            case R.id.tv_refresh:
                switchFragmentRefresh(viewpager.getCurrentItem());
                break;
            case R.id.v3Back:
                finish();
                break;
        }
    }

    private void complie() {
        isComplie = !isComplie;
        if (isComplie) {
            tvRight.setText("完成");
            tvRight.setTextColor(getResources().getColor(R.color.color_red));
        } else {
            tvRight.setText("编辑");
            tvRight.setTextColor(getResources().getColor(R.color.color_333));
        }
        switchFragmentIsShowCheck(viewpager.getCurrentItem(),isComplie);
    }

    private void switchFragmentRefresh(int currentItem) {
        switch (currentItem) {
            case 0:
                if (null != gooodsFragment2)
                    gooodsFragment2.initData();
                break;
            case 1:
                if (null != storeFragment2)
                    storeFragment2.initData();
                break;
            case 2:
                if (merchantFragment2 != null)
                    merchantFragment2.initData();
                break;
            case 3:
                if (comboFragment2 != null)
                    comboFragment2.initData();
                break;
        }
    }

    private void switchFragmentIsShowCheck(int currentItem,boolean isComplie) {
        switch (currentItem) {
            case 0:
                if (null != gooodsFragment2)
                    gooodsFragment2.initActivirtyState(isComplie);
                break;
            case 1:
                if (null != storeFragment2)
                    storeFragment2.initActivirtyState(isComplie);
                break;
            case 2:
                if (merchantFragment2 != null)
                    merchantFragment2.initActivirtyState(isComplie);
                break;
            case 3:
                if (comboFragment2 != null)
                    comboFragment2.initActivirtyState(isComplie);
                break;
        }
    }

}
