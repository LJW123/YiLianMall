package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.V5ComboAdapter;
import com.yilian.mall.adapter.V5EvaAdapter;
import com.yilian.mall.adapter.V5MerchantListAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.MTComboDetailsEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.FlowLayout;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ShopsSort;
import com.yilian.networkingmodule.entity.V5MerchantDetailEntity;
import com.yilian.networkingmodule.entity.V5MerchantListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  V5版本商家详情
 * @author Ray_L_Pain
 * @date 2017/12/21 0021
 */

public class V5MerchantDetailActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    private View emptyView, errorView, headView;
    private TextView tvRefresh;

    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.iv_collect)
    ImageView ivCollect;
    @ViewInject(R.id.iv_share)
    ImageView ivShare;
    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;
    @ViewInject(R.id.tv_btn)
    TextView tvBtn;

    //收藏标识
    private boolean isCollect = false;
    private JPNetRequest jpNetRequest;
    //分享有关
    private UmengDialog mShareDialog;
    private String shareTitle, shareContent, share_Img, shareUrl, shareImg;
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;
    private int page = 0;
    private V5MerchantListAdapter adapter;
    private ArrayList<V5MerchantListEntity.MerchantListBean> list = new ArrayList<>();
    private String merchantId;

    /**
     * headerView里包含的控件
     */
    private RelativeLayout bannerLayout;
    private Banner topBanner, advBanner;
    private TextView tvBannerCount, tvMerchantName, tvMerchantRenqi, tvMerchantPercent, tvAddress, tvTime, tvFlagName, tvDesc, tvEvaCount, tvNoEvalue, tvEvaState1, tvEvaState2, tvEvaState3, tvEvaState4;
    private ImageView ivPhone;
    private LinearLayout jumpFlagLayout, productLayout, comboLayout, evalueLayout, descLayout;
    private RecyclerView productRecycleView, comboRecycleView, evalueRecycleView;
    private FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v5_merchant_detail);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        merchantId = getIntent().getStringExtra("merchant_id");
        Logger.i("merchant_id-" + merchantId);
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCollect();
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    /**
                     * 跳转web之前判断当前是否有支付密码，没有去设置，防止从web条转原生界面后无法获取最新数据
                     */
                    if (PreferenceUtils.readBoolConfig(Constants.PAY_PASSWORD, mContext, false)) {
                        Intent intent = new Intent(V5MerchantDetailActivity.this, WebViewActivity.class);
                        intent.putExtra(Constants.SPKEY_URL, Ip.getWechatURL(mContext) + "m/pay/merScanPay.php?merchant_id=" + merchantId
                                + "&employee_id=0&token=" + RequestOftenKey.getToken(V5MerchantDetailActivity.this) + "&device_index=" + RequestOftenKey.getDeviceIndex(V5MerchantDetailActivity.this));
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setMessage("您还未设置支付密码，请设置支付密码后在支付！")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(mContext, InitialPayActivity.class));
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                } else {
                    Intent intent = new Intent(V5MerchantDetailActivity.this, LeFenPhoneLoginActivity.class);
                    startActivity(intent);
                }
            }
        });

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
            headView = View.inflate(mContext, R.layout.header_v5_merchant_detail, null);
            bannerLayout = (RelativeLayout) headView.findViewById(R.id.layout_banner);
            topBanner = (Banner) headView.findViewById(R.id.banner_top);
            tvBannerCount = (TextView) headView.findViewById(R.id.tv_banner_count);
            tvMerchantName = (TextView) headView.findViewById(R.id.tv_merchant_name);
            tvMerchantRenqi = (TextView) headView.findViewById(R.id.tv_merchant_renqi);
            tvMerchantPercent = (TextView) headView.findViewById(R.id.tv_merchant_percent);
            tvAddress = (TextView) headView.findViewById(R.id.tv_address);
            ivPhone = (ImageView) headView.findViewById(R.id.iv_phone);
            tvTime = (TextView) headView.findViewById(R.id.tv_time);
            jumpFlagLayout = (LinearLayout) headView.findViewById(R.id.layout_jump_flag);
            tvFlagName = (TextView) headView.findViewById(R.id.tv_flag_name);
            productLayout = (LinearLayout) headView.findViewById(R.id.layout_product);
            productRecycleView = (RecyclerView) headView.findViewById(R.id.recycler_view_product);
            productRecycleView.setFocusable(false);
            productRecycleView.setFocusableInTouchMode(false);
            comboLayout = (LinearLayout) headView.findViewById(R.id.layout_combo);
            comboRecycleView = (RecyclerView) headView.findViewById(R.id.recycler_view_combo);
            comboRecycleView.setFocusable(false);
            comboRecycleView.setFocusableInTouchMode(false);
            advBanner = (Banner) headView.findViewById(R.id.banner_adv);
            tvEvaCount = (TextView) headView.findViewById(R.id.tv_eva_count);
            flowLayout = (FlowLayout) headView.findViewById(R.id.flow_layout);
            evalueRecycleView = (RecyclerView) headView.findViewById(R.id.recycler_view_evalue);
            evalueRecycleView.setFocusable(false);
            evalueRecycleView.setFocusableInTouchMode(false);
            evalueLayout = (LinearLayout) headView.findViewById(R.id.layout_evalue);
            tvNoEvalue = (TextView) headView.findViewById(R.id.tv_no_evalue);
            descLayout = (LinearLayout) headView.findViewById(R.id.layout_desc);
            tvDesc = (TextView) headView.findViewById(R.id.tv_desc);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new V5MerchantListAdapter(R.layout.item_v5_merchant_list);
        }
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
    }

    private void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getFirstPageData();
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
    }

    private void showCollectState() {
        if (isCollect) {
            ivCollect.setImageDrawable(getResources().getDrawable(R.mipmap.star_solid));
        } else {
            ivCollect.setImageDrawable(getResources().getDrawable(R.mipmap.merchant_star_empty));
        }
    }

    private void switchCollect() {
        if (!isLogin()) {
            Intent intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
            startActivity(intent);
            return;
        }
        if (isCollect) {
            cancelCollect();
        } else {
            addCollect();
        }
    }

    private void addCollect() {
        startMyDialog();
        jpNetRequest.collectV3(merchantId, "1", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                ivCollect.setClickable(false);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        ivCollect.setClickable(true);
                        showToast("添加收藏成功");
                        isCollect = true;
                        showCollectState();
                        //刷新个人页面标识
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        break;
                    default:
                        showToast("添加收藏异常码" + responseInfo.result.code);
                        ivCollect.setClickable(true);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                ivCollect.setClickable(true);
            }
        });
    }

    private void cancelCollect() {
        startMyDialog();
        jpNetRequest.cancelCollectV3(merchantId, "1", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                ivCollect.setClickable(false);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        ivCollect.setClickable(true);
                        showToast("取消收藏成功");
                        isCollect = false;
                        showCollectState();
                        //刷新个人页面标识
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        break;
                    default:
                        showToast("异常码" + responseInfo.result.code);
                        ivCollect.setClickable(true);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ivCollect.setClickable(true);
                stopMyDialog();
            }
        });
    }

    private void share() {
        getShareUrl();
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(mContext, R.anim.anim_wellcome_bottom),
                    R.style.DialogControl, new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {

                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            shareTitle,
                            shareContent,
                            shareImg,
                            shareUrl).share();
                }
            });
        }
        mShareDialog.show();
    }

    public void getShareUrl() {
        startMyDialog();
        /**
         * 由于这些页面wx端不再更新 故分享出的页面变为登陆
         * 原参数getShareUrl(3, merchant_id)
         */
        jpNetRequest.getShareUrl("9", "", new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        shareTitle = responseInfo.result.title;
                        shareContent = responseInfo.result.content;
                        share_Img = responseInfo.result.imgUrl;
                        shareUrl = responseInfo.result.url;
                        if (TextUtils.isEmpty(shareImg)) {
                            shareImg = "";
                        } else {
                            if (share_Img.contains("http://") || share_Img.contains("https://")) {
                                shareImg = share_Img;
                            } else {
                                shareImg = Constants.ImageUrl + share_Img;
                            }
                        }
                        break;
                    case -3:
                        showToast("系统繁忙");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                Logger.e("分享错误" + e);
            }
        });

    }

    @SuppressWarnings("unchecked")
    private void getData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .v5MerchantList("package/merchant_recommend", merchantId, String.valueOf(page), String.valueOf(Constants.PAGE_COUNT), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext)
                        , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V5MerchantListEntity>() {
                    @Override
                    public void onCompleted() {
                        netRequestEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        Logger.i("详情异常:"+e.getMessage());
                        if (page == 0) {
                            adapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                        }
                        adapter.loadMoreFail();
                        netRequestEnd();
                    }

                    @Override
                    public void onNext(V5MerchantListEntity entity) {
                        if (getFirstPageDataFlag) {
                            initHead();
                            getFirstPageDataFlag = false;
                        }

                        ArrayList<V5MerchantListEntity.MerchantListBean> newList = entity.merchantList;
                        ShopsSort.sort(mContext, newList);
                        if (page <= 0) {
                            if (null == newList || newList.size() <= 0) {
                                adapter.loadMoreEnd();
                                adapter.setEmptyView(emptyView);
                            } else {
                                adapter.setNewData(newList);
                                adapter.loadMoreComplete();
                            }
                        } else {
                            if (newList.size() >= Constants.PAGE_COUNT) {
                                adapter.loadMoreComplete();
                            } else {
                                adapter.loadMoreEnd();
                            }
                            adapter.addData(newList);
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void initHead() {
        Logger.i("2017年12月25日 15:24:53-走了这里");
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }
        getHeadData();
    }

    @SuppressWarnings("unchecked")
    private void getHeadData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .v5MerchantDetail("package/merchant_details_v2", merchantId, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext)
                        , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V5MerchantDetailEntity>() {
                    @Override
                    public void onCompleted() {
                        netRequestEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("头" + e.getMessage());
                        netRequestEnd();
                    }

                    @Override
                    public void onNext(V5MerchantDetailEntity entity) {
                        if ("1".equals(entity.yetCollect)) {
                            isCollect = true;
                        } else {
                            isCollect = false;
                        }
                        showCollectState();
                        initHeadData(entity);
                        netRequestEnd();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 设置头部数据
     */
    private void initHeadData(V5MerchantDetailEntity entity) {
        V5MerchantDetailEntity.InfoBean info  = entity.info;

        /**
         * 设置头部轮播图
         */
        RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) topBanner.getLayoutParams();
        topParams.height = (int) ((150 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));
        ArrayList<String> imageList = new ArrayList<String>();
        int size = info.images.size();
        if (size <= 0) {
            bannerLayout.setVisibility(View.GONE);
        } else {
            bannerLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < size; i++) {
                String image = info.images.get(i).photoPath;
                imageList.add(com.yilian.mylibrary.WebImageUtil.getInstance().getWebImageUrlNOSuffix(image));
            }
            topBanner.setImages(imageList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new BannerViewGlideUtil())
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            showToast("点击了banner");
                        }
                    }).start();
            tvBannerCount.setText("共" + imageList.size() + "张");
        }


        tvTitle.setText(info.merchantName);
        tvMerchantName.setText(info.merchantName);
        tvMerchantRenqi.setText("人气" + info.renqi);
        tvMerchantPercent.setText(info.merDiscount + "%");
        tvAddress.setText(info.merchantAddress);
        ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTel(info.merchantTel);
            }
        });
        tvTime.setText("营业时间：" + info.merchantWorktime);
        if (0 == info.supplierIsExist) {
            jumpFlagLayout.setVisibility(View.GONE);
        } else {
            jumpFlagLayout.setVisibility(View.VISIBLE);
            tvFlagName.setText("线上店铺：" + info.supplierName);
            jumpFlagLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(V5MerchantDetailActivity.this, JPFlagshipActivity.class);
                    intent.putExtra("index_id", info.supplierId);
                    startActivity(intent);
                }
            });
        }

        /**
         * 设置单品
         */
        //TODO 推荐单品接口还没好,暂时隐藏
        productLayout.setVisibility(View.GONE);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        productRecycleView.setLayoutManager(linearLayoutManager);
//        productRecycleView.setAdapter(new v5MerchantProductAdapter(R.layout.item_v5_merchant_product));

        /**
         * 设置套餐
         */
        List<V5MerchantDetailEntity.InfoBean.PackageBean> comboList = info.packages;
        if (null == comboList || comboList.size() <= 0) {
            comboLayout.setVisibility(View.GONE);
        } else {
            comboLayout.setVisibility(View.VISIBLE);
            LinearLayoutManager comboManager = new LinearLayoutManager(mContext);
            comboRecycleView.setLayoutManager(comboManager);
            V5ComboAdapter comboAdapter = new V5ComboAdapter(R.layout.item_v5_combo_list, merchantId);
            comboRecycleView.setAdapter(comboAdapter);
            comboAdapter.setNewData(comboList);
            comboAdapter.loadMoreComplete();
        }

        /**
         * 设置活动轮播
         */
        //TODO 活动轮播接口还没好,暂时隐藏
        advBanner.setVisibility(View.GONE);

        /**
         * 设置评论
         */
        List<V5MerchantDetailEntity.InfoBean.CommentsBean> commentList = info.comments;
        Logger.i("2017年12月26日 16:16:26-" + commentList.size());
        Logger.i("2017年12月26日 16:16:26-" + commentList.toString());
        tvEvaCount.setText("用户评论（" + info.commentsCount + "）");
        if (null == commentList || commentList.size() <= 0) {
            tvNoEvalue.setVisibility(View.VISIBLE);
            evalueLayout.setVisibility(View.GONE);
        } else {
            tvNoEvalue.setVisibility(View.GONE);
            evalueLayout.setVisibility(View.VISIBLE);
            // TODO 这里写评论有关的个数
            tvEvaState1.setText("不满");
            tvEvaState2.setText("一般");
            tvEvaState3.setText("满意");
            tvEvaState4.setText("超赞");
            // TODO 这里设置flow的词条 这是属于新功能，后台数据还没有
//            initFlow();
            flowLayout.setVisibility(View.GONE);

            LinearLayoutManager commentManager = new LinearLayoutManager(mContext);
            evalueRecycleView.setLayoutManager(commentManager);
            V5EvaAdapter evaAdapter = new V5EvaAdapter(R.layout.item_v5_eva_list, info.commentsCount);
            evalueRecycleView.setAdapter(evaAdapter);
            evaAdapter.setNewData(commentList);
            evaAdapter.loadMoreComplete();
        }

        /**
         * 设置商家简介
         */
        if (TextUtils.isEmpty(info.merchantDesp)) {
            descLayout.setVisibility(View.GONE);
        } else {
            descLayout.setVisibility(View.VISIBLE);
            tvDesc.setText(info.merchantDesp);
        }
    }

    private List<MTComboDetailsEntity.DataBean.TagsBean> tags = new ArrayList<>();
    private void initFlow() {
        TextView tv = null;
        flowLayout.removeAllViews();
        tags.clear();
//        tags.addAll();
        for (int i = 0; i < tags.size(); i++) {
            tv = new TextView(mContext);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(DPXUnitUtil.dp2px(mContext, 15f), 0, DPXUnitUtil.dp2px(mContext, 15f), DPXUnitUtil.dp2px(mContext, 10f));
            tv.setLayoutParams(params);
            tv.setBackgroundResource(R.drawable.evaluate_bad_drawable);
            tv.setTextColor(getResources().getColor(R.color.color_666));
            tv.setPadding(20, 5, 20, 5);
            flowLayout.addView(tv);
        }
    }

    private void callTel(String tel) {
        if (!TextUtils.isEmpty(tel)) {
            showDialog(null, tel, null, 0, Gravity.CENTER, "拨打", "取消", true,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }, this.mContext);
        } else {
            showToast("亲,这家太懒了,没留电话哦!");
        }
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
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
