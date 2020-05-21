package com.yilian.luckypurchase.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.LuckyWinPushPopWindowEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  中奖推送弹窗
 */
public class LuckyWinPopWindowActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivRotate;
    private ImageView ivBgCircle;
    private ImageView ivPrize;
    private TextView tvGoodsInfo;
    private ImageView ivWinner;
    private TextView tvWinnerInfo;
    private ImageView ivText;
    private ImageView ivClose;
    private Button btnLookDetail;
    private String activityId;
    private ObjectAnimator rotationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //     Activity是继承自AppCompatActivity，所以requestWindowFeature(Window.FEATURE_NO_TITLE);这句失效了。
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.lucky_activity_lucky_win_pop_window);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        打开页面后就将该ID置为空，防止APP从后台到前台时重复打开该页面
        PreferenceUtils.writeStrConfig(Constants.APP_LUCKY_PRIZE_ACTIVITY_ID, "", mContext);

    }

    private void initData() {
        activityId = getIntent().getStringExtra("activityId");
        getData();
    }

    @SuppressWarnings("unchecked")
    private void getData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(this).getLuckyWinPushPopWindowData("snatch/snatch_award_message", activityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyWinPushPopWindowEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(LuckyWinPushPopWindowEntity luckyWinPushPopWindowEntity) {
                        LuckyWinPushPopWindowEntity luckyWinPushPopWindowEntity1 = luckyWinPushPopWindowEntity;
                        LuckyWinPushPopWindowEntity.WinLogBean winLog = luckyWinPushPopWindowEntity.winLog;
                        GlideUtil.showImageWithSuffix(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(winLog.prizeGoodsUrl), ivPrize);
                        tvGoodsInfo.setText(winLog.prizeGoodsName);
                        GlideUtil.showCirImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(winLog.photo), ivWinner);
                        Spanned spanned = Html.fromHtml("<font color='#333333'>获得者:" + winLog.userName + "</font><br><font color = '#fe5062'>"
                                + winLog.phone + "<br>参与" + winLog.count + "人次" + "<br>" + DateUtils.timeStampToStr5(winLog.time) + "揭晓<font>");
                        tvWinnerInfo.setText(spanned);

                    }
                });
        addSubscription(subscription);
    }

    private void initView() {
        ivRotate = (ImageView) findViewById(R.id.iv_rotate);
        ivBgCircle = (ImageView) findViewById(R.id.iv_bg_circle);
        ivPrize = (ImageView) findViewById(R.id.iv_prize);
        tvGoodsInfo = (TextView) findViewById(R.id.tv_goods_info);
        ivWinner = (ImageView) findViewById(R.id.iv_winner);
        tvWinnerInfo = (TextView) findViewById(R.id.tv_winner_info);
        ivText = (ImageView) findViewById(R.id.iv_text);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        btnLookDetail = (Button) findViewById(R.id.btn_look_detail);

        ivClose.setOnClickListener(this);
        btnLookDetail.setOnClickListener(this);

        rotationY = ObjectAnimator.ofFloat(ivRotate, "rotation", 0.0f, 359.0f);
        rotationY.setDuration(8000);
        rotationY.setRepeatCount(-1);
        rotationY.setInterpolator(new LinearInterpolator());
        rotationY.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotationY != null) {
            rotationY.end();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_close) {
            finish();
        } else if (i == R.id.btn_look_detail) {
            Intent intent = new Intent(mContext, LuckyMyRecordActivity.class);
            intent.putExtra("current_position", 2);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
