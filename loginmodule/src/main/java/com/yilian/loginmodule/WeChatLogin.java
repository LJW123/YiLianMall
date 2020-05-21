package com.yilian.loginmodule;

import android.content.Context;
import android.widget.Toast;

import com.yilian.mylibrary.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by  on 2017/4/12 0012.
 * 拉起微信授权登录
 * 登录成功后，走主module里面的WxEntryActivity的登录回调
 */

public class WeChatLogin {
    private static volatile WeChatLogin weChatLogin;
    private IWXAPI api;

    private WeChatLogin() {
    }

    public static  WeChatLogin getInstance() {
        if(weChatLogin==null){
            synchronized (WeChatLogin.class){
                if(weChatLogin==null){
                    weChatLogin = new WeChatLogin();
                }
            }
        }
        return weChatLogin;
    }

    public void wxLogin(Context context) {//微信登录
        try {
//            startMyDialog();//拉起微信时弹窗，该弹窗在微信拉起后消失，消失操作在WXEntryActivity界面的OnCreate方法中执行
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "正在跳转微信,请稍等", Toast.LENGTH_SHORT).show();

        if (api == null) {
            api = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        }

        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }

        api.registerApp(Constants.APP_ID);

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "login_state";
        api.sendReq(req);
//        finish();
    }
}
