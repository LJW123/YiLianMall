package com.yilian.mall.ctrip.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripKeywordEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripMultiItem.CtripKeywordLayoutType;

/**
 * 携程 关键字 适配器
 *
 * @author Created by Zg on 2018/9/03.
 */

public class CtripKeywordAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public CtripKeywordAdapter() {
        super(null);
        addItemType(CtripKeywordLayoutType.record, R.layout.ctrip_item_keyword);
        addItemType(CtripKeywordLayoutType.location, R.layout.ctrip_item_keyword);
        addItemType(CtripKeywordLayoutType.zone, R.layout.ctrip_item_keyword);
        addItemType(CtripKeywordLayoutType.brand, R.layout.ctrip_item_keyword);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case CtripKeywordLayoutType.record:
                CtripKeywordEntity.Record record = (CtripKeywordEntity.Record) item;
                helper.setText(R.id.tv_name, record.name);
                break;
            case CtripKeywordLayoutType.location:
                CtripKeywordEntity.Location location = (CtripKeywordEntity.Location) item;
                helper.setText(R.id.tv_name, location.locationname);
                break;
            case CtripKeywordLayoutType.zone:
                CtripKeywordEntity.Zone zone = (CtripKeywordEntity.Zone) item;
                helper.setText(R.id.tv_name, zone.zonename);
                break;
            case CtripKeywordLayoutType.brand:
                CtripKeywordEntity.Brand brand = (CtripKeywordEntity.Brand) item;
                helper.setText(R.id.tv_name, brand.BrandName);
                break;
            default:
                break;
        }
    }
}
