package com.yilian.mall.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mall.ui.JPFlagshipActivity;
import com.yilian.mall.ui.JPLeFenHomeActivity;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.MallMainActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.CustScrollView;
import com.yilian.mall.widgets.MyLoading;
import com.yilian.mall.widgets.ScreenNumView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.GoodsSkuPrice;
import com.yilian.networkingmodule.entity.GoodsSkuProperty;
import com.yilian.networkingmodule.entity.JPCommodityDetail;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mall.ui.JPNewCommDetailActivity.JFG_GOODS;
import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS;
import static com.yilian.mall.ui.JPNewCommDetailActivity.LE_DOU_GOODS_2;
import static com.yilian.mall.ui.JPNewCommDetailActivity.YHS_GOODS;


/**
 * Created by Ray_L_Pain on 2016/11/1 0001.
 */

public class JPCommDetailTopFragment extends Fragment {

    private static final int ROTATE = 0;//viewpager轮播消息
    public CustScrollView customScrollView;
    public TextView tv_container_tag;
    public TextView tv_container_price;
    public TextView tv_container_marker;
    public TextView tv_container_juan;
    public TextView tv_container_ledou;
    public LinearLayout ll_container_choose;
    public TextView tv_container_choose;
    public String goodsId, filialeId, supplierId, tagsName;
    public com.yilian.networkingmodule.entity.JPCommodityDetail.DataBean.GoodInfoBean goodInfo;
    public String urlOne;
    public DisplayImageOptions options;
    public boolean isCollectTop;
    public Map<String, GoodsSkuProperty> mSelectedProperty;
    public Map<String, GoodsSkuPrice> mSelectedPropertyPrice;
    public String collectType;
    public String sku, skuCount;
    public List<String> selectSkuList;
    public String getedId;
    public TextView tv_container_jifen;
    public TextView tv_container_juan_jfg;
    public TextView tv_container_question_jfg;
    //判断是哪个专区的标识
    public String regional_type;
    //运费
    public String freight_change;
    //运费说明的弹窗
    public PopupWindow mFregihtChargeWindow;
    /**
     * 之前这个价格为服务器返回的最外层的数据，现在改为先循环遍历对应的sku，再取这个sku的价格，奖券等属性
     */
    public float skuGavePowerFirst ;//初始化，对应该SKU的平台加赠益豆
    public String skuCostPriceFirst = "";//初始化，对应该SKU的价格
    public String skuReturnIntegralFirst = "";//初始化，对应的销售奖券
    public String skuReturnBeanFirst = "";//初始化，对应的区块益豆
    public String skuMarketPriceFirst = "";//初始化，对应该SKU的价格
    public String skuIntegralFirst = "";//初始化，对应的SKU奖券
    public String skuRetailPriceFirst = ""; //初始化，对应的市场价（type = 2 时 现金价格）
    protected int screenWidth;
    protected MyLoading myloading;
    AlbumAdapter albumAdapter;
    private View rootView;
    private JPNewCommDetailActivity activity;
    private Context context;
    private TextView tv_container_title;
    private RatingBar ratingBar;
    private View view_container_choose;
    private TextView tv_container_grade;
    private TextView tv_container_sale;
    private LinearLayout ll_container_marker;
    private TextView tv_container_question;
    private TextView tv_container_assure1;
    private TextView tv_container_assure2;
    private TextView tv_container_assure3;
    private TextView tv_container_assure4;
    private TextView tv_product_name;
    private TextView tv_product_from;
    private TextView tv_product_desc;
    private LinearLayout ll_product_know;
    private TextView tv_product_know;
    private LinearLayout ll_shop;
    private TextView tv_shop_name;
    //轮播图
    private ViewPager viewPager;
    private ScreenNumView llDot;
    private JPNetRequest jpNetRequest;
    private List<String> mAlbum;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private int width;
    private Intent intent;
    private JPCommDetailTopFragment.onUpLoadErrorListener onUpLoadErrorListener;
    //---------------------------------------------------
    private TextView tv_jifen;
    private TextView tv_go_yhs;
    private ImageView iv_classify;
    private LinearLayout ll_jfg;
    private TextView tvGavePower;
    private View rlGavePower;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_commdetail_top, null);
        activity = (JPNewCommDetailActivity) getActivity();
        context = getContext();

        initView();
        getCommodityDetails();
        return rootView;
    }

    public void initView() {
        Logger.i("ray-" + "走了top的initView");
        if (activity.layoutNormal.getVisibility() == View.GONE) {
            activity.layoutNormal.setVisibility(View.VISIBLE);
        }
        if (activity.layoutQuestion.getVisibility() == View.VISIBLE) {
            activity.layoutQuestion.setVisibility(View.GONE);
        }
        screenWidth = ScreenUtils.getScreenWidth(context);
        customScrollView = (CustScrollView) rootView.findViewById(R.id.customScrollView);
        customScrollView.scrollTo(0, 1000);
        tv_container_tag = (TextView) rootView.findViewById(R.id.tv_container_tag);
        tv_container_title = (TextView) rootView.findViewById(R.id.tv_container_title);
        tv_container_price = (TextView) rootView.findViewById(R.id.tv_container_price);
        ll_container_marker = (LinearLayout) rootView.findViewById(R.id.ll_container_marker);
        tv_container_marker = (TextView) rootView.findViewById(R.id.tv_container_marker);
        tv_container_juan = (TextView) rootView.findViewById(R.id.tv_container_juan);
        tv_container_ledou = rootView.findViewById(R.id.tv_container_ledou);
        tv_container_question = (TextView) rootView.findViewById(R.id.tv_container_question);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        tv_container_grade = (TextView) rootView.findViewById(R.id.tv_container_grade);
        tv_container_sale = (TextView) rootView.findViewById(R.id.tv_container_sale);
        tv_container_assure1 = (TextView) rootView.findViewById(R.id.tv_container_assure1);
        tv_container_assure2 = (TextView) rootView.findViewById(R.id.tv_container_assure2);
        tv_container_assure3 = (TextView) rootView.findViewById(R.id.tv_container_assure3);
        tv_container_assure4 = (TextView) rootView.findViewById(R.id.tv_container_assure4);
        view_container_choose = rootView.findViewById(R.id.view_container_choose);
        ll_container_choose = (LinearLayout) rootView.findViewById(R.id.ll_container_choose);
        tv_container_choose = (TextView) rootView.findViewById(R.id.tv_container_choose);
        tv_product_name = (TextView) rootView.findViewById(R.id.tv_product_name);
        tv_product_from = (TextView) rootView.findViewById(R.id.tv_product_from);
        tv_product_desc = (TextView) rootView.findViewById(R.id.tv_product_desc);
        ll_product_know = (LinearLayout) rootView.findViewById(R.id.ll_product_know);
        tv_product_know = (TextView) rootView.findViewById(R.id.tv_product_know);
        ll_shop = (LinearLayout) rootView.findViewById(R.id.ll_shop);
        tv_shop_name = (TextView) rootView.findViewById(R.id.tv_shop_name);
        //轮播图
        viewPager = (ViewPager) rootView.findViewById(R.id.vp_comm_img);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new ViewPager.LayoutParams());
        params.height = (int) (screenWidth / 1);
        viewPager.setLayoutParams(params);
        rlGavePower = rootView.findViewById(R.id.rl_gave_power);
        tvGavePower = rootView.findViewById(R.id.tv_gave_power);
        llDot = (ScreenNumView) rootView.findViewById(R.id.vp_indicator);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                llDot.snapToPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        width = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();// 获取屏幕宽度
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
                .showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        mSelectedProperty = new HashMap<>();
        mSelectedPropertyPrice = new HashMap<>();
        tv_jifen = (TextView) rootView.findViewById(R.id.tv_jifen);
        tv_go_yhs = (TextView) rootView.findViewById(R.id.tv_go_yhs);
        tv_container_jifen = (TextView) rootView.findViewById(R.id.tv_container_jifen);
        iv_classify = (ImageView) rootView.findViewById(R.id.iv_classify);
        ll_jfg = (LinearLayout) rootView.findViewById(R.id.ll_jfg);
        tv_container_juan_jfg = (TextView) rootView.findViewById(R.id.tv_container_juan_jfg);
        tv_container_question_jfg = (TextView) rootView.findViewById(R.id.tv_container_question_jfg);
    }

    /**
     * 请求商品详情信息
     */
    public void getCommodityDetails() {
        goodsId = activity.goodsId;
        filialeId = activity.filialeId;
        //tagsName = activity.tagsName;
        regional_type = activity.type;

        if (TextUtils.isEmpty(filialeId)) {
            filialeId = "0";
        }

        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(context);
        }
        startMyDialog();

        Logger.i("商品详细  filialeId  " + filialeId + "  goodsId  " + goodsId);
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).getV3MallGoodInfo("goods/mallgood_info_v3",
                filialeId, goodsId, regional_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JPCommodityDetail>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();
                        if (null != onUpLoadErrorListener) {
                            onUpLoadErrorListener.isError(true);
                        }
                    }

                    @Override
                    public void onNext(JPCommodityDetail o) {
                        if (null != onUpLoadErrorListener) {
                            onUpLoadErrorListener.isError(false);
                        }
                        goodInfo = o.data.goodInfo;
                        initData();
                    }
                });
        if (activity != null) {
            activity.addSubscription(subscription);
        }
    }

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (isAdded() && getActivity() != null) {
            if (myloading == null) {
                myloading = MyLoading.createLoadingDialog(activity);
                Logger.i(this.getClass().getName() + "  创建了了dialog  " + this.toString());
            }
            try {//捕获异常，处理 is your activity running的异常
                myloading.show();
                ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20000);
                            if (myloading != null && myloading.isShowing()) {
                                ((Activity) getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopMyDialog();
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
            } catch (Exception e) {
                Logger.i("异常信息：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (myloading != null) {
                            if (null != getActivity()) {
                                if (!getActivity().isFinishing()) {
                                    myloading.dismiss();
                                    Logger.i(this.getClass().getName() + "  取消了dialog  " + this.toString());
                                }
                            }
                            myloading = null;
                        }
                    }
                });
            }
        });
    }

    /**
     * 加载数据
     */
    public void initData() {
        regional_type = goodInfo.type;

        getedId = goodInfo.goodsId;
        //tag
        tagsName = goodInfo.tagsMsg;
        float subsidy = goodInfo.subsidy;
        if (subsidy == 0) {
            rlGavePower.setVisibility(View.GONE);
        } else {
            rlGavePower.setVisibility(View.VISIBLE);
            tvGavePower.setText("平台加赠" + MoneyUtil.getLeXiangBiNoZero(subsidy));
        }

        if (TextUtils.isEmpty(tagsName) || tagsName == null) {
            tv_container_tag.setVisibility(View.GONE);
            //标题
            tv_container_title.setText(goodInfo.goodsName);
        } else {
            tv_container_tag.setVisibility(View.VISIBLE);
            tv_container_tag.setText(tagsName);
            //标题
            int length = tagsName.length();
            String space = "         ";
            for (int i = 0; i < length; i++) {
                space = space + " ";
            }
            tv_container_title.setText(space + goodInfo.goodsName);
        }

        /**
         *初始化先循环对应的sku
         */
        for (GoodsSkuPrice skuPrice : goodInfo.goodsSkuPrice) {
            if (skuPrice.skuInfo.equals(goodInfo.goodsSku)) {
                skuCostPriceFirst = skuPrice.skuCostPrice;
                skuGavePowerFirst = skuPrice.subsidy;
                skuReturnIntegralFirst = skuPrice.returnIntegral;
                skuReturnBeanFirst = skuPrice.returnBean;
                skuMarketPriceFirst = skuPrice.skuMarketPrice;
                skuIntegralFirst = skuPrice.skuIntegralPrice;
                skuRetailPriceFirst = skuPrice.skuRetailPrice;
                skuCount = skuPrice.skuInventory;
            }
        }

        if (TextUtils.isEmpty(skuCostPriceFirst)) {
            skuCostPriceFirst = goodInfo.goodsCost;
        }

        if (TextUtils.isEmpty(skuMarketPriceFirst)) {
            skuMarketPriceFirst = goodInfo.goodsPrice;
        }

        if (TextUtils.isEmpty(skuReturnBeanFirst)) {
            skuReturnBeanFirst = goodInfo.returnBean;
        }

        //先判断是线上商城还是易划算专区 假设0是线上商城 1为易划算 2为奖券购
        if (YHS_GOODS.equals(regional_type)) {
            iv_classify.setImageResource(R.mipmap.icon_yhs);
            //只有商品详情请价格显示后边2位00
            tv_container_price.setText(String.format("%.2f", NumberFormat.convertToDouble(skuIntegralFirst, 0.0) / 100));
            tv_container_price.setTextColor(getResources().getColor(R.color.color_green));
            tv_jifen.setVisibility(View.VISIBLE);
            tv_container_juan.setVisibility(View.GONE);
            tv_container_ledou.setVisibility(View.GONE);
            tv_container_question.setVisibility(View.VISIBLE);
            tv_container_question.setText(activity.getResources().getString(R.string.yhs) + "交易说明？");
            tv_container_question.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question.getPaint().setAntiAlias(true);
            tv_go_yhs.setText(R.string.yhs);
            tv_go_yhs.setTextColor(activity.getResources().getColor(R.color.color_green));
            ll_jfg.setVisibility(View.GONE);
        } else if (JFG_GOODS.equals(regional_type)) {
            iv_classify.setImageResource(R.mipmap.icon_jfg);
            int jifen = Integer.parseInt(skuMarketPriceFirst) - Integer.parseInt(skuRetailPriceFirst);
            tv_container_price.setText(MoneyUtil.set¥Money(String.format("%.2f", NumberFormat.convertToDouble(skuRetailPriceFirst, 0.0) / 100)));
            ll_container_marker.setVisibility(View.GONE);
            tv_container_jifen.setVisibility(View.VISIBLE);
            tv_container_jifen.setText(" + " + String.format("%.2f", NumberFormat.convertToDouble(jifen, 0.0) / 100));
            tv_jifen.setVisibility(View.VISIBLE);
            ll_jfg.setVisibility(View.VISIBLE);
            tv_container_juan_jfg.setText("原价：¥" + MoneyUtil.getLeXiangBi(skuMarketPriceFirst));
            tv_container_juan_jfg.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tv_container_juan.setVisibility(View.GONE);
            tv_container_ledou.setVisibility(View.GONE);
            tv_container_question_jfg.setText(activity.getResources().getString(R.string.jfg) + "交易说明？");
            tv_container_question_jfg.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question_jfg.getPaint().setAntiAlias(true);
            tv_go_yhs.setText(activity.getResources().getString(R.string.jfg));
            tv_go_yhs.setTextColor(activity.getResources().getColor(R.color.color_red));
        } else if (LE_DOU_GOODS.equals(regional_type)
                || GoodsType.CAL_POWER.equals(regional_type)) {
            iv_classify.setVisibility(View.GONE);
            tv_container_price.setText(MoneyUtil.set¥Money(String.format("%.2f", NumberFormat.convertToDouble(skuCostPriceFirst, 0.0) / 100)));
            ll_container_marker.setVisibility(View.VISIBLE);
            tv_jifen.setVisibility(View.GONE);
            tv_container_marker.setText("¥" + MoneyUtil.getLeXiangBi(skuMarketPriceFirst));
            tv_container_marker.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            if (TextUtils.isEmpty(skuMarketPriceFirst) && TextUtils.isEmpty(skuCostPriceFirst)) {
                Logger.e("商品详情价格异常 goodsPrice " + skuMarketPriceFirst + " goodInfo.goodsCost " + skuCostPriceFirst);
                return;
            }
            tv_container_juan.setVisibility(View.GONE);
            tv_container_ledou.setVisibility(View.VISIBLE);
            tv_container_ledou.setText("送  " + MoneyUtil.getLeXiangBiNoZero(skuReturnBeanFirst));
            tv_container_question.setVisibility(View.VISIBLE);
            tv_container_question.setText(Constants.APP_PLATFORM_DONATE_NAME+"说明？");
            tv_container_question.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question.getPaint().setAntiAlias(true);
            tv_go_yhs.setText("");
            ll_jfg.setVisibility(View.GONE);
        } else if (LE_DOU_GOODS_2.equals(regional_type)) {
            iv_classify.setVisibility(View.GONE);
            tv_container_price.setText(MoneyUtil.set¥Money(String.format("%.2f", NumberFormat.convertToDouble(skuCostPriceFirst, 0.0) / 100)));
            ll_container_marker.setVisibility(View.GONE);
            tv_jifen.setVisibility(View.GONE);
            tv_container_marker.setText("¥" + MoneyUtil.getLeXiangBi(skuMarketPriceFirst));
            tv_container_marker.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            if (TextUtils.isEmpty(skuMarketPriceFirst) && TextUtils.isEmpty(skuCostPriceFirst)) {
                Logger.e("商品详情价格异常 goodsPrice " + skuMarketPriceFirst + " goodInfo.goodsCost " + skuCostPriceFirst);
                return;
            }
            tv_container_juan.setVisibility(View.GONE);
            tv_container_ledou.setVisibility(View.VISIBLE);
            tv_container_ledou.setText("送  " + MoneyUtil.getLeXiangBiNoZero(skuReturnBeanFirst));
            tv_container_question.setVisibility(View.VISIBLE);
            tv_container_question.setText(Constants.APP_PLATFORM_DONATE_NAME+"说明？");
            tv_container_question.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question.getPaint().setAntiAlias(true);
            tv_go_yhs.setText("");
            ll_jfg.setVisibility(View.GONE);
        } else {
            iv_classify.setVisibility(View.GONE);
            tv_container_price.setText(MoneyUtil.set¥Money(String.format("%.2f", NumberFormat.convertToDouble(skuCostPriceFirst, 0.0) / 100)));
            ll_container_marker.setVisibility(View.VISIBLE);
            tv_jifen.setVisibility(View.GONE);
            tv_container_marker.setText("¥" + MoneyUtil.getLeXiangBi(skuMarketPriceFirst));
            tv_container_marker.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            if (TextUtils.isEmpty(skuMarketPriceFirst) && TextUtils.isEmpty(skuCostPriceFirst)) {
                Logger.e("商品详情价格异常 goodsPrice " + skuMarketPriceFirst + " goodInfo.goodsCost " + skuCostPriceFirst);
                return;
            }
            tv_container_juan.setText(activity.getResources().getString(R.string.back_score) + MoneyUtil.getLeXiangBiNoZero(skuReturnIntegralFirst));
            tv_container_juan.setVisibility(View.VISIBLE);
            tv_container_ledou.setVisibility(View.GONE);
            tv_container_question.setVisibility(View.VISIBLE);
            tv_container_question.setText(activity.getResources().getString(R.string.xiaofeijifen) + "说明？");
            tv_container_question.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_container_question.getPaint().setAntiAlias(true);
            tv_go_yhs.setText("");
            ll_jfg.setVisibility(View.GONE);
        }

        tv_container_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                if ("0".equals(regional_type)) {
                    intent.putExtra(com.yilian.mylibrary.Constants.SPKEY_URL, com.yilian.mylibrary.Constants.INTEGRAL_AGREEMENT);
                } else if ("1".equals(regional_type)) {
                    intent.putExtra(com.yilian.mylibrary.Constants.SPKEY_URL, Constants.YHS_AGREEMENT);
                } else if (LE_DOU_GOODS.equals(regional_type) || LE_DOU_GOODS_2.equals(regional_type)
                        || GoodsType.CAL_POWER.equals(regional_type)) {
                    intent.putExtra(com.yilian.mylibrary.Constants.SPKEY_URL, Constants.LEDOU_AGREEMENT);
                }
                startActivity(intent);
            }
        });

        tv_container_question_jfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra(com.yilian.mylibrary.Constants.SPKEY_URL, Constants.JFG_AGREEMENT);
                startActivity(intent);
            }
        });

        collectType = "0";//商品传“0”
        //评分
        float grade = (float) (Math.round((goodInfo.goodsGrade / 10.0f) * 10)) / 10;
        ratingBar.setRating(grade == 0 ? (float) 5.0 : grade);
        tv_container_grade.setText(grade == 0 ? "5.0分" : grade + "分");
        //销量
        tv_container_sale.setText("累计销量：" + goodInfo.goodsSale);
        //四个标签
        List<String> listTag = goodInfo.descTags;
        if (listTag.size() == 4) {
            tv_container_assure1.setVisibility(View.VISIBLE);
            tv_container_assure2.setVisibility(View.VISIBLE);
            tv_container_assure3.setVisibility(View.VISIBLE);
            tv_container_assure4.setVisibility(View.VISIBLE);
            tv_container_assure1.setText(listTag.get(0));
            tv_container_assure2.setText(listTag.get(1));
            tv_container_assure3.setText(listTag.get(2));
            tv_container_assure4.setText(listTag.get(3));
        } else {
            tv_container_assure1.setVisibility(View.GONE);
            tv_container_assure2.setVisibility(View.GONE);
            tv_container_assure3.setVisibility(View.GONE);
            tv_container_assure4.setVisibility(View.GONE);
        }
        //商品规格
        sku = goodInfo.goodsSku;
        String[] arraySku = sku.split(",");
        selectSkuList = new ArrayList<>(Arrays.asList(arraySku));

        ll_container_choose.setVisibility(View.VISIBLE);
        tv_container_choose.setVisibility(View.VISIBLE);
        if ("0:0".equals(sku) || TextUtils.isEmpty(sku)) {
            /**
             * Ray_L_Pain 2017-2-9 需求变为有规格的的产品也显示选择的数量
             */
            tv_container_choose.setText(noskuPropertyTxt(1));
        } else {
            tv_container_choose.setText(staticPropertyTxt(selectSkuList));
        }
        ll_container_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.mOperate = JPNewCommDetailActivity.OPERATE_BUY;
                activity.showPropertyWindow();
            }
        });
        //商品信息
        com.yilian.networkingmodule.entity.JPCommodityDetail.DataBean.GoodInfoBean.SupplierInfoBean suppBean = goodInfo.supplierInfo;
        //品牌
        tv_product_name.setText(goodInfo.goodsBrand);
        tv_product_from.setText(suppBean.supplierAddress);
        freight_change = suppBean.goodsFreightDesc;
        if (TextUtils.isEmpty(freight_change)) {
            tv_product_desc.setVisibility(View.INVISIBLE);
        } else if (freight_change.contains("\n")) {
            try {
                String freightStr = freight_change.split("\n")[0];
                tv_product_desc.setText(freightStr);
            } catch (Exception e) {
                tv_product_desc.setVisibility(View.INVISIBLE);
            }
        } else {
            tv_product_desc.setText(freight_change);
        }
        if (TextUtils.isEmpty(freight_change)) {
            tv_product_desc.setClickable(false);
        } else {
            tv_product_desc.setClickable(true);
            tv_product_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFreightChargeWindow();
                }
            });
        }
        tv_shop_name.setText("店铺：" + suppBean.supplierName);
        ll_product_know.setVisibility(View.GONE);
        ll_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (LE_DOU_GOODS.equals(regional_type) || LE_DOU_GOODS_2.equals(regional_type)) {
//                    startActivity(new Intent(activity, LeDouHomePageViewImplActivity.class));
//                } else {
                if (Integer.parseInt(filialeId) > 1) {
                    intent = new Intent(activity, JPLeFenHomeActivity.class);
                    intent.putExtra("index_id", filialeId);
                    startActivity(intent);
                } else {
                    switch (regional_type) {
                        case "0":
                            intent = new Intent(activity, JPFlagshipActivity.class);
                            intent.putExtra("index_id", goodInfo.goodsSupplier);
                            break;
                        case "1":
                        case "2":
                            intent = new Intent(activity, MallMainActivity.class);
                            intent.putExtra("title", ("1".equals(regional_type)) ? "易划算" : "奖券购");
                            break;
                        case LE_DOU_GOODS:
                        case LE_DOU_GOODS_2:
                        case GoodsType.CAL_POWER:
                            intent = new Intent(activity, JPFlagshipActivity.class);
                            intent.putExtra("index_id", goodInfo.goodsSupplier);
                            break;
                        default:
                            break;
                    }
                    startActivity(intent);
                }
            }
//            }
        });
        //网址
        urlOne = Ip.getWechatURL(activity) + "m/goods/contentPic.php?goods_id=" + goodInfo.goodsId + "&filiale_id=" + filialeId + "&goods_supplier=" + goodInfo.goodsSupplier;
        Logger.i("url==" + urlOne);
        activity.urlOne = urlOne;
        //收藏
        if (goodInfo.yetCollect == 0) {
            isCollectTop = false;
        } else if (goodInfo.yetCollect == 1) {
            isCollectTop = true;
        }
        if (isCollectTop) {
            Drawable topDrawable = activity.getResources().getDrawable(R.mipmap.star_solid);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            activity.tvCollect.setCompoundDrawables(null, topDrawable, null, null);
            activity.tvCollect.setText(R.string.collected);
        } else {
            Drawable topDrawable = activity.getResources().getDrawable(R.mipmap.star_empty);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            activity.tvCollect.setCompoundDrawables(null, topDrawable, null, null);
            activity.tvCollect.setText(R.string.click_collect);
        }
        //轮播图
        mAlbum = goodInfo.goodsAlbum;
        albumAdapter = new AlbumAdapter();
        viewPager.setAdapter(albumAdapter);
        llDot.initScreen(albumAdapter.getCount());
    }

    public String noskuPropertyTxt(int count) {
        return "已选择 " + count + " " + goodInfo.goodsUnit;
    }

    public String staticPropertyTxt(List<String> selectSkuList) {
        String selectedValues = "";
        String selectedId = "";

        if (selectSkuList != null && selectSkuList.size() != 0 && selectSkuList != null) {
            for (int i = 0; i < selectSkuList.size(); i++) {
                String value = selectSkuList.get(i);

                for (GoodsSkuProperty property : goodInfo.goodsSkuValues) {
                    if (null != property.skuParentId && null != property.skuId) {
                        String item = property.skuParentId + ":" + property.skuId;
                        if (item.equals(value)) {
                            selectedValues += property.skuName + ", ";
                            selectedId += property.skuParentId + ":" + property.skuId + "，";
                        }
                    }
                }
            }
            Logger.i("goodsSkuValues:" + goodInfo.goodsSkuValues);
            Logger.i("selectedValues:" + selectedValues);
            Logger.i("selectedId:" + selectedId);
            return "已选择 " + selectedValues + "1" + goodInfo.goodsUnit;
        }
        return null;
    }

    /**
     * 运费说明popwindow
     */
    public void showFreightChargeWindow() {
        if (mFregihtChargeWindow == null) {
            View contentView = activity.getLayoutInflater().inflate(R.layout.good_freight_charge_layout, null);
            mFregihtChargeWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mFregihtChargeWindow.setContentView(contentView);
            mFregihtChargeWindow.setAnimationStyle(R.style.AnimationSEX);
            mFregihtChargeWindow.setOutsideTouchable(true);
            View view_placeholder = contentView.findViewById(R.id.view_placeholder);
            view_placeholder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFregihtChargeWindow.dismiss();
                    activity.mShadowView.setVisibility(View.GONE);
                    activity.mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_out));
                    return;
                }
            });
            TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            tv_content.setText(freight_change);
            TextView tv_ok = (TextView) contentView.findViewById(R.id.tv_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFregihtChargeWindow.dismiss();
                    activity.mShadowView.setVisibility(View.GONE);
                    activity.mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_out));
                }
            });
        }

        if (!mFregihtChargeWindow.isShowing()) {
            mFregihtChargeWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            activity.mShadowView.setVisibility(View.VISIBLE);
            activity.mShadowView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.library_module_anim_alpha_in));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myloading != null) {
            if (myloading.isShowing()) {
                myloading.dismiss();
                myloading = null;
            }
        }
    }

    public void showToast(String msg) {
        Logger.i("返回码：" + msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取商品属性选择结果字串
     */
    public String getPropertyTxt(int count) {
        String selectedValues = "";

        if (goodInfo != null) {
            int selectedPropertyCount = mSelectedProperty.size(); //我的选择
            Logger.i("2016-12-15mSelectedProperty:" + mSelectedProperty);
            int propertyCount = goodInfo.goodsSkuProperty.size(); //商品选择--2
            if (propertyCount > 0) {
                if (selectedPropertyCount == propertyCount) {
                    for (GoodsSkuProperty property : goodInfo.goodsSkuProperty) {
                        selectedValues += mSelectedProperty.get(property.skuId).skuName + " ";
                    }
                    return "已选择 " + selectedValues + count + goodInfo.goodsUnit;
                } else if (selectedPropertyCount < propertyCount) {
                    for (GoodsSkuProperty property : goodInfo.goodsSkuProperty) {
                        if (null == mSelectedProperty.get(property.skuId)) {
                            selectedValues += property.skuName + " ";
                        }
                    }
                    return "请选择 " + selectedValues + count + goodInfo.goodsUnit;
                }
            }
        }
        return null;
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    public void setOnUpLoadErorrListener(onUpLoadErrorListener onUpLoadErorrListener) {
        this.onUpLoadErrorListener = onUpLoadErorrListener;
    }

    public interface onUpLoadErrorListener {
        void isError(boolean isError);
    }

    class AlbumAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (mAlbum != null) {
                return mAlbum.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub


            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            final ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageLoader.displayImage(Constants.ImageUrl + mAlbum.get(position) + Constants.ImageSuffix,
//                    imageView, options, new SimpleImageLoadingListener() {
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            // TODO Auto-generated method stub
//                            // imageView.setScaleType(ScaleType.CENTER);
//                        }
//                    });
            GlideUtil.showImageNoSuffix(context, mAlbum.get(position), imageView);
            container.addView(imageView, lp);

            int size = mAlbum.size();
            final String[] files = (String[]) mAlbum.toArray(new String[size]);
//            前缀 后缀取消 防止乐分商品无法显示
//            for (int i = 0; i < files.length; i++) {
//                files[i] = Constants.ImageUrl + files[i] + Constants.ImageSuffix;
//            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageBrower(position, files);
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

}
