package com.yilian.mall.ctrip.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.fragment.order.CtripOrderCommonFragment;
import com.yilian.mall.ctrip.util.CtripHomeUtils;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderListEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripMultiItem.CtripOrderListLayoutType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class CtripOrderCommonAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    TextView tvDelete;
    TextView tvShare;
    TextView tvAgain;
    TextView tvReminder;
    TextView tvSubmit;

    public CtripOrderCommonAdapter() {
        super(null);
        addItemType(CtripOrderListLayoutType.header, R.layout.ctrip_item_order_common_header);
        addItemType(CtripOrderListLayoutType.content, R.layout.ctrip_item_order_common);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case CtripOrderListLayoutType.header:
                CtripOrderListEntity.HeadBean dataBean = (CtripOrderListEntity.HeadBean) item;
                helper.setText(R.id.tv_date, "预订日期：" + dataBean.headDate);
                break;
            case CtripOrderListLayoutType.content:
                CtripOrderListEntity.OrderList mData = (CtripOrderListEntity.OrderList) item;
                setDataByType(helper, mData);
                break;
            default:
                break;
        }
    }

    private void setDataByType(BaseViewHolder helper, CtripOrderListEntity.OrderList mData) {
        tvDelete = helper.getView(R.id.tv_delete);
        tvShare = helper.getView(R.id.tv_share);
        tvAgain = helper.getView(R.id.tv_again);
        tvReminder = helper.getView(R.id.tv_reminder);
        tvSubmit = helper.getView(R.id.tv_submit);
        initButton();
        //添加点击事件
        helper.addOnClickListener(R.id.tv_delete);
        helper.addOnClickListener(R.id.tv_share);
        helper.addOnClickListener(R.id.tv_again);
        helper.addOnClickListener(R.id.tv_reminder);
        helper.addOnClickListener(R.id.tv_submit);
        //订单icon
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        ivIcon.setImageResource(R.mipmap.ctrip_order_ic_normal);
        //标题
        TextView tvTitle = helper.getView(R.id.tv_title);
        tvTitle.setText(mData.HotelName);
        tvTitle.setTextColor(Color.parseColor("#333333"));
        //地址
        helper.setText(R.id.tv_address, mData.Address);
        //入住离店时间 时长
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        helper.setText(R.id.tv_duration, String.format("%s至%s %s间",sdf.format( Long.parseLong(mData.TimeSpanStart)*1000), sdf.format( Long.parseLong(mData.TimeSpanEnd)*1000), mData.NumberOfUnits));
        //价格
        helper.setText(R.id.tv_price, Html.fromHtml("<font><small>¥</small>" + mData.InclusiveAmount + "</font>"));
        //益豆
        TextView tv_has_rate = helper.getView(R.id.tv_has_rate);
        tv_has_rate.setText(String.format("送%s", mData.ReturnBean));
        tv_has_rate.setTextColor(Color.parseColor("#FF4E0E"));
        tv_has_rate.setBackgroundResource(R.drawable.ctrip_order_bean_bg);
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_yidou_orange);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_has_rate.setCompoundDrawables(null, null, drawable, null);

        if (mData.status.equals(CtripOrderCommonFragment.OrderType_submit)) {
            /**未提交*/
            helper.setText(R.id.tv_status, "未提交");
            tvShare.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.VISIBLE);
            tvSubmit.setVisibility(View.VISIBLE);
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_process)) {
            /**确认中*/
            helper.setText(R.id.tv_status, "确认中");
            tvReminder.setVisibility(View.VISIBLE);
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_affirm)) {
            /**已确认*/
            tvShare.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_status, "已确认");
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_expense)) {
            /**已消费*/
            helper.setText(R.id.tv_status, "已消费");
            tvShare.setVisibility(View.VISIBLE);
        } else if (mData.status.equals(CtripOrderCommonFragment.OrderType_cancel)) {
            /**已取消*/
            helper.setText(R.id.tv_status, "已取消");
            //订单icon  置灰
            tvTitle.setTextColor(Color.parseColor("#666666"));
            //标题  置灰
            ivIcon.setImageResource(R.mipmap.ctrip_order_ic_gray);
            //益豆  置灰
            tv_has_rate.setTextColor(Color.parseColor("#999999"));
            tv_has_rate.setBackgroundResource(R.drawable.ctrip_order_bean_bg_grey);
            Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.ic_yidou_grey);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tv_has_rate.setCompoundDrawables(null, null, drawable1, null);
            tvShare.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.VISIBLE);
        }

    }


    private void initButton() {
        tvShare.setVisibility(View.GONE);
        tvDelete.setVisibility(View.GONE);
        tvReminder.setVisibility(View.GONE);
        tvSubmit.setVisibility(View.GONE);
    }
}
