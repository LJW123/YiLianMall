package com.yilianmall.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.mylibrary.widget.PasswordFinished;
import com.yilian.mylibrary.widget.PayDialog;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.MerchantDiscountEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改让利折扣
 */
public class MerchantChangeDiscountActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, PasswordFinished {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private TextView tvMerchantName;
    private TextView tvCurrentDiscount;
    private SeekBar seekBar;
    private TextView tvMinDiscount;
    private TextView tvMaxDiscount;
    private TextView tvDiscountRecommend;
    private Button btnConfirm;
    private TextView tvDiscountNumber;
    private float minDiscount = 0;
    private float maxDiscount = 99;
    private float totalDiscount;
    private TextView tvDiscountResult;
    private PayDialog payDialog;
    private EditText etInputDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_change_discount);
        initView();
        initSeekBar();
        initData();
        initListener();
    }

    private void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMerchantDiscount();

    }

    private float oldDiscount = 0;//默认为0折，商家未设置折扣时，进入该页面，默认为0折
    private float newDiscount;

    private void getMerchantDiscount() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantDiscountInfo(PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext), new Callback<MerchantDiscountEntity>() {
                    @Override
                    public void onResponse(Call<MerchantDiscountEntity> call, Response<MerchantDiscountEntity> response) {
                        stopMyDialog();
                        MerchantDiscountEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        setData(body);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantDiscountEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    private void setData(MerchantDiscountEntity body) {
        MerchantDiscountEntity.DataBean data = body.data;
        if (!TextUtils.isEmpty(data.merchantName)) {
            tvMerchantName.setText(data.merchantName);
        } else {
            tvMerchantName.setText("未设置店铺名称");
        }
        if (data.merchantPercent < 10) {
            Spanned spanned = Html.fromHtml("当前折扣:<big>" + (float) data.merchantPercent + "</big>折");
            tvCurrentDiscount.setText(spanned);
            newDiscount = oldDiscount = data.merchantPercent;
            Logger.i("设置折扣：" + data.merchantPercent);
            int i = (int) (data.merchantPercent * 10);
            Logger.i("修改后的折扣:" + i);
            seekBar.setProgress(i);//折扣是1-10  progress进度是1-100 所以这里显示时应该扩大十倍
        } else {
            tvCurrentDiscount.setText("未设置折扣");
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void initView() {
        etInputDiscount = (EditText) findViewById(R.id.et_input_discount);
        tvDiscountResult = (TextView) findViewById(R.id.tv_discount_result);
        tvDiscountResult.setBackgroundResource(R.mipmap.merchant_bg_change_discount_text);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("修改让利折扣");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.merchant_icon_discount_record));
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        tvCurrentDiscount = (TextView) findViewById(R.id.tv_current_discount);
        tvDiscountNumber = new TextView(mContext);
        tvDiscountNumber.setBackgroundResource(R.mipmap.merchant_bg_change_discount_text);
        tvDiscountNumber.setTextColor(Color.BLACK);
        tvDiscountResult.layout(0, 50, 50, 50);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        tvMinDiscount = (TextView) findViewById(R.id.tv_min_discount);
        tvMaxDiscount = (TextView) findViewById(R.id.tv_max_discount);
        tvDiscountRecommend = (TextView) findViewById(R.id.tv_discount_recommend);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float v1 = maxDiscount + 1;//最大值+1  修正进度  （最大进度为100时不需要修正，如果是99，则需要修正，否则会四舍五入 出现折扣异常问题  比如5.3会变为5.4）
        float v = ((float) progress / v1) * 10;
        Logger.i("VVV:" + v);
        if (v == 10f) {//修正最大折扣
            Logger.i("V进来了");
            v = 9.9f;
        }
        String format = String.format("%.1f", v);
        tvDiscountResult.setText(format + "折");
        Logger.i("etInputDiscount是否有焦点：" + etInputDiscount.hasFocus());
        if (!etInputDiscount.hasFocus()) {

        }
        etInputDiscount.setText(format);
        etInputDiscount.setSelection(format.length());
        newDiscount = NumberFormat.convertToFloat(format, 100);//获取最新折扣
        btnConfirm.setText("确定修改折扣为" + format + "折");
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tvDiscountResult.measure(spec, spec);
        int quotaWidth = tvDiscountResult.getMeasuredWidth();

        int spec2 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tvDiscountResult.measure(spec2, spec2);
        int sbWidth = seekBar.getMeasuredWidth();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDiscountResult.getLayoutParams();
        int i = (int) (((double) progress / seekBar.getMax()) * sbWidth - quotaWidth / 3);
        if (i > 0) {
            params.leftMargin = i;//修正textView位置
        }
        tvDiscountResult.setLayoutParams(params);
    }

    private void initListener() {
        etInputDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = etInputDiscount.getText().toString().trim();
                DecimalUtil.keepDecimal(trim, etInputDiscount, 1);
                Float value = NumberFormat.convertToFloat(String.valueOf(trim), 0f);
                if (value > 10) {
                    etInputDiscount.setText("9.9");
                    etInputDiscount.setSelection(3);
                }
                if (value < 0) {
                    etInputDiscount.setText("0.0");
                    etInputDiscount.setSelection(3);
                }
                newDiscount = NumberFormat.convertToFloat(etInputDiscount.getText().toString().trim(), 0f);
                btnConfirm.setText("确定修改折扣为"+newDiscount+"折");
//                if (!trim.endsWith(".")) {
//                    seekBar.setProgress((int) (NumberFormat.convertToFloat(trim, 0f) * 10));//edittext与progressbar联动
//                } else {
//
//                }
            }
        });
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void initSeekBar() {
        String text = String.valueOf(minDiscount);
        tvMinDiscount.setText(text + "折");
        tvMaxDiscount.setText(String.valueOf(maxDiscount / 10) + "折");
        tvDiscountResult.setText(text + "折");
        etInputDiscount.setText(text);
        etInputDiscount.setSelection(text.length());
        Logger.i("tvDiscountNumber的位置：" + tvDiscountNumber.getTop() + "：" + tvDiscountNumber.getBottom() + ":" + tvDiscountNumber.getRight() + ":" + tvDiscountNumber.getLeft());
        totalDiscount = totalDiscount(minDiscount, maxDiscount);
        seekBar.setEnabled(true);
        seekBar.setMax((int) totalDiscount);
        seekBar.setProgress(0);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
        } else if (i == R.id.v3Shop) {
            startActivity(new Intent(this, MerchantDiscountChangeRecordActivity.class));
        } else if (i == R.id.btn_confirm) {
            submit();
        }
    }

    public void submit() {
        String trim = etInputDiscount.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            showToast("请输入折扣");
            return;
        }
        if (PreferenceUtils.readBoolConfig(Constants.PAY_PASSWORD, mContext, false)) {
            payDialog = new PayDialog(mContext, this);
            payDialog.show();
        } else {
            new VersionDialog.Builder(mContext).setMessage("您还未设置支付密码,请设置支付密码后再支付！")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转设置支付密码界面使用广播
                            JumpToOtherPageUtil.getInstance().jumpToInitialPayActivity(mContext);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }

    }

    private void setMerchantDiscount(String psw) {
//        psw = CommonUtils.getMD5Str(psw).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
        startMyDialog();
        final String merchantDiscount = String.valueOf(newDiscount);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .setMerchantDiscount(PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext), merchantDiscount, psw, new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        stopMyDialog();
                        payDialog.dismiss();
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        showToast("折扣修改成功");
                                        Intent data = new Intent();
                                        data.putExtra("merchantDiscount", merchantDiscount);
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, true, mContext);
                                        setResult(1, data);
                                        finish();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        stopMyDialog();
                        payDialog.dismiss();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 计算折扣区间
     *
     * @param minDiscount
     * @param maxDiscount
     * @return
     */
    private float totalDiscount(float minDiscount, float maxDiscount) {
        return maxDiscount - minDiscount;
    }

    @Override
    public void passwordFinished(String psw) {
        setMerchantDiscount(psw);
    }
}
