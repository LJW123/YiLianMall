package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseListAdapter;
import com.yilian.mall.alipay.PayResult;
import com.yilian.mall.entity.GoodsChargeForPayResultEntity;
import com.yilian.mall.entity.OrderSubmitGoods;
import com.yilian.mall.entity.PayTypeListEntity;
import com.yilian.mall.entity.PaymentIndexEntity;
import com.yilian.mall.entity.UserDefaultAddressEntity;
import com.yilian.mall.entity.WeiXinOrderEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.http.PaymentNetRequest;
import com.yilian.mall.http.WeiXinNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.entity.LimitBuyMakeOrderEntity;
import com.yilian.networkingmodule.entity.SpellGroupOrderEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeOutFlashSalePayActivity extends BaseActivity implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;
    private TextView tvGoodsName;
    private TextView tvQuanPrice;
    private TextView tvExpressPrice;
    private TextView tvContent;
    private TextView tvAddress;
    private TextView tvBalancePrice;
    private NoScrollListView listView;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private Button btnNowBuyCom;
    private String flashGoodsName;
    private MallNetRequest mallNetRequest;
    private JPNetRequest jpNetRequest;
    private float totalLebi;
    private float totalVoucher;
    private float fExpressPrice;
    private ImageView ivBalanceSelect;
    private PaymentNetRequest paymentNetRequest;
    private boolean isHasTotalBanalce;
    private boolean isHasTotalVoucher;
    private LinearLayout llAddress;
    private PayTypeListAdapter adapter;
    private int selectPosition = -1;
    private PayDialog payDialog;
    private int payType;
    private ArrayList<PayTypeListEntity.DataBean> payTypeList;
    private WeiXinNetRequest weiXinNetRequest;
    private String price, fregihtPrice, phone, orderIndex, addressId, paymentFee;
    private String orderId = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.EXECUTE_SUCCESS:
                    dismissInputMethod();
                    finish();
                    break;
                case SDK_PAY_FLAG:
                    Logger.i("msg.obj  " + msg.obj.toString() + "    msg.obj " + msg.obj);
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**`
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();//返回状态码
                    Logger.i("resultStatus   22222222" + resultStatus);
                    switch (resultStatus) {
                        case "9000":
                            //成功
                            getPayResult();
                            break;
                        case "8000":
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            showToast("支付结果确认中");
                            sp.edit().putString("type", "").commit();
                            sp.edit().putString("lebiPay", "false").commit();
                            break;
                        case "4000":
                            showToast("请安装支付宝插件");
                            sp.edit().putString("type", "").commit();
                            sp.edit().putString("lebiPay", "false").commit();
                            break;
                        default:
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showToast("支付失败");
                            sp.edit().putString("type", "").commit();
                            sp.edit().putString("lebiPay", "false").commit();
                            break;
                    }
                    break;

            }
        }
    };
    private int getPayResultTimes = 0;
    private String goodsId;
    private String bindPhone;
    private MyIncomeNetRequest myIncomeNetRequest;
    private float moreOverLeBi;
    private String paymentIndex;
    private String jumpType;
    private LinearLayout llHasIcon;
    private ImageView goodsIcon;
    private TextView tvTag;
    private TextView tvName;
    private TextView tvCount;
    private TextView tvPrice;
    private EditText etNote;
    private String imageUrl;
    private String mCount;
    private String label;
    private String goodSku;
    private String activityId;
    private String spellGroupPayType;
    private String orderRemark;
    private String type;
    private TextView tvLingQuan;
    private TextView tvSku;
    private String sku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_out_flash_sale_pay);
        if (!isLogin()) {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
        }
        OrderSubmitGoods orderSubmitGoods = (OrderSubmitGoods) getIntent().getSerializableExtra("orderSubmitGoods");
        price = String.valueOf(orderSubmitGoods.goodsPrice);
        goodsId = orderSubmitGoods.goodsId;
        jumpType = orderSubmitGoods.type;
        flashGoodsName = orderSubmitGoods.name;
        imageUrl = orderSubmitGoods.goodsPic;
        mCount = String.valueOf(orderSubmitGoods.goodsCount);
        fregihtPrice = orderSubmitGoods.fregihtPrice;
        label = orderSubmitGoods.label;
        goodSku = orderSubmitGoods.sku;
        activityId = orderSubmitGoods.activityId;
        sku = orderSubmitGoods.sku;

        Logger.i("接受的Activity Id  " + activityId);
        spellGroupPayType = orderSubmitGoods.payType;
        if (TextUtils.isEmpty(fregihtPrice)) {
            fregihtPrice = "0";
        }


        initView();
        switchPrice();
        getDefaultAddress();
        getPayTypeList();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindPhone = sp.getString(Constants.SPKEY_PHONE, "");
        etNote.setEnabled(true);
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.i("isHasToatalLebi  " + isHasTotalBanalce);
                if (!isHasTotalBanalce) {

                    PayTypeListEntity.DataBean payList = (PayTypeListEntity.DataBean) parent.getItemAtPosition(position);
                    payType = payList.payType;
                    if (payList.isuse == 1) {
                        selectPosition = position;
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void switchPrice() {
        totalLebi = Float.parseFloat(sp.getString("availableMoney", "0.00")) / 100;
        totalVoucher = Float.parseFloat(sp.getString("voucher", "0.00")) / 100;
        float payPrice = Float.parseFloat(price) / 100;
        fExpressPrice = Float.parseFloat(fregihtPrice) / 100;

        Logger.i("totalLebi  " + totalLebi + " \ntotalVoucher " + totalVoucher + " \nprice   " + price + "\nfExpressPrice" + fExpressPrice);
        //判断是哪个界面跳转的支付
        switch (jumpType) {
            case Constants.SPELL_GROUP://拼团 只需支付零购券没有运费,这个是如果零购券不足，支付的时候直接谈Toast
                initPayPrice(payPrice, totalVoucher);
                break;
            case Constants.FLASHSALEPAY://限时购 零购券+运费
                initPayPrice(fExpressPrice, totalLebi);//支付的时候根据运费和奖励来判断
                break;
        }


    }

    private void initPayPrice(float price, float totalPrice) {
        //支付的金额大于可用奖励
        if (price > totalPrice) {//如果还需要充值，即钱不够,提示还需支付多少，那么奖励可支付数量就是用户奖励
            ivBalanceSelect.setImageResource(R.mipmap.library_module_cash_desk_off);
            selectPosition = 0;
            //差值的计算必须是分为单位的
            moreOverLeBi = price - totalPrice;
            btnNowBuyCom.setText("确认支付(还需支付" + MoneyUtil.set¥Money(String.valueOf(moreOverLeBi)) + ")");//显示需要充值的金额
            isHasTotalBanalce = false;
            isHasTotalVoucher = false;
        } else {//钱够了，那么奖励可支付数量就是订单金额
            ivBalanceSelect.setImageResource(R.mipmap.library_module_cash_desk_on);
            moreOverLeBi = price;
            isHasTotalBanalce = true;
            isHasTotalVoucher = true;
        }

    }

    private void initView() {
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
        tvQuanPrice = (TextView) findViewById(R.id.tv_quan_price);
        tvExpressPrice = (TextView) findViewById(R.id.tv_express_price);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvBalancePrice = (TextView) findViewById(R.id.tv_balance_price);
        ivBalanceSelect = (ImageView) findViewById(R.id.iv_balance_select);
        listView = (NoScrollListView) findViewById(R.id.listView);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setVisibility(View.VISIBLE);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("限时抢购");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        btnNowBuyCom = (Button) findViewById(R.id.btn_now_buy_com);
        llAddress = (LinearLayout) findViewById(R.id.ll_address);
        tvLingQuan = (TextView) findViewById(R.id.tv_linggouquan);
        llHasIcon = (LinearLayout) findViewById(R.id.ll_has_icon);
        goodsIcon = (ImageView) findViewById(R.id.iv_icon);
        tvTag = (TextView) findViewById(R.id.tv_tag);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvSku = (TextView) findViewById(R.id.tv_sku);
        tvCount = (TextView) findViewById(R.id.tv_goods_count);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        etNote = (EditText) findViewById(R.id.et_note);

        //拼团才显示图片
        if (jumpType.equals(Constants.SPELL_GROUP)) {
            type = "11";
            tvLingQuan.setVisibility(View.GONE);
            llHasIcon.setVisibility(View.VISIBLE);
            tvName.setText(flashGoodsName);
            tvTag.setText(label);
            tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(price)));
            tvExpressPrice.setText(fregihtPrice);
            tvQuanPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(price)));
            tvCount.setText("×" + mCount);
            GlideUtil.showImage(mContext, imageUrl, goodsIcon);
            tvSku.setText(sku);
        } else {
            type = "9";
            tvLingQuan.setVisibility(View.VISIBLE);
            tvGoodsName.setVisibility(View.VISIBLE);
            tvGoodsName.setText(flashGoodsName);
            tvExpressPrice.setText(MoneyUtil.getLeXiangBi(fregihtPrice));
            tvQuanPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(price)));
        }

        v3Back.setOnClickListener(this);
        btnNowBuyCom.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        ivBalanceSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_now_buy_com:
                btnNowBuy();
                break;
            case R.id.ll_address:
                selectAddress();
                break;
            case R.id.iv_balance_select:
                switchImageSelect();
                break;
        }
    }

    private void btnNowBuy() {
        orderRemark = etNote.getText().toString().trim();
        if (TextUtils.isEmpty(orderRemark)) {
            orderRemark = "";
        }
        etNote.setEnabled(false);
        etNote.setFocusable(false);
        if (!isHasTotalVoucher) {
            showToast("零购券不足");
            return;
        }
        //如果使用现金支付并且奖励够用
        if (isHasTotalBanalce) {
            pay();
        } else {
            //第三方支付方式支付
            if (null != payTypeList) {
                if (payTypeList.get(selectPosition).isuse == 1) {
                    switch (payTypeList.get(selectPosition).payType) {
                        case 0://支付宝
                            jumpToZhiFuBao();
                            break;
                        case 1://微信
                            jumpToWeChar();
                            break;
                        case 2://银联支付
                            jumpToUnionpay();
                            break;

                    }
                }
            }
        }

    }

    private void jumpToZhiFuBao() {
        payType = 0;
        switch (jumpType) {
            case Constants.SPELL_GROUP:
                getSpellGroupPayOrderId();
                break;
            case Constants.FLASHSALEPAY:
                sp.edit().putString("lebiPay", "true").commit();
                startMyDialog();
                limitBuyMakeOrder();
                break;
        }
    }

    private void jumpToWeChar() {
        payType = 1;
        switch (jumpType) {
            case Constants.SPELL_GROUP:
                getSpellGroupPayOrderId();
                break;
            case Constants.FLASHSALEPAY:
                sp.edit().putString("lebiPay", "true").commit();
                startMyDialog();
                limitBuyMakeOrder();
                break;
        }
    }

    /**
     * 银联支付
     */
    private void jumpToUnionpay() {
        //银联或者财付通支付（跳转网页）
        //因为上个界面并没有 得到当前的订单Id所需要去请求预支付的订单借口获取当前的订单
        payType = 2;
        switch (jumpType) {
            case Constants.SPELL_GROUP:
                getSpellGroupPayOrderId();
                break;
            case Constants.FLASHSALEPAY:
                sp.edit().putString("lebiPay", "true").commit();
                startMyDialog();
                limitBuyMakeOrder();
                break;
        }
    }

    /**
     * 请求充值的订单
     */
    private void limitBuyMakeOrder() {
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .rechargLimitBuyMakeOrder(goodsId, addressId, new Callback<LimitBuyMakeOrderEntity>() {
                    @Override
                    public void onResponse(Call<LimitBuyMakeOrderEntity> call, Response<LimitBuyMakeOrderEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        stopMyDialog();
                                        orderId = response.body().orderId;
                                        Logger.i("生成预支付订单  " + orderId);
                                        switch (payType) {
                                            case 0: //支付宝
                                                chrage();
                                                Logger.i("zhifubao 2222222222222222222222222");
                                                break;
                                            case 1: //微信
                                                chrage();
                                                Logger.i("weixin 2222222222222222222222222");
                                                break;
                                            case 2:
                                                chrage();
                                                Logger.i("银联 2222222222222222222222222");
                                                break;
                                        }
                                        break;
                                    default:
                                        Logger.i("code 111111" + response.body().code + response.body().msg);
                                        stopMyDialog();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LimitBuyMakeOrderEntity> call, Throwable t) {
                        Logger.e(getClass().getSimpleName() + t);
                        stopMyDialog();
                    }
                });
    }

    //调用支付前要先调用拼团下订单借口
    private void getSpellGroupPayOrderId() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSpellGroupOrderData(activityId, "0", addressId, spellGroupPayType, goodsId, goodSku, mCount, orderRemark, new Callback<SpellGroupOrderEntity>() {
                    @Override
                    public void onResponse(Call<SpellGroupOrderEntity> call, Response<SpellGroupOrderEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        orderId = response.body().orderId; //生成预支付的订单ID
                                        Logger.i("生成预支付的订单 拼团  " + orderId);

                                        Logger.i("isHasTotalBanalce  " + isHasTotalBanalce);
                                        if (!isHasTotalBanalce) {
                                            Logger.i("isHasTotalBanalce  " + isHasTotalBanalce + "  " + payTypeList.get(selectPosition).payType);
                                            switch (payTypeList.get(selectPosition).payType) {
                                                case 0://支付宝
                                                    chrage();
                                                    break;
                                                case 1://微信
                                                    chrage();
                                                    break;
                                                case 2://银联或者财付通支付（跳转网页）
                                                    chrage();
                                                    break;
                                            }
                                        } else {
                                            payDialog.paySpellGroupOrder(orderId, payDialog.password);
                                            payDialog.dismissJP();
                                            payDialog.dismiss();
                                        }
                                        break;
                                    default:
                                        Logger.i(getClass().getSimpleName() + response.body().code);
                                        stopMyDialog();
                                        payDialog.dismissJP();
                                        payDialog.dismiss();
                                        break;
                                }
                            } else {
                                stopMyDialog();
                                payDialog.dismissJP();
                                payDialog.dismiss();
                                Logger.i(getClass().getSimpleName() + response.body().code);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SpellGroupOrderEntity> call, Throwable t) {
                        stopMyDialog();
                        payDialog.dismissJP();
                        payDialog.dismiss();
                    }
                });

    }

    /**
     * 生成商品的预支付订单
     */
    private void chrage() {

        if (myIncomeNetRequest == null) {
            myIncomeNetRequest = new MyIncomeNetRequest(mContext);
        }

        Logger.i(payType + "payType");
        Logger.i("充值参数1：  payType:" + payType + "TYPE  " + type + "  支付的价格:" + moreOverLeBi + "  orderIndex:" + orderId);
        if (!TextUtils.isEmpty(orderId)) {
            myIncomeNetRequest.NPaymentIndexNet(String.valueOf(payType), type, MoneyUtil.getLeXiangBi(moreOverLeBi), orderId, new RequestCallBack<PaymentIndexEntity>() {
                @Override
                public void onStart() {
                    super.onStart();
                    startMyDialog();
                }

                @Override
                public void onSuccess(ResponseInfo<PaymentIndexEntity> responseInfo) {
                    PaymentIndexEntity result = responseInfo.result;
                    Logger.i(result.code + "responseInfo.result.code");
                    stopMyDialog();
                    switch (result.code) {
                        case 1:
                            paymentFee = result.paymentFee;//充值金额
                            paymentIndex = result.paymentIndex;
                            switch (payType) {
                                case 0:
                                    zhifubao(result.orderString);
                                    break;
                                case 1:
                                    weCharOrder();
                                    break;
                                case 2:
                                    //银联或者财付通支付（跳转网页）
                                    Intent intent = new Intent(mContext, WebViewActivity.class);
                                    Logger.i("payType1:" + payType + "  paymentIndex:" + paymentIndex + "  orderId  " + orderId);
                                    String yinLianPayUrl = Ip.getBaseURL(mContext) + payTypeList.get(selectPosition).content + "?token=" + RequestOftenKey.getToken(mContext)
                                            + "&device_index=" + RequestOftenKey.getDeviceIndex(mContext)
                                            + "&type=" + type + "&money=" + Float.valueOf(moreOverLeBi) + "&order_id=" + orderId;
                                    Logger.i("银联支付链接：" + yinLianPayUrl);
                                    intent.putExtra("url", yinLianPayUrl);//此处money单位是分
                                    intent.putExtra("isRecharge", true);
                                    startActivity(intent);
                                    getPayResult();
                                    break;

                            }
                            break;
                        case -23:
                            showToast(R.string.login_failure);
                            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                            break;
                        default:
                            showToast("支付前错误码:" + result.code);
                            break;
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
    }

    /**
     * 微信支付
     */
    private void weCharOrder() {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(Constants.WXCHARGEFORPAY, true).commit();//微信支付的类型，该笔支付使用直接支付的，而不是充值
        edit.putString(Constants.WXPAYORDER, orderId).commit();//微信支付的订单ID
        edit.putString(Constants.WXPAYTYPE1, payType + "").commit();//微信支付的类型
        int orderNumer = 1;
        edit.putString(Constants.WXPAYORDERNUMBER, String.valueOf(orderNumer)).commit();//该笔微信支付的订单数量

        if (weiXinNetRequest == null) {
            weiXinNetRequest = new WeiXinNetRequest(mContext);
        }
        startMyDialog();
        Logger.i("weCharOrder  " + paymentFee + "\norderId" + orderId);
        weiXinNetRequest.WeiXinOrder(paymentFee, orderId, new RequestCallBack<WeiXinOrderEntity>() {

            @Override
            public void onSuccess(ResponseInfo<WeiXinOrderEntity> responseInfo) {
                Logger.i(responseInfo.result.code + "  " + responseInfo.result.appId);
                IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, responseInfo.result.appId);
                wxapi.registerApp(responseInfo.result.appId);
                PayReq payReq = new PayReq();
                payReq.appId = responseInfo.result.appId;
                payReq.partnerId = responseInfo.result.partnerId;
                payReq.prepayId = responseInfo.result.prepayId;
                payReq.nonceStr = responseInfo.result.nonceStr;
                payReq.timeStamp = responseInfo.result.timeStamp;
                payReq.packageValue = responseInfo.result.packageValue;
                payReq.sign = responseInfo.result.sign;
                payReq.extData = "app data";


                wxapi.sendReq(payReq);
                stopMyDialog();
                getPayResult();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("支付失败11111");
            }
        });
    }

    /**
     * 支付宝支付
     *
     * @param orderinfo
     */
    private void zhifubao(String orderinfo) {
        //支付宝的私钥放置在服务器，返回相对应的字段
        if (TextUtils.isEmpty(orderinfo)) {
            return;
        }
        stopMyDialog();
        Logger.i("orderinfo  " + orderinfo);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((TakeOutFlashSalePayActivity) mContext);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(orderinfo, true);
                Logger.i("result " + result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }


    private void pay() {
        Logger.i("bindPhone  " + bindPhone);
        if (TextUtils.isEmpty(bindPhone)) {
            //奖励支付时，先要验证是否有绑定手机号（因为奖励支付需要支付密码，支付密码的设置必须有手机号码）
            new VersionDialog.Builder(mContext)
                    .setMessage("请绑定手机号码")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext, BindPhoneActivity.class));
                            dialog.dismiss();
                        }
                    }).create().show();
            return;
        }
        //奖励支付实，先检测是否有支付密码，如果有直接支付，如果没有则提示跳转密码支付界面
            if(PreferenceUtils.readBoolConfig(com.yilian.mylibrary.Constants.PAY_PASSWORD,mContext,false)){//有支付密码
            payDialog = new PayDialog(TakeOutFlashSalePayActivity.this, orderIndex, handler);
            payDialog.show();
        } else {
            //没有支付密码,提示跳转设置支付密码
            new VersionDialog.Builder(mContext)
                    .setMessage("您还未设置支付密码，请设置支付密码后在支付！")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext, InitialPayActivity.class));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }

    }


    public class PayDialog extends Dialog {

        private BaseActivity context;
        private ImageView ivDismiss;
        private TextView tvForgetPwd;
        private GridPasswordView pwdView;
        private String password;

        public PayDialog(BaseActivity context, String orderIndex, Handler handler) {
            super(context, R.style.GiftDialog);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);
            initView();
            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        private void initView() {
            ivDismiss = (ImageView) findViewById(R.id.img_dismiss);
            tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {
                }

                @Override
                public void onMaxLength(String psw) {
                    Logger.i("Psw  " + psw + "   " + pwdView.getPassWord());
                    //支付的啥时候需要判断是限时购还是拼团
                    switch (jumpType) {
                        case Constants.SPELL_GROUP:
                            getSpellGroupPayOrderId();
                            password = psw;
                            break;
                        case Constants.FLASHSALEPAY:
                            sendFlashSalePayOrder(psw);
                            break;
                    }
                }
            });
            ivDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tvForgetPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, InitialPayActivity.class));
                }
            });


            Window dialogWindow = getWindow();
            WindowManager windowManager = context.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);

        }

        //本地配送的支付
        private void sendFlashSalePayOrder(String psw) {
            //支付的密码必须要MD5加密
            String passWord = CommonUtils.getMD5Str(psw).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
            RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                    .activityPaySendOrder(goodsId, addressId, passWord, new Callback<com.yilian.networkingmodule.entity.PaymentIndexEntity>() {
                        @Override
                        public void onResponse(retrofit2.Call<com.yilian.networkingmodule.entity.PaymentIndexEntity> call, Response<com.yilian.networkingmodule.entity.PaymentIndexEntity> response) {
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                                if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                    switch (response.body().code) {
                                        case 1:
                                            dismissJP();
                                            payDialog.dismiss();
                                            com.yilian.networkingmodule.entity.PaymentIndexEntity.DataBean result = response.body().data;
                                            jumpToNOrderPaySuccessActivity(result.lebi, "", "", orderIndex, result.paytime, result.voucher, "", result.voucher);
                                            break;
                                        case -5:
                                            showErrorPWDDialog();
                                            stopMyDialog();
                                            payDialog.dismiss();
                                            break;
                                        default:
                                            dismissJP();
                                            payDialog.dismiss();
                                            Logger.e(response.body().code + response.body().msg);
                                            break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<com.yilian.networkingmodule.entity.PaymentIndexEntity> call, Throwable t) {
                            Logger.e("本地配送支付异常", t);
                            payDialog.dismissJP();
                            payDialog.dismiss();
                        }
                    });
        }

        /**
         * 支付密码填写错误后，弹出重置框
         */
        public void showErrorPWDDialog() {
            context.showDialog(null, "密码错误，请重新输入", null, 0, Gravity.CENTER, "重置密码", "重新输入", false, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //重置密码
                            dialog.dismiss();
                            context.startActivity(new Intent(context, InitialPayActivity.class));
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //重新输入
                            dialog.dismiss();
                            break;
                    }
                }
            }, context);

        }

        //软键盘消失
        public void dismissJP() {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        public void paySpellGroupOrder(String orderId, String pwd) {
            //奖励支付
            Logger.i("paySpellGroupOrder  " + orderId + "  pwd   " + pwd);
            String passWord = CommonUtils.getMD5Str(pwd).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
            RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                    .getSpellGoupOrderPayData(orderId, passWord, new Callback<SpellGroupOrderEntity>() {
                        @Override
                        public void onResponse(Call<SpellGroupOrderEntity> call, Response<SpellGroupOrderEntity> response) {
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                                if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                    switch (response.body().code) {
                                        case 1:
                                            stopMyDialog();
                                            payDialog.dismissJP();
                                            payDialog.dismiss();
                                            String groupId = response.body().groupId;
                                            jumpToSpellGroupResultActivity(response.body().groupId, response.body().activityId, response.body().orderId);
                                            break;
                                        case -5:
                                            showErrorPWDDialog();
                                            stopMyDialog();
                                            break;
                                        default:
                                            stopMyDialog();
                                            payDialog.dismissJP();
                                            payDialog.dismiss();
                                            Logger.i(getClass().getSimpleName() + response.body().code);
                                            break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SpellGroupOrderEntity> call, Throwable t) {
                            stopMyDialog();
                            payDialog.dismissJP();
                            payDialog.dismiss();
                        }
                    });
        }
    }

    //跳转拼团状态界面
    private void jumpToSpellGroupResultActivity(String groupId, String activityId, String orderId) {
        showToast("支付成功");
        Intent intent = new Intent(mContext, SpellGroupResultStatusActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("activityId", activityId);
        intent.putExtra("orderId", orderId);
        Logger.i("传递前的groupId  " + groupId);
        startActivity(intent);
        finish();
    }

    private void switchImageSelect() {
        isHasTotalBanalce = !isHasTotalBanalce;
        if (isHasTotalBanalce) { //使用奖励
            ivBalanceSelect.setImageResource(R.mipmap.library_module_cash_desk_on);
            if (Float.parseFloat(price) > totalLebi) { //奖励不足
                //默认选中支付方式列表第一个支付方式
                if (payTypeList.size() > 0) {
                    payType = payTypeList.get(0).payType;
                    if (payTypeList.get(0).isuse == 1) {
                        selectPosition = 0;
                        adapter.notifyDataSetChanged();
                    }
                }
                btnNowBuyCom.setText("确认支付(还需支付" + MoneyUtil.set¥Money(String.valueOf(moreOverLeBi)) + ")");
            } else {//奖励足够
                selectPosition = -1;
                adapter.notifyDataSetChanged();
                btnNowBuyCom.setText("立即支付");
            }
        } else {//不使用奖励
            //默认选中支付方式列表第一个支付方式
            if (payTypeList.size() > 0) {
                payType = payTypeList.get(0).payType;
                if (payTypeList.get(0).isuse == 1) {
                    selectPosition = 0;
                    adapter.notifyDataSetChanged();
                }
            }
            ivBalanceSelect.setImageResource(R.mipmap.library_module_cash_desk_off);
            btnNowBuyCom.setText("确认支付(还需支付" + MoneyUtil.set¥Money(String.valueOf(moreOverLeBi)) + ")");
        }

    }

    public void getDefaultAddress() {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        mallNetRequest.getDefaultUserAddress(UserDefaultAddressEntity.class, new RequestCallBack<UserDefaultAddressEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UserDefaultAddressEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        UserAddressLists addressLists = responseInfo.result.info;
                        addressId = addressLists.address_id;
                        phone = addressLists.phone;
                        tvContent.setText(addressLists.contacts + "    " + phone);
                        tvAddress.setText(addressLists.province_name + addressLists.city_name + addressLists.county_name + addressLists.fullAddress + addressLists.address);
                        break;
                    case -3:
                        showToast("系统繁忙请稍后重试");

                        break;
                    case -4:
                        new Intent(mContext, LeFenPhoneLoginActivity.class);

                        break;
                    case -21:
                        showDialog("温馨提示", "请设置收货地址", null, 0, Gravity.CENTER, "确定", "取消", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        dialog.dismiss();
                                        selectAddress();
                                        break;
                                    case DialogInterface.BUTTON_NEUTRAL:
                                        dialog.dismiss();
                                        finish();
                                        break;
                                }
                            }
                        }, mContext);

                        break;
                    default:
                        showToast(responseInfo.result.code);

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }


    //跳转选择地址界面
    private void selectAddress() {
        Intent intent = new Intent(mContext, AddressManageActivity.class);
        intent.putExtra("FLAG", "orderIn");
        if (null == addressId) {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
        } else {
            intent.putExtra(Constants.ADDRESS_ID_SELECTED, addressId);
        }
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    UserAddressLists useAddressList = (UserAddressLists) data.getSerializableExtra("USE_RADDRESS_LIST");
                    Logger.i("useAddressList  " + useAddressList.address_id);
                    addressId = useAddressList.address_id;
                    phone = useAddressList.phone;
                    tvContent.setText(useAddressList.contacts + "    " + phone);
                    tvAddress.setText(useAddressList.province_name + useAddressList.city_name + useAddressList.county_name + useAddressList.fullAddress + useAddressList.address);
                }

                break;
        }
    }

    public void getPayTypeList() {
        if (paymentNetRequest == null) {
            paymentNetRequest = new PaymentNetRequest(mContext);
        }
        startMyDialog();
        paymentNetRequest.getPayTypeList(new RequestCallBack<PayTypeListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<PayTypeListEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        payTypeList = responseInfo.result.data;
                        adapter = new PayTypeListAdapter(payTypeList);
                        listView.setAdapter(adapter);
                        if (!isHasTotalBanalce) {
                            //钱不足，默认选中第一条
                            if (payTypeList.size() > 0) {
                                payType = payTypeList.get(0).payType;
                                if (payTypeList.get(0).isuse == 1) {
                                    selectPosition = 0;
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        stopMyDialog();
                        break;
                    default:
                        showToast(responseInfo.result.code + responseInfo.result.msg);
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    /**
     * 获取支付的结果是否支付成功
     */
    public void getPayResult() {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        Logger.i("orderId  " + orderId + "  paymentIndex " + paymentIndex + " type  " + type);
        mallNetRequest.getPayResult(paymentIndex, type, new RequestCallBack<GoodsChargeForPayResultEntity>() {
            @Override
            public void onSuccess(ResponseInfo<GoodsChargeForPayResultEntity> responseInfo) {
                GoodsChargeForPayResultEntity result = responseInfo.result;
                Logger.i("获取支付的结果 code " + result.code);
                switch (result.code) {
                    case 1:
                        switch (jumpType) {
                            case Constants.SPELL_GROUP:
                                jumpToSpellGroupResultActivity(result.groupId, result.activityId, result.orderId);
                                break;
                            case Constants.FLASHSALEPAY:
                                jumpToNOrderPaySuccessActivity(result.lebi, result.coupon, result.integral, orderIndex, result.dealTime, result.voucher, result.giveCoupon, result.giveVouncher);
                                break;
                        }
                        break;
                    case -3://系统繁忙
                        showToast(R.string.system_busy);
                        break;
                    case -14://订单失效
                        showToast("订单失效");
                        break;
                    case -4://token失效
                    case -23://设备ID验证失败
                        showToast(R.string.login_failure);
                        startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                        break;
                    case -100://10秒钟轮询接口pay_info五次
                        getPayResultTimes = 0;
                        if (getPayResultTimes < 5) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        sleep(2000);
                                        getPayResult();
                                        getPayResultTimes++;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                        break;
                    default:
                        showToast("错误码2:" + result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void jumpToNOrderPaySuccessActivity(String lebi, String coupon, String integral, String orderIndex, String dealTime, String voucher, String giveCoupon, String giveVouncher) {
        Intent intent = new Intent(mContext, NOrderPaySuccessActivity.class);
        intent.putExtra("lebi", lebi);
        intent.putExtra("voucher", voucher);
        if (!TextUtils.isEmpty(dealTime)) {
            intent.putExtra("deal_time", DateUtils.formatDate(Long.parseLong(dealTime)));
        }
        startActivity(intent);
        finish();
        Logger.i("jumpToNorderpaysuccess       " + lebi + voucher + dealTime);
    }


    public class PayTypeListAdapter extends BaseListAdapter {
        public PayTypeListAdapter(ArrayList<PayTypeListEntity.DataBean> datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_pay_type_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PayTypeListEntity.DataBean dataBean = (PayTypeListEntity.DataBean) datas.get(position);
            BitmapUtils utils = new BitmapUtils(context);
            utils.display(holder.mIvIcon, Constants.ImageUrl + dataBean.icon);
            holder.mTvClassName.setText(dataBean.className);
            holder.mTvClassSubTitle.setText(dataBean.classSubtitle);
            if (dataBean.isuse == 0) {
                holder.mRL.setBackgroundColor(context.getResources().getColor(R.color.bac_color));
            }
            if (selectPosition == -1) {
                holder.ivSelect.setImageResource(R.mipmap.merchant_big_is_noselect);
            } else if (selectPosition == 0) {
                if (position == 0) {
                    holder.ivSelect.setImageResource(R.mipmap.merchant_big_is_select);
                } else {
                    holder.ivSelect.setImageResource(R.mipmap.merchant_big_is_noselect);
                }
            } else {
                if (selectPosition == position) {
                    holder.ivSelect.setImageResource(R.mipmap.merchant_big_is_select);
                } else {
                    holder.ivSelect.setImageResource(R.mipmap.merchant_big_is_noselect);
                }
            }

            return convertView;
        }

        public class ViewHolder {
            public RelativeLayout mRL;
            public View rootView;
            public ImageView mIvIcon;
            public TextView mTvClassName;
            public TextView mTvClassSubTitle;
            public ImageView ivSelect;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.mIvIcon = (ImageView) rootView.findViewById(R.id.iv_icon);
                this.mTvClassName = (TextView) rootView.findViewById(R.id.tv_class_name);
                this.mTvClassSubTitle = (TextView) rootView.findViewById(R.id.tv_class_sub_title);
                this.mRL = (RelativeLayout) rootView.findViewById(R.id.rl);
                this.ivSelect = (ImageView) rootView.findViewById(R.id.imageView3);
            }

        }
    }
}
