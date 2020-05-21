package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.PreferenceUtils;

/**
 * Created by Ray_L_Pain on 2018/2/12 0012.
 */

public class TakeLedouSuccessActivity extends BaseActivity {

    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;

    @ViewInject(R.id.tv_ledou)
    TextView tvLedou;

    String ledouAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_ledou_success);
        ViewUtils.inject(this);

        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);

        ledouAmount = getIntent().getStringExtra("amount");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvTitle.setText("提取成功");

        tvRight.setText("完成");
        tvRight.setTextColor(mContext.getResources().getColor(R.color.color_red));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getInstance().killActivity(TakeLedouActivity.class);
                finish();
            }
        });

        tvLedou.setText(MoneyUtil.getLeXiangBiNoZero(ledouAmount) + Constants.APP_PLATFORM_DONATE_NAME);
    }
}
