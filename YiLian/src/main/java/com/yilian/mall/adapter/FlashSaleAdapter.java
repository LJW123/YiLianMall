package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.FlashSaleEntity;
import com.yilian.mall.entity.FlashSaleTransferEntity;
import com.yilian.mall.entity.OrderSubmitGoods;
import com.yilian.mall.ui.FlashSalePayActivity;
import com.yilian.mall.ui.MallFlashSaleDetailActivity;
import com.yilian.mall.ui.TakeOutFlashSalePayActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.CountdownView.CountdownView;
import com.yilian.mall.widgets.CountdownView.DynamicConfig;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * Created by liuyuqi on 2017/3/23 0023.
 */
public class FlashSaleAdapter extends BaseListAdapter<FlashSaleEntity.DataBean.GoodsBean> {

    private final String time;
    private final Context mContext;
    private final String click;

    public FlashSaleAdapter(List<FlashSaleEntity.DataBean.GoodsBean> flashList, String Time, Context mContext, String click) {
        super(flashList);
        this.time = Time;
        this.mContext = mContext;
        this.click = click;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view) {
            view = View.inflate(context, R.layout.list_item_flash_sale1, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        FlashSaleEntity.DataBean.GoodsBean goodsBean = datas.get(position);
        if (TextUtils.isEmpty(goodsBean.goodsLabel)) {//没有标签
            holder.tvFlashGoodLabel.setVisibility(View.GONE);
        } else {//有标签
            holder.tvFlashGoodLabel.setVisibility(View.VISIBLE);
            holder.tvFlashGoodLabel.setText(goodsBean.goodsLabel);
        }
        String imageUrl = goodsBean.imageUrl;
        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
                imageUrl = imageUrl + Constants.ImageSuffix;
            } else {
                imageUrl = Constants.ImageUrl + imageUrl + Constants.ImageSuffix;
            }
        } else {
            holder.ivFlashSale.setImageResource(R.mipmap.login_module_default_jp);
        }


        imageLoader.displayImage(imageUrl, holder.ivFlashSale, options);
        long differTime = Long.parseLong(goodsBean.buyTime) - Long.parseLong(time);


        //本地显示零购升级每个item都包含倒计时和总部一样
        if (click.equals("2") && differTime > 0) {
            holder.rlDefault.setVisibility(View.GONE);
            holder.llNextContent.setVisibility(View.VISIBLE);
            holder.tvNextTitle.setText(goodsBean.goodsName);
            holder.tvNextDes.setText(goodsBean.merchantName);
            holder.tvNextPrice.setText(MoneyUtil.getLeXiangBiNoZero(goodsBean.price));
            holder.tvNextResidue.setText(Html.fromHtml("<font color=\"#999999\"<small><small><small>共</small></small></small></font>" +
                    goodsBean.totalNum + "<font color=\"#999999\"<small><small><small>份</small></small></small>"));
            long countDownTime = differTime * 1000;

            holder.countdownView.start(countDownTime);
            holder.countdownView.dynamicShow(new DynamicConfig.Builder().setConvertDaysToHours(true).build());
        } else {
            holder.llNextContent.setVisibility(View.GONE);
            holder.rlDefault.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(goodsBean.goodsName);
            holder.tvIntro.setText(goodsBean.merchantName);
            holder.tvPrice.setText(MoneyUtil.getLeXiangBiNoZero(goodsBean.price));
            String html;
            //现要实现功能，如果抢购中的商品没有，就把即将开始的数据赋值给抢购中
            holder.progressbar.setMax(Integer.parseInt(goodsBean.totalNum));
            //开始的时间大于现在的时间未开始
            if (Integer.parseInt(goodsBean.buyTime) > Integer.parseInt(time)) {
                holder.isState.setText("未开始");
                holder.isState.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_btn_style_green));
                holder.progressbar.setProgress(0);
                html = "<font color=\"#999999\"<small><small><small>共计</small></small></small></font>" +
                        goodsBean.goodsNum + "<font color=\"#999999\"<small><small><small>份</small></small></small></font> ";
                holder.tvResidue.setText(Html.fromHtml(html));
            } else {
                if (Integer.parseInt(goodsBean.goodsNum) > 0) {
                    holder.isState.setBackground(ContextCompat.getDrawable(context, R.drawable.merchant_bg_btn_style_red));
                    holder.isState.setText("去抢购");
                    int anInt = (Integer.parseInt(goodsBean.totalNum) - Integer.parseInt(goodsBean.goodsNum));
                    holder.progressbar.setProgress(anInt);
                    Logger.i("anInt  " + anInt);
                    html = "<font color=\"#999999\"<small><small><small>剩余</small></small></small></font>" +
                            goodsBean.goodsNum + "<font color=\"#999999\"<small><small><small>份</small></small></small></font> ";
                    holder.tvResidue.setText(Html.fromHtml(html));
                    //去抢购点击直接进行抢购
                    String finalImageUrl = imageUrl;
                    holder.isState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            switch (goodsBean.belong) {
                                case "1":
                                    intent = new Intent(mContext, MallFlashSaleDetailActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("goods_id", goodsBean.goodsId);
                                    mContext.startActivity(intent);
                                    break;
                                case "0":
                                    //判断是店内消费还是配送的
                                    if ("1".equals(goodsBean.goodsType)) {//配送
                                        //传递对象
                                        intent=new Intent(context, TakeOutFlashSalePayActivity.class);
                                        OrderSubmitGoods orderSubmitGoods=new OrderSubmitGoods();
                                        orderSubmitGoods.goodsPrice = Double.parseDouble(goodsBean.price);
                                        orderSubmitGoods.goodsPic = finalImageUrl;
                                        orderSubmitGoods.goodsCount = 1;
                                        orderSubmitGoods.name = goodsBean.goodsName;
                                        orderSubmitGoods.goodsId = goodsBean.goodsId;
                                        orderSubmitGoods.type = Constants.FLASHSALEPAY;
                                        orderSubmitGoods.label = "";
                                        intent.putExtra("orderSubmitGoods", orderSubmitGoods);
                                        mContext.startActivity(intent);

                                    } else if ("2".equals(goodsBean.goodsType)) {//店内消费
                                        intent = new Intent(mContext, FlashSalePayActivity.class);
                                        FlashSaleTransferEntity flashSaleTransferEntity = new FlashSaleTransferEntity();
                                        flashSaleTransferEntity.buyTime = goodsBean.buyTime;
                                        flashSaleTransferEntity.goodsId = goodsBean.goodsId;
                                        flashSaleTransferEntity.goodsName = goodsBean.goodsName;
                                        flashSaleTransferEntity.goodsNum = goodsBean.goodsNum;
                                        flashSaleTransferEntity.imageUrl = goodsBean.imageUrl;
                                        flashSaleTransferEntity.introduction = goodsBean.introduction;
                                        flashSaleTransferEntity.merchantId = goodsBean.merchantId;
                                        flashSaleTransferEntity.price = goodsBean.price;
                                        flashSaleTransferEntity.merchantName = goodsBean.merchantName;
                                        intent.putExtra("FlashSaleTransferEntity", flashSaleTransferEntity);
                                        mContext.startActivity(intent);
                                    }
                                    break;
                            }
                        }
                    });
                } else {
                    holder.tvState.setVisibility(View.VISIBLE);
                    holder.isState.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_border_btn_bg));
                    holder.isState.setText("已抢光");
                    holder.isState.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
                    holder.progressbar.setProgress(Integer.parseInt(goodsBean.totalNum));
                    html = "<font color=\"#999999\"<small><small><small>剩余</small></small></small></font>" +
                            goodsBean.goodsNum + "<font color=\"#999999\"<small><small><small>份</small></small></small></font> ";
                    holder.tvResidue.setText(Html.fromHtml(html));
                }
            }

        }


        return view;
    }


    class ViewHolder {
        private final TextView tvFlashGoodLabel;
        RelativeLayout rlDefault;
        ImageView ivFlashSale;
        TextView tvState;
        TextView tvTitle;
        TextView tvIntro;
        TextView isState;
        TextView tvPrice;
        TextView tvResidue;
        ProgressBar progressbar;
        LinearLayout llNextContent;
        TextView tvNextTitle;
        TextView tvNextDes;
        TextView tvNextPrice;
        TextView tvNextResidue;
        CountdownView countdownView;

        public ViewHolder(View view) {
            this.rlDefault = (RelativeLayout) view.findViewById(R.id.rldefault);
            this.ivFlashSale = (ImageView) view.findViewById(R.id.iv_flash);
            this.tvState = (TextView) view.findViewById(R.id.tv_empty);
            this.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            this.tvIntro = (TextView) view.findViewById(R.id.tv_intor);
            this.isState = (TextView) view.findViewById(R.id.tv_isState);
            this.tvPrice = (TextView) view.findViewById(R.id.tv_price);
            this.tvResidue = (TextView) view.findViewById(R.id.tv_residue);
            this.progressbar = (ProgressBar) view.findViewById(R.id.flash_progressbar);
            this.llNextContent = (LinearLayout) view.findViewById(R.id.ll_nextContent);
            this.tvNextTitle = (TextView) view.findViewById(R.id.tv_next_title);
            this.tvNextDes = (TextView) view.findViewById(R.id.tv_next_dis);
            this.tvNextPrice = (TextView) view.findViewById(R.id.tv_next_price);
            this.tvNextResidue = (TextView) view.findViewById(R.id.tv_next_residue);
            this.countdownView = (CountdownView) view.findViewById(R.id.countdownview);
            this.tvFlashGoodLabel = (TextView) view.findViewById(R.id.tv_flash_good_label);
        }
    }

}
