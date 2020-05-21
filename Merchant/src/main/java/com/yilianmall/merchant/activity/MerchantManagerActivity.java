package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.MerchantManagerListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.MerchantManagerListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Ray_L_Pain
 * @date 2017/9/28 0028
 * 商品管理页面
 */

public class MerchantManagerActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    public static final int SEARCH_SUCCESS = 1;
    private ImageView ivBack;
    private TextView tvTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ImageView ivReturnTop;
    private View emptyView, errorView;
    private TextView tvRefresh, tvNoData;
    private EditText etHead;
    private ImageView ivHeadSearch;
    private MerchantManagerListAdapter adapter;
    private int page;
    private boolean getFirstPageDataFlag = true;
    private ArrayList<MerchantManagerListEntity.DataBean> list = new ArrayList<>();
    private String etStr = ""; //搜索的string
    private boolean isRefresh = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH_SUCCESS:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                ivHeadSearch.setClickable(true);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_manager);

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_MERCHANT_MANAGER_LIST, mContext, false);

        if (isRefresh) {
            Logger.i("ray-" + "走了onResume");
            getFirstPageDataFlag = true;
            initData();

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_MANAGER_LIST, false, mContext);
        }
    }

    private void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getFirstPageData();
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                .getMerchantManagerList(String.valueOf(page), "20", etStr, new Callback<MerchantManagerListEntity>() {
                    @Override
                    public void onResponse(Call<MerchantManagerListEntity> call, Response<MerchantManagerListEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                MerchantManagerListEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        ArrayList<MerchantManagerListEntity.DataBean> newList = entity.data;
                                        if (newList.size() > 0 && newList != null) {
                                            if (getFirstPageDataFlag) {
                                                adapter.setNewData(newList);
                                                getFirstPageDataFlag = false;
                                                list.clear();
                                            } else {
                                                adapter.addData(newList);
                                            }
                                            list.addAll(newList);

                                            if (newList.size() < Constants.PAGE_COUNT_20) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
                                                adapter.setNewData(newList);
                                                adapter.setEmptyView(emptyView);
                                            } else {
                                                adapter.loadMoreEnd();
                                            }
                                        }

                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<MerchantManagerListEntity> call, Throwable t) {
                        if (page == 0) {
                            adapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                        }
                        adapter.loadMoreFail();
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
        adapter.setEnableLoadMore(true);
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("商品管理");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setFocusable(false);
        recyclerView.setFocusableInTouchMode(false);
        ivReturnTop = (ImageView) findViewById(R.id.iv_return_top);
        //
        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.merchant_barcode_nothing, null);
            tvNoData = (TextView) emptyView.findViewById(R.id.tv_no_data);
            tvNoData.setText("暂无更多数据");
        }
        //
        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getData();
                }
            });
        }

        etHead = (EditText) findViewById(R.id.et);
        ivHeadSearch = (ImageView) findViewById(R.id.iv_search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MerchantManagerListAdapter(R.layout.merchant_item_manager_list);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.merchant_color_red));
        swipeRefreshLayout.setEnabled(true);
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                if (scrollDy > ScreenUtils.getScreenHeight(mContext) * 3) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        ivReturnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        adapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });

        ivHeadSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etStr = etHead.getText().toString().trim();
                if (TextUtils.isEmpty(etStr)) {
                    etStr = "";
                }
                getFirstPageDataFlag = true;
                swipeRefreshLayout.setRefreshing(true);
                getFirstPageData();
                adapter.setEnableLoadMore(false);
                handler.sendEmptyMessage(SEARCH_SUCCESS);
                ivHeadSearch.setClickable(false);
            }
        });

        etHead.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    etStr = etHead.getText().toString().trim();
                    if (TextUtils.isEmpty(etStr)) {
                        etStr = "";
                    }
                    getFirstPageDataFlag = true;
                    swipeRefreshLayout.setRefreshing(true);
                    getFirstPageData();
                    adapter.setEnableLoadMore(false);
                    handler.sendEmptyMessage(SEARCH_SUCCESS);
                    ivHeadSearch.setClickable(false);

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }
}
