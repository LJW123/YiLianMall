package com.yilian.mall.ctrip.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.presenter.CtripPayPresenterImpl;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.jd.presenter.JDOrderPayPresenter;
import com.yilian.mall.jd.presenter.impl.JDPayPresenterImpl;
import com.yilian.mall.ui.mvp.presenter.impl.UserMoneyPresenterImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IUserMoneyPresenter;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.ctrip.CtripCommitOrderEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripPayInfoEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import rx.Subscription;

import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.CASH_PAY;
import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.MIX_PAY;
import static com.yilian.mall.jd.presenter.JDOrderPayPresenter.THIRD_PAY;
import static com.yilian.mylibrary.Constants.CTRIP_PEYMENT_WARM_PROMPT;

/**
 * 携程 收银台
 *
 * @author Created by Zg on 2018/10/24.
 */

public class CtripOrderPaymentActivity extends BaseAppCompatActivity implements View.OnClickListener, JDOrderPayPresenter.View, IUserMoneyView {
    public static final String TAG = "CtripOrderPaymentActivity";
    /**
     * 最终的支付方式 默认为奖励支付
     */
    public int myPayType = CASH_PAY;
    private boolean isShowDetails = false;
    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvHotelName;
    private TextView tvRoomType;
    private TextView tvRoomIntroduce;
    private TextView tvCheckIn;
    private TextView tvCheckOut;
    private TextView tvDuration;
    private TextView tvNumber;
    private TextView tvRoomPrice;
    private TextView tvCheckInPerson;
    private TextView tvLinkman;
    private TextView tvPhone;
    private TextView tvOrderPrice;
    private TextView tvHasRate;
    private ImageView ivDetails;
    private LinearLayout llShowDetails;
    private LinearLayout llDetails;
    private TextView tvOrderCashPrice;
    private LinearLayout llMerchantIncomePay;
    private TextView tvOrderMerchantIncome;
    private ImageView ivIcon;
    private TextView tvClassName;
    private TextView tvClassSubTitle;
    private ImageView commitExpressIcon;
    private RelativeLayout rl;
    private TextView tvConfirm;
    private TextView tvDeductionsInstructions;
    private TextView tvWarmPrompt;
    private ImageView btnWhetherUseMerchantIncome;

    /**
     * 用户奖励是否足够支付商品
     */
    private boolean userMoneyIsEnough = false;
    /**
     * 用户拥有营收款金额 单位元
     */
    private float merchantIncomeMoney;
    /**
     * 除去用户奖励支付部分的订单价格 单位元
     */
    private float orderPriceWithoutUserCash;
    /**
     * 使用营收支付金额 单位 元
     */
    private String userMerchantIncomePayMoney = "0";
    /**
     * 用户奖励支付的那部分订单金额
     */
    private float orderPricePartOfUserCash;
    /**
     * 第三方支付需要的金额
     */
    private float thirdPayFee = 0;
    /**
     * 用户奖励余额
     */
    private float userMoney;
    private JDOrderPayPresenter jdPayCashPresenter;
    private CtripCommitOrderEntity ctripCommitOrderEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_order_payment);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
            }
        });

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);

        tvHotelName = (TextView) findViewById(R.id.tv_hotel_name);
        tvRoomType = (TextView) findViewById(R.id.tv_room_type);
        tvRoomIntroduce = (TextView) findViewById(R.id.tv_room_introduce);
        tvCheckIn = (TextView) findViewById(R.id.tv_check_in);
        tvCheckOut = (TextView) findViewById(R.id.tv_check_out);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvRoomPrice = (TextView) findViewById(R.id.tv_room_price);
        tvCheckInPerson = (TextView) findViewById(R.id.tv_check_in_person);
        tvLinkman = (TextView) findViewById(R.id.tv_linkman);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvOrderPrice = (TextView) findViewById(R.id.tv_order_price);
        tvHasRate = (TextView) findViewById(R.id.tv_has_rate);
        ivDetails = (ImageView) findViewById(R.id.iv_details);
        llShowDetails = (LinearLayout) findViewById(R.id.ll_show_details);
        llShowDetails.setVisibility(View.GONE);
        llDetails = (LinearLayout) findViewById(R.id.ll_details);
        tvOrderCashPrice = (TextView) findViewById(R.id.tv_order_cash_price);
        llMerchantIncomePay = (LinearLayout) findViewById(R.id.ll_merchant_income_pay);
        tvOrderMerchantIncome = (TextView) findViewById(R.id.tv_order_merchant_income);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvClassName = (TextView) findViewById(R.id.tv_class_name);
        tvClassSubTitle = (TextView) findViewById(R.id.tv_class_sub_title);
        btnWhetherUseMerchantIncome = (ImageView) findViewById(R.id.btn_whether_use_merchant_income);
        commitExpressIcon = (ImageView) findViewById(R.id.commit_express_icon);
        rl = (RelativeLayout) findViewById(R.id.rl);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvDeductionsInstructions = (TextView) findViewById(R.id.tv_deductions_instructions);
        tvWarmPrompt = (TextView) findViewById(R.id.tv_warm_prompt);

    }

    public void initData() {
        tvTitle.setText("在线支付");

        ctripCommitOrderEntity = (CtripCommitOrderEntity) getIntent().getSerializableExtra(TAG);

        getUserMoney();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llDetails.setOnClickListener(this);

        btnWhetherUseMerchantIncome.setOnClickListener(this);
        commitExpressIcon.setOnClickListener(this);

        tvConfirm.setOnClickListener(this);
        tvDeductionsInstructions.setOnClickListener(this);
        tvWarmPrompt.setOnClickListener(this);
    }

    private void getUserMoney() {
        IUserMoneyPresenter userMoneyPresenter = new UserMoneyPresenterImpl(this);
        Subscription subscription = userMoneyPresenter.getUserMoney(mContext);
        addSubscription(subscription);
    }

    @Override
    public void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2) {

        merchantIncomeMoney = NumberFormat.convertToFloat(myBalanceEntity2.merchantIncome, 0) / 100;
        userMoney = NumberFormat.convertToFloat(myBalanceEntity2.lebi, 0) / 100;

        //        需支付的订单总价
        float orderTotalPrice = NumberFormat.convertToFloat(ctripCommitOrderEntity.orderPrice, 0);
        tvOrderPrice.setText(String.format("%s元", orderTotalPrice));
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
        setData(ctripCommitOrderEntity);

        initPayType();
    }

    /**
     * 绑定数据
     *
     * @param entity
     */
    private void setData(CtripCommitOrderEntity entity) {
        tvOrderCashPrice.setText(String.format("¥%s", orderPricePartOfUserCash));

        tvHotelName.setText(entity.hotelName);
        tvRoomType.setText(entity.roomType);
        String breakfast = "无早餐";
        if (entity.breakfast == 1) {
            breakfast = "单份早餐";
        } else if (entity.breakfast == 2) {
            breakfast = "双份早餐";
        }
        tvRoomIntroduce.setText(String.format("%s %s %s", entity.bedName, entity.netMsg, breakfast));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日");
        try {
            tvCheckIn.setText(sdf1.format(sdf.parse(entity.checkIn)));
            tvCheckOut.setText(sdf1.format(sdf.parse(entity.checkOut)));
        } catch (ParseException e) {
            e.printStackTrace();

            tvCheckIn.setText(entity.checkIn);
            tvCheckOut.setText(entity.checkOut);
        }
        tvDuration.setText(entity.duration + "");
        tvNumber.setText(entity.number + "");
        tvRoomPrice.setText(entity.orderPrice);
        tvCheckInPerson.setText(entity.checkInPerson);
        tvLinkman.setText(entity.linkman);
        tvPhone.setText(entity.phone);

        tvOrderPrice.setText(entity.orderPrice);
        tvHasRate.setText(entity.returnBean);

        varyViewUtils.showDataView();
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

    /**
     * 设置支付方式为奖励+营收额支付
     */
    private void setPayTypeCash() {
        myPayType = CASH_PAY;
        userMoneyIsEnough = true;
        btnWhetherUseMerchantIncome.setImageResource(R.mipmap.icon_red_pay_switch);
        commitExpressIcon.setImageResource(R.mipmap.library_module_commit_express_off);
        setCahsPayView(String.valueOf(orderPriceWithoutUserCash));

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
    }

    /**
     * 设置支付方式为第三方支付
     *
     * @param orderPriceWithoutUserCash
     */
    private void setPayTypeThird(float orderPriceWithoutUserCash) {
        myPayType = THIRD_PAY;
        btnWhetherUseMerchantIncome.setImageResource(R.mipmap.ctrip_order_payment_switch_on);
        commitExpressIcon.setImageResource(R.mipmap.icon_red_check_pay_type);
        setCahsPayView("0.00");
        thirdPayFee = orderPriceWithoutUserCash;
    }

    void setCahsPayView(String price) {
        userMerchantIncomePayMoney = price;
        tvOrderMerchantIncome.setText("¥" + price);
    }

    private String getLabel(ArrayList<String> labels) {
        StringBuilder labelStr = new StringBuilder();
        for (int i = 0; i < labels.size(); i++) {
            labelStr.append(labels.get(i));
        }
        return labelStr.toString();
    }

    @Override
    public void onBackPressed() {
        showLeaveDialog();
    }

    void showLeaveDialog() {
        showSystemV7Dialog("确认离开支付界面？", "您的支付尚未完成，是否立即取消？", "取消支付", "继续支付", new DialogInterface.OnClickListener() {
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

    /**
     * 支付成功回调
     */
    @Override
    public void paySuccess(HttpResultBean httpResultBean) {
        //EventBus 推送
        /**
         * {@link com.yilian.mall.ctrip.fragment.order.CtripOrderCommonFragment}
         */
        EventBus.getDefault().post(new CtripOrderListUpdateModel(CtripOrderListUpdateModel.HandleType_pay, ctripCommitOrderEntity.ResID_Value));

        ((CtripPayPresenterImpl) jdPayCashPresenter).onDestory();
        JumpCtripActivityUtils.toCtripOrderPaySuccess(mContext, (CtripPayInfoEntity) httpResultBean);
        finish();


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                showLeaveDialog();
                break;
            case R.id.ll_details:
                //详情是否展开
                isShowDetails = !isShowDetails;
                if (isShowDetails) {
                    llShowDetails.setVisibility(View.VISIBLE);
                    ivDetails.setImageResource(R.mipmap.ctrip_order_payment_upward);
                } else {
                    llShowDetails.setVisibility(View.GONE);
                    ivDetails.setImageResource(R.mipmap.ctrip_order_payment_downward);
                }
                break;
            case R.id.btn_whether_use_merchant_income:
                //营收按钮
                changePayType();
                break;
            case R.id.commit_express_icon:
                //使用支付宝支付
                changePayType();
                break;
            case R.id.tv_confirm:
                //支付
                pay(myPayType);
                break;
            case R.id.tv_deductions_instructions:
                //扣款需知
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, String.format("%smobi/dist/Withhold?HotelID=%s&RoomID=%s&Start=%s&End=%s", Ip.BASE_PAY_URL,
                        ctripCommitOrderEntity.hotelId, ctripCommitOrderEntity.roomId, ctripCommitOrderEntity.checkIn, ctripCommitOrderEntity.checkOut), false);
                break;
            case R.id.tv_warm_prompt:
                //温馨提示
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, CTRIP_PEYMENT_WARM_PROMPT, false);
                break;
            default:
                break;
        }
    }

    private void pay(int payType) {
        if (jdPayCashPresenter == null) {
            jdPayCashPresenter = new CtripPayPresenterImpl(
                    mContext,
                    this,
                    ctripCommitOrderEntity.orderIndex,
                    String.valueOf(
                            new BigDecimal(userMerchantIncomePayMoney)
                                    .multiply(new BigDecimal("100"))));
        }
        jdPayCashPresenter.pay(payType, thirdPayFee);
    }
}
