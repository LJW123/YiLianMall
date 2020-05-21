package com.yilian.mall.ctrip.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.yilian.mall.R;

public class EasySwitchButton extends View {

    Bitmap mSwitchBackOn;

    //	private String NAMESPACE = "http://schemas.android.com/apk/res/com.yilian.mall";
//	private String ATTR_IS_OPENED = "isOpened";
    Bitmap mSwitchBackOff;
    Bitmap mCurrSwitch;
    boolean isOpened = false;
    private OnOpenedListener onOpenedListener;

    public EasySwitchButton(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
//		if (attrs != null){
//			int  switchBackOnId= attrs.getAttributeResourceValue(NAMESPACE, "switch_on", -1);
//			mSwitchBackOn = BitmapFactory.decodeResource(getResources(), switchBackOnId);
//
//			int  switchBackOffId= attrs.getAttributeResourceValue(NAMESPACE, "switch_off", -1);
//			mSwitchBackOff = BitmapFactory.decodeResource(getResources(), switchBackOffId);
//
//			if (mSwitchBackOn == null || mSwitchBackOff == null){
//				throw new NullPointerException("资源图片不能为空");
//			}
//		}
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // 读取配置
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EasySwitchButton);
        mSwitchBackOn = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.EasySwitchButton_switch_on,R.mipmap.ios7_switch_on));
        mSwitchBackOff = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.EasySwitchButton_switch_off, R.mipmap.ios7_switch_off));
        mCurrSwitch = mSwitchBackOff;
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setStatus(!isOpened);

                if (onOpenedListener != null) {
                    onOpenedListener.onChecked(EasySwitchButton.this, isOpened);
                }
            }
        });

        initStatus(array);
    }

    private void setStatus(boolean flag) {
        if (flag) {
            mCurrSwitch = mSwitchBackOn;
            isOpened = true;
        } else {
            mCurrSwitch = mSwitchBackOff;
            isOpened = false;
        }

        invalidate();
    }

    /* 初始化开关状态 */
    private void initStatus(TypedArray array) {
        if (array != null) {
            boolean isInitOpened = array.getBoolean(R.styleable.EasySwitchButton_isOpened, false);
            setStatus(isInitOpened);
        }
    }

    public EasySwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EasySwitchButton(Context context) {
        super(context);
        initView(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mCurrSwitch, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSwitchBackOn.getWidth(), mSwitchBackOn.getHeight());
    }

    public void setOnCheckChangedListener(OnOpenedListener onOpenedListener) {
        this.onOpenedListener = onOpenedListener;
    }

    public interface OnOpenedListener {
        void onChecked(View v, boolean isOpened);
    }

}
