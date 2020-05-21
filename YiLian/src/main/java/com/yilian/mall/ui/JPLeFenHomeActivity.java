package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.JPGoodsAdapter;
import com.yilian.mall.adapter.LefenHomeHozlvAdapter;
import com.yilian.mall.adapter.ViewPagerAdapter;
import com.yilian.mall.entity.JPFragmentGoodEntity;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.LeFenHome;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.LeFenHomeRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.widgets.HorizontalListView;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yilian.mall.R.id.ll_dianzhang;
import static com.yilian.mall.utils.CommonUtils.divideWidth;

/**
 * Created by Ray_L_Pain on 2016/10/18 0018.
 * 本地之家
 */

public class JPLeFenHomeActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private final int AUTO = 0;
    private final long delay = 3 * 1000;
    /**
     * 标题
     */
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    /**
     * 返回
     */
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Left)
    ImageView ivLeft;
    /**
     * 购物车
     */
    @ViewInject(R.id.v3Shop)
    ImageView ivShop;
    /**
     * 分享
     */
    @ViewInject(R.id.v3Share)
    ImageView ivShare;
    @ViewInject(R.id.sv_pull_refresh)
    PullToRefreshScrollView mScrollView;
    /**
     * 轮播图
     */
    @ViewInject(R.id.viewpager)
    ViewPager viewPager;
    @ViewInject(R.id.ll_points)
    LinearLayout points;
    /**
     * 分店LinearLayout
     */
    @ViewInject(R.id.ll_fendian)
    LinearLayout llFendian;
    /**
     * 分店LinearLayout
     */
    @ViewInject(ll_dianzhang)
    LinearLayout llDianzhang;
    /**
     * 内容
     */
    @ViewInject(R.id.tv_content_title)
    TextView tvContentTitle;
    @ViewInject(R.id.ratingBar)
    RatingBar ratingBar;
    @ViewInject(R.id.tv_content_grade)
    TextView tvContentNum;
    @ViewInject(R.id.tv_content_address)
    TextView tvContentAddress;
    @ViewInject(R.id.iv_call)
    ImageView ivCall;
    @ViewInject(R.id.tv_content_time)
    TextView tvContentTime;
    @ViewInject(R.id.tv_content_introduce)
    TextView tvContentIntro;
    /**
     * 水平的listview
     */
    @ViewInject(R.id.hListView)
    HorizontalListView hListView;
    /**
     * GridView
     */
    @ViewInject(R.id.girdView)
    NoScrollGridView gridView;
    /**
     * 没有合适的button
     */
    @ViewInject(R.id.btn_nothing)
    Button btnNo;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    LeFenHome.DataBean bean;
    ArrayList<LeFenHome.DataBean.ImageListBean> imgList;
    LeFenHomeRequest request;
    JPNetRequest jpRequest;
    private UmengDialog mShareDialog;
    private int pageIndex = 0;
    private ArrayList<String> urlString;
    private ArrayList<Image> bitmaps;
    private ImageView[] pointList;
    private int width;
    private int newWidth;
    private int padding;
    private ViewPagerAdapter vpAdpater;
    private String type; //0兑换中心详情 1分店
    private String id;  //兑换中心 id或者分店id
    private String tel; //兑换中心电话
    private float grade; //评分
    private ArrayList<LeFenHome.DataBean.ShopListBean> hozList = new ArrayList<>(); //水平的list
    private LefenHomeHozlvAdapter hAdapter; //水平的adapter

    private ArrayList<JPGoodsEntity> list = new ArrayList<>();
    private JPGoodsAdapter adapter;

    private Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){
                case AUTO:
                    int index = viewPager.getCurrentItem();
                    viewPager.setCurrentItem(index + 1);
                    mHandler.sendEmptyMessageDelayed(AUTO, delay);
                    break;

                default:
                    break;
            }
        }
    };
    //分享有关
    String share_type = "4"; //  4.本地商城分享:#
    String getedId;
    String share_title,share_content,share_img,share_url,shareImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lefen_jp);
        ViewUtils.inject(this);

        initView();
        initData();
        loadRecommend();
        initScrollView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        imageLoader.clearDiscCache();
        imageLoader.clearMemoryCache();
    }

    private void initScrollView() {
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);

        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex = 0;
                loadRecommend();
                mScrollView.onRefreshComplete();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageIndex++;
                loadRecommend();
                mScrollView.onRefreshComplete();
            }
        });
    }

    private void initView(){
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("index_id");

        tvTitle.setText("本地商城");
        ivBack.setOnClickListener(this);
        ivLeft.setVisibility(View.INVISIBLE);
        ivShop.setOnClickListener(this);
        ivShop.setVisibility(View.VISIBLE);
        ivShare.setOnClickListener(this);
        ivShare.setVisibility(View.VISIBLE);
        ivCall.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);

        width = getResources().getDisplayMetrics().widthPixels;
        newWidth = (int) (divideWidth(width, 1080, 6) * 17);
        padding = (int) (divideWidth(width, 1080, 6) * 7);
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
                .showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        //设置girdview
        adapter = new JPGoodsAdapter(mContext, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JPLeFenHomeActivity.this, JPNewCommDetailActivity.class);
                intent.putExtra("filiale_id", list.get(position).JPFilialeId);
                intent.putExtra("goods_id", list.get(position).JPGoodsId);
                intent.putExtra("tags_name", list.get(position).JPTagsName);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        String page = pageIndex+"";
        if(request == null){
            request = new LeFenHomeRequest(mContext);
        }
        request.getLeFenHomeMsg(type, id, page, new RequestCallBack<LeFenHome>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<LeFenHome> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code){
                    case 1:
                        bean = responseInfo.result.data;
                        getedId = bean.filialeId;
                        //初始化轮播图
                        imgList = new ArrayList();
                        for (int i = 0; i < bean.imageList.size(); i++){
                            LeFenHome.DataBean.ImageListBean img = new LeFenHome.DataBean.ImageListBean();
                            img.photoName = bean.imageList.get(i).photoName;
                            img.photoPath = bean.imageList.get(i).photoPath;
                            imgList.add(img);
                        }
                        getViewPager(imgList);

                        tvContentTitle.setText(bean.filialeName);
                        tvContentAddress.setText(bean.filialeAddress);
                        tvContentTime.setText(bean.shopWorktime);
                        tel = bean.filialeTel;
                        grade = (float) (Math.round((bean.filialeGraded / 10.0f)*10))/10;
                        ratingBar.setRating(grade == 0 ? (float) 0.0 : grade);
                        tvContentNum.setText(grade + "分");
                        tvContentIntro.setText(bean.filialeDesp);

                        //初始化分店的listview
                        hozList = bean.shop;
                        if(hozList.size() <= 0){
                            llFendian.setVisibility(View.GONE);
                        } else {
                            llFendian.setVisibility(View.VISIBLE);
                            //设置水平的listview
                            hAdapter = new LefenHomeHozlvAdapter(mContext, hozList);
                            hListView.setAdapter(hAdapter);
                            hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(JPLeFenHomeActivity.this, JPLeFenShopActivity.class);
                                    intent.putExtra("index_id", hozList.get(position).shopIndex);
                                    intent.putExtra("type", "1");
                                    startActivity(intent);
                                }
                            });
                        }

                        //初始化数据直接获取分享url
                        getShareUrl();
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    /**
     * 初始化vp
     * @param list 网络获取的vp集合
     */
    private void getViewPager(List<LeFenHome.DataBean.ImageListBean> list){
        bitmaps = new ArrayList<>();
        urlString = new ArrayList<>();
        if(list == null){
            return;
        }
        urlString.clear();
        bitmaps.clear();
        points.removeAllViews();
        mHandler.removeMessages(AUTO);
        if (list != null){
            for (int i = 0; i < list.size(); i++){
                urlString.add(list.get(i).photoPath);
            }
        }
        initPoint();
        initViewPager(list);
    }

    /**
     * 初始化vp下的点
     */
    private void initPoint(){
        pointList = new ImageView[urlString.size()];
        if (urlString.size() <= 0){
            return;
        }
        for (int i = 0; i < urlString.size(); i++){
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(newWidth, newWidth);
            lp.leftMargin = padding;
            lp.rightMargin = padding;
            lp.topMargin = padding;
            lp.bottomMargin = padding;
            iv.setLayoutParams(lp);
            iv.setImageResource(R.mipmap.circle_off);
            points.addView(iv);
            pointList[i] = iv;
        }
        pointList[0].setImageResource(R.mipmap.circle_on);
    }

    /**
     * 初始化vp
     * @param list 网络获取的图片集合
     */
    private void initViewPager(List<LeFenHome.DataBean.ImageListBean> list){
        int imgSize;
        if (list.size() <= 0){
            viewPager.setVisibility(View.GONE);
            return;
        }
        if(list.size() <= 3){
            imgSize = list.size() * 4;
        } else {
            imgSize = list.size();
        }
        ImageView[] imgs = new ImageView[imgSize];
        for (int i = 0; i<imgs.length; i++){
            ImageView iv = new ImageView(mContext);

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LeFenHome.DataBean.ImageListBean img = list.get(i % list.size());

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideUtil.showImageWithSuffix(mContext,img.photoPath,iv);
            imgs[i] = iv;
        }
        vpAdpater = new ViewPagerAdapter(imgs);
        viewPager.setAdapter(vpAdpater);
        mHandler.sendEmptyMessageDelayed(AUTO, delay);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        int count = urlString.size();
        if (count > 0) {
            setCurrentPoint(position % count);
        }
    }

    /**
     * 设置ViewPager当前的底部小点
     */
    private void setCurrentPoint(int currentPosition) {
        for (int i = 0; i < pointList.length; i++) {
            if (i == currentPosition) {
                pointList[i].setImageResource(R.mipmap.circle_on);
            } else {
                pointList[i].setImageResource(R.mipmap.circle_off);
            }
        }
    }

    /**
     * 加载店长推荐
     */
    public void loadRecommend(){
        if(jpRequest == null){
            jpRequest = new JPNetRequest(mContext);
        }
        jpRequest.getJPGoodsData(id, type, "0", pageIndex, new RequestCallBack<JPFragmentGoodEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPFragmentGoodEntity> responseInfo) {
                JPFragmentGoodEntity result = responseInfo.result;
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,result)){
                    if (CommonUtils.serivceReturnCode(mContext,result.code,result.msg)){
                        switch (result.code){
                            case 1:
                                ArrayList<JPGoodsEntity> moreList = result.JPShopData.JPShopGoods;
                                if(pageIndex == 0){
                                    list.clear();
                                }
                                if(moreList.size() > 0){
                                    list.addAll(moreList);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    if (pageIndex != 0) {
                                        showToast(R.string.no_more_data);
                                    }
                                }

                                if (list.size() == 0 || list == null) {
                                    llDianzhang.setVisibility(View.GONE);
                                } else {
                                    llDianzhang.setVisibility(View.VISIBLE);
                                }
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    /**
     * 分享
     */
    private void share(){
        if(mShareDialog == null){
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener(){

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            share_content,
                            shareImg,
                            share_url).share();
                }
            });
        }
        mShareDialog.show();
    }


    private void getShareUrl() {
        if (jpRequest == null) {
            jpRequest = new JPNetRequest(mContext);
        }
        jpRequest.getShareUrl(share_type, getedId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        String sub_title = responseInfo.result.subTitle;
                        share_content = responseInfo.result.content;
                        share_img = responseInfo.result.imgUrl;
                        share_url = responseInfo.result.url;
                        if (TextUtils.isEmpty(share_img)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg =  Constants.ImageUrl + share_img;
                            }
                        }

                        Logger.i("share_title"+share_title);
                        Logger.i("share_content"+share_content);
                        Logger.i("share_img"+share_img);
                        Logger.i("share_url"+share_url);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.v3Back:
                JPLeFenHomeActivity.this.finish();
                break;
            case R.id.v3Shop:
                intent = new Intent(this, JPMainActivity.class);
                intent.putExtra("jpMainActivity", "jpShoppingCartFragment");
                startActivity(intent);
                break;
            case R.id.v3Share:
                share();
                break;
            case R.id.iv_call:
                if (TextUtils.isEmpty(tel)) {
                    showToast(R.string.no_phone);
                } else {
                    intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + tel);
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
        }
    }

}
