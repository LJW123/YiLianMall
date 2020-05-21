package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MerchantListAdapter;
import com.yilian.mall.adapter.ViewPagerAdapter;
import com.yilian.mall.entity.InvateEntity;
import com.yilian.mall.entity.MerchantEntity;
import com.yilian.mall.http.InvateNetRequest;
import com.yilian.mall.http.NearbyNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;

//import com.amap.api.location.LocationManagerProxy;

/**
 * 原来的 联盟商家和兑换中心 共用的详情界面
 * 现在已改为兑换中心专用，联盟商家页面改为了ShopsActivity
 */
public class MerchantActivity extends BaseActivity implements OnPageChangeListener {
    @ViewInject(R.id.tv_shop_desp)
    private TextView tvShopDesp;// 商家详情

    @ViewInject(R.id.tv_merchant_address)
    private TextView tvMerchantAddress;// 地址

    @ViewInject(R.id.tv_shopping_time)
    private TextView tvWorkTime;// 工作时间

    @ViewInject(R.id.ratingBar_grade)
    private RatingBar merchantRatingBarGraded;// 星级评分

    @ViewInject(R.id.tv_grade)
    private TextView tvGraded; // 文字评分

    @ViewInject(R.id.tv_merchant_name)
    private TextView tvMerchantName; // 名字

//    @ViewInject(R.id.tv_back)
//    private TextView tvBack;

    @ViewInject(R.id.iv_share)
    private ImageView btnShare;

    @ViewInject(R.id.viewpager)
    private ViewPager mViewpager;

    @ViewInject(R.id.lls_points)
    private LinearLayout layoutDots;

    @ViewInject(R.id.iv_no_data)
    private ImageView ivNoData;

    @ViewInject(R.id.sv_data)
    private PullToRefreshScrollView svData;

    @ViewInject(R.id.listview)
    private NoScrollListView listview;

    private ArrayList<String> urlString;
    private ArrayList<Bitmap> bitmaps;
    private ViewPagerAdapter mViewPagerAdp;
    private final long delay = 3 * 1000;
    private final int AUTO = 0;
    private ImageView[] mDots;
    private int width;
    private int newWidth;
    private int padding;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    NearbyNetRequest nearbyNetRequest;
    private Animation animBottom;
    private InvateNetRequest request;
    private String shareUrl;

    String type;
    String id;

    private int page = 0;
    private MerchantEntity.Info merchInfo;
    private ArrayList<MerchantEntity.ListEntity> list = new ArrayList<>();
    private MerchantListAdapter adapter;
    private String recommend;

    /**
     * 联盟商家详情
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);
        ViewUtils.inject(this);
        init();
        initListener();
        initAdapter();
        svData.scrollTo(0, 0);
        getData();
        getShareUrl();
    }

    private void initAdapter() {
        adapter = new MerchantListAdapter(mContext, list);
        listview.setAdapter(adapter);

        listview.setFocusable(false);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MerchantEntity.ListEntity goods = (MerchantEntity.ListEntity) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);

                intent.putExtra("goods_id", goods.goods_index);
                intent.putExtra("filiale_id", merchInfo.shop_filiale_id);

                startActivity(intent);
            }
        });
    }

    private void initListener() {
        /**
         * 分享
         */
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animBottom = AnimationUtils.loadAnimation(mContext, R.anim.anim_wellcome_bottom);
                UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
                        new UmengGloble().getAllIconModels());
                dialog1.setItemLister(new UmengDialog.OnListItemClickListener() {

                    @Override
                    public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                        String content = getResources().getString(R.string.appshare);
                        new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, shareUrl).share();
                    }
                });

                dialog1.show();

            }
        });

        svData.setMode(PullToRefreshBase.Mode.BOTH);
        svData.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page++;
                getData();
            }
        });

    }

    private void getShareUrl() {
        if (request == null) {
            request = new InvateNetRequest(mContext);
        }
        request.invateNet(new RequestCallBack<InvateEntity>() {
            @Override
            public void onSuccess(ResponseInfo<InvateEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        shareUrl = responseInfo.result.url;
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void init() {
        nearbyNetRequest = new NearbyNetRequest(mContext);
        width = getResources().getDisplayMetrics().widthPixels;

        // 获取上一个要获取信息的类型 id标示（type == 1 时为联盟商家 2时为兑换中心）
        Intent intent = getIntent();
        type = intent.getStringExtra("itemType");
        id = intent.getStringExtra("merchant_id");
        String recommend = intent.getStringExtra("recommend");
        this.recommend = recommend == null ? "0" : recommend;

        newWidth = (int) (divideWidth(width, 1080, 6) * 17);
        padding = (int) (divideWidth(width, 1080, 6) * 9);
        mViewpager.setOnPageChangeListener(this);
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.jiazaizhong)
                .showImageForEmptyUri(R.mipmap.jiazaizhong).showImageOnFail(R.mipmap.jiazaizhong)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public double divideWidth(int screenWidth, int picWidth, int retainValue) {
        BigDecimal screenBD = new BigDecimal(Double.toString(screenWidth));
        BigDecimal picBD = new BigDecimal(Double.toString(picWidth));
        return screenBD.divide(picBD, retainValue, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    // 导航 我要到这去
    public void goTo(View view) {
        showToast(R.string.library_module_navi);
    }

    @Override
    public void onBack(View view) {
        finish();
    }

    /**
     * 联系商户 电话
     */
    public void callTel(View view) {

        final String tel = merchInfo.shop_tel;
        if (tel != null && tel.length() > 0) {
            showDialog(null, tel, null, 0, Gravity.CENTER, "拨打", "取消", true,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    }, mContext);

        } else {
            showToast("亲,这家太懒了,没留电话哦!");
        }

    }

    /**
     * 初始化ViewPager的底部小点
     */
    private void initDots() {
        if (urlString.size() <= 0) {
            return;
        }
        mDots = new ImageView[urlString.size()];
        for (int i = 0; i < urlString.size(); i++) {
            ImageView iv = new ImageView(mContext);
            LayoutParams lp = new LayoutParams(newWidth, newWidth);
            lp.leftMargin = padding;
            lp.rightMargin = padding;
            lp.topMargin = padding;
            lp.bottomMargin = padding;
            iv.setLayoutParams(lp);
            iv.setImageResource(R.mipmap.circle_off);
            layoutDots.addView(iv);
            mDots[i] = iv;
        }
        mDots[0].setImageResource(R.mipmap.circle_on);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        int imgSize;
        if (urlString.size() <= 0) {
            mViewpager.setVisibility(View.GONE);
            return;
        }
        if (urlString.size() <= 3) {
            imgSize = urlString.size() * 4;
        } else {
            imgSize = urlString.size();
        }
        ImageView[] imgs = new ImageView[imgSize];
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(mContext);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            imageLoader.displayImage(urlString.get(i % urlString.size()), iv, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                case DECODING_ERROR:
                                case NETWORK_DENIED:
                                case OUT_OF_MEMORY:
                                case UNKNOWN:
                                    message = "图片加载错误";
                                    break;
                            }
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ((ImageView) view).setScaleType(ScaleType.CENTER_CROP);
                        }
                    });
            imgs[i] = iv;
        }

        mViewPagerAdp = new ViewPagerAdapter(imgs);
        mViewpager.setAdapter(mViewPagerAdp);
        // mViewpager.setCurrentItem(10*imgSize);
        mHandler.sendEmptyMessageDelayed(AUTO, delay);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {

            switch (msg.what) {
                case AUTO:
                    int index = mViewpager.getCurrentItem();
                    mViewpager.setCurrentItem(index + 1);
                    mHandler.sendEmptyMessageDelayed(AUTO, delay);
                    break;

                default:
                    break;
            }

        }

        ;
    };

    /**
     * 设置ViewPager当前的底部小点
     */
    private void setCurrentDot(int currentPosition) {

        for (int i = 0; i < mDots.length; i++) {
            if (i == currentPosition) {
                mDots[i].setImageResource(R.mipmap.circle_on);
            } else {
                mDots[i].setImageResource(R.mipmap.circle_off);
            }
        }
    }

    public void getData() {
        final int width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();//获取屏幕宽度
        startMyDialog();
        nearbyNetRequest.getMerchantDetail(recommend, page, id, new RequestCallBack<MerchantEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MerchantEntity> responseInfo) {
                stopMyDialog();
                svData.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:
                        urlString = new ArrayList<String>();
                        bitmaps = new ArrayList<Bitmap>();
                        urlString.clear();
                        bitmaps.clear();
                        layoutDots.removeAllViews();
                        mHandler.removeMessages(AUTO);
                        merchInfo = responseInfo.result.info;
                        if (page == 0) {
                            list.clear();
                        }
                        list.addAll(responseInfo.result.list);
                        adapter.notifyDataSetChanged();
                        for (int i = 0; i < merchInfo.images.size(); i++) {
                            urlString.add(Constants.ImageUrl + merchInfo.images.get(i).photo_path + "?x-oss-process=image/resize,limit_0," + "w_" + width / 2 + ",h_" + width / 2 + ",m_fill");
                        }
                        initView(merchInfo);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                svData.onRefreshComplete();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    void initView(MerchantEntity.Info merchInfo) {

        if (merchInfo.shop_type.equals("0")) {
            tvMerchantName.setText(merchInfo.shop_name + "(分店)");
        } else if (merchInfo.shop_type.equals("1")) {
            tvMerchantName.setText(merchInfo.shop_name + "(总店)");
        }
        tvWorkTime.setText(merchInfo.shop_worktime == null ? "暂无时间" : merchInfo.shop_worktime);
        tvMerchantAddress.setText(merchInfo.shop_address == null ? "暂无地址" : merchInfo.shop_address);
        merchantRatingBarGraded.setRating(Float.parseFloat(merchInfo.graded));
        tvGraded.setText(NumberFormat.convertToFloat(merchInfo.graded, 0f) / 10 + "分");
        tvShopDesp.setText(merchInfo.shop_desp);
        double[] latlon = CommonUtils.bd2gcj02(Double.parseDouble(merchInfo.shop_latitude), Double.parseDouble(merchInfo.shop_longitude));

        initDots();
        initViewPager();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {

        if (urlString.size() > 0) {
            setCurrentDot(arg0 % urlString.size());
        }

    }

    //返回键处理事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 删除导航监听
    }
}
