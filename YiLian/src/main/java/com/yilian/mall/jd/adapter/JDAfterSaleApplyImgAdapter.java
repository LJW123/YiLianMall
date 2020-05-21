package com.yilian.mall.jd.adapter;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;


public class JDAfterSaleApplyImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public JDAfterSaleApplyImgAdapter() {
        super(R.layout.jd_item_after_sale_apply_img);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideUtil.showImageWithSuffix(mContext, item, helper.getView(R.id.iv));
        helper.addOnClickListener(R.id.iv_del);
    }


}
