package com.leshan.ylyj.view.activity.creditmodel;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;


/**
 * 确认并提交身份证信息
 */
public class ConfirmIdentityInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView informtion_edit_tv;

    private AlertDialog dialog;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_identity_information);
        InitializationView();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    private void InitializationView() {
        informtion_edit_tv = (TextView) findViewById(R.id.informtion_edit_tv);
        informtion_edit_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        informtion_edit_tv.getPaint().setAntiAlias(true);
        informtion_edit_tv.setOnClickListener(this);
    }

    /**
     * 编辑dialog
     */
    private void EditDialog() {
        dialog = new AlertDialog.Builder(this, R.style.transverse_bespread_dialog).create();
        window = dialog.getWindow();
        dialog.show();
        View view = getLayoutInflater().inflate(R.layout.dialog_check_correct_car_license, null, false);
        TextView check_car_edit = (TextView) view.findViewById(R.id.check_car_edit);
        TextView check_car_delete = (TextView) view.findViewById(R.id.check_car_delete);
        TextView check_car_cancel = (TextView) view.findViewById(R.id.check_car_cancel);
        check_car_edit.setOnClickListener(this);
        check_car_delete.setOnClickListener(this);
        check_car_cancel.setOnClickListener(this);
//        AddressSelector address_edit_as=view.findViewById(R.id.address_edit_as);
        window.setContentView(view);
        //这句设置我们dialog在底部
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = 1200;
        window.setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.informtion_edit_tv) {
            EditDialog();

        } else if (i == R.id.check_car_edit) {
            SkipUtils.toIdEdit(this);
            dialog.dismiss();

        } else if (i == R.id.check_car_delete) {
            dialog.dismiss();

        } else if (i == R.id.check_car_cancel) {
            dialog.dismiss();

        }
    }
}
