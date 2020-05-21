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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.fragment.MemberRecordFragment;
import com.yilian.luckypurchase.fragment.MemberShowPrizeRecordFragment;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.widget.SlidingTabLayout;
import com.yilian.networkingmodule.entity.LuckyMemberJoinRecordEntity;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author  会员夺宝记录 点击幸运购模块所有会员头像跳转该页面
 */
public class LuckyRecordActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private ImageView ivHead;
    private TextView tvUsernamePhone;
    private SlidingTabLayout slidingTab;
    private ViewPager viewpager;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_lucky_prize_record);
        initView();
        initData();
    }

    private void initData() {

    }

    @Subscribe
    @Override
    public void updateUserInfo(LuckyMemberJoinRecordEntity luckyMemberJoinRecordEntity) {
        GlideUtil.showCirImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(luckyMemberJoinRecordEntity.photo), ivHead);
        tvUsernamePhone.setText(luckyMemberJoinRecordEntity.userName + "\n" + luckyMemberJoinRecordEntity.phone);
    }

    private void initView() {
        position = getIntent().getIntExtra("position", 0);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("会员夺宝记录");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        ivHead = (ImageView) findViewById(R.id.iv_head);
        tvUsernamePhone = (TextView) findViewById(R.id.tv_username_phone);
        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        slidingTab.setViewPager(viewpager);
        viewpager.setCurrentItem(position);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
    }

    Fragment getFragment(int position) {
        switch (position) {
            case 0:
                MemberRecordFragment memberRecordFragment = new MemberRecordFragment();
                Bundle args = new Bundle();
                args.putString("userId", getIntent().getStringExtra("userId"));
                args.putInt("type", 1);
                memberRecordFragment.setArguments(args);
                return memberRecordFragment;
            case 1:
                MemberRecordFragment memberRecordFragment2 = new MemberRecordFragment();
                Bundle args2 = new Bundle();
                args2.putString("userId", getIntent().getStringExtra("userId"));
                args2.putInt("type", 2);
                memberRecordFragment2.setArguments(args2);
                return memberRecordFragment2;
            case 2:
                MemberShowPrizeRecordFragment memberRecordFragment3 = new MemberShowPrizeRecordFragment();
                Bundle args3 = new Bundle();
                args3.putString("userId", getIntent().getStringExtra("userId"));
                memberRecordFragment3.setArguments(args3);
                return memberRecordFragment3;
            default:
                return null;
        }
    }

    String[] titles = new String[]{"参与记录", "中奖记录", "晒单记录"};

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
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
                        if (isLogin()) {
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
