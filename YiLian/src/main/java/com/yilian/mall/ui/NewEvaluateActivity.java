package com.yilian.mall.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.widgets.GoodView.GoodView;


/**
 * @author Ray_L_Pain
 *         新版评价界面
 */
public class NewEvaluateActivity extends BaseActivity {

    private ImageView iv;
    private TextView tvName;
    private TextView tvZan;
    private TextView tvTime;
    private TextView tvDesc;


    private int zanNum = 0;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_evaluate);
        initView();
    }

    private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvZan = (TextView) findViewById(R.id.tv_zan);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDesc = (TextView) findViewById(R.id.tv_desc);

        GoodView goodView = new GoodView(this);

        Drawable drawableOff = mContext.getResources().getDrawable(R.mipmap.ic_shops_praise_off);
        Drawable drawableOn = mContext.getResources().getDrawable(R.mipmap.ic_shops_praise_on);
        drawableOff.setBounds(0, 0, drawableOff.getMinimumWidth(), drawableOff.getMinimumHeight());
        drawableOn.setBounds(0, 0, drawableOn.getMinimumWidth(), drawableOn.getMinimumHeight());

        tvZan.setText(String.valueOf(zanNum));
        tvZan.setCompoundDrawables(drawableOff, null, null, null);

        tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    zanNum ++;
                    goodView.setText("+1");
                    goodView.setTextColor(getResources().getColor(R.color.color_orange));
                    goodView.setDistance(3);
                    goodView.show(v);

                    tvZan.setText(String.valueOf(zanNum));
                    tvZan.setCompoundDrawables(drawableOn, null, null, null);
                    flag = false;
                } else {
                    zanNum --;
                    tvZan.setText(String.valueOf(zanNum));
                    tvZan.setCompoundDrawables(drawableOff, null, null, null);
                    flag = true;
                }
            }
        });
    }
}
