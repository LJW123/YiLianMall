package com.yilian.mall.jd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilian.mall.jd.GoodsConfigBean;
import com.yilian.mall.R;

import java.util.List;


/**
 * 商品详情里的规格参数数据适配器
 */
public class GoodsConfigAdapter extends RecyclerView.Adapter<GoodsConfigAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private List<GoodsConfigBean> data;

    public GoodsConfigAdapter(Context context, List<GoodsConfigBean> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }

    public void setData(List<GoodsConfigBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public GoodsConfigAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jd_config_listview_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_config_key.setText(data.get(position).getKeyProp());
        holder.tv_config_value.setText(data.get(position).getValue());
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_config_key;
        TextView tv_config_value;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_config_key = (TextView) itemView.findViewById(R.id.tv_config_key);
            tv_config_value = (TextView) itemView.findViewById(R.id.tv_config_value);
        }
    }

}
