package com.yilian.mall.jd.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.enums.JdCommitOrderNoStockGoodsCount;
import com.yilian.mall.jd.enums.JdShoppingCartType;
import com.yilian.mall.jd.fragment.commitorder.CommitOrderGoodsListInfoFragment;
import com.yilian.mall.jd.fragment.commitorder.CommitOrderNoStockFragment;
import com.yilian.mall.jd.presenter.JDCommitOrderPresenter;
import com.yilian.mall.jd.presenter.JDGetFreightPresenter;
import com.yilian.mall.jd.presenter.impl.JDCommitOrderPresenterImpl;
import com.yilian.mall.jd.presenter.impl.JDGetFregithPresenterImpl;
import com.yilian.mall.jd.utils.JDDetailAddressUtil;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.ui.mvp.presenter.impl.UserMoneyPresenterImpl;
import com.yilian.mall.ui.mvp.view.inter.IUserMoneyView;
import com.yilian.mall.utils.DaiGouQuanConstant;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntities;
import com.yilian.networkingmodule.entity.jd.JDCheckPriceEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.JDFreightEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntities;
import com.yilian.networkingmodule.entity.jd.JDGoodsStoreEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;
import com.yilian.networkingmodule.entity.jd.jdEventBusModel.JDOrderListUpdateModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Subscription;

import static com.yilian.mylibrary.Constants.REFRESH_JD_GOODS_DETAIL;

/**
 * @author  JD 提交订单页面
 *         先检测库存 再检查价格是否改变 然后再提交订单
 */
public class JDCommitOrderActivity extends BaseAppCompatActivity implements View.OnClickListener,
        JDGetFreightPresenter.View, JDCommitOrderPresenter.View, CommitOrderNoStockFragment.NoStockListener, IUserMoneyView {
    public static final String TAG_SHIPPING_ADDRESS_INFO = "JDShippingAddressInfoEntity.DataBean";
    public static final String TAG_GOODS = "JDGoodsDetailInfoEntity";
    /**
     * 系统自动定位地址时的地址ID
     */
    public static final String SYSTEM_LOCATION_ADDRESS_ID = "0";
    /**
     * 提交订单界面显示商品图片数量最大值
     */
    public static final int MAX_SHOW_COUNT = 4;
    public JdShoppingCartType jdShoppingCartType = JdShoppingCartType.FROM_SHOPPING_CART;
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
    private TextView tvShippingName;
    private TextView tvJdShippingPhone;
    private TextView tvJdShippingDetailAddress;
    private ImageView ivSingleJdGoodsPic;
    private TextView tvSingleJdGoodsName;
    private TextView tvSingleJdPrice;
    private TextView tvSingleJdGoodsCount;
    private TextView tvTextGoodsCount;
    private View tvSubCount;
    private TextView tvSingleChangeCount;
    private View tvAddCount;
    private LinearLayout llJdGoodsCount;
    private TextView tvOrderPriceTitle;
    /**
     * 订单商品总价 不包括运费
     */
    private TextView tvOrderGoodsTotalPrice;
    private TextView tvOrderGavePowerTitle;
    private TextView tvOrderGavePower;
    private TextView tvOrderFreightTitle;
    private TextView tvOrderFreight;
    private Button btnCommitOrder;
    private ArrayList<JDCommitOrderEntity> jdCommitOrderEntities;
    // 京东商品类型限制 类型 0普通京东商品 1购物卡京东商
    private int jdType = JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON;
    private JDCommitOrderEntity jdSingleCommitOrderEntity;
    private JDShippingAddressInfoEntity.DataBean shippingAddressInfo;
    private View includeShippingInfo;
    private Button btnAddJdAddress;
    /**
     * 商品数量 默认1件 随着用户手动加减而改变 最小为1
     */
    private int goodsCount = 1;
    /**
     * 订单运费
     */
    private float orderFreight = 0;
    private JDGetFreightPresenter jdGetFreightPresenter;
    /**
     * 订单总价 商品总价+运费
     */
    private TextView tvOrderTotalPrice;
    private JDCommitOrderPresenter jdCommitOrderPresenter;
    private View includeSingleGoodsInfoView;
    private View includeMultipleGoodsInfoView;
    /**
     * 点击区域
     */
    private View llMultipleGoodsImages;
    /**
     * 图片容器
     */
    private LinearLayout llMultipleGoodsImagesContainer;
    /**
     * 订单商品总价 单位元
     * 初始赋值为0 防止BigDecimal计算时报空
     */
    private String orderTotalGoodsJdPrice = "0";
    /**
     * 订单赠送益豆总数量
     * 初始赋值为0 防止BigDecimal计算时报空
     */
    private String orderTotalReturnBean = "0";
    private String cartList = null;
    private UserMoneyPresenterImpl userMoneyPresenter;
    private AppCompatCheckBox checkBox;
    private TextView tvMoneyDaiGouQuan;
    /**
     * 用户抵扣券 单位元
     */
    private String mDiGouQuan;
    /**
     * 可使用的代购券数量 单位元
     */
    private String usableDaiGouQuanMoney = "0";
    /**
     * 使用的代购券数量
     */
    private String usedDaiGouQuanMoney = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jdcommit_order);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvMoneyDaiGouQuan = findViewById(R.id.tv_money_dai_gou);
        checkBox = findViewById(R.id.check_box_user_dai_gou);
        includeSingleGoodsInfoView = findViewById(R.id.include_single_goods_info);
        includeMultipleGoodsInfoView = findViewById(R.id.include_multiple_goods_info);
        llMultipleGoodsImages = includeMultipleGoodsInfoView.findViewById(R.id.ll_images_area);
        llMultipleGoodsImagesContainer = includeMultipleGoodsInfoView.findViewById(R.id.ll_images_contents);
        tvOrderTotalPrice = findViewById(R.id.tv_order_price_confirm);
        includeShippingInfo = findViewById(R.id.include_shipping_info);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("填写订单");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvShippingName = (TextView) findViewById(R.id.tv_shipping_name);
        tvJdShippingPhone = (TextView) findViewById(R.id.tv_jd_shipping_phone);
        tvJdShippingDetailAddress = (TextView) findViewById(R.id.tv_jd_shipping_detail_address);
        ivSingleJdGoodsPic = (ImageView) findViewById(R.id.iv_jd_goods_pic);
        tvSingleJdGoodsName = (TextView) findViewById(R.id.tv_jd_goods_name);
        tvSingleJdPrice = (TextView) findViewById(R.id.tv_jd_price);
        tvSingleJdGoodsCount = (TextView) findViewById(R.id.tv_jd_goods_count);
        tvTextGoodsCount = (TextView) findViewById(R.id.tv_text_goods_count);
        tvSubCount = (View) findViewById(R.id.tv_sub_count);
        tvSingleChangeCount = (TextView) findViewById(R.id.tv_change_count);
        tvAddCount = (View) findViewById(R.id.tv_add_count);
        llJdGoodsCount = (LinearLayout) findViewById(R.id.ll_jd_goods_count);
        tvOrderPriceTitle = (TextView) findViewById(R.id.tv_order_price_title);
        tvOrderGoodsTotalPrice = (TextView) findViewById(R.id.tv_order_price);
        tvOrderGavePowerTitle = (TextView) findViewById(R.id.tv_order_gave_power_title);
        tvOrderGavePower = (TextView) findViewById(R.id.tv_order_gave_power);
        tvOrderFreightTitle = (TextView) findViewById(R.id.tv_order_freight_title);
        tvOrderFreight = (TextView) findViewById(R.id.tv_order_fregith);
        btnCommitOrder = (Button) findViewById(R.id.btn_commit_order);
        btnAddJdAddress = findViewById(R.id.btn_add_jd_address);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        jdCommitOrderEntities = (ArrayList<JDCommitOrderEntity>) getIntent().getSerializableExtra(TAG_GOODS);
        jdShoppingCartType = (JdShoppingCartType) getIntent().getSerializableExtra("jdShoppingCartType");
        jdType = getIntent().getIntExtra("jdType", JumpToOtherPageUtil.JD_GOODS_TYPE_CARD);
        if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            tvOrderGavePowerTitle.setVisibility(View.GONE);
            tvOrderGavePower.setVisibility(View.GONE);
        }

        int goodsListSize = jdCommitOrderEntities.size();
        if (jdCommitOrderEntities != null && goodsListSize == 1 && jdShoppingCartType == JdShoppingCartType.FROM_GOODS_DETAIL) {
//            jdShoppingCartType = JdShoppingCartType.FROM_GOODS_DETAIL;
//            单个商品
            includeSingleGoodsInfoView.setVisibility(View.VISIBLE);
            includeMultipleGoodsInfoView.setVisibility(View.GONE);
            jdSingleCommitOrderEntity = jdCommitOrderEntities.get(0);
            goodsCount = jdSingleCommitOrderEntity.goodsCount;
            orderTotalGoodsJdPrice = jdSingleCommitOrderEntity.goodsJdPrice;
            orderTotalReturnBean = priceMultiplyCount(jdSingleCommitOrderEntity.returnBean, goodsCount);
            setSingleGoodsInfoView(jdSingleCommitOrderEntity);
        } else if (jdCommitOrderEntities != null) {
//            jdShoppingCartType = JdShoppingCartType.FROM_SHOPPING_CART;
//            多个商品
            includeSingleGoodsInfoView.setVisibility(View.GONE);
            includeMultipleGoodsInfoView.setVisibility(View.VISIBLE);
//                添加数据前先清空数据 防止移除无货商品时数据混乱
            orderTotalGoodsJdPrice = "0";
            orderTotalReturnBean = "0";
            cartList = null;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < goodsListSize; i++) {
                JDCommitOrderEntity jdCommitOrderEntity = jdCommitOrderEntities.get(i);
                Logger.i("商品价格信息" + i + "  jdprice:" + jdCommitOrderEntity.goodsJdPrice + "  returnBean:" + jdCommitOrderEntity.returnBean);
                orderTotalGoodsJdPrice = String.valueOf(MyBigDecimal.add(orderTotalGoodsJdPrice,
                        priceMultiplyCount(jdCommitOrderEntity.goodsJdPrice, jdCommitOrderEntity.goodsCount)));
                orderTotalReturnBean = String.valueOf(MyBigDecimal.add(orderTotalReturnBean,
                        priceMultiplyCount(jdCommitOrderEntity.returnBean, jdCommitOrderEntity.goodsCount)));
                if (i < goodsListSize - 1) {
                    stringBuilder.append(jdCommitOrderEntity.cartIndex).append(",");
                } else {
                    stringBuilder.append(jdCommitOrderEntity.cartIndex);
                }
            }
            cartList = String.valueOf(stringBuilder);

            setMultipleGoodsInfoView(jdCommitOrderEntities);
        }
//        获取用户代购券
        getUserMoney();
//        第一步判空是防止移除无货商品时地址信息被清空
        if (shippingAddressInfo == null) {
            shippingAddressInfo = (JDShippingAddressInfoEntity.DataBean) getIntent().getSerializableExtra(TAG_SHIPPING_ADDRESS_INFO);
            if (shippingAddressInfo == null) {
                if (jdCommitOrderPresenter == null) {
                    jdCommitOrderPresenter = new JDCommitOrderPresenterImpl(this, jdShoppingCartType);
                }
                jdCommitOrderPresenter.getDefaultAddress(mContext);
            } else {
                setAddressView();
            }
        }

    }

    /**
     * 获取用户代购券数量
     */
    private void getUserMoney() {
        checkBox.setEnabled(false);
        if (userMoneyPresenter == null) {
            userMoneyPresenter = new UserMoneyPresenterImpl(this);
        }
        Subscription subscription = userMoneyPresenter.getUserMoney(mContext);
        addSubscription(subscription);
    }

    private void initListener() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvOrderGavePowerTitle.setVisibility(View.GONE);
                    tvOrderGavePower.setVisibility(View.GONE);
                    usedDaiGouQuanMoney = usableDaiGouQuanMoney;
                } else {
                    tvOrderGavePowerTitle.setVisibility(View.VISIBLE);
                    tvOrderGavePower.setVisibility(View.VISIBLE);
                    usedDaiGouQuanMoney = "0";
                }
                setOrderTotalPriceView();
            }
        });
        RxUtil.clicks(llMultipleGoodsImages, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                打开多个商品列表
                CommitOrderGoodsListInfoFragment instance = CommitOrderGoodsListInfoFragment.getInstance(jdCommitOrderEntities);
                instance.initParentActivity(JDCommitOrderActivity.this);
                instance.show(getSupportFragmentManager(), CommitOrderGoodsListInfoFragment.class.getName());
            }
        });
        RxUtil.clicks(tvSingleJdPrice, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showPriceInstructionDialog("知道了", "因可能存在系统缓存、页面更新导致价格变动异常等不确定性情况出现，" +
                        "商品售价以本结算页商品价格为准；\n\n" +
                        "如有疑问，请立即联系销售商咨询。", "价格说明");

            }
        });

        RxUtil.clicks(tvSubCount, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//减少购买数量
                if (goodsCount > 1) {
                    goodsCount--;
                    orderTotalGoodsJdPrice = priceMultiplyCount(jdSingleCommitOrderEntity.goodsJdPrice, goodsCount);
                    orderTotalReturnBean = priceMultiplyCount(jdSingleCommitOrderEntity.returnBean, goodsCount);
                    if (hasAddress()) {
                        getFreight(shippingAddressInfo, goodsCount);
                    } else {
                        goodsCountChanged();
                    }
                }
            }
        });
        RxUtil.clicks(tvAddCount, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//增加购买数量
                goodsCount++;
                orderTotalGoodsJdPrice = priceMultiplyCount(jdSingleCommitOrderEntity.goodsJdPrice, goodsCount);
                orderTotalReturnBean = priceMultiplyCount(jdSingleCommitOrderEntity.returnBean, goodsCount);
                if (hasAddress()) {
                    getFreight(shippingAddressInfo, goodsCount);
                } else {
                    goodsCountChanged();
                }
            }
        });
        RxUtil.clicks(btnCommitOrder, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                提交订单 先检测库存 再核实价格 最后再提交订单
                if (!hasAddress()) {
                    showToast("请选择收货地址");
                    return;
                }
                checkJDStore();
            }
        });
        RxUtil.clicks(btnAddJdAddress, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                添加地址
                JumpJdActivityUtils.toJDShippingAddressListActivityForReuslt(JDCommitOrderActivity.this);
            }
        });
        RxUtil.clicks(includeShippingInfo, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
//                重新选择地址
                JumpJdActivityUtils.toJDShippingAddressListActivityForReuslt(JDCommitOrderActivity.this);
            }
        });
    }

    /**
     * 设置单个商品信息view界面内容
     *
     * @param jdGoodsDetailInfo
     */
    private void setSingleGoodsInfoView(JDCommitOrderEntity jdGoodsDetailInfo) {
        GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N2(jdGoodsDetailInfo.imagePath), ivSingleJdGoodsPic);
        tvSingleJdGoodsName.setText(jdGoodsDetailInfo.goodsName);
        tvSingleJdPrice.setText("¥ " + jdGoodsDetailInfo.goodsJdPrice);
        goodsCountChanged();
    }

    /**
     * 设置多个商品信息view界面内容
     *
     * @param jdCommitOrderEntities
     */
    private void setMultipleGoodsInfoView(ArrayList<JDCommitOrderEntity> jdCommitOrderEntities) {
        if (llMultipleGoodsImagesContainer != null) {
            llMultipleGoodsImagesContainer.removeAllViews();
        }
        for (int i = 0; i < jdCommitOrderEntities.size(); i++) {
            if (i < MAX_SHOW_COUNT) {
                JDCommitOrderEntity jdCommitOrderEntity = jdCommitOrderEntities.get(i);
                ImageView child = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DPXUnitUtil.dp2px(mContext, 62),
                        DPXUnitUtil.dp2px(mContext, 62));
                layoutParams.rightMargin = DPXUnitUtil.dp2px(mContext, 5);
                child.setLayoutParams(layoutParams);
                GlideUtil.showImage(mContext, JDImageUtil.getJDImageUrl_N2(jdCommitOrderEntity.imagePath), child);
                llMultipleGoodsImagesContainer.addView(child);
            }

        }
        changeOrderPrice();
    }

    private void checkJDStore() {
        if (jdCommitOrderPresenter == null) {
            jdCommitOrderPresenter = new JDCommitOrderPresenterImpl(this, jdShoppingCartType);
        }
        jdCommitOrderPresenter.checkJDStore(mContext, getSingleGoodsSku(),
                goodsCount, cartList, shippingAddressInfo.id,jdType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JDEditShippingAddressListActivity.GET_JD_ADDRESS
                && resultCode == JDEditShippingAddressListActivity.JD_ADDRESS_RESULT) {
//            选择地址后返回
            shippingAddressInfo = (JDShippingAddressInfoEntity.DataBean) data.getSerializableExtra(JDEditShippingAddressListActivity.TAG);
            setAddressView();
        }
    }

    /**
     * 设置地址信息view界面内容
     */
    private void setAddressView() {
        if (shippingAddressInfo != null && hasAddress()) {
            Logger.i("shippingAddressInfo:" + shippingAddressInfo.toString());
            includeShippingInfo.setVisibility(View.VISIBLE);
            btnAddJdAddress.setVisibility(View.GONE);
            tvShippingName.setText(shippingAddressInfo.name);
            tvJdShippingPhone.setText(PhoneUtil.formatPhoneMiddle4Asterisk(shippingAddressInfo.mobile));
            tvJdShippingDetailAddress.setText("        " + JDDetailAddressUtil.getJDShippingDetailAddressStr(shippingAddressInfo.province,
                    shippingAddressInfo.city, shippingAddressInfo.county, shippingAddressInfo.town, shippingAddressInfo.detailAddress));
            getFreight(shippingAddressInfo, goodsCount);
        } else {
            includeShippingInfo.setVisibility(View.GONE);
            btnAddJdAddress.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 地址信息是否空,或是否为系统自动定位地址
     *
     * @return true地址不为空且为用户手动设置的地址信息 false 为空或是系统自动定位地址
     */
    private boolean hasAddress() {
        return shippingAddressInfo != null && shippingAddressInfo.id != null && !SYSTEM_LOCATION_ADDRESS_ID.equals(shippingAddressInfo.id);
    }

    /**
     * 获取运费
     *
     * @param shippingAddressInfo
     */
    private void getFreight(JDShippingAddressInfoEntity.DataBean shippingAddressInfo, int goodsCount) {
        if (jdGetFreightPresenter == null) {
            jdGetFreightPresenter = new JDGetFregithPresenterImpl(this, jdShoppingCartType);
        }
        Subscription subscription = jdGetFreightPresenter.getFreight(mContext,
                getSingleGoodsSku(), goodsCount, cartList, shippingAddressInfo.id,jdType);
        addSubscription(subscription);
    }

    private String getSingleGoodsSku() {
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                if (jdSingleCommitOrderEntity != null) {
                    return jdSingleCommitOrderEntity.goodsSku;
                }
            case FROM_SHOPPING_CART:
                return null;
            default:
                return null;
        }
    }

    /**
     * 删除地址EventBus接收
     *
     * @param dataBean {@link JDEditShippingAddressListActivity#deleteShippingAddress(String)}
     */
    @Subscribe
    public void deleteAddress(JDShippingAddressInfoEntity.DataBean dataBean) {
        if (shippingAddressInfo != null) {
            if (shippingAddressInfo.id.equals(dataBean.id)) {
                clearAddress();
            }
        }
    }

    /**
     * 清除地址信息
     */
    void clearAddress() {
        shippingAddressInfo = null;
        orderFreight = 0;
        setAddressView();
        clearFreightView();
    }

    /**
     * 清除运费
     */
    private void clearFreightView() {
        tvOrderFreight.setText("");
        setOrderTotalPriceView();
    }

    /**
     * 设置订单总价View信息
     */
    private void setOrderTotalPriceView() {
        tvOrderTotalPrice.setText(getOrderTotalPriceStrWith¥(orderTotalGoodsJdPrice, orderFreight, usedDaiGouQuanMoney));
    }

    /**
     * 订单商品总价+订单运费 并拼接上¥符号
     *
     * @param orderTotalGoodsPrice 订单商品总价
     * @param orderFreight         订单运费
     * @param daiGouQuanMoney      使用的代购券数量
     * @return
     */
    private String getOrderTotalPriceStrWith¥(String orderTotalGoodsPrice, float orderFreight, String daiGouQuanMoney) {
        BigDecimal orderPriceBigDecimal = new BigDecimal(orderTotalGoodsPrice)
                .add(new BigDecimal(String.valueOf(orderFreight)))
                .subtract(new BigDecimal(daiGouQuanMoney));
        return "¥" + String.valueOf(orderPriceBigDecimal);

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
            default:
                break;
        }
    }

    @Override
    public void jdFreightChanged(JDFreightEntity jdFreightEntity) {
        orderFreight = jdFreightEntity.freight;
        setFreightView();
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                goodsCountChanged();
                break;
            case FROM_SHOPPING_CART:
                changeOrderPrice();
                break;
            default:
                break;
        }
    }

    private void setFreightView() {
        tvOrderFreight.setText("+ ¥" + orderFreight);
    }

    /**
     * 设置界面上商品数量信息
     */
    private void goodsCountChanged() {
        tvSingleJdGoodsCount.setText("×" + goodsCount);
        tvSingleChangeCount.setText(String.valueOf(goodsCount));
        changeOrderPrice();
    }

    private void changeOrderPrice() {
        setOrderTotalGoodsPriceView();
        setOrderTotalReturnBeanView();
        setOrderTotalPriceView();
    }

    /**
     * 设置订单商品总价View信息
     */
    private void setOrderTotalGoodsPriceView() {
        tvOrderGoodsTotalPrice.setText(getOrderGoodsTotalPriceStr(orderTotalGoodsJdPrice));
        refreshDaiGouQuanView();
    }

    /**
     * 设置订单赠送益豆总数View信息
     */
    private void setOrderTotalReturnBeanView() {
        tvOrderGavePower.setText(getOrderTotalReturnBeanStr());
    }

    @NonNull
    private String getOrderTotalReturnBeanStr() {
        return "+ " + orderTotalReturnBean;
    }

    @NonNull
    private String getOrderGoodsTotalPriceStr(String orderGoodsTotalPrice) {
        return "¥" + orderGoodsTotalPrice;
    }

    /**
     * 金额*数量
     *
     * @param price 金额
     * @param count 数量
     * @return 乘积
     */
    String priceMultiplyCount(String price, int count) {
        BigDecimal multiply = new BigDecimal(price).multiply(new BigDecimal(String.valueOf(count)));
        return String.valueOf(multiply);
    }

    @Override
    public void commitOrderSuccess(JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity) {
        // DaiGouQuan: 2018/8/7 代购券业务暂时隐藏
        jdCommitOrderSuccessEntity.daiGouQuanMoney = "0";
        //EventBus 推送 刷新订单列表
        JDOrderListUpdateModel updateModel = new JDOrderListUpdateModel();
        updateModel.handleType = JDOrderListUpdateModel.HandleType_add;
        /**
         * {@link  com.yilian.mall.jd.fragment.order.JDOrderCommonFragment #refreshUserBasicInfo(JDOrderListUpdateModel)}
         */
        EventBus.getDefault().post(updateModel);
        //EventBus 推送 刷新购物车 购物清单
        EventBus.getDefault().post(new JdShoppingCarEntity());
        if(jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
            //购物卡 京东收银台
            JumpCardActivityUtils.toJDCashDeskActivity(mContext, jdCommitOrderSuccessEntity);
        }else {
            JumpJdActivityUtils.toJDCashDeskActivity(mContext, jdCommitOrderSuccessEntity);
        }
        finish();
    }

    @Override
    public void checkJDPriceSuccess(JDCheckPriceEntity jdCheckPriceEntity) {
        float oldPrice = NumberFormat.convertToFloat(jdSingleCommitOrderEntity.goodsJdPrice, 0);
        float newPrice = NumberFormat.convertToFloat(jdCheckPriceEntity.data.jdPrice, 0);
        if (oldPrice == newPrice) {
//            价格正常 提交订单
            Logger.i("价格正常 提交订单");
            commitOrderFinally();
        } else {
//            价格异常 提示用户 并回到详情时进行刷新
            Logger.i("价格异常");
            PreferenceUtils.writeBoolConfig(REFRESH_JD_GOODS_DETAIL, true, mContext);
            showToast("价格发生了变化,请重新下单");
            stopMyDialog();
        }
    }

    @Override
    public void checkJDPriceSuccess(JDCheckPriceEntities jdCheckPriceEntities) {
        for (JDCheckPriceEntities.DataBean datum : jdCheckPriceEntities.data) {
            for (JDCommitOrderEntity jdCommitOrderEntity : jdCommitOrderEntities) {
                if (datum.cartIndex.equals(jdCommitOrderEntity.cartIndex)) {
                    float oldPrice = NumberFormat.convertToFloat(jdCommitOrderEntity.goodsJdPrice, 0);
                    float newPrice = NumberFormat.convertToFloat(datum.priceInfo.jdPrice, 0);
                    if (oldPrice != newPrice) {
                        //            价格异常 提示用户 并回到详情时进行刷新
                        Logger.i("价格异常");
                        PreferenceUtils.writeBoolConfig(REFRESH_JD_GOODS_DETAIL, true, mContext);
                        showToast("价格发生了变化,请重新下单");
                        stopMyDialog();
                        return;
                    }
                }
            }
        }
        //            价格正常 提交订单
        Logger.i("价格正常 提交订单");
        commitOrderFinally();
    }

    @Override
    public void checkJDStoreSuccess(JDGoodsStoreEntity jdGoodsStoreEntity) {
        checkJDGoodsPrice();
    }

    @Override
    public void checkJDStoreSuccess(JDGoodsStoreEntities jdGoodsStoreEntities) {
        List<JDGoodsStoreEntities.DataBean> data = jdGoodsStoreEntities.data;
        ArrayList<JDCommitOrderEntity> noStockDatas = new ArrayList<>();
        for (JDGoodsStoreEntities.DataBean datum : data) {
            if (datum.stock == JDGoodsStoreEntities.OUT_STOCK) {
                for (JDCommitOrderEntity jdCommitOrderEntity : jdCommitOrderEntities) {
                    if (datum.cartIndex.equals(jdCommitOrderEntity.cartIndex)) {
                        noStockDatas.add(jdCommitOrderEntity);
                    }
                }
            }
        }
        if (noStockDatas.size() > 0) {
            stopMyDialog();
//弹出无货商品列表
            if (noStockDatas.size() < jdGoodsStoreEntities.data.size()) {
                CommitOrderNoStockFragment.getInstance(noStockDatas, JdCommitOrderNoStockGoodsCount.PARTLY_NO_STOCK)
                        .show(getSupportFragmentManager(), CommitOrderNoStockFragment.TAG);
            } else {
                CommitOrderNoStockFragment.getInstance(noStockDatas, JdCommitOrderNoStockGoodsCount.ALL_NO_STOCK)
                        .show(getSupportFragmentManager(), CommitOrderNoStockFragment.TAG);
            }

            return;
        }
        checkJDGoodsPrice();
    }

    @Override
    public void getDefaultShippingAddressSuccess(@Nullable JDShippingAddressInfoEntity.DataBean defaultShippingAddress) {
        shippingAddressInfo = defaultShippingAddress;
        setAddressView();
    }

    private void checkJDGoodsPrice() {
        if (jdCommitOrderPresenter == null) {
            jdCommitOrderPresenter = new JDCommitOrderPresenterImpl(this, jdShoppingCartType);
        }
        jdCommitOrderPresenter.checkJDPrice(mContext, getSingleGoodsSku(), cartList,jdType);
    }

    private void commitOrderFinally() {
        if (jdCommitOrderPresenter == null) {
            jdCommitOrderPresenter = new JDCommitOrderPresenterImpl(this, jdShoppingCartType);
        }
        jdCommitOrderPresenter.commitOrder(mContext, getSingleGoodsSku(),
                goodsCount, shippingAddressInfo.id, "没有地方写备注的", getSingleGoodsPrice(),
                getSingleGoodsImagePath(), cartList, checkBox.isChecked() ? "1" : "0",jdType);
    }

    private String getSingleGoodsPrice() {
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                if (jdSingleCommitOrderEntity != null) {
                    return jdSingleCommitOrderEntity.goodsPrice;
                }
            case FROM_SHOPPING_CART:
                return null;
            default:
                return null;
        }
    }

    private String getSingleGoodsImagePath() {
        switch (jdShoppingCartType) {
            case FROM_GOODS_DETAIL:
                if (jdSingleCommitOrderEntity != null) {
                    return jdSingleCommitOrderEntity.imagePath;
                }
            case FROM_SHOPPING_CART:
                return null;
            default:
                return null;
        }
    }

    @Override
    public void backToShoppingCart() {
        finish();
    }

    @Override
    public void removeNoStockGoods(ArrayList<JDCommitOrderEntity> noStockDatas) {
        showToast("已移除无货商品");
        jdCommitOrderEntities.removeAll(noStockDatas);
        initData();
    }

    @Override
    public void changeShippingAddress() {
        JumpJdActivityUtils.toJDShippingAddressListActivityForReuslt(JDCommitOrderActivity.this);
    }

    /**
     * 展示价格说明弹窗
     */
    public void showPriceInstructionDialog(String cancelTxt, String message, String title) {
        View contentView = View.inflate(mContext, R.layout.jd_layout_price_instruction_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setView(contentView)
                .create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.library_module_bg_dialog_v7);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvTitle = (TextView) contentView.findViewById(R.id.title);
        TextView tvMessage = (TextView) contentView.findViewById(R.id.message);
        TextView btnCancel = (TextView) contentView.findViewById(R.id.btn_cancel);
        TextView tvPhone = contentView.findViewById(R.id.tv_phone);
        btnCancel.setText(cancelTxt);
        String mobile = "400-152-5159";
        String phone = "客服电话：" + mobile;
        SpannableString spannableString = new SpannableString(phone);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#F72D42"));
        spannableString.setSpan(colorSpan, phone.indexOf("：") + 1, phone.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPhone.setText(spannableString);
        RxUtil.clicks(tvPhone, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                PhoneUtil.call(mobile, mContext);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        }
        alertDialog.show();
        int width = ScreenUtils.getScreenWidth(mContext);
        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
        layoutParams.width = width - DPXUnitUtil.dp2px(mContext, 60);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    public void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2) {
        // DaiGouQuan: 2018/8/7 代购券业务暂时隐藏
        mDiGouQuan = "0";
//        mDiGouQuan = String.valueOf(
//                new BigDecimal(myBalanceEntity2.diGouQuan)
//                        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN));
        if (NumberFormat.convertToFloat(mDiGouQuan, 0f) > 0) {
            checkBox.setEnabled(true);
        } else {
            checkBox.setEnabled(false);
        }

        refreshDaiGouQuanView();
    }

    private void refreshDaiGouQuanView() {
        BigDecimal goodsPriceHalf = null;
        if (orderTotalGoodsJdPrice == null || mDiGouQuan == null) {
            return;
        }
        goodsPriceHalf = new BigDecimal(orderTotalGoodsJdPrice).divide(new BigDecimal(DaiGouQuanConstant.Rate), 2, BigDecimal.ROUND_HALF_DOWN);
        if (new BigDecimal(mDiGouQuan).compareTo(goodsPriceHalf) > 0) {
//            代购券充足
            usableDaiGouQuanMoney = String.valueOf(goodsPriceHalf);
        } else {
//            代购券不足
            usableDaiGouQuanMoney = mDiGouQuan;
        }
        tvMoneyDaiGouQuan.setText(String.format("¥%s", usableDaiGouQuanMoney));
    }
}
