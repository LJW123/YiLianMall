package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActEvaDetailAdapter;
import com.yilian.mall.adapter.ImageRecycleAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.ActEvaDetailEntity;
import com.yilian.networkingmodule.entity.GALZanEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  猜价格碰运气活动-评论详情
 * @author Ray_L_Pain
 * @date 2017/12/8 0008
 */

public class ActEvaDetailActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;
    @ViewInject(R.id.layout_bottom)
    LinearLayout bottomLayout;
    @ViewInject(R.id.et)
    EditText et;
    @ViewInject(R.id.btn)
    TextView btn;
    @ViewInject(R.id.iv)
    ImageView iv;

    private View emptyView, errorView, headView, nothingView;
    private ImageView headIv;
    private TextView tvHeadName, tvHeadZan, tvHeadContent, tvHeadTime, tvHeadDesc;
    private RecyclerView recycleViewHead;

    private int page = 0;
    private ActEvaDetailAdapter adapter;
    private ArrayList<ActEvaDetailEntity.ListBean> list = new ArrayList<>();
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;
    private TextView tvRefresh;

    private String id;
    private ActEvaDetailEntity entity;
    private String isParise, commentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_eva_detail);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        id = getIntent().getStringExtra("comment_index");
        Logger.i("2017年12月12日 11:13:48-" + id);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("评价");
        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getFirstPageData();
                }
            });
        }

        if (headView == null) {
            headView = View.inflate(mContext, R.layout.header_act_eva_detail, null);
            headIv = (ImageView) headView.findViewById(R.id.iv_head);
            tvHeadName = (TextView) headView.findViewById(R.id.tv_head_name);
            tvHeadZan = (TextView) headView.findViewById(R.id.tv_head_zan);
            tvHeadContent = (TextView) headView.findViewById(R.id.tv_head_content);
            recycleViewHead = (RecyclerView) headView.findViewById(R.id.recycler_view_head);
            tvHeadTime = (TextView) headView.findViewById(R.id.tv_head_time);
            tvHeadDesc = (TextView) headView.findViewById(R.id.tv_head_desc);
        }

        if (nothingView == null) {
            nothingView = View.inflate(mContext, R.layout.act_view_nothing, null);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new ActEvaDetailAdapter(R.layout.item_new_evaluate);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));

        adapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    showToast(R.string.please_write_comment);
                    return;
                }
                startMyDialog();
                RetrofitUtils2.getInstance(mContext).galReplyComment(id, et.getText().toString(), new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        stopMyDialog();
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        showToast(body.msg);
                                        et.setText("");
                                        if (swipeRefreshLayout != null) {
                                            swipeRefreshLayout.setRefreshing(true);
                                        }
                                        getFirstPageDataFlag = true;
                                        getFirstPageData();
                                        adapter.setEnableLoadMore(false);
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
                        stopMyDialog();
                    }
                });
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPraise;
                if ("0".equals(isParise)) {
                    newPraise = "1";
                } else {
                    newPraise = "2";
                }
                RetrofitUtils2.getInstance(mContext).setPraise(commentIndex, "3", newPraise, new Callback<GALZanEntity>() {
                    @Override
                    public void onResponse(Call<GALZanEntity> call, Response<GALZanEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        if (swipeRefreshLayout != null) {
                                            swipeRefreshLayout.setRefreshing(true);
                                        }
                                        getFirstPageDataFlag = true;
                                        getFirstPageData();
                                        break;
                                    default:
                                        showToast(body.msg);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GALZanEntity> call, Throwable t) {
                        showToast(R.string.net_work_not_available_zan);
                    }
                });
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ActEvaDetailEntity.ListBean item = (ActEvaDetailEntity.ListBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_zan:
                        String isParise;
                        if ("0".equals(item.is_parise)) {
                            isParise = "1";
                        } else {
                            isParise = "2";
                        }
                        int zanCount = item.countPraise;
                        RetrofitUtils2.getInstance(mContext).setPraise(item.reply_index, "4", isParise, new Callback<GALZanEntity>() {
                            @Override
                            public void onResponse(Call<GALZanEntity> call, Response<GALZanEntity> response) {
                                HttpResultBean body = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                        switch (body.code) {
                                            case 1:
                                                GALZanEntity entity = response.body();

                                                int newCount = entity.pariseNum;
                                                item.countPraise = newCount;
                                                if (zanCount > newCount) {
                                                    //取消赞
                                                    item.is_parise = "0";
                                                } else {
                                                    //点赞
                                                    item.is_parise = "1";
                                                }

                                                adapter.notifyItemChanged(position + 1, item);
                                                break;
                                            default:
                                                showToast(body.msg);
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GALZanEntity> call, Throwable t) {
                                showToast(R.string.net_work_not_available_zan);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
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

    private void getData() {
        RetrofitUtils2.getInstance(mContext).galEvaDetail(id, String.valueOf(page), "30", new Callback<ActEvaDetailEntity>() {
            @Override
            public void onResponse(Call<ActEvaDetailEntity> call, Response<ActEvaDetailEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        entity = response.body();
                        switch (response.body().code) {
                            case 1:
                                ActEvaDetailEntity.InfoBean infoBean = entity.info;
                                commentIndex = infoBean.comment_index;
                                Logger.i("onResume:" + infoBean.toString());

                                ArrayList<ActEvaDetailEntity.ListBean> newList = entity.reply_list;
                                Logger.i("onResume:" + newList.size());

                                if (newList != null && newList.size() > 0) {
                                    if (page > 0) {
                                        adapter.addData(newList);
                                    } else {
                                        getFirstPageDataFlag = false;
                                        adapter.setNewData(newList);
                                    }
                                    if (newList.size() < Constants.PAGE_COUNT) {
                                        adapter.loadMoreEnd();
                                    } else {
                                        adapter.loadMoreComplete();
                                    }

                                    if (null == infoBean.comment_index) {
                                        bottomLayout.setVisibility(View.GONE);
                                    } else {
                                        initHeadView();
                                    }
                                } else {
                                    if (page == 0) {
                                        adapter.setNewData(newList);

                                        if (null == infoBean.comment_index) {
                                            bottomLayout.setVisibility(View.GONE);
                                            adapter.setEmptyView(emptyView);
                                        } else {
                                            initHeadView();
                                            initFootView();
                                        }
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
            public void onFailure(Call<ActEvaDetailEntity> call, Throwable t) {
                if (page == 0) {
                    adapter.setEmptyView(errorView);
                } else if (page > 0) {
                    page--;
                    if (adapter.getEmptyViewCount() > 0) {
                        ((ViewGroup) adapter.getEmptyView()).removeAllViews();
                    }
                    adapter.notifyDataSetChanged();
                }
                adapter.loadMoreFail();
                netRequestEnd();
                showToast(R.string.system_busy);
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

    private void initFootView() {
        if (adapter.getFooterLayoutCount() == 0) {
            adapter.addFooterView(nothingView);
            Logger.i("2017年12月12日 11:13:48-走了initFootView");
        }
    }

    private void initHeadView() {
        if (adapter.getFooterLayoutCount() != 0) {
            adapter.removeAllFooterView();
        }

        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
            Logger.i("2017年12月12日 11:13:48-走了initHeadView");
        }

        ActEvaDetailEntity.InfoBean infoBean = entity.info;
        bottomLayout.setVisibility(View.VISIBLE);
        GlideUtil.showCirImageNoLit(mContext, infoBean.user_photo, headIv, commentIndex);
        if (TextUtils.isEmpty(infoBean.comment_consumer)) {
            tvHeadName.setText("暂无昵称");
        } else {
            tvHeadName.setText(infoBean.comment_consumer);
        }
        tvHeadZan.setText(String.valueOf(infoBean.countPraise));
        Drawable drawable;
        isParise = infoBean.is_parise;
        Logger.i("ray-" + isParise);
        if ("0".equals(isParise)) {
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_off);
        } else {
            drawable = mContext.getResources().getDrawable(R.mipmap.act_zan_on);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvHeadZan.setCompoundDrawables(drawable, null, null, null);

        tvHeadZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPraise;
                if ("0".equals(isParise)) {
                    newPraise = "1";
                } else {
                    newPraise = "2";
                }
                RetrofitUtils2.getInstance(mContext).setPraise(commentIndex, "3", newPraise, new Callback<GALZanEntity>() {
                    @Override
                    public void onResponse(Call<GALZanEntity> call, Response<GALZanEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        if (swipeRefreshLayout != null) {
                                            swipeRefreshLayout.setRefreshing(true);
                                        }
                                        getFirstPageDataFlag = true;
                                        getFirstPageData();
                                        break;
                                    default:
                                        showToast(body.msg);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GALZanEntity> call, Throwable t) {
                        showToast(R.string.net_work_not_available_zan);
                    }
                });
            }
        });

        tvHeadContent.setText(infoBean.comment_content);
        String[] imageUrls = infoBean.comment_images.split(",");
        for (int i = 0; i < imageUrls.length; i++) {
            if (!TextUtils.isEmpty(imageUrls[i])) {
                imageUrls[i] = Constants.ImageUrl + imageUrls[i];
                recycleViewHead.setVisibility(View.VISIBLE);
            } else {
                recycleViewHead.setVisibility(View.GONE);
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleViewHead.setLayoutManager(layoutManager);
        ImageRecycleAdapter bigAdapter = new ImageRecycleAdapter(R.layout.lucky_item_show_image_big, new ArrayList<String>(Arrays.asList(imageUrls)));
        recycleViewHead.setAdapter(bigAdapter);
        bigAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                imageBrower(position, imageUrls);
            }
        });

        tvHeadTime.setText(DateUtils.timeStampToStr2(Long.parseLong(infoBean.comment_time)));
        tvHeadDesc.setText(infoBean.goods_name);

        if ("0".equals(isParise)) {
            iv.setImageResource(R.mipmap.act_zan_off_big);
        } else {
            iv.setImageResource(R.mipmap.act_zan_on_big);
        }
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }

}
