package com.yilian.mall.ctrip.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.base.BaseRecyclerAdapter;
import com.yilian.mall.ctrip.adapter.base.BaseRecyclerViewHolder;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/9/7 16:40
 */

public class AdapterSecData extends BaseRecyclerAdapter<SearchFilterBean.SecSortBean> {

    private MyItemClickListener mItemClickListener;

    public AdapterSecData(Context context, List<SearchFilterBean.SecSortBean> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public BaseRecyclerViewHolder initViewHolder(View view) {
        ViewHolder mViewHolder = new ViewHolder(view, mItemClickListener);
        return mViewHolder;
    }

    @Override
    public void dealView(Context context, int position, BaseRecyclerViewHolder holder) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        SearchFilterBean.SecSortBean secSortBean = _list.get(position);
        mViewHolder.cb_sec.setText(secSortBean.getTitle());
        if (secSortBean.isCheck() == true) {
            mViewHolder.cb_sec.setBackgroundResource(R.drawable.yuanjiao_blue_radiu10);
            mViewHolder.cb_sec.setTextColor(Color.parseColor("#FF4289FF"));
        } else {
            mViewHolder.cb_sec.setBackgroundResource(R.drawable.yuanjiao_white_radiu10);
            mViewHolder.cb_sec.setTextColor(Color.parseColor("#333333"));
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private CheckBox cb_sec;

        protected ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initView(View view) {
            cb_sec = view.findViewById(R.id.cb_sec);
        }

        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
