package com.yilian.mall.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;

/**
 * @author
 *         打卡成功弹窗界面
 */
public class ActClockInSuccessDialogActivity extends BaseDialogActivity implements View.OnClickListener {

    private ImageView ivTitle;
    private TextView tvRemind;
    private Button btnClose1;
    private Button btnClose2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_clock_in_success_dialog);
        initView();
    }

    private void initView() {
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvRemind = (TextView) findViewById(R.id.tv_remind);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml("你的早起奖励金<br>将于今日<font color='#FE8019'>11点前</font>到账<font color='#FE8019'>奖励</font>", Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml("你的早起奖励金<br>将于今日<font color='#FE8019'>11点前</font>到账<font color='#FE8019'>奖励</font>");
        }
        tvRemind.setText(spanned);
        btnClose1 = (Button) findViewById(R.id.btn_close1);
        btnClose2 = (Button) findViewById(R.id.btn_close2);

        btnClose1.setOnClickListener(this);
        btnClose2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close1:
            case R.id.btn_close2:
                finish();
                break;
            default:
                break;
        }
    }


}
