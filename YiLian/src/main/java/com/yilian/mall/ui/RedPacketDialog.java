package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RedAnimation;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.NoDoubleClickListener;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.RedPacketOpenOneEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Ray_L_Pain
 *         奖励弹窗
 */
public class RedPacketDialog extends BaseActivity {
    @ViewInject(R.id.iv_bg)
    ImageView ivBg;
    @ViewInject(R.id.iv_bot)
    ImageView ivBot;
    @ViewInject(R.id.layout)
    LinearLayout layout;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_desc)
    TextView tvDesc;
    @ViewInject(R.id.tv_money)
    TextView tvMoney;
    @ViewInject(R.id.tv_look)
    TextView tvLook;
    @ViewInject(R.id.iv_top)
    ImageView ivTop;
    @ViewInject(R.id.iv_btn)
    ImageView ivBtn;
    @ViewInject(R.id.iv_close)
    ImageView ivClose;

    private int screenWidth;
    FrameLayout.LayoutParams paramsLayout;

    public String redPacketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red_packet);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        screenWidth = ScreenUtils.getScreenWidth(mContext);
        layout.setClickable(false);

        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) ivBg.getLayoutParams();
        params1.width = screenWidth;
        params1.height = (int) ((901 * 0.1) * screenWidth / (1080 * 0.1));

        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) ivBot.getLayoutParams();
        params2.height = (int) ((882 * 0.1) * screenWidth / (820 * 0.1));

        FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) ivTop.getLayoutParams();
        params3.height = (int) ((768 * 0.1) * screenWidth / (455 * 0.1));

        FrameLayout.LayoutParams params4 = (FrameLayout.LayoutParams) ivBtn.getLayoutParams();

        paramsLayout = (FrameLayout.LayoutParams) layout.getLayoutParams();
        paramsLayout.setMargins(0, DPXUnitUtil.dp2px(mContext, 15), 0, 0);


        switch (screenWidth) {
            case 720:
                params3.setMargins(0, 135, 0, 0);
                params4.setMargins(0, 80, 0, 0);
                break;
            case 1080:
                params3.setMargins(0, 200, 0, 0);
                params4.setMargins(0, 120, 0, 0);
                break;
            case 1440:
                params3.setMargins(0, 270, 0, 0);
                params4.setMargins(0, 170, 0, 0);
                break;
            default:
                break;
        }

    }

    private void initData() {
        redPacketId = PreferenceUtils.readStrConfig(Constants.RED_PACKET_ID, mContext, "");

        RetrofitUtils2.getInstance(mContext).openOneRedPop(redPacketId, new Callback<HttpResultBean>() {
            @Override
            public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                Logger.i("走了界面销毁---奖励弹窗---" + body.msg);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<HttpResultBean> call, Throwable t) {

            }
        });
    }

    private void initListener() {
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

                RetrofitUtils2.getInstance(mContext).openOneRedPacket(redPacketId, new Callback<RedPacketOpenOneEntity>() {
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
                                                            tvTitle.setText("奖励已到账");
                                                            AlphaAnimation aAnimation = new AlphaAnimation(1, 0);
                                                            aAnimation.setDuration(1000);
                                                            tvDesc.startAnimation(aAnimation);
                                                            tvDesc.setVisibility(View.GONE);

                                                            AlphaAnimation bAnimation = new AlphaAnimation(0, 1);
                                                            bAnimation.setDuration(2000);
                                                            tvMoney.setVisibility(View.VISIBLE);
                                                            tvMoney.setAnimation(bAnimation);
                                                            tvLook.setVisibility(View.VISIBLE);
                                                            tvLook.startAnimation(bAnimation);

                                                            ivBtn.setImageResource(R.mipmap.red_packet_btn_off);
                                                            an.cancel();
                                                            ivBtn.setClickable(false);
                                                            Animation cAnimation = AnimationUtils.loadAnimation(mContext, R.anim.red_transla);
                                                            cAnimation.setFillAfter(true);
                                                            layout.setAnimation(cAnimation);
                                                            layout.setClickable(true);
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

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, V3MoneyActivity.class);
                intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                startActivity(intent);
            }
        });
    }
}
