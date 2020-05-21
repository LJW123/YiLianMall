package com.yilian.mall.ctrip.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripKeywordBySearchEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripMultiItem.CtripKeywordLayoutType;

/**
 * 携程 关键字 搜索 适配器
 *
 * @author Created by Zg on 2018/9/03.
 */

public class CtripKeywordBySearchAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public CtripKeywordBySearchAdapter() {
        super(null);
        addItemType(CtripKeywordLayoutType.location, R.layout.ctrip_item_keyword_by_search);
        addItemType(CtripKeywordLayoutType.zone, R.layout.ctrip_item_keyword_by_search);
        addItemType(CtripKeywordLayoutType.brand, R.layout.ctrip_item_keyword_by_search);
        addItemType(CtripKeywordLayoutType.hotel, R.layout.ctrip_item_keyword_by_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case CtripKeywordLayoutType.location:
                CtripKeywordBySearchEntity.Location location = (CtripKeywordBySearchEntity.Location) item;
                helper.setText(R.id.tv_name, location.cityname + "," + location.locationname);
                helper.setText(R.id.tv_type, "城市");
                break;
            case CtripKeywordLayoutType.zone:
                CtripKeywordBySearchEntity.Zone zone = (CtripKeywordBySearchEntity.Zone) item;
                helper.setText(R.id.tv_name, zone.zonename);
                helper.setText(R.id.tv_type, "商业区");
                break;
            case CtripKeywordLayoutType.brand:
                CtripKeywordBySearchEntity.Brand brand = (CtripKeywordBySearchEntity.Brand) item;
                helper.setText(R.id.tv_name, brand.BrandName);
                helper.setText(R.id.tv_type, "品牌");
                break;
            case CtripKeywordLayoutType.hotel:
                CtripKeywordBySearchEntity.Hotel hotel = (CtripKeywordBySearchEntity.Hotel) item;
                helper.setText(R.id.tv_name, hotel.HotelName);
                helper.setText(R.id.tv_type, "酒店");
                break;
            default:
                break;
        }
    }
}
