package com.yilian.mylibrary.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

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
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.R;

import org.greenrobot.eventbus.EventBus;

public class ShareUtile {
    private final static String strPreferName = "UserInfor";
    private String callBackUrl;
    private int type = 0;
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
                postEvent();

//
            } else {
//				Toast.makeText(mContext,"分享失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private String content = "";
    private String title = "益联益家";
    private String extendUrl = "";
    private String imgPath = "";
    private UMSocialService mController;
    private Context mContext;
    private String orderId;
    private String oneBuyPrizeShareId;
    private boolean isNotFromOneBuyActivity;


    public ShareUtile(Context mContext) {
        this.mContext = mContext;
    }

    public ShareUtile(Context mContext, int type, String content, String imgPath, String extendUrl) {
        this.mContext = mContext;
        this.type = type;
        this.content = content;
        this.imgPath = imgPath;
        if (!TextUtils.isEmpty(extendUrl)) {
            this.extendUrl = extendUrl;
        } else {
            SharedPreferences preferences = mContext.getSharedPreferences(strPreferName, 0);
            this.extendUrl = preferences.getString(Constants.USERURL, "");
        }
    }

    /**
     * andoridJS调用的分享方法
     *
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
            SharedPreferences preferences = mContext.getSharedPreferences(strPreferName, 0);
            this.extendUrl = preferences.getString(Constants.USERURL, "");
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
            SharedPreferences preferences = mContext.getSharedPreferences(strPreferName, 0);
            this.extendUrl = preferences.getString(Constants.USERURL, "");
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
            SharedPreferences preferences = mContext.getSharedPreferences(strPreferName, 0);
            this.extendUrl = preferences.getString(Constants.USERURL, "");
        }
        this.orderId = orderId;
        this.callBackUrl = callBackUrl;
    }

    /**
     * 分享成功到页面的回调
     */
    private void postEvent() {
        switch (type) {
            case 3:// QQ空间分享
                EventBus.getDefault().post("Qzone");
                break;
            case 4:// QQ分享
                EventBus.getDefault().post("QQ");
                break;

        }
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
                shareImage = new UMImage(mContext, R.mipmap.library_module_share_red_icon);
            } else {
                Logger.i("2017年11月27日 09:57:32-走了普通");
                shareImage = new UMImage(mContext, R.mipmap.library_module_shareicon);
            }
        }
        mController.setShareMedia(shareImage);
        Logger.i("分享Url:" + extendUrl);
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
        if (!extendUrl.startsWith("https://") && !extendUrl.startsWith("http://")) {
            Logger.i("拼接前的extendURL：" + extendUrl);
            extendUrl = "https://" + extendUrl;
            Logger.i("拼接后的extendURL：" + extendUrl);
        }
    }

    /**
     * 纯图片分享
     *
     * @param bitmap
     */
    public void shares(int type, Bitmap bitmap) {
        // 分享 开始
        mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
        UMImage shareImage;
        shareImage = new UMImage(mContext, R.mipmap.library_module_shareicon);
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        mController.setShareMedia(shareImage);
        switch (type) {
            case 3:// QQ空间分享
                QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, Constants.QQ_APPID,
                        Constants.QQ_APPKEY);
                qZoneSsoHandler.addToSocialSDK();
                QZoneShareContent qzone = new QZoneShareContent();
                qzone.setShareImage(new UMImage(mContext, bitmap));
                mController.setShareMedia(qzone);
                mController.directShare(mContext, SHARE_MEDIA.QZONE, snsListener);
                break;
            case 4:// QQ分享
                UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, Constants.QQ_APPID,
                        Constants.QQ_APPKEY);
                qqSsoHandler.addToSocialSDK();
                QQShareContent qqShareContent = new QQShareContent();
                qqShareContent.setShareImage(new UMImage(mContext, bitmap));
                mController.setShareMedia(qqShareContent);
                mController.directShare(mContext, SHARE_MEDIA.QQ, snsListener);
                break;
            case 0:// 微信分享
                UMWXHandler wxHandler = new UMWXHandler(mContext, Constants.APP_ID, Constants.WEIXIN_APPSECRET);
                wxHandler.addToSocialSDK();
                WeiXinShareContent weixinContent = new WeiXinShareContent();
                weixinContent.setShareImage(new UMImage(mContext, bitmap));
                weixinContent.setTitle(content);
                mController.setShareMedia(weixinContent);
                mController.directShare(mContext, SHARE_MEDIA.WEIXIN, snsListener);
                break;
            case 1:// 朋友圈分享
                UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constants.APP_ID, Constants.WEIXIN_APPSECRET);
                wxCircleHandler.setToCircle(true);
                wxCircleHandler.addToSocialSDK();
                CircleShareContent circle = new CircleShareContent();
                circle.setShareImage(new UMImage(mContext, bitmap));
                mController.setShareMedia(circle);
                mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, snsListener);
                break;
            default:
                break;
        }
    }
}
