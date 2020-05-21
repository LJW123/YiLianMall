package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.WheelOfFortuneResultEntity;

public class WheelOfFortuneDialogActivity extends BaseActivity implements View.OnClickListener {

    private Button btnClose;
    private ImageView ivWheelPrize;
    private ImageButton ibTryAgainOrGetPrize;
    private ImageButton ibShare;
    private WheelOfFortuneResultEntity.DataBean wheelPrizeData;
    private ImageView ivBackGround;
    private TextView tvPrizeName;
    private String content;
    private String imgUrl;
    private String title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_of_fortune_dialog);
        initView();
        initData();
    }

    private void initData() {



        wheelPrizeData = (WheelOfFortuneResultEntity.DataBean) getIntent().getSerializableExtra("data");
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(wheelPrizeData.image), ivWheelPrize);
        if ("2".equals(wheelPrizeData.type)) {//显示立即领取图标
            ibTryAgainOrGetPrize.setImageResource(R.mipmap.icon_get_prize);
        } else {
        }
        tvPrizeName.setText(wheelPrizeData.name);
        getShareUrl(wheelPrizeData.prizeId);
    }

    private void initView() {
        tvPrizeName = (TextView) findViewById(R.id.tv_prize_name);
        btnClose = (Button) findViewById(R.id.btn_clost);
        ivWheelPrize = (ImageView) findViewById(R.id.iv_wheel_prize);
        ibTryAgainOrGetPrize = (ImageButton) findViewById(R.id.ib_try_again_or_get_prize);
        ibShare = (ImageButton) findViewById(R.id.ib_share);

        btnClose.setOnClickListener(this);
        ibTryAgainOrGetPrize.setOnClickListener(this);
        ibShare.setOnClickListener(this);
    }

    private JPNetRequest jpNetRequest;
    private UmengDialog mShareDialog;
    public void share(){
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(this.mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            title,
                            content,
                            WebImageUtil.getInstance().getWebImageUrlNOSuffix(imgUrl),
                            url)
                            .share();
                }
            });
        }
        mShareDialog.show();
    }

    private void getShareUrl(int prizeId) {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.getShareUrl("13", "" + prizeId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        content = responseInfo.result.content;
                        imgUrl = responseInfo.result.imgUrl;
                        title = responseInfo.result.title;
                        url = responseInfo.result.url;
                        Logger.i("content  " + content + "\\dimagUrl  " + imgUrl + "\\dtitle " + title + "\\d url " + url);
                        stopMyDialog();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clost:
                finish();
                break;
            case R.id.ib_try_again_or_get_prize:

                if ("2".equals(wheelPrizeData.type)) {//跳转到中奖列表
                    startActivity(new Intent(mContext, PrizeGoodsListActivity.class));
                } else {//再来一次
                    setResult(200);
                }
                finish();
                break;
            case R.id.ib_share:
//                分享
                share();

                break;
        }
    }


}
