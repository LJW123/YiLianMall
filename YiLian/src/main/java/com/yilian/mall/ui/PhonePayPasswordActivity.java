package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.ui.mvp.view.CertificationResultImplViewActivity;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 设置支付密码|修改支付密码
 */
public class PhonePayPasswordActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.tv_explain)
    private TextView tvExplain;
    @ViewInject(R.id.gv)
    private GridPasswordView gv;

    @ViewInject(R.id.tv_hint)
    private TextView tvHint;

    String titleName;
    String oldPayPassword = "";
    String newPayPassword = "";
    String payPassword = "";

    private AlertDialog mResultDialog;

    private AccountNetRequest accountNetRequest;
    /**
     * 新增字段 用于判断是否是从实名认证跳转来的，如果是从实名认证跳转过来，那么密码设置成功后 跳转实名认证成功界面，否则弹出密码设置成功的弹窗
     */
    private boolean pageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_pay_password);
        ViewUtils.inject(this);
        titleName = this.getIntent().getStringExtra("titleName");
        pageType = getIntent().getBooleanExtra("pageType", false);
        if (!titleName.equals("手机支付密码")) {
            tvHint.setText("请输入旧支付密码");
        } else {
            tvExplain.setVisibility(View.VISIBLE);
        }
        tv_back.setText(titleName);

        accountNetRequest = new AccountNetRequest(mContext);
    }

    public void Next(View view) {

        if (titleName.equals("手机支付密码")) {
            if (gv.getPassWord().length() != 6) {
                showToast("请输入支付密码");
                return;
            }

            if (((Button) view).getText().toString().equals("下一步")) {
                if (gv.getPassWord().length() == 6) {
                    oldPayPassword = CommonUtils.getMD5Str(gv.getPassWord()) + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(PhonePayPasswordActivity.this));
                    ((Button) view).setText("完成");
                    gv.clearPassword();
                    tvExplain.setVisibility(View.GONE);
                    tvHint.setText("请再次输入的支付密码");
                } else {
                    showToast("请输入支付密码");
                }
            } else {
                if (gv.getPassWord().length() == 6) {
                    newPayPassword = CommonUtils.getMD5Str(gv.getPassWord()) + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(PhonePayPasswordActivity.this));
                    if (!newPayPassword.equals(oldPayPassword)) {
                        showToast(R.string.password_not_consistent);
                        return;
                    }

                    String newPwd = (CommonUtils.getMD5Str(gv.getPassWord()) + CommonUtils.getMD5Str(sp.getString("server_salt", "0"))).toLowerCase();
                    if (pageType) {
                        setPasswordAfterCeri(newPwd);
                    } else {
                        setPasswordPersonCenter(newPwd);
                    }
                } else {
                    showToast("请再次输入支付密码");
                }
            }

        } else {
            if (((Button) view).getText().equals("下一步")) {
                if (gv.getPassWord().length() == 6) {
                    oldPayPassword = CommonUtils.getMD5Str(gv.getPassWord()) +
                            CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(PhonePayPasswordActivity.this));
                    ((Button) view).setText("继续");
                    gv.clearPassword();
                    tvHint.setText("请输入新的支付密码");
                } else {
                    showToast("请输入旧的支付密码");
                }
            } else if (((Button) view).getText().equals("继续")) {
                if (gv.getPassWord().length() == 6) {
                    newPayPassword = CommonUtils.getMD5Str(gv.getPassWord()) +
                            CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(PhonePayPasswordActivity.this));
                    ((Button) view).setText("完成");
                    gv.clearPassword();
                    tvHint.setText("请再次输入新的支付密码");
                } else {
                    showToast("请输入支付密码");
                }
            } else if (((Button) view).getText().equals("完成")) {
                if (gv.getPassWord().length() == 6) {
                    payPassword = CommonUtils.getMD5Str(gv.getPassWord()) +
                            CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(PhonePayPasswordActivity.this));
                    if (payPassword.equals(newPayPassword)) {

                        amendPayPassword();
                    } else {

                        showToast("两次输入不一致");
                    }
                } else {
                    showToast("请输入支付密码");
                }
            }

        }

    }

    private void setPasswordAfterCeri(String password) {
        startMyDialog();

        RetrofitUtils3.getRetrofitService(mContext).setPasswordAfterCer("userAuth/reset_pay_password", password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        startActivity(new Intent(mContext, CertificationResultImplViewActivity.class));
                        finish();
                    }
                });

    }

    /**
     * 从个人中心设置支付密码接口
     *
     * @param newPwd
     */
    private void setPasswordPersonCenter(String newPwd) {
        startMyDialog();
        accountNetRequest.resetPayPwd(newPwd, BaseEntity.class, new RequestCallBack<BaseEntity>() {
            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        PreferenceUtils.writeBoolConfig(Constants.PAY_PASSWORD, true, mContext);

                        showDialog();

                        break;
                    default:
                        showToast(responseInfo.result.msg);
                        break;
                }
                stopMyDialog();
            }
        });
    }

    /**
     * 修改支付密码
     */
    void amendPayPassword() {
        startMyDialog();
        accountNetRequest.modifyPayPwd(oldPayPassword.toLowerCase(), newPayPassword.toLowerCase(), BaseEntity.class, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                int code = responseInfo.result.code;
                switch (code) {
                    case -5:
                        showToast("原支付密码错误,请重新输入");
                        finish();
                        return;
                }
                if (CommonUtils.NetworkRequestReturnCode(PhonePayPasswordActivity.this, code + "")) {
                    onBackPressed();
                    showToast("修改成功，请牢记！");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 支付密码设置成功的弹框
     */
    private void showDialog() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int scrWidth = dm.widthPixels;
        int scrHeight = dm.heightPixels;

        View view = getLayoutInflater().inflate(R.layout.dialog_hint, null);
        TextView msg = (TextView) view.findViewById(R.id.message);
        msg.setText(Html.fromHtml("<font color=\'#333333\'>恭喜您手机支付密码已经设置成功，将用于<br></font><font color=\'#e12323\'>在线支付</font>"));
        TextView tvColos = (TextView) view.findViewById(R.id.tv_close);
        if (mResultDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mResultDialog = builder.create();
            mResultDialog.setCanceledOnTouchOutside(false);
        }
        tvColos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mResultDialog.dismiss();
                finish();
            }
        });
        mResultDialog.show();
        Window window = mResultDialog.getWindow();
        window.setContentView(view);
        window.setLayout((int) (scrWidth * 0.85f), (int) (scrHeight * 0.5f));
    }


}
