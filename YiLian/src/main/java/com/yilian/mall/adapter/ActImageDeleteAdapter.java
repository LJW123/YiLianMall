package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/20.
 */
public class ActImageDeleteAdapter extends SimpleAdapter {

    private int flag = 0;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    ArrayList<String> list;
    ArrayList<String> list1;
    private String imageUrl;

    public ActImageDeleteAdapter(Context context, ArrayList<String> list1, ArrayList<String> list) {
        super(context, R.layout.library_module_item_delete_photo_jp, list1);

        this.list = list;
        this.list1 = list1;
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
                .showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
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
            iv.setImageResource(R.mipmap.act_photo_icon);
        } else {
            viewHolder.getImageView(R.id.iv_del).setVisibility(View.VISIBLE);
            Logger.i("图片地址是1：" + item.toString());
            if (!TextUtils.isEmpty(item.toString())) {
                if (!item.toString().contains("http://")||!item.toString().contains("https://")) {
                    imageUrl = Constants.ImageUrl + item.toString() + Constants.ImageSuffix;
                }
                showImage(iv);
            }

            viewHolder.getImageView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(item);
                    list.remove(position);
                    if (list1.size() < 5 && !list1.get(list1.size() - 1).equals("add")) {
                        list1.add("add");
                    }
                }
            });
        }

    }

    private void showImage(final ImageView iv) {
        imageLoader.displayImage(imageUrl, iv, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                //Toast.makeText(mContext, "图片开始上传", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "图片加载错误1";
                        break;
                    case DECODING_ERROR:
                        message = "图片加载错误2";
                        break;
                    case NETWORK_DENIED:
                        message = "图片加载错误3";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片加载错误4";
                        break;
                    case UNKNOWN:
                        message = "图片加载错误5";

                        //Toast.makeText(mContext, "图片上传失败", Toast.LENGTH_SHORT).show();
                        flag++;
                        if (flag <= 5) {
                            showImage(iv);
                        }
                        break;
                    default:
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
}



