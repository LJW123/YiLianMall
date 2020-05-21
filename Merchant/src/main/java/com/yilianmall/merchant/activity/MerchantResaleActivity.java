package com.yilianmall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DecimalUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.MerchantData;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.MoneyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilianmall.merchant.R.id.btn_pay;
import static com.yilianmall.merchant.R.id.collection_rqcode;

/**
 * 商家零售商超
 */
public class MerchantResaleActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private EditText etPhone;
    private EditText etActualMoney;
    private EditText etProfitMoney;
    private EditText etRemark;
    private TextView tvConsumerObtainJifen;
    private TextView tvMerchantObtainJifen;
    private Button btnPay;
    private Button btnCollectionRqcode;
    private boolean userPhone = true;
    private String merchantId;
    private String merchantImage;
    private String merchantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_resale);
        merchantId = getIntent().getStringExtra("merchantId");
        initView();
        initData();
    }

    /**
     * 线请求商家数据用于下个界面的传递
     */
    private void initData() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantData(merchantId, new Callback<MerchantData>() {
                    @Override
                    public void onResponse(Call<MerchantData> call, Response<MerchantData> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        MerchantData.DataBean merchantData = response.body().data;
                                        merchantName = merchantData.merchantName;
                                        merchantImage = merchantData.merchantImage;

                                        break;
                                }
                            }
                            stopMyDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantData> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商超零售");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("交易记录");
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.GONE);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        etPhone = (EditText) findViewById(R.id.et_phone);
        SpannableString ss = new SpannableString("请输入手机号");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        etPhone.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
        etPhone.addTextChangedListener(this);
        etPhone.requestFocus();
        etActualMoney = (EditText) findViewById(R.id.et_actual_money);
        etActualMoney.addTextChangedListener(this);
        etProfitMoney = (EditText) findViewById(R.id.et_profit_money);
        etProfitMoney.addTextChangedListener(this);
        etRemark = (EditText) findViewById(R.id.et_remark);
        tvConsumerObtainJifen = (TextView) findViewById(R.id.tv_consumer_obtain_jifen);
        tvMerchantObtainJifen = (TextView) findViewById(R.id.tv_merchant_obtain_jifen);
        btnPay = (Button) findViewById(btn_pay);
        btnCollectionRqcode = (Button) findViewById(collection_rqcode);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        btnCollectionRqcode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
            //交易记录
            startActivity(new Intent(mContext, MerchantMoneyRecordActivity2.class));
        } else if (i == R.id.btn_pay) {
            checkData(R.id.btn_pay);
//            checkData(btn_pay);
        } else if (i == R.id.collection_rqcode) {
//            checkData(collection_rqcode);
            checkData(R.id.collection_rqcode);
        }
    }

    private void checkData(int btnId) {
        //只有是现金付款是才需要验证手机号，二维码扫码的时候直接获取扫码人的信息支付
        String phone = null;
        phone = etPhone.getText().toString().trim();
        if (btnId == btnPay.getId() && !CommonUtils.isPhoneNumer(phone)) {
            showToast("请输入正确的手机号");
            return;
        }
        if (btnId == btnPay.getId() && !userPhone) {
            showToast("该手机号尚未注册,请重新输入");
            return;
        }
        if (btnId == btnPay.getId() && phone.equals(PreferenceUtils.readStrConfig(Constants.SPKEY_PHONE, mContext, "0"))) {
            showToast("请勿在自己店铺消费");
            return;
        }
        String cashMoney = etActualMoney.getText().toString().trim();
        if (TextUtils.isEmpty(cashMoney)) {
            showToast("请输入消费金额");
            return;
        }
        String profitCash = etProfitMoney.getText().toString().trim();
        if (TextUtils.isEmpty(profitCash)) {
            showToast("请输入让利金额");
            return;
        }
        if (Double.parseDouble(etProfitMoney.getText().toString().trim()) >
                Double.parseDouble(etActualMoney.getText().toString().trim())) {
            showToast("让利金额不能大于消费金额");
            return;
        }
        if (btnId == btnPay.getId()) {
            //跳转商超支付界面
            if (isLogin()) {
                Intent intent = new Intent(mContext, MerchantResalePayActivity.class);
                intent.putExtra("totalCash", cashMoney);//消费金额单位是元
                intent.putExtra("profitCash", profitCash);//让利金额单位元
                intent.putExtra("phone", phone);
                intent.putExtra("remark", etRemark.getText().toString().trim());
                startActivity(intent);
            } else {
                JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(mContext);
            }

        } else if (btnId == btnCollectionRqcode.getId()) {
            if (isLogin()){
                Intent intent=new Intent(mContext,MerchantCollectionRqcodeActivity.class);
                intent.putExtra("imageUrl",merchantImage);
                intent.putExtra("merchantName",merchantName);
                intent.putExtra("totalCash", Double.parseDouble(cashMoney));//消费金额单位是元
                intent.putExtra("profitCash", Double.parseDouble(profitCash));//让利金额单位元
                intent.putExtra("remark", etRemark.getText().toString().trim());
                intent.putExtra("merchantId", merchantId);
                Logger.i("传参前   totalCash "+cashMoney+"  profitCash   "+profitCash);
                startActivity(intent);
            }else {
                JumpToOtherPageUtil.getInstance().jumpToLeFenPhoneLoginActivity(mContext);
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Logger.i("etphone HasFoucusable " + etPhone.isFocusable() + " :: " + etProfitMoney.hasFocus());
        if (etPhone.hasFocus() && etPhone.getText().length() == 11) {
            if (CommonUtils.isPhoneNumer(etPhone.getText().toString().trim())) {
                checkPhone(etPhone.getText().toString().trim());
            } else {
                showToast("请输入正确的手机号");
            }
        } else if (etProfitMoney.hasFocus()) {
            String actialMoney = etActualMoney.getText().toString().trim();
            //让利金额
            DecimalUtil.keep2Decimal(s, etProfitMoney);
            String genlisMoney = etProfitMoney.getText().toString().trim();
            if (TextUtils.isEmpty(actialMoney)) {
                showToast("请输入消费金额");
                return;
            }
            if (!TextUtils.isEmpty(genlisMoney)) {
                double obtionJifen = Double.parseDouble(genlisMoney);
                double actialObtionJifen = Double.parseDouble(actialMoney);
                if (obtionJifen > actialObtionJifen) {
                    showToast("让利金额不能大于消费金额");
                } else {
                    tvConsumerObtainJifen.setText(MoneyUtil.getMoneyNoZero(obtionJifen * 5));
                    //商家获得的销售奖券是根据是实体商家还是个体商家来决定返奖券的倍数
                    switch (PreferenceUtils.readStrConfig(Constants.MERCHANT_TYPE, mContext, "-1")) {
                        case "0"://个体
                            tvMerchantObtainJifen.setText(MoneyUtil.getMoneyNoZero(obtionJifen * 1));
                            break;
                        case "1"://实体
                            tvMerchantObtainJifen.setText(MoneyUtil.getMoneyNoZero(obtionJifen * 2));
                            break;
                    }
                }

            } else {
                tvMerchantObtainJifen.setText("");
                tvConsumerObtainJifen.setText("");
            }

        } else if (etActualMoney.hasFocus()) {
            //消费金额
            DecimalUtil.keep2Decimal(s, etActualMoney);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 验证手机号
     *
     * @param phone
     */
    private void checkPhone(String phone) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<CheckUserEntity>() {
            @Override
            public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                    switch (response.body().code) {
                        case -12:
                            userPhone = true;
                            break;
                        case 1:
                            showToast("该手机号尚未注册");
                            userPhone = false;
                            break;
                    }
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                showToast(R.string.network_module_net_work_error);
                stopMyDialog();
            }
        });

    }
}
