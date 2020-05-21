package com.yilian.mall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.entity.AfterSale;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;


@SuppressLint("HandlerLeak")
public class AfterSaleAdapter extends BaseAdapter {

    Drawable drawable = null;
    private Context context;            //上下文对象
    private List<AfterSale.AfterSaleList> list;        //数据集合List
    private BitmapUtils bitmapUtil;

    public AfterSaleAdapter(Context context,
                            List<AfterSale.AfterSaleList> list) {
        this.list = list;
        this.context = context;
        bitmapUtil = new BitmapUtils(context);

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AfterSale.AfterSaleList data = list.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_after_sale, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //售后不需要有易划算或者奖券购的图标，售后状态退货换货状态高于图标显示
        switch (data.payStyle) {
            case "0"://普通商品
                viewHolder.tvGoodsYiDou.setVisibility(View.GONE);
                viewHolder.tvGoodsPrice.setVisibility(View.VISIBLE);
//                viewHolder.tvGoodsIntegral.setVisibility(View.GONE);
                viewHolder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(data.goodsCost)));
                viewHolder.tvGoodsCoupon.setText(" + " + MoneyUtil.getXianJinQuan(Integer.valueOf(data.goodsPrice) - Integer.valueOf(data.goodsCost)));
//                viewHolder.tvScore.setVisibility(View.VISIBLE);
//                viewHolder.tvScore.setText(context.getString(R.string.back_score) + MoneyUtil.getLeXiangBi(data.returnIntegral));
                break;
            case "1"://易划算
                viewHolder.tvGoodsYiDou.setVisibility(View.GONE);
                viewHolder.tvGoodsPrice.setVisibility(View.GONE);
//                viewHolder.tvGoodsIntegral.setVisibility(View.VISIBLE);
//                viewHolder.tvGoodsIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(data.goodsIntegral)));
//                viewHolder.tvScore.setVisibility(View.GONE);
                break;
            case "2"://奖券购
                viewHolder.tvGoodsYiDou.setVisibility(View.GONE);
                viewHolder.tvGoodsPrice.setVisibility(View.VISIBLE);
                viewHolder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(data.goodsRetail)));
                float integral = Float.parseFloat(data.goodsPrice) - Float.parseFloat(data.goodsRetail);
//                viewHolder.tvGoodsIntegral.setVisibility(View.VISIBLE);
//                viewHolder.tvGoodsIntegral.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(integral)));
//                viewHolder.tvScore.setVisibility(View.GONE);
                break;
            case "3":
            case "4":
            case GoodsType.CAL_POWER: //益豆专区商品
                viewHolder.tvGoodsYiDou.setVisibility(View.VISIBLE);
                viewHolder.tvGoodsYiDou.setText("送 " + MoneyUtil.getLeXiangBi(data.returnBean));
                viewHolder.tvGoodsPrice.setVisibility(View.VISIBLE);
//                viewHolder.tvGoodsIntegral.setVisibility(View.GONE);
                viewHolder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(data.goodsCost)));
                viewHolder.tvGoodsCoupon.setText(" + " + MoneyUtil.getXianJinQuan(Integer.valueOf(data.goodsPrice) - Integer.valueOf(data.goodsCost)));
//                viewHolder.tvScore.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(data.subsidy) && Double.valueOf(data.subsidy) != 0) {
                    viewHolder.tvSubsidy.setVisibility(View.VISIBLE);
                    viewHolder.tvSubsidy.setText("平台加赠"+Constants.APP_PLATFORM_DONATE_NAME+" " + MoneyUtil.getLeXiangBi(data.subsidy));
                }else {
                    viewHolder.tvSubsidy.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }


        if (list.get(position).AfterSaleType == 0) {
            viewHolder.tvIcon.setVisibility(View.VISIBLE);
            if (list.get(position).barterType == 1) {
                viewHolder.tvIcon.setText("换货");
            } else {
                viewHolder.tvIcon.setText("返修");
            }
        } else if (list.get(position).AfterSaleType == 1) {
            viewHolder.tvIcon.setVisibility(View.VISIBLE);
            viewHolder.tvIcon.setText("退货");
        } else {
            viewHolder.tvIcon.setVisibility(View.GONE);
        }

        if (data.AfterSaleStatus == 0) {
            viewHolder.BtAfterSaleStatus.setBackgroundResource(R.drawable.shape_after_sale_gray);
            viewHolder.BtAfterSaleStatus.setTextColor(Color.parseColor("#a5a5a5"));
        } else {
            viewHolder.BtAfterSaleStatus.setBackgroundResource(R.drawable.jp_bg_btn_red_all_radius1);
            viewHolder.BtAfterSaleStatus.setTextColor(Color.parseColor("#ffffff"));
        }


        showAuditStatus(viewHolder.BtAfterSaleStatus, data.AfterSaleStatus, data.AfterSaleType);
        GlideUtil.showImageWithSuffix(context, data.goodsIcon, viewHolder.ivGoodsPhoto);
        viewHolder.tvAfterSaleId.setText("售后编号:" + data.serviceOrder);

        viewHolder.tvAfterSaleTime.setText(StringFormat.formatDate(data.AfterSaleTime));

        viewHolder.tvGoodsName.setText(data.goodsName);
        viewHolder.tvGoodsNorms.setText(data.goodsNorms);
        viewHolder.tvGoodsCount.setText("× " + data.returnGoodsCount);


        return convertView;
    }

    /**
     * 判断审核状态
     *
     * @param btn
     * @param status
     */
    private void showAuditStatus(TextView btn, int status, int type) {
        //0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成
        switch (status) {
            case 0:
                btn.setText("已取消");
                break;
            case 1:
                btn.setText("待审核");
                break;
            case 2:
                btn.setText("审核拒绝");
                break;
            case 3:
                btn.setText("待退货");
                break;
            case 4:
                if (type == 1)
                    btn.setText("退货中");
                else
                    btn.setText("换货/维修中");
                break;
            case 5:
                if (type == 1)
                    btn.setText("待退款");
                else
                    btn.setText("待发货");
                break;
            case 6:
                if (type == 1) {
                    btn.setText("已完成");
                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
                } else {
                    btn.setText("待收货");
                }
                break;
            case 7:
                btn.setText("已完成");
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
                break;

        }
    }

    private final class ViewHolder {
        public TextView tvGoodsCoupon;
        public TextView tvIconFen;
        public TextView tvIconQuan;
        TextView tvAfterSaleId; // 售后服务号
        TextView tvAfterSaleTime; // 申请售后时间
        CircleImageView1 ivGoodsPhoto;  // 商品缩略图
        ImageView ivGoodsType;
        TextView tvGoodsName; //商品名称
        TextView tvGoodsNorms; // 商品描述
        TextView tvGoodsPrice; // 商品购买价格 goodsType goodsType为4 时显示
        TextView tvGoodsCount; // 商品数量
        TextView BtAfterSaleStatus;
        TextView tvIcon;
//        TextView tvScore;
//        TextView tvGoodsIntegral;//奖券价格
        TextView tvGoodsYiDou;
        TextView tvSubsidy;//平台加增益豆

        public ViewHolder(View convertView) {
            this.tvAfterSaleId = (TextView) convertView.findViewById(R.id.tv_after_sale_id);
            this.tvAfterSaleTime = (TextView) convertView.findViewById(R.id.tv_after_sale_time);
            this.ivGoodsPhoto = (CircleImageView1) convertView.findViewById(R.id.iv_item_shopping_cart);
            this.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            this.tvGoodsNorms = (TextView) convertView.findViewById(R.id.tv_goods_norms);
            this.tvGoodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
            this.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
            this.tvGoodsCoupon = (TextView) convertView.findViewById(R.id.tv_goods_coupon);
            this.BtAfterSaleStatus = (TextView) convertView.findViewById(R.id.bt_audit_status);
            this.ivGoodsType = (ImageView) convertView.findViewById(R.id.iv_goods_type);
            this.tvIconFen = (TextView) convertView.findViewById(R.id.tv_icon_fen);
            this.tvIconQuan = (TextView) convertView.findViewById(R.id.tv_icon_integral);
            this.tvIcon = (TextView) convertView.findViewById(R.id.tv_icon);
//            this.tvScore = (TextView) convertView.findViewById(R.id.tv_score);
//            this.tvGoodsIntegral = (TextView) convertView.findViewById(R.id.tv_goods_integral);
            this.tvGoodsYiDou = convertView.findViewById(R.id.tv_goods_beans);
            this.tvSubsidy = convertView.findViewById(R.id.tv_subsidy);

        }
    }


}
