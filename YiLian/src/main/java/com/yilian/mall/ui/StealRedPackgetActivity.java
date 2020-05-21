package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yilianmall.merchant.activity.MerchantSendRedPackgeActivity;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.MoreRedpackgetFragment;
import com.yilian.mall.ui.fragment.MyRedpackgetFragment;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mall.widgets.SlidingTabLayout;

import java.util.ArrayList;

/**
 *
 * @author ZYH
 * @date 2017/11/30 0030
 */

public class StealRedPackgetActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private int orderType = 0;
    private String[] titles = {"我的奖励", "抢更多奖励"};
    private ArrayList<BaseFragment> fragments;
    private MyAdapter vpAdapter;
    private ImageView v3Back;
    private TextView v3Title;
    private ImageView tvRight;
    private FrameLayout mLinearLayout;
    private UmengDialog mShareDialog;
    private String share_title;
    private String shareImg;
    private String share_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steal_redpackget);
        initFragments();
        initView();
        initListener();
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(new MyRedpackgetFragment());
        fragments.add(new MoreRedpackgetFragment());

    }

    private void initView() {
        //设置状态栏的颜色
        StatusBarUtil.setColor(this,getResources().getColor(R.color.color_status_redpackget));
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.baijiantou);

        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("偷奖励");
        v3Title.setTextColor(getResources().getColor(R.color.white));

        tvRight = (ImageView) findViewById(R.id.v3Share);
        tvRight.setImageResource(R.mipmap.icon_share_red_packge);
        tvRight.setVisibility(View.VISIBLE);
        mLinearLayout = (FrameLayout) findViewById(R.id.v3Layout);
        mLinearLayout.setBackgroundColor(getResources().getColor(R.color.color_status_redpackget));
        mTabLayout = (SlidingTabLayout) findViewById(R.id.top_tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (vpAdapter == null) {
            vpAdapter = new MyAdapter(getSupportFragmentManager(), fragments);
        }
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragments));

        mTabLayout.setViewPager(mViewPager);
        //设置一跳进来就显示的界面
        mViewPager.setCurrentItem(orderType);
        mViewPager.setOffscreenPageLimit(titles.length);
        mTabLayout.setIndicatorWidth(97);//设置指示器的宽度
        mTabLayout.setIndicatorMargin(0, 30, 0, 6);
        mViewPager.setCurrentItem(0, true);
        mViewPager.setOffscreenPageLimit(fragments.size());
    }

    private void initListener() {
        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
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
            case R.id.v3Share:
                startActivity(new Intent(this, MerchantSendRedPackgeActivity.class));
                umShare();
                break;
        }
    }

    /**
     * 分享
     */
    public void umShare() {
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            shareImg,
                            share_url).share();
                }
            });

        }
        mShareDialog.show();
    }


}
