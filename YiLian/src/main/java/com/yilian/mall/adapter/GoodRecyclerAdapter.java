package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by  on 2016/11/18 0018.
 * 精品推荐商品列表的adapter
 */

public class GoodRecyclerAdapter extends RecyclerView.Adapter<GoodRecyclerAdapter.GoodViewHolder> {
    private final ArrayList<JPGoodsEntity> jpCategoryRecommendListFromSp;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private final Context mContext;
    private OnItemClickLitener mOnItemClickLitener;

    public GoodRecyclerAdapter(Context context, ArrayList<JPGoodsEntity> list, ImageLoader imageLoader, DisplayImageOptions options) {
        mContext = context;
        this.jpCategoryRecommendListFromSp = list;
        this.imageLoader = imageLoader;
        this.options = options;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_jp_good, parent, false);
        return new GoodViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final GoodViewHolder holder, int position) {
//            Logger.i(jpLevel1CategoryId+"：onBindViewHolder()");
        JPGoodsEntity jpGoodsEntity = jpCategoryRecommendListFromSp.get(position);
        String jpImageUrl = jpGoodsEntity.JPImageUrl;
        int imageWidth = ScreenUtils.getScreenWidth(mContext) / 2;
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlSetSuffix(jpGoodsEntity.JPImageUrl,String.valueOf(imageWidth/2),String.valueOf(imageWidth/2)),holder.ivGoods1);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageWidth, imageWidth);
        holder.ivGoods1.setLayoutParams(params);
        holder.tvGoodsName.setText(jpGoodsEntity.JPGoodsName);
        holder.tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.JPGoodsCost)));//售价
        holder.tvMarketPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.JPGoodsPrice)));//市场价
        holder.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//设置中划线
        holder.tvDiscount.setText(mContext.getString(R.string.back_score)+MoneyUtil.getLeXiangBiNoZero(jpGoodsEntity.returnIntegral));//返利
        holder.tvTag.setVisibility(View.GONE);
        holder.tvSoldNumber.setText("已售:"+jpGoodsEntity.JPSellCount);
        if (jpGoodsEntity.hasGoods.equals("0")) {
            holder.ivSoldOut.setVisibility(View.VISIBLE);
        } else {
            holder.ivSoldOut.setVisibility(View.GONE);
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
//            Logger.i(jpLevel1CategoryId+"：jpLevel1CategoryId:"+jpLevel1CategoryId+"  +jpCategoryRecommendList:"+jpCategoryRecommendList.size()+"   jpCategoryRecommendList:"+jpCategoryRecommendList.toString());
        return jpCategoryRecommendListFromSp.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public class GoodViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private final ImageView ivSoldOut;
        public View rootView;
        public ImageView ivGoods1;
        public TextView tvGoodsName;
        public TextView tvCostPrice;
        public TextView tvMarketPrice;
        public TextView tvDiscount;
        public TextView tvTag;
        public FrameLayout frameLayoutJpGoodsItem;
        private final TextView tvPrice;
        private final TextView tvSoldNumber;

        public GoodViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.ivGoods1 = (ImageView) rootView.findViewById(R.id.iv_goods1);
            this.tvGoodsName = (TextView) rootView.findViewById(R.id.tv_goods_name);
            this.tvCostPrice = (TextView) rootView.findViewById(R.id.tv_cost_price);
            this.tvMarketPrice = (TextView) rootView.findViewById(R.id.tv_market_price);
            this.tvDiscount = (TextView) rootView.findViewById(R.id.tv_discount);
            this.tvTag = (TextView) rootView.findViewById(R.id.tv_tag);
            this.frameLayoutJpGoodsItem = (FrameLayout) rootView.findViewById(R.id.frame_layout_jp_goods_item);
            ivSoldOut = (ImageView) rootView.findViewById(R.id.iv_sold_out);
            tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
            tvSoldNumber = (TextView) rootView.findViewById(R.id.tv_sold_number);
        }

    }
}
