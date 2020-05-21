package com.yilianmall.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ImgCodeUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.MerchantCartListEntity;
import com.yilian.networkingmodule.entity.MerchantMakeOrderEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.MerchantCartListAdapter;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/8/26 0026.
 */

public class MerchantShopActivity extends BaseActivity implements MerchantCartListAdapter.RefreshInterface {
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;
    private TextView tvScan;
    private TextView tvWrite;
    private LinearLayout layoutNoData;
    private RecyclerView recyclerView;
    private TextView tvTotalSell;
    private TextView tvTotalConsume;
    private TextView tvTotalCount;
    private TextView tvTotalPay;
    private TextView tvPayCodeBtn;
    private TextView tvPayCashBtn;
    private TextView tvPayCashMoney;
    private TextView tvPayCodeMoney;
    private LinearLayout layoutPayCash;
    private LinearLayout layoutPayCode;
    private LinearLayout bottomLayout;

    private ArrayList<MerchantCartListEntity.DataBean> list = new ArrayList<>();
    private MerchantCartListAdapter adapter;

    ///dialog
    EditText dialogEt;
    LinearLayout layoutImgCode, layoutSmsCode;
    EditText etImgCode;
    ImageView ivImg;
    ImageView imgCode;
    EditText etSmsCode;
    Button tvSmsCode;
    TextView dialogTv;
    ImageView dialogIv;
    String phoneStr, imgCodeStr, smsCodeStr;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SMS_CODE_SUCCESS:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.i("ray-" + "走了这里2");
                            if (smsCodeGap == 0) {
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Logger.i("ray-" + "走了这里3");
                                        ImgCodeRefresh();
                                        tvSmsCode.setClickable(true);
                                        tvSmsCode.setText("重新发送");
                                        tvSmsCode.setBackgroundResource(R.drawable.merchant_bg_btn_red_radious_3);
                                    }
                                });
                            } else {
                                if (smsCodeGap > 0) {
                                    smsCodeGap--;
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Logger.i("ray-" + "走了这里4");
                                            tvSmsCode.setText(smsCodeGap + "秒后重新发送");
                                            tvSmsCode.setBackgroundResource(R.drawable.merchant_bg_btn_gray_3);
                                        }
                                    });

                                    try {
                                        Thread.sleep(1000);
                                        if (handler != null) {
                                            handler.sendEmptyMessage(SMS_CODE_SUCCESS);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }).start();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_shop);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(cardIdList.toString())) {
                    finish();
                } else {
                    showBackDialog();
                }
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("售卖商品列表");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("交易记录");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MerchantBarcodeTransactionRecodeActivity.class));
            }
        });
        tvScan = (TextView) findViewById(R.id.tv_scan);
        tvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                打开二维码/条码扫描界面
                JumpToOtherPageUtil.getInstance().jumToMipcaActivityCapture(mContext,new Intent());
            }
        });
        tvWrite = (TextView) findViewById(R.id.tv_write);
        tvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                打开条码查询界面
                startActivity(new Intent(mContext, MerchantInputBarCodeSearchActivity.class));
            }
        });
        layoutNoData = (LinearLayout) findViewById(R.id.layout_no_data);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tvTotalSell = (TextView) findViewById(R.id.tv_total_sell);
        tvTotalConsume = (TextView) findViewById(R.id.tv_total_consume);
        tvTotalCount = (TextView) findViewById(R.id.tv_total_count);
        tvTotalPay = (TextView) findViewById(R.id.tv_total_pay);
        tvPayCodeBtn = (TextView) findViewById(R.id.tv_pay_code_btn);
        tvPayCashBtn = (TextView) findViewById(R.id.tv_pay_cash_btn);
        tvPayCashMoney = (TextView) findViewById(R.id.tv_pay_cash_money);
        tvPayCodeMoney = (TextView) findViewById(R.id.tv_pay_code_money);
        layoutPayCash = (LinearLayout) findViewById(R.id.layout_pay_cash);
        layoutPayCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCashDialog();
            }
        });
        layoutPayCode = (LinearLayout) findViewById(R.id.layout_pay_code);
        layoutPayCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder("0", "0");
            }
        });
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
    }

    private void showNoData() {
        stopMyDialog();
        layoutNoData.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showShopList() {
        stopMyDialog();
        if (adapter == null) {
            adapter = new MerchantCartListAdapter(mContext, list, MerchantShopActivity.this);
            adapter.setRefreshInterface(this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        layoutNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private float totalPrice;
    private float totalBouns; //总让利额
    private float totalMerchantIntegral;
    private float totalUserIntegral;
    private int totalCount;
    private StringBuffer cardIdList;

    private void initInfo() {
        totalPrice = 0f;
        totalBouns = 0f;
        totalMerchantIntegral = 0f;
        totalUserIntegral = 0f;
        totalCount = 0;
        cardIdList = new StringBuffer();
        Logger.i("2017年9月1日 11:12:33-" + list.toString());
        for (int i = 0; i < list.size(); i++) {
            MerchantCartListEntity.DataBean dataBean = list.get(i);
            totalPrice += dataBean.goodsCount * dataBean.goodsCostPrice;
            totalBouns += dataBean.goodsCount * dataBean.bonus;
            totalMerchantIntegral += dataBean.goodsCount * dataBean.merchantIntegral;
            totalUserIntegral += dataBean.goodsCount * dataBean.userIntegral;
            totalCount += dataBean.goodsCount;
            cardIdList.append(dataBean.cartIndex);
            cardIdList.append(",");
        }

        tvTotalPay.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(totalPrice)));
        tvTotalSell.setText(MoneyUtil.getLeXiangBiNoZero(totalMerchantIntegral) + "分");
        tvTotalConsume.setText(MoneyUtil.getLeXiangBiNoZero(totalUserIntegral) + "分");
        tvTotalCount.setText("X" + totalCount);

        Logger.i("2017年9月1日 11:12:33-" + totalBouns);
        if (totalBouns == 0) {
            tvPayCashMoney.setTextColor(getResources().getColor(R.color.merchant_color_999));
            tvPayCashBtn.setTextColor(getResources().getColor(R.color.merchant_color_999));
            layoutPayCash.setClickable(false);
        } else {
            tvPayCashMoney.setTextColor(getResources().getColor(R.color.merchant_color_333));
            tvPayCashBtn.setTextColor(getResources().getColor(R.color.merchant_color_333));
            layoutPayCash.setClickable(true);
        }
        tvPayCodeMoney.setText("（应收：" + MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(totalPrice)) + "）");

        if (totalPrice == 0) {
            layoutPayCode.setBackgroundColor(getResources().getColor(R.color.merchant_color_ccc));
            layoutPayCode.setClickable(false);
        } else {
            layoutPayCode.setBackgroundColor(getResources().getColor(R.color.merchant_color_red));
            layoutPayCode.setClickable(true);
        }
        tvPayCashMoney.setText("（应付：" + MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(totalBouns)) + "）");

        if (cardIdList.toString().endsWith(",")) {
            cardIdList.deleteCharAt(cardIdList.length() - 1);
        }

        if (TextUtils.isEmpty(cardIdList.toString())) {
            bottomLayout.setVisibility(View.GONE);
        } else {
            bottomLayout.setVisibility(View.VISIBLE);
        }
    }

    private void loadData() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantCartList(new Callback<MerchantCartListEntity>() {
                    @Override
                    public void onResponse(Call<MerchantCartListEntity> call, Response<MerchantCartListEntity> response) {
                        HttpResultBean resultBean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, resultBean)) {
                            if (CommonUtils.serivceReturnCode(mContext, resultBean.code, resultBean.msg)) {
                                MerchantCartListEntity entity = response.body();
                                switch (entity.code) {
                                    case 1:
                                        if (list.size() != 0) {
                                            list.clear();
                                        }
                                        ArrayList<MerchantCartListEntity.DataBean> moreList = entity.data;
                                        list.addAll(moreList);
                                        if (list.size() == 0 || list == null) {
                                            showNoData();
                                        } else {
                                            showShopList();
                                        }
                                        initInfo();
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<MerchantCartListEntity> call, Throwable t) {
                        showToast(R.string.merchant_system_busy);
                        showNoData();
                        list.clear();
                        initInfo();
                    }
                });
    }

    /**
     * 下订单---二维码收款
     */
    private void makeOrder(final String phoneStr, String smsCodeStr) {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).
                merchantMakeOrder(cardIdList.toString(), phoneStr, smsCodeStr, new Callback<MerchantMakeOrderEntity>() {
            @Override
            public void onResponse(Call<MerchantMakeOrderEntity> call, Response<MerchantMakeOrderEntity> response) {
                stopMyDialog();
                HttpResultBean resultBean = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, resultBean)) {
                    if (CommonUtils.serivceReturnCode(mContext, resultBean.code, resultBean.msg)) {
                        switch (resultBean.code) {
                            case 1:
                                MerchantMakeOrderEntity entity = response.body();
                                Intent intent = null;
                                if ("0".equals(phoneStr)) {
                                    intent = new Intent(MerchantShopActivity.this, MerchantCollectionRqcodeActivity.class);
                                    intent.putExtra("order_index", entity.list.orderIndex);
                                    intent.putExtra("type", "MerchantShopActivity");
                                } else {
                                    intent = new Intent(MerchantShopActivity.this, MerchantResalePayActivity.class);
                                    intent.putExtra("order_index", entity.list.orderIndex);
                                    intent.putExtra("profitCash", MoneyUtil.getLeXiangBiNoZero(entity.totalGoodsPrice));
                                    intent.putExtra("type", "8");
                                    intent.putExtra("from_type", "MerchantShopActivity");
                                    intent.putExtra("phone", phoneStr);
                                }
                                startActivity(intent);
                                MerchantShopActivity.this.finish();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MerchantMakeOrderEntity> call, Throwable t) {
                stopMyDialog();
                showToast(R.string.merchant_system_busy);
            }
        });
    }

    //true为未注册用户  false为已注册用户
    boolean flag = false;

    /**
     * 现金付款dialog
     */
    private void showCashDialog() {
        final AlertDialog builder = new AlertDialog.Builder(mContext).create();
        View view = View.inflate(mContext, R.layout.merchant_dialog_cash_pay, null);
        builder.setView(view);
        builder.setCancelable(true);
        ///////////dialog的控件////////////////
        dialogEt = (EditText) view.findViewById(R.id.et);
        dialogIv = (ImageView) view.findViewById(R.id.iv);
        dialogIv.setVisibility(View.GONE);
        //
        layoutImgCode = (LinearLayout) view.findViewById(R.id.layout_img_code);
        layoutImgCode.setVisibility(View.GONE);
        ivImg = (ImageView) view.findViewById(R.id.iv_img);
        etImgCode = (EditText) view.findViewById(R.id.et_img_code);
        imgCode = (ImageView) view.findViewById(R.id.iv_img_code);
        imgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgCodeRefresh();
            }
        });
        //
        layoutSmsCode = (LinearLayout) view.findViewById(R.id.layout_sms_code);
        layoutSmsCode.setVisibility(View.GONE);
        etSmsCode = (EditText) view.findViewById(R.id.et_sms_code);
        tvSmsCode = (Button) view.findViewById(R.id.tv_sms_code);
        tvSmsCode.setClickable(false);
        //
        dialogTv = (TextView) view.findViewById(R.id.tv);
        dialogTv.setVisibility(View.GONE);
        TextView tvBtnCancel = (TextView) view.findViewById(R.id.tv_cancel_btn);
        TextView tvBtnNext = (TextView) view.findViewById(R.id.tv_next_btn);

        builder.show();

        dialogEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneStr = dialogEt.getText().toString().trim();
                int length = phoneStr.length();
                if (length >= 11) {
                    startMyDialog(false);
                    dialogEt.requestFocus();
                    if (!CommonUtils.isPhoneNumer(phoneStr)) {
                        dialogIv.setVisibility(View.GONE);
                        stopMyDialog();
                        showToast(R.string.merchant_phone_number_not_legal);
                        return;
                    }
                    RetrofitUtils.getInstance(mContext).checkUser(phoneStr, new Callback<CheckUserEntity>() {
                        @Override
                        public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                            stopMyDialog();
                            BaseEntity body = response.body();
                            if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                showToast(R.string.merchant_module_service_exception);
                                return;
                            }
                            if (body.code == 1) {
                                flag = true;
                                dialogTv.setVisibility(View.VISIBLE);
                                dialogTv.setText(Html.fromHtml("<font color=\"#888888\">该手机号还</font><font color=\"#fe5062\">未注册" +
                                        "</font><font color=\"#888888\">,点击“下一步”将自动注册成为会员,立享消费发奖励。</font>"));
                                dialogEt.setTextColor(getResources().getColor(R.color.merchant_color_red));
                                //
                                layoutImgCode.setVisibility(View.VISIBLE);
                                ImgCodeRefresh();
                                //
                                layoutSmsCode.setVisibility(View.VISIBLE);
                            } else if (body.code == -12) {
                                dialogIv.setVisibility(View.VISIBLE);
                                flag = false;
                            } else {
                                showToast(R.string.merchant_module_service_exception);
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                            showToast(R.string.network_module_net_work_error);
                        }
                    });
                } else {
                    dialogEt.setTextColor(getResources().getColor(R.color.merchant_color_333));
                    dialogIv.setVisibility(View.GONE);
                    dialogTv.setVisibility(View.GONE);
                    layoutImgCode.setVisibility(View.GONE);
                    layoutSmsCode.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etImgCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imgCodeStr = etImgCode.getText().toString().trim();
                if (imgCodeStr.length() == 4) {
                    tvSmsCode.setClickable(true);
                    tvSmsCode.setBackgroundResource(R.drawable.merchant_bg_btn_red_radious_3);
                } else {
                    etImgCode.setTextColor(getResources().getColor(R.color.merchant_color_333));
                    tvSmsCode.setClickable(false);
                    tvSmsCode.setBackgroundResource(R.drawable.merchant_bg_btn_gray_3);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != imgCodeStr) {
                    if (imgCodeStr.length() == 4) {
                        getSmsCode(null);
                    }
                } else {
                    showToast(R.string.merchant_img_number_not_legal);
                }
            }
        });

        tvBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        tvBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneStr = dialogEt.getText().toString().trim();
                smsCodeStr = etSmsCode.getText().toString().trim();
                int length = phoneStr.length();
                if (length >= 11) {
                    if (!CommonUtils.isPhoneNumer(phoneStr)) {
                        showToast(R.string.merchant_phone_number_not_legal);
                        return;
                    }

                    if (flag) {
                        if (TextUtils.isEmpty(imgCodeStr) || imgCodeStr.length() != 4) {
                            showToast(R.string.merchant_img_number_not_legal);
                            return;
                        }
                        if (TextUtils.isEmpty(smsCodeStr) || smsCodeStr.length() != 6) {
                            showToast(R.string.merchant_sms_number_not_legal);
                            return;
                        }
                    }

                    if (TextUtils.isEmpty(smsCodeStr)) {
                        smsCodeStr = "0";
                    }
                    makeOrder(phoneStr, smsCodeStr);

                    builder.dismiss();
                } else {
                    showToast(R.string.merchant_phone_number_not_legal);
                }
            }
        });
    }

    private void ImgCodeRefresh() {
        ImgCodeUtil.setImgCode(phoneStr, imgCode, mContext);
    }

    private int smsCodeGap = Constants.SMS_COUNT_TIME;
    public static final int SMS_CODE_SUCCESS = 9;

    /**
     * 获取短信验证码
     */
    private void getSmsCode(String voice) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSmsCode(phoneStr, "0", imgCodeStr, voice, "1", new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        etImgCode.setTextColor(getResources().getColor(R.color.merchant_color_333));
                                        //短信et获取焦点
                                        etSmsCode.setFocusable(true);
                                        etSmsCode.setFocusableInTouchMode(true);
                                        etSmsCode.requestFocus();
                                        etSmsCode.findFocus();
                                        showToast("发送成功");
                                        smsCodeGap = Constants.SMS_COUNT_TIME;
                                        if (handler != null) {
                                            handler.sendEmptyMessage(SMS_CODE_SUCCESS);
                                        }
                                        tvSmsCode.setClickable(false);
                                        break;
                                    case -2:
                                        showToast("操作频繁");
                                        break;
                                    case -3://操作频繁
                                        break;
                                }
                            } else {
                                //如果返回码为0正码图形验证码验证错误

                                switch (response.body().code) {
                                    case 0:
                                        etImgCode.setTextColor(getResources().getColor(R.color.merchant_color_red));
                                        ImgCodeRefresh();
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.merchant_module_service_exception);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 离开当前页面的dialog
     */
    private void showBackDialog() {
        AlertDialog builder = new AlertDialog.Builder(mContext).create();
        builder.setMessage("当前有未结账的商品，返回后售卖列表将清空，是否确定返回？");
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                clearShopList();
            }
        });
        builder.show();
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.merchant_color_red));
        builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.merchant_color_333));
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(cardIdList.toString())) {
            finish();
        } else {
            showBackDialog();
        }
    }

    /**
     * 清空购物车商品列表
     */
    private void clearShopList() {
        startMyDialog(false);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .clearMerchantShopList(new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        stopMyDialog();
                        HttpResultBean resultBean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, resultBean)) {
                            if (CommonUtils.serivceReturnCode(mContext, resultBean.code, resultBean.msg)) {
                                switch (resultBean.code) {
                                    case 1:
                                        MerchantShopActivity.this.finish();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.merchant_system_busy);
                    }
                });
    }

    @Override
    public void refreshCount() {
        showShopList();
        initInfo();
    }

    @Override
    public void deleteGoods() {
        loadData();
    }
}
