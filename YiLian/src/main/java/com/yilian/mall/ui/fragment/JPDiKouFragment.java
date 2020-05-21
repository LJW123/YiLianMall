package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPLevel1CategoryEntity;
import com.yilian.mall.ui.OnlineMallGoodsSortListActivity;
import com.yilian.mall.ui.SearchCommodityActivity;
import com.yilian.mall.utils.SharedPreferencesUtils;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * 抵扣券区Fragment
 */
public class JPDiKouFragment extends JPBaseFragment implements View.OnClickListener {

    private View rootView;
    private static JPDiKouFragment jpDiKouFragment;

    private ImageView ivLeft1;
    private ImageView ivLeft2;
    private ImageView ivTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private View viewLine;
    private SlidingTabLayout tabLayout_3;
    private ViewPager vp;

    private ArrayList<JPLevel1CategoryEntity.ListBean> jpLevel1CategoryList = new ArrayList<>();
    //商品分类数组
    private JPLevel1CategoryEntity.ListBean[] jpLevelCategorys;
    private LinearLayout llJPTitle;
    private RelativeLayout rlIvLeft1;
    private RelativeLayout rlIvRight2;
    private TextView tvTitle;


    public static JPDiKouFragment getInstance() {
        if (jpDiKouFragment == null) {
            jpDiKouFragment = new JPDiKouFragment();
        }
        return jpDiKouFragment;
    }

    public android.support.v4.view.ViewPager getVp() {
        return vp;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Logger.i("二〇一八年二月八日 18:09:56-走了setUserVisibleHint");
            initView(rootView);
            initListener();
        }
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_jp_home, container, false);
            SharedPreferencesUtils spUtils = new SharedPreferencesUtils(mContext, Constants.SPKEY_JP_CATEGORY_OBJECT);
            jpLevelCategorys = (JPLevel1CategoryEntity.ListBean[]) spUtils.getObject(Constants.SPKEY_JP_CATEGORY_OBJECT, Object.class);
        }

        Logger.i("二〇一八年二月八日 18:09:56-走了createView");

        return rootView;
    }

    @Override
    protected void loadData() {
    }

    private void initListener() {
        Logger.i("走了这里initListener");
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //正在滑动   pager处于正在拖拽中

                    ImageLoader.getInstance().pause();


                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    ImageLoader.getInstance().pause();
                    //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程

                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    ImageLoader.getInstance().resume();
                    //空闲状态  pager处于空闲状态
                }
            }
        });
    }

    private void initView(View view) {
        Logger.i("走了这里JPHomeFramgnetInitView");
        llJPTitle = (LinearLayout) view.findViewById(R.id.ll_jp_title);
        ivLeft1 = (ImageView) view.findViewById(R.id.iv_left1);
        ivLeft1.setClickable(true);
        ivLeft1.setOnClickListener(this);
        ivLeft2 = (ImageView) view.findViewById(R.id.iv_left2);
        ivTitle = (ImageView) view.findViewById(R.id.iv_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(getResources().getString(R.string.jifenmall));
        tvTitle.setVisibility(View.VISIBLE);
        ivRight1 = (ImageView) view.findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) view.findViewById(R.id.iv_right2);
        ivRight2.setClickable(true);
        ivRight2.setOnClickListener(this);
        viewLine = view.findViewById(R.id.view_line);
        tabLayout_3 = (SlidingTabLayout) view.findViewById(R.id.tl_3);
        vp = (ViewPager) view.findViewById(R.id.vp);
        vp.setOffscreenPageLimit(1);
        //使用getChildFragmentManager  而不适用getFragmentManager 解决Fragment嵌套Fragment时，内部Fragment不显示的问题
        vp.setAdapter(new HomeViewPagerAdapter(getChildFragmentManager()));
        tabLayout_3.setViewPager(vp);

        rlIvLeft1 = (RelativeLayout) view.findViewById(R.id.rl_iv_left1);
        rlIvLeft1.setOnClickListener(this);
        rlIvRight2 = (RelativeLayout) view.findViewById(R.id.rl_iv_right2);
        rlIvRight2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_iv_left1:
            case R.id.iv_left1:
                intent = new Intent(mContext, OnlineMallGoodsSortListActivity.class);
                break;
            case R.id.rl_iv_right2:
            case R.id.iv_right2:
                intent = new Intent(mContext, SearchCommodityActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    //使用FragmentStatePagerAdapter 而不是FragmentPagerAdapter，在Fragment比较多时，滑动更流畅
    private class HomeViewPagerAdapter extends FragmentPagerAdapter {

        public HomeViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MainCategoryFragment();
            } else {
                CategoryFragment categoryFragment = new CategoryFragment();

                Bundle bundle = new Bundle();
                bundle.putString("categoryId", jpLevelCategorys[position].JPLevel1CategoryId);
                Logger.i("存入的CategoryId:" + jpLevelCategorys[position].JPLevel1CategoryId);
                categoryFragment.setArguments(bundle);
                return categoryFragment;
            }
        }

        @Override
        public int getCount() {
            if (jpLevelCategorys != null) {
                return jpLevelCategorys.length;
            } else {
                return 0;

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
//                Logger.i("走了这里HomeViewPagerAdapter,leng:" + jpLevelCategorys.length);
                return jpLevelCategorys[position].JPLevel1CategoryName;
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i("错误信息:" + e.getMessage());
                return "获取失败";
            }
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

}
