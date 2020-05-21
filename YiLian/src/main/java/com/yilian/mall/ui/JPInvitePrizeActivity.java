package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mylibrary.QRCodeUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;

/**
 * Created by liuyuqi on 2016/11/14 0014.
 * 分享好友
 */
public class JPInvitePrizeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView specification;
    private ImageView ivInviteShare;
    private UmengDialog mShareDialog;
    private JPNetRequest jpNetRequest;
    //分享有关
    String share_type = "9"; // 9.邀请有奖分享
    String share_title,share_content,share_img,share_url,shareImg;
    private ImageView rqCode;
    private TextView tvName;
    private ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jp_invite_friend);
        if (!isLogin()){
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
            return;
        }
        initView();
        getShareUrl();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        specification = (TextView) findViewById(R.id.specification);
        ivInviteShare = (ImageView) findViewById(R.id.iv_invite_share);
        rqCode = (ImageView) findViewById(R.id.iv_rqcode);
        ViewGroup.LayoutParams layoutParams = rqCode.getLayoutParams();
        //动态计算宽高
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        double imageWidth = screenWidth * 0.43;
        layoutParams.height= (int) imageWidth;
        layoutParams.width= (int) imageWidth;
        tvName = (TextView) findViewById(R.id.tv_name);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        String headUrl = PreferenceUtils.readStrConfig(Constants.USERURL, mContext, "");
        Logger.i("Constants  "+PreferenceUtils.readStrConfig(Constants.USERURL, mContext, "")  +" "+headUrl);
        if (!TextUtils.isEmpty(headUrl)){
//            QRCodeUtils.createImage(headUrl,rqCode);
            QRCodeUtils.createQRLogoImage(headUrl,rqCode.getWidth(),rqCode.getHeight(), BitmapFactory.decodeResource(getResources(),R.mipmap.yilianlogo),rqCode);
        }else{
            rqCode.setVisibility(View.INVISIBLE);
        }

        String imageUrl = PreferenceUtils.readStrConfig(Constants.PHOTO, mContext, "");
        if (!TextUtils.isEmpty(imageUrl)){
            if (!imageUrl.contains("http://")||!imageUrl.contains("https://")){
                imageUrl=Constants.ImageUrl+imageUrl+Constants.ImageSuffix;
            }else{
                imageUrl=imageUrl+Constants.ImageSuffix;
            }
            imageLoader.displayImage(imageUrl,ivIcon);
        }else{
            ivIcon.setImageResource(R.mipmap.userinfor_photo);
        }
        Logger.i("NAME   "+PreferenceUtils.readStrConfig("name",mContext,""));
        String name = PreferenceUtils.readStrConfig("name", mContext, "");
        if (TextUtils.isEmpty(name)){
            tvName.setText("暂无昵称");
        }else {
            tvName.setText(name);
        }

        ivBack.setOnClickListener(this);
        specification.setOnClickListener(this);
        ivInviteShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.specification:
                //跳转web
                jumpWeb();
                break;
            case R.id.iv_invite_share:
                shareGoods();
                break;
        }
    }

    private void jumpWeb() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", Ip.getHelpURL() + "agreementforios/style_share.html");
        startActivity(intent);

    }

    private void shareGoods() {
        if (mShareDialog == null) {

            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

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
        jpNetRequest.getShareUrl(share_type, "", new RequestCallBack<JPShareEntity>() {
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


}
