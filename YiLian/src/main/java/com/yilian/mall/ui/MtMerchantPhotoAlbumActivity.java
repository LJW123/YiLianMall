package com.yilian.mall.ui;

import android.content.Context;
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
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.networkingmodule.entity.MerchantPhotoAlbumName;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MtMerchantPhotoAlbumActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private SlidingTabLayout tablayout;
    private ViewPager viewpager;
    private LinearLayout activityMtMerchantPhotoAlbum;
    private Context mContent;
    private String merchant_id;
    private List<MerchantPhotoAlbumName.DataBean.GroupNameBean> groupNameList;
    private ArrayList<MerchantPhotoFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt_merchant_photo_album);
        mContent = this;
        merchant_id = getIntent().getStringExtra("merchant_id");
        initView();
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商家相册");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tablayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        activityMtMerchantPhotoAlbum = (LinearLayout) findViewById(R.id.activity_mt_merchant_photo_album);

        v3Back.setOnClickListener(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        startMyDialog();
        RetrofitUtils.getInstance(context).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContent)).setToken(RequestOftenKey.getToken(mContent))
                .getMerchantPackageAlbumTitleName(merchant_id, new Callback<MerchantPhotoAlbumName>() {
                    @Override
                    public void onResponse(Call<MerchantPhotoAlbumName> call, Response<MerchantPhotoAlbumName> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContent,response.body())){
                            if (CommonUtils.serivceReturnCode(mContent, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        stopMyDialog();
                                        groupNameList = response.body().data.groupName;
                                        if (null != groupNameList && groupNameList.size() > 0) {
                                            initFragment();
                                        }
                                        break;
                                    default:
                                        showToast(response.body().msg);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantPhotoAlbumName> call, Throwable t) {
                        stopMyDialog();
                        Logger.e("onFailure" + t);
                    }
                });
    }

    private void initFragment() {

        fragments = new ArrayList<>();
        MerchantPhotoFragment merchantPhotoFragment;

        for (int i = 0; i < groupNameList.size(); i++) {
            merchantPhotoFragment = new MerchantPhotoFragment();
            merchantPhotoFragment.groupId=groupNameList.get(i).groupId;
            merchantPhotoFragment.merchantId=groupNameList.get(i).merchantId;
            fragments.add(merchantPhotoFragment);
        }
        initViewPager();
    }

    private void initViewPager() {
        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tablayout.setViewPager(viewpager);
        viewpager.setOffscreenPageLimit(fragments.size()-1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return groupNameList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return groupNameList.get(position).groupName;
        }
    }
}
