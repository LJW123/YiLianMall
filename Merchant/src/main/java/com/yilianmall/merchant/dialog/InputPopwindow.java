package com.yilianmall.merchant.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yilianmall.merchant.R;

import java.math.BigDecimal;


/**
 * 提醒弹出
 * Created by Zg on 2018/6/05.
 */
public class InputPopwindow extends PopupWindow {
    /**
     * 数量位数限制
     */
    private static int SHIFT_ICON_DIGIT = 2;
    private static String POINT = ".";
    private static String PREFIX_0 = "0";

    private Context mContext;
    private View view;
    private TextView tv_title;
    private ImageView iv_close;
    private EditText et_input;
    private TextView tv_confirm;
    private BigDecimal inputMax = new BigDecimal("0");

    public InputPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.merchant_module_popupwindow_input, null);
        this.mContext = mContext;
        /* 初始化view */
        initView();
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void initView() {
        tv_title = view.findViewById(R.id.tv_title);
        iv_close = view.findViewById(R.id.iv_close);
        et_input = view.findViewById(R.id.et_input);
        tv_confirm = view.findViewById(R.id.tv_confirm);

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (!TextUtils.isEmpty(s)) {
                    keepDigitDecimal(s, et_input, SHIFT_ICON_DIGIT);
                    BigDecimal num = new BigDecimal(et_input.getText().toString().trim());
                    if (num.compareTo(inputMax) > 0) {
                        et_input.setText(inputMax + "");
                        et_input.setSelection(et_input.getText().length());
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 限制输入小数点后digit位
     *
     * @param s
     * @param etMoney
     * @param digit
     */
    private static void keepDigitDecimal(CharSequence s, EditText etMoney, int digit) {

        if (s.toString().contains(POINT)) {
            if (s.length() - 1 - s.toString().indexOf(POINT) > digit) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(POINT) + digit + 1);
                etMoney.setText(s);
                etMoney.setSelection(s.length());
            }
        }
        if (POINT.equals(s.toString().trim())) {
            s = PREFIX_0 + s;
            etMoney.setText(s);
            etMoney.setSelection(2);
        }

        if (s.toString().startsWith(PREFIX_0)
                && s.toString().trim().length() > 1) {
            if (!POINT.equals(s.toString().substring(1, 2))) {
                etMoney.setText(s.subSequence(0, 1));
                etMoney.setSelection(1);
            }
        }
    }

    /**
     * 弹出对话框
     *
     * @param v
     */
    public void showPop(View v) {
        showAtLocation(v, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.3f);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }


    /**
     * 设置输入最大数量
     *
     * @param inputMax
     */
    public void setInputMax(String inputMax) {
        this.inputMax = new BigDecimal(inputMax);
    }

    /**
     * 置空输入框
     */
    public void setEtInputIsEmpty() {
         et_input.setText("");
    }

    public String getEtInput() {
        return et_input.getText().toString().trim();
    }

    public void confirm(View.OnClickListener listener) {
        tv_confirm.setOnClickListener(listener);
    }

}