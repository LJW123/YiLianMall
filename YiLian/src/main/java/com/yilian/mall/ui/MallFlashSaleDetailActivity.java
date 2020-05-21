package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.listener.onLoadErrorListener;
import com.yilian.mall.ui.fragment.MallFlashDetailBottomFragment;
import com.yilian.mall.ui.fragment.MallFlashSaleDetailTopFragment;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.widgets.DragLayout;
import com.yilian.mylibrary.Constants;

public class MallFlashSaleDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private FrameLayout viewFirst;
    private FrameLayout viewSecond;
    private DragLayout draglayout;
    private ImageView ivBack;
    private RelativeLayout layoutTitle;
    private ImageView ivBackBottom;
    private RelativeLayout layoutTitleBottom;
    private View viewShadow;
    private TextView tvUpdateError;
    private LinearLayout llErrorView;
    private RelativeLayout layoutAll;
    private MallFlashSaleDetailTopFragment topFragment;
    public String goodsId;
    private MallFlashDetailBottomFragment bottomFragment;
    public int which = 0;
    public String urlOne, urlTwo;
    public String filialeId;
    public String picId;
    private ImageView ivShare;
    private ImageView ivShareBottom;
    private String shareContent;
    private String shareImgUrl;
    private String shareTitle;
    private String shareUrl;
    private String shareSubTitle;
    private String type = "12";
    private UmengDialog umengDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sale_mall_detailes);
        goodsId = getIntent().getStringExtra("goods_id");
        filialeId = "1";//给固定值“1”
        initView();
        initContent();
        getShareUrl();
    }

    private void initContent() {
        topFragment = new MallFlashSaleDetailTopFragment();
        switchContentFragment();
        bottomFragment = new MallFlashDetailBottomFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.view_first, topFragment)
                .add(R.id.view_second, bottomFragment).commit();
        DragLayout.ShowNextPageNotifier next = new DragLayout.ShowNextPageNotifier() {

            @Override
            public void onDragNext() {
                bottomFragment.initView(which);
                layoutTitle.setVisibility(View.GONE);
                layoutTitleBottom.setVisibility(View.VISIBLE);
            }
        };
        draglayout.setNextPageListener(next);
        DragLayout.ShowTopPageNotifier topPageNotifier = new DragLayout.ShowTopPageNotifier() {
            @Override
            public void onDragTop() {
                layoutTitle.setVisibility(View.VISIBLE);
                layoutTitleBottom.setVisibility(View.GONE);
            }
        };
        draglayout.setTopPageListener(topPageNotifier);
    }

    private void switchContentFragment() {
        topFragment.setOnUploadListener(new onLoadErrorListener() {
            @Override
            public void isError(boolean isError) {
                Logger.i("isError  " + isError);
                if (isError) {
                    draglayout.setVisibility(View.GONE);
                    llErrorView.setVisibility(View.VISIBLE);
                } else {
                    llErrorView.setVisibility(View.GONE);
                    draglayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        viewFirst = (FrameLayout) findViewById(R.id.view_first);
        viewSecond = (FrameLayout) findViewById(R.id.view_second);
        draglayout = (DragLayout) findViewById(R.id.draglayout);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        ivBackBottom = (ImageView) findViewById(R.id.iv_back_bottom);
        layoutTitleBottom = (RelativeLayout) findViewById(R.id.layout_title_bottom);
        viewShadow = (View) findViewById(R.id.view_shadow);
        tvUpdateError = (TextView) findViewById(R.id.tv_update_error);
        llErrorView = (LinearLayout) findViewById(R.id.llUpdataError);
        layoutAll = (RelativeLayout) findViewById(R.id.layout_all);

        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivShareBottom = (ImageView) findViewById(R.id.iv_share_bottom);


        ivBack.setOnClickListener(this);
        ivBackBottom.setOnClickListener(this);
        tvUpdateError.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivShareBottom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.iv_back_bottom:
                finish();
                break;
            case R.id.tv_update_error:
                //调用fragment的方法
                topFragment.loadData();
                break;
            case R.id.iv_share:
            case R.id.iv_share_bottom:
                showUMengDialog();
                break;
        }
    }

    private void showUMengDialog() {

        if (umengDialog == null) {
            umengDialog = new UmengDialog(context, AnimationUtils.loadAnimation(context, R.anim.anim_wellcome_bottom),
                    R.style.DialogControl, new UmengGloble().getAllIconModels());
            umengDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            context,
                            ((IconModel) arg4).getType(),
                            shareContent,
                            shareTitle,
                            shareImgUrl,
                            shareUrl).share();
                }
            });
        }
        umengDialog.show();

    }

    public void getShareUrl() {

        JPNetRequest request = new JPNetRequest(context);
        request.getShareUrl(type, goodsId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        shareContent = responseInfo.result.content;
                        shareImgUrl = responseInfo.result.imgUrl;
                        if (!TextUtils.isEmpty(shareImgUrl)) {
                            if (!shareImgUrl.contains("http://") || !shareImgUrl.contains("https://")) {
                                shareImgUrl = Constants.ImageUrl + shareImgUrl + Constants.ImageSuffix;
                            } else {
                                shareImgUrl = shareImgUrl + Constants.ImageSuffix;
                            }
                        }
                        shareTitle = responseInfo.result.title;
                        shareUrl = responseInfo.result.url;
                        shareSubTitle = responseInfo.result.subTitle;
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
