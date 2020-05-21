package com.yilianmall.merchant.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.yilian.networkingmodule.entity.SendPackgeRecorder;
import com.yilian.networkingmodule.entity.SendRedStealedDetails;
import com.yilian.networkingmodule.entity.SendRedTotals;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zyh
 */

public class StealedPackedDetailsAdapter extends BaseQuickAdapter<SendRedStealedDetails.Data.Details, BaseViewHolder> {
    public boolean hasHead;

    public StealedPackedDetailsAdapter(@LayoutRes int layoutResId, boolean hasHead) {
        super(layoutResId);
        this.hasHead = hasHead;
    }

    @Override
    protected void convert(BaseViewHolder helper, SendRedStealedDetails.Data.Details item) {
        setData(helper, item);
        TextView merchantCount = (TextView) helper.getView(R.id.merchant_amonut);
        if (item.lastAmount.contains("-")) {
            merchantCount.setTextColor(mContext.getResources().getColor(R.color.merchant_color_status_redpackget));
        } else {
            merchantCount.setTextColor(mContext.getResources().getColor(R.color.merchant_color_999));
        }
        merchantCount.setText(item.lastAmount);

    }

    private void setData(BaseViewHolder helpe, SendRedStealedDetails.Data.Details item) {
        String time= DateUtils.timeStampToStr5(Long.parseLong(item.applyAt));
        helpe.setText(R.id.merchant_time,time);
    }
}
