package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.V3MoneyDetailAdapter;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.V3MoneyDetailEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author
 *         奖券、奖励 明细详情（除领奖励界面以外的详情，奖励界面{@link V3MoneyDetailActivity_With_Draw}）
 */
public class V3MoneyDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     * 奖励
     */
    public static final int TYPE_0 = 0;
    /**
     * 奖券
     */
    public static final int TYPE_1 = 1;
    /**
     * 区块益豆
     */
    public static final int TYPE_2 = 2;
    /**
     * 代购券 客户端自定义字段
     */
    public static final int TYPE_3 = 99;
    /**
     * 收入支出分界 大于100是支出 小于100是收入
     */
    public static final int INT_100 = 100;
    /**
     * 益豆明细的type  101
     */
    public static final int LE_DOU_FAILURE_DETAIL_TYPE = 101;

    /**
     * 购物卡明细详情 type 102
     */
    public static final int CARD_DETAIL_INFO_TYPE = 102;

    /**
     * 提取失败的status
     */
    public static final int FAILURE_STATUS = 3;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 0奖励 1奖券 108提取营收明细
     */
    private int type;
    private String img;
    private String orderId;
    private V3MoneyDetailAdapter v3MoneyDetailAdapter;
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvAmount;
    private TextView tvDetail;
    private String apiC;
    private Button mBtnExtract;
    /**
     * url路径
     */
    private String path;
    private V3MoneyDetailEntity v3MoneyDetailEntity;
    private View footView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v3_money_detail);
        StatusBarUtil.setColor(this, Color.WHITE);
        type = getIntent().getIntExtra("type", 0);
        img = getIntent().getStringExtra("img");
        orderId = getIntent().getStringExtra("orderId");
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                MenuUtil.showMenu(V3MoneyDetailActivity.this, R.id.v3Shop);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMoneyDetailData();
            }
        });
    }

    private void initData() {
        if (type == TYPE_2) {
            apiC = "get_bean_change_detail";
            path = "account.php";
        } else if (type == Constants.TYPE_EXTRACT_REVENUE_108) {
            apiC = "account/get_money_change_detail";
            path = "mall.php";
        } else if (type == TYPE_3) {
            apiC = "account/get_quan_change_detail";
            path = "mall.php";
        }else if(type == CARD_DETAIL_INFO_TYPE){
            apiC = "card/get_card_change_detail";
            path = "mall.php";
        } else {
            apiC = "jfb/get_balance_change_detail_v2";
            path = "mall.php";
        }
        getMoneyDetailData();
    }

    private void initView() {
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("明细详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.merchant_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.merchant_v3back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundColor(Color.WHITE);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        v3MoneyDetailAdapter = new V3MoneyDetailAdapter(R.layout.item_v3_money_detail);
        recyclerView.setAdapter(v3MoneyDetailAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);

    }

    @SuppressWarnings("unchecked")
    private void getMoneyDetailData() {
        swipeRefreshLayout.setRefreshing(true);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMoneyDetailData(path, apiC, type, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V3MoneyDetailEntity>() {
                    private LinearLayout llLoadError;
                    private TextView tvRefresh;

                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        View errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
                        View tvRefresh = errorView.findViewById(R.id.tv_refresh);
                        RxUtil.clicks(tvRefresh, new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                getMoneyDetailData();
                            }
                        });
                        v3MoneyDetailAdapter.setEmptyView(errorView);
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(V3MoneyDetailEntity v3MoneyDetailEntity) {
                        if (v3MoneyDetailEntity == null) {
                            v3MoneyDetailAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
                        }
                        V3MoneyDetailActivity.this.v3MoneyDetailEntity = v3MoneyDetailEntity;
                        setData(v3MoneyDetailEntity);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(V3MoneyDetailEntity v3MoneyDetailEntity) {
        String amount = v3MoneyDetailEntity.amount;
        String topImage = v3MoneyDetailEntity.topImage;
        String topName = v3MoneyDetailEntity.topName;
        String tradeStatus = v3MoneyDetailEntity.tradeStatus;
        int categoryType = v3MoneyDetailEntity.type;

        List<V3MoneyDetailEntity.DataBean> data = v3MoneyDetailEntity.data;
        v3MoneyDetailAdapter.setNewData(data);


//        头部
        View headerView = View.inflate(mContext, R.layout.header_v3_money_detail_1, null);
        ivIcon = (ImageView) headerView.findViewById(R.id.iv_icon);
        if (!TextUtils.isEmpty(img)) {
            com.yilian.mylibrary.GlideUtil.showCirIconNoSuffix(mContext, img, ivIcon);
        }
        tvName = (TextView) headerView.findViewById(R.id.tv_name);
        tvName.setText(topName);
        tvAmount = (TextView) headerView.findViewById(R.id.tv_amount);
        if (categoryType < INT_100) {
            tvAmount.setText("+" + MoneyUtil.getLeXiangBiNoZero(amount));
        } else {
            tvAmount.setText("-" + MoneyUtil.getLeXiangBiNoZero(amount));
        }
        tvDetail = (TextView) headerView.findViewById(R.id.tv_detail);
        tvDetail.setText(tradeStatus);
        if (v3MoneyDetailAdapter.getHeaderLayoutCount() <= 0) {
            v3MoneyDetailAdapter.addHeaderView(headerView);
        }
        //提取营收页面一定不显示来往记录
        if (type != Constants.TYPE_EXTRACT_REVENUE_108) {
            //        是否显示来往
            int showGiven = v3MoneyDetailEntity.showGiven;
            switch (showGiven) {
                case 0:
                    break;
                case 1:
                    Logger.i("v3MoneyDetailAdapter.getFooterLayoutCount():" + v3MoneyDetailAdapter.getFooterLayoutCount());
                    if (v3MoneyDetailAdapter.getFooterLayoutCount() <= 0) {
                        TextView showGivenFooter = new TextView(mContext);
                        showGivenFooter.setBackgroundColor(Color.WHITE);
                        showGivenFooter.setText("查看来往记录");
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, DPXUnitUtil.dp2px(mContext, 10), 0, 0);
                        showGivenFooter.setLayoutParams(layoutParams);
                        showGivenFooter.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
                        showGivenFooter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        showGivenFooter.setPadding(DPXUnitUtil.dp2px(mContext, 15f), DPXUnitUtil.dp2px(mContext, 18f)
                                , DPXUnitUtil.dp2px(mContext, 15f), DPXUnitUtil.dp2px(mContext, 18f));
                        showGivenFooter.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.jiantou_order), null);
                        RxUtil.clicks(showGivenFooter, new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                Intent intent = new Intent(mContext, V3MoneyDealingsActivity.class);
                                if (TextUtils.isEmpty(v3MoneyDetailEntity.userName) && TextUtils.isEmpty(v3MoneyDetailEntity.userPhone)) {
                                    intent.putExtra("titleName", v3MoneyDetailEntity.topName);
                                } else {
                                    intent.putExtra("titleName", v3MoneyDetailEntity.userName);
                                    intent.putExtra("titlePhone", v3MoneyDetailEntity.userPhone);
                                }
                                intent.putExtra("id", v3MoneyDetailEntity.id);
                                intent.putExtra("type", type);
                                startActivity(intent);
                            }
                        });
                        v3MoneyDetailAdapter.addFooterView(showGivenFooter);
                    }
                    break;
                default:
                    break;
            }
            isAddFooterView(v3MoneyDetailEntity);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 是否显示提交按钮
     */
    private void isAddFooterView(V3MoneyDetailEntity v3MoneyDetailEntity) {
        if (footView == null) {
            footView = View.inflate(mContext, R.layout.foot_extract_again, null);
            mBtnExtract = footView.findViewById(R.id.extract_btn_confirm);
            RxUtil.clicks(mBtnExtract, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    Intent intent = new Intent(mContext, ExtractLeDouToPurseActivity.class);
                    //详情数据
                    intent.putExtra("data", v3MoneyDetailEntity);
                    //跳转页面执行重新提取即修改钱包地址
                    intent.putExtra("isReExtract", true);
                    startActivity(intent);
                }
            });
            if (v3MoneyDetailEntity.type == LE_DOU_FAILURE_DETAIL_TYPE && v3MoneyDetailEntity.status == FAILURE_STATUS) {
                if (v3MoneyDetailAdapter.getFooterLayoutCount() == 0) {
                    v3MoneyDetailAdapter.addFooterView(footView);
                }
            } else {
                if (v3MoneyDetailAdapter.getFooterLayoutCount() > 0) {
                    v3MoneyDetailAdapter.removeFooterView(footView);
                }
            }
        }
    }

    /**
     * 修改提取益豆钱包地址成功
     * {@link ExtractLeDouToPurseActivity#reExtractLeDou()}
     */
    @Subscribe
    public void getReExtractEvent(V3MoneyDetailEntity v3MoneyDetailEntity) {
        finish();
    }

}
