package com.yilian.mylibrary.widget.jdaddressseletor;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.R;


/**
 * Created by smartTop on 2016/10/19.
 */

public class BottomDialog extends Dialog {
    private AddressSelector selector;

    public BottomDialog(Context context, AddressDataSourceInterface addressDataSource) {
        super(context, R.style.library_module_bottom_dialog);
        init(context, addressDataSource);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context, AddressDataSourceInterface addressDataSource) {
        selector = new AddressSelector(context, addressDataSource);
        setContentView(selector.getView());

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DPXUnitUtil.dp2px(context, 300);
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }

    public BottomDialog(Context context, int themeResId, AddressDataSourceInterface addressDataSource) {
        super(context, themeResId);
        init(context, addressDataSource);
    }

    public BottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener, AddressDataSourceInterface addressDataSource) {
        super(context, cancelable, cancelListener);
        init(context, addressDataSource);
    }

    public static BottomDialog show(Context context, AddressDataSourceInterface addressDataSource) {
        return show(context, null, addressDataSource);
    }

    public static BottomDialog show(Context context, OnAddressSelectedListener listener, AddressDataSourceInterface addressDataSource) {
        BottomDialog dialog = new BottomDialog(context, R.style.library_module_bottom_dialog, addressDataSource);
        dialog.selector.setOnAddressSelectedListener(listener);
        dialog.show();

        return dialog;
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.selector.setOnAddressSelectedListener(listener);
    }

    public void setDialogDismisListener(AddressSelector.OnDialogCloseListener listener) {
        this.selector.setOnDialogCloseListener(listener);
    }

    /**
     * 设置选中位置的监听
     *
     * @param listener
     */
    public void setSelectorAreaPositionListener(AddressSelector.onSelectorAreaPositionListener listener) {
        this.selector.setOnSelectorAreaPositionListener(listener);
    }

    /**
     * 设置字体选中的颜色
     */
    public void setTextSelectedColor(int selectedColorResourceId) {
        this.selector.setTextSelectedColor(selectedColorResourceId);
    }

    public void setSelectorTitle(String title) {
        this.selector.setTitle(title);
    }

    /**
     * 设置字体没有选中的颜色
     */
    public void setTextUnSelectedColor(int unSelectedColorResourceId) {
        this.selector.setTextUnSelectedColor(unSelectedColorResourceId);
    }

    /**
     * 设置字体的大小
     */
    public void setTextSize(float dp) {
        this.selector.setTextSize(dp);
    }

    /**
     * 设置取消按钮是否显示
     */
    public void setCancelImgShow(boolean imgShow) {
        this.selector.setCancelImgShow(imgShow);
    }

    /**
     * 设置返回按钮是否显示
     *
     * @param show
     */
    public void setSelectorBackButttomShow(boolean show) {
        this.selector.setSelectBackButtonShow(show);
    }


    /**
     * 设置字体的背景
     */
    public void setBackgroundColor(int colorId) {
        this.selector.setBackgroundColor(colorId);
    }


    /**
     * 设置指示器的背景
     */
    public void setIndicatorBackgroundColor(int colorId) {
        this.selector.setIndicatorBackgroundColor(colorId);
    }

    public void setCheckedItemBackgroundResource(int resId) {
        this.selector.setCheckedItemBackgroundResource(resId);
    }

    /**
     * 设置指示器的背景
     */
    public void setIndicatorBackgroundColor(String color) {
        this.selector.setIndicatorBackgroundColor(color);
    }

    /**
     * 设置已选中的地区
     *
     * @param provinceCode   省份code
     * @param provinPosition 省份所在的位置
     * @param cityCode       城市code
     * @param cityPosition   城市所在的位置
     * @param countyCode     乡镇code
     * @param countyPosition 乡镇所在的位置
     * @param streetCode     街道code
     * @param streetPosition 街道所在位置
     */
    public void setDisplaySelectorArea(String provinceCode, int provinPosition, String cityCode, int cityPosition, String countyCode, int countyPosition, String streetCode, int streetPosition) {
        this.selector.getSelectedArea(provinceCode, provinPosition, cityCode, cityPosition, countyCode, countyPosition, streetCode, streetPosition);
    }

}