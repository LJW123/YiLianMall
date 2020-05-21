package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.MemberRelationSystemEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;

import static com.yilian.mall.R.id.et_referrer_phone;

public class SettingReferrerActivity extends BaseActivity implements View.OnClickListener {

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
    private EditText etReferrerPhone;
    private Button btnBindReferrer;
    private LinearLayout activitySettingReferrer;
    private AccountNetRequest accountNetRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_referrer);
        initView();
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
        etReferrerPhone = (EditText) findViewById(et_referrer_phone);
        btnBindReferrer = (Button) findViewById(R.id.btn_bind_referrer);
        activitySettingReferrer = (LinearLayout) findViewById(R.id.activity_setting_referrer);

        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("推荐人");
        tvTitle.setVisibility(View.VISIBLE);
        ivRight2.setVisibility(View.GONE);

        ivLeft1.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnBindReferrer.setOnClickListener(this);
    }

    private void initListener() {
        etReferrerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (accountNetRequest == null) {
                        accountNetRequest = new AccountNetRequest(mContext);
                    }
                    String referrerPhone = etReferrerPhone.getText().toString().trim();
                    accountNetRequest.getMemberRelationSystem(referrerPhone, new RequestCallBack<MemberRelationSystemEntity>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            startMyDialog();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<MemberRelationSystemEntity> responseInfo) {
                            MemberRelationSystemEntity result = responseInfo.result;
                            switch (result.code) {
                                case 1://查询推荐人信息成功，弹窗显示
//                                    获取推荐人信息
                                    String merchantName = result.merchantName;
                                    String filialeName = result.filialeName;
                                    String userName = result.userName;
                                    TextView textView = new TextView(mContext);
                                    textView.setText("您的推荐人");
                                    textView.setPadding(20, 20, 20, 20);
                                    textView.setGravity(Gravity.CENTER);
                                    LinearLayout layout = new LinearLayout(mContext);
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    layout.setGravity(Gravity.CENTER);
                                    TextView tv = new TextView(mContext);
                                    tv.setText((TextUtils.isEmpty(merchantName) ? TextUtils.isEmpty(filialeName) ? TextUtils.isEmpty(userName) ? "暂无昵称" : userName : userName : merchantName) );
                                    tv.setPadding(20, 20, 20, 20);
                                    TextView tv1 = new TextView(mContext);
                                    tv1.setText("(" + referrerPhone + ")" );
                                    tv1.setPadding(20, 0, 20, 20);
                                    LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layout.addView(tv, pm);
                                    layout.addView(tv1,pm);
                                    new AlertDialog.Builder(mContext)
                                            .setCustomTitle(textView)
                                            .setView(layout)
                                            .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create().show();
                                    break;
                                case -80:
                                    showToast("未注册用户不能绑定");
                                    break;
                                default:
                                    showToast("查询推荐人信息失败");
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.btn_bind_referrer:
                bindReferrerPhone();
                break;
        }
    }

    /**
     * 绑定推荐人手机号码
     */
    private void bindReferrerPhone() {
        // validate
        String referrerPhone = etReferrerPhone.getText().toString().trim();
        if (TextUtils.isEmpty(referrerPhone)) {
            Toast.makeText(this, "请输入推荐人手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CommonUtils.isPhoneNumer(referrerPhone)) {
            showToast("请输入正确的手机号");
            return;

        }
        // TODO validate success, do something
        if (accountNetRequest == null) {
            accountNetRequest = new AccountNetRequest(mContext);
        }
        accountNetRequest.bindReferrerPhone(referrerPhone, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                BaseEntity result = responseInfo.result;
                switch (result.code) {
                    case 1://绑定成功
                        showToast("绑定成功");
                        sp.edit().putString(Constants.REFERRER_PHONE,referrerPhone).commit();
                        startActivity(new Intent(SettingReferrerActivity.this, UserInfoActivity.class));
                        finish();
                        break;
                    case -79://已有会员关系，请勿重复操作
                        showToast("已有会员关系,请勿重复操作!");
                        break;
                    case -82:
                        showToast("推荐人不能是自己");
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });

    }
}
