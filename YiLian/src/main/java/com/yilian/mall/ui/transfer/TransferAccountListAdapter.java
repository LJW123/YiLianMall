package com.yilian.mall.ui.transfer;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;

import java.util.List;

/**
 * Created by ZYH on 2017/12/8 0008.
 */

public class TransferAccountListAdapter extends BaseQuickAdapter<Donee, BaseViewHolder> {
    private boolean isPupContact = false;

    public TransferAccountListAdapter(int layoutResId, @Nullable List<Donee> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Donee item) {
        if (!TextUtils.isEmpty(item.phone)) {
            helper.setText(R.id.tv_code_number, item.phone);
        }

        if (!TextUtils.isEmpty(item.nick)) {
            helper.setText(R.id.tv_nick_name, item.nick);
        }

        ImageView imageView = helper.getView(R.id.iv_circle_photo);
        if (imageView != null) {
            if (!TextUtils.isEmpty(item.photo)) {
                com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, item.photo, imageView);
            }else{
                com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, "", imageView);
            }
        }
    }
}
