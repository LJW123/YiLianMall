package com.yilian.mall.adapter;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.networkingmodule.entity.ActInventSubMemberListEntity;

import java.text.SimpleDateFormat;

/**
 * Created by Ray_L_Pain on 2017/12/16 0016.
 */

public class ActInvitationFriendListAdapter extends BaseQuickAdapter<ActInventSubMemberListEntity.DataBean, BaseViewHolder> {

    public ActInvitationFriendListAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActInventSubMemberListEntity.DataBean item) {

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirImage(mContext, item.photo, iv);

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(item.name)) {
            tvName.setText("暂无昵称");
        } else {
            tvName.setText(item.name);
        }

        TextView tvTime = helper.getView(R.id.tv_time);
        String time="";
        if (!TextUtils.isEmpty(item.regtime)){
           time=TimeUtils.millis2String(Long.parseLong(item.regtime)*1000,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
        tvTime.setText(time);


    }
}
