package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;

/**
 * Created by Administrator on 2016/4/7.
 */
public class SnatchPayDialog extends Dialog {


    private int count;
    private String goodsName;
    private Context context;
    private String strLeft;
    private String strCenter;
    private String strRight;
    private String number;
    private String activityId, activity_expend;

    private TextView textView;
    private GridPasswordView pwdView;
    private TextView tvGoodname;
    private TextView tvTitle;


    private SnatchNetRequest request;
    private TextView tv_snatch_cargo;

    private Handler handler;

    public SnatchPayDialog(Context context, String goodsName, String strLeft, String strCenter, String strRight, String activityId, int count, String activity_expend,Handler handler) {
        super(context, R.style.Dialog);
        this.context = context;
        this.strLeft = strLeft;
        this.strCenter = strCenter;
        this.strRight = strRight;
        this.activityId = activityId;
        this.goodsName = goodsName;
        this.count = count;
        this.activity_expend = activity_expend;
        this.handler=handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_snatch_pay);
        initView();
    }

    public void initView() {
        textView = (TextView) findViewById(R.id.tv_snatch_number);
        tvGoodname = (TextView) findViewById(R.id.tv_snatch_name);
        tv_snatch_cargo = (TextView) findViewById(R.id.tv_snatch_cargo);
        tvTitle = (TextView) findViewById(R.id.iv_title);
        pwdView = (GridPasswordView) findViewById(R.id.pwd);
        Logger.i(activity_expend+"+++++++++++++++");
        if (!activity_expend.equals("0"))
            tv_snatch_cargo.setText(String.format("%.2f", Float.parseFloat(activity_expend) / 100));
        if (strLeft == null) {
            strLeft = "0";
        }
        if (strCenter == null) {
            strCenter = "0";
        }
        if (strRight == null) {
            strRight = "0";
        }
        number = strLeft + strCenter + strRight;
        textView.setText(number);
        tvGoodname.setText(goodsName);
        tvTitle.setText("本次夺宝1件商品，参与" + count + "人次");
        pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged(String psw) {

            }

            @Override
            public void onMaxLength(String psw) {

                if (request == null) {
                    request = new SnatchNetRequest(context);
                }
                //支付密码
                String pwd = CommonUtils.getMD5Str(pwdView.getPassWord()).toLowerCase() + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(context));
/**
 * 活动id    String activityId;
 */
                request.SnatchPayNet(activityId, number, pwd, new RequestCallBack<BaseEntity>() {
                    @Override
                    public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                        switch (responseInfo.result.code) {
                            case 1:
                                Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();

                                //软键盘消失
                                View view = getWindow().peekDecorView();
                                if (view != null) {
                                    InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.obj = Constants.ACTIVITY_STATE_END;
                                        message.arg1 = Constants.EXECUTE_SUCCESS;
                                        handler.sendMessage(message);
                                    }
                                }).start();

                                dismiss();

                                break;
                            case -5:
                                pwdView.clearPassword();
                                Toast.makeText(context, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                                break;
                            case -13:
                                Toast.makeText(context, "奖励不足，请充值", Toast.LENGTH_SHORT).show();
                                dismiss();

                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
