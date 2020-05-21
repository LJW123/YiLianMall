package com.yilian.mylibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.R;
import com.yilian.mylibrary.RequestOftenKey;

/**
 * Created by  on 2017/6/16 0016.
 */

public class PayDialog extends Dialog {
    private PasswordFinished passwordFinished;
    private Context context;
    private ImageView imgDismiss;
    private GridPasswordView pwd;
    private TextView tvForgetPwd;

    public PayDialog(@NonNull Context context, PasswordFinished passwordFinished) {
        super(context, R.style.library_module_GiftDialog);
        if (!(context instanceof Activity)) {
            Toast.makeText(context, "非法activity", Toast.LENGTH_SHORT).show();
            return;
        }
        this.passwordFinished = passwordFinished;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_module_dialog_suregift_pwd);
        initView();
        //dialog弹出时弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initView() {
        imgDismiss = (ImageView) findViewById(R.id.img_dismiss);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        pwd = (GridPasswordView) findViewById(R.id.pwd);
        pwd.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged(String psw) {

            }

            @Override
            public void onMaxLength(String psw) {
//密码输入完毕后的操作
                psw = CommonUtils.getMD5Str(psw).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(context));
                passwordFinished.passwordFinished(psw);
            }
        });

        imgDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                点击忘记密码
                JumpToOtherPageUtil.getInstance().jumpToInitialPayActivity(context);
            }
        });


        Window dialogWindow = getWindow();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    /**
     * 清除密码
     */
    public void clearPassword() {
        if (pwd != null) {
            pwd.clearPassword();
        }

    }

}
