package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.ScreenUtils;

/**
 * 奖励说明页
 *
 * @author Ray_L_Pain
 * @date 2017/11/23 0023
 */

public class RedExplainDialog extends BaseActivity {
    @ViewInject(R.id.iv_img)
    ImageView ivImg;
    @ViewInject(R.id.tv)
    TextView tv;
    @ViewInject(R.id.iv_close)
    ImageView ivClose;

    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red_explain);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        screenWidth = ScreenUtils.getScreenWidth(mContext);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivImg.getLayoutParams();
        params.width = (int) (screenWidth * 0.75);
        params.height = (int) ((264 * 0.1) * params.width / (836 * 0.1));

        tv.setText("       每日凌晨5点之前，平台根据消费指数向奖券持有会员自动发放不定额奖励。会" +
                "员当日首次登录APP奖励自动弹出，未能及时拆取的奖励可通过首页“奖励”进入详情页拆取。" +
                "\n" + "        15个自然日内未拆的奖励将失效，针对失效奖励需使用等额奖券激活方可再次拆取。\n\n");

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
