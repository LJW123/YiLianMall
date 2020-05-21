package com.yilian.mall.suning.activity.goodsdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.dialog.SpecPopwindow;
import com.yilian.mall.jd.presenter.JDCheckAreaSupportSellPresenter;
import com.yilian.mall.suning.activity.SnCommitOrderActivity;
import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean.SnGoodsDetailInfoPresenter;
import com.yilian.mall.suning.activity.goodsdetail.goodsdetailclean.SnGoodsDetailInfoPresenterImpl;
import com.yilian.mall.suning.fragment.goodsdetail.SnGoodsInfoLeftFragment;
import com.yilian.mall.suning.module.SuNingCommitOrderGoodsModule;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.activity.ShareType;
import com.yilian.mylibrary.entity.TabEntity;
import com.yilian.mylibrary.utils.ShareUtil;
import com.yilian.networkingmodule.entity.ShareEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;
import com.yilian.networkingmodule.entity.suning.SnCheckStockEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnShoppingCarEntity;
import com.yilian.networkingmodule.entity.suning.SnShoppingCartCountEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

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
public class SnGoodsDetailActivity extends BaseAppCompatActivity implements
        View.OnClickListener, SnGoodsDetailInfoPresenter.View,
        SnGoodsInfoLeftFragment.ShippingAddressListener,
        JDCheckAreaSupportSellPresenter.View, SnGoodsInfoLeftFragment.GoodsSoldOutListener, SnGoodsInfoLeftFragment.ScrollDistanceListener {

    public static final String TAG = "SnGoodsDetailActivity";
    public static final String JD_GOODS_DETAIL_INFO_ENTITY = "jdGoodsDetailInfoEntity";
    public static final int ALPHA_INT_255 = 255;
    public static final int ALPHA_0 = 0;
    public static final int ALPHA_FLOAT_1 = 1;
    public static Toast toast;
    public com.flyco.tablayout.CommonTabLayout tabLayout;
    public FrameLayout vpContent;
    String[] tabsText = new String[]{"商品", "详情"};
    ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
    private LinearLayout llBack;
    private SnGoodsInfoLeftFragment goodsInfoFragment;
    /**
     * 商品详情
     */
    private SnGoodsDetailInfoEntity mSnGoodsDetailInfoEntity;
    //    private LinearLayout secondView;
    private SpecPopwindow specPopwindow;//规格弹出框
    private int sHeight;
    private Button mHiddenButton;
    private Button btnCommit;
    private String sku;
    private String lat;
    private String lng;
    /**
     * 收货地址
     */
    private SnShippingAddressInfoEntity.DataBean shippingAddressInfoEntity;
    private SnGoodsDetailInfoPresenter jdGoodsDetailInfoPresenter;
    private View tvAreaSupportSell;
    private LinearLayout llShoppingCar;
    private TextView tvShoppingCounts;
    private Button bntAddShoppingCar;
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
    private HashMap<String, String> mParams = new HashMap<>();
    private View titleRootView;
    private ImageView ivBack;
    //    private View viewLine;
    private ImageView ivShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_activity_goods_detail);
        initView();
        initData();
        initListener();
    }

    protected void initView() {
//        viewLine = findViewById(R.id.view_line);
        ivShare = findViewById(R.id.iv_share);
        ivBack = findViewById(R.id.iv_back);
        titleRootView = findViewById(R.id.ll_title_root);
//        titleRootView.getBackground().setAlpha(ALPHA_0);
        tvShoppingCounts = findViewById(R.id.tv_shopping_num);
        bntAddShoppingCar = findViewById(R.id.bnt_add_shopping);
        tvAreaSupportSell = findViewById(R.id.tv_area_support_sell);
        btnCommit = findViewById(R.id.btn_commit);
        tabLayout = (com.flyco.tablayout.CommonTabLayout) findViewById(R.id.psts_tabs);
        tabLayout.setAlpha(ALPHA_0);
        pstsTabsAddView();
        vpContent = (FrameLayout) findViewById(R.id.vp_content);
        llBack = findViewById(R.id.ll_back);
        llShoppingCar = findViewById(R.id.ll_shopping_car);
    }

    protected void initData() {
        sku = getIntent().getStringExtra(TAG);
        Logger.i("拿到的用于请求详情sku:" + sku);
        lat = PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext, "0");
        lng = PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext, "0");
//        TODO 添加测试数据
//        setTestData();
        getGoodsDetailInfo();
        getSnShoppingCarGoodsCounts();
    }

    protected void initListener() {
        btnCommit.setOnClickListener(this);
        llBack.setOnClickListener(this);
        llShoppingCar.setOnClickListener(this);
        RxUtil.clicks(bntAddShoppingCar, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                addJdGoodsToShoppingCar();
            }
        });
    }

    private void pstsTabsAddView() {
        for (int i = 0; i < tabsText.length; i++) {
            tabEntities.add(new TabEntity(tabsText[i], 0, 0));
        }
        tabLayout.setTabData(tabEntities);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (0 == position) {
                    goodsInfoFragment.svGoodsInfo.scrollTo(0, 0);
                } else {
                    goodsInfoFragment.svGoodsInfo.scrollTo(0, (int) goodsInfoFragment.includeWebView.getY());
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void getGoodsDetailInfo() {
        if (jdGoodsDetailInfoPresenter == null) {
            jdGoodsDetailInfoPresenter = new SnGoodsDetailInfoPresenterImpl(this);
        }
        mParams.put("c", "suning_goods/suning_goods_info");
        mParams.put("skuId", sku);
        jdGoodsDetailInfoPresenter.getJdGoodsDetailInfo(mParams, mContext);
    }

    /**
     * 获取苏宁购物车数量
     */
    @SuppressWarnings("unchecked")
    private void getSnShoppingCarGoodsCounts() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnShoppingCartCount("suning_cart/suning_cart_count")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SnShoppingCartCountEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SnShoppingCartCountEntity entity) {
                        int num = 100;
                        if (entity.count >= num) {
                            tvShoppingCounts.setVisibility(View.VISIBLE);
                            tvShoppingCounts.setText("+99");
                        } else if (entity.count == 0) {
                            tvShoppingCounts.setVisibility(View.GONE);
                        } else {
                            tvShoppingCounts.setVisibility(View.VISIBLE);
                            tvShoppingCounts.setText(String.valueOf(entity.count));
                        }
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
                .putInShoppingCart("suning_cart/suning_cart_add", "1", sku)
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
                        EventBus.getDefault().post(new SnShoppingCarEntity());
                        showAddChoppingCarSuc();
                        getSnShoppingCarGoodsCounts();
                    }
                });
        addSubscription(subscription);
    }

    private void showAddChoppingCarSuc() {
        if (toastView == null) {
            toastView = View.inflate(mContext, R.layout.jd_layout_add_shopping_car_suc, null);
            toast = new Toast(mContext);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(toastView);
        }
        toast.show();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_shopping_car:
                JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_CART, null);
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
                if (mSnGoodsDetailInfoEntity == null) {
                    showToast("商品信息异常");
                    return;
                }
                commit();
                break;
            default:
                break;
        }
    }

    private void commit() {
        SuNingCommitOrderGoodsModule suNingCommitOrderGoodsModule = new SuNingCommitOrderGoodsModule(
                mSnGoodsDetailInfoEntity.info.skuId, 1, mSnGoodsDetailInfoEntity.info.name, mSnGoodsDetailInfoEntity.info.snPrice,
                mSnGoodsDetailInfoEntity.info.returnBean, mSnGoodsDetailInfoEntity.info.image, null);
        JumpSnActivityUtils.toSnCommitOrderActivity(mContext, shippingAddressInfoEntity, suNingCommitOrderGoodsModule
                , SnCommitOrderActivity.FromeType.SN_GOODS_DETAIL);
    }

    @Override
    public void getGoodsDetailInfoSuccess(SnGoodsDetailInfoEntity snGoodsDetailInfoEntity) {
        goodsInfoFragment = SnGoodsInfoLeftFragment.newInstance(snGoodsDetailInfoEntity,
                this, this);
        goodsInfoFragment.setOnScrollDistance(this);
        this.mSnGoodsDetailInfoEntity = snGoodsDetailInfoEntity;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.vp_content, goodsInfoFragment).commitAllowingStateLoss();

        getShareContent();
    }

    /**
     * 获取分享内容
     */
    @SuppressWarnings("unchecked")
    private void getShareContent() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getShareContent("share_list", ShareType.SN_SHARE, sku)
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
                                        .share(mContext, shareEntity.title, shareEntity.content, shareEntity.imgUrl, shareEntity.url);
                            }
                        });
                    }
                });
        addSubscription(subscription);
    }


    @Override
    public void setShippingAddressCallback(SnShippingAddressInfoEntity.DataBean dataBean) {
        shippingAddressInfoEntity = dataBean;
        if (dataBean != null && !TextUtils.isEmpty(dataBean.id)) {

            checkStock();
        } else {
            isSupportAreaSell = true;
            checkBntStatus();
        }
    }

    /**
     * 检查是否有货
     */
    @SuppressWarnings("unchecked")
    private void checkStock() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .checkStock("suning_goods/suning_goods_count", sku, shippingAddressInfoEntity.cityId,
                        shippingAddressInfoEntity.countyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnCheckStockEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SnCheckStockEntity snCheckStockEntity) {
                        switch (snCheckStockEntity.result) {
                            case SnCheckStockEntity.HAS_STOCK:
                            case SnCheckStockEntity.STOCK_NOT_ENOUGH:
                                isSaleOut = false;
                                isSupportAreaSell = true;
                                goodsInfoFragment.ivGoodsSoldOut.setVisibility(View.GONE);
                                tvAreaSupportSell.setVisibility(View.GONE);
                                break;
                            case SnCheckStockEntity.AREA_NO_STOCK:
                                isSaleOut = false;
                                isSupportAreaSell = false;
                                goodsInfoFragment.ivGoodsSoldOut.setVisibility(View.GONE);
                                tvAreaSupportSell.setVisibility(View.VISIBLE);
                                break;
                            case SnCheckStockEntity.NO_STOCK:
                                isSaleOut = true;
                                isSupportAreaSell = true;
                                goodsInfoFragment.ivGoodsSoldOut.setVisibility(View.VISIBLE);
                                tvAreaSupportSell.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                        checkBntStatus();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void supportAreaSell() {
        tvAreaSupportSell.setVisibility(View.GONE);
        isSupportAreaSell = true;
        checkBntStatus();
    }

    private void checkBntStatus() {
        if (!isSaleOut && isSupportAreaSell) {
            btnCanCommit();
        } else {
            btnCanNotCommit(Color.parseColor("#CACACA"), false);
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
        bntAddShoppingCar.setClickable(clickable);
        if (clickable) {
            btnCommit.setBackgroundResource(R.drawable.sn_shape_buy_now);
            btnCommit.setVisibility(View.VISIBLE);
            bntAddShoppingCar.setBackgroundResource(R.drawable.sn_shape_shopping_car);
        } else {
            btnCommit.setVisibility(View.GONE);
            bntAddShoppingCar.setBackgroundColor(color);
        }
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

    @Override
    public void scrollDistance(int distance) {
        tabLayout.setVisibility(View.VISIBLE);
        titleRootView.setBackgroundColor(Color.WHITE);
        Logger.i(TAG + "distance:" + distance);
        if (goodsInfoFragment.includeWebView.getY() > distance) {
//            还没滑动到详情
            tabLayout.setCurrentTab(0);
        } else {
            tabLayout.setCurrentTab(1);
        }
        if (ALPHA_0 >= distance) {
            distance = ALPHA_0;
        }
        if (ALPHA_INT_255 > distance) {
            ivBack.setImageResource(R.mipmap.v3_back_top);
            ivBack.setAlpha((float) (255 - distance) / ALPHA_INT_255);
            ivShare.setImageResource(R.mipmap.icon_sn_share);
            ivShare.setAlpha((float) (255 - distance) / ALPHA_INT_255);
            titleRootView.getBackground().setAlpha(distance);
            tabLayout.setAlpha((float) distance / ALPHA_INT_255);
        } else {
//            icon_sn_share_white
            ivBack.setImageResource(R.mipmap.icon_sn_goods_detail_back_white);
            ivBack.setAlpha((float) ALPHA_FLOAT_1);
            ivShare.setImageResource(R.mipmap.icon_sn_share_white);
            ivShare.setAlpha((float) ALPHA_FLOAT_1);
            titleRootView.getBackground().setAlpha(ALPHA_INT_255);
            tabLayout.setAlpha(ALPHA_FLOAT_1);
        }
    }
}
