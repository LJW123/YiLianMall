package com.yilian.mall.jd.fragment.goodsdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxDeviceTool;
import com.yilian.mall.R;
import com.yilian.mall.jd.activity.JDEditShippingAddressListActivity;
import com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity;
import com.yilian.mall.jd.utils.GlideImageLoader;
import com.yilian.mall.jd.utils.GoodsIntroduceUtil;
import com.yilian.mall.jd.utils.JDDetailAddressUtil;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.jd.view.SlideDetailsLayout;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.FontUtils;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity.JD_GOODS_DETAIL_INFO_ENTITY;
import static com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity.DataBean.GoodsInfoBean.GOODS_SOLD_OUT;
import static com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity.DataBean.GoodsInfoBean.NOT7_TO_RETURN;
import static com.yilian.networkingmodule.entity.jd.JDGoodsDetailInfoEntity.DataBean.GoodsPriceBean.RETURN_NON;

/**
 * item页ViewPager里的商品Fragment
 */
public class JDGoodsInfoLeftFragment extends JPBaseFragment implements View.OnClickListener, SlideDetailsLayout.OnSlideDetailsListener {
    public JDGoodsDetailActivity activity;
    public FrameLayout flContent;
    public LinearLayout llPullUp;
    public TextView tvGoodsName;
    public JDGoodsInfoConfigFragment goodsInfoConfigFragment;
    public JDGoodsDetailInfoWebFragment goodsInfoWebFragment;
    public JDGoodsInfoAfterSaleFragment goodsInfoServiceFragment;
    JDShippingAddressInfoEntity.DataBean shippingAddressInfo;
    private ShippingAddressListener shippingAddressCallback;
    private GoodsSoldOutListener goodsSoldOutListener;
    private SlideDetailsLayout svSwitch;
    private ScrollView svGoodsInfo;
    private FloatingActionButton fabUpSlide;
    private Banner bannerGoodsImg;
    private LinearLayout llGoodsDetail, llGoodsConfig, llGoodsService;
    private TextView tvGoodsDetail, tvGoodsConfig, tvGoodsService;
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
    private TextView tvGavePower, tvCardFlag;
    private TextView tvGoodsWeight;
    private TextView tvExplain;
    private View llExplain;
    private View ivGoodsSoldOut;
    //问题反馈
    private View llFeedback;

    private JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity;

    public static JDGoodsInfoLeftFragment newInstance(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity,
                                                      ShippingAddressListener shippingAddressCallback,
                                                      GoodsSoldOutListener goodsSoldOutListener) {
        JDGoodsInfoLeftFragment goodsInfoFragment = new JDGoodsInfoLeftFragment();
        goodsInfoFragment.setGoodsSoldOutListener(goodsSoldOutListener);
        goodsInfoFragment.setShippingAddressCallback(shippingAddressCallback);
        Bundle bundle = new Bundle();
        bundle.putSerializable(JD_GOODS_DETAIL_INFO_ENTITY, jdGoodsDetailInfoEntity);
        goodsInfoFragment.setArguments(bundle);
        return goodsInfoFragment;
    }

    public void setGoodsSoldOutListener(GoodsSoldOutListener goodsSoldOutListener) {
        this.goodsSoldOutListener = goodsSoldOutListener;
    }

    public void setShippingAddressCallback(ShippingAddressListener shippingAddressCallback) {
        this.shippingAddressCallback = shippingAddressCallback;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jd_fragment_goods_info, null);
        Bundle arguments = getArguments();
        jdGoodsDetailInfoEntity = (JDGoodsDetailInfoEntity) arguments.getSerializable(JD_GOODS_DETAIL_INFO_ENTITY);

        initView(rootView);
        initListener();
        initData(jdGoodsDetailInfoEntity);
        return rootView;
    }

    private void initView(View rootView) {
        ivGoodsSoldOut = rootView.findViewById(R.id.iv_goods_sold_out);
        llExplain = rootView.findViewById(R.id.ll_explain);
        tvExplain = rootView.findViewById(R.id.tv_explain);
        llFeedback = rootView.findViewById(R.id.ll_feedback);
        fabUpSlide = (FloatingActionButton) rootView.findViewById(R.id.fab_up_slide);
        svSwitch = (SlideDetailsLayout) rootView.findViewById(R.id.sv_switch);
        svGoodsInfo = (ScrollView) rootView.findViewById(R.id.sv_goods_info);
        indicatorCurrent = rootView.findViewById(R.id.indicator_current);
        indicatorTotal = rootView.findViewById(R.id.indicator_total);

        bannerGoodsImg = rootView.findViewById(R.id.banner_goods_img);
        ViewGroup.LayoutParams lp = bannerGoodsImg.getLayoutParams();
        lp.width = RxDeviceTool.getScreenWidths(getContext());
        lp.height = RxDeviceTool.getScreenWidths(getContext());
        bannerGoodsImg.setLayoutParams(lp);

        flContent = (FrameLayout) rootView.findViewById(R.id.fl_content);
        addressChoose = rootView.findViewById(R.id.address_choose);
        tvShippingAddress = rootView.findViewById(R.id.tv_receive_address);
        llPullUp = (LinearLayout) rootView.findViewById(R.id.ll_pull_up);

        llGoodsDetail = rootView.findViewById(R.id.ll_goods_detail);
        llGoodsConfig = rootView.findViewById(R.id.ll_goods_config);
        llGoodsService = rootView.findViewById(R.id.ll_goods_service);
        tvGoodsDetail = rootView.findViewById(R.id.tv_goods_detail);
        tvGoodsConfig = rootView.findViewById(R.id.tv_goods_config);
        tvGoodsService = rootView.findViewById(R.id.tv_goods_service);

        tvGoodsName = (TextView) rootView.findViewById(R.id.tv_goods_name);
        tvGoodsPrice = rootView.findViewById(R.id.tv_goods_price);
        tvGoodsPrice.setTypeface(FontUtils.getFontTypeface(mContext, "fonts/RUBIK-REGULAR.TTF"));
        tvGavePower = rootView.findViewById(R.id.tv_gave_power);
        tvGavePower.setVisibility(View.GONE);
        tvCardFlag = rootView.findViewById(R.id.tv_card_flag);
        tvCardFlag.setVisibility(View.GONE);
        tvGoodsWeight = rootView.findViewById(R.id.tv_goods_weight);

        fabUpSlide.hide();
    }

    private void initListener() {
        fabUpSlide.setOnClickListener(this);
        addressChoose.setOnClickListener(this);
        llPullUp.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
        llGoodsDetail.setOnClickListener(this);
        llGoodsConfig.setOnClickListener(this);
        llGoodsService.setOnClickListener(this);
        svSwitch.setOnSlideDetailsListener(this);
    }

    private void initData(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
        initBottomFragment(jdGoodsDetailInfoEntity);
        JDGoodsDetailInfoEntity.DataBean.GoodsInfoBean goodsInfo = jdGoodsDetailInfoEntity.data.goodsInfo;
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
        if (goodsInfo.is7ToReturn == NOT7_TO_RETURN) {
            tvExplain.setText("该商品不支持7天无理由退款");
            llExplain.setVisibility(View.VISIBLE);
        } else {
            llExplain.setVisibility(View.GONE);
        }
        Logger.i("JD 商品详情数据:" + jdGoodsDetailInfoEntity.toString());
        tvGoodsName.setText(goodsInfo.name);
        tvGoodsPrice.setText("¥" + jdGoodsDetailInfoEntity.data.goodsPrice.jdPrice);
        if (jdGoodsDetailInfoEntity.data.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            tvCardFlag.setVisibility(View.VISIBLE);
        } else {
            if (!RETURN_NON.equals(jdGoodsDetailInfoEntity.data.goodsPrice.returnBean)) {
                tvGavePower.setText("预计 " + jdGoodsDetailInfoEntity.data.goodsPrice.returnBean);
                tvGavePower.setVisibility(View.VISIBLE);
            } else {
                tvGavePower.setVisibility(View.GONE);
            }
        }
        shippingAddressInfo = jdGoodsDetailInfoEntity.data.addressInfo;
        showShippingAddress();
        tvGoodsWeight.setText(goodsInfo.weight + "kg");
        List<String> pics = jdGoodsDetailInfoEntity.data.pics;
        List<String> wholePics = new ArrayList<>();
        for (String pic : pics) {
            pic = JDImageUtil.getJDImageUrl_N0(pic);
            Logger.i("wholePic:" + pic);
            wholePics.add(pic);
        }
        setGoodsPics(wholePics);
//        fragmentList = new ArrayList<>();
        tabTextList = new ArrayList<>();
        tabTextList.add(tvGoodsDetail);
        tabTextList.add(tvGoodsConfig);
        tabTextList.add(tvGoodsService);
    }

    /**
     * 加载完商品详情执行
     */
    public void initBottomFragment(JDGoodsDetailInfoEntity jdGoodsDetailInfoEntity) {
        if (jdGoodsDetailInfoEntity == null) {
            return;
        }
        String appIntroduce = jdGoodsDetailInfoEntity.data.goodsInfo.appintroduce;
        String introduce = jdGoodsDetailInfoEntity.data.goodsInfo.introduction;
        goodsInfoWebFragment = JDGoodsDetailInfoWebFragment.newInstance(GoodsIntroduceUtil.getIntroduce(appIntroduce, introduce));
        goodsInfoConfigFragment = JDGoodsInfoConfigFragment.newInstance(jdGoodsDetailInfoEntity.data.goodsInfo.param);
        goodsInfoServiceFragment = JDGoodsInfoAfterSaleFragment.newInstance(jdGoodsDetailInfoEntity.data.goodsInfo.wareQD
                , jdGoodsDetailInfoEntity.data.goodsInfo.shouhou);
//        fragmentList.add(goodsInfoConfigFragent);
//        fragmentList.add(goodsInfoWebFragment);
//        fragmentList.add(goodsInfoServiceFragment);
        nowFragment = goodsInfoWebFragment;
        fragmentManager = getChildFragmentManager();
        //默认显示商品详情tab
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();
    }

    /**
     * 显示地址信息并把地址信息回调给Activity
     */
    private void showShippingAddress() {
        tvShippingAddress.setText(getAddress(shippingAddressInfo));
        shippingAddressCallback.setShippingAddressCallback(shippingAddressInfo);
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

    @NonNull
    private String getAddress(JDShippingAddressInfoEntity.DataBean addressInfo) {
        if (addressInfo == null) {
            return "";
        }
        return JDDetailAddressUtil.getJDShippingDetailAddressStr(
                addressInfo.province, addressInfo.city, addressInfo.county, addressInfo.town, addressInfo.detailAddress);
    }

    /**
     * 编辑地址成功后更新和检查送至地址
     * {@link com.yilian.mall.jd.activity.JDEditShippingAddressActivity#postEvent()}
     */
    @Subscribe
    public void showShoppingAddress(JDShippingAddressInfoEntity entity) {
        shippingAddressInfo = entity.data;
        showShippingAddress();
    }

    /**
     * 删除地址EventBus接收
     *
     * @param dataBean {@link com.yilian.mall.jd.activity.JDEditShippingAddressListActivity#deleteShippingAddress(String)}
     */
    @Subscribe
    public void deleteAddress(JDShippingAddressInfoEntity.DataBean dataBean) {
        if (dataBean == null) {
            return;
        }
        if (shippingAddressInfo != null && dataBean.id.equals(shippingAddressInfo.id)) {
            clearAddressInfo();
        }
    }

    /**
     * 清除地址信息
     */
    void clearAddressInfo() {
        tvShippingAddress.setText("请选择地址");
        shippingAddressCallback.setShippingAddressCallback(null);

    }

    /**
     * 购物车数量更新
     *
     * @param entity
     */
    @Subscribe
    public void updateShoppingCarData(JdShoppingCarEntity entity) {
        activity.updateShoppingCarData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pull_up:
                //上拉查看图文详情
                svSwitch.smoothOpen(true);
                break;
            case R.id.fab_up_slide:
                //点击滑动到顶部
                if (goodsInfoWebFragment != null)
                    goodsInfoWebFragment.scrollToTop();
                svGoodsInfo.smoothScrollTo(0, 0);
                svSwitch.smoothClose(true);
                break;
            case R.id.ll_goods_detail:
                //商品详情tab
                nowIndex = 0;
                scrollCursor();
                switchFragment(nowFragment, goodsInfoWebFragment);
                nowFragment = goodsInfoWebFragment;
                break;
            case R.id.ll_goods_config:
                //规格参数tab
                nowIndex = 1;
                scrollCursor();
                switchFragment(nowFragment, goodsInfoConfigFragment);
                nowFragment = goodsInfoConfigFragment;
                break;
            case R.id.ll_goods_service:
                //包装售后tab
                switchFragment(nowFragment, goodsInfoServiceFragment);
                nowIndex = 2;
                nowFragment = goodsInfoServiceFragment;
                scrollCursor();
                break;
            case R.id.address_choose:
                //收货地址选择
                JumpJdActivityUtils.toJDShippingAddressListActivity(JDGoodsInfoLeftFragment.this);
                break;
            case R.id.ll_feedback:
                //问题反馈
                JumpJdActivityUtils.toJDGoodsFeedback(mContext, jdGoodsDetailInfoEntity.data.goodsInfo.sku, jdGoodsDetailInfoEntity.data.goodsInfo.name, jdGoodsDetailInfoEntity.data.goodsInfo.imagePath);
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
        if (requestCode == JDEditShippingAddressListActivity.GET_JD_ADDRESS
                && resultCode == JDEditShippingAddressListActivity.JD_ADDRESS_RESULT) {
//            选择地址后返回
            shippingAddressInfo = (JDShippingAddressInfoEntity.DataBean) data.getSerializableExtra(JDEditShippingAddressListActivity.TAG);
            showShippingAddress();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (JDGoodsDetailActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
        if (status == SlideDetailsLayout.Status.OPEN) {
            //当前为图文详情页
            fabUpSlide.show();
            activity.vpContent.setNoScroll(true);
            activity.tvTitle.setVisibility(View.VISIBLE);
            activity.pstsTabs.setVisibility(View.GONE);
        } else {
            //当前为商品详情页
            fabUpSlide.hide();
            activity.vpContent.setNoScroll(false);
            activity.tvTitle.setVisibility(View.GONE);
            activity.pstsTabs.setVisibility(View.VISIBLE);
        }
    }

    public interface ShippingAddressListener {
        void setShippingAddressCallback(JDShippingAddressInfoEntity.DataBean dataBean);
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
