package com.yilianmall.merchant.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.widget.SlidingTabLayout;
import com.yilian.networkingmodule.entity.AfterSaleBtnClickResultEntity;
import com.yilian.networkingmodule.entity.SupplierListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantAfterSaleDetailsActivity;
import com.yilianmall.merchant.activity.MerchantAfterSaleRefuseDialogActivity;
import com.yilianmall.merchant.activity.MerchantExpressSelectActivity;
import com.yilianmall.merchant.adapter.AfterSaleAdapter;
import com.yilianmall.merchant.utils.UpdateMerchantOrderNumber;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LYQ on 2017/10/10.
 * 订单列表界面
 */

public class MerchantAfterSaleFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private static final int REFUND_UPDATE_CODE = 99;
    private static final int SEND_GOODS = 89;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int type, page;
    private AfterSaleAdapter adapter;
    private String exchangeType = "0";//售后的状态 换货返修
    private String refundType = "1";//售后的状态 退货
    private boolean isFirst = true;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_list_fragment, null, false);
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addSubscribe(UpdateMerchantOrderNumber.getMerchantAfterSaleOrderNumber(mContext));
                getFirstPageData();
            }
        });
        initAdapterListener();
    }


    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new AfterSaleAdapter(R.layout.merchant_item_after_sale);
        adapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
    }

    private SlidingTabLayout slidingTabLayout;
    private int titleCount;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser) {
            isFirst = !isFirst;
            Bundle arguments = getArguments();
            type = arguments.getInt("type");
            slidingTabLayout = (SlidingTabLayout) arguments.getSerializable("SlidingTabLayout");
            titleCount = arguments.getInt("titleCount");
            initData();
        }
    }


    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(200);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            getFirstPageData();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }

    public void getData() {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSupplierList(String.valueOf(page), String.valueOf(type), new Callback<SupplierListEntity>() {
                    @Override
                    public void onResponse(Call<SupplierListEntity> call, Response<SupplierListEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        List<SupplierListEntity.ListBean> list = response.body().list;
                                        if (null != list && list.size() > 0) {
                                            if (page > 0) {
                                                adapter.addData(list);
                                            } else {
                                                adapter.setNewData(list);
                                            }
                                            if (list.size() < Constants.PAGE_COUNT) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
                                                adapter.setNewData(list);
                                                adapter.setEmptyView(getEmptyView());
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<SupplierListEntity> call, Throwable t) {
                        if (page == 0) {
                            adapter.setNewData(null);
                            adapter.setEmptyView(getErrorView());
                        } else {
                            showToast(R.string.aliwx_net_null_setting);
                        }

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Boolean isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_AFTER_lIST, mContext, false);
        Logger.i("onResumeIsRefresh  " + isRefresh);
        if (isRefresh) {
            getFirstPageData();
            addSubscribe(UpdateMerchantOrderNumber.getMerchantAfterSaleOrderNumber(mContext));
        }
    }

    /**
     * adapter内部的点击事件
     */
    private void initAdapterListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int i = view.getId();
                SupplierListEntity.ListBean item = (SupplierListEntity.ListBean) adapter.getItem(position);

                if (i == R.id.ll_content) {
                    //跳转售后订单详情
                    Intent intent = new Intent(mContext, MerchantAfterSaleDetailsActivity.class);
                    intent.putExtra(Constants.SERVICE_INDEX, item.orderId);
                    intent.putExtra(Constants.SERVICE_TYPE, item.type);
                    startActivity(intent);
                } else if (i == R.id.tv_look_express_exchange) {
                    //卖家查看物流
                    Logger.i("卖家查看物流   ");
                    jumpToExpressActivity(item.merchantExpressCompany, item.merchantExpressNumber, item.goodsIcon);
                } else if (i == R.id.tv_look_express_btn) {
                    //买家查看物流、拒绝
                    TextView tvLookExpress = (TextView) view.findViewById(R.id.tv_look_express_btn);
                    String textStr = tvLookExpress.getText().toString().trim();
                    Logger.i("tv_look_express_btn getTextStr   " + textStr);
                    if (textStr.equals("查看物流")) {
                        jumpToExpressActivity(item.userExpressCompany, item.userExpressNumber, item.goodsIcon);
                    } else if (textStr.equals("拒绝") && item.status.equals("1")) {
                        Intent intent = new Intent(mContext, MerchantAfterSaleRefuseDialogActivity.class);
                        intent.putExtra(Constants.CHECK_ID, item.orderId);
                        intent.putExtra(Constants.SERVICE_TYPE, item.type);
                        intent.putExtra(Constants.LIST_POSITION, position);
                        startActivityForResult(intent, REFUND_UPDATE_CODE);
                    }
                } else if (i == R.id.tv_confirm_btn) {
                    //备货、已修复、发货、通过、确认收货、确认退款
                    TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm_btn);
                    String contentStr = tvConfirm.getText().toString().trim();
                    Logger.i("tv_confirm_btnTextStr " + contentStr);

                    if (contentStr.equals("备货") || contentStr.equals("已修复") && item.type.equals(exchangeType)) {
                        //备货和已修复走的都是同一个借口
                        barterStock(item.orderId, position, false);
                    } else if (contentStr.equals("确认发货") && item.type.equals(exchangeType)) {
                        barterExpress(item.orderId, position);
                    } else if (contentStr.equals("通过") && item.status.equals("1")) {
                        serviceCheck(item.orderId, item.type, position, true);
                    } else if (contentStr.equals("确认收货")) {
                        showReceiveDialog(item.type, item.orderId, position, false);
                    } else if (contentStr.equals("确认退款") && item.type.equals(refundType)) {
                        //退款
                        showRefundDialog(item.orderId, position, true);
                    }
                }
            }
        });
    }

    /**
     * 确认退款
     *
     * @param serviceOrder
     * @param position
     * @param isRemove
     */
    private void showRefundDialog(final String serviceOrder, final int position, final boolean isRemove) {

        showSystemV7Dialog("确认退款", "确认退款后，无法恢复，请核对详细信息!", "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        refundMoneyBack(serviceOrder, position, isRemove);
                        dialog.dismiss();
                        break;
                }
            }
        });


    }

    /**
     * 确认退款
     *
     * @param serviceOrder
     * @param position
     * @param isRemove
     */
    private void refundMoneyBack(String serviceOrder, final int position, final boolean isRemove) {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .refundMoneyBack(serviceOrder, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext,response.body().code,response.body().msg)){
                                switch (response.body().code){
                                    case 1:
                                        removePosition(position);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 退货的确认收货
     *
     * @param serviceOrder
     * @param position
     * @param isRemove
     */
    private void refundExpressReceive(String serviceOrder, final int position, final boolean isRemove) {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .refundExpressReceive(serviceOrder, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response, position, isRemove);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 确认收货
     *
     * @param type
     * @param serviceOrder
     * @param position
     * @param isRemove
     */
    private void showReceiveDialog(final String type, final String serviceOrder, final int position, final boolean isRemove) {

        showSystemV7Dialog("确认收货", "请确认商品名称，规格信息，以及数量!", "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        if (type.equals(exchangeType)) {
                            //换货返修的确认收货
                            exchangeBarterExpressReceive(serviceOrder, position, isRemove);
                        } else if (type.equals(refundType)) {
                            //退货的确认收货
                            refundExpressReceive(serviceOrder, position, isRemove);
                        }
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    /**
     * 换货 返修的确认收货
     *
     * @param serviceOrder
     * @param position
     * @param isRemove
     */
    private void exchangeBarterExpressReceive(String serviceOrder, final int position, final boolean isRemove) {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .barterExpressReceive(serviceOrder, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response, position, isRemove);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 发货————————>选择地址的activity的跳转获取数据信息
     *
     * @param serviceOrder
     * @param position
     */
    private void barterExpress(String serviceOrder, int position) {
        Logger.i("position");
        Intent intent = new Intent(mContext, MerchantExpressSelectActivity.class);
        intent.putExtra("orderIndex", serviceOrder);
        intent.putExtra(Constants.JUMP_STATUS, "after");
        intent.putExtra(Constants.LIST_POSITION, position);
        startActivityForResult(intent, SEND_GOODS);
    }

    /**
     * 备货
     *
     * @param serviceOrder 短的订单号
     * @param position
     * @param isRemove
     */
    private void barterStock(String serviceOrder, final int position, final boolean isRemove) {
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .barterStock(serviceOrder, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response, position, isRemove);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    private void showResultToast(Response<AfterSaleBtnClickResultEntity> response, int position, boolean... isRemoves) {
        HttpResultBean httpResultBean = response.body();
        boolean isRemove = false;
        if (isRemoves.length > 0) {
            isRemove = isRemoves[0];//是否移除当前的item
        }
        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, httpResultBean)) {
            if (CommonUtils.serivceReturnCode(mContext, httpResultBean.code, httpResultBean.msg)) {
                switch (httpResultBean.code) {
                    case 1:
                        showToast(httpResultBean.msg);
                        addSubscribe(UpdateMerchantOrderNumber.getMerchantAfterSaleOrderNumber(mContext));
                        if (isRemove) {
                            removePosition(position);
                        } else {
                            SupplierListEntity.ListBean item = adapter.getItem(position);
                            item.barterType = response.body().barterType;
                            item.type = response.body().type;
                            item.status = response.body().status;
                            item.merchantExpressNumber=response.body().merchantExpressNumber;
                            item.merchantExpressCompany=response.body().merchantExpressCompany;
                            adapter.notifyItemChanged(position, item);
                        }
                        break;
                }
            }
        }
    }

    private void removePosition(int position) {
        adapter.remove(position);
        //******如果是只有一条数据的话刷新界面
        Logger.i("adapter.getItemCount()   "+adapter.getItemCount());
        if (adapter.getItemCount()<=0){
            adapter.setNewData(null);
            adapter.setEmptyView(getEmptyView());
        }
        addSubscribe(UpdateMerchantOrderNumber.getMerchantAfterSaleOrderNumber(mContext));
//        PreferenceUtils.writeBoolConfig(Constants.REFRESH_AFTER_lIST, true, mContext);
    }

    /**
     * 通过审核
     *
     * @param serviceOrder
     * @param type
     * @param position
     * @param isRemove     是否移除当前条目
     * @param_status 审核通过传值“3”
     * @param_reason 通过时给“”
     */
    private void serviceCheck(String serviceOrder, String type, final int position, final boolean isRemove) {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .supplierServiceCheck(serviceOrder, "3", "", type, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        showResultToast(response, position, isRemove);
                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case REFUND_UPDATE_CODE:
                        //拒绝操作完成后刷新界面
                        int position = data.getIntExtra(Constants.LIST_POSITION, -1);
                        removePosition(position);
                        break;
                    case SEND_GOODS:
                        //确认发货之后更新当前按钮显示状态
                        int sendGoodsPosition = data.getIntExtra(Constants.LIST_POSITION, -1);
                        AfterSaleBtnClickResultEntity body = (AfterSaleBtnClickResultEntity) data.getSerializableExtra("body");
                        SupplierListEntity.ListBean item = adapter.getItem(sendGoodsPosition);
                        item.barterType = body.barterType;
                        item.status = body.status;
                        item.type = body.type;
                        item.merchantExpressNumber=body.merchantExpressNumber;
                        item.merchantExpressCompany=body.merchantExpressCompany;
                        adapter.notifyItemChanged(sendGoodsPosition, item);
                        break;
                }
                break;
        }

    }


    /**
     * 查看物流
     *
     * @param expressCompany
     * @param expressNumber
     */
    private void jumpToExpressActivity(String expressCompany, String expressNumber, String goodUrl) {
        if (TextUtils.isEmpty(expressCompany) || TextUtils.isEmpty(expressNumber)) {
            showToast("物流信息异常");
            return;
        }

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.LookExpressActivity"));
        intent.putExtra("express_number", expressNumber);
        intent.putExtra("express_company", expressCompany);
        intent.putExtra("express_img", goodUrl);
        mContext.startActivity(intent);
    }


    public View getEmptyView() {
        View emptyView = null;
        Logger.i("getEmptyView  "+emptyView);
        if (null == emptyView) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }

        Logger.i("getEmptyView333  "+emptyView);
        return emptyView;
    }

    public View getErrorView() {
        View errorView = null;
        Logger.i("errorView   "+errorView);
        if (null == errorView) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            TextView tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(this);
        }
        Logger.i("errorView333   "+errorView);
        return errorView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_refresh) {
            getFirstPageData();
        }
    }
}
