package com.yilian.luckypurchase.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyItemJoinRecordAdapter;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.luckypurchase.utils.MoneyUtil;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.luckypurchase.widget.NumberAddSubView;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.networkingmodule.entity.LuckyInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * 幸运GO 商品详情
 *
 * @author Ray_L_Pain
 * @date 2017/10/23 0023
 */

public class LuckyActivityDetailActivity extends BaseAppCompatActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    /**
     * 漂浮view的最大个数
     */
    public static final int FLUTTER_VIEW_COUNT = 10;
    private final MyHandler myHandler = new MyHandler(this);

    /**
     * 判断是否刷新的标志
     */
    public boolean isRefresh = false;
    /**
     * 本次活动剩余参与人次
     */
    int leftCount;
    ArrayList<LuckyInfoEntity.SnatchLog> flutterDatas = new ArrayList<>();
    /**
     * 开始做动画的position
     */
    int animatorPosition = 0;
    /**
     * NumberAddSubView显示的值
     */
    int mCount;
    String userId;
    int number;
    //一开始不可见的back 和 更多
    private ImageView ivBackBlack, ivMoreBlack;
    //一开始可见的back 和 更多
    private ImageView ivBackWhite, ivMoreWhite;
    private TextView tvTitle;
    private RelativeLayout layoutTitle;
    private RelativeLayout layoutNormal;
    private LinearLayout bottomLayout;
    //
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout joinNextLayout;
    private TextView tvJoinNow, tvJoinNext;
    /**
     * 活动列表的条目位置，用于点击条目进入详情，参加活动后，动态修改条目的进度
     */
    private int prizeListPosition;
    /**
     * headView
     */
    private View headView;
    private Banner banner;
    private TextView tvActivityTitle, tvActivityTitleSub, tvActivityIssue, tvActivityStatus, tvActivityNeedPeople, tvActivityLeftPeople, tvActivityJoinPeople, tvInfoDetail, tvInfoUnboxing, tvInfoReview, tvSuccessNum, tvSuccessLuckyNum, tvSuccessName, tvSuccessPhone, tvSuccessTime, tvSuccessPeople, tvSuccessDetail, tvSuccessMore;
    private LinearLayout prizeLayout, successLayout;
    private ImageView ivSuccess;
    /**
     * 进度条 及 标识
     */
    private TextView tvLeBean;
    /**
     * 进度条 及 标识
     */
    private TextView progesssValue;
    private ProgressBar progressBar;

    private FrameLayout frameLayout;
    /**
     * emptyView
     */
    private View emptyView;
    /**
     * errorView
     */
    private View errorView;
    private TextView tvRefresh;
    /**
     * footView
     */
    private View footView;
    private int page = 0;
    private LuckyItemJoinRecordAdapter adapter;
    private boolean getFirstPageDataFlag = true;
    private LuckyInfoEntity entity;
    /**
     * isShow 0不展示 1展示
     * type 0本期 1下期
     */
    private String id, type, showWindow;
    /**
     * 屏幕宽度,高度,边距
     */
    private int width, s_width, s_height, margin;
    private RelativeLayout rlFlutter;
    /**
     * 承载所有漂浮View动画效果的集合
     */
    private ArrayList<AnimatorSet> animatorSetArrayList = new ArrayList<>();
    /**
     * 装载漂浮view的集合
     */
    private ArrayList<LinearLayout> flutterViewList = new ArrayList<>();
    private TextView tvMerchant;
    private LinearLayout llMerchant;
    private View viewMerchantLine;
    private TextView tvDownLine;
    private View viewDownLine;
    /**
     * 用户是否能参与该活动 0不能参与 1可以参与 默认可以参与 只有服务器返回0时，才不可以参与
     */
    private int flag = 1;
    private int height;
    private int half_height = height / 2;
    /**
     * 立即参加的dialog
     */
    private PopupWindow joinWindow;
    private View contentView, view;
    private NumberAddSubView numberView;
    private TextView tv5;
    private TextView tv20;
    private TextView tv50;
    private TextView tv100;
    private TextView tvResult;
    private TextView tvJoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_detail_activity);
        initView();
        initData();
        initListener();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Logger.i("HomePageFragment-走了这里1111");
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) layoutTitle.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(mContext);
            layoutTitle.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
            StatusBarUtil.setTranslucentForImageViewInFragment(LuckyActivityDetailActivity.this, 60, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("ray-" + "走了onResume");

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_LUCKY_DETAIL, mContext, false);
        Logger.i("onResume:" + isRefresh);

        if (isRefresh == true) {
            getFirstPageDataFlag = true;
            initData();

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_LUCKY_DETAIL, isRefresh, mContext);
        }

    }

    private void initData() {
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeRefreshLayout != null) {
                                swipeRefreshLayout.setRefreshing(true);
                            }
                            getFirstPageData();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                .luckyInfo(id, String.valueOf(page), "20", type, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), new Callback<LuckyInfoEntity>() {
                    @Override
                    public void onResponse(Call<LuckyInfoEntity> call, Response<LuckyInfoEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                entity = response.body();
                                switch (response.body().code) {
                                    case 1:
                                        //        参与记录
                                        ArrayList<LuckyInfoEntity.SnatchLog> newList = entity.snatch_log;
                                        if (newList != null && newList.size() > 0) {
                                            initHeadView();
                                            if (page > 0) {
                                                adapter.addData(newList);
                                            } else {
                                                //1表示正在进行，2表示已经结束未开奖
                                                if ("1".equals(entity.snatch_info.snatch_status)) {
                                                    setFlutterData(newList);
                                                }
                                                getFirstPageDataFlag = false;
                                                adapter.setNewData(newList);
                                            }
                                            if (newList.size() < Constants.PAGE_COUNT_20) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
                                                //走这里 说明当前活动 没有任何人参与 显示一个头部
                                                Logger.i("onResume:走了page = 0");
                                                adapter.setNewData(newList);
                                                initHeadView();
                                                initFootView();
                                                if (adapter.getEmptyViewCount() > 0) {
                                                    ((ViewGroup) adapter.getEmptyView()).removeAllViews();
                                                }
                                                getFirstPageDataFlag = false;
                                            } else {
                                                adapter.loadMoreEnd();
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<LuckyInfoEntity> call, Throwable t) {
                        if (page == 0) {
                            adapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                            if (adapter.getEmptyViewCount() > 0) {
                                ((ViewGroup) adapter.getEmptyView()).removeAllViews();
                            }
                            adapter.notifyDataSetChanged();
                        }
                        adapter.loadMoreFail();
                        netRequestEnd();
                        showToast(R.string.lucky_system_busy);
                        bottomLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void initHeadView() {
        Logger.i("onResume:head" + adapter.getHeaderLayoutCount());
        if (adapter.getHeaderLayoutCount() == 0) {
            adapter.addHeaderView(headView);
        }

        if (adapter.getFooterLayoutCount() > 0) {
            adapter.removeAllFooterView();
        }

        if (bottomLayout.getVisibility() == View.GONE) {
            bottomLayout.setVisibility(View.VISIBLE);
        }

        Logger.i("onResume:head" + entity.toString());
        final LuckyInfoEntity.SnatchInfo info = entity.snatch_info;
        LuckyInfoEntity.WinLog winInfo = entity.win_log;
        userId = winInfo.user_id;
        if (!TextUtils.isEmpty(info.merchantId) && !TextUtils.isEmpty(info.merchantName)) {
            viewMerchantLine.setVisibility(View.VISIBLE);
            llMerchant.setVisibility(View.VISIBLE);
            tvMerchant.setText(info.merchantName);
            RxUtil.clicks(llMerchant, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.MTMerchantDetailActivity"));
                    intent.putExtra("merchant_id", info.merchantId);
                    startActivity(intent);
                }
            });
        }
//        tvActivityStatus.setVisibility(View.VISIBLE);

        banner.setImages(info.snatch_goods_album)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new BannerViewGlideUtil())
                .setIndicatorGravity(BannerConfig.CENTER)
                .setDelayTime(5000)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int i) {
                        int size = info.snatch_goods_album.size();
                        final String[] files = (String[]) info.snatch_goods_album.toArray(new String[size]);
                        imageBrower(i, files);
                    }
                })
                .start();
        tvActivityTitle.setText(info.activity_name);
        tvActivityTitleSub.setText(info.snatch_subhead);

        tvActivityIssue.setText("期号：" + info.snatch_issue);
        //送益豆
        tvLeBean.setText("送" + MoneyUtil.getLeXiangBiNoZero(info.extraBean));

        int playCount = NumberFormat.convertToInt(info.snatch_play_count, 0);
        int totalCount = NumberFormat.convertToInt(info.snatch_total_count, 0);
        leftCount = totalCount - playCount;

        progressBar.setMax(totalCount);
        progressBar.setProgress(playCount);
        progesssValue.setBackgroundResource(R.mipmap.percentage);
        progesssValue.setText(new StringBuffer().append(progressBar.getProgress() * 100 / progressBar.getMax()).append("%"));
        setPosWay();

        tvActivityNeedPeople.setText("总需人次：" + totalCount);
        tvActivityLeftPeople.setText("剩余人次：" + leftCount);

        if ("-1".equals(entity.user_play_count)) {
            tvActivityJoinPeople.setText("未登录");
            tvActivityJoinPeople.setTextColor(getResources().getColor(R.color.lucky_color_red));
        } else {
            tvActivityJoinPeople.setText(Html.fromHtml("<font color=\"#666666\">本期您已参与 </font><font color=\"#fe5062\">" + entity.user_play_count + "</font> <font color=\"#666666\">人次 </font>"));
        }


        if ("1".equals(info.snatch_status)) {
//            tvActivityStatus.setText("进行中");
//            tvActivityStatus.setBackgroundResource(R.drawable.lucky_btn_going);
            showJoinNow();
        } else if ("3".equals(info.snatch_status)) {
//            tvActivityStatus.setText("已揭晓");
//            tvActivityStatus.setBackgroundResource(R.drawable.lucky_btn_finish);
            showJoinNext();
            GlideUtil.showCirHeadNoSuffix(mContext, winInfo.photo, ivSuccess);
            tvSuccessNum.setText("期号 " + info.snatch_issue);
            tvSuccessLuckyNum.setText("幸运号码：" + winInfo.win_num);
            if (TextUtils.isEmpty(winInfo.user_name)) {
                tvSuccessName.setText("获得者：暂无昵称");
            } else {
                tvSuccessName.setText("获得者：" + winInfo.user_name);
            }
            String phone = winInfo.phone;
            tvSuccessPhone.setText(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            tvSuccessTime.setText(DateUtils.luckyTimeNoYearNoSecond(winInfo.time) + "揭晓");
            tvSuccessPeople.setText(winInfo.count + "人参与");
        } else if ("4".equals(info.snatch_status)) {
//            tvActivityStatus.setText("已揭晓");
//            tvActivityStatus.setBackgroundResource(R.drawable.lucky_btn_finish);

            tvJoinNow.setVisibility(View.GONE);
            joinNextLayout.setVisibility(View.VISIBLE);
            prizeLayout.setVisibility(View.VISIBLE);
            tvJoinNext.setBackgroundColor(getResources().getColor(R.color.lucky_color_999));
            tvJoinNext.setClickable(false);

            GlideUtil.showCirHeadNoSuffix(mContext, winInfo.photo, ivSuccess);
            tvSuccessNum.setText("期号 " + info.snatch_issue);
            tvSuccessLuckyNum.setText("幸运号码：" + winInfo.win_num);
            if (TextUtils.isEmpty(winInfo.user_name)) {
                tvSuccessName.setText("获得者：暂无昵称");
            } else {
                tvSuccessName.setText("获得者：" + winInfo.user_name);
            }
            String phone = winInfo.phone;
            tvSuccessPhone.setText(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            tvSuccessTime.setText(DateUtils.luckyTimeNoYearNoSecond(winInfo.time) + "揭晓");
            tvSuccessPeople.setText(winInfo.count + "人参与");
        } else if ("5".equals(info.snatch_status)) {
            progressBar.setProgress(0);
            progesssValue.setText(new StringBuffer().append(0).append("%"));
            setPosWay();
            tvActivityNeedPeople.setText("总需人次：" + totalCount);
            tvActivityLeftPeople.setText("剩余人次：" + 0);
//            tvActivityStatus.setText("商品已下架");
//            tvActivityStatus.setBackgroundResource(R.drawable.lucky_btn_down_line);
            tvDownLine.setVisibility(View.VISIBLE);
            viewDownLine.setVisibility(View.VISIBLE);
            Spanned spanned;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                spanned = Html.fromHtml("<font color='#999999'>" + info.snatchXjTitle + "</font><br><br><font color='#333333'><small>" + info.snatchXjDesc + "</small></font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                spanned = Html.fromHtml("<font color='#999999'>" + info.snatchXjTitle + "</font><br><br><font color='#333333'><small>" + info.snatchXjDesc + "</small></font>");
            }
            tvDownLine.setText(spanned);
            tvJoinNow.setVisibility(View.GONE);
            joinNextLayout.setVisibility(View.VISIBLE);
            tvJoinNext.setBackgroundColor(getResources().getColor(R.color.lucky_color_999));
            tvJoinNext.setClickable(false);
        }
// 看到该详情的用户是否能参加该活动 0 不可以参加 1 可以参加 该值得优先级大于snatch_status 所以最后判断
//        不可以参加时，若是正在进行中的活动，则参加按钮可点击，但是点击时提示不在范围内
        flag = info.flag;
        if (flag == 0) {
            if ("1".equals(info.snatch_status)) {
//                tvActivityStatus.setText("进行中");
//                tvActivityStatus.setBackgroundResource(R.drawable.lucky_btn_going);
                showJoinNow();
            } else {
                tvJoinNow.setVisibility(View.GONE);
                joinNextLayout.setVisibility(View.VISIBLE);
                tvJoinNext.setBackgroundColor(getResources().getColor(R.color.lucky_color_999));
                tvJoinNext.setClickable(false);
            }

        }
    }

    /**
     * 取出前十条（不足十条时取全部）做为顶部飘动参与信息
     *
     * @param newList
     */
    private void setFlutterData(ArrayList<LuckyInfoEntity.SnatchLog> newList) {
        resetAnimator();
        if (newList == null || newList.size() <= 0) {
            return;
        }
        if (newList.size() <= FLUTTER_VIEW_COUNT) {
            flutterDatas.addAll(newList);
        } else {
            flutterDatas.addAll(newList.subList(0, FLUTTER_VIEW_COUNT));
        }

        for (int i = 0; i < flutterDatas.size(); i++) {
            LuckyInfoEntity.SnatchLog snatchLog = flutterDatas.get(i);
            final LinearLayout flutterView = (LinearLayout) View.inflate(mContext, R.layout.lucky_item_lucky_detail_flutter, null);
            String userName = TextUtils.isEmpty(snatchLog.user_name) ? "暂无昵称" : snatchLog.user_name;
            ((TextView) flutterView.findViewById(R.id.tv_content)).setText("刚刚 " + userName + " 参与了" + snatchLog.count + "人次");
            GlideUtil.showCirImage(mContext, snatchLog.photo, ((ImageView) flutterView.findViewById(R.id.iv_photo)));
            rlFlutter.addView(flutterView, i);
            final int finalI = i;

            flutterView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    flutterView.removeOnLayoutChangeListener(this);
                    int flutterViewWidth = right - left;
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) flutterView.getLayoutParams();
                    layoutParams.setMargins(DPXUnitUtil.dp2px(mContext, 10), 0, 0, 0);
                    flutterView.setAlpha(0);
                    ObjectAnimator translationY = ObjectAnimator.ofFloat(flutterView, "translationY", -DPXUnitUtil.dp2px(mContext, 50));
                    translationY.setDuration(400);
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(flutterView, "alpha", 0, 1f);
                    alpha.setDuration(400);
                    ObjectAnimator translationY2 = ObjectAnimator.ofFloat(flutterView, "translationY", -DPXUnitUtil.dp2px(mContext, 50), -DPXUnitUtil.dp2px(mContext, 80));
                    translationY2.setDuration(1000);
                    ObjectAnimator alpha2 = ObjectAnimator.ofFloat(flutterView, "alpha", 1f, 0);
                    alpha2.setDuration(1000);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(alpha).with(translationY).before(alpha2);
                    animatorSet.play(alpha2).with(translationY2).after(2000);
                    animatorSet.setInterpolator(new LinearInterpolator());
                    animatorSetArrayList.add(animatorSet);
                    flutterViewList.add(flutterView);
                }
            });
        }
//        由于view的测绘需要一定的时间，所以为了保证漂浮view的位置的准确性，需要延迟做动画
        Subscription subscription = Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        myHandler.sendEmptyMessageDelayed(animatorPosition, 0);
                    }
                });
        addSubscription(subscription);
    }

    private void initFootView() {
        Logger.i("onResume:foot" + adapter.getFooterLayoutCount());
        adapter.addFooterView(footView);
        adapter.loadMoreEnd();
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.mall.ui.ImagePagerActivity"));
        intent.putExtra("image_urls", urls);
        intent.putExtra("image_index", position);
        startActivity(intent);
    }

    private void setPosWay() {
        progesssValue.post(new Runnable() {
            @Override
            public void run() {
                setPos();
            }
        });
    }

    private void showJoinNow() {
        tvJoinNow.setVisibility(View.VISIBLE);
        joinNextLayout.setVisibility(View.GONE);
        prizeLayout.setVisibility(View.GONE);

        Logger.i("ray-" + entity.toString());
        Logger.i("ray-show" + showWindow);
        if (!TextUtils.isEmpty(showWindow)) {
            if ("1".equals(showWindow)) {
                showJoinWindow();
            }
        }
    }

    private void showJoinNext() {
        tvJoinNow.setVisibility(View.GONE);
        joinNextLayout.setVisibility(View.VISIBLE);
        prizeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 重置漂浮动画
     * <p>
     * 移除数据源
     * 动画集合清空
     * 做动画的view布局清空
     * handler移除所有消息
     * 重置做动画的position
     */
    void resetAnimator() {
        rlFlutter.setVisibility(View.INVISIBLE);
        flutterDatas.clear();
        animatorSetArrayList.clear();
        rlFlutter.removeAllViews();
        flutterViewList.clear();
//        记录最多取10条，handler是根据记录的position设置的msg.what 所以msg.what范围是0-10 包含10
        for (int i = 0; i <= FLUTTER_VIEW_COUNT; i++) {
            myHandler.removeMessages(i);
        }
        animatorPosition = 0;
    }

    /**
     * 设置进度显示在对应的位置
     */
    public void setPos() {
        ViewGroup.MarginLayoutParams progesssParams = (ViewGroup.MarginLayoutParams) progressBar.getLayoutParams();
        int marginLeft = progesssParams.leftMargin;
        int w = getWindowManager().getDefaultDisplay().getWidth();
        Log.e("w=====", "" + w);
        //去除两边外间距的长度
        w = w - marginLeft * 2;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) progesssValue.getLayoutParams();
        int pro = progressBar.getProgress();
        int tW = progesssValue.getWidth();
        params.leftMargin = (int) (w * pro / progressBar.getMax() - tW * 0.5) + marginLeft;
        progesssValue.setLayoutParams(params);

    }

    private void showJoinWindow() {
//        防止操作过快，弹出时页面已经finish
        if (isFinishing()) {
            return;
        }
        if (null != joinWindow && joinWindow.isShowing()) {
            joinWindow.dismiss();
            return;
        }

        contentView = View.inflate(mContext, R.layout.lucky_dialog_join, null);
        joinWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        joinWindow.setContentView(contentView);
        joinWindow.setAnimationStyle(R.style.lucky_AnimationSEX);
        joinWindow.setFocusable(true);
        joinWindow.setOutsideTouchable(false);
        /**
         * 解决popupWindow在部分手机上被软键盘遮挡问题
         */
//        joinWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        joinWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        joinWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        joinWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        //点击背景变暗
        ColorDrawable cd = new ColorDrawable(0x000000);
        joinWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        numberView = (NumberAddSubView) contentView.findViewById(R.id.numberView);
        numberView.setMinValue(1);
        view = contentView.findViewById(R.id.view);
        tv5 = (TextView) contentView.findViewById(R.id.tv_5);
        tv20 = (TextView) contentView.findViewById(R.id.tv_20);
        tv50 = (TextView) contentView.findViewById(R.id.tv_50);
        tv100 = (TextView) contentView.findViewById(R.id.tv_100);
        tvResult = (TextView) contentView.findViewById(R.id.tv_result);
        tvJoin = (TextView) contentView.findViewById(R.id.tv_join);
        if (mCount <= 0) {
            mCount = 1;
        }
        final EditText et = numberView.getEditText();
        numberView.setValue(mCount);
        changeIntegral(mCount);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("5");
            }
        });
        tv20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("20");
            }
        });
        tv50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("50");
            }
        });
        tv100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(String.valueOf(leftCount));
            }
        });
        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (isLogin()) {
                    if (mCount <= 0) {
                        showToast("至少参加一次");
                        return;
                    }
                    intent = new Intent(LuckyActivityDetailActivity.this, LuckySubmitOrderActivity.class);
                    intent.putExtra("activity_id", entity.snatch_info.activity_id);
                    intent.putExtra("activity_name", entity.snatch_info.activity_name);
                    intent.putExtra("activity_count", String.valueOf(mCount));
                    intent.putExtra("activity_integral", entity.snatch_info.snatch_once_expend);
                    intent.putExtra("prizeListPosition", prizeListPosition);
//                    该值重置为0 返回该页面时不再弹窗
                    showWindow = "0";
                } else {
                    intent = new Intent();
                    intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                }
                startActivity(intent);
            }
        });
        numberView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {

            @Override
            public void onButtonAddClick(View view, int value) {
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                if (mCount == 1) {
                    Logger.i("value-" + "走了value==1");
                    return;
                }
            }
        });
        et.setSelection(et.getText().toString().trim().length());
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }
                number = NumberFormat.convertToInt(s.toString(), 0);
                if (compareCount(number)) {
                    mCount = number;
                } else {
                    mCount = leftCount;
                    et.setText(String.valueOf(leftCount));
                    ToastUtil.showMessage(mContext, "亲,本期仅剩" + leftCount + "次哦!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    changeIntegral(1);
                } else {
                    et.setSelection(et.getText().toString().trim().length());
                    changeIntegral(mCount);
                }
            }
        });
        et.setEnabled(true);
    }

    private void changeIntegral(int value) {
        tvResult.setText(Html.fromHtml("<font color=\"#999999\">共消耗 </font><font color=\"#fe5062\">" + MoneyUtil.getLeXiangBiNoZero(value * NumberFormat.convertToInt(entity.snatch_info.snatch_once_expend, 0)) + "</font><font color=\"#fe5062\"> 益豆</font>"));
    }

    private void dismissPopWindow() {
        if (joinWindow != null && joinWindow.isShowing()) {
            joinWindow.dismiss();

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 1f;
            getWindow().setAttributes(lp);
            Logger.i("2017年11月9日 11:03:51-走了这里dismissPopWindow");
        }
    }

    private boolean compareCount(int count) {
        if (leftCount >= count) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (joinWindow != null && joinWindow.isShowing()) {
            joinWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        resetAnimator();
        super.onDestroy();
    }

    private void initView() {
        Logger.i("ray-" + "走了initView");
        id = getIntent().getStringExtra("activity_id");
        type = getIntent().getStringExtra("type");
        showWindow = getIntent().getStringExtra("show_window");
        prizeListPosition = getIntent().getIntExtra("prizeListPosition", -1);
        if (TextUtils.isEmpty(showWindow)) {
            showWindow = "0";
        }
        Logger.i("ray-" + id);
        Logger.i("ray-" + type);
        Logger.i("ray-" + showWindow);

        width = ScreenUtils.getScreenWidth(mContext);
        if (width == 1080) {
            margin = 75;
        } else if (width == 720) {
            margin = 50;
        } else if (width == 1440) {
            margin = 100;
        } else {
            margin = 75;
        }
        s_width = width - margin * 2;
        s_height = (int) MyBigDecimal.div(MyBigDecimal.mul(s_width, 568), 946, 0);
        Logger.i("s_width-" + s_width);
        Logger.i("s_width-" + s_height);

        ivBackBlack = (ImageView) findViewById(R.id.iv_back_black);
        ivBackBlack.setOnClickListener(this);
        ivBackWhite = (ImageView) findViewById(R.id.iv_back_white);
        ivBackWhite.setOnClickListener(this);
        ivMoreBlack = (ImageView) findViewById(R.id.iv_more_black);
        ivMoreBlack.setOnClickListener(this);
        ivMoreWhite = (ImageView) findViewById(R.id.iv_more_white);
        ivMoreWhite.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        layoutNormal = (RelativeLayout) findViewById(R.id.layout_right);
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        tvJoinNow = (TextView) findViewById(R.id.tv_join_now);
        tvJoinNow.setOnClickListener(this);
        joinNextLayout = (LinearLayout) findViewById(R.id.layout_join_next);
        tvJoinNext = (TextView) findViewById(R.id.tv_join_next);
        tvJoinNext.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (headView == null) {
            headView = View.inflate(mContext, R.layout.lucky_head_activity_detail, null);
            viewDownLine = headView.findViewById(R.id.view_down_line);
            tvDownLine = (TextView) headView.findViewById(R.id.tv_down_line);
            viewMerchantLine = headView.findViewById(R.id.view_merchant_line);
            tvMerchant = (TextView) headView.findViewById(R.id.tv_merchant);
            llMerchant = (LinearLayout) headView.findViewById(R.id.ll_merchant);
            rlFlutter = (RelativeLayout) headView.findViewById(R.id.ll_flutter);
            ((RelativeLayout.LayoutParams) rlFlutter.getLayoutParams()).height = width / 2;
            banner = (Banner) headView.findViewById(R.id.banner);
            ((RelativeLayout.LayoutParams) banner.getLayoutParams()).height = width;
            tvActivityTitle = (TextView) headView.findViewById(R.id.tv_activity_title);
            tvActivityTitleSub = (TextView) headView.findViewById(R.id.tv_activity_title_sub);
            tvActivityIssue = (TextView) headView.findViewById(R.id.tv_activity_issue);
//            tvActivityStatus = (TextView) headView.findViewById(R.id.tv_activity_status);
            tvLeBean = (TextView) headView.findViewById(R.id.tv_le_bean);
            progesssValue = (TextView) headView.findViewById(R.id.progesss_value);
            progressBar = (ProgressBar) headView.findViewById(R.id.progressBar);
            tvActivityNeedPeople = (TextView) headView.findViewById(R.id.tv_activity_need_people);
            tvActivityLeftPeople = (TextView) headView.findViewById(R.id.tv_activity_left_people);
            tvActivityJoinPeople = (TextView) headView.findViewById(R.id.tv_activity_join_people);
            tvActivityJoinPeople.setOnClickListener(this);
            prizeLayout = (LinearLayout) headView.findViewById(R.id.layout_prize);
            successLayout = (LinearLayout) headView.findViewById(R.id.layout_success);
            tvInfoDetail = (TextView) headView.findViewById(R.id.tv_info_detail);
            tvInfoDetail.setOnClickListener(this);
            tvInfoUnboxing = (TextView) headView.findViewById(R.id.tv_info_unboxing);
            tvInfoUnboxing.setOnClickListener(this);
            tvInfoReview = (TextView) headView.findViewById(R.id.tv_info_review);
            tvInfoReview.setOnClickListener(this);

            frameLayout = (FrameLayout) headView.findViewById(R.id.frame_layout);
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
            linearParams.width = s_width;
            linearParams.height = s_height;
            if (linearParams == null) {
                linearParams = new RelativeLayout.LayoutParams(s_width, s_height);
            }
            frameLayout.setLayoutParams(linearParams);

            ivSuccess = (ImageView) headView.findViewById(R.id.iv_success);
            ivSuccess.setOnClickListener(this);
            tvSuccessNum = (TextView) headView.findViewById(R.id.tv_success_num);
            tvSuccessLuckyNum = (TextView) headView.findViewById(R.id.tv_success_lucky_num);
            tvSuccessName = (TextView) headView.findViewById(R.id.tv_success_name);
            tvSuccessPhone = (TextView) headView.findViewById(R.id.tv_success_phone);
            tvSuccessTime = (TextView) headView.findViewById(R.id.tv_success_time);
            tvSuccessPeople = (TextView) headView.findViewById(R.id.tv_success_people);
            tvSuccessDetail = (TextView) headView.findViewById(R.id.tv_success_detail);
            tvSuccessDetail.setOnClickListener(this);
            tvSuccessMore = (TextView) headView.findViewById(R.id.tv_success_more);
            tvSuccessMore.setOnClickListener(this);


            //这里判断分辨率
            Logger.i("2017年11月16日 11:24:36-" + width);
            FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) tvSuccessLuckyNum.getLayoutParams();
            FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) tvSuccessNum.getLayoutParams();
            FrameLayout.LayoutParams params3 = (FrameLayout.LayoutParams) successLayout.getLayoutParams();
            ivSuccess.setAdjustViewBounds(true);
            switch (width) {
                case 720:
                    params1.setMargins(0, 90, 0, 0);
                    params2.setMargins(0, 130, 0, 0);
                    params3.setMargins(50, 210, 50, 0);
                    break;
                case 1080:
                    params1.setMargins(0, 140, 0, 0);
                    params2.setMargins(0, 200, 0, 0);
                    params3.setMargins(80, 320, 80, 0);
                    break;
                case 1440:
                    params1.setMargins(0, 190, 0, 0);
                    params2.setMargins(0, 270, 0, 0);
                    params3.setMargins(110, 430, 110, 0);
                    break;
                default:
                    break;
            }
        }

        if (emptyView == null) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }

        if (errorView == null) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefreshLayout.setRefreshing(true);
                    getFirstPageData();
                }
            });
        }

        if (footView == null) {
            footView = View.inflate(mContext, R.layout.lucky_view_nothing, null);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new LuckyItemJoinRecordAdapter(R.layout.lucky_item_join_record);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.lucky_color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int distance = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Logger.i("recyclerView-newState-" + newState);
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                distance += dy;
                Logger.i("recyclerView滑动了-" + distance);
                Logger.i("height-" + height);
                if (distance <= 0) {
                    ivBackWhite.setVisibility(View.VISIBLE);
                    ivMoreWhite.setVisibility(View.VISIBLE);
                    ivMoreBlack.setVisibility(View.GONE);
                    ivBackBlack.setVisibility(View.GONE);
                    layoutTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                } else if (distance > 0 && distance <= half_height) {
                    ivBackWhite.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.VISIBLE);
                    ivMoreWhite.setVisibility(View.VISIBLE);
                    ivMoreBlack.setVisibility(View.VISIBLE);

                    float scale = (float) distance / half_height;
                    float alpha = (255 * scale);
                    ivBackWhite.setImageAlpha((int) (255 * (1 - scale)));
                    ivMoreWhite.setImageAlpha((int) (255 * (1 - scale)));
                    ivBackBlack.setImageAlpha((int) alpha);
                    ivMoreBlack.setImageAlpha((int) alpha);
                    layoutTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else if (distance > half_height && distance <= height) {
                    ivBackWhite.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.VISIBLE);
                    ivMoreWhite.setVisibility(View.VISIBLE);
                    ivMoreBlack.setVisibility(View.VISIBLE);

                    float scale = (float) distance / height;
                    float alpha = (255 * scale);
                    ivBackWhite.setImageAlpha((int) (255 * (1 - scale)));
                    ivMoreWhite.setImageAlpha((int) (255 * (1 - scale)));
                    ivBackBlack.setImageAlpha((int) alpha);
                    ivMoreBlack.setImageAlpha((int) alpha);
                    layoutTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    ivBackWhite.setVisibility(View.GONE);
                    ivMoreWhite.setVisibility(View.GONE);
                    ivMoreBlack.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.VISIBLE);
                    layoutTitle.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                }
            }
        });

        ViewTreeObserver vto = banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = banner.getHeight();
            }
        });

        adapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.iv_more_white);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }    @Override
    public void onClick(View v) {
        Intent intent = null;
        int i = v.getId();
        if (i == R.id.tv_refresh) {
            initData();
        } else if (i == R.id.iv_back_black) {
            finish();
        } else if (i == R.id.iv_back_white) {
            finish();
        } else if (i == R.id.iv_more_black) {
            showMenu();
        } else if (i == R.id.iv_more_white) {
            showMenu();
        } else if (i == R.id.tv_info_detail) { //查看图文详情
            intent = new Intent(LuckyActivityDetailActivity.this, LuckyGraphicDetailActivity.class);
            intent.putStringArrayListExtra("list", entity.snatch_info.snatch_goods_introduce);
            startActivity(intent);
        } else if (i == R.id.tv_info_unboxing) { //查看晒单列表
            intent = new Intent(LuckyActivityDetailActivity.this, LuckyAllCommentListActivity.class);
            intent.putExtra("type", "0");
            intent.putExtra("activity_id", id);
            startActivity(intent);
        } else if (i == R.id.tv_info_review) { //往期回顾
            intent = new Intent(LuckyActivityDetailActivity.this, LuckyReviewActivity.class);
            intent.putExtra("activity_id", id);
            startActivity(intent);
        } else if (i == R.id.tv_success_detail) {
            intent = new Intent(LuckyActivityDetailActivity.this, LuckyCountDetailActivity.class);
            intent.putExtra("activity_id", id);
            startActivity(intent);
        } else if (i == R.id.tv_success_more) { //查看更多参与号码
            intent = new Intent(LuckyActivityDetailActivity.this, LuckyNumberRecordActivity.class);
            intent.putExtra("activity_id", id);
            intent.putExtra("user_id", userId);
            intent.putExtra("from", "detail");
            startActivity(intent);
        } else if (i == R.id.tv_join_now) {
            if (flag == 0) {
                showToast("商家离您太远了,暂时无法参与!!!");
                return;
            }
            showJoinWindow();
        } else if (i == R.id.tv_join_next) {
            intent = new Intent(LuckyActivityDetailActivity.this, LuckyActivityDetailActivity.class);
            intent.putExtra("activity_id", id);
            intent.putExtra("type", "1");
            intent.putExtra("show_window", "1");
            startActivity(intent);
        } else if (i == R.id.tv_activity_join_people) {
            if ("-1".equals(entity.user_play_count)) {
                intent = new Intent();
                intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                startActivity(intent);
            }
        } else if (i == R.id.iv_success) {
            if (isLogin()) {
                intent = new Intent(mContext, LuckyRecordActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            } else {
                intent = new Intent();
                intent.setComponent(new ComponentName(LuckyActivityDetailActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setPos();
        }
    }

    private class MyHandler extends Handler {

        private final WeakReference<LuckyActivityDetailActivity> mLuckyDetailActivity;

        private MyHandler(LuckyActivityDetailActivity luckyDetailActivity) {
            mLuckyDetailActivity = new WeakReference<LuckyActivityDetailActivity>(luckyDetailActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rlFlutter.setVisibility(View.VISIBLE);
            for (int i = 0; i < animatorSetArrayList.size(); i++) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlFlutter.getLayoutParams();
                if (animatorPosition == i) {
                    flutterViewList.get(i).setVisibility(View.VISIBLE);
                    animatorSetArrayList.get(i).start();
                }
            }
            animatorPosition++;
            if (msg.what >= animatorSetArrayList.size()) {
                animatorPosition = 0;
                return;
            }
            myHandler.sendEmptyMessageDelayed(animatorPosition, 3000);
        }
    }



}
