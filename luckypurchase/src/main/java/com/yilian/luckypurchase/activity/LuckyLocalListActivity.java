package com.yilian.luckypurchase.activity;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.fragment.LuckyLocalPrizeFragment;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.widget.SlidingTabLayout;

/**
 * @author  幸运购全部进行中活动列表
 */
public class LuckyLocalListActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View includeTitle;
    private SlidingTabLayout slidingTab;
    private final String[] mTitles = {
            "夺宝进度", "最佳人气", "最新发布", "总需人次"
    };
    private ViewPager viewPager;
    /**
     * 0全部的本地夺宝 大于0标识从商家详情进入夺宝列表 只显示对应的本商家的夺宝列表
     */
    private String merchantId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_local_prize_list);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    String sort = "0,0,1,0";

    class LocalFragmentPagerAdapter extends FragmentPagerAdapter {

        public LocalFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    sort = "0,0,1,0";
                    break;
                case 1:
                    sort = "1,0,0,0";
                    break;
                case 2:
                    sort = "0,1,0,0";
                    break;
                case 3:
                    sort = "0,0,0,1";
                    break;
                default:
                    break;
            }
            LuckyLocalPrizeFragment luckyLocalPrizeFragment = new LuckyLocalPrizeFragment();
            Bundle args = new Bundle();
            args.putString("sort", sort);
            args.putString("merchantId", merchantId);
            luckyLocalPrizeFragment.setArguments(args);
            return luckyLocalPrizeFragment;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    private void initView() {
        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new LocalFragmentPagerAdapter(getSupportFragmentManager()));
        slidingTab.setViewPager(viewPager);
        includeTitle = findViewById(R.id.include_title);

        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("本地夺宝");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
    }

    private void initData() {
        String merchantId = getIntent().getStringExtra("merchantId");
        this.merchantId = merchantId == null ? "0" : merchantId;
    }


    private void initListener() {
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {
        } else if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.v3Shop) {
            showMenu();
        }
    }


    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Shop);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (sp.getBoolean(Constants.SPKEY_STATE, false)) {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

}
