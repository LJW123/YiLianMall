package com.yilianmall.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
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
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.widget.PasswordFinished;
import com.yilian.mylibrary.widget.PayDialog;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.MerchantDiscountEntity;
import com.yilian.networkingmodule.entity.MerchantOffLineOrderEntity;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.PayTypeEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.PayTypeListAdapter;
import com.yilianmall.merchant.utils.MoneyUtil;
import com.yilianmall.merchant.utils.NumberFormat;
import com.yilianmall.merchant.widget.NoScrollListView;

import java.math.BigDecimal;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 线下现金交易
 * 支付方式逻辑
 * 初始化：用户输入金额，根据用户奖励和输入金额进行判断：
 * 若奖励=0，则奖励按钮关闭，第三方列表选中第一条
 * 若奖励>=金额，则奖励按钮打开，第三方列表不选中
 * 若奖励< 金额，则奖励按钮打开，第三方列表选中第一条
 * 用户主动选择支付方式：
 * 若第三方支付不可用，则保持初始化，点击奖励支付按钮不进行任何操作
 * 若第三方支付可用：
 * 若用户更换第三方支付方式，则支付方式列表响应条目选择事件，奖励支付按钮保持不变
 * 若用户关闭奖励支付按钮，则默认选中支付方式列表第一条
 * 若用户再次打开奖励支付按钮，则再次根据金额和奖励进行判断，同初始化
 */
public class MerchantOffLineDealActivity extends BaseActivity implements View.OnClickListener, TextWatcher, PasswordFinished {

    String payType = "-1";//支付方式，默认-1为全部使用奖励，
    ArrayList<PayTypeEntity.PayData> payTypeList = new ArrayList<>();
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private ImageView ivMerchantImg;
    private TextView tvMerchantName;
    private TextView tvMerchantDiscount;
    private TextView tvMerchantChangeCoupon;
    private Button btnCommit;
    private EditText etPhone;
    private EditText etComsumeMoney;
    private TextView tvProfit;
    private TextView tvPayment;
    private TextView tvScore;
    private boolean userExist;
    private float merchantDiscount;
    private PayDialog payDialog;
    private NoScrollListView lvPayType;
    private double availableMoney;//用户可用奖励 单位分
    private TextView tvUsableMoney;
    private ImageView btnWhetherUseMoney;
    private boolean moneyEnough;//标记商家奖励是否足够让利
    private PayTypeListAdapter adapter;
    private boolean whetheruseMoney = true;//是否使用奖励支付,默认使用
    private int checkPosition;
    private EditText etRemark;
    private TextView tvContact;
    private String merchantId;
    private float merchantBonus;
    private View llCostIncome;
    private TextView tvCostIncome;
    private ImageView btnWhetherUseIncome;
    private int isMerchant;
    private String merchantIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_off_line_deal);
        initView();
        initListener();
        getPayTypeList();
        getUserBalance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMerchantDiscount();
        initText();
    }

    private void getMerchantDiscount() {
        if (TextUtils.isEmpty(merchantId)) {
            merchantId = PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext);
        }

        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantDiscountInfo(merchantId, new Callback<MerchantDiscountEntity>() {
                    @Override
                    public void onResponse(Call<MerchantDiscountEntity> call, Response<MerchantDiscountEntity> response) {
                        stopMyDialog();
                        MerchantDiscountEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        setData(body);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantDiscountEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    /**
     * 重置文本 防止修改折扣后回到该界面 数值没有根据最新折扣发生变化
     */
    private void initText() {
        etComsumeMoney.setText("");
        tvProfit.setText("");
        tvPayment.setText("");
        tvScore.setText("");
        btnCommit.setText("请输入消费金额");
        payType = "-1";
        btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_on);
        whetheruseMoney = true;
    }

    private void setData(MerchantDiscountEntity body) {
        MerchantDiscountEntity.DataBean data = body.data;
        tvMerchantName.setText(data.merchantName);
        tvMerchantDiscount.setText(data.merchantPercent + "折");
        merchantDiscount = data.merchantPercent;
        merchantBonus = data.merchantBonus;
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(data.merchantImage), ivMerchantImg);
    }

    private void initView() {
        merchantId = getIntent().getStringExtra(Constants.MERCHANT_ID);

        llCostIncome = findViewById(R.id.ll_cost_income);
        tvCostIncome = findViewById(R.id.tv_cost_income);
        btnWhetherUseIncome = findViewById(R.id.btn_whether_use_income);
        btnWhetherUseMoney = (ImageView) findViewById(R.id.btn_whether_use_money);
        tvUsableMoney = (TextView) findViewById(R.id.tv_usable_money);
        lvPayType = (NoScrollListView) findViewById(R.id.lv_pay_type);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("线下现金交易");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("收款记录");
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);

        ivMerchantImg = (ImageView) findViewById(R.id.iv_merchant_head_img);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        tvMerchantDiscount = (TextView) findViewById(R.id.tv_merchant_discount);
        tvMerchantChangeCoupon = (TextView) findViewById(R.id.tv_merchant_change_discount);
        btnCommit = (Button) findViewById(R.id.btn_commit);

        View includePhone = findViewById(R.id.include_phone);
        ((TextView) includePhone.findViewById(R.id.tv_key)).setText("消费者注册手机号:");
        etPhone = (EditText) includePhone.findViewById(R.id.et_value);
        etPhone.setVisibility(View.VISIBLE);
        InputFilter[] filters = {new InputFilter.LengthFilter(11)};
        etPhone.setPadding(0, 0, 0, 0);
        etPhone.setFilters(filters);
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        etPhone.setHint("请输入消费者手机号");
        etPhone.addTextChangedListener(this);
//        showSoftInputFromWindow(this, etPhone);

        View includeContact = findViewById(R.id.include_contact);
        ((TextView) includeContact.findViewById(R.id.tv_key)).setText("消费者昵称:");
        tvContact = (TextView) includeContact.findViewById(R.id.tv_value);
        tvContact.setVisibility(View.VISIBLE);
        tvContact.setText("暂无昵称");

        View includeConsumeMoney = findViewById(R.id.include_consume_money);
        ((TextView) includeConsumeMoney.findViewById(R.id.tv_key)).setText("实际消费金额(元):");
        etComsumeMoney = (EditText) includeConsumeMoney.findViewById(R.id.et_value);
        etComsumeMoney.setPadding(0, 0, 0, 0);
        etComsumeMoney.setVisibility(View.VISIBLE);
        etComsumeMoney.setHint("输入金额");
        etComsumeMoney.setInputType(8194);//InputType.TYPE_NUMBER_FLAG_DECIMAL 包括数字、点和符号 十进制值是8192,8194是纯数字和点
        etComsumeMoney.addTextChangedListener(this);

        View includeConsumeProfit = findViewById(R.id.include_consume_profit);
        ((TextView) includeConsumeProfit.findViewById(R.id.tv_key)).setText(
                "消费者获得" + Constants.APP_PLATFORM_DONATE_NAME + ":");
        tvProfit = (TextView) includeConsumeProfit.findViewById(R.id.tv_value);
        tvProfit.setVisibility(View.VISIBLE);
        tvProfit.setHint("0");

        View includeConsumeRemark = findViewById(R.id.include_consume_remark);
        ((TextView) includeConsumeRemark.findViewById(R.id.tv_key)).setText("备注:");
        etRemark = (EditText) includeConsumeRemark.findViewById(R.id.et_value);
        etRemark.setVisibility(View.VISIBLE);
        etRemark.setHint("填写备注");

        View includeConsumePayment = findViewById(R.id.include_consume_payment);
        ((TextView) includeConsumePayment.findViewById(R.id.tv_key)).setText("需让利金额:");
        tvPayment = (TextView) includeConsumePayment.findViewById(R.id.tv_value);
        tvPayment.setVisibility(View.VISIBLE);
        tvPayment.setHint("0");

        View includeConsumeScore = findViewById(R.id.include_consume_score);
        ((TextView) includeConsumeScore.findViewById(R.id.tv_key)).setText("获得销售" + Constants.APP_PLATFORM_DONATE_NAME+":");
        tvScore = (TextView) includeConsumeScore.findViewById(R.id.tv_value);
        tvScore.setVisibility(View.VISIBLE);
        tvScore.setHint("0");

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvMerchantChangeCoupon.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    private void initListener() {
        btnWhetherUseMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payTypeList.size() > 0) {//如果第三方支付列表不为空
                    whetheruseMoney = !whetheruseMoney;
                    if (whetheruseMoney) {//使用奖励
                        btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_on);
                        if (moneyEnough) {//如果奖励足够
                            clearPayType();
                        } else {//如果奖励不足
                            checkFirstPayType();
                        }
                    } else {//不使用奖励
                        //默认选中支付方式列表第一个支付方式
                        checkFirstPayType();
                        btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_off);
                    }
                } else {

                }

            }
        });
        lvPayType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkPosition = position;
                PayTypeEntity.PayData itemAtPosition = (PayTypeEntity.PayData) lvPayType.getItemAtPosition(position);
                payType = itemAtPosition.payType;
                if (moneyEnough && whetheruseMoney) {//如果奖励足够 且使用奖励
                    whetheruseMoney = false;
                    btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_off);
                    itemAtPosition.isChecked = true;
                    Logger.i("付款走了这里1");
                } else {//如果奖励不足够且使用奖励，或奖励不足且不使用奖励，或奖励足够但不使用
                    Logger.i("付款走了这里2");
                    for (int i = 0; i < payTypeList.size(); i++) {//
                        if (i == position) {
                            payTypeList.get(i).isChecked = true;
                        } else {
                            payTypeList.get(i).isChecked = false;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取支付方式列表
     */
    private void getPayTypeList() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getPayTypeList(new Callback<PayTypeEntity>() {
                    @Override
                    public void onResponse(Call<PayTypeEntity> call, Response<PayTypeEntity> response) {
                        stopMyDialog();
                        PayTypeEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        ArrayList<PayTypeEntity.PayData> data = body.data;
                                        payTypeList.addAll(data);
                                        adapter = new PayTypeListAdapter(mContext, data);
                                        lvPayType.setAdapter(adapter);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayTypeEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    /**
     * 获取用户奖励
     */
    private void getUserBalance() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMyBalance(new Callback<MyBalanceEntity2>() {
                    @Override
                    public void onResponse(Call<MyBalanceEntity2> call, Response<MyBalanceEntity2> response) {
                        MyBalanceEntity2 body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        availableMoney = NumberFormat.convertToDouble(body.lebi, 0d);
                                        isMerchant = body.isMerchant;
                                        merchantIncome = body.merchantIncome;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyBalanceEntity2> call, Throwable t) {

                    }
                });
    }

    /**
     * 清空支付方式的选中状态
     */
    void clearPayType() {
        payType = "-1";
        for (int i = 0; i < payTypeList.size(); i++) {
            payTypeList.get(i).isChecked = false;
        }
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void checkFirstPayType() {
        if (payTypeList.size() > 0) {
            checkListViewItem(0);
        }
    }

    private void checkListViewItem(int index) {
        lvPayType.performItemClick(lvPayType.getChildAt(index), index, lvPayType.getItemIdAtPosition(index));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etComsumeMoney.hasFocus()) {
            DecimalUtil.keep2Decimal(s, etComsumeMoney);
            float payment = (NumberFormat.convertToFloat(s, 0f) * (10 - merchantDiscount)) / 10;//需要上缴让利额
//            //消费者获得奖券
            tvProfit.setText(String.format("%.2f", new BigDecimal(String.valueOf(payment)).multiply(new BigDecimal("0.93")).multiply(new BigDecimal("0.7"))));//消费者获得奖券
            tvPayment.setText(String.format("%.2f", payment));//需让利金额
            btnCommit.setText("确认让利" + String.format("%.2f", payment) + "元");//确认上缴按钮
            if ("0".equals(PreferenceUtils.readStrConfig(Constants.MERCHANT_TYPE, mContext, "-1"))) {
                tvScore.setText(String.format("%.2f", payment));//获得消费奖券
            } else if ("1".equals(PreferenceUtils.readStrConfig(Constants.MERCHANT_TYPE, mContext, "-1"))) {
                tvScore.setText(String.format("%.2f", new BigDecimal(String.valueOf(payment)).multiply(new BigDecimal("0.93"))
                .multiply(new BigDecimal(String.valueOf(merchantBonus)).divide(new BigDecimal("100")))));//获得消费奖券
            }
            initPayType(payment * 100);
        }
        if (etPhone.hasFocus()) {
            Logger.i("检查手机号码");
            String phone = etPhone.getText().toString().trim();
            Logger.i(phone.length() + "号码长度");
            if (phone.length() == 11) {
                Logger.i("检查手机号2");
                checkUser(phone);
                etComsumeMoney.requestFocus();
            } else {
                tvContact.setText("暂无昵称");
            }
        }

    }

    /**
     * 初始化支付方式
     *
     * @param payment 商家上缴让利额 单位分
     */
    private void initPayType(float payment) {
        if (availableMoney <= 0) {//如果用户奖励小于0，则不能使用奖励支付
            btnWhetherUseMoney.setEnabled(false);
            tvUsableMoney.setText("奖励可支付:" + MoneyUtil.getLeXiangBi(0));
            btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_off);
            checkFirstPayType();
        } else {//有可使用奖励
            btnWhetherUseMoney.setImageResource(R.mipmap.library_module_cash_desk_on);
            if (availableMoney >= payment) {//奖励足够让利
                moneyEnough = true;
                tvUsableMoney.setText("奖励可支付:" + MoneyUtil.getLeXiangBi(payment));
                clearPayType();
            } else {//奖励不足以让利
                moneyEnough = false;
                tvUsableMoney.setText("奖励可支付:" + MoneyUtil.getLeXiangBi(availableMoney));
                checkFirstPayType();
            }
        }
    }

    /**
     * 检查输入手机号是否存在
     *
     * @param phone
     */
    private void checkUser(String phone) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<CheckUserEntity>() {
            @Override
            public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                stopMyDialog();
                BaseEntity body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    switch (body.code) {
                        case -12:
                            userExist = true;
                            String userName = response.body().userName;
                            if (!TextUtils.isEmpty(userName)) {
                                tvContact.setText(userName);
                            }
                            break;
                        case 1:
                            showToast("手机号尚未注册");
                            userExist = false;
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                stopMyDialog();
                showToast(R.string.network_module_net_work_error);

            }
        });
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void passwordFinished(String psw) {
        goToDeal(psw);
    }

    private void goToDeal(String psw) {
        startMyDialog();
        String trim = etComsumeMoney.getText().toString().trim();
        float money = NumberFormat.convertToFloat(trim, 0f);//支付金额传递单位为分 用户输入金额*100
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .merchantOffLinePay(String.valueOf(money * 100),
                        etPhone.getText().toString().trim(),
                        psw,
                        etRemark.getText().toString().trim(),"0",
                        new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        stopMyDialog();
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        //刷新个人页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                        finish();
                                        showToast("交易成功");
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_right) {
            startActivity(new Intent(this, MerchantMoneyRecordActivity2.class));
        } else if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_merchant_change_discount) {
            startActivity(new Intent(this, MerchantChangeDiscountActivity.class));
        } else if (i == R.id.btn_commit) {
            commit();
        }
    }


    private void commit() {
        String phone = etPhone.getText().toString().trim();
        if (phone.equals(PreferenceUtils.readStrConfig(Constants.SPKEY_PHONE, mContext))) {
            showToast("请勿在自己店铺消费");
            return;
        }
        if (!CommonUtils.isPhoneNumer(phone)) {
            showToast("请输入正确的手机号码");
            return;
        }
        if (etComsumeMoney.length() <= 0) {
            showToast("请输入消费金额");
            return;
        }
        if (!userExist) {
            showToast("该手机号尚未注册,请重新输入");
            etPhone.requestFocus();
            return;
        }
        switch (payType) {
            case "-1"://奖励支付
                if (PreferenceUtils.readBoolConfig(Constants.PAY_PASSWORD, mContext, false)) { //如果有支付密码
                    payDialog = new PayDialog(mContext, this);
                    payDialog.show();
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
                break;
            default:
                getPayOrder(phone);
                break;
        }


    }


    /**
     * 生成预支付订单 （第三方支付时使用）
     *
     * @param phone
     */
    private void getPayOrder(String phone) {
        //                生成订单
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantOffLineDealOrder(String.valueOf(NumberFormat.convertToFloat(etComsumeMoney.getText().toString().trim(), 0f) * 100), phone, etRemark.getText().toString().trim(), new Callback<MerchantOffLineOrderEntity>() {
                    @Override
                    public void onResponse(Call<MerchantOffLineOrderEntity> call, Response<MerchantOffLineOrderEntity> response) {
                        final MerchantOffLineOrderEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        double moreOverMoney;//需要第三方支付的钱数,如果没有使用奖励，该值即为body的bound字段，如果使用奖励，则是bound-奖励
                                        if (whetheruseMoney) {
                                            moreOverMoney = body.bonus - availableMoney;
                                        } else {
                                            moreOverMoney = body.bonus;
                                        }
                                        switch (payType) {
                                            case "1":
                                            case "2":
                                                getThirdPayInfo(body, moreOverMoney);
                                                break;
                                            default://银联或财付通支付（跳转到网页支付）
                                                //广播跳转界面
                                                String valueUrl = Ip.getBaseURL(mContext) + payTypeList.get(checkPosition).content + "&token=" + RequestOftenKey.getToken(mContext)
                                                        + "&device_index=" + RequestOftenKey.getDeviceIndex(mContext)
                                                        + "&pay_type=" + payType//1支付宝 2微信 3微信公共账号 4网银
                                                        + "&type=4"//1商品订单 2商家入驻缴费 3扫码支付 4线下交易
                                                        + "&payment_fee=" + moreOverMoney//此处payment_fee单位是分 ,不能带小数点
                                                        + "&order_index=" + body.orderIndex
                                                        +"&moneys=0";
                                                JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext, valueUrl, true);
                                                break;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantOffLineOrderEntity> call, Throwable t) {

                    }
                });
    }


    /**
     * 获取第三方支付订单信息
     *
     * @param merchantOffLineOrderEntity
     * @param moreOverMoney
     */
    private void getThirdPayInfo(final MerchantOffLineOrderEntity merchantOffLineOrderEntity, double moreOverMoney) {

        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .rechargeOrder(String.valueOf(payType), com.yilian.mylibrary.pay.ThirdPayForType.PAY_FOR_OFF_LINE_PAY, MoneyUtil.getLeXiangBi(moreOverMoney),
                        merchantOffLineOrderEntity.orderIndex, "0", new Callback<JsPayClass>() {
                            @Override
                            public void onResponse(Call<JsPayClass> call, Response<JsPayClass> response) {
                                JsPayClass thirdRechargeOrderEntity = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, thirdRechargeOrderEntity)) {
                                    if (CommonUtils.serivceReturnCode(mContext, thirdRechargeOrderEntity.code, thirdRechargeOrderEntity.msg)) {
                                        switch (payType) {
                                            case "1"://支付宝
                                                PullAliPayUtil.zhifubao(thirdRechargeOrderEntity, mContext, new PullAliPayUtilSuccessListener() {
                                                    @Override
                                                    public void pullAliPaySuccessListener() {
                                                        //Success
                                                        showToast("支付成功");
                                                    }
                                                });
                                                break;
                                            default:
                                                break;

                                        }
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<JsPayClass> call, Throwable t) {

                            }
                        });
    }
}
