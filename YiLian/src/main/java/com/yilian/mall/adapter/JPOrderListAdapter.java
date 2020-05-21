//package com.yilian.mall.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.orhanobut.logger.Logger;
//import com.yilian.mall.R;
//import com.yilian.mall.entity.MallOrderListEntity;
//import com.yilian.mall.ui.EvaluateActivity;
//import com.yilian.mall.ui.JPOrderActivity;
//import com.yilian.mall.utils.MoneyUtil;
//import com.yilian.mall.utils.Toast;
//import com.yilian.mall.widgets.NoScrollListView;
//import com.yilian.mylibrary.Constants;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Ray_L_Pain on 2016/12/23 0023.
// */
//
//public class JPOrderListAdapter extends android.widget.BaseAdapter {
//    private Context context;
//    private ArrayList<MallOrderListEntity.MallOrderLists> list;
//    private String filialeId, orderCreateTime, orderIndex;
//    private double buyPrice;
//    private int givingPrice;
//    private int orderType;
//    protected ImageLoader imageLoader = ImageLoader.getInstance();
//    protected DisplayImageOptions options;
//
//    public JPOrderListAdapter(Context context, ArrayList<MallOrderListEntity.MallOrderLists> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @Override
//    public int getCount() {
//        if (list.size() == 0 || list == null) {
//            return 0;
//        }
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_mall_order_lists, null);
//            holder.orderStatusTv = (TextView) convertView.findViewById(R.id.order_status_tv);
//            holder.goodsOrderLv = (NoScrollListView) convertView.findViewById(R.id.goods_order_lv);
//            holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_shop_name1);
//            holder.goodsMoney1 = (TextView) convertView.findViewById(R.id.goods_money1);
//            holder.goodsIntergalMoney = (TextView) convertView.findViewById(R.id.goods_integral);
//            holder.iconfen = (TextView) convertView.findViewById(R.id.tv_score);
//            holder.iconJuan = (TextView) convertView.findViewById(R.id.tv_icon_integral);
//            holder.buy = (TextView) convertView.findViewById(R.id.tv_buy);
//            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
//            holder.topLayout = (LinearLayout) convertView.findViewById(R.id.top_layout);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        final MallOrderListEntity.MallOrderLists entity;
//        if (list.size() != 0) {
//            entity = list.get(position);
//            Logger.i("首次打印entity:  position:"+position+"    "+entity.toString());
//            /**
//             * v3新写的
//             */
//            filialeId = entity.filiale_id;
//            buyPrice = Integer.parseInt(entity.order_total_price) / 100;
//            givingPrice = Integer.parseInt(entity.order_coupon);//需要支付抵扣券总额
//            orderCreateTime = entity.order_time;//下单时间
//            orderIndex = entity.order_index;
//
//            holder.tvShopName.setText(entity.order_name);
//            if (holder.orderStatusTv != null) {
//                switch (entity.order_status) {
//                    case 0:
//                        holder.orderStatusTv.setText("已取消");
//                        holder.buy.setVisibility(View.GONE);
//                        break;
//                    case 1:
//                        holder.orderStatusTv.setText("待支付");
//                        holder.buy.setVisibility(View.VISIBLE);
//                        break;
//                    case 2:
//                        holder.orderStatusTv.setText("待出库");
//                        break;
//                    case 3:
//                        holder.orderStatusTv.setText("正在出库");
//                        break;
//                    case 4:
//                        holder.orderStatusTv.setText("已部分发货");
//                        break;
//                    case 5:
//                        holder.orderStatusTv.setText("待收货");//已全部发货&待收货
//                        break;
//                    case 6:
//                        if (orderType == 3) {
//                            holder.orderStatusTv.setText("已完成");
//                        } else {
//                            holder.orderStatusTv.setText("待评价");
//                            holder.buy.setVisibility(View.GONE);
//                            if (orderType == 4) {
//                                holder.layout.setVisibility(View.GONE);
//                                holder.topLayout.setVisibility(View.GONE);
//                            } else {
//                                holder.layout.setVisibility(View.VISIBLE);
//                                holder.topLayout.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                        break;
//                }
//                if (holder.goodsOrderLv != null) {
//                    JPOrderListItemAdapter itemAdapter = new JPOrderListItemAdapter(context, entity.goods_list, entity);
//                    holder.goodsOrderLv.setAdapter(itemAdapter);
//                }
//
//                if (entity.order_type.equals("3")){
//                    holder.goodsMoney1.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(entity.order_total_price)));
//                    holder.goodsIntergalMoney.setText(" + "+MoneyUtil.getLeFenBi(Integer.parseInt(entity.order_coupon)));
//                    holder.iconfen.setVisibility(View.VISIBLE);
//                    holder.iconJuan.setVisibility(View.GONE);
//                } else {
//                    holder.goodsMoney1.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(entity.order_total_price)));
//                    holder.goodsIntergalMoney.setText(" + "+MoneyUtil.getXianJinQuan(entity.order_coupon));
//                    holder.iconJuan.setVisibility(View.VISIBLE);
//                    holder.iconfen.setVisibility(View.GONE);
//                }
//                holder.buy.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //pay(entity);
//                        Toast.makeText(context, "支付", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            Logger.i("zxc==first"+entity.order_index);
//            holder.goodsOrderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        MallOrderLists.MallOrderGoodsList
//
//                    Intent intent = new Intent(context, JPOrderActivity.class);
//                    intent.putExtra("order_index", entity.order_index);
//                    Logger.i("entity.order_index"+entity.order_index);
//                    context.startActivity(intent);
//                }
//            });
//        }
//
//        return convertView;
//    }
//
//    class ViewHolder {
//        private TextView orderStatusTv;
//        private NoScrollListView goodsOrderLv;
//        private TextView tvShopName;
//        private TextView goodsMoney1;
//        private TextView goodsIntergalMoney;
//        private TextView iconfen;
//        private TextView iconJuan;
//        private TextView buy;
//        private LinearLayout layout;
//        private LinearLayout topLayout;
//    }
//
//    class JPOrderListItemAdapter extends android.widget.BaseAdapter {
//
//        private Context context;
//        private List<MallOrderListEntity.MallOrderLists.MallOrderGoodsList> goodsList;
//        private MallOrderListEntity.MallOrderLists mallOrderLists;
//
//        public JPOrderListItemAdapter(Context context, List<MallOrderListEntity.MallOrderLists.MallOrderGoodsList> goodsList, MallOrderListEntity.MallOrderLists mallOrderLists) {
//            this.context = context;
//            this.goodsList = goodsList;
//            this.mallOrderLists = mallOrderLists;
//            options = new DisplayImageOptions.Builder()
//                    .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
//                    .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
//                    .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
//                    .cacheInMemory(true) //设置下载的图片是否缓存在内存中
//                    .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
//                    .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//                    .imageScaleType(ImageScaleType.EXACTLY)
//                    .build();
//        }
//
//        @Override
//        public int getCount() {
//            if (goodsList.size() == 0 || goodsList == null) {
//                return 0;
//            }
//            return goodsList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return goodsList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            InnerViewHolder holder;
//            if (convertView == null) {
//                holder = new InnerViewHolder();
//                convertView = LayoutInflater.from(context).inflate(R.layout.item_order_submit_goods, null);
//                holder.item = (RelativeLayout) convertView.findViewById(R.id.item);
//                holder.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods);
//                holder.ivGoodsType = (ImageView) convertView.findViewById(R.id.iv_goods_type);
//                holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
//                holder.tvGoodsNorms = (TextView) convertView.findViewById(R.id.tv_goods_norms);
//                holder.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
//                holder.tvGoodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
//                holder.tvJuan = (TextView) convertView.findViewById(R.id.tv_goods_integral);
//                holder.iconFen = (TextView) convertView.findViewById(R.id.tv_score);
//                holder.iconJuan = (TextView) convertView.findViewById(R.id.tv_icon_integral);
//                holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_order_over);
//                holder.tvEvaluate = (TextView) convertView.findViewById(R.id.tv_evaluate);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (InnerViewHolder) convertView.getTag();
//            }
//
//            if (goodsList != null) {
//                final MallOrderListEntity.MallOrderLists.MallOrderGoodsList data = goodsList.get(position);
//
//                if(mallOrderLists.order_status == 6 && orderType == 4 && data.goods_status != 2){
//                    holder.item.setVisibility(View.GONE);
//                } else {
//                    holder.item.setVisibility(View.VISIBLE);
//                }
//
//                holder.tvEvaluate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(context, EvaluateActivity.class);
//                        intent.putExtra("goods_id", data.goods_id);
//                        intent.putExtra("orderGoodsIndex", data.order_goods_index);
//                        intent.putExtra("orderId", mallOrderLists.order_index);
//                        intent.putExtra("filiale_id",mallOrderLists.filiale_id);
//                        intent.putExtra("order_type",mallOrderLists.order_type);
//                        intent.putExtra("type",data.type);
//                        context.startActivity(intent);
//                    }
//                });
//
//                String url = Constants.ImageUrl + data.goods_icon + Constants.ImageSuffix;
//                if (url.equals(holder.ivGoods.getTag())) {
//
//                } else {
//                    imageLoader.displayImage(url, holder.ivGoods, options);
//                    holder.ivGoods.setTag(url);
//                }
//
//                holder.tvGoodsName.setText(data.goods_name);
//
//                String goodsNormsStr = data.goods_norms;
//
//                if (goodsNormsStr.length() > 0) {
//                    holder.tvGoodsNorms.setVisibility(View.VISIBLE);
//                    holder.tvGoodsNorms.setText(goodsNormsStr);
//                } else {
//                    holder.tvGoodsNorms.setText("");
//                }
//
//                if (orderType == 4) {
//                    if (data.goods_status == 2) {
//                        holder.ll.setVisibility(View.VISIBLE);
//                        holder.tvEvaluate.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.ll.setVisibility(View.GONE);
//                        holder.tvEvaluate.setVisibility(View.GONE);
//                    }
//                }
//
//                if ("3".equals(data.goods_type)) {
//                    holder.tvGoodsPrice.setText(MoneyUtil.getLeFenBi(data.goods_price));
//                    holder.tvJuan.setVisibility(View.GONE);
//                    holder.iconFen.setVisibility(View.VISIBLE);
//                    holder.iconJuan.setVisibility(View.GONE);
//                    holder.tvGoodsCount.setText(" × " + data.goods_count);
//                } else {
//                    holder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(data.goods_cost)));
//                    holder.tvJuan.setVisibility(View.VISIBLE);
//                    holder.tvJuan.setText(" + "+MoneyUtil.getXianJinQuan(Integer.parseInt(data.goods_price)-Integer.parseInt(data.goods_cost)));
//                    holder.iconJuan.setVisibility(View.VISIBLE);
//                    holder.iconFen.setVisibility(View.GONE);
//                    holder.tvGoodsCount.setText(" × " + data.goods_count);
//                }
//            }
//            return convertView;
//        }
//
//        class InnerViewHolder {
//            RelativeLayout item;
//            ImageView ivGoods;
//            ImageView ivGoodsType;
//            TextView tvGoodsName;
//            TextView tvGoodsNorms;
//            TextView tvGoodsPrice;
//            TextView tvGoodsCount;
//            LinearLayout ll;
//            TextView tvEvaluate;
//            TextView tvJuan;
//            TextView iconJuan;
//            TextView iconFen;
//        }
//    }
//
//
//}
