package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.ActSignInSuccessEntity;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ActSignInSuccessDialogActivity extends BaseDialogActivity {

    private ImageView ivClose;
    private TextView tvSignInResult;
    private RelativeLayout rlShare;
    private String getIntegral;
    private int days;
    private ActSignInSuccessEntity data;
    /**
     * 是否是第一次请求分享--默认是true
     */
    private boolean isFirstGetShare=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_sign_in_success_dialog);
        initView();
        initData();
    }

    private void initData() {
        data=getIntent().getParcelableExtra("data");
        days = data.days;
        if (data.weekIntegral==0){
            getIntegral =data.integral;
        }else {
            getIntegral=data.weekIntegral+"";
        }
        if (TextUtils.isEmpty(getIntegral)){
            getIntegral="0.00";
        }else {
            if (!getIntegral.contains(".")){
                getIntegral=getIntegral+".00";
            }
        }
        getShareUrl();

        setNoticeWord1();
    }

    private void setNoticeWord1() {
        if (data.weekIntegral==0){
            String wordSpandString="获得" + getIntegral + "奖券，已连续"+days+"天签到";
            Spannable wordtoSpan = new SpannableString(wordSpandString);
            int integralEndIndex;
            int dayStartIndex=wordSpandString.indexOf("续");
            integralEndIndex=getIntegral.indexOf(".");
            wordtoSpan.setSpan(new AbsoluteSizeSpan(16,true), 2, 2+integralEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#DE9418")),2,2+getIntegral.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#DE9418")),dayStartIndex+1,wordSpandString.length()-3,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSignInResult.setText(wordtoSpan);
        }else{
            //连续七天签到
            String wordSpandString="累计"+getIntegral+"奖券已到账，坚持签到赢更多奖券";
            int endIndex=getIntegral.indexOf(".");
            Spannable wordtoSpan = new SpannableString(wordSpandString);
            wordtoSpan.setSpan(new AbsoluteSizeSpan(16,true), 2, 2+endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#DE9418")),2,2+getIntegral.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSignInResult.setText(wordtoSpan);
        }
    }

    private void setNoticeWord(){
        if (days==1){//提示签到成功

        }else if (days>1){
            String wordSpandString="获得" + getIntegral + "奖券，已连续"+days+"天签到";
            Spannable wordtoSpan = new SpannableString(wordSpandString);
            int integralEndIndex;
            int dayStartIndex=wordSpandString.indexOf("续");
            integralEndIndex=getIntegral.indexOf(".");
            wordtoSpan.setSpan(new AbsoluteSizeSpan(16,true), 2, 2+integralEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#DE9418")),2,2+getIntegral.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#DE9418")),dayStartIndex+1,wordSpandString.length()-3,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSignInResult.setText(wordtoSpan);
        }

    }

    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        RxUtil.clicks(ivClose, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                finish();
            }
        });
        tvSignInResult = (TextView) findViewById(R.id.tv_sign_in_result);
        rlShare = (RelativeLayout) findViewById(R.id.rl_share);
        RxUtil.clicks(rlShare, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (!TextUtils.isEmpty(share_content)){
                    share();
                }else {
                   againGetShareUrl();
                }
//                finish();
//                startActivity(new Intent(mContext, ActInvitationFriendActivity.class));
            }
        });
    }
    //分享有关
    String share_type = "19";
    String share_title,share_content,share_img,share_url,shareImg;
    private UmengDialog mShareDialog;
    private JPNetRequest jpNetRequest;
    private void againGetShareUrl(){
        isFirstGetShare=false;
        startMyDialog(false);
        getShareUrl();
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
                        share_content = responseInfo.result.content;
                        String sub_title = responseInfo.result.subTitle;
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
                        if (!isFirstGetShare){
                            share();
                        }
                        break;
                    default:
                        break;
                }
                if (!isFirstGetShare){
                    stopMyDialog();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                if (!isFirstGetShare){
                   stopMyDialog();
                }
            }
        });
    }

    private void share() {
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
}
