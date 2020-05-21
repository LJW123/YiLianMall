package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ActKnowMoreEntity;

import java.util.ArrayList;
import java.util.List;

public class ActRuleDialogActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private LinearLayout llContent;
    private ImageView ivClose;
    private ActKnowMoreEntity knowMoreData;
    private TextView tvTitle1;
    private TextView tvTitle2;
    private TextView tvContent1;
    private TextView tvContent2;
    private ArrayList<TextView> tvTitles;
    private ArrayList<TextView> tvContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_rule_dialog);
        initView();
        initData();
    }

    private void initData() {
        knowMoreData = (ActKnowMoreEntity) getIntent().getSerializableExtra("knowMoreData");
        Logger.i("knowMoreData:" + knowMoreData.toString());
        if (knowMoreData != null) {
            List<ActKnowMoreEntity.DataBean> data = knowMoreData.data;
            int size = data.size();
            if (data != null && size > 0) {
                for (int i = 0; i < size; i++) {
                    if (tvTitles.size() < i) {
                        return;
                    }
                    tvTitles.get(i).setText(data.get(i).title);
                    tvContents.get(i).setText(data.get(i).content);
                }
            }
        }
    }

    private void initView() {
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvTitle1 = (TextView) findViewById(R.id.tv_title1);
        tvContent1 = (TextView) findViewById(R.id.tv_content1);
        tvTitle2 = (TextView) findViewById(R.id.tv_title2);
        tvContent2 = (TextView) findViewById(R.id.tv_content2);
        tvTitles = new ArrayList<>();
        tvTitles.add(tvTitle1);
        tvTitles.add(tvTitle2);
        tvContents = new ArrayList<>();
        tvContents.add(tvContent1);
        tvContents.add(tvContent2);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            default:
                break;
        }
    }
}
