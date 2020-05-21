package com.yilianmall.merchant.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.networkingmodule.entity.GoodsSkuPriceBean;
import com.yilian.networkingmodule.entity.GoodsSkuPropertyBean;
import com.yilian.networkingmodule.entity.MerchantManagerDetailEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.widget.FlowLayout;
import com.yilianmall.merchant.widget.GradationScrollView;
import com.yilianmall.merchant.widget.NoScrollListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ray_L_Pain on 2017/9/29 0029.
 * 商品管理详情页
 */

public class MerchantManagerDetailActivity extends BaseActivity {
    public static final String NOTHING = "L";
    public List<String> selectSkuList;
    //
    public Map<String, GoodsSkuPropertyBean> mSelectedProperty = new HashMap<>();
    public Map<String, GoodsSkuPriceBean> mSelectedPropertyPrice = new HashMap<>();
    //初始化sku ListView的标识
    public boolean isFirst = true;
    MerchantManagerDetailEntity.DataBean entity;
    private RelativeLayout layout_normal;
    private RelativeLayout layoutTitle;
    private ImageView ivBack;
    private TextView tvTitle;
    private GradationScrollView scrollView;
    //    private ImageView img;
    private Banner banner;
    private int height;
    //
    private TextView tv_good_name, tv_sales, tv_inventory, tv_price_mark, tv_price_sale, tv_price_real, tv_can_buy_count, tv_real_buy_count, tv_btn_normal;
    //   private TextView tv_mark_normal,tv_mark_yhs, tv_mark_jfg;
    //   private TextView tv_price_yhs;
    //   private TextView tv_integral_merchant, tv_integral_people;
    //   private TextView tv_btn_yhs, tv_btn_jfg;
    private NoScrollListView listView;
    private LinearLayout bottom_layout;
    private LinearLayout layout_net_error;
    private TextView tv_refresh;
    private FlowListAdapter adapter;
    private String goodsId, filialeId;
    //市场价
    private String markPrice = "0";
    //售价
    private String salePrice = "0";
    //成本价
    private String realPrice = "0";
    //易划算价
    private String yhsPrice = "0";
    //可订货库存
    private String willBuyCount = "0";
    //实际库存
    private String realBuyCount = "0";
    //商家奖券
    private String merchantIntegral = "0";
    //消费者奖券
    private String peopleIntegral = "0";
    private String is_normal, is_yhs, is_jfg;
    //第一次的SKU
    private String firstSKU = "";
    //筛选后的SKU
    private String finalSKU = "";
    private String firstStr = "";
    private String secondStr = "";
    private boolean isHandClick = true;
    private List<String> rememberList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_manager_detail);

        initView();
        initData();
        initListener();


//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) layoutTitle.getLayoutParams();
//            titleParams.height += StatusBarUtils.getStatusBarHeight(mContext);
//            layoutTitle.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
//
//            StatusBarUtil.setTranslucentForImageViewInFragment(MerchantManagerDetailActivity.this, 60, null);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        goodsId = getIntent().getStringExtra("goods_id");
        if (TextUtils.isEmpty(filialeId)) {
            filialeId = "0";
        }
        is_normal = getIntent().getStringExtra("is_normal");
        is_yhs = getIntent().getStringExtra("is_yhs");
        is_jfg = getIntent().getStringExtra("is_jfg");

        layout_normal = (RelativeLayout) findViewById(R.id.layout_normal);
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);

        ivBack = (ImageView) findViewById(R.id.iv_back);
//        ivBack.setImageAlpha(0);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
//        img = (ImageView) findViewById(R.id.img);
        banner = (Banner) findViewById(R.id.banner);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) banner.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(mContext);
        layoutParams.height = ScreenUtils.getScreenWidth(mContext);
        banner.setLayoutParams(layoutParams);
        scrollView = (GradationScrollView) findViewById(R.id.scrollView);
//        img.setFocusable(true);
//        img.setFocusableInTouchMode(true);
//        img.requestFocus();

        tv_good_name = (TextView) findViewById(R.id.tv_good_name);
//        tv_mark_normal = (TextView) findViewById(R.id.tv_mark_normal);
//        tv_mark_yhs = (TextView) findViewById(R.id.tv_mark_yhs);
//        tv_mark_jfg = (TextView) findViewById(R.id.tv_mark_jfg);
        tv_sales = (TextView) findViewById(R.id.tv_sales);
        tv_inventory = (TextView) findViewById(R.id.tv_inventory);

        listView = (NoScrollListView) findViewById(R.id.listView);

        tv_price_mark = (TextView) findViewById(R.id.tv_price_mark);
        tv_price_sale = (TextView) findViewById(R.id.tv_price_sale);
        tv_price_real = (TextView) findViewById(R.id.tv_price_real);
//        tv_price_yhs = (TextView) findViewById(R.id.tv_price_yhs);
        tv_can_buy_count = (TextView) findViewById(R.id.tv_can_buy_count);
        tv_real_buy_count = (TextView) findViewById(R.id.tv_real_buy_count);
//        tv_integral_merchant = (TextView) findViewById(R.id.tv_integral_merchant);
//        tv_integral_people = (TextView) findViewById(R.id.tv_integral_people);

        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        tv_btn_normal = (TextView) findViewById(R.id.tv_btn_normal);
        tv_btn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(MerchantManagerDetailActivity.this, "com.yilian.mall.ui.JPNewCommDetailActivity"));
                intent.putExtra("goods_id", goodsId);
                intent.putExtra("filiale_id", filialeId);
                intent.putExtra("classify", "0");
                intent.putExtra("from", "merchant");
                startActivity(intent);
            }
        });
//        tv_btn_yhs = (TextView) findViewById(R.id.tv_btn_yhs);
//        tv_btn_yhs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(MerchantManagerDetailActivity.this, "com.yilian.mall.ui.JPNewCommDetailActivity"));
//                intent.putExtra("goods_id", goodsId);
//                intent.putExtra("filiale_id", filialeId);
//                intent.putExtra("classify", "1");
//                intent.putExtra("from", "merchant");
//                startActivity(intent);
//            }
//        });
//        tv_btn_jfg = (TextView) findViewById(R.id.tv_btn_jfg);
//        tv_btn_jfg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(MerchantManagerDetailActivity.this, "com.yilian.mall.ui.JPNewCommDetailActivity"));
//                intent.putExtra("goods_id", goodsId);
//                intent.putExtra("filiale_id", filialeId);
//                intent.putExtra("classify", "2");
//                intent.putExtra("from", "merchant");
//                startActivity(intent);
//            }
//        });
        layout_net_error = (LinearLayout) findViewById(R.id.layout_net_error);
        tv_refresh = (TextView) findViewById(R.id.tv_refresh);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void initData() {
        getData();
    }

    private void getData() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                .goodsDownUpInfo(goodsId, new Callback<MerchantManagerDetailEntity>() {
                    @Override
                    public void onResponse(Call<MerchantManagerDetailEntity> call, Response<MerchantManagerDetailEntity> response) {
                        stopMyDialog();
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        entity = response.body().data;

//                                        GlideUtil.showImageNoSuffix(mContext, entity.goods_icon, img);
                                        banner.setImages(entity.goods_album)
                                                .setBannerStyle(BannerConfig.NOT_INDICATOR)
                                                .setImageLoader(new BannerViewGlideUtil())
                                                .setIndicatorGravity(BannerConfig.RIGHT)
                                                .setDelayTime(5000)
                                                .setOnBannerListener(new OnBannerListener() {
                                                    @Override
                                                    public void OnBannerClick(int i) {
                                                        int size = entity.goods_album.size();
                                                        final String[] files = (String[]) entity.goods_album.toArray(new String[size]);
                                                        imageBrower(i, files);
                                                    }
                                                })
                                                .start();

                                        tv_good_name.setText(entity.goods_name);
//                                        if ("1".equals(entity.is_normal)) {
//                                            tv_mark_normal.setVisibility(View.VISIBLE);
//                                        } else {
//                                            tv_mark_normal.setVisibility(View.GONE);
//                                        }
//                                        if ("1".equals(entity.is_yhs)) {
//                                            tv_mark_yhs.setVisibility(View.VISIBLE);
//                                        } else {
//                                            tv_mark_yhs.setVisibility(View.GONE);
//                                        }
//                                        if ("1".equals(entity.is_jfg)) {
//                                            tv_mark_jfg.setVisibility(View.VISIBLE);
//                                        } else {
//                                            tv_mark_jfg.setVisibility(View.GONE);
//                                        }
                                        tv_sales.setText("销量 " + entity.goods_sale);
                                        tv_inventory.setText("库存 " + entity.goods_sku_count);

//                                        switch (entity.authority) {
//                                            case "1":
//                                                tv_btn_yhs.setVisibility(View.VISIBLE);
//                                                tv_btn_jfg.setVisibility(View.VISIBLE);
//                                                break;
//                                            case "2":
//                                                tv_btn_yhs.setVisibility(View.GONE);
//                                                tv_btn_jfg.setVisibility(View.VISIBLE);
//                                                break;
//                                            case "3":
//                                                tv_btn_yhs.setVisibility(View.VISIBLE);
//                                                tv_btn_jfg.setVisibility(View.GONE);
//                                                break;
//                                        }

                                        initListView(entity);
                                        layout_normal.setVisibility(View.VISIBLE);
                                        layout_net_error.setVisibility(View.GONE);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantManagerDetailEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.merchant_system_busy);
                        layout_normal.setVisibility(View.GONE);
                        bottom_layout.setVisibility(View.GONE);
                        layout_net_error.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(MerchantManagerDetailActivity.this, "com.yilian.mall.ui.ImagePagerActivity"));
        intent.putExtra("image_urls", urls);
        intent.putExtra("image_index", position);
        startActivity(intent);
    }

    private void initListView(MerchantManagerDetailEntity.DataBean entity) {
        String sku = entity.goods_sku;
        if (isFirst) {
            firstSKU = sku.toString();

            for (GoodsSkuPriceBean skuPriceBean : entity.goodsSkuPrice) {
                if (skuPriceBean.skuInfo.equals(entity.goods_sku)) {
                    markPrice = MoneyUtil.getLeXiangBiNoZero(skuPriceBean.skuMrketPrice);
                    salePrice = MoneyUtil.getLeXiangBiNoZero(skuPriceBean.skuCostPrice);
                    realPrice = MoneyUtil.getLeXiangBiNoZero(skuPriceBean.skuRetailPrice);
                    yhsPrice = MoneyUtil.getLeXiangBiNoZero(skuPriceBean.skuIntegralPrice);
                    willBuyCount = skuPriceBean.skuInventory;
                    realBuyCount = skuPriceBean.skuRepertory;
                    merchantIntegral = MoneyUtil.getLeXiangBiNoZero(skuPriceBean.returnMerchantIntegral);
                    peopleIntegral = MoneyUtil.getLeXiangBiNoZero(skuPriceBean.returnIntegral);
                }
            }
        }
//        Logger.i("we-win-" + firstSKU);
        if (firstSKU.contains(",")) {
            String[] str = firstSKU.split(",");
            firstStr = str[0];
            secondStr = str[1];
        }

        if ("0:0".equals(sku)) {
            listView.setVisibility(View.GONE);
        } else {
            String[] arraySku;
            if (isFirst) {
                arraySku = sku.split(",");
            } else {
                if (firstSKU.contains(",")) {
                    arraySku = new String[]{firstStr, secondStr};
                } else {
                    arraySku = new String[]{firstSKU};
                }
            }
            selectSkuList = new ArrayList<>(Arrays.asList(arraySku));

            if (adapter == null) {
                adapter = new FlowListAdapter(entity.goodsSkuProperty, entity.goodsSkuValues, entity.goodsSkuPrice, selectSkuList);
            }
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
        }

        if (isFirst) {
            tv_price_mark.setText(markPrice);
            tv_price_sale.setText(salePrice);
            tv_price_real.setText(realPrice);
//            tv_price_yhs.setText(yhsPrice);
            tv_can_buy_count.setText(willBuyCount);
            tv_real_buy_count.setText(realBuyCount);
//            tv_integral_merchant.setText(merchantIntegral);
//            tv_integral_people.setText(peopleIntegral);
        } else if (rememberList.contains("0")) {
            tv_price_mark.setText(markPrice);
            tv_price_sale.setText(salePrice);
            tv_price_real.setText(realPrice);
//            tv_price_yhs.setText(yhsPrice);
            tv_can_buy_count.setText(willBuyCount);
            tv_real_buy_count.setText(realBuyCount);
//            tv_integral_merchant.setText(merchantIntegral);
//            tv_integral_people.setText(peopleIntegral);
        } else {
            tv_price_mark.setText("0");
            tv_price_sale.setText("0");
            tv_price_real.setText("0");
//            tv_price_yhs.setText("0");
            tv_can_buy_count.setText("0");
            tv_real_buy_count.setText("0");
//            tv_integral_merchant.setText("0");
//            tv_integral_people.setText("0");
        }

        isFirst = false;
    }

    private void initListener() {
//        ViewTreeObserver vto = banner.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                layoutTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
//                        this);
//                height = banner.getHeight();
//
//                scrollView.setScrollViewListener(MerchantManagerDetailActivity.this);
//            }
//        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
//        if (y <= 10) {   //设置标题的背景颜色
//            ivBack.setImageAlpha(0);
//            layoutTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
//        } else if (y > 10 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
//            float scale = (float) y / height;
//            float alpha = (255 * scale);
//            tvTitle.setTextColor(Color.argb((int) alpha, 51, 51, 51));
//            layoutTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
//            ivBack.setImageAlpha((int) alpha);
//        } else {    //滑动到banner下面设置普通颜色
//            layoutTitle.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
//        }
//    }

    class FlowListAdapter extends BaseAdapter {
        private List<GoodsSkuPropertyBean> mPropertys;
        private List<GoodsSkuPropertyBean> mValue;
        private List<GoodsSkuPriceBean> mPrice;
        private ArrayList<String> skuList = new ArrayList<>();
        private ArrayList<String> skuNewList = new ArrayList<>();
        private List<String> selectSkuList;

        public FlowListAdapter(List<GoodsSkuPropertyBean> mPropertys,
                               List<GoodsSkuPropertyBean> mValue,
                               List<GoodsSkuPriceBean> mPrice,
                               List<String> selectSkuList) {
            this.mPropertys = mPropertys;
            this.mValue = mValue;
            this.mPrice = mPrice;
            this.selectSkuList = selectSkuList;
        }

        @Override
        public int getCount() {
            if (null == mPropertys || mPropertys.size() == 0) {
                return 0;
            } else {
                for (int i = 0; i < mPropertys.size(); i++) {
                    String s = NOTHING;
                    skuList.add(s);
                }
                return mPropertys.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mPropertys != null) {
                return mPropertys.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Logger.i("gogogo-" + skuList.toString());
            if (firstSKU.contains(",") && isHandClick) {
                skuList.set(0, firstSKU.split(",")[0]);
                skuList.set(1, firstSKU.split(",")[1]);
            }

            convertView = getLayoutInflater().inflate(R.layout.merchant_item_commodity_property_list, null);
            TextView tvPropertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
            FlowLayout layoutPropertyValue = (FlowLayout) convertView.findViewById(R.id.flayout_property_values);

            final GoodsSkuPropertyBean property = mPropertys.get(position);
            if (property != null) {
                tvPropertyName.setText(property.skuName);
                //经过筛选，父类有的子类
                List<GoodsSkuPropertyBean> mValueFilter = new ArrayList<>();
                for (GoodsSkuPropertyBean value : mValue) {
                    if (property.skuIndex == null || value == null) {
                        continue;
                    }
                    if (property.skuIndex.equals(value.skuParentId)) {
                        mValueFilter.add(value);
                    }
                    //未点击
                    if (isHandClick) {
                        /**
                         * selectSkuList.size() 两种情况 为0不走adapter这里 现在的商品也没有三个及以上的属性
                         * 1：单个属性
                         * 2：多个属性
                         * 这里遍历 将筛选出的item存到map里
                         * map("696",  {"sku_index": "723","sku_name": "300","sku_parent": "696","supplier_belong": "152"})
                         */
                        if (selectSkuList.size() >= 1) {
                            for (int m = 0; m < selectSkuList.size(); m++) {
                                String select = selectSkuList.get(m);
                                String[] selectStr = select.split(":");
                                if (selectStr[0].equals(value.skuParentId) && selectStr[1].equals(value.skuIndex)) {
                                    mSelectedProperty.put(selectStr[0], value);
                                    break;
                                }
                            }
                        } else {
                            Logger.i("we-win-" + "讲道理不会走这里的");
                        }
                    }
                }
                //一个规格选择后，子规格遍历
                for (int i = 0; i < mValueFilter.size(); i++) {
                    final GoodsSkuPropertyBean value = mValueFilter.get(i);
                    final GoodsSkuPriceBean price = mPrice.get(i);
                    final TextView tv = getValueView(value.skuName, value.skuParentId);
                    //如果选中的和已有的符合 则背景变为红色
                    if (value == mSelectedProperty.get(property.skuIndex)) {
                        tv.setBackgroundResource(R.drawable.merchant_commodity_property_item_select_bg_jp);
                        tv.setTextColor(getResources().getColor(R.color.merchant_white));
                        tv.setTextSize(12);
                    }

                    tv.setOnClickListener(new View.OnClickListener() {
                        private String sku;

                        @Override
                        public void onClick(View v) {
                            //当前选中的按钮的sku
                            for (int j = 0; j < mValue.size(); j++) {
                                GoodsSkuPropertyBean goodsSkuProperty = mValue.get(j);
                                if (tv.getText().toString().equals(goodsSkuProperty.skuName)) {
                                    sku = goodsSkuProperty.skuParentId + ":" + goodsSkuProperty.skuIndex;
                                }
                            }
                            //把选中的SKU的值赋值到SKU列表里
                            skuList.set(position, sku);
                            Logger.i("gogogo-" + skuList.toString());

                            if (mSelectedProperty.containsValue(value)) {
                                mSelectedProperty.remove(property.skuIndex);
                                mSelectedPropertyPrice.remove(property.skuIndex + ":" + mSelectedProperty.get(property.skuIndex));

                                tv.setEnabled(true);
                                tv.setTextColor(getResources().getColor(R.color.merchant_white));
                                tv.setTextSize(12);
                                //有默认显示的SKU所以不能根据L来判断点击没有
                                skuList.set(position, NOTHING);
                                Logger.i("gogogo-" + skuList.toString());
                            } else {
                                tv.setEnabled(false);
                                tv.setTextColor(Color.parseColor("#E0E0E0"));
                                tv.setTextSize(12);
                                mSelectedProperty.put(property.skuIndex, value);
                                mSelectedPropertyPrice.put(property.skuIndex + ":" + mSelectedProperty.get(property.skuIndex).skuIndex, price);
                            }

                            Logger.i("gogogo-" + skuList.toString());
                            skuNewList.clear();
                            for (int j = 0; j < mPropertys.size(); j++) {
                                String s = skuList.get(j);
                                skuNewList.add(s);
                            }

                            finalSKU = "";

                            for (int s = 0, count = skuNewList.size(); s < count; s++) {
                                if (s < count - 1) {
                                    finalSKU += skuNewList.get(s) + ",";
                                } else {
                                    finalSKU += skuNewList.get(s);
                                }
                            }
                            Logger.i("we-win-" + finalSKU);
//                            if (finalSKU.contains(NOTHING)) {
//                                if (!firstSKU.contains(",")) {
//                                    return;
//                                }
//                                if (isHandClick) {
//                                    if (finalSKU.startsWith(NOTHING)) {//第一个属性改变
//                                        finalSKU = finalSKU.replace(NOTHING, firstStr);
//                                    } else {//第二个属性改变
//                                        finalSKU = finalSKU.replace(NOTHING, secondStr);
//                                    }
//                                }
//                            }
                            Logger.i("we-win-" + finalSKU);
                            //更新过的sku赋值给之前回显时的sku
                            firstSKU = finalSKU;
//                            Logger.i("we-win-" + firstSKU);
                            switchGoodsNormsData(finalSKU);
                            notifyDataSetChanged();

                            isHandClick = false;
                        }
                    });
                    layoutPropertyValue.addView(tv);
                }
            }

            return convertView;
        }

        private TextView getValueView(String name, String parentId) {
            TextView tv = new TextView(mContext);
            ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    CommonUtils.dip2px(mContext, 27));
            mlp.setMargins(0, CommonUtils.dip2px(mContext, 10F), CommonUtils.dip2px(mContext, 15F),
                    CommonUtils.dip2px(mContext, 10F));
            tv.setLayoutParams(mlp);
            tv.setTextSize(12);
            tv.setBackgroundResource(R.drawable.merchant_norms_btn_enable_bg);
            tv.setTextColor(getResources().getColor(R.color.merchant_color_h1));
            tv.setText(name);
            tv.setTag(parentId);
            return tv;
        }

        private void switchGoodsNormsData(String selectSkuResult) {
            Logger.i("rng-" + "走了这里1");
            rememberList.clear();
            if (selectSkuResult.contains(NOTHING)) {
                Logger.i("rng-" + "走了这里2");
                markPrice = "0";
                salePrice = "0";
                realPrice = "0";
                yhsPrice = "0";
                willBuyCount = "0";
                realBuyCount = "0";
                merchantIntegral = "0";
                peopleIntegral = "0";
                initListView(entity);
                return;
            }
            if (!TextUtils.isEmpty(selectSkuResult)) {
                Logger.i("rng-" + "走了这里3");
                for (int i = 0; i < mPrice.size(); i++) {
                    String skuInfo = mPrice.get(i).skuInfo;
                    if (skuInfo.equals(selectSkuResult)) {
                        Logger.i("rng-" + "走了这里4");
                        rememberList.add("0");
                        markPrice = MoneyUtil.getLeXiangBiNoZero(mPrice.get(i).skuMrketPrice);
                        salePrice = MoneyUtil.getLeXiangBiNoZero(mPrice.get(i).skuCostPrice);
                        realPrice = MoneyUtil.getLeXiangBiNoZero(mPrice.get(i).skuRetailPrice);
                        yhsPrice = MoneyUtil.getLeXiangBiNoZero(mPrice.get(i).skuIntegralPrice);
                        willBuyCount = mPrice.get(i).skuInventory;
                        realBuyCount = mPrice.get(i).skuRepertory;
                        merchantIntegral = MoneyUtil.getLeXiangBiNoZero(mPrice.get(i).returnMerchantIntegral);
                        peopleIntegral = MoneyUtil.getLeXiangBiNoZero(mPrice.get(i).returnIntegral);
                    } else if (!skuInfo.contains(selectSkuResult)) {
                        Logger.i("rng-" + "走了这里5");
                        rememberList.add("1");
                    }
                }
            } else if (!TextUtils.isEmpty(selectSkuResult) && finalSKU.contains(NOTHING)) {
                Logger.i("rng-" + "走了这里6");
                markPrice = "0";
                salePrice = "0";
                realPrice = "0";
                yhsPrice = "0";
                willBuyCount = "0";
                realBuyCount = "0";
                merchantIntegral = "0";
                peopleIntegral = "0";
            }
            initListView(entity);
        }
    }

}
