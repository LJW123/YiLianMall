package com.yilian.mall.ctrip.fragment.hoteldetail;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;

import java.util.ArrayList;

/**
 * @author xiaofo on 2018/9/1.
 */

public class DescriptionsDialogFragment extends BaseDialogFragment {

    public static final String TAG = "DescriptionsDialogFragment";
    private RecyclerView recyclerView;
    private View ivClose;
    private ArrayList<CtripHotelDetailEntity.DataBean.DescriptionsBean> datas;

    public static DescriptionsDialogFragment getInstance(ArrayList<CtripHotelDetailEntity.DataBean.DescriptionsBean> datas) {
        DescriptionsDialogFragment descriptionsDialogFragment = new DescriptionsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, datas);
        descriptionsDialogFragment.setArguments(args);
        return descriptionsDialogFragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        datas = (ArrayList<CtripHotelDetailEntity.DataBean.DescriptionsBean>) arguments.get(TAG);

    }

    @Override
    public void onStart() {
        super.onStart();
//        底部显示
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomToTopAnim);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = (int) (ScreenUtils.getScreenHeight(getContext()) / 1.2);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.ctrip_hotel_detail_descriptions_header, null);
        linearLayout.addView(headerView);
        ivClose = headerView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(
                new BaseQuickAdapter<CtripHotelDetailEntity.DataBean.DescriptionsBean, BaseViewHolder>(
                        R.layout.item_ctrip_hotel_detail_descriptions, datas) {
                    @Override
                    protected void convert(BaseViewHolder helper, CtripHotelDetailEntity.DataBean.DescriptionsBean item) {
                        helper.setText(R.id.tv_content, item.text);
                    }
                });
        linearLayout.addView(recyclerView);
        return linearLayout;
    }

}
