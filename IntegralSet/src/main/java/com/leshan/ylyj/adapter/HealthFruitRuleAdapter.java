package com.leshan.ylyj.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;

import java.util.ArrayList;

import rxfamily.entity.MyHealthFruitBean;

/**
 *  健康果规则
 * @author Administrator
 * @date 2017/12/27 0027
 */

public class HealthFruitRuleAdapter extends BaseQuickAdapter<MyHealthFruitBean.DataBean.MsgBean, BaseViewHolder> {
    private ArrayList<MyHealthFruitBean.DataBean.MsgBean> list;

    public HealthFruitRuleAdapter(@LayoutRes int layoutResId, ArrayList<MyHealthFruitBean.DataBean.MsgBean> list) {
        super(layoutResId);
        this.list = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyHealthFruitBean.DataBean.MsgBean item) {
        LinearLayout itemLayout = helper.getView(R.id.item_layout);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemLayout.getLayoutParams();
        params.width = (ScreenUtils.getScreenWidth(mContext)) / list.size();

        View leftView = helper.getView(R.id.view_left);
        View leftView7 = helper.getView(R.id.view_left_7);
        View rightView = helper.getView(R.id.view_right);
        View rightView7 = helper.getView(R.id.view_right_7);
        int size = list.size();
        int postion = helper.getPosition();
        Logger.i("2018年1月29日 09:34:09-size-" + size);
        Logger.i("2018年1月29日 09:34:09-postion-" + postion);

        if (size <= 1) {
            leftView.setVisibility(View.VISIBLE);
            leftView7.setVisibility(View.GONE);
            rightView7.setVisibility(View.GONE);
            rightView.setVisibility(View.VISIBLE);
            Logger.i("2018年1月29日 09:34:09-size-走了这里1");
        } else {
            if (postion == 0) {
                Logger.i("2018年1月29日 09:34:09-size-走了这里2");
                leftView.setVisibility(View.VISIBLE);
                leftView7.setVisibility(View.GONE);
                rightView.setVisibility(View.GONE);
                rightView7.setVisibility(View.VISIBLE);
            } else if ((postion + 1) == size) {
                Logger.i("2018年1月29日 09:34:09-size-走了这里3");
                leftView.setVisibility(View.GONE);
                leftView7.setVisibility(View.VISIBLE);
                rightView.setVisibility(View.VISIBLE);
                rightView7.setVisibility(View.GONE);
            } else {
                Logger.i("2018年1月29日 09:34:09-size-走了这里4");
                leftView.setVisibility(View.GONE);
                leftView7.setVisibility(View.VISIBLE);
                rightView.setVisibility(View.GONE);
                rightView7.setVisibility(View.VISIBLE);
            }
        }

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showImage(mContext, item.ICON_URL, iv);

        helper.setText(R.id.tv_title, item.TITLE);

        helper.setText(R.id.tv_desc, item.SUB_TITLE);
    }
}
