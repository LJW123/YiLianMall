package com.yilian.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;

import java.util.Map;


/**
 * Created by  on 2017/6/23 0023.
 */

public class PullAliPayUtil {
    private static Context mContext;
    private static Activity activity;
    private static final int ALI_PAY=666;
    static PullAliPayUtilSuccessListener myPullAliPayUtilSuccessListener;
    protected SharedPreferences sp;

    /**
     * 支付宝支付
     *
     * @param jsPayClass js传递过来的订单信息
     * @param context
     */
    public static void zhifubao(final JsPayClass jsPayClass, final Context context,PullAliPayUtilSuccessListener pullAliPayUtilSuccessListener) {
        mContext = context;
        myPullAliPayUtilSuccessListener = pullAliPayUtilSuccessListener;

        /**
         * 完整的符合支付宝参数规范的订单信息服务器返回不需要客户端处理
         */
        if (TextUtils.isEmpty(jsPayClass.orderString)) {
            return;
        }
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                activity = (Activity) context;
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(jsPayClass.orderString, true);
                Logger.i("result " + result.toString());
                Message msg = new Message();
                msg.what = ALI_PAY;
                msg.obj = result;
                Bundle data = new Bundle();
                data.putSerializable("orderInfo", jsPayClass);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        };

//         必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

  static Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what) {
              case ALI_PAY:
                  PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                  String resultStatus = payResult.getResultStatus();
                  Logger.i("resultStatus  " + resultStatus);

                  // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                  if (TextUtils.equals(resultStatus, "9000")) {
                      myPullAliPayUtilSuccessListener.pullAliPaySuccessListener();

//
////                    通知JS支付完成  appPayCallback()
//                      Logger.i("存入奖励sp:"+true);
//                      PreferenceUtils.writeBoolConfig(Constants.SPKEY_HONG_BAO,true,mContext);
//                      Boolean aBoolean = PreferenceUtils.readBoolConfig(Constants.SPKEY_HONG_BAO, mContext, false);
//                      Logger.i("去除的奖励sp1:"+aBoolean);
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
                      break;
                  }
          }
      }
  };
}
