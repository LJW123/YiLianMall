package com.yilian.mall.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yilian.mall.R;
import com.yilian.mall.ui.ImagePagerActivity;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/5/8 0008.
 */
public class AlbumGridViewAdapter extends BaseListAdapter {

    public AlbumGridViewAdapter(ArrayList<String> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageHolder holder;
        if (null == view) {
            view = View.inflate(context, R.layout.item_merchant_photo_album, null);
            holder = new ImageHolder(view);
            view.setTag(holder);
        } else {
            holder = (ImageHolder) view.getTag();
        }

        String imageUrl = (String) datas.get(position);
        GlideUtil.showImage(context, imageUrl, holder.imageView);
        //点击放大
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, datas.toArray(new String[datas.size()]));
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return view;
    }

    class ImageHolder {
        ImageView imageView;

        public ImageHolder(View view) {
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
