package com.yilian.mall.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.GetGoodInfoResultV2;
import com.yilian.mall.entity.GoodsSkuProperty;
import com.yilian.mall.entity.MallGoodsInfo;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.FlowLayout;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mall.widgets.ScreenNumView;
import com.yilian.mall.widgets.ScrollViewContainer;
import com.yilian.mall.widgets.ScrollViewContainer.SwitchCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 商品详情页
 *
 * @author Administrator
 */
public class StoreGoodsDetailActivity extends BaseActivity implements SwitchCallback {

    /**
     * 标题
     */
    @ViewInject(R.id.tv_back)
    TextView mTvTitle;
    /**
     * 商品图片
     */
    @ViewInject(R.id.vp_commodity_img)
    ViewPager mViewPagerAlbum;

    /**
     * 商品图片指示器
     */
    @ViewInject(R.id.pager_indicator)
    ScreenNumView mIndicator;

    /**
     * 商品描述
     */
    @ViewInject(R.id.tv_description)
    TextView mTvDescription;

    /**
     * 商品价格分区
     */
    @ViewInject(R.id.tv_integral_type)
    TextView mTvIntegralType;

    /**
     * 商品价格
     */
    @ViewInject(R.id.tv_price)
    TextView mTvPrice;

    /**
     * "券"图标
     */
    @ViewInject(R.id.tv_ticket)
    TextView mTvTicketImg;

    /**
     * 差价
     */
    @ViewInject(R.id.tv_goods_fill_price)
    TextView mTvFillPrice;

    /**
     * 建议市场价
     */
    @ViewInject(R.id.tv_goods_market_price)
    TextView mTvMarketPrice;

    /**
     * 商品评分条
     */
    @ViewInject(R.id.ratingBar_grade)
    RatingBar mRateBarGrade;

    /**
     * 商品评分
     */
    @ViewInject(R.id.tv_grade)
    TextView mTvGrade;

    /**
     * 商品销量
     */
    @ViewInject(R.id.tv_sales_volume)
    TextView mTvSales;

    /**
     * 商品品牌
     */
    @ViewInject(R.id.tv_commodity_brand)
    TextView mTvBrand;

    /**
     * 商品货号
     */
    @ViewInject(R.id.tv_commodity_number)
    TextView mTvNumber;

    /**
     * 商品属性列表
     */
    @ViewInject(R.id.lv_commodity_property)
    NoScrollListView mLvProperty;

    /**
     * 继续拖动图片
     */
    @ViewInject(R.id.imgView_pull_up)
    ImageView mImgPullUp;

    /**
     * 商品详细信息导航栏
     */
    @ViewInject(R.id.rg_commodity_detail)
    RadioGroup mRgDetails;

    /**
     * 图文详情按钮
     */
    @ViewInject(R.id.rb_img_text)
    RadioButton mRbImgText;

    /**
     * 商品参数按钮
     */
    @ViewInject(R.id.rb_commodity_param)
    RadioButton mRbCommodityParam;

    /**
     * 图文详情列表
     */
    @ViewInject(R.id.lv_img_text)
    NoScrollListView mLvImageText;

    /**
     * 返回顶部按钮
     */
    @ViewInject(R.id.imgView_go_top)
    ImageView mImgViewGoTop;

    /**
     * 上下拉切换控件
     */
    @ViewInject(R.id.sv_container)
    ScrollViewContainer mSvContainer;

    /**
     * 上部布局
     */
    @ViewInject(R.id.sv_top)
    ScrollView mSvTop;

    /**
     * 无数据展示的视图
     */
    @ViewInject(R.id.layout_no_data)
    View mViewNoData;

    /**
     * 无数据时的文本
     */
    @ViewInject(R.id.tv_no_data)
    TextView mTvNoData;

    /**
     * 顶部隐藏的商品详细信息导航栏
     */
    @ViewInject(R.id.rg_commodity_detail_top)
    RadioGroup mRgDetailsTop;

    /**
     * 顶部隐藏的图文详情按钮
     */
    @ViewInject(R.id.rb_img_text_top)
    RadioButton mRbImgTextTop;

    /**
     * 顶部隐藏的商品参数按钮
     */
    @ViewInject(R.id.rb_commodity_param_top)
    RadioButton mRbCommodityParamTop;

    private String mCommodityId;// 商品id
    private MallNetRequest mMallRequest;
    private MallGoodsInfo mGoodInfo;
    private List<String> mAlbum;
    private List<String> mImgTextList;
    private boolean mIsCollected = false;// 是否收藏
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private int width;

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.imgView_go_top:
                    mSvContainer.rollToTop();
                    mSvTop.scrollTo(0, 0);
                    break;
                case R.id.rb_img_text_top:
                    mRgDetails.check(R.id.rb_img_text);
                    break;
                case R.id.rb_commodity_param_top:
                    mRgDetails.check(R.id.rb_commodity_param);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_goods_detail);
        ViewUtils.inject(this);
        width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();// 获取屏幕宽度
        init();
        registerEvents();
        getCommodityDetails();
    }

    private void init() {
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.jiazaizhong)
                .showImageForEmptyUri(R.mipmap.jiazaizhong).showImageOnFail(R.mipmap.jiazaizhong)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        mCommodityId = getIntent().getStringExtra("good_id");
        mTvTitle.setText(R.string.commodity_details);
        mMallRequest = new MallNetRequest(mContext);
    }

    private void registerEvents() {
        OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

            private Drawable drawableBottom = getResources().getDrawable(R.drawable.blue_underline);

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
                if (group == mRgDetails) {
                    switch (checkedId) {
                        case R.id.rb_img_text:
                            mRgDetailsTop.check(R.id.rb_img_text_top);
                            mRbImgText.setCompoundDrawables(null, null, null, drawableBottom);
                            mRbCommodityParam.setCompoundDrawables(null, null, null, null);
                            if (mImgTextList == null) {
                                return;
                            }
                            if (mImgTextList.size() == 0) {
                                mLvImageText.setVisibility(View.GONE);
                                mViewNoData.setVisibility(View.VISIBLE);
                                mTvNoData.setText("很抱歉，暂无图文详情！");
                            } else {
                                mViewNoData.setVisibility(View.GONE);
                                mLvImageText.setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.rb_commodity_param:
                            mRgDetailsTop.check(R.id.rb_commodity_param_top);
                            mRbImgText.setCompoundDrawables(null, null, null, null);
                            mRbCommodityParam.setCompoundDrawables(null, null, null, drawableBottom);
                            mLvImageText.setVisibility(View.GONE);
                            mViewNoData.setVisibility(View.VISIBLE);
                            mTvNoData.setText("很抱歉，暂无产品参数！");
                            break;

                        default:
                            break;
                    }
                } else if (group == mRgDetailsTop) {
                    switch (checkedId) {
                        case R.id.rb_img_text_top:
                            mRbImgTextTop.setCompoundDrawables(null, null, null, drawableBottom);
                            mRbCommodityParamTop.setCompoundDrawables(null, null, null, null);
//						mRgDetails.check(R.id.rb_img_text);
                            break;
                        case R.id.rb_commodity_param_top:
                            mRbImgTextTop.setCompoundDrawables(null, null, null, null);
                            mRbCommodityParamTop.setCompoundDrawables(null, null, null, drawableBottom);
//						mRgDetails.check(R.id.rb_commodity_param);
                            break;

                        default:
                            break;
                    }
                }
            }
        };
        mImgViewGoTop.setOnClickListener(mOnClickListener);
        mSvContainer.setSwitchCallbak(this);
        mRgDetails.setOnCheckedChangeListener(checkedChangeListener);
        mRgDetailsTop.setOnCheckedChangeListener(checkedChangeListener);
        mRbImgTextTop.setOnClickListener(mOnClickListener);
        mRbCommodityParamTop.setOnClickListener(mOnClickListener);
        mViewPagerAlbum.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                mIndicator.snapToPage(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 获取商品详情
     */
    private void getCommodityDetails() {

        mMallRequest.getGoodsInfo(mCommodityId, GetGoodInfoResultV2.class, new RequestCallBack<GetGoodInfoResultV2>() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<GetGoodInfoResultV2> arg0) {
                // TODO Auto-generated method stub
                stopMyDialog();

                if (arg0.result.code == 1) {
                    mGoodInfo = arg0.result.good;
                    Logger.i("getCommodityDetails");
                    initCommodityInfo();
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                stopMyDialog();
            }
        });
    }


    /**
     * 加载商品信息
     */
    private void initCommodityInfo() {
        if (mGoodInfo != null) {
            mTvDescription.setText(mGoodInfo.goodsName);
            //mTvGrade.setText(Float.parseFloat(mGoodInfo.goodsGrade.substring(0, 1)) + "评分");
            mTvSales.setText(getString(R.string.sales_volume_month, mGoodInfo.goodsSalesVolume));
            mTvBrand.setText(mGoodInfo.goodsBrand);

            String id = "LF-0000000000";
            mTvNumber.setText(id.substring(0, 13 - mGoodInfo.goodsId.length()) + mGoodInfo.goodsId);
            //mRateBarGrade.setRating(Float.parseFloat(mGoodInfo.goodsGrade.substring(0, 1)));
            mAlbum = mGoodInfo.goodsAlbum;
            mImgTextList = mGoodInfo.goodsIntroduce;
            switch (mGoodInfo.goodsType) {
                case 0:
                case 1:
                    mTvFillPrice.setVisibility(View.GONE);
                    // mTvMarketPrice.setVisibility(View.VISIBLE);
                    mTvIntegralType.setText("建议乐换价");
                    mTvPrice.setText(mGoodInfo.goodsIntegralPrice + ".00");
                    mTvMarketPrice.setText(
                            "建议市场价  ¥" + String.format("%.2f", Float.parseFloat(mGoodInfo.goodsMarketPrice) / 100.00));
                    break;
                case 4:
                    // mTvFillPrice.setVisibility(View.VISIBLE);
                    // mTvMarketPrice.setVisibility(View.VISIBLE);
                    mTvIntegralType.setText("建议乐选价");
                    mTvPrice.setText(mGoodInfo.goodsIntegralPrice + ".00");
                    mTvFillPrice.setText("所需差价 ¥ " + String.format("%.2f", Float.parseFloat(mGoodInfo.goodsIntegralMoney) / 100.00f));
                    mTvMarketPrice.setText(
                            "建议市场价  ¥" + String.format("%.2f", Float.parseFloat(mGoodInfo.goodsMarketPrice) / 100.00));
                    break;
                case 3:
                    mTvFillPrice.setVisibility(View.GONE);
                    mTvMarketPrice.setVisibility(View.GONE);
                    mTvTicketImg.setVisibility(View.GONE);
                    mTvIntegralType.setText("乐购价");
                    mTvPrice.setText("¥" + String.format("%.2f", Float.parseFloat(mGoodInfo.goodsMarketPrice) / 100.00));
                    break;

                default:
                    break;
            }

            AlbumAdapter albumAdapter = new AlbumAdapter();
            mViewPagerAlbum.setAdapter(albumAdapter);
            mIndicator.initScreen(albumAdapter.getCount());

            mLvProperty.setAdapter(new PropertyAdapter());
        }

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
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            final ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            GlideUtil.showImageWithSuffix(mContext, mAlbum.get(position), imageView);
            container.addView(imageView, lp);
            return imageView;
        }

    }

    class PropertyAdapter extends BaseAdapter {

        private List<GoodsSkuProperty> mPropertys = mGoodInfo.goods_sku_property;
        private List<GoodsSkuProperty> mValues = mGoodInfo.goods_sku_values;
        private List<String> mValueFilter = new ArrayList<String>();

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (mPropertys != null) {
                return mPropertys.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (mPropertys != null) {
                return mPropertys.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            mValueFilter.clear();
            convertView = getLayoutInflater().inflate(R.layout.item_commodity_property_list, null);
            TextView tvPropertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
            FlowLayout layoutPropertyValue = (FlowLayout) convertView.findViewById(R.id.flayout_property_values);
            GoodsSkuProperty property = mPropertys.get(position);
            if (property != null) {
                tvPropertyName.setText("该商品提供的" + property.skuName);
                for (GoodsSkuProperty value : mValues) {
                    if (property.skuId == null || value == null) {
                        continue;
                    }
                    if (property.skuId.equals(value.skuParentId)) {
                        mValueFilter.add(value.skuName);
                    }
                }

                Collections.sort(mValueFilter, new Comparator<String>() {

                    @Override
                    public int compare(String lhs, String rhs) {
                        // TODO Auto-generated method stub
                        return lhs.length() - rhs.length();
                    }
                });
                for (String str : mValueFilter) {
                    layoutPropertyValue.addView(getValueView(str));
                }
            }

            return convertView;
        }

        class ViewHolder {
            TextView tvPropertyName;
            FlowLayout layoutPropertyValue;
        }

        private View getValueView(String value) {
            TextView tv = new TextView(mContext);
            MarginLayoutParams mlp = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
                    CommonUtils.dip2px(mContext, 27));

            mlp.setMargins(0, CommonUtils.dip2px(mContext, 8.3F), CommonUtils.dip2px(mContext, 10F),
                    CommonUtils.dip2px(mContext, 8.3F));
            tv.setLayoutParams(mlp);
            tv.setTextSize(12);
            tv.setTextColor(0xff999999);
            tv.setText(value);
            tv.setBackgroundResource(R.drawable.commodity_property_item_bg);
            return tv;
        }
    }

    class ImageTextAdapter extends BaseAdapter {

        int imgHeight;

        public ImageTextAdapter() {

            WindowManager wm = getWindowManager();

            imgHeight = wm.getDefaultDisplay().getWidth();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (mImgTextList != null) {
                return mImgTextList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (mImgTextList != null) {
                return mImgTextList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_commodity_img_text_list, null);
            }
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imgView_img_text);
            imageView.setScaleType(ScaleType.FIT_XY);
            GlideUtil.showImageWithSuffix(mContext, mImgTextList.get(position), imageView);
            return convertView;
        }
    }

    @Override
    public void onMove(int moveY, View topView, View bottomView) {
        // TODO Auto-generated method stub
        if (moveY == -topView.getHeight()) {
            mRgDetailsTop.setVisibility(View.VISIBLE);
            mRgDetails.setVisibility(View.INVISIBLE);
            mImgViewGoTop.setVisibility(View.VISIBLE);
            if (mImgTextList == null || mImgTextList.size() == 0) {
                mLvImageText.setVisibility(View.GONE);
                mViewNoData.setVisibility(View.VISIBLE);
                mTvNoData.setText("很抱歉，暂无图文详情！");
            } else {
                mLvImageText.setAdapter(new ImageTextAdapter());
            }
        } else {
            mRgDetailsTop.setVisibility(View.INVISIBLE);
            mRgDetails.setVisibility(View.VISIBLE);
            mImgViewGoTop.setVisibility(View.INVISIBLE);
        }
    }
}
