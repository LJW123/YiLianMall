package com.yilian.luckypurchase.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyTimeAdapter;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.CalculateEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author LYQ 时间详情
 */
public class LuckyTimeDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView ivMore;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RecyclerView recycleView;
    private SwipeRefreshLayout refreshLayout;
    private String snatchIndex;
    private LuckyTimeAdapter adapter;
    private boolean getFirstPageDataFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_time_details);
        snatchIndex = getIntent().getStringExtra("activity_id");
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    private void initData() {
        refreshLayout.setRefreshing(true);
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .snatchCalculate(snatchIndex, new Callback<CalculateEntity>() {
                    @Override
                    public void onResponse(Call<CalculateEntity> call, Response<CalculateEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        List<CalculateEntity.SnatchLogBean> snatchLog = response.body().snatchLog;
                                        if (null==snatchLog || snatchLog.size() <= 0){
                                            adapter.setEmptyView(getEmptyView());
                                        } else {
                                            adapter.setNewData(snatchLog);
                                            initHead(response.body());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<CalculateEntity> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        adapter.setEmptyView(getErrorView());
                    }
                });
    }

    private void initHead(CalculateEntity body) {
        View headView = View.inflate(mContext,R.layout.lucky_item_time_details_head,null);
        TextView tvCalculateView= (TextView) headView.findViewById(R.id.tv_calculate);
        tvCalculateView.append(body.baseNum);
        TextView tvNumberA= (TextView) headView.findViewById(R.id.tv_number_a);
        tvNumberA.append(body.resTime);
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
    }

    private void initView() {
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("时间详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivMore = (ImageView) findViewById(R.id.v3Shop);
        ivMore.setImageResource(R.mipmap.library_module_v3_more_bottom);
        ivMore.setVisibility(View.VISIBLE);
        ivMore.setOnClickListener(this);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recycleView.setLayoutManager(linearLayoutManager);
        adapter = new LuckyTimeAdapter(R.layout.lucky_item_time_details);
        recycleView.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.lucky_color_red));

        v3Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_refresh) {
            initData();
        } else if (i == R.id.v3Shop) {
            showMenu();
        }
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Shop);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent.setComponent(new ComponentName(LuckyTimeDetailsActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyTimeDetailsActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyTimeDetailsActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyTimeDetailsActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

    public View getEmptyView() {
        View emptyView = null;
        if (null == emptyView) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }
        return emptyView;
    }

    public View getErrorView() {
        View errorView = null;
        if (null == errorView) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            TextView tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(this);
        }
        return errorView;
    }
}
