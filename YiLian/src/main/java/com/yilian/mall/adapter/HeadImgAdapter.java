package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ui.TeamDetailActivity;
import com.yilian.networkingmodule.entity.MembersEntity;
import com.yilianmall.merchant.utils.DateUtils;

import java.util.ArrayList;


/**
 * Created by Ray_L_Pain on 2017/7/17 0017.
 */

public class HeadImgAdapter extends RecyclerView.Adapter<HeadImgAdapter.HeadImgHolder> {
    private Context mContext;
    private ArrayList<MembersEntity> list;

    public HeadImgAdapter(Context mContext, ArrayList<MembersEntity> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public HeadImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_member, null);
        HeadImgHolder holder = new HeadImgHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HeadImgHolder holder, int position) {
        com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, list.get(position).memberIcon, holder.imgPhoto);

        if (!TextUtils.isEmpty(list.get(position).rank)) {
            switch (list.get(position).rank) {
                case "0":
                    holder.userLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip0), null);
                    break;
                case "1":
                    holder.userLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip1), null);
                    break;
                case "2":
                    holder.userLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip2), null);
                    break;
                case "3":
                    holder.userLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip3), null);
                    break;
                case "4":
                    holder.userLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip4), null);
                    break;
                case "5":
                    holder.userLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip5), null);
                    break;
                default:
                    holder.userLevel.setVisibility(View.INVISIBLE);
                    break;
            }
        } else {
            holder.userLevel.setVisibility(View.INVISIBLE);
        }

        holder.tvName.setText(list.get(position).memberName);
        holder.tvTime.setText(DateUtils.convertTimeToFormat(list.get(position).regTime));
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent1 = new Intent(mContext, DetailsActivity.class);
                Intent intent1 = new Intent(mContext, TeamDetailActivity.class);
                String userId = list.get(position).memberId;
                intent1.putExtra("user_id", userId);
                mContext.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HeadImgHolder extends RecyclerView.ViewHolder {
        private FrameLayout llParent;
        private ImageView imgPhoto;
        private TextView tvName;
        private TextView tvTime;
        private TextView userLevel;

        public HeadImgHolder(View itemView) {
            super(itemView);
            llParent = (FrameLayout) itemView.findViewById(R.id.ll_parent);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            userLevel = (TextView) itemView.findViewById(R.id.user_level);
        }
    }
}
