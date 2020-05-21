package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.NewGuessListFragment;
import com.yilian.mall.widgets.PopupMenu;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.yilian.mylibrary.Constants;

/**
 * Created by Ray_L_Pain on 2017/10/12 0012.
 */

public class NewGuessListActivity extends BaseFragmentActivity {
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Left)
    ImageView ivLeft;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.v3Shop)
    ImageView ivShop;
    @ViewInject(R.id.v3Share)
    ImageView ivMenu;

    @ViewInject(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @ViewInject(R.id.vp)
    ViewPager vp;

    public static final String[] titles = {"进行中", "已开奖", "我参与的"};
    public NewGuessListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guess);
        ViewUtils.inject(this);

        initView();
        initViewPager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivLeft.setVisibility(View.GONE);
        tvTitle.setText("大家猜");
        ivShop.setVisibility(View.GONE);
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuShow();
            }
        });
    }

    private void initViewPager() {
        if (adapter == null) {
            adapter = new NewGuessListAdapter(getSupportFragmentManager());
        }
        vp.setAdapter(adapter);
        tabLayout.setViewPager(vp);
        vp.setOffscreenPageLimit(3);
    }

    /**
     * 右上角更多
     */
    private void menuShow() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Share);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            startActivity(new Intent(NewGuessListActivity.this, InformationActivity.class));
                        } else {
                            startActivity(new Intent(NewGuessListActivity.this, LeFenPhoneLoginActivity.class));
                        }
                        break;
                    case ITEM2:
                        Intent intentHome = new Intent(NewGuessListActivity.this, JPMainActivity.class);
                        intentHome.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        startActivity(intentHome);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        Intent intentUser = new Intent(NewGuessListActivity.this, JPMainActivity.class);
                        intentUser.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        startActivity(intentUser);
                        break;
                }
            }
        });
    }


    class NewGuessListAdapter extends FragmentPagerAdapter {

        public NewGuessListAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            NewGuessListFragment fragment = new NewGuessListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", String.valueOf(position + 1));
            fragment.setArguments(bundle);
            return fragment;
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
