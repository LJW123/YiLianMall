package com.leshan.ylyj.view.activity.welfaremodel;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.WelfarePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RefreshPersonCenter;
import com.yilian.mylibrary.umeng.IconModel;
import com.yilian.mylibrary.umeng.ShareUtile;
import com.yilian.mylibrary.umeng.UmengDialog;
import com.yilian.mylibrary.umeng.UmengGloble;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.WelfareListEntity;
import rxfamily.entity.WelfareShareDataEntivity;
/**
 * 公益爱心捐赠成功页面
 *
 * @author zhaiyaohua on 2018/1/14 0014.
 */

public class WelfareLoveMsgActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mLeftBack;
    private ImageView mIvIocnShare;
    private ImageView mIvTopPhoto;
    private TextView mTvLeaveMessage;
    private TextView mTvLoveCourse;
    private ImageView mIvWelfareImage;
    private TextView mTvWelfarePlain;
    private TextView mTvActionWelfare;
    private String proId;
    public   static final int TO_SUB_MSG_RESESULT_CODE=2;
    private UmengDialog mShareDialog;
    private String share_title, share_content, shareImg, share_url;
    private WelfarePresenter mPresent;
    private FrameLayout flWelfare;
    private Map<String, String> map = new HashMap<>();
    //记录分享到哪
    public int mType = 0;
    private String recordId;

    private boolean isToBackPersonDetail=false;
    public static final int TO_PERSON_DETAIL=3;
    private boolean isFist=true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_love_msg);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        mLeftBack = (ImageView) findViewById(R.id.left_back);
        mIvIocnShare = (ImageView) findViewById(R.id.iv_iocn_share);
        mIvIocnShare.setVisibility(View.VISIBLE);
        mIvTopPhoto = (ImageView) findViewById(R.id.iv_top_photo);
        Glide.with(this).load(R.mipmap.icon_gif_donation).asGif().into(mIvTopPhoto);
        mTvLeaveMessage = (TextView) findViewById(R.id.tv_leave_message);
        mTvLoveCourse = (TextView) findViewById(R.id.tv_love_course);
        mIvWelfareImage = (ImageView) findViewById(R.id.iv_welfare_image);
        mTvWelfarePlain = (TextView) findViewById(R.id.tv_welfare_plain);
        mTvActionWelfare = (TextView) findViewById(R.id.tv_action_welfare);
        flWelfare = (FrameLayout) findViewById(R.id.fl_welfare);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus&&isFist){
            RefreshPersonCenter.refresh(mContext);
            isFist=false;
        }
    }

    /**
     * 获取公益列表数据
     */
    private void getWelfareListData() {
        mPresent.getWelfareList(1, 0);
    }

    /**
     * 分享
     */
    private void share() {
        if (mShareDialog == null) {

            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.library_module_anim_wellcome_bottom), R.style.library_module_DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    mType = arg2;
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
    protected void initListener() {
        mIvIocnShare.setOnClickListener(this);
        flWelfare.setOnClickListener(this);
        mLeftBack.setOnClickListener(this);
        mTvLeaveMessage.setOnClickListener(this);
        mTvLoveCourse.setOnClickListener(this);
    }


    @Override
    protected void initData() {
        proId = getIntent().getStringExtra("proId");
        recordId=getIntent().getStringExtra("msg_id");
        share_url=Constants.WELFARE_LOVE_DONATION+proId;
        if (mPresent == null) {
            mPresent = new WelfarePresenter(mContext, this);
        }
        map.put("project_id", proId);
        getWelfareListData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==TO_SUB_MSG_RESESULT_CODE){
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_iocn_share) {
            if (TextUtils.isEmpty(share_title)) {
                startMyDialog(false);
                mPresent.getWelfareShareData(proId);
            } else {
                share();
            }
        } else if (id == R.id.left_back) {
            finish();
        }else if (id == R.id.fl_welfare) {
            if (TextUtils.isEmpty(bottomProId)) {
                return;
            }
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            String url =Constants.WELFARE_LOVE_DONATION+bottomProId;
            intent.putExtra("url", url);
            SkipUtils.toWelfareDetailsWebView(mContext, intent);
        }else if (id == R.id.tv_leave_message) {
            //TO--提交留言界面
            if (proId == null) {
                return;
            }
            Intent intent = new Intent(mContext, WelfareSubMsgActivity.class);
            intent.putExtra("proId", proId);
            intent.putExtra("recordId",recordId);
            startActivityForResult(intent, 0);
        } else if (id == R.id.tv_love_course) {
            //TO--爱心历程
            isToBackPersonDetail=true;
            finish();
        }
    }

    @Override
    public void onCompleted() {
        stopMyDialog();

    }

    @Override
    public void onError(Throwable e) {
        stopMyDialog();
        showToast(e.getMessage());

    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        goToShareWelfare(baseEntity);
        setBottomWelfare(baseEntity);
    }

    /**
     * 跳转分享
     *
     * @param baseEntity
     */
    private void goToShareWelfare(BaseEntity baseEntity) {
        if (baseEntity instanceof WelfareShareDataEntivity) {
            WelfareShareDataEntivity entivity = (WelfareShareDataEntivity) baseEntity;
            share_content = entivity.data.projectDescribe;
            share_title = "我支持【"+entivity.data.projectName+"】奖券爱心捐赠，爱要一起来。";
            shareImg = resetShareImgUrl(entivity.data.img3);
            share();
        }
    }
    private String resetShareImgUrl(String shareImg){
            if (!shareImg.startsWith("http://")){
                shareImg= Constants.ImageUrl+shareImg;
            }
            return shareImg;
    }


    /**
     * 设置底部的公益项目
     *
     * @param baseEntity
     */
    private String bottomProId;

    private void setBottomWelfare(BaseEntity baseEntity) {
        if (baseEntity instanceof WelfareListEntity) {
            WelfareListEntity listEntity = (WelfareListEntity) baseEntity;
            if (listEntity.data != null && listEntity.data.list.size() >= 0) {
                GlideUtil.showImage(mContext, listEntity.data.list.get(0).img2, mIvWelfareImage);
                mTvWelfarePlain.setText(listEntity.data.list.get(0).projectName);
                String welfareCountString=listEntity.data.list.get(0).projectDescribe+"\n共"+listEntity.data.list.get(0).count+"爱心（份）";
                SpannableString spannableString=new SpannableString(welfareCountString);
                ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor("#FB9B35"));
                int  endIndex=spannableString.length()-5;
                int startIndex=endIndex-listEntity.data.list.get(0).count.length();
                spannableString.setSpan(colorSpan,startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTvActionWelfare.setText(spannableString);
                bottomProId = listEntity.data.list.get(0).id;
            }
        }
    }


    //接收的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String type) {
        if (mType == 0) {
            //微信好友
            map.put("share_app", "1");

        } else if (mType == 1) {
            //微信朋友圈
            map.put("share_app", "2");

        } else if (mType == 2) {
            //QQ空间
            map.put("share_app", "4");

        } else if (mType == 3) {
            //QQ好友
            map.put("share_app", "3");
        }
        mPresent.postWelfareShareResult(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        if (isToBackPersonDetail){
            setResult(TO_PERSON_DETAIL);
        }else {
            setResult(1);
        }
        super.finish();
    }
}
