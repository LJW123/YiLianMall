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
import com.yilian.mall.entity.MemberLevel1Entity;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



/**
 * Created by Administrator on 2016/3/29.
 */
public class MemberAdapter extends SimpleAdapter<MemberLevel1Entity.Member> {
    private Context context;
    private ArrayList<MemberLevel1Entity.Member> list = new ArrayList<>();
    //    BitmapUtils map ;
    private String url;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public MemberAdapter(Context context, ArrayList<MemberLevel1Entity.Member> list) {
        super(context, R.layout.item_member, list);
        this.context = context;
        this.list = list;
//        map = new BitmapUtils(mContext);


        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }
    @Override
    protected void convert(final BaseViewHolder viewHolder, MemberLevel1Entity.Member item, int position) {

        viewHolder.getTextView(R.id.tv_name).setText(item.memberName);
        String time0 = System.currentTimeMillis() + "";
        String curTIme0 = time0.substring(0, 10);
        long curTime = Long.parseLong(curTIme0);
        long regTime = item.regTime;
        long time = (curTime - regTime) * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String day = sdf.format(time);
        viewHolder.getTextView(R.id.tv_time).setText(day + "天前");

        if (!TextUtils.isEmpty(item.memberIcon)){
            if (item.memberIcon.contains("http://")||item.memberIcon.contains("https://")) {
                url = item.memberIcon;
            } else {
                url = Constants.ImageUrl + item.memberIcon;
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
