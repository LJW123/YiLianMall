package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leshan.ylyj.view.activity.bankmodel.SetUpCashCardsActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.CashSuccessModel;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CodeException;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.TakeCashEntity2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.leshan.ylyj.view.activity.bankmodel.SetUpCashCardsActivity.RESULT_SET_BANK_OK;


/**
 * Created by Ray_L_Pain on 2017/7/7 0007.
 */

public class JTakeCashActivity extends BaseActivity {
    public static final int CHECK_BANK_CARD_REQUEST_CODE = 22;
    @ViewInject(R.id.v3Back)
    ImageView v3Back;
    @ViewInject(R.id.v3Left)
    ImageView v3Left;
    @ViewInject(R.id.v3Title)
    TextView v3Title;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.v3Shop)
    ImageView v3Shop;
    @ViewInject(R.id.layout_card)
    RelativeLayout layout_card;
    @ViewInject(R.id.layout_no_card)
    LinearLayout layout_no_card;
    @ViewInject(R.id.layout_has_card)
    LinearLayout layout_has_card;
    @ViewInject(R.id.iv_has_card)
    ImageView iv_has_card;
    @ViewInject(R.id.tv1_has_card)
    TextView tv1_has_card;
    @ViewInject(R.id.tv2_has_card)
    TextView tv2_has_card;
    @ViewInject(R.id.tv_all_money)
    TextView tv_all_money;
    @ViewInject(R.id.tv_take_money)
    TextView tv_take_money;
    @ViewInject(R.id.btn_take)
    Button btn_take;
    @ViewInject(R.id.et)
    EditText etInputMoney;
    @ViewInject(R.id.tv_explain_first)
    TextView tvExplainFirst;
    @ViewInject(R.id.tv_explain_four)
    TextView tvExplainFour;
    @ViewInject(R.id.tv_explain_five)
    TextView tvExplainFive;
    @ViewInject(R.id.tv_explain_six)
    TextView tvExplainSix;

    private int all_money;
    private String money;
    private int status;
    private String card_index;

    private String bankId, card_holder, card_bank, branch_bank, pro_id, city_id, county_id, pro_name, city_name, county_name, card_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_cash);
        ViewUtils.inject(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLogin()) {
            initData();
        } else {
            finish();
        }
    }

    private void initView() {
        btn_take.setClickable(false);
        v3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        v3Left.setVisibility(View.INVISIBLE);
        v3Title.setText("使用奖励");
        v3Shop.setVisibility(View.GONE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("使用奖励记录");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BalanceDetailActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });
        layout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheckBankCardActivity();
            }
        });
        tv_take_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputMoney.setText(money);
                if (!TextUtils.isEmpty(money)) {
                    etInputMoney.setSelection(money.length());
                }
            }
        });
        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCash();
            }
        });
        etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etInputMoney.getText().toString().length() >= 3 && Integer.parseInt(etInputMoney.getText().toString()) % 100 == 0) {
                    Logger.i("输入金额满足提现");
                    btn_take.setBackgroundResource(R.drawable.bg_solid_red_90);
                    btn_take.setClickable(true);
                } else {
                    Logger.i("输入金额  不满足提现");
                    btn_take.setBackgroundResource(R.drawable.bg_solid_red50_90);
                    btn_take.setClickable(false);
                }
            }
        });
        etInputMoney.requestFocus();

        tvExplainFirst.setText(Html.fromHtml("<font color=\"#999999\">1.每次奖励金额 </font><font color=\"#fe5062\">≥100.00 " +
                "</font><font color=\"#999999\">，领取金额必须是 </font><font color=\"#fe5062\">100 </font><font color=\"#999999\">的整数倍。 </font>"));

        tvExplainFour.setText(Html.fromHtml("<font color=\"#999999\">1.工作日领奖励, </font><font color=\"#fe5062\">T+1 </font><font color=\"#999999\">到银行卡上。 </font>"));

        tvExplainFive.setText(Html.fromHtml("<font color=\"#999999\">2.节假日领奖励，顺延节假日后第 </font><font color=\"#fe5062\">2 </font><font color=\"#999999\">个工作日到账。 </font>"));

        tvExplainSix.setText(Html.fromHtml("<font color=\"#999999\">3.如有疑问，请拨打 </font><font color=\"#00bef7\">400-152-5159 </font><font color=\"#999999\">进行咨询。 </font>"));
        tvExplainSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-152-5159"));
                startActivity(intent);
            }
        });
    }

    /**
     * 新版获取默认提现银行卡
     */
    private void getTakeCashDefaultBankCard() {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getTakeCashDefaultCard("userBank/my_default_bank")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TakeCashEntity2>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        if (e instanceof CodeException) {
                            if (0 == ((CodeException) e).code) {
//                                暂无银行卡
                                showToast("请先绑定银行卡");
                                showNoCardDialog();
                            } else {
                                showToast(e.getMessage());
                            }
                        } else {
                            showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(TakeCashEntity2 takeCashEntity2) {
                        showHasCard();
                        TakeCashEntity2.InfoBean cardInfo = takeCashEntity2.info;
                        GlideUtil.showImageNoSuffix(mContext, cardInfo.bankLogo, iv_has_card);
                        tv1_has_card.setText(cardInfo.cardTypeCn);
                        tv2_has_card.setText(cardInfo.cardBank + "尾号" + cardInfo.cardNumber);
                        all_money = NumberFormat.convertToInt(takeCashEntity2.availableCash, 0);
                        money = String.valueOf(all_money / 100);
                        tv_all_money.setText(all_money / 100 + "");
                        card_index = cardInfo.cardIndex;
                    }
                });
        addSubscription(subscription);
    }

    private void startCheckBankCardActivity() {
        startActivityForResult(new Intent(mContext, SetUpCashCardsActivity.class), CHECK_BANK_CARD_REQUEST_CODE);
    }

    private void initData() {
        getTakeCashDefaultBankCard();
    }

    private void showHasCard() {
        layout_no_card.setVisibility(View.GONE);
        layout_has_card.setVisibility(View.VISIBLE);
    }


    private void takeCash() {
        String cash = etInputMoney.getText().toString().trim();
        if (TextUtils.isEmpty(cash)) {
            showToast(R.string.please_write_number);
            return;
        }
        if (Integer.parseInt(cash) > all_money || Integer.parseInt(cash) == 0) {
            showToast(R.string.cash_not_enough);
            return;
        }
        if (Integer.parseInt(cash) % 100 != 0) {
            showToast(R.string.cash_not_100);
            return;
        }
        startMyDialog();
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext).takeCash(cash, card_index, new Callback<CashSuccessModel>() {
                @Override
                public void onResponse(Call<CashSuccessModel> call, Response<CashSuccessModel> response) {
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                        stopMyDialog();
                        switch (response.body().code) {
                            case 1:
                                sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                Intent intent = new Intent(mContext, JTakeCashSuccessActivity.class);
                                intent.putExtra("card_name", response.body().cardBank);
                                intent.putExtra("card_num", response.body().cardNumber);
                                intent.putExtra("card_time", response.body().time);
                                intent.putExtra("card_money", response.body().cashAmount);
                                startActivityForResult(intent, Constants.TAKE_CASH_SUCCESS);
                                break;
                            case -17:
                                startCheckBankCardActivity();
                                break;
                            default:
                                showToast(response.body().msg);
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<CashSuccessModel> call, Throwable t) {
                    stopMyDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.TAKE_CASH_SUCCESS && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == CHECK_BANK_CARD_REQUEST_CODE && resultCode == RESULT_SET_BANK_OK) {
            getTakeCashDefaultBankCard();
        }
    }

    private void showNoCardDialog() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setTitle("温馨提示");
        builder.setMessage("您尚未绑定银行卡，是否现在绑定？");
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startCheckBankCardActivity();
                dialog.dismiss();
            }
        });
        builder.show();
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.color_red));
        builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.color_333));
    }
}
