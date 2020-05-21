package com.yilian.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.SupportBankListEntity;

/**
 * @author Created by  on 2018/1/21.
 */

public class SupportBankListAdapter extends BaseQuickAdapter<SupportBankListEntity.DataBean, com.chad.library.adapter.base.BaseViewHolder> {
    public SupportBankListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SupportBankListEntity.DataBean item) {
        helper.setText(R.id.tv_bank_name, item.bankName);
        GlideUtil.showImageNoSuffix(mContext, item.bankLogo, helper.getView(R.id.iv_icon));
    }
}
