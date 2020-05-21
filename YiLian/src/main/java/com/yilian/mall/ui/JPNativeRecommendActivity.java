package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshGridView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.JPGoodsAdapter;
import com.yilian.mall.entity.InvateEntity;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPSubClassfiyGoodsEntity;
import com.yilian.mall.http.InvateNetRequest;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuqi on 2016/10/20 0020.
 * 本地精品推荐
 */

public class JPNativeRecommendActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private RadioButton rb_default;
    private CheckBox cbSortHasData;
    private PullToRefreshGridView mGridView;
    private RadioButton rbPrice;
    private JPNetRequest netRequest;
    private String filiale_id;
    private int page;
    private int count;
    private String sort;
    private JPGoodsAdapter adapter;
    private List<JPGoodsEntity> jpGoodsEntityList = new ArrayList<>();
    private ImageView back;
    private ImageView share;
    private ImageView shopCar;
    private UmengDialog mShareDialog;
    private InvateNetRequest request;
    private String shareUrl;
    private ImageView ivNothing;
    private int isFirst = 0;
    private ImageView v3Left;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.jp_activity_native_recommend);
        initView();
        initListener();
        getShareUrl();
    }

    private void initView() {
        mContext = this;
        filiale_id = getIntent().getStringExtra("filiale_id");
        tv_title = (TextView) findViewById(R.id.v3Title);
        back = (ImageView) findViewById(R.id.v3Back);
        shopCar = (ImageView) findViewById(R.id.v3Shop);
        shopCar.setVisibility(View.VISIBLE);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        shopCar.setImageDrawable(getResources().getDrawable(R.mipmap.iv_shopingcar));
        shopCar.setOnClickListener(this);
        share = (ImageView) findViewById(R.id.v3Share);
        share.setVisibility(View.VISIBLE);
        share.setImageDrawable(getResources().getDrawable(R.mipmap.iv_shear));
        share.setOnClickListener(this);
        rbPrice = (RadioButton) findViewById(R.id.rb_price);
        rb_default = (RadioButton) findViewById(R.id.rb_default);
        rb_default.setChecked(true);
        rb_default.setTextColor(getResources().getColor(R.color.red));
        cbSortHasData = (CheckBox) findViewById(R.id.cb_sort_has_data);
        mGridView = (PullToRefreshGridView) findViewById(R.id.class_gv);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        tv_title.setText("本地精品推荐");
        if (adapter == null) {
            adapter = new JPGoodsAdapter(mContext,jpGoodsEntityList);
        }
        mGridView.setAdapter(adapter);
        back.setOnClickListener(this);
        mGridView.setMode(PullToRefreshBase.Mode.BOTH);//设置支持上拉加载更多跟下拉刷新
        sort = "100";
        page = 0;
        count = 40;

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//              .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
                .build();
        initDate();
    }

    //初始化gridView适配器
    private void initDate() {
        //请求网络的时候去增加逗号
        StringBuffer sb = new StringBuffer();
        for (int i = 0, count = sort.length(); i < count; i++) {
            if (i < count - 1) {
                sb.append(sort.charAt(i) + ",");
            } else {
                sb.append(sort.charAt(i));
            }
        }

        Logger.i("本地精品推荐sort" + sb.toString());
        isFirst++;
        if (netRequest == null) {
            netRequest = new JPNetRequest(this);
        }
        startMyDialog();
        netRequest.getNativeRecommendList(filiale_id, sb.toString(), page, count, new RequestCallBack<JPSubClassfiyGoodsEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPSubClassfiyGoodsEntity> responseInfo) {
                stopMyDialog();
                mGridView.onRefreshComplete();//刷新完成
                JPSubClassfiyGoodsEntity.DataBean dataBean = responseInfo.result.data;
                ArrayList<JPGoodsEntity> goodsList = dataBean.goods;
                if (responseInfo.result == null || goodsList.size() == 0 && isFirst == 1) {
                    ivNothing.setVisibility(View.VISIBLE);
                    mGridView.setVisibility(View.GONE);
                    return;
                } else if (responseInfo.result == null || goodsList.size() == 0) {
                    showToast("没有更多数据");
                    return;
                }
                switch (responseInfo.result.code) {
                    case 1:
                        if (page == 0 && jpGoodsEntityList != null) {
                            jpGoodsEntityList.clear();
                        }
                        if (dataBean.goods != null && dataBean.goods.size() != 0) {
                            jpGoodsEntityList.addAll(dataBean.goods);
                            adapter.notifyDataSetChanged();
                        }

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });

    }


    private void initListener() {

        //是否有货 作为排序条件监听
        cbSortHasData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.hasgoods), null);
                    cbSortHasData.setTextColor(getResources().getColor(R.color.color_red));
                    sort = sort.substring(0, sort.length() - 1) + "1";
                } else {
                    cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.no_hasgoods), null);
                    cbSortHasData.setTextColor(getResources().getColor(R.color.color_h1));
                    sort = sort.substring(0, sort.length() - 1) + "0";
                }
                page = 0;
                initDate();
            }
        });
        rbPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbPrice.setTextColor(getResources().getColor(R.color.red));
                rb_default.setTextColor(getResources().getColor(R.color.color_h1));
                setPrice();
            }
        });

        rb_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                rbPrice.setTextColor(getResources().getColor(R.color.color_h1));
                rb_default.setTextColor(getResources().getColor(R.color.color_red));
                String subEnd = sort.substring(sort.length() - 1);
                sort = "10" + subEnd;
                page = 0;
                initDate();
            }
        });
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override//拉上加载更多
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 0;
                initDate();
            }

            @Override //下拉刷新
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                initDate();
            }
        });

    }

    //价格的排序
    private void setPrice() {
        sort = sort.replace(",", "");//处理sort之前先把逗号去掉，在进行网络请求时再加上
        String substring = sort.substring(0, sort.length() - 1);
        String subEnd = sort.substring(sort.length() - 1);
        if ("01".equals(substring)) {
            sort = "00" + subEnd;
            rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);
        } else {
            sort = "01" + subEnd;
            rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
        }
        Logger.i("价格只看有货排序" + sort);
        page = 0;
        initDate();
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Shop:
                //购物车
                Intent intent = new Intent(this.mContext, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                startActivity(intent);
                break;
            case R.id.v3Share:
                shareGoods();
                break;
        }
    }



    private void shareGoods() {
        if (mShareDialog == null) {

            mShareDialog = new UmengDialog(this.mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    String content = getResources().getString(R.string.appshare);
                    new ShareUtile(JPNativeRecommendActivity.this.mContext, ((IconModel) arg4).getType(), content, null, shareUrl).share();

                }
            });
        }
        mShareDialog.show();
    }

    private void getShareUrl() {
        if (request == null) {
            request = new InvateNetRequest(this.mContext);
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
                showToast("请求分享URL失败，请重试");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (request != null) {
            request = null;
        }
    }
}
