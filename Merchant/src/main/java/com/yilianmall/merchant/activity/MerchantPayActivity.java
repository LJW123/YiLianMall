package com.yilianmall.merchant.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ArithUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.JsPayClass;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PayResult;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.mylibrary.pay.ThirdPayForType;
import com.yilian.mylibrary.widget.GridPasswordView;
import com.yilian.networkingmodule.entity.MerchantCashPayEntity;
import com.yilian.networkingmodule.entity.MerchantPayEntity;
import com.yilian.networkingmodule.entity.PayTypeEntity;
import com.yilian.networkingmodule.entity.WeiXinOrderEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.BaseListAdapter;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.utils.PullWXPayUtil;
import com.yilianmall.merchant.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 商家缴费
 */
public class MerchantPayActivity extends BaseActivity implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    double moreOverLeBi;//moreOverLeBi单位元
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvVipLabel;
    private TextView tvVipBalance;
    private TextView tvYearMoneyState;
    private TextView tvMerchantType;
    private TextView tvServiceTime;
    private TextView tvPayMoney;
    private TextView tvBalancePrice;
    private ImageView ivBalanceSelect;
    private NoScrollListView listView;
    private String payMerchantType;  //要成为的商家类型 0个体商家，1实体商家
    private double hasPayMoney;//共需支付金额
    private int selectedPosition = -1;
    private PayListAdapter adapter;
    private boolean moneyEnough;
    private Button btnPay;
    private Boolean whetheruseMoney = true;//是否使用奖励支付
    private String payType;
    private ArrayList<PayTypeEntity.PayData> payList;
    private double userTotalLebi;//用户奖励
    private String merchantId;
    private String orderIndex, type;
    private String paymentIndex;//微信使用的订单
    private String paymentFee;
    private PayDialog paydialog;
    private  String payOrRenew; //商家缴费的状态 缴费或续费
    private View llAgreement;
    private CheckBox checkbox;
    private TextView tvAgreement;
    private TextView tvAgreement2;
    private int getPayResultTimes = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (msg.arg1 == Constants.EXECUTE_SUCCESS) {
//                dismissInputMethod();
//                finish();
//            }
            switch (msg.what) {
                case Constants.EXECUTE_SUCCESS:
                    dismissInputMethod();
                    finish();
                    break;
                case SDK_PAY_FLAG:
                    Logger.i("msg.obj" + msg.obj.toString() + "   msg.obj:" + msg.obj);
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**`
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    Logger.i("resultStatus  " + resultStatus);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        getPayResult();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            Toast.makeText(mContext, "请安装支付宝插件", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                        sp.edit().putString("type", "").commit();
                        sp.edit().putString("lebiPay", "false").commit();
                    }
                    break;

                case SDK_CHECK_FLAG: {
                    Toast.makeText(mContext, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_pay);
        merchantId = PreferenceUtils.readStrConfig(Constants.MERCHANT_ID,mContext,"0");
        //商家缴费的状态 缴费或续费
        payOrRenew = getIntent().getStringExtra("merchantPayType");
        payMerchantType = "1";
        type = ThirdPayForType.PAY_FOR_ANNUAL_FEE;//支付的时候传递的type
        initView();
//        getPayList();
        merchantPay();
        initListener();
    }

    private void initView() {
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        Spanned agreementText = Html.fromHtml("我已阅读并接受<font color='#fe5062'><u>《益联益家商户协议》</u></color></font>");
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        tvAgreement.setText(agreementText);
        tvAgreement.setOnClickListener(this);
        tvAgreement2 = (TextView) findViewById(R.id.tv_agreement2);
        tvAgreement2.setText(Html.fromHtml("和<font color='#fe5062'><u>《商家入驻须知》</u></color></font>"));
        tvAgreement2.setOnClickListener(this);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        llAgreement = findViewById(R.id.ll_agreement);
        llAgreement.setOnClickListener(this);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.GONE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setVisibility(View.VISIBLE);
        v3Title.setText("商家平台运营费缴纳");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.INVISIBLE);

        tvVipLabel = (TextView) findViewById(R.id.tv_vip_label);
        tvVipBalance = (TextView) findViewById(R.id.tv_vip_balance);
        tvYearMoneyState = (TextView) findViewById(R.id.tv_year_money_state);
        tvMerchantType = (TextView) findViewById(R.id.tv_pay_type);
        tvServiceTime = (TextView) findViewById(R.id.tv_service_time);
        tvPayMoney = (TextView) findViewById(R.id.tv_pay_money);
        tvBalancePrice = (TextView) findViewById(R.id.tv_balance_price);
        ivBalanceSelect = (ImageView) findViewById(R.id.iv_balance_select);
        listView = (NoScrollListView) findViewById(R.id.listView);
        btnPay = (Button) findViewById(R.id.btn_pay);

        v3Back.setOnClickListener(this);
        ivBalanceSelect.setOnClickListener(this);
        btnPay.setOnClickListener(this);
    }

    //商家缴费或续费下订单
    private void merchantPay() {
        startMyDialog();
        Logger.i("MerchantIdspget  " + merchantId + "  merchantPayType   " + payOrRenew);
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .merchantPayOrder(merchantId, payMerchantType, payOrRenew, new Callback<MerchantPayEntity>() {
                    @Override
                    public void onResponse(Call<MerchantPayEntity> call, Response<MerchantPayEntity> response) {
                        MerchantPayEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        orderIndex = body.orderIndex;
                                        tvVipLabel.setText(body.levName);
                                        tvVipBalance.setText(MoneyUtil.getLeXiangBi(body.lebi));
                                        switch (body.failTime) {
                                            case "0":
                                                tvYearMoneyState.setText("未缴纳");//年费的状态
                                                break;
                                            default:
                                                tvServiceTime.setText(DateUtils.timeStampToStr(Long.parseLong(body.failTime)));
                                                tvYearMoneyState.setText("续缴运营费");//年费的状态
                                                break;
                                        }
                                        tvPayMoney.setText("需要支付: " + MoneyUtil.getLeXiangBi(body.cash) + "元");
                                        hasPayMoney = Double.parseDouble(MoneyUtil.getLeXiangBi(body.cash));
                                        tvMerchantType.setText(body.merchantType);
                                        tvServiceTime.setText(DateUtils.timeStampToStr(Long.parseLong(body.serviceTime)));
                                        userTotalLebi = Double.parseDouble(MoneyUtil.getLeXiangBi(body.lebi));
                                        switchPayMoney(userTotalLebi);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
//                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<MerchantPayEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (whetheruseMoney && moneyEnough) {
//                    return;//如果选组使用奖励支付。且奖励足够，则不能寻找充值方式
//                }
                PayTypeEntity.PayData itemAtPosition = (PayTypeEntity.PayData) parent.getItemAtPosition(position);
                if ("1".equals(itemAtPosition.isuse)) {
                    if (whetheruseMoney && moneyEnough) {
                        whetheruseMoney = !whetheruseMoney;
                        ivBalanceSelect.setImageResource(R.mipmap.library_module_cash_desk_off);
                        moreOverLeBi = hasPayMoney;
                        tvBalancePrice.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(0));
                        btnPay.setText("还需支付" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(moreOverLeBi * 100)));
                    }
                }

                payType = itemAtPosition.payType;
                if ("1".equals(payList.get(position).isuse)) {
                    payType = itemAtPosition.payType;//保证支付方式可用，才重新对payType赋值
                    selectedPosition = position;
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void switchPayMoney(double userTotalLebi) {
        //获取总的奖励 单位元 ，消费金额单位元
        if (userTotalLebi == 0 || hasPayMoney <= 0) { //奖励或者支付的金额小于0默认选择支付的按钮不可以点击
            ivBalanceSelect.setImageResource(R.mipmap.merchant_cash_desk_off);
            ivBalanceSelect.setEnabled(false);
            whetheruseMoney = false;
        }
        //奖励-需要支付金额
        Logger.i("userTotalLebi " + userTotalLebi + "  profitCash  " + hasPayMoney);
        //需要充值的金额moreOverLeBi
        moreOverLeBi = ArithUtil.sub(hasPayMoney, userTotalLebi);
        getPayList();
    }

    public void getPayList() {
//        startMyDialog();\
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getPayTypeList(new Callback<PayTypeEntity>() {
                    @Override
                    public void onResponse(Call<PayTypeEntity> call, Response<PayTypeEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        payList = response.body().data;
                                        adapter = new PayListAdapter(mContext, payList);
                                        listView.setAdapter(adapter);
                                        if (moreOverLeBi <= 0) { //奖励足够不需要充值
                                            moneyEnough = true;
                                            tvBalancePrice.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(hasPayMoney));
                                            btnPay.setText("确认支付");
                                            selectedPosition = -1;
                                            if (adapter != null) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            //奖励不足那么当前当前奖励可支付的金额就是当前用户的总奖励
                                            //需要充值的时候默认选中第一个支付方式
                                            moneyEnough = false;
                                            tvBalancePrice.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(userTotalLebi));
                                            btnPay.setText("还需支付 (" + MoneyUtil.getMoneyNoZero(moreOverLeBi) + ")");
                                            selectedPosition = 0;
                                            if (adapter != null) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<PayTypeEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.iv_balance_select) {
            switchImageSelect();
        } else if (i == R.id.btn_pay) {
            nowBuy();
        } else if (i == R.id.ll_agreement || i == R.id.tv_agreement) {
            //跳转网页商家入驻协议
            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, Constants.MERCHANT_REGISTE, false);
        } else if (i == R.id.tv_agreement2) {

            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, Constants.MERCHANT_INSTRUCTIONS, false);
        }
    }

    private void nowBuy() {
        if (!checkbox.isChecked()) {
            showToast("请阅读益联益家网用户协议");
            return;
        }
        //如果奖励充足，且使用奖励支付
        if ((moneyEnough && whetheruseMoney) || moreOverLeBi == 0) {
            pay();
        } else {
            //1：使用奖励且奖励不足  2：不使用奖励且奖励不足 3:奖励足够但不使用 这三种情况都走充值，充值金额是moreOverLeBi，充值成功后订单结算由服务器完成
            if (payList != null) {
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
                                    + "&type=2"//1商品订单  2商家入驻缴费   3扫码支付   4线下交易
                                    + "&payment_fee=" + (int) (moreOverLeBi * 100)//此处payment_fee单位是分,不能带小数点
                                    + "&order_index=" + orderIndex;
                            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, valueUrl, true);
                            break;
                    }
                }
            }
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

    private void charge() {
        startMyDialog();
        Logger.i("充值参数1：  payType:" + payType + "  type:" + type + "  未处理的moreOverLeBi:" + moreOverLeBi + "  orderIndex:" + orderIndex);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .rechargeOrder(payType, type, String.valueOf(moreOverLeBi), orderIndex, "0",new Callback<JsPayClass>() {
                    @Override
                    public void onResponse(Call<JsPayClass> call, Response<JsPayClass> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        //支付的订单编号微信使用
                                        paymentIndex = response.body().paymentIndex;
                                        //充值金额
                                        paymentFee = response.body().payment_fee;//单位元
                                        Logger.i("payTYPE   " + payType);
                                        switch (payType) {
                                            case "1"://支付宝
                                                zhifubao(response.body().orderString);
                                                break;
                                            case "2"://微信
                                                weixinOrder();
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<JsPayClass> call, Throwable t) {
                        stopMyDialog();

                    }
                });
    }

    private void zhifubao(final String orderString) {
        //支付宝的私钥放置在服务器，服务器返回相对应的字符

        /**
         * 完整的符合支付宝参数规范的订单信息服务器返回不需要客户端处理
         */
        if (TextUtils.isEmpty(orderString)) {
            return;
        }
        stopMyDialog();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((MerchantPayActivity) mContext);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(orderString, true);
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
//        奖励支付时，先检测是否有支付密码，如果有直接支付，如果没有则提示跳转密码支付设置界面
        if (PreferenceUtils.readBoolConfig(Constants.PAY_PASSWORD, mContext, false)) { //如果有支付密码
            //传递需要的数据
            paydialog = new PayDialog(mContext, orderIndex, handler);
            paydialog.show();
        } else { //没有支付密码，提示跳转设置支付密码界面
            new VersionDialog.Builder(mContext).setMessage("您还未设置支付密码,请设置支付密码后再支付！")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转设置支付密码界面使用广播
                            JumpToOtherPageUtil.getInstance().jumpToInitialPayActivity(mContext);
                            dialog.dismiss();
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

    //跳转资料提交界面
    private void jumpToMerchantCertificateActivity(String status, String merchantType) {
        Logger.i("MERCHANT_STATUS  " + status);
        switch (status) {
            case "0":
                /**
                 * 入驻去提交资料判断当前是个体还是实体的提交资料
                 */
                Intent intent = new Intent(mContext, MerchantCertificateActivity.class);
                intent.putExtra("merchantType", merchantType);
                intent.putExtra("merchantId", PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext, ""));
                startActivity(intent);
                break;
            case "1"://续费不需要提交资料
                break;
        }
        finish();
    }

    //微信支付
    private void weixinOrder() {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(Constants.WXCHARGEFORPAY, true).commit();//微信的支付类型，该笔支付是用于直接支付订单的，而不是充值的
        edit.putString(Constants.WXPAYORDER, paymentIndex).commit();//微信支付的订单ID
        edit.putString(Constants.WXPAYTYPE1, type).commit();//微信支付的类型 1 商品订单 2商家入驻缴费  3扫码支付   4线下交易
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

    //是否使用奖励支付
    private void switchImageSelect() {
        whetheruseMoney = !whetheruseMoney;
        if (whetheruseMoney) { //使用奖励
            ivBalanceSelect.setImageResource(R.mipmap.merchant_cash_desk_on);
            if (hasPayMoney > userTotalLebi) {  //奖励不足
                tvBalancePrice.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(userTotalLebi));
                //默认选中第一个支付方式
                moneyEnough = false;
                if (payList.size() > 0) {
                    if ("1".equals(payList.get(0).isuse)) {
                        payType = String.valueOf(payList.get(0).payType);//支付宝
                        selectedPosition = 0;
                        adapter.notifyDataSetChanged();
                    }
                }
                //奖励不足，需要充值的金额
                moreOverLeBi = ArithUtil.sub(hasPayMoney, userTotalLebi);
                btnPay.setText("还需支付 (" + MoneyUtil.getMoneyNoZero(moreOverLeBi) + ")");
            } else {
                payType = "-1";//奖励充足使用奖励
                tvBalancePrice.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(hasPayMoney));
                moneyEnough = true;
                selectedPosition = -1;
                adapter.notifyDataSetChanged();
                btnPay.setText("确认支付");
            }
        } else {
            //不使用奖励支付,默认选中第一种支付方式
            //充值的金额等于商品的金额
            moreOverLeBi = hasPayMoney;
            if (payList.size() > 0) {
                if ("1".equals(payList.get(0).isuse)) {
                    payType = String.valueOf(payList.get(0).payType);
                    selectedPosition = 0;
                    adapter.notifyDataSetChanged();
                }
            }
            tvBalancePrice.setText("奖励支付: " + MoneyUtil.getMoneyNoZero(0));
            ivBalanceSelect.setImageResource(R.mipmap.merchant_cash_desk_off);
            btnPay.setText("还需支付 (" + MoneyUtil.getMoneyNoZero(moreOverLeBi) + ")");
        }
    }

    public void getPayResult() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getPayResult(orderIndex, type, new Callback<MerchantCashPayEntity>() {
                    @Override
                    public void onResponse(Call<MerchantCashPayEntity> call, Response<MerchantCashPayEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            stopMyDialog();
                            switch (response.body().code) {
                                case 1:
                                    showToast("支付成功");
                                    dismissInputMethod();
                                    if (paydialog != null) {
                                        paydialog.dismissJP();
                                    }
                                    //刷新个人页面标识
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                    //刷新主页面标识
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true, mContext);
                                    //刷新商家中心标识
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, true, mContext);

                                    //支付成功跳转提交资料界面

                                    //支付成功之后存下当前的在支付商家类型
//                                    todo 2018年07月08日23:05:22 支付实体类修改后报错 等待修复 2018年07月09日10:13:05已修复
                                    sp.edit().putString(Constants.MERCHANT_TYPE, response.body().merchantType).commit();

                                    sp.edit().putString(Constants.MERCHANT_STATUS, response.body().status).commit();
                                    sp.edit().putString(Constants.MERCHANT_ID, response.body().merchantId).commit();
                                    jumpToMerchantCertificateActivity(response.body().status, response.body().merchantType);
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
                                        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(2000);
                                                    getPayResult();
                                                    getPayResultTimes++;
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantCashPayEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    public class PayDialog extends Dialog {
        private final Handler handler;
        private final String orderIndexs;
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        private MerchantPayActivity activity;

        public PayDialog(Context context, String orderIndexs, Handler handler) {
            super(context, R.style.library_module_GiftDialog);
            this.activity = (MerchantPayActivity) context;
            this.handler = handler;
            this.orderIndexs = orderIndexs;
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

                    sendMerchantPay(psw);
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
         * 商家奖励支付
         *
         * @param
         */
        private void sendMerchantPay(String psw) {
            startMyDialog();
            String password = CommonUtils.getMD5Str(psw).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
            Logger.i("diviceIndex  " + RequestOftenKey.getDeviceIndex(mContext) + " password  " + password);
            RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                    .getMerchantCashPay(orderIndexs, password, new Callback<MerchantCashPayEntity>() {
                        @Override
                        public void onResponse(Call<MerchantCashPayEntity> call, Response<MerchantCashPayEntity> response) {
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                if (CommonUtils.serivceReturnCode(activity, response.body().code, response.body().msg)) {
                                    stopMyDialog();
                                    switch (response.body().code) {
                                        case 1:
                                            dismissJP();
                                            paydialog.dismiss();
                                            dismissInputMethod();
                                            showToast("支付成功");
                                            //刷新个人页面标识
                                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                            //刷新主页面标识
                                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true, mContext);
                                            //刷新商家中心标识
                                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, true, mContext);
                                            //支付成功之后存下当前的在支付商家类型 返回的merchantType为 0个体  1实体
                                            sp.edit().putString(Constants.MERCHANT_TYPE, response.body().merchantType).commit();
                                            sp.edit().putString(Constants.MERCHANT_STATUS, response.body().status).commit();
                                            sp.edit().putString(Constants.MERCHANT_ID, response.body().merchantId).commit();
                                            sp.edit().putString(Constants.MERCHANT_TYPE, response.body().merchantType).commit();
                                            jumpToMerchantCertificateActivity(response.body().status, response.body().merchantType);
                                            break;
                                        case -5:
                                            dismissJP();
                                            paydialog.dismiss();
                                            showErrorPWDDialog();
                                            break;
                                        case -3:
                                            dismissJP();
                                            paydialog.dismiss();
                                            showToast("系统繁忙，请稍后重试");
                                            break;
                                        default:
                                            dismissJP();
                                            paydialog.dismiss();
                                            break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MerchantCashPayEntity> call, Throwable t) {
                            showToast("支付失败");
                            stopMyDialog();

                        }
                    });
        }

        //软键盘消失
        public void dismissJP() {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        /**
         * 支付密码填写错误后，弹出提示框
         */
        private void showErrorPWDDialog() {
            activity.showDialog(null, "密码错误，请重新输入", null, 0, Gravity.CENTER, "重置密码", "重新输入", false, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_NEGATIVE://重新输入
                            dialog.dismiss();
                            paydialog.show();
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
            Logger.i("iamgeUrl    " + imageUrl);
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
