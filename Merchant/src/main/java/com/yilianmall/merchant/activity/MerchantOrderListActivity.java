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

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.BaseFragmentActivity;
import com.yilian.mylibrary.widget.SlidingTabLayout;
import com.yilian.networkingmodule.entity.MerchantOrderNumber;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.fragment.MerchantOrderListFragment;
import com.yilianmall.merchant.utils.UpdateMerchantOrderNumber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 商家中心的订单列表
 */
public class MerchantOrderListActivity extends BaseFragmentActivity implements View.OnClickListener {

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
    private String[] titles = {"待备货", "待发货", "待收货", "已完成"};
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_order_list);
        EventBus.getDefault().register(this);
        index = getIntent().getIntExtra("index", 0);
        initView();
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
    protected void onResume() {
        super.onResume();
        Logger.i("走了onresume" + getClass().getSimpleName());
        addSubscription(UpdateMerchantOrderNumber.getMerchantOrderNumber(this));
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
        v3Title.setText("订单列表");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new OrderListAdapter(getSupportFragmentManager()));
        slidingTab.setViewPager(viewpager);
        viewpager.setCurrentItem(index);

        v3Back.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
    }

    class OrderListAdapter extends FragmentPagerAdapter {

        public OrderListAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getOrderListFragment(position);
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

    private MerchantOrderListFragment getOrderListFragment(int position) {
        MerchantOrderListFragment fragment = new MerchantOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", position + 1);//请求订单的类型 1待备货(已取消) 2待发货 3待收货 4已完成
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
     * @param numberBean
     */
    @Subscribe
    public void acceptOrderNumber(MerchantOrderNumber.DataBean numberBean) {
        int ready = numberBean.ready;//带备货数量
        int send = numberBean.send;//待发货数量
        int receive = numberBean.receive;//待收货数量
        if (ready > 0) {
            slidingTab.showMsg(0, ready);
        } else {
            slidingTab.hideMsg(0);
        }
        if (send > 0) {
            slidingTab.showMsg(1, send);
        } else {
            slidingTab.hideMsg(1);
        }
        if (receive > 0) {
            slidingTab.showMsg(2, receive);
        } else {
            slidingTab.hideMsg(2);
        }
        for (int i = 0; i < titles.length; i++) {
            slidingTab.getMsgView(i).setBackgroundColor(ContextCompat.getColor(this, R.color.library_module_color_red));
            slidingTab.setMsgMargin(i, 15, 10);
        }
    }
}
