package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.SureGiftPwdDialog;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.FontUtils;
import com.yilian.mylibrary.RefreshPersonCenter;
import com.yilian.networkingmodule.entity.MyBalanceBeanEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 奖券转赠页面
 *
 * @author ZYH
 * @date 2017/12/9 0009
 */

public class IntegralGiveAwayActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static final int INTERGRAL_GIVE_RESULT_CODE = 1;
    /**
     * （实际到账的长度
     */
    public static final int START = 5;
    private Button mButton;
    private ImageView mImageView;
    private TextView nickName, tvDonationType;
    private TextView code;
    private EditText etIntergral;
    private TextView v3Titile;
    private ImageView v3Back;
    private Double mPercent = 1d;
    private TextView tvIntegralCommand;
    private boolean canAway = false;
    private TextView tvAddMark;
    private boolean payPassword;
    private String factIntegralGived;
    private final int TO_INTEGRAL_SUCCESS_REQUEST_CODE = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.i("msg.what  " + msg.what + "  constants  " + Constants.EXECUTE_SUCCESS);
            RefreshPersonCenter.refresh(mContext);
            if (msg.what == Constants.EXECUTE_SUCCESS) {
                dismissInputMethod();
            }
            if (type == V3MemberGiftActivity.TYPE_INTEGRAL) {
                Intent intent = new Intent(mContext, IntegralSuccessActivity.class);
                intent.putExtra("integral", factIntegralGived);
                intent.putExtra("nick_name", nickName.getText().toString().trim());
                intent.putExtra("phone", handlerPhone(titlePhone));
                startActivityForResult(intent, TO_INTEGRAL_SUCCESS_REQUEST_CODE);
                setResult(INTERGRAL_GIVE_RESULT_CODE);
            } else {
                setResult(INTERGRAL_GIVE_RESULT_CODE);
                finish();
            }
        }
    };
    private String avilableIntegral;
    private String mIntegral;
    private String percent;
    private AlertDialog dialog;
    private EditText etInputMark;
    private TextView markContent;
    private TextView tvRight2;
    private String beUsedId;
    private String titileName;
    private String titlePhone;
    private DecimalFormat df;
    /**
     * 0 奖券转赠
     * 1 益豆转赠
     * 默认奖券转赠
     */
    private int type = V3MemberGiftActivity.TYPE_INTEGRAL;
    /**
     * 最低转赠额度
     */
    private String lowerDonation = "1000";
    private TextView tvNoticeCalculateStress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_give_away);
        initView();
        initListner();
    }


    @Override
    protected void onResume() {
        super.onResume();
        payPassword = sp.getBoolean("pay_password", false);
    }

    private String maxIntegral;
    private boolean isMaxIntegral = false;

    private void initListner() {
        tvRight2.setOnClickListener(this);
        tvAddMark.setOnClickListener(this);
        mButton.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        etIntergral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (type == V3MemberGiftActivity.TYPE_INTEGRAL) {
                    DecimalUtil.keep2Decimal(charSequence, etIntergral);
                } else {
                    //                2018-03-19 14:07:08优化为两位小数
                    DecimalUtil.keepDecimal(charSequence, etIntergral, 2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mIntegral = formatIntegrals(editable.toString().trim());
                if (isMaxIntegral) {
                    etIntergral.setSelection(etIntergral.getText().toString().trim().length());
                    isMaxIntegral = false;
                }
                if (Double.parseDouble(maxIntegral) < Double.parseDouble(mIntegral)) {
                    mIntegral = maxIntegral;
                    isMaxIntegral = true;
                    etIntergral.setText(mIntegral);
                    return;
                }
                factIntegralGived = formatIntegrals(mIntegral, true);
                showDonationInfo();
            }
        });
    }

    /**
     * 展示转赠信息
     */
    private void showDonationInfo() {
        Double integral = Double.parseDouble(mIntegral);
        if (integral < setFactIntegral(lowerDonation)) {
            if (type == V3MemberGiftActivity.TYPE_INTEGRAL) {
                tvIntegralCommand.setText("(最低转赠" + lowerDonation + "奖券；手续费：" + percent + "%）");
            } else {
                tvIntegralCommand.setText("(最低转赠" + lowerDonation +Constants.APP_PLATFORM_DONATE_NAME+"；手续费：" + percent + "%）");
            }
            canAway = false;
            mButton.setBackgroundResource(R.drawable.bg_dim_red_coner_5);
            mButton.setClickable(false);
        } else {
            mButton.setBackgroundResource(R.drawable.bg_red_coner_5);
            mButton.setClickable(true);
            canAway = true;
            if (type == V3MemberGiftActivity.TYPE_INTEGRAL) {
                tvIntegralCommand.setText("(实际到账" + factIntegralGived + "奖券)");
            } else {
                String donationNotice="(实际到账" + factIntegralGived + Constants.APP_PLATFORM_DONATE_NAME+")";
                SpannableString spannableString=new SpannableString(donationNotice);
                ForegroundColorSpan colorSpan=new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.price));
                spannableString.setSpan(colorSpan, START,factIntegralGived.length()+5,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvIntegralCommand.setText(spannableString);
            }
        }
    }

    private String formatIntegrals(String integrals) {
        if (TextUtils.isEmpty(integrals) || integrals.equals(".")) {
            return "0";
        } else {
            if (integrals.contains(".")) {
                int index = integrals.indexOf(".");
                if (index == integrals.length() - 1) {
                    integrals = integrals.subSequence(0, index).toString();
                }
            }
            return integrals;
        }
    }


    private void initView() {
        tvNoticeCalculateStress = findViewById(R.id.tv_notice_calculate_stress);
        tvDonationType = findViewById(R.id.tv_fact_inrgtal);
        markContent = (TextView) findViewById(R.id.tv_mark_content);
        tvAddMark = (TextView) findViewById(R.id.tv_add_mark);
        tvIntegralCommand = (TextView) findViewById(R.id.tv_integral_command);
        v3Titile = (TextView) findViewById(R.id.v3Title);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        mButton = (Button) findViewById(R.id.btn_intergral_away);
        nickName = (TextView) findViewById(R.id.tv_nick_name);
        mImageView = (ImageView) findViewById(R.id.iv_photo);
        code = (TextView) findViewById(R.id.tv_code);
        etIntergral = (EditText) findViewById(R.id.et_integral);
        etIntergral.requestFocus();
        etIntergral.setTypeface(FontUtils.getFontTypeface(mContext, "fonts/DINMittelschrift.otf"));
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        tvRight2.setText("来往记录");
        tvRight2.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        tvRight2.setVisibility(View.VISIBLE);


        Intent intent = getIntent();
        type = intent.getIntExtra("type", V3MemberGiftActivity.TYPE_INTEGRAL);
        titileName = intent.getStringExtra("name");
        String photoUrl = intent.getStringExtra("photo");
        titlePhone = intent.getStringExtra("code");
        beUsedId = intent.getStringExtra("be_userid");
        DecimalFormat decimalFormat = new DecimalFormat("#0.######");
        if (type == V3MemberGiftActivity.TYPE_STRESS) {
            v3Titile.setText(Constants.APP_PLATFORM_DONATE_NAME+"转赠");
            tvDonationType.setText(Constants.APP_PLATFORM_DONATE_NAME+"转赠");
            MyBalanceBeanEntity entity = intent.getParcelableExtra("balance_bean");
            avilableIntegral = entity.bean;
            percent = entity.beanTransferChargeRate;//比率
            lowerDonation = entity.lowerBeanForTransfer;
            //去除小数点后面多余的0
            lowerDonation = decimalFormat.format(Double.parseDouble(lowerDonation) / 100);
            tvIntegralCommand.setText("(最低转赠" + lowerDonation +Constants.APP_PLATFORM_DONATE_NAME+ "；手续费：" + percent + "%）");

            maxIntegral = MoneyUtil.getLeXiangBiNoZero(avilableIntegral);
//                2018-03-19 14:07:08优化为两位小数
            DecimalFormat decimalFormat1 = new DecimalFormat("#0.00");
            maxIntegral = decimalFormat1.format(Double.parseDouble(maxIntegral));
            tvNoticeCalculateStress.setText(getString(R.string.hint_hashrate) + maxIntegral);
            tvNoticeCalculateStress.setVisibility(View.VISIBLE);

        } else {
            v3Titile.setText("奖券转赠");
            tvDonationType.setText("奖券转赠");
            avilableIntegral = intent.getStringExtra("avilableIntegral");
            percent = intent.getStringExtra("poundagePercent");//比率
            //去除小数点后面多余的0
            lowerDonation = decimalFormat.format(Double.parseDouble(lowerDonation) / 100);
            tvIntegralCommand.setText("(最低转赠" + lowerDonation + "奖券；手续费：" + percent + "%）");
            maxIntegral = MoneyUtil.getLeXiangBiNoZero(avilableIntegral);
        }
        com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, photoUrl, mImageView);
        if (!TextUtils.isEmpty(titileName)) {
            nickName.setText(titileName);
        }
        code.setText(titlePhone);
        mPercent = Double.parseDouble(percent);
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_intergral_away://转赠奖券
                if (canAway) {
                    String mark = markContent.getText().toString().trim();
                    if (TextUtils.isEmpty(mark)) {
                        showGiveDialog("");
                    } else {
                        showGiveDialog(mark);
                    }
                }
                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.tv_add_mark:
                showMarkDialog(markContent.getText().toString().trim());
                break;
            case R.id.tv_right2://跳到来往记录页面beUsedId
                if (TextUtils.isEmpty(beUsedId)) {
                    showToast(R.string.service_exception);
                    return;
                }
                Intent intent = new Intent(mContext, V3MoneyDealingsActivity.class);
                intent.putExtra("titlePhone", handlerPhone(titlePhone));
                intent.putExtra("titleName", titileName);
                intent.putExtra("type", 1);
                intent.putExtra("id", beUsedId);
                intent.putExtra("from", "integral_give_away");
                intent.putExtra("donation_list_type", type);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    /**
     * 计算实际转赠奖券
     *
     * @param integral
     * @return
     */

    private double setFactIntegral(String integral) {
        double factIntegral = Double.parseDouble(integral) * (1 - mPercent / 100);
        return factIntegral;

    }

    private String formatIntegrals(String integral, boolean isHander) {
        if (df == null) {
            if (type == V3MemberGiftActivity.TYPE_INTEGRAL) {
                df = new DecimalFormat("#0.00");
            } else {
//                2018-03-19 14:07:08优化为两位小数
                df = new DecimalFormat("#0.00");
            }
        }
        if (TextUtils.isEmpty(integral)) {
            return "0";
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
            Double handerIntegral;
            if (isHander) {
                handerIntegral = setFactIntegral(integral);
            } else {
                handerIntegral = Double.parseDouble(integral);
            }
            BigDecimal bd = new BigDecimal(String.valueOf(handerIntegral));
            if (type == V3MemberGiftActivity.TYPE_INTEGRAL) {
                integral = df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));
            } else {
                integral = df.format(bd.setScale(4, BigDecimal.ROUND_DOWN));
            }
            return integral;
        }
    }


    private void showGiveDialog(String mark) {
        //先判断当前是否有设置支付密码
        if (!payPassword) {
            new VersionDialog.Builder(mContext)
                    .setMessage("您还未设置支付密码，请设置支付密码后在支付！")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext, InitialPayActivity.class));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            if (type == V3MemberGiftActivity.TYPE_INTEGRAL) {
                SureGiftPwdDialog mDialog = new SureGiftPwdDialog(this, true, mark, "integral", mIntegral, "", code.getText().toString(), avilableIntegral, handler);
                mDialog.show();
            } else {
                SureGiftPwdDialog mDialog = new SureGiftPwdDialog(this, true, mark, "calculatedStress", mIntegral, "", code.getText().toString(), avilableIntegral, handler);
                mDialog.show();
            }
            showInputMethod();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IntegralSuccessActivity.INTERGRAL_SUCCESS_RESULT_CODE) {
            finish();
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

    private boolean isFirstShow;

    private void showMarkDialog(String mark) {
        if (null == dialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View dialogView = View.inflate(mContext, R.layout.pup_input_mark, null);
            etInputMark = (EditText) dialogView.findViewById(R.id.et_input_mark);
            etInputMark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (isFirstShow) {
                        Selection.setSelection(editable, editable.toString().length());
                        isFirstShow = false;
                    }

                }
            });
            TextView tvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
            TextView tvCancel = (TextView) dialogView.findViewById(R.id.tv_calcel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disDialog();
                    dismissInputMethod();
                }
            });
            tvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mark = etInputMark.getText().toString().trim();
                    markContent.setText(mark);
                    if (TextUtils.isEmpty(mark)) {
                        markContent.setVisibility(View.GONE);
                        tvAddMark.setText("添加备注");
                    } else {
                        markContent.setVisibility(View.VISIBLE);
                        tvAddMark.setText("修改");
                    }
                    disDialog();
                }
            });
            builder.setView(dialogView);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
        }
        isFirstShow = true;
        etInputMark.setText(mark);
        dialog.show();
        showInputMethod();
    }

    private void disDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        disDialog();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        dismissInputMethod();
        super.onPause();
    }

    private String handlerPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            StringBuffer buffer = new StringBuffer();
            char[] chars = phone.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i >= 3 && i <= 6) {
                    buffer.append("*");
                } else {
                    buffer.append(chars[i]);
                }
            }
            phone = buffer.toString().trim();
        }
        return phone;
    }
}
