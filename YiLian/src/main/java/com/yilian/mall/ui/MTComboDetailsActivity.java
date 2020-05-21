package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ComboAdapter;
import com.yilian.mall.adapter.ComboListAdapter;
import com.yilian.mall.adapter.EvluateAdapter;
import com.yilian.mall.adapter.MerchantRecommendAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.MTComboDetailsEntity;
import com.yilian.mall.entity.MTMerchantCommendEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.AMapDistanceUtils;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.FlowLayout;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.ShopsSort;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuyuqi on 2016/12/6 0006.
 * 套餐详情
 */
public class MTComboDetailsActivity extends BaseActivity implements View.OnClickListener, PullToRefreshScrollView.ScrollListener {

    private ImageView mV3Back;
    private ImageView mV3Left;
    private TextView mV3Title;
    private ImageView mV3Shop;
    private ImageView mV3Share;
    private ViewPager mViewpager;
    private LinearLayout mLlPoints;
    private TextView mHeadComboPrice;
    private Button mBtnPromptlyBuy;
    private LinearLayout mLlComboHead;
    private FrameLayout mFlBanner;
    private TextView mTvComboTitle;
    private TextView mTvSellCount;
    private Button mBtnNowBuy;
    private NoScrollListView mListViewCombo;
    private LinearLayout mLlGoMore;
    private View viewLine;
    private TextView mTvBuyNeedKnow;
    private TextView mTvTermTime;
    private TextView mTvUseTime;
    private TextView mTvUseRule;
    private TextView mTvDelivery;
    private TextView mTvAllEvaluate;
    private RatingBar mRbCombo;
    private TextView mTvRbComboText;
    private TextView mTvEvaluate;
    private FlowLayout mFlLabelCount;
    private ImageView mIvIcon;
    private TextView mTvNearMerchantRecommend;
    private NoScrollListView mLvNearMerchantRecommend;
    private PullToRefreshScrollView mSvPullRefresh;
    private Context mContext;
    private MTNetRequest mtNetRequest;
    private MTComboDetailsEntity.DataBean comnoList;
    private ComboAdapter vpAdapter;
    private TextView mtvComboPrice;
    private TextView tvMerchantName;
    private TextView tvMerchantDis;
    private TextView tvMerchantAddress;
    private ImageView ivCall;
    private ArrayList<ImageView> dotImageViews = new ArrayList<>();
    private NoScrollListView lvEvaluate;
    private int page;
    private ArrayList<MTMerchantCommendEntity.MerchantListBean> copyMerchantList = new ArrayList<>();
    ;
    private TextView tvEvaluateText;
    private LinearLayout llAllEvaluate;
    private List<MTComboDetailsEntity.DataBean.TagsBean> tags = new ArrayList<>();
    private TextView tvLabel1;
    private TextView tvLabel2;
    private TextView tvLabel3;
    private TextView tvLabel4;
    private MTComboDetailsEntity result;
    private String merchant_id;
    private boolean isFrovite = false;
    private LinearLayout llEvaluate;
    private String package_id;
    private View includeStoreDetail;
    private int voucher;
    private LinearLayout llStance;
    private RelativeLayout rlContent;
    private LinearLayout llLoadError;
    private TextView tvRefresh;
    private TextView tvCost;

    //Ray_L_Pain 益联益家修改
    private TextView tvTitle; //套餐名字
    private TextView tvPageFirst; //当前页数
    private TextView tvPageSecond; //总页数
    private TextView tvIntegral; //套餐赠送的奖券
    private TextView tvIntegralGone; //一开始是隐藏的套餐赠送的奖券
    private LinearLayout evaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.mt_activity_combo);
        mContext = this;
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mV3Back.setOnClickListener(this);
        mV3Shop.setOnClickListener(this);
        mV3Share.setOnClickListener(this);
        mSvPullRefresh.setScrollListener(this);
        mLlGoMore.setOnClickListener(this);
        mBtnPromptlyBuy.setOnClickListener(this);
        mBtnNowBuy.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        llAllEvaluate.setOnClickListener(this);
        tvMerchantAddress.setOnClickListener(this);
        includeStoreDetail.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);

        mSvPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                getBottomData(merchant_id);
            }
        });

        mLvNearMerchantRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext, MTMerchantDetailActivity.class);
                Logger.i("附近商家推荐传值" + copyMerchantList.get(position).merchantId);
                intent.putExtra("merchant_id", copyMerchantList.get(position).merchantId);
                startActivity(intent);
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int finalPostion;
            int lastPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                finalPostion = position;
                initChageDot(position);
            }

            private void initChageDot(int position) {
                initAllDot();
                if (comnoList.packageIcon.size() > 0) {
                    position = position % comnoList.packageIcon.size();
                    if (position != 0) {
                        position = position - 1;
                    } else {
                        position = comnoList.packageIcon.size() - 1;
                    }
                    View childAt = mLlPoints.getChildAt(lastPosition);
                    if (childAt != null) {
                        childAt.setEnabled(false);
                    }
                    View childAt1 = mLlPoints.getChildAt(position);
                    if (childAt1 != null) {
                        childAt1.setEnabled(true);
                    }
                    lastPosition = position;

                    tvPageFirst.setText(String.valueOf(position + 1));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (finalPostion == vpAdapter.getCount() - 1) {
                        //最后一个位置定位到第一个位置
                        mViewpager.setCurrentItem(1, false);
                    } else if (finalPostion == 0) {
                        //第一个位置定位到最后一个
                        mViewpager.setCurrentItem(vpAdapter.getCount() - 2, false);
                    }
                }
            }
        });
    }

    private void initAllDot() {
        for (int i = 0; i < dotImageViews.size(); i++) {
            dotImageViews.get(i).setEnabled(false);
        }
    }

    //请求显示的数据
    private void initData() {
        page = 0;
        package_id = getIntent().getStringExtra("package_id");
        merchant_id = getIntent().getStringExtra("merchant_id");
        Logger.i("接受到的merchant_id：" + merchant_id + "package_ id " + package_id);
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        //请求套餐详情
        mtNetRequest.getComboDetailsList(package_id, new RequestCallBack<MTComboDetailsEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog(false);
            }

            @Override
            public void onSuccess(ResponseInfo<MTComboDetailsEntity> responseInfo) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        switch (responseInfo.result.code) {
                            case 1:
                                result = responseInfo.result;
                                comnoList = responseInfo.result.data;
                                merchant_id = result.data.merchantInfo.merchantId;

                                //根据经纬度获取距离
                                String merchantLatitude = comnoList.merchantInfo.merchantLatitude;
                                String merchantLongitude = comnoList.merchantInfo.merchantLongitude;
                                float singleDistance = AMapDistanceUtils.getSingleDistance2(merchantLatitude, merchantLongitude);
                                initViewData(comnoList, singleDistance);
                                rlContent.setVisibility(View.VISIBLE);
                                voucher = result.data.voucher;
                                getShareUrl();
                                // initScrollY();
                                Logger.i("工具类获取位置" + singleDistance);
                                break;
                        }
                    } else {
                        llLoadError.setVisibility(View.VISIBLE);

                    }
                }
                stopMyDialog();
                mSvPullRefresh.onRefreshComplete();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                rlContent.setVisibility(View.GONE);
                llLoadError.setVisibility(View.VISIBLE);
                mSvPullRefresh.onRefreshComplete();
                stopMyDialog();
            }
        });
        getBottomData(merchant_id);
    }


    private void getBottomData(String merchant_id) {
        //请求附近商家推荐
        startMyDialog(false);
        mtNetRequest.getMerchantRecommendList(merchant_id, page, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), new RequestCallBack<MTMerchantCommendEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MTMerchantCommendEntity> responseInfo) {

                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        switch (responseInfo.result.code) {
                            case 1:
                                List<MTMerchantCommendEntity.MerchantListBean> merchantList = responseInfo.result.merchantList;
                                ShopsSort.sort(mContext, merchantList);
                                if (page == 0) {
                                    if (merchantList.size() > 0) {
                                        mLvNearMerchantRecommend.setVisibility(View.VISIBLE);
                                        copyMerchantList.clear();
                                        copyMerchantList.addAll(merchantList);
                                    } else {
//                                showToast("没有附近商家推荐");
                                        mLvNearMerchantRecommend.setVisibility(View.GONE);
                                        return;
                                    }
                                }
                                if (page > 0) {
                                    if (merchantList.size() > 0) {
                                        copyMerchantList.addAll(merchantList);
                                    } else {
                                        showToast("没有更多数据");
                                    }
                                }
                                //附近商家推荐
                                mLvNearMerchantRecommend.setAdapter(new MerchantRecommendAdapter(copyMerchantList));
                                Logger.i("附近商家推荐的listsize" + copyMerchantList.size());
                                break;
                            default:
                                break;
                        }
                    }
                }
                stopMyDialog();
                mSvPullRefresh.onRefreshComplete();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                mLvNearMerchantRecommend.setVisibility(View.GONE);
                mSvPullRefresh.onRefreshComplete();
                stopMyDialog();
                Logger.e("附近商家推荐请求" + e + "内容" + s);
            }
        });
    }


    //给初始化的控件赋值
    private void initViewData(MTComboDetailsEntity.DataBean comnoList, float distance) {
        initOtherData();
        //收藏赋值
        if ("1".equals(result.yetCollect)) {
            isFrovite = true;
        } else {
            isFrovite = false;
        }
        showFavoriteState();
        mV3Shop.setVisibility(View.VISIBLE);
        //头布局
        mHeadComboPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(comnoList.price)));
        tvCost.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(comnoList.priceTotal)));
        tvCost.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        mtvComboPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(comnoList.price)));
//        mTvComboTitle.setText(comnoList.packageName);
        tvTitle.setText(comnoList.packageName);
        mTvSellCount.setText("已售:" + comnoList.sellCount);
        tvIntegral.setText(MoneyUtil.getLeXiangBiNoZero(comnoList.returnIntegral) + getResources().getString(R.string.integral));
        tvIntegralGone.setText(MoneyUtil.getLeXiangBiNoZero(comnoList.returnIntegral) + getResources().getString(R.string.integral));
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");

        tvMerchantName.setText(comnoList.merchantInfo.merchantName);
        if (TextUtils.isEmpty(String.valueOf(distance))) {
            tvMerchantDis.setText("计算距离失败");
        } else {
            if (distance > 1000) {
                tvMerchantDis.setText(decimalFormat.format(distance / 1000) + "km");
            } else {
                tvMerchantDis.setText((int) distance + "m");
            }
        }

        Logger.i("套餐距离" + distance + "km");
        tvMerchantAddress.setText(comnoList.merchantInfo.merchantAddress);

        //标签
        int fullMinusFee = Integer.parseInt(comnoList.fullMinusFee);
        if ("1".equals(comnoList.isDelivery) && 0 == fullMinusFee) {
            tvLabel4.setText("免费送");
        } else if ("1".equals(comnoList.isDelivery) && fullMinusFee >= 1) {
            tvLabel4.setText("满" + MoneyUtil.getXianJinQuanNoDecimal(fullMinusFee) + "免费送");
        } else {
            tvLabel4.setText("暂不支持配送");
            mTvDelivery.setText("暂不支持配送");
        }

        //满减送 配送规则 要根据商家是否支持配送来处理
        if ("1".equals(comnoList.isDelivery)) {
            String fullMinusFeeStr = MoneyUtil.getXianJinQuanNoDecimal(comnoList.fullMinusFee);
            //拼接的配送的规则
            Logger.i("配送范围" + comnoList.scope);
            String deliveryAmount = MoneyUtil.getXianJinQuan(comnoList.deliveryAmount);
            int scope = Integer.parseInt(comnoList.scope) / 1000;//服务器给的数据是米
            Logger.i("支付money" + deliveryAmount);
            mTvDelivery.setText("1.满" + fullMinusFeeStr + "免费送\r\n"
                    + "2.订单不满" + fullMinusFeeStr + "元，要求配送时，需支付" + deliveryAmount + "元配送费\r\n" +
                    "3.配送范围:" + scope + "km以内\r\n" + "4.配送时间:" + "营业时间内\r\n"
                    + "5.具体配送要求,请联系商家"
            );
        }

        Logger.i("满减送" + "满" + MoneyUtil.getXianJinQuanNoDecimal(comnoList.fullMinusFee) + "免费送");
        //服务器给的数据可以直接显示，不给时间戳
        mTvTermTime.setText(comnoList.notesInfo.effectiveTime);
        Logger.i("返回的时间戳" + comnoList.notesInfo.effectiveTime + " timeStamp" + mTvTermTime + "mTvTermTime " + mTvTermTime);
        mTvUseTime.setText(comnoList.notesInfo.usableTime);
        mTvUseRule.setText(comnoList.notesInfo.useRules);
        Logger.i("使用规则" + comnoList.notesInfo.useRules);
        mTvEvaluate.setText(comnoList.evaluateCount + "人评价");

        Logger.i("详情的总体评价" + comnoList.evaluate);
        float graded = Float.parseFloat(comnoList.evaluate) / 10;

        if (graded == 0f) {
            mRbCombo.setRating(5f);
        } else {
            mRbCombo.setRating(graded);
        }
        mTvRbComboText.setText(graded + "分");
    }

    private void showFavoriteState() {
        if (isFrovite) {
            mV3Shop.setImageDrawable(getResources().getDrawable(R.mipmap.star_solid));
        } else {
            mV3Shop.setImageDrawable(getResources().getDrawable(R.mipmap.merchant_star_empty));
        }
    }


    private void initView() {
        includeStoreDetail = findViewById(R.id.include_store_detail);
        mV3Back = (ImageView) findViewById(R.id.v3Back);
        mV3Left = (ImageView) findViewById(R.id.v3Left);
        mV3Left.setVisibility(View.INVISIBLE);
        mV3Title = (TextView) findViewById(R.id.v3Title);
        mV3Title.setText("套餐详情");
        mV3Shop = (ImageView) findViewById(R.id.v3Shop);
        mV3Share = (ImageView) findViewById(R.id.v3Share);
        mV3Share.setVisibility(View.VISIBLE);
        mV3Share.setImageDrawable(getResources().getDrawable(R.mipmap.iv_shear));
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mLlPoints = (LinearLayout) findViewById(R.id.ll_points);
        //套餐详情的悬浮头布局
        mHeadComboPrice = (TextView) findViewById(R.id.tv_combo_price_head);
        mBtnPromptlyBuy = (Button) findViewById(R.id.btn_promptly_buy);
        mLlComboHead = (LinearLayout) findViewById(R.id.ll_combo_head);
        mFlBanner = (FrameLayout) findViewById(R.id.fl_banner);
        mtvComboPrice = (TextView) findViewById(R.id.tv_combo_price);
        mTvComboTitle = (TextView) findViewById(R.id.tv_combo_title);
        mTvSellCount = (TextView) findViewById(R.id.tv_sell_count);
        llStance = (LinearLayout) findViewById(R.id.llstance);

        mBtnNowBuy = (Button) findViewById(R.id.btn_now_buy_com);
        tvMerchantName = (TextView) findViewById(R.id.tv_shop_name);
        tvMerchantDis = (TextView) findViewById(R.id.tv_shop_distance);
        tvMerchantAddress = (TextView) findViewById(R.id.tv_shop_address);
        ivCall = (ImageView) findViewById(R.id.iv_call);
        llAllEvaluate = (LinearLayout) findViewById(R.id.llall_evaluate);
        llAllEvaluate.setVisibility(View.VISIBLE);
        tvEvaluateText = (TextView) findViewById(R.id.tv_evaluate_text);
        llEvaluate = (LinearLayout) findViewById(R.id.ll_evaluate);
        llEvaluate.setVisibility(View.GONE);

        tvLabel1 = (TextView) findViewById(R.id.tv_label1);
        tvLabel2 = (TextView) findViewById(R.id.tv_label2);
        tvLabel3 = (TextView) findViewById(R.id.tv_label3);
        tvLabel4 = (TextView) findViewById(R.id.tv_label4);

        mListViewCombo = (NoScrollListView) findViewById(R.id.listView_combo);
        mListViewCombo.setFocusable(false);
        viewLine = findViewById(R.id.view_line);
        viewLine.setVisibility(View.VISIBLE);
        mLlGoMore = (LinearLayout) findViewById(R.id.ll_go_more);
        mTvBuyNeedKnow = (TextView) findViewById(R.id.tv_buy_need_know);
        mTvTermTime = (TextView) findViewById(R.id.tv_term_time);
        mTvUseTime = (TextView) findViewById(R.id.tv_use_time);
        mTvUseRule = (TextView) findViewById(R.id.tv_use_rule);
        mTvDelivery = (TextView) findViewById(R.id.tv_delivery);//配送说明
        mTvAllEvaluate = (TextView) findViewById(R.id.tv_all_evaluate);
        mRbCombo = (RatingBar) findViewById(R.id.rb_combo);
        mTvRbComboText = (TextView) findViewById(R.id.tv_rb_combo_text);
        mTvEvaluate = (TextView) findViewById(R.id.tv_evaluate);
        mFlLabelCount = (FlowLayout) findViewById(R.id.fl_label_count);
        lvEvaluate = (NoScrollListView) findViewById(R.id.lv_evaluate);
        lvEvaluate.setFocusable(false);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mTvNearMerchantRecommend = (TextView) findViewById(R.id.tv_near_merchant_recommend);
        mLvNearMerchantRecommend = (NoScrollListView) findViewById(R.id.lv_near_merchant_recommend);
        mLvNearMerchantRecommend.setFocusable(false);
        mSvPullRefresh = (PullToRefreshScrollView) findViewById(R.id.sv_pull_refresh);
        //滑动到scrollview顶部，处理第一次加载这个页面时，页面不再顶部的问题
//        mSvPullRefresh.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
        mSvPullRefresh.setFocusable(true);
        mSvPullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        rlContent = (RelativeLayout) findViewById(R.id.rl_content);
        rlContent.setVisibility(View.GONE);
        llLoadError = (LinearLayout) findViewById(R.id.ll_load_error);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        //套餐的原价
        tvCost = (TextView) findViewById(R.id.tv_cost);

        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }


        //yilian
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPageFirst = (TextView) findViewById(R.id.tv_page_first);
        tvPageSecond = (TextView) findViewById(R.id.tv_page_second);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvIntegralGone = (TextView) findViewById(R.id.tv_integral_gone);
        evaLayout = (LinearLayout) findViewById(R.id.eva_layout);
    }

    private void initOtherData() {
        initViewPager();
        initViewPagerDot();
        initComboData();
        initLableCount();
        initEvaluateList();
    }

    @Override
    public void onScroll(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        //控件在onResume中就已经确定了位置不需要viewTree的监听 layout_marginTop="@dimen/margin15"
        if (y > mFlBanner.getHeight() + mTvComboTitle.getHeight() + DPXUnitUtil.dp2px(mContext, 15)) {
            mLlComboHead.setVisibility(View.VISIBLE);
        } else {
            mLlComboHead.setVisibility(View.GONE);
        }
    }

    //评价
    private void initEvaluateList() {
        if (comnoList.evaluateList.size() <= 0 || null == comnoList.evaluateList) {
            evaLayout.setVisibility(View.GONE);
            return;
        }
        lvEvaluate.setAdapter(new EvluateAdapter(comnoList.evaluateList, comnoList.evaluateCount, merchant_id, comnoList.packageId));
    }

    //总体评价标签
    private void initLableCount() {
        TextView textview = null;
        mFlLabelCount.removeAllViews();
        tags.clear();
        tags.addAll(comnoList.tags);
        Logger.i("TagsSize  " + tags.size() + "comnoList.tags" + comnoList.tags.toString());
        for (int i = 0; i < tags.size(); i++) {
            textview = new TextView(mContext);
//            使用自定义的流式布局要重写 属性代码 否则他不知道child的位置 VewGroup.MarginLayoutParams
            ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mlp.setMargins(DPXUnitUtil.dp2px(mContext, 15f), DPXUnitUtil.dp2px(mContext, 10f),
                    DPXUnitUtil.dp2px(mContext, 7f), DPXUnitUtil.dp2px(mContext, 0f));
            textview.setLayoutParams(mlp);
//            textview.setTextSize(13);

            textview.setText(tags.get(i).tagsName + " " + tags.get(i).tagsCount);
            if (i < 3) {
                textview.setBackgroundResource(R.drawable.evaluate_good_drawable);
                textview.setTextColor(getResources().getColor(R.color.color_good_evaluate));
            } else {
                textview.setBackgroundResource(R.drawable.evaluate_bad_drawable);
                textview.setTextColor(getResources().getColor(R.color.color_999));
            }
            textview.setPadding(20, 20, 20, 20);
            mFlLabelCount.addView(textview);
        }

    }

    //套餐信息 服务器没有数据
    private void initComboData() {
        if (null == comnoList.packageInfo) {
            return;
        }
        mListViewCombo.setAdapter(new ComboListAdapter(comnoList.packageInfo));
    }


    private void initViewPagerDot() {
        //添加小圆点之前先删除所有小圆点，避免小圆点数量异常
        mLlPoints.removeAllViews();
        dotImageViews.clear();
        for (int i = 0, count = comnoList.packageIcon.size(); i < count; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DPXUnitUtil.dp2px(mContext, 10);

            imageView.setImageResource(R.drawable.lldot_white_enable);
            dotImageViews.add(imageView);
            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
            }
            mLlPoints.addView(imageView, params);
        }


    }

    //初始化ViewPager
    private void initViewPager() {
        if (comnoList.packageIcon.size() <= 0 || null == comnoList.packageIcon) {
            return;
        }
        if (vpAdapter == null) {
            vpAdapter = new ComboAdapter(comnoList.packageIcon, options, imageLoader, mContext);
        }
        mViewpager.setAdapter(vpAdapter);
        mViewpager.setCurrentItem(1);

        tvPageFirst.setText("1");
        Logger.i("111:" + comnoList.packageIcon.size());
        tvPageSecond.setText("/" + comnoList.packageIcon.size());
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_promptly_buy:
            case R.id.btn_now_buy_com:
                NowBuy();
                break;
            case R.id.rl_all_comment:
                //查看全部评价
                showToast("跳转查看全部评价");
                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.iv_call:
                //拨打电话
                callTel();
                break;
            case R.id.ll_go_more:
                //图文详情
                intent = new Intent(mContext, MTPhotoTextMoreActivity.class);
                intent.putExtra("package_id", comnoList.packageId);
                intent.putExtra("price", comnoList.price);
                intent.putExtra("orderName", comnoList.packageName);
                intent.putExtra("isDelivery", comnoList.isDelivery);
                intent.putExtra("merchantId", comnoList.merchantInfo.merchantId);
                //针对满减的优惠做处理
                intent.putExtra("fullMinusFee", comnoList.fullMinusFee);
                Logger.i("传值前" + comnoList.fullMinusFee);
                startActivity(intent);
                break;
            case R.id.v3Share:
                share();
                break;
            case R.id.v3Shop:
                switchFavorite();
                break;
            case R.id.llall_evaluate:
                //跳转评价列表
                intent = new Intent(this, MTPackageCommentListActivity.class);
                intent.putExtra("merchantId", "0");//针对套餐的评论，商家ID传递0
                intent.putExtra("packageId", package_id);
                startActivity(intent);
                break;
            case R.id.include_store_detail:
            case R.id.tv_shop_address:
                intent = new Intent(this, MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id", merchant_id);
                Logger.i("传递走的merchant_id：" + merchant_id);
                startActivity(intent);
                break;
            case R.id.tv_refresh:
                initData();
                break;
        }
    }

    private void NowBuy() {
        Intent intent;
        if (!isLogin()) {
            intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
            startActivity(intent);
        } else {
            if (comnoList == null) {
                return;
            }
            intent = new Intent(mContext, MTSubmitOrderActivity.class);
            intent.putExtra("package_id", comnoList.packageId);
            intent.putExtra("price", comnoList.price);
            intent.putExtra("orderName", comnoList.packageName);
            intent.putExtra("isDelivery", comnoList.isDelivery);
            intent.putExtra("mtMerchantId", comnoList.merchantInfo.merchantId);
            intent.putExtra("voucher", voucher);
            //针对满减的优惠做处理
            intent.putExtra("fullMinusFee", comnoList.fullMinusFee);
            //奖券
            intent.putExtra("return_integral", comnoList.returnIntegral);
            startActivity(intent);
        }
    }

    //判断是否有收藏
    private void switchFavorite() {
        if (!isLogin()) {
            Intent intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
            startActivity(intent);
            return;
        } else {
            //判断当前的收藏状态
            if (isFrovite) {
                cancelFavorite();
            } else {
                addFavorite();
            }
        }
    }


    private void addFavorite() {
        startMyDialog();
        Logger.i("packageId" + comnoList.packageId);
        jpNetRequest.collectV3(comnoList.packageId, "2", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                mV3Shop.setClickable(false);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        mV3Shop.setClickable(true);
                        showToast("收藏成功");
                        isFrovite = true;
                        showFavoriteState();
                        //刷新个人页面标识
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        break;
                    default:
                        showToast("收藏返回码" + responseInfo.result.code);
                        mV3Shop.setClickable(true);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mV3Shop.setClickable(true);
                stopMyDialog();
            }
        });
    }


    private void cancelFavorite() {
        startMyDialog();
        jpNetRequest.cancelCollectV3(comnoList.packageId, "2", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                mV3Shop.setClickable(false);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        mV3Shop.setClickable(true);
                        isFrovite = false;
                        showFavoriteState();
                        showToast("取消收藏成功");
                        //刷新个人页面标识
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        break;
                    default:
                        showToast("取消收藏异常码" + responseInfo.result.code);
                        mV3Shop.setClickable(true);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mV3Shop.setClickable(true);
                stopMyDialog();
            }
        });

    }

    //分享有关
    private UmengDialog mShareDialog;
    JPNetRequest jpNetRequest;
    String share_title, share_content, share_url, shareImg, share_img;


    //分享
    private void share() {
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(this.mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            share_content,
                            shareImg,
                            share_url)
                            .share();
                }
            });
        }
        mShareDialog.show();
    }

    public void getShareUrl() {
        startMyDialog();
        /**
         * 由于这些页面wx端不再更新 故分享出的页面变为登陆
         * 原参数getShareUrl(11, comnoList.packageId)
         */
        jpNetRequest.getShareUrl("9", "", new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        share_content = responseInfo.result.content;
                        share_url = responseInfo.result.url;
                        share_img = responseInfo.result.imgUrl;
                        if (TextUtils.isEmpty(shareImg)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg = Constants.ImageUrl + share_img;
                            }
                        }
                        Logger.i("share_title " + share_title);
                        Logger.i("share_content " + share_content);
                        Logger.i("share_img " + share_img);
                        Logger.i("share_url " + share_url);
                        break;
                    case -3:
                        showToast("系统繁忙，请稍后再试");
                        break;
                    default:
                        showToast("分享异常码" + responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }


    private void callTel() {
        if (comnoList == null) {
            return;
        }
        final String tel = comnoList.merchantInfo.merchantContact;

        if (tel != null && tel.length() > 0) {
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
                            }
                        }
                    }, this.mContext);

        } else {
            showToast("亲,这家太懒了,没留电话哦!");
        }

    }

}
