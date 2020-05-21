package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.networkingmodule.entity.V5ComboDetailEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  V5版本套餐详情
 * @author Ray_L_Pain
 * @date 2017/12/26 0026
 */

public class V5ComboDetailActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.iv_collect)
    ImageView ivCollect;
    @ViewInject(R.id.iv_share)
    ImageView ivShare;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.tv_btn)
    TextView tvBtn;
    @ViewInject(R.id.layout_net_error)
    LinearLayout layoutNetError;
    @ViewInject(R.id.tv_refresh)
    TextView tvRefresh;

    @ViewInject(R.id.banner)
    Banner banner;
    @ViewInject(R.id.tv_name)
    TextView tvName;
    @ViewInject(R.id.tv_sale_count)
    TextView tvSaleCount;
    @ViewInject(R.id.tv_price)
    TextView tvPrice;
    @ViewInject(R.id.tv_marker_price)
    TextView tvMarkerPrice;
    @ViewInject(R.id.tv_integral)
    TextView tvIntegral;
    @ViewInject(R.id.tv_label4)
    TextView tvLabel4;
    @ViewInject(R.id.tv_merchant_name)
    TextView tvMerchantName;
    @ViewInject(R.id.tv_merchant_renqi)
    TextView tvMerchantRenqi;
    @ViewInject(R.id.tv_merchant_distance)
    TextView tvMerchantDistance;
    @ViewInject(R.id.tv_merchant_address)
    TextView tvMerchantAddress;
    @ViewInject(R.id.iv_phone)
    ImageView ivPhone;
    @ViewInject(R.id.listView_combo)
    ListView comboListView;
    @ViewInject(R.id.tv_buy_need_know)
    TextView tvBuyNeedKnow;
    @ViewInject(R.id.tv_term_time)
    TextView tvTermTime;
    @ViewInject(R.id.tv_use_time)
    TextView tvUseTime;
    @ViewInject(R.id.tv_use_rule)
    TextView tvUseRule;
    @ViewInject(R.id.tv_delivery)
    TextView tvDelivery;

    //收藏标识
    private boolean isCollect = false;
    private JPNetRequest jpNetRequest;
    //分享有关
    private UmengDialog mShareDialog;
    private String shareTitle, shareContent, share_Img, shareUrl, shareImg;


    private String title, packageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v5_combo_detail);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        packageId = getIntent().getStringExtra("package_id");
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitle.setText(title);

        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchCollect();
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                share();
            }
        });

        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @SuppressWarnings("unchecked")
    private void initData() {
        swipeRefreshLayout.setRefreshing(true);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .v5ComboDetail("package/package_details", packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V5ComboDetailEntity>() {
                    @Override
                    public void onCompleted() {
                        netRequestEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        netRequestEnd();
                        layoutNetError.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        tvBtn.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(V5ComboDetailEntity entity) {
                        layoutNetError.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        tvBtn.setVisibility(View.VISIBLE);

                    }
                });
        addSubscription(subscription);
    }

    private void initListener() {
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
    }
}
