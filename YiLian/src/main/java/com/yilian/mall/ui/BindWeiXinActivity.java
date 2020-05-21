package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.Constants;

/**
 * 领奖励时绑定微信界面
 * Created by Administrator on 2016/3/28.
 */
public class BindWeiXinActivity extends BaseActivity {
    private IWXAPI api;

    @ViewInject(R.id.img_weixin)
    private ImageView img_weixin;
    @ViewInject(R.id.tv_nike_name)
    private TextView nikename;

    @ViewInject(R.id.rv)
    private RelativeLayout relativeLayout;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_weixin);

        ViewUtils.inject(this);
//
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    //
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String url = intent.getStringExtra("headimgurl");
        String name = intent.getStringExtra("nickname");
        if (name != null) {
            nikename.setText(name);
        }
        if (url != null) {
            imageLoader.displayImage(url, img_weixin, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            img_weixin.setImageBitmap(loadedImage);
                            relativeLayout.setClickable(false);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                case DECODING_ERROR:
                                case NETWORK_DENIED:
                                case OUT_OF_MEMORY:
                                case UNKNOWN:
                                    message = "图片加载错误";
                                    break;
                            }
                        }
                    });
        }
    }

    public void onBack(View view) {
        finish();
    }

    public void bindWeChat(View view) {
        showToast("正在跳转微信,请稍等");
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        }

        if (!api.isWXAppInstalled()) {
            showToast("没有安装微信");
            return;
        }

        api.registerApp(Constants.APP_ID);

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "login_state";
        api.sendReq(req);
        finish();
    }
}
