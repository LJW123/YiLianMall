package com.leshan.ylyj.customview;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;


import com.leshan.ylyj.testfor.R;

import es.dmoral.toasty.Toasty;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class DialogPopup3 extends BasePopupWindow implements View.OnClickListener {


    private LinearLayout call_ll;
    private LinearLayout index_ll;
    private LinearLayout solo_ll;

    public DialogPopup3(Activity context) {
        super(context);

        call_ll = (LinearLayout) findViewById(R.id.call_ll);
        index_ll = (LinearLayout) findViewById(R.id.index_ll);
        solo_ll = (LinearLayout) findViewById(R.id.solo_ll);


    }


    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(false);
        //        Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        //        shakeAnima.setInterpolator(new CycleInterpolator(5));
        //        shakeAnima.setDuration(400);
        //        set.addAnimation(getDefaultAlphaAnimation());
        //        set.addAnimation(shakeAnima);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.different_buy_dialog_layout);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.reminder_ll);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.call_ll) {
            Toasty.success(getContext(), "消息").show();

        } else if (i == R.id.index_ll) {
            Toasty.success(getContext(), "首页").show();

        } else if (i == R.id.solo_ll) {
            Toasty.success(getContext(), "分享").show();

        }
    }
}