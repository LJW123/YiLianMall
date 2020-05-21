package com.yilian.mall.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPLocalMTMerchantPackageEntity;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by  on 2016/12/6 0006.
 */

public class JPLocalMTPackageAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<JPLocalMTMerchantPackageEntity> list;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private JPLocalMTMerchantPackageEntity jpLocalMTMerchantPackageEntity;

    public JPLocalMTPackageAdapter(Context context, ArrayList<JPLocalMTMerchantPackageEntity> list, ImageLoader imageLoader, DisplayImageOptions options) {
        Logger.i("本地套餐入口2");
        this.context = context;
        this.list = list;
        this.imageLoader = imageLoader;
        this.options = options;
    }

    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < list.size()) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Logger.i("本地套餐入口3");
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_mt_local_package_entrance, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JPLocalMTMerchantPackageEntity jpLocalMTMerchantPackageEntity = null;
        viewHolder.rlMtPackageItem.setLayoutParams(new LinearLayout.LayoutParams((int) (ScreenUtils.getScreenWidth(context) / 3.5), (int) (ScreenUtils.getScreenWidth(context) / 4.6)));
        viewHolder.llMtPacakageItem.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int) (ScreenUtils.getScreenWidth(context) / 3.5)));
        if(position<list.size()){//前面的显示每个套餐对应数据
            jpLocalMTMerchantPackageEntity   =  list.get(position);
            setViewHolderData(viewHolder, jpLocalMTMerchantPackageEntity);
            viewHolder.tvMtPackagePrice.setVisibility(View.VISIBLE);
        }else{//最后一个显示更多套餐
            viewHolder.ivJpMtPackageIcon.setBackgroundResource(R.mipmap.more_local_package);
            viewHolder.tvMtPackageName.setText("更多精选套餐");
            viewHolder.tvMtPackagePrice.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void setViewHolderData(ViewHolder viewHolder, @Nullable JPLocalMTMerchantPackageEntity jpLocalMTMerchantPackageEntity) {
        Logger.i("本地套餐入口4");
        if(jpLocalMTMerchantPackageEntity!=null){
            viewHolder.tvMtPackageName.setText(jpLocalMTMerchantPackageEntity.name);//套餐名称
            String imgUrl = jpLocalMTMerchantPackageEntity.packageIcon;
            if (!imgUrl.contains("http://")||!imgUrl.contains("https://")){
                imgUrl = Constants.ImageUrl+imgUrl+ BitmapUtil.getBitmapSuffix(context,3);
            }
            imageLoader.displayImage(imgUrl,viewHolder.ivJpMtPackageIcon,options);//套餐图片
            viewHolder.tvMtPackagePrice.setVisibility(View.VISIBLE);
            viewHolder.tvMtPackagePrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(jpLocalMTMerchantPackageEntity.price)));//套餐价格
        }else{
            viewHolder.tvMtPackagePrice.setVisibility(View.GONE);
            viewHolder.tvMtPackageName.setText("更多精选套餐");
        }

    }

    public static class ViewHolder {
        private final LinearLayout llMtPacakageItem;
        public View rootView;
        public ImageView ivJpMtPackageIcon;
        public TextView tvMtPackagePrice;
        public RelativeLayout rlMtPackageItem;
        public TextView tvMtPackageName;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.ivJpMtPackageIcon = (ImageView) rootView.findViewById(R.id.iv_jp_mt_package_icon);
            this.tvMtPackagePrice = (TextView) rootView.findViewById(R.id.tv_mt_package_tag);
            this.rlMtPackageItem = (RelativeLayout) rootView.findViewById(R.id.rl_mt_package_item);
            this.tvMtPackageName = (TextView) rootView.findViewById(R.id.tv_mt_package_price);
            this.llMtPacakageItem = (LinearLayout)rootView.findViewById(R.id.ll_mt_package_item);
        }

    }
}
