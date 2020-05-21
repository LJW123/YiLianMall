package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BannerViewPager;
import com.yilian.mall.entity.FlashSaleDetailsEntity;
import com.yilian.mall.entity.FlashSaleTransferEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.OrderSubmitGoods;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;

import java.util.List;

/**
 * 本地限时购
 */
public class FlashSaleDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private ViewPager viewpager;
    private LinearLayout llPoints;
    private TextView tvTitle;
    private TextView tvMerchantsName;
    private TextView tvResidue;
    private LinearLayout llFlashSaleContent;
    private FrameLayout flBanner;
    private TextView tvCostPrice;
    private TextView tvMarketPrice;
    private TextView tvNowBuy;
    private TextView tvContentAddress;
    private TextView tvLocation;
    private ImageView ivCall;
    private TextView tvMerchantDesp;
    private TextView tvShopDespContent;
    private PullToRefreshScrollView svPullRefresh;
    private RelativeLayout activityFlashSaleDetails;
    private JPNetRequest jpNetRequest;
    private String telephone;
    private UmengDialog shareDialog;
    private String goods_id;
    private String shareContent;
    private String shareTitle;
    private String shareUrl;
    private String shareImageUrl;
    private String price;
    private LinearLayout llPhoto;
    private String filialeId;
    private FlashSaleDetailsEntity flashSaleDetailsEntity;
    private Context mContext;
    private String buyTimeState;
    private LinearLayout llPrice;
    private LinearLayout llUpdataError;
    private TextView tvUpdata;
    private TextView tvSelectAddress;
    private FlashSaleDetailsEntity.DataBean.InfoBean flashSaleData;
    private BannerViewPager bannerViewPager;
    private ImageView ivNothing;
    private TextView tvNotice;
    private String goodsType;
    private String goodsName;
    private String fregihtPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_flash_sale_details);
        mContext = this;
        goods_id = getIntent().getStringExtra("goods_id");
        initView();
        initData();
        initListener();
        getShareUrl();
    }

    private void initListener() {
        //防止点击的时候需要用到请求的数据

        ivCall.setOnClickListener(this);
        tvUpdata.setOnClickListener(this);
        tvSelectAddress.setOnClickListener(this);


        svPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int finalPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                finalPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (bannerViewPager.getCount() - 1 == finalPosition) {
                        viewpager.setCurrentItem(1, false);
                    } else if (finalPosition == 0) {
                        viewpager.setCurrentItem(bannerViewPager.getCount() - 2, false);
                    }
                }
            }
        });
    }

    private void initData() {
        Logger.i("FlahSaleDtailsActivity  goods_id " + goods_id);
        if (null != goods_id) {
            if (jpNetRequest == null) {
                jpNetRequest = new JPNetRequest(mContext);
            }
            startMyDialog();
            jpNetRequest.getFlashSaleDetailsData(goods_id, new RequestCallBack<FlashSaleDetailsEntity>() {
                @Override
                public void onSuccess(ResponseInfo<FlashSaleDetailsEntity> responseInfo) {
                    flashSaleDetailsEntity = responseInfo.result;
                    switch (flashSaleDetailsEntity.code) {
                        case 1:
                            if (null != responseInfo.result.data.info) {
                                svPullRefresh.setVisibility(View.VISIBLE);
                                llUpdataError.setVisibility(View.GONE);
                                flashSaleData = flashSaleDetailsEntity.data.info;
                                fregihtPrice = flashSaleData.fregihtPrice;
                                if (null != flashSaleData) {
                                    initViewContent(flashSaleData);
                                    telephone = flashSaleData.merchantTel;
                                    price = flashSaleData.price;
                                    filialeId = flashSaleData.filialeId;
                                }
                            } else {
                                ivNothing.setVisibility(View.VISIBLE);
                            }

                            stopMyDialog();
                            svPullRefresh.onRefreshComplete();
                            break;
                        case 0:
                            svPullRefresh.setVisibility(View.GONE);
                            ivNothing.setVisibility(View.VISIBLE);
                            break;
                        default:
                            llUpdataError.setVisibility(View.VISIBLE);
                            svPullRefresh.setVisibility(View.GONE);
                            Logger.i("显示抢购详情返回码  " + flashSaleDetailsEntity.code);
                            break;

                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    stopMyDialog();
                    llUpdataError.setVisibility(View.VISIBLE);
                    svPullRefresh.setVisibility(View.GONE);
                    svPullRefresh.onRefreshComplete();
                }
            });
        }
    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("抢购详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.VISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        llPoints = (LinearLayout) findViewById(R.id.ll_points);
        llPoints.setVisibility(View.GONE);
        llFlashSaleContent = (LinearLayout) findViewById(R.id.ll_flash_sale_content);
        llFlashSaleContent = (LinearLayout) findViewById(R.id.ll_flash_sale_content);
        llPrice = (LinearLayout) findViewById(R.id.ll_price);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvMerchantsName = (TextView) findViewById(R.id.tv_des);
        tvResidue = (TextView) findViewById(R.id.tv_residue);
        flBanner = (FrameLayout) findViewById(R.id.fl_banner);
        tvCostPrice = (TextView) findViewById(R.id.tv_cost_price);
        tvMarketPrice = (TextView) findViewById(R.id.tv_market_price);
        tvNowBuy = (TextView) findViewById(R.id.tv_now_buy);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
        tvContentAddress = (TextView) findViewById(R.id.tv_content_address);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        ivCall = (ImageView) findViewById(R.id.iv_call);
        tvMerchantDesp = (TextView) findViewById(R.id.tv_merchant_desp);
        tvShopDespContent = (TextView) findViewById(R.id.tv_shop_desp_content);
        tvUpdata = (TextView) findViewById(R.id.tv_update_error);
        llUpdataError = (LinearLayout) findViewById(R.id.ll_updata_error);
        llPhoto = (LinearLayout) findViewById(R.id.ll_photo);
        svPullRefresh = (PullToRefreshScrollView) findViewById(R.id.sv_pull_refresh);
        svPullRefresh.setVisibility(View.GONE);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        activityFlashSaleDetails = (RelativeLayout) findViewById(R.id.activity_flash_sale_details);
        tvSelectAddress = (TextView) findViewById(R.id.tv_select_address);

        v3Back.setOnClickListener(this);
        v3Share.setOnClickListener(this);
        tvNowBuy.setOnClickListener(this);
    }

    private void initViewContent(FlashSaleDetailsEntity.DataBean.InfoBean flashSaleData) {
        llFlashSaleContent.setVisibility(View.VISIBLE);
        goodsName = flashSaleData.goodsName;
        tvTitle.setText(goodsName);
        tvMerchantsName.setText(flashSaleData.merchantName);
        tvResidue.setText("剩余" + flashSaleData.goodsNum + "份");
        tvCostPrice.setText(MoneyUtil.getLeXiangBiNoZero(flashSaleData.price));
        tvMarketPrice.setVisibility(View.VISIBLE);
        tvMarketPrice.setText("");
        tvContentAddress.setText(flashSaleData.merchantName);
        tvLocation.setText(flashSaleData.merchantAddress);
        tvShopDespContent.setText(flashSaleData.introduction);
        llPrice.setVisibility(View.VISIBLE);

        if (null != flashSaleData.buyTime && null != flashSaleData.time) {

            //先判断当前是否可用购买
            if (Long.parseLong(flashSaleData.buyTime) > Long.parseLong(flashSaleData.time)) {
                tvNowBuy.setText("未开始");
                tvNowBuy.setBackgroundResource(R.drawable.bg_btn_style_green);
            } else {
                if ("0".equals(flashSaleData.goodsNum)) {
                    tvNowBuy.setText("已抢光");
                    tvNowBuy.setBackgroundResource(R.drawable.grey_border_btn_bg);

                } else {
                    tvNowBuy.setText("去抢购");
                    tvNowBuy.setBackgroundResource(R.drawable.merchant_bg_btn_style_red);
                }
            }
        }
        //1是配送
        goodsType = flashSaleData.goodsType;
        if (goodsType.equals("1")) {
            tvNotice.setText("同城快递配送到家");
        } else{
            tvNotice.setText("需到店使用，购后不退，过期不退");
        }
        if (!TextUtils.isEmpty(flashSaleData.merchantLatitude) && TextUtils.isEmpty(flashSaleData.merchantLongitude)) {
            double[] lation = CommonUtils.bd2gcj02(Double.parseDouble(flashSaleData.merchantLatitude),
                    Double.parseDouble(flashSaleData.merchantLongitude));
            Logger.i("flashSaleData.merchantLatitude " + flashSaleData.merchantLatitude);
            Logger.i("flashSaleData.merchantLongitude " + flashSaleData.merchantLongitude);
            Logger.i("lation " + lation[0] + "   " + lation[1]);
        }

        initViewPager(flashSaleData.albums);
        initAlbums(flashSaleData.albumsDetails);
    }

    private void initAlbums(List<String> albumsDetails) {
        llPhoto.removeAllViews();
        String imageUrl;
        for (int i = 0; i < albumsDetails.size(); i++) {
            imageUrl = albumsDetails.get(i);
            if (!TextUtils.isEmpty(imageUrl)) {
                if (!imageUrl.contains("http://") || !imageUrl.contains("https://")) {
                    imageUrl = Constants.ImageUrl + imageUrl + Constants.ImageSuffix;
                } else {
                    imageUrl = imageUrl + Constants.ImageSuffix;
                }
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext), ViewGroup.LayoutParams.WRAP_CONTENT);
//                图片宽度缩放到屏幕宽度，图片高度按照同样的比例进行缩放
                String finalImageUrl = imageUrl;
                Glide.with(mContext)
                        .load(finalImageUrl)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                imageView.setImageResource(R.mipmap.default_jp_banner);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (resource.getIntrinsicHeight() == resource.getIntrinsicWidth()) {
                                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                } else {
                                    int intrinsicWidth = resource.getIntrinsicWidth();
                                    int screenWidth = ScreenUtils.getScreenWidth(mContext);
                                    double value = (double) intrinsicWidth / screenWidth;
                                    Logger.i("value  " + value);
                                    if (value != 0.0) {
                                        params.height = (int) (resource.getIntrinsicHeight() / value);
                                        Logger.i("params.height  " + (resource.getIntrinsicHeight() / value));
                                    }
                                }
                                imageView.setLayoutParams(params);
                                return false;
                            }
                        }).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                llPhoto.addView(imageView, params);
                int finalI = i;

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageBrower(finalI, (String[]) albumsDetails.toArray(new String[albumsDetails.size()]));
                    }
                });
            }
        }
        llPhoto.setVisibility(View.VISIBLE);
        llPhoto.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(FlashSaleDetailsActivity.this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }


    private void initViewPager(List<String> albums) {
        bannerViewPager = new BannerViewPager(albums, imageLoader, options, mContext,true);
        viewpager.setAdapter(bannerViewPager);
        viewpager.setCurrentItem(1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.iv_call:
                callTel();
                break;
            case R.id.v3Share:
                share();
                break;
            case R.id.tv_now_buy:
                nowBuy();
                break;
            case R.id.tv_update_error:
                initData();
                break;
            case R.id.tv_select_address:
                openAMap();
                break;
        }
    }

    private void nowBuy() {
        if ("未开始".equals(tvNowBuy.getText().toString().trim()) || "已抢光".equals(tvNowBuy.getText().toString().trim())) {
            return;
        }

        Intent intent = null;
        if (TextUtils.isEmpty(goodsType)) {
            return;
        }
        FlashSaleTransferEntity flashSaleTransferEntity;
        FlashSaleDetailsEntity.DataBean.InfoBean info;
        switch (goodsType) {
            case "1": //配送
                info = flashSaleDetailsEntity.data.info;
                intent=new Intent(mContext, TakeOutFlashSalePayActivity.class);
                OrderSubmitGoods orderSubmitGoods=new OrderSubmitGoods();
                orderSubmitGoods.goodsPrice = Double.parseDouble(price);
                orderSubmitGoods.goodsPic = "";
                orderSubmitGoods.goodsCount = 1;
                orderSubmitGoods.name = goodsName;
                orderSubmitGoods.goodsId = flashSaleData.goodsId;
                orderSubmitGoods.type = Constants.FLASHSALEPAY;
                orderSubmitGoods.label = "";
                orderSubmitGoods.fregihtPrice=info.fregihtPrice;
                intent.putExtra("orderSubmitGoods", orderSubmitGoods);

                if (!isLogin()) {
                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                }
                startActivity(intent);
// TODO 服务器少返回字段               intent.putExtra("orderIndex",)
                Logger.i(" TakeOut  " + price + "\ngoodsName  " + goodsName + "\nfregihtPrice  " + fregihtPrice);
                break;
            case "2"://到店消费
                intent = new Intent(mContext, FlashSalePayActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("order_total_coupon", price);
                intent.putExtra("type", "FlashSalePay");
                 flashSaleTransferEntity = new FlashSaleTransferEntity();
                info = flashSaleDetailsEntity.data.info;
                flashSaleTransferEntity.buyTime = info.buyTime;
                flashSaleTransferEntity.goodsId = info.goodsId;
                flashSaleTransferEntity.goodsName = info.goodsName;
                flashSaleTransferEntity.goodsNum = info.goodsNum;
                flashSaleTransferEntity.introduction = info.introduction;
                flashSaleTransferEntity.merchantId = info.merchantId;
                flashSaleTransferEntity.merchantName = info.merchantName;
                flashSaleTransferEntity.price = info.price;
                if (info.albums.size() > 0) {
                    flashSaleTransferEntity.imageUrl = info.albums.get(0);
                }
                intent.putExtra("FlashSaleTransferEntity", flashSaleTransferEntity);
                if (!isLogin()) {
                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                }
                startActivity(intent);
                break;
            default:
                //到店消费
                intent = new Intent(mContext, FlashSalePayActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("order_total_coupon", price);
                intent.putExtra("type", "FlashSalePay");
                flashSaleTransferEntity = new FlashSaleTransferEntity();
                info = flashSaleDetailsEntity.data.info;
                flashSaleTransferEntity.buyTime = info.buyTime;
                flashSaleTransferEntity.goodsId = info.goodsId;
                flashSaleTransferEntity.goodsName = info.goodsName;
                flashSaleTransferEntity.goodsNum = info.goodsNum;
                flashSaleTransferEntity.introduction = info.introduction;
                flashSaleTransferEntity.merchantId = info.merchantId;
                flashSaleTransferEntity.merchantName = info.merchantName;
                flashSaleTransferEntity.price = info.price;
                if (info.albums.size() > 0) {
                    flashSaleTransferEntity.imageUrl = info.albums.get(0);
                }
                intent.putExtra("FlashSaleTransferEntity", flashSaleTransferEntity);
                if (!isLogin()) {
                    intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    private void share() {
        if (shareDialog == null) {
            shareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            shareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            shareContent,
                            shareTitle,
                            WebImageUtil.getInstance().getWebImageUrlNOSuffix(shareImageUrl),
                            shareUrl).share();
                }
            });

        }
        shareDialog.show();
    }

    private void callTel() {

        if (null != telephone && telephone.length() > 0) {
            showDialog(null, telephone, null, 0, Gravity.CENTER, "拨打", "取消", true, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephone));
                            startActivity(intent);
                            break;
                    }

                }
            }, mContext);
        } else {
            showToast("亲，这这家伙太懒了，还没留电话哦~");
        }
    }

    public void getShareUrl() {
        //新增的分享type值
        jpNetRequest.getShareUrl("12", goods_id, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        shareContent = responseInfo.result.content;
                        shareImageUrl = responseInfo.result.imgUrl;
                        String imageUrl = responseInfo.result.title;
                        shareUrl = responseInfo.result.url;
                        if (TextUtils.isEmpty(imageUrl)) {
                            shareImageUrl = "";
                        } else {
                            if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
                                shareImageUrl = imageUrl;
                            } else {
                                shareImageUrl = Constants.ImageUrl + imageUrl;
                            }
                        }
                        Logger.i("shareContent  " + shareContent + "\ndimagUrl  " + shareImageUrl + "\nshareTitle " + shareTitle + "\n url " + shareUrl);
                        break;
                    default:
                        Logger.i("分享错误 " + responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    //打开导航
    private void openAMap() {
        showToast(R.string.library_module_navi);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
