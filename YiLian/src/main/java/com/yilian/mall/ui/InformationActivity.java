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

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.NoticeListFragment;
import com.yilian.mall.ui.fragment.OrderListFragment;
import com.yilian.mall.ui.fragment.SystemListFragment;
import com.yilian.mall.widgets.SlidingTabLayout;

import java.util.ArrayList;


/**
 * 新版消息中心
 */
public class InformationActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private SlidingTabLayout tabLayout;
    private ViewPager viewpager;
    private String[] titles = {"订单消息", "益联快报", "系统消息"};
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private int currentItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        currentItem = getIntent().getIntExtra("currentItem", 0);
        Logger.i("ray-" + currentItem);

        initFragment();
        initView();
    }

    private void initFragment() {
        fragments.add(new OrderListFragment());
        fragments.add(new NoticeListFragment());
        fragments.add(new SystemListFragment());
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("消息中心");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new InformationViewpagerAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(viewpager);

        v3Back.setOnClickListener(this);
        viewpager.setCurrentItem(currentItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            default:
                break;
        }
    }


    private class InformationViewpagerAdapter extends FragmentPagerAdapter {

        public InformationViewpagerAdapter(FragmentManager fm) {
            super(fm);
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
            return titles[position];
        }
    }
}
