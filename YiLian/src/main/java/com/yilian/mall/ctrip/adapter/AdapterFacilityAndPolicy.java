package com.yilian.mall.ctrip.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.bean.FacilityListBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;

/**
 * 作者：马铁超 on 2018/10/28 16:29
 * 详情/设施——设施政策
 */

public class AdapterFacilityAndPolicy extends BaseQuickAdapter<FacilityListBean,BaseViewHolder>{

    public AdapterFacilityAndPolicy(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, FacilityListBean item) {
        helper.setText(R.id.tv_facility_title,item.FacilityTitle);
        if(item.FacilityTitle.equals("服务项目")){
            helper.setBackgroundRes(R.id.iv_facility_head,R.mipmap.iv_facility1);
        }else if(item.FacilityTitle.equals("活动设施")){
            helper.setBackgroundRes(R.id.iv_facility_head,R.mipmap.iv_facility2);
        }else if(item.FacilityTitle.equals("客房设施和服务")){
            helper.setBackgroundRes(R.id.iv_facility_head,R.mipmap.iv_facility3);
        }else if (item.FacilityTitle.equals("通用设施")){
            helper.setBackgroundRes(R.id.iv_facility_head,R.mipmap.iv_facility4);
        }
        RecyclerView recyclerView = helper.getView(R.id.rv_facility_and_policy);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(
                new BaseQuickAdapter<FacilityListBean.FacilityListData, BaseViewHolder>(
                        R.layout.item_ctrip_hotel_detail_descriptions, item.facilityListData) {
                    @Override
                    protected void convert(BaseViewHolder helper, FacilityListBean.FacilityListData item) {
                        helper.getView(R.id.view_circle).setVisibility(View.GONE);
                        helper.setText(R.id.tv_content,  item.name);
                    }
                });
    }
}
