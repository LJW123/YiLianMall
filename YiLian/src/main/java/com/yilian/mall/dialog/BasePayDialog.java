package com.yilian.mall.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ui.InitialPayActivity;
import com.yilian.mall.widgets.GridPasswordView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.MerchantCashPayEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.activity.MerchantAuditActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/6/14 0014.
 * 支付的弹窗
 */
public class BasePayDialog extends Dialog {
    private final Context mContext;
    private final String orderIndex;
    private final String payType;
    private ImageView img_dismiss;
    private TextView tv_forget_pwd;
    private GridPasswordView pwdView;


    public BasePayDialog(Context context, String orderIndex, String payType) {
        super(context, R.style.GiftDialog);
        this.mContext = context;
        this.orderIndex = orderIndex;
        this.payType = payType;

        Logger.i("BasePayDialog   "+payType+" orderIndex "+orderIndex);
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
                Logger.i("payType   " + payType + "  orderIndex  " + orderIndex);
                switch (payType) {
                    case "merchantPay": //商家缴费（奖励）
                        String password = CommonUtils.getMD5Str(pwdView.getPassWord()) + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
                        sendMerchantPay(password);
                        break;
                }
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
                mContext.startActivity(new Intent(mContext, InitialPayActivity.class));
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtils.getScreenWidth(mContext); //设置宽度
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    //商家缴费
    public void sendMerchantPay(String passWord) {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantCashPay(orderIndex, passWord, new Callback<MerchantCashPayEntity>() {
                    @Override
                    public void onResponse(Call<MerchantCashPayEntity> call, Response<MerchantCashPayEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        dismiss();
                                        dismissJP();
                                        Intent intent = new Intent(mContext, MerchantAuditActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantCashPayEntity> call, Throwable t) {

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
}