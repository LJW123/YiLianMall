package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.ComboListEntity;
import com.yilian.mall.ui.MTComboDetailsActivity;
import com.yilian.mall.ui.MTMerchantDetailActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/7 0007.
 * 套餐列表的adapter
 */
public class MtComboListAdapter extends android.widget.BaseAdapter{
    private final List<ComboListEntity.DataBean> comboList;
    private final Context mContext;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private int finalPosition;

    public MtComboListAdapter(List<ComboListEntity.DataBean> copyComboList, Context mContext, DisplayImageOptions options, ImageLoader imageLoader) {
        this.comboList=copyComboList;
        Logger.i("传递至Adapter的数据"+comboList.toString());
        this.mContext =mContext;
        this.imageLoader =imageLoader;
        this.options =options;
    }

    @Override
    public int getCount() {
        if (comboList.size()>0){
            return comboList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return comboList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //内部是lsitView嵌套
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ParentHolder holder;
        if (view==null){
            view=View.inflate(mContext, R.layout.combo_lv_item,null);
            holder=new ParentHolder();
            holder.comboLayout= (RelativeLayout) view.findViewById(R.id.rl_combo_title);
            holder.merchantName= (TextView) view.findViewById(R.id.tv_merchant_title);
            holder.merchantRb= (RatingBar) view.findViewById(R.id.merchant_ratingBar);
            holder.ratingCount= (TextView) view.findViewById(R.id.tv_rating_count);
            holder.discount= (TextView) view.findViewById(R.id.tv_discount);
            holder.listView= (NoScrollListView) view.findViewById(R.id.lv_combo_item);
            holder.takeOut= (TextView) view.findViewById(R.id.tv_take_out);
            holder.lookMore= (TextView) view.findViewById(R.id.tv_look_more);
            view.setTag(holder);
        }else{
            holder= (ParentHolder) view.getTag();
        }
        //服务器返回的是一个对象商家信息是直接的属性套餐信息才是list
        finalPosition = position;
        final ComboListEntity.DataBean dataBean = comboList.get(position);
        Logger.i("现实头布局的信息"+"dataBean"+dataBean.toString()+"</br> dataBean.merchantName"+dataBean.merchantName);
        holder.merchantName.setText(dataBean.merchantName+"("+dataBean.merchantAddress+")");
        //计算点赞数量
        if (dataBean.evaluate.equals("0")){
            holder.merchantRb.setRating(5.0f);
        }else{
            holder.merchantRb.setRating(Float.parseFloat(dataBean.evaluate)/10);
        }
        holder.ratingCount.setText((Float.parseFloat(dataBean.evaluate)/10+"分"));
        if (dataBean.isDelivery.equals("1"))
        {
            holder.takeOut.setVisibility(View.VISIBLE);
        }else{
            holder.takeOut.setVisibility(View.GONE);
        }
        //计算距离

        //计算距离

        if (TextUtils.isEmpty(dataBean.distance)){
            holder.discount.setText("计算距离失败");
        }else{
            holder.discount.setText(dataBean.distance);
        }
        if(dataBean.packages!=null&&dataBean.packages.size()>=2){
            holder.lookMore.setVisibility(View.VISIBLE);
        }

        holder.comboLayout.setOnClickListener(new View.OnClickListener() {//跳转商家详情
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(mContext, MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id", comboList.get(position).merchantId);
                mContext.startActivity(intent);
            }
        });
        holder.lookMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(mContext, MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id", comboList.get(position).merchantId);
                mContext.startActivity(intent);
            }
        });
        holder.listView.setAdapter(new ListItemAdapter(dataBean.packages));

        //点击不同的条目跳转不同的套餐详情
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent(mContext, MTComboDetailsActivity.class);
                intent.putExtra("package_id",dataBean.packages.get(position).packageId);
                intent.putExtra("merchant_id",dataBean.merchantId);
                mContext.startActivity(intent);
            }
        });

        return view;
    }


    class ParentHolder{
        RelativeLayout comboLayout;
        TextView merchantName;
        RatingBar merchantRb;
        TextView ratingCount;
        TextView takeOut;//外卖
        TextView discount;
        NoScrollListView listView;
        TextView lookMore;
    }


    class ListItemAdapter extends android.widget.BaseAdapter{

        private final List<ComboListEntity.DataBean.PackagesBean> comboDetailsList;

        public ListItemAdapter(List<ComboListEntity.DataBean.PackagesBean> packages) {
            this.comboDetailsList =packages;
        }

        @Override
        public int getCount() {
            if (comboDetailsList.size()>0){
                if(comboDetailsList.size()<=2){
                    return comboDetailsList.size();
                }else{
                    return 2;
                }
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return comboDetailsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int positon, View view, ViewGroup viewGroup) {
            ItemHolder holder;
            if (view==null){
                view=View.inflate(mContext,R.layout.mt_item_merchant_body,null);
                holder=new ItemHolder();
                holder.comboIcon= (ImageView) view.findViewById(R.id.iv_merchant_icon);
                holder.comboName= (TextView) view.findViewById(R.id.tv_merchant_name);
                holder.comboPrice= (TextView) view.findViewById(R.id.tv_merchant_price);
                holder.sellCount= (TextView) view.findViewById(R.id.tv_sell_count);
                view.setTag(holder);
            }else{
                holder= (ItemHolder) view.getTag();
            }
            ComboListEntity.DataBean.PackagesBean comboBean = comboDetailsList.get(positon);
             holder.comboName.setText(comboBean.name);
            imageLoader.displayImage(Constants.ImageUrl+comboBean.packageIcon+ Constants.ImageSuffix,holder.comboIcon,options);
//            Logger.i("套餐价格"+comboBean.price);
            holder.comboPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(comboBean.price)));
            holder.sellCount.setText("已售出:"+comboBean.sellCount);
//            Logger.i("已售出"+comboBean.sellCount);//null


            return view;
        }
    }

    class ItemHolder{
        ImageView comboIcon;
        TextView  comboName;
        TextView  comboPrice;
        TextView  sellCount;
    }

}
