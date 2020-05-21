package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallMenu;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by yukun on 2016/4/22.
 */
public class MallMenuAdapter extends SimpleAdapter<MallMenu> {

    BitmapUtils bitmapUtils;
    ImageView ivMenu;
    public MallMenuAdapter(Context context, List<MallMenu> datas) {
        super(context, R.layout.item_mall_goods_menu, datas);
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MallMenu item, int position) {

        viewHolder.getTextView(R.id.tv_menu_name).setText(item.menuName);
        viewHolder.getTextView(R.id.tv_menu_describe).setText(item.menuDescribe);

        ivMenu = viewHolder.getImageView(R.id.iv_menu);
        if (TextUtils.isEmpty(item.photoUrl)) {
            ivMenu.setImageResource(item.resId);
        } else {
            bitmapUtils.display(ivMenu, Constants.ImageUrl + item.photoUrl + Constants.ImageSuffix, null, new BitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                    imageView.setBackgroundResource(R.mipmap.login_module_default_jp);
                }
            });
        }

    }
}
