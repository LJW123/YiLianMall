package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yilian.mall.R;


/**
 * Created by Administrator on 2016/4/29.
 */
public class GiveUpPayDialog extends Dialog implements View.OnClickListener {


    private Button btnCancle;
    private Button btnSure;

    private Context context;


    public GiveUpPayDialog(Context context) {
        super(context,R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_give_up_pay);

        initView();
    }

    private void initView() {
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnSure = (Button) findViewById(R.id.btn_sure);

        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
}
