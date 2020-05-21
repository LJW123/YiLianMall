package com.yilian.mall.jd.activity;

import android.content.DialogInterface;
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
import com.yilian.mall.jd.presenter.impl.JDPayPresenterImpl;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.ui.mvp.presenter.impl.UserMoneyPresenterImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IUserMoneyPresenter;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.jdEventBusModel.JDOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;
import rx.Subscription;
import rx.functions.Action1;

import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.CASH_PAY;
import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.MIX_PAY;
import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.THIRD_PAY;

/**
 * @author  京东收银台
 */
public class JDCashDeskActivity2 extends BaseAppCompatActivity implements View.OnClickListener, JDOrderPayPresenter.View, IUserMoneyView {

    public static final String TAG = "JDCashDeskActivity";

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
    private LinearLayout llTitle;
    private TextView tvMinutes;
    private TextView tvSecond;
    private TextView tvJdOrderPrice;
    private TextView tvOrderMerchantIncomePayMoney;
    private ImageView btnWhetherUseMerchantIncome;
    private ImageView ivIcon;
    private TextView tvClassName;
    private TextView tvClassSubTitle;
    private ImageView commitExpressIcon;
    private RelativeLayout rl;
    private Button btnPay;
    private JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity;
    /**
     * 用户营收款是否足够支付商品
     */
    private boolean userMoneyIsEnough = false;
    /**
     * 支付方式里面是否有奖励
     */
    private boolean isUseMoney = true;
    /**
     * 用户拥有营收款金额 单位元
     */
    private float merchantIncomeMoney;
    /**
     * 除去用户奖励支付部分的订单价格 单位元
     */
    private float orderPriceWithoutUserCash;
    /**
     * 用户奖励支付的那部分订单金额
     */
    private float orderPricePartOfUserCash;
    private JDOrderPayPresenter jdPayCashPresenter;
    /**
     * 第三方支付需要的金额
     */
    private float thirdPayFee = 0;
    private String orderCountTime = "";
    /**
     * 使用奖励支付金额  单位 元
     */
    private String userMoneyPayMoney = "0";
    /**
     * 使用营收支付金额 单位 元
     */
    private String userMerchantIncomePayMoney = "0";
    /**
     * 用户奖励余额
     */
    private float userMoney;
    private TextView tvOrderCashPayMoney;
    private LinearLayout llMerchantIncomePay;
    private TextView tvHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdcash_desk_2);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvHours = findViewById(R.id.tv_hours);
        llMerchantIncomePay = findViewById(R.id.ll_merchant_income_pay);
        tvOrderCashPayMoney = findViewById(R.id.tv_order_cash_price);
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
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvMinutes = (TextView) findViewById(R.id.tv_minutes);
        tvSecond = (TextView) findViewById(R.id.tv_second);
        tvJdOrderPrice = (TextView) findViewById(R.id.tv_jd_order_price);
        tvOrderMerchantIncomePayMoney = (TextView) findViewById(R.id.tv_order_merchant_income);
        btnWhetherUseMerchantIncome = (ImageView) findViewById(R.id.btn_whether_use_merchant_income);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvClassName = (TextView) findViewById(R.id.tv_class_name);
        tvClassSubTitle = (TextView) findViewById(R.id.tv_class_sub_title);
        commitExpressIcon = (ImageView) findViewById(R.id.commit_express_icon);
        rl = (RelativeLayout) findViewById(R.id.rl);
        btnPay = (Button) findViewById(R.id.btn_pay);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnPay.setOnClickListener(this);
    }

    private void initData() {
        jdCommitOrderSuccessEntity = (JDCommitOrderSuccessEntity) getIntent().getSerializableExtra(TAG);
//        先根据改订单代购券数量计算(/修正)商品价钱 商品价钱=原商品价钱-代购券数量
        jdCommitOrderSuccessEntity.goodsPrice = String.valueOf(
                new BigDecimal(jdCommitOrderSuccessEntity.goodsPrice)
                        .subtract(new BigDecimal(jdCommitOrderSuccessEntity.daiGouQuanMoney))
        );

        getUserMoney();
    }

    private void initListener() {
        RxUtil.clicks(btnWhetherUseMerchantIncome, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                changePayType();
            }
        });
        RxUtil.clicks(commitExpressIcon, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                changePayType();
            }
        });
    }

    private void getUserMoney() {
        IUserMoneyPresenter userMoneyPresenter = new UserMoneyPresenterImpl(this);
        Subscription subscription = userMoneyPresenter.getUserMoney(mContext);
        addSubscription(subscription);
    }

    /**
     * 改变支付类型
     */
    private void changePayType() {
        switch (myPayType) {
            case CASH_PAY:
                setPayTypeThird(orderPriceWithoutUserCash);
                break;
            case MIX_PAY:
                setPayTypeThird(orderPriceWithoutUserCash);
                break;
            case THIRD_PAY:
                if (userMoneyIsEnough) {
                    setPayTypeCash();
                } else {
                    if (merchantIncomeMoney > 0) {
                        setPayTypeMix(merchantIncomeMoney, orderPriceWithoutUserCash);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置支付方式为第三方支付
     *
     * @param orderPriceWithoutUserCash
     */
    private void setPayTypeThird(float orderPriceWithoutUserCash) {
        myPayType = THIRD_PAY;
        btnWhetherUseMerchantIncome.setImageResource(R.mipmap.library_module_cash_desk_off);
        commitExpressIcon.setImageResource(R.mipmap.icon_red_check_pay_type);
        setCahsPayView("0.00");
        thirdPayFee = orderPriceWithoutUserCash;
        btnPay.setText("支付宝支付" + thirdPayFee + "元");
    }

    /**
     * 设置支付方式为奖励+营收额支付
     */
    private void setPayTypeCash() {
        myPayType = CASH_PAY;
        userMoneyIsEnough = true;
        btnWhetherUseMerchantIncome.setImageResource(R.mipmap.icon_red_pay_switch);
        commitExpressIcon.setImageResource(R.mipmap.library_module_commit_express_off);
        setCahsPayView(String.valueOf(orderPriceWithoutUserCash));
        btnPay.setText("立即支付");
    }

    /**
     * 设置支付方式为混合支付
     *
     * @param userMoney
     * @param orderPriceWithoutUserCash
     */
    private void setPayTypeMix(float userMoney, float orderPriceWithoutUserCash) {
        myPayType = MIX_PAY;
        userMoneyIsEnough = false;
        btnWhetherUseMerchantIncome.setImageResource(R.mipmap.icon_red_pay_switch);
        commitExpressIcon.setImageResource(R.mipmap.icon_red_check_pay_type);
        setCahsPayView(String.valueOf(userMoney));
        BigDecimal subtract = new BigDecimal(String.valueOf(orderPriceWithoutUserCash)).subtract(new BigDecimal(String.valueOf(userMoney)));
        thirdPayFee = Float.valueOf(String.valueOf(subtract));
        btnPay.setText("第三方支付" + thirdPayFee + "元");
    }

    void setCahsPayView(String price) {
        userMerchantIncomePayMoney = price;
        tvOrderMerchantIncomePayMoney.setText("¥" + price);
    }

    @Override
    public void onBackPressed() {
        showLeaveDialog();
    }

    void showLeaveDialog() {
        showSystemV7Dialog("确认要离开收银台？", "您的订单在" + orderCountTime + "内未支付将被取消,请尽快完成支付。", "确认离开", "继续支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
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
                showLeaveDialog();
                break;
            case R.id.btn_pay:
                pay(myPayType);
                break;
            default:
                break;
        }
    }

    private void pay(int payType) {
        if (jdPayCashPresenter == null) {
            jdPayCashPresenter = new JDPayPresenterImpl(
                    mContext,
                    this,
                    jdCommitOrderSuccessEntity.orderIndex,
                    String.valueOf(
                            new BigDecimal(userMerchantIncomePayMoney)
                                    .multiply(new BigDecimal("100"))));
        }
        jdPayCashPresenter.pay(payType, thirdPayFee);
    }

    /**
     * 奖励支付成功
     *
     * @param jdPayInfoEntity
     */
    @Override
    public void paySuccess(HttpResultBean jdPayInfoEntity) {
        ((JDPayPresenterImpl) jdPayCashPresenter).onDestory();
        JumpJdActivityUtils.toJDJDPaySuccessActivity(mContext, jdPayInfoEntity, JDPaySuccessActivity.PaySide.JD);
        finish();

        //EventBus 推送
        JDOrderListUpdateModel updateModel = new JDOrderListUpdateModel();
        updateModel.handleType = JDOrderListUpdateModel.HandleType_pay;
        updateModel.jdOrderId = jdCommitOrderSuccessEntity.orderId;
        /**
         * {@link  com.yilian.mall.jd.fragment.order.JDOrderCommonFragment#receiveEvent(JDOrderListUpdateModel)}
         */
        EventBus.getDefault().post(updateModel);
    }

    @Override
    public void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2) {

        merchantIncomeMoney = NumberFormat.convertToFloat(myBalanceEntity2.merchantIncome, 0) / 100;
        userMoney = NumberFormat.convertToFloat(myBalanceEntity2.lebi, 0) / 100;

        //        需支付的订单总价
        float orderTotalPrice = com.yilian.mylibrary.NumberFormat.convertToFloat(
                getOrderPrice(jdCommitOrderSuccessEntity.goodsPrice, jdCommitOrderSuccessEntity.freight), 0);
        tvJdOrderPrice.setText(String.format("%s元", orderTotalPrice));
        if (userMoney >= orderTotalPrice) {
            orderPricePartOfUserCash = orderTotalPrice;
            orderPriceWithoutUserCash = 0;
            llMerchantIncomePay.setVisibility(View.GONE);
            commitExpressIcon.setEnabled(false);
        } else {
            orderPricePartOfUserCash = userMoney;
            orderPriceWithoutUserCash =
                    NumberFormat.convertToFloat(
                            String.valueOf(new BigDecimal(String.valueOf(orderTotalPrice))
                                    .subtract(new BigDecimal(String.valueOf(userMoney)))), 0);
        }
        setData(jdCommitOrderSuccessEntity);

        initPayType();
    }

    /**
     * 根据商品价格和运费计算订单价格
     *
     * @param goodPrice
     * @param freight
     * @return
     */
    String getOrderPrice(String goodPrice, String freight) {
        return String.valueOf(new BigDecimal(goodPrice).add(new BigDecimal(freight)));
    }

    private void setData(JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity) {
        tvOrderCashPayMoney.setText(String.format("¥%s", orderPricePartOfUserCash));
        RxUtil.countDown(NumberFormat.convertToInt(jdCommitOrderSuccessEntity.countDown, 0))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        String hours = String.valueOf(aLong / 3600);
                        if (hours.length() == 1) {
                            hours = supZero(hours);
                        }
                        tvHours.setText(hours);
                        String minute = String.valueOf(aLong % 3600 / 60);
                        if (minute.length() == 1) {
                            minute = supZero(minute);
                        }
                        tvMinutes.setText(minute);
                        String second = String.valueOf(aLong % 60);
                        if (second.length() == 1) {
                            second = supZero(second);
                        }
                        tvSecond.setText(second);
                        orderCountTime = hours + "小时" + minute + "分" + second + "秒";
                    }
                });
    }

    /**
     * 初始化支付方式
     */
    private void initPayType() {
        if (merchantIncomeMoney >= orderPriceWithoutUserCash) {
//            使用营收额支付
            setPayTypeCash();
        } else {
//            使用营收额+第三方混合支付
            setPayTypeMix(merchantIncomeMoney, orderPriceWithoutUserCash);
            if (merchantIncomeMoney == 0) {
//                第三方支付
                setPayTypeThird(orderPriceWithoutUserCash);
            }
        }
    }

    String supZero(String str) {
        return "0" + str;
    }
}
