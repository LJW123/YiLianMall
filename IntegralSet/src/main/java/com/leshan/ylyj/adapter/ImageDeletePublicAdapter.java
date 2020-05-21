package com.leshan.ylyj.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.leshan.ylyj.testfor.R;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/20.
 */
public class ImageDeletePublicAdapter extends SimpleAdapter {

    BitmapUtils bm;
    ArrayList<String> list;
    ArrayList<String> list1;
    private int flag = 0;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private String imageUrl;
    private DelImageListener delImageListener;

    public ImageDeletePublicAdapter(Context context, ArrayList<String> list1, ArrayList<String> list, BitmapUtils bm) {
        super(context, R.layout.library_module_item_delete_photo_integral, list1);

        this.bm = bm;
        this.list = list;
        this.list1 = list1;
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.bind_card_photo)
                .showImageForEmptyUri(R.mipmap.bind_card_photo).showImageOnFail(R.mipmap.bind_card_photo)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Object item, final int position) {
        ImageView iv = (ImageView) viewHolder.getView(R.id.iv);

        Logger.i("adapter" + String.valueOf(item));
        if (item.equals("add")) {
            viewHolder.getImageView(R.id.iv_del).setVisibility(View.GONE);
            iv.setImageResource(R.mipmap.bind_card_photo);
        } else {
            viewHolder.getImageView(R.id.iv_del).setVisibility(View.VISIBLE);
            Logger.i("图片地址是1：" + item.toString());
            if (!TextUtils.isEmpty(item.toString())) {
                if (!item.toString().contains("http://") || !item.toString().contains("https://")) {
                    imageUrl = Constants.ImageUrl + item.toString() + Constants.ImageSuffix;
                }
                showImage(iv);
            }

            viewHolder.getImageView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(item);
                    list.remove(position);
                    list1.add("add");
                    if (null != delImageListener) {
                        delImageListener.onDelImg();
                    }
                }
            });
        }
    }

    private void showImage(final ImageView iv) {
        imageLoader.displayImage(imageUrl, iv, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "图片加载错误1";
                    case DECODING_ERROR:
                        message = "图片加载错误2";
                    case NETWORK_DENIED:
                        message = "图片加载错误3";
                    case OUT_OF_MEMORY:
                        message = "图片加载错误4";
                    case UNKNOWN:
                        message = "图片加载错误5";

                        flag++;
                        if (flag <= 2) {
                            showImage(iv);
                        }
                        break;
                }
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
    }

    /**
     * 设置删除图片监听
     *
     * @param delImgListener
     */
    public void setDelImgListener(DelImageListener delImgListener) {
        this.delImageListener = delImgListener;
    }

    public interface DelImageListener {
        /**
         * 删除图片回调
         */
        void onDelImg();
    }
}



