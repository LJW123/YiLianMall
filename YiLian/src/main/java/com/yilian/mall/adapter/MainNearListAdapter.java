package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.yilian.mall.R;
import com.yilian.mall.entity.MerchantList;
import com.yilian.mall.image.RoundImageDrawable;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * 主页附近商家适配器
 * Created by Administrator on 2016/8/1.
 */
public class MainNearListAdapter extends android.widget.BaseAdapter {
    private List<MerchantList> mList;
    private Context context;

    public MainNearListAdapter(Context context, List<MerchantList> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_merchant, null);
            holder.merchantIvPrize = (ImageView) convertView.findViewById(R.id.merchant_iv_prize);
            holder.merchantName = (TextView) convertView.findViewById(R.id.merchant_name);
            holder.RatingBar = (RatingBar) convertView.findViewById(R.id.merchant_ratingBar);
            holder.tvPraiseCount = (TextView) convertView.findViewById(R.id.tv_presence_count);
            holder.graded = (TextView) convertView.findViewById(R.id.tv_graded);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (this.mList != null) {
            bitmapUtils.display(holder.merchantIvPrize,
                    Constants.ImageUrl + mList.get(position).merchantImage + Constants.ImageSuffix,
                    new BitmapLoadCallBack<ImageView>() {
                        @Override
                        public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2,
                                                    BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
                            arg0.setImageDrawable(new RoundImageDrawable(arg2));
                        }

                        @Override
                        public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {

                        }
                    });
            holder.merchantName.setText(mList.get(position).merchantName);
            holder.RatingBar.setRating(Float.parseFloat(mList.get(position).graded) / 10);
            holder.graded.setText(Float.valueOf(mList.get(position).graded)/10.0 + "分");

            if (TextUtils.isEmpty(mList.get(position).praiseCount)) {
                holder.tvPraiseCount.setText("被赞 0 次");
            } else {
                holder.tvPraiseCount.setText("被赞 " + mList.get(position).praiseCount + " 次");
            }


        }
        return convertView;
    }
    class ViewHolder {
        /**
         * 商家图片
         */
        ImageView merchantIvPrize;
        /**
         * 商家名字
         */
        TextView merchantName;
        /**
         * 商家地址
         */
        TextView merchantAddress;
        /**
         * 商家距离
         */
        TextView merchantDistance;

        /**
         * 商家评分
         */
        RatingBar RatingBar;
        TextView graded;

        /**
         * 商家点赞数量
         */
        TextView tvPraiseCount;

    }
}




