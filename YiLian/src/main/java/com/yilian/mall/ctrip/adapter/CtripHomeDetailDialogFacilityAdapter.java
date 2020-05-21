package com.yilian.mall.ctrip.adapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripRoomTypeInfo;

/**
 * 携程 服务设施 适配器
 *
 * @author Created by Zg on 2018/11/7.
 */

public class CtripHomeDetailDialogFacilityAdapter extends BaseQuickAdapter<CtripRoomTypeInfo.FacilitiesBean.facilityItemBean, BaseViewHolder> {


    public CtripHomeDetailDialogFacilityAdapter() {
        super(R.layout.ctrip_item_home_detail_dialog_facility);
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripRoomTypeInfo.FacilitiesBean.facilityItemBean item) {
        helper.setText(R.id.tv_name, item.name);
    }
}