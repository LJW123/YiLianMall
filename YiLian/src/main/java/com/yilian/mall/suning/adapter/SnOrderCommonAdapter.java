package com.yilian.mall.suning.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.suning.SnOrderListEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import rx.Subscription;
import rx.functions.Action1;


public class SnOrderCommonAdapter extends BaseQuickAdapter<SnOrderListEntity.DataBean, BaseViewHolder> {

    public List<BaseViewHolder> myViewHolderList = new ArrayList<>();

    //服务器系统时间
    private long serviceSystemTime = 0;
    //付款剩余总时长
    private long totalDuration = 0;
    //系统时间与本地时间的差值
    private long marginTime = 0;
    private TextView tvOrderStatus;
    private LinearLayout llCountDown;

    private TextView tvDelete, tvBuyAgain, tvCancel, tvPay, tvApplyBalance;
    public HashMap<Integer, Subscription> countDownsMap = new HashMap<>();
    private SimpleDateFormat sdf;

    public SnOrderCommonAdapter() {
        super(R.layout.sn_item_order_common);
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

    private String formatTime(long duration) {
        String format = "HH小时mm分钟ss秒";
        if (sdf==null) {
            sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        }
     return sdf.format(duration * 1000);
    }

    @Override
    protected void convert(BaseViewHolder helper, SnOrderListEntity.DataBean item) {

//        Logger.i("布局item " + getData().get(helper.getAdapterPosition()).snOrderId + "   BaseViewHolder: " + helper.hashCode());
        //判断list里面是否含有该helper，没有就增加
        //因为list已经持有helper的引用，所有数据自动会改变
        if (!(myViewHolderList.contains(helper))) {
            myViewHolderList.add(helper);
        }

        //************添加点击监听**********************
        //删除订单
        helper.addOnClickListener(R.id.tv_delete);
        //再次购买
        helper.addOnClickListener(R.id.tv_buy_again);
        //取消订单
        helper.addOnClickListener(R.id.tv_cancel);
        //去支付
        helper.addOnClickListener(R.id.tv_pay);
        //申请乐豆
        helper.addOnClickListener(R.id.tv_apply_balance);

        LinearLayout llOrderInfo = helper.getView(R.id.ll_order_info);
        //************初始 状态相关布局************
        tvOrderStatus = helper.getView(R.id.tv_order_status);

        llCountDown = helper.getView(R.id.ll_count_down);
        llCountDown.setVisibility(View.GONE);
        TextView tvCountDown = helper.getView(R.id.tv_count_down);

        tvDelete = helper.getView(R.id.tv_delete);
        tvDelete.setVisibility(View.GONE);
        tvBuyAgain = helper.getView(R.id.tv_buy_again);
        tvBuyAgain.setTextColor(mContext.getResources().getColor(R.color.main_black_text));
        tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_black);
        tvBuyAgain.setVisibility(View.GONE);
        tvCancel = helper.getView(R.id.tv_cancel);
        tvCancel.setVisibility(View.GONE);
        tvPay = helper.getView(R.id.tv_pay);
        tvPay.setVisibility(View.GONE);
        tvApplyBalance = helper.getView(R.id.tv_apply_balance);
        tvApplyBalance.setVisibility(View.GONE);

        RecyclerView rvGoodsList = helper.getView(R.id.rv_goods_list);
        rvGoodsList.setLayoutManager(new LinearLayoutManager(mContext));
        SnOrderCommonGoodsListAdapter goodsListAdapter = new SnOrderCommonGoodsListAdapter(item.goodsist);
        rvGoodsList.setAdapter(goodsListAdapter);
        rvGoodsList.setNestedScrollingEnabled(false);
        rvGoodsList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //模拟父控件的点击
                    llOrderInfo.performClick();
                }
                return false;
            }
        });
        //************订单价格 订单运费************
//        helper.setText(R.id.tv_order_price, "¥" + MyBigDecimal.add(String.valueOf(item.payment),item.coupon));
        helper.setText(R.id.tv_order_price, "¥" + String.valueOf(item.payment));
        helper.setText(R.id.tv_order_freight, "(含运费¥ " + item.freight + ")");
        //************根据状态处理不同展示************
        setState(item.orderStatus, item.settleStatus, item.applySettle, item.orderTime, tvCountDown);
    }

    /**
     * 订单item不同类型 展示处理
     *
     * @param orderStatus  订单状态 1:审核中; 2:待发货; 3:待收货; 4:已完成; 5:已取消; 6:已退货; 7:待处理; 8：审核不通过，订单已取消; 9：待支付
     * @param settleStatus 结算状态 0未结算  1已结算
     * @param applySettle  0 不可申请结算 1 可申请结算
     */
    private void setState(int orderStatus, int settleStatus, int applySettle, long orderTime, TextView tvCountDown) {
        if (orderStatus == 1 || orderStatus == 9) {
            /** 待支付*/
            tvOrderStatus.setText("待支付");

            //剩余支付时间 = 等待支付限制时间 - （当前本地时间 + 当时本地时间和当时服务器时间的差值 - 订单时间）
            long waitTime = totalDuration  - (System.currentTimeMillis() / 1000 + marginTime - orderTime) ;
            Subscription oldSubscription = countDownsMap.get(tvCountDown.hashCode());
            if (oldSubscription != null) {
                oldSubscription.unsubscribe();
                oldSubscription = null;
            }
            Subscription subscription = RxUtil.countDown((int) waitTime)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            tvCountDown.setText(formatTime(aLong));
                        }
                    });
            countDownsMap.put(tvCountDown.hashCode(), subscription);

            llCountDown.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.VISIBLE);
        } else if (orderStatus == 2) {
            /** 待发货*/
            tvOrderStatus.setText("待发货");
            tvCancel.setVisibility(View.VISIBLE);
            tvBuyAgain.setTextColor(mContext.getResources().getColor(R.color.color_main_suning));
            tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else if (orderStatus == 3) {
            /** 待收货*/
            tvOrderStatus.setText("待收货");
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else if (orderStatus == 4) {
            /** 已完成*/
            tvOrderStatus.setText("已完成");
            tvDelete.setVisibility(View.VISIBLE);
            tvBuyAgain.setVisibility(View.VISIBLE);
            if (settleStatus == 0 && applySettle == 1) {
                tvApplyBalance.setTextColor(mContext.getResources().getColor(R.color.color_main_suning));
                tvApplyBalance.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            } else {
                tvApplyBalance.setTextColor(mContext.getResources().getColor(R.color.notice_text_color));
                tvApplyBalance.setBackgroundResource(R.drawable.sn_order_bt_bg_grey);
            }
            tvApplyBalance.setVisibility(View.VISIBLE);
        } else if (orderStatus == 5 || orderStatus == 8) {
            /** 已取消*/
            tvOrderStatus.setText("已取消");
            tvDelete.setVisibility(View.VISIBLE);
            tvBuyAgain.setTextColor(mContext.getResources().getColor(R.color.color_main_suning));
            tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            tvBuyAgain.setVisibility(View.VISIBLE);
        } else {
            tvOrderStatus.setVisibility(View.GONE);
            if (orderStatus == 6) {
                /** 已退货*/
                tvOrderStatus.setText("已退货");
                tvOrderStatus.setVisibility(View.VISIBLE);
            }
            if (orderStatus == 7) {
                /** 待处理*/
                tvOrderStatus.setText("待处理");
                tvOrderStatus.setVisibility(View.VISIBLE);
            }
            tvBuyAgain.setTextColor(mContext.getResources().getColor(R.color.color_main_suning));
            tvBuyAgain.setBackgroundResource(R.drawable.sn_order_bt_bg_orange);
            tvBuyAgain.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 倒计时 时间展示
     */
//    public void countDown() {
//        //遍历list，刷新相应helper的TextView
//        for (int i = 0; i < myViewHolderList.size(); i++) {
//            //订单是待支付显示倒计时
//            //Logger.i("倒计时：订单状态  " + getData().get(i).orderStatus);
//            if (getData().get(i).orderStatus == 1 || getData().get(i).orderStatus == 9) {
//                /** 待支付*/
//                long waitTime = time - (System.currentTimeMillis() / 1000 + marginTime - getData().get(i).orderTime);
////                Logger.i("倒计时：  " + getData().get(i).snOrderId + "waitTime  " + waitTime);
//                if (waitTime > 0) {
//                    String format = "HH小时mm分钟ss秒";
//                    SimpleDateFormat sdf = new SimpleDateFormat(format);
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//                    String video_time = sdf.format(waitTime * 1000);
////                    Logger.i("倒计时：  " + getData().get(i).snOrderId + "待支付时间  " + video_time);
//                    //设置时间
////                    myViewHolderList.get(i).setText(R.id.tv_count_down, video_time);
//                    TextView tv_count_down = myViewHolderList.get(i).getView(R.id.tv_count_down);
////                    Logger.i("倒计时： 订单号" + getData().get(i).snOrderId + "   TextView: " + tv_count_down.hashCode() + "  待支付时间  " + video_time);
//                    tv_count_down.setText(video_time);
//                } else {
//                    myViewHolderList.get(i).setText(R.id.tv_count_down, "00小时00分钟00秒");
//                }
//            }
//        }
//    }
    public void setSysTime(long systemTime, long time, long marginTime) {
        this.serviceSystemTime = systemTime;
        this.totalDuration = time;
        this.marginTime = marginTime;
    }

    public void clearHolderList() {
        if (myViewHolderList != null) {
            myViewHolderList.clear();
        }
    }
}
