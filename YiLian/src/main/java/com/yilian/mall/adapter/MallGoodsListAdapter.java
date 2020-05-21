package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallGoodsList;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class MallGoodsListAdapter extends BaseAdapter {
    private List<MallGoodsList> mList;
    private Context context;
    BitmapUtils bitmapUtils;

    public MallGoodsListAdapter(Context context,List<MallGoodsList> mList) {
        this.context = context;
        this.mList = mList;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_mall_goods_item, null);
            holder.ivGoodsIcon = (ImageView) convertView.findViewById(R.id.iv_goods_icon);
            holder.tvGoosName = (TextView) convertView.findViewById(R.id.tv_goos_name);
            holder.rbGoodsGrade = (RatingBar) convertView.findViewById(R.id.rb_goods_grade);
            holder.tvGoodsGrade = (TextView) convertView.findViewById(R.id.tv_goods_grade);
            holder.tvGoodsIntegralPrice = (TextView) convertView.findViewById(R.id.tv_goods_integral_price);
            holder.tvGoodsViewedCount = (TextView) convertView.findViewById(R.id.tv_goods_viewed_count);
            holder.tvGoodsSalesVolume = (TextView) convertView.findViewById(R.id.tv_goods_sales_volume);
            holder.llFillPrice=(LinearLayout) convertView.findViewById(R.id.ll_fill_price);
            holder.tvFillPrice=(TextView) convertView.findViewById(R.id.tv_goods_fill_price);
            holder.tvTicket=(TextView) convertView.findViewById(R.id.tv_ticket);
            holder.tvIntegralType=(TextView) convertView.findViewById(R.id.tv_integral_type);
            holder.first = (LinearLayout) convertView.findViewById(R.id.first);
            holder.second = (LinearLayout) convertView.findViewById(R.id.second);
            holder.third = (LinearLayout) convertView.findViewById(R.id.third);
            holder.fifth = (LinearLayout) convertView.findViewById(R.id.fifth);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.mList != null) {
            MallGoodsList good=mList.get(position);
            if (holder.ivGoodsIcon != null) {
                bitmapUtils.display(holder.ivGoodsIcon,Constants.ImageUrl + good.goods_icon+ Constants.ImageSuffix);

            }
            if (holder.tvGoosName != null) {
                holder.tvGoosName.setText(good.goodsName);
            }
            if (holder.rbGoodsGrade != null) {
                holder.rbGoodsGrade.setRating(Float.parseFloat(good.goodsGrade)/10);
            }
            if (holder.tvGoodsGrade != null) {
                holder.tvGoodsGrade.setText(NumberFormat.convertToFloat(good.goodsGrade,5f)/10.0 + "分");
            }
            if (holder.tvGoodsViewedCount != null) {
                holder.tvGoodsViewedCount.setText(good.goodsViewedCount + "人已浏览");
            }
            if (holder.tvGoodsSalesVolume != null) {
                holder.tvGoodsSalesVolume.setText(good.goodsSalesVolume);
            }

            switch (good.goodType) {
                case 0:
                case 1:
                    holder.llFillPrice.setVisibility(View.GONE);
                    holder.tvTicket.setVisibility(View.VISIBLE);

                    holder.first.setPadding(0,CommonUtils.dip2px(context, 3),0,0);
                    holder.second.setPadding(0,CommonUtils.dip2px(context, 4),0,0);
                    holder.third.setPadding(0, CommonUtils.dip2px(context, 5), 0, 0);
                    holder.fifth.setPadding(0, CommonUtils.dip2px(context, 4), 0, 0);

                    holder.tvIntegralType.setText("建议乐换价");
                    holder.tvGoodsIntegralPrice.setText(good.goodsIntegralPrice);
                    break;
                case 2:
                    holder.llFillPrice.setVisibility(View.VISIBLE);
                    holder.tvTicket.setVisibility(View.VISIBLE);

                    holder.first.setPadding(0,0,0,0);
                    holder.second.setPadding(0,0,0,0);
                    holder.third.setPadding(0, 0, 0, 0);
                    holder.fifth.setPadding(0, 0, 0, 0);

                    holder.tvIntegralType.setText("建议乐选价");
                    holder.tvGoodsIntegralPrice.setText(good.goodsIntegralPrice);
                    if (good.integralMoney!=null&&good.integralMoney.length()>0) {
                        holder.tvFillPrice.setText("¥  "+String.format("%.2f", Float.parseFloat(good.integralMoney)/100.00f));
                    }
                    break;
                case 3:
                    holder.llFillPrice.setVisibility(View.GONE);
                    holder.tvTicket.setVisibility(View.GONE);

                    holder.first.setPadding(0,CommonUtils.dip2px(context, 3),0,0);
                    holder.second.setPadding(0,CommonUtils.dip2px(context, 4),0,0);
                    holder.third.setPadding(0, CommonUtils.dip2px(context, 5), 0, 0);
                    holder.fifth.setPadding(0, CommonUtils.dip2px(context, 4), 0, 0);

                    holder.tvIntegralType.setText("乐购价");
                    holder.tvGoodsIntegralPrice.setText("¥"+String.format("%.2f", Float.parseFloat(good.marketPrice)/100.00f));
                    break;

                default:
                    break;
            }
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ivGoodsIcon;
        TextView tvGoosName;
        RatingBar rbGoodsGrade;
        TextView tvGoodsGrade;
        TextView tvGoodsIntegralPrice;
        TextView tvGoodsViewedCount;
        TextView tvGoodsSalesVolume;
        LinearLayout llFillPrice;
        TextView tvIntegralType;
        TextView tvFillPrice;
        TextView tvTicket;
        LinearLayout first;
        LinearLayout second;
        LinearLayout third;
        LinearLayout fifth;
    }
}
