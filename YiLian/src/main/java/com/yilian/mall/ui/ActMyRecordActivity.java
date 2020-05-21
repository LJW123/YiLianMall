package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActMyRecordAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.ActMyRecordEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 猜价格-碰运气 我的战绩
 * @author Ray_L_Pain
 */
public class ActMyRecordActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    private View emptyView, errorView;
    private TextView tvRefresh;

    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    private int page = 0;
    private ActMyRecordAdapter adapter;
    private ArrayList<ActMyRecordEntity.ListBean> list = new ArrayList<>();
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;

    private String actType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_my_record);

        ViewUtils.inject(this);
        initView();
        initListener();
    }

    private void initView() {
        actType = getIntent().getStringExtra("act_type");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("我的战绩");

        emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                getFirstPageData();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new ActMyRecordAdapter(R.layout.item_act_my_record, actType);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFirstPageDataFlag = true;
        initData();
    }

    private void initData() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeRefreshLayout != null) {
                                swipeRefreshLayout.setRefreshing(true);
                            }
                            getFirstPageData();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
                    iv_return_top.setVisibility(View.VISIBLE);
                } else {
                    iv_return_top.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        iv_return_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        adapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ActMyRecordEntity.ListBean bean = (ActMyRecordEntity.ListBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_right:
                        Intent intent = null;
                        switch (bean.orderStatus) {
                            case "0":
                                /**
                                 * 确认订单
                                 */
                                intent = new Intent(ActMyRecordActivity.this, ActivityDetailActivity.class);
                                intent.putExtra("goods_id", bean.goodsId);
                                intent.putExtra("activity_id", bean.actIntegralGoodsId);
                                intent.putExtra("activity_type", actType);
                                intent.putExtra("is_show", "1");
                                intent.putExtra("activity_orderId", bean.orderId);
                                startActivity(intent);
                                break;
                            case "1":
                                /**
                                 * 待支付去交押金
                                 * 跳转到收银台界面
                                 */
                                intent = new Intent(ActMyRecordActivity.this, CashDeskActivity.class);
                                intent.putExtra("type", "ActivityDetail");
                                intent.putExtra("orderId", bean.orderId);
                                intent.putExtra("orderIndex", bean.orderIndex);
                                intent.putExtra("order_total_lebi", "0");
                                intent.putExtra("order_total_integral", bean.orderIntegral);
                                intent.putExtra("payType", "1");
                                startActivity(intent);
                                break;
                            case "4":
                            case "5":
                                /**
                                 * 确认收货
                                 */
                                confirmOrder(bean.orderIndex, position);
                                break;
                            case "8":
                                /**
                                 * 待评价
                                 */
                                intent = new Intent(ActMyRecordActivity.this, ActEvaluateActivity.class);
                                intent.putExtra("goods_index", bean.orderGoodsIndex);
                                intent.putExtra("goods_icon", bean.goodsIcon);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 确认收货
     */
    private void confirmOrder(String index, int position) {
        RetrofitUtils2.getInstance(mContext).galConfirmOrder(index, new Callback<HttpResultBean>() {
            @Override
            public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                showToast(body.msg);
                                ActMyRecordEntity.ListBean item = adapter.getItem(position);
                                item.orderStatus="8";
                                adapter.notifyItemChanged(position, item);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<HttpResultBean> call, Throwable t) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).galMyRecordList(actType, String.valueOf(page), "30", new Callback<ActMyRecordEntity>() {
            @Override
            public void onResponse(Call<ActMyRecordEntity> call, Response<ActMyRecordEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        ActMyRecordEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                ArrayList<ActMyRecordEntity.ListBean> newList = entity.list;
                                if (newList.size() > 0 && newList != null) {
                                    if (getFirstPageDataFlag) {
                                        adapter.setNewData(newList);
                                        getFirstPageDataFlag = false;
                                        list.clear();
                                    } else {
                                        adapter.addData(newList);
                                    }
                                    list.addAll(newList);

                                    if (newList.size() < Constants.PAGE_COUNT) {
                                        adapter.loadMoreEnd();
                                    } else {
                                        adapter.loadMoreComplete();
                                    }
                                } else {
                                    if (page == 0) {
                                        adapter.setNewData(newList);
                                        adapter.setEmptyView(emptyView);
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
            public void onFailure(Call<ActMyRecordEntity> call, Throwable t) {
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


    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }

}
