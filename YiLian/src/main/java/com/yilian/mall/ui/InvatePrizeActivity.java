package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.InvateEntity;
import com.yilian.mall.http.InvateNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.widgets.RiseNumberTextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;


/**
 * 邀请有奖界面
 * Created by Administrator on 2016/3/22.
 */
public class InvatePrizeActivity extends BaseActivity {
    @ViewInject(R.id.tv)
    private TextView mTv;
    @ViewInject(R.id.tv_invate_prize_members_count)
    private RiseNumberTextView menbersCountTv;


    private Animation animBottom;

    private InvateNetRequest request;
    private String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate_prize);

        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        String str = "邀请好友还有<font color=\"#ffe63b\">礼品赠送哦～</font>";
        mTv.setText(Html.fromHtml(str));
        menbersCountTv.setText("");
        initDate();
    }

    private void initDate() {
        if (request == null) {
            request = new InvateNetRequest(mContext);
        }
        request.invateNet(new RequestCallBack<InvateEntity>() {
            @Override
            public void onSuccess(ResponseInfo<InvateEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        menbersCountTv.withNumber(Integer.parseInt(responseInfo.result.count));
                        menbersCountTv.setDuration(400);
                        menbersCountTv.start();
                        shareUrl = responseInfo.result.url;
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }


    public void onBack(View view) {
        finish();
    }

    /**
     * 分享
     *
     * @param view
     */
    public void share(View view) {


        //String phone = sp.getString("phone", "");

//        if (TextUtils.isEmpty(phone)) {
//            showToast("数据异常，请重新登录后，再来分享！");
//            return;
//        }

        animBottom = AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom);
        UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
                new UmengGloble().getAllIconModels());
        dialog1.setItemLister(new UmengDialog.OnListItemClickListener() {

            @Override
            public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                String content = getResources().getString(R.string.appshare);
                new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, shareUrl).share();
            }
        });
        Logger.i(shareUrl+"");
        dialog1.show();
    }
}
