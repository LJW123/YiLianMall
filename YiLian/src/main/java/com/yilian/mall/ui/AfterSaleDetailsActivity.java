package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yilian.mylibrary.GlideUtil;

/**
 * 售后订单详情界面
 */
public class AfterSaleDetailsActivity extends BaseActivity {

    @ViewInject(R.id.rl_time_top_one)
    private RelativeLayout rlTimeTopOne; // 时间信息显示在上面 审核拒绝 已完成 退款中

    @ViewInject(R.id.rl_time_top_two)
    private RelativeLayout rlTimeTopTwo;

    @ViewInject(R.id.ll_time_bottom_one)
    private LinearLayout llTimeBottomOne;

    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    @ViewInject(R.id.tv_id)
    private TextView tvId; // 订单 id

    @ViewInject(R.id.tv_id_two)
    private TextView tvIdTwo;

    @ViewInject(R.id.tv_after_sale_type)
    private TextView tvAfterSaleType; // 售后方式

    @ViewInject(R.id.tv_after_sale_status)
    private TextView tvAfterSaleStatus; // 审核状态

    @ViewInject(R.id.tv_after_sale_status_two)
    private TextView tvAfterSaleStatusTwo;

    @ViewInject(R.id.iv_goods)
    private ImageView ivGoods; //商品图片

    @ViewInject(R.id.tv_goods_name)
    private TextView tvGoodsName; // 商品名称

    @ViewInject(R.id.tv_goods_norms)
    private TextView tvGoodsNorms; // 商品规格 文字描述

    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice; // 商品价格

    @ViewInject(R.id.tv_goods_count)
    private TextView tvGoodsCount; // 商品数量

    @ViewInject(R.id.tv_after_sale_error_details)
    private TextView tvAfterSaleErrorDetails; // 售后失败原因

    @ViewInject(R.id.tv_after_sale_error_time)
    private TextView tvAfterSaleErrorTime; // 售后时间

    @ViewInject(R.id.tv_after_sale_error_time_two)
    private TextView tvAfterSaleErrorTimeTwo;

    @ViewInject(R.id.tv_after_sale_status_info)
    private TextView tvAfterSaleStatusInfo; // 售后提示信息

    @ViewInject(R.id.bt_apply_after_sale)
    private Button btApplyAfterSale;

    BarterOrderInfo.Barter barter;
    RefundOrderInfo.Refund refund;
    ReturnAddress address;
    String orderId;
    String filialeId;
    AfterSaleNetRequest netRequest;

    int afterSaleType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_details);
        ViewUtils.inject(this);

        getData();

    }

    private void InitBarter() {
        tvBack.setText("售后订单详情");
        tvId.setText("订单编号:" + barter.barterOrder);

        tvGoodsName.setText(barter.barterGoodsName);
        tvGoodsNorms.setText(barter.barterGoodsNorms);
        tvGoodsCount.setText("× " + barter.barterGoodsCount);

        tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(barter.barterGoodsPrice)));
        tvAfterSaleErrorDetails.setText(barter.barterRefuse);
        tvAfterSaleErrorTime.setText(StringFormat.formatDate(barter.barterTime));

        showAuditStatus(barter.barterStatus);

        tvAfterSaleType.setText("换货/返修");

    }

    private void InitRefund() {
        tvBack.setText("售后订单详情");
        tvIdTwo.setText("订单编号 : " + refund.refundOrder);
        tvAfterSaleErrorTimeTwo.setText("申请时间 : " + StringFormat.formatDate(refund.refundTime));
        GlideUtil.showImageWithSuffix(mContext,refund.refundGoodsIcon,ivGoods);
        tvGoodsName.setText(refund.refundGoodsName);
        tvGoodsNorms.setText(refund.refundGoodsNorms);
        tvGoodsCount.setText("× " + refund.refundGoodsCount);

        tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(refund.refundGoodsPrice)));
        tvAfterSaleErrorDetails.setText(refund.refundRefuse);
        tvAfterSaleErrorTime.setText(StringFormat.formatDate(refund.refundTime));

        showAuditStatus(refund.refundStatus);

        tvAfterSaleType.setText("退款至账户奖励");

    }

    private void getData() {

        afterSaleType = getIntent().getExtras().getInt("afterSaleType");

        Logger.i(afterSaleType+"");

        orderId = getIntent().getStringExtra("orderId");
        filialeId = getIntent().getStringExtra("filiale_id");
        if (netRequest == null)
            netRequest = new AfterSaleNetRequest(mContext);

        // 0换货返修 1退货
        switch (afterSaleType) {
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
        netRequest.barterOrderInfo(orderId,filialeId, new RequestCallBack<BarterOrderInfo>() {
            @Override
            public void onSuccess(ResponseInfo<BarterOrderInfo> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        barter = responseInfo.result.barter;
                        address = barter.returnAddress;

                        InitBarter();

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 获取 退货 订单信息
     */
    private void getRefundOrderInfo() {
        netRequest.refundOrderInfo(orderId,filialeId, new RequestCallBack<RefundOrderInfo>() {
            @Override
            public void onSuccess(ResponseInfo<RefundOrderInfo> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        refund = responseInfo.result.refund;
                        address = refund.returnAddress;
                        InitRefund();

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     *  判断售后订单状态
     * @param status
     */
    private void showAuditStatus(int status) {
        //0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成
        switch (status) {
            case 0:
                process("已取消");
                break;

            case 1:
                process("待审核");
                break;

            case 2:
                refused();
                tvAfterSaleStatus.setText("审核拒绝");
                tvAfterSaleStatusInfo.setText("您的订单将在3个工作日内审核完成");
                break;

            case 3:
                tvAfterSaleStatus.setText("待退货");
                process("待退货");
                Logger.i(status+"");
                tvAfterSaleStatusInfo.setText("请将商品快递至：\n" +
                        address.contact + "\n" +
                        address.tel + "\n" +
                        address.address);

                break;

            case 4:
                process("维修中");

                break;

            case 5:
                process("待发货");
                break;

            case 6:
                process("待收货");
                break;

            case 7:
                accomplish();
                    break;

        }
    }
    /**
     * 售后中显示效果
     */
    private void process(String str){
        llTimeBottomOne.setVisibility(View.VISIBLE);
        tvAfterSaleStatusTwo.setText(Html.fromHtml("订单状态(<font color='#247df7'>"+str+"</fond>)"));
    }

    /**
     * 售后完成后显示效果
     */
    private void accomplish(){
        tvAfterSaleStatus.setText("已完成");
        rlTimeTopOne.setVisibility(View.VISIBLE);
    }

    /**
     * 审核拒绝后显示效果
     */
    private void refused(){
        rlTimeTopOne.setVisibility(View.VISIBLE);
        rlTimeTopTwo.setVisibility(View.VISIBLE);
        btApplyAfterSale.setVisibility(View.VISIBLE);
    }
}
