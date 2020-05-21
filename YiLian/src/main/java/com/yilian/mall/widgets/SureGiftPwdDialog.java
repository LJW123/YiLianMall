package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.ui.InitialPayActivity;
import com.yilian.mall.ui.IntegralGiveAwayActivity;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.TransferIntegralEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.mall.MyApplication.sp;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SureGiftPwdDialog extends Dialog {
    private String integral;
    private ImageView img_dismiss;
    private TextView tv_forget_pwd;
    private GridPasswordView pwdView;

    private Context context;
    private String type;
    private String money, phone,marContent;
    private String scanLebi, scanLefen;
    private MyIncomeNetRequest request;
    private Handler handler;
    private boolean hasMark=false;


    public SureGiftPwdDialog(Context context, String type, String money, String phone, Handler handler) {
        super(context, R.style.Dialog);
        this.context = context;
        this.type = type;
        this.money = money;
        this.phone = phone;
        this.handler = handler;
    }

    public SureGiftPwdDialog(Context context, String money) {
        super(context, R.style.Dialog);
        this.context = context;
        this.money = money;
    }

    /**
     * 扫码支付 密码弹窗构造 奖券转赠
     *
     * @param context
     * @param type        "all"
     * @param scanLebi
     * @param scanLefen
     * @param acceptPhone
     * @param handler
     */
    public SureGiftPwdDialog(Context context, String type, String scanLebi, String scanLefen, String acceptPhone, Handler handler) {
        super(context, R.style.GiftDialog);
        this.context = context;
        this.type = type;
        this.scanLebi = scanLebi;
        this.scanLefen = scanLefen;
        this.phone = acceptPhone;
        this.handler = handler;
    }

    /**
     * 奖券转赠
     *
     * @param context
     * @param type        "all"
     * @param scanLebi
     * @param scanLefen
     * @param acceptPhone
     * @param handler
     */
    public SureGiftPwdDialog(Context context, String type, String scanLebi, String scanLefen, String acceptPhone, String Integral, Handler handler) {
        super(context, R.style.GiftDialog);
        this.context = context;
        this.type = type;
        this.scanLebi = scanLebi;
        this.scanLefen = scanLefen;
        this.phone = acceptPhone;
        this.integral = Integral;
        this.handler = handler;
    }
    /**
     * 奖券转赠--需要备注信息
     *
     * @param context
     * @param type        "all"
     * @param scanLebi
     * @param scanLefen
     * @param acceptPhone
     * @param handler
     */
    public SureGiftPwdDialog(Context context,boolean hasMark, String markContent,String type, String scanLebi, String scanLefen, String acceptPhone, String Integral, Handler handler) {
        super(context, R.style.GiftDialog);
        this.context = context;
        this.type = type;
        this.scanLebi = scanLebi;
        this.scanLefen = scanLefen;
        this.phone = acceptPhone;
        this.integral = Integral;
        this.handler = handler;
        this.marContent=markContent;
        this.hasMark=hasMark;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_suregift_pwd);

        initView();
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
                sendRequest(pwdView.getPassWord());
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
    }

    private void sendRequest(String pwd) {
        //支付密码
        Logger.i("sendRequestType   " + type);
        String password = CommonUtils.getMD5Str(pwd).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(context));
        switch (type) {
            case "vouchers":
                //乐享币
                lebi(password);
                break;
            case "shopping":
                //乐分币
                lefen(password);
                break;
            case "all":
                //扫码转赠乐分、乐享
                leBiAndLeFen(password);
                break;
            case "integral":
                if (hasMark){
                    integralDonations(password);
                }else {
                    integralDonation(password);
                }
                break;
            case "calculatedStress":
                calculatedStressDonations(password);
                break;
                default:
                    break;
        }
    }

    /**
     * 奖券转赠
     *
     * @param password
     */
    private void integralDonation(String password) {
        RetrofitUtils2.getInstance(context).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(context)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(context))
                .getTransferIntegral(phone, scanLebi, "0", password, new Callback<TransferIntegralEntity>() {
                    @Override
                    public void onResponse(Call<TransferIntegralEntity> call, Response<TransferIntegralEntity> response) {

                        HttpResultBean body = response.body();

                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                            if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        TransferIntegralEntity body1 = response.body();
                                        Logger.i("转赠结果："+body1.data.toString());
                                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                        Toast.makeText(context, "转赠成功", Toast.LENGTH_SHORT).show();
                                        //软键盘消失
                                        dismissJP();
                                        dismiss();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Message message = new Message();
                                                message.what = Constants.EXECUTE_SUCCESS;
                                                handler.sendMessage(message);
                                            }
                                        }).start();
                                        break;
                                }
                            }else {
                                Toast.makeText(context, body.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TransferIntegralEntity> call, Throwable t) {
                        Toast.makeText(context,"转赠失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * 奖券转赠
     *
     * @param password
     */
    private void integralDonations(String password) {
        if (context instanceof IntegralGiveAwayActivity){
            ((IntegralGiveAwayActivity) context).startMyDialog();
        }
        RetrofitUtils2.getInstance(context).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(context)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(context))
                .getTransferIntegrals(phone, scanLebi, "0", password,marContent, new Callback<TransferIntegralEntity>() {
                    @Override
                    public void onResponse(Call<TransferIntegralEntity> call, Response<TransferIntegralEntity> response) {
                        if (context instanceof IntegralGiveAwayActivity){
                            ((IntegralGiveAwayActivity) context).stopMyDialog();
                        }

                        HttpResultBean body = response.body();

                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                            if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        TransferIntegralEntity body1 = response.body();
                                        Logger.i("转赠结果："+body1.data.toString());
                                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                        Toast.makeText(context, "转赠成功", Toast.LENGTH_SHORT).show();
                                        //软键盘消失
                                        dismissJP();
                                        dismiss();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Message message = new Message();
                                                message.what = Constants.EXECUTE_SUCCESS;
                                                handler.sendMessage(message);
                                            }
                                        }).start();
                                        break;
                                }
                            }else {
                                Toast.makeText(context, body.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TransferIntegralEntity> call, Throwable t) {
                        if (context instanceof IntegralGiveAwayActivity){
                            ((IntegralGiveAwayActivity) context).stopMyDialog();
                        }
                        Toast.makeText(context,"转赠失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 益豆转赠
     * @param password
     */
    private void calculatedStressDonations(String password) {
        if (context instanceof IntegralGiveAwayActivity){
            ((IntegralGiveAwayActivity) context).startMyDialog();
        }
        RetrofitUtils2.getInstance(context).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(context)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(context))
                .getTransferCalculatedStress(phone, scanLebi, "0", password,marContent, new Callback<TransferIntegralEntity>() {
                    @Override
                    public void onResponse(Call<TransferIntegralEntity> call, Response<TransferIntegralEntity> response) {
                        if (context instanceof IntegralGiveAwayActivity){
                            ((IntegralGiveAwayActivity) context).stopMyDialog();
                        }

                        HttpResultBean body = response.body();

                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                            if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        TransferIntegralEntity body1 = response.body();
                                        Logger.i("转赠结果："+body1.data.toString());
                                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                        Toast.makeText(context, "转赠成功", Toast.LENGTH_SHORT).show();
                                        //软键盘消失
                                        dismissJP();
                                        dismiss();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Message message = new Message();
                                                message.what = Constants.EXECUTE_SUCCESS;
                                                handler.sendMessage(message);
                                            }
                                        }).start();
                                        break;
                                }
                            }else {
                                Toast.makeText(context, body.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TransferIntegralEntity> call, Throwable t) {
                        if (context instanceof IntegralGiveAwayActivity){
                            ((IntegralGiveAwayActivity) context).stopMyDialog();
                        }
                        Toast.makeText(context,"转赠失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void leBiAndLeFen(String password) {
        if (request == null) {
            request = new MyIncomeNetRequest(context);
        }
        request.sendMoney(phone, scanLefen, String.valueOf(NumberFormat.convertToDouble(scanLebi, 0.0) * 100), password, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        Toast.makeText(context, "转赠成功", Toast.LENGTH_SHORT).show();
                        //软键盘消失
                        dismissJP();

                        dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
//                                message.obj = Constants.ACTIVITY_STATE_END;
                                message.what = Constants.EXECUTE_SUCCESS;
                                handler.sendMessage(message);
                            }
                        }).start();

                        break;
                    case -3:
                        Toast.makeText(context, "系统繁忙", Toast.LENGTH_SHORT).show();
                        break;
                    case -5:
                        pwdView.clearPassword();
                        Toast.makeText(context, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        break;
                    case -13:
                        Toast.makeText(context, "奖励不足", Toast.LENGTH_SHORT).show();
                        break;
                    case -18:
                        Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
                        break;
                    case -56:
                        Toast.makeText(context, "转账金额过小或未输入", Toast.LENGTH_SHORT).show();
                        break;
                    case -57:
                        Toast.makeText(context, "转账金额过大或超限", Toast.LENGTH_SHORT).show();
                        break;
                    case -60:
                        Toast.makeText(context, "不能给自己转账", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    //软键盘消失
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void lebi(String password) {
        if (request == null) {
            request = new MyIncomeNetRequest(context);
        }
        request.sendMoney(phone, "0", Float.parseFloat(money) * 100 + "", password, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        Toast.makeText(context, "转赠成功", Toast.LENGTH_SHORT).show();
                        dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.obj = Constants.ACTIVITY_STATE_END;
                                message.arg1 = Constants.EXECUTE_SUCCESS;
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    case -3:
                        Toast.makeText(context, "系统繁忙", Toast.LENGTH_SHORT).show();
                        break;
                    case -5:
                        pwdView.clearPassword();
                        Toast.makeText(context, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        break;
                    case -13:
                        Toast.makeText(context, "奖励不足", Toast.LENGTH_SHORT).show();
                        dismiss();
                        break;
                    case -56:
                        pwdView.clearPassword();
                        Toast.makeText(context, "转账金额过小", Toast.LENGTH_SHORT).show();
                        break;
                    case -57:
                        pwdView.clearPassword();
                        Toast.makeText(context, "转账金额过大", Toast.LENGTH_SHORT).show();
                        break;
                    case -60:
                        Toast.makeText(context, "不能给自己转账", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lefen(String password) {
        if (request == null) {
            request = new MyIncomeNetRequest(context);
        }
        request.sendMoney(phone, money, "0", password, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        Toast.makeText(context, "转赠成功", Toast.LENGTH_SHORT).show();
                        dismiss();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.obj = Constants.ACTIVITY_STATE_END;
                                message.arg1 = Constants.EXECUTE_SUCCESS;
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    case -3:
                        Toast.makeText(context, "系统繁忙", Toast.LENGTH_SHORT).show();
                        break;
                    case -5:
                        pwdView.clearPassword();
                        Toast.makeText(context, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        break;
                    case -13:
                        Toast.makeText(context, "奖励不足", Toast.LENGTH_SHORT).show();
                        dismiss();
                        break;
                    case -56:
                        pwdView.clearPassword();
                        Toast.makeText(context, "转账金额过小", Toast.LENGTH_SHORT).show();
                        break;
                    case -57:
                        pwdView.clearPassword();
                        Toast.makeText(context, "转账金额过大", Toast.LENGTH_SHORT).show();
                        break;
                    case -60:
                        Toast.makeText(context, "不能给自己转账", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override

            public void onFailure(HttpException e, String s) {
                Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
