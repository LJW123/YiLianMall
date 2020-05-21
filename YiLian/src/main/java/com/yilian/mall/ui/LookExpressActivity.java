package com.yilian.mall.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.LookExpressAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ExpressInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 查看物流
 *
 * @author Ray_L_Pain
 * @date 2017/12/7 0007
 */

public class LookExpressActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;

    ImageView iv;
    TextView tvJustTv;
    TextView tvStatus;
    TextView tvCompany;
    TextView tvNumber;
    TextView tvCopy;

    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycleView)
    RecyclerView recyclerView;
    ExpressInfoEntity entity;
    private LookExpressAdapter adapter;
    private ArrayList<ExpressInfoEntity.ListBean> list = new ArrayList<>();
    private String expressNum, expressImg;
    private View emptyView, errorView, headerView;
    private ImageView emptyIv;
    private TextView tvRefresh;
    private String expressCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_express);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        expressNum = getIntent().getStringExtra("express_number");
        expressImg = getIntent().getStringExtra("express_img");
        expressCompany = getIntent().getStringExtra("express_company");

        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
            emptyIv = (ImageView) emptyView.findViewById(R.id.iv_empty);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) emptyIv.getLayoutParams();
            params.setMargins(0, DPXUnitUtil.dp2px(mContext, 200f), 0, 0);
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    initData();
                }
            });
        }

        if (headerView == null) {
            headerView = View.inflate(mContext, R.layout.header_look_express, null);
            iv = (ImageView) headerView.findViewById(R.id.iv);
            tvStatus = (TextView) headerView.findViewById(R.id.tv_status);
            tvCompany = (TextView) headerView.findViewById(R.id.tv_company);
            tvNumber = (TextView) headerView.findViewById(R.id.tv_number);
            tvCopy = (TextView) headerView.findViewById(R.id.tv_copy);
            tvJustTv = (TextView) headerView.findViewById(R.id.tv_just_tv);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitle.setText("查看物流");

        if (!TextUtils.isEmpty(expressNum)) {
            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(expressNum);
                    showToast("复制成功");
                }
            });
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new LookExpressAdapter(R.layout.item_look_express);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    private void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        RetrofitUtils2.getInstance(mContext).getExpressInfo(expressNum, new Callback<ExpressInfoEntity>() {
            @Override
            public void onResponse(Call<ExpressInfoEntity> call, Response<ExpressInfoEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        stopMyDialog();
                        entity = response.body();
                        switch (entity.code) {
                            case 1:
                                initHead();
                                ArrayList<ExpressInfoEntity.ListBean> newList = entity.list;
                                Logger.i("2017年12月7日 11:39:33-" + newList.size());
                                if (newList.size() > 0 && newList != null) {
                                    adapter.setNewData(newList);
                                    adapter.loadMoreComplete();
                                    list.clear();
                                    if (adapter.getFooterLayoutCount() != 0) {
                                        adapter.removeAllFooterView();
                                    }
                                } else {
                                    adapter.setNewData(newList);
                                    initFoot();
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        if (adapter.getHeaderLayoutCount() == 0) {
                            adapter.addHeaderView(headerView);
                        }
                        GlideUtil.showImage(mContext, expressImg, iv);
                        tvStatus.setText("物流状态：暂无");
                        tvCompany.setText("物流公司：" + expressCompany);
                        tvNumber.setText("运单编号：" + expressNum);
                        tvJustTv.setVisibility(View.GONE);
                        initFoot();
                    }
                }
                netRequestEnd();
            }

            @Override
            public void onFailure(Call<ExpressInfoEntity> call, Throwable t) {
                adapter.setEmptyView(errorView);
                netRequestEnd();
                showToast(R.string.merchant_module_service_exception);
            }
        });
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(false);
    }

    private void initHead() {
        if (adapter.getFooterLayoutCount() != 0) {
            adapter.removeAllFooterView();
        }

        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headerView);
        }
        tvJustTv.setVisibility(View.VISIBLE);

        GlideUtil.showImage(mContext, expressImg, iv);
        if (TextUtils.isEmpty(entity.info.deliv)) {
            tvStatus.setText("物流状态：暂无");
        } else {
            String delivStr = "";
            switch (entity.info.deliv) {
                case "1":
                    delivStr = "在途中";
                    break;
                case "2":
                    delivStr = "派件中";
                    break;
                case "3":
                    delivStr = "已签收";
                    break;
                case "4":
                    delivStr = "派件失败";
                    break;
                default:
                    delivStr = "暂无";
                    break;
            }
            tvStatus.setText(Html.fromHtml("<font color=\"#333333\">物流状态：</font><font color=\"#F25024\">" + delivStr + "</font>"));
        }

        if (TextUtils.isEmpty(entity.info.express_name)) {
            tvCompany.setText("物流公司：" + expressCompany);
        } else {
            tvCompany.setText("物流公司：" + entity.info.express_name);
        }

        tvNumber.setText("运单编号：" + expressNum);
    }

    private void initFoot() {
        if (adapter.getFooterLayoutCount() == 0) {
            adapter.addFooterView(emptyView);
        }
    }

}
