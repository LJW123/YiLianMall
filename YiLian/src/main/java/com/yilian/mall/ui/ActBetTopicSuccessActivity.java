package com.yilian.mall.ui;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mylibrary.Constants;

/**
 *
 *       押注成功弹窗
 */
public class ActBetTopicSuccessActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView tvTogetherPlay,tvIKnow,tvStakeNotice;
    //分享的主题
    private String topicTitle;
    private int kaiTime;
    private TextView kaiTimeNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activty_act_stake_success);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        Intent intent=getIntent();
        kaiTime=intent.getIntExtra("kaiTime",12);
        topicTitle=intent.getStringExtra("topicTitle");
        String side=intent.getStringExtra("side");
        String count=intent.getStringExtra("count");
        if (side.equals("1")){
            tvStakeNotice.setText("成功押注蓝方"+count+"注！");
        }else {
            tvStakeNotice.setText("成功押注红方"+count+"注！");
        }
        kaiTimeNotice.setText("本期开奖时间："+kaiTime+":00，开奖后会通过站内信告知您本期押注结果，祝您好运！");
        getShareUrl();
    }

    private void initListener() {
        tvIKnow.setOnClickListener(this);
        tvTogetherPlay.setOnClickListener(this);
    }

    private void initView() {
        kaiTimeNotice= (TextView) findViewById(R.id.tv_kai_time_notice);
        tvTogetherPlay= (TextView) findViewById(R.id.tv_together_play);
        tvIKnow= (TextView) findViewById(R.id.tv_i_known);
        tvStakeNotice= (TextView) findViewById(R.id.tv_stake_notice);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_together_play://跳到邀请的页面
//                startActivity(new Intent(mContext, ActInvitationFriendActivity.class));
                if (!TextUtils.isEmpty(topicTitle)){
                    share();
                }else {
                    againGetShareUrl();
                }
                break;
            case R.id.tv_i_known:
                onBackPressed();
                break;
            default:
                break;
        }
    }
    //分享有关
    String share_type = "20"; // 9.邀请有奖分享
    String share_title,share_content,share_img,share_url,shareImg;
    private UmengDialog mShareDialog;
    private JPNetRequest jpNetRequest;
    //是否第一次请求分享
    private boolean isFirstGetShare=true;
    private void againGetShareUrl(){
        isFirstGetShare=false;
        getShareUrl();
    }

    private void getShareUrl() {
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        if (!isFirstGetShare){
            startMyDialog();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
