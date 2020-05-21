package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.NoticeModel;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;


/**
 * Created by Ray_L_Pain on 2017/7/13 0013.
 */

public class YhsFragmentAdapter extends RecyclerView.Adapter<YhsFragmentAdapter.NoticeViewHolder> {
    private Context mContext;
    private ArrayList<NoticeModel.NewsBean> list;

    public YhsFragmentAdapter(Context mContext, ArrayList<NoticeModel.NewsBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_jp_good, parent, false);
        return new NoticeViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(NoticeViewHolder holder, int position) {
        NoticeModel.NewsBean newsBean = list.get(position);

        if ("1".equals(newsBean.recomend)) {
            holder.iv_up.setVisibility(View.VISIBLE);
        } else {
            holder.iv_up.setVisibility(View.GONE);
        }
        holder.tv_content.setText(newsBean.name);
        holder.tv_right.setText(newsBean.pv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, newsBean.content);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_view;
        private ImageView iv_up;
        private TextView tv_content;
        private TextView tv_right;

        public NoticeViewHolder(View itemView) {
            super(itemView);

            item_view = (LinearLayout) itemView.findViewById(R.id.item_view);
            iv_up = (ImageView) itemView.findViewById(R.id.iv_up);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_right = (TextView) itemView.findViewById(R.id.tv_right);
        }
    }
}
