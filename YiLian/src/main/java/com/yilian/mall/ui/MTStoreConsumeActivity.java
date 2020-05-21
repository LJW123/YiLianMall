package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.LebiPayResult;
import com.yilian.mall.http.PaymentNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mylibrary.Constants;

/**
 * 店内消费  来到该页面之前需要做登录判断，只有登录状态才能跳转过来
 */
public class MTStoreConsumeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private EditText etPayCount;
    private TextView tvAwardCount;
    private Button btnConfirmPay;
    private LinearLayout activityMtconsume;
    private String merchantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtconsume);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        etPayCount = (EditText) findViewById(R.id.te_pay_count);//消费金额
        tvAwardCount = (TextView) findViewById(R.id.tv_award_count);//赠送抵扣券数量
        btnConfirmPay = (Button) findViewById(R.id.btn_confirm_pay);//支付按钮
        activityMtconsume = (LinearLayout) findViewById(R.id.activity_mtconsume);


        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivLeft1.setOnClickListener(this);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.online_pay));
        ivRight2.setVisibility(View.GONE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.award_quan_explain));
        tvRight.setTextColor(getResources().getColor(R.color.color_red));
        tvRight.setOnClickListener(this);
        btnConfirmPay.setOnClickListener(this);
    }

    private void initData() {
        merchantId = getIntent().getStringExtra("merchantId");//商家ID

    }

    private void initListener() {
        etPayCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                ........................................................................
                //修正输入金额的格式
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etPayCount.setText(s);
                        etPayCount.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etPayCount.setText(s);
                    etPayCount.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etPayCount.setText(s.subSequence(0, 1));
                        etPayCount.setSelection(1);
                        return;
                    }
                }
//              ........................................................................

                if (!TextUtils.isEmpty(s.toString().trim())) {//输入内容后按钮可以点击
                    btnConfirmPay.setBackgroundResource(R.drawable.selector_btn_jp_all_screen);
                    btnConfirmPay.setTextColor(getResources().getColor(R.color.white));
                    btnConfirmPay.setClickable(true);
                } else {//如果没有输入内容，则按钮不可点击
                    btnConfirmPay.setBackgroundResource(R.drawable.shape_bg_btn_unclick);
                    btnConfirmPay.setClickable(false);
                }
                tvAwardCount.setText(s.toString().trim()+getResources().getString(R.string.dikouquan));//赠送抵扣券与消费金额1:1；
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private float userMoney;//用户奖励
    private float inputMoney;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_pay://确认支付
                RequestOftenKey.getUserInfor(mContext, sp);//刷新用户信息
                inputMoney = NumberFormat.convertToFloat(etPayCount.getText().toString().trim(), 0);//用户输入金额
                if (inputMoney < 0 || inputMoney == 0) {
                    showToast("请输入正确的消费金额");
                    return;
                }
                userMoney = NumberFormat.convertToFloat(MoneyUtil.getLeXiangBi(NumberFormat.convertToFloat(sp.getString("lebi", "0"), 0)), 0);//用户的奖励（已做除以100处理）
                if (inputMoney <= userMoney) {//用户奖励足够支付，则直接支付
                    pay();
                } else {//用户奖励不足，需要充值，跳转到收银台
                    Intent intent = new Intent(this, CashDeskActivity.class);
                    intent.putExtra("order_total_lebi",String.valueOf((int)(inputMoney*100)));
                    intent.putExtra("order_total_coupon","0");
                    intent.putExtra("payType","4");
                    intent.putExtra("type","MTStoreConsume");
                    intent.putExtra("merId",merchantId);
                    intent.putExtra("employee","0");
                    startActivity(intent);
                }
                break;
            case R.id.tv_right://送券说明
                Intent intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("url", Constants.HowToGetCoupon);
                startActivity(intent);
                break;
            case R.id.iv_left1:
                finish();
                break;
        }
    }

    private PayDialog paydialog;

    /**
     * 支付
     */
    private void pay() {
        paydialog = new PayDialog(mContext);
        paydialog.show();
    }

    public class PayDialog extends Dialog {
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        private Context context;
        private PaymentNetRequest paymentNetRequest;

        public PayDialog(Context context) {
            super(context, R.style.GiftDialog);
            this.context = context;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);

            initView();
            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {

                }

                @Override
                public void onMaxLength(String psw) {
//                   支付 TODO
                    payForStore(psw);
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
                    context.startActivity(new Intent(context, InitialPayActivity.class));
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


        private void payForStore(String pwd) {
            //支付密码
            final String password = CommonUtils.getMD5Str(pwd).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(context));
            if (paymentNetRequest == null) {
                paymentNetRequest = new PaymentNetRequest(context);
            }
            //套餐店内消费时，抵扣券金额和员工ID都为0,消费金额以分为单位传递给服务器
            paymentNetRequest.lebiPayMerchant(merchantId, String.valueOf(inputMoney * 100), "0", "0", password, LebiPayResult.class, new RequestCallBack<LebiPayResult>() {
                @Override
                public void onStart() {
                    super.onStart();
                    startMyDialog();
                }

                @Override
                public void onSuccess(ResponseInfo<LebiPayResult> responseInfo) {
                    LebiPayResult result = responseInfo.result;
                    switch (result.code) {
                        case 1:
//支付成功跳转店内消费支付成功页面
                            Intent intent = new Intent(MTStoreConsumeActivity.this, MTPayForStoreResult.class);
                            intent.putExtra("orderPrice", inputMoney);
                            intent.putExtra("order_id", result.dealId);
                            startActivity(intent);
                            finish();
                            //软键盘消失
                            dismissJP();
                            dismissInputMethod();
                            paydialog.dismiss();
                            break;
                        case -5:
                            pwdView.clearPassword();
                            showToast("密码错误，请重新输入");
                            break;
                        case -26:
                            showToast("二维码错误");
                            break;
                        case -4:
                            showToast(R.string.login_failure);
                            //软键盘消失
                            dismissJP();

                            paydialog.dismiss();
                            startActivity(new Intent(MTStoreConsumeActivity.this, LeFenPhoneLoginActivity.class));
                            break;
                        case -13:
                            showToast("奖励不足");
                            paydialog.dismiss();
                            break;
                        case -14:
                            showToast("订单失效");
                            paydialog.dismiss();
                            break;
                        default:
                            showToast("店内消费错误码:" + result.code);
                            break;


                    }
                    stopMyDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                }
            });
        }

        //软键盘消失
        public void dismissJP() {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }
}
