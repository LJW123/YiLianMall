package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.OfflineTransferCardInfoDatailEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.utils.MoneyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.mall.R.id.tv_time_end;

/**
 * Created by Ray_L_Pain on 2017/9/5 0005.
 * 线下转账详情
 */

public class OfflineTransferRecordDetailActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;
    @ViewInject(R.id.tv_refresh)
    TextView tvRefresh;
    @ViewInject(R.id.sv)
    ScrollView sv;
    @ViewInject(R.id.layout_net_error)
    LinearLayout layoutError;

    @ViewInject(R.id.tv_phone)
    TextView tvPhone;
    @ViewInject(R.id.tv_cash)
    TextView tvCash;
    @ViewInject(R.id.tv_state)
    TextView tvState;
    @ViewInject(R.id.layout_reason)
    LinearLayout layoutReason;
    @ViewInject(R.id.tv_reason)
    TextView tvReason;
    @ViewInject(R.id.tv_time_start)
    TextView tvTimeStart;
    @ViewInject(tv_time_end)
    TextView tvTimeEnd;
    @ViewInject(R.id.tv_remark)
    TextView tvRemark;
    @ViewInject(R.id.tv_bank)
    TextView tvBank;
    @ViewInject(R.id.et_card)
    EditText etCard;
    @ViewInject(R.id.tv_name)
    TextView tvName;
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.ll_dispose_progress)
    LinearLayout llDisposeProgress;

    String id;
    String[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_transfer_record_detail);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        id = getIntent().getStringExtra("id");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitle.setText("详情");

        tvRight.setVisibility(View.GONE);

        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(0, img);
            }
        });
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    private void initData() {
        getData();
    }

    private void getData() {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .cardInfo2("2", id, new Callback<OfflineTransferCardInfoDatailEntity>() {
                    @Override
                    public void onResponse(Call<OfflineTransferCardInfoDatailEntity> call, Response<OfflineTransferCardInfoDatailEntity> response) {
                        stopMyDialog();
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                sv.setVisibility(View.VISIBLE);
                                layoutError.setVisibility(View.GONE);
                                OfflineTransferCardInfoDatailEntity.DataBean infoBody = response.body().data;
                                tvPhone.setText("充值账号：" + infoBody.phone);
                                tvCash.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(infoBody.amount)));
                                switch (infoBody.transStatus) {
                                    case "0":
                                        tvState.setText("待处理");
                                        tvState.setTextColor(mContext.getResources().getColor(R.color.color_orange));
                                        layoutReason.setVisibility(View.GONE);
                                        llDisposeProgress.setVisibility(View.GONE);
                                        break;
                                    case "1":
                                        tvState.setText("已充值");
                                        tvState.setTextColor(mContext.getResources().getColor(R.color.color_green));
                                        layoutReason.setVisibility(View.GONE);
                                        llDisposeProgress.setVisibility(View.VISIBLE);
                                        break;
                                    case "2":
                                        tvState.setText("已驳回");
                                        tvState.setTextColor(mContext.getResources().getColor(R.color.color_red));
                                        layoutReason.setVisibility(View.VISIBLE);
                                        tvReason.setText(infoBody.reason);
                                        llDisposeProgress.setVisibility(View.VISIBLE);
                                        break;
                                }
                                tvTimeStart.setText(DateUtils.timeStampToStr(Long.parseLong(infoBody.transTime)));
                                tvTimeEnd.setText(DateUtils.timeStampToStr(Long.parseLong(infoBody.subTime)));
                                tvRemark.setText(infoBody.remark);
                                tvBank.setText(infoBody.transBank);
                                bankCardNumAddSpace(etCard);
                                etCard.setText(infoBody.transCard);
                                tvName.setText(infoBody.transName);
                                GlideUtil.showImageWithSuffix(mContext, infoBody.transVouchar, iv);
                                img = new String[]{infoBody.transVouchar};
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OfflineTransferCardInfoDatailEntity> call, Throwable t) {
                        stopMyDialog();
                        sv.setVisibility(View.GONE);
                        layoutError.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void bankCardNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0; //记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }
}
