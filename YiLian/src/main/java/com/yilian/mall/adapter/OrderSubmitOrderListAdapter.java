package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ShoppingCartListEntity2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/8.
 */
public class OrderSubmitOrderListAdapter extends BaseAdapter {

    public HashMap<Integer, String> selectMap = new HashMap<>();
    //类型
    public String fromType;
    Holder holder;
    //
    double needPrice;
    //不需要的钱
    double dontNeedPrice;
    //需要的全部奖券
    double needAllIntegral;
    //需要的易划算奖券
    double needYhsIntegral;
    //需要的奖券购的奖券
    double needJfgIntegral;
    private Context context;
    private ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> list;
    private double expressPrice, smalladdlexiangbi = 0, smalladdxianjinquan = 0, smalladdgouwuquan = 0;
    private int smalladdlefenbi = 0;
    private int goodsCount = 0;
    private String string;
    private String remark;
    private String type = "";

    public OrderSubmitOrderListAdapter(ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> list, Context context, double expressPrice) {
        this.list = list;
        this.context = context;
        this.expressPrice = expressPrice;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    class Holder {
        TextView tvOrderNumber, tvOrderAddress, tvOrderCount, tvGivingPriceSmall, tvBuyPriceSmall, tvXianJinQuanSmall, tv_buy_jifen_small, tv_buy_jifen_small_str;
        NoScrollListView listView;
        EditText etNote;
    }

    class ShopOrderSubmitGoodsAdapter extends BaseAdapter {
        BitmapUtils bitmapUtils;
        private Context context;
        private Drawable drawable;
        private ArrayList<ShoppingCartListEntity2.ListBean> shoppingCartListItems;

        public ShopOrderSubmitGoodsAdapter(Context context, ArrayList<ShoppingCartListEntity2.ListBean> shoppingCartListItems) {
            this.context = context;
            this.shoppingCartListItems = shoppingCartListItems;
            bitmapUtils = new BitmapUtils(context);
        }

        class ViewHolder {
            public TextView tvIconFen;
            public TextView tvGoodsQuan;
            public TextView tvIconQuan;
            ImageView ivGoods;
            TextView tvGoodsName;
            TextView tvGoodsNorms;
            TextView tvGoodsPrice;
            TextView tvGoodsCount;
            ImageView iv_goods_type;
            TextView tv_goods_jifen;
            //            TextView tv_goods_jifen_str;
            TextView tv_goods_ledou;
            TextView tv_subsidy;
        }

        @Override
        public int getCount() {
            if (shoppingCartListItems != null) {
                return shoppingCartListItems.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return shoppingCartListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_order_submit_goods, null);
                holder.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods);
                holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
                holder.tvGoodsNorms = (TextView) convertView.findViewById(R.id.tv_goods_norms);
                holder.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
                holder.tvGoodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
                holder.tvIconFen = (TextView) convertView.findViewById(R.id.tv_score);
                holder.tvGoodsQuan = (TextView) convertView.findViewById(R.id.tv_goods_integral);
                holder.tvIconQuan = (TextView) convertView.findViewById(R.id.tv_icon_quan);
                holder.iv_goods_type = (ImageView) convertView.findViewById(R.id.iv_goods_type);
                holder.tv_goods_jifen = (TextView) convertView.findViewById(R.id.tv_goods_integral);
                holder.tv_goods_ledou = convertView.findViewById(R.id.tv_goods_ledou);
                holder.tv_subsidy = convertView.findViewById(R.id.tv_subsidy);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ShoppingCartListEntity2.ListBean data = shoppingCartListItems.get(position);
            GlideUtil.showImageWithSuffix(context, data.goodsIcon, holder.ivGoods);
            holder.tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(data.goodsCost)));
            switch (data.type) {
                case "0":
                    holder.tvGoodsPrice.setVisibility(View.VISIBLE);
                    holder.iv_goods_type.setVisibility(View.GONE);
                    holder.tv_goods_jifen.setVisibility(View.GONE);
//                    holder.tv_goods_jifen_str.setVisibility(View.GONE);
                    holder.tv_goods_ledou.setVisibility(View.GONE);
                    break;
                case "3":
                case "4":
                case GoodsType.CAL_POWER:
                    holder.tvGoodsPrice.setVisibility(View.VISIBLE);
                    holder.iv_goods_type.setVisibility(View.GONE);
                    holder.tv_goods_jifen.setVisibility(View.GONE);
//                    holder.tv_goods_jifen_str.setVisibility(View.GONE);
                    holder.tv_goods_ledou.setVisibility(View.VISIBLE);
                    holder.tv_goods_ledou.setText("送  " + MoneyUtil.getLeXiangBiNoZero(data.returnBean));
                    if (data.subsidy != 0) {
                        holder.tv_subsidy.setVisibility(View.VISIBLE);
                        holder.tv_subsidy.setText("平台加赠" + Constants.APP_PLATFORM_DONATE_NAME + " " + MoneyUtil.getLeXiangBi(data.subsidy));
                    } else {
                        holder.tv_subsidy.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }

            if (data.isShowGave) {
                holder.tv_goods_ledou.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_goods_ledou.setVisibility(View.VISIBLE);
            }

            holder.tvGoodsName.setText(data.goodsName);
            holder.tvGoodsNorms.setText(data.goodsNorms);
            holder.tvGoodsCount.setText(String.format("×️%d", data.goodsCount));
            holder.tvIconFen.setVisibility(View.GONE);
            holder.tvIconQuan.setVisibility(View.GONE);

            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tvGoodsPrice.setCompoundDrawables(null, null, drawable, null);
            }

            return convertView;
        }


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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_submit_order_list, null);
            holder = new Holder();

            holder.listView = (NoScrollListView) convertView.findViewById(R.id.listview_order);
            holder.tvOrderAddress = (TextView) convertView.findViewById(R.id.tv_order_address);
            holder.tvOrderNumber = (TextView) convertView.findViewById(R.id.tv_order_number);
            holder.tvOrderCount = (TextView) convertView.findViewById(R.id.tv_order_count);
            holder.tvGivingPriceSmall = (TextView) convertView.findViewById(R.id.tv_giving_price_small);
            holder.tvBuyPriceSmall = (TextView) convertView.findViewById(R.id.tv_buy_price_small);
            holder.tvXianJinQuanSmall = (TextView) convertView.findViewById(R.id.tv_xianjinquan_small);
            holder.etNote = (EditText) convertView.findViewById(R.id.et_note);
            holder.tv_buy_jifen_small = (TextView) convertView.findViewById(R.id.tv_buy_jifen_small);
            holder.tv_buy_jifen_small_str = (TextView) convertView.findViewById(R.id.tv_buy_jifen_small_str);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.etNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Logger.i("备注焦点触发");
            }
        });
        holder.etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Logger.i("beforeTextChanged222");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.i("onTextChanged111");

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.i("afterTextChanged333");

            }
        });
        ArrayList<ShoppingCartListEntity2.ListBean> data = list.get(position);
        holder.listView.setAdapter(new ShopOrderSubmitGoodsAdapter(context, list.get(position)));
        holder.tvOrderAddress.setText(data.get(0).shopName);
        holder.tvOrderNumber.setText("订单" + (position + 1));
        smalladdlefenbi = 0;
        smalladdlexiangbi = 0;
        smalladdxianjinquan = 0;
        goodsCount = 0;
        smalladdgouwuquan = 0;
        needPrice = 0;
        needAllIntegral = 0;

        for (int i = 0; i < data.size(); i++) {
            ShoppingCartListEntity2.ListBean listBean = data.get(i);

            goodsCount += listBean.goodsCount;
            type += listBean.type;

            switch (listBean.type) {
                case "0":
                    needPrice += listBean.goodsCost * listBean.goodsCount;
                    needAllIntegral += 0;
                    break;
                case "1":
                    needPrice += 0;
                    needAllIntegral += listBean.goodsIntegral * listBean.goodsCount;
                    break;
                case "2":
                    needPrice += listBean.goodsRetail * listBean.goodsCount;
                    needAllIntegral += (listBean.goodsPrice - listBean.goodsRetail) * listBean.goodsCount;
                    break;
                case "3":
                case "4":
                case GoodsType.CAL_POWER:
                    needPrice += listBean.goodsCost * listBean.goodsCount;
                    needAllIntegral += 0;
                default:
                    break;
            }
        }

        string = "小计" + goodsCount + "件商品";

        holder.tvBuyPriceSmall.setVisibility(View.VISIBLE);
        holder.tvBuyPriceSmall.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(needPrice)));
        holder.tv_buy_jifen_small.setVisibility(View.GONE);
        holder.tv_buy_jifen_small.setText(" + " + MoneyUtil.getLeXiangBi(needAllIntegral));
        holder.tv_buy_jifen_small_str.setVisibility(View.GONE);

        SpannableString span = new SpannableString(string);
        span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_999)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_h1)), 2, 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_999)), 3, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvOrderCount.setText(span);

        return convertView;
    }


}

