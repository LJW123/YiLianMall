package com.yilian.mall.ui;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.NMemberChargeFragmentYu_eFragment;
import com.yilian.mall.widgets.CustomViewPager;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.BaseFragmentActivity;

import java.util.ArrayList;

/**
 * 会员充值界面
 * Created by Administrator on 2016/6/6.
 */
public class NMemberChargeActivity extends BaseFragmentActivity {

    @ViewInject(R.id.rg_record)
    private RadioGroup radioGroup;

    @ViewInject(R.id.rb_one)
    private RadioButton rbOne;

    @ViewInject(R.id.rb_two)
    private RadioButton rbTwo;

//    @ViewInject(R.id.rb_three)
//    private RadioButton rbThree;

    @ViewInject(R.id.viewPager)
    private CustomViewPager viewPager;

    private Drawable drawableBottom;

    private ArrayList<Fragment> listFragment = new ArrayList<>();
    private Drawable drawableBottomwhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏

        setContentView(R.layout.activity_nmember_charge);
        ViewUtils.inject(this);
        rbOne.setVisibility(View.GONE);//隐藏乐分币充值的RadioButton

        initView();
    }

    public void onBack(View view) {
        finish();
    }

    private void initView() {

        drawableBottom = getResources().getDrawable(R.drawable.line_blue2dpa);
        drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        drawableBottomwhite = getResources().getDrawable(R.drawable.line_white2dpa);
        drawableBottomwhite.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        AppManager.getInstance().addActivity(NMemberChargeActivity.this);

//        listFragment.add(new NMemberChargeFragmentBalance());//隐藏乐分币充值的Fragment
        listFragment.add(new NMemberChargeFragmentYu_eFragment());
//        listFragment.add(new NMemberChargeFragmentXianJin());//隐藏抵扣券充值的Fragment

        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_one);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_two);
                        break;
//                    case 2:
//                        radioGroup.check(R.id.rb_three);
//                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_one:
                        rbOne.setCompoundDrawables(null, null, null, drawableBottom);
                        rbTwo.setCompoundDrawables(null, null, null, drawableBottomwhite);
//                        rbThree.setCompoundDrawables(null,null,null,drawableBottomwhite);

                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_two:
                        rbOne.setCompoundDrawables(null, null, null, drawableBottomwhite);
                        rbTwo.setCompoundDrawables(null, null, null, drawableBottom);
//                        rbThree.setCompoundDrawables(null,null,null,drawableBottomwhite);
                        viewPager.setCurrentItem(1);
                        break;

//                    case R.id.rb_three:
//                        rbOne.setCompoundDrawables(null, null, null, drawableBottomwhite);
//                        rbTwo.setCompoundDrawables(null, null, null, drawableBottomwhite);
//                        rbThree.setCompoundDrawables(null,null,null,drawableBottom);
//                        viewPager.setCurrentItem(2);
//                        break;
                }
            }
        });
        switch (getIntent().getStringExtra("type")) {
            case "lefenbi":
                viewPager.setCurrentItem(0);
                break;
            case "chooseCharge":
                viewPager.setCurrentItem(0);

                break;
            case "xianjinquan":
                viewPager.setCurrentItem(0);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消mActivityStack对该activity的持有，防止内存泄漏
        AppManager.getInstance().killActivity(this);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }
    }
}
