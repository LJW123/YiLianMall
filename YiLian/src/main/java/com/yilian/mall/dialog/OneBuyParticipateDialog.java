package com.yilian.mall.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.NumberAddSubView;

/**
 * Created by lefenandroid on 16/5/28.
 */
public class OneBuyParticipateDialog extends Dialog {

    private Context context;

    private ClickListenerInterface clickListenerInterface;
    private EditTextTextWatcher editTextTextWatcher;
    private NumberAddSubView numberAddSub;
    private TextView mTxtMoney;
    private ImageView ivBack;

    public OneBuyParticipateDialog(Context context, int maxValue) {
        super(context, R.style.DialogControl);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_one_buy_participate, null);
        setContentView(view);

        numberAddSub = (NumberAddSubView) view.findViewById(R.id.number_add_sub);
        mTxtMoney = (TextView) view.findViewById(R.id.txt_money);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        Button btn5or10 = (Button) view.findViewById(R.id.btn_5or10);
        Button btn20 = (Button) view.findViewById(R.id.btn_20);
        Button btn50 = (Button) view.findViewById(R.id.btn_50);
        Button btn100 = (Button) view.findViewById(R.id.btn_100);
        Button btnParticipation = (Button) view.findViewById(R.id.participation);

        btn5or10.setOnClickListener(new clickListener());
        btn20.setOnClickListener(new clickListener());
        btn50.setOnClickListener(new clickListener());
        btn100.setOnClickListener(new clickListener());
        btnParticipation.setOnClickListener(new clickListener());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        numberAddSub.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                clickListenerInterface.onButtonAddClick(view, value);
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                clickListenerInterface.onButtonSubClick(view, value);
            }
        });

        numberAddSub.getEditText().addTextChangedListener(new TextWatcher());
        btn5or10.setText("5");
        Window dialogWindow = getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public void setTextWatcher(EditTextTextWatcher editTextTextWatcher) {
        this.editTextTextWatcher = editTextTextWatcher;
    }

    public void setCount(int value, double snatchOnceExpend) {
        if (numberAddSub != null) {
            numberAddSub.setValue(value);

            Editable text = getEditText().getText();
            Spannable spanText = text;
            Selection.setSelection(spanText, text.length());
//            mTxtMoney.setText(Html.fromHtml("共" + "&nbsp<font color=\"#247df7\">"+String.format("%.2f",value * snatchOnceExpend / 100)+"乐享币</font>"));
        }
    }

    public void setCountValue(int value, double snatchOnceExpend) {
        if (numberAddSub != null) {
//            numberAddSub.setValue(value);
            mTxtMoney.setText(MoneyUtil.set¥Money(String.valueOf(value)) + "元");
        }
    }

    public String getCount() {
        return String.valueOf(numberAddSub.getValue());
    }

    public EditText getEditText() {
        return numberAddSub.getEditText();
    }

    public interface ClickListenerInterface {

        public void time5or10();

        public void time20();

        public void time50();

        public void time100();

        public void onButtonAddClick(View view, int value);

        public void onButtonSubClick(View view, int value);

        public void participation();

    }

    public interface EditTextTextWatcher {
        public void beforeTextChanged(CharSequence s, int start, int count, int after);

        public void onTextChanged(CharSequence s, int start, int before, int count);

        public void afterTextChanged(Editable s);
    }

    private class TextWatcher implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            editTextTextWatcher.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextTextWatcher.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            editTextTextWatcher.afterTextChanged(s);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.btn_5or10:
                    clickListenerInterface.time5or10();
                    break;
                case R.id.btn_20:
                    clickListenerInterface.time20();
                    break;
                case R.id.btn_50:
                    clickListenerInterface.time50();
                    break;
                case R.id.btn_100:
                    clickListenerInterface.time100();
                    break;
                case R.id.participation:
                    clickListenerInterface.participation();
                    break;

            }
        }

    }


}
