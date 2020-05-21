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
import com.yilian.luckypurchase.fragment.BaseFragment;
import com.yilian.luckypurchase.fragment.PriceRecordJoinListFragment;
import com.yilian.luckypurchase.fragment.PriceRecordSnatchAwardFragment;
import com.yilian.luckypurchase.fragment.PriceRecordSnatchShowFragment;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.BaseFragmentActivity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.widget.SlidingTabLayout;

import java.util.ArrayList;

/**
 * @author我的记录
 */
public class LuckyMyRecordActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private SlidingTabLayout slidingTab;
    private ViewPager viewpager;
    private String[] titles={"进行中","已揭晓","中奖记录","晒单记录"};
    private MyRecordAdapter adapter;
    private ArrayList<BaseFragment> fragments;

    private int currentPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_my_record);
        initFragment();
        initView();
    }

    private void initView() {
        currentPostion = getIntent().getIntExtra("current_position", 0);

        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("记录");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new MyRecordAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        slidingTab.setViewPager(viewpager);

        viewpager.setCurrentItem(currentPostion);

        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        //参与记录
        fragments.add(new PriceRecordJoinListFragment());
        fragments.add(new PriceRecordJoinListFragment());
        //中奖记录
        fragments.add(new PriceRecordSnatchAwardFragment());
        //晒单记录
        fragments.add(new PriceRecordSnatchShowFragment());

        Bundle bundle;
        for (int i = 0; i < fragments.size(); i++) {
            bundle=new Bundle();
            bundle.putInt("position",i);
            bundle.putString("userId", PreferenceUtils.readStrConfig(Constants.USER_ID,this,""));
            fragments.get(i).setArguments(bundle);
        }
    }

    class MyRecordAdapter extends FragmentPagerAdapter{

        public MyRecordAdapter(FragmentManager fm) {
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

    private Fragment getFragment(int position) {
        return fragments.get(position);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
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
                        if (PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, LuckyMyRecordActivity.this)) {
                            intent.setComponent(new ComponentName(LuckyMyRecordActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyMyRecordActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyMyRecordActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyMyRecordActivity.this, "com.yilian.mall.ui.JPMainActivity"));
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
