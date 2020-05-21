package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ActSeekRewardEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.math.BigDecimal;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 有人求赏
 * Created by ZYH on 2017/12/17 0017.
 */

public class ActSeekRewardDialogActivity extends BaseDialogActivity implements View.OnClickListener {
    private TextView tvSeekMan,tvSeekIntegral;
    private Button btnPssReward,btnRefuseReward,btnClose;
    private String id;
    private int from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_seek_reward_dialog);
        initView();
        initData();
        initListner();
    }

    private void initListner() {
        btnPssReward.setOnClickListener(this);
        btnRefuseReward.setOnClickListener(this);
        btnClose= (Button) findViewById(R.id.btn_close3);
        btnClose.setOnClickListener(this);


    }

    private void initData() {
        setData();
    }

    private void setData() {
        String name=getIntent().getStringExtra("name");
        String integral=getIntent().getStringExtra("reward");
        if (TextUtils.isEmpty(name)){
            name="暂无昵称";
        }
        tvSeekMan.setText("用户 "+name+" 认为 ");
        String reward="你能打赏"+integral+"奖券";
        Spannable spannable=new SpannableString(reward);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FE8019")),4,4+integral.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(18,true),4,4+integral.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSeekIntegral.setText(spannable);
    }

    private void initView() {
        id=getIntent().getStringExtra("reward_id");
        from=getIntent().getIntExtra("from",0);
        tvSeekMan= (TextView) findViewById(R.id.tv_seek_man);
        tvSeekIntegral= (TextView) findViewById(R.id.tv_seek_integral);
        btnPssReward= (Button) findViewById(R.id.bnt_reward);
        btnRefuseReward= (Button) findViewById(R.id.bnt_refuse);
    }

    /**
     *
     * 1--通过
     * 2--拒绝
     * @param type
     */
    @SuppressWarnings("unchecked")
    private void chekReward(int type){
        startMyDialog(false);
        Subscription subscription= RetrofitUtils3.getRetrofitService(mContext)
                    .checkSeekReward("seek_reward_check",id,type)
                    .subscribeOn(rx.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResultBean>() {
                        @Override
                        public void onCompleted() {
                            stopMyDialog();

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            stopMyDialog();

                        }

                        @Override
                        public void onNext(HttpResultBean rewardEntity) {
                            stopMyDialog();
                            if (from==1){
                                //刷新页面使用
                                setResult(1);
                            }
                            finish();
                        }
                    });
        addSubscription(subscription);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close3:
                finish();
                break;
            case R.id.bnt_refuse:
                chekReward(2);
                break;
            case R.id.bnt_reward:
                chekReward(1);
                break;
        }
    }

}
