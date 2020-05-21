package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.SignInDaysAdapter;
import com.yilian.mall.entity.eventEntity.RefreshActHomePageDataEntity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.ActHomeClockEntity;
import com.yilian.networkingmodule.entity.ActKnowMoreEntity;
import com.yilian.networkingmodule.entity.ActSignInData;
import com.yilian.networkingmodule.entity.ActSignInSuccessEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity;
import com.yilian.networkingmodule.entity.TopicEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.utils.MoneyUtil;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  活动首页
 */
public class ActHomeActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private LinearLayout llKnowMore;
    private RecyclerView horizontalListView;
    private TextView tvBtnSignIn;
    private TextView tvGetIntegral;
    private ImageView ivClock;
    private TextView tvClockTimes;
    private TextView tvBtnPartIn;
    private TextView tvBetTimes;
    private LinearLayout llBet;
    private TextView tvBetTopic;
    private Button btnPros;
    private Button btnCons;
    private TextView tvShare;
    /**
     * 每次最多注数
     */
    private int betLimit;
    /**
     * 话题ID
     */
    private String topicId;
    /**
     * 单注消耗奖券
     */
    private int betConsume;
    private View llClock;
    private String clockInId;
    private String topicHomePageLinkUrl;
    private String myIntegral;
    private ActKnowMoreEntity knowMoreData;
    private final int TO_BET_TOPIC_REQUEST_CODE=0;
    private final int TO_BET_TOPIC_RESULT_CODE=1;
    private String betNums;
    /**
     * 押话题的主题--分享用
     */
    private String topicTitle;
    private int kaiTime;


    private boolean getSignInDataFinish;
    private ActSignInData actSignInDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_sign_in);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
//        押话题活动主页
        RxUtil.clicks(llBet, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                if (TextUtils.isEmpty(topicHomePageLinkUrl)) {
                    showToast(R.string.library_module_getting_data);
                    return;
                }
                intent.putExtra(Constants.SPKEY_URL, topicHomePageLinkUrl);
                startActivity(intent);
            }
        });
//        邀请好友
        RxUtil.clicks(tvShare, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (!isLogin()) {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                    return;
                }
                startActivity(new Intent(mContext, ActInvitationFriendActivity.class));
            }
        });
//        打卡活动首页
        RxUtil.clicks(llClock, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (TextUtils.isEmpty(clockInId)) {
                    showToast(R.string.service_exception);
                    return;
                }
                Logger.i("colorInId from:" + clockInId);
                Intent intent = new Intent(mContext, ActClockInActivity.class);
                intent.putExtra("clockInId", clockInId);
                startActivity(intent);
            }
        });
        RxUtil.clicks(btnCons, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                betTopic("1");
            }
        });
        RxUtil.clicks(btnPros, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                betTopic("0");
            }
        });
//        了解更多
        RxUtil.clicks(llKnowMore, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                getKnowMoreData();

            }
        });
    }

    private void initData() {
        getActHomeData();
    }

    private void getActHomeData() {
        getSignInData();
        getClockData();
        getTopicData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void refreshData() {
        getActHomeData();
    }



    @SuppressWarnings("unchecked")
    private void getSignInData() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSignData("sign/sign_list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActSignInData>() {

                    @Override
                    public void onCompleted() {
                        getSignInDataFinish = true;
                        stopDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getSignInDataFinish = true;
                        stopDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ActSignInData actSignInData) {
                        actSignInDatas=actSignInData;
                        setSignInDatas(actSignInDatas);

                    }
                });
        addSubscription(subscription);
    }

    private void setSignInDatas(ActSignInData actSignInData) {
        List<ActSignInData.DatelistBean> datelist = actSignInData.datelist;
        int totalDay = actSignInData.totalDay;
        if (totalDay > 0) {
            horizontalListView.setLayoutManager(new GridLayoutManager(mContext, 8));
            horizontalListView.setAdapter(new SignInDaysAdapter(R.layout.item_act_sign_in_days, datelist, actSignInData.curMonth, actSignInData.isTodaySign));
        } else {
            horizontalListView.setVisibility(View.INVISIBLE);
        }
        setTotalIntegral(actSignInData);
        switch (actSignInData.isTodaySign) {
            case 0:
                tvBtnSignIn.setText("签到领奖券");
                RxUtil.clicks(tvBtnSignIn, new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (!isLogin()) {
                            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                            return;
                        }
                        signIn();
                    }
                });
                break;
            case 1:
                int moreDays = actSignInData.totalDay - actSignInData.days;
                moreDays = (moreDays == 0 ? 7 : moreDays);
                tvBtnSignIn.setText("再签到" + moreDays + "天\n有惊喜");
                RxUtil.clicks(tvBtnSignIn, new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        showToast("已签到");
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 设置当前奖券和连续签到天数和增加的奖券
     * @param actSignInData
     */

    private void setTotalIntegral(ActSignInData actSignInData) {
        if (actSignInData==null){
            return;
        }
        if (actSignInData.addIntegral.contains(".")) {
            String[] split = actSignInData.addIntegral.split("\\.");
            Spanned spanned;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                spanned = Html.fromHtml("<font color='#FFC357'><big><big>+" + split[0] + "</big></big>" + "."
                        + split[1] + "奖券</font><br>" + "当前奖券" + MoneyUtil.getLeXiangBiNoZero(actSignInData.integral) + "&nbsp;丨&nbsp;连续签到<font color='#FFC357'>" + actSignInData.days + "</font>天", Html.FROM_HTML_MODE_LEGACY);
            } else {
                spanned = Html.fromHtml("<font color='#FFC357'><big><big>+" + split[0] + "</big></big>" + "."
                        + split[1] + "奖券</font><br>" + "当前奖券" + MoneyUtil.getLeXiangBiNoZero(actSignInData.integral) + "&nbsp;丨&nbsp;连续签到<font color='#FFC357'>" + actSignInData.days + "</font>天");
            }
            tvGetIntegral.setText(spanned);
        } else {
            Spanned spanned;
            if (Build.VERSION.SDK_INT >= 24) {
                spanned = Html.fromHtml("<font color='#FFC357'><big><big>+" + actSignInData.addIntegral +
                        "</big></big>奖券</font><br>" + "当前奖券" + MoneyUtil.getLeXiangBiNoZero(actSignInData.integral) + "&nbsp;丨&nbsp;连续签到<font color='#FFC357'>" + actSignInData.days + "</font>天", Html.FROM_HTML_MODE_LEGACY);

            } else {
                spanned = Html.fromHtml("<font color='#FFC357'><big><big>+" + actSignInData.addIntegral +
                        "</big></big>奖券</font><br>" + "当前奖券" + MoneyUtil.getLeXiangBiNoZero(actSignInData.integral) + "&nbsp;丨&nbsp;连续签到<font color='#FFC357'>" + actSignInData.days + "</font>天");
            }
            tvGetIntegral.setText(spanned);
        }
    }

    /**
     * 签到之后重新--需要在测试，和接口增加字段
     */

    private void resetSignData(ActSignInSuccessEntity actSignInSuccessEntity) {
        if (actSignInDatas==null){
            return;
        }
        //连续签到的天数
        actSignInDatas.days=actSignInSuccessEntity.days;
        //本次获得的奖券
        BigDecimal bigDecimal=new BigDecimal(actSignInSuccessEntity.integral);
        bigDecimal.setScale(2,BigDecimal.ROUND_DOWN);
        actSignInDatas.addIntegral=bigDecimal+"";
        //签够规定天数
        if (actSignInSuccessEntity.weekIntegral!=0){
            double totalIntegral=Double.parseDouble(actSignInDatas.integral)+actSignInSuccessEntity.weekIntegral*100.00;
            actSignInDatas.integral=totalIntegral+"";
        }
        actSignInDatas.isTodaySign=1;
        setSignInDatas(actSignInDatas);
    }

    @Subscribe
    @Override
    public void refreshData(RefreshActHomePageDataEntity refreshActHomePageDataEntity) {
        refreshData();
    }

    /**
     * 签到
     */
    @SuppressWarnings("unchecked")
    private void signIn() {
        startMyDialog(false);
        Subscription subscription1 = RetrofitUtils3.getRetrofitService(mContext).signIn("sign/user_sign")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActSignInSuccessEntity>() {
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
                    public void onNext(ActSignInSuccessEntity actSignInSuccessEntity) {
                        if (actSignInSuccessEntity==null){
                            return;
                        }
                        //重新刷新签到数据
                        resetSignData(actSignInSuccessEntity);
                        Intent intent = new Intent(mContext, ActSignInSuccessDialogActivity.class);
                        intent.putExtra("data",actSignInSuccessEntity);
                        startActivity(intent);
                        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
//                        refreshData();

                    }
                });
        addSubscription(subscription1);
    }

    boolean getClockDataFinish;

    @SuppressWarnings("unchecked")
    private void getClockData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getHomeClockData("get_early_clockin", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActHomeClockEntity>() {
                    @Override
                    public void onCompleted() {
                        getClockDataFinish = true;
                        stopDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        getClockDataFinish = true;
                        stopDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ActHomeClockEntity actHomeClockEntity) {
                        clockInId = actHomeClockEntity.id;
                        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(actHomeClockEntity.cover), ivClock);
                        Spanned spanned = Html.fromHtml("<font color='#333333'>" + actHomeClockEntity.titleSub +
                                "</font><br><font color ='#51C8CD'><big><big><big><big>" + actHomeClockEntity.joinNum + "</big></big></big></big></font><br>" +
                                "<font color='#999999'>今日参与挑战(人)</font>");
                        tvClockTimes.setText(spanned);
                    }
                });
        addSubscription(subscription);
    }

    boolean getTopicDataFinish;

    @SuppressWarnings("unchecked")
    private void getTopicData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getTopic("activity_mortgage_topic", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TopicEntity>() {
                    @Override
                    public void onCompleted() {
                        getTopicDataFinish = true;
                        stopDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getTopicDataFinish = true;
                        stopDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(TopicEntity topicEntity) {
                        topicTitle=topicEntity.title;
                        kaiTime=topicEntity.kaiTime;
                        topicHomePageLinkUrl = topicEntity.topicHomePageLinkUrl;
                        tvBetTimes.setText("每天" + topicEntity.kaiTime + "点开奖,截至10分钟前本场累计押注" + topicEntity.tenMinNum + "注");
                        tvBetTopic.setText("#" + topicEntity.title + "#");
                        btnPros.setText(topicEntity.topicATitle);
                        btnCons.setText(topicEntity.topicBTitle);
                        betLimit = topicEntity.betLimit;
                        topicId = topicEntity.id;
                        betConsume = topicEntity.betConsume;
                        if (!TextUtils.isEmpty(betNums)){
                            if (!TextUtils.isEmpty(topicEntity.myIntegral)){
                                actSignInDatas.integral=topicEntity.myIntegral;
                                setTotalIntegral(actSignInDatas);
                            }
                            betNums=null;
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 三个接口都请求结束后再结束dialog
     */
    private void stopDialog() {
        if (getClockDataFinish && getTopicDataFinish && getSignInDataFinish) {
            stopMyDialog();
        }
    }

    private void initView() {
        llClock = findViewById(R.id.ll_clock);
        findViewById(R.id.view_line).setBackgroundColor(Color.parseColor("#3F4344"));
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("签到");
        v3Title.setTextColor(Color.WHITE);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.library_module_white_left_arrow);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundColor(Color.parseColor("#3F4344"));
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        llTitle.setBackgroundColor(Color.parseColor("#3F4344"));
        llKnowMore = (LinearLayout) findViewById(R.id.ll_know_more);
        horizontalListView = (RecyclerView) findViewById(R.id.horizontal_listView);
        tvBtnSignIn = (TextView) findViewById(R.id.tv_btn_sign_in);
        tvGetIntegral = (TextView) findViewById(R.id.tv_get_integral);
        ivClock = (ImageView) findViewById(R.id.iv_clock);
        tvClockTimes = (TextView) findViewById(R.id.tv_clock_times);
        tvBtnPartIn = (TextView) findViewById(R.id.tv_btn_part_in);
        tvBetTimes = (TextView) findViewById(R.id.tv_bet_times);
        llBet = (LinearLayout) findViewById(R.id.ll_bet);
        tvBetTopic = (TextView) findViewById(R.id.tv_bet_topic);
        btnPros = (Button) findViewById(R.id.btn_pros);
        btnCons = (Button) findViewById(R.id.btn_cons);
        tvShare = (TextView) findViewById(R.id.tv_share);

        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        llKnowMore.setOnClickListener(this);
        tvBtnSignIn.setOnClickListener(this);
        llBet.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        llClock.setOnClickListener(this);
        StatusBarUtil.setColor(this, Color.parseColor("#282828"));
    }

    @SuppressWarnings("unchecked")
    void getKnowMoreData() {
        if (knowMoreData==null){
            startMyDialog();
            Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).getActKnowMoreData("sign/know_more")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ActKnowMoreEntity>() {
                        @Override
                        public void onCompleted() {
                            stopDialog();

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            stopDialog();

                        }

                        @Override
                        public void onNext(ActKnowMoreEntity actKnowMoreEntity) {
                            knowMoreData = actKnowMoreEntity;
                            stopDialog();
                            jumpTtoActRuleDialog();

                        }
                    });
            addSubscription(subscription);

        }else {
            jumpTtoActRuleDialog();

        }
    }

    /**
     * 跳了解更多页面
     */
    private void jumpTtoActRuleDialog(){
        Intent intent = new Intent(mContext, ActRuleDialogActivity.class);
        intent.putExtra("knowMoreData", knowMoreData);
        startActivity(intent);
        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
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
            case R.id.tv_btn_sign_in:

                break;
            case R.id.ll_bet:
                break;

            default:
                break;
        }
    }


    private void betTopic(String side) {
        Logger.i("isLogin():" + isLogin());
        if (!isLogin()) {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
            return;
        }
        if (betLimit == 0 || TextUtils.isEmpty(topicId) || betConsume == 0) {
            showToast("数据请求中,请稍后再试… ");
            return;
        }
        Intent intent = new Intent(mContext, ActBetTopicActivity.class);
        intent.putExtra("side", side);
        intent.putExtra("betLimit", betLimit);
        intent.putExtra("topicId", topicId);
        intent.putExtra("kaiTime",kaiTime);
        intent.putExtra("betConsume", betConsume / 100);
        intent.putExtra("topicTitle",topicTitle);
        startActivityForResult(intent,TO_BET_TOPIC_REQUEST_CODE);
        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==TO_BET_TOPIC_RESULT_CODE){
            betNums=data.getStringExtra("num");
            getTopicData();
        }
    }


}
