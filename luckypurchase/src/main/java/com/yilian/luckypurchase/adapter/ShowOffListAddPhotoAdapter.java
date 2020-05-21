package com.yilian.luckypurchase.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by LYQ on 2017/10/24.
 */

public class ShowOffListAddPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShowOffListAddPhotoAdapter(@LayoutRes int layoutResId, @Nullable List data) {
        super(layoutResId, data);
        Logger.i("listSize    "+data.size()+" text  "+data.get(0));
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.iv_add);
        helper.addOnClickListener(R.id.iv_del);
        int position = helper.getLayoutPosition();

        Logger.i("position   "+position+"  itemStr  "+item);
        ImageView ivIcon = helper.getView(R.id.iv_add);
        ImageView ivDel = helper.getView(R.id.iv_del);
        if (position == 0 && item.equals("add")) {
            ivDel.setVisibility(View.GONE);
            ivIcon.setImageResource(R.mipmap.library_module_add_photo);
        } else if (item.equals("add")){
            ivDel.setVisibility(View.GONE);
            ivIcon.setImageResource(R.mipmap.library_module_add);
        }else {
            ivDel.setVisibility(View.VISIBLE);
            GlideUtil.showImageWithSuffix(mContext, item, ivIcon);
        }
    }
}
