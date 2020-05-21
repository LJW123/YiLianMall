package com.leshan.ylyj.view.activity.healthmodel;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.HealthAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.CountNumberView;
import com.leshan.ylyj.presenter.HealthPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;

import java.util.ArrayList;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.MyHealthListBean;
import rxfamily.entity.MyHealthTopMsgBean;
import rxfamily.entity.WeatherBean;

/**
 * 我的健康主页
 *
 * @author Ray_L_Pain
 * @date 2018/1/13 0013
 */

public class HealthHomeActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    private RelativeLayout layoutTitle;
    private LinearLayout layoutBack, layoutBackBlack;
    private LinearLayout layoutRight, layoutRightBlack;
    private ImageView ivBackWhite, ivBackBlack, ivRightWhite, ivRightBlack;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HealthAdapter adapter;

    private View errorView, emptyView, headView;
    private LinearLayout fruitLayout, familyLayout;
    private CountNumberView tvMoney;
    private TextView tvRefresh, tvTemp, tvCity, tvFeel, tvFruit, tvFamily, tvUp, tvHelpCount;

    private HealthPresenter presenter, wPresenter;
    private boolean getFirstPageDataFlag = true;
    private int page;
    private int bannerHeight = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_home);
        initView();
        initListener();
        presenter = new HealthPresenter(mContext, this, 0);
        wPresenter = new HealthPresenter(mContext, this, 1);
        getNetData();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) layoutTitle.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(mContext);
            layoutTitle.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            StatusBarUtil.setTranslucentForImageViewInFragment(HealthHomeActivity.this, 60, null);
        }
    }

    @Override
    protected void initView() {
        layoutTitle = findViewById(R.id.layout_title);
        layoutBack = (LinearLayout) findViewById(R.id.layout_back);
        layoutBackBlack = (LinearLayout) findViewById(R.id.layout_back_black);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layoutBackBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivBackWhite = findViewById(R.id.iv_back_white);
        ivBackBlack = findViewById(R.id.iv_back_black);
        ivRightWhite = findViewById(R.id.iv_right_white);
        ivRightBlack = findViewById(R.id.iv_right_black);
        tvTitle = findViewById(R.id.tv_title);

        layoutRight = (LinearLayout) findViewById(R.id.layout_right);
        layoutRightBlack = (LinearLayout) findViewById(R.id.layout_right_black);
        layoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(HealthHomeActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "myHealth");
                startActivity(intent);
            }
        });
        layoutRightBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(HealthHomeActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "myHealth");
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 300);

        if (headView == null) {
            headView = View.inflate(mContext, R.layout.head_health_home, null);
            tvTemp = headView.findViewById(R.id.tv_temp);
            tvCity = headView.findViewById(R.id.tv_city);
            tvFeel = headView.findViewById(R.id.tv_feel);
            tvMoney = headView.findViewById(R.id.tv_money);
            fruitLayout = headView.findViewById(R.id.health_result_ll);
            tvFruit = headView.findViewById(R.id.tv_health_fruit);
            familyLayout = headView.findViewById(R.id.my_family_ll);
            tvFamily = headView.findViewById(R.id.tv_my_family);
            tvUp = headView.findViewById(R.id.tv_up);
            tvHelpCount = headView.findViewById(R.id.tv_help_count);

            fruitLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipUtils.toHealthFruit(mContext);
                }
            });

            familyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipUtils.toMyFamily(mContext);
                }
            });

            tvUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipUtils.toMyFamily(mContext);
                }
            });
        }
        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getNetData();
                }
            });
        }
        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }

        if (adapter == null) {
            adapter = new HealthAdapter(R.layout.item_health_layout);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
    }

    private void initHead() {
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
    }

    @Override
    protected void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.i("2018年1月17日 11:07:16-走了刷新");
                getFirstPageDataFlag = true;
                getNetData();
                adapter.setEnableLoadMore(false);
            }
        });

        adapter.setOnLoadMoreListener(this, recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int distance = 0;

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                distance += dy;
                if (distance <= 0) {
                    Logger.i("iv_back:initListener");
                    Logger.i("iv_back:" + successOrError);
                    if (successOrError) {
                        layoutBack.setVisibility(View.VISIBLE);
                        layoutBackBlack.setVisibility(View.GONE);
                    } else {
                        layoutBack.setVisibility(View.GONE);
                        layoutBackBlack.setVisibility(View.VISIBLE);
                    }

                    tvTitle.setTextColor(Color.argb((int) 255, 255, 255, 255));

                    layoutRight.setVisibility(View.VISIBLE);
                    layoutRightBlack.setVisibility(View.GONE);

                    layoutTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                } else if (distance > 0 && distance <= bannerHeight) {
                    layoutBack.setVisibility(View.VISIBLE);
                    layoutBackBlack.setVisibility(View.VISIBLE);

                    float scale = (float) distance / bannerHeight;
                    float alpha = (255 * scale);
                    ivBackWhite.setImageAlpha((int) (255 * (1 - scale)));
                    ivRightWhite.setImageAlpha((int) (255 * (1 - scale)));
                    ivBackBlack.setImageAlpha((int) alpha);
                    ivRightBlack.setImageAlpha((int) alpha);

                    layoutRight.setVisibility(View.VISIBLE);
                    layoutRightBlack.setVisibility(View.VISIBLE);

                    tvTitle.setTextColor(Color.argb((int) alpha, 0, 0, 0));

                    layoutTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    layoutBack.setVisibility(View.GONE);
                    layoutBackBlack.setVisibility(View.VISIBLE);

                    tvTitle.setTextColor(Color.argb((int) 255, 0, 0, 0));

                    layoutRight.setVisibility(View.GONE);
                    layoutRightBlack.setVisibility(View.VISIBLE);

                    layoutTitle.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                }

            }
        });
    }

    @Override
    protected void initData() {
    }

    private boolean isRefresh = false;

    @Override
    protected void onResume() {
        super.onResume();

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_HEALTH_HOME, mContext, false);

        if (isRefresh) {
            Logger.i("走了onResume - home");
            getNetData();

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_HEALTH_HOME, isRefresh, mContext);
        }
    }

    private void getNetData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        wPresenter.getWeather(PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext) + "," + PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext));
        presenter.getMyHealthTopMsg();
        page = 0;
        presenter.getMyHealList(String.valueOf(page), String.valueOf(Constants.PAGE_COUNT));
    }

    private void getNextPageData() {
        page ++;
        presenter.getMyHealList(String.valueOf(page), String.valueOf(Constants.PAGE_COUNT));
    }

    @Override
    public void onCompleted() {
        Logger.i("2018年1月16日 16:36:36-走了onCompleted");
    }

    boolean successOrError = true;

    @Override
    public void onErrors(int flag, Throwable e) {
        Logger.i("2018年1月16日 16:36:36-走了onError"+e.toString());
        if (1 == flag) {
            tvTemp.setText("");
            tvCity.setText("");
            tvFeel.setText("");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setEmptyView(errorView);
            adapter.loadMoreFail();
            showToast(e.getMessage());
            Logger.i("iv_back:onErrors");
            successOrError = false;
        }

        netRequestEnd();
    }

    private boolean flag1 = false;
    private boolean flag3 = false;
    private boolean flag4 = false;

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("2018年1月16日 16:36:36-走了onNext");
        if (baseEntity instanceof MyHealthTopMsgBean) {
            MyHealthTopMsgBean topBean = (MyHealthTopMsgBean) baseEntity;

            tvMoney.showNumberWithAnimation(Float.parseFloat(topBean.data.AMOUNT), CountNumberView.INTREGEX);
            float beanNum = Float.parseFloat(topBean.data.APPLES_NUM);
            float joinNum = Float.parseFloat(topBean.data.JOIN_NUM);
            if (beanNum < 10000) {
                tvFruit.setText(topBean.data.APPLES_NUM);
            } else if (beanNum >= 10000 && beanNum < 10000000) {
                tvFruit.setText(MyBigDecimal.div(beanNum, 10000, 2) + "万");
            } else {
                tvFruit.setText("999万+");
            }

            if (joinNum >= 1000) {
                tvFamily.setText("999+");
            } else {
                tvFamily.setText(topBean.data.JOIN_NUM);
            }

            if (TextUtils.isEmpty(topBean.data.total)) {
                tvHelpCount.setVisibility(View.GONE);
            } else {
                tvHelpCount.setVisibility(View.VISIBLE);
                tvHelpCount.setText(topBean.data.total + "人加入");
            }

            flag1 = true;
        } else if (baseEntity instanceof MyHealthListBean) {
            MyHealthListBean listBean = (MyHealthListBean) baseEntity;

            if (getFirstPageDataFlag) {
                initHead();
                getFirstPageDataFlag = false;
            }

            ArrayList<MyHealthListBean.DataBean.ListBean> newList = listBean.data.projects;
            if (page <= 0) {
                adapter.setNewData(newList);
                if (newList.size() >= Constants.PAGE_COUNT) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                }
            } else {
                if (newList.size() >= Constants.PAGE_COUNT) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                }
                adapter.addData(newList);
            }

            recyclerView.setVisibility(View.VISIBLE);

            flag3 = true;
        } else if (baseEntity instanceof WeatherBean) {
            WeatherBean weatherBean = (WeatherBean) baseEntity;

            tvTemp.setText(weatherBean.data.temp + "°C");
            tvCity.setText(weatherBean.data.city + "  " + weatherBean.data.weather);
            tvFeel.setText(weatherBean.data.aqi);

            flag4 = true;
        }

        if (flag1 && flag3 && flag4) {
            netRequestEnd();
        }
    }

    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }
}
