package com.leshan.ylyj.adapter;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GlideUtil;

import rxfamily.entity.HealthFriendRobListBean;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class RobAdapter extends BaseQuickAdapter<HealthFriendRobListBean.DataBean.ListBean, BaseViewHolder> {

    public RobAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(BaseViewHolder helper, HealthFriendRobListBean.DataBean.ListBean item) {
        helper.addOnClickListener(R.id.already_rob_tv);
        helper.addOnClickListener(R.id.icon_iv);

        ImageView iv = helper.getView(R.id.icon_iv);
        GlideUtil.showCirImage(mContext, item.photo, iv);
        if (TextUtils.isEmpty(item.name)) {
            helper.setText(R.id.nickname_tv, "暂无昵称");
        } else {
            helper.setText(R.id.nickname_tv, item.name);
        }
        helper.setText(R.id.can_tv, "可抢" + item.apples_num + "个健康果");

        TextView tvDesc = helper.getView(R.id.can_tv);
        TextView tvBtn = helper.getView(R.id.already_rob_tv);

        Logger.i("2018年1月17日 16:07:09-" + item.state);
        if (null == item.state) {
            tvDesc.setText("可抢" + item.apples_num + "个健康果");
            tvBtn.setText("快抢");
            tvBtn.setTextColor(mContext.getColor(R.color.library_module_color_white));
            tvBtn.setBackgroundResource(R.drawable.library_module_bg_style_green_30_0);
            tvBtn.setClickable(true);
        } else {
            tvDesc.setText("可抢0个健康果");
            tvBtn.setText("已抢");
            tvBtn.setTextColor(mContext.getColor(R.color.library_module_color_666));
            tvBtn.setBackground(null);
            tvBtn.setClickable(false);
        }
    }
}
