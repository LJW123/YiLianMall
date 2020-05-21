package com.yilian.mall.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.MTMerchantCommendEntity;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/8 0008.
 *  商家推荐的
 */
public class MerchantRecommendAdapter extends BaseListAdapter<MTMerchantCommendEntity.MerchantListBean> {

    public MerchantRecommendAdapter(List<MTMerchantCommendEntity.MerchantListBean> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = View.inflate(context, R.layout.item_merchant, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        MTMerchantCommendEntity.MerchantListBean merchantListBean = datas.get(position);
        if (merchantListBean.merchantSortTime > 0) {
            holder.tvRecommend.setVisibility(View.VISIBLE);
        } else {
            holder.tvRecommend.setVisibility(View.GONE);
        }
        imageLoader.displayImage(Constants.ImageUrl+merchantListBean.merchantImage+ Constants.ImageSuffix, holder.merchantIvPrize, options);
        holder.merchantName.setText(merchantListBean.merchantName);
        float graded = Float.parseFloat(merchantListBean.evaluate)/10;
        if (0 == graded){
            holder.RatingBar.setRating(5.0f);
        }else{
            holder.RatingBar.setRating(graded);
        }
//        holder.graded.setText(NumberFormat.convertToFloat(merchantListBean.evaluate, 0) / 10 + "");
        holder.merchantAddress.setText(merchantListBean.merchantAddress);
        if (!TextUtils.isEmpty(merchantListBean.renqi)) {
            holder.tvPresence.setText(merchantListBean.renqi + "人光临");
        }
        holder.merchantDistance.setText(TextUtils.isEmpty(merchantListBean.formatServiceMerDistance) ? "计算距离失败" : merchantListBean.formatServiceMerDistance);

        if (!TextUtils.isEmpty(merchantListBean.merDiscount)){
            holder.tvPrivilege.setVisibility(View.VISIBLE);
            holder.tvPrivilege.setText(Html.fromHtml(merchantListBean.merDiscount+"<font><small><small>%</small></small></font"));
        }else{
            holder.tvPrivilege.setVisibility(View.GONE);
        }

        if ("1".equals(merchantListBean.isReserve)) {
            if ("1".equals(merchantListBean.isDelivery)) {
                holder.tvIdentityOut.setVisibility(View.VISIBLE);
                holder.tvIdentityCombo.setVisibility(View.GONE);
            } else {
                holder.tvIdentityOut.setVisibility(View.GONE);
                holder.tvIdentityCombo.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tvIdentityOut.setVisibility(View.GONE);
            holder.tvIdentityCombo.setVisibility(View.GONE);
        }

        return view;
    }


    private static class ViewHolder {
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
        android.widget.RatingBar RatingBar;
        TextView graded;

        /**
         * 商家光临次数
         */
        TextView tvPresence;
        /**
         * 是否支持在线预订
         */
        TextView tvSustainReserve;
        /**
         * 让利标签
         */
        TextView tvPrivilege;

        TextView tvIdentityOut;
        TextView tvIdentityCombo;
        private final TextView tvRecommend;

        public ViewHolder(View view) {
            tvRecommend = (TextView) view.findViewById(R.id.tv_recommend);
            this.merchantIvPrize = (ImageView) view.findViewById(R.id.merchant_iv_prize);
            this.merchantName = (TextView) view.findViewById(R.id.merchant_name);
            this.merchantAddress = (TextView) view.findViewById(R.id.merchant_address);
            this.merchantDistance = (TextView) view.findViewById(R.id.merchant_distance);
            this.RatingBar = (RatingBar) view.findViewById(R.id.merchant_ratingBar);
            this.tvPresence = (TextView) view.findViewById(R.id.tv_presence_count);
            this.graded = (TextView) view.findViewById(R.id.tv_graded);
            this.tvSustainReserve = (TextView) view.findViewById(R.id.tv_sustain_reserve);
            this.tvPrivilege = (TextView) view.findViewById(R.id.tv_privilege);
            this.tvIdentityOut = (TextView) view.findViewById(R.id.tv_identity_out);
            this.tvIdentityCombo = (TextView) view.findViewById(R.id.tv_identity_combo);
        }
    }
}
