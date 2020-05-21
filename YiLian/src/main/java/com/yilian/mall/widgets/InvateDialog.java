package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ui.InvatePrizeActivity;

/**
 * Created by Administrator on 2016/4/15.
 */
public class InvateDialog extends Dialog implements View.OnClickListener{

    private Button btnCancle;
    private Button btnSure;

    private TextView tvMessage;

    private Context context;
    private String message;

    public InvateDialog(Context context,String message) {
        super(context,R.style.ShareDialog);
        this.context=context;
        this.message=message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invate);

        initView();
    }

    private void initView() {
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnSure = (Button) findViewById(R.id.btn_sure);

        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        tvMessage= (TextView) findViewById(R.id.tv_message);
        tvMessage.setText(message);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                context.startActivity(new Intent(context, InvatePrizeActivity.class));
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
}
