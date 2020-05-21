package com.leshan.ylyj.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.yilian.mylibrary.GlideUtil;

import rxfamily.entity.MyHealthListBean;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class HealthAdapter extends BaseQuickAdapter<MyHealthListBean.DataBean.ListBean, BaseViewHolder> {

    public HealthAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyHealthListBean.DataBean.ListBean item) {
        ImageView iv = helper.getView(R.id.icon_iv);
        GlideUtil.showImageRadius(mContext, item.IMG_URL, iv, 20);

        helper.setText(R.id.title_tv, item.NAME);

        helper.setText(R.id.diease_tv, item.RANGE_TITLE);

        helper.setText(R.id.time_tv, item.LIMIT);

        LinearLayout cardView = helper.getView(R.id.card_view);
        TextView tvJoin = helper.getView(R.id.add_tv);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkipUtils.toHelpEach(mContext, item.projectId, "", "", "", "", "", null, item.NAME);
            }
        });
    }
}
