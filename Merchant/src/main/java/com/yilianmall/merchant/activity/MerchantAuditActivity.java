package com.yilianmall.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilianmall.merchant.R;

/**
 * 商家审核界面
 */
public class MerchantAuditActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private TextView tvAuditState;
    private TextView tvAuditContent;
    private TextView tvAuditHint;
    private TextView tvErrorCause;
    private TextView tvErrorPhone;
    private LinearLayout llAuditError;
    private Button btnSubmit;
    private TextView tvPhoneBottom;
    private ImageView ivIcon;
    private String merchantStatus;
    private String refuse;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_audit);
        merchantStatus = PreferenceUtils.readStrConfig(Constants.MER_STATUS, mContext);
        refuse = getIntent().getStringExtra("refuse");
        phone = "400-152-5159";
        initView();
        initData();
    }

    private void initData() {
        Spanned spanned = Html.fromHtml("若有疑问请致电 " + "<font color=\"#FE5062\">" + phone + "</font>");
        switch (merchantStatus) {
            // 0 没有缴年费未成为商家 1 已缴年费成为商家 未提交认证资料  2提交认证待审核 3提交认证审核通过 4提交认证审核拒绝
            case "2":
                v3Title.setText("认证资料审核中");
                ivIcon.setImageResource(R.mipmap.merchant_audit_ing);
                tvAuditState.setText("资料正在审核中");
                tvAuditContent.setText("请耐心等待平台的审核");
                tvAuditHint.setText("温馨提示: 平台审核周期预计需要1-5个工作日");
                llAuditError.setVisibility(View.GONE);
                tvPhoneBottom.setVisibility(View.VISIBLE);
                tvPhoneBottom.setText(spanned);
                break;
            case "3":
                v3Title.setText("商家认证");
                ivIcon.setImageResource(R.mipmap.merchant_audit_success);
                llAuditError.setVisibility(View.GONE);
                tvAuditState.setText("恭喜您的资料审核通过");
                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText("进入商家中心");
                break;
            case "4":
                v3Title.setText("商家认证");
                ivIcon.setImageResource(R.mipmap.merchant_audit_erroe);
                tvAuditState.setText("您提交的资料审核未通过");
                tvAuditContent.setText("请重新检查上传资料并重新上传!");
                llAuditError.setVisibility(View.VISIBLE);
                tvErrorCause.setText(refuse);
                tvErrorPhone.setText(spanned);
                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText("重新提交");
                break;
        }
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvAuditState = (TextView) findViewById(R.id.tv_audit_state);
        tvAuditContent = (TextView) findViewById(R.id.tv_audit_content);
        tvAuditHint = (TextView) findViewById(R.id.tv_audit_hint);
        tvErrorCause = (TextView) findViewById(R.id.tv_error_cause);
        tvErrorPhone = (TextView) findViewById(R.id.tv_error_phone);
        tvErrorPhone.setOnClickListener(this);
        llAuditError = (LinearLayout) findViewById(R.id.ll_audit_error);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        tvPhoneBottom = (TextView) findViewById(R.id.tv_phone_bottom);
        tvPhoneBottom.setOnClickListener(this);

        v3Back.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.btn_submit) {
            switch (btnSubmit.getText().toString().trim()) {
//                0普通会员,1VIP会员,2个体商家,3实体商家,4市服务中心,5省服务中心
                case "重新提交":
                    Intent intent = new Intent(mContext, MerchantCertificateActivity.class);
                    Logger.i("merStatus  "+com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MER_STATUS, mContext, "")+" levname  "+com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MERCHANT_LEV, mContext, ""));
                    intent.putExtra("merchantId", com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext, ""));
                    //  MER_STATUS  0 未成为商家（未缴纳年费） 1 已缴年费成为商家，但未提交资料  2已提交资料待审核 3提交资料审核通过 4审核拒绝
                    switch (com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MER_STATUS, mContext, "")) {
                        case "4"://成为商家未提交资料
                            String merchantLev = PreferenceUtils.readStrConfig(Constants.MERCHANT_LEV, mContext, "");
                            if (merchantLev.equals("2")) { //个体商家
                                intent.putExtra("merchantType", "0");
                            } else if (Integer.parseInt(merchantLev)>2) {
                                //实体商家或者服务中心
                                intent.putExtra("merchantType", "1");
                            }
                            break;
                    }
                    startActivity(intent);
                    break;
                case "进入商家中心":
                    intent = new Intent(mContext, MerchantCenterActivity.class);
                    startActivity(intent);
                    break;
            }
            finish();
        } else if (i == R.id.tv_error_phone || i == R.id.tv_phone_bottom) {
            showDialog(null, phone, null, 0, Gravity.CENTER, "拨打", "取消", true, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                            startActivity(intent);
                            break;
                    }
                }
            }, mContext);
        }
    }
}
