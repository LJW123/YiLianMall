package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.FlagshipAdapter;
import com.yilian.mall.adapter.FlagshipNearAdapter;
import com.yilian.mall.entity.FlagshipList;
import com.yilian.mall.entity.JPFragmentGoodEntity;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.LeFenHomeRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/21 0021.
 * 旗舰店列表
 */

public class JPFlagshipListActivity extends BaseActivity implements View.OnClickListener{
    private static final int LOADOTHERDATA = 1;//没有第一个列表数据
    private static final int NOMOREFIRSTDATA = 2;//第一个列表没有更多数据
    @ViewInject(R.id.sv_pull_refresh)
    PullToRefreshScrollView mScrollView;
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
    /**
     * 标题下面的图
     */
    @ViewInject(R.id.img_top)
    ImageView ivTop;
    /**
     * 周边精选GridView
     */
    @ViewInject(R.id.grid_near)
    NoScrollGridView topGridView;
    /**
     * 本地新品LinearLayout
     */
    @ViewInject(R.id.layout_location)
    LinearLayout ll_location;
    /**
     * 本地新品GridView
     */
    @ViewInject(R.id.grid_location)
    NoScrollGridView botGridView;
    /**
     *  周边精选
     */
    @ViewInject(R.id.ll_round_goods)
     LinearLayout llRoundGoods;

    @ViewInject(R.id.tv_nothing)
    TextView tvNothing;


    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private UmengDialog mShareDialog;
    String id,type;
    JPFragmentGoodEntity.DataBean goodsEntity;
    LeFenHomeRequest request;
    FlagshipList.DataBean bean;
    ArrayList<FlagshipList.DataBean.SuppliersBean> suppList = new ArrayList<>();
    FlagshipNearAdapter suppAdapter;
    JPNetRequest jpNetRequest;
    private ArrayList<JPGoodsEntity> goodList = new ArrayList<>();
    private FlagshipAdapter goodAdapter;
//    private boolean isFirstPage = true;
    private int firstPage = 0;
//    private boolean isSecondPage = false;
    private int secondPage = 0;
    private int count=30;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOADOTHERDATA://如果第一个列表完全没有数据，那么隐藏该列表 并停止请求该列表  开放请求第二个列表
                    isLoadSecond = true;
                    loadGood();
                    break;
            }
        }
    };
    //分享有关
    String share_type = "6"; // 6.本地旗舰店列表分享:#
    String share_title,share_content,share_img,share_url,shareImg;
    private boolean isLoadSecond=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagship_list);
        ViewUtils.inject(this);

        initView();
        firstPage=0;
        initData();
        initScrollView();
    }

    private void initView() {
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("index_id");
        Logger.i("本地旗舰店列表  type  "+type+"  id"+id);

        ll_location.setVisibility(View.GONE);
        tvNothing.setVisibility(View.GONE);

        topGridView.setFocusable(false);
        topGridView.setFocusableInTouchMode(false);
        botGridView.setFocusable(false);
        botGridView.setFocusableInTouchMode(false);

        tvTitle.setText("本地旗舰店");
        ivBack.setOnClickListener(this);
        ivLeft.setVisibility(View.INVISIBLE);
        ivShop.setOnClickListener(this);
        ivShop.setVisibility(View.VISIBLE);
        ivShare.setOnClickListener(this);
        ivShare.setVisibility(View.VISIBLE);


        request = new LeFenHomeRequest(mContext);
        jpNetRequest = new JPNetRequest(mContext);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.default_jp_banner) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.default_jp_banner)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_jp_banner) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                .imageScaleType(ImageScaleType.EXACTLY)
//              .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
                .build();

        suppAdapter = new FlagshipNearAdapter(mContext, suppList);
        topGridView.setAdapter(suppAdapter);
        topGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JPFlagshipListActivity.this, JPFlagshipActivity.class);
                intent.putExtra("index_id", suppList.get(position).supplierId);
                startActivity(intent);
            }
        });

        goodAdapter = new FlagshipAdapter(mContext, goodList);
        botGridView.setAdapter(goodAdapter);
        botGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JPFlagshipListActivity.this, JPNewCommDetailActivity.class);
                intent.putExtra("goods_id", goodList.get(position).JPGoodsId);
                intent.putExtra("filiale_id", goodList.get(position).JPFilialeId);
                intent.putExtra("tags_name", goodList.get(position).JPTagsName);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        request.getFlagshipList(type, id, firstPage,count, new RequestCallBack<FlagshipList>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }
            @Override
            public void onSuccess(ResponseInfo<FlagshipList> responseInfo) {
                switch (responseInfo.result.code){
                    case 1:
                        bean = responseInfo.result.data;
                        GlideUtil.showImageWithSuffix(mContext,bean.imageUrl,ivTop);
                        List<FlagshipList.DataBean.SuppliersBean> moreSuppList = bean.suppliers;
                        if (null != moreSuppList && moreSuppList.size()>=1){
                            llRoundGoods.setVisibility(View.VISIBLE);
                            ll_location.setVisibility(View.GONE);
                            topGridView.setVisibility(View.VISIBLE);
                            if (firstPage==0){
                                suppList.clear();
                                suppList.addAll(moreSuppList);
                            }else{
                                suppList.addAll(moreSuppList);
                            }
                            if (moreSuppList.size()<count) {
                                handler.sendEmptyMessage(LOADOTHERDATA);
                            }
                            suppAdapter.notifyDataSetChanged();
                        }else{
                            //没有数据请求下一个列表的数据
                            if (firstPage==0){
                                llRoundGoods.setVisibility(View.GONE);
                                topGridView.setVisibility(View.GONE);
                                handler.sendEmptyMessage(LOADOTHERDATA);
                            }else{
                                handler.sendEmptyMessage(LOADOTHERDATA);
                            }
                        }

                        //初始化时直接获取分享url
                        getShareUrl();

                        stopMyDialog();
                        mScrollView.onRefreshComplete();
                        break;
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                if (mScrollView != null) {
                    mScrollView.onRefreshComplete();
                }
            }
        });
    }

    private void loadGood(){
        jpNetRequest.getJPGoodsData(id, "5", "0,0,0,0", secondPage, new RequestCallBack<JPFragmentGoodEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPFragmentGoodEntity> responseInfo) {
                switch (responseInfo.result.code){
                    case 1:
                        mScrollView.onRefreshComplete();
                        if (null ==responseInfo.result || null ==responseInfo.result.JPShopData){
                            ll_location.setVisibility(View.GONE);
                            tvNothing.setVisibility(View.VISIBLE);
                        }else{
                            ll_location.setVisibility(View.VISIBLE);
                            JPFragmentGoodEntity.DataBean bean = responseInfo.result.JPShopData;
                            ArrayList<JPGoodsEntity> moreGoodList = bean.JPShopGoods;

                            if (null!= moreGoodList && moreGoodList.size()>=1){
                                if (secondPage==0){
                                    goodList.clear();
                                    goodList.addAll(moreGoodList);
                                }else{
                                    goodList.addAll(moreGoodList);
                                }
                                goodAdapter.notifyDataSetChanged();
                            }else{
                                if (secondPage==0){
                                    tvNothing.setVisibility(View.VISIBLE);
                                }else{
                                    showToast(R.string.no_more_data);
                                }
                            }
                        }

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                mScrollView.onRefreshComplete();
            }
        });
    }

    private void initScrollView() {
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);

        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                firstPage = 0;
                secondPage = 0;
                isLoadSecond=false;
                initData();
                handler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (!isLoadSecond) {
                    firstPage++;
                    initData();
                }else{
                    secondPage++;
                    loadGood();
                }
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
                    new ShareUtile(
                            mContext,
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
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.getShareUrl(share_type, id, new RequestCallBack<JPShareEntity>() {
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
        switch (v.getId()){
            case R.id.v3Back:
                JPFlagshipListActivity.this.finish();
                break;
            case R.id.v3Shop:
                Intent intent = new Intent(JPFlagshipListActivity.this, JPMainActivity.class);
                intent.putExtra("jpMainActivity", "jpShoppingCartFragment");
                startActivity(intent);
                break;
            case R.id.v3Share:
                share();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageLoader.clearDiscCache();
        imageLoader.clearMemoryCache();
    }
}
