package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.PayTypeSelectorAdapter;
import com.yilian.mall.entity.MakeMallShoppingOrderEntity;
import com.yilian.mall.ui.mvp.presenter.impl.GetPayTypePresenterImpl;
import com.yilian.mall.ui.mvp.presenter.impl.OrderPayPresenterImpl;
import com.yilian.mall.ui.mvp.presenter.impl.UserMoneyPresenterImpl;
import com.yilian.mall.ui.mvp.view.inter.IOrderPayView;
import com.yilian.mall.ui.mvp.view.inter.IPayTypeView;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JsPayClass;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PullAliPayUtil;
import com.yilian.mylibrary.PullAliPayUtilSuccessListener;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.pay.PayFrom;
import com.yilian.mylibrary.pay.PayType;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.mylibrary.widget.PasswordFinished;
import com.yilian.mylibrary.widget.PayDialog;
import com.yilian.networkingmodule.entity.GoodsChargeForPayResultEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.PayOkEntity;
import com.yilian.networkingmodule.entity.PayTypeEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

import static com.yilian.mylibrary.pay.ThirdPayForType.PAY_FOR_GOODS;
import static com.yilian.networkingmodule.entity.MyBalanceEntity2.IS_MERCHANT;

public class CashDeskActivity2 extends BaseAppCompatActivity implements IUserMoneyView, IPayTypeView, PasswordFinished, IOrderPayView, PullAliPayUtilSuccessListener {


    public static final String PAY_ZERO = "0";
    public static final int POSITION_ZERO = 0;
    public static final int UN_SELECTION_THIRD_PAY = -1;
    public static final String PAY_FROM_TAG = "type";
    /**
     * 获取支付结果类型1  (所有类型包括 商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11)
     */
    public static final String GET_PAY_RESULT_TYPE = "1";
    public static final double DEFAULT_VALUE = 0d;
    /**
     * 是否使用用户奖励支付 该值永远为true 有奖励使用奖励 无奖励则使用奖励支付金额为0
     */
    private final boolean useUserMoney = true;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private LinearLayout llV3title;
    private Button btnSurePay;
    private LinearLayout llPay;
    private TextView tvBuyPrice;
    private TextView tvUsableMoney;
    private TextView tvCostIncome;
    private ImageView btnWhetherUseIncome;
    private LinearLayout llCostIncome;
    private TextView tvThirdPayTypeTitle;
    private NoScrollListView payTypeListView;
    private ScrollView scrollview;
    /**
     * 支付来源
     */
    private PayFrom payFrom;
    /**
     * 支付订单号
     */
    private String orderId = "";
    /**
     * 订单总金额 单位分
     */
    private String orderTotalMoney = "0";
    /**
     * 1商城订单 2 商家入驻或续费 3店内支付
     */
    private String orderType;
    private UserMoneyPresenterImpl userMoneyPresenter;
    /**
     * 用户奖励 单位 分
     */
    private String userMoney = "0";
    /**
     * 商家营收 单位 分
     */
    private String merchantIncome = "0";
    /**
     * 奖励支付金额 单位 分
     */
    private String mPayUserMoney = "0";
    /**
     * 营收额支付金额 单位 分
     */
    private String mPayMerchantIncome = "0";
    /**
     * 第三放支付金额 单位 分
     */
    private String mPayThird = "0";
    /**
     * 支付方是否是商家身份
     */
    private boolean mIsMerchant = false;
    /**
     * 商家支付时 是否使用营收款进行支付
     */
    private boolean merchantUseIncome = true;
    /**
     * 用户奖励是否足够支付订单
     */
    private boolean userMoneyIsEnough = false;
    /**
     * 商家营收是否足够支付订单
     */
    private boolean merchantIncomeIsEnough = false;
    /**
     * 用户奖励+商家营收综合是否足够支付订单
     */
    private boolean moneyAddIncomeIsEnough = false;
    private GetPayTypePresenterImpl payTypePresenter;
    /**
     * 支付方式集合
     */
    private ArrayList<PayTypeEntity.PayData> payTypeList;
    private PayTypeSelectorAdapter payTypeSelectorAdapter;
    /**
     * 是否选择使用商家营收
     */
    private boolean mUseMerchantIncome = false;
    /**
     * 是否使用第三方支付
     */
    private boolean useThirdPay = false;
    /**
     * 1支付宝 2微信 3微信公共账号 4网银
     */
    private String mPayType = "";
    private OrderPayPresenterImpl orderPayPresenter;
    /**
     * 订单数量
     */
    private int orderCount;
    private String onlineBankHost = "";
    private PayDialog payDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_desk2);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("确认支付");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        llV3title = (LinearLayout) findViewById(R.id.ll_v3title);
        btnSurePay = (Button) findViewById(R.id.btn_surePay);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        tvBuyPrice = (TextView) findViewById(R.id.tv_buy_price);
        tvUsableMoney = (TextView) findViewById(R.id.tv_usable_money);
        tvCostIncome = (TextView) findViewById(R.id.tv_cost_income);
        btnWhetherUseIncome = (ImageView) findViewById(R.id.btn_whether_use_income);
        llCostIncome = (LinearLayout) findViewById(R.id.ll_cost_income);
        tvThirdPayTypeTitle = (TextView) findViewById(R.id.tv_third_pay_type_title);
        payTypeListView = (NoScrollListView) findViewById(R.id.lv_third_pay_type);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Intent intent = getIntent();
        payFrom = (PayFrom) intent.getSerializableExtra(PAY_FROM_TAG);
        switch (payFrom) {
            case GOODS_DETAIL:
                orderId = intent.getStringExtra("orderIndex");
                orderCount = 1;
                break;
            case GOODS_SHOPPING_CART:
                //从购物车过来
                ArrayList<MakeMallShoppingOrderEntity.MakeMallShopping> makeMallShoppings
                        = (ArrayList<MakeMallShoppingOrderEntity.MakeMallShopping>) intent.getSerializableExtra("list");
                StringBuilder stringBuilder = new StringBuilder();
                int shoppingCartOrderCount = makeMallShoppings.size();
                for (int i = 0; i < shoppingCartOrderCount; i++) {
                    MakeMallShoppingOrderEntity.MakeMallShopping makeMallShopping = makeMallShoppings.get(i);
                    if (i < shoppingCartOrderCount - 1) {
                        stringBuilder.append(makeMallShopping.orderIndex).append(",");
                    } else {
                        stringBuilder.append(makeMallShopping.orderIndex);
                    }
                }
                orderId = stringBuilder.toString();
                orderCount = shoppingCartOrderCount;
                break;
            case GOODS_ORDER:
                orderId = intent.getStringExtra("orderIndex");
                orderCount = 1;
                break;
            default:
                break;
        }
        orderTotalMoney = intent.getStringExtra("order_total_lebi");
        tvBuyPrice.setText(MoneyUtil.setSmall¥Money(MoneyUtil.getLeXiangBiNoZero(orderTotalMoney)));
        orderType = intent.getStringExtra("payType");
        if (userMoneyPresenter == null) {
            userMoneyPresenter = new UserMoneyPresenterImpl(this);
        }
        userMoneyPresenter.getUserMoney(mContext);
    }

    private void initListener() {
        payTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PayTypeEntity.PayData itemAtPosition = (PayTypeEntity.PayData) parent.getItemAtPosition(position);
                if (itemAtPosition != null) {
                    if (!itemAtPosition.isChecked) {
                        if (PayTypeEntity.PAY_TYPE_ISUSE.equals(itemAtPosition.isuse)) {
                            selectThirdPayMethod(position);
                        } else {
                            showToast("支付方式暂不可用");
                        }
                    }
                }

            }
        });
        RxUtil.clicks(btnWhetherUseIncome, 200L, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                选择是否使用营收款支付
                setIsMerchantView(!mUseMerchantIncome);
            }
        });
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showDialog(null, "便宜不等人,请君三思而后行~", null, 0, Gravity.NO_GRAVITY, "去意已决", "我再想想", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }, mContext);
            }
        });
        RxUtil.clicks(btnSurePay, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                立即支付
                switch (mPayType) {
                    case PayType.CASH_PAY:
                        //奖励支付时，先检测是否有绑定手机号(因为奖励支付需要支付密码，而支付密码的设置必须有手机号码)
                        if (TextUtils.isEmpty(PreferenceUtils.readStrConfig(Constants.SPKEY_PHONE,mContext))) {
                            showBindPhoneDialog();
                            return;
                        }
                        //没有支付密码，提示跳转设置支付密码界面
                        if (!PreferenceUtils.readBoolConfig(Constants.PAY_PASSWORD, mContext, false)) {
                            showSetPayPasswordDialog();
                            return;
                        }
                        if (payDialog == null) {
                            payDialog = new PayDialog(mContext, CashDeskActivity2.this);
                        }
                        payDialog.show();
                        break;
                    case PayType.ALI_PAY:
                        if (orderPayPresenter == null) {
                            orderPayPresenter = new OrderPayPresenterImpl(CashDeskActivity2.this);
                        }
                        orderPayPresenter.createThirdPayOrder(mContext, mPayType, MoneyUtil.getLeXiangBi(mPayThird), "0"
                                , PAY_FOR_GOODS, orderId, mPayMerchantIncome);
                        break;
                    case PayType.WE_CHAT_PAY:
                        break;
                    case PayType.ONLINE_BANK_PAY:
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String valueUrl = Ip.getBaseURL(mContext) + onlineBankHost
                                + "&token=" + RequestOftenKey.getToken(mContext)
                                + "&device_index=" + RequestOftenKey.getDeviceIndex(mContext)
                                + "&pay_type=" + mPayType
                                + "&type=1"//商品订单 1商家入驻缴费 2扫码支付 3线下交易 4现金充值 5 未定 6 套餐  7团购转配送
                                + "&payment_fee=" + mPayThird //此处payment_fee单位是分,不能带小数点
                                + "&moneys=" + mPayMerchantIncome
                                + "&order_index=" + orderId;
                        Logger.i("银联URL：" + valueUrl);
                        intent.putExtra("url", valueUrl);
                        intent.putExtra("isRecharge", true);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private void showBindPhoneDialog() {
        new VersionDialog.Builder(mContext)
                .setMessage("请绑定手机号码")
                .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(mContext, BindPhoneActivity.class));
                    }
                }).create().show();
    }

    private void showSetPayPasswordDialog() {
        new VersionDialog.Builder(mContext).setMessage("您还未设置支付密码,请设置支付密码后再支付！")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(CashDeskActivity2.this, InitialPayActivity.class));
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    /**
     * 选择第三方支付方式
     *
     * @param position
     */
    private void selectThirdPayMethod(int position) {
        for (int i = 0; i < payTypeList.size(); i++) {
            PayTypeEntity.PayData payTypeData = payTypeList.get(i);
            if (i == position) {
                payTypeData.isChecked = true;
                mPayType = payTypeData.payType;
                onlineBankHost = payTypeData.content;
            } else {
                payTypeData.isChecked = false;
            }
        }
        payTypeSelectorAdapter.notifyDataSetChanged();
    }

    private void setIsMerchantView(boolean useMerchantIncome) {
        //            是商家
        llCostIncome.setVisibility(View.VISIBLE);
        if (userMoneyIsEnough) {
//                奖励足够支付订单 则不能选择第三方支付和商家营收
            merchantUseMoneyOnly();
        } else {
//                奖励不足以支付订单 则可以选择商家营收或第三方补足差价
//                默认选择商家营收补足差价
            if (moneyAddIncomeIsEnough) {
//                    如果奖励+商家营收足以支付订单
                if (useMerchantIncome) {
//                    如果选择使用商家营收  则打开商家营收开关 并显示商家营收支付金额
                    merchantUseMoneyAndIncome();
                } else {
//                    如果选择不使用商家营收  则关闭商家营收开关 并显示第三方支付金额
                    merchantUseMoneyAndThird();
                }
            } else {
//                    如果奖励+商家营收不足以支付订单
//                    如果商家营收为0 则关闭商家营收开关且默认选择第一个第三方支付方式
                if (NumberFormat.convertToDouble(merchantIncome, DEFAULT_VALUE) == 0) {
                    merchantUseMoneyAndThird();
                } else {
//                    如果奖励+商家营收不足以支付订单
//                    如果商家营收不为0
                    if (useMerchantIncome) {
//                        如果选择使用商家营收 则打开商家营收开关且默认选择第一个第三方支付方式 并显示商家营收支付金额
                        merchantUseMoneyAndIncomeAndThird();
                    } else {
//                        如果不选择使用商家营收 则关闭商家营收开关且默认选择第一个第三方支付方式
                        merchantUseMoneyAndThird();
                    }
                }

            }
        }
    }

    /**
     * 支付身份是商家 只使用奖励支付
     */
    private void merchantUseMoneyOnly() {
        mPayType = PayType.CASH_PAY;
        setThreeMoneyCost(orderTotalMoney, PAY_ZERO, PAY_ZERO);
        btnWhetherUseIncome.setEnabled(false);
        payTypeListView.setEnabled(false);
        btnWhetherUseIncome.setImageResource(R.mipmap.library_module_cash_desk_off);
        mUseMerchantIncome = false;
        useThirdPay = false;
    }

    /**
     * 支付身份是商家 使用奖励和商家营收组合支付
     */
    private void merchantUseMoneyAndIncome() {
        mPayType = PayType.CASH_PAY;
        setThreeMoneyCost(userMoney,
                String.valueOf(new BigDecimal(orderTotalMoney).subtract(new BigDecimal(userMoney))), PAY_ZERO);
        selectThirdPayMethod(UN_SELECTION_THIRD_PAY);
        btnWhetherUseIncome.setEnabled(true);
        payTypeListView.setEnabled(false);
        btnWhetherUseIncome.setImageResource(R.mipmap.library_module_cash_desk_on);
        mUseMerchantIncome = true;
        useThirdPay = false;
    }

    /**
     * 支付身份是商家 使用奖励和第三方支付组合支付
     */
    private void merchantUseMoneyAndThird() {
        setThreeMoneyCost(userMoney, PAY_ZERO,
                String.valueOf(new BigDecimal(orderTotalMoney).subtract(new BigDecimal(userMoney))));
        if (NumberFormat.convertToDouble(merchantIncome, DEFAULT_VALUE) == 0) {
            btnWhetherUseIncome.setEnabled(false);
        } else {
            btnWhetherUseIncome.setEnabled(true);
        }
        selectThirdPayMethod(POSITION_ZERO);
        payTypeListView.setEnabled(true);
        btnWhetherUseIncome.setImageResource(R.mipmap.library_module_cash_desk_off);
        mUseMerchantIncome = false;
        useThirdPay = true;
    }

    /**
     * 支付身份是商家 使用奖励和商家营收和第三方支付 三种支付方式组合支付
     */
    private void merchantUseMoneyAndIncomeAndThird() {
        setThreeMoneyCost(userMoney, merchantIncome,
                String.valueOf(new BigDecimal(orderTotalMoney)
                        .subtract(new BigDecimal(userMoney))
                        .subtract(new BigDecimal(merchantIncome))));
        selectThirdPayMethod(POSITION_ZERO);
        btnWhetherUseIncome.setEnabled(true);
        payTypeListView.setEnabled(true);
        btnWhetherUseIncome.setImageResource(R.mipmap.library_module_cash_desk_on);
    }

    /**
     * 设置三种支付方式对应的金额
     *
     * @param payUserMoney      用户奖励支付金额
     * @param payMerchantIncome 商家营收款支付金额
     * @param payThird          第三方支付金额
     */
    void setThreeMoneyCost(String payUserMoney, String payMerchantIncome, String payThird) {
        mPayUserMoney = payUserMoney;
        mPayMerchantIncome = payMerchantIncome;
        mPayThird = payThird;
        tvUsableMoney.setText("奖励支付:" + MoneyUtil.add¥Front(MoneyUtil.getLeXiangBiNoZero(mPayUserMoney)));
        tvCostIncome.setText("营收额支付:" + MoneyUtil.add¥Front(MoneyUtil.getLeXiangBiNoZero(mPayMerchantIncome)));
        if (NumberFormat.convertToDouble(payThird, DEFAULT_VALUE) > 0) {
            btnSurePay.setText("确认支付(还需支付:" + MoneyUtil.add¥Front(MoneyUtil.getLeXiangBiNoZero(mPayThird)) + ")");
        } else {
            btnSurePay.setText("立即支付");
        }
    }

    @Override
    public void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2) {
        userMoney = myBalanceEntity2.lebi;
        merchantIncome = myBalanceEntity2.merchantIncome;
        if (NumberFormat.convertToDouble(userMoney, DEFAULT_VALUE) >= NumberFormat.convertToDouble(orderTotalMoney, DEFAULT_VALUE)) {
            userMoneyIsEnough = true;
        }
        if (NumberFormat.convertToDouble(merchantIncome, DEFAULT_VALUE) >= NumberFormat.convertToDouble(orderTotalMoney, DEFAULT_VALUE)) {
            merchantIncomeIsEnough = true;
        }
        if (NumberFormat.convertToDouble(MyBigDecimal.add(userMoney, merchantIncome), DEFAULT_VALUE) >= NumberFormat.convertToDouble(orderTotalMoney, DEFAULT_VALUE)) {
            moneyAddIncomeIsEnough = true;
        }
        if (myBalanceEntity2.isMerchant == IS_MERCHANT) {
            mIsMerchant = true;
        } else {
            mIsMerchant = false;
        }
        if (payTypePresenter == null) {
            payTypePresenter = new GetPayTypePresenterImpl(this);
        }
        payTypePresenter.getPayType(mContext);
    }

    @Override
    public void getPayTypeListSuccess(PayTypeEntity payTypeEntity) {
        payTypeList = payTypeEntity.data;
        setView();
    }

    void setView() {
        payTypeSelectorAdapter = new PayTypeSelectorAdapter(mContext, payTypeList);
        payTypeListView.setAdapter(payTypeSelectorAdapter);
        if (!mIsMerchant) {
            setNotMerchantView();
        } else {
            setIsMerchantView(true);
        }
    }

    private void setNotMerchantView() {
        //            不是商家
        llCostIncome.setVisibility(View.GONE);
        mUseMerchantIncome = false;
        btnWhetherUseIncome.setEnabled(false);
//            设置奖励支付金额
        if (userMoneyIsEnough) {
//                奖励足够支付订单 则不能选择商家营收和第三方支付
            mPayType = PayType.CASH_PAY;
            payTypeListView.setEnabled(false);
            setThreeMoneyCost(orderTotalMoney, PAY_ZERO, PAY_ZERO);
            useThirdPay = false;
        } else {
//                奖励不足以支付订单 则显示奖励支付金额 并默认选中第一条第三方支付方式
            payTypeListView.setEnabled(true);
            setThreeMoneyCost(userMoney, PAY_ZERO,
                    String.valueOf(new BigDecimal(orderTotalMoney).subtract(new BigDecimal(userMoney))));
            if (payTypeList != null && payTypeList.size() > 0) {
                selectThirdPayMethod(0);
                useThirdPay = true;
            } else {
                Logger.i("用户奖励不足以支付,且第三方支付方式获取失败");
                useThirdPay = false;
            }
        }
    }

    @Override
    public void passwordFinished(String psw) {
        if (orderPayPresenter == null) {
            orderPayPresenter = new OrderPayPresenterImpl(CashDeskActivity2.this);
        }
        orderPayPresenter.cashPay(mContext, orderId, psw, mPayMerchantIncome);
    }

    @Override
    public void cashPaySuccess(PayOkEntity payOkEntity) {
        if (orderPayPresenter == null) {
            orderPayPresenter = new OrderPayPresenterImpl(CashDeskActivity2.this);
        }
        orderPayPresenter.getPayResult(mContext, payOkEntity.orderId, GET_PAY_RESULT_TYPE);
    }

    @Override
    public void payFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void createGoodsPrepareThirdPayOrder(JsPayClass jsPayClass) {
        switch (mPayType) {
            case PayType.ALI_PAY:
                PullAliPayUtil.zhifubao(jsPayClass, mContext, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void getPayResultSuccess(GoodsChargeForPayResultEntity goodsChargeForPayResultEntity) {
        if (payDialog != null && payDialog.isShowing()) {
            payDialog.cancel();
        }
        jumpToNOrderPaySuccessActivity(goodsChargeForPayResultEntity.lebi,
                goodsChargeForPayResultEntity.dealTime, goodsChargeForPayResultEntity.returnBean,
                goodsChargeForPayResultEntity.subsidy,goodsChargeForPayResultEntity.daiGouQuanMoney);
    }

    /**
     * 支付成功后跳转到支付成功界面
     */
    private void jumpToNOrderPaySuccessActivity(String lebi, String dealTime, String returnBean, String subsidy,String daiGouQuan) {
        Intent intent = new Intent(mContext, NOrderPaySuccessActivity.class);
        intent.putExtra("deal_time", dealTime);
        intent.putExtra("lebi", lebi);
        intent.putExtra("returnBean", returnBean);
        intent.putExtra("subsidy", subsidy);
        intent.putExtra("order_quan",daiGouQuan);
        startActivity(intent);
        finish();
    }

    @Override
    public void pullAliPaySuccessListener() {
//        支付宝支付成功
        if (orderPayPresenter == null) {
            orderPayPresenter = new OrderPayPresenterImpl(CashDeskActivity2.this);
        }
        orderPayPresenter.getPayResult(mContext, orderId, GET_PAY_RESULT_TYPE);
    }
}
