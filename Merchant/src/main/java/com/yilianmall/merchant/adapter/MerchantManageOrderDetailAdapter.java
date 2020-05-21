package com.yilianmall.merchant.adapter;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.GoodsType;
import com.yilian.networkingmodule.entity.MerchantManageOrderBaseEntity;
import com.yilian.networkingmodule.entity.MerchantManageOrderButtonEntity;
import com.yilian.networkingmodule.entity.MerchantManageOrderTitleEntity;
import com.yilian.networkingmodule.entity.MerchantOrderEntity;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantExpressSelectActivity;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.List;

import static com.yilian.networkingmodule.entity.MerchantManageOrderBaseEntity.BUTTON;
import static com.yilian.networkingmodule.entity.MerchantManageOrderBaseEntity.GOOD;
import static com.yilian.networkingmodule.entity.MerchantManageOrderBaseEntity.TITLE;

/**
 * Created by  on 2017/10/13 0013.
 */

public class MerchantManageOrderDetailAdapter extends BaseMultiItemQuickAdapter<MerchantManageOrderBaseEntity, BaseViewHolder> {
    private String orderStatus;
    private int position;//该position是商家待处理订单列表的position，用来在该adapter中处理某一订单后，刷新最外层（商家未处理订单列表）列表时使用
    private String orderIndex;
    private String goodUrl;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MerchantManageOrderDetailAdapter(List<MerchantManageOrderBaseEntity> data, String orderStatus, String orderIndex, int position, String goodUrl) {
        super(data);
        this.orderIndex = orderIndex;
        this.orderStatus = orderStatus;
        this.position = position;
        this.goodUrl = goodUrl;
        addItemType(TITLE, R.layout.merchant_item_merchant_manage_order_detail_adapter_title);
        addItemType(GOOD, R.layout.merchant_order_details_goods);
        addItemType(BUTTON, R.layout.merchant_item_merchant_manage_order_button);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MerchantManageOrderBaseEntity item) {
        switch (item.getItemType()) {
            case TITLE:
                MerchantManageOrderTitleEntity itemTitle = (MerchantManageOrderTitleEntity) item;
                helper.setText(R.id.tv_title_name, itemTitle.title);
                helper.setText(R.id.tv_good_count, "共" + itemTitle.count + "件");
                break;
            case GOOD:
                final MerchantOrderEntity.GoodsBean itemGood = (MerchantOrderEntity.GoodsBean) item;
                final CheckBox checkBox = helper.getView(R.id.checkbox);
                ((CheckBox) checkBox).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemGood.isChecked = checkBox.isChecked();
                    }
                });
                helper.setText(R.id.tv_good_name, itemGood.goodsName);
                helper.setText(R.id.tv_count, "×" + itemGood.goodsCount);
                helper.setText(R.id.tv_sku, TextUtils.isEmpty(itemGood.goodsNorms) ? "" : itemGood.goodsNorms);
                ImageView goodIcon = helper.getView(R.id.iv_icon);
                GlideUtil.showImageWithSuffix(mContext, itemGood.goodsIcon, goodIcon);
                switch (itemGood.goodsStatus) {
                    case "0":
                        if (orderStatus.equals("0")) {
                            //订单已取消的情况
                            helper.getView(R.id.ll_buttons).setVisibility(View.GONE);
                        } else {
                            helper.getView(R.id.ll_buttons).setVisibility(View.VISIBLE);
                        }
                        ((CheckBox) checkBox).setEnabled(true);
                        helper.setText(R.id.tv_order_status, "未发货");
                        helper.setText(R.id.tv_revise_logistics, "发 货");
                        helper.getView(R.id.tv_revise_logistics).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            发货
                                Intent intent = new Intent(mContext, MerchantExpressSelectActivity.class);
                                intent.putExtra("fromPage", "MerchantManageOrderDetailAdapter");
                                intent.putExtra("parcelIndex", itemGood.orderGoodsIndex);
                                intent.putExtra("orderIndex", orderIndex);
                                intent.putExtra("position", position);
                                intent.putExtra("orderStatus", orderStatus);
                                intent.putExtra(Constants.JUMP_STATUS, "order");

                                mContext.startActivity(intent);
                            }
                        });
                        break;
                    case "1":
                        ((CheckBox) checkBox).setEnabled(false);
                        helper.setText(R.id.tv_order_status, "已发货");
                        helper.getView(R.id.ll_buttons).setVisibility(View.GONE);
                        break;
                    case "2":
                        ((CheckBox) checkBox).setEnabled(false);
                        helper.setText(R.id.tv_order_status, "已完成");
                        helper.getView(R.id.ll_buttons).setVisibility(View.GONE);
                        break;
                    case "3":
                        ((CheckBox) checkBox).setEnabled(false);
                        helper.setText(R.id.tv_order_status, "");
                        helper.getView(R.id.ll_buttons).setVisibility(View.GONE);
                        break;
                    default:
                        ((CheckBox) checkBox).setEnabled(false);
                        helper.setText(R.id.tv_order_status, "");
                        helper.getView(R.id.ll_buttons).setVisibility(View.GONE);
                        break;
                }
                switch (itemGood.type) {
                    case "0"://商城
                        //helper.getView(R.id.tv_integral).setVisibility(View.GONE);
                        helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
                        helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(itemGood.goodsCost)));

                        if (!TextUtils.isEmpty(itemGood.returnIntegral) && Double.parseDouble(itemGood.returnIntegral) > 0) {
                            helper.getView(R.id.tv_score).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_score, mContext.getResources().getString(R.string.library_module_score) + MoneyUtil.getLeXiangBiNoZero(itemGood.returnIntegral));
                        } else {
                            helper.getView(R.id.tv_score).setVisibility(View.GONE);
                            helper.setText(R.id.tv_score, "");
                        }
                        break;
                    case "1"://易划算
                        helper.getView(R.id.tv_price).setVisibility(View.GONE);
                        helper.getView(R.id.tv_score).setVisibility(View.GONE);
//                        if (!TextUtils.isEmpty(itemGood.goodsIntegral) && Double.parseDouble(itemGood.goodsIntegral) > 0) {
//                            helper.getView(R.id.tv_integral).setVisibility(View.VISIBLE);
//                            helper.setText(R.id.tv_integral, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(itemGood.goodsIntegral)));
//                        } else {
//                            helper.getView(R.id.tv_integral).setVisibility(View.GONE);
//                            helper.setText(R.id.tv_integral, "");
//                        }
                        helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
                        break;
                    case "2"://奖券够
                        helper.getView(R.id.tv_score).setVisibility(View.GONE);
                        helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(itemGood.goodsRetail)));
                        helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
//                        if (!TextUtils.isEmpty(itemGood.goodsPrice) && Double.parseDouble(itemGood.goodsPrice) > 0 &&
//                                !TextUtils.isEmpty(itemGood.goodsRetail) && Double.parseDouble(itemGood.goodsRetail) > 0) {
//                            helper.getView(R.id.tv_integral).setVisibility(View.VISIBLE);
//                            //奖券够的奖券价格 市场价减去供货商提供价格
//                            double integral = Double.parseDouble(itemGood.goodsPrice) - Double.parseDouble(itemGood.goodsRetail);
//                            helper.setText(R.id.tv_integral, MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(integral)));
//                        } else {
//                            helper.getView(R.id.tv_integral).setVisibility(View.GONE);
//                            helper.setText(R.id.tv_integral, "");
//                        }
                        break;
                    case "3":
                    case GoodsType.CAL_POWER:
                        helper.getView(R.id.tv_price).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_price, MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(itemGood.goodsCost)));
                        if (itemGood.givenBean  > 0) {
                            helper.getView(R.id.tv_return_bean).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_return_bean, " +" + MoneyUtil.getLeXiangBiNoZero(itemGood.givenBean) +Constants.APP_PLATFORM_DONATE_NAME);
                        } else {
                            helper.getView(R.id.tv_return_bean).setVisibility(View.GONE);
                            helper.setText(R.id.tv_return_bean, "");
                        }
                        break;
                    default:
                        break;
                }
                break;
            case BUTTON:
                final MerchantManageOrderButtonEntity itemButton = (MerchantManageOrderButtonEntity) item;
                if ("0".equals(itemButton.orderStatus)) {
                    helper.getView(R.id.ll_buttons).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.ll_buttons).setVisibility(View.VISIBLE);
                    switch (itemButton.goodStatus) {
                        case "0":
                            helper.getView(R.id.ll_buttons).setVisibility(View.GONE);
                            break;
                        case "1":
                            helper.getView(R.id.ll_buttons).setVisibility(View.VISIBLE);

                            helper.getView(R.id.tv_gray).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_gray, "查看物流");
//                            查看物流
                            helper.getView(R.id.tv_gray).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //查看物流
                                    Intent intent = new Intent();
                                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.LookExpressActivity"));
                                    intent.putExtra("express_number", itemButton.expressNum);
                                    intent.putExtra("express_company", itemButton.expressCompany);
                                    intent.putExtra("express_img", goodUrl);
                                    mContext.startActivity(intent);
                                }
                            });
                            helper.getView(R.id.tv_red).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_red, "修改物流");
//                            修改物流
                            helper.getView(R.id.tv_red).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, MerchantExpressSelectActivity.class);
                                    intent.putExtra("parcelIndex", itemButton.parcelIndex);
                                    intent.putExtra(Constants.JUMP_STATUS, "changeExpress");
                                    mContext.startActivity(intent);
                                }
                            });
                            break;
                        case "2":
                            helper.getView(R.id.ll_buttons).setVisibility(View.VISIBLE);
                            helper.getView(R.id.tv_gray).setVisibility(View.VISIBLE);
                            helper.setText(R.id.tv_gray, "查看物流");
//                            查看物流
                            helper.getView(R.id.tv_gray).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //查看物流
                                    Intent intent = new Intent();
                                    intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.LookExpressActivity"));
                                    intent.putExtra("express_number", itemButton.expressNum);
                                    intent.putExtra("express_company", itemButton.expressCompany);
                                    intent.putExtra("express_img", goodUrl);
                                    mContext.startActivity(intent);
                                }
                            });
                            helper.getView(R.id.tv_red).setVisibility(View.GONE);
                            break;
                        default:
                            helper.getView(R.id.tv_red).setVisibility(View.GONE);
                            helper.getView(R.id.tv_gray).setVisibility(View.GONE);
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }
}
