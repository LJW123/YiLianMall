package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.adapter.AfterSaleRoutingAdapter;
import com.yilian.mall.adapter.layoutManager.FullyLinearLayoutManager;
import com.yilian.mall.entity.BarterAfterSaleRouing;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.RefundAfterSaleRouing;
import com.yilian.mall.http.AfterSaleNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * 售后订单追踪
 */
public class AfterSaleRouteActivity extends BaseActivity {

    AfterSaleNetRequest netRequest;
    ArrayList<String> list = new ArrayList<>();
    String url, name, norms, count, id, time, index, orderId, ownId;
    int goodsType, status, type;
    String price;
    @ViewInject(R.id.tv_back)
    private TextView tvBack;
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
    //    @ViewInject(R.id.tv_icon_fen0)
//    private TextView tvIconFen0;
//    @ViewInject(R.id.tv_goods_integral)
//    private TextView tvGoodsIntegral;
    @ViewInject(R.id.tv_goods_coupon0)
    private TextView tvGoodsCoupon0;
    @ViewInject(R.id.tv_goods_count)
    private TextView tvGoodsCount; // 商品数量
    @ViewInject(R.id.tv_id)
    private TextView tvId; //售后订单
    @ViewInject(R.id.tv_type)
    private TextView tvType;
    @ViewInject(R.id.tv_status)
    private TextView tvStatus; // 订单状态
    @ViewInject(R.id.rv)
    private RecyclerView rv;
    @ViewInject(R.id.ll1)
    private LinearLayout ll1;
    @ViewInject(R.id.bt_take_delivery)
    private Button btTakeDelivery;
    @ViewInject(R.id.tv_own_id)
    private TextView tvOwnId;

    @ViewInject(R.id.iv_yhs_icon)
    private ImageView ivYhsIcon;

    @ViewInject(R.id.tv_goods_beans)
    private TextView tvGoodsBeans; // 送益豆
    @ViewInject(R.id.tv_subsidy)
    private TextView tvSubsidy; // 平台加赠益豆

    private String cost;
    private String returnIntegral;
    private String payStyle;
    private String goodsIntegral;
    private String goodsRetail;
    private String givenBean;
    private String subsidy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_route);
        ViewUtils.inject(this);
        netRequest = new AfterSaleNetRequest(mContext);
        Intent intent = getIntent();
        type = intent.getIntExtra("order_type", 0);
        Logger.i("售后订单追踪type   " + type);
        orderId = intent.getStringExtra("order_id");
        goodsType = intent.getIntExtra("goods_type", 3);
        name = intent.getStringExtra("goods_name");
        norms = intent.getStringExtra("norms");
        count = intent.getStringExtra("goods_count");
        price = intent.getStringExtra("price");
        time = intent.getStringExtra("time");
        id = intent.getStringExtra("id");
        ownId = intent.getStringExtra("own_id");
        url = intent.getStringExtra("url");
        index = intent.getStringExtra("index");
        cost = intent.getStringExtra("cost");
        returnIntegral = intent.getStringExtra("return_integral");
        payStyle = intent.getStringExtra("payStyle");
        goodsIntegral = intent.getStringExtra("goodsIntegral");
        goodsRetail = intent.getStringExtra("goodsRetail");
        givenBean = intent.getStringExtra("givenBean");
        subsidy = intent.getStringExtra("subsidy");


        getData();
        initView();
    }

    private void getData() {
        // 0换货返修 1退货

        startMyDialog();
        switch (type) {
            case 0:
                getBarterOrderStatus();
                tvType.setText("换货/返修");
                break;

            case 1:
                getRefundOrderStatus();
                tvType.setText("退货");
                break;

            default:
                break;
        }

        initView();
    }

    private void initView() {
        Drawable drawable = null;
        GlideUtil.showImageWithSuffix(mContext, url, ivGoods);
        tvBack.setText("售后订单踪迹");
        tvGoodsName.setText(name);
        tvGoodsNorms.setText(norms);
        tvGoodsCount.setText("x " + count);
        tvId.setText("订单编号：" + id);
        tvOwnId.setText("原订单号：" + ownId);

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
//                float integral = Float.parseFloat(price) - Float.parseFloat(goodsRetail);
//                tvGoodsIntegral.setText("+" + MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(integral)));
                break;
            case GoodsType.CAL_POWER: //区块益豆专区商品
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(cost)));
                tvGoodsBeans.setVisibility(View.VISIBLE);
                tvGoodsBeans.setText("送 " + MoneyUtil.getLeXiangBi(givenBean));
                if (!TextUtils.isEmpty(subsidy) && Double.valueOf(subsidy) != 0) {
                    tvSubsidy.setVisibility(View.VISIBLE);
                    tvSubsidy.setText("平台加赠"+ Constants.APP_PLATFORM_DONATE_NAME+" " + MoneyUtil.getLeXiangBi(subsidy));
                } else {
                    tvSubsidy.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 维修/换货
     */
    private void getBarterOrderStatus() {
        netRequest.barterOrderStatus(orderId, new RequestCallBack<BarterAfterSaleRouing>() {
            @Override
            public void onSuccess(ResponseInfo<BarterAfterSaleRouing> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1://1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成

                        switch (responseInfo.result.status.status) {
                            case 3:
                                tvStatus.setText("待退货");
                                ll1.setVisibility(View.GONE);

                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");

                                break;

                            case 4:
                                tvStatus.setText("维修中/换货中");
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");
                                list.add(3, responseInfo.result.status.barterExpressTime + ",已退货");
                                list.add(4, "00,换货中/维修中");

                                break;

                            case 5:
                                tvStatus.setText("待发货");
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");
                                list.add(3, responseInfo.result.status.barterExpressTime + ",已退货");
                                list.add(4, "00,换货中/维修中");
                                list.add(5, "00,待发货");

                                break;

                            case 6:
                                tvStatus.setText("待收货");
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");
                                list.add(3, responseInfo.result.status.barterExpressTime + ",已退货");
                                list.add(4, "00,换货中/维修中");
                                list.add(5, "00,待发货");
                                list.add(6, responseInfo.result.status.merchantExpressTime + ",已发货");
                                list.add(7, "00,待收货");

                                btTakeDelivery.setVisibility(View.VISIBLE);
                                break;

                            case 7:
                                tvStatus.setText("已完成");
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");
                                list.add(3, responseInfo.result.status.barterExpressTime + ",已退货");
                                list.add(4, "00,换货中/维修中");
                                list.add(5, "00,待发货");
                                list.add(6, responseInfo.result.status.merchantExpressTime + ",已发货");
                                list.add(7, "00,待收货");
                                list.add(8, responseInfo.result.status.consumerConfirmTime + ",已完成");

                                break;
                            default:
                                break;
                        }
                        list.add(responseInfo.result.status.consumerConfirmTime + ",已完成");
                        rv.setAdapter(new AfterSaleRoutingAdapter(mContext, list));
                        rv.setLayoutManager(new FullyLinearLayoutManager(mContext));

                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("网络链接失败");
            }
        });
    }

    /**
     * 退货
     */
    private void getRefundOrderStatus() {
        netRequest.refundOrderStatus(orderId, new RequestCallBack<RefundAfterSaleRouing>() {
            @Override
            public void onSuccess(ResponseInfo<RefundAfterSaleRouing> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1://1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成

                        switch (responseInfo.result.status.status) {
                            case 3:
                                tvStatus.setText("待退货");
                                ll1.setVisibility(View.GONE);
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");

                                break;
                            case 4:
                                tvStatus.setText("待收货");
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");
                                list.add(3, responseInfo.result.status.expressTime + ",已退货");
                                list.add(4, "00,退货中");
                                break;

                            case 5:
                                tvStatus.setText("待退款");
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");
                                list.add(3, responseInfo.result.status.expressTime + ",已退货");
                                list.add(4, "00,退货中");
                                list.add(5, responseInfo.result.status.confirmTime + ",已收货");
                                list.add(6, "00,待退款");
                                break;
//
                            case 6:
                                tvStatus.setText("已完成");
                                list.add(0, "00,待审核");
                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
                                list.add(2, "00,待退货");
                                list.add(3, responseInfo.result.status.expressTime + ",已退货");
                                list.add(4, "00,退货中");
                                list.add(5, responseInfo.result.status.confirmTime + ",已收货");
                                list.add(6, "00,待退款");
                                list.add(7, responseInfo.result.status.paymentTime + ",已完成");

                                break;

//                            case 7:
//                                tvStatus.setText("已完成");
//                                list.add(0,  "00,待审核");
//                                list.add(1, responseInfo.result.status.checkTime + ",已审核");
//                                list.add(2,  "00,待退货");
//                                list.add(3, responseInfo.result.status.expressTime + ",已退货");
//                                list.add(4, "00,退货中");
//                                list.add(5, responseInfo.result.status.confirmTime + ",已收货");
//                                list.add(6, responseInfo.result.status.paymentTime + ",已完成");
//                                break;

                        }
                        list.add(responseInfo.result.status.confirmTime + ",已完成");
                        rv.setAdapter(new AfterSaleRoutingAdapter(mContext, list));
                        rv.setLayoutManager(new FullyLinearLayoutManager(mContext));
                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();

                showToast("网络链接失败");
            }
        });
    }

    public void onBack(View view) {
        finish();
    }

    public void submitExpressId(View view) {
        Intent intent = new Intent(this, CommitExpressNumActivity.class);
        intent.putExtra("order_id", orderId);
        startActivity(intent);
    }

    public void delivery(View view) {
        netRequest.setExpress(type == 1 ? 0 : 1, orderId, "0", "0", new RequestCallBack<BaseEntity>() {

            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("提交成功");
                        finish();
                        break;
                    case 0:
                    case -3:
                        showToast("繁忙");
                        break;

                    case -4:
                        showToast("登录状态失效，请重新登录");
                        break;

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("请检查网络连接");
            }
        });
    }

    public void takeDelivery(View view) {

        showDialog(null, "确认商品已收到？", null, 0, Gravity.CENTER, "确定",
                "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                getGoods();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                }, mContext);

    }

    private void getGoods() {
        netRequest.confirmBarterOrder(orderId, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {

                switch (responseInfo.result.code) {
                    case 1:
                        showToast("收货成功");
                        setResult(RESULT_OK);
                        finish();

//                        AppManager.getInstance().killActivity(AfterSaleOneActivity.class.getSuperclass());
                        break;

                    default:
                        showToast("收货失败");
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("网络连接失败");
            }
        });
    }

}
