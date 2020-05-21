package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.yilian.mall.R;
import com.yilian.mall.entity.imentity.IMGroupMembersInfoEntity;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/29.
 */
public class IMGroupMembersInforAdapter extends SimpleAdapter<IMGroupMembersInfoEntity.DataBean> {
    private Context context;
    private ArrayList<IMGroupMembersInfoEntity.DataBean> list = new ArrayList<>();
    //    BitmapUtils map ;
    private String url;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    public IMGroupMembersInforAdapter(Context context, ArrayList<IMGroupMembersInfoEntity.DataBean> list) {
        super(context, R.layout.im_item_chat_details_members, list);
        this.context = context;
        this.list = list;
//        map = new BitmapUtils(mContext);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, IMGroupMembersInfoEntity.DataBean item, int position) {
        viewHolder.getTextView(R.id.tv_name).setText(TextUtils.isEmpty(item.name)?"暂无昵称":item.name);
        viewHolder.getTextView(R.id.tv_time).setVisibility(View.GONE);
        if (!TextUtils.isEmpty(item.photo)){
            if (item.photo.contains("http://")||item.photo.contains("https://")) {
                url = item.photo;
            } else {
                url = Constants.ImageUrl + item.photo;
            }

            imageLoader.displayImage(url, viewHolder.getImageView(R.id.img_photo), options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            viewHolder.getImageView(R.id.img_photo).setImageBitmap(loadedImage);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                case DECODING_ERROR:
                                case NETWORK_DENIED:
                                case OUT_OF_MEMORY:
                                case UNKNOWN:
                                    message = "图片加载错误";
                                    break;
                            }
                        }
                    });
        }

    }
}
