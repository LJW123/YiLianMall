package com.yilian.mall.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.FlashSalePayResult;
import com.yilian.mall.entity.FlashSaleTransferEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FlashSalePayActivity extends BaseActivity implements View.OnClickListener {


    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private ImageView ivFlashSaleImage;
    private View view3;
    private TextView tvFlashSaleGoodsName;
    private TextView tvFlashSaleGoodsRecommend;
    private TextView tvFlashSaleGoodsPrice;
    private TextView tvFlashSaleGoodsCount;
    private TextView textView21;
    private TextView textView22;
    private TextView textView24;
    private TextView tvFlashSaleGoodsTotalPrice;
    private TextView tvGetLingGouQuan;
    private Button btnFlashSalePay;
    private LinearLayout activityFlashSalePay;
    private String goodsId;
    private FlashSaleTransferEntity flashSaleTransferEntity;
    private PayDialog paydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sale_pay);
        initView();
        initData();
    }

    private void initData() {
        flashSaleTransferEntity = (FlashSaleTransferEntity) getIntent().getSerializableExtra("FlashSaleTransferEntity");
        goodsId = flashSaleTransferEntity.goodsId;
        GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(flashSaleTransferEntity.imageUrl), ivFlashSaleImage);
        tvFlashSaleGoodsName.setText(flashSaleTransferEntity.goodsName);
        tvFlashSaleGoodsRecommend.setText(flashSaleTransferEntity.merchantName);
        tvFlashSaleGoodsCount.setText("×1");
        tvFlashSaleGoodsPrice.setText(MoneyUtil.getLeXiangBiNoZero(flashSaleTransferEntity.price));
        tvFlashSaleGoodsTotalPrice.setText(MoneyUtil.getLeXiangBiNoZero(flashSaleTransferEntity.price));
    }


    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        ivRight2.setVisibility(View.GONE);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        ivFlashSaleImage = (ImageView) findViewById(R.id.ivFlashSaleImage);
        view3 = (View) findViewById(R.id.view3);
        tvFlashSaleGoodsName = (TextView) findViewById(R.id.tvFlashSaleGoodsName);
        tvFlashSaleGoodsRecommend = (TextView) findViewById(R.id.tvFlashSaleGoodsRecommend);
        tvFlashSaleGoodsPrice = (TextView) findViewById(R.id.tvFlashSaleGoodsPrice);
        tvFlashSaleGoodsCount = (TextView) findViewById(R.id.tvFlashSaleGoodsCount);
        textView21 = (TextView) findViewById(R.id.textView21);
        textView22 = (TextView) findViewById(R.id.textView22);
        textView24 = (TextView) findViewById(R.id.textView24);
        tvFlashSaleGoodsTotalPrice = (TextView) findViewById(R.id.tvFlashSaleGoodsTotalPrice);
        tvGetLingGouQuan = (TextView) findViewById(R.id.tv_get_ling_gou_quan);
        Spanned spanned = Html.fromHtml("<font color=\"#999999\">注:本订单不需要支付现金,只是支付相应的零购券。购物券不够?</font><font color=\"#f75a53\">去获得零购券</font>");
        tvGetLingGouQuan.setText(spanned);
        tvGetLingGouQuan.setOnClickListener(this);
        btnFlashSalePay = (Button) findViewById(R.id.btn_flash_sale_pay);
        activityFlashSalePay = (LinearLayout) findViewById(R.id.activity_flash_sale_pay);

        ivTitle.setVisibility(View.GONE);
        ivLeft1.setImageResource(R.mipmap.iv_back);
        tvTitle.setText(getResources().getString(R.string.commit_order));
        tvTitle.setVisibility(View.VISIBLE);

        ivLeft1.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnFlashSalePay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.btn_flash_sale_pay:
                pay();
                break;
            case R.id.tv_get_ling_gou_quan:
//                Intent intent = new Intent(this, JPMainActivity.class);
//                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_DI_KOU_FRAGMENT);
//                startActivity(intent);
               //点击跳转获取零购券web界面
                Intent intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("url",Constants.HowToGetVoucher);
                startActivity(intent);
                break;
        }
    }
    private String phone;
    @Override
    protected void onResume() {
        super.onResume();
        phone = sp.getString(Constants.SPKEY_PHONE, "");
    }
    private void pay() {
        if (TextUtils.isEmpty(phone)) {//支付时，先检测是否有绑定手机号(因为奖励支付需要支付密码，而支付密码的设置必须有手机号码)
            new VersionDialog.Builder(mContext)
                    .setMessage("请绑定手机号码")
                    .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mContext,BindPhoneActivity.class));
                            dialog.dismiss();
                        }
                    }).create().show();

            return;
        }
//        支付时，先检测是否有支付密码，如果有直接支付，如果没有则提示跳转密码支付设置界面
        if (PreferenceUtils.readBoolConfig(com.yilian.mylibrary.Constants.PAY_PASSWORD,mContext,false)) {//如果有支付密码
            paydialog = new PayDialog(mContext,new Handler());
            paydialog.show();
        } else {//没有支付密码，提示跳转设置支付密码界面
            new VersionDialog.Builder(mContext)
                    .setMessage("您还未设置支付密码,请设置支付密码后再支付！")
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
                    })
                    .create().show();
        }
    }

    public class PayDialog extends Dialog {
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        private Context context;
        private Handler handler;


        private MallNetRequest request;
        private MTNetRequest mtNetRequest;

        public PayDialog(Context context, Handler handler) {
            super(context, R.style.GiftDialog);
            this.context = context;
            this.handler = handler;
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
         * 商品支付
         *
         * @param pwd
         */
        private void sendGoodsRequest(String pwd) {
            //支付密码
            final String password = CommonUtils.getMD5Str(pwd).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(context));
            try {
                RetrofitUtils.getInstance(mContext).setContext(mContext).payForFlashSale(goodsId,  password, new Callback<FlashSalePayResult>() {
                            @Override
                            public void onResponse(Call<FlashSalePayResult> call, Response<FlashSalePayResult> response) {
                                FlashSalePayResult body = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                        if(-5==body.code){//支付密码错误
                                            showErrorPWDDialog();
                                            return;
                                        }
                                        Intent intent = new Intent(mContext, PrizeVoucherDetailActivity.class);
                                        intent.putExtra("voucher_index", body.data.voucherIndex);
                                        intent.putExtra(Constants.FLASHSALEPAY,true);
                                        startActivity(intent);
                                        finish();
                                        //软键盘消失
                                        dismissJP();
                                        paydialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<FlashSalePayResult> call, Throwable t) {
                                Logger.i("限时抢购网络失败信息："+t.getMessage());
                                showToast(R.string.net_work_not_available);
                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 支付密码填写错误后，弹出提示框
         */
        private void showErrorPWDDialog() {
       showDialog(null, "密码错误，请重新输入", null, 0, Gravity.CENTER, "重置密码", "重新输入", false, new OnClickListener() {
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


        //软键盘消失
        public void dismissJP() {
            View view = getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }
}
