package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author 参与打卡挑战成功
 */
public class ActClockInChallengeSuccessActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvClockTime;
    private Button btnShare;
    private Button btnClose;
    private String applyIntegral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_clock_in_challenge_success);
        initView();
        initListener();
        initData();
    }

    //分享有关
    String share_type = "21"; // 9.邀请有奖分享
    String share_title,share_content,share_img,share_url,shareImg;
    private UmengDialog mShareDialog;
    private JPNetRequest jpNetRequest;

    private void getShareUrl() {
        startMyDialog();
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.getShareUrl(share_type, "", new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                stopMyDialog();
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
                        share();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
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

    private void initData() {
        Spanned spanned = Html.fromHtml("明日<font color = '#FE8019'>5:00-8:00</font>记得打卡");
        tvClockTime.setText(spanned);
    }

    private void initListener() {
        RxUtil.clicks(btnShare, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (TextUtils.isEmpty(share_title)) {
                    getShareUrl();
                } else {
                    share();
                }
            }
        });
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                MenuUtil.showMenu(ActClockInChallengeSuccessActivity.this, R.id.v3Shop);
            }
        });
    }

    private void initView() {
        applyIntegral=getIntent().getStringExtra("applyIntegral");
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("早起打卡赢奖券");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvClockTime = (TextView) findViewById(R.id.tv_clock_time);
        btnShare = (Button) findViewById(R.id.btn_share);
        btnClose = (Button) findViewById(R.id.btn_close);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_close:
                finish();
                break;
            default:
                break;
        }
    }
}
