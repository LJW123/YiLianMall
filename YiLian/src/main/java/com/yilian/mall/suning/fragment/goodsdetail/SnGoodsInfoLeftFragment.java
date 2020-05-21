package com.yilian.mall.suning.fragment.goodsdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxDeviceTool;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.GlideImageLoader;
import com.yilian.mall.jd.utils.JDDetailAddressUtil;
import com.yilian.mall.suning.activity.goodsdetail.SnGoodsDetailActivity;
import com.yilian.mall.suning.activity.goodsdetail.addressclean.SnDefaultShippingAddressContract;
import com.yilian.mall.suning.activity.goodsdetail.addressclean.SnDefaultShippingAddressPresenterImpl;
import com.yilian.mall.suning.activity.shippingaddress.SnShippingAddressListActivity;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.FontUtils;
import com.yilian.mylibrary.widget.ScrollListenerScrollView;
import com.yilian.networkingmodule.entity.suning.SnFreightEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsDetailInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity.JD_GOODS_DETAIL_INFO_ENTITY;
import static com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity.DataBean.GoodsInfoBean.GOODS_SOLD_OUT;
import static com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity.DataBean.GoodsPriceBean.RETURN_NON;

/**
 * item页ViewPager里的商品Fragment
 */
public class SnGoodsInfoLeftFragment extends JPBaseFragment
        implements View.OnClickListener, SnDefaultShippingAddressContract.IView {
    public SnGoodsDetailActivity activity;
    public FrameLayout flContent;
    public TextView tvGoodsName;
    //    public SnGoodsInfoConfigFragment goodsInfoConfigFragment;
    public SnGoodsDetailInfoWebFragment goodsInfoWebFragment;
    //    private SlideDetailsLayout svSwitch;
    public ScrollListenerScrollView svGoodsInfo;
    public View includeWebView;
    public View ivGoodsSoldOut;
    //    public SnGoodsInfoAfterSaleFragment goodsInfoServiceFragment;
    SnShippingAddressInfoEntity.DataBean mShippingAddressInfo;
    private ShippingAddressListener mShippingAddressCallback;
    private GoodsSoldOutListener goodsSoldOutListener;
    private FloatingActionButton fabUpSlide;
    private Banner bannerGoodsImg;
    private TextView tvGoodsDetail;
    /**
     * 选择地址
     */
    private LinearLayout addressChoose;
    /**
     * 自定义banner指示器
     */
    private TextView indicatorCurrent, indicatorTotal;
    private TextView tvGoodsPrice;
    /**
     * 当前商品详情数据页的索引分别是图文详情、规格参数
     */
    private int nowIndex;
    private Fragment nowFragment;
    private List<TextView> tabTextList;
    //    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private int index = 1;
    private TextView tvShippingAddress;
    private TextView tvGavePower;
    private TextView tvGoodsWeight;
    private TextView tvFreight;
    private View llExplain;
    //问题反馈
    private View llFeedback;
    private SnGoodsDetailInfoEntity mSnGoodsDetailInfoEntity;
    private ScrollDistanceListener mScrollDistanceListener;
    private SnDefaultShippingAddressPresenterImpl snDefaultShippingAddressPresenter;
    private TextView tvGoodsIntroduce;

    public static SnGoodsInfoLeftFragment newInstance(SnGoodsDetailInfoEntity snGoodsDetailInfoEntity,
                                                      ShippingAddressListener shippingAddressCallback,
                                                      GoodsSoldOutListener goodsSoldOutListener) {
        SnGoodsInfoLeftFragment goodsInfoFragment = new SnGoodsInfoLeftFragment();
        goodsInfoFragment.setGoodsSoldOutListener(goodsSoldOutListener);
        goodsInfoFragment.setShippingAddressCallback(shippingAddressCallback);
        Bundle bundle = new Bundle();
        bundle.putSerializable(JD_GOODS_DETAIL_INFO_ENTITY, snGoodsDetailInfoEntity);
        goodsInfoFragment.setArguments(bundle);
        return goodsInfoFragment;
    }

    public void setGoodsSoldOutListener(GoodsSoldOutListener goodsSoldOutListener) {
        this.goodsSoldOutListener = goodsSoldOutListener;
    }

    public void setShippingAddressCallback(ShippingAddressListener shippingAddressCallback) {
        this.mShippingAddressCallback = shippingAddressCallback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SnGoodsDetailActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setDefaultShippingAddress(SnShippingAddressInfoEntity snShippingAddressInfoEntity) {
        showToast("收到地址信息");
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sn_fragment_goods_info, null);
        Bundle arguments = getArguments();
        mSnGoodsDetailInfoEntity = (SnGoodsDetailInfoEntity) arguments.getSerializable(JD_GOODS_DETAIL_INFO_ENTITY);

        initView(rootView);
        initListener();
        initData(mSnGoodsDetailInfoEntity);
        return rootView;
    }

    private void initView(View rootView) {
        tvGoodsIntroduce = rootView.findViewById(R.id.tv_goods_introduce);
        includeWebView = rootView.findViewById(R.id.include_web_view);
        ivGoodsSoldOut = rootView.findViewById(R.id.iv_goods_sold_out);
        llExplain = rootView.findViewById(R.id.ll_explain);
        tvFreight = rootView.findViewById(R.id.tv_freight);
        llFeedback = rootView.findViewById(R.id.ll_feedback);
        fabUpSlide = (FloatingActionButton) rootView.findViewById(R.id.fab_up_slide);
//        svSwitch = (SlideDetailsLayout) rootView.findViewById(R.id.sv_switch);
        svGoodsInfo = (ScrollListenerScrollView) rootView.findViewById(R.id.sv_goods_info);
        indicatorCurrent = rootView.findViewById(R.id.indicator_current);
        indicatorTotal = rootView.findViewById(R.id.indicator_total);

        bannerGoodsImg = rootView.findViewById(R.id.banner_goods_img);
        ViewGroup.LayoutParams lp = bannerGoodsImg.getLayoutParams();
        lp.width = RxDeviceTool.getScreenWidths(getContext());
        lp.height = RxDeviceTool.getScreenWidths(getContext());
        bannerGoodsImg.setLayoutParams(lp);

        flContent = (FrameLayout) rootView.findViewById(R.id.fl_content);
        addressChoose = rootView.findViewById(R.id.address_choose);
        tvShippingAddress = rootView.findViewById(R.id.tv_shipping_address);

        tvGoodsDetail = rootView.findViewById(R.id.tv_goods_detail);

        tvGoodsName = (TextView) rootView.findViewById(R.id.tv_goods_name);
        tvGoodsPrice = rootView.findViewById(R.id.tv_goods_price);
        tvGoodsPrice.setTypeface(FontUtils.getFontTypeface(mContext, "fonts/RUBIK-REGULAR.TTF"));
        tvGavePower = rootView.findViewById(R.id.tv_gave_power);
        tvGoodsWeight = rootView.findViewById(R.id.tv_goods_weight);

        fabUpSlide.hide();
    }

    private void initListener() {
        svGoodsInfo.setOnScrollChanged(new ScrollListenerScrollView.OnScrollChanged() {
            int distance = 0;

            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                Logger.i("distance SnGoodsInfoLeftFragment: l:" + l + "  oldl:" + oldl + "  t:" + t + "  oldt:" + oldt);
                distance += (t - oldt);
                if (mScrollDistanceListener != null) {
                    mScrollDistanceListener.scrollDistance(t);
                }
            }
        });
        fabUpSlide.setOnClickListener(this);
        addressChoose.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
//        svSwitch.setOnSlideDetailsListener(this);
    }

    private void initData(SnGoodsDetailInfoEntity snGoodsDetailInfoEntity) {
        if (isLogin()) {
            getSnDefaultShippingAddress();
        }
        initBottomFragment(snGoodsDetailInfoEntity);
        SnGoodsDetailInfoEntity.InfoBean goodsInfo = snGoodsDetailInfoEntity.info;
        if (goodsInfo == null) {
            return;
        }
        if (goodsInfo.state == GOODS_SOLD_OUT) {
            ivGoodsSoldOut.setVisibility(View.VISIBLE);
            goodsSoldOutListener.soldOutCallback(true);
        } else {
            ivGoodsSoldOut.setVisibility(View.GONE);
            goodsSoldOutListener.soldOutCallback(false);
        }
        if (SnGoodsDetailInfoEntity.SUPPORT_GOODS_RETURN.equals(goodsInfo.returnGoods)) {
            tvGoodsIntroduce.setText("该商品支持7天无理由退款");
        } else {
            tvGoodsIntroduce.setText("该商品不支持7天无理由退款");
        }
        Logger.i("JD 商品详情数据:" + goodsInfo.toString());
        tvGoodsName.setText(goodsInfo.name);
        tvGoodsPrice.setText("¥" + goodsInfo.snPrice);
        if (!RETURN_NON.equals(goodsInfo.returnBean)) {
            tvGavePower.setText("预计 " + goodsInfo.returnBean);
            tvGavePower.setVisibility(View.VISIBLE);
        } else {
            tvGavePower.setVisibility(View.GONE);
        }
//        shippingAddressInfo = jdGoodsDetailInfoEntity.data.addressInfo;
//        showShippingAddress();
        tvGoodsWeight.setText(String.format("%skg", goodsInfo.weight));
        setGoodsPics(snGoodsDetailInfoEntity.info.imgArr);
//        fragmentList = new ArrayList<>();
        tabTextList = new ArrayList<>();
        tabTextList.add(tvGoodsDetail);
    }

    private void getSnDefaultShippingAddress() {
        if (snDefaultShippingAddressPresenter == null) {
            snDefaultShippingAddressPresenter = new SnDefaultShippingAddressPresenterImpl(this);
        }
        snDefaultShippingAddressPresenter.getSnDefaultShippingAddressInfo(mContext);
    }

    /**
     * 加载完商品详情执行
     */
    public void initBottomFragment(SnGoodsDetailInfoEntity snGoodsDetailInfoEntity) {
        if (snGoodsDetailInfoEntity == null) {
            return;
        }
        String appIntroduce = snGoodsDetailInfoEntity.info.introduction;
        goodsInfoWebFragment = SnGoodsDetailInfoWebFragment.newInstance(getIntroduce(appIntroduce));
        nowFragment = goodsInfoWebFragment;
        fragmentManager = getChildFragmentManager();
        //默认显示商品详情tab
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();
    }

    /**
     * 给商品轮播图设置图片路径
     */
    public void setGoodsPics(List<String> imgUrls) {
        if (imgUrls == null) {
            return;
        }
        //图片指示器-当前
        indicatorCurrent.setText("1");
        //图片指示器-总数
        indicatorTotal.setText("/" + imgUrls.size());
        bannerGoodsImg.isAutoPlay(false)
                .setBannerStyle(BannerConfig.NOT_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setImages(imgUrls)
                .start();
        bannerGoodsImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position > imgUrls.size()) {
                    position = 1;
                }
                indicatorCurrent.setText(String.valueOf(position));
                Logger.i("banner position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public String getIntroduce(String appintroduce) {
        if (!TextUtils.isEmpty(appintroduce)) {
            if (!TextUtils.isEmpty(appintroduce.trim())) {
                return appintroduce.trim();
            }
        }
        return "暂无详情";
    }

    @Subscribe
    public void shippingAddressChanged(SnShippingAddressInfoEntity.DataBean shippingAddressInfo) {
        if (mShippingAddressInfo.id.equals(shippingAddressInfo.id)) {
            mShippingAddressInfo = null;
            clearAddressInfo();

        }
    }

    /**
     * 清除地址信息
     */
    void clearAddressInfo() {
        tvShippingAddress.setText(R.string.add_address);
        mShippingAddressCallback.setShippingAddressCallback(null);

    }

    public void setOnScrollDistance(ScrollDistanceListener scrollDistanceListener) {

        this.mScrollDistanceListener = scrollDistanceListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pull_up:
                //上拉查看图文详情
//                svSwitch.smoothOpen(true);
                break;
            case R.id.fab_up_slide:
                //点击滑动到顶部
                svGoodsInfo.smoothScrollTo(0, 0);
//                svSwitch.smoothClose(true);
                break;
            case R.id.ll_goods_detail:
                //商品详情tab
                nowIndex = 0;
                scrollCursor();
                switchFragment(nowFragment, goodsInfoWebFragment);
                nowFragment = goodsInfoWebFragment;
                break;
//            case R.id.ll_goods_config:
//                //规格参数tab
//                nowIndex = 1;
//                scrollCursor();
//                switchFragment(nowFragment, goodsInfoConfigFragment);
//                nowFragment = goodsInfoConfigFragment;
//                break;
//            case R.id.ll_goods_service:
//                //包装售后tab
//                switchFragment(nowFragment, goodsInfoServiceFragment);
//                nowIndex = 2;
//                nowFragment = goodsInfoServiceFragment;
//                scrollCursor();
//                break;
            case R.id.address_choose:
                //收货地址选择
                if (mShippingAddressInfo != null && mShippingAddressInfo.id != null) {
                    JumpSnActivityUtils.toShippingAddressListActivitiy(this, mShippingAddressInfo.id);
                } else {
                    JumpSnActivityUtils.toShippingAddressListActivitiy(this);
                }
                break;
            case R.id.ll_feedback:
                //问题反馈
                JumpSnActivityUtils.toSnGoodsFeedback(mContext, mSnGoodsDetailInfoEntity.info.skuId, mSnGoodsDetailInfoEntity.info.name,
                        mSnGoodsDetailInfoEntity.info.image);
//                JumpJdActivityUtils.toJDGoodsFeedback(mContext, jdGoodsDetailInfoEntity.data.goodsInfo.sku, jdGoodsDetailInfoEntity.data.goodsInfo.name, jdGoodsDetailInfoEntity.data.goodsInfo.imagePath);
                break;
            default:
                break;
        }
    }

    /**
     * 滑动游标
     */
    private void scrollCursor() {
        //设置Tab切换颜色
        for (int i = 0; i < tabTextList.size(); i++) {
            tabTextList.get(i).setTextColor(i == nowIndex ? Color.parseColor("#ec0f38") : Color.parseColor("#222222"));
        }
    }

    /**
     * 切换Fragment
     * <p>(hide、show、add)
     *
     * @param fromFragment
     * @param toFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if (nowFragment != toFragment) {
            fragmentTransaction = fragmentManager.beginTransaction();
            if (!toFragment.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fromFragment).add(R.id.fl_content, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到activity中
            } else {
                fragmentTransaction.hide(fromFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SnShippingAddressListActivity.REQUEST_CODE
                && resultCode == SnShippingAddressListActivity.RESULT_CODE) {
//            选择地址后返回
            if (data != null) {
                mShippingAddressInfo = (SnShippingAddressInfoEntity.DataBean) data.getSerializableExtra(SnShippingAddressListActivity.TAG_CHOOSE_SHIPPING_ADDRESS);
                showShippingAddress();
                getFreight();
            } else {
                getSnDefaultShippingAddress();
            }
        }
    }

    /**
     * 显示地址信息并把地址信息回调给Activity
     */
    private void showShippingAddress() {
        tvShippingAddress.setText(getAddress(mShippingAddressInfo));
        mShippingAddressCallback.setShippingAddressCallback(mShippingAddressInfo);
    }

    /**
     * 获取运费
     */
    @SuppressWarnings("unchecked")
    private void getFreight() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnFreight("suning_goods/suning_goods_freight", mSnGoodsDetailInfoEntity.info.skuId + ":1",
                        mShippingAddressInfo.address, mShippingAddressInfo.cityId, mShippingAddressInfo.countyId)
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
                        tvFreight.setText(snFreightEntity.freight);
                    }
                });
        addSubscription(subscription);
    }

    private String getAddress(@Nullable SnShippingAddressInfoEntity.DataBean addressInfo) {
        if (addressInfo == null) {
            return "";
        }
        return JDDetailAddressUtil.getJDShippingDetailAddressStr(
                addressInfo.province, addressInfo.city, addressInfo.county, addressInfo.town, addressInfo.address);
    }

    @Override
    public void getSnDefaultShippingAddressInfoSuccess(SnShippingAddressInfoEntity snShippingAddressInfoEntity) {
        List<SnShippingAddressInfoEntity.DataBean> data = snShippingAddressInfoEntity.data;
        if (data != null && data.size() > 0) {
            mShippingAddressInfo = data.get(0);
            tvShippingAddress.setText(String.format("%s%s%s", mShippingAddressInfo.province, mShippingAddressInfo.city, mShippingAddressInfo.county));
            showShippingAddress();
            getFreight();
        }
    }


    public interface ScrollDistanceListener {
        void scrollDistance(int distance);
    }

    public interface ShippingAddressListener {
        void setShippingAddressCallback(SnShippingAddressInfoEntity.DataBean dataBean);
    }

    /**
     * 商品下架监听
     */
    public interface GoodsSoldOutListener {
        /**
         * 是否下架
         *
         * @param soldOut trur:已下架  false:未下架
         */
        void soldOutCallback(boolean soldOut);
    }
}
