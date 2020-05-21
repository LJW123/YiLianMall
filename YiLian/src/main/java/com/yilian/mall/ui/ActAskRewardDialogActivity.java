package com.yilian.mall.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.Random;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 求赏弹窗
 */
public class ActAskRewardDialogActivity extends BaseDialogActivity implements View.OnClickListener {

    private ImageView ivClose;
    private ImageView ivPhoto;
    private TextView tvIntegral;
    private Button btnAskReward;
    private String recordId;
    /**
     * 求赏最大限制   用于客户端随机生成求赏金额
     */
    private int seekRewardLimit;
    private String photoUrl;
    /**
     * 求赏金额
     */
    private String askRewardIntegral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ask_reward);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        getRandom();
        photoUrl = getIntent().getStringExtra("photoUrl");
        GlideUtil.showCirImage(mContext, photoUrl, ivPhoto);
        recordId = getIntent().getStringExtra("recordId");
        seekRewardLimit = getIntent().getIntExtra("seekRewardLimit", 1);
    }

    private void initListener() {
//        刷新求赏金额
        RxUtil.clicks(tvIntegral, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                getRandom();
            }
        });
//        求赏
        RxUtil.clicks(btnAskReward, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                askReward();
            }
        });
    }

    /**
     * 求赏
     */
    @SuppressWarnings("unchecked")
    private void askReward() {
        startMyDialog(false);
        double askRewrad=Double.parseDouble(askRewardIntegral)*100;
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .clockInRecordAskReward("clockin_seek_reward", recordId, askRewrad+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
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
                    public void onNext(HttpResultBean httpResultBean) {
                        showToast(httpResultBean.msg);
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    private void getRandom() {
        float randomReward=new Random().nextFloat();
        if (randomReward<0.01){
            randomReward=0.01f;
        }
        askRewardIntegral = String.valueOf(randomReward);
        if (askRewardIntegral.length() > 4) {//只保留小数点后两位
            askRewardIntegral = askRewardIntegral.substring(0, 4);
        }
        Spanned integralSpanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            integralSpanned = Html.fromHtml("<big><big><big><b>" + askRewardIntegral + "</b></big></big></big>奖券", Html.FROM_HTML_MODE_LEGACY);
        } else {
            integralSpanned = Html.fromHtml("<big><big><big><b>" + askRewardIntegral + "</b></big></big></big>奖券");
        }
        tvIntegral.setText(integralSpanned);
    }

    private void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        btnAskReward = (Button) findViewById(R.id.btn_ask_reward);

        ivClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;

            default:
                break;
        }
    }
}
