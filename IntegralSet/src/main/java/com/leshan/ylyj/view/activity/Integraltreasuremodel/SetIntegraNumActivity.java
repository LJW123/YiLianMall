package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.presenter.IntegralTreasurePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;


/**
 * 自动转入详情
 */
public class SetIntegraNumActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_right;//限额说明

    private IntegralTreasurePresenter mPresent;
    private TextView edit_tv;//编辑
    private TextView retain_integral_tv;//保留奖券

    private String jfbAutoTime = ""; //后台设置 集分宝自动转入时间
    private String jfbHoldIntegral;//自动转入 保留奖券数
    private String limitExplainShow;// 限额说明  0不展示 1展示
    private String limitExplainUrl;//   限额说明H5链接地址
    private String isTransferLimit;//  1不限制转入 2限制转入
    private String jfbTransferLimit;// 集分宝转入限额


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_integra_num);

        initToolbar();
        setToolbarTitle("自动转入");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {
        toolbar_right = findViewById(R.id.toolbar_right);
        toolbar_right.setVisibility(View.GONE);

        edit_tv = findViewById(R.id.edit_tv);
        retain_integral_tv = findViewById(R.id.retain_integral_tv);
    }

    @Override
    protected void initListener() {
        toolbar_right.setOnClickListener(this);
        edit_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        jfbHoldIntegral = getIntent().getExtras().getString("jfbHoldIntegral");
        jfbAutoTime = getIntent().getExtras().getString("jfbAutoTime");
        limitExplainShow = getIntent().getExtras().getString("limitExplainShow");
        limitExplainUrl = getIntent().getExtras().getString("limitExplainUrl");
        isTransferLimit = getIntent().getExtras().getString("isTransferLimit");
        jfbTransferLimit = getIntent().getExtras().getString("jfbTransferLimit");

        if (limitExplainShow.equals("1"))//限额说明  0不展示 1展示
            toolbar_right.setVisibility(View.VISIBLE);
        retain_integral_tv.setText("保留" + jfbHoldIntegral + "奖券");
        mPresent = new IntegralTreasurePresenter(mContext, this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.toolbar_right) {//限额说明
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, limitExplainUrl);
            startActivity(intent);
        } else if (i == R.id.edit_tv) {//编辑
            ActionSheetDialog();
        } else {
        }
    }

    private void ActionSheetDialog() {
        final String[] stringItems = {"修改金额", "关闭自动转入"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.isTitleShow(false)
                .itemTextColor(getResources().getColor(R.color.main_black_text))
                .cancelText(getResources().getColor(R.color.main_black_text))
                .layoutAnimation(null)
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    SkipUtils.toSetRetainedIntegra(mContext, 1, jfbAutoTime, limitExplainShow, limitExplainUrl, isTransferLimit, jfbTransferLimit);
                } else {
                    showSystemV7Dialog(null, "关闭自动转入后，新产生的奖券将不能自动转入集分宝！", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case DialogInterface.BUTTON_NEGATIVE://取消
                                    dialogInterface.dismiss();
                                    break;
                                case DialogInterface.BUTTON_POSITIVE://确认
                                    //type 类型 0开启自动转入 1关闭自动转入 2编辑自动转入奖券金额
                                    mPresent.setRetainedIntegra("1", "0");
                                    break;
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof rxfamily.entity.BaseEntity) {
            Logger.i("获取数据成功");
            finish();
        } else {
            Logger.i("获取数据失败");
        }
    }
}
