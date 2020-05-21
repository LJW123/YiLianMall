package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.leshan.ylyj.view.activity.mypursemodel.TransactionRecordActivity;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.V3MoneyMenuAdapter;
import com.yilian.mall.ui.mvp.view.CertificationViewImplActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mall.widgets.pulllayout.DropZoomScrollView;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.FontUtils;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.BannerDataEntity;
import com.yilian.networkingmodule.entity.GetBannerDataType;
import com.yilian.networkingmodule.entity.MyBalanceBeanEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.V3MoneyMenuEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的奖券、奖励页面
 *
 * @author xiaomi_Android
 */
public class V3MoneyActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     * 奖励金额
     */
    public static final int TYPE_MONEY = 0;
    /**
     * 奖券金额
     */
    public static final int TYPE_INTEGRAL = 1;
    /**
     * 益豆金额
     */
    public static final int TYPE_LE_DOU = 2;
    public static final int TYPE_DONATION_STRESS = 1;
    String takeLedou;
    int rateLedou;
    int rateRateAngel;
    double angelConversionRatio;
    private ImageView ivHeader;
    private TextView tvMoney;
    private TextView tvWaitClose;
    private TextView tvTotalMoney;
    private TextView tvRemark;
    private Banner banner;
    private DropZoomScrollView dropZoomScrollView;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private int type;
    private ImageView ivIcon1;
    private TextView tvName1;
    private ImageView ivIcon2;
    private TextView tvName2;
    private ImageView ivIcon3;
    private TextView tvName3;
    private View includeView3;
    private TextView tvRecommend1;
    private TextView tvRecommend2;
    private TextView tvCategory;
    private View includeView1;
    private View includeView2;
    private View includeView4;
    private ImageView ivIcon4;
    private TextView tvName4;
    private String yiDouUrl;
    /**
     * 菜单列表
     */
    private RecyclerView menu_rv;
    private V3MoneyMenuAdapter menuAdapter;
    /**
     * 益豆转赠
     */
    private View includeView5;
    private TextView tvDonationStressName;
    private ImageView ivDonationStressIcon;
    private MyBalanceBeanEntity entity;

    /**
     * 奖券活动
     */
    //    private LinearLayout integral_activity_ll;
    //    private ImageView integral_activity1, integral_activity2, integral_activity3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v3_money);
        StatusBarUtils.transparencyBar(this);
        type = getIntent().getIntExtra("type", TYPE_MONEY);
        Logger.i("ledou-type:" + type);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        //区块益豆转赠
        includeView5 = findViewById(R.id.include_stress_donation);
        ivDonationStressIcon = includeView5.findViewById(R.id.iv_icon);
        tvDonationStressName = includeView5.findViewById(R.id.tv_name);


        tvCategory = (TextView) findViewById(R.id.tv_category);
        tvRecommend1 = (TextView) findViewById(R.id.tv_recommend1);
        tvRecommend2 = (TextView) findViewById(R.id.tv_recommend2);
        ivHeader = (ImageView) findViewById(R.id.iv_header);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvMoney.setTypeface(FontUtils.getFontTypeface(mContext, "fonts/DINMittelschrift.otf"));
        tvWaitClose = (TextView) findViewById(R.id.tv_wait_close);
        tvTotalMoney = (TextView) findViewById(R.id.tv_total_money);
        tvRemark = (TextView) findViewById(R.id.tv_remark);
        includeView1 = findViewById(R.id.include_yi_dou_bao);
        ivIcon1 = (ImageView) includeView1.findViewById(R.id.iv_icon);
        tvName1 = (TextView) includeView1.findViewById(R.id.tv_name);
        includeView2 = findViewById(R.id.include_transfer);
        ivIcon2 = (ImageView) includeView2.findViewById(R.id.iv_icon);
        tvName2 = (TextView) includeView2.findViewById(R.id.tv_name);

        includeView4 = findViewById(R.id.include_extractLeTaoAngle);
        ivIcon4 = (ImageView) includeView4.findViewById(R.id.iv_icon);
        tvName4 = (TextView) includeView4.findViewById(R.id.tv_name);
        if (type == TYPE_LE_DOU) {
            includeView4.setVisibility(View.VISIBLE);
        }
        includeView3 = findViewById(R.id.include_detail);
        ivIcon3 = (ImageView) includeView3.findViewById(R.id.iv_icon);
        tvName3 = (TextView) includeView3.findViewById(R.id.tv_name);

        banner = (Banner) findViewById(R.id.banner);
        dropZoomScrollView = (DropZoomScrollView) findViewById(R.id.drop_zoom_scroll_view);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setTextColor(Color.WHITE);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.VISIBLE);
        v3Shop.setImageResource(R.mipmap.icon_money_question_mark);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llTitle
                .getLayoutParams();
        layoutParams.setMargins(0, com.yilian.mylibrary.StatusBarUtils.getStatusBarHeight
                (mContext), 0, 0);

        menu_rv = (RecyclerView) findViewById(R.id.menu_rv);
        menu_rv.setLayoutManager(new GridLayoutManager(this, 3));
        menu_rv.setNestedScrollingEnabled(false);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    private void initData() {
    }

    private void initListener() {
        //        常见问题
        RxUtil.clicks(tvRecommend2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Ip.getWechatURL(mContext) + Constants
                        .HELP_CENTER);
                startActivity(intent);
            }
        });
        switch (type) {
            case TYPE_MONEY:
                setMoneyClick();
                break;
            case TYPE_INTEGRAL:
                setIntegralClick();
                break;
            case TYPE_LE_DOU:
                setLedouClick();
            default:
                break;
        }
    }

    private void setMoneyClick() {

        RxUtil.clicks(includeView5, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (isCert()) {
                    Intent intent = new Intent(mContext, JTakeCashActivity.class);
                    startActivity(intent);
                } else {
                    showToast("请进行实名认证");
                    Intent intent = new Intent(mContext, CertificationViewImplActivity.class);
                    startActivity(intent);
                }
            }
        });
        //                领奖励
        RxUtil.clicks(includeView1, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                startActivity(new Intent(mContext, OpenRedPackageActivity.class));
            }
        });
        //奖励明细
        RxUtil.clicks(includeView2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, V3MoneyListActivity.class);
                intent.putExtra("type", V3MoneyDetailActivity.TYPE_0);
                startActivity(intent);
            }
        });
        //                充值
        RxUtil.clicks(includeView3, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, RechargeActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        //奖励充值说明
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Constants.VOUCHER_AGREEMENT);
                startActivity(intent);
            }
        };
        //        右上角 交易记录
        v3Shop.setVisibility(View.GONE);
        tvRight2.setText("交易记录");
        tvRight2.setTextColor(Color.WHITE);
        tvRight2.setVisibility(View.VISIBLE);
        RxUtil.clicks(tvRight2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, TransactionRecordActivity.class);
                mContext.startActivity(intent);
            }
        });
        RxUtil.clicks(tvRecommend1, consumer);
    }

    private void setIntegralClick() {
        //        奖券转赠
        RxUtil.clicks(includeView2
                , new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        startActivity(new Intent(mContext, V3MemberGiftActivity.class));
                    }
                });
        //                奖券明细
        RxUtil.clicks(includeView3, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, V3MoneyListActivity.class);
                intent.putExtra("type", V3MoneyDetailActivity.TYPE_1);
                startActivity(intent);
            }
        });
        //奖券说明
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Constants.INTEGRAL_AGREEMENT);
                startActivity(intent);
            }
        };
        RxUtil.clicks(v3Shop, consumer);
        RxUtil.clicks(tvRecommend1, consumer);
    }

    private void setLedouClick() {
        //        提取到交易平台
        RxUtil.clicks(includeView2
                , new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Intent intent = new Intent(mContext, TakeLedouActivity.class);
                        intent.putExtra("ledou", takeLedou);
                        intent.putExtra("rate", rateLedou);
                        startActivity(intent);
                    }
                });
        //                提取益豆到益豆钱包
        RxUtil.clicks(includeView4, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, ExtractLeDouToPurseActivity.class);
                intent.putExtra("leDou", takeLedou);
                intent.putExtra("rate", rateLedou);
                intent.putExtra("angelConversionRatio", angelConversionRatio);
                startActivity(intent);
            }
        });
        //                区块益豆明细
        RxUtil.clicks(includeView3, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, BeansListActivity.class);
                startActivity(intent);
            }
        });
        //跳转益豆转赠
        RxUtil.clicks(includeView5, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, V3MemberGiftActivity.class);
                intent.putExtra("type", TYPE_DONATION_STRESS);
                intent.putExtra("balance_bean", entity);
                startActivity(intent);
            }
        });
        //区块益豆说明
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Constants.LEDOU_AGREEMENT);
                startActivity(intent);
            }
        };
        RxUtil.clicks(v3Shop, consumer);
        RxUtil.clicks(tvRecommend1, consumer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        及时更新数据，防止奖券转赠回到该界面后数据不刷新
        if (type == TYPE_LE_DOU) {
            getBean();
        } else {
            getMoney();
        }
    }

    @SuppressWarnings("unchecked")
    private void getBean() {
        startMyDialog(false);

        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getBeanData("get_bean_balance")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyBalanceBeanEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MyBalanceBeanEntity entity) {
                        V3MoneyActivity.this.entity = entity;
                        includeView1.setVisibility(View.GONE);
                        v3Title.setText(Constants.APP_PLATFORM_DONATE_NAME);
                        tvCategory.setText("我的" + Constants.APP_PLATFORM_DONATE_NAME);
                        tvCategory.setTextColor(Color.parseColor("#CFEFFF"));
                        ivHeader.setImageResource(R.mipmap.bg_ledou_header);
                        takeLedou = entity.bean;
                        rateLedou = entity.chargeRate;
                        rateRateAngel = entity.chargeRateAngel;
                        angelConversionRatio = entity.angelValue;
                        tvMoney.setText(MoneyUtil.getLeXiangBiNoZero(takeLedou));

                        Spanned spanned;
                        Spanned spanned1;
                        String str = "累计获得" + Constants.APP_PLATFORM_DONATE_NAME;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            spanned = Html.fromHtml(str + "<br><font color='#333333'><big><big>"
                                            + MoneyUtil.getLeXiangBiNoZero(entity.totalBean) +
                                            "</font>",
                                    Html.FROM_HTML_MODE_LEGACY);
                            spanned1 = Html.fromHtml("累计提取到账<br><font color='#333333'><big><big>"
                                    + MoneyUtil.getLeXiangBiNoZero(entity.extractBean) +
                                    "</font>", Html.FROM_HTML_MODE_LEGACY);
                        } else {
                            spanned = Html.fromHtml(str + "<br><font color='#333333'><big><big>"
                                    + MoneyUtil.getLeXiangBiNoZero(entity.totalBean) + "</font>");
                            spanned1 = Html.fromHtml("累计提取到账<br><font color='#333333'><big><big>"
                                    + MoneyUtil.getLeXiangBiNoZero(entity.extractBean) + "</font>");
                        }

                        tvTotalMoney.setText(spanned);
                        tvRemark.setText(spanned1);
                        includeView2.setVisibility(View.GONE);
                        tvName2.setText("提取到交易平台");
                        ivIcon2.setImageResource(R.mipmap.icon_mine_bean);
                        tvName4.setText("提取到益豆钱包");
                        ivIcon4.setImageResource(R.mipmap.icon_purse);
                        tvName3.setText(Constants.APP_PLATFORM_DONATE_NAME + "明细");
                        ivIcon3.setImageResource(R.mipmap.icon_money_record);
                        tvRecommend1.setText(Constants.APP_PLATFORM_DONATE_NAME + "说明");
                        tvRecommend2.setText("常见问题");

                        //益豆转赠
                        includeView5.setVisibility(View.VISIBLE);
                        tvDonationStressName.setText(Constants.APP_PLATFORM_DONATE_NAME + "转赠");
                        ivDonationStressIcon.setImageResource(R.mipmap.icon_ledou);
                    }
                });
        addSubscription(subscription);
    }

    @SuppressWarnings("unchecked")
    private void getMoney() {
        startMyDialog(false);

        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMoneyData("get_integral_balance")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyBalanceEntity2>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MyBalanceEntity2 myBalanceEntity) {
                        setData(myBalanceEntity);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(MyBalanceEntity2 myBalanceEntity) {
        Spanned spanned;
        Spanned spanned1;
        Logger.i("type:" + type);
        switch (type) {
            case TYPE_MONEY:
//                2018-06-27 15:30:08隐藏领奖励入口 用户不再手动拆红包 而是由系统自动拆红包
                includeView1.setVisibility(View.GONE);
                v3Title.setText("奖励");
                tvCategory.setText("我的奖励");
                tvCategory.setTextColor(Color.parseColor("#FFEFD4"));
                ivHeader.setImageResource(R.mipmap.bg_money_header);
                tvMoney.setText(MoneyUtil.getMoneyWith¥(myBalanceEntity.lebi));
                tvWaitClose.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(myBalanceEntity.dailyCash)) {
                    tvWaitClose.setVisibility(View.INVISIBLE);
                } else {
                    tvWaitClose.setVisibility(View.VISIBLE);
                }
                tvWaitClose.setText("今日奖励: " + MoneyUtil.getMoneyWith¥(myBalanceEntity.dailyCash));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    spanned = Html.fromHtml("累计领奖励<br><font color='#333333'><big><big>" +
                            MoneyUtil.getMoneyWith¥(myBalanceEntity.totalIntegralBonus) +
                            "</font>", Html.FROM_HTML_MODE_LEGACY);
                    spanned1 = Html.fromHtml("累计使用奖励<br><font color='#333333'><big><big>" +
                            MoneyUtil.getMoneyWith¥(myBalanceEntity.balanceNum) + "</font>", Html
                            .FROM_HTML_MODE_LEGACY);
                } else {
                    spanned = Html.fromHtml("累计领奖励<br><font color='#333333'><big><big>" +
                            MoneyUtil.getMoneyWith¥(myBalanceEntity.totalIntegralBonus) +
                            "</font>");
                    spanned1 = Html.fromHtml("累计使用奖励<br><font color='#333333'><big><big>" +
                            MoneyUtil.getMoneyWith¥(myBalanceEntity.balanceNum) + "</font>");
                }
                tvTotalMoney.setText(spanned);
                tvRemark.setText(spanned1);
                tvName1.setText("领奖励");
                ivIcon1.setImageResource(R.mipmap.icon_use_cash);
                tvName2.setText("奖励明细");
                ivIcon2.setImageResource(R.mipmap.icon_money_record);
                tvName3.setText("充值");
                ivIcon3.setImageResource(R.mipmap.icon_recharge);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                        includeView3.getLayoutParams();
                layoutParams.setMargins(0, DPXUnitUtil.dp2px(mContext, 10), 0, 0);
                tvRecommend1.setText("奖励说明");
                tvRecommend2.setText("常见问题");
//每日拆奖励
                LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams)
                        includeView5.getLayoutParams();
                layoutParams5.setMargins(0, DPXUnitUtil.dp2px(mContext, 10), 0, 0);
                includeView5.setVisibility(View.VISIBLE);
                tvDonationStressName.setText("使用奖励");
                ivDonationStressIcon.setImageResource(R.mipmap.icon_get_redpacket);
                includeView5.setVisibility(View.GONE);
                break;
            //                奖券
            case TYPE_INTEGRAL:
                getV3MoneyMenu();
                includeView1.setVisibility(View.GONE);
                v3Title.setText("奖券");
                tvCategory.setText("我的奖券");
                tvCategory.setTextColor(Color.parseColor("#CFEFFF"));
                ivHeader.setImageResource(R.mipmap.bg_integral_header);
                tvMoney.setText(MoneyUtil.getLeXiangBiNoZero(myBalanceEntity.integral));
                tvWaitClose.setVisibility(View.INVISIBLE);
                tvWaitClose.setText("待结算奖券: " + MoneyUtil.getLeXiangBiNoZero(myBalanceEntity
                        .PendIntegral));
                yiDouUrl = myBalanceEntity.yiDouUrl;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    spanned = Html.fromHtml("累计获得奖券<br><font color='#333333'><big><big>" +
                            MoneyUtil.getLeXiangBiNoZero(myBalanceEntity.mallIntegral) +
                            "</font>", Html.FROM_HTML_MODE_LEGACY);
                    spanned1 = Html.fromHtml("累计使用奖券<br><font color='#333333'><big><big>" +
                            MoneyUtil.getLeXiangBiNoZero(myBalanceEntity.usedIntegral) +
                            "</font>", Html.FROM_HTML_MODE_LEGACY);
                } else {
                    spanned = Html.fromHtml("累计获得奖券<br><font color='#333333'><big><big>" +
                            MoneyUtil.getLeXiangBiNoZero(myBalanceEntity.mallIntegral) + "</font>");
                    spanned1 = Html.fromHtml("累计使用奖券<br><font color='#333333'><big><big>" +
                            MoneyUtil.getLeXiangBiNoZero(myBalanceEntity.usedIntegral) + "</font>");
                }

                //                integral_activity_ll.setVisibility(View.VISIBLE);
                tvTotalMoney.setText(spanned);
                tvRemark.setText(spanned1);
                //                tvName1.setText("益豆宝");
                //                ivIcon1.setImageResource(R.mipmap.icon_yi_dou_bao);
                tvName2.setText("奖券转赠");
                ivIcon2.setImageResource(R.mipmap.icon_transfer);
                tvName3.setText("奖券明细");
                ivIcon3.setImageResource(R.mipmap.icon_money_record);
                tvRecommend1.setText("奖券说明");
                tvRecommend2.setText("常见问题");
                //                getBannerData();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void getV3MoneyMenu() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getV3MoneyMenu("jfb/get_integral_acts")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<V3MoneyMenuEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(V3MoneyMenuEntity menuEntity) {
                        List<V3MoneyMenuEntity.DataBean> imageList = menuEntity.data;
                        if (imageList != null && imageList.size() > 0) {
                            menuAdapter = new V3MoneyMenuAdapter(V3MoneyActivity.this, imageList);
                            menu_rv.setAdapter(menuAdapter);
                            menuAdapter.setOnItemClickListener(new BaseQuickAdapter
                                    .OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int
                                        position) {
                                    V3MoneyMenuEntity.DataBean bean = (V3MoneyMenuEntity
                                            .DataBean) adapter.getItem(position);
                                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(Integer
                                            .valueOf(bean.getType()), bean.getContent());
                                }
                            });
                            menu_rv.setVisibility(View.VISIBLE);
                        } else {
                            menu_rv.setVisibility(View.GONE);
                        }

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取益豆宝指数 获取指数API为益豆宝工作人员提供的固定API
     */
    @SuppressWarnings("unchecked")
    private void getYiDouBaoIndex() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getYiDouBaoIndex
                ("https://game.hnlfcywlkj" +
                        ".com/api/tongji/app_get_exp?pwd=76bc556ead0b90a0aab76ce83e867f0a")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            Gson gson = new Gson();
                            YiDouIndex yiDouIndex = gson.fromJson(string, YiDouIndex.class);
                            Logger.i("益豆指数：" + yiDouIndex.yiDouIndex);
                            TextView tvYiDouIndex = (TextView) findViewById(R.id.tv_yi_dou_index);
                            tvYiDouIndex.setMaxWidth(ScreenUtils.getScreenWidth(mContext) / 2);
                            tvYiDouIndex.setText("益豆宝指数(" + yiDouIndex.yiDouIndex + "‱)");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        addSubscription(subscription);
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

    @SuppressWarnings("unchecked")
    void getBannerData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getJumpBanner("account/get_jump_banner", GetBannerDataType.MY_INTEGRAL,
                        GetBannerDataType.FLG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerDataEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(BannerDataEntity bannerDataEntity) {
                        List<BannerDataEntity.DataBean> data = bannerDataEntity.data;
                        setBannerData(data);
                    }
                });
        addSubscription(subscription);
    }

    private void setBannerData(List<BannerDataEntity.DataBean> data) {
        if (data == null || data.size() <= 0) {
            banner.setVisibility(View.GONE);
            return;
        }
        banner.setVisibility(View.VISIBLE);
        ArrayList<String> imageUrls = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            String image = data.get(i).image;
            String webImageUrlNOSuffix = WebImageUtil.getInstance().getWebImageUrlNOSuffix(image);
            Logger.i("bannerImageUrl: " + image + "  nosuffix: " + webImageUrlNOSuffix);
            imageUrls.add(webImageUrlNOSuffix);
        }
        banner.setImages(imageUrls)
                .isAutoPlay(false)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new BannerViewGlideUtil())
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        BannerDataEntity.DataBean dataBean = data.get(position);
                        JumpToOtherPage.getInstance(mContext).jumpToOtherPage(dataBean.type,
                                dataBean.content);
                    }
                }).start();

    }

    private class YiDouIndex extends HttpResultBean {
        @SerializedName("exp")
        private String yiDouIndex;
    }
}
