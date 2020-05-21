package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.service.UpdateService;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;


/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/20 0020
 * 版本更新的dialog
 */

public class VersionUpdateDialog extends BaseActivity {
    /**
     * 强制更新
     */
    public static final String FORCE_UPDATE ="1";
    public static final String UN_FORCE_UPDATE="0";
    @ViewInject(R.id.frame_layout)
    FrameLayout frameLayout;
    @ViewInject(R.id.tv_version_number)
    TextView tvVersionNumber;
    @ViewInject(R.id.tv_version_message)
    TextView tvVersionMessage;
    @ViewInject(R.id.tv_version_up)
    TextView tvVersionUp;
    @ViewInject(R.id.iv_version_close)
    ImageView ivVersionClose;

    /**
     * type 0 普通更新 1 强制更新
     */
    String version, message, type;
    private int screenW, width, height, margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_version_update);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        screenW = (ScreenUtils.getScreenWidth(mContext));
        if (screenW == 1080) {
            margin = 90;
        } else if (screenW == 720) {
            margin = 60;
        } else if (screenW == 1440) {
            margin = 120;
        } else {
            margin = 90;
        }
        width = screenW - margin * 2;
        height = (int) MyBigDecimal.div(MyBigDecimal.mul(width, 900), 896, 0);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

        Logger.i("version-" + width);
        Logger.i("version-" + height);


        version = getIntent().getStringExtra("version");
        message = getIntent().getStringExtra("message");
        type = getIntent().getStringExtra("type");
        Logger.i("ray-" + type);

        if (UN_FORCE_UPDATE.equals(type)) {
            ivVersionClose.setVisibility(View.VISIBLE);
        } else if (FORCE_UPDATE.equals(type)) {
            ivVersionClose.setVisibility(View.GONE);
        }

        tvVersionNumber.setText("版本号（" + version + "）");
        tvVersionMessage.setText(message);
        tvVersionMessage.setMovementMethod(ScrollingMovementMethod.getInstance());

        tvVersionUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateService.class);
                mContext.startService(intent);
                finish();
            }
        });
        ivVersionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (UN_FORCE_UPDATE.equals(type)) {
                finish();
            } else if (FORCE_UPDATE.equals(type)) {
                return false;
            }
        }
        return true;
    }
}
