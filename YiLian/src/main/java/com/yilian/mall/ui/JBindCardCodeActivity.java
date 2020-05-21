package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.BaseEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ray_L_Pain on 2017/7/12 0012.
 */

public class JBindCardCodeActivity extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView v3Back;
    @ViewInject(R.id.v3Title)
    TextView v3Title;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.tv_get_code)
    Button tv_get_code;
    @ViewInject(R.id.btn_next)
    Button btn_next;
    @ViewInject(R.id.et)
    EditText et;

    int getSmsCodeGap = Constants.GET_SMS_CODE_GAP;

    private String card_bank, card_holder, card_number, province_id, city_id, county_id, card_branch, phone;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (getSmsCodeGap > 0) {
                            getSmsCodeGap--;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_get_code.setText(getSmsCodeGap + "秒后重新发送");
                                }
                            });
                            Thread.sleep(1000);
                            handler.sendEmptyMessage(Constants.GET_SMS_CODE_COUNT_DOWN_TIME);
                        } else {
                            tv_get_code.setClickable(true);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_get_code.setText("重新发送");
                                    if (et.length() <= 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setMessage("短信验证码没有获取到?试试语音验证码吧");
                                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                getCode("1");
                                            }
                                        });
                                        builder.show();
                                    }
                                }
                            });
                            getSmsCodeGap = Constants.GET_SMS_CODE_GAP;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card_code);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        card_bank = getIntent().getStringExtra("card_bank");
        card_holder = getIntent().getStringExtra("card_holder");
        card_number = getIntent().getStringExtra("card_number");
        province_id = getIntent().getStringExtra("province_id");
        city_id = getIntent().getStringExtra("city_id");
        county_id = getIntent().getStringExtra("county_id");
        card_branch = getIntent().getStringExtra("card_branch");

        v3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        v3Title.setText("验证手机号");
        phone = PreferenceUtils.readStrConfig("phone", mContext, "");
        tv_phone.setText(phone);

        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(null);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    private void getCode(String voice) {
        startMyDialog();
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext).getSmsCode(phone, "2", "", voice, "6", new Callback<BaseEntity>() {
                @Override
                public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                        stopMyDialog();
                        switch (response.body().code) {
                            case 1:
                                tv_get_code.setClickable(false);
                                handler.sendEmptyMessage(Constants.GET_SMS_CODE_COUNT_DOWN_TIME);
                                break;
                            default:
                                showToast(response.body().msg);
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseEntity> call, Throwable t) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void next() {
        String phone = tv_phone.getText().toString().trim();
        String code = et.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            showToast(R.string.please_complete_info_first);
            return;
        }

        startMyDialog();
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext).bindCard(card_bank, card_holder, card_number, card_branch, province_id, city_id, "", phone, code, new Callback<BaseEntity>() {
                @Override
                public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                        stopMyDialog();
                        switch (response.body().code) {
                            case 1:
                                startActivity(new Intent(mContext, JBindCardSuccessActivity.class));
                                break;
                            default:
                                showToast(response.body().msg);
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseEntity> call, Throwable t) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
