package com.yilian.mall.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ui.NewGuessListActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.ParadiseEntity;

/**
 * Created by Ray_L_Pain on 2017/10/23 0023.
 */

public class ParadiseAdapter extends BaseQuickAdapter<ParadiseEntity.DataBean.ListBean, com.chad.library.adapter.base.BaseViewHolder> {
    protected int screenW, width, height, margin;

    public ParadiseAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ParadiseEntity.DataBean.ListBean item) {
        screenW = (ScreenUtils.getScreenWidth(mContext));
        if (screenW == 1080) {
            margin = 20;
        } else if (screenW == 720) {
            margin = 10;
        } else if (screenW == 1440) {
            margin = 27;
        } else {
            margin = 20;
        }
        width = screenW - margin * 2;
        height = (int) MyBigDecimal.div(MyBigDecimal.mul(width, 412), 1002, 0);

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, item.pic, iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("okhttp-" + item.name);
                Logger.i("okhttp-" + item.urls);
                Intent intent = null;
                switch (item.type) {
                    case "0":
                        intent = new Intent(mContext, NewGuessListActivity.class);
                        break;
                    case "1":
                        intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("url", item.urls);
                        intent.putExtra(Constants.WEBVIEW_ICON_EXPLAIN, "VISIBLE");
                        break;
                    default:
                        break;
                }
                mContext.startActivity(intent);
            }
        });
    }
}
