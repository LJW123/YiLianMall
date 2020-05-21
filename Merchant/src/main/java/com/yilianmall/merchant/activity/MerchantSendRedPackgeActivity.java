package com.yilianmall.merchant.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.yilian.mylibrary.DecimalUtil;
import com.yilian.networkingmodule.entity.StealRedPackgesPushStatus;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;

import java.math.BigDecimal;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 发奖励
 * zyh
 * 2017-12-6
 */

public class MerchantSendRedPackgeActivity extends BaseSimpleActivity implements View.OnClickListener {
    private ImageView v3Back;
    private TextView v3Title;
    private TextView tv_right;
    private EditText totalsJiFen;
    private EditText countsJiFen;
    private TextView amountJifen;
    private TextView redpackgeMode;
    private CheckBox pushStatus;
    private Button btMmoneyPut;
    private TextView luckyTvPin;
    private TextView noticeSendMoney;
    private TextView noticeMoney;
    private boolean isPinRedMode = false;
    private boolean isSendPackge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_send_redpackges);
        initView();
        getPushStatus();
        initListener();
    }

    private void initListener() {
        v3Back.setOnClickListener(this);
        v3Title.setOnClickListener(this);
        btMmoneyPut.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        pushStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setStealRedPackgesPushStatus(1);

                } else {
                    setStealRedPackgesPushStatus(0);

                }

            }
        });
        redpackgeMode.setOnClickListener(this);
        totalsJiFen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //限制输入两位小数
                DecimalUtil.keep2Decimal(charSequence,totalsJiFen);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String jiFenCounts = editable.toString().trim();
                jiFenCounts=formatIntegrals(jiFenCounts);
                if (Double.parseDouble(jiFenCounts)==0){
                    amountJifen.setText(null);
                }else {
                    amountJifen.setText(jiFenCounts);
                }
                checkInput();
            }
        });
        countsJiFen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkInput();
            }
        });
    }
    @SuppressWarnings("unchecked")
    private void getPushStatus() {
            Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).getStealRedPackgesPushStatus("stealbonus/get_push_status")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StealRedPackgesPushStatus>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(StealRedPackgesPushStatus stealRedPackgesPushStatus) {
                            String type = stealRedPackgesPushStatus.type;
                            if ("0".equals(type)) {
                                pushStatus.setChecked(false);
                            } else {
                                pushStatus.setChecked(true);
                            }

                        }
                    });
            addSubscription(subscription);


    }
    @SuppressWarnings("unchecked")
    private void sendRedPackges(int type, double amount, String count) {
         Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).sendRedPackges("stealbonus/put_bonus", type, amount, count)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResultBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onNext(HttpResultBean stealRedPackgesPushStatus) {
                            if (dialog!=null&&dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            showToast(stealRedPackgesPushStatus.msg);
                        }
                    });
         addSubscription(subscription);

    }
    @SuppressWarnings("unchecked")
    private void setStealRedPackgesPushStatus(int type) {
            Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).setStealRedPackgesPushStatus("stealbonus/set_push", type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResultBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());

                        }

                        @Override
                        public void onNext(HttpResultBean stealRedPackgesPushStatus) {
                            showToast(stealRedPackgesPushStatus.msg);

                        }
                    });
            addSubscription(subscription);


    }

    private boolean checkInput() {
        String amount = amountJifen.getText().toString();
        String counts = countsJiFen.getText().toString();
        if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(counts)
                || Double.parseDouble(amount) <= 0
                || Integer.parseInt(counts) <= 0) {
            isSendPackge = false;
            btMmoneyPut.setBackgroundResource(R.drawable.merchant_dim_red_solid_corner);
        } else {
            btMmoneyPut.setBackgroundResource(R.drawable.merchant_bg_red_solid_corner3);
            isSendPackge = true;
        }
        return isSendPackge;
    }

    private void initView() {
        ImageView v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        luckyTvPin = (TextView) findViewById(R.id.merchant_tv_pin);
        v3Title.setText("发奖励");
        tv_right.setText("发奖励记录");
        tv_right.setVisibility(View.VISIBLE);

        btMmoneyPut = (Button) findViewById(R.id.merchant_bt_money_put);
        totalsJiFen = (EditText) findViewById(R.id.merchant_et_totals_jifen);
        countsJiFen = (EditText) findViewById(R.id.merchant_et_jifen_counts);
        amountJifen = (TextView) findViewById(R.id.merchant_tv_amount_jifen);
        redpackgeMode = (TextView) findViewById(R.id.merchant_tv_redpackge_mode);
        pushStatus = (CheckBox) findViewById(R.id.merchant_check_push_status);
        noticeMoney = (TextView) findViewById(R.id.merchant_one_money);
        noticeSendMoney = (TextView) findViewById(R.id.merchant_notice_send);


    }

    private void setSendRedPackgeMode() {
        if (isPinRedMode) {
            luckyTvPin.setVisibility(View.VISIBLE);
            noticeMoney.setText("总金额");
            noticeSendMoney.setText("当前为拼手气奖励，");
            redpackgeMode.setText("改为普通奖励");
        } else {
            luckyTvPin.setVisibility(View.GONE);
            noticeMoney.setText("单个奖励");
            noticeSendMoney.setText("用户每人收到固定金额，");
            redpackgeMode.setText("改为拼手气奖励");

        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.v3Back) {
            finish();
        } else if (id == R.id.tv_right) {
            //跳到奖励记录
            startActivity(new Intent(this, MerchantSendPackgeRecorderActivity.class));

        } else if (id == R.id.merchant_bt_money_put) {
            //塞奖励
            if (isSendPackge) {
                showPayDialog(amountJifen.getText().toString().trim());
            }
        } else if (id == R.id.merchant_tv_redpackge_mode) {
            //切换奖励类型
            isPinRedMode = !isPinRedMode;
            setSendRedPackgeMode();
        }

    }

    private Dialog dialog;
    private ImageView ivCancel;
    private TextView tvJiFen;
    private Button btSend;

    /**
     * 奖券塞进奖励
     * @param jiFen
     */

    private void showPayDialog(String jiFen) {
        if (null == dialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View dialogView = View.inflate(mContext, R.layout.merchant_dialog_send_redpackge, null);
            btSend = (Button) dialogView.findViewById(R.id.merchant_bt_send);
            tvJiFen = (TextView) dialogView.findViewById(R.id.merchant_tv_amounts);
            ivCancel = (ImageView) dialogView.findViewById(R.id.merchant_iv_cancel);
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }
            });

            btSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendRedPackge();
                }
            });
            builder.setView(dialogView);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
        }
        dialog.show();
        tvJiFen.setText(jiFen);

    }

    //发奖励
    private void sendRedPackge() {
        if (isPinRedMode) {
            sendRedPackges(2, Double.parseDouble(totalsJiFen.getText().toString().trim())*100
                    , countsJiFen.getText().toString().trim());

        } else {
            sendRedPackges(1, Double.parseDouble(totalsJiFen.getText().toString().trim())*100
                    , countsJiFen.getText().toString().trim());
        }

    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onDestroy();
    }

    public class PointLengthFilter implements InputFilter {

        /**
         * 输入框小数的位数
         */
        private static final int DECIMAL_DIGITS = 2;
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    }

    private void limitInputContont(Editable edt) {
        String temp = edt.toString();
        int posDot = temp.indexOf(".");
        if (posDot <= 0) {
            return;
        }
        if (temp.length() - posDot - 1 > 2) {
            edt.delete(posDot + 3, posDot + 4);
        }

    }

    private String formatIntegrals(String integral) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#####0.00");
        if (TextUtils.isEmpty(integral)) {
            return "0.00";
        } else {
            if (integral.contains(".")) {
                int index = integral.indexOf(".");
                if (index == 0) {//首位
                    integral = 0 + integral + 0;
                } else {
                    if (index == integral.length() - 1) {//末尾
                        integral = integral + 0;
                    }
                }
            }
            BigDecimal bd = new BigDecimal(integral);
            integral = df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));//截取末尾两位小数而不四舍5入
            return integral;
        }
    }
}
