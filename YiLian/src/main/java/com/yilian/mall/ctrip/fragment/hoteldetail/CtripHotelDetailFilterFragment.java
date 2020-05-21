package com.yilian.mall.ctrip.fragment.hoteldetail;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mylibrary.ScreenUtils;

/**
 * @author xiaofo on 2018/9/20.
 */

public class CtripHotelDetailFilterFragment extends BaseDialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//       顶部部显示
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomToTopAnim);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.TOP;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = (int) (ScreenUtils.getScreenHeight(getContext()) / 1.2);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
