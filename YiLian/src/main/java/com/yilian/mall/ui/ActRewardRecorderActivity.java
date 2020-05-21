package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.MoreRedpackgetFragment;
import com.yilian.mall.ui.fragment.MyRedpackgetFragment;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * Created by ${zhaiyaohua} on 2017/12/28 0028.
 *
 * @author zhaiyaohua
 */

public class ActRewardRecorderActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private String[] titles = new String[]{"我的打赏", "我的求赏"};
    private TextView v3Title;
    private ImageView v3Back;
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<BaseFragment> fragments;
    private int orderType=0;
    private MyAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_reward_recorder);
        initFragments();
        initView();
        initListner();
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.clear();
        //我的求赏记录
        ActMyRewardRecorderFragment mySeekRewardRecorderFragment=new ActMyRewardRecorderFragment();
        mySeekRewardRecorderFragment.setType("1");
        //我的打赏记录
        ActMyRewardRecorderFragment myRewardRecorderFragment=new ActMyRewardRecorderFragment();
        myRewardRecorderFragment.setType("0");
        fragments.add(myRewardRecorderFragment);
        fragments.add(mySeekRewardRecorderFragment);
    }

    private void initListner() {
        v3Back.setOnClickListener(this);
    }

    private void initView() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.white));
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Title.setText("打赏记录");

        mTabLayout = (SlidingTabLayout) findViewById(R.id.top_tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (vpAdapter == null) {
            vpAdapter = new MyAdapter(getSupportFragmentManager(), fragments);
        }
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragments));

        mTabLayout.setViewPager(mViewPager);
        //设置一跳进来就显示的界面--我的求赏页面
        mViewPager.setCurrentItem(orderType);
        mTabLayout.setIndicatorWidth(97);//设置指示器的宽度
        mTabLayout.setIndicatorMargin(0, 30, 0, 6);
        mViewPager.setCurrentItem(0, true);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v3Back:
                finish();
                break;
            default:
                break;

        }
    }
}
