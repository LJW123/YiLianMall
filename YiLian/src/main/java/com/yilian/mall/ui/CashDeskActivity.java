package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.alipay.PayResult;
import com.yilian.mall.entity.GoodsChargeForPayResultEntity;
import com.yilian.mall.entity.MakeMallShoppingOrderEntity;
import com.yilian.mall.entity.PaymentIndexEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.http.PaymentNetRequest;
import com.yilian.mall.http.WeiXinNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.mylibrary.pay.PayFrom;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.PayOkEntity;
import com.yilian.networkingmodule.entity.PayTypeEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.mall.ui.CashDeskActivity2.PAY_FROM_TAG;

/**
 * 收银台
 * 该界面所有货币信息只是展示使用，支付时只是向服务器传递一个订单ID
 * 只有在充值时，才牵涉到货币金额的计算，也就是"moreOverLeBi"这个字段的计算
 * 该页面已废弃
 */
public class CashDeskActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    String order_total_lebi, orderCreateTime;
    String orderName;
    MallNetRequest mallNetRequest;
    String paymentIndex;
    String fee;
    float orderTotalLebi;//订单原本需要支付多少钱  单位分
    IWXAPI api;
    @ViewInject(R.id.v3Back)
    private ImageView tvBack;
    @ViewInject(R.id.v3Title)
    private TextView tv_title;
    @ViewInject(R.id.tv_buy_price)
    private TextView tv_buy_price;
    @ViewInject(R.id.btn_surePay)
    private Button btnSurePay;
    @ViewInject(R.id.tv_usable_money)
    private TextView tvUsableMoney;
    @ViewInject(R.id.btn_whether_use_money)
    private ImageView btnWhetherUseMoney;
    @ViewInject(R.id.lv_pay_type)
    private NoScrollListView nslvPayType;
    private Boolean whetheruseMoney = true;//是否使用奖励支付
    private PayFrom type;//支付来源（商品详情、购物车、商品订单、WebView、套餐支付、套餐店内消费）
    //支付订单的订单ID
    private String orderIndex;
    private ArrayList<MakeMallShoppingOrderEntity.MakeMallShopping> list;
    private int getPayResultTimes = 0;
    private GoodsChargeForPayResultEntity result;
    private String phone;
    private String addressId;//团购转配送时的type=7时，需要传入此字段
    private String totalTotalBeans;
    private PayDialog paydialog;
    private int orderNumber = 0;//订单数量
    private float userMoney;//用户奖励
    private String payType1;//1商城订单 2 商家入驻或续费 3店内支付 6套餐
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
    private float moreOverLeBi;//还需要充值多少钱 单位分
    private Boolean moneyEnough = true;//用户奖励是否足够
    private PaymentNetRequest paymentNetRequest;
    private PayFragmentAdapter payTypeadapter;
    private ArrayList<PayTypeEntity.PayData> payList;
    //1支付宝 2微信 3微信公共账号 4网银
    private String payType;
    private MyIncomeNetRequest myIncomeNetRequest;
    private int selectedPosition = -1;
    private WeiXinNetRequest weiXinNetRequest;

    private void getPayResult() {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        Logger.i("获取支付结果OrderId:" + orderIndex + " payType1:" + payType1);
        mallNetRequest.getPayResult(orderIndex, payType1, new RequestCallBack<GoodsChargeForPayResultEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<GoodsChargeForPayResultEntity> responseInfo) {
                result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                        totalTotalBeans = result.totalTotalBean;
                        jumpToNOrderPaySuccessActivity(result.lebi,result.dealTime, result.returnBean, result.subsidy);
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
                        showToast(result.msg);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jp_cash_desk);
        ViewUtils.inject(this);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        phone = sp.getString(Constants.SPKEY_PHONE, "");
    }

    private void initView() {
/**
 * 本页面用于结算的数据，需要从上个页面传递过来的有：
 order_total_lebi String类型  订单所需金额
 “payType”String类型  1乐享币直充  3商城订单 4.付款给商家支付（套餐店内消费） 5兑换中心扫码支付 6商家套餐支付
 “orderIndex”String类型  订单ID，多个订单ID使用英文逗号连接，店内消费不传
 “type” String类型 该值决定是从哪里跳转过来的，从而决定怎么处理支付金额
 */
        tv_title.setText("确认支付");
        Intent intent = getIntent();
        type = (PayFrom) intent.getSerializableExtra(PAY_FROM_TAG);
        order_total_lebi = intent.getStringExtra("order_total_lebi");
        orderTotalLebi = Integer.valueOf(order_total_lebi);
        addressId = intent.getStringExtra("addressId");
        orderCreateTime = intent.getStringExtra("orderCreateTime");
        payType1 = intent.getStringExtra("payType");//1商城订单 2 商家入驻或续费 3店内支付
        sp.edit().putString("payType1", payType1).commit();
        Spanned spanned = null;
        if (Integer.valueOf(order_total_lebi) > 0) {
            spanned = Html.fromHtml("<font color='#fe5062'><small><small>¥</small></small>"
                    + MoneyUtil.getLeXiangBi(order_total_lebi) + "</color></font>");
        } else if (Integer.valueOf(order_total_lebi) > 0) {
            spanned = Html.fromHtml("<font color='#fe5062'><small><small>¥</small></small>"
                    + MoneyUtil.getLeXiangBi(order_total_lebi) + "</color></font>");
        } else if (Integer.valueOf(order_total_lebi) <= 0) {
            spanned = Html.fromHtml("<font color='#fe5062'><small><small>¥</small></small>"
                    + MoneyUtil.getLeXiangBi(order_total_lebi) + "</color></font>");
        }

        tv_buy_price.setText(spanned);
        RequestOftenKey.getUserInfor(mContext, sp);//更新本地用户信息
        switch (type) {
            case GOODS_DETAIL:
                orderIndex = intent.getStringExtra("orderIndex");
                orderNumber = 1;
                break;
            case GOODS_SHOPPING_CART:
                //从购物车过来
                list = (ArrayList<MakeMallShoppingOrderEntity.MakeMallShopping>) intent.getSerializableExtra("list");
                orderIndex = "";
                for (MakeMallShoppingOrderEntity.MakeMallShopping data : list
                        ) {
                    orderIndex += data.orderIndex + ",";
                    Logger.i("收银台接收的orderIndex：" + data.orderIndex);
                }
                orderIndex = orderIndex.substring(0, orderIndex.length() - 1);
                orderNumber = list.size();
                break;
            case GOODS_ORDER:
                //从订单过来
                orderIndex = intent.getStringExtra("orderIndex");
                orderNumber = 1;
                break;
            default:
                break;
        }

        Logger.i("接收到的订单号：" + orderIndex);
    }

    private void initData() {
        getUserBalance();
    }

    private void initListener() {
        nslvPayType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PayTypeEntity.PayData itemAtPosition = (PayTypeEntity.PayData) parent.getItemAtPosition(position);

                if ("1".equals(itemAtPosition.isuse)) {
                    if (whetheruseMoney && moneyEnough) {
//                    return;//如果选择奖励支付，且奖励足够，则不能选择充值方式
                        whetheruseMoney = !whetheruseMoney;
                        btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_off);
                        moreOverLeBi = orderTotalLebi;//需要支付的金额 和 用户奖励 的差额   即需要另外充值的金额 (未处理的金额)
                        btnSurePay.setText("确认支付(还需支付" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(moreOverLeBi)) + ")");
                    }

                    payType = String.valueOf(itemAtPosition.payType);
                    selectedPosition = position;
                    payTypeadapter.notifyDataSetChanged();
                } else {
                    showToast("支付方式暂不可用");
                }
            }
        });
        btnWhetherUseMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payList != null && payList.size() > 0) {
                    whetheruseMoney = !whetheruseMoney;
                    if (whetheruseMoney) {//使用奖励
                        btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_on);
                        if (orderTotalLebi > userMoney) {//奖励不足
                            //默认选中支付方式列表第一个支付方式
                            if (payList.size() > 0) {
                                if ("1".equals(payList.get(0).isuse)) {
                                    payType = String.valueOf(payList.get(0).payType);
                                    selectedPosition = 0;
                                    payTypeadapter.notifyDataSetChanged();
                                }
                            }
                            moreOverLeBi = orderTotalLebi - userMoney;//需要支付的金额 和 用户奖励 的差额    即需要另外充值的金额
                            btnSurePay.setText("确认支付(还需支付" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(moreOverLeBi)) + ")");
                        } else {//奖励足够
                            selectedPosition = -1;
                            payTypeadapter.notifyDataSetChanged();
                            btnSurePay.setText("立即支付");
                        }
                    } else {//不使用奖励
                        //默认选中支付方式列表第一个支付方式
                        if (payList.size() > 0) {
                            if ("1".equals(payList.get(0).isuse)) {
                                payType = String.valueOf(payList.get(0).payType);
                                selectedPosition = 0;
                                payTypeadapter.notifyDataSetChanged();
                            }
                        }
                        btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_off);
                        moreOverLeBi = orderTotalLebi;//需要支付的金额 和 用户奖励 的差额   即需要另外充值的金额 (未处理的金额)
                        btnSurePay.setText("确认支付(还需支付" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(moreOverLeBi)) + ")");
                    }
                }

            }
        });
        btnSurePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((moneyEnough && whetheruseMoney) || moreOverLeBi == 0) { //如果奖励足够，且使用奖励支付
                    pay();
                } else { //1：使用奖励且奖励不足  2：不使用奖励且奖励不足 3：不使用奖励且奖励足够且支付乐享币不为0 这三种情况都走充值，充值金额是moreOverLeBi，充值成功后订单结算由服务器完成
                    if (payList != null) {
                        if ("1".equals(payList.get(selectedPosition).isuse)) {
                            Intent intent = null;
                            switch (payList.get(selectedPosition).payType) {
                                case "1"://支付宝
                                    jumpToZhiFuBao();
                                    break;
                                default://银联或财付通支付（跳转到网页支付）
                                    intent = new Intent(mContext, WebViewActivity.class);
                                    String valueUrl = Ip.getBaseURL(mContext) + payList.get(selectedPosition).content + "&token=" + RequestOftenKey.getToken(mContext)
                                            + "&device_index=" + RequestOftenKey.getDeviceIndex(mContext)
                                            + "&pay_type=" + payList.get(selectedPosition).payType
                                            + "&type=" + payType1//商品订单 1商家入驻缴费 2扫码支付 3线下交易 4现金充值 5 未定 6 套餐  7团购转配送
                                            + "&payment_fee=" + (int) moreOverLeBi //此处payment_fee单位是分,不能带小数点
                                            + "&order_index=" + orderIndex
                                            + "&address_id=" + addressId;
                                    Logger.i("银联URL：" + valueUrl);
                                    intent.putExtra("url", valueUrl);
                                    intent.putExtra("isRecharge", true);
                                    startActivity(intent);
                                    break;
                            }
                        } else {
                            Toast.makeText(mContext, "该方式暂不可用,请选择其他方式,谢谢!", Toast.LENGTH_SHORT).show();
                            stopMyDialog();
                        }
                    } else {
                        showToast("订单异常,请重新支付");
                    }

                }
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(null, "便宜不等人,请君三思而后行~", null, 0, Gravity.NO_GRAVITY, "去意已决", "我再想想", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                }, mContext);
            }
        });
    }

    /**
     * 获取用户奖励
     */
    private void getUserBalance() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMyBalance(new Callback<MyBalanceEntity2>() {
                    @Override
                    public void onResponse(Call<MyBalanceEntity2> call, Response<MyBalanceEntity2> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                MyBalanceEntity2 entity = response.body();
                                switch (body.code) {
                                    case 1:
                                        userMoney = NumberFormat.convertToFloat(entity.lebi, 0);
                                        setData();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<MyBalanceEntity2> call, Throwable t) {
                        showToast("获取用户奖励失败，请重试");
                        stopMyDialog();
                    }
                });
    }

    private void pay() {
        if (TextUtils.isEmpty(phone)) { //奖励支付时，先检测是否有绑定手机号(因为奖励支付需要支付密码，而支付密码的设置必须有手机号码)
            new VersionDialog.Builder(mContext)
                    .setMessage("请绑定手机号码")
                    .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext, BindPhoneActivity.class));
                            dialog.dismiss();
                        }
                    }).create().show();
            return;
        }
//        奖励支付时，先检测是否有支付密码，如果有直接支付，如果没有则提示跳转密码支付设置界面
        if (PreferenceUtils.readBoolConfig(com.yilian.mylibrary.Constants.PAY_PASSWORD, mContext, false)) { //如果有支付密码
            paydialog = new PayDialog(mContext, orderIndex, handler);
            paydialog.show();
        } else { //没有支付密码，提示跳转设置支付密码界面
            new VersionDialog.Builder(mContext).setMessage("您还未设置支付密码,请设置支付密码后再支付！")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(CashDeskActivity.this, InitialPayActivity.class));
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

    /**
     * 支付宝支付
     */
    private void jumpToZhiFuBao() {
        payType = "1";
        sp.edit().putString("lebiPay", "true").commit();
        charge();
    }

    private void setData() {
        Logger.i("userMoney:" + userMoney);
        if (userMoney == 0 || orderTotalLebi <= 0) {  //如果用户奖励为0 或者 支付金额为0，或者奖券不足，则默认不适用奖励支付，且不能选择奖励支付选项
            btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_off);
            btnWhetherUseMoney.setEnabled(false);
        }
        tvUsableMoney.setText(Html.fromHtml("奖励支付：" + "<font color=\"#333333\">" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(sp.getString("lebi", "0"))) + "</font>"));

        moreOverLeBi = orderTotalLebi - userMoney;//需要支付的金额 和 用户奖励 的差额(未处理的金额)
        if (moreOverLeBi > 0) { //如果还需要充值，即钱不够,提示还需支付多少，那么奖励支付数量就是用户奖励
//            selectedPosition=0;//如果需要充值，则需要根据该值初始化充值方式的选择
            btnSurePay.setText("确认支付(还需支付" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(moreOverLeBi)) + ")");
            moneyEnough = false;
            //默认选中支付方式列表第一个支付方式
            tvUsableMoney.setText(Html.fromHtml("奖励支付：" + "<font color=\"#333333\">" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(userMoney)) + "</font>"));
        } else {//钱够了，那么奖励支付数量就是订单金额
            moneyEnough = true;
            tvUsableMoney.setText(Html.fromHtml("奖励支付：" + "<font color=\"#333333\">" + MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(orderTotalLebi)) + "</font>"));
        }

        getPayTypeList();//获取充值方式列表
    }

    /**
     * 请求充值订单
     * payType 1支付宝 2微信
     * paymentFree 支付总价
     */
    public void charge() {

        if (myIncomeNetRequest == null) {
            myIncomeNetRequest = new MyIncomeNetRequest(mContext);
        }

        Logger.i(payType + "payType" + moreOverLeBi + "lebiPrice");
        Logger.i("充值参数1：  payType:" + payType + "  paytype1:" + payType1 + "  未处理的moreOverLeBi:" + moreOverLeBi + "  orderIndex:" + orderIndex);
        myIncomeNetRequest.NPaymentIndexNet(payType, payType1,
                MoneyUtil.getLeXiangBi(moreOverLeBi), orderIndex,
                addressId, new RequestCallBack<PaymentIndexEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        startMyDialog();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<PaymentIndexEntity> responseInfo) {
                        PaymentIndexEntity result = responseInfo.result;
                        Logger.i(result.code + "   responseInfo.result.code" + "支付订单信息：" + result.orderString + "  paytype:" + payType);
                        switch (result.code) {
                            case 1:
                                paymentIndex = result.paymentIndex;//支付订单编号
                                fee = result.paymentFee;//充值金额

                                switch (payType) {
                                    case "1":
                                        zhifubao(result.orderString);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case -23:
                                showToast(R.string.login_failure);
                                startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                                break;
                            default:
                                showToast("支付前错误码:" + result.code + result.msg);
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

    /**
     * 获取充值方式
     */
    private void getPayTypeList() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext))
                .getPayTypeList(new Callback<PayTypeEntity>() {
                    @Override
                    public void onResponse(Call<PayTypeEntity> call, Response<PayTypeEntity> response) {
                        PayTypeEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        payList = body.data;
                                        payTypeadapter = new PayFragmentAdapter(mContext, payList);
                                        nslvPayType.setAdapter(payTypeadapter);
                                        if (!moneyEnough) { //如果钱不足，则要默认选中第一条充值方式
                                            if (payList.size() > 0) {
                                                if ("1".equals(payList.get(0).isuse)) {
                                                    payType = String.valueOf(payList.get(0).payType);
                                                    selectedPosition = 0;
                                                    payTypeadapter.notifyDataSetChanged();
                                                }
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
                        showToast(R.string.net_work_not_available);
                    }
                });
    }

    /**
     * 支付宝支付
     *
     * @param orderString
     */
    public void zhifubao(String orderString) {

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
                PayTask alipay = new PayTask((CashDeskActivity) mContext);
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

    /**
     * 支付成功后跳转到支付成功界面
     */
    private void jumpToNOrderPaySuccessActivity(String lebi, String dealTime, String returnBean, String subsidy) {
        Intent intent = null;
        switch (payType1) {//1商城订单 2 商家入驻或续费（商家入驻支付页面处理） 3店内支付（网页处理） 6套餐支付
            case "1":
                intent = new Intent(mContext, NOrderPaySuccessActivity.class);
                intent.putExtra("deal_time", dealTime);
                intent.putExtra("lebi", lebi);
                intent.putExtra("returnBean", returnBean);
                intent.putExtra("subsidy", subsidy);
                break;
            default:
                break;
        }
        startActivity(intent);
        finish();
    }

    public class PayDialog extends Dialog {
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        private Context context;
        private Handler handler;

        private String orderIndexs;

        private MallNetRequest request;
        private MTNetRequest mtNetRequest;

        public PayDialog(Context context, String orderIndexs, Handler handler) {
            super(context, R.style.GiftDialog);
            this.context = context;
            this.handler = handler;
            this.orderIndexs = orderIndexs;
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
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {

                }

                @Override
                public void onMaxLength(String psw) {
                    sendGoodsRequest(pwdView.getPassWord());
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
                    context.startActivity(new Intent(context, InitialPayActivity.class));
                }
            });


            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }


        /**
         * 商品支付 密码支付
         */
        private void sendGoodsRequest(String pwd) {
            //支付密码
            final String password = CommonUtils.getMD5Str(pwd).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(context));
            if (request == null) {
                request = new MallNetRequest(context);
            }

            request.CashDeskPayGoods(orderIndexs, password, new RequestCallBack<PayOkEntity>() {
                @Override
                public void onStart() {
                    super.onStart();
                    startMyDialog();
                }

                @Override
                public void onSuccess(ResponseInfo<PayOkEntity> responseInfo) {
                    stopMyDialog();
                    PayOkEntity result = responseInfo.result;
                    Logger.i("result", new Gson().toJson(result));
                    switch (result.code) {
                        case 1:
                            Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, context);
                            jumpToNOrderPaySuccessActivity(result.lebi, result.dealTime,
                                    result.returnBean, result.subsidy);
                            //软键盘消失
                            dismissJP();
                            paydialog.dismiss();
                            finish();
                            break;
                        case -3:
                            paydialog.dismiss();
                            showToast("系统繁忙，请稍后再试");
                            break;
                        case -5:
                            pwdView.clearPassword();
                            paydialog.dismiss();
                            showErrorPWDDialog();
                            break;
                        case -13:
                            Toast.makeText(context, "奖励不足", Toast.LENGTH_SHORT).show();
                            paydialog.dismiss();
                            if (isLogin()) {
                                startActivity(new Intent(context, RechargeActivity.class));
                            } else {
                                startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                            }
                            break;
                        default:
                            showToast(result.msg);
                            break;
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                }
            });
            Logger.i(orderIndexs + "orderIds" + password + "password");

        }

        //软键盘消失
        public void dismissJP() {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        /**
         * 支付密码填写错误后，弹出提示框
         */
        private void showErrorPWDDialog() {
            CashDeskActivity.this.showDialog(null, "密码错误，请重新输入", null, 0, Gravity.CENTER, "重置密码", "重新输入", false, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_NEGATIVE://重新输入
                            dialog.dismiss();
                            break;
                        case Dialog.BUTTON_POSITIVE://密码重置
                            context.startActivity(new Intent(context, InitialPayActivity.class));
                            dialog.dismiss();
                            break;

                    }
                }
            }, context);
        }

    }

    public class PayFragmentAdapter extends android.widget.BaseAdapter {
        private final ArrayList<PayTypeEntity.PayData> list;
        private final Context context;

        private Map<Integer, Boolean> selectedMap;//保存checkbox是否被选中的状态


        public PayFragmentAdapter(Context context, ArrayList<PayTypeEntity.PayData> list) {
            this.context = context;
            this.list = list;
            selectedMap = new HashMap<Integer, Boolean>();
            initData();
        }

        public void initData() {

            for (int i = 0; i < list.size(); i++) {
                selectedMap.put(i, false);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final PayFragmentAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_pay_fragment_adapter, null);
                holder = new PayFragmentAdapter.ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (PayFragmentAdapter.ViewHolder) convertView.getTag();
            }
            PayTypeEntity.PayData dataBean = list.get(position);
            GlideUtil.showImageNoSuffix(mContext, dataBean.icon, holder.mIvIcon);
            holder.mTvClassName.setText(dataBean.className);
            holder.mTvClassSubTitle.setText(dataBean.classSubtitle);
            if ("0".equals(dataBean.isuse)) {
                holder.mRL.setBackgroundColor(context.getResources().getColor(R.color.bac_color));
            }
            if (selectedPosition == -1) {
                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
            } else if (selectedPosition == 0) {
                if (position == 0) {
                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
                } else {
                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
                }
            } else {
                if (selectedPosition == position) {
                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
                } else {
                    holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
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
