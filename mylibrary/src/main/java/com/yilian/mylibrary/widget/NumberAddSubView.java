package com.yilian.mylibrary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.R;


public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    public static final String TAG = "NumberAddSubView";
    public static final int DEFUALT_MAX = 1000;

    private EditText mEtxtNum;
    private Button mBtnAdd;
    private Button mBtnSub;

    private int layout;

    private OnButtonClickListener onButtonClickListener;

    private LayoutInflater mInflater;

    private int value;
    private int minValue;
    private int maxValue = DEFUALT_MAX;


    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.library_module_NumberAddSubView, defStyleAttr, 0);

            int layout = a.getResourceId(R.styleable.library_module_NumberAddSubView_library_module_lefenlayout, R.layout.library_module_widet_num_add_sub);
            setLayout(layout);
            initView();

            int val = a.getInt(R.styleable.library_module_NumberAddSubView_library_module_value, 0);
            setValue(val);

            int maxVal = a.getInt(R.styleable.library_module_NumberAddSubView_library_module_maxValue, 0);
            if (maxVal != 0)
                setMaxValue(maxVal);

            int minVal = a.getInt(R.styleable.library_module_NumberAddSubView_library_module_minValue, 0);
            setMinValue(minVal);

            Drawable etBackground = a.getDrawable(R.styleable.library_module_NumberAddSubView_library_module_editBackground);
            if (etBackground != null)
                setEditTextBackground(etBackground);

            Drawable buttonAddBackground = a.getDrawable(R.styleable.library_module_NumberAddSubView_library_module_buttonAddBackgroud);
            if (buttonAddBackground != null)
                setButtonAddBackgroud(buttonAddBackground);

            Drawable buttonSubBackground = a.getDrawable(R.styleable.library_module_NumberAddSubView_library_module_buttonSubBackgroud);
            if (buttonSubBackground != null)
                setButtonSubBackgroud(buttonSubBackground);

            a.recycle();
        }


    }


    private void initView() {
        View view = mInflater.inflate(layout, this, true);

        mEtxtNum = (EditText) view.findViewById(R.id.etxt_num);
//        mEtxtNum.setInputType(InputType.TYPE_NULL);
//        mEtxtNum.setKeyListener(null);

        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnSub = (Button) view.findViewById(R.id.btn_sub);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            numAdd();

            if (onButtonClickListener != null) {
                onButtonClickListener.onButtonAddClick(v, this.value);
            }

        } else if (v.getId() == R.id.btn_sub) {
            numSub();

            if (onButtonClickListener != null) {
                onButtonClickListener.onButtonSubClick(v, this.value);
            }
        }
    }


    private void numAdd() {
        getValue();

        if (this.value <= maxValue)
            this.value = +this.value + 1;

        mEtxtNum.setText(value + "");
    }


    private void numSub() {
        getValue();

        if (this.value > minValue)
            this.value = this.value - 1;

        mEtxtNum.setText(value + "");
    }


    public int getValue() {
        String value = mEtxtNum.getText().toString();

        if (value != null && !"".equals(value))
            this.value = NumberFormat.convertToInt(value,9999999);

        return this.value;
    }

    public void setValue(int value) {
        mEtxtNum.setText(value + "");
        this.value = value;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }


    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }


    public void setEditTextBackground(Drawable drawable) {

        mEtxtNum.setBackgroundDrawable(drawable);

    }


    public void setEditTextBackground(int drawableId) {

        setEditTextBackground(getResources().getDrawable(drawableId));

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud) {
        this.mBtnAdd.setBackground(backgroud);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud) {
        this.mBtnSub.setBackground(backgroud);
    }


    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {

        public void onButtonAddClick(View view, int value);

        public void onButtonSubClick(View view, int value);

    }

    public EditText getEditText() {
        return mEtxtNum;
    }


}
