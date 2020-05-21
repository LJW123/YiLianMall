package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTPackageCommentListEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.ui.fragment.MTPackageCommentListAllFragment;
import com.yilian.mall.ui.fragment.MTPackageCommentListHaveImgFragment;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 套餐评论列表
 */

public class MTPackageCommentListActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private SlidingTabLayout tl3;
    private ViewPager vp;
    private LinearLayout activityMtpackageCommentList;
    private String[] titles = new String[2];
    ArrayList<Fragment> fragments = new ArrayList<>();
    private String packageId;
    private String merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (fragments.size() <= 0) {
            MTPackageCommentListAllFragment fragment1 = new MTPackageCommentListAllFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHideHeader", false);
            fragment1.setArguments(bundle);
            fragments.add(fragment1);

            MTPackageCommentListHaveImgFragment fragment2 = new MTPackageCommentListHaveImgFragment();
            bundle.putBoolean("idHideHeader", true);
            fragment2.setArguments(bundle);
            fragments.add(fragment2);
        }
        setContentView(R.layout.activity_mtpackage_comment_list);
        getData();
        initViewTitle();
//        initData();

    }

    private void initViewTitle() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        ivLeft1.setOnClickListener(this);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);


//        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("套餐评论");
        ivRight2.setVisibility(View.GONE);
    }

    private void initListener() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        merchantId = getIntent().getStringExtra("merchantId");
        packageId = getIntent().getStringExtra("packageId");
    }

    private void initContentView() {

        tl3 = (SlidingTabLayout) findViewById(R.id.tl_3);

        vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new MTPackageCommentViewPagerAdapter(getSupportFragmentManager()));
        tl3.setViewPager(vp);
        activityMtpackageCommentList = (LinearLayout) findViewById(R.id.activity_mtpackage_comment_list);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left1:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    public class MTPackageCommentViewPagerAdapter extends FragmentStatePagerAdapter {

        public MTPackageCommentViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
////            return super.instantiateItem(container, position);
//            return fragments.get(position);
//        }

        /**
         * 设置Viewpager上方标题
         *
         * @param newTitles
         */
        public void setPageTitle(String[] newTitles) {
            titles = newTitles;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);//重写该方法，保证item不被销毁，从而返回每个已加载的Fragment时，不再重新加载,增加Viewpager滑动时的流畅性
        }


    }

    private MTNetRequest mtNetRequest;

    /**
     * 获取评论
     */
    private void getData() {
        merchantId = getIntent().getStringExtra("merchantId");
        packageId = getIntent().getStringExtra("packageId");
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(context);
        }
        mtNetRequest.getMTPackageCommentAllList(packageId, merchantId, "0", 0, new RequestCallBack<MTPackageCommentListEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTPackageCommentListEntity> responseInfo) {
                MTPackageCommentListEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        titles[0] = "全部(" + result.count + ")";
                        titles[1] = "晒图(" + result.imgCount + ")";
                        initContentView();
                        initListener();
                        break;
                    case 0:
                        break;
                    case -17:
                        break;
                    case -4:
                        break;
                    case -3:
                        showToast(R.string.system_busy);
                        break;
                    case -23:
                        break;
                    default:
                        showToast("" + result.code);
                        break;

                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

}
