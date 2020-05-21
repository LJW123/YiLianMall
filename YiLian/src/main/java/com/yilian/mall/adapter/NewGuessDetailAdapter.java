package com.yilian.mall.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.GuessInfoEntity;
import com.yilianmall.merchant.utils.DateUtils;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/13 0013
 * 大家猜列表的adapter
 */

public class NewGuessDetailAdapter extends BaseQuickAdapter<GuessInfoEntity.InfoBean.GuessNumberBean, BaseViewHolder> {
    public String isParticipate;

    public NewGuessDetailAdapter(@LayoutRes int layoutResId, String isParticipate) {
        super(layoutResId);
        this.isParticipate = isParticipate;
    }

    @Override
    protected void convert(BaseViewHolder helper, GuessInfoEntity.InfoBean.GuessNumberBean item) {
        TextView tvPrizeNum = helper.getView(R.id.tv_prize_num);
        TextView tvGuessNum = helper.getView(R.id.tv_guess_num);
        TextView tvDesc1 = helper.getView(R.id.tv_desc1);
        TextView tvDesc2 = helper.getView(R.id.tv_desc2);

        switch (isParticipate) {
            case "1":
                tvPrizeNum.setText(item.guess_number);
                tvGuessNum.setVisibility(View.GONE);
                tvDesc1.setText("我猜过的数字");
                tvDesc2.setText(DateUtils.timeStampToStr(Long.parseLong(item.apply_at)));
                tvDesc2.setTextSize(12);
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_guess_time);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc2.setCompoundDrawables(drawable, null, null, null);
                break;
            case "2":
                tvPrizeNum.setText(item.guess_number);
                tvGuessNum.setText(item.number_count + "次");
                tvGuessNum.setVisibility(View.VISIBLE);
                tvDesc1.setText("被猜过的数字");
                tvDesc2.setText("被猜过的次数");
                tvDesc2.setTextSize(15);
                tvDesc2.setCompoundDrawables(null, null, null, null);
                break;
        }

    }
}
