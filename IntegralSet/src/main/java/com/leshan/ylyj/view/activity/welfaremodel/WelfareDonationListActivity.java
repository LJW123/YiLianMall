package com.leshan.ylyj.view.activity.welfaremodel;


import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.WelfareListAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.WelfarePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FontUtils;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.PreferenceUtils;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.WelfareListEntity;
import rxfamily.entity.WelfareLoveEntity;


/**
 * 获取公益列表
 *
 * @author zhaiyaohua on 2018/1/11 0011.
 */

public class WelfareDonationListActivity extends BaseActivity implements View.OnClickListener {
    private ImageView right, left;
    private TextView middleTitile, tvLoveNum;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private WelfareListAdapter mAdapter;
    private  WelfarePresenter  mPresenter;
    private WelfarePresenter listPresenter;
    private Integer pager = 0;
    private View emptyView;
    private View errorView;
    private WelfareListEntity welfareListEntity;
    private String totalIntegral;
    private String fromCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_list);
        PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true, mContext);
        initView();
        initListener();
        initData();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        right = (ImageView) findViewById(R.id.right_image);
        right.setVisibility(View.VISIBLE);
        left = (ImageView) findViewById(R.id.left_back);
        middleTitile = (TextView) findViewById(R.id.middle_titile);
        tvLoveNum = findViewById(R.id.tv_love_num);
        middleTitile.setText("爱心捐赠");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_icon_color));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mAdapter = new WelfareListAdapter(R.layout.item_welfare_list);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 设置爱心数量
     */
    private void setLoveNum(String loveNum) {
        if (null == loveNum) {
            return;
        }
        loveNum = "凝聚" + formatLoveNum(loveNum) + "爱心";
        SpannableString spannableString = new SpannableString(loveNum);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FA5C16"));
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(27, true);
        spannableString.setSpan(colorSpan, 2, loveNum.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, 2, loveNum.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Typeface typeface = FontUtils.getFontTypeface(mContext, "fonts/DINMittelschrift.otf");
        tvLoveNum.setTypeface(typeface);
        tvLoveNum.setText(spannableString);
    }


    /**
     * 格式化爱心数量
     *
     * @param loveNum
     * @return
     */
    private String formatLoveNum(String loveNum) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" ");
        for (int i = 0; i < loveNum.length(); i++) {
            buffer.append(loveNum.charAt(i));
            if ((i + 1) % 3 == 0 && i != loveNum.length() - 1) {
                buffer.append(",");
            }
        }
        return buffer.toString() + " ";
    }

    @Override
    protected void initListener() {
        right.setOnClickListener(this);
        left.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWelfareListFirstData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Logger.i("获取了下一页");
                getWelfareLisNextData();
            }
        }, recyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WelfareListEntity.Data.Details item = (WelfareListEntity.Data.Details) adapter.getItem(position);
                String id = item.id;
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                String url =  Constants.WELFARE_LOVE_DONATION+ id;
                intent.putExtra("url", url);
                startActivityForResult(intent,0);
            }
        });
    }


    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new WelfarePresenter(mContext, this);
        }
        //列表请求
        listPresenter = new WelfarePresenter(mContext, this, 1);
        fromCode=getIntent().getStringExtra("from");
        if (null!=fromCode){
            Intent intent = new Intent(mContext, WelfarePersonInfoActivity.class);
            SkipUtils.toCareDonation(this, intent);
        }
    }

    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
            //请求网络获取爱心列表
            pager = 0;
            getWelfareListData();
            swipeRefreshLayout.setRefreshing(true);
            isFirst = false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.left_image) {
            finish();
        } else if (id == R.id.right_image) {
            Intent intent = new Intent(mContext, WelfarePersonInfoActivity.class);
            SkipUtils.toCareDonation(this, intent);
        } else if (id == R.id.left_back) {
            finish();
        }
    }

    @Override
    public void onCompleted() {
        if (welfareListEntity!=null){
            swipeRefreshLayout.setRefreshing(false);

        }

    }

    @Override
    public void onErrors(int flag, Throwable e) {
        swipeRefreshLayout.setRefreshing(false);
        showToast(e.getMessage());
        if (1 == flag) {
            if (pager > 0) {
                pager--;
            } else {
                if (errorView == null) {
                    errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
                }
                errorView.findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWelfareListFirstData();
                        //请求网络获取爱心数量
                        mPresenter.getWelfareLoveNum();
                    }
                });
                mAdapter.setEmptyView(errorView);
            }
        }
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
            if (baseEntity instanceof WelfareListEntity) {
             welfareListEntity = (WelfareListEntity) baseEntity;
                 totalIntegral=welfareListEntity.data.totalIntegral;
                 Integer integral=Integer.parseInt(totalIntegral)/100;
                 setLoveNum(integral+"");
            setWelfareListData(welfareListEntity);
        }

    }

    /**
     * 请求公益列表下一页数据
     */
    private void getWelfareLisNextData() {
        pager++;
        getWelfareListData();
    }

    /**
     * 下拉刷新获取第一页数据
     */
    private void getWelfareListFirstData() {
        pager = 0;
        getWelfareListData();
    }

    /**
     * 获取公益列表数据
     */
    private void getWelfareListData() {
        listPresenter.getWelfareList(Constants.PAGE_COUNT_20, pager);
    }

    /**
     * 设置公益列表数据
     */
    private  void setWelfareListData(WelfareListEntity baseEntity) {
                if (pager <= 0) {
                    if (null == baseEntity.data || baseEntity.data.list.size() <= 0) {
                        if (emptyView == null) {
                            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
                        }
                        mAdapter.loadMoreEnd();
                        mAdapter.setEmptyView(emptyView);
                    } else {
                        mAdapter.setNewData(baseEntity.data.list);
                        mAdapter.loadMoreEnd();
                    }
                } else {
                    //条数大于分页条数继续上拉继续可以加载
                    mAdapter.addData(baseEntity.data.list);
                    if (Constants.PAGE_COUNT_20 <= baseEntity.data.list.size()) {
                        mAdapter.loadMoreComplete();
                    } else {
                        mAdapter.loadMoreEnd();
                    }
                }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getWelfareListFirstData();
        if (1==resultCode){
            finish();
        }
        if (WelfareLoveMsgActivity.TO_PERSON_DETAIL==resultCode){
            Intent intent = new Intent(mContext, WelfarePersonInfoActivity.class);
            startActivity(intent);
        }
    }
}
