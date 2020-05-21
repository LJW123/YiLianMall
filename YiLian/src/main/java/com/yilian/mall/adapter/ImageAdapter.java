package com.yilian.mall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.BitmapUtils;
import com.yilian.mall.R;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/20.
 */
public class ImageAdapter extends SimpleAdapter {
    Context context;
    BitmapUtils bm;
    ArrayList<String> list;
    String imgUrl;

    public ImageAdapter(Context context, ArrayList<String> list,BitmapUtils bm) {
        super(context, R.layout.item_evaluate_photo,list);
        this.context = context;
        this.bm = bm;
        this.list = list;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Object item, int position) {
        CircleImageView1 iv = (CircleImageView1) viewHolder.getView(R.id.iv);
        imgUrl = list.get(position).toString();
        if (!TextUtils.isEmpty(list.get(position).toString()))
            bm.display(iv, Constants.ImageUrl + imgUrl + Constants.ImageSuffix);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击了图片"+imgUrl, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
