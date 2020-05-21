package com.yilian.mall.adapter;


import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.utils.DistanceUtil;
import com.yilian.mylibrary.GlideUtil;

import com.yilian.networkingmodule.entity.StealMoreRedpackgs;

/**
 *
 * @author zhaiyaohua
 * @date 2017/12/2 0002
 */

public class MorePackgetsAdapter extends BaseQuickAdapter<StealMoreRedpackgs.RedPackgeDetails, BaseViewHolder> {
    private boolean isHasHeader = false;

    public MorePackgetsAdapter(int layoutResId, boolean isHasHeader) {
        super(layoutResId);
        this.isHasHeader = isHasHeader;
    }

    @Override
    protected void convert(BaseViewHolder helper, StealMoreRedpackgs.RedPackgeDetails item) {
            if (TextUtils.isEmpty(item.merchantName)){
                helper.setText(R.id.name, "暂无昵称");
            }else {
                helper.setText(R.id.name, item.merchantName);
            }
            helper.setText(R.id.redpackgers_counts, "x" + item.count);
            GlideUtil.showImageWithSuffix(mContext, item.merchantImage, helper.getView(R.id.shop_imge));
            if (!TextUtils.isEmpty(item.distance)){
                if (Double.parseDouble(item.distance)>=1000){
                    double distance=Double.parseDouble(item.distance)/1000;
                    helper.setText(R.id.instance,distance+"km");
                }else {
                    helper.setText(R.id.instance,item.distance+"m");
                }
            }else {
                helper.setText(R.id.instance, "计算距离失败");
            }
    }
}
