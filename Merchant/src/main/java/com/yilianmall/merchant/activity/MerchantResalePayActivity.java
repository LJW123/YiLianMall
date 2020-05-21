package com.yilianmall.merchant.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.ArithUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JsPayClass;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.PullAliPayUtil;
import com.yilian.mylibrary.PullAliPayUtilSuccessListener;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.mylibrary.widget.GridPasswordView;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.MerchantCashPayEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.PayTypeEntity;
import com.yilian.networkingmodule.entity.RetailPayEntity;
import com.yilian.networkingmodule.entity.WeiXinOrderEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.BaseListAdapter;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.utils.NumberFormat;
import com.yilianmall.merchant.utils.PullWXPayUtil;
import com.yilianmall.merchant.widget.NoScrollListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 商超的支付
 */
public class MerchantResalePayActivity extends BaseActivity implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private Button btnSurePay;
    private LinearLayout llPay;
    private TextView tvBuyPrice;
    private TextView tvDeduction;
    private TextView tvXianjinquan;
    private TextView tvLinggouquan;
    private TextView tvGouwuquan;
    private LinearLayout llGouwuquan;
    private TextView tvGivingPrice;
    private TextView tvCouponNotEnough;
    private TextView tvHowToGetLequan;
    private TextView tvUsableMoney;
    private ImageView btnWhetherUseMoney;
    private NoScrollListView listView;
    private ScrollView scrollview;
    private String phone, remark, isCash, payType, type;
    private PayListAdapter adapter;
    private int selectedPosition = -1;
    private boolean whetheruseMoney = true;//默认使用奖励
    private ArrayList<PayTypeEntity.PayData> payList;
    private double totalCash, profitCash;
    private double moreOverLeBi;//moreOverLeBi需要充值金额
    private double userTotalLebi;
    private boolean moneyEnough;
    private PayDialog paydialog;
    private String paymentFee;
    private String paymentIndex;
    private String orderIndex;
    private String fromType; //从哪里来的标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_resale_pay);
        //消费金额，单位元
        if (TextUtils.isEmpty(getIntent().getStringExtra("totalCash"))) {
            totalCash = 0f;
        } else {
            totalCash = Double.parseDouble(getIntent().getStringExtra("totalCash"));
        }
        //让利金额，单位元
        profitCash = Double.parseDouble(getIntent().getStringExtra("profitCash"));
        phone = getIntent().getStringExtra("phone");
        remark = getIntent().getStringExtra("remark");
        if (TextUtils.isEmpty(remark)) {
            remark = "";
        }
        type = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(type)) {
            type = "19";//商超的支付类型type
        }
        fromType = getIntent().getStringExtra("from_type");
        orderIndex = getIntent().getStringExtra("order_index");
        initView();
        initData();
        getPayList();
        initListener();
    }

    private void initData() {
        /**
         * 获取当前的奖励
         */
        startMyDialog(false);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMyBalance(new Callback<MyBalanceEntity2>() {
                    @Override
                    public void onResponse(Call<MyBalanceEntity2> call, Response<MyBalanceEntity2> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        userTotalLebi = Double.parseDouble(MoneyUtil.getLeXiangBiNoZero(response.body().lebi));
                                        initPayType();
                                        break;
                                }
                            }
                            stopMyDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyBalanceEntity2> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    /**
     * 初始化支付方式
     */
    private void initPayType() {
        //获取总的奖励 单位元 ，消费金额单位元
        if (userTotalLebi == 0 || profitCash <= 0) {//奖励或者支付的金额小于0默认选择支付的按钮不可以点击
            btnWhetherUseMoney.setImageResource(R.mipmap.merchant_cash_desk_off);
            btnWhetherUseMoney.setEnabled(false);
            whetheruseMoney = false;
        }
        //奖励-需要支付金额
        Logger.i("userTotalLebi " + userTotalLebi + "  profitCash  " + profitCash);
        //需要充值的金额moreOverLeBi
        moreOverLeBi = ArithUtil.sub(profitCash, userTotalLebi);
        if (moreOverLeBi <= 0) { //奖励足够不需要充值
            moneyEnough = true;
            tvUsableMoney.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(profitCash));
            btnSurePay.setText("确认支付");
            selectedPosition = -1;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } else {
            //奖励不足那么当前当前奖励可支付的金额就是当前用户的总奖励
            //需要充值的时候默认选中第一个支付方式
            moneyEnough = false;
            tvUsableMoney.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(userTotalLebi));
            btnSurePay.setText("还需支付 (" + MoneyUtil.getMoneyNoZero(moreOverLeBi) + ")");
            selectedPosition = 0;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("收银台");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        btnSurePay = (Button) findViewById(R.id.btn_surePay);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        tvBuyPrice = (TextView) findViewById(R.id.tv_buy_price);
        //支付金额，当前消费额
        tvBuyPrice.setText(MoneyUtil.getMoneyNoZero(profitCash));
        tvDeduction = (TextView) findViewById(R.id.tv_deduction);
        tvXianjinquan = (TextView) findViewById(R.id.tv_xianjinquan);
        tvLinggouquan = (TextView) findViewById(R.id.tv_linggouquan);
        tvGouwuquan = (TextView) findViewById(R.id.tv_gouwuquan);
        llGouwuquan = (LinearLayout) findViewById(R.id.ll_gouwuquan);
        tvGivingPrice = (TextView) findViewById(R.id.tv_giving_price);
        tvCouponNotEnough = (TextView) findViewById(R.id.tv_coupon_not_enough);
        tvHowToGetLequan = (TextView) findViewById(R.id.tv_how_to_get_lequan);
        tvUsableMoney = (TextView) findViewById(R.id.tv_usable_money);
        btnWhetherUseMoney = (ImageView) findViewById(R.id.btn_whether_use_money);
        listView = (NoScrollListView) findViewById(R.id.lv_pay_type);
        scrollview = (ScrollView) findViewById(R.id.scrollview);

        v3Back.setOnClickListener(this);
        btnWhetherUseMoney.setOnClickListener(this);
        btnSurePay.setOnClickListener(this);
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (whetheruseMoney && moneyEnough) {
//                    return;//如果选组使用奖励支付。且奖励足够，则不能选择充值方式
//                }

                PayTypeEntity.PayData itemAtPosition = (PayTypeEntity.PayData) parent.getItemAtPosition(position);
                if ("1".equals(itemAtPosition.isuse)) {
                    if (whetheruseMoney && moneyEnough) {
                        whetheruseMoney = !whetheruseMoney;
                        btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_off);
                        moreOverLeBi = profitCash;
                        tvUsableMoney.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(0));
                        btnSurePay.setText("还需支付" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(moreOverLeBi * 100)));
                    }
                }

                payType = itemAtPosition.payType;
                if ("1".equals(payList.get(position).isuse)) {
                    selectedPosition = position;
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.btn_surePay) {
            nowBuy();
        } else if (i == R.id.btn_whether_use_money) {
            switchImageSelect();
        }
    }

    private void nowBuy() {
        //先获取订单号
        //如果奖励充足，且使用奖励支付
        if (whetheruseMoney) {
            isCash = "1";//使用奖励
        } else {
            isCash = "0";//不使用奖励
        }
        Logger.i("moneyEnough  " + moneyEnough + "  whetjerUserMoney  " + whetheruseMoney);
        if ((moneyEnough && whetheruseMoney) || moreOverLeBi == 0) {
            pay();
        } else {
            getOrderIndex(); //获取第三方支付的订单号
        }
    }

    private void charge() {
        Logger.i("充值参数1：  payType:" + payType + "  type:" + type + "  未处理的moreOverLeBi:" + moreOverLeBi + "  orderIndex:" + orderIndex);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .rechargeOrder(payType, type, String.valueOf(moreOverLeBi), orderIndex,"0", new Callback<JsPayClass>() {
                    @Override
                    public void onResponse(Call<JsPayClass> call, Response<JsPayClass> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        //支付的订单编号微信使用
                                        paymentIndex = response.body().paymentIndex;
                                        //充值金额
                                        //单位元
                                        paymentFee = response.body().payment_fee;
                                        Logger.i("payTYPE   " + payType);
                                        switch (payType) {
                                            case "1"://支付宝
                                                PullAliPayUtil.zhifubao(response.body(), mContext, new PullAliPayUtilSuccessListener() {
                                                    @Override
                                                    public void pullAliPaySuccessListener() {
                                                        getPayResult();
                                                    }
                                                });
                                                break;
                                            case "2"://微信
                                                weixinOrder();
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsPayClass> call, Throwable t) {
                        Logger.e(getClass().getSimpleName() + t);
                    }
                });
    }


    //微信支付
    private void weixinOrder() {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(Constants.WXCHARGEFORPAY, true).commit();//微信的支付类型，该笔支付是用于直接支付订单的，而不是充值的
        edit.putString(Constants.WXPAYORDER, paymentIndex).commit();//微信支付的订单ID
        edit.putString(Constants.WXPAYTYPE1, type).commit();//微信支付的类型 1 商品订单 2商家入驻缴费  3扫码支付   4线下交易  6商超支付
        String orderNumber = "1";
        edit.putString(Constants.WXPAYORDERNUMBER, String.valueOf(orderNumber)).commit();//该笔微信支付的订单数量
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getWeiXinOrder(paymentFee, paymentIndex, new Callback<WeiXinOrderEntity>() {
                    @Override
                    public void onResponse(Call<WeiXinOrderEntity> call, Response<WeiXinOrderEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        PullWXPayUtil.pullWxPay(mContext, response.body());
                                        stopMyDialog();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeiXinOrderEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    private void pay() {
//        奖励支付时，先检测是否有支付密码，如果有直接支付，如果没有则提示跳转密码支付设置界面
        if (PreferenceUtils.readBoolConfig(com.yilian.mylibrary.Constants.PAY_PASSWORD, mContext, false)) { //如果有支付密码
            //传递需要的数据
            paydialog = new PayDialog(MerchantResalePayActivity.this);
            paydialog.show();
        } else { //没有支付密码，提示跳转设置支付密码界面
            new VersionDialog.Builder(mContext).setMessage("您还未设置支付密码,请设置支付密码后再支付！")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转设置支付密码界面使用广播
                            dialog.dismiss();
                            JumpToOtherPageUtil.getInstance().jumpToInitialPayActivity(mContext);
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        }
    }

    public void getPayList() {
        startMyDialog(false);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getPayTypeList(new Callback<PayTypeEntity>() {
                    @Override
                    public void onResponse(Call<PayTypeEntity> call, Response<PayTypeEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (adapter == null) {
                                payList = response.body().data;
                                if (null != payList && payList.size() > 0) {
                                    adapter = new PayListAdapter(mContext, payList);
                                    listView.setAdapter(adapter);
                                } else {
                                    showToast("获取支付列表异常");
                                }
                            }
                            stopMyDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<PayTypeEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    //是否使用奖励支付
    private void switchImageSelect() {
        whetheruseMoney = !whetheruseMoney;
        if (whetheruseMoney) { //使用奖励
            btnWhetherUseMoney.setImageResource(R.mipmap.merchant_cash_desk_on);
            if (profitCash > userTotalLebi) {  //奖励不足
                tvUsableMoney.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(userTotalLebi));
                //默认选中第一个支付方式
                moneyEnough = false;
                if (null != payList && payList.size() > 0) {
                    payType = String.valueOf(payList.get(0).payType);//支付宝
                    if ("1".equals(payList.get(0).isuse)) {
                        selectedPosition = 0;
                        adapter.notifyDataSetChanged();
                    }
                }
                //奖励不足，需要充值的金额
                moreOverLeBi = ArithUtil.sub(profitCash, userTotalLebi);
                btnSurePay.setText("还需支付 (" + MoneyUtil.getMoneyNoZero(moreOverLeBi) + ")");
            } else {
                payType = "-1";//奖励充足使用奖励
                tvUsableMoney.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(profitCash));
                moneyEnough = true;
                selectedPosition = -1;
                adapter.notifyDataSetChanged();
                btnSurePay.setText("确认支付");
            }
        } else {
            //不使用奖励支付,默认选中第一种支付方式
            //充值的金额等于商品的金额
            moreOverLeBi = profitCash;
            if (null != payList && payList.size() > 0) {
                payType = String.valueOf(payList.get(0).payType);
                if ("1".equals(payList.get(0).isuse)) {
                    selectedPosition = 0;
                    adapter.notifyDataSetChanged();
                }
            }
            tvUsableMoney.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(0));
            btnWhetherUseMoney.setImageResource(R.mipmap.merchant_cash_desk_off);
            btnSurePay.setText("还需支付 (" + MoneyUtil.getMoneyNoZero(moreOverLeBi) + ")");
        }
    }

    /**
     * 第三方的充值订单
     */
    public void getOrderIndex() {
        if ("MerchantShopActivity".equals(fromType)) { //如果是商超购物车过来是直接下过订单的直接走recharge
            if (payList != null) {
                if ("1".equals(payList.get(selectedPosition).isuse)) {
                    switch (payList.get(selectedPosition).payType) { //1支付宝 2微信 4网银
                        case "1": //支付宝
                            jumpToZhiFuBao();
                            break;
                        case "2": //微信
                            jumpToWeiXin();
                            break;
                        default:
                            //广播跳转界面
                            String valueUrl = Ip.getBaseURL(mContext) + payList.get(selectedPosition).content + "&token=" + RequestOftenKey.getToken(mContext)
                                    + "&device_index=" + RequestOftenKey.getDeviceIndex(mContext)
                                    + "&pay_type=" + payList.get(selectedPosition).payType
                                    + "&type=" + type //1商品订单  2商家入驻缴费   3扫码支付   4线下交易  19商超支付 8商超购物车
                                    + "&payment_fee=" + NumberFormat.doubleConvertInt(moreOverLeBi * 100) //此处payment_fee单位是分,不能带小数点 四舍五入
                                    + "&order_index=" + orderIndex;
                            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext,valueUrl,true);
                            break;
                    }
                }
            }
        } else {
            startMyDialog();
            /**
             * 请求订单orderIndex时传递金额是订单的总额
             * DecimalUtil.convertDoubleToString 方法防止double相乘之后变成科学数据法显示的数据
             */
            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .getRetailPayOrderIndex(DecimalUtil.convertDoubleToString(totalCash, 100), DecimalUtil.convertDoubleToString(profitCash, 100), isCash, phone, remark, new Callback<RetailPayEntity>() {
                        @Override
                        public void onResponse(Call<RetailPayEntity> call, Response<RetailPayEntity> response) {
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                    switch (response.body().code) {
                                        case 1:
                                            orderIndex = response.body().orderIndex;
                                            Logger.i("服务器返回orderIndex    " + orderIndex);
                                            if (payList != null) {
                                                //1：使用奖励且奖励不足  2：不使用奖励且奖励不足  这三种情况都走充值，充值金额是moreOverLeBi，充值成功后订单结算由服务器完成
                                                if ("1".equals(payList.get(selectedPosition).isuse)) {
                                                    switch (payList.get(selectedPosition).payType) { //1支付宝 2微信 3微信公共账号 4网银
                                                        case "1": //支付宝
                                                            jumpToZhiFuBao();
                                                            break;
                                                        case "2": //微信
                                                            jumpToWeiXin();
                                                            break;
                                                        default:
                                                            //广播跳转界面
                                                            String valueUrl = Ip.getBaseURL(mContext) + payList.get(selectedPosition).content + "&token=" + RequestOftenKey.getToken(mContext)
                                                                    + "&device_index=" + RequestOftenKey.getDeviceIndex(mContext)
                                                                    + "&pay_type=" + payList.get(selectedPosition).payType
                                                                    + "&type=" + type //1商品订单  2商家入驻缴费   3扫码支付   4线下交易  19商超支付 8商超购物车
                                                                    + "&payment_fee=" + NumberFormat.doubleConvertInt(moreOverLeBi * 100) //此处payment_fee单位是分,不能带小数点 四舍五入
                                                                    + "&order_index=" + orderIndex;
                                                            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext,valueUrl,true);
                                                            break;
                                                    }
                                                }
                                            }
                                            break;
                                    }
                                }
                                stopMyDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<RetailPayEntity> call, Throwable t) {
                            stopMyDialog();
                        }
                    });
        }
    }


    /**
     * 支付宝支付
     */
    private void jumpToZhiFuBao() {
        payType = "1";
        sp.edit().putString("lebiPay", "true").commit();
        charge();
    }

    /**
     * 微信支付
     */
    private void jumpToWeiXin() {
        payType = "2";
        sp.edit().putString("lebiPay", "true").commit();
        charge();
    }

    private int getPayResultTimes = 0;

    /**
     * 支付宝支付成功验证
     */
    public void getPayResult() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getPayResult(orderIndex, type, new Callback<MerchantCashPayEntity>() {
                    @Override
                    public void onResponse(Call<MerchantCashPayEntity> call, Response<MerchantCashPayEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            switch (response.body().code) {
                                case 1:
                                    showToast("支付成功");
                                    //刷新个人页面标识
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                    //刷新主页面标识
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true, mContext);
                                    //刷新刷家中心标识
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, true, mContext);

                                    //支付成功跳转提交资料界面

                                    //支付成功之后存下当前的在支付商家类型
//                                    todo 2018年07月08日23:05:22 支付实体类修改后报错 等待修复 2018年07月09日10:12:55 已修复
                                    sp.edit().putString(Constants.MERCHANT_TYPE, response.body().merchantType).commit();

                                    sp.edit().putString(Constants.MERCHANT_STATUS, response.body().status).commit();
                                    sp.edit().putString(Constants.MERCHANT_ID, response.body().merchantId).commit();
                                    finish();
                                    AppManager.getInstance().killActivity(MerchantResaleActivity.class);
                                    break;
                                case -3://系统繁忙
                                    showToast(R.string.merchant_system_busy);
                                    break;
                                case -14://订单失效
                                    showToast("订单失效");
                                    break;
                                case -4://token失效
                                case -23://设备ID验证失败
                                    showToast(R.string.merchant_login_failure);
//                                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                                    break;
                                case -100://10秒钟轮询接口pay_info五次
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
                            }
                            stopMyDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantCashPayEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });

    }

    public class PayDialog extends Dialog {

        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        private MerchantResalePayActivity activity;

        public PayDialog(Context context) {
            super(context, R.style.library_module_GiftDialog);
            this.activity = (MerchantResalePayActivity) context;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.merchant_dialog_suregift_pwd);

            initView();
            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {
                }

                @Override
                public void onMaxLength(String psw) {

                    sendCashPay(psw);
                }
            });

            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                           //跳转设置支付密码界面使用广播
                    JumpToOtherPageUtil.getInstance().jumpToInitialPayActivity(mContext);
                    paydialog.dismiss();
                }
            });


            Window dialogWindow = getWindow();
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }

        /**
         * 奖励支付
         *
         * @param psw
         */
        private void sendCashPay(String psw) {
            Logger.i("psw   " + psw);

            startMyDialog();
            String password = CommonUtils.getMD5Str(psw).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
            Logger.i("OkHttp,奖励支付   passwrod   " + password + " \n totalCash  " + profitCash + "  \n profitCash  " + profitCash + "  '\nphone  " + phone + "  \nremark   " + remark);

            if ("MerchantShopActivity".equals(fromType)) {
                //商超零售现金付款
                RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                        .merchantCashPay(password, orderIndex, new Callback<HttpResultBean>() {
                            @Override
                            public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                                stopMyDialog();//放在finish之前，防止内存泄漏
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                    HttpResultBean bean = response.body();
                                    if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                        switch (bean.code) {
                                            case 1:
                                                showToast("支付成功");
                                                //刷新界面的标识
                                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                                //刷新商家中心标识
                                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, true, mContext);
                                                paydialog.dismiss();
                                                finish();
                                                AppManager.getInstance().killActivity(MerchantResaleActivity.class);
                                                break;
                                            case -5:
                                                showErrorPWDDialog();
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<HttpResultBean> call, Throwable t) {
                                stopMyDialog();
                                showToast("奖励支付失败");
                            }
                        });
            } else {
                RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                        .retailCashPay(password, DecimalUtil.convertDoubleToString(totalCash, 100), DecimalUtil.convertDoubleToString(profitCash, 100), phone, remark, new Callback<BaseEntity>() {
                            @Override
                            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                                stopMyDialog();//放在finish之前，防止内存泄漏
                                Logger.i("sendCashPay onResponse ");
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                    if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                        switch (response.body().code) {
                                            case 1:
                                                showToast("支付成功");
                                                //刷新界面的标识
                                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                                paydialog.dismiss();
                                                finish();
                                                AppManager.getInstance().killActivity(MerchantResaleActivity.class);
                                                break;
                                            case -5:
                                                showErrorPWDDialog();
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseEntity> call, Throwable t) {
                                stopMyDialog();
                                Logger.e("sendCashPay onFailure " + t);
                                showToast("奖励支付失败");
                            }
                        });
            }
        }


        /**
         * 支付密码填写错误后，弹出提示框
         */
        private void showErrorPWDDialog() {
            activity.showDialog(null, "密码错误，请重新输入", null, 0, Gravity.CENTER, "重置密码", "重新输入", false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_NEGATIVE://重新输入
                            dialog.dismiss();
                            paydialog.show();
                            //dialog弹出时弹出软键盘
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            paydialog.pwdView.clearPassword();
                            break;
                        case Dialog.BUTTON_POSITIVE://密码重置 跳转支付密码
                            JumpToOtherPageUtil.getInstance().jumpToInitialPayActivity(mContext);
                            dialog.dismiss();
                            break;

                    }
                }
            }, activity);
        }

    }

    public class PayListAdapter extends BaseListAdapter<PayTypeEntity.PayData> {
        public PayListAdapter(BaseActivity mContext, ArrayList<PayTypeEntity.PayData> data) {
            super(mContext, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.merchant_item_pay_type, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PayTypeEntity.PayData dataBean = datas.get(position);
            String imageUrl = dataBean.icon;
            if (TextUtils.isEmpty(imageUrl)) {
                holder.mIvIcon.setImageResource(R.mipmap.merchant_default_jp);
            } else {
                if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
                    imageUrl = imageUrl + Constants.ImageSuffix;
                } else {
                    imageUrl = Constants.ImageUrl + imageUrl + Constants.ImageSuffix;
                }
            }
            GlideUtil.showImage(mContext, imageUrl, holder.mIvIcon);
            holder.mTvClassName.setText(dataBean.className);
            holder.mTvClassSubTitle.setText(dataBean.classSubtitle);
            if ("0".equals(dataBean.isuse)) {
                holder.mRL.setBackgroundColor(mContext.getResources().getColor(R.color.merchant_bac_color));
            }
            if (selectedPosition == -1) {
                holder.selectImg.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
            } else if (selectedPosition == 0) {
                if (position == 0) {
                    holder.selectImg.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.merchant_big_is_select));
                } else {
                    holder.selectImg.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
                }
            } else {
                if (selectedPosition == position) {
                    holder.selectImg.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.merchant_big_is_select));
                } else {
                    holder.selectImg.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
                }
            }
            return convertView;
        }

        class ViewHolder {
            public RelativeLayout mRL;
            public View rootView;
            public ImageView mIvIcon;
            public TextView mTvClassName;
            public TextView mTvClassSubTitle;
            public ImageView selectImg;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.mIvIcon = (ImageView) rootView.findViewById(R.id.iv_icon);
                this.mTvClassName = (TextView) rootView.findViewById(R.id.tv_class_name);
                this.mTvClassSubTitle = (TextView) rootView.findViewById(R.id.tv_class_sub_title);
                this.mRL = (RelativeLayout) rootView.findViewById(R.id.rl);
                this.selectImg = (ImageView) rootView.findViewById(R.id.commit_express_icon);
            }
        }
    }
}
