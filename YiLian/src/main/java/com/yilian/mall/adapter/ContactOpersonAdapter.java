package com.yilian.mall.adapter;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ContactSperson;

import java.util.List;

/**
 * Created by ZYH on 2017/12/8 0008.
 */

public class ContactOpersonAdapter extends BaseQuickAdapter<ContactSperson.Data, BaseViewHolder> {
    private boolean isPupContact = false;

    public ContactOpersonAdapter(int layoutResId, @Nullable List<ContactSperson.Data> data) {
        super(layoutResId, data);
    }

    public void setPupContact(boolean isPupContact) {
        this.isPupContact = isPupContact;

    }

    @Override
    protected void convert(BaseViewHolder helper, ContactSperson.Data item) {
        if (!TextUtils.isEmpty(item.phone)){
            helper.setText(R.id.tv_code_number, item.phone);
        }

        if (!TextUtils.isEmpty(item.name)){
            helper.setText(R.id.tv_nick_name, item.name);
        }

        if (isPupContact) {
            ImageView imageView = helper.getView(R.id.iv_circle_photo);
            com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, item.photo, imageView);
        }
    }
}
