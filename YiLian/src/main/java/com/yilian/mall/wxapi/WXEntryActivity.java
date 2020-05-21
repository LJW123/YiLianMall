package com.yilian.mall.wxapi;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.LoginSuccessFlow;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.WeiXinNetRequest;
import com.yilian.mall.ui.BindPhoneActivity;
import com.yilian.mall.ui.BindWeiXinActivity;
import com.yilian.mall.utils.SharedPreferencesUtils;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.entity.ThirdPartyNewUserEntity;
import com.yilian.networkingmodule.entity.WeiXinInfoEntity;
import com.yilian.networkingmodule.entity.WeiXinLoginEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    //    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;


    private WeiXinNetRequest request;
    private String access_token;
    private String openid;
    private String headimgurl;
    private String nickname;
    @ViewInject(R.id.img_weixin)
    private ImageView img_weixin;
    @ViewInject(R.id.tv_nike_name)
    private TextView nikeName;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private boolean comment_success, pay_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("UserInfor", 0);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.handleIntent(getIntent(), this);

//        setContentView(R.layout.activity_bind_weixin);
        stopMyDialog();//拉起微信时会弹窗，执行到这里代表微信登录已经拉起，弹窗消失，弹窗拉起操作在JPLoginActivity的微信授权登录方法里发起的
        ViewUtils.inject(this);

        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }


    @Override
    public void onReq(BaseReq arg0) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.getType()) {
            case ConstantsAPI.COMMAND_SENDAUTH:
                //登录回调
                LoginCallBack(resp);
                break;
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                //分享回调
                ShareCallBack(resp);
                break;
            default:
                break;
        }
    }

    /**
     * 分享回调
     *
     * @param resp
     */
    private void ShareCallBack(BaseResp resp) {
        int result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                hongbao();//调用拆奖励
                EventBus.getDefault().post("WeiXin");
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
//        2017年11月10日11:54:11 产品提出不再提示分享结果的消息
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.change_in, R.anim.change_out);
    }

    private SharedPreferencesUtils spUtils;

    private void hongbao() {
        Logger.i("微信奖励分享成功后设置spboolean值为false");
//        2017年11月10日12:02:03 去掉微信分享时回调javascript:appPayCallback()的方法，只有在网页拉起第三方支付{@link AndroidJs jumpToVendorPay(String type, String orderStr)}回来时，才回调该方法
        sp.edit().putBoolean(Constants.SPKEY_HONG_BAO, false).commit();
    }


    /**
     * 授权登录回调
     *
     * @param resp
     */
    public void LoginCallBack(BaseResp resp) {
        SendAuth.Resp sendAuthResp = (SendAuth.Resp) resp;// 用于分享时不要有这个，不能强转
        String code = sendAuthResp.code;
        Logger.i("微信授权登录回调code: " + code);
        if (TextUtils.isEmpty(code)) {
            showToast("授权取消");
            finish();
            return;
        }
        showToast("正在登录,请稍等");
        getResult(code);
    }

    private void getResult(final String code) {
        Logger.i("微信登录回调成功code：" + code);
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Constants.APP_ID
                + "&secret="
                + Constants.WEIXIN_APPSECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        try {
            RetrofitUtils.getInstance(mContext).getWeiXinLoginEntity(Constants.APP_ID, Constants.WEIXIN_APPSECRET, code, "authorization_code", new Callback<WeiXinLoginEntity>() {

                @Override
                public void onResponse(Call<WeiXinLoginEntity> call, Response<WeiXinLoginEntity> response) {
                    WeiXinLoginEntity body = response.body();
                    if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,body)) {
                        showToast(getString(R.string.login_module_service_exception));
                        finish();
                        return;
                    }
                    access_token = body.access_token;
                    openid = body.openid;
                    if (openid != null) {//在微信登录请求过程中，强制取消登录，比如连续点击返回键，取消dialog转动，并返回个人中心，返回的openid为null,此时会出现异常的信息，所以openid为null时不处理
                        Logger.i("微信登录走了这里1");
                        getweixininfo(access_token, openid);
                    } else {
                        showToast("绑定失败,请重试");
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<WeiXinLoginEntity> call, Throwable t) {
                    Toast.makeText(mContext, "绑定失败,请重新绑定", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }


    private void getweixininfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        try {
            RetrofitUtils.getInstance(mContext).getWeiXinUserInfo(access_token, openid, new Callback<WeiXinInfoEntity>() {
                @Override
                public void onResponse(Call<WeiXinInfoEntity> call, Response<WeiXinInfoEntity> response) {
                    Logger.i("微信登录走了这里4");
                    WeiXinInfoEntity body = response.body();
                    if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,body)) {
                        Toast.makeText(mContext, R.string.login_module_service_exception, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }


                    if ("2".equals(sp.getString(Constants.SPKEY_WX_LOGIN, "-1"))) {//如果微信登录是用于和已登录账号进行绑定，则在微信授权成功后调取绑定第三方账号接口
                        try {
//                                        绑定微信时，先检测绑定的微信号是否是已有账号， 已存在则提示用户是否进行账号信息合并，不存在则直接绑定
                            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                                    .checkThirdPartyRecord(body.unionid, new Callback<ThirdPartyNewUserEntity>() {
                                        @Override
                                        public void onResponse(Call<ThirdPartyNewUserEntity> call, Response<ThirdPartyNewUserEntity> response) {
                                            ThirdPartyNewUserEntity body1 = response.body();
                                            if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,body1)) {
                                                showToast(R.string.service_exception);
                                                return;
                                            }
                                            switch (body1.newUser) {
                                                case "1":  //该微信号已经存在,提示用户是否合并账号
                                                    new VersionDialog.Builder(mContext)
                                                            .setMessage("该微信账号已是乐分会员,是否进行账号合并")
                                                            .setPositiveButton("合并", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    bindWeChatAccount(body);
                                                                }
                                                            })
                                                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    finish();
                                                                }
                                                            }).create().show();
                                                    break;
                                                case "2"://该微信号不存在,直接绑定
                                                    bindWeChatAccount(body);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ThirdPartyNewUserEntity> call, Throwable t) {
                                            showToast(R.string.net_work_not_available);
                                        }
                                    });

                        } catch (Exception e) {
                            finish();
                            e.printStackTrace();
                        }
                    } else if ("3".equals(sp.getString(Constants.SPKEY_WX_LOGIN, "-1"))) { //否则则是领奖励绑定
                        //判断绑定成功与否
                        showToast("微信登录走了这里");
                        Intent intent = new Intent(WXEntryActivity.this, BindWeiXinActivity.class);
                        intent.putExtra("headimgurl", body.headimgurl);
                        intent.putExtra("nickname", body.nickname);
                        startActivity(intent);
                        updateOpenid(body.openid, body.unionid, body.nickname, body.headimgurl);
                        finish();

                    } else {//微信登录是用于账号登录
                        //如果微信登录是用于账号登录的,则在微信授权登录后，调取第三方登录接口获取token
                        try {
//                            先检查第三方账户是否已存在，如果存在则直接登录，
// 如果不存在，则提示用户是否将该新账号和自己已有的手机号账号进行合并
                            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                                    .checkThirdPartyRecord(body.unionid, new Callback<ThirdPartyNewUserEntity>() {
                                        @Override
                                        public void onResponse(Call<ThirdPartyNewUserEntity> call, Response<ThirdPartyNewUserEntity> response) {
                                            ThirdPartyNewUserEntity body1 = response.body();
                                            if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                                                showToast(R.string.service_exception);
                                                finish();
                                                return;
                                            }
                                            if (CommonUtils.serivceReturnCode(mContext, body1.code, body1.msg, LeFenPhoneLoginActivity.class)) {
                                                switch (body1.code) {
                                                    case 1:

                                                        switch (body1.newUser) {
                                                            case "1":

                                                                //该微信号已经存在，直接登录
                                                                loginByThird(body.unionid, body.nickname, body.headimgurl, body.openid, "", "", "");
                                                                break;
                                                            case "2":
                                                                //该微信号不存在，弹窗提示用户是否绑定手机号码
                                                                new VersionDialog.Builder(mContext)
                                                                        .setMessage("是否已有手机账号")
                                                                        .setPositiveButton("有,去绑定", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Intent intent = new Intent(WXEntryActivity.this, BindPhoneActivity.class);
                                                                                intent.putExtra("loginType", "mergeAccount");
                                                                                intent.putExtra("openId", body.openid);
                                                                                intent.putExtra("unionid", body.unionid);
                                                                                intent.putExtra("nickname", body.nickname);
                                                                                intent.putExtra("headimgurl", body.headimgurl);
                                                                                intent.putExtra("verifyType", "3");
                                                                                intent.putExtra("type", "0");//该次登录是微信账号和手机号合并
                                                                                startActivity(intent);
                                                                                dialog.dismiss();
                                                                                finish();
                                                                            }
                                                                        })
                                                                        .setNegativeButton("没有,直接登录", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                loginByThird(body.unionid, body.nickname, body.headimgurl, body.openid, "2", "", "");
                                                                                dialog.dismiss();
                                                                            }
                                                                        }).create().show();
                                                                break;
                                                            default:
                                                                break;
                                                        }

                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ThirdPartyNewUserEntity> call, Throwable t) {

                                        }
                                    });

                        } catch (Exception e) {
                            e.printStackTrace();
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<WeiXinInfoEntity> call, Throwable t) {
                    Toast.makeText(WXEntryActivity.this, R.string.net_work_not_available, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
    /**
     * 已登录账号绑定微信账号
     *
     * @param body
     */
    private void bindWeChatAccount(WeiXinInfoEntity body) {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext)).
                bindThirdPartyAccount(body.unionid, "1", body.nickname, body.headimgurl, body.openid, "", null, new Callback<com.yilian.networkingmodule.entity.BaseEntity>() {
                    @Override
                    public void onResponse(Call<com.yilian.networkingmodule.entity.BaseEntity> call, Response<com.yilian.networkingmodule.entity.BaseEntity> response) {
                        com.yilian.networkingmodule.entity.BaseEntity baseEntity = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                            showToast(getString(R.string.login_module_service_exception));
                            finish();
                            return;
                        }
                        if (CommonUtils.serivceReturnCode(mContext, baseEntity.code, baseEntity.msg, LeFenPhoneLoginActivity.class)) {
                            switch (baseEntity.code) {
                                case 1:
                                    showToast("绑定成功");
                                    break;
                                default:
                                    break;
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<com.yilian.networkingmodule.entity.BaseEntity> call, Throwable t) {
                        showToast(R.string.net_work_not_available);
                        finish();
                    }
                });
    }

    private void loginByThird(String unionid, String nickname, String headimgurl, String openid, String newUser, String phone, String smsCode) {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).
                loginByThirdParyt(unionid, "0", "1", nickname, headimgurl, openid, newUser, phone, smsCode, new Callback<LoginEntity>() {
                    @Override
                    public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                        LoginEntity loginEntity = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())) {
                            Toast.makeText(mContext, R.string.login_module_service_exception, Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        Logger.i("loginEntity.toString :  " + loginEntity.toString());
                        Logger.i("微信登录走了这里5:code:" + loginEntity.code + "  " + loginEntity.token);
                        if (CommonUtils.serivceReturnCode(mContext, loginEntity.code, "", LeFenPhoneLoginActivity.class)) {
                            switch (loginEntity.code) {

                                case 1:
                                    showToast("微信登录成功");
                                    Logger.i("微信登录走了这里6" + loginEntity.token);
                                    LoginSuccessFlow.getInstance().loginsuccessFlow(loginEntity, mContext, "");
                                    break;
                                case -5:
                                    Logger.i("微信登录走了这里7" + loginEntity.token);
                                    break;
                                default:
                                    Logger.i("微信登录走了这里8" + loginEntity.code);
                                    break;
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<LoginEntity> call, Throwable t) {
                        Toast.makeText(WXEntryActivity.this, R.string.net_work_not_available, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    /**
     * 更新 openId
     *
     * @param openid
     * @param unionid
     * @param nickname
     * @param headimgurl
     */
    private void updateOpenid(String openid, String unionid, String nickname, String headimgurl) {
        if (request == null) {
            request = new WeiXinNetRequest(WXEntryActivity.this);
        }

        request.updateOpenid(openid, unionid, nickname, headimgurl, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                Logger.i("updateOpenid接口返回code" + responseInfo.result.code);
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("绑定成功");
                        break;
                    default:
                        showToast("微信回调页面绑定失败," + responseInfo.result.code);
                        break;
                }
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Logger.i("onFailure" + e.getExceptionCode() + "--" + s);
                finish();
            }
        });
    }
}