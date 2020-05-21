package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Ray_L_Pain on 2017/1/6 0006.
 * 收货后退款界面
 */

public class MTRefundActivity2 extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.layout)
    LinearLayout layout;

    private String id,tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund2_mt);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        id = getIntent().getStringExtra("index_id");
        tel = getIntent().getStringExtra("shop_tel");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTRefundActivity2.this.finish();
            }
        });
        tvTitle.setText("申请退款");
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tel)) {
                    showToast(R.string.no_phone);
                } else {
                    showTelDialog();
                }
            }
        });

        showDialog("提示", "建议您优先联系商家协商处理，如因您自身原因导致的申请，商家和客服有权利拒绝。 ", null, 0, Gravity.CENTER, "知道了",
                null, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                break;
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                dialog.dismiss();
//                                break;
                        }
                    }
                }, mContext);
    }

    private void showTelDialog() {
        showDialog(null, tel, null, 0, Gravity.CENTER, "呼叫",
                "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + tel);
                                intent.setData(data);
                                startActivity(intent);
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                }, mContext);
    }
}
