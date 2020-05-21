package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.presenter.JDOrderPayPresenter;
import com.yilian.mall.ui.mvp.presenter.impl.ExchangeOrderPayPresenter;
import com.yilian.mall.ui.mvp.presenter.impl.UserMoneyPresenterImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IUserMoneyPresenter;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;

import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.CASH_PAY;
import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.MIX_PAY;
import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.THIRD_PAY;

public class ExchangeCashDeskActivity extends BaseAppCompatActivity implements View.OnClickListener, IUserMoneyView, JDOrderPayPresenter.View {

    public static final String TAG_EXPRESS = "express";
    public static final String TAG_EXCHANGE = "exchange";
    public static final String TAG_ORDER_ID = "orderId";
    /**
     * 奖励支付金额 单位：分
     */
    public String paymentWithCash = "0";
    /**
     * 除去奖励的支付金额 单位：分
     */
    public String paymentWithoutCash = "0";
    /**
     * 最终的支付方式 默认为奖励支付
     */
    public int myPayType = CASH_PAY;
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private TextView tvUserCanUseExchange;
    private TextView tvAmountCash;
    private TextView tvAmountMerchantIncome;
    private ImageView ivWhetherUseMerchantIncome;
    private ImageView ivIcon;
    private TextView tvClassName;
    private TextView tvClassSubTitle;
    private ImageView commitExpressIcon;
    private RelativeLayout rl;
    private TextView tvOrderTotalExchange;
    private TextView tvOrderExpress;
    private Button btnPay;
    private IUserMoneyPresenter userMoneyPresenter;
    /**
     * 用户拥有兑换券金额
     */
    private String mExchange = "0";
    /**
     * 用户拥有奖励
     */
    private String mCash = "0";
    /**
     * 订单支付兑换券金额 单位：分
     */
    private String orderExchange = "0";
    /**
     * 订单运费 单位：分
     */
    private String orderExpress = "0";
    /**
     * 用户拥有营收额 单位：分
     */
    private String mMerchantIncome = "0";
    private String orderId;
    /**
     * 最终支付的营收款金额 单位：分
     */
    private String mMerchantPayment = "0";
    private String mThirdPayment = "0";
    /**
     * 用户营收款是否足够支付商品
     */
    private boolean userMerchantIncomeIsEnough = false;
    private ExchangeOrderPayPresenter exchangeOrderPayPresenter;
    private String paySuccessHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_cash_desk);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("收银台");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvUserCanUseExchange = (TextView) findViewById(R.id.tv_user_can_use_exchange);
        tvAmountCash = (TextView) findViewById(R.id.tv_amount_cash);
        tvAmountMerchantIncome = (TextView) findViewById(R.id.tv_amount_merchant_income);
        ivWhetherUseMerchantIncome = (ImageView) findViewById(R.id.iv_whether_use_merchant_income);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvClassName = (TextView) findViewById(R.id.tv_class_name);
        tvClassSubTitle = (TextView) findViewById(R.id.tv_class_sub_title);
        commitExpressIcon = (ImageView) findViewById(R.id.commit_express_icon);
        rl = (RelativeLayout) findViewById(R.id.rl);
        tvOrderTotalExchange = (TextView) findViewById(R.id.tv_order_total_exchange);
        tvOrderExpress = (TextView) findViewById(R.id.tv_order_freight_price);
        btnPay = (Button) findViewById(R.id.btn_pay);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnPay.setOnClickListener(this);
    }

    private void initData() {
        orderExpress = getIntent().getStringExtra(TAG_EXPRESS);
        orderExchange = getIntent().getStringExtra(TAG_EXCHANGE);
//        setTestData();

        tvOrderTotalExchange.setText(MoneyUtil.getLeXiangBi(orderExchange));
        tvOrderExpress.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(orderExpress)));
        orderId = getIntent().getStringExtra(TAG_ORDER_ID);
        paySuccessHtml = Ip.getBaseURL(mContext) + "mobi/dist/ESuccessView?orderIndex=" + orderId;

        getUserMoney();
    }

    private void initListener() {
        RxUtil.clicks(btnPay, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (exchangeOrderPayPresenter == null) {
                    exchangeOrderPayPresenter
                            = new ExchangeOrderPayPresenter(mContext, ExchangeCashDeskActivity.this, orderId, mMerchantPayment);
                }
                exchangeOrderPayPresenter.pay(myPayType, NumberFormat.convertToFloat(mThirdPayment, 0f));
            }
        });
        RxUtil.clicks(commitExpressIcon, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                changePayType();
            }
        });
        RxUtil.clicks(ivWhetherUseMerchantIncome, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                changePayType();
            }
        });
    }

    private void getUserMoney() {
        if (userMoneyPresenter == null) {
            userMoneyPresenter = new UserMoneyPresenterImpl(this);
        }
        userMoneyPresenter.getUserMoney(mContext);
    }

    private void changePayType() {
        switch (myPayType) {
            case CASH_PAY:
                setPayTypeThird(paymentWithoutCash);
                break;
            case MIX_PAY:
                setPayTypeThird(paymentWithoutCash);
                break;
            case THIRD_PAY:
                if (userMerchantIncomeIsEnough) {
                    setPayTypeCash(paymentWithoutCash);
                } else {
                    if (new BigDecimal(mMerchantIncome).compareTo(new BigDecimal("0")) > 0) {
//                            如果有营收款
                        setPayTypeMix(mMerchantIncome, new BigDecimal(paymentWithoutCash)
                                .subtract(new BigDecimal(mMerchantIncome)).toString());
                    }
                }

                break;

            default:
                break;
        }
    }

    /**
     * 使用第三方支付
     *
     * @param thirdPayment 第三方支付金额
     */
    void setPayTypeThird(String thirdPayment) {
        myPayType = THIRD_PAY;
        ivWhetherUseMerchantIncome.setImageResource(R.mipmap.library_module_cash_desk_off);
        commitExpressIcon.setImageResource(R.mipmap.icon_red_check_pay_type);
        mMerchantPayment = "0.00";
        tvAmountMerchantIncome.setText("0.00");
        mThirdPayment = thirdPayment;
        btnPay.setText("支付宝支付" + MoneyUtil.getLeXiangBi(thirdPayment) + "元");
    }

    /**
     * 使用营收款支付
     *
     * @param merchantIncomePayment 营收款支付金额
     */
    void setPayTypeCash(String merchantIncomePayment) {
        myPayType = CASH_PAY;
        ivWhetherUseMerchantIncome.setImageResource(R.mipmap.icon_red_pay_switch);
        commitExpressIcon.setImageResource(R.mipmap.library_module_commit_express_off);
        mMerchantPayment = merchantIncomePayment;
        tvAmountMerchantIncome.setText(MoneyUtil.getLeXiangBi(merchantIncomePayment));
        btnPay.setText("立即支付");
    }

    /**
     * 使用混合支付
     *
     * @param merchantIncomePayment 营收款支付金额
     * @param thirdPayment          第三方支付金额
     */
    void setPayTypeMix(String merchantIncomePayment, String thirdPayment) {
        myPayType = MIX_PAY;
        ivWhetherUseMerchantIncome.setImageResource(R.mipmap.icon_red_pay_switch);
        commitExpressIcon.setImageResource(R.mipmap.icon_red_check_pay_type);
        mMerchantPayment = merchantIncomePayment;
        tvAmountMerchantIncome.setText(MoneyUtil.getLeXiangBi(merchantIncomePayment));
        mThirdPayment = thirdPayment;
        btnPay.setText("第三方支付" + MoneyUtil.getLeXiangBi(thirdPayment) + "元");
    }

    void setTestData() {
        orderExpress = "90000";
        orderExchange = "100";
        orderId = "1";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_pay:

                break;
            default:
                break;
        }
    }

    @Override
    public void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2) {
        mExchange = myBalanceEntity2.exchange;
        tvUserCanUseExchange.setText(MoneyUtil.getLeXiangBi(mExchange));

        mMerchantIncome = myBalanceEntity2.merchantIncome;
        mCash = myBalanceEntity2.lebi;

//orderExpress 支付金额
//        mCash   用户奖励
//        mMerchantIncome 用户营收额
        if (new BigDecimal(orderExpress).compareTo(new BigDecimal(mCash)) > 0) {
//            用户奖励不足以支付订单
//            先强制扣除奖励 营收额和第三方可以自由选择
            ivWhetherUseMerchantIncome.setEnabled(true);
            commitExpressIcon.setEnabled(true);
            paymentWithCash = mCash;
            paymentWithoutCash = new BigDecimal(orderExpress).subtract(new BigDecimal(mCash)).toString();


        } else {
//            用户奖励足以支付订单 强制使用奖励支付
            ivWhetherUseMerchantIncome.setEnabled(false);
            ivWhetherUseMerchantIncome.setImageResource(R.mipmap.library_module_cash_desk_off);
            commitExpressIcon.setEnabled(false);
            paymentWithCash = orderExpress;
            paymentWithoutCash = "0";
        }
        tvAmountCash.setText(MoneyUtil.getLeXiangBi(paymentWithCash));
        initPayType();
    }

    private void initPayType() {
        if (new BigDecimal(mMerchantIncome).compareTo(new BigDecimal(paymentWithoutCash)) >= 0) {
//            营收款足以支付除去现金的剩余部分
//            使用营收款支付
            userMerchantIncomeIsEnough = true;
            setPayTypeCash(paymentWithoutCash);
        } else {
            userMerchantIncomeIsEnough = false;
//            营收款不足以支付除去现金的剩余部分
//            使用混合支付
            if (new BigDecimal(mMerchantIncome).compareTo(new BigDecimal("0")) == 0) {
//                没有营收款 则使用第三方支付
                setPayTypeThird(paymentWithoutCash);
            } else {
//                有营收款，则使用混合支付
                setPayTypeMix(mMerchantIncome,
                        new BigDecimal(paymentWithoutCash)
                                .subtract(new BigDecimal(mMerchantIncome)).toString());
            }

        }
    }

    @Override
    public void paySuccess(HttpResultBean httpResultBean) {
        finish();
//        todo 跳转支付成功h5
        WebViewActivity webViewActivity = (WebViewActivity) AppManager.getInstance().getActivity("WebViewActivity");
        if (webViewActivity != null) {
            webViewActivity.webView.loadUrl(paySuccessHtml);
        } else {
            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, paySuccessHtml, false);
        }
    }
}
