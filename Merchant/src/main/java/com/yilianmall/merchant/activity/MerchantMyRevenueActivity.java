package com.yilianmall.merchant.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mylibrary.CodeException;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.networkingmodule.entity.MerchantMyRevenueEntity;
import com.yilian.networkingmodule.entity.TakeCashEntity2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;

import org.greenrobot.eventbus.Subscribe;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的营收
 *
 * @author Created by zhaiyaohua on 2018/4/25.
 */

public class MerchantMyRevenueActivity extends BaseActivity {
    public static final int HAS_IS_CERT = 1;
    /**
     * 页面类型
     */
    private ImageView ivHeader;
    private TextView tvBalance, tvBalanceNum, tvAddUpBalance, tvUseBalance, tvExplain, tvQuestion;
    private View includeDetails, includeExtractRevenue;

    private ImageView ivBack;
    private ImageView ivRight;
    private TextView tvTitle;
    private View title;
    private MerchantMyRevenueEntity mEntity;
    private boolean isFirst = true;
    private TakeCashEntity2 mCashEntity;
    /**
     * 0代表没有卡
     * 1代表有卡
     */
    private Integer cardStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_my_revenue);
        initView();
        initListener();
    }

    protected void initView() {
        StatusBarUtils.transparencyBar(this);
        title = findViewById(R.id.titile);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) title
                .getLayoutParams();
        layoutParams.setMargins(0, com.yilian.mylibrary.StatusBarUtils.getStatusBarHeight
                (mContext), 0, 0);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivRight = findViewById(R.id.iv_right);
        ivRight.setImageResource(R.mipmap.merchant_icon_money_question_mark);
        ivRight.setVisibility(View.VISIBLE);
        ivHeader = findViewById(R.id.iv_header);
        tvBalance = findViewById(R.id.tv_balance);
        tvBalanceNum = findViewById(R.id.tv_balance_num);
        tvAddUpBalance = findViewById(R.id.tv_add_up_balance);
        tvUseBalance = findViewById(R.id.tv_use_balance);
        includeDetails = findViewById(R.id.include_details);
        includeExtractRevenue = findViewById(R.id.include_extract_revenue);
        tvExplain = findViewById(R.id.tv_explain);
        tvQuestion = findViewById(R.id.tv_question);
        tvTitle.setText("我的营收");
        tvBalance.setText("营收余额");
        tvAddUpBalance.setText("累计营收额");
        tvUseBalance.setText("累计提取营收额");
        tvExplain.setText("营收说明");
        tvQuestion.setText("常见问题");
        ivHeader.setImageResource(R.mipmap.merchant_bg_revenue_header);
        //提取营收
        setItemData(includeExtractRevenue);
        //明细
        setItemData(includeDetails);
    }

    protected void initListener() {
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        //跳转明细
        RxUtil.clicks(includeDetails, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.V3MoneyListActivity"));
                intent.putExtra("type", Constants.TYPE_EXTRACT_REVENUE_108);
                intent.putExtra("screen_type",Constants.TYPE_EXTRACT_REVENUE_0);
                startActivity(intent);
            }
        });
        //提取营收跳转
        RxUtil.clicks(includeExtractRevenue, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (null == mEntity) {
                    getMyRevenue(true);
                } else {
                    if (mEntity.isCert == HAS_IS_CERT) {
                        jumpToExtractRevenue();
                    } else {
                        jumpToCertification();
                    }
                }
            }
        });
        //跳转说明
        RxUtil.clicks(tvExplain, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpToExplain();
            }
        });
        //跳转常见问题
        RxUtil.clicks(tvQuestion, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                intent.putExtra(Constants.SPKEY_URL, Ip.getWechatURL(mContext) + Constants
                        .HELP_CENTER);
                mContext.startActivity(intent);
            }
        });
        //说明
        RxUtil.clicks(ivRight, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                jumpToExplain();
            }
        });
    }

    private void setItemData(View view) {
        ImageView ivIcon = view.findViewById(R.id.iv_icon);
        TextView tvName = view.findViewById(R.id.tv_name);
        int viewId = view.getId();
        if (viewId == R.id.include_details) {
            tvName.setText("明细");
            ivIcon.setImageResource(R.mipmap.merchant_icon_details);
        } else if (viewId == R.id.include_extract_revenue) {
            tvName.setText("提取营收");
            ivIcon.setImageResource(R.mipmap.merchant_icon_revenue);
        }
    }

    /**
     * 点击提取营跳转
     */
    private void jumpToExtractRevenue() {
        if (null == cardStatus) {
            startMyDialog(false);
            getTakeCashDefaultBankCard(true);
        } else {
            JumpBindCarOrExtractRevenue();
        }
    }

    /**
     * 跳转说明
     */
    private void jumpToExplain() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
        intent.putExtra(Constants.SPKEY_URL, Constants.MERCHANT_MAY_REVENUE);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst && hasFocus) {
            getMyRevenue(false);
            isFirst = false;
        }
    }

    /**
     * 获取我的营收
     */
    @SuppressWarnings("unchecked")
    private void getMyRevenue(final boolean jump) {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getMerchantMyRevenueEntity("get_money_balance")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantMyRevenueEntity>() {
                    @Override
                    public void onCompleted() {
                        if (jump && mEntity.isCert != 1) {
                            stopMyDialog();
                            jumpToCertification();
                        } else {
                            getTakeCashDefaultBankCard(jump);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MerchantMyRevenueEntity entity) {
                        mEntity = entity;
                        setAddNum(tvAddUpBalance, "累计营收额", MoneyUtil.getLeXiangBiNoZero(entity.totalMoney));
                        setAddNum(tvUseBalance, "累计提取营收额", MoneyUtil.getLeXiangBiNoZero(entity.extractMoney));
                        tvBalanceNum.setText(MoneyUtil.add¥Front(MoneyUtil.getLeXiangBiNoZero(entity.money)));
                    }
                });


        addSubscription(subscription);
    }

    /**
     * 跳转实名认证
     */
    private void jumpToCertification() {
        showToast("请您先进行实名认证");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationViewImplActivity"));
        startActivity(intent);

    }

    /**
     * 新版获取默认提现银行卡
     */
    @SuppressWarnings("unchecked")
    private void getTakeCashDefaultBankCard(final boolean jump) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getTakeCashDefaultCard("userBank/my_default_bank")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TakeCashEntity2>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        cardStatus = 1;
                        if (jump) {
                            JumpBindCarOrExtractRevenue();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        if (e instanceof CodeException) {
                            if (0 == ((CodeException) e).code) {
                                cardStatus = ((CodeException) e).code;
                                if (jump) {
                                    JumpBindCarOrExtractRevenue();
                                }
                            } else {
                                showToast(e.getMessage());
                            }
                        } else {
                            showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(TakeCashEntity2 takeCashEntity2) {
                        mCashEntity = takeCashEntity2;
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 设置累计额
     *
     * @param textView
     * @param note
     * @param num
     */
    private void setAddNum(TextView textView, String note, String num) {
        String str = note + "\n" + MoneyUtil.add¥Front(num);
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.merchant_c333333));
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(15, true);
        int length = str.length();
        int startIndex = length - num.length() - 1;
        spannableString.setSpan(colorSpan, startIndex, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, startIndex, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    private void JumpBindCarOrExtractRevenue() {
        switch (cardStatus) {
            case 0:
                //跳转营收绑卡
                Intent intent = new Intent(mContext, MerchantRevenueBindCardActivity.class);
                startActivity(intent);
                break;
            case 1:
                //  跳转提取营收
                Intent intent1 = new Intent(mContext, MerchantExtractRevenueActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    /**
     * 刷新我的营收页面数据并置空银行卡状态
     * {@link com.yilianmall.merchant.activity.MerchantRevenueBindCardActivity#onActivityResult(int requestCode, int resultCode, Intent data)}
     *
     * @param entity
     */
    @Subscribe
    public void updateRevenueInfo(MerchantMyRevenueEntity entity) {
        isFirst = true;
        cardStatus = null;
    }
}
