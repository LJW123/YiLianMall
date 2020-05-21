package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.CashURLEntity;
import com.yilian.mall.entity.TiXianEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

/**
 * 会员领奖励界面
 */

public class MembersCash extends BaseActivity {

    @ViewInject(R.id.tv_can_withdrawal)
    private TextView tvTiXian;
    @ViewInject(R.id.tv_daijinquan_count1)
    private TextView tvLeXiang;
    @ViewInject(R.id.tv_voucher_count1)
    private TextView tvLeFen;
    @ViewInject(R.id.tv_coupons_count1)
    private TextView tvButieCount;
    @ViewInject(R.id.et_withdrawal)
    private EditText editText;
    @ViewInject(R.id.tv_spend_daijinquan)
    private TextView tvSpendDJQ;
    @ViewInject(R.id.btn_tixian)
    private Button btnTiXian;


    private MyIncomeNetRequest request;
    //判断是否绑定微信
    private boolean flag;
    private String availableLebi;

    private Animation animBottom;
    private String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menbers_cash);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ViewUtils.inject(this);

        initData();
        EDText();
    }

    private void initData() {

        startMyDialog();
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }

        request.TiXianNet(new RequestCallBack<TiXianEntity>() {
            @Override
            public void onSuccess(ResponseInfo<TiXianEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        Logger.json(responseInfo.result.availableLebi);
                        availableLebi = responseInfo.result.availableLebi;
                        if (availableLebi.equals("0")|Float.parseFloat(availableLebi) / 100<100) {
                            btnTiXian.setText("奖励不足,暂不能领奖励");
                            btnTiXian.setClickable(false);
                        } else {
                            btnTiXian.setText("确认领奖励");
                            btnTiXian.setClickable(true);
                        }
                        tvTiXian.setText(MoneyUtil.set¥Money(String.format("%.2f", Float.parseFloat(availableLebi) / 100)));
                        tvButieCount.setText(String.format("%.2f", responseInfo.result.coupon / 100));
                        tvLeFen.setText(responseInfo.result.lefen);
                        tvLeXiang.setText(String.format("%.2f", responseInfo.result.lebi / 100));
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("网络连接失败");
            }
        });
    }


    public void sureCash(View view) {
        request.WeiXinBind(new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        CashDialog dialog = new CashDialog(mContext);
                        dialog.show();
                        break;
                    case -51:
                        //未绑定微信
                        flag = true;
                        sp.edit().putString(Constants.SPKEY_WX_LOGIN,"3").commit();//此处标记微信登录是用于绑定微信
                        startActivity(new Intent(MembersCash.this,BindWeiXinActivity.class));
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
    /**
     * 设置edittext
     */
    private void EDText() {
        //编辑框只能输入小数点后两位
        editText.clearFocus();
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (editText.getText() == null) {
                    tvSpendDJQ.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi("0.00")));
                } else {
//                    tvSpendDJQ.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(editText.getText().toString())));
//                    tvSpendDJQ.setText(MoneyUtil.set¥Money(editText.getText().toString()));//2016-12-28此处发现问题，参考iOS端显示，领奖励金额输入多少，此处就显示多少。
                    tvSpendDJQ.setText("¥" + editText.getText().toString());//2016-12-28此处发现问题，参考iOS端显示，领奖励金额输入多少，此处就显示多少。
                }

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    return;
                }
                if (!availableLebi.equals("0") && editText.getText().length() != 0) {
                    if (Double.parseDouble(availableLebi)/100 >= 100&&Double.parseDouble(editText.getText().toString())>=100) {
                        btnTiXian.setText("确认领奖励");
                        btnTiXian.setClickable(true);
                    } else {
                        //TODO 100.1会异常走这里
                        btnTiXian.setText("领奖励金额不能小于100");
                        btnTiXian.setClickable(false);
                    }
                } else {
                    btnTiXian.setText("可领奖励金额不足，暂不能领奖励");
                    btnTiXian.setClickable(false);
                }
            }
        });
    }


    public void onBack(View view) {
        finish();
    }

    /**
     * 分享
     *
     * @param view
     */
    public void info(View view) {

        String phone = sp.getString("phone", "");

        if (TextUtils.isEmpty(phone)) {
            showToast("数据异常，请重新登录后，再来分享！");
            return;
        }
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }
        request.ShareNet(new RequestCallBack<CashURLEntity>() {
            @Override
            public void onSuccess(ResponseInfo<CashURLEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        shareUrl = responseInfo.result.url;
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

        animBottom = AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom);
        UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
                new UmengGloble().getAllIconModels());
        dialog1.setItemLister(new UmengDialog.OnListItemClickListener() {

            @Override
            public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                String content = getResources().getString(R.string.appshare);
                new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, shareUrl).share();

            }
        });

        dialog1.show();
    }

    /**
     * 跳转领奖励页面
     */
    public void jumpToWarn(View view) {
        Intent intent = new Intent(MembersCash.this, WebViewActivity.class);
        intent.putExtra("title_name", "提醒注意事项");
        intent.putExtra("url", Ip.getHelpURL() + "agreementforios/draw_cash.html");
        startActivity(intent);

    }

    /**
     * 充值对话框
     */
    public class CashDialog extends Dialog implements View.OnClickListener {

        private GridPasswordView passwordView;
        private TextView tv_cash_amount;
        private Button btn_cancleCash, btn_sureCash;


        public CashDialog(Context context) {
            super(context, R.style.ShareDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_cash_password);

            initDialog();
        }

        private void initDialog() {
            tv_cash_amount = (TextView) findViewById(R.id.tv_cash_amount);
            tv_cash_amount.setText(editText.getText());

            btn_cancleCash = (Button) findViewById(R.id.btn_cancleCash);
            btn_sureCash = (Button) findViewById(R.id.btn_sureCash);

            btn_cancleCash.setOnClickListener(this);
            btn_sureCash.setOnClickListener(this);

            passwordView = (GridPasswordView) findViewById(R.id.pwd);

            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sureCash:
                    dismiss();
                    startMyDialog();
                    //领奖励类型 0公众号 1微信 2 支付宝
                    request.CashNet(editText.getText().toString(), "1", CommonUtils.getMD5Str(passwordView.getPassWord()) + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext)), new RequestCallBack<BaseEntity>() {
                        @Override
                        public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                            stopMyDialog();
                            switch (responseInfo.result.code) {
                                case 1:
                                    //领奖励成功
                                    finish();
                                    Intent intent = new Intent(MembersCash.this, CashApplySuccess.class);
                                    intent.putExtra("money", editText.getText().toString());
                                    startActivity(intent);
                                    break;
                                case -3:
                                    showToast("系统繁忙，请稍后再试");
                                    break;
                                case -5:
                                    showToast("支付密码错误，请重新输入");
                                    break;
                                case -52:
                                    showToast("领奖励超额");
                                    break;
                                case -53:
                                    showToast("领奖励金额小于100");
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });
                    break;
                case R.id.btn_cancleCash:
                    dismiss();
                    break;
            }
        }
    }
}
