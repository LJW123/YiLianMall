package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.RollOutEntity;
import com.yilian.mall.entity.RollOutSuccessEntity;
import com.yilian.mall.http.BankRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.GridPasswordView;

/**
 * 乐分宝奖励转出界面
 * Created by Administrator on 2016/6/27.
 */
public class BankRollOutActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    @ViewInject(R.id.et_lebi)
    private EditText etLebi;

    @ViewInject(R.id.tv_user_money)
    private TextView tv_user_money;

    @ViewInject(R.id.tv_account_time)
    private TextView tv_account_time;

    @ViewInject(R.id.tv_sure)
    private TextView tv_sure;

    private String availableLebi = "0";
    private BankRequest request;
    private SurePwdDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_roll_out);

        ViewUtils.inject(this);

        tvBack.setText("转出");
        EDLebi();
        init();

        tv_account_time.setText(Html.fromHtml("预计增值到账时间<font color=\"#247df7\">" + StringFormat.formatDateE(System.currentTimeMillis(), "MM月dd日(EEEE)") + "</font>"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {
        if (request == null) {
            request = new BankRequest(mContext);
        }
        startMyDialog();
        request.RollOutAviable(new RequestCallBack<RollOutEntity>() {
            @Override
            public void onSuccess(ResponseInfo<RollOutEntity> responseInfo) {
                Logger.i(responseInfo.result.avible_lebi.toString());
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        availableLebi = responseInfo.result.avible_lebi;
                        if (!TextUtils.isEmpty(availableLebi)) {
                            tv_user_money.setText(String.format("%.2f", NumberFormat.convertToDouble(availableLebi, 0d) / 100));
                            etLebi.setHint("本次最多可转出¥" + String.format("%.2f", NumberFormat.convertToDouble(availableLebi, 0d) / 100));
                        } else {
                            etLebi.setHint("本次最多可转出¥" + 0);
                            tv_user_money.setText("0.00");
                        }
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("请检查网络");
            }
        });
    }

    /**
     * 设置转赠金额edittext
     */
    private void EDLebi() {
        //编辑框只能输入小数点后两位
        etLebi.clearFocus();
        etLebi.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etLebi.setText(s);
                        etLebi.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etLebi.setText(s);
                    etLebi.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etLebi.setText(s.subSequence(0, 1));
                        etLebi.setSelection(1);
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
                if (!TextUtils.isEmpty(etLebi.getText().toString())) {
                    if (!availableLebi.equals("0")) {
//                        if (Float.parseFloat(etLebi.getText().toString()) > (Float.parseFloat(availableMoney) / 100)) {
//                            etLebi.setText(String.format("%.2f", Float.parseFloat(availableMoney) / 100));
//                        }
                        if (Float.parseFloat(etLebi.getText().toString()) > (NumberFormat.convertToFloat(availableLebi,0.0f) / 100)) {
                            etLebi.setText(String.format("%.2f", NumberFormat.convertToFloat(availableLebi,0.0f) / 100));
                        }

                        Editable text = etLebi.getText();
                        Spannable spanText = text;
                        Selection.setSelection(spanText, text.length());
                    }
                }
                if (TextUtils.isEmpty(etLebi.getText().toString()) || etLebi.getText().toString().equals("0") || etLebi.getText().toString().equals("0.") || etLebi.getText().toString().equals("0.0") || etLebi.getText().toString().equals("0.00")) {
                    tv_sure.setBackgroundResource(R.drawable.btn_login_not_phone_login);
                    tv_sure.setClickable(false);

                } else {
                    tv_sure.setBackgroundResource(R.drawable.bg_btn_style_blue);
                    tv_sure.setClickable(true);
                }

                if (availableLebi.equals("0")) {
                    tv_sure.setBackgroundResource(R.drawable.btn_login_not_phone_login);
                    tv_sure.setClickable(false);
                }
            }
        });
    }

    /**
     * 确认转出
     */
    public void sureOut(View view) {


        if (isLogin()) {
            dialog = new SurePwdDialog(mContext, etLebi.getText().toString());
            dialog.show();

        } else {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
        }
    }

    //    //返回键时对话框消失
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            dismissJP();
//            if (dialog!=null&&dialog.isShowing()){
//                dialog.dismiss();
//            }
//            showToast("返回");
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    //软键盘消失
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);


        }
    }

    public class SurePwdDialog extends Dialog {

        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        private String lebi;

        public SurePwdDialog(Context context, String lebi) {
            super(context, R.style.GiftDialog);
            this.lebi = Float.parseFloat(lebi) * 100 + "";
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);

            initView();
        }


        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);


            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {

                }

                @Override
                public void onMaxLength(String psw) {
                    sendRequest(pwdView.getPassWord());
                }
            });

            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, InitialPayActivity.class));
                }
            });


            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }


        private void sendRequest(String pwd) {
            //支付密码
            String password = CommonUtils.getMD5Str(pwd).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));

            if (request == null) {
                request = new BankRequest(mContext);
            }

            stopMyDialog();
            request.RollOutSucceccAviable(lebi, password, new RequestCallBack<RollOutSuccessEntity>() {
                @Override
                public void onSuccess(ResponseInfo<RollOutSuccessEntity> responseInfo) {
                    Logger.json(responseInfo.result.toString());
                    switch (responseInfo.result.code) {
                        case 1:
                            //软键盘消失
                            dismissJP();
                            dismiss();
                            stopMyDialog();
                            Intent intent = new Intent(mContext, BankRollOutSuccessActivity.class);
                            intent.putExtra("avible_lebi", responseInfo.result.avible_lebi);
                            intent.putExtra("lebi", responseInfo.result.lebi);
                            intent.putExtra("time", responseInfo.result.time);
                            startActivity(intent);
                            break;
                        case -3:
                            //软键盘消失
                            dismissJP();
                            dismiss();stopMyDialog();

                            Toast.makeText(mContext, "系统繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                            break;
                        case -4:
                            //软键盘消失
                            dismissJP();
                            dismiss();
                            stopMyDialog();
                            startActivity(new Intent(BankRollOutActivity.this, LeFenPhoneLoginActivity.class));
                            break;
                        case -5:
                            pwdView.clearPassword();
                            stopMyDialog();
                            Toast.makeText(mContext, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                            break;
                        case -13:
                            //软键盘消失
                            dismissJP();
                            dismiss();
                            stopMyDialog();
                            Toast.makeText(mContext, "奖励不足", Toast.LENGTH_SHORT).show();
                            break;
                        case -23:
                            //软键盘消失
                            dismissJP();
                            dismiss();
                            stopMyDialog();
                            startActivity(new Intent(BankRollOutActivity.this, LeFenPhoneLoginActivity.class));
                            break;
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //软键盘消失
                    dismissJP();
                    dismiss();
                    stopMyDialog();
                    Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


}
