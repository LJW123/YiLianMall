package com.yilian.mall.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.yilian.mall.R;
import com.yilian.mall.alipay.PayResult;
import com.yilian.mall.entity.PaymentIndexEntity;
import com.yilian.mall.entity.WeiXinOrderEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.http.PaymentNetRequest;
import com.yilian.mall.http.WeiXinNetRequest;
import com.yilian.mylibrary.pay.ThirdPayForType;
import com.yilian.mall.ui.ChargeRuleDialog;
import com.yilian.mall.ui.ChargeSuccessActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.EToast;
import com.yilian.mall.widgets.MyLoading;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mall.widgets.RadioGroup;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.networkingmodule.entity.PayTypeEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/6/6.
 * 奖励充值界面
 */
public class NMemberChargeFragmentYu_eFragment extends BaseFragment {

    //支付宝
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    //微信支付
    IWXAPI api;
    /**
     * 请求充值订单
     * type 0支付宝 1微信 2微信公共账号
     * paymentFree 支付总价
     */
    String paymentIndex;
    String fee;
    private View view;
    private EditText rbElse;
    private RadioGroup rgCharge;
    private Button btn_surePay;
    private MyIncomeNetRequest request;
    private WeiXinNetRequest weiXinNetRequest;
    //1支付宝 1微信 3微信公共账号
    private String payType = "1";
    private String lebiPrice = "1000";
    private RadioButton rb_50;
    private RadioButton rb_100;
    private RadioButton rb_200;
    private RadioButton rb_500;
    private RadioButton rb_1000;
    private SharedPreferences sp;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    try {
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                        String resultStatus = payResult.getResultStatus();
                        Logger.i("resultInfo:" + resultInfo + " resultStatus:" + resultStatus);
//                        showToast("resultInfo:"+resultInfo+" resultStatus:"+resultStatus);
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (resultStatus.equals("9000")) {
                            //                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), ChargeSuccessActivity.class));
                            //刷新个人页面标识
                            sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                        } else {
                            // 判断resultStatus 为非"9000"则代表可能支付失败
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(getContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();

                            } else if (TextUtils.equals(resultStatus, "4000")) {
                                Toast.makeText(getContext(), "请安装支付宝插件", Toast.LENGTH_SHORT).show();
                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                            }
                            sp.edit().putString("type", "").commit();
                            sp.edit().putString("lebiPay", "false").commit();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "支付失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Logger.i("支付失败" + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case SDK_CHECK_FLAG: {
                    Toast.makeText(getContext(), "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    };
    private MyLoading myloading;
    private NoScrollListView mLvPayType;
    private PaymentNetRequest paymentNetRequest;
    private ArrayList<PayTypeEntity.PayData> list;
    private PayFragmentAdapter adapter;
    private int selectedPosition = 0;
    private Context context;
    private TextView tvRule;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.fragment_charge_lexiangbi, container, false);
        sp = getActivity().getSharedPreferences("UserInfor", 0);
        initView();
        initData();
//        chargeDiaplay();
        Listener();
        return view;
    }

    private void initData() {
        getPayTypeList();
    }

    private void getPayTypeList() {
        startMyDialog();
        RetrofitUtils.getInstance(context).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(context)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(context))
                .getPayTypeList(new Callback<PayTypeEntity>() {
                    @Override
                    public void onResponse(Call<PayTypeEntity> call, Response<PayTypeEntity> response) {
                        stopMyDialog();
                        PayTypeEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                            if (CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        list = body.data;
                                        adapter = new PayFragmentAdapter(getContext(), list);
                                        mLvPayType.setAdapter(adapter);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayTypeEntity> call, Throwable t) {
                        showToast("充值方式获取失败,请重试");
                        stopMyDialog();
                    }
                });
        if (paymentNetRequest == null) {
            paymentNetRequest = new PaymentNetRequest(getContext());
        }
    }

    private void initView() {
        tvRule = (TextView) view.findViewById(R.id.tv_rule);
        tvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChargeRuleDialog.class));
            }
        });
        rgCharge = (RadioGroup) view.findViewById(R.id.rg_charge);
        rbElse = (EditText) view.findViewById(R.id.rb_else);
        rbElse.setFocusable(false);
        mLvPayType = (NoScrollListView) view.findViewById(R.id.lv_pay_type);
        mLvPayType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("1".equals(list.get(position).isuse)) {
                    selectedPosition = position;
                    adapter.notifyDataSetChanged();
                }
            }
        });

        rb_50 = (RadioButton) view.findViewById(R.id.rb_50);
        rb_50.setText(getString(R.string.recharge_1) + "元");
        rb_100 = (RadioButton) view.findViewById(R.id.rb_100);
        rb_100.setText(getString(R.string.recharge_2) + "元");
        rb_200 = (RadioButton) view.findViewById(R.id.rb_200);
        rb_200.setText(getString(R.string.recharge_3) + "元");
        rb_500 = (RadioButton) view.findViewById(R.id.rb_500);
        rb_500.setText(getString(R.string.recharge_4) + "元");
        rb_1000 = (RadioButton) view.findViewById(R.id.rb_1000);
        rb_1000.setText(getString(R.string.recharge_5) + "元");

        rgCharge.check(R.id.rb_100);
        rb_100.setTextColor(getResources().getColor(R.color.white));
        rbElse.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_empty_red));

        //输入为两位小数
        rbElse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        rbElse.setText(s);
                        rbElse.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    rbElse.setText(s);
                    rbElse.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        rbElse.setText(s.subSequence(0, 1));
                        rbElse.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (rbElse.getText().toString().equals("0") | rbElse.getText().toString().equals("0.") | rbElse.getText().toString().equals("0.0") | rbElse.getText().toString().equals("0.00")) {
                    btn_surePay.setClickable(false);
                } else {
                    btn_surePay.setClickable(true);
                }
            }
        });

        rgCharge = (RadioGroup) view.findViewById(R.id.rg_charge);
        btn_surePay = (Button) view.findViewById(R.id.btn_surePay);


        btn_surePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startMyDialog();
                if (!rbElse.getText().toString().equals("")) {
                    lebiPrice = rbElse.getText().toString();
                    if (Integer.valueOf(lebiPrice) < 500) {
                        android.widget.Toast.makeText(context, "最低充值金额500", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Integer.valueOf(lebiPrice) % 10 != 0) {
                        android.widget.Toast.makeText(context, "请输入10的整数倍", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (lebiPrice == "") {
                    Toast.makeText(getContext(), "请输入金额" + lebiPrice, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (list != null) {
                    PayTypeEntity.PayData payData = list.get(selectedPosition);
                    if ("1".equals(payData.isuse)) {
                        Intent intent = null;
                        switch (payData.payType) {
                            case "1"://支付宝
                                jumpToZhiFuBao();
                                break;
                            case "2"://微信
                                jumpToWeiXin();
                                break;
                            case "4"://银联或财付通支付（跳转到网页支付）
                                intent = new Intent(getContext(), WebViewActivity.class);
                                String rechargeUrl = Ip.getBaseURL(context) + payData.content + "&token=" + RequestOftenKey.getToken(getContext())
                                        + "&device_index=" + RequestOftenKey.getDeviceIndex(getContext())
                                        + "&pay_type=" + payData.payType
                                        + "&type=5"
                                        + "&payment_fee=" + (int) (Float.valueOf(lebiPrice) * 100) //银联支付的单位是分，并且必须是int类型的，不识别小数点
                                        + "&order_index=0";
                                Logger.i("充值Url:" + rechargeUrl);
                                intent.putExtra("url", rechargeUrl);//此处money单位是分
                                intent.putExtra("isRecharge", true);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    } else {
                        Toast.makeText(getContext(), "该方式暂不可用,请选择其他方式,谢谢!", Toast.LENGTH_SHORT).show();
                        stopMyDialog();
                    }
                } else {
                    Toast.makeText(getContext(), "支付方式获取失败，请刷新重试", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getContext(),"lebiPrice"+lebiPrice,Toast.LENGTH_SHORT).show();
                sp.edit().putString("price", lebiPrice).commit();
                sp.edit().putString("payType", payType).commit();
                sp.edit().putString("chooseFrom", "lexiangbi").commit();
            }
        });

        rbElse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCancelEdit();
                rb_50.setChecked(false);
                rb_100.setChecked(false);
                rb_200.setChecked(false);
                rb_500.setChecked(false);
                rb_1000.setChecked(false);
                showInputMethod();
                rbElse.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_new_solid_red_2));
                lebiPrice = "";
            }
        });
    }

    private void Listener() {
        rgCharge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_50:
                        rb_50.setTextColor(getResources().getColor(R.color.white));
                        rb_100.setTextColor(getResources().getColor(R.color.color_red));
                        rb_200.setTextColor(getResources().getColor(R.color.color_red));
                        rb_500.setTextColor(getResources().getColor(R.color.color_red));
                        rb_1000.setTextColor(getResources().getColor(R.color.color_red));
                        lebiPrice = getResources().getString(R.string.recharge_1);
                        Logger.i("lebiPrice" + lebiPrice);
                        resetEdit();
                        break;
                    case R.id.rb_100:
                        rb_50.setTextColor(getResources().getColor(R.color.color_red));
                        rb_100.setTextColor(getResources().getColor(R.color.white));
                        rb_200.setTextColor(getResources().getColor(R.color.color_red));
                        rb_500.setTextColor(getResources().getColor(R.color.color_red));
                        rb_1000.setTextColor(getResources().getColor(R.color.color_red));
                        lebiPrice = getResources().getString(R.string.recharge_2);
                        Logger.i("lebiPrice" + lebiPrice);
                        resetEdit();
                        break;
                    case R.id.rb_200:
                        rb_50.setTextColor(getResources().getColor(R.color.color_red));
                        rb_100.setTextColor(getResources().getColor(R.color.color_red));
                        rb_200.setTextColor(getResources().getColor(R.color.white));
                        rb_500.setTextColor(getResources().getColor(R.color.color_red));
                        rb_1000.setTextColor(getResources().getColor(R.color.color_red));
                        lebiPrice = getResources().getString(R.string.recharge_3);
                        Logger.i("lebiPrice" + lebiPrice);
                        resetEdit();
                        break;
                    case R.id.rb_500:
                        rb_50.setTextColor(getResources().getColor(R.color.color_red));
                        rb_100.setTextColor(getResources().getColor(R.color.color_red));
                        rb_200.setTextColor(getResources().getColor(R.color.color_red));
                        rb_500.setTextColor(getResources().getColor(R.color.white));
                        rb_1000.setTextColor(getResources().getColor(R.color.color_red));
                        lebiPrice = getResources().getString(R.string.recharge_4);
                        Logger.i("lebiPrice" + lebiPrice);
                        resetEdit();
                        break;
                    case R.id.rb_1000:
                        rb_50.setTextColor(getResources().getColor(R.color.color_red));
                        rb_100.setTextColor(getResources().getColor(R.color.color_red));
                        rb_200.setTextColor(getResources().getColor(R.color.color_red));
                        rb_500.setTextColor(getResources().getColor(R.color.color_red));
                        rb_1000.setTextColor(getResources().getColor(R.color.white));
                        lebiPrice = getResources().getString(R.string.recharge_5);
                        Logger.i("lebiPrice" + lebiPrice);
                        resetEdit();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 支付宝支付
     */
    private void jumpToZhiFuBao() {
        payType = "1";
        sp.edit().putString("lebiPay", "true").commit();
        startMyDialog();
        charge();
    }

    /**
     * 微信支付
     */
    private void jumpToWeiXin() {
        payType = "2";
        sp.edit().putString("lebiPay", "true").commit();
        startMyDialog();
        charge();
    }

    /**
     * 重置editText
     */
    private void resetCancelEdit() {
        rbElse.setText("");
        rbElse.setHint("");
        rbElse.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_empty_red));
        rbElse.setFocusable(true);
        rbElse.setFocusableInTouchMode(true);
        rbElse.requestFocus();
        rb_50.setTextColor(getResources().getColor(R.color.color_red));
        rb_100.setTextColor(getResources().getColor(R.color.color_red));
        rb_200.setTextColor(getResources().getColor(R.color.color_red));
        rb_500.setTextColor(getResources().getColor(R.color.color_red));
        rb_1000.setTextColor(getResources().getColor(R.color.color_red));
    }

    /**
     * 重置editText
     */
    private void resetEdit() {
        rbElse.setText("");
        rbElse.setHint("其他金额");
        rbElse.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_empty_red));
        rbElse.setFocusable(false);
        rbElse.setFocusableInTouchMode(false);
        rbElse.clearFocus();
    }

    public void charge() {

        if (request == null) {
            request = new MyIncomeNetRequest(getContext());
        }

        Logger.i(payType + "payType" + lebiPrice + "lebiPrice");
//        RetrofitUtils.getInstance().setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(context)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(context))
//                .rechargeOrder(payType, "5", lebiPrice, "0", new Callback<JsPayClass>() {
//                    @Override
//                    public void onResponse(Call<JsPayClass> call, Response<JsPayClass> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsPayClass> call, Throwable t) {
//
//                    }
//                });
        request.NPaymentIndexNet(payType, ThirdPayForType.PAY_FOR_RECHARGE, lebiPrice, new RequestCallBack<PaymentIndexEntity>() {
            @Override
            public void onSuccess(ResponseInfo<PaymentIndexEntity> responseInfo) {
                Logger.i(responseInfo.result.code + "responseInfo.result.code");
                switch (responseInfo.result.code) {
                    case 1:
                        paymentIndex = responseInfo.result.paymentIndex;
                        fee = responseInfo.result.paymentFee;
                        switch (payType) {
                            case "1":
                                zhifubao(responseInfo.result.orderString);
                                break;
                            case "2":
                                weixinOrder();
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                Toast.makeText(getContext(), R.string.net_work_not_available, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void weixinOrder() {
        if (weiXinNetRequest == null) {
            weiXinNetRequest = new WeiXinNetRequest(getContext());
        }
        weiXinNetRequest.WeiXinOrder(fee, paymentIndex, new RequestCallBack<WeiXinOrderEntity>() {
            @Override
            public void onSuccess(ResponseInfo<WeiXinOrderEntity> responseInfo) {
                Logger.i(responseInfo.result.code + "responseInfo.result.code");
                api = WXAPIFactory.createWXAPI(getContext(), responseInfo.result.appId);
                api.registerApp(responseInfo.result.appId);
                PayReq req = new PayReq();
                req.appId = responseInfo.result.appId;
                req.partnerId = responseInfo.result.partnerId;
                req.prepayId = responseInfo.result.prepayId;
                req.nonceStr = responseInfo.result.nonceStr;
                req.timeStamp = responseInfo.result.timeStamp;
                req.packageValue = responseInfo.result.packageValue;
                req.sign = responseInfo.result.sign;
                req.extData = "app data"; // optional

//                showToast("appId : " + responseInfo.result.appId +
//                        "\npartnerId : " + responseInfo.result.partnerId +
//                        "\nprepayId : " + responseInfo.result.prepayId +
//                        "\nnonceStr : " + responseInfo.result.nonceStr +
//                        "\ntimeStamp : " + responseInfo.result.timeStamp +
//                        "\npackageValue : " + responseInfo.result.packageValue +
//                        "\nsign : " + responseInfo.result.sign +
//                        "\nextData : extData"
//                );
                api.sendReq(req);
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
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
         * 完整的符合支付宝参数从服务器获取
         */
        final String payInfo = orderString;
        stopMyDialog();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接，口获取支付结果
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(getActivity());
                // 调用查询接口，获取查询结果
//				boolean isExist = payTask.checkAccountIfExist();
                boolean isExist = true;
                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        stopMyDialog();
    }

    @Override
    protected void loadData() {

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
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_pay_fragment_adapter, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PayTypeEntity.PayData dataBean = list.get(position);
            BitmapUtils utils = new BitmapUtils(context);
            GlideUtil.showImageWithSuffix(context, dataBean.icon, holder.mIvIcon);
//            utils.display(holder.mIvIcon, Constants.ImageUrl + dataBean.icon, new BitmapLoadCallBack<ImageView>() {
//                @Override
//                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
//                    holder.mIvIcon.setImageBitmap(bitmap);
//                }
//
//                @Override
//                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
//                    holder.mIvIcon.setImageResource(R.mipmap.login_module_default_jp);
//                }
//            });
            holder.mTvClassName.setText(dataBean.className);
            holder.mTvClassSubTitle.setText(dataBean.classSubtitle);
            if ("0".equals(dataBean.isuse)) {
                holder.mRL.setBackgroundColor(context.getResources().getColor(R.color.bac_color));
            }
            if (selectedPosition == 0) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EToast.reset();
    }

}
