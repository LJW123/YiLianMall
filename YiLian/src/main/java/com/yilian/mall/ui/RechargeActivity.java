package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.ui.fragment.NMemberChargeFragmentYu_eFragment;
import com.yilian.mall.ui.fragment.OfflineTransferFragment;
import com.yilian.mall.widgets.SlidingTabLayout;

/**
 * 新版充值中心
 *
 * @author Ray_L_Pain
 * @date 2017/12/7 0007
 */

public class RechargeActivity extends BaseFragmentActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @ViewInject(R.id.iv_right)
    ImageView ivRight;
    @ViewInject(R.id.vp)
    ViewPager vp;

    private RechargeAdapter adapter;
//   2018-05-16 15:20:25大额充值页面关闭
//    private String[] titles = {"在线充值", "大额充值"};
    private String[] titles = {"在线充值"};
    private NMemberChargeFragmentYu_eFragment chargeFragment;
    private OfflineTransferFragment offlineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeActivity.this, OfflineTransferRecordActivity.class);
                intent.putExtra("from_type", "OfflineTransferActivity");
                startActivity(intent);
            }
        });

        vp.setOffscreenPageLimit(1);

        if (adapter == null) {
            adapter = new RechargeAdapter(getSupportFragmentManager());
        }
        vp.setAdapter(adapter);
        tabLayout.setViewPager(vp);
    }

    class RechargeAdapter extends FragmentPagerAdapter {

        public RechargeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return initFragment(position);
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

    private BaseFragment initFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                if (chargeFragment == null) {
                    chargeFragment = new NMemberChargeFragmentYu_eFragment();
                }
                fragment = chargeFragment;
                break;
            case 1:
                if (offlineFragment == null) {
                    offlineFragment = new OfflineTransferFragment();
                }
                fragment = offlineFragment;
                break;
            default:
                break;
        }
        return fragment;
    }

}
