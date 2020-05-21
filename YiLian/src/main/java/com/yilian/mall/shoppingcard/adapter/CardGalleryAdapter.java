package com.yilian.mall.shoppingcard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.yilian.mall.R;
import com.yilian.mall.shoppingcard.widgets.gallery.CardAdapterHelper;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.networkingmodule.entity.shoppingcard.CardHomeHeaderEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zg on 2018/11/15.
 */
public class CardGalleryAdapter extends RecyclerView.Adapter<CardGalleryAdapter.ViewHolder> {
    private  Context mContext;
    private List<CardHomeHeaderEntity.CommJumpBean> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    public CardGalleryAdapter(List<CardHomeHeaderEntity.CommJumpBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_gallery, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, mList.get(position).img,  holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(mList.get(position).type, mList.get(position).content);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }

}
