package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BarterOrderInfo;
import com.yilian.mall.entity.RefundOrderInfo;
import com.yilian.mall.entity.ReturnAddress;
import com.yilian.mall.http.AfterSaleNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.GoodsType;

public class AfterSaleThreeActivity extends BaseActivity {

    BarterOrderInfo.Barter barter;
    RefundOrderInfo.Refund refund;
    ReturnAddress address;
    String orderId;
    String filialeId;
    String orderGoodsIndex;
    String extra = "0";//初始化为"0",防止再次申请售后，到达ApplyAfterSaleActivity页面时空指针
    AfterSaleNetRequest netRequest;
    String url, name, norms, count, id, time, index, reason;
    int goodsType, status;
    String price, cost;
    @ViewInject(R.id.tv_back)
    private TextView tvBack;
    @ViewInject(R.id.right_textview)
    private TextView tvRight;
    @ViewInject(R.id.iv_goods)
    private ImageView ivGoods; //商品图片
    @ViewInject(R.id.iv_type)
    private ImageView ivGoodsType;
    @ViewInject(R.id.tv_goods_name)
    private TextView tvGoodsName; // 商品名称
    @ViewInject(R.id.tv_goods_norms)
    private TextView tvGoodsNorms; // 商品规格 文字描述

    //    @ViewInject(R.id.tv_icon_fen0)
//    private TextView tvIconFen0;
//    @ViewInject(R.id.tv_goods_integral)
//    private TextView tvIconQuan0;
    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice; // 价格
    //    @ViewInject(R.id.tv_goods_integral)
//    private TextView tvGoodsIntegral;
    @ViewInject(R.id.tv_goods_coupon0)//抵扣券价格
    private TextView tvGoodsCoupon0;
    @ViewInject(R.id.tv_goods_count)
    private TextView tvGoodsCount; // 商品数量
    @ViewInject(R.id.tv_id)
    private TextView tvId; //售后订单
    @ViewInject(R.id.tv_own_id)
    private TextView tvOwnId; //原订单号
    @ViewInject(R.id.tv_type)
    private TextView tvType;
    @ViewInject(R.id.tv_status)
    private TextView tvStatus; // 订单状态
    @ViewInject(R.id.tv_time)
    private TextView tvTime; // 拒绝时间
    @ViewInject(R.id.tv_reason)
    private TextView tvReason; // 拒绝原因
    @ViewInject(R.id.tv_goods_beans)
    private TextView tvGoodsBeans; // 送益豆
    @ViewInject(R.id.tv_subsidy)
    private TextView tvSubsidy; // 平台加赠益豆
    /**
     * 奖券购/易划算
     */
    @ViewInject(R.id.iv_yhs_icon)
    private ImageView ivYhsIcon;
    private int type;
    private String newOrderId, ownId;
    private String goodsIntegral;
    private String payStyle;
    private String goodsRetail;

    private String givenBean;
    private String subsidy;
    private String refundQuanMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_three);
        ViewUtils.inject(this);
        netRequest = new AfterSaleNetRequest(mContext);
        type = getIntent().getIntExtra("type", 0);
        orderId = getIntent().getStringExtra("orderId");
        filialeId = getIntent().getStringExtra("filialeId");

        getData();
    }

    @Override
    public void onBack(View view) {
        finish();
    }

    private void getData() {
        // 0换货返修 1退货
        switch (type) {
            case 0:
                getBarterOrderInfo();
                tvType.setText("换货/返修");
                break;

            case 1:
                getRefundOrderInfo();
                tvType.setText("退货");
                break;

            default:
                break;
        }
    }

    /**
     * 获取 返修/换货 订单信息
     */
    public void getBarterOrderInfo() {
        netRequest.barterOrderInfo(orderId, filialeId, new RequestCallBack<BarterOrderInfo>() {
            @Override
            public void onSuccess(ResponseInfo<BarterOrderInfo> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:

                        barter = responseInfo.result.barter;
                        address = barter.returnAddress;
                        index = barter.barterIndex;
                        orderGoodsIndex = barter.orderGoodsIndex;
                        id = barter.barterOrder;
                        url = barter.barterGoodsIcon;
                        ownId = barter.barterNum;
                        newOrderId = barter.barterId;
                        name = barter.barterGoodsName;
                        norms = barter.barterGoodsNorms;
                        price = barter.barterGoodsPrice;
                        cost = barter.barterGoodsCost;
                        count = barter.barterGoodsCount;
                        goodsType = barter.barterGoodsType;
                        time = barter.checkTime;
                        status = barter.barterStatus;
                        reason = barter.barterRefuse;
                        goodsIntegral = barter.goodsIntegral;
                        payStyle = barter.payStyle;
                        goodsRetail = barter.goodsRetail;
                        givenBean = barter.returnBean;
                        subsidy = barter.subsidy;
                        //代购券
//                        refundQuanMoney = barter.barterQuan;
                        refundQuanMoney = "0";
                        initView();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("获取信息失败");

            }
        });
    }

    /**
     * 获取 退货 订单信息
     */
    private void getRefundOrderInfo() {
        netRequest.refundOrderInfo(orderId, filialeId, new RequestCallBack<RefundOrderInfo>() {
            @Override
            public void onSuccess(ResponseInfo<RefundOrderInfo> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        refund = responseInfo.result.refund;
                        address = refund.returnAddress;
                        index = refund.refundIndex;
                        orderGoodsIndex = refund.orderGoodsIndex;
                        id = refund.refundOrder;
                        ownId = refund.refundNum;
                        extra = refund.extra;
                        url = refund.refundGoodsIcon;
                        name = refund.refundGoodsName;
                        norms = refund.refundGoodsNorms;
                        price = refund.refundGoodsPrice;
                        cost = refund.refundGoodsCost;
                        count = refund.refundGoodsCount;
                        goodsType = refund.refundGoodsType;
                        time = refund.checkTime;
                        status = refund.refundStatus;
                        reason = refund.refundRefuse;
                        newOrderId = refund.refundId;
                        goodsIntegral = refund.goodsIntegral;
                        payStyle = refund.payStyle;
                        goodsRetail = refund.goodsRetail;
                        givenBean = refund.returnBean;
                        subsidy = refund.subsidy;
//                        该字段是退货专属
//                        refundQuanMoney = refund.refundQuanMoney;
                        refundQuanMoney = "0";
                        initView();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("获取信息失败");
            }
        });
    }

    private void initView() {
        Drawable drawable = null;
        GlideUtil.showImageWithSuffix(mContext, url, ivGoods);
        tvBack.setText("售后订单详情");
        tvRight.setText("再次申请售后");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAfterSale();
            }
        });


        switch (type) {
            case 0://返修/换货
                Logger.i("0000000000000");
                setPrice(barter.payStyle, barter.barterGoodsCost, barter.returnIntegral, barter.barterGoodsPrice, barter.goodsIntegral, "", barter.goodsRetail, barter.returnBean, barter.subsidy);
                break;
            case 1://退货
                Logger.i("11111111111111");
                setPrice(refund.payStyle, refund.refundGoodsCost, refund.returnIntegral, refund.refundGoodsPrice, refund.goodsIntegral, refund.refundGoodsPrice, refund.goodsRetail, refund.returnBean, refund.subsidy);
                break;
        }

        tvOwnId.setText("原订单号：" + ownId);
        tvGoodsName.setText(name);
        tvGoodsNorms.setText(norms);
        tvGoodsCount.setText("x " + count);
        tvId.setText("订单编号：" + id);
        tvTime.setText("拒绝时间：" + StringFormat.formatDate(time));
        tvReason.setText(reason);
        tvStatus.setText("审核拒绝");


        tvGoodsCoupon0.setText(" + " + MoneyUtil.getXianJinQuan(Integer.valueOf(price) - Integer.valueOf(cost)));
//        tvIconQuan0.setVisibility(View.VISIBLE);


    }

    private void setPrice(String payStyle, String barterGoodsCost, String returnIntegral, String barterGoodsPrice, String goodsIntegral, String refundGoodsPrice, String goodsRetail, String returnBean, String subsidy) {
        Logger.i("type  " + payStyle + " cost  " + cost + "  returnIntegral  " + returnIntegral + "  price  " + price + "  goodsIntegral  " +
                goodsIntegral + " refundGoodsPrice  " + refundGoodsPrice + " goodsRetail " + goodsRetail);
        switch (payStyle) {
            case "0":
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(cost)));
//                tvIconFen0.setVisibility(View.VISIBLE);
//                tvGoodsIntegral.setVisibility(View.GONE);
//                tvIconFen0.setText(getString(R.string.back_score)+MoneyUtil.getLeXiangBi(returnIntegral));
                tvGoodsCoupon0.setText(" + " + MoneyUtil.getXianJinQuan(Integer.valueOf(price) - Integer.valueOf(cost)));
                break;
            case "1":
                tvGoodsPrice.setVisibility(View.GONE);
//                tvIconFen0.setVisibility(View.GONE);
                ivYhsIcon.setVisibility(View.VISIBLE);
                ivYhsIcon.setImageResource(R.mipmap.icon_yhs);
//                tvGoodsIntegral.setVisibility(View.VISIBLE);
//                tvGoodsIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(goodsIntegral)));
                break;
            case "2":
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(goodsRetail)));
//                tvIconFen0.setVisibility(View.GONE);
                ivYhsIcon.setVisibility(View.VISIBLE);
                ivYhsIcon.setImageResource(R.mipmap.icon_jfg);
//                tvGoodsIntegral.setVisibility(View.VISIBLE);
//                float integral = Float.parseFloat(refundGoodsPrice) - Float.parseFloat(goodsRetail);
//                tvGoodsIntegral.setText("+"+MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(integral)));
            case GoodsType.CAL_POWER: //区块益豆专区商品
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(cost)));
                tvGoodsBeans.setVisibility(View.VISIBLE);
                tvGoodsBeans.setText("送 " + MoneyUtil.getLeXiangBi(returnBean));
                if (!TextUtils.isEmpty(subsidy) && Double.valueOf(subsidy) != 0) {
                    tvSubsidy.setVisibility(View.VISIBLE);
                    tvSubsidy.setText("平台加赠" + Constants.APP_PLATFORM_DONATE_NAME + " " + MoneyUtil.getLeXiangBi(subsidy));
                } else {
                    tvSubsidy.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    public void applyAfterSale() {
        Intent intent = new Intent(this, ApplyAfterSaleActivity.class);
        intent.putExtra("AgainAfterSale", true);
        intent.putExtra("goods_name", name);//商品名称
        intent.putExtra("goods_count", com.yilian.mylibrary.NumberFormat.convertToInt(count, 1));//商品数量
        intent.putExtra("goods_price", price);//商品市场价
        intent.putExtra("order_goods_norms", norms);//商品规格
        intent.putExtra("goods_cost", cost);//商品销售价格
        intent.putExtra("order_left_extra", extra);//由于抵扣券不足，用户使用乐享币垫付的抵扣券金额
        intent.putExtra("goods_img_url", url);//商品图片URL
        intent.putExtra("goods_type", goodsType);//商品类型
        intent.putExtra("order_id", newOrderId);//订单Id
        intent.putExtra("order_goods_index", orderGoodsIndex);//商品订单自增编号
        intent.putExtra("goodsIntegral", goodsIntegral);
        intent.putExtra("payStyle", payStyle);
        intent.putExtra("goodsRetail", goodsRetail);
        intent.putExtra("givenBean", givenBean);
        intent.putExtra("subsidy", subsidy);
        intent.putExtra("each_di_gou_quan_money", refundQuanMoney);
        startActivity(intent);


//        重新申请售后时，取消直接提交该订单的售后，而是重新回到售后信息编辑界面
//        netRequest.recreate(orderId, type, new RequestCallBack<BaseEntity>() {
//            @Override
//            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
//                switch (responseInfo.result.code) {
//                    case 1:
//                        showToast("重新申请成功");
//                        finish();
//                        break;
//
//                    case 0:
//                    case -3:
//                        showToast("繁忙");
//                        break;
//
//                    case -4:
//                        showToast("登录状态失效请重新登录");
//                        break;
//
//                    default:
//                        showToast("获取数据失败");
//                        break;
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                showToast("网络连接失败");
//            }
//        });
    }
}
