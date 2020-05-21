package com.yilian.mall.jd.adapter;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.jd.JDOrderListEntity;


public class JDOrderCommonAdapter extends BaseQuickAdapter<JDOrderListEntity.DataBean, BaseViewHolder> {

    private ImageView ivOrderCardTab;
    private ImageView ivOrderFinishTab;
    private TextView tvOrderStatus;
    private View viewLine;
    private LinearLayout llOrderDelete;
    private TextView tvBuyAgain, tvConfirm, tvPay, tvApplyBalance;

    private float downX, downY;

    public JDOrderCommonAdapter() {
        super(R.layout.jd_item_order_common);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(BaseViewHolder helper, JDOrderListEntity.DataBean item) {

        //************添加点击监听**********************
        //删除订单
        helper.addOnClickListener(R.id.ll_order_delete);
        //再次购买
        helper.addOnClickListener(R.id.tv_buy_again);
        //确认收货
        helper.addOnClickListener(R.id.tv_confirm);
        //去支付
        helper.addOnClickListener(R.id.tv_pay);

        LinearLayout llOrderInfo = helper.getView(R.id.ll_order_info);
        //************初始 状态相关布局************
        ivOrderCardTab = helper.getView(R.id.iv_order_card_tab);
        if(item.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
            ivOrderCardTab.setVisibility(View.VISIBLE);
        }else {
            ivOrderCardTab.setVisibility(View.GONE);
        }
        ivOrderFinishTab = helper.getView(R.id.iv_order_finish_tab);
        ivOrderFinishTab.setVisibility(View.GONE);
        tvOrderStatus = helper.getView(R.id.tv_order_status);
        tvOrderStatus.setTextColor(Color.parseColor("#F72D42"));
        tvOrderStatus.setVisibility(View.GONE);
        viewLine = helper.getView(R.id.view_line);
        viewLine.setVisibility(View.GONE);
        llOrderDelete = helper.getView(R.id.ll_order_delete);
        llOrderDelete.setVisibility(View.GONE);
        tvBuyAgain = helper.getView(R.id.tv_buy_again);
        tvBuyAgain.setTextColor(Color.parseColor("#F72D42"));
        tvBuyAgain.setBackgroundResource(R.drawable.jd_order_bt_bg_red);
        tvBuyAgain.setVisibility(View.GONE);
        tvConfirm = helper.getView(R.id.tv_confirm);
        tvConfirm.setVisibility(View.GONE);
        tvPay = helper.getView(R.id.tv_pay);
        tvPay.setVisibility(View.GONE);
        tvApplyBalance = helper.getView(R.id.tv_apply_balance);
        tvApplyBalance.setVisibility(View.GONE);


        LinearLayout llGoodsInfo = helper.getView(R.id.ll_goods_info);
        llGoodsInfo.setVisibility(View.GONE);
        RecyclerView rvGoodsList = helper.getView(R.id.rv_goods_list);
        rvGoodsList.setVisibility(View.GONE);

        int goodsNum = 0;
        if (item.goodsist != null) {
            if (item.goodsist.size() == 1) {
                GlideUtil.showImageWithSuffix(mContext, JDImageUtil.getJDImageUrl_N3(item.goodsist.get(0).getSkuPic()), helper.getView(R.id.iv_goods_img));
                helper.setText(R.id.tv_goods_name, item.goodsist.get(0).getSkuName());
                goodsNum = item.goodsist.get(0).getSkuNum();
                llGoodsInfo.setVisibility(View.VISIBLE);
            } else {
                for (JDOrderListEntity.GoodsList goods : item.goodsist) {
                    goodsNum += goods.getSkuNum();
                }
                rvGoodsList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                JDOrderCommonGoodsListAdapter goodsListAdapter = new JDOrderCommonGoodsListAdapter(item.goodsist);
                rvGoodsList.setAdapter(goodsListAdapter);
                rvGoodsList.setVisibility(View.VISIBLE);

                rvGoodsList.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        //Logger.i(" RecyclerView触摸走了:  " + action);
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                downX = event.getRawX();
                                downY = event.getRawY();
                                //Logger.i(" RecyclerView触摸DOWN:DOWN X:  " + downX+"  Y  " + downY);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            case MotionEvent.ACTION_UP:
                                int mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
                                float xDistance = Math.abs(downX - event.getRawX());
                                float yDistance = Math.abs(downY - event.getRawY());
                                //Logger.i(" RecyclerView触摸UP:偏移 X:  " + xDistance+"  Y  " + yDistance);
                                if (xDistance < mTouchSlop && yDistance < mTouchSlop) {
                                    //模拟父控件的点击
                                    llOrderInfo.performClick();
                                }
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        }
        //************订单商品数量 订单价格************
        helper.setText(R.id.tv_goods_num, "共" + goodsNum + "件商品 需付款：");
        helper.setText(R.id.tv_order_price, "¥" + MyBigDecimal.add(item.orderJdPrice, item.freight));

        //************根据状态处理不同展示************
        setState(item.type, item.settleStatus, item.applySettle,item.jdType);
    }

    /**
     * 订单item不同类型 展示处理
     *
     * @param type         0 只显示再次购买按钮 原则上不存在 ,1 待付款 ,2 待收货, 3 已完成, 4 已取消
     * @param settleStatus 结算状态 0未结算  1已结算
     * @param applySettle  0 不可申请结算 1 可申请结算
     * @param jdType  类型 0普通京东商品 1购物卡京东商品
     */
    private void setState(int type, int settleStatus, int applySettle,int jdType) {
        int other = 0;
        //1 待付款
        int waitPay = 1;
        //2 待收货
        int waitReceiving = 2;
        //3 已完成
        int completed = 3;
        //4 已取消
        int canceled = 4;

        if (type == waitPay) {
            tvOrderStatus.setText("等待付款");
            tvOrderStatus.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.VISIBLE);
        } else if (type == waitReceiving) {
            tvOrderStatus.setText("等待收货");
            tvOrderStatus.setVisibility(View.VISIBLE);
            tvConfirm.setVisibility(View.VISIBLE);
            tvBuyAgain.setTextColor(Color.parseColor("#333333"));
            tvBuyAgain.setBackgroundResource(R.drawable.jd_order_bt_bg_gry);
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else if (type == completed) {
            //已完成
            ivOrderFinishTab.setVisibility(View.VISIBLE);
            llOrderDelete.setVisibility(View.VISIBLE);
            tvBuyAgain.setVisibility(View.VISIBLE);
            //已完成 且 为普通商品 展示结算
            if(jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_COMMON){
                //结算状态
                if (settleStatus == 1) {
                    tvApplyBalance.setText("益豆已结算");
                    tvApplyBalance.setTextColor(Color.parseColor("#999999"));
                    tvApplyBalance.setBackgroundResource(R.drawable.jd_order_bt_bg_gry);
                    tvApplyBalance.setVisibility(View.VISIBLE);
                }
                // 0 不可申请结算 1 可申请结算
                if (applySettle == 1) {
                    tvApplyBalance.setText("申请益豆");
                    tvApplyBalance.setTextColor(Color.parseColor("#F72D42"));
                    tvApplyBalance.setBackgroundResource(R.drawable.jd_order_bt_bg_red);
                    tvApplyBalance.setVisibility(View.VISIBLE);
                }
                tvApplyBalance.setClickable(true);
            }

        } else if (type == canceled) {
            tvOrderStatus.setText("已取消");
            tvOrderStatus.setTextColor(Color.parseColor("#999999"));
            tvOrderStatus.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
            llOrderDelete.setVisibility(View.VISIBLE);
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else if (type == other) {
            tvBuyAgain.setTextColor(Color.parseColor("#333333"));
            tvBuyAgain.setBackgroundResource(R.drawable.jd_order_bt_bg_gry);
            tvBuyAgain.setVisibility(View.VISIBLE);
        }
    }


}
