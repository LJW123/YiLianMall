package com.leshan.ylyj.customview;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;


import com.leshan.ylyj.testfor.R;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class DialogPopup7 extends BasePopupWindow implements View.OnClickListener {


//    private String[] strings = new String[]{"fsdafdas", "fasfa", "fasdfasf", "fasfdafa"};

    public NumberPickerView picker;
    public NumberPickerView picker1;

    public DialogPopup7(final Activity context, String[] strings, String[] strings1) {
        super(context);
        picker = (NumberPickerView) findViewById(R.id.record_year_picker);
        picker1 = (NumberPickerView) findViewById(R.id.record_month_picker);
        picker.refreshByNewDisplayedValues(strings);
        picker1.refreshByNewDisplayedValues(strings1);
//        picker.setOnClickListener(this);
    }


    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(false);
        Animation shakeAnima = new TranslateAnimation(0, 0, 500, 0);
//        shakeAnima.setInterpolator(new CycleInterpolator(5));
        shakeAnima.setDuration(500);
//        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(shakeAnima);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {

        return createPopupById(R.layout.dialog_record_graduation_time);

    }

    @Override
    public View initAnimaView() {

        return findViewById(R.id.record_numberpicker_ll);


    }

    @Override
    public void onClick(View view) {

    }
}
