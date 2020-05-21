package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.RedAnimation;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.NoDoubleClickListener;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.RedPacketOpenOneEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 奖励弹窗-奖励Fragment中的
 *
 * @author Ray_L_Pain
 * @date 2017/11/23 0023
 */

public class RedPacketDialog2 extends BaseActivity {
    @ViewInject(R.id.layout)
    FrameLayout layout;
    @ViewInject(R.id.iv_bot)
    ImageView ivBot;
    @ViewInject(R.id.iv_top)
    ImageView ivTop;
    @ViewInject(R.id.iv_btn)
    ImageView ivBtn;
    @ViewInject(R.id.iv_close)
    ImageView ivClose;
    @ViewInject(R.id.iv_logo)
    ImageView ivLogo;
    @ViewInject(R.id.tv_com)
    TextView tvCom;
    @ViewInject(R.id.tv_money)
    TextView tvMoney;
    @ViewInject(R.id.tv_go)
    TextView tvGo;

    int screenWidth;

    private String id, money, from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red_packet2);
        ViewUtils.inject(this);

        initView();
        if (!TextUtils.isEmpty(from)) {
            initData();
        }
    }


    private void initView() {
        screenWidth = ScreenUtils.getScreenWidth(mContext);
        id = getIntent().getStringExtra("red_id");
        money = getIntent().getStringExtra("red_money");
        from = getIntent().getStringExtra("red_from");

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) layout.getLayoutParams();

        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) ivBot.getLayoutParams();
        params2.height = (int) ((1052 * 0.1) * screenWidth / (836 * 0.1));

        FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) ivTop.getLayoutParams();
        params3.height = (int) ((510 * 0.1) * screenWidth / (836 * 0.1));

        FrameLayout.LayoutParams params4 = (FrameLayout.LayoutParams) ivBtn.getLayoutParams();

        LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) ivClose.getLayoutParams();

        FrameLayout.LayoutParams params6 = (FrameLayout.LayoutParams) tvCom.getLayoutParams();
        FrameLayout.LayoutParams params7 = (FrameLayout.LayoutParams) tvMoney.getLayoutParams();
        FrameLayout.LayoutParams params8 = (FrameLayout.LayoutParams) tvGo.getLayoutParams();

        FrameLayout.LayoutParams params9 = (FrameLayout.LayoutParams) ivLogo.getLayoutParams();

        switch (screenWidth) {
            case 720:
                params4.setMargins(0, DPXUnitUtil.dp2px(mContext, -40), 0, 0);
                params5.setMargins(0, DPXUnitUtil.dp2px(mContext, -27), 0, 0);

                params6.setMargins(0, DPXUnitUtil.dp2px(mContext, 0), 0, 0);
                params7.setMargins(0, DPXUnitUtil.dp2px(mContext, 40), 0, 0);
                params8.setMargins(0, DPXUnitUtil.dp2px(mContext, 80), 0, 0);

                params9.setMargins(0, DPXUnitUtil.dp2px(mContext, 114), 0, 0);
                break;
            case 1080:
                params4.setMargins(0, DPXUnitUtil.dp2px(mContext, -40), 0, 0);
                params5.setMargins(0, DPXUnitUtil.dp2px(mContext, -40), 0, 0);

                params6.setMargins(0, DPXUnitUtil.dp2px(mContext, 0), 0, 0);
                params7.setMargins(0, DPXUnitUtil.dp2px(mContext, 50), 0, 0);
                params8.setMargins(0, DPXUnitUtil.dp2px(mContext, 100), 0, 0);

                params9.setMargins(0, DPXUnitUtil.dp2px(mContext, 100), 0, 0);
                break;
            case 1440:
                params4.setMargins(0, DPXUnitUtil.dp2px(mContext, -58), 0, 0);
                params5.setMargins(0, DPXUnitUtil.dp2px(mContext, -54), 0, 0);

                params6.setMargins(0, DPXUnitUtil.dp2px(mContext, 0), 0, 0);
                params7.setMargins(0, DPXUnitUtil.dp2px(mContext, 60), 0, 0);
                params8.setMargins(0, DPXUnitUtil.dp2px(mContext, 120), 0, 0);

                params9.setMargins(0, DPXUnitUtil.dp2px(mContext, 124), 0, 0);
                break;
            default:
                break;
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                RedAnimation an = new RedAnimation();
                an.setRepeatCount(1);
                ivBtn.startAnimation(an);

                RetrofitUtils2.getInstance(mContext).openOneRedPacket(id, new Callback<RedPacketOpenOneEntity>() {
                    @Override
                    public void onResponse(Call<RedPacketOpenOneEntity> call, Response<RedPacketOpenOneEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                RedPacketOpenOneEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);
                                        PreferenceUtils.writeStrConfig(Constants.RED_DIALOG_MONEY, entity.money, mContext);
                                        PreferenceUtils.writeStrConfig(Constants.RED_DIALOG_TIME, entity.openTime, mContext);
                                        tvMoney.setText(entity.money);

                                        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(2000);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Animation cAnimation;
                                                            switch (screenWidth) {
                                                                case 720:
                                                                    cAnimation = AnimationUtils.loadAnimation(mContext, R.anim.red_transla_100);
                                                                    break;
                                                                default:
                                                                    cAnimation = AnimationUtils.loadAnimation(mContext, R.anim.red_transla);
                                                                    break;
                                                            }
                                                            cAnimation.setFillAfter(true);
                                                            ivTop.setAnimation(cAnimation);

                                                            AlphaAnimation aAnimation = new AlphaAnimation(1, 0);
                                                            aAnimation.setDuration(2000);
                                                            ivBtn.setAnimation(aAnimation);

                                                            AlphaAnimation bAnimation = new AlphaAnimation(0, 1);
                                                            bAnimation.setDuration(2000);
                                                            ivLogo.setVisibility(View.VISIBLE);
                                                            ivLogo.setAnimation(bAnimation);
                                                            tvCom.setVisibility(View.VISIBLE);
                                                            tvCom.setAnimation(bAnimation);
                                                            tvMoney.setVisibility(View.VISIBLE);
                                                            tvMoney.setAnimation(bAnimation);
                                                            tvGo.setVisibility(View.VISIBLE);
                                                            tvGo.setAnimation(bAnimation);

                                                            an.cancel();
                                                            ivBtn.setVisibility(View.GONE);

                                                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                                        }
                                                    });
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        break;
                                    default:
                                        an.cancel();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RedPacketOpenOneEntity> call, Throwable t) {
                        an.cancel();
                        showToast(R.string.net_work_not_available);
                    }
                });
            }
        });

        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, V3MoneyActivity.class);
                intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                startActivity(intent);
            }
        });
    }


    private void initData() {
        PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);
        RedAnimation an = new RedAnimation();
        an.setRepeatCount(1);
        ivBtn.startAnimation(an);
        ivBtn.setClickable(false);
        if ("one".equals(from)) {
            /**
             * 是日历中的奖励单个拆取
             */
            RetrofitUtils2.getInstance(mContext).openOneRedPacket(id, new Callback<RedPacketOpenOneEntity>() {
                @Override
                public void onResponse(Call<RedPacketOpenOneEntity> call, Response<RedPacketOpenOneEntity> response) {
                    HttpResultBean body = response.body();
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                            RedPacketOpenOneEntity entity = response.body();
                            switch (entity.code) {
                                case 1:
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);
                                    PreferenceUtils.writeStrConfig(Constants.RED_DIALOG_MONEY, entity.money, mContext);
                                    PreferenceUtils.writeStrConfig(Constants.RED_DIALOG_TIME, entity.openTime, mContext);
                                    tvMoney.setText(money);

                                    ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(2000);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Animation cAnimation;
                                                        switch (screenWidth) {
                                                            case 720:
                                                                cAnimation = AnimationUtils.loadAnimation(mContext, R.anim.red_transla_100);
                                                                break;
                                                            default:
                                                                cAnimation = AnimationUtils.loadAnimation(mContext, R.anim.red_transla);
                                                                break;
                                                        }
                                                        cAnimation.setFillAfter(true);
                                                        ivTop.setAnimation(cAnimation);

                                                        AlphaAnimation aAnimation = new AlphaAnimation(1, 0);
                                                        aAnimation.setDuration(2000);
                                                        ivBtn.setAnimation(aAnimation);

                                                        AlphaAnimation bAnimation = new AlphaAnimation(0, 1);
                                                        bAnimation.setDuration(2000);
                                                        ivLogo.setVisibility(View.VISIBLE);
                                                        ivLogo.setAnimation(bAnimation);
                                                        tvCom.setVisibility(View.VISIBLE);
                                                        tvCom.setAnimation(bAnimation);
                                                        tvMoney.setVisibility(View.VISIBLE);
                                                        tvMoney.setAnimation(bAnimation);
                                                        tvGo.setVisibility(View.VISIBLE);
                                                        tvGo.setAnimation(bAnimation);

                                                        an.cancel();
                                                        ivBtn.setVisibility(View.GONE);

                                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                                    }
                                                });
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    break;
                                default:
                                    an.cancel();
                                    break;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RedPacketOpenOneEntity> call, Throwable t) {
                    an.cancel();
                    showToast(R.string.net_work_not_available);
                }
            });
        } else {
            /**
             * 走这里说明是一键拆取
             */
            tvMoney.setText(money);
            ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation cAnimation = AnimationUtils.loadAnimation(mContext, R.anim.red_transla);
                                cAnimation.setFillAfter(true);
                                ivTop.setAnimation(cAnimation);

                                AlphaAnimation aAnimation = new AlphaAnimation(1, 0);
                                aAnimation.setDuration(2000);
                                ivBtn.setAnimation(aAnimation);

                                AlphaAnimation bAnimation = new AlphaAnimation(0, 1);
                                bAnimation.setDuration(2000);
                                ivLogo.setVisibility(View.VISIBLE);
                                ivLogo.setAnimation(bAnimation);
                                tvCom.setVisibility(View.VISIBLE);
                                tvCom.setAnimation(bAnimation);
                                tvMoney.setVisibility(View.VISIBLE);
                                tvMoney.setAnimation(bAnimation);
                                tvGo.setVisibility(View.VISIBLE);
                                tvGo.setAnimation(bAnimation);

                                an.cancel();
                                ivBtn.setVisibility(View.GONE);

                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
