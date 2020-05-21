package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.transfer.impl.TransferDaiGouQUanCategoryImpl;
import com.yilian.mall.ui.transfer.impl.TransferAccountDaiGouQuanImpl;
import com.yilian.mall.utils.JumpYlActivityUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mall.widgets.pulllayout.DropZoomScrollView;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilian.networkingmodule.entity.ExchangeTicketEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mylibrary.Constants.EXCHANGE_TICKET_FAQ;
import static com.yilian.mylibrary.Constants.EXCHANGE_TICKET_RECOMMEND;

/**
 * 兑换券
 *
 * @author Zg by 2018/9/5
 */
public class ExchangeTicketActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /**
     * 我的兑换券
     */
    private TextView tvMoneyName;
    private TextView tvMoney;
    private TextView tvTotalMoney;
    private TextView tvRemark;
    private LinearLayout llDaigouPresent;
    private LinearLayout llDaigouRecord;
    private DropZoomScrollView dropZoomScrollView;
    /**
     * 标题栏
     */
    private TextView v3Title;
    private ImageView v3Back;
    private ImageView v3Question;
    private LinearLayout llTitle;
    /**
     * 兑换券说明
     */
    private TextView tvRecommend1;
    /**
     * 常见问题
     */
    private TextView tvRecommend2;
    private TransferDaiGouQUanCategoryImpl transferCategory;
    private TextView tvRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_ticket);
        StatusBarUtils.transparencyBar(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvRate = findViewById(R.id.tv_rate);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setTextColor(Color.WHITE);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Question = (ImageView) findViewById(R.id.v3Question);
        v3Question.setVisibility(View.VISIBLE);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llTitle
                .getLayoutParams();
        layoutParams.setMargins(0, com.yilian.mylibrary.StatusBarUtils.getStatusBarHeight
                (mContext), 0, 0);

        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvMoneyName = (TextView) findViewById(R.id.tv_money_name);
        tvTotalMoney = (TextView) findViewById(R.id.tv_total_money);
        tvRemark = (TextView) findViewById(R.id.tv_remark);

        llDaigouPresent = (LinearLayout) findViewById(R.id.ll_daigou_present);
        llDaigouRecord = (LinearLayout) findViewById(R.id.ll_daigou_record);
        llDaigouRecord.setVisibility(View.GONE);

        tvRecommend1 = (TextView) findViewById(R.id.tv_recommend1);
        tvRecommend2 = (TextView) findViewById(R.id.tv_recommend2);
    }

    private void initData() {
        v3Title.setText("兑换券");
    }

    private void initListener() {
        //返回
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        //问号
        RxUtil.clicks(v3Question, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, EXCHANGE_TICKET_RECOMMEND
                        , false);
            }
        });
        //兑换券转赠
        RxUtil.clicks(llDaigouPresent, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpYlActivityUtils.toTransferAccountActivity(
                        mContext,
                        new TransferAccountDaiGouQuanImpl(),
                        transferCategory);
            }
        });
        //兑换券明细
        RxUtil.clicks(llDaigouRecord, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpYlActivityUtils.toRecordList(mContext, RecordListRetention.EXCHANGE_MINE);
            }
        });
        //兑换券说明
        RxUtil.clicks(tvRecommend1, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, EXCHANGE_TICKET_RECOMMEND
                        , false);
            }
        });
        //常见问题
        RxUtil.clicks(tvRecommend2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, EXCHANGE_TICKET_FAQ
                        , false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //        及时更新数据，防止奖券转赠回到该界面后数据不刷新
        getDaiGouTicket();
    }

    @SuppressWarnings("unchecked")
    private void getDaiGouTicket() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getExchangeTicket("quan/get_quan_balance")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExchangeTicketEntity>() {
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
                    public void onNext(ExchangeTicketEntity entity) {
                        transferCategory =
                                new TransferDaiGouQUanCategoryImpl(
                                        MoneyUtil.getLeXiangBi(entity.rate),
                                        MoneyUtil.getLeXiangBi(entity.minValue),
                                        MoneyUtil.getLeXiangBi(entity.quan)
                            );
                        tvRate.setText(String.format("费率: %s%%", entity.rate));
                        setData(entity);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(ExchangeTicketEntity mData) {
        tvMoneyName.setText("我的兑换券");
        tvMoney.setText(MoneyUtil.getLeXiangBiNoZero(mData.quan));
        Spanned spanned;
        Spanned spanned1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml("累计获得兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.income) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            spanned1 = Html.fromHtml("累计使用兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.expend) + "</font>", Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml("累计获得兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.income) + "</font>");
            spanned1 = Html.fromHtml("累计使用兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.expend) + "</font>");
        }
        tvTotalMoney.setText(spanned);
        tvRemark.setText(spanned1);
        llDaigouRecord.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_right:
//                break;
//            case R.id.tv_right2:
//                break;
//            case R.id.v3Back:
//                break;
//            default:
//                break;
        }
    }
}
