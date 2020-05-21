package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.OrderSubmitOrderListAdapter;
import com.yilian.mall.entity.MakeMallShoppingOrderEntity;
import com.yilian.mall.entity.MakeMallorderEntity;
import com.yilian.mall.entity.OrderSubmitGoods;
import com.yilian.mall.entity.UserDefaultAddressEntity;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.ui.fragment.HomePageFragment;
import com.yilian.mall.ui.mvp.presenter.impl.UserMoneyPresenterImpl;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.mall.utils.DaiGouQuanConstant;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.mylibrary.pay.PayFrom;
import com.yilian.networkingmodule.entity.MallGoodsExpressEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.ShoppingCartListEntity2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mall.R.id.tv_express_price;
import static com.yilian.mall.ui.CashDeskActivity2.PAY_FROM_TAG;
import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS;
import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS_2;


/**
 * 提交订单
 */
public class JPCommitOrderActivity extends BaseActivity implements View.OnClickListener, IUserMoneyView {

    public String actSku, actAddressId, actOrderId;
    boolean addressFlag = true;
    String orderRemark = " ";//提交订单时，订单备注为空字符串
    //从商品详情过来时的商品类型  1yhs 2jfg
    String typeFromDetail;
    //从购物车过来的商品类型
    String typeFromShopCar;


    private ImageView ivLeft1;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private TextView tvRight;
    private TextView tvReceiverNamePhone;
    private TextView tvReceiverAddress;
    private LinearLayout llSelectAddress;
    private TextView tvGoodsCount;
    private TextView tvTotalPrice;
    private TextView tvExpressPrice;
    private NoScrollListView nslvOuter1;
    private TextView tvLeXiang;
    private Button btnPay;
    private PayFrom orderType;
    private NoScrollListView nslvOuter2;
    private LinearLayout llOrderType1;
    private TextView tvShopName1;
    private TextView tvGoodsCount1;
    private TextView tvGoodsCountPrice1;
    private ImageView ivGoodsPhoto1;
    private TextView tvGoodName1;
    private TextView tvGoodRecommend1;
    private TextView tvGoodPriceLeXiangBi;
    private TextView tvGoodsNum;
    private OrderSubmitGoods orderSubmitGoods1;//从商品详情进入提交订单界面时，获取的商品实体类
    private ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> shoppingCartList;
    private double buyPrice;//购物车跳转 乐享币总价
    private double returnLedou;//购物车跳转 区块益豆总和
    private double gavePower;//购物车跳转 平台额外赠送区块益豆总和
    private StringBuffer cartId = new StringBuffer();//购物车跳转 所有订单cartId的拼接
    private int goodsCount;//购物车跳转 商品总数
    private String expressId;
    private double expressPrice;
    private String addressId;
    private MallNetRequest mallNetRequest;
    private UserAddressLists userAddressLists;
    private RelativeLayout llFinalMoney;
    private ArrayList<MakeMallShoppingOrderEntity.MakeMallShopping> makeMallShoppingList;
    private boolean expressIsSucess = false;
    private TextView tvSplitPackage;
    private RelativeLayout rlIvLeft1;
    private Context mContext;
    private EditText et;
    private OrderSubmitOrderListAdapter orderSubmitOrderListAdapter;
    private String remarkText = "";//所有订单备注拼接到一起的字符串，格式是服务器要求的接收格式
    private String type;//获取运费的type，限时抢购的时候传1，不是的话不传
    private TextView tvBackLedou;
    private TextView tvTotalBackLedou;
    //yhs jfg
    private View llDaiGouQuan;
    private AppCompatCheckBox checkDaiGouQuan;
    private AppCompatTextView tvMoneyDaiGou;
    private UserMoneyPresenterImpl userMoneyPresenter;
    /**
     * 用户使用的代购券数量 单位 分
     */
    private String usedDaiGouQuanMoney = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpcommit_order);
        mContext = this;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        llDaiGouQuan = findViewById(R.id.ll_dai_gou_quan);
        checkDaiGouQuan = findViewById(R.id.check_box_user_dai_gou);
        checkDaiGouQuan.setEnabled(false);
        tvMoneyDaiGou = findViewById(R.id.tv_money_dai_gou);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        tvSplitPackage = (TextView) findViewById(R.id.tv_split_package);
        llFinalMoney = (RelativeLayout) findViewById(R.id.ll_final_money);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        ivLeft1.setImageDrawable(getResources().getDrawable(R.mipmap.v3back));
        ivLeft1.setOnClickListener(this);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        ivTitle.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("确认提交订单");
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight1.setVisibility(View.GONE);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        ivRight2.setVisibility(View.GONE);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.GONE);
        tvReceiverNamePhone = (TextView) findViewById(R.id.tv_receiver_name_phone);
        tvReceiverAddress = (TextView) findViewById(R.id.tv_receiver_address);
        llSelectAddress = (LinearLayout) findViewById(R.id.ll_select_address);
        tvGoodsCount = (TextView) findViewById(R.id.tv_goods_count);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvExpressPrice = (TextView) findViewById(tv_express_price);

        nslvOuter2 = (NoScrollListView) findViewById(R.id.nslv_outer2);
        tvLeXiang = (TextView) findViewById(R.id.tv_lexiang);
        tvTotalBackLedou = (TextView) findViewById(R.id.tv_total_back_ledou);
        btnPay = (Button) findViewById(R.id.btn_pay);
//        从商品详情来到提交订单，只有一件商品时的 填充商品内容控件
        tvBackLedou = (TextView) findViewById(R.id.tv_back_ledou);
        llOrderType1 = (LinearLayout) findViewById(R.id.ll_order_type1);
        tvShopName1 = (TextView) findViewById(R.id.tv_shop_name1);
        nslvOuter1 = (NoScrollListView) findViewById(R.id.nslv_outer1);
        tvGoodsCount1 = (TextView) findViewById(R.id.tv_goods_count_1);
        tvGoodsCountPrice1 = (TextView) findViewById(R.id.tv_goods_count_price_1);
        ivGoodsPhoto1 = (ImageView) findViewById(R.id.iv_goods_photo_1);
        tvGoodName1 = (TextView) findViewById(R.id.tv_good_name_1);
        tvGoodRecommend1 = (TextView) findViewById(R.id.tv_good_recommend_1);
        tvGoodPriceLeXiangBi = (TextView) findViewById(R.id.tv_good_price_lexiangbi);
        tvGoodsNum = (TextView) findViewById(R.id.tv_goods_num);
        btnPay.setOnClickListener(this);
        et = (EditText) findViewById(R.id.et);
    }

    private void initData() {
        Intent intent = getIntent();
        orderType = (PayFrom) intent.getSerializableExtra("OrderType");
        switch (orderType) {
            case GOODS_DETAIL:
                btnPay.setText("提交订单");
                nslvOuter1.setVisibility(View.VISIBLE);
                nslvOuter1.setFocusable(false);
                nslvOuter1.setFocusableInTouchMode(false);
                nslvOuter2.setVisibility(View.GONE);
                tvSplitPackage.setVisibility(View.GONE);
                ArrayList<OrderSubmitGoods> orderSubmitGoods = (ArrayList<OrderSubmitGoods>) intent.getSerializableExtra("orderSubmitGoods");
                Logger.i("提交订单商品详情：" + orderSubmitGoods.toString());
                if (orderSubmitGoods.size() > 0) {
                    orderSubmitGoods1 = orderSubmitGoods.get(0);
                    //判断是否是全球购商品
                    tvGoodsCount.setText("共计" + orderSubmitGoods1.goodsCount + "件商品");
                    tvShopName1.setText(orderSubmitGoods1.name);//店名
                    String imageUrl = orderSubmitGoods1.goodsPic;//商品图片URL
                    GlideUtil.showImageNoSuffix(mContext, imageUrl, ivGoodsPhoto1);
                    tvGoodName1.setText(orderSubmitGoods1.goodsName);//商品名称
                    tvGoodRecommend1.setText(TextUtils.isEmpty(orderSubmitGoods1.goodsProperty) ? "" : orderSubmitGoods1.goodsProperty);//商品属性
                    tvGoodsNum.setText("×" + orderSubmitGoods1.goodsCount);
                    tvGoodsCount1.setText("小计" + orderSubmitGoods1.goodsCount + "件商品");
                    tvTotalPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero((int) orderSubmitGoods1.goodsCost * orderSubmitGoods1.goodsCount)));//乐享币价格
                    tvGoodPriceLeXiangBi.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero((int) orderSubmitGoods1.goodsCost)));//商品详情中的乐享币价格
                    tvGoodsCountPrice1.setText(
                            MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(
                                    (int) orderSubmitGoods1.goodsCost * orderSubmitGoods1.goodsCount)));//小计乐享币

                    typeFromDetail = orderSubmitGoods1.goods_type;
                    Logger.i("2017年12月14日 13:49:54-" + typeFromDetail);
                    switch (typeFromDetail) {
                        case LE_DOU_GOODS:
                        case LE_DOU_GOODS_2:
                        case GoodsType.CAL_POWER:
//                        区块益豆试验区/钜惠专区/区块益豆专区
                            tvBackLedou.setVisibility(View.VISIBLE);
                            tvBackLedou.setText(String.format("送%s", MoneyUtil.getLeXiangBi(orderSubmitGoods1.goodsReturnBean)));

                            tvTotalBackLedou.setVisibility(View.VISIBLE);
                            int goodsReturnBean = (int) (orderSubmitGoods1.goodsReturnBean);
                            float subsidy = orderSubmitGoods1.subsidy;
                            if (subsidy == 0) {
                                tvTotalBackLedou.setText(
                                        String.format("送%s", MoneyUtil.getLeXiangBiNoZero(
                                                goodsReturnBean * orderSubmitGoods1.goodsCount))
                                );
                            } else {
                                tvTotalBackLedou.setText(
                                        String.format("送%s+平台加赠%s", MoneyUtil.getLeXiangBiNoZero(
                                                goodsReturnBean * orderSubmitGoods1.goodsCount),
                                                MoneyUtil.getLeXiangBiNoZero(subsidy * orderSubmitGoods1.goodsCount)));
                            }

                            tvTotalBackLedou.setCompoundDrawablesWithIntrinsicBounds(
                                    ContextCompat.getDrawable(mContext, R.mipmap.icon_ledou),
                                    null, null, null
                            );
                            break;
                        default:
                            break;
                    }
                }
                break;
            case GOODS_SHOPPING_CART:
                btnPay.setText("提交订单");
                llOrderType1.setVisibility(View.GONE);//从购物车来到确认提交订单界面，单独的商品订单要隐藏
                shoppingCartList = (ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>>) intent.getSerializableExtra("cartList");
                Logger.i("2017年7月25日 15:16:21-" + shoppingCartList.toString());

                if (shoppingCartList.size() <= 1) {//如果只有一家店的商品，那么隐藏拆分订单提示
                    tvSplitPackage.setVisibility(View.GONE);
                }
                for (int i = 0; i < shoppingCartList.size(); i++) {
                    for (int n = 0; n < shoppingCartList.get(i).size(); n++) {
                        ShoppingCartListEntity2.ListBean listBean = shoppingCartList.get(i).get(n);
//                    商品价格计算
                        cartId.append(listBean.cartId).append(",");
                        goodsCount += listBean.goodsCount;
                        typeFromShopCar += listBean.type;
                        buyPrice += listBean.goodsCost * listBean.goodsCount;
                        if ("3".equals(listBean.type) || "4".equals(listBean.type) || "5".equals(listBean.type)) {
//                        returnIntegral += listBean.returnIntegral * listBean.goodsCount;
                            returnLedou += listBean.returnBean * listBean.goodsCount;
                            gavePower += listBean.subsidy * listBean.goodsCount;
                        }
                    }
                    //评论
                }
                cartId = cartId.deleteCharAt(cartId.length() - 1);//去除最后一个逗号

                tvGoodsCount.setText("共计" + goodsCount + "件商品");
                Logger.i("初始化请求运费");
                orderSubmitOrderListAdapter = new OrderSubmitOrderListAdapter(shoppingCartList, this.mContext, 0);
                nslvOuter2.setAdapter(orderSubmitOrderListAdapter);
                nslvOuter2.setFocusable(false);
                nslvOuter2.setFocusableInTouchMode(false);

                Logger.i("2017年7月25日 11:34:45-" + typeFromShopCar);
                if (typeFromShopCar.contains("0")
                        && !typeFromShopCar.contains("1")
                        && !typeFromShopCar.contains("2")
                        && !typeFromShopCar.contains("3")
                        && !typeFromShopCar.contains("4")) {
                    //线上商品
                    tvTotalPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(buyPrice)));
                } else if (isCalPowerGoods()) {
                    tvTotalPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(buyPrice)));
                    tvTotalBackLedou.setVisibility(View.VISIBLE);
                    if (gavePower == 0) {
                        tvTotalBackLedou.setText(String.format("送%s", MoneyUtil.getLeXiangBiNoZero(returnLedou)));
                    } else {
                        tvTotalBackLedou.setText(String.format("送%s+平台加赠%s",
                                MoneyUtil.getLeXiangBiNoZero(returnLedou), MoneyUtil.getLeXiangBiNoZero(gavePower)));
                    }
                    tvTotalBackLedou.setCompoundDrawablesWithIntrinsicBounds(
                            ContextCompat.getDrawable(mContext, R.mipmap.icon_ledou),
                            null, null, null
                    );
                }
                break;
            default:
                break;
        }

        //        获取用户代购券数量
        if (userMoneyPresenter == null) {
            userMoneyPresenter = new UserMoneyPresenterImpl(this);
        }
        Subscription subscription = userMoneyPresenter.getUserMoney(mContext);
        addSubscription(subscription);
    }

    private void initListener() {
        llSelectAddress.setOnClickListener(this);
        checkDaiGouQuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvTotalBackLedou.setVisibility(View.INVISIBLE);
                    refreshLeDouView(true, View.INVISIBLE);
                    tvLeXiang.setText(
                            MoneyUtil.set¥Money(
                                    MoneyUtil.getLeXiangBiNoZero(
                                            String.valueOf(
                                                    new BigDecimal(String.valueOf(getOrderTotalPriceWithGoodsAndExpress()))
                                                            .subtract(new BigDecimal(usedDaiGouQuanMoney))))));
                } else {
                    tvTotalBackLedou.setVisibility(View.VISIBLE);
                    refreshLeDouView(false, View.VISIBLE);
                    tvLeXiang.setText(
                            MoneyUtil.set¥Money(
                                    MoneyUtil.getLeXiangBiNoZero(
                                            getOrderTotalPriceWithGoodsAndExpress()
                                    )
                            )
                    );
                }
            }
        });
    }

    /**
     * 只包含区块益豆商品
     *
     * @return
     */
    private boolean isCalPowerGoods() {
        return (typeFromShopCar.contains("4") || typeFromShopCar.contains("3") || typeFromShopCar.contains(GoodsType.CAL_POWER))
                && !typeFromShopCar.contains("1") && !typeFromShopCar.contains("2") && !typeFromShopCar.contains("0");
    }

    private void refreshLeDouView(boolean isChecked, @HomePageFragment.Visibility int visibility) {
        switch (orderType) {
            case GOODS_SHOPPING_CART:
                for (ArrayList<ShoppingCartListEntity2.ListBean> listBeans : shoppingCartList) {
                    for (ShoppingCartListEntity2.ListBean listBean : listBeans) {
                        listBean.isShowGave = isChecked;
                    }
                }
                orderSubmitOrderListAdapter.notifyDataSetChanged();
                break;
            case GOODS_DETAIL:
                tvBackLedou.setVisibility(visibility);
                break;
            default:
                break;
        }
    }

    /**
     * 获取订单商品价格+运费总额
     *
     * @return
     */
    private double getOrderTotalPriceWithGoodsAndExpress() {
        return getGoodsTotalPrice() + expressPrice;
    }

    private double getGoodsTotalPrice() {
        double goodsTotalPrice = 0;
        switch (orderType) {
            case GOODS_SHOPPING_CART:
                goodsTotalPrice = getGoodsTotalPriceFromShoppingCart();
                break;
            case GOODS_DETAIL:
                goodsTotalPrice = getGoodsTotalPriceFromGoodsDetail();
                break;
            default:
                break;
        }
        return goodsTotalPrice;
    }

    /**
     * 从购物车提交订单 商品总价 单位分
     *
     * @return
     */
    private double getGoodsTotalPriceFromShoppingCart() {
        return buyPrice;
    }

    /**
     * 从商品详情提交订单 商品总价 单位分
     *
     * @return
     */
    private double getGoodsTotalPriceFromGoodsDetail() {
        return orderSubmitGoods1.goodsCost
                * orderSubmitGoods1.goodsCount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (addressFlag) {
            getDefaultUserAddress();
        }
    }

    /**
     * 获取默认地址
     */
    public void getDefaultUserAddress() {
        startMyDialog();
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(this.mContext);
        }
        mallNetRequest.getDefaultUserAddress(UserDefaultAddressEntity.class, new RequestCallBack<UserDefaultAddressEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UserDefaultAddressEntity> responseInfo) {
                stopMyDialog();
                Log.i("确认提交订单", "确认提交订单" + responseInfo.result.info);
                Log.i("确认提交订单code", "确认提交订单" + responseInfo.result.code);
                userAddressLists = responseInfo.result.info;
                switch (responseInfo.result.code) {
                    case 1://如果有默认地址，则返回默认地址，如果没有默认地址，服务器会自动取地址列表第一条，
                        //有默认地址
                        tvReceiverNamePhone.setText(userAddressLists.contacts + "    " + userAddressLists.phone);
                        Spanned spanned = Html.fromHtml(userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name + userAddressLists.fullAddress + userAddressLists.address);
                        tvReceiverAddress.setText(spanned);
                        addressId = responseInfo.result.info.address_id;
                        Logger.i("主动请求默认地址获取运费");
                        getExpressList(addressId);
                        break;
                    case -23:
                    case -4:
                        tokenFailure();
                        break;
                    case -21:
                        //如果地址列表没有数据，则返回-21
                        //请求地址的列表,取第一条数据拿到当前的数据去请求快递费用
//                        getAddressList();
                        String title = "温馨提示";
                        String str = "请设置收货地址";
                        showDialog(title, str, null, 0, Gravity.CENTER, "是", "否", true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        selectAddress();
                                        dialog.dismiss();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }, JPCommitOrderActivity.this.mContext);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("获取地址失败，请检查网络");
//                addressId = "";
            }
        });
    }

    private void getExpressList(String addressId) {
        switch (orderType) {
            case GOODS_DETAIL:
                type = "";
                getExpressMoneyFromDetail(orderSubmitGoods1.goodsId, String.valueOf(orderSubmitGoods1.goodsCount), orderSubmitGoods1.goodsNorms, orderSubmitGoods1.filiale_id, addressId
                        , orderSubmitGoods1.supplier_id, type);
                break;
            case GOODS_SHOPPING_CART:
                getExpressMoneyFromShoppingCart(cartId.toString(), addressId);
                break;
            default:
                break;
        }
    }

    /**
     * 从 商品详情 进入提交订单界面时获取运费
     */
    @SuppressWarnings("unchecked")
    private void getExpressMoneyFromDetail(String goodsId, String goodsCount, String goodsSku, String filialeId, String addressId, String supplierId, String type) {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(this.mContext);
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getGoodDetailExpressV3("goods/mallgoods_express_v3",
                        goodsCount, goodsSku, filialeId, addressId, supplierId, goodsId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallGoodsExpressEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llFinalMoney.setVisibility(View.GONE);
                        tvPayUnclickable();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MallGoodsExpressEntity o) {
                        List<MallGoodsExpressEntity.ListBean> list = o.list;
                        if (list.size() >= 0) {
                            MallGoodsExpressEntity.ListBean listBean = list.get(0);
                            expressId = listBean.expressId;
                            expressPrice = listBean.expressPrice;
                            tvExpressPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(String.valueOf(expressPrice))));
                            tvLeXiang.setText(
                                    MoneyUtil.set¥Money(
                                            MoneyUtil.getLeXiangBiNoZero(
                                                    getOrderTotalPriceWithGoodsAndExpress()
                                            )));
                            llFinalMoney.setVisibility(View.VISIBLE);
                        }
                        expressIsSucess = true;//运费请求成功，可以提交订单
                        tvPayClickable();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 从 购物车 进入提交订单界面时获取运费
     */
    @SuppressWarnings("unchecked")
    private void getExpressMoneyFromShoppingCart(String cartList, String addressId) {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(this.mContext);
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getCartExpressV3("goods/mallgoods_cart_express_v3", cartList, addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallGoodsExpressEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tvPayUnclickable();
                        llFinalMoney.setVisibility(View.GONE);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MallGoodsExpressEntity o) {
                        List<MallGoodsExpressEntity.ListBean> list = o.list;
                        if (list.size() >= 0) {
                            MallGoodsExpressEntity.ListBean listBean = list.get(0);
                            expressId = listBean.expressId;
                            expressPrice = listBean.expressPrice;
                            tvExpressPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(String.valueOf(expressPrice))));
                            llFinalMoney.setVisibility(View.VISIBLE);
                            tvLeXiang.setText(
                                    MoneyUtil.set¥Money(
                                            MoneyUtil.getLeXiangBiNoZero(
                                                    getOrderTotalPriceWithGoodsAndExpress())
                                    ));
                        }
                        expressIsSucess = true;//运费请求成功，可以提交订单
                        tvPayClickable();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 确认提交订单按钮设置为不可点击
     */
    private void tvPayUnclickable() {
        btnPay.setClickable(false);
        btnPay.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.btn_unclick));
    }

    /**
     * 确认提交订单按钮设置为可点击
     */
    private void tvPayClickable() {
        btnPay.setClickable(true);
        btnPay.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.color_red));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if (expressIsSucess) {//运费请求成功，可以提交订单
                    commitOrder();
                } else {
                    showToast(R.string.beyond_express_range);
                }
                break;
            case R.id.ll_select_address:
                selectAddress();
                break;
            case R.id.tv_how_to_get_lequan:
                Intent intent = new Intent(this.mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Constants.HowToGetCoupon);
                startActivity(intent);
                break;
            case R.id.rl_iv_left1://扩大点击返回按钮区域
            case R.id.iv_left1:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 提交订单
     */
    private void commitOrder() {
        switch (orderType) {
            case GOODS_DETAIL:
                //从商品详情来到该界面后，提交订单
                if (mallNetRequest == null) {
                    mallNetRequest = new MallNetRequest(this.mContext);
                }
                orderRemark = et.getText().toString();
                mallNetRequest.makeMallOrderV3(checkDaiGouQuan.isChecked() ? "1" : "0", orderSubmitGoods1.region_id, orderSubmitGoods1.goodsId, orderSubmitGoods1.goodsCount
                        , orderSubmitGoods1.goodsNorms, orderRemark, addressId, expressId, expressPrice, "1",
                        orderSubmitGoods1.filiale_id, orderSubmitGoods1.supplier_id, typeFromDetail, new RequestCallBack<MakeMallorderEntity>() {
                            @Override
                            public void onStart() {
                                startMyDialog();
                                super.onStart();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<MakeMallorderEntity> responseInfo) {
                                stopMyDialog();
                                MakeMallorderEntity result = responseInfo.result;
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, result)) {
                                    if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(mContext, result.code, result.msg)) {
                                        switch (responseInfo.result.code) {
                                            case 1:
                                                MakeMallorderEntity data = responseInfo.result;
                                                sendNextActivity(data.orderIndex, data.order_total_lebi);
                                                break;
                                            case -23:
                                            case -4:
                                                tokenFailure();
                                                break;
                                            default:
                                                showToast(responseInfo.result.msg);
                                                break;
                                        }
                                    }
                                }


                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                stopMyDialog();
                                showToast(R.string.net_work_not_available);

                            }
                        });
                break;
            case GOODS_SHOPPING_CART:

//            从购物车页面跳转过来
                for (int i = 0, count = nslvOuter2.getCount(); i < count; i++) {
                    int shopId = shoppingCartList.get(i).get(0).shopId;
                    int shopType = shoppingCartList.get(i).get(0).shopType - 1;
                    LinearLayout childAt = (LinearLayout) nslvOuter2.getChildAt(i);
                    EditText etNote = (EditText) childAt.findViewById(R.id.et_note);
                    String remark = etNote.getText().toString().trim();
                    //按照服务器需要的字符串格式，拼接每个订单的备注
                    if (i < count - 1) {
                        remarkText += "{\"shop_id\":\"" + shopId + "\",\"remark\":\"" + remark + "\",\"type\":\"" + shopType + "\"},";
                    } else {
                        remarkText += "{\"shop_id\":\"" + shopId + "\",\"remark\":\"" + remark + "\",\"type\":\"" + shopType + "\"}";

                    }

                }
                remarkText = "[" + remarkText + "]";
                Logger.i("sunString:" + remarkText);
                if (mallNetRequest == null) {
                    mallNetRequest = new MallNetRequest(this.mContext);
                }
                /**
                 * 购物车下单备注信息 [{"shop_id":"","remark":"","type":""},{}] type 0 (对应购物车列表shop_type=1)正常旗舰店，1(对应购物车列表shop_type=2)兑换中心id
                 */

                mallNetRequest.makeMallorderCartV3(checkDaiGouQuan.isChecked() ? "1" : "0", cartId.toString(), remarkText, addressId, expressId, expressPrice, new RequestCallBack<MakeMallShoppingOrderEntity>() {
                    @Override
                    public void onStart() {
                        startMyDialog();
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<MakeMallShoppingOrderEntity> responseInfo) {
                        stopMyDialog();
                        MakeMallShoppingOrderEntity result = responseInfo.result;
                        switch (result.code) {
                            case 1:
                                makeMallShoppingList = result.list;
                                sendNextActivity(result.order_total_lebi);
                                break;
                            case -23:
                            case -4:
                                tokenFailure();
                                break;
                            default:
                                showToast(result.msg);
                                break;

                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        startMyDialog();
                        showToast(R.string.net_work_not_available);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 选择收货地址
     */
    public void selectAddress() {
        Intent intent = new Intent(this.mContext, AddressManageActivity.class);
        intent.putExtra("FLAG", "orderIn");//判断是从下订单进入还是从我的地址进入
        if (addressId != null) {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, addressId);//选中的标示
        } else {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
        }
        startActivityForResult(intent, 0);
    }

    /**
     * 商品详情直接下订单进入收银台
     *
     * @param orderIndex
     * @param orderTotalLebi
     */
    private void sendNextActivity(String orderIndex, String orderTotalLebi) {
        Intent intent = new Intent(this, CashDeskActivity2.class);
        intent.putExtra(PAY_FROM_TAG, orderType);
        intent.putExtra("orderIndex", orderIndex);
        intent.putExtra("order_total_lebi", orderTotalLebi);
        intent.putExtra("payType", "1");//1商城订单 2 商家入驻或续费 3店内支付

        startActivity(intent);
        finish();
        AppManager.getInstance().killActivity(JPNewCommDetailActivity.class);
        //刷新购物车页面标识
        sp.edit().putBoolean(Constants.REFRESH_SHOP_FRAGMENT, true).commit();
        //刷新个人页面标识
        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
    }

    private void tokenFailure() {
        showDialog("提示", "登录状态失效", "可能您的账号在其他设备登录，请重新登录", R.mipmap.ic_warning, Gravity.RIGHT, "确定", null, true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        startActivity(new Intent(JPCommitOrderActivity.this.mContext, LeFenPhoneLoginActivity.class));
                        break;
                    default:
                        break;
                }

            }
        }, this.mContext);
    }

    /**
     * 购物车下订单进入收银台
     *
     * @param orderTotalLebi
     */
    private void sendNextActivity(String orderTotalLebi) {
        Intent intent = new Intent(this, CashDeskActivity2.class);
        intent.putExtra(CashDeskActivity2.PAY_FROM_TAG, orderType);
        intent.putExtra("list", getList());
        intent.putExtra("order_total_lebi", orderTotalLebi);
        intent.putExtra("payType", "1");//1商城订单 2 商家入驻或续费 3店内支付

        startActivity(intent);
        //刷新购物车页面标识
        sp.edit().putBoolean(Constants.REFRESH_SHOP_FRAGMENT, true).commit();
        //刷新个人页面标识
        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
        finish();
    }

    private ArrayList<MakeMallShoppingOrderEntity.MakeMallShopping> getList() {
        ArrayList<MakeMallShoppingOrderEntity.MakeMallShopping> list = new ArrayList<>();

        for (MakeMallShoppingOrderEntity.MakeMallShopping makeMallShopping : makeMallShoppingList
                ) {
            list.add(makeMallShopping);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: //选择地址
                Logger.i("onActivityResult 222 " + requestCode + "  " + RESULT_OK);
                if (resultCode == RESULT_OK) {

                    UserAddressLists userAddressList = (UserAddressLists) data.getExtras().getSerializable("USE_RADDRESS_LIST");
                    Logger.i("2017年2月27日 09:48:47---" + userAddressList.toString());
                    addressId = userAddressList.address_id;
                    tvReceiverNamePhone.setText(userAddressList.contacts + "  " + userAddressList.phone);
                    if (userAddressList.default_address == 1) {
                        Spanned spanned = Html.fromHtml("<font color  ='#f75a53'>[默认]</font>" + userAddressList.province_name + userAddressList.city_name + userAddressList.county_name + userAddressList.fullAddress + userAddressList.address);
                        tvReceiverAddress.setText(spanned);
                    } else {
                        tvReceiverAddress.setText(userAddressList.province_name + userAddressList.city_name + userAddressList.county_name + userAddressList.fullAddress + userAddressList.address);
                    }
                    actAddressId = userAddressList.address_id;

                    addressFlag = false;
                    Logger.i("手动选择地址后请求运费");
                    getExpressList(addressId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2) {
        checkDaiGouQuan.setEnabled(true);
        // DaiGouQuan: 2018/8/7 代购券业务暂时隐藏
        myBalanceEntity2.diGouQuan="0";

        if (NumberFormat.convertToFloat(myBalanceEntity2.diGouQuan, 0f) <= 0) {
//            没有代购券
            checkDaiGouQuan.setEnabled(false);
            usedDaiGouQuanMoney = "0";
        } else {
            BigDecimal goodsPriceHalf = null;
            switch (orderType) {
                case GOODS_DETAIL:
                    goodsPriceHalf = new BigDecimal(String.valueOf(getGoodsTotalPrice()))
                            .divide(new BigDecimal(DaiGouQuanConstant.Rate), 0, BigDecimal.ROUND_HALF_DOWN);
                    break;
                case GOODS_SHOPPING_CART:
                    goodsPriceHalf = new BigDecimal(String.valueOf(getGoodsTotalPrice())).divide(new BigDecimal(DaiGouQuanConstant.Rate), 0, BigDecimal.ROUND_HALF_DOWN);
                    break;
                default:
                    break;
            }
            if (new BigDecimal(myBalanceEntity2.diGouQuan).compareTo(
                    goodsPriceHalf) >= 0) {
                usedDaiGouQuanMoney = String.valueOf(goodsPriceHalf);
            } else {
                usedDaiGouQuanMoney = myBalanceEntity2.diGouQuan;
            }
        }
        tvMoneyDaiGou.setText(MoneyUtil.getMoneyWith¥Floor(usedDaiGouQuanMoney));

    }
}
