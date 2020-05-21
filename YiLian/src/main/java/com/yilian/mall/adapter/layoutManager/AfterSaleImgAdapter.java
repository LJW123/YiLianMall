package com.yilian.mall.adapter.layoutManager;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseViewHolder;
import com.yilian.mall.adapter.SimpleAdapter;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/20.
 */
public class AfterSaleImgAdapter extends SimpleAdapter {

    BitmapUtils bm;

    public AfterSaleImgAdapter(Context context, ArrayList<String> list, BitmapUtils bm) {
        super(context, R.layout.item_after_sale_img,list);

        this.bm = bm;

    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Object item, int position) {
        CircleImageView1 iv = (CircleImageView1) viewHolder.getView(R.id.img);
        Logger.i(item.toString());
        GlideUtil.showImageNoSuffix(context,item.toString(),iv);
    }




}
