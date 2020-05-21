package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.CommitEmailEntity;
import rxfamily.entity.MyEmailEntity;


/**
 * 我的邮箱
 */
public class MyEmailActivity extends BaseActivity implements View.OnClickListener {

    private EditText my_email_box;
    private EditText my_email_identifying_code;
    private TextView my_email_send_out;
    private TextView my_email_submit_tv;
    //时间倒计时
    private int second = 600;
    private String email, checkCode;
    CreditPresenter presenter;
    private TextView myEmailSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_email);
        initView();
        initListener();
        initData();
        StatusBarUtil.setColor(this, Color.WHITE);


    }

    @Override
    protected void initView() {
        my_email_box = findViewById(R.id.my_email_box);
        my_email_identifying_code = findViewById(R.id.my_email_identifying_code);
        my_email_send_out = findViewById(R.id.my_email_send_out);
        my_email_submit_tv = findViewById(R.id.my_email_submit_tv);
        myEmailSubscribe = (TextView) findViewById(R.id.my_email_subscribe);
    }

    @Override
    protected void initListener() {
        my_email_send_out.setOnClickListener(this);
        my_email_submit_tv.setOnClickListener(this);
        my_email_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                Logger.i("BOX—onTextChanged:text-" + text.toString() + ",start:" + start + ",count:" + count);
                if (count > 0) {
                    my_email_send_out.setBackgroundResource(R.color.price);
                    my_email_send_out.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        my_email_identifying_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (count > 0 && email != null) {
                    my_email_submit_tv.setBackgroundResource(R.color.price);
                    my_email_submit_tv.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    protected void initData() {
        //@net 初始化presenter，并绑定网络回调，
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                second -= 1;
                if (second == 0) {
                    removeMessages(1);
                    my_email_send_out.setEnabled(true);
                    my_email_send_out.setText("获取验证码");
                    second = 600;
                } else {
//                    ViewGroup.LayoutParams layoutParams = my_email_send_out.getLayoutParams();
//                    layoutParams.width = 300;
//                    my_email_send_out.setLayoutParams(layoutParams);
                    my_email_send_out.setText("10分钟后失效");
                    my_email_send_out.setBackgroundResource(R.color.bg_gray);
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };

    /**
     * 取验证码
     */
    private void GetTheVerifyingCode() {
        myEmailSubscribe.setText("请查看您的邮箱6位数验证码");
        email = my_email_box.getText().toString();
        boolean email1 = isEmail(email);
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "邮箱不能为空!", Toast.LENGTH_SHORT).show();
        } else if (email1 == false) {
            ToastUtil.showMessage(this, "邮箱格式不正确，请重新输入！");
        } else {
            //传入email进行验证
            presenter.getEmailCode(email);
            if (second == 600) {
//                ViewGroup.LayoutParams layoutParams = my_email_send_out.getLayoutParams();
//                layoutParams.width = 300;
//                my_email_send_out.setLayoutParams(layoutParams);
                my_email_send_out.setText("10分钟后失效");
                my_email_send_out.setBackgroundResource(R.color.bg_gray);
                my_email_send_out.setEnabled(false);
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    }

    //判断email格式是否正确
    public boolean isEmail(String email) {

        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        Pattern p = Pattern.compile(str);

        Matcher m = p.matcher(email);

        return m.matches();
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");

    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(this, "邮箱提交失败！" + e.getMessage());
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        if (baseEntity instanceof CommitEmailEntity) {
            String date = ((CommitEmailEntity) baseEntity).getData().getCreate_time().toString();
            String email = ((CommitEmailEntity) baseEntity).getData().getEmail().toString();
            Logger.i("邮箱提交结果:" + date + "email:" + email);
            ToastUtil.showMessage(this, "邮箱提交成功！");
            finish();
            SkipUtils.toMyEmailSuccess(this, date, email);
        } else if (baseEntity instanceof MyEmailEntity) {
            String s = ((MyEmailEntity) baseEntity).toString();
            Logger.i("获取邮箱验证结果:" + s);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.my_email_send_out) {
            GetTheVerifyingCode();

        } else if (i == R.id.my_email_submit_tv) {
            email = my_email_box.getText().toString().trim();
            checkCode = my_email_identifying_code.getText().toString().trim();
            if (!TextUtils.isEmpty(checkCode) && !TextUtils.isEmpty(email)) {
                //提交保存Email信息
                presenter.addEmail(email, checkCode);
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "邮箱不能为空!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(checkCode)) {
                Toast.makeText(this, "校验码不能为空!", Toast.LENGTH_SHORT).show();
            }


        } else if (i == R.id.returnImg) {
            finish();
        }
    }
}
