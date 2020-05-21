package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.DialogPopup8;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 编辑身份证信息
 */
public class IdEditActivity extends BaseActivity implements View.OnClickListener {

    private TextView id_edit_confirm_tv, take_effect_tv, lose_efficacy_tv, sex_tv, birth_tv;
    private DialogPopup8 dialogPopup8;
    private String[] strings1 = new String[]{"男", "女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_edit);
        initToolbar();
        setToolbarTitle("身份证");
        hasBack(true);
        initView();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        id_edit_confirm_tv = findViewById(R.id.id_edit_confirm_tv);
        take_effect_tv = findViewById(R.id.take_effect_tv);
        lose_efficacy_tv = findViewById(R.id.lose_efficacy_tv);
        sex_tv = findViewById(R.id.sex_tv);
        birth_tv = findViewById(R.id.birth_tv);
    }

    @Override
    protected void initListener() {
        id_edit_confirm_tv.setOnClickListener(this);
        take_effect_tv.setOnClickListener(this);
        lose_efficacy_tv.setOnClickListener(this);
        sex_tv.setOnClickListener(this);
        birth_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * 弹出dialogPopup8选择框
     *
     * @param
     * @param strings1
     */
    private void DialogPopupShow(String[] strings1) {
        dialogPopup8 = new DialogPopup8(this, strings1);
        dialogPopup8.showPopupWindow();
    }

    private void datePicker(final TextView textView) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.id_edit_confirm_tv) {
            SkipUtils.toExistingIDCard(this);

        } else if (i == R.id.take_effect_tv) {
            datePicker(take_effect_tv);

        } else if (i == R.id.lose_efficacy_tv) {
            datePicker(lose_efficacy_tv);

        } else if (i == R.id.sex_tv) {
            DialogPopupShow(strings1);

        } else if (i == R.id.birth_tv) {
            datePicker(birth_tv);

        } else if (i == R.id.record_sure_tv) {
            sex_tv.setText(dialogPopup8.picker.getContentByCurrValue());
            dialogPopup8.dismiss();

        } else if (i == R.id.record_cancel_tv) {
            dialogPopup8.dismiss();

        }
    }
}
