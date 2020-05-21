package com.yilian.mall.adapter;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.V3MoneyMenuEntity;

import java.util.List;

/**
 * 奖券 菜单
 */

public class V3MoneyMenuAdapter extends BaseQuickAdapter<V3MoneyMenuEntity.DataBean, BaseViewHolder> {

    private Activity activity;

    private double ratio = 0.508695652173913;//宽高比

    public V3MoneyMenuAdapter(Activity activity, List<V3MoneyMenuEntity.DataBean> data) {
        super(R.layout.item_money_menu, data);
        this.activity = activity;
    }

    @Override
    protected void convert(final BaseViewHolder helper, V3MoneyMenuEntity.DataBean item) {
        ImageView img_iv = helper.getView(R.id.img_iv);

        DisplayMetrics d = RxDeviceTool.getDisplayMetrics(activity);
        int width = (d.widthPixels - RxImageTool.dp2px(10)) / 3 - RxImageTool.dp2px(10);
        int height = (int) (width * ratio);
        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) img_iv.getLayoutParams();
        para.height = height;
        para.width = width;
        img_iv.setLayoutParams(para);

        GlideUtil.showImageNoSuffixNoPlaceholder(activity, item.getImage(), img_iv);
    }
}
