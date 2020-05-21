package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallGoodsListEntity;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.Constants;

import java.util.List;


/**
 * Created by yukun on 2016/4/22.
 */
public class LikeGoodsAdapter extends SimpleAdapter<MallGoodsListEntity.MallGoodsLists> {
    BitmapUtils bitmapUtils;

    Drawable drawable = null;

    TextView tvPrice;
    ImageView ivGoodsType;

    public LikeGoodsAdapter(Context context, List<MallGoodsListEntity.MallGoodsLists> datas) {
        super(context, R.layout.item_like_goods,datas);
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MallGoodsListEntity.MallGoodsLists item, int position) {
        bitmapUtils.display(viewHolder.getImageView(R.id.iv_goods), Constants.ImageUrl + item.goods_icon);
        viewHolder.getTextView(R.id.tv_goods_name).setText(item.goods_name);
        viewHolder.getTextView(R.id.tv_sales_volume).setText("销量:" + item.goods_sale + "件");
        tvPrice = viewHolder.getTextView(R.id.tv_goods_price);
        ivGoodsType = viewHolder.getImageView(R.id.iv_goods_type);

        Logger.i("销量:" + item.goods_sale + "件");
        switch (item.goods_type) {
            case 3: // goodsType 为3时 显示赠送价格
                tvPrice.setText(item.goods_price + "");
                ivGoodsType.setImageResource(R.mipmap.ic_goods_giving);
                drawable = context.getResources().getDrawable(R.mipmap.ic_mall_shopping);
                break;

            case 4: // goodsType 为4时 显示购买价格

                float goodsBuyPrice = NumberFormat.convertToFloat(item.goods_price, 0f);
                tvPrice.setText(String.format("%.2f", goodsBuyPrice / 100));
                ivGoodsType.setImageResource(R.mipmap.ic_goods_buy);
                drawable = context.getResources().getDrawable(R.mipmap.ic_mall_vouchers);
                break;

            default:

                break;
        }

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvPrice.setCompoundDrawables(null, null, drawable, null);
        }

    }

}
