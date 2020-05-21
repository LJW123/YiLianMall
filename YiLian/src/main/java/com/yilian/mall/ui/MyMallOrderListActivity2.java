package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.MyMallOrderFragment;
import com.yilian.mall.widgets.SlidingTabLayout;

/**
 * 新版本订单列表
 * @author
 */
public class MyMallOrderListActivity2 extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private SlidingTabLayout tabLayout;
    private ViewPager viewpager;
    private int orderType, fiser = 0;
    private String[] titles = {"全部", "待付款", "待收货", "已完成", "待评价"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mall_order_list2);
        orderType = getIntent().getIntExtra("order_Type", 0);
        Logger.i("接收的orderType=" + orderType);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMallOrderFragmemt(orderType);
    }
    private void initListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                orderType = position;
                Logger.i("onPageSelected  Position  " + position);
                v3Title.setText(titles[position] + "订单");
//                getMallOrderFragmemt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText(titles[orderType] + "订单");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(viewpager);
        //设置一跳进来就显示的界面
        viewpager.setCurrentItem(orderType);
        viewpager.setOffscreenPageLimit(titles.length);

        v3Back.setOnClickListener(this);
    }

    public MyMallOrderFragment getMallOrderFragmemt(int orderType) {
        Bundle bundle = new Bundle();
        bundle.putInt("orderType", orderType);
        Logger.i("传递前的OrderType   " + orderType);
        MyMallOrderFragment mallOrderFragment = new MyMallOrderFragment();
        mallOrderFragment.setArguments(bundle);
        return mallOrderFragment;
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            fiser++;
            Logger.i("getItem  Position  " + position + " orderType " + orderType+"  fister  "+fiser);
            if (fiser == 0) {
                return getMallOrderFragmemt(orderType);
            } else {
                return getMallOrderFragmemt(position);
            }

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
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
        }
    }
}
