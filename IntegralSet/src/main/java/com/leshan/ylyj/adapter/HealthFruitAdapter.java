package com.leshan.ylyj.adapter;

import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;

import rxfamily.entity.HealthFruitListBean;

/**
 *  健康果
 * @author Administrator
 * @date 2017/12/27 0027
 */

public class HealthFruitAdapter extends BaseQuickAdapter<HealthFruitListBean.DataBean.ListBean, BaseViewHolder> {

    public HealthFruitAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HealthFruitListBean.DataBean.ListBean item) {
        helper.setText(R.id.title_tv, item.describe);
        helper.setText(R.id.time_tv, item.CREATE_DATE);

        TextView tvRight = helper.getView(R.id.state_tv);
        switch (item.TYPE) {
            case "1":
                tvRight.setTextColor(mContext.getResources().getColor(R.color.color_act_green2));
                tvRight.setText("+" + item.APPLES_NUM);
                break;
            case "2":
                tvRight.setTextColor(mContext.getResources().getColor(R.color.color_act_green2));
                tvRight.setText("+" + item.APPLES_NUM);
                break;
            case "3":
                tvRight.setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
                tvRight.setText("-" + item.APPLES_NUM);
                break;
            case "4":
                tvRight.setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
                tvRight.setText("-" + item.APPLES_NUM);
                break;
            default:
                break;
        }
    }
}
