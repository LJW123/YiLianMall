package com.yilian.mall.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.UserRecordGatherEntity;
import com.yilian.mall.http.UserdataNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 领奖励申请成功界面
 * Created by Administrator on 2016/3/28.
 */
public class CashApplySuccess extends BaseActivity {

    @ViewInject(R.id.tv_remain_shopping_vouchers)
    private TextView tv_remain_shopping_vouchers;

    @ViewInject(R.id.tv_remain_vouchers)
    private TextView tv_remain_vouchers;

    @ViewInject(R.id.tv_apply_time)
    private TextView tv_apply_time;

    @ViewInject(R.id.tv_apply_money)
    private TextView tv_apply_money;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_apply_success);
        ViewUtils.inject(this);
        initView();

        refreshData();

    }

    private void refreshData() {
        startMyDialog();
        new UserdataNetRequest(mContext).getUserRecordGather(UserRecordGatherEntity.class, new RequestCallBack<UserRecordGatherEntity>() {

            @Override
            public void onSuccess(ResponseInfo<UserRecordGatherEntity> arg0) {

                UserRecordGatherEntity result = arg0.result;
                switch (result.code) {
                    case 1:
                        stopMyDialog();
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("phone", result.phone).commit();
                        edit.putString("name", result.name).commit();
                        edit.putString("integral", result.balance).commit();
                        edit.putString("lebi", result.lebi).commit();
                        edit.putString("coupon", result.coupon).commit();
                        edit.putString("photo", result.photo).commit();
                        edit.putString("availableMoney", result.availableLebi).commit();
                        edit.putString(Constants.JIFEN, result.integral).commit();
                        edit.putString(Constants.MERCHANT_LEV, result.lev).commit();
                        edit.putString(Constants.LEV_NAME, result.levName).commit();
                        edit.putString(Constants.YI_DOU_BAO, result.yiDouBao);
                        edit.putString(Constants.YI_DOU_BAO_URL, result.yiDouBaoUrl);
                        edit.putString(Constants.YI_DOU_JUMP, result.yiDouJump);
                        tv_remain_vouchers.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(result.lebi)));
                        tv_remain_shopping_vouchers.setText(result.balance+".00");
                        tv_apply_money.setText(getIntent().getStringExtra("money"));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(new Date(System.currentTimeMillis()));
                        tv_apply_time.setText(time);
                        break;

                   default:
                       stopMyDialog();
                        break;
                }


            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                stopMyDialog();
            }
        });

    }

    private void initView() {

    }

    //查看领奖励记录
    public void jumpToCashRecord(View view) {
        finish();
    }

    public void onBack(View view){
        finish();
    }
}
