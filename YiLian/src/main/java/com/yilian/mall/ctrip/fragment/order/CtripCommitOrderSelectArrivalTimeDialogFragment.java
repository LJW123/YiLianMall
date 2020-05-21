package com.yilian.mall.ctrip.fragment.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaofo on 2018/9/28.
 */

public class CtripCommitOrderSelectArrivalTimeDialogFragment extends BaseDialogFragment {
    public static final String TAG = "CtripCommitOrderSelectArrivalTimeDialogFragment";
    public static final String TAG_ARRAIVAL_TIME_SELECTED = "CtripCommitOrderSelectArrivalTimeDialogFragment__ARRAIVAL_TIME_SELECTED";
    public static final String TAG_SELECTED_TIME_NAME = "CtripCommitOrderSelectArrivalTimeDialogFragment_SELECTED_TIME_NAME";
    private ArrayList<CtripHotelDetailEntity.DataBean.ArrivalOperationsBean> mArraivalOperationsBeans;

    public static CtripCommitOrderSelectArrivalTimeDialogFragment
    getInstance(ArrayList<CtripHotelDetailEntity.DataBean.ArrivalOperationsBean> arrivalOperationsBeans,
                ArraivalTimeSelected arraivalTimeSelected, String perSelectArrivalTimeName) {
        CtripCommitOrderSelectArrivalTimeDialogFragment ctripCommitOrderSelectArrivalTimeDialogFragment = new CtripCommitOrderSelectArrivalTimeDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, arrivalOperationsBeans);
        args.putSerializable(TAG_ARRAIVAL_TIME_SELECTED, arraivalTimeSelected);
        args.putSerializable(TAG_SELECTED_TIME_NAME, perSelectArrivalTimeName);
        ctripCommitOrderSelectArrivalTimeDialogFragment.setArguments(args);
        return ctripCommitOrderSelectArrivalTimeDialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mArraivalOperationsBeans = (ArrayList<CtripHotelDetailEntity.DataBean.ArrivalOperationsBean>) arguments.getSerializable(TAG);
        ArraivalTimeSelected mArraivalTimeSelected = (ArraivalTimeSelected) arguments.getSerializable(TAG_ARRAIVAL_TIME_SELECTED);
        String mSelectedTimeName = arguments.getString(TAG_SELECTED_TIME_NAME);
        MyAdapter myAdapter = new MyAdapter(mArraivalOperationsBeans, mSelectedTimeName);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(getContext());
        title.setText("预计到店");
        title.setPadding(DPXUnitUtil.dp2px(getContext(),15),DPXUnitUtil.dp2px(getContext(),15),0,DPXUnitUtil.dp2px(getContext(),10));
        linearLayout.addView(title);
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setPadding(DPXUnitUtil.dp2px(getContext(),5),0,DPXUnitUtil.dp2px(getContext(),15),0);
        myAdapter.bindToRecyclerView(recyclerView);
        linearLayout.addView(recyclerView);
//        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                CtripHotelDetailEntity.DataBean.ArrivalOperationsBean selectArrivalTime
//                        = (CtripHotelDetailEntity.DataBean.ArrivalOperationsBean) adapter.getItem(position);
//                mArraivalTimeSelected.onArraivalTimeSelected(selectArrivalTime);
//                dismiss();
//            }
//        });
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CtripHotelDetailEntity.DataBean.ArrivalOperationsBean selectArrivalTime = (CtripHotelDetailEntity.DataBean.ArrivalOperationsBean) adapter.getItem(position);
                mArraivalTimeSelected.onArraivalTimeSelected(selectArrivalTime);
                dismiss();
            }
        });
        return linearLayout;
    }

    public interface ArraivalTimeSelected extends Serializable {
        void onArraivalTimeSelected(CtripHotelDetailEntity.DataBean.ArrivalOperationsBean arrivalOperationsBean);
    }

    class MyAdapter extends BaseQuickAdapter<CtripHotelDetailEntity.DataBean.ArrivalOperationsBean, BaseViewHolder> {
        private String mSelectTime;

        public MyAdapter(@Nullable List<CtripHotelDetailEntity.DataBean.ArrivalOperationsBean> data, String selectTime) {
            super(R.layout.item_ctrip_commit_order_select_arrival_time,data);
            mSelectTime = selectTime;
        }

        @Override
        protected void convert(BaseViewHolder helper, CtripHotelDetailEntity.DataBean.ArrivalOperationsBean item) {
            TextView itemView = helper.getView(R.id.tv_select_time);
            itemView.setText(item.name);
            if (mSelectTime.equals(item.name)) {
                itemView.setTextColor(ContextCompat.getColor(mContext, R.color.color_4289FF));
                itemView.setBackgroundResource(R.drawable.item_ctrip_select_arrival_time_selected);
            } else {
                itemView.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
                itemView.setBackgroundResource(R.drawable.item_ctrip_select_arrival_time_unselected);
            }
        }
    }
}
