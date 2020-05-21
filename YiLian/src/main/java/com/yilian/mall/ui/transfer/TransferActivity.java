package com.yilian.mall.ui.transfer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.transfer.bean.TransferSuccessBean;
import com.yilian.mall.ui.transfer.inter.ITransfer;
import com.yilian.mall.ui.transfer.inter.ITransferCategory;
import com.yilian.mall.utils.JumpYlActivityUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.widget.PasswordFinished;
import com.yilian.mylibrary.widget.PayDialog;

import java.math.BigDecimal;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilian.mall.ui.V3MemberGiftActivity.TYPE_DAI_GOU_QUAN;

public class TransferActivity extends BaseAppCompatActivity implements View.OnClickListener, PasswordFinished {

    public static final String TAG = "TransferActivity";
    EditText etInputMark = null;
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private TextView tvNickName;
    private TextView tvDoneePhone;
    private TextView tvTransferRemindTitle;
    private TextView tvTransferRemindContent;
    private EditText etTransferAmount;
    private TextView tvMarkContent;
    private TextView tvAddMark;
    private Button btnTransfer;
    private ImageView ivPhoto;
    private ITransfer transfer;
    /**
     * 受赠人
     */
    private Donee donee;
    private ITransferCategory transferCategory;
    private AlertDialog remarkDialog;
    private PayDialog payDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        tvRight2.setText("来往记录");
        tvRight2.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        tvRight2.setVisibility(View.VISIBLE);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvNickName = (TextView) findViewById(R.id.tv_nick_name);
        tvDoneePhone = (TextView) findViewById(R.id.tv_donee_phone);
        tvTransferRemindTitle = (TextView) findViewById(R.id.tv_transfer_remind_title);
        tvTransferRemindContent = (TextView) findViewById(R.id.tv_transfer_remind_content);
        etTransferAmount = (EditText) findViewById(R.id.et_transfer_amount);
        tvMarkContent = (TextView) findViewById(R.id.tv_mark_content);
        tvAddMark = (TextView) findViewById(R.id.tv_add_mark);
        tvAddMark.setOnClickListener(this);
        btnTransfer = (Button) findViewById(R.id.btn_transfer);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
    }

    private void initData() {
        transfer = (ITransfer) getIntent().getSerializableExtra(TAG);

        donee = transfer.getDonee();
        com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, donee.photo, ivPhoto);
        tvNickName.setText(TextUtils.isEmpty(donee.nick) ? "暂无昵称" : donee.nick);
        tvDoneePhone.setText(donee.phone);

        transferCategory = transfer.getTransferCategory();
        v3Title.setText(transferCategory.getTitle());
        tvTransferRemindTitle.setText(transferCategory.getSubTitle());
        tvTransferRemindContent.setText(getActualAmount(new BigDecimal("0")));
        etTransferAmount.setHint("最低转赠" + transferCategory.getMinTransferAmount() + transferCategory.getCategoryName());


    }

    private void initListener() {
        etTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                DecimalUtil.keep2Decimal(charSequence, etTransferAmount);
                if (TextUtils.isEmpty(charSequence)) {
                    etTransferAmount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    etTransferAmount.setHint("最低转赠" + transferCategory.getMinTransferAmount() + transferCategory.getCategoryName());
                } else {
                    etTransferAmount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45);
                    String maxTransferAmount = transferCategory.getMaxTransferAmount();
                    if (new BigDecimal(charSequence.toString())
                            .compareTo(new BigDecimal(maxTransferAmount)) > 0) {
//                    输入的太多了
                        Logger.i("maxTransferAmount:" + maxTransferAmount);
                        etTransferAmount.setText(maxTransferAmount);
                        etTransferAmount.setSelection(etTransferAmount.length());
                    } else {
//                    足够
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputContent = etTransferAmount.getText().toString().trim();
                if (!TextUtils.isEmpty(inputContent)) {
                    BigDecimal rateMount =
                            new BigDecimal(inputContent).subtract(new BigDecimal(inputContent)
                                    .multiply(new BigDecimal(transferCategory.getRate())));
                    tvTransferRemindContent.setText(getActualAmount(rateMount));
                    if (new BigDecimal(inputContent).compareTo(new BigDecimal(transferCategory.getMinTransferAmount())) >= 0) {
//                    输入超过最低转赠
                        btnTransfer.setEnabled(true);
                        btnTransfer.setBackgroundResource(R.drawable.bg_red_coner_5);
                    } else {
//                    输入的太少 达不到最低转赠
                        btnTransfer.setEnabled(false);
                        btnTransfer.setBackgroundResource(R.drawable.bg_dim_red_coner_5);

                    }

                } else {
                    tvTransferRemindContent.setText(getActualAmount(new BigDecimal("0")));
                }

            }
        });
    }

    private Spanned getActualAmount(BigDecimal actualAmount) {
        return Html.fromHtml("实际到账<font color='#ff0000'>" + actualAmount.setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString() + "</font>" + transferCategory.getCategoryName());
    }

    @Override
    public void passwordFinished(String psw) {
        startMyDialog();
        String transferAmount = new BigDecimal(etTransferAmount.getText().toString().trim()).multiply(new BigDecimal("100")).toPlainString();
        Subscription subscription = transfer.transfer(mContext,
                transferAmount,
                psw, tvMarkContent.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TransferSuccessBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        dismissInputMethod();
                        dissmissPayDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                        dismissInputMethod();
                        dissmissPayDialog();
                    }

                    @Override
                    public void onNext(TransferSuccessBean transferSuccessBean) {
                        finish();
                        AppManager.getInstance().killActivity(TransferAccountActivity.class);
                        JumpYlActivityUtils.toTransferSuccessActivity(
                                mContext,
                                transfer.getDonee().nick + "(" + PhoneUtil.formatPhoneMiddle4Asterisk(transfer.getDonee().phone) + ")" + "已收到你的转赠",
                                MoneyUtil.getLeXiangBi(transferSuccessBean.successAmount),
                                transferCategory.getTitle());
                    }
                });
        addSubscription(subscription);
    }

    void dissmissPayDialog() {
        if (payDialog != null && payDialog.isShowing()) {
            payDialog.dismiss();
        }
    }

    private void showMarkDialog() {
        if (null == remarkDialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View dialogView = View.inflate(mContext, R.layout.pup_input_mark, null);
            etInputMark = (EditText) dialogView.findViewById(R.id.et_input_mark);
            Selection.setSelection(etInputMark.getText(), etInputMark.length());
            TextView tvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
            TextView tvCancel = (TextView) dialogView.findViewById(R.id.tv_calcel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissInputMethod();
                    disInputMarkDialog();
                }
            });
            tvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismissInputMethod();
                    disInputMarkDialog();
                    String mark = etInputMark.getText().toString().trim();
                    tvMarkContent.setText(mark);
                    if (TextUtils.isEmpty(mark)) {
                        tvAddMark.setText("添加备注");
                    } else {
                        tvAddMark.setText("修改");
                    }

                }
            });
            builder.setView(dialogView);
            remarkDialog = builder.create();
            remarkDialog.setCanceledOnTouchOutside(true);
        }
        remarkDialog.show();
        showInputMethod(etInputMark);
    }

    private void disInputMarkDialog() {
        if (remarkDialog != null && remarkDialog.isShowing()) {
            remarkDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:
                JumpYlActivityUtils.toV3MoneyDealingsActivity(mContext, transfer.getDonee().phone, transfer.getDonee().nick,
                        1, transfer.getDonee().userId, "integral_give_away", TYPE_DAI_GOU_QUAN);
                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_transfer:
                if (TextUtils.isEmpty(etTransferAmount.getText().toString().trim())) {
                    return;
                }
                if (payDialog == null) {
                    payDialog = new PayDialog(mContext, this);
                } else {
                    payDialog.clearPassword();
                }
                if (!payDialog.isShowing()) {
                    payDialog.show();
                }
                break;
            case R.id.tv_add_mark:
//                添加备注
                showMarkDialog();
                break;
            default:
                break;
        }
    }


}
