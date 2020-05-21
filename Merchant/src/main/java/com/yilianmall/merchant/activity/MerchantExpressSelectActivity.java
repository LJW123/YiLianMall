package com.yilianmall.merchant.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.AfterSaleBtnClickResultEntity;
import com.yilian.networkingmodule.entity.ExpressListEntity;
import com.yilian.networkingmodule.entity.MerchantDeliverGoods;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.event.ChangeExpressSuccessEvent;
import com.yilianmall.merchant.event.RemoveMerchantManageOrderList;
import com.yilianmall.merchant.event.UpdateMerchantManageOrderList;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.yilianmall.merchant.event.RemoveMerchantManageOrderList.TYPE_SEND;
import static com.yilianmall.merchant.event.RemoveMerchantManageOrderList.TYPE_STOCK;

/**
 * 选择快递界面
 */
public class MerchantExpressSelectActivity extends BaseSimpleActivity implements View.OnClickListener {

    private TextView tvExpressName;
    private ImageView ivArrow;
    private EditText etExpressOrderId;
    private ImageView ivClear;
    private ImageView ivScan;
    private TextView tvCancelBtn;
    private TextView tvConfirmBtn;
    private LinearLayout llExpress;
    private Context mContext;
    private int listPosition;
    private String jumpStatus;
    private String parcelIndex;
    private String orderIndex;
    private List<ExpressListEntity.ListBean> expressList;
    private String expressId;
    private String fromPage;
    private int position = -1;
    private String orderStatus;
    private ExpressSelectBroadCase receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.merchant_activity_express_select);
        mContext = this;
        fromPage = getIntent().getStringExtra("fromPage");
        position = getIntent().getIntExtra("position", -1);
        orderStatus = getIntent().getStringExtra("orderStatus");
        parcelIndex = getIntent().getStringExtra("parcelIndex");//包裹id
        orderIndex = getIntent().getStringExtra("orderIndex");
        listPosition = getIntent().getIntExtra(Constants.LIST_POSITION, -1);
        jumpStatus = getIntent().getStringExtra(Constants.JUMP_STATUS);
        initView();
        initExpressList();
        /**
         * 注册广播接收扫描界面获得的快递
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yilianmall.merchant.activity.MerchantExpressSelectActivity");
        receiver = new ExpressSelectBroadCase();
        registerReceiver(receiver, filter);
    }


    class ExpressSelectBroadCase extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            Logger.i("onReceive   " + result);
            if (!TextUtils.isEmpty(result)) {
                etExpressOrderId.setText(result);
            }
        }
    }


    //TODO 要考虑快递获取失败的问题
    private void initExpressList() {
        getExpressListInfo();
    }

    private void getExpressListInfo() {
        expressListIsRequesting = true;
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .setToken(RequestOftenKey.getToken(mContext))
                .getExpressList(new Callback<ExpressListEntity>() {
                    @Override
                    public void onResponse(Call<ExpressListEntity> call, Response<ExpressListEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        expressList = response.body().list;
                                        break;
                                }
                            }
                        }
                        expressListIsRequesting = false;
                    }

                    @Override
                    public void onFailure(Call<ExpressListEntity> call, Throwable t) {
                        expressListIsRequesting = false;

                    }
                });
    }


    private void initView() {
        tvExpressName = (TextView) findViewById(R.id.tv_express_name);
        ivArrow = (ImageView) findViewById(R.id.iv_arrow);
        llExpress = (LinearLayout) findViewById(R.id.ll_express_select);
        etExpressOrderId = (EditText) findViewById(R.id.et_express_order_id);
        ivClear = (ImageView) findViewById(R.id.iv_clear);
        ivScan = (ImageView) findViewById(R.id.iv_scan);
        tvCancelBtn = (TextView) findViewById(R.id.tv_cancel_btn);
        tvConfirmBtn = (TextView) findViewById(R.id.tv_confirm_btn);

        llExpress.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        ivScan.setOnClickListener(this);
        tvCancelBtn.setOnClickListener(this);
        tvConfirmBtn.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    boolean expressListIsRequesting = false;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_express_select) {
            if (expressListIsRequesting) {
                showToast("正在获取快递信息,请稍后再试");
                return;
            }
            if (null == expressList || expressList.size() <= 0) {
                showToast("快递信息获取错误,请稍后重试");
                getExpressListInfo();
            } else {
                showExpressListPickerView();
            }
        } else if (i == R.id.iv_clear) {
            etExpressOrderId.setText("");
            etExpressOrderId.setHint("请输入快递单号");
        } else if (i == R.id.iv_scan) {
            //打开扫描二维码界面  扫描订单号码
            Intent intent = new Intent();
            intent.putExtra(Constants.JUMP_MIPCA, Constants.EXPRESS_SELECT);
            JumpToOtherPageUtil.getInstance().jumToMipcaActivityCapture(mContext,intent);
        } else if (i == R.id.tv_cancel_btn) {
            finish();
        } else if (i == R.id.tv_confirm_btn) {
            switch (jumpStatus) {
                case "after":
                    afterSend(expressId);
                    break;
                case "order":
                    sendGoods();
                    break;
                case "changeExpress":
                    changeExpressInfo();
                    break;
                default:
                    break;
            }
        }


    }

    /**
     * 售后的换货返修的发货
     *
     * @param expressId
     */
    private void afterSend(String expressId) {
        String expressNumber = getExpressNumber();
        if (expressNumber == null) return;
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .barterExpress(orderIndex, expressId, expressNumber, new Callback<AfterSaleBtnClickResultEntity>() {
                    @Override
                    public void onResponse(Call<AfterSaleBtnClickResultEntity> call, Response<AfterSaleBtnClickResultEntity> response) {
                        HttpResultBean bean = response.body();
                        stopMyDialog();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        Toast.makeText(mContext, response.body().msg, Toast.LENGTH_SHORT).show();
                                        AfterSaleBtnClickResultEntity body = response.body();
                                        Intent intent = new Intent();
                                        intent.putExtra(Constants.LIST_POSITION, listPosition);
                                        intent.putExtra("body", body);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AfterSaleBtnClickResultEntity> call, Throwable t) {
                        stopMyDialog();
                        Toast.makeText(mContext, R.string.aliwx_net_null_setting, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    private String getExpressNumber() {
        if (TextUtils.isEmpty(tvExpressName.getText().toString())) {
            showToast("请选择快递公司");
            return null;
        }
        String expressNumber = etExpressOrderId.getText().toString();
        if (!RegExUtils.isExpressNum(expressNumber)) {
            showToast("快递单号为8-18位字符，支持字母、数字");
            return null;
        }
        return expressNumber;
    }

    /**
     * 弹出快递选择器
     */
    private void showExpressListPickerView() {
        final OptionsPickerView expressOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                ExpressListEntity.ListBean expressBean = expressList.get(options1);
                tvExpressName.setText(expressBean.expressName);
                expressId = expressBean.expressId;
            }
        }).setTitleText("快递选择").setContentTextSize(20)
                .build();
        expressOptions.setPicker(expressList);
        expressOptions.show();
    }

    @SuppressWarnings("unchecked")
    public void sendGoods() {
        String expressNumber = getExpressNumber();
        if (expressNumber == null) return;
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .orderSend("order_sends", RequestOftenKey.getToken(mContext), RequestOftenKey.getDeviceIndex(mContext),
                        orderIndex, parcelIndex, expressId, expressNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantDeliverGoods>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(MerchantDeliverGoods merchantDeliverGoods) {
//                        发货后需要知道 发货后该订单状态，用以区分发货后的状态是全部发货还是部分发货 如果是全部发货，则刷新来源列表，如果是部分刷新，则不刷新来源列表
                        showToast(merchantDeliverGoods.msg);
                        switch (orderStatus) {//订单的状态 0已取消，1待支付 2待出库（待备货）3正在出库（点击完成备货按钮/待发货） 4已部分发货5已全部发货&待收货6已完成
                            case "2":
//                                从待备货进入详情，然后发货，发货后刷新“待备货”列表以及详情界面
                                /**
                                 * {@link com.yilianmall.merchant.fragment.MerchantOrderListFragment}
                                 */
                                EventBus.getDefault().post(new RemoveMerchantManageOrderList(position, TYPE_STOCK));
                                break;
                            case "3":
                            case "4":
                                /**
                                 * {@link com.yilianmall.merchant.fragment.MerchantOrderListFragment}
                                 * {@link MerchantOrderDetailsActivity}
                                 */
//                               ( 从待发货进入详情，然后发货)/(从待发货列表直接发货)，发货后刷新“待发货”列表 以及详情界面
                                switch (merchantDeliverGoods.status) {
                                    case 4://部分发货后，刷新订单列表该条目状态
                                        EventBus.getDefault().post(new UpdateMerchantManageOrderList(position, merchantDeliverGoods.status));
                                        break;
                                    case 5://全部发货后，移除发货列表该条目
                                        EventBus.getDefault().post(new RemoveMerchantManageOrderList(position, TYPE_SEND));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
//                        switch (fromPage) {
//                            case "MerchantOrderListFragment":
////                                /**
////                                 * {@link MerchantOrderListFragment }
////                                 */
////                                EventBus.getDefault().post(new RemoveMerchantManageOrderList(position, TYPE_STOCK));
////                                break;
//                            case "MerchantManageOrderDetailAdapter":
//                            case "MerchantOrderDetailsActivity":
//                                /**
//                                 * {@link MerchantOrderListFragment }
//                                 */
//                                EventBus.getDefault().post(new RemoveMerchantManageOrderList(position, TYPE_SEND));
//                                break;
//                        }
                    }
                });
        addSubscription(subscription);
    }

    @SuppressWarnings("unchecked")
    void changeExpressInfo() {
        String expressNumber = getExpressNumber();
        if (expressNumber == null) return;
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .updateParcelInfo("update_parcel_info", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                        parcelIndex, expressId, expressNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        showToast(httpResultBean.msg);
                        /**
                         * {@link MerchantOrderDetailsActivity}
                         */
                        EventBus.getDefault().post(new ChangeExpressSuccessEvent());
                    }
                });
        addSubscription(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver)
            unregisterReceiver(receiver);
    }
}
