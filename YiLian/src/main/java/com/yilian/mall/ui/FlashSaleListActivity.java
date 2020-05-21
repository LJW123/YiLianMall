package com.yilian.mall.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.FlashLastFragment;
import com.yilian.mall.ui.fragment.FlashNextFragment;
import com.yilian.mall.ui.fragment.FlashUnderwayFragment;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.yilian.mylibrary.BaseFragmentActivity;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;


public class FlashSaleListActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private SlidingTabLayout tablayout;
    private ViewPager viewpager;
    private LinearLayout activityFlashSale;
    private String[] titles = {"已抢光", "抢购中", "即将开始"};
    private MyFragmentViewPagerAdapter adapter;
    private ArrayList<BaseFragment> fragments;
    private String flash_state,belong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_flash_sale);
        initFragments();
        initView();
        Logger.i("DEVICE_INDEX "+ RequestOftenKey.getDeviceIndex(this)+"\nTOKEN "+RequestOftenKey.getToken(this));
    }

    private void initFragments() {

        String city_id = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, this, "0");
        String county_id = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, this, "0");
        flash_state = getIntent().getStringExtra("flash_state");
        flash_state = "1";//自动定位到抢购中列表
        belong = getIntent().getStringExtra("belong");
        Logger.i("flash_state    " + flash_state);
        Logger.i("FlashSaleListActivity  city_id " + city_id);
        Logger.i("FlashSaleListActivity  county_id " + county_id);
        if (TextUtils.isEmpty(flash_state)) {
            flash_state = "1";
        }
        fragments = new ArrayList<>();
        fragments.add(new FlashLastFragment());
        fragments.add(new FlashUnderwayFragment());
        fragments.add(new FlashNextFragment());
        Bundle bundle = new Bundle();
        bundle.putString("city_id", city_id);
        bundle.putString("county_id", county_id);
        bundle.putString("belong", belong);
        for (int i = 0; i < fragments.size(); i++) {
            fragments.get(i).setArguments(bundle);
        }
    }


    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        ivLeft1.setImageResource(R.mipmap.v3back);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        ivTitle.setImageResource(R.mipmap.flash_sale_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        ivRight2.setVisibility(View.GONE);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        rlIvLeft2.setVisibility(View.GONE);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        rlTvRight.setVisibility(View.GONE);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        tablayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        View viewLine = findViewById(R.id.view_line);
        viewLine.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new MyFragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
        }
        viewpager.setAdapter(adapter);
        tablayout.setViewPager(viewpager);
        viewpager.setOffscreenPageLimit(2);
        activityFlashSale = (LinearLayout) findViewById(R.id.activity_flash_sale);
        viewpager.setCurrentItem(Integer.parseInt(flash_state));
        ivLeft1.setOnClickListener(this);

    }


    class MyFragmentViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> fragments;

        public MyFragmentViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);
            return titles[position];
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:
                finish();
                break;
        }
    }
}
