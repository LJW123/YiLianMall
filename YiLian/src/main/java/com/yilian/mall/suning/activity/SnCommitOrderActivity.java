package com.yilian.mall.suning.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.enums.JdCommitOrderNoStockGoodsCount;
import com.yilian.mall.suning.activity.goodsdetail.addressclean.SnDefaultShippingAddressContract;
import com.yilian.mall.suning.activity.goodsdetail.addressclean.SnDefaultShippingAddressPresenterImpl;
import com.yilian.mall.suning.activity.shippingaddress.SnShippingAddressListActivity;
import com.yilian.mall.suning.fragment.SnCommitOrderNoStockFragment;
import com.yilian.mall.suning.fragment.SnShoppingCarFragment;
import com.yilian.mall.suning.module.SuNingCommitOrderGoodsModule;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.ui.mvp.presenter.impl.UserMoneyPresenterImpl;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.mall.utils.DaiGouQuanConstant;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.suning.SnCheckGoodsStock;
import com.yilian.networkingmodule.entity.suning.SnCommitOrderEntity;
import com.yilian.networkingmodule.entity.suning.SnFreightEntity;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnShoppingCarEntity;
import com.yilian.networkingmodule.entity.suning.snEventBusModel.SnOrderListUpdateModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mall.suning.activity.shippingaddress.SnShippingAddressListActivity.TAG_CHOOSE_SHIPPING_ADDRESS;

public class SnCommitOrderActivity extends BaseAppCompatActivity
        implements View.OnClickListener,
        SnDefaultShippingAddressContract.IView,
        IUserMoneyView,
        SnCommitOrderNoStockFragment.NoStockListener {

    public static final String TAG = "SnCommitOrderActivityGoodsList";
    public static final String TAG_ADDRESS = "SnCommitOrderActivityShippingAddress";
    public static final String TAG_FROM_TYPE = "tag_from_type";
    public static final int ONLY_ONE_GOODS = 1;
    public static final int MAX_IMG_COUNT = 3;
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
    private TextView tvNoShippingAddress;
    private AppCompatImageView ivIcon;
    private AppCompatTextView tvNamePhone;
    private AppCompatTextView tvShippingAddress;
    private RelativeLayout rlShippingAddress;
    private AppCompatImageView ivOne;
    private AppCompatTextView tvOneGoodsName;
    private AppCompatTextView tvOneGoodsPrice;
    private AppCompatTextView tvOneGoodsCount;
    private RelativeLayout rlOneGoodsInfo;
    private LinearLayout llMultiGoodsImgs;
    private AppCompatImageView ivMoreGoods;
    private TextView tvMultiGoodsCount;
    private AppCompatTextView tvOrderGoodsPrice;
    private TextView tvOrderFreight;
    private AppCompatTextView tvOrderReturnBean;
    private AppCompatTextView tvOrderTotalPrice;
    private Button btnSuNingCommitOrder;
    private ArrayList<SuNingCommitOrderGoodsModule> goodsDatas;
    private LinearLayout llMultiGoodsInfo;
    private SnShippingAddressInfoEntity.DataBean mShippingAddress;
    /**
     * 订单商品总数量
     */
    private int orderGoodsCount = 0;
    /**
     * 订单商品总价
     */
    private String orderGoodsPrice = "0";
    /**
     * 订单运费
     */
    private String orderFreight = "0";
    /**
     * 订单赠送益豆数量
     */
    private String orderReturnBean = "0";
    /**
     * 订单总价(商品价+运费)
     */
    private String orderTotalPrice = "0";
    private SnDefaultShippingAddressPresenterImpl snDefaultShippingAddressPresenter;
    /**
     * 购物车id
     */
    private String shoppingCartIndexList = "";
    /**
     * 订单商品sku信息 多商品以逗号分隔
     */
    private String skuIds = "";
    /**
     * 订单商品sku信息和数量信息  格式sku:count,sku:count
     */
    private String skuIdAndCount = "";
    /**
     * 从商品详情提交订单的商品数量
     */
    private int fromGoodsDetailCount = 0;
    private CheckBox checkBoxUserDaiGouQuan;
    private TextView tvDaiGouQuanMoney;
    private UserMoneyPresenterImpl userMoneyPresenter;
    //    用户代购券数量 单位 元
    private String mDiGouQuan;
    private LinearLayout llReturnBean;
    /**
     * 用户最终可使用的代购券数量
     */
    private String mUsableDaiGouQuan = "0";
    /**
     * 用户最终使用的代购券数量
     */
    private String mUsedDaiGouQuanMoney = "0";
    private FromeType fromeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sn_commit_order);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        llReturnBean = findViewById(R.id.ll_return_bean);
        checkBoxUserDaiGouQuan = findViewById(R.id.check_box_user_dai_gou);
        tvDaiGouQuanMoney = findViewById(R.id.tv_money_dai_gou);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("确认订单");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvNoShippingAddress = (TextView) findViewById(R.id.tv_no_shipping_address);
        ivIcon = (AppCompatImageView) findViewById(R.id.iv_icon);
        tvNamePhone = (AppCompatTextView) findViewById(R.id.tv_name_phone);
        tvShippingAddress = (AppCompatTextView) findViewById(R.id.tv_shipping_address);
        rlShippingAddress = (RelativeLayout) findViewById(R.id.rl_shipping_address);
        ivOne = (AppCompatImageView) findViewById(R.id.iv_one);
        tvOneGoodsName = (AppCompatTextView) findViewById(R.id.tv_one_goods_name);
        tvOneGoodsPrice = (AppCompatTextView) findViewById(R.id.tv_one_goods_price);
        tvOneGoodsCount = (AppCompatTextView) findViewById(R.id.tv_one_goods_count);
        rlOneGoodsInfo = (RelativeLayout) findViewById(R.id.rl_one_goods_info);
        llMultiGoodsImgs = (LinearLayout) findViewById(R.id.ll_multi_goods_imgs);
        ivMoreGoods = (AppCompatImageView) findViewById(R.id.iv_more_goods);
        tvMultiGoodsCount = (TextView) findViewById(R.id.tv_multi_goods_count);
        tvOrderGoodsPrice = (AppCompatTextView) findViewById(R.id.tv_order_goods_price);
        tvOrderFreight = (TextView) findViewById(R.id.tv_order_freight);
        tvOrderReturnBean = (AppCompatTextView) findViewById(R.id.tv_order_gave_bean);
        tvOrderTotalPrice = (AppCompatTextView) findViewById(R.id.tv_order_total_price);
        btnSuNingCommitOrder = (Button) findViewById(R.id.btn_su_ning_commit_order);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnSuNingCommitOrder.setOnClickListener(this);
        llMultiGoodsInfo = (LinearLayout) findViewById(R.id.ll_multi_goods_info);
    }
    @Subscribe
    public void shippingAddressChanged(SnShippingAddressInfoEntity.DataBean shippingAddressInfo) {
        if (mShippingAddress.id.equals(shippingAddressInfo.id)) {
            mShippingAddress = null;
            setAddressInfoView();
        }
    }
    @SuppressWarnings("unchecked")
    private void initData() {
        fromeType = (FromeType) getIntent().getSerializableExtra(TAG_FROM_TYPE);
        goodsDatas = (ArrayList<SuNingCommitOrderGoodsModule>) getIntent().getSerializableExtra(TAG);
        if (goodsDatas != null) {
//            有商品信息
            if (goodsDatas.size() == ONLY_ONE_GOODS) {
//                只有一种商品
                rlOneGoodsInfo.setVisibility(View.VISIBLE);
                llMultiGoodsInfo.setVisibility(View.GONE);
                SuNingCommitOrderGoodsModule suNingCommitOrderGoodsModule = goodsDatas.get(0);
                setGoodsData(suNingCommitOrderGoodsModule);
                setOrderData(suNingCommitOrderGoodsModule);
            } else if (goodsDatas.size() > ONLY_ONE_GOODS) {
//                有多种商品
                rlOneGoodsInfo.setVisibility(View.GONE);
                llMultiGoodsInfo.setVisibility(View.VISIBLE);
                setGoodsData(goodsDatas);
                setOrderData(goodsDatas);
            }
            showOrderData();
        }
        mShippingAddress = (SnShippingAddressInfoEntity.DataBean) getIntent().getSerializableExtra(TAG_ADDRESS);
        if (mShippingAddress != null) {
            setAddressInfoView();
        } else {
            if (snDefaultShippingAddressPresenter == null) {
                snDefaultShippingAddressPresenter = new SnDefaultShippingAddressPresenterImpl(this);
            }
            snDefaultShippingAddressPresenter.getSnDefaultShippingAddressInfo(mContext);
        }

        getUserMoney();
    }

    private void initListener() {
        RxUtil.clicks(rlOneGoodsInfo, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                跳转商品清单
                JumpSnActivityUtils.toSnCommitOrderMultiGoodsList(mContext, goodsDatas, orderGoodsCount);
            }
        });
        RxUtil.clicks(llMultiGoodsInfo, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                跳转商品清单
                JumpSnActivityUtils.toSnCommitOrderMultiGoodsList(mContext, goodsDatas, orderGoodsCount);
            }
        });
        checkBoxUserDaiGouQuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llReturnBean.setVisibility(View.GONE);
                    mUsedDaiGouQuanMoney = mUsableDaiGouQuan;
                } else {
                    llReturnBean.setVisibility(View.VISIBLE);
                    mUsedDaiGouQuanMoney = "0";
                }
                showOrderData();
            }
        });
        Consumer chooseShippingAddressConsumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (mShippingAddress != null && mShippingAddress.id != null) {
                    JumpSnActivityUtils.toShippingAddressListActivitiy(SnCommitOrderActivity.this, mShippingAddress.id);
                } else {
                    JumpSnActivityUtils.toShippingAddressListActivitiy(SnCommitOrderActivity.this);
                }
            }
        };
        RxUtil.clicks(rlShippingAddress, chooseShippingAddressConsumer);
        RxUtil.clicks(tvNoShippingAddress, chooseShippingAddressConsumer);
    }

    /**
     * 设置单商品信息
     *
     * @param suNingCommitOrderGoodsModule 单商品信息
     */
    private void setGoodsData(SuNingCommitOrderGoodsModule suNingCommitOrderGoodsModule) {
        GlideUtil.showImage(mContext, suNingCommitOrderGoodsModule.imgUrl, ivOne);
        tvOneGoodsName.setText(suNingCommitOrderGoodsModule.goodsName);
        tvOneGoodsPrice.setText(String.format("¥%s/件", suNingCommitOrderGoodsModule.price));
        orderGoodsCount = suNingCommitOrderGoodsModule.count;
        fromGoodsDetailCount = orderGoodsCount;
        tvOneGoodsCount.setText(String.format(Locale.getDefault(), "×%d", orderGoodsCount));
        skuIds = suNingCommitOrderGoodsModule.skuId;
        skuIdAndCount = suNingCommitOrderGoodsModule.skuId + ":1";
        shoppingCartIndexList = suNingCommitOrderGoodsModule.cartIndex;
    }

    /**
     * 设置单商品订单价格信息
     *
     * @param suNingCommitOrderGoodsModule 单商品信息
     */
    private void setOrderData(SuNingCommitOrderGoodsModule suNingCommitOrderGoodsModule) {
        orderGoodsPrice = String.valueOf(new BigDecimal(suNingCommitOrderGoodsModule.price)
                .multiply(new BigDecimal(String.valueOf(suNingCommitOrderGoodsModule.count))));
        orderReturnBean = String.valueOf(new BigDecimal(suNingCommitOrderGoodsModule.returnBean)
                .multiply(new BigDecimal(String.valueOf(suNingCommitOrderGoodsModule.count))));
        orderTotalPrice = String.valueOf(new BigDecimal(orderGoodsPrice).add(new BigDecimal(orderFreight)));
    }

    /**
     * 设置多商品信息
     *
     * @param goodsDatas 多商品信息
     */
    @SuppressLint("DefaultLocale")
    private void setGoodsData(ArrayList<SuNingCommitOrderGoodsModule> goodsDatas) {
        StringBuilder skuIdsBuilder = new StringBuilder();
        StringBuilder skuIdsAndCountBuilder = new StringBuilder();
        llMultiGoodsImgs.removeAllViews();
        for (int i = 0; i < goodsDatas.size(); i++) {
            SuNingCommitOrderGoodsModule suNingCommitOrderGoodsModule = goodsDatas.get(i);
            if (i <= MAX_IMG_COUNT - 1) {
                View inflate = View.inflate(mContext, R.layout.item_sn_commit_order_goods, null);
                ImageView ivGoods = inflate.findViewById(R.id.iv_goods);
                GlideUtil.showImage(mContext, suNingCommitOrderGoodsModule.imgUrl, ivGoods);
                if (suNingCommitOrderGoodsModule.count > 1) {
                    TextView tvGoodsCount = inflate.findViewById(R.id.tv_goods_count);
                    tvGoodsCount.setVisibility(View.VISIBLE);
                    tvGoodsCount.setText(String.format("%s件", suNingCommitOrderGoodsModule.count));
                }
                llMultiGoodsImgs.addView(inflate);
            }
            orderGoodsCount += suNingCommitOrderGoodsModule.count;
            if (goodsDatas.size() - 1 > i) {
                skuIdsBuilder.append(suNingCommitOrderGoodsModule.skuId).append(",");
                skuIdsAndCountBuilder.append(suNingCommitOrderGoodsModule.skuId).append(":").append(suNingCommitOrderGoodsModule.count).append(",");
            } else {
                skuIdsBuilder.append(suNingCommitOrderGoodsModule.skuId);
                skuIdsAndCountBuilder.append(suNingCommitOrderGoodsModule.skuId).append(":").append(suNingCommitOrderGoodsModule.count);
            }
        }
        if (orderGoodsCount > MAX_IMG_COUNT) {
            ivMoreGoods.setVisibility(View.VISIBLE);
        } else {
            ivMoreGoods.setVisibility(View.INVISIBLE);

        }
        skuIds = skuIdsBuilder.toString();
        skuIdAndCount = skuIdsAndCountBuilder.toString();
        tvMultiGoodsCount.setText(String.format("共%d件", orderGoodsCount));
    }

    /**
     * 设置多商品订单价格信息
     *
     * @param goodsDatas 多商品信息
     */
    private void setOrderData(ArrayList<SuNingCommitOrderGoodsModule> goodsDatas) {
        BigDecimal goodsPriceMultiply = new BigDecimal("0");
        BigDecimal goodsReturnBeanMultiply = new BigDecimal("0");
        StringBuilder cartIndexStringBuilder = new StringBuilder();
        for (int i = 0; i < goodsDatas.size(); i++) {
            SuNingCommitOrderGoodsModule suNingCommitOrderGoodsModule = goodsDatas.get(i);
//            计算商品价格
            goodsPriceMultiply = goodsPriceMultiply.add(
                    new BigDecimal(suNingCommitOrderGoodsModule.price)
                            .multiply(new BigDecimal(String.valueOf(suNingCommitOrderGoodsModule.count))));
//            计算赠送益豆数量
            goodsReturnBeanMultiply = goodsReturnBeanMultiply.add(
                    new BigDecimal(suNingCommitOrderGoodsModule.returnBean)
                            .multiply(new BigDecimal(String.valueOf(suNingCommitOrderGoodsModule.count))));
            if (goodsDatas.size() - 1 > i) {
                cartIndexStringBuilder.append(suNingCommitOrderGoodsModule.cartIndex).append(",");
            } else {
                cartIndexStringBuilder.append(suNingCommitOrderGoodsModule.cartIndex);
            }

        }
        shoppingCartIndexList = cartIndexStringBuilder.toString();
        orderGoodsPrice = String.valueOf(goodsPriceMultiply);
        orderReturnBean = String.valueOf(goodsReturnBeanMultiply);
        orderTotalPrice = String.valueOf(new BigDecimal(orderGoodsPrice).add(new BigDecimal(orderFreight)));
    }

    /**
     * 获取用户代购券数量
     */
    private void getUserMoney() {
        checkBoxUserDaiGouQuan.setEnabled(false);
        if (userMoneyPresenter == null) {
            userMoneyPresenter = new UserMoneyPresenterImpl(this);
        }
        Subscription subscription = userMoneyPresenter.getUserMoney(mContext);
        addSubscription(subscription);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SnShippingAddressListActivity.REQUEST_CODE && resultCode == SnShippingAddressListActivity.RESULT_CODE) {
            if (data != null) {
                mShippingAddress = (SnShippingAddressInfoEntity.DataBean) data.getSerializableExtra(TAG_CHOOSE_SHIPPING_ADDRESS);
                setAddressInfoView();
            }
        }
    }

    private void setAddressInfoView() {
        if (mShippingAddress != null) {
//            有地址
            rlShippingAddress.setVisibility(View.VISIBLE);
            tvNoShippingAddress.setVisibility(View.GONE);
            tvNamePhone.setText(String.format("收货人: %s  %s", mShippingAddress.name, PhoneUtil.formatPhoneMiddle4Asterisk(mShippingAddress.mobile)));
            tvShippingAddress.setText(mShippingAddress.fullAddress);
            getFreight();
        } else {
//            无地址
            rlShippingAddress.setVisibility(View.GONE);
            tvNoShippingAddress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取运费
     */
    @SuppressWarnings("unchecked")
    private void getFreight() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnFreight("suning_goods/suning_goods_freight", skuIdAndCount,
                        mShippingAddress.address, mShippingAddress.cityId, mShippingAddress.countyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnFreightEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SnFreightEntity snFreightEntity) {
                        orderFreight = snFreightEntity.freight;
                        orderTotalPrice = String.valueOf(new BigDecimal(orderGoodsPrice).add(new BigDecimal(orderFreight)));
                        showOrderData();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 显示订单信息
     */
    private void showOrderData() {
        tvOrderGoodsPrice.setText(String.format("¥%s", orderGoodsPrice));
        tvOrderFreight.setText(String.format("+¥%s", orderFreight));
        tvOrderReturnBean.setText(String.format("+%s", orderReturnBean));
        tvOrderTotalPrice.setText(orderFinalPay(orderTotalPrice, mUsedDaiGouQuanMoney));
    }

    /**
     * 订单价格减去使用的代购券(也就是最终支付金额)
     *
     * @param orderTotalPrice
     * @param usedDaiGouQuanMoney
     * @return
     */
    private String orderFinalPay(String orderTotalPrice, String usedDaiGouQuanMoney) {
        return String.valueOf(
                Html.fromHtml(
                        "实付金额:<font color= '#FF5000'> ¥"
                                + new BigDecimal(orderTotalPrice)
                                .subtract(new BigDecimal(usedDaiGouQuanMoney))
                                + "</font>"));
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
            case R.id.btn_su_ning_commit_order:
                checkAddressInfo();

                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void checkAddressInfo() {
//        检查地址信息
        if (mShippingAddress == null || mShippingAddress.id == null) {
            showToast("请选择地址");
            return;
        }
        checkGoodsStock();
    }

    @SuppressWarnings("unchecked")
    private void checkGoodsStock() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .checkGoodsStock("suning_goods/suning_goods_inventory", skuIds, mShippingAddress.cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnCheckGoodsStock>() {
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
                    public void onNext(SnCheckGoodsStock snCheckGoodsStock) {
                        ArrayList<SuNingCommitOrderGoodsModule> noStockModule = new ArrayList<>();
                        for (SnCheckGoodsStock.ResultBean resultBean : snCheckGoodsStock.result) {
                            if (SnCheckGoodsStock.HAS_STOCK != resultBean.state) {
                                for (SuNingCommitOrderGoodsModule goodsData : goodsDatas) {
                                    if (resultBean.skuId.equals(goodsData.skuId)) {
                                        noStockModule.add(goodsData);
                                    }
                                }
                            }
                        }
                        if (noStockModule.size() > 0) {
                            if (noStockModule.size() >= goodsDatas.size()) {
//                           订单中所有商品，在所选城市暂时无货
                                SnCommitOrderNoStockFragment.getInstance(noStockModule,
                                        JdCommitOrderNoStockGoodsCount.ALL_NO_STOCK).show(getSupportFragmentManager(),
                                        SnCommitOrderNoStockFragment.TAG);
                            } else {
//                           订单中部分商品，在所选城市暂时无货
                                SnCommitOrderNoStockFragment.getInstance(noStockModule,
                                        JdCommitOrderNoStockGoodsCount.PARTLY_NO_STOCK).show(getSupportFragmentManager(),
                                        SnCommitOrderNoStockFragment.TAG);
                            }
                        } else {
                            commitOrderFinally();
                        }
                    }
                });
        addSubscription(subscription);
    }

    @SuppressWarnings("unchecked")
    private void commitOrderFinally() {
        String commitGoodsCount = String.valueOf(fromGoodsDetailCount);
//        提交订单前矫正提交参数
        switch (fromeType) {
            case SN_GOODS_DETAIL:
                shoppingCartIndexList = "";
                break;
            case SN_SHOPPING_CART:
                skuIds = "";
                commitGoodsCount = "";
                break;
            default:
                break;
        }
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .snCommitOrder("suning_orders/suning_order_action", mShippingAddress.id,
                        shoppingCartIndexList, mUsedDaiGouQuanMoney, skuIds,
                        commitGoodsCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnCommitOrderEntity>() {
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
                    public void onNext(SnCommitOrderEntity snCommitOrderEntity) {
                        JumpSnActivityUtils.toSnCashDeskActivity(mContext, snCommitOrderEntity);
                        /**
                         * {@link SnShoppingCarFragment#updateShoppingCarData(com.yilian.networkingmodule.entity.suning.SnShoppingCarEntity)}
                         */
                        EventBus.getDefault().post(new SnOrderListUpdateModel(SnOrderListUpdateModel.HandleType_add, snCommitOrderEntity.snOrderId));
                        EventBus.getDefault().post(new SnShoppingCarEntity());
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getSnDefaultShippingAddressInfoSuccess(SnShippingAddressInfoEntity snShippingAddressInfoEntity) {
        List<SnShippingAddressInfoEntity.DataBean> data = snShippingAddressInfoEntity.data;
        if (data != null && data.size() > 0) {
            mShippingAddress = data.get(0);
        } else {
            mShippingAddress = null;
        }
        setAddressInfoView();
    }

    @Override
    public void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2) {
        // DaiGouQuan: 2018/8/7 代购券业务暂时隐藏
        myBalanceEntity2.diGouQuan = "0";
        mDiGouQuan = String.valueOf(
                new BigDecimal(myBalanceEntity2.diGouQuan)
                        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN));
        if (NumberFormat.convertToFloat(mDiGouQuan, 0f) > 0) {
            checkBoxUserDaiGouQuan.setEnabled(true);
        } else {
            checkBoxUserDaiGouQuan.setEnabled(false);
        }
        refreshDaiGouQuanView();
    }

    private void refreshDaiGouQuanView() {
//        订单可使用代购券数量
        BigDecimal orderUseableDaiGouQuan = null;
        if (orderTotalPrice == null || mDiGouQuan == null) {
            return;
        }
        orderUseableDaiGouQuan
                = new BigDecimal(orderGoodsPrice)
                .divide(new BigDecimal(DaiGouQuanConstant.Rate), 2, BigDecimal.ROUND_HALF_DOWN);
        if (new BigDecimal(mDiGouQuan).compareTo(orderUseableDaiGouQuan) > 0) {
//            代购券充足
            mUsableDaiGouQuan = String.valueOf(orderUseableDaiGouQuan);
        } else {
//            代购券不足
            mUsableDaiGouQuan = mDiGouQuan;
        }
        tvDaiGouQuanMoney.setText(String.format("¥%s", mUsableDaiGouQuan));
    }

    @Override
    public void backToShoppingCart() {
        finish();
    }

    @Override
    public void removeNoStockGoodsAndCommitOrder(ArrayList<SuNingCommitOrderGoodsModule> noStockDatas) {
        StringBuilder removedNoStockGoodsCartIndexList = new StringBuilder();
        goodsDatas.removeAll(noStockDatas);
        commitOrderFinally();
    }

    /**
     * 数据来源
     */
    public enum FromeType {
        /**
         * 商品详情
         */
        SN_GOODS_DETAIL,
        /**
         * 购物车
         */
        SN_SHOPPING_CART
    }
}
