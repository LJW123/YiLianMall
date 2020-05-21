package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.eventEntity.RefreshActHomePageDataEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.widget.ActClockTimeToast;
import com.yilian.networkingmodule.entity.ActClockHomePageEntity;
import com.yilian.networkingmodule.entity.ActSeekRewardEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * @author  打卡首页
 */
public class ActClockInActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView tvText1;
    private TextView tvIntegralCount;
    private TextView tvClockNumber;
    private LinearLayout llHeaderPhoto;
    private TextView tvRole;
    private ImageView ivShare;
    private RelativeLayout rlContent1;
    private TextView tvClockStatus;
    private TextView tvClockSituation;
    private ImageView ivHeaderEarliest;
    private TextView tvEarliest;
    private ImageView ivHeaderMost;
    private TextView tvMost;
    private ImageView ivHeaderLongest;
    private TextView tvLongest;
    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivDot;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private RelativeLayout rlEarliest;
    private RelativeLayout rlMost;
    private RelativeLayout rlLongest;
    /**
     * 用于点击上一期参与人数的头像进入排行榜用
     */
    private String actClockId;
    private String clockInId;
    private ImageView ivGifZzz;
    private String rewardId;
    private String type;
    private String photoId;
    private boolean isFirst = true;
    /**
     * 跳往打卡成功界面
     */
    private final int TO_CLOCK_IN_SUCCESS_DIALOG=0;
    //参与1次需要的奖券数
    private String applyIntegral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_clock_in);
        StatusBarUtil.setColor(this, Color.parseColor("#115D77"));
        initView();
        initListener();
        initData();
        jumpRewardSeekDiolog(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        jumpRewardSeekDiolog(intent);
    }
    private void jumpRewardSeekDiolog(Intent intent){
        rewardId = intent.getStringExtra("reward_id");
        type = intent.getStringExtra("type");
        if (rewardId != null) {
            getRewardData();
        }
    }

    private void initListener() {
        RxUtil.clicks(tvRight, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                startActivity(new Intent(mContext, ActMyClockInResultActivity.class));
            }
        });
//        邀请好友
        RxUtil.clicks(ivShare, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                startActivity(new Intent(mContext, ActInvitationFriendActivity.class));
            }
        });
//        跳转打卡排行榜
        RxUtil.clicks(llHeaderPhoto, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (TextUtils.isEmpty(photoId)) {
                    showToast("正在获取数据");
                }
                Intent intent = new Intent(mContext, ActClockInTopTenActivity.class);
                intent.putExtra("photo_id", photoId);
                startActivity(intent);
            }
        });
//        活动规则
        RxUtil.clicks(tvRole, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Intent intent = new Intent(mContext, ActClockRuleDialogActivity.class);
                intent.putExtra("applyIntegral",applyIntegral);
                startActivity(intent);
                overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
            }
        });
    }

    private void initView() {
        ivGifZzz = (ImageView) findViewById(R.id.iv_gif_zzz);
        Glide.with(mContext).load(R.mipmap.zzz).asGif().diskCacheStrategy(DiskCacheStrategy.ALL).into(ivGifZzz);
        tvText1 = (TextView) findViewById(R.id.tv_text1);
        tvIntegralCount = (TextView) findViewById(R.id.tv_integral_count);
        tvClockNumber = (TextView) findViewById(R.id.tv_clock_number);
        llHeaderPhoto = (LinearLayout) findViewById(R.id.ll_header_photo);
        tvRole = (TextView) findViewById(R.id.tv_role);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        rlContent1 = (RelativeLayout) findViewById(R.id.rl_content_1);
        tvClockStatus = (TextView) findViewById(R.id.tv_clock_status);
        tvClockSituation = (TextView) findViewById(R.id.tv_clock_situation);
        ivHeaderEarliest = (ImageView) findViewById(R.id.iv_header_earliest);
        tvEarliest = (TextView) findViewById(R.id.tv_earliest);
        ivHeaderMost = (ImageView) findViewById(R.id.iv_header_most);
        tvMost = (TextView) findViewById(R.id.tv_most);
        ivHeaderLongest = (ImageView) findViewById(R.id.iv_header_longest);
        tvLongest = (TextView) findViewById(R.id.tv_longest);
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        ivLeft1.setImageResource(R.mipmap.library_module_white_left_arrow);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("早起打卡赢奖券");
        tvTitle.setTextColor(Color.WHITE);
        ivDot = (ImageView) findViewById(R.id.iv_dot);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        ivRight2.setVisibility(View.GONE);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("我的战绩");
        tvRight.setTextColor(Color.WHITE);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        viewLine.setBackgroundColor(Color.TRANSPARENT);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        llJpTitle.setBackgroundColor(Color.TRANSPARENT);

        llHeaderPhoto.setOnClickListener(this);
        tvRole.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvClockStatus.setOnClickListener(this);
        ivLeft1.setOnClickListener(this);
        rlEarliest = (RelativeLayout) findViewById(R.id.rl_earliest);
        rlEarliest.setOnClickListener(this);
        rlMost = (RelativeLayout) findViewById(R.id.rl_most);
        rlMost.setOnClickListener(this);
        rlLongest = (RelativeLayout) findViewById(R.id.rl_longest);
        rlLongest.setOnClickListener(this);
    }

    private void initData() {
        getClockHomePageData();
    }

    void refreshData() {
        getClockHomePageData();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressWarnings("unchecked")
    private void getClockHomePageData() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getClockHomePageData("activity_early_clockin")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActClockHomePageEntity>() {
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
                    public void onNext(ActClockHomePageEntity actClockHomePageEntity) {
                        stopMyDialog();
                        if (!TextUtils.isEmpty(actClockHomePageEntity.applyIntegral)){
                                double integral=Double.parseDouble(actClockHomePageEntity.applyIntegral)/100;
                                DecimalFormat decimalFormat=new DecimalFormat("######0.#######");
                                applyIntegral=decimalFormat.format(integral);
                        }
                        setData(actClockHomePageEntity);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(ActClockHomePageEntity actClockHomePageEntity) {
        if (actClockHomePageEntity.info != null) {
            clockInId = actClockHomePageEntity.info.id;
        }
        photoId = actClockHomePageEntity.photoId;
        ActClockHomePageEntity.DayResultBean dayResult = actClockHomePageEntity.dayResult;
        GlideUtil.showCirImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(dayResult.firstUserPhoto), ivHeaderEarliest);
        GlideUtil.showCirImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(dayResult.mostUserPhoto), ivHeaderMost);
        GlideUtil.showCirImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(dayResult.yiliUserPhoto), ivHeaderLongest);
        ForegroundColorSpan firstSpan = new ForegroundColorSpan(Color.parseColor("#FF8019"));
        String firstSpanText = dayResult.firstUserName + "\n" + dayResult.firstUserTimeStr;
        String mostSpannText = dayResult.mostUserName + "\n" + dayResult.mostUserIntegral;
        String longestSpanText = dayResult.yiliUserName + "\n" + dayResult.yiliUserNum;
        SpannableString firstSpandString = new SpannableString(firstSpanText);
        SpannableString mostSpandString = new SpannableString(mostSpannText);
        SpannableString longestSpandString = new SpannableString(longestSpanText);
        mostSpandString.setSpan(firstSpan, mostSpannText.length() - dayResult.mostUserIntegral.length() - 1, mostSpannText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        longestSpandString.setSpan(firstSpan, longestSpanText.length() - dayResult.yiliUserNum.length() - 1, longestSpanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        firstSpandString.setSpan(firstSpan, firstSpanText.length() - dayResult.firstUserTimeStr.length() - 1, firstSpanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvEarliest.setText(firstSpandString);
        tvMost.setText(mostSpandString);
        tvLongest.setText(longestSpandString);

        Spanned spanned1 = Html.fromHtml("<font color='#FF8019'>" + dayResult.colckinSuccess + "</font>成功" + "  " + "<font color='#2DC490'>" + dayResult.colckinFail + "</font>失败");
        tvClockSituation.setText(spanned1);
        //        打卡按钮
        RxUtil.clicks(tvClockStatus, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                switch (actClockHomePageEntity.buttonType) {
                    case 0:
//                可参与阶段未参与
                        clockInChallenge();
                        break;
                    case 1:
//                可参与阶段已参与
                        android.widget.Toast toast = ActClockTimeToast.getToast(mContext, "打卡时间  " + actClockHomePageEntity.dakaStr1 + "\n" + actClockHomePageEntity.dakaStr2, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 220);
                        toast.show();
                        break;
                    case 2:
//                可打卡阶段未打卡
                        clockIn();
                        break;
                    case 3:
//                可打卡阶段已打卡
//                        startActivity(new Intent(mContext, ActInvitationFriendActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        switch (actClockHomePageEntity.buttonType) {
            case 0:
//                可参与阶段未参与
                tvClockStatus.setText(applyIntegral+"奖券参与挑战");
                break;
            case 1:
//                可参与阶段已参与,开启倒计时
                int duration = (int) (actClockHomePageEntity.dakaTime - actClockHomePageEntity.serverTime);
                Logger.i("duration:" + duration);
                Subscription subscription = RxUtil.countDown(duration)
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
//                                开始倒计时
                            }
                        })
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                long s = aLong % 60;
                                String ss;
                                if (String.valueOf(s).length() < 2) {
                                    ss = "0" + s;
                                } else {
                                    ss = String.valueOf(s);
                                }
                                long m = aLong / 60 % 60;
                                String sm;
                                if (String.valueOf(m).length() < 2) {
                                    sm = "0" + m;
                                } else {
                                    sm = String.valueOf(m);
                                }
                                long h = aLong / 60 / 60;
                                String sh;
                                if (String.valueOf(h).length() < 2) {
                                    sh = "0" + h;
                                } else {
                                    sh = String.valueOf(h);
                                }
                                tvClockStatus.setText("打卡倒计时  " + sh + "时" + sm + "分" + ss + "秒");
                            }
                        });
                addSubscription(subscription);
                break;
            case 2:
//                可打卡阶段未打卡
                tvClockStatus.setText("打卡");
                break;
            case 3:
//                可打卡阶段已打卡
//                tvClockStatus.setText("邀请好友加入挑战");
                tvClockStatus.setText("已打卡");
                break;
            default:
                break;
        }
        actClockId = actClockHomePageEntity.photoId;
        tvIntegralCount.setText(actClockHomePageEntity.joinIntegral);
        Spanned spanned = Html.fromHtml("当前有<font color = '#FBDB28'>" + actClockHomePageEntity.joinNum + "</font>人参与打卡挑战");
        tvClockNumber.setText(spanned);
        List<String> photoArr = actClockHomePageEntity.photoArr;
        int size = photoArr.size();
        llHeaderPhoto.removeAllViews();
        if (size > 0) {
            //最多显示8个头像
            size=Math.min(size,8);
            for (int i = 0; i < size; i++) {
                ImageView photoView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPXUnitUtil.dp2px(mContext, 24), DPXUnitUtil.dp2px(mContext, 24));
                if (i > 0) {
                    params.setMargins(DPXUnitUtil.dp2px(mContext, -5), 0, 0, 0);
                }

                photoView.setLayoutParams(params);
                GlideUtil.showCirImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(photoArr.get(i)), photoView);
                llHeaderPhoto.addView(photoView);
                if (i == size - 1) {
                    ImageView moreImageView = new ImageView(mContext);
                    moreImageView.setImageResource(R.mipmap.icon_clock_photo);
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(DPXUnitUtil.dp2px(mContext, 24), DPXUnitUtil.dp2px(mContext, 24));
                    params2.setMargins(0, 0, 0, 0);
                    moreImageView.setLayoutParams(params2);
                    llHeaderPhoto.addView(moreImageView);
                }
            }
        }
        int historyCheckCode = actClockHomePageEntity.historyCheck;
        if (historyCheckCode == 1 && isFirst) {//用提醒过时
            isFirst = false;
            Intent intent = new Intent(mContext, ActClockInFailedDialogActivity.class);
            intent.putExtra("history_integral", MoneyUtil.getLeXiangBiNoZero(actClockHomePageEntity.historyIntegral));
            startActivity(intent);
            overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
        }
    }

    /**
     * 打卡
     */
    @SuppressWarnings("unchecked")
    private void clockIn() {
        if (TextUtils.isEmpty(clockInId)) {
            showToast(R.string.service_exception);
            return;
        }
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .clockIn("punch_clock", clockInId)
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
                        startActivityForResult(new Intent(mContext, ActClockInSuccessDialogActivity.class),TO_CLOCK_IN_SUCCESS_DIALOG);
                        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 参与打卡挑战
     */
    @SuppressWarnings("unchecked")
    private void clockInChallenge() {
        startMyDialog(false);
        Logger.i("clockInId:" + clockInId);
        if (TextUtils.isEmpty(clockInId)) {
            showToast(R.string.service_exception);
            return;
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).clockInChallenge("join_clockin_challenge", clockInId)
                .subscribeOn(Schedulers.io())
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
                    public void onNext(HttpResultBean httpResultBean) {
                        Intent intent=new Intent(mContext, ActClockInChallengeSuccessActivity.class);
                        intent.putExtra("applyIntegral",applyIntegral);
                        startActivity(intent );
                        refreshData();
                        EventBus.getDefault().post(new RefreshActHomePageDataEntity());
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_header_photo:

                break;
            case R.id.tv_role:

                break;
            case R.id.tv_clock_status:

                break;
            case R.id.iv_left1:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.rl_earliest:
                break;
            case R.id.rl_most:
                break;
            case R.id.rl_longest:
                break;
            default:
                break;
        }
    }

    private void setIntent(String name, String reward) {
        //接受推送到求赏页面
        if (!TextUtils.isEmpty(rewardId)) {
            switch (type) {
                case "0":
                    startActivity(new Intent(mContext, ActSeekRewardDialogActivity.class).putExtra("name", name).putExtra("reward", reward).putExtra("reward_id", rewardId));
                    break;
                case "1":
                    startActivity(new Intent(mContext, ActPassRewardDialogActivity.class).putExtra("name", name).putExtra("reward", reward));//求赏拒绝
                    break;
                case "2":
                    startActivity(new Intent(mContext, ActRefuseRewardDialogActivity.class).putExtra("name", name).putExtra("reward", reward));//求赏拒绝
                    break;
                default:
                    break;
            }
            overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);

        }
    }

    /**
     * 获取提示求赏信息
     */

    private void getRewardData() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .seekReward("seek_reward_info", rewardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActSeekRewardEntity>() {
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
                        public void onNext(ActSeekRewardEntity rewardEntity) {
                            stopMyDialog();
                            if (rewardEntity==null) {
                                return;
                            }
                            String name=rewardEntity.name;
                            double rewardIntegrals=Double.parseDouble(rewardEntity.awardIntegral)/100;
                            String reward=formatReward(rewardIntegrals+"");
                            setIntent(name,reward);
                        }
                    });
            addSubscription(subscription);

    }
    private String formatReward(String reward) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        if (TextUtils.isEmpty(reward)) {
            return "0.00";
        } else {
            if (reward.contains(".")) {
                int index = reward.indexOf(".");
                //点在首位
                if (index == 0) {
                    reward = 0 + reward + 0;
                } else {
                    //末尾
                    if (index == reward.length() - 1) {
                        reward = reward + 0;
                    }
                }
            }
            BigDecimal bd = new BigDecimal(reward);
            reward = df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));
            return reward;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==TO_CLOCK_IN_SUCCESS_DIALOG){
            refreshData();
        }
    }
}
