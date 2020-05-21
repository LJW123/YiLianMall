package com.yilian.mall.shoppingcard.activity;

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
import com.yilian.mall.jd.activity.JDPaySuccessActivity;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.popwindow.CardWarnCommonPopwindow;
import com.yilian.mall.shoppingcard.presenter.CardJDOrderPayPresenter;
import com.yilian.mall.shoppingcard.presenter.impl.CardJDPayPresenterImpl;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.ui.mvp.presenter.impl.UserCardPresenterImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IUserCardPresenter;
import com.yilian.mall.ui.mvp.view.inter.IUserCardView;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.CheckShopCardRelEntity;
import com.yilian.networkingmodule.entity.MyCardEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.jdEventBusModel.JDOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.yilian.mall.shoppingcard.presenter.CardJDOrderPayPresenter.CARD_PAY;
import static com.yilian.mall.shoppingcard.presenter.CardJDOrderPayPresenter.MIX_PAY;
import static com.yilian.mall.shoppingcard.presenter.CardJDOrderPayPresenter.THIRD_PAY;


/**
 * @author Zg 购物卡 京东收银台 1018/11/19
 */
public class CardJdCashDeskActivity extends BaseAppCompatActivity implements View.OnClickListener, CardJDOrderPayPresenter.View, IUserCardView {

    public static final String TAG = "CardJdCashDeskActivity";
    /**
     * 支付方式 默认为购物卡支付
     */
    public int myPayType = CARD_PAY;
    private VaryViewUtils varyViewUtils;
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
    private ImageView ivIcon;
    private TextView tvClassName;
    private TextView tvClassSubTitle;

    private ImageView selectCardPay;
    private ImageView commitExpressIcon;

    private RelativeLayout rl;
    private Button btnPay;
    private JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity;
    private CardJDOrderPayPresenter jdPayCashPresenter;
    /**
     * 用户 购物卡 是否足够支付商品
     */
    private boolean userCardIsEnough = false;
    /**
     * 订单金额 单位元
     */
    private float orderTotalPrice;
    /**
     * 用户 购物卡余额 单位元
     */
    private float userCardBalance;

    /**
     * 购物卡需要支付的金额
     */
    private float payByCard = 0;
    /**
     * 第三方支付需要的金额
     */
    private float payByThird = 0;


    private String orderCountTime = "";
    private TextView tvHours;

    private CardWarnCommonPopwindow openCardPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_jdcash_desk);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setJdVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                checkShopCardRel();
            }
        });

        tvHours = findViewById(R.id.tv_hours);
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
        selectCardPay = (ImageView) findViewById(R.id.select_card_pay);
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
    }

    private void initListener() {
        RxUtil.clicks(selectCardPay, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (myPayType == CARD_PAY) {
                    setPayTypeThird();
                } else if (myPayType == THIRD_PAY) {
                    if (userCardIsEnough) {
                        setPayTypeCard();
                    } else {
                        setPayTypeMix();
                    }
                } else {
                    setPayTypeThird();
                }
            }
        });
        RxUtil.clicks(commitExpressIcon, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (myPayType == CARD_PAY) {
                    if (userCardIsEnough) {
                        setPayTypeThird();
                    } else {
                        setPayTypeMix();
                    }
                } else if (myPayType == THIRD_PAY) {
                    if (userCardIsEnough) {
                        setPayTypeCard();
                    } else {
                        showToast("购物卡余额不足");
                        setPayTypeMix();
                    }
                } else {
                    if (userCardIsEnough) {
                        setPayTypeCard();
                    } else {
                        showToast("购物卡余额不足");
                        setPayTypeMix();
                    }
                }
            }
        });
    }

    /**
     * 判断当前登陆用户是否关联购物卡
     */
    private void checkShopCardRel() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .checkShopCardRel("card/checkShopCardRel")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckShopCardRelEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(CheckShopCardRelEntity entity) {
                        //1未关联购物卡     2已关联购物卡
                        if (null != entity && entity.isOpenCard == 2) {
                            if (openCardPop != null && openCardPop.isShowing()) {
                                openCardPop.dismiss();
                            }
                            getUserCard();
                        } else {
                            openCardPop();
                        }
                    }
                });
        addSubscription(subscription);

    }

    /**
     * 设置支付方式为第三方支付
     */
    private void setPayTypeThird() {
        myPayType = THIRD_PAY;
        selectCardPay.setImageResource(R.mipmap.library_module_commit_express_off);
        commitExpressIcon.setImageResource(R.mipmap.icon_red_check_pay_type);
        payByCard = 0;
        payByThird = orderTotalPrice;
        btnPay.setText(String.format("支付宝支付%s元", orderTotalPrice));
    }

    /**
     * 设置支付方式为 购物卡支付
     */
    private void setPayTypeCard() {
        myPayType = CARD_PAY;
        selectCardPay.setImageResource(R.mipmap.icon_red_check_pay_type);
        commitExpressIcon.setImageResource(R.mipmap.library_module_commit_express_off);
        payByCard = orderTotalPrice;
        payByThird = 0;
        btnPay.setText(String.format("购物卡支付%s元", orderTotalPrice));
    }

    /**
     * 设置支付方式为混合支付
     */
    private void setPayTypeMix() {
        myPayType = MIX_PAY;
        selectCardPay.setImageResource(R.mipmap.icon_red_check_pay_type);
        commitExpressIcon.setImageResource(R.mipmap.icon_red_check_pay_type);
        payByCard = userCardBalance;
        payByThird = orderTotalPrice - userCardBalance;
        btnPay.setText(String.format("购物卡+支付宝支付%s元", orderTotalPrice));
    }

    private void getUserCard() {
        IUserCardPresenter userCardPresenter = new UserCardPresenterImpl(this);
        Subscription subscription = userCardPresenter.getUserCard(mContext);
        addSubscription(subscription);
    }

    private void openCardPop() {
        if (openCardPop == null) {
            openCardPop = new CardWarnCommonPopwindow(mContext);
            openCardPop.setCloseActivity();
            openCardPop.setTitle("温馨提示");
            openCardPop.setContent("您还未开通购物卡，快去开通吧~");
            openCardPop.setLeft("再想想", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            openCardPop.setRight("去开通", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpCardActivityUtils.toCardMyShoppingCardActivity(mContext,null);
                }
            });
        }
        if (!openCardPop.isShowing()) {
            openCardPop.showPop(btnPay);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (openCardPop != null) {
            openCardPop.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkShopCardRel();
    }

    @Override
    public void onBackPressed() {
        if (openCardPop != null && openCardPop.isShowing()) {
            finish();
        } else {
            showLeaveDialog();
        }
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

    /**
     * 支付成功
     *
     * @param jdPayInfoEntity
     */
    @Override
    public void paySuccess(HttpResultBean jdPayInfoEntity) {
        //EventBus 推送
        JDOrderListUpdateModel updateModel = new JDOrderListUpdateModel();
        updateModel.handleType = JDOrderListUpdateModel.HandleType_pay;
        updateModel.jdOrderId = jdCommitOrderSuccessEntity.orderId;
        /**
         * {@link  com.yilian.mall.jd.fragment.order.JDOrderCommonFragment#receiveEvent(JDOrderListUpdateModel)}
         */
        EventBus.getDefault().post(updateModel);

        ((CardJDPayPresenterImpl) jdPayCashPresenter).onDestory();
        JumpJdActivityUtils.toJDJDPaySuccessActivity(mContext, jdPayInfoEntity, JDPaySuccessActivity.PaySide.Card_JD);
        finish();
    }

    @Override
    public void getUserCardSuccess(MyCardEntity myCardEntity) {
        //需支付的订单总价
        orderTotalPrice = com.yilian.mylibrary.NumberFormat.convertToFloat(getOrderPrice(jdCommitOrderSuccessEntity.goodsPrice, jdCommitOrderSuccessEntity.freight), 0);
        //用户
        userCardBalance = NumberFormat.convertToFloat(myCardEntity.cardAmount, 0) / 100;

        tvJdOrderPrice.setText(String.format("%s元", orderTotalPrice));
        if (userCardBalance >= orderTotalPrice) {
            userCardIsEnough = true;
            setPayTypeCard();
        } else {
            userCardIsEnough = false;
            setPayTypeMix();
        }
        setData(jdCommitOrderSuccessEntity);

        varyViewUtils.showDataView();
    }

    @Override
    public void getUserCardError(String errorMsg) {
        varyViewUtils.showErrorView();
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
        if (userCardIsEnough) {
            btnPay.setText(String.format("购物卡支付%s元", orderTotalPrice));
        } else {
            btnPay.setText(String.format("购物卡+支付宝支付%s元", orderTotalPrice));
        }
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

    String supZero(String str) {
        return "0" + str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            jdPayCashPresenter = new CardJDPayPresenterImpl(mContext, this, jdCommitOrderSuccessEntity.orderIndex);
        }
        jdPayCashPresenter.pay(payType, payByCard, payByThird);
    }


}
