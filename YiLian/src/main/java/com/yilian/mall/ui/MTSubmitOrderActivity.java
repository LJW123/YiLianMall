package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTEntity;
import com.yilian.mall.entity.MTSubmitOrderEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.NumberAddSubView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.entity.UserAddressLists;


/**
 * Created by liuyuqi on 2016/12/6 0006.
 * 提交订单配送/不配送界面
 */
public class MTSubmitOrderActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private RelativeLayout mRlFavorite;
    private TextView mTvOrderName;
    private TextView mTvSubmitPrice;
    private NumberAddSubView mNaOrderCount;
    private Button mBtnNeedDelivery;
    private RelativeLayout mRlDelivery;
    private TextView mTvTotalPrice;
    private LinearLayout mllNoHasDelivery;
    private TextView mHasDelivery;
    private LinearLayout mLlHasDelivery;
    private Button btnSubmitOrder;
    private Context mContext;
    private MTNetRequest netRequest;
    private Button mBtnSub;
    private EditText etCount;
    private Button mBtnReduce;
    private Button mBtnAdd;
    private int mEtcount;
    private String address_id;
    private boolean hasAddress;
    private TextView mtvAddress;
    private String package_id;
    private String address;
    private LinearLayout mllGoTAddress;
    private String orderName;
    private String price;
    private String priceFromNet;
    public static final int RESULT_FROM_ACTIVITY = 11;
    private String fromAddress;
    private TextView mAllHasDelivery;
    private LinearLayout mllGoToAddress;
    private String isDelivery;
    private String merchantId;
    private Double fullMinusfee;

    ///yilian
    private TextView tvIntegral;
    private String returnIntegral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mt_activity_submit_order);
        mContext = this;
        initView();
    }

    private void initView() {
        //默认显示的界面是未配送 从上个界面传递过来的数据
        Intent intent = getIntent();
        package_id = intent.getStringExtra("package_id");
        address_id = intent.getStringExtra("address_id");
        price = intent.getStringExtra("price");
        isDelivery = intent.getStringExtra("isDelivery");
        merchantId = intent.getStringExtra("merchantId");
        Logger.i("传递的isDelivery" + isDelivery);
        Logger.i("传递的price" + price);
        String orderName = intent.getStringExtra("orderName");

        priceFromNet = "0";//默认配送金额
        //满减赠送
        String fullMinusFee = getIntent().getStringExtra("fullMinusFee");
        Logger.i("传值后" + fullMinusFee);
        //计算当前的总价有没有超出满减免费送
        fullMinusfee = Double.parseDouble(fullMinusFee);

        returnIntegral = intent.getStringExtra("return_integral");


        Logger.i("address_id" + address_id);
        mEtcount = 1;//默认一份
        if (null == address_id) {
            hasAddress = false;
        } else {
            hasAddress = true;
        }

        TextView tvHint = (TextView) findViewById(R.id.tv_hint);
        tvHint.setText("注:" + getResources().getString(R.string.integral) + "每天可以发奖励，还可以在商城购买商品时，直接抵现金使用");
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mRlFavorite = (RelativeLayout) findViewById(R.id.rl_favorite);
        mTvOrderName = (TextView) findViewById(R.id.tv_order_name);
        mTvOrderName.setText(orderName);
        mTvSubmitPrice = (TextView) findViewById(R.id.tv_submit_price);
        mTvSubmitPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(price)));
        mNaOrderCount = (NumberAddSubView) findViewById(R.id.na_order_count);
        mNaOrderCount.setValue(mEtcount);
        mBtnNeedDelivery = (Button) findViewById(R.id.btn_need_delivery);
        mRlDelivery = (RelativeLayout) findViewById(R.id.rl_delivery);
        mTvTotalPrice = (TextView) findViewById(R.id.tv_total_price_no_delivery);
        mTvTotalPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(price)));
        mllNoHasDelivery = (LinearLayout) findViewById(R.id.ll_no_delivery);
        mHasDelivery = (TextView) findViewById(R.id.tv_deivery_price);
        mAllHasDelivery = (TextView) findViewById(R.id.tvall_deivery_price);
        mLlHasDelivery = (LinearLayout) findViewById(R.id.ll_has_delivery);
        btnSubmitOrder = (Button) findViewById(R.id.submit_order);
        etCount = (EditText) findViewById(R.id.etxt_num);
        etCount.setText(String.valueOf(mEtcount));
        mBtnReduce = (Button) findViewById(R.id.btn_sub);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        mtvAddress = (TextView) findViewById(R.id.tv_submit_address);
        mllGoToAddress = (LinearLayout) findViewById(R.id.ll_goto_address);

        mllGoToAddress.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mBtnNeedDelivery.setOnClickListener(this);
        btnSubmitOrder.setOnClickListener(this);
        mBtnReduce.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);

        if (netRequest == null) {
            netRequest = new MTNetRequest(mContext);
        }

        ///yilian
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvIntegral.setText(MoneyUtil.getLeXiangBiNoZero(returnIntegral) + getResources().getString(R.string.integral));
    }

    private void initStartAcitiviyData() {
        //说明是不配送订单，判断当前是否支持配送
        String mEtcountStr = String.valueOf(mEtcount);
        if (address_id == null) {
            address_id = "0";
        }
        Logger.i("address_id" + address_id);
        startMyDialog();
        Logger.i("请求前参数package_Id " + package_id + " address_id " + address_id + " count " + mEtcountStr);

        netRequest.getSubmitOrderList(package_id, address_id, mEtcountStr, new RequestCallBack<MTSubmitOrderEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MTSubmitOrderEntity> responseInfo) {
                stopMyDialog();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        MTSubmitOrderEntity result = responseInfo.result;
                        //赋值跳转确认支付
                        //价格是配送费和套餐费的总和
                        int totalMoney;
                        if (priceFromNet != null) {
                            totalMoney = (Integer.parseInt(result.money) + Integer.parseInt(priceFromNet));
                        } else {
                            totalMoney = Integer.parseInt(responseInfo.result.money);
                        }

                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);

                        Intent intent = new Intent(mContext, CashDeskActivity.class);
                        intent.putExtra("order_total_lebi", String.valueOf(totalMoney));
                        intent.putExtra("order_total_coupon", "0");//套餐支付不需要抵扣券
                        intent.putExtra("payType", "6"); //6为套餐
                        intent.putExtra("orderIndex", result.orderId);
                        intent.putExtra("orderNumber", mEtcountStr);
                        intent.putExtra("type", "MTPackage");
                        startActivity(intent);
                        finish();
                        Logger.i("提交订单数据count:  " + result.count + "moeny:::" + totalMoney + "orderid::" + result.orderId + "name::" +
                                result.name);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //不支持配送
        if (!hasAddress) {
            mllNoHasDelivery.setVisibility(View.VISIBLE);
            mLlHasDelivery.setVisibility(View.GONE);
        } else {
            mLlHasDelivery.setVisibility(View.VISIBLE);
            mllNoHasDelivery.setVisibility(View.GONE);
            //支持配送的话去请求配送费用
            initDeliveryPrice();
        }
    }

    private void initDeliveryPrice() {
        String mEtcountStr = String.valueOf(mEtcount);
        Logger.i("请求数据的addressid" + address_id);
        if (address_id == null) {
            address_id = "0";
        }
        startMyDialog();
        netRequest.switchMT(package_id, mEtcountStr, address_id, "0", "0", new RequestCallBack<MTEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MTEntity> responseInfo) {
                MTEntity result = responseInfo.result;
                double summation;
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, result)){
                    if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(mContext, result.code, result.msg)){
                        switch (result.code) {
                            case 1:
                                priceFromNet = result.price;
                                Logger.i("请求的配送费" + priceFromNet + "商品价格" + price);
                                summation = Double.parseDouble(price) * mEtcount + Double.parseDouble(priceFromNet);
                                double comboTotalPrice = Double.parseDouble(price) * mEtcount;
                                Logger.i("请求合计价格" + summation);
                                mtvAddress.setText(fromAddress);
                                if (comboTotalPrice >= fullMinusfee) {
                                    mHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi("0")));
                                    mAllHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(comboTotalPrice)));
                                } else {
                                    mHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(priceFromNet)));
                                    mAllHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(summation)));
                                }
                                break;

                        }
                    }
                    //此处单独处理返回码-70时配送范围超出时 价格的显示问题
                    switch (result.code){
                        case -70:
                        case 0:
                            priceFromNet = "0";
                            mHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(priceFromNet)));
                            mtvAddress.setText("商品" + result.msg);
                            summation = Double.parseDouble(price) * mEtcount;
                            Logger.i("请求合计价格" + summation);
                            mAllHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(summation)));
                            break;
                    }
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_need_delivery:
            case R.id.ll_goto_address:
                //需要配送按钮点击跳转设置地址界面
                selectAddress();
                break;
            case R.id.submit_order:
                //提交订单跳转确认支付
                initStartAcitiviyData();
                break;
            case R.id.btn_sub:
                if (mEtcount <= 1) {
                    return;
                }
                mEtcount--;
                switchPrice();
                break;
            case R.id.btn_add:
                mEtcount++;
                switchPrice();
                break;
        }
    }

    //选择地址界面
    private void selectAddress() {
        if ("0".equals(isDelivery)) {
            showToast("此套餐暂不支持配送");
            return;
        }
        Intent intent = new Intent(this.mContext, AddressManageActivity.class);
        intent.putExtra("FLAG", "fromMT");//判断是从下订单进入还是从我的地址进入
        if (address_id != null) {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, address_id);//选中的标示
        } else {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
        }
        startActivityForResult(intent, RESULT_FROM_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                UserAddressLists userAddressList = (UserAddressLists) data.getExtras().getSerializable("USE_RADDRESS_LIST");
                address = userAddressList.address;
                Logger.i("address" + address);
                if (null != address) {
                    hasAddress = true;
                }
                address_id = userAddressList.address_id;
                Logger.i("activity传递addressid" + address_id);
                fromAddress = userAddressList.province_name + userAddressList.city_name + userAddressList.county_name + userAddressList.fullAddress + userAddressList.address;
                break;
        }
    }


    private void switchPrice() {
        double summation;
        summation = mEtcount * Double.parseDouble(price) + Double.parseDouble(priceFromNet);
        double comboTotalPrice = mEtcount * Double.parseDouble(price);
        if (hasAddress) {
            //带配送地址金额
            etCount.setText(String.valueOf(mEtcount));
            mHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(priceFromNet)));
            Logger.i("提交订单的配送费=" + summation + "满减送金额" + fullMinusfee);
            if (comboTotalPrice >= fullMinusfee) {
                mHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi("0")));
                mAllHasDelivery.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(comboTotalPrice)));
            } else {
                //小于的时候重新请求数据
                initDeliveryPrice();
            }
        } else {
            etCount.setText(String.valueOf(mEtcount));
            summation = mEtcount * Double.parseDouble(price);
            mTvTotalPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(summation)));
        }

        tvIntegral.setText(MoneyUtil.getLeXiangBiNoZero(Double.parseDouble(returnIntegral) * mEtcount) + getResources().getString(R.string.integral));
    }

}
