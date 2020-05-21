package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.DialogChangeAdress;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.MyEmailEntity;
import rxfamily.entity.MyEmailInfoEntity;


/**
 * 邮箱提交成功
 */
public class MyEmailSuccessActivity extends BaseActivity implements View.OnClickListener {

    private TextView delete_tv, my_email, commit_time;
    CreditPresenter presenter;
    private String commitDate, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_email_success);
        initToolbar();
        setToolbarTitle("个人信息");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);
        commitDate = getIntent().getStringExtra("commitDate");
        email = getIntent().getStringExtra("email");
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        delete_tv = findViewById(R.id.delete_tv);
        my_email = findViewById(R.id.my_email);
        commit_time = findViewById(R.id.commit_time);
        if (commitDate != null && email != null) {
            //显示邮箱信息
            commit_time.setText("提交时间：" + commitDate);
            my_email.setText(email);
        }
    }

    @Override
    protected void initListener() {
        delete_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //@net 初始化presenter，并绑定网络回调，
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
        startMyDialog(false);
        presenter.queryEmailInfo();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.delete_tv) {
            DialogChangeAdress.TwoEditDialog(this);

        } else if (i == R.id.check_car_delete) {
            //删除邮箱

            presenter.deleteEmail();
        } else if (i == R.id.check_car_cancel) {
            DialogChangeAdress.dialog.dismiss();
        }
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("邮箱获取：" + e.getMessage());
        stopMyDialog();
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        if (baseEntity instanceof MyEmailEntity) {
            Logger.i("删除邮箱：" + ((MyEmailEntity) baseEntity).toString());
            ToastUtil.showMessage(mContext, "邮箱删除成功！");
            DialogChangeAdress.dialog.dismiss();
            finish();
            SkipUtils.toMyEmail(this);
        } else if (baseEntity instanceof MyEmailInfoEntity) {
            Logger.i("查询邮箱成功：" + ((MyEmailInfoEntity) baseEntity).toString());
            String create_time = ((MyEmailInfoEntity) baseEntity).getData().getCreate_time().toString();
            String email = ((MyEmailInfoEntity) baseEntity).getData().getEmail();
            Logger.i("查询邮箱：create_time-" + create_time + "email:" + email);
            //显示邮箱信息
            commit_time.setText("提交时间：" + create_time);
            my_email.setText(email);
        }

    }


}
