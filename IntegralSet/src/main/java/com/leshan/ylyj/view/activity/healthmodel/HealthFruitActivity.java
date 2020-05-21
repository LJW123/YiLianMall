package com.leshan.ylyj.view.activity.healthmodel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.leshan.ylyj.adapter.HealthFruitAdapter;
import com.leshan.ylyj.adapter.HealthFruitRuleAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.CountNumberView;
import com.leshan.ylyj.presenter.HealthPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.widget.MyRadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.HealthFruitListBean;
import rxfamily.entity.MyHealthFruitBean;


/**
 * 我的健康果
 *
 * @author Ray_L_Pain
 */
public class HealthFruitActivity extends BaseActivity {

    private HealthFruitAdapter adapter;
    private HealthFruitRuleAdapter ruleAdapter1;
    private HealthFruitRuleAdapter ruleAdapter2;
    private Dialog dialog;
    private View view;
    private AlertDialog.Builder builder1;

    private RelativeLayout titleLayout;
    private ImageView ivBack, ivBackBlack;
    private TextView tvTitle, tvRight;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View errorView, emptyView, headView, footView;
    private TextView tvRefresh;
    private CountNumberView tvCount;
    private LinearLayout exchangeLayout, robLayout;
    private ImageView ivExchangeBg;
    private ImageView ivExchangeLogo;
    private TextView tvExchangeCompany;
    private TextView tvExchangeName;
    private TextView tvExchangeTime;
    private TextView tvExchangeNeed;
    private TextView tvToHelpOther;
    private TextView tvLookMore;
    private TextView tvListTitle;
    private RecyclerView recyclerView1, recyclerView2;

    private HealthPresenter presenter, tPresenter;
    private boolean getFirstPageDataFlag = true;
    private int page = 0;

    private String exchangeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_health_result);
        initView();
        initListener();
        presenter = new HealthPresenter(mContext, this, 0);
        tPresenter = new HealthPresenter(mContext, this, 1);
        getFirstPageData();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(mContext);
            titleLayout.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            StatusBarUtil.setTranslucentForImageViewInFragment(HealthFruitActivity.this, 60, null);
        }
    }

    @Override
    protected void initView() {
        titleLayout = findViewById(R.id.layout_title);
        ivBack = findViewById(R.id.iv_back);
        ivBackBlack = findViewById(R.id.iv_back_black);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        if (headView == null) {
            headView = View.inflate(mContext, R.layout.head_health_fruit, null);
            tvCount = headView.findViewById(R.id.tv_count);
            exchangeLayout = headView.findViewById(R.id.layout_exchange);
            robLayout = headView.findViewById(R.id.layout_rob);
            ivExchangeBg = headView.findViewById(R.id.iv_exchange_bg);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivExchangeBg.getLayoutParams();
            params.height = (int) ((150 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (345 * 0.1));

            ivExchangeLogo = headView.findViewById(R.id.iv_exchange_logo);
            tvExchangeCompany = headView.findViewById(R.id.tv_exchange_company);
            tvExchangeName = headView.findViewById(R.id.tv_exchange_name);
            tvExchangeTime = headView.findViewById(R.id.tv_exchange_time);
            tvExchangeNeed = headView.findViewById(R.id.tv_exchange_need);
            tvToHelpOther = headView.findViewById(R.id.tv_to_help_other);
            tvListTitle = headView.findViewById(R.id.tv_list_title);
        }

        if (footView == null) {
            footView = View.inflate(mContext, R.layout.foot_health_fruit, null);
            tvLookMore = footView.findViewById(R.id.tv_look_more);
            recyclerView1 = footView.findViewById(R.id.recycler_view1);
            recyclerView2 = footView.findViewById(R.id.recycler_view2);
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getFirstPageData();
                }
            });
        }

        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }

        if (adapter == null) {
            adapter = new HealthFruitAdapter(R.layout.item_health_result_layout);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 300);
    }

    private void getNetData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        Logger.i("ray---666");
        presenter.getHealFruitCount();
        presenter.getHealFruitList(String.valueOf(page), String.valueOf(Constants.PAGE_COUNT));
    }

    @Override
    protected void initListener() {
        RxView.clicks(tvRight)//返回 我的健康
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(ivBack)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(ivBackBlack)//返回 我的健康
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        SkipUtils.toMyHealth(mContext);
                    }
                });

        //兑换健康果
        RxView.clicks(exchangeLayout)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dialog.show();
                    }
                });

        //抢健康果
        RxView.clicks(robLayout)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        SkipUtils.toRob(HealthFruitActivity.this);
                    }
                });

        RxView.clicks(tvLookMore)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        SkipUtils.toHealthFruitList(HealthFruitActivity.this);
                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getNetData();
                adapter.setEnableLoadMore(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int distance = 0;

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                distance += dy;
                if (distance <= 0) {
                    ivBack.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.GONE);

                    tvTitle.setTextColor(Color.argb((int) 255, 255, 255, 255));
                    tvRight.setTextColor(Color.argb((int) 255, 255, 255, 255));

                    titleLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                } else if (distance > 0 && distance <= bannerHeight) {
                    ivBack.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.VISIBLE);

                    float scale = (float) distance / bannerHeight;
                    float alpha = (255 * scale);

                    ivBack.setImageAlpha((int) (255 * (1 - scale)));
                    ivBackBlack.setImageAlpha((int) alpha);

                    tvTitle.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                    tvRight.setTextColor(Color.argb((int) alpha, 0, 0, 0));

                    titleLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    ivBack.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.VISIBLE);

                    tvTitle.setTextColor(Color.argb((int) 255, 0, 0, 0));
                    tvRight.setTextColor(Color.argb((int) 255, 0, 0, 0));

                    titleLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                }
            }
        });
    }

    private int bannerHeight = 400;
    private String toExchangeCount = "1";
    private MyRadioGroup myRadioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton5;
    private RadioButton radioButton10;
    private RadioButton radioButton20;
    private RadioButton radioButton50;

    @Override
    protected void initData() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(this).inflate(R.layout.dialog1forhealthlayout, null);

        ImageView dissmiss_iv = view.findViewById(R.id.dissmiss_iv);
        dissmiss_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        myRadioGroup = view.findViewById(R.id.radio_group);
        radioButton1 = view.findViewById(R.id.radioBtn1);
        radioButton2 = view.findViewById(R.id.radioBtn2);
        radioButton5 = view.findViewById(R.id.radioBtn5);
        radioButton10 = view.findViewById(R.id.radioBtn10);
        radioButton20 = view.findViewById(R.id.radioBtn20);
        radioButton50 = view.findViewById(R.id.radioBtn50);
        radioButton1.setChecked(true);

        LinearLayout exchangeLayout = view.findViewById(R.id.layout_exchange);

        TextView tvWebTcp = view.findViewById(R.id.tv_web_tcp);
        tvWebTcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(HealthFruitActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "happleAppleAgreement");
                startActivity(intent);
            }
        });

        TextView tvDesc = view.findViewById(R.id.tv_desc);
        tvDesc.setText(exchangeNum + "个奖券兑换一个健康果");

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialogWindow.setAttributes(lp);

        myRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup myRadioGroup, @IdRes int i) {
                if (i == R.id.radioBtn1) {
                    toExchangeCount = "1";
                } else if (i == R.id.radioBtn2) {
                    toExchangeCount = "2";
                } else if (i == R.id.radioBtn5) {
                    toExchangeCount = "5";
                } else if (i == R.id.radioBtn10) {
                    toExchangeCount = "10";
                } else if (i == R.id.radioBtn20) {
                    toExchangeCount = "20";
                } else if (i == R.id.radioBtn50) {
                    toExchangeCount = "50";
                }
            }
        });

        exchangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.i("2018年1月25日 16:20:46-" + toExchangeCount);
                if ("0".equals(toExchangeCount)) {
                    showToast("请选择您要兑换的健康果数量");
                    return;
                }

                startMyDialog(false);
                tPresenter.exchangeFruit(toExchangeCount);
            }
        });
    }

    @Override
    public void onCompleted() {
        netRequestEnd();
    }

    @Override
    public void onErrors(int flag, Throwable e) {
        Logger.i("2018年1月25日 16:20:46-失败");
        if (1 == flag) {
            showToast(e.getMessage());
        } else {
            if (page == 0) {
                if (getFirstPageDataFlag) {
                    initHead();
                    initFoot();
                    tvListTitle.setVisibility(View.GONE);
                    getFirstPageDataFlag = false;
                    titleLayout.setBackgroundColor(Color.argb((int) 255, 0, 217, 128));
                }
            } else if (page > 0) {
                page--;
            }
            adapter.loadMoreFail();
        }

        stopMyDialog();
        netRequestEnd();
    }

    private boolean flag1 = false;
    private boolean flag3 = false;

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("2018年1月25日 16:20:46-成功");
        if (baseEntity instanceof MyHealthFruitBean) {
            final MyHealthFruitBean bean = (MyHealthFruitBean) baseEntity;

            tvCount.showNumberWithAnimation(Float.parseFloat(bean.data.APPLES_NUM), CountNumberView.INTREGEX);

            final MyHealthFruitBean.DataBean.PlanBean planBean = bean.data.mutualPlan;
            GlideUtil.showImage(mContext, planBean.IMG_URL, ivExchangeBg);
            tvExchangeName.setText(planBean.NAME);
            tvExchangeNeed.setText(planBean.EXCHANGE_RATIO + "个健康果");
            exchangeNum = bean.data.tardeRatio;
            tvToHelpOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipUtils.toHelpEach(HealthFruitActivity.this, planBean.ID, "", "", "", "", "", null, planBean.NAME);
                }
            });

            ArrayList<MyHealthFruitBean.DataBean.MsgBean> msgBean = bean.data.instructions;
            ArrayList<MyHealthFruitBean.DataBean.MsgBean> firstList = new ArrayList<>();
            ArrayList<MyHealthFruitBean.DataBean.MsgBean> secondList = new ArrayList<>();

            for (int i = 0; i < msgBean.size(); i++) {
                String type = msgBean.get(i).TYPE;
                if ("1".equals(type)) {
                    firstList.add(msgBean.get(i));
                } else {
                    secondList.add(msgBean.get(i));
                }
            }

            if (ruleAdapter1 == null) {
                ruleAdapter1 = new HealthFruitRuleAdapter(R.layout.item_health_rule, firstList);
            }
            if (ruleAdapter2 == null) {
                ruleAdapter2 = new HealthFruitRuleAdapter(R.layout.item_health_rule, secondList);
            }
            LinearLayoutManager hozManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager hozManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            recyclerView1.setLayoutManager(hozManager1);
            recyclerView2.setLayoutManager(hozManager2);
            recyclerView1.setAdapter(ruleAdapter1);
            recyclerView2.setAdapter(ruleAdapter2);
            ruleAdapter1.setNewData(firstList);
            ruleAdapter1.loadMoreEnd();
            ruleAdapter2.setNewData(secondList);
            ruleAdapter2.loadMoreEnd();
            recyclerView1.setFocusable(false);
            recyclerView1.setFocusableInTouchMode(false);
            recyclerView2.setFocusable(false);
            recyclerView2.setFocusableInTouchMode(false);

            flag1 = true;
        } else if (baseEntity instanceof HealthFruitListBean) {
            HealthFruitListBean listBean = (HealthFruitListBean) baseEntity;

            if (getFirstPageDataFlag) {
                initHead();
                initFoot();
                tvListTitle.setVisibility(View.VISIBLE);
                getFirstPageDataFlag = false;
            }

            List<HealthFruitListBean.DataBean.ListBean> newList = listBean.data.list;
            if (null == newList || newList.size() <= 0) {
                adapter.setEmptyView(emptyView);
                adapter.loadMoreEnd();
            } else {
                if (newList.size() > 3) {
                    newList = newList.subList(0, 3);
                    tvLookMore.setVisibility(View.VISIBLE);
                } else {
                    tvLookMore.setVisibility(View.GONE);
                }
                adapter.setNewData(newList);
                adapter.loadMoreComplete();
            }

            flag3 = true;
        } else {
            Toast.makeText(mContext, "兑换成功，快去参与互助计划吧！", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            presenter.getHealFruitCount();
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_HEALTH_HOME, true, mContext);
            //刷新个人页面标识
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);

            stopMyDialog();
        }

        initData();
        if (flag1 && flag3) {
            netRequestEnd();
        }
    }

    private void initHead() {
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
    }

    private void initFoot() {
        if (adapter.getFooterLayoutCount() == 0) {
            adapter.addFooterView(footView);
        }
    }

    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    private void getFirstPageData() {
        page = 0;
        getNetData();
    }

}
