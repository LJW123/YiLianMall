package com.yilian.mylibrary.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.R;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/20.
 */
public class ImageDeleteAdapter extends SimpleAdapter {

    private final RefreshImageList refreshImageList;
    private int flag = 0;
    ArrayList<String> list;
    ArrayList<String> list1;
    private String imageUrl;

    public ImageDeleteAdapter(Context context, ArrayList<String> list1, ArrayList<String> list, RefreshImageList refreshImageList) {
        super(context, R.layout.library_module_item_delete_photo_jp, list1);
        this.list1 = list1;
        this.list = list;
        this.refreshImageList = refreshImageList;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Object item, final int position) {
        ImageView iv = (ImageView) viewHolder.getView(R.id.iv);
        viewHolder.getView(R.id.tv_text_info).setVisibility(View.VISIBLE);//环境照片标签显示
        Logger.i("adapter" + String.valueOf(item));
        if (item.equals("add")) {
            viewHolder.getImageView(R.id.iv_del).setVisibility(View.GONE);
            iv.setImageResource(R.mipmap.library_module_ic_after_sale_img);
        } else {
            viewHolder.getImageView(R.id.iv_del).setVisibility(View.VISIBLE);
            Logger.i("图片地址是1：" + item.toString());
            if (!TextUtils.isEmpty(item.toString())) {
                if (!item.toString().contains("http://") || !item.toString().contains("https://")) {
                    imageUrl = Constants.ImageUrl + item.toString() + Constants.ImageSuffix;
                }
                GlideUtil.showImage(context, imageUrl, iv);
            }
            viewHolder.getImageView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(item);
                    ImageDeleteAdapter.this.notifyItemRangeChanged(0, list1.size());
                    list.remove(position);
                    refreshImageList.refreshImageList(list);
                    if (list1.size() < 3 && !list1.get(list1.size() - 1).equals("add"))
                        list1.add("add");
                }
            });
        }

    }

    public interface RefreshImageList {//删除图片时，刷新上传的图片集合

        void refreshImageList(ArrayList<String> list);
    }
}



