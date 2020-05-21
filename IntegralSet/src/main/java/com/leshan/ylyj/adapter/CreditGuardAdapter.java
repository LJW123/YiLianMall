package com.leshan.ylyj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.bean.LevelBean;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

import rxfamily.entity.CreditGuardEntity;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class CreditGuardAdapter extends RecyclerView.Adapter<CreditGuardAdapter.MyViewHolder> {
private Context context;
private List<CreditGuardEntity.ListBean> data;
private OnRecyclerViewItemClickListener mOnItemClickListener;
private MyViewHolder holder;//
private int layoutPosition;

public interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, String data, int position);
}

    public CreditGuardAdapter(Context context, List<CreditGuardEntity.ListBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//
        View itemView = View.inflate(context, R.layout.item_credit_guard, null);
        holder = new MyViewHolder(itemView);//

        return holder;
    }

    @Override

    public void onBindViewHolder(final MyViewHolder holder, final int position) {//
        holder.credit_guard_title_tv.setText(data.get(position).getTitle());//
        holder.credit_guard_introduce_tv.setText(data.get(position).getDesc());
        GlideUtil.showImageWithSuffix(context, data.get(position).getImg(), holder.credit_guard_img);
        if(position==0){
            holder.view_horizontal_v.setVisibility(View.GONE);
        }
        holder.itemView.setTag(String.valueOf(data.get(position).getContent()));//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的位置
                layoutPosition = holder.getLayoutPosition();
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(holder.itemView, String.valueOf(holder.itemView.getTag()), layoutPosition);
            }
        });

        //更改状态
//        if (position == layoutPosition) {
//            holder.donation_integral_ll.setBackgroundResource(R.drawable.red_ellipse_side_line);//
//            holder.donation_integral_integral_tv.setTextColor(Color.WHITE);
//            holder.donation_integral_credit_tv.setTextColor(Color.WHITE);
//        } else {
//            holder.donation_integral_ll.setBackgroundResource(R.drawable.gray_ellipse_side_line);//
//            holder.donation_integral_integral_tv.setTextColor(Color.BLACK);
//            holder.donation_integral_credit_tv.setTextColor(Color.GRAY);
//        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView credit_guard_title_tv;
    private TextView credit_guard_introduce_tv;
    private ImageView credit_guard_img;
    private View view_horizontal_v;

    public MyViewHolder(View itemView) {
        super(itemView);
        credit_guard_title_tv = (TextView) itemView.findViewById(R.id.credit_guard_title_tv);
        credit_guard_introduce_tv = (TextView) itemView.findViewById(R.id.credit_guard_introduce_tv);
        credit_guard_img=itemView.findViewById(R.id.credit_guard_img);
        view_horizontal_v=itemView.findViewById(R.id.view_horizontal_v);
    }
}
}
