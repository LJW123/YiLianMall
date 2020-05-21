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
import com.yilian.networkingmodule.entity.ctrip.CtripHotelFilterEntity;

/**
 * 作者：马铁超 on 2018/10/20 11:33
 */

public class HotelFiltrate_PullDown_DialogFragment extends BaseDialogFragment {
    public static final String TAG = "HotelFiltrate_PullDown_DialogFragment";
    CtripHotelFilterEntity.DataBean.OpeartionsBean datas;

    //  测试
    public static HotelFiltrate_PullDown_DialogFragment getInstance(CtripHotelFilterEntity.DataBean.OpeartionsBean datas) {
        HotelFiltrate_PullDown_DialogFragment hotelFiltrate_pullDown_dialogFragment = new HotelFiltrate_PullDown_DialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, datas);
        hotelFiltrate_pullDown_dialogFragment.setArguments(args);
        return hotelFiltrate_pullDown_dialogFragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        datas = (CtripHotelFilterEntity.DataBean.OpeartionsBean) arguments.get(TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
//        顶部显示
        Window window = getDialog().getWindow();
        if (window != null) {
//            window.setWindowAnimations(R.style.TopToButtomAnim);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.TOP;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = (int) (ScreenUtils.getScreenHeight(getContext()) / 1.2);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popuwindow_hotel_filtrate, null);
        return view;
    }
}
