package com.yilian.mall.jd.activity.goodsdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.adapter.ItemTitlePagerAdapter;
import com.yilian.mall.jd.dialog.SpecPopwindow;
import com.yilian.mall.jd.enums.JdShoppingCartType;
import com.yilian.mall.jd.fragment.goodsdetail.JDGoodsDetailRightFragment;
import com.yilian.mall.jd.fragment.goodsdetail.JDGoodsInfoLeftFragment;
import com.yilian.mall.jd.presenter.JDCheckAreaSupportSellPresenter;
import com.yilian.mall.jd.presenter.impl.JDCheckAreaSupportSellPresenterImpl;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.jd.utils.NoScrollViewPager;
import com.yilian.mall.jd.view.PagerSlidingTabStrip;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.activity.ShareType;
import com.yilian.mylibrary.utils.ShareUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ShareEntity;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarGoodsCountsEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mylibrary.Constants.REFRESH_JD_GOODS_DETAIL;


/**
 * 商品详情
 */
public class JDGoodsDetailActivity extends BaseAppCompatActivity implements
        View.OnClickListener, JDGoodsDetailInfoPresenter.View,
        JDGoodsInfoLeftFragment.ShippingAddressListener,
        JDCheckAreaSupportSellPresenter.View, JDGoodsInfoLeftFragment.GoodsSoldOutListener {

    public static final String JD_GOODS_DETAIL_INFO_ENTITY = "jdGoodsDetailInfoEntity";
    public static Toast toast;
    public PagerSlidingTabStrip pstsTabs;
    public NoScrollViewPager vpContent;
    public TextView tvTitle;
    private VaryViewUtils varyViewUtils;
    private LinearLayout llBack;
    private List<Fragment> fragmentList = new ArrayList<>();
    private JDGoodsInfoLeftFragment goodsInfoFragment;
    private JDGoodsDetailRightFragment goodsDetailFragment;
    /**
     * 商品详情
     */
    private JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity;
    //    private LinearLayout secondView;
    private SpecPopwindow specPopwindow;//规格弹出框
    private int sHeight;
    private Button mHiddenButton;
    private String sku, lat, lng;
    private int jdType = JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON;
    /**
     * 收货地址
     */
    private JDShippingAddressInfoEntity.DataBean shippingAddressInfoEntity;
    private JDGoodsDetailInfoPresenter jdGoodsDetailInfoPresenter;
    private View tvAreaSupportSell;
    private LinearLayout llJdTypeCommon, llJdTypeCard;
    private LinearLayout llShoppingCar, llShoppingCarCard;
    private TextView tvShoppingCounts, tvShoppingCountsCard;
    private Button bntAddShoppingCar, bntAddShoppingCarCard;
    private Button btnCommit, btnCommitCard;
    private String jdAddGoodsSku;
    private int jdAddGoodsCounts;
    private View toastView;

    /**
     * 是否卖光
     * 默认没有卖光
     */
    private boolean isSaleOut = false;
    /**
     * 是否在售货范围内
     * 默认在范围内
     */
    private boolean isSupportAreaSell = true;
    private View ivShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_goods_detail);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (toast != null) {
            toast.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceUtils.readBoolConfig(Constants.REFRESH_JD_GOODS_DETAIL, mContext, false)) {
            getGoodsDetailInfo();
            PreferenceUtils.writeBoolConfig(REFRESH_JD_GOODS_DETAIL, false, mContext);
        }
    }

    private void getGoodsDetailInfo() {
        if (jdGoodsDetailInfoPresenter == null) {
            jdGoodsDetailInfoPresenter = new JDGoodsDetailInfoPresenterImpl(this);
        }
        jdGoodsDetailInfoPresenter.getJdGoodsDetailInfo(sku, lat, lng, String.valueOf(jdType));
    }

    protected void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setVaryViewByJDOrderList(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                initData();
            }
        });

        ivShare = findViewById(R.id.iv_share);
        tvAreaSupportSell = findViewById(R.id.tv_area_support_sell);
        pstsTabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp_content);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llBack = findViewById(R.id.ll_back);

        //普通 京东详情 点击按钮
        llJdTypeCommon = findViewById(R.id.ll_jd_type_common);
        llShoppingCar = findViewById(R.id.ll_shopping_car);
        tvShoppingCounts = findViewById(R.id.tv_shopping_num);
        bntAddShoppingCar = findViewById(R.id.bnt_add_shopping);
        btnCommit = findViewById(R.id.btn_commit);
        //购物卡 京东详情 点击按钮
        llJdTypeCard = findViewById(R.id.ll_jd_type_card);
        llShoppingCarCard = findViewById(R.id.ll_shopping_car_card);
        tvShoppingCountsCard = findViewById(R.id.tv_shopping_num_card);
        bntAddShoppingCarCard = findViewById(R.id.bnt_add_shopping_card);
        btnCommitCard = findViewById(R.id.btn_commit_card);
    }

    protected void initData() {
        sku = getIntent().getStringExtra("skuID");
        jdType = getIntent().getIntExtra("jdType", JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON);
        Logger.i("拿到的用于请求详情sku:" + sku);
        lat = PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext, "0");
        lng = PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext, "0");
//        setTestData();
        getGoodsDetailInfo();
        if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            //购物卡 京东详情
            llJdTypeCommon.setVisibility(View.GONE);
            llJdTypeCard.setVisibility(View.VISIBLE);
            getCardJdShoppingCarGoodsCounts();
        } else {
            //普通 京东详情
            llJdTypeCommon.setVisibility(View.VISIBLE);
            llJdTypeCard.setVisibility(View.GONE);
            getJdShoppingCarGoodsCounts();
        }

    }

    protected void initListener() {
        btnCommit.setOnClickListener(this);
        btnCommitCard.setOnClickListener(this);
        llBack.setOnClickListener(this);
        llShoppingCar.setOnClickListener(this);
        llShoppingCarCard.setOnClickListener(this);
        RxUtil.clicks(bntAddShoppingCar, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                addJdGoodsToShoppingCar();
            }
        });
        RxUtil.clicks(bntAddShoppingCarCard, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                addJdGoodsToShoppingCarCard();
            }
        });

    }

    /**
     * 添加商品到 购物卡 京东 购物清单
     */
    private void addJdGoodsToShoppingCarCard() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .addCardJdGoodsToShoppingCar("jd_card_cart/card_cart_add", 1 + "", sku)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();

                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        /**
                         *{@link com.yilian.mall.jd.fragment.JdShoppingCarFragment#updateShoppingCarData(JdShoppingCarEntity entity)}
                         */
                        EventBus.getDefault().post(new JdShoppingCarEntity());
                        showAddChoppingCarSuc();
                        getCardJdShoppingCarGoodsCounts();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 添加商品到购物车
     */
    @SuppressWarnings("unchecked")
    private void addJdGoodsToShoppingCar() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .addJdGoodsToShoppingCar("jd_cart/cart_add", 1 + "", sku)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();

                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        /**
                         *{@link com.yilian.mall.jd.fragment.JdShoppingCarFragment#updateShoppingCarData(JdShoppingCarEntity entity)}
                         */
                        EventBus.getDefault().post(new JdShoppingCarEntity());
                        showAddChoppingCarSuc();
                        getJdShoppingCarGoodsCounts();
                    }
                });
        addSubscription(subscription);
    }

    private void showAddChoppingCarSuc() {
        if (toastView == null) {
            toastView = View.inflate(mContext, R.layout.jd_layout_add_shopping_car_suc, null);
            TextView tv_msg = toastView.findViewById(R.id.tv_msg);
            if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
                //购物卡 京东详情
                tv_msg.setText("加入购物清单成功");
            } else {
                //普通 京东详情
                tv_msg.setText("加入购物车成功");
            }

            toast = new Toast(mContext);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(toastView);
        }
        toast.show();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    @Override
    public void showGoodsDetailInfo(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
        goodsInfoFragment = JDGoodsInfoLeftFragment.newInstance(jdGoodsDetailInfoEntity,
                this, this);
        goodsDetailFragment = JDGoodsDetailRightFragment.newInstance(jdGoodsDetailInfoEntity);
        this.jdGoodsDetailInfoEntity = jdGoodsDetailInfoEntity;
        fragmentList.add(goodsInfoFragment);
        fragmentList.add(goodsDetailFragment);

        vpContent.setAdapter(new ItemTitlePagerAdapter(getSupportFragmentManager(), fragmentList, new String[]{"商品", "详情"}));
        vpContent.setOffscreenPageLimit(3);
        pstsTabs.setViewPager(vpContent);

        varyViewUtils.showDataView();
        getShareContent();
    }

    /**
     * 获取分享内容
     */
    @SuppressWarnings("unchecked")
    private void getShareContent() {
        int shareType = ShareType.JD_SHARE;
        if(jdType==JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
            shareType =ShareType.CARD_JD_SHARE;
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getShareContent("share_list", shareType, sku)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShareEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShareEntity shareEntity) {
                        RxUtil.clicks(ivShare, new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                new ShareUtil(mContext)
                                        .share(mContext, shareEntity.title,
                                                shareEntity.content, shareEntity.imgUrl,
                                                shareEntity.url
                                                        + "&lat=" + PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext)
                                                        + "&lng=" + PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext));
                            }
                        });
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void setShippingAddressCallback(JDShippingAddressInfoEntity.DataBean dataBean) {
        shippingAddressInfoEntity = dataBean;
        if (dataBean != null && !TextUtils.isEmpty(dataBean.id)) {
            JDCheckAreaSupportSellPresenter jdCheckAreaSupportSellPresenter = new JDCheckAreaSupportSellPresenterImpl(this);
            jdCheckAreaSupportSellPresenter.checkAreaSupportSell(mContext, sku, dataBean.provinceId, dataBean.cityId, dataBean.countyId, dataBean.townId);
        } else {
            isSupportAreaSell = true;
            checkBntStatus();
        }
    }

    private void checkBntStatus() {
        if (!isSaleOut && isSupportAreaSell) {
            btnCanCommit();
        } else {
            btnCanNotCommit(Color.parseColor("#CACACA"), false);
        }
    }    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_shopping_car:
                JumpJdActivityUtils.toJdHomePageActivity(mContext, Constants.JD_INDEX_SHOPPING_CAR, null);
                break;
            case R.id.ll_shopping_car_card:
                JumpCardActivityUtils.toCardJdShoppingList(mContext, null);
                break;
            case R.id.ll_back:
                //返回
                finish();
                break;
            case R.id.btn_commit:
//                点击立即购买按钮
                if (!isLogin()) {
                    JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(mContext);
                    return;
                }
                if (jdGoodsDetailInfoEntity == null) {
                    showToast("商品信息异常");
                    return;
                }
                commit();
                break;
            case R.id.btn_commit_card:
                if (!isLogin()) {
                    JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(mContext);
                    return;
                }
                if (jdGoodsDetailInfoEntity == null) {
                    showToast("商品信息异常");
                    return;
                }
                commit();
                break;
            default:
                break;
        }
    }

    /**
     * 立即购买按钮可点击
     */
    private void btnCanCommit() {
        btnCanNotCommit(ContextCompat.getColor(mContext, R.color.color_red), true);
    }

    /**
     * 立即购买按钮不可点击
     *
     * @param color
     * @param clickable
     */
    private void btnCanNotCommit(int color, boolean clickable) {
        if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            //购物卡 京东详情
            bntAddShoppingCarCard.setClickable(clickable);
            if (clickable) {
                btnCommitCard.setBackgroundColor(color);
                btnCommitCard.setVisibility(View.VISIBLE);
                bntAddShoppingCarCard.setBackgroundResource(R.drawable.jd_shap_shopping_car);
            } else {
                btnCommitCard.setVisibility(View.GONE);
                bntAddShoppingCarCard.setBackgroundColor(color);
            }
        } else {
            //普通 京东详情
            bntAddShoppingCar.setClickable(clickable);
            if (clickable) {
                btnCommit.setBackgroundColor(color);
                btnCommit.setVisibility(View.VISIBLE);
                bntAddShoppingCar.setBackgroundResource(R.drawable.jd_shap_shopping_car);
            } else {
                btnCommit.setVisibility(View.GONE);
                bntAddShoppingCar.setBackgroundColor(color);
            }
        }

    }
    private void commit() {
        JDCommitOrderEntity jdCommitOrderEntity = new JDCommitOrderEntity(jdGoodsDetailInfoEntity.data.goodsInfo.name, jdGoodsDetailInfoEntity.data.goodsInfo.sku,
                jdGoodsDetailInfoEntity.data.goodsInfo.imagePath, 1, jdGoodsDetailInfoEntity.data.goodsPrice.price,
                jdGoodsDetailInfoEntity.data.goodsPrice.jdPrice, jdGoodsDetailInfoEntity.data.goodsPrice.returnBean);
        JumpJdActivityUtils.toJDCommitOrderActivity(mContext, shippingAddressInfoEntity, jdCommitOrderEntity, JdShoppingCartType.FROM_GOODS_DETAIL, jdType);
    }

    @Override
    public void supportAreaSell() {
        tvAreaSupportSell.setVisibility(View.GONE);
        isSupportAreaSell = true;
        checkBntStatus();
    }

    @Override
    public void unSupportAreaSell() {
        tvAreaSupportSell.setVisibility(View.VISIBLE);
        isSupportAreaSell = false;
        checkBntStatus();
    }

    @Override
    public void soldOutCallback(boolean soldOut) {
        isSaleOut = soldOut;
        checkBntStatus();
    }

    public void updateShoppingCarData() {
        if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            //购物卡
            getCardJdShoppingCarGoodsCounts();
        } else {
            //普通 购物车数量
            getJdShoppingCarGoodsCounts();
        }

    }

    /**
     * 购物卡 获取京东购物清单数量
     */
    private void getCardJdShoppingCarGoodsCounts() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getCardJdShoppingCarGoodsCounts("jd_card_cart/card_cart_count")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JdShoppingCarGoodsCountsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JdShoppingCarGoodsCountsEntity entity) {
                        int num = 100;
                        if (entity.list >= num) {
                            tvShoppingCountsCard.setVisibility(View.VISIBLE);
                            tvShoppingCountsCard.setText("+99");
                        } else if (entity.list == 0) {
                            tvShoppingCountsCard.setVisibility(View.GONE);
                        } else {
                            tvShoppingCountsCard.setVisibility(View.VISIBLE);
                            tvShoppingCountsCard.setText("" + NumberFormat.convertToInt(entity.list, 0));
                        }
                    }
                });
        addSubscription(subscription);

    }

    /**
     * 获取京东购物车数量
     */
    @SuppressWarnings("unchecked")
    private void getJdShoppingCarGoodsCounts() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getJdShoppingCarGoodsCounts("jd_cart/cart_count")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JdShoppingCarGoodsCountsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JdShoppingCarGoodsCountsEntity entity) {
                        int num = 100;
                        if (entity.list >= num) {
                            tvShoppingCounts.setVisibility(View.VISIBLE);
                            tvShoppingCounts.setText("+99");
                        } else if (entity.list == 0) {
                            tvShoppingCounts.setVisibility(View.GONE);
                        } else {
                            tvShoppingCounts.setVisibility(View.VISIBLE);
                            tvShoppingCounts.setText("" + NumberFormat.convertToInt(entity.list, 0));
                        }
                    }
                });
        addSubscription(subscription);

    }






}
