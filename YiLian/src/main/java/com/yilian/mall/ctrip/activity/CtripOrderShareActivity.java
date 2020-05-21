package com.yilian.mall.ctrip.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.fragment.orderShare.CtripShareHotelFragment;
import com.yilian.mall.ctrip.fragment.orderShare.CtripShareOrderFragment;
import com.yilian.mall.ctrip.popupwindow.CtripOrderSharePopwindow;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.umeng.ShareUtile;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderShareEntity;

import java.util.ArrayList;

/**
 * 携程 订单分享
 *
 * @author Created by Zg on 2018/9/28.
 */

public class CtripOrderShareActivity extends BaseFragmentActivity implements View.OnClickListener {

    private SegmentTabLayout tabLayout_1;
    private CtripOrderShareEntity shareData;
    private String[] mTitles = {"分享订单", "分享产品"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ImageView ivBack;
    private ImageView ivShare;
    private CtripOrderSharePopwindow sharePopwindow;
    private CtripShareOrderFragment ctripShareOrderFragment;
    private CtripShareHotelFragment ctripShareHotelFragment;
    private ShareUtile shareUtile;
    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_order_share);

        shareData = getIntent().getParcelableExtra("shareData");

        initView();
        initData();
        initListener();
    }


    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(context, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        tabLayout_1 = findViewById(R.id.tl_1);
        ctripShareOrderFragment = new CtripShareOrderFragment().getInstance(shareData);
        mFragments.add(ctripShareOrderFragment);
        ctripShareHotelFragment = new CtripShareHotelFragment().getInstance(shareData);
        mFragments.add(ctripShareHotelFragment);


        tabLayout_1.setTabData(mTitles, this, R.id.fl_change, mFragments);

        ivBack = findViewById(R.id.iv_back);
        ivShare = findViewById(R.id.iv_share);

        shareUtile = new ShareUtile(context);

    }

    public void initData() {

    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);

        tabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentTab = position;
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.iv_share:
                //分享
                if (currentTab == 0) {
                    ctripShareOrderFragment.setShareLayoutData();
                } else if (currentTab == 1) {
                    ctripShareHotelFragment.setShareLayoutData();
                }

                if (sharePopwindow == null) {
                    sharePopwindow = new CtripOrderSharePopwindow(context);
                    sharePopwindow.setWechat(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharePopwindow.dismiss();
                            Bitmap bitmap;
                            if (currentTab == 0) {
                                bitmap = ctripShareOrderFragment.createShareBitmap();
                            } else {
                                bitmap = ctripShareHotelFragment.createShareBitmap();
                            }
                            shareUtile.shares(0, bitmap);
                        }
                    });
                    sharePopwindow.setFriendsCircle(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharePopwindow.dismiss();
                            Bitmap bitmap;
                            if (currentTab == 0) {
                                bitmap = ctripShareOrderFragment.createShareBitmap();
                            } else {
                                bitmap = ctripShareHotelFragment.createShareBitmap();
                            }
                            shareUtile.shares(1, bitmap);
                        }
                    });
                    sharePopwindow.setQq(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharePopwindow.dismiss();
                            Bitmap bitmap;
                            if (currentTab == 0) {
                                bitmap = ctripShareOrderFragment.createShareBitmap();
                            } else {
                                bitmap = ctripShareHotelFragment.createShareBitmap();
                            }
                            shareUtile.shares(4, bitmap);
                        }
                    });
                    sharePopwindow.setQzone(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharePopwindow.dismiss();
                            Bitmap bitmap;
                            if (currentTab == 0) {
                                bitmap = ctripShareOrderFragment.createShareBitmap();
                            } else {
                                bitmap = ctripShareHotelFragment.createShareBitmap();
                            }
                            shareUtile.shares(3, bitmap);
                        }
                    });
                }
                sharePopwindow.showPop(ivShare);
                break;
            default:
                break;
        }
    }
}
