package com.yilian.mall.adapter;/**
 * Created by  on 2017/3/23 0023.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.ShopsEntity;
import com.yilian.mall.ui.MTMerchantDetailActivity;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by  on 2017/3/23 0023.
 */
public class MTHomePageShopsAdapter extends RecyclerView.Adapter<MTHomePageShopsAdapter.MTHomePageShopsViewHolder> {
    private final Context mContext;
    private final List<ShopsEntity.MerchantListBean> list;

    public MTHomePageShopsAdapter(Context context, List<ShopsEntity.MerchantListBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public MTHomePageShopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_merchant, parent, false);
        return new MTHomePageShopsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MTHomePageShopsViewHolder holder, int position) {
        Logger.i("检测列表性能：商家holder: position："+position+" "+holder.hashCode());
        ShopsEntity.MerchantListBean merchantListBean = list.get(position);
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(merchantListBean.merchantImage),holder.circleImageView1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id",merchantListBean.merchantId);
                mContext.startActivity(intent);
            }
        });
        holder.tvMerchantName.setText(merchantListBean.merchantName);
        holder.tvMerchantAddress.setText(merchantListBean.merchantAddress);

        if (null==merchantListBean.graded||TextUtils.isEmpty(String.valueOf(merchantListBean.graded))){
            holder.merchantRatingBar.setRating(5.0f);
        }else{
            holder.merchantRatingBar.setRating(Float.parseFloat(merchantListBean.graded)/10);
//            holder.tvGraded.setText(String.valueOf(Float.parseFloat(merchantListBean.graded)/10));
        }
        holder.tvPresenceCount.setText(merchantListBean.renqi+"人光临");
        holder.tvMerchantDistance.setText(TextUtils.isEmpty(merchantListBean.formatServiceMerDistance) ? "计算距离失败" : merchantListBean.formatServiceMerDistance);
//        if ("1".equals(merchantListBean.isReserve)) {
//            holder.tvSustainReserve.setVisibility(View.VISIBLE);
//        } else {
//            holder.tvSustainReserve.setVisibility(View.GONE);
//        }
        if (!TextUtils.isEmpty(merchantListBean.merDiscount)){
            holder.tvPervilege.setVisibility(View.VISIBLE);
            holder.tvPervilege.setText(Html.fromHtml(merchantListBean.merDiscount+"<font><small><small>%</small></small></font>"));
        }else{
            holder.tvPervilege.setVisibility(View.GONE);
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MTHomePageShopsViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private final ImageView circleImageView1;
        private final TextView tvSustainReserve;
        private final TextView tvMerchantName;
        private final RatingBar merchantRatingBar;
        private final TextView tvGraded;
        private final TextView tvPresenceCount;
        private final TextView tvMerchantAddress;
        private final TextView tvMerchantDistance;
        private final LinearLayout llContent;
        private final TextView tvPervilege;
        TextView tvIdentityOut;
        TextView tvIdentityCombo;

        public MTHomePageShopsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            circleImageView1 = (ImageView) itemView.findViewById(R.id.merchant_iv_prize);//图片
            tvSustainReserve = (TextView) itemView.findViewById(R.id.tv_sustain_reserve);//是否支持在线预定标签
            tvMerchantName = (TextView) itemView.findViewById(R.id.merchant_name);//商家名称
            merchantRatingBar = (RatingBar) itemView.findViewById(R.id.merchant_ratingBar);//商家星星个数
            tvGraded = (TextView) itemView.findViewById(R.id.tv_graded);//商家分数
            tvPresenceCount = (TextView) itemView.findViewById(R.id.tv_presence_count);//光临人数
            tvMerchantAddress = (TextView) itemView.findViewById(R.id.merchant_address);//商家地址
            tvMerchantDistance = (TextView) itemView.findViewById(R.id.merchant_distance);//商家距离
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            tvPervilege= (TextView) itemView.findViewById(R.id.tv_privilege);
            tvIdentityOut = (TextView) itemView.findViewById(R.id.tv_identity_out);
            tvIdentityCombo = (TextView) itemView.findViewById(R.id.tv_identity_combo);
        }
    }
}
