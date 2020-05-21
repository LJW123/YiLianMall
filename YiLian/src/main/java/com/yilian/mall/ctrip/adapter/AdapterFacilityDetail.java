package com.yilian.mall.ctrip.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * 作者：马铁超 on 2018/10/27 16:13
 * 详情/设施图片列表数据
 */

public class AdapterFacilityDetail extends BaseQuickAdapter<CtripHotelDetailEntity.DataBean.PicturesBean, BaseViewHolder> {
    public AdapterFacilityDetail(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripHotelDetailEntity.DataBean.PicturesBean item) {
        GlideUtil.showImage(mContext, item.url, helper.getView(R.id.iv_facility_ivlist_item));
    }
}
