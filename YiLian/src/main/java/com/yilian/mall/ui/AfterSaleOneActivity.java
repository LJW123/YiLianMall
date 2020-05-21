package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.entity.BarterOrderInfo;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.RefundOrderInfo;
import com.yilian.mall.entity.ReturnAddress;
import com.yilian.mall.http.AfterSaleNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

/**
 * 售后订单详情
 */
public class AfterSaleOneActivity extends BaseActivity {

    BarterOrderInfo.Barter barter;
    RefundOrderInfo.Refund refund;
    ReturnAddress address;
    AfterSaleNetRequest netRequest;
    String url, name, norms, count, id, ownId, time, msg, index, orderId, filialeId;
    int goodsType, status, type;
    String price;
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
    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice; // 商品价格
    @ViewInject(R.id.tv_goods_count)
    private TextView tvGoodsCount; // 商品数量
    @ViewInject(R.id.tv_id)
    private TextView tvId; //售后订单
    @ViewInject(R.id.tv_own_id)
    private TextView tvOwnId; //原订单号
    @ViewInject(R.id.tv_time)
    private TextView tvTime; // 申请时间
    @ViewInject(R.id.tv_subTitle)
    private TextView tvInfo; // 售后信息
    @ViewInject(R.id.tv_msg)
    private TextView tvMsg; //备注信息
    @ViewInject(R.id.tv_status)
    private TextView tvStatus; // 订单状态
    @ViewInject(R.id.tv_bt)
    private TextView tvBt;
    @ViewInject(R.id.bt)
    private Button btCommit;
//    @ViewInject(R.id.tv_icon_fen0)
//    private TextView tvIconFen0;
//    @ViewInject(R.id.tv_goods_coupon0)
//    private TextView tvGoodsCoupon0;

    @ViewInject(R.id.tv_goods_beans)
    private TextView tvGoodsBeans; // 送益豆
    @ViewInject(R.id.tv_subsidy)
    private TextView tvSubsidy; // 平台加赠益豆

    /**
     * 奖券购/易划算
     */
    @ViewInject(R.id.iv_yhs_icon)
    private ImageView ivYhsIcon;
//    @ViewInject(R.id.tv_goods_integral)
//    private TextView tvGoodsIntegral;

    private String cost;
    private String returnIntegral;//销售奖券
    private String payStyle;
    private String goodsIntegral;
    private String goodsRetail;
    private String givenBean;
    private String subsidy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_one);
        ViewUtils.inject(this);
        netRequest = new AfterSaleNetRequest(mContext);
        type = getIntent().getIntExtra("type", 0);
        orderId = getIntent().getStringExtra("orderId");
        filialeId = getIntent().getStringExtra("filialeId");
        if (filialeId == null) {
            filialeId = "";
        }
        getData();
    }

    @Override
    public void onBack(View view) {
        finish();
    }

    private void getData() {
        // 0换货返修 1退货

        startMyDialog();
        switch (type) {
            case 0:
                getBarterOrderInfo();
                break;
            case 1:
                getRefundOrderInfo();
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
                stopMyDialog();
                Logger.i("获取 返修/换货 订单信息  " + responseInfo.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        barter = responseInfo.result.barter;
                        address = barter.returnAddress;
                        index = barter.barterIndex;

                        id = barter.barterOrder;
                        ownId = barter.barterNum;
                        url = barter.barterGoodsIcon;
                        name = barter.barterGoodsName;
                        norms = barter.barterGoodsNorms;
                        price = barter.barterGoodsPrice;
                        count = barter.barterGoodsCount;
                        goodsType = barter.barterGoodsType;
                        time = barter.barterTime;
                        msg = barter.barterRefuse;
                        status = barter.barterStatus;
                        cost = barter.barterGoodsCost;
                        returnIntegral = barter.returnIntegral;
                        payStyle = barter.payStyle;
                        goodsIntegral = barter.goodsIntegral;
                        goodsRetail = barter.goodsRetail;
                        givenBean = barter.returnBean;
                        subsidy = barter.subsidy;
                        initView();
                        break;
                    default:
                        showToast(responseInfo.result.msg);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("获取信息失败");
                stopMyDialog();
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
                        Logger.i(" 获取 退货 订单信息   " + responseInfo.toString());
                        refund = responseInfo.result.refund;
                        address = refund.returnAddress;
                        index = refund.refundIndex;
                        id = refund.refundOrder;
                        ownId = refund.refundNum;
                        url = refund.refundGoodsIcon;
                        name = refund.refundGoodsName;
                        norms = refund.refundGoodsNorms;
                        price = refund.refundGoodsPrice;
                        count = refund.refundGoodsCount;
                        goodsType = refund.refundGoodsType;
                        time = refund.refundTime;
                        msg = refund.refundRefuse;
                        status = refund.refundStatus;
                        cost = refund.refundGoodsCost;
                        returnIntegral = refund.returnIntegral;
                        payStyle = refund.payStyle;
                        goodsIntegral = refund.goodsIntegral;
                        goodsRetail = refund.goodsRetail;
                        givenBean = refund.returnBean;
                        subsidy = refund.subsidy;
                        initView();
                        break;
                    default:
                        showToast(responseInfo.result.msg);

                        break;

                }

                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("获取信息失败");
                stopMyDialog();
            }
        });
    }

    private void initView() {
        Drawable drawable = null;
        GlideUtil.showImageWithSuffix(mContext, url, ivGoods);
        tvBack.setText("售后订单详情");
        tvRight.setText("取消售后");
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        tvGoodsName.setText(name);
        tvGoodsNorms.setText(norms);
        tvGoodsCount.setText("x " + count);
        tvId.setText("售后编号：" + id);
        tvOwnId.setText("原订单号：" + ownId);
        tvTime.setText("申请时间：" + StringFormat.formatDate(time));
        tvMsg.setText(msg == null ? "暂无" : msg);
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterSaleOneActivity.this, CommitExpressNumActivity.class);
                intent.putExtra("order_id", orderId);
                intent.putExtra("order_type", type);
                startActivityForResult(intent, Constants.COMMIT_EXPRESS_NUM_ACTIVITY);
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
            default:
                break;

        }

        switch (status) {
            case 1:
                tvRight.setVisibility(View.VISIBLE);
                tvBt.setVisibility(View.VISIBLE);
                btCommit.setVisibility(View.GONE);
                tvInfo.setText("您的订单将在3个工作日内进行审核");
                tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>待审核</fond><font color='#000000'>)</fond>"));
                break;
            case 3:
//                2018-06-04 下午 服务器波哥提出,售后待用户发货期间不能取消售后申请.
                tvRight.setVisibility(View.GONE);
                tvBt.setVisibility(View.VISIBLE);
                btCommit.setVisibility(View.VISIBLE);
                drawable = mContext.getResources().getDrawable(R.mipmap.right_sanjiao);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvStatus.setCompoundDrawables(null, null, drawable, null);
                tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>待退货</fond><font color='#000000'>)</fond>"));
                tvInfo.setText("请将商品邮寄至：" + address.address + " " + " " + address.contact + " " + " " + address.tel);
                tvStatus.setClickable(true);
                break;
            case 4:
                tvRight.setVisibility(View.GONE);
                tvBt.setVisibility(View.GONE);
                btCommit.setVisibility(View.GONE);
                drawable = mContext.getResources().getDrawable(R.mipmap.right_sanjiao);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvStatus.setCompoundDrawables(null, null, drawable, null);
                if (type == 1) {
                    tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>退货中</fond><font color='#000000'>)</fond>"));
                } else {
                    tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>换货中/维修中</fond><font color='#000000'>)</fond>"));
                }
                tvInfo.setText("请将商品邮寄至：" + address.address + " " + " " + address.contact + " " + " " + address.tel);
                tvStatus.setClickable(true);
                break;
            case 5:
                tvRight.setVisibility(View.GONE);
                tvBt.setVisibility(View.GONE);
                btCommit.setVisibility(View.GONE);
                drawable = mContext.getResources().getDrawable(R.mipmap.right_sanjiao);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvStatus.setCompoundDrawables(null, null, drawable, null);
                tvInfo.setText("请将商品邮寄至：" + address.address + " " + " " + address.contact + " " + " " + address.tel);
                tvStatus.setClickable(true);
                if (type == 1) {
                    tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>待退款</fond><font color='#000000'>)</fond>"));
                } else {
                    tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>待发货</fond><font color='#000000'>)</fond>"));
                }
                tvStatus.setClickable(true);
                break;
            case 6:
                tvRight.setVisibility(View.GONE);
                tvBt.setVisibility(View.GONE);
                btCommit.setVisibility(View.GONE);
                drawable = mContext.getResources().getDrawable(R.mipmap.right_sanjiao);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvStatus.setCompoundDrawables(null, null, drawable, null);
                tvInfo.setText("请将商品邮寄至：" + address.address + " " + " " + address.contact + " " + " " + address.tel);
                tvStatus.setClickable(true);
                if (type == 1) {
                    tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>已完成</fond><font color='#000000'>)</fond>"));
                } else {
                    tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>待收货</fond><font color='#000000'>)</fond>"));
                }
                tvStatus.setClickable(true);
                break;
            case 7:
                tvRight.setVisibility(View.GONE);
                tvBt.setVisibility(View.GONE);
                btCommit.setVisibility(View.GONE);
                drawable = mContext.getResources().getDrawable(R.mipmap.right_sanjiao);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvStatus.setCompoundDrawables(null, null, drawable, null);
                tvStatus.setText(Html.fromHtml("订单状态(<font color='#f75a53'>已完成</fond><font color='#000000'></fond>)"));
                tvInfo.setText("请将商品邮寄至：" + address.address + " " + " " + address.contact + " " + " " + address.tel);
                tvStatus.setClickable(true);
                break;
            default:
                break;

        }
        stopMyDialog();
    }

    private void setPrice(String payStyle, String cost, String returnIntegral, String price, String goodsIntegral, String refundGoodsPrice, String goodsRetail, String givenBean, String subsidy) {
        Logger.i("type  " + payStyle + " cost  " + cost + "  returnIntegral  " + returnIntegral + "  price  " + price + "  goodsIntegral  " +
                goodsIntegral + " refundGoodsPrice  " + refundGoodsPrice + " goodsRetail " + goodsRetail);
        switch (payStyle) {
            case "0":
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(cost)));
//                tvIconFen0.setVisibility(View.VISIBLE);
//                tvGoodsIntegral.setVisibility(View.GONE);
//                tvIconFen0.setText(getString(R.string.back_score) + MoneyUtil.getLeXiangBi(returnIntegral));
//                tvGoodsCoupon0.setText(" + " + MoneyUtil.getXianJinQuan(Integer.valueOf(price) - Integer.valueOf(cost)));
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
//                float integral = Float.parseFloat(price) - Float.parseFloat(goodsRetail);
//                tvGoodsIntegral.setText("+" + MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(integral)));
                break;
            case "3":
            case GoodsType.CAL_POWER: //区块益豆专区商品
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(cost)));
                tvGoodsBeans.setVisibility(View.VISIBLE);
                tvGoodsBeans.setText("送 " + MoneyUtil.getLeXiangBi(givenBean));
                if (!TextUtils.isEmpty(subsidy) && Double.valueOf(subsidy) != 0) {
                    tvSubsidy.setVisibility(View.VISIBLE);
                    tvSubsidy.setText("平台加赠"+Constants.APP_PLATFORM_DONATE_NAME+" " + MoneyUtil.getLeXiangBi(subsidy));
                } else {
                    tvSubsidy.setVisibility(View.GONE);
                }
                break;
            default:
                break;

        }
    }

    public void cancel() {

        showDialog(null, "确认取消售后?", null, 0, Gravity.NO_GRAVITY, "确定", "取消", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        cancelAfterSale(dialog);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    default:
                        break;

                }
            }
        }, mContext);

    }

    /**
     * 取消售后
     */
    private void cancelAfterSale(final DialogInterface dialog) {
        startMyDialog();
        netRequest.cancel(index, type, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                dialog.dismiss();
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        //刷新个人页面标识
                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                        showToast("取消成功");
                        finish();
                        break;
                    default:
                        showToast("取消失败");
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                stopMyDialog();
            }
        });
    }

    public void route(View view) {
        Intent intent = new Intent(this, AfterSaleRouteActivity.class);
        intent.putExtra("norms", norms);
        intent.putExtra("url", url);
        intent.putExtra("goods_name", name);
        intent.putExtra("goods_count", count);
        intent.putExtra("price", String.valueOf(price));
        intent.putExtra("goods_type", goodsType);
        intent.putExtra("order_type", type);
        intent.putExtra("order_id", orderId);
        intent.putExtra("time", time);
        intent.putExtra("id", id);
        intent.putExtra("index", index);
        intent.putExtra("cost", cost);
        intent.putExtra("own_id", ownId);
        intent.putExtra("return_integral", returnIntegral);
        intent.putExtra("payStyle", payStyle);
        intent.putExtra("goodsIntegral", goodsIntegral);
        intent.putExtra("goodsRetail", goodsRetail);
        intent.putExtra("givenBean", givenBean);
        intent.putExtra("subsidy", subsidy);
        startActivityForResult(intent, Constants.AFTER_SALE_ROUTE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.COMMIT_EXPRESS_NUM_ACTIVITY && resultCode == RESULT_OK) {
            AfterSaleOneActivity.this.finish();
        }
        if (requestCode == Constants.AFTER_SALE_ROUTE_ACTIVITY && resultCode == RESULT_OK) {
            AfterSaleOneActivity.this.finish();
        }
    }
}
