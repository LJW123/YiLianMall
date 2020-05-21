package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mylibrary.BaseFragmentActivity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.widget.SlidingTabLayout;
import com.yilian.networkingmodule.entity.MerchantAfterSaleOrderNumber;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.fragment.MerchantAfterSaleFragment;
import com.yilianmall.merchant.utils.UpdateMerchantOrderNumber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 售后订单列表
 */
public class MerchantAfterSaleListActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private SlidingTabLayout slidingTab;
    private ViewPager viewpager;
    private String[] titles = {"待审核", "处理中", "已完成"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_after_sale_list);
        EventBus.getDefault().register(this);
        PreferenceUtils.writeBoolConfig(Constants.REFRESH_AFTER_lIST, false, this);
        initView();
        initData();
    }

    private void initData() {
        //        更新商家售后订单数量
        addSubscription(UpdateMerchantOrderNumber.getMerchantAfterSaleOrderNumber(this));
    }

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    private void initView() {
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("售后订单列表");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new AfterSaleAdapter(getSupportFragmentManager()));
        slidingTab.setViewPager(viewpager);

        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    class AfterSaleAdapter extends FragmentPagerAdapter {

        public AfterSaleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getAfterSaleFragment(position + 1);
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

    private Fragment getAfterSaleFragment(int position) {
        MerchantAfterSaleFragment fragment = new MerchantAfterSaleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        }
    }

    /**
     * 接收到更改数量事件后更新数量
     *
     * @param numData
     */
    @Subscribe
    public void updateAfterSaleOrderNumber(MerchantAfterSaleOrderNumber.DataBean numData) {
        int ready = numData.ready;//待审核数量
        int doing = numData.doing;//处理中数量
        int done = numData.done;//已完成数量
        if (ready > 0) {
            slidingTab.showMsg(0, ready);
        } else {
            slidingTab.hideMsg(0);
        }
        if (doing > 0) {
            slidingTab.showMsg(1, doing);
        } else {
            slidingTab.hideMsg(1);
        }
        for (int i = 0; i < titles.length; i++) {
            slidingTab.getMsgView(i).setBackgroundColor(ContextCompat.getColor(this, R.color.library_module_color_red));
            slidingTab.setMsgMargin(i, 30, 10);
        }
    }
}
