package com.yilian.mall.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTConsumerProfit;
import com.yilian.mall.entity.MTShareRedBag;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.http.OneBuyNetRequest;
import com.yilian.mall.http.PaymentNetRequest;
import com.yilian.mall.ui.JPMainActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;

import static com.yilian.mylibrary.Constants.SHARE_CALLBACK;

public class ShareUtile {
    private String callBackUrl;
    private int type = 0;
    private String content = "";
    private String title = "益联益家";
    private String extendUrl = "";
    private String imgPath = "";

    private UMSocialService mController;
    private Context mContext;
    private String orderId;
    private String oneBuyPrizeShareId;
    private boolean isNotFromOneBuyActivity;
    private OneBuyNetRequest oneBuyNetRequest;
    private MallNetRequest mallNetRequest;
    private PaymentNetRequest paymentNetRequest;
    //    这个回调监听，只有QQ回调才能监听到  微信的回调监听在WXEntryActivity里面
    SocializeListeners.SnsPostListener snsListener = new SocializeListeners.SnsPostListener() {
        @Override
        public void onStart() {
//			Toast.makeText(mContext, "分享开始",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
            Logger.json(eCode + "eCode");
            if (eCode == 200) {
                {
//					打开URL
                    Logger.i("callBackUrl1:" + callBackUrl);
                    if (!TextUtils.isEmpty(callBackUrl)) {
                        Logger.i("callBackUrl2:" + callBackUrl);
                        if ("1".equals(callBackUrl)) {
                            try {//防止类型转换错误
                                ((WebViewActivity) mContext).webView.loadUrl("javascript:appCallback()");
                                ((WebViewActivity) mContext).webView.loadUrl("javascript:appCallback(" + com.yilian.mylibrary.PreferenceUtils.readStrConfig(SHARE_CALLBACK, mContext) + ")");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("0".equals(callBackUrl)) {
                            //不做任何事情
                        } else if (("comment_success").equals(callBackUrl)) {
                            getShareRedBag();
                            Intent intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                            mContext.startActivity(intent);
                        } else if (("pay_success").equals(callBackUrl)) {
                            getConsumerProfit();
                            Intent intent = new Intent(mContext, JPMainActivity.class);
                            intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                            mContext.startActivity(intent);
                        } else if (Constants.RED_SHARE.equals(callBackUrl)) {
                            /**
                             * 这里是奖励的分享 不做回调
                             */
                        } else {//此时callBackURL是一个新的url
                            //打开url
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra("url", callBackUrl);
                            mContext.startActivity(intent);
                        }

                    } else {
                        Intent intent = new Intent(mContext, JPMainActivity.class);
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        mContext.startActivity(intent);
                    }
                }
            } else {
//				Toast.makeText(mContext,"分享失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public ShareUtile(Context mContext, int type, String content, String imgPath, String extendUrl) {
        this.mContext = mContext;
        this.type = type;
        this.content = content;
        this.imgPath = imgPath;
        if (!TextUtils.isEmpty(extendUrl)) {
            this.extendUrl = extendUrl;
        } else {
            this.extendUrl = PreferenceUtils.readStrConfig(Constants.USERURL, mContext);
        }
    }

    /**
     * andoridJS调用的分享方法
     * @param mContext
     * @param type
     * @param content
     * @param title
     * @param imgPath
     * @param extendUrl
     */
    public ShareUtile(Context mContext, int type, String content, String title, String imgPath, String extendUrl) {
        this.mContext = mContext;
        this.type = type;
        this.title = title;
        this.content = content;
        this.imgPath = imgPath;
        if (!TextUtils.isEmpty(extendUrl)) {
            this.extendUrl = extendUrl;
        } else {
            this.extendUrl = PreferenceUtils.readStrConfig(Constants.USERURL, mContext);
        }

    }

    /**
     * 分享
     *
     * @param mContext
     * @param type
     * @param content
     * @param title
     * @param imgPath
     * @param extendUrl
     * @param callBackUrl
     */
    public ShareUtile(Context mContext, int type, String content, String title, String imgPath, String extendUrl, String callBackUrl) {
        this.mContext = mContext;
        this.type = type;
        this.title = title;
        this.content = content;
        this.imgPath = imgPath;
        if (!TextUtils.isEmpty(extendUrl)) {
            this.extendUrl = extendUrl;
        } else {
            this.extendUrl = PreferenceUtils.readStrConfig(Constants.USERURL, mContext);
        }
        this.callBackUrl = callBackUrl;
    }

    //带orider_id的分享
    public ShareUtile(Context mContext, int type, String content, String title, String imgPath, String extendUrl, String orderId, String callBackUrl) {
        this.mContext = mContext;
        this.type = type;
        this.title = title;
        this.content = content;
        this.imgPath = imgPath;
        if (!TextUtils.isEmpty(extendUrl)) {
            this.extendUrl = extendUrl;
        } else {
            this.extendUrl = PreferenceUtils.readStrConfig(Constants.USERURL, mContext);
        }
        this.orderId = orderId;
        this.callBackUrl = callBackUrl;
    }

    public void share() {
        // 分享 开始
        mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
        if (content == null || "".equals(content)) {
            content = "花出去的钱再也不是泼出去的水，而是您的资本";
        }
        if (title == null || "".equals(title)) {
            title = mContext.getString(R.string.library_module_share_default_content);
        }
        mController.setShareContent(content);
        UMImage shareImage;
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        if (!TextUtils.isEmpty(imgPath)) {
            if (imgPath.indexOf("http://") >= 0 || imgPath.indexOf("https://") >= 0) {
                shareImage = new UMImage(mContext, imgPath);
            } else {
                shareImage = new UMImage(mContext, BitmapFactory.decodeFile(imgPath));
            }
        } else {
            if (Constants.RED_SHARE.equals(callBackUrl)) {
                Logger.i("2017年11月27日 09:57:32-走了奖励");
                shareImage = new UMImage(mContext, R.mipmap.share_red_icon);
            } else {
                Logger.i("2017年11月27日 09:57:32-走了普通");
                shareImage = new UMImage(mContext, R.mipmap.shareicon);
            }
        }
        mController.setShareMedia(shareImage);
        Logger.i("分享Url:"+extendUrl);
        switch (type) {
            case 3:// QQ空间分享
                QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, Constants.QQ_APPID,
                        Constants.QQ_APPKEY);
                qZoneSsoHandler.addToSocialSDK();
                QZoneShareContent qzone = new QZoneShareContent();
                qzone.setShareContent(title);
                //                QQ分享url必须以http或https开头
                repairExtendUrl();
                qzone.setTargetUrl(extendUrl);
                qzone.setTitle(content);
                qzone.setShareImage(shareImage);
                mController.setShareMedia(qzone);
                mController.directShare(mContext, SHARE_MEDIA.QZONE, snsListener);
                break;
            case 4:// QQ分享
                UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, Constants.QQ_APPID,
                        Constants.QQ_APPKEY);
                qqSsoHandler.addToSocialSDK();
                // 设置QQ平台的分享内容
                QQShareContent qqShareContent = new QQShareContent(shareImage);
                qqShareContent.setShareContent(title);
                //                QQ分享url必须以http或https开头
                repairExtendUrl();
                qqShareContent.setTargetUrl(extendUrl);
                qqShareContent.setShareImage(shareImage);
                qqShareContent.setTitle(content);
                mController.setShareMedia(qqShareContent);
                mController.directShare(mContext, SHARE_MEDIA.QQ, snsListener);
                break;
            case 0:// 微信分享
                UMWXHandler wxHandler = new UMWXHandler(mContext, Constants.APP_ID, Constants.WEIXIN_APPSECRET);
                wxHandler.addToSocialSDK();
                WeiXinShareContent weixinContent = new WeiXinShareContent();
                weixinContent.setShareContent(title);
                weixinContent.setTargetUrl(extendUrl);
                weixinContent.setShareImage(shareImage);
                weixinContent.setTitle(content);
                mController.setShareMedia(weixinContent);
                mController.directShare(mContext, SHARE_MEDIA.WEIXIN, snsListener);
                break;
            case 1:// 朋友圈分享
                UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constants.APP_ID, Constants.WEIXIN_APPSECRET);
                wxCircleHandler.setToCircle(true);
                wxCircleHandler.addToSocialSDK();
                CircleShareContent circle = new CircleShareContent(shareImage);
                circle.setShareContent(title);
                circle.setTitle(content);
                circle.setTargetUrl(extendUrl);
                mController.setShareMedia(circle);
                mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, snsListener);
                break;
            default:
                break;
        }
    }

    /**
     * 修复URL
     */
    private void repairExtendUrl() {
        if (!extendUrl.startsWith("https://")&&!extendUrl.startsWith("http://")) {
            Logger.i("拼接前的extendURL："+extendUrl);
            extendUrl="https://"+extendUrl;
            Logger.i("拼接后的extendURL："+extendUrl);
        }
    }

    /**
     * 评论回调
     */
    private void getShareRedBag() {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        mallNetRequest.getShareRedBag(new RequestCallBack<MTShareRedBag>() {
            @Override
            public void onSuccess(ResponseInfo<MTShareRedBag> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        if ("0".equals(responseInfo.result.prize)) {
                            Toast.makeText(mContext, R.string.comment_failed, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "恭喜您获得:" + MoneyUtil.getLeXiangBiNoZero(responseInfo.result.prize) + mContext.getResources().getString(R.string.dikouquan), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(mContext, R.string.comment_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, R.string.comment_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 支付回调
     */
    private void getConsumerProfit() {
        if (paymentNetRequest == null) {
            paymentNetRequest = new PaymentNetRequest(mContext);
        }
        paymentNetRequest.getConsumerProfit(orderId, new RequestCallBack<MTConsumerProfit>() {
            @Override
            public void onSuccess(ResponseInfo<MTConsumerProfit> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        if ("0".equals(responseInfo.result.money)) {
                            Toast.makeText(mContext, R.string.comment_failed, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "恭喜您获得:" + MoneyUtil.getLeXiangBiNoZero(responseInfo.result.money) + "奖励", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(mContext, R.string.comment_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, R.string.net_work_not_available, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
