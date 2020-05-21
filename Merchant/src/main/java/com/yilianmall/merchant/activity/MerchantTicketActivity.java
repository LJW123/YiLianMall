package com.yilianmall.merchant.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilian.mylibrary.widget.DropZoomScrollView;
import com.yilian.networkingmodule.entity.AngelBalanceEntity;
import com.yilian.networkingmodule.entity.VerificationExchangeTicketEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.dialog.ExtractAngelPopwindow;
import com.yilianmall.merchant.dialog.InputPopwindow;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mylibrary.Constants.EXTRACT_ANGEL_FAQ;
import static com.yilian.mylibrary.Constants.EXTRACT_ANGEL_RECOMMEND;
import static com.yilian.mylibrary.Constants.VERIFICATION_EXCHANGE_TICKET_FAQ;
import static com.yilian.mylibrary.Constants.VERIFICATION_EXCHANGE_TICKET_RECOMMEND;

/**
 * 兑换券
 *
 * @author Zg by 2018/9/5
 */
public class MerchantTicketActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 我的兑换券
     */
    private TextView tvMoneyName;
    private TextView tvMoney;
    private TextView tvTotalMoney;
    private TextView tvRemark;
    //    private LinearLayout llDaigouPresent;
    private LinearLayout llRecord;
    private TextView tvRecord;
    private DropZoomScrollView dropZoomScrollView;
    /**
     * 标题栏
     */
    private TextView v3Title;
    private ImageView v3Back;
    private ImageView v3Question;
    private LinearLayout llTitle;
    /**
     * 按钮
     */
    private Button btnApply;
    /**
     * XXX说明
     */
    private TextView tvRecommend1;
    /**
     * 常见问题
     */
    private TextView tvRecommend2;

    /**
     * 申请核销数量 弹出框
     */
    private InputPopwindow inputPopwindow;
    /**
     * 提取乐天使 弹出框
     */
    private ExtractAngelPopwindow extractAngelPopwindow;
    /**
     * 记录列表类型
     */
    private int recordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_ticket);
        StatusBarUtils.transparencyBar(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
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

//        llDaigouPresent = (LinearLayout) findViewById(R.id.ll_daigou_present);
        llRecord = (LinearLayout) findViewById(R.id.ll_record);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        llRecord.setVisibility(View.GONE);

        btnApply = findViewById(R.id.btn_apply);
        tvRecommend1 = (TextView) findViewById(R.id.tv_recommend1);
        tvRecommend2 = (TextView) findViewById(R.id.tv_recommend2);
    }

    private void initData() {
        recordType = getIntent().getIntExtra("recordType", -1);
        if (recordType == -1) {
            showToast("数据处理异常");
            return;
        }
        switch (recordType) {
            case RecordListRetention.EXCHANGE_VERIFICATION:
                //服务中心带核销
                v3Title.setText("待核销兑换券");
                tvMoneyName.setText("待核销兑换券");
                btnApply.setText("申请核销");
                tvRecord.setText("待核销兑换券明细");
                tvRecommend1.setText("核销兑换券说明");
                getVerificationExchangeTicket();
                break;
            case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                //待提取乐天使
                v3Title.setText("待提取天使");
                tvMoneyName.setText("总额");
                btnApply.setText("提取至乐淘天使");
                tvRecord.setText("待提取天使明细");
                tvRecommend1.setText("待提取天使说明");
                getAngelBalance();
                break;
            default:
                break;
        }

    }

    private void initListener() {
        //返回
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
    }

    /**
     * 获取 待核销兑换券 数据
     */
    @SuppressWarnings("unchecked")
    private void getVerificationExchangeTicket() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getVerificationExchangeTicket("agent/user_agent_info")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VerificationExchangeTicketEntity>() {
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
                    public void onNext(VerificationExchangeTicketEntity entity) {
                        setVerificationExchangeTicketData(entity);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取 商家天使首页 数据
     */
    @SuppressWarnings("unchecked")
    private void getAngelBalance() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getAngelBalance("get_angel_balance")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AngelBalanceEntity>() {
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
                    public void onNext(AngelBalanceEntity entity) {
                        setAngelBalance(entity);
                    }
                });
        addSubscription(subscription);
    }

    private void setVerificationExchangeTicketData(final VerificationExchangeTicketEntity mData) {

        tvMoney.setText(MoneyUtil.getLeXiangBiNoZero(mData.agencyQuan));
        if (Double.valueOf(mData.agencyQuan) > 0) {
            btnApply.setEnabled(true);
            btnApply.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            btnApply.setEnabled(false);
            btnApply.setTextColor(Color.parseColor("#999999"));
        }
        Spanned spanned;
        Spanned spanned1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml("累计收兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.incomeQuan) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            spanned1 = Html.fromHtml("累计核销兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.expendQuan) + "</font>", Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml("累计收兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.incomeQuan) + "</font>");
            spanned1 = Html.fromHtml("累计核销兑换券<br><font color='#333333'><big><big>" +
                    MoneyUtil.getLeXiangBiNoZero(mData.expendQuan) + "</font>");
        }
        tvTotalMoney.setText(spanned);
        tvRemark.setText(spanned1);
        llRecord.setVisibility(View.VISIBLE);

        //问号
        RxUtil.clicks(v3Question, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, VERIFICATION_EXCHANGE_TICKET_RECOMMEND
                        , false);
            }
        });

        RxUtil.clicks(llRecord, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.RecordListActivity"));
                intent.putExtra("recordType", RecordListRetention.EXCHANGE_VERIFICATION);
                startActivity(intent);
            }
        });
        RxUtil.clicks(btnApply, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (inputPopwindow == null) {
                    inputPopwindow = new InputPopwindow(mContext);
                    inputPopwindow.setTitle("申请核销数量");
                    inputPopwindow.setInputMax(mData.agencyQuan);
                    inputPopwindow.confirm(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String verificationNum = inputPopwindow.getEtInput();
                            if (TextUtils.isEmpty(verificationNum)) {
                                showToast("请输入申请核销数量");
                            } else {
                                applyVerification(verificationNum);
                            }

                        }
                    });
                }
                inputPopwindow.setEtInputIsEmpty();
                inputPopwindow.showPop(btnApply);
            }
        });

        RxUtil.clicks(tvRecommend1, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, VERIFICATION_EXCHANGE_TICKET_RECOMMEND
                        , false);
            }
        });
        //常见问题
        RxUtil.clicks(tvRecommend2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, VERIFICATION_EXCHANGE_TICKET_FAQ
                        , false);

            }
        });
    }

    private void setAngelBalance(final AngelBalanceEntity mData) {
        tvMoney.setText(mData.userAngel);
        if (Double.valueOf(mData.userAngel) > 0) {
            btnApply.setEnabled(true);
            btnApply.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            btnApply.setEnabled(false);
            btnApply.setTextColor(Color.parseColor("#999999"));
        }
        Spanned spanned;
        Spanned spanned1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml("累计产生天使<br><font color='#333333'><big><big>" +
                    mData.incomeAngel.substring(0, mData.incomeAngel.length() - 6) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            spanned1 = Html.fromHtml("累计提取天使<br><font color='#333333'><big><big>" +
                    mData.incomeAngel.substring(0, mData.expendAngel.length() - 6) + "</font>", Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml("累计产生天使<br><font color='#333333'><big><big>" +
                    mData.incomeAngel.substring(0, mData.incomeAngel.length() - 6) + "</font>");
            spanned1 = Html.fromHtml("累计提取天使<br><font color='#333333'><big><big>" +
                    mData.incomeAngel.substring(0, mData.expendAngel.length() - 6) + "</font>");
        }
        tvTotalMoney.setText(spanned);
        tvRemark.setText(spanned1);
        llRecord.setVisibility(View.VISIBLE);

        //问号
        RxUtil.clicks(v3Question, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, EXTRACT_ANGEL_RECOMMEND
                        , false);
            }
        });

        RxUtil.clicks(llRecord, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.RecordListActivity"));
                intent.putExtra("recordType", RecordListRetention.WAIT_EXTRACT_LE_ANGEL);
                startActivity(intent);
            }
        });
        RxUtil.clicks(btnApply, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (extractAngelPopwindow == null) {
                    extractAngelPopwindow = new ExtractAngelPopwindow(mContext);

                    final String phone = PreferenceUtils.readStrConfig(Constants.SPKEY_PHONE, mContext);
                    String content = "确定将全部天使提取至" + phone + "帐号的挖矿收益中？";

                    SpannableString spannableString = new SpannableString(content);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#F22424")), 10, 10 + phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    extractAngelPopwindow.setContent(spannableString);
                    extractAngelPopwindow.confirm(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            angelToBalance();
                            extractAngelPopwindow.dismiss();
                        }
                    });
                }
                extractAngelPopwindow.showPop(btnApply);
            }
        });

        RxUtil.clicks(tvRecommend1, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, EXTRACT_ANGEL_RECOMMEND
                        , false);
            }
        });
        //常见问题
        RxUtil.clicks(tvRecommend2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, EXTRACT_ANGEL_FAQ
                        , false);

            }
        });
    }


    private void applyVerification(String verificationNum) {
        Double d = Double.valueOf(verificationNum);
        verificationNum = String.valueOf(d * 100);

        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .applyVerification("quan/apply_verification", verificationNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        if (inputPopwindow != null) {
                            inputPopwindow.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean entity) {
                        showToast("申请核销成功");
                        getVerificationExchangeTicket();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 商家提取天使
     */
    private void angelToBalance() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .angelToBalance("angelToBalance")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean entity) {
                        showToast("提取成功");
                        getAngelBalance();
                    }
                });
        addSubscription(subscription);
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
