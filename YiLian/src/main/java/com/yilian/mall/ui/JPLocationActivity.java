package com.yilian.mall.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.JPLocationShakeFragment;
import com.yilian.mylibrary.BaseFragmentActivity;

import java.util.ArrayList;


/**
 * Created by Ray_L_Pain on 2016/10/26 0026.
 * v3本地活动
 */

public class JPLocationActivity extends BaseFragmentActivity {
    @ViewInject(R.id.titleLayout)
    RelativeLayout titleLayout;
    @ViewInject(R.id.titleLayout2)
    LinearLayout titleLayout2;
    @ViewInject(R.id.v3Back)
    ImageView back;
    @ViewInject(R.id.v3Title)
    TextView title;
//    @ViewInject(R.id.tablayout)
//    SlidingTabLayout tablayout;
    @ViewInject(R.id.viewpager)
    ViewPager viewpager;

//    public final String[] titles = {"大转盘","摇一摇","大家猜","刮刮卡"};
    public final String[] titles = {"摇一摇"};
    ArrayList<BaseFragment> listFragment = new ArrayList<>();
    private InnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        setContentView(R.layout.activity_location_jp);
        ViewUtils.inject(this);

        initView();
        initFragment();
    }

    private void initView() {

        title.setText("本地活动");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPLocationActivity.this.finish();
            }
        });

        if(adapter == null){
            adapter = new InnerAdapter(getSupportFragmentManager(), listFragment);
        }
        viewpager.setAdapter(adapter);
//        tablayout.setViewPager(viewpager);
    }

    private void initFragment(){
//        listFragment.add(new JPLocationCircleFragment());//大转盘
        listFragment.add(new JPLocationShakeFragment());//摇一摇
//        listFragment.add(new JPLocationGuessFragment());//大家猜
//        listFragment.add(new JPLocationScrapeFragment());//刮刮卡
    }

    class InnerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> fragments;

        public InnerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        @Override
        public int getCount() {
            return 1;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles[position];
//        }
    }
}
