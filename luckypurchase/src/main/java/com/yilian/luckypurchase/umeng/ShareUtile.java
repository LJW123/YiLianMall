package com.yilian.luckypurchase.umeng;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

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
import com.yilian.luckypurchase.R;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;

public class ShareUtile {
    private String platformType; //分享类型 分享出去的平台 0微信 1朋友圈 2QQ 3QQ空间
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
    /**
     * 这个回调监听，只有QQ回调才能监听到  微信的回调监听在WXEntryActivity里面（实际是在webview的onresume中处理的）
     */
    SocializeListeners.SnsPostListener snsListener = new SocializeListeners.SnsPostListener() {
        @Override
        public void onStart() {
//			Toast.makeText(mContext, "分享开始",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(SHARE_MEDIA arg0, int eCode, SocializeEntity arg2) {
        }
    };
    private UMImage shareImage;

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
        }
        this.orderId = orderId;
        this.callBackUrl = callBackUrl;
    }

    /**
     * 单个分享功能
     * @param context
     * @param content 分享出去的副标题
     * @param title 分享出去的标题
     * @param imageUrl 分享图片url 如果不需要图片 可为空字符串 但需要传该参数
     * @param url 分享的url
     * @param callBackStr 回调的参数 回调成功之后对web注入js方法 appCallback("callBackStr");
     */
    public ShareUtile(Context context, String content, String title, String imageUrl, String url, String callBackStr) {
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.imgPath = imageUrl;
        if (!TextUtils.isEmpty(url)) {
            this.extendUrl = url;
        } else {
            this.extendUrl = PreferenceUtils.readStrConfig(Constants.USERURL, mContext);
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
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        if (!TextUtils.isEmpty(imgPath)) {
            if (imgPath.indexOf("http://") >= 0 || imgPath.indexOf("https://") >= 0) {
                shareImage = new UMImage(mContext, imgPath);
            } else {
                shareImage = new UMImage(mContext, BitmapFactory.decodeFile(imgPath));
            }
        } else {
            shareImage = new UMImage(mContext, R.mipmap.lucky_shareicon);
        }
        mController.setShareMedia(shareImage);
        switch (type) {
        /*case 2:
            SinaShareContent shareContent = new SinaShareContent();
			shareContent.setShareContent(content);
			mController.directShare(mContext, SHARE_MEDIA.SINA, snsListener);
			break;
		case 5:
			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
			mController.directShare(mContext, SHARE_MEDIA.TENCENT, snsListener);
			break;*/
            case 3:// QQ空间分享

                qZoneShare();
                break;
            case 4:// QQ分享
                qqShare();
                break;
            case 0:// 微信分享
//                微信分享才存储该字段，用于分享回调中使用
                PreferenceUtils.writeStrConfig(Constants.APP_CALLBACK_NO_PARAMETER, callBackUrl, mContext);
                weChatShare();
                break;
            case 1:// 朋友圈分享
                //  微信分享才存储该字段，用于分享回调中使用
                PreferenceUtils.writeStrConfig(Constants.APP_CALLBACK_NO_PARAMETER, callBackUrl, mContext);
                weChatCircleShare();
                break;
            default:
                break;
        }
    }

    public void weChatCircleShare() {
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constants.APP_ID, Constants.WEIXIN_APPSECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        CircleShareContent circle = new CircleShareContent(shareImage);
        circle.setShareContent(title);
        circle.setTitle(content);
        circle.setTargetUrl(extendUrl);
        mController.setShareMedia(circle);
        mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, snsListener);
    }

    public void weChatShare() {
        UMWXHandler wxHandler = new UMWXHandler(mContext, Constants.APP_ID, Constants.WEIXIN_APPSECRET);
        wxHandler.addToSocialSDK();
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(title);
        weixinContent.setTargetUrl(extendUrl);
        weixinContent.setShareImage(shareImage);
        weixinContent.setTitle(content);
        mController.setShareMedia(weixinContent);
        mController.directShare(mContext, SHARE_MEDIA.WEIXIN, snsListener);
    }

    public void qqShare() {
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, Constants.QQ_APPID,
                Constants.QQ_APPKEY);
        qqSsoHandler.addToSocialSDK();
        // 设置QQ平台的分享内容
        QQShareContent qqShareContent = new QQShareContent(shareImage);
        qqShareContent.setShareContent(content);
        qqShareContent.setTargetUrl(extendUrl);
        qqShareContent.setShareImage(shareImage);
        qqShareContent.setTitle(title);
        mController.setShareMedia(qqShareContent);
        mController.directShare(mContext, SHARE_MEDIA.QQ, snsListener);
    }

    public void qZoneShare() {
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, Constants.QQ_APPID,
                Constants.QQ_APPKEY);
        qZoneSsoHandler.addToSocialSDK();
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(extendUrl);
        qzone.setTitle(title);
        qzone.setShareImage(shareImage);
        mController.setShareMedia(qzone);
        mController.directShare(mContext, SHARE_MEDIA.QZONE, snsListener);
    }


}
