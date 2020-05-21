package com.yilian.mall.suning.fragment.order;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.yilian.mall.R;
import com.yilian.mall.suning.activity.SnAfterSaleSearchActivity;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.OnTabSelectListener;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.entity.TabEntity;
import com.yilian.networkingmodule.entity.suning.snEventBusModel.SnOrderTabModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * 苏宁售后
 *
 * @author Created by Zg on 2018/5/25.
 */
@SuppressLint("ValidFragment")
public class SnAfterSaleFragment extends JPBaseFragment implements OnTabSelectListener {

    public static final String[] mTitles = {
            "售后申请", "申请记录"
    };
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CommonTabLayout mTabLayout_3;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private LinearLayout llSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.sn_fragment_after_sale, null);
        }
        initView(rootView);
        initData();
        initListener();
        return rootView;
    }

    public void initView(View rootView) {
        llSearch = rootView.findViewById(R.id.ll_search);
        mFragments.add(new SnAfterSaleApplyForFragment());
        mFragments.add(new SnAfterSaleApplicationRecordFragment());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }
        /**自定义部分属性*/
        mTabLayout_3 = rootView.findViewById(R.id.tl_3);
        mTabLayout_3.setTabData(mTabEntities, getActivity(), R.id.fl_change, mFragments);


    }


    public void initData() {

    }

    public void initListener() {
        RxUtil.clicks(llSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                switch (mTabLayout_3.getCurrentTab()) {
                    case 0:
                        JumpSnActivityUtils.toSnAfterSearchActivity(mContext, SnAfterSaleSearchActivity.AfterSaleType.TYPE_CAN_APPLY_AFTER_SALE);
                        break;
                    case 1:
                        JumpSnActivityUtils.toSnAfterSearchActivity(mContext, SnAfterSaleSearchActivity.AfterSaleType.TYPE__APPLY_AFTER_SALE_RECORD);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void setCurrentTab(int p) {
        mTabLayout_3.setCurrentTab(p);
    }

    @Override
    public void onTabSelect(int position) {
//        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
//        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

}