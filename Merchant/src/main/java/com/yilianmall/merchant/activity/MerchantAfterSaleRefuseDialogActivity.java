package com.yilianmall.merchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.AfterSaleBtnClickResultEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilianmall.merchant.R.id.et_remark;

/**
 * 售后订单的拒绝弹框
 */
public class MerchantAfterSaleRefuseDialogActivity extends Activity implements View.OnClickListener {

    private EditText etRemark;
    private TextView tvCancelBtn;
    private TextView tvConfirmBtn;
    private Context mContext;
    private String checkId;
    private String serviceType;
    private boolean isSuccess = false;
    private int position;

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, mContext);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.merchant_activity_after_sale_refuse_dialog);
        mContext = this;
        checkId = getIntent().getStringExtra(Constants.CHECK_ID);
        serviceType = getIntent().getStringExtra(Constants.SERVICE_TYPE);
        position = getIntent().getIntExtra(Constants.LIST_POSITION,-1);
        initView();
    }

    private void initView() {
        etRemark = (EditText) findViewById(et_remark);
        tvCancelBtn = (TextView) findViewById(R.id.tv_cancel_btn);
        tvConfirmBtn = (TextView) findViewById(R.id.tv_confirm_btn);

        tvCancelBtn.setOnClickListener(this);
        tvConfirmBtn.setOnClickListener(this);
    }

    private void submit() {
        final String remark = etRemark.getText().toString().trim();
        if (TextUtils.isEmpty(remark)) {
            Toast.makeText(this, "描述一下拒绝原因吧~", Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .supplierServiceCheck(checkId, "2", remark, serviceType, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        Toast.makeText(mContext, response.body().msg, Toast.LENGTH_SHORT).show();
                                        isSuccess = true;
                                        break;
                                }
                            }
                        }
                        AfterSaleBtnClickResultEntity body = response.body();
                        Intent intent=new Intent();
                        intent.putExtra(Constants.LIST_POSITION,position);
                        intent.putExtra("body",body);
                        if (isSuccess) {
                            setResult(RESULT_OK,intent);
                        } else {
                            setResult(RESULT_CANCELED);
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        Toast.makeText(mContext, R.string.aliwx_net_null_setting, Toast.LENGTH_SHORT).show();
                        isSuccess = false;
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel_btn) {
            finish();
        } else if (i == R.id.tv_confirm_btn) {
            submit();
        }
    }


    @Override
    public void finish() {
        super.finish();
    }
}
