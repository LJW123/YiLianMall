package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallOrderListEntity;
import com.yilian.mall.ui.ActEvaluateActivity;
import com.yilian.mall.ui.EvaluateActivity;
import com.yilian.mall.ui.JPOrderActivity;
import com.yilian.mall.ui.MyMallOrderListActivity2;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/8/1 0001.
 */
public class MallOrderAdapter extends BaseListAdapter<MallOrderListEntity.MallOrderLists> {

    private int orderType;
    private MyMallOrderListActivity2 acticity;
    private onDelecteOrderListener onDelectListener;

    public MallOrderAdapter(ArrayList<MallOrderListEntity.MallOrderLists> listData, int orderType, Context mContext) {
        super(listData, orderType, mContext);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_mall_order_lists, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MallOrderListEntity.MallOrderLists entity = datas.get(position);
        orderType = (int) object[0];
        acticity = (MyMallOrderListActivity2) object[1];

        /**
         * 判断是否是猜价格-碰运气订单
         */

        holder.tvShopName.setText(entity.order_name);
        if (holder.orderStatusTv != null) {
            switch (entity.order_status) {
                case 0:
                    holder.orderStatusTv.setText("已取消");
                    holder.buy.setVisibility(View.VISIBLE);
                    holder.buy.setText("删除订单");
                    break;
                case 1:
                    holder.orderStatusTv.setText("待支付");
                    holder.buy.setVisibility(View.VISIBLE);
                    switch (entity.order_type) {
                        case "5":
                        case "6":
                            holder.buy.setText("付押金");
                            break;
                        default:
                            holder.buy.setText("去支付");
                            break;
                    }
                    break;
                case 2:
                    holder.orderStatusTv.setText("待出库");
                    holder.buy.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.orderStatusTv.setText("正在出库");
                    holder.buy.setVisibility(View.GONE);
                    break;
                case 4:
                    holder.orderStatusTv.setText("已部分发货");
                    holder.buy.setVisibility(View.GONE);
                    break;
                case 5:
                    holder.orderStatusTv.setText("待收货");//已全部发货&待收货
                    holder.buy.setVisibility(View.GONE);
                    break;
                case 6:
                    holder.buy.setVisibility(View.GONE);
                    switch (entity.profitStatus) {
                        case 1:
                            holder.orderStatusTv.setText("已结算");
                            break;
                        default:
                            holder.orderStatusTv.setText("已完成");
                            break;
                    }
                    if (orderType == 3) {
//                                holder.orderStatusTv.setText("已完成");
                    } else {
//                                holder.orderStatusTv.setText("待评价");
                        holder.layout.setVisibility(View.GONE);
                        holder.topLayout.setVisibility(View.GONE);
                        if (orderType == 4) {
                            holder.layout.setVisibility(View.GONE);
                            holder.topLayout.setVisibility(View.GONE);
                        } else {
                            holder.layout.setVisibility(View.VISIBLE);
                            holder.topLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                default:
                    break;
            }
            if (entity.goods_list.size() > 0) {
                OrderGoodsItemListAdapter goodsItemListAdapter = new OrderGoodsItemListAdapter(entity.goods_list, entity);
                holder.goodsOrderLv.setAdapter(goodsItemListAdapter);
            }

            switch (entity.order_type) {
                case "5":
                case "6":
                    holder.goodsMoney1.setVisibility(View.GONE);
//                    holder.goodsIntergalMoney.setVisibility(View.VISIBLE);
//                    holder.goodsIntergalMoney.setText(MoneyUtil.setSmallIntegralMoney("0"));
                    break;
                default:
                    holder.goodsMoney1.setVisibility(View.VISIBLE);
                    holder.goodsMoney1.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(entity.order_total_price)));
//                    if (!TextUtils.isEmpty(entity.order_integral_price) && Double.parseDouble(entity.order_integral_price) != 0) {
//                        holder.goodsIntergalMoney.setVisibility(View.VISIBLE);
//                        holder.goodsIntergalMoney.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getXianJinQuan(entity.order_integral_price)));
//                    } else {
//                        holder.goodsIntergalMoney.setVisibility(View.GONE);
//                    }
                    break;
            }

            holder.iconJuan.setVisibility(View.GONE);
//            holder.iconfen.setVisibility(View.GONE);
            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDelectListener != null) {
                        onDelectListener.delectOrderItem(position);
                        Logger.i("  onClick ");
                    }
                }
            });
        }
        Logger.i("zxc==first" + entity.order_index);
        holder.goodsOrderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, JPOrderActivity.class);
                intent.putExtra("order_index", entity.order_index);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Logger.i("entity.order_index" + entity.order_index);
                context.startActivity(intent);
            }
        });


        return view;
    }

    public void setOnDelectOderItemListener(onDelecteOrderListener onDelectOderItemListener) {
        this.onDelectListener = onDelectOderItemListener;
    }

    /**
     * 删除当前的条目
     */
    public interface onDelecteOrderListener {
        void delectOrderItem(int position);
    }

    /**
     * pullListView内部的list适配器
     */
    class OrderGoodsItemListAdapter extends BaseListAdapter {
        MallOrderListEntity.MallOrderLists entity;

        public OrderGoodsItemListAdapter(ArrayList<MallOrderListEntity.MallOrderLists.MallOrderGoodsList> goods_list, MallOrderListEntity.MallOrderLists entity) {
            super(goods_list, entity);
            this.entity = entity;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ItemViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_order_submit_goods, null);
                holder = new ItemViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ItemViewHolder) view.getTag();
            }

            final MallOrderListEntity.MallOrderLists.MallOrderGoodsList data = (MallOrderListEntity.MallOrderLists.MallOrderGoodsList) datas.get(position);

            MallOrderListEntity.MallOrderLists mallOrderLists = (MallOrderListEntity.MallOrderLists) object[0];

            if (mallOrderLists.order_status == 6 && orderType == 4 && data.goods_status != 2) {
                holder.item.setVisibility(View.GONE);
            } else {
                holder.item.setVisibility(View.VISIBLE);
            }

            holder.tvEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    switch (entity.order_type) {
                        case "5":
                        case "6":
                            intent = new Intent(context, ActEvaluateActivity.class);
                            intent.putExtra("goods_icon", data.goods_icon);
                            intent.putExtra("goods_index", data.order_goods_index);
                            break;
                        default:
                            intent = new Intent(context, EvaluateActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("goods_id", data.goods_id);
                            intent.putExtra("orderGoodsIndex", data.order_goods_index);
                            intent.putExtra("orderId", mallOrderLists.order_index);
                            intent.putExtra("filiale_id", mallOrderLists.filiale_id);
                            intent.putExtra("order_type", mallOrderLists.order_type);
                            intent.putExtra("type", data.type);
                            break;
                    }
                    context.startActivity(intent);
                }
            });
            if ("2".equals(mallOrderLists.order_type)) {
                holder.ivWinState.setVisibility(View.VISIBLE);
            } else {
                holder.ivWinState.setVisibility(View.GONE);
            }
            GlideUtil.showImageWithSuffix(context, data.goods_icon, holder.ivGoods);
            if (!TextUtils.isEmpty(data.goods_name)) {
                holder.tvGoodsName.setText(data.goods_name);
            }
            String goodsNormsStr = data.goods_norms;
            if (goodsNormsStr != null && goodsNormsStr.length() > 0) {
                holder.tvGoodsNorms.setVisibility(View.VISIBLE);
                holder.tvGoodsNorms.setText(goodsNormsStr);
            } else {
                holder.tvGoodsNorms.setText("");
            }
            if (orderType == 4) {
                if (data.goods_status == 2) {
                    holder.ll.setVisibility(View.VISIBLE);
                    holder.tvEvaluate.setVisibility(View.VISIBLE);
                } else {
                    holder.ll.setVisibility(View.GONE);
                    holder.tvEvaluate.setVisibility(View.GONE);
                }
            }

            Logger.i("ray-" + mallOrderLists.order_turns);
            if (mallOrderLists.order_turns == 1) {
                holder.ivWinState.setVisibility(View.VISIBLE);
            } else {
                holder.ivWinState.setVisibility(View.GONE);
            }
            //6表示购物卡订单
            if (entity.orderBelongs == 6) {

            } else {
                /**
                 * 判断是否是猜价格-碰运气订单
                 */

                if (!TextUtils.isEmpty(data.type)) {
                    switch (data.type) {
                        case "0":
                            holder.tvGoodsPrice.setVisibility(View.VISIBLE);
                            holder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(data.goods_cost)));
//                        holder.tvIntegral.setVisibility(View.GONE);
                            holder.ivYhsIcon.setVisibility(View.GONE);
                            holder.tvGoodsLeDou.setVisibility(View.GONE);
                            holder.tv_subsidy.setVisibility(View.GONE);
//                        holder.iconFen.setVisibility(View.VISIBLE);
//                        holder.iconFen.setText(context.getString(R.string.back_score) + MoneyUtil.getLeXiangBi(data.returnIntegral));
                            break;
                        case "1"://易划算
                            holder.ivYhsIcon.setVisibility(View.VISIBLE);
                            holder.ivYhsIcon.setImageResource(R.mipmap.icon_yhs);
//                        holder.tvIntegral.setVisibility(View.VISIBLE);
//                        if (!TextUtils.isEmpty(data.goods_integral)) {
//                            holder.tvIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(data.goods_integral)));
//                        }
                            holder.tvGoodsPrice.setVisibility(View.GONE);
//                        holder.iconFen.setVisibility(View.GONE);
                            holder.tvGoodsLeDou.setVisibility(View.GONE);
                            holder.tv_subsidy.setVisibility(View.GONE);
                            break;
                        case "2"://奖券购
                            holder.tvGoodsPrice.setVisibility(View.VISIBLE);
                            holder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(data.goods_retail)));
                            holder.ivYhsIcon.setVisibility(View.VISIBLE);
                            holder.ivYhsIcon.setImageResource(R.mipmap.icon_jfg);
//                        holder.iconFen.setVisibility(View.GONE);
//                        holder.tvIntegral.setVisibility(View.VISIBLE);
                            holder.tvGoodsLeDou.setVisibility(View.GONE);
                            holder.tv_subsidy.setVisibility(View.GONE);
                            float integralPrice = Float.parseFloat(data.goods_price) - Float.parseFloat(data.goods_retail);
//                        holder.tvIntegral.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(integralPrice)));
                            break;
                        case "3":
                        case "4":
                        case GoodsType.CAL_POWER: //区块益豆专区商品
                            holder.tvGoodsPrice.setVisibility(View.VISIBLE);
                            holder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(data.goods_cost)));
//                        holder.tvIntegral.setVisibility(View.GONE);
                            holder.ivYhsIcon.setVisibility(View.GONE);
//                        holder.iconFen.setVisibility(View.GONE);
                            holder.tvGoodsLeDou.setVisibility(View.VISIBLE);
                            holder.tvGoodsLeDou.setText("送 " + MoneyUtil.getLeXiangBi(data.giveBean));
                            if (!TextUtils.isEmpty(data.subsidy) && Double.valueOf(data.subsidy) != 0) {
                                holder.tv_subsidy.setVisibility(View.VISIBLE);
                                holder.tv_subsidy.setText("平台加赠" + Constants.APP_PLATFORM_DONATE_NAME + " " + MoneyUtil.getLeXiangBi(data.subsidy));
                            } else {
                                holder.tv_subsidy.setVisibility(View.GONE);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            holder.iconJuan.setVisibility(View.GONE);
            holder.tvGoodsCount.setText(" × " + data.goods_count);
            return view;
        }
    }

    class ViewHolder {
        public TextView tvTotalPrice;
        public TextView tvWheelOrder;
        private TextView orderStatusTv;
        private NoScrollListView goodsOrderLv;
        private TextView tvShopName;
        private TextView goodsMoney1;
        //        private TextView goodsIntergalMoney;
//        private TextView iconfen;
        private TextView iconJuan;
        private TextView buy;
        private LinearLayout layout;
        private LinearLayout topLayout;

        public ViewHolder(View view) {
            this.tvTotalPrice = (TextView) view.findViewById(R.id.tv_total_price);
            this.tvWheelOrder = (TextView) view.findViewById(R.id.tv_wheel_order);
            this.orderStatusTv = (TextView) view.findViewById(R.id.order_status_tv);
            this.goodsOrderLv = (NoScrollListView) view.findViewById(R.id.goods_order_lv);
            this.tvShopName = (TextView) view.findViewById(R.id.tv_shop_name1);
            this.goodsMoney1 = (TextView) view.findViewById(R.id.goods_money1);
//            this.goodsIntergalMoney = (TextView) view.findViewById(R.id.goods_integral);
//            this.iconfen = (TextView) view.findViewById(R.id.tv_score);
            this.iconJuan = (TextView) view.findViewById(R.id.tv_icon_integral);
            this.buy = (TextView) view.findViewById(R.id.tv_buy);
            this.layout = (LinearLayout) view.findViewById(R.id.layout);
            this.topLayout = (LinearLayout) view.findViewById(R.id.top_layout);
        }
    }

    class ItemViewHolder {
        RelativeLayout item;
        ImageView ivGoods;
        ImageView ivGoodsType;
        TextView tvGoodsName;
        TextView tvGoodsNorms;
        TextView tvGoodsPrice;
        TextView tvGoodsCount;
        LinearLayout ll;
        TextView tvEvaluate;
        //        TextView tvIntegral;
        TextView iconJuan;
        //        TextView iconFen;
        ImageView ivWinState;
        ImageView ivYhsIcon;
        TextView tvGoodsLeDou;
        TextView tv_subsidy;


        public ItemViewHolder(View convertView) {
            this.tvGoodsLeDou = (TextView) convertView.findViewById(R.id.tv_goods_beans);
            this.item = (RelativeLayout) convertView.findViewById(R.id.item);
            this.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods);
            this.ivGoodsType = (ImageView) convertView.findViewById(R.id.iv_goods_type);
            this.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
            this.tvGoodsNorms = (TextView) convertView.findViewById(R.id.tv_goods_norms);
            this.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
            this.tvGoodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
//            this.tvIntegral = (TextView) convertView.findViewById(R.id.tv_goods_integral);//奖券
//            this.iconFen = (TextView) convertView.findViewById(R.id.tv_score);
            this.iconJuan = (TextView) convertView.findViewById(R.id.tv_icon_quan);
            this.ll = (LinearLayout) convertView.findViewById(R.id.ll_order_over);
            this.tvEvaluate = (TextView) convertView.findViewById(R.id.tv_evaluate);
            this.ivWinState = (ImageView) convertView.findViewById(R.id.iv_win_state);
            this.ivYhsIcon = (ImageView) convertView.findViewById(R.id.iv_yhs_icon);
            this.tv_subsidy = (TextView) convertView.findViewById(R.id.tv_subsidy);

        }
    }

}
