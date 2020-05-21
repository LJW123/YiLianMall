package com.yilian.mall.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.GoodRegionViewPagerAdapter;
import com.yilian.mall.entity.GoodsList;
import com.yilian.mall.entity.MallGoodsList;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 兑换中心商品列表界面
 */
public class FilialeGoodListActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    TextView mTvTitle;

    @ViewInject(R.id.vp_good_list)
    ViewPager mVpGoodList;

    @ViewInject(R.id.rg_good_region)
    RadioGroup mRgGoodRegion;

    @ViewInject(R.id.rb_legou)
    RadioButton mRbLegou;

    @ViewInject(R.id.rb_lehuan)
    RadioButton mRbLehuan;

    @ViewInject(R.id.rb_lexuan)
    RadioButton mRbLexuan;


    private MallNetRequest mMallRequest;
    private List<List<MallGoodsList>> mGoodPagers;
    private GoodRegionViewPagerAdapter mAdapter;
    private List<MallGoodsList> mLegouGoodList=new ArrayList<>();
    private List<MallGoodsList> mLehuanGoodList=new ArrayList<>();
    private List<MallGoodsList> mLexuanGoodList=new ArrayList<>();
    private Drawable drawableBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filiale_good_list);
        ViewUtils.inject(this);
        init();
        registerEvents();
        getFilialeGoodList(Constants.FILIALE_GOOD_REGION_LEGOU);
    }

    private void init(){
        mTvTitle.setText(getIntent().getStringExtra("filiale_name"));
        drawableBottom = getResources().getDrawable(R.drawable.line_blue);
        drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        mMallRequest=new MallNetRequest(mContext);
        mGoodPagers=new ArrayList<>(3);
        mGoodPagers.add(0,mLegouGoodList);
        mGoodPagers.add(1,mLehuanGoodList);
        mGoodPagers.add(2,mLexuanGoodList);
        mAdapter=new GoodRegionViewPagerAdapter(mContext,mGoodPagers);
        mVpGoodList.setAdapter(mAdapter);

    }

    private void registerEvents(){

        mRgGoodRegion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_legou:
                        mVpGoodList.setCurrentItem(0);
                        mRbLegou.setCompoundDrawables(null, null, null, drawableBottom);
                        mRbLehuan.setCompoundDrawables(null, null, null, null);
                        mRbLexuan.setCompoundDrawables(null, null, null, null);
                        break;
                    case R.id.rb_lehuan:
                        mVpGoodList.setCurrentItem(1);
                        mRbLegou.setCompoundDrawables(null, null, null, null);
                        mRbLehuan.setCompoundDrawables(null, null, null, drawableBottom);
                        mRbLexuan.setCompoundDrawables(null, null, null, null);
                        break;
                    case R.id.rb_lexuan:
                        mVpGoodList.setCurrentItem(2);
                        mRbLegou.setCompoundDrawables(null, null, null, null);
                        mRbLehuan.setCompoundDrawables(null, null, null, null);
                        mRbLexuan.setCompoundDrawables(null, null, null, drawableBottom);
                        break;
                }
            }
        });

        mVpGoodList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mRgGoodRegion.check(R.id.rb_legou);
                        if (mLegouGoodList.size()==0){
                            getFilialeGoodList(Constants.FILIALE_GOOD_REGION_LEGOU);
                        }
                        break;
                    case 1:
                        mRgGoodRegion.check(R.id.rb_lehuan);
                        if (mLehuanGoodList.size()==0){
                            getFilialeGoodList(Constants.FILIALE_GOOD_REGION_LEHUAN);
                        }
                        break;
                    case 2:
                        mRgGoodRegion.check(R.id.rb_lexuan);
                        if (mLexuanGoodList.size()==0){
                            getFilialeGoodList(Constants.FILIALE_GOOD_REGION_LEXUAN);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getFilialeGoodList(final int type){
        startMyDialog();
        String filialeId=getIntent().getStringExtra("filiale_id");//"6";
        mMallRequest.getFilialeGoodList(filialeId, type, GoodsList.class, new RequestCallBack<GoodsList>() {
            @Override
            public void onSuccess(ResponseInfo<GoodsList> responseInfo) {
                stopMyDialog();
                mLegouGoodList.clear();
                mLehuanGoodList.clear();
                mLexuanGoodList.clear();
                if (responseInfo==null||responseInfo.result==null) return;
                if (responseInfo.result.code==1){
                    if (Constants.FILIALE_GOOD_REGION_LEGOU==type){
                        mLegouGoodList.addAll(responseInfo.result.list);
                        if(mLegouGoodList.size()==0){
                            mAdapter.setDataView(0,false);
                        }else {
                            mAdapter.setDataView(0,true);
                            mAdapter.notifyDataSetChanged(0);
                        }
                    }else if (Constants.FILIALE_GOOD_REGION_LEHUAN==type){
                        mLehuanGoodList.addAll(responseInfo.result.list);
                        if(mLehuanGoodList.size()==0){
                            mAdapter.setDataView(1,false);
                        }else {
                            mAdapter.setDataView(1,true);
                            mAdapter.notifyDataSetChanged(1);
                        }
                    }else if (Constants.FILIALE_GOOD_REGION_LEXUAN==type){
                        mLexuanGoodList.addAll(responseInfo.result.list);
                        if(mLexuanGoodList.size()==0){
                            mAdapter.setDataView(2,false);
                        }else {
                            mAdapter.setDataView(2,true);
                            mAdapter.notifyDataSetChanged(2);
                        }
                    }

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }
}
