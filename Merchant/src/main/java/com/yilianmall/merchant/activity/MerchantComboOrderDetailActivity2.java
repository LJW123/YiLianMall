package com.yilianmall.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.CancelComboEntity;
import com.yilian.networkingmodule.entity.ComboDetailEntity;
import com.yilian.networkingmodule.entity.ComboOrderBaseEntity;
import com.yilian.networkingmodule.entity.UpdateComboStatusEntity;
import com.yilian.networkingmodule.retrofitutil.NoNetworkException;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.MyMultiItemAdapter;
import com.yilianmall.merchant.event.UpdateComboOrderItemStatusEvent;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MerchantComboOrderDetailActivity2 extends BaseActivity implements View.OnClickListener {


    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private FrameLayout v3Layout;
    private Button btnCommit;
    private LinearLayout llCommit;
    private RecyclerView recyclerView;
    private MyMultiItemAdapter adapter;
    private ArrayList<MultiItemEntity> itemDatas;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_combo_order_detail2);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int headerLayoutCount = adapter.getHeaderLayoutCount();
                int itemViewType = adapter.getItemViewType(position + headerLayoutCount);
                switch (itemViewType) {
                    case ComboOrderBaseEntity.ITEM5:
                        //图文详情
                        JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext,Constants.PHOTO_TEXT_URL + mComboDetailEntity.packageId,false);
                        break;
                }
            }
        });

    }

    public void refresh(View v) {
        getComboDetail();
    }

    private String orderId;
    private int orderStatus;
    private ComboDetailEntity mComboDetailEntity;
    @SuppressWarnings("unchecked")
    Observer<ComboDetailEntity> observer = new Observer<ComboDetailEntity>() {
        @Override
        public void onCompleted() {
            stopMyDialog();
            switch (orderType) {
                case 2:
                    llCommit.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onError(Throwable e) {
            stopMyDialog();
            showToast(e.getMessage());
            if (e instanceof NoNetworkException) {
                adapter.setEmptyView(R.layout.library_module_load_error);
                llCommit.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNext(final ComboDetailEntity comboDetailEntity) {
            mComboDetailEntity = comboDetailEntity;
            Logger.i("onNext:");
            orderId = comboDetailEntity.orderId;
//            //1未使用待接单 2待配送 3配送中 4已使用已送达 6取消订单
            orderStatus = comboDetailEntity.status;
            setBtnCommitStatus();
            View headerView = View.inflate(mContext, R.layout.merchant_combo_detai_header, null);
            TextView tvNamePhone = (TextView) headerView.findViewById(R.id.tv_name_phone);
            TextView tvAddress = (TextView) headerView.findViewById(R.id.tv_address);
            switch (orderType) {
                case 2:
                    //            地址信息
                    final String linkPhone = comboDetailEntity.addressInfo.linkPhone;
                    Spanned spanned = Html.fromHtml(comboDetailEntity.addressInfo.linkMan + "<font color='#fe5062'    >" + linkPhone);
                    tvNamePhone.setText(spanned);
                    tvAddress.setText(comboDetailEntity.addressInfo.linkAddress);
                    adapter.addHeaderView(headerView);
                    tvNamePhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (linkPhone != null && linkPhone.length() > 0) {
                                showDialog(null, linkPhone, null, 0, Gravity.CENTER, "拨打", "取消", true,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + linkPhone));
                                                        startActivity(intent);
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        dialog.dismiss();
                                                        break;
                                                }
                                            }
                                        }, mContext);

                            } else {
                                showToast("亲,这家太懒了,没留电话哦!");
                            }
                        }
                    });
                    break;
                default:
                    //                    券码信息
                    itemDatas.add(new ComboTitle("券码信息", "有效期至" + DateUtils.formatDate(comboDetailEntity.endTime), ComboOrderBaseEntity.ITEM1));
                    itemDatas.addAll(comboDetailEntity.codes);
                    break;
            }
            itemDatas.add(new ComboTitle("套餐信息", null, ComboOrderBaseEntity.ITEM1_1));
            itemDatas.add(new ComboTitle(comboDetailEntity.name, String.valueOf(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(comboDetailEntity.price))), ComboOrderBaseEntity.ITEM4));
            List<ComboDetailEntity.PackageInfoBean> packageInfo = comboDetailEntity.packageInfo;
            for (int i = 0; i < packageInfo.size(); i++) {
                ComboDetailEntity.PackageInfoBean packageInfoBean = packageInfo.get(i);
                List<ComboDetailEntity.PackageInfoBean.ContentBean> content = packageInfoBean.content;
                for (int j = 0; j < content.size(); j++) {
                    itemDatas.add(content.get(j));
                }
            }
            itemDatas.add(new ComboTitle("查看图文详情", null, ComboOrderBaseEntity.ITEM5));

            itemDatas.add(new ComboTitle("订单详情", null, ComboOrderBaseEntity.ITEM1_1));
//            订单详情
            View footerView = View.inflate(mContext, R.layout.merchant_combo_detai_footer, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(DPXUnitUtil.dp2px(mContext, 10), 0, DPXUnitUtil.dp2px(mContext, 10), DPXUnitUtil.dp2px(mContext, 24));
            footerView.setLayoutParams(params);
            View includeComboOrderNumber = footerView.findViewById(R.id.include_combo_order_number);
            ((TextView) includeComboOrderNumber.findViewById(R.id.tv_key)).setText("订单号");
            TextView tvOrderNumber = ((TextView) includeComboOrderNumber.findViewById(R.id.tv_value));
            tvOrderNumber.setVisibility(View.VISIBLE);
            View includeComboOrderPhone = footerView.findViewById(R.id.include_combo_order_phone);
            ((TextView) includeComboOrderPhone.findViewById(R.id.tv_key)).setText("付款手机号");
            TextView tvPhone = (TextView) includeComboOrderPhone.findViewById(R.id.tv_value);
            tvPhone.setVisibility(View.VISIBLE);
            View includeComboOrderTime = footerView.findViewById(R.id.include_combo_order_time);
            ((TextView) includeComboOrderTime.findViewById(R.id.tv_key)).setText("订单时间");
            TextView tvOrderTime = (TextView) includeComboOrderTime.findViewById(R.id.tv_value);
            tvOrderTime.setVisibility(View.VISIBLE);
            View includeComboOrderCount = footerView.findViewById(R.id.include_combo_order_count);
            ((TextView) includeComboOrderCount.findViewById(R.id.tv_key)).setText("购买数量");
            TextView tvCount = (TextView) includeComboOrderCount.findViewById(R.id.tv_value);
            tvCount.setVisibility(View.VISIBLE);
            View includeComboOrderFreight = footerView.findViewById(R.id.include_combo_order_freight);
            ((TextView) includeComboOrderFreight.findViewById(R.id.tv_key)).setText("配送费");
            TextView tvFreight = (TextView) includeComboOrderFreight.findViewById(R.id.tv_value);
            View includeComboOrderMoney = footerView.findViewById(R.id.include_combo_order_money);
            ((TextView) includeComboOrderMoney.findViewById(R.id.tv_key)).setText("金额(合计)");
            TextView tvOrderMoney = (TextView) includeComboOrderMoney.findViewById(R.id.tv_value);
            tvOrderMoney.setVisibility(View.VISIBLE);
            View includeComboOrderIntegral = footerView.findViewById(R.id.include_combo_order_integral);
            ((TextView) includeComboOrderIntegral.findViewById(R.id.tv_key)).setText("销售奖励");
            TextView tvIntegral = (TextView) includeComboOrderIntegral.findViewById(R.id.tv_value);
            tvIntegral.setVisibility(View.VISIBLE);
//            订单号
            tvOrderNumber.setText(comboDetailEntity.orderId);
            ComboDetailEntity.OrderInfoBean orderInfo = comboDetailEntity.orderInfo;
//            付款手机号
            tvPhone.setText(orderInfo.phone);
//            下单时间
            tvOrderTime.setText(DateUtils.timeStampToStr(orderInfo.payTime));
//            购买数量
            tvCount.setText("× " + orderInfo.count);
//            配送费
            String deliveryCost = orderInfo.deliveryCost;
            Logger.i("配送费：" + deliveryCost);
            switch (orderType) {
                case 2:
                    tvFreight.setVisibility(View.VISIBLE);
                    includeComboOrderFreight.setVisibility(View.VISIBLE);
                    tvFreight.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(deliveryCost)));
                    break;
                default:
                    includeComboOrderFreight.setVisibility(View.GONE);
                    break;
            }
//            订单金额
            tvOrderMoney.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBiNoZero(comboDetailEntity.orderInfo.amount)));
            tvOrderMoney.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
//            订单销售获得奖券
            tvIntegral.setText(MoneyUtil.getLeXiangBiNoZero(comboDetailEntity.returnIntegral) + "奖券");
            tvIntegral.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_18c99d));
            adapter.addFooterView(footerView);
            adapter.notifyDataSetChanged();
        }
    };

    private void setBtnCommitStatus() {
        switch (orderStatus) {
            case 1:
                btnCommit.setText("立即接单");
                btnCommit.setClickable(true);
                tvRight2.setVisibility(View.GONE);
                tvRight2.setText("");
                break;
            case 2:
                btnCommit.setText("立即配送");
                btnCommit.setClickable(true);
                tvRight2.setVisibility(View.VISIBLE);
                tvRight2.setText("取消订单");
                break;
            case 3:
                btnCommit.setText("配送完成");
                btnCommit.setClickable(true);
                btnCommit.setBackgroundResource(R.drawable.merchant_bg_btn_blue_radious);
                tvRight2.setVisibility(View.VISIBLE);
                tvRight2.setText("取消订单");
                break;
            case 4:
                btnCommit.setText("已送达");
                btnCommit.setClickable(false);
                btnCommit.setBackgroundResource(R.drawable.merchant_bg_btn_gray_radious);
                tvRight2.setVisibility(View.GONE);
                tvRight2.setText("");
                break;
            case 5:
            case 6:
                tvRight2.setVisibility(View.GONE);
                tvRight2.setText("");
                btnCommit.setText("已取消");
                btnCommit.setClickable(false);
                btnCommit.setBackgroundResource(R.drawable.merchant_bg_btn_gray_radious);
                break;
        }
    }

    public class ComboTitle implements MultiItemEntity {
        private final int type;
        public String title;
        public String value;

        public ComboTitle(String title, @Nullable String value, int type) {
            this.title = title;
            this.value = value;
            this.type = type;
        }

        @Override
        public int getItemType() {
            return type;
        }
    }

    private String comboOrderId;
    private int orderType; //1到店消费订单 2 配送套餐列表

    @SuppressWarnings("unchecked")
    private void initData() {
        comboOrderId = getIntent().getStringExtra("order_id");
        position = getIntent().getIntExtra("position", -1);
        orderType = getIntent().getIntExtra("order_type", -1);
        switch (orderType) {
            case 2:
                break;
            default:
                llCommit.setVisibility(View.GONE);//隐藏接单按钮
                break;
        }
        getComboDetail();
    }

    @SuppressWarnings("unchecked")
    private void getComboDetail() {
        startMyDialog();
        Subscription subscribe = RetrofitUtils3.getRetrofitService(mContext)
                .getComboDetail("package/getOrderDetails", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext), comboOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        addSubscription(subscribe);
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("订单详情");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        itemDatas = new ArrayList<>();
        adapter = new MyMultiItemAdapter(itemDatas);
        adapter.bindToRecyclerView(recyclerView);
//        recyclerView.setAdapter(adapter);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        llCommit = (LinearLayout) findViewById(R.id.ll_commit);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {
            showCancelOrderDialog();
        } else if (i == R.id.btn_commit) {

            switch (orderStatus) {
                case 1:
                    showUpdateDialog("确认接单?", "接单", "取消", orderStatus);
                    break;
                case 2:
                    showUpdateDialog("确认配送?", "立即配送", "取消", orderStatus);
                    break;
                case 3:
                    showUpdateDialog("确认送达?", "已送达", "取消", orderStatus);
                    break;
            }
        }
    }

    private void showCancelOrderDialog() {
        showSystemV7Dialog(null, "确认取消订单?", "确认", "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        cancelOrder();
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 取消套餐订单
     */
    @SuppressWarnings("unchecked")
    private void cancelOrder() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).cancelComboOrder("cancelMerMealOrder", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CancelComboEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CancelComboEntity cancelComboEntity) {
                        showToast(cancelComboEntity.msg);
                        orderStatus = cancelComboEntity.orderStatus;
                        updateComboListStatus(orderStatus);
                        setBtnCommitStatus();
                    }
                });
        addSubscription(subscription);
    }

    private void showUpdateDialog(String message, String positiveText, String negativeText, final int orderStatus) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateOrderStatus(orderStatus + 1);
                    }
                })
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_333));
    }


    /**
     * 更新订单状态
     */
    @SuppressWarnings("unchecked")
    private void updateOrderStatus(int newStatus) {
        RetrofitUtils3.getRetrofitService(mContext).updateComboStatus("updMerMealOrderStatus", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                orderId, String.valueOf(newStatus))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateComboStatusEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UpdateComboStatusEntity updateComboStatusEntity) {
                        int orderStatus = updateComboStatusEntity.orderStatus;
                        updateComboListStatus(orderStatus);
                        btnCommit.setText("立即配送");
                        MerchantComboOrderDetailActivity2.this.orderStatus = orderStatus;//更新订单状态
                        setBtnCommitStatus();
                    }

                });
    }

    /**
     * {@link com.yilianmall.merchant.fragment.MerchantDeliveryComboListFragment}
     *
     * @param orderStatus
     */
    private void updateComboListStatus(int orderStatus) {
        if (position >= 0) {
            EventBus.getDefault().post(new UpdateComboOrderItemStatusEvent(position, orderStatus));
        }
    }
}
