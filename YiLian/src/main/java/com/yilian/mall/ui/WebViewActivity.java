package com.yilian.mall.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareLoveMsgActivity;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.widgets.PopupMenu;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JsStringUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.widget.X5WebView;
import com.yilian.networkingmodule.entity.WebShareEntity;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 显示网页的Activity
 */
public class WebViewActivity extends BaseActivity implements Serializable, View.OnClickListener {

    private static final int ICON_TYPE_SHARE = 0;
    private static final int ICON_TYPE_TEXT = 1;
    private static final int ICON_TYPE_TWO_ICON = 2;
    public static int TITLE_THEME_DEFAULT = 0;
    public static int TITLE_THEME_ALL_SCREEN = 1;
    public X5WebView webView;
    ViewGroup mRoot;
    private ProgressBar progressBar;
    private ArrayList<String> titles = new ArrayList<>();
    private boolean isRecharge;//判断是否是充值页面，如果是充值页面则需要特殊处理返回按钮和物理返回按键
    private boolean isCallBack;
    private boolean hongbao;
    private ImageView imageViewRefresh;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tv_right;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tv_right2;
    private FrameLayout v3Layout;
    private String shareTitle = "益联益家";
    private String shareUrl;
    private String shareContent;
    private String imgPath = "";
    private String titleText;
    private View header;
    private int titleTheme = TITLE_THEME_DEFAULT;
    private TitleThemeBroadcastReceiver titleThemeBroadcastReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int iconTheme;
    private int iconType;
    private IconThemeBroadcastReceiver iconThemeBroadcastReceiver;
    private IntentFilter titleFilter;
    private IntentFilter iconFilter;
    private int themeType;
    private int alpha;
    private boolean isDefaultIcon = true;
    private FrameLayout.LayoutParams headerLayoutParams;
    /**
     * header最初的高度，还原头部样式时使用
     */
    private int headerOriginalHeight;
    private UmengDialog mShareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senderlist);
        isRecharge = getIntent().getBooleanExtra("isRecharge", false);
        isCallBack = getIntent().getBooleanExtra("isCallBack", false);
        initView();
        initData();
        initListener();
        titleThemeBroadcastReceiver = new TitleThemeBroadcastReceiver();
        iconThemeBroadcastReceiver = new IconThemeBroadcastReceiver();
        titleFilter = new IntentFilter("com.yilian.webview.titlethemebroadcastreceiver");
        iconFilter = new IntentFilter("com.yilian.webview.iconthemebroadcastreceiver");
    }

    @Override
    protected void onResume() {
        super.onResume();
//iOS菜鸟因为title颜色无法更好实现，提出需求，
// 页面内逻辑跳转是通过js再次打开一个WebViewActivity，
// 所以再次打开的页面可能会改变title的icon颜色，导致返回上一级时icon颜色发生变化，此处需要重新设置一下title
        setTitleTheme();
        registerReceiver(titleThemeBroadcastReceiver, titleFilter);
        registerReceiver(iconThemeBroadcastReceiver, iconFilter);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                    //这里真正实现了Android调用JS方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hongbao = sp.getBoolean(Constants.SPKEY_HONG_BAO, false);
                            Logger.i("接收的奖励sp:" + hongbao);
                            if (hongbao) {
                                Logger.i("回调了拆奖励");
                                String callBack = "javascript:appPayCallback()";//网页使用原生里面的第三方支付方式，支付成功回调
                                webView.loadUrl(callBack);
                                sp.edit().putBoolean(Constants.SPKEY_HONG_BAO, false).commit();//回调拆奖励后，把该值设置为false，以免下次进来再次调用；
                            }

                            String callBackParams = PreferenceUtils.readStrConfig(Constants.APP_CALLBACK_PARAMETER, mContext, "");
                            if (!TextUtils.isEmpty(callBackParams)) {
                                webView.loadUrl("javascript:appCallback("+callBackParams+")");
                            }
                            String callBackUrl = PreferenceUtils.readStrConfig(Constants.APP_CALLBACK_NO_PARAMETER, mContext, "");
                            if (!TextUtils.isEmpty(callBackUrl)) {
                                if ("1".equals(callBackUrl)) {
                                    webView.loadUrl("javascript:appCallback()");
                                } else if ("0".equals(callBackUrl)) {
                                    //不做任何事情
                                } else {//此时callBackURL是一个新的url
                                    //打开url
                                    Intent intent = new Intent(mContext, WebViewActivity.class);
                                    intent.putExtra("url", callBackUrl);
                                    mContext.startActivity(intent);
                                }
                            }
//分享回调
//                            webView.loadUrl("javascript:appCallback(" + com.yilian.mylibrary.PreferenceUtils.readStrConfig(SHARE_CALLBACK, mContext) + ")");
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
//                    重置该字段
                    PreferenceUtils.writeStrConfig(Constants.APP_CALLBACK_NO_PARAMETER, "", mContext);
                    PreferenceUtils.writeStrConfig(Constants.APP_CALLBACK_PARAMETER, "", mContext);
                }
            }
        }.start();

    }

    //    boolean isFirst = true;
    private void setTitleTheme() {
        if (titleTheme == TITLE_THEME_DEFAULT) {
            setDefaultTitleTheme();
        } else if (titleTheme == TITLE_THEME_ALL_SCREEN) {
            FrameLayout.LayoutParams swipeRefreshLayoutLayoutParams = (FrameLayout.LayoutParams) swipeRefreshLayout.getLayoutParams();
            swipeRefreshLayoutLayoutParams.setMargins(0, 0, 0, 0);
            FrameLayout.LayoutParams headerLayoutParams = (FrameLayout.LayoutParams) header.getLayoutParams();
            if (alpha == 0 && themeType == 1) {
                StatusBarUtils.setStatusBarColor(this, R.color.color_66000000, false);
                headerLayoutParams.height += StatusBarUtils.getStatusBarHeight(mContext);
                header.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
                Logger.i("设置statusBar走了这里1");
//                if (isFirst) {
//                    StatusBarUtils.setStatusBarColor(this, R.color.color_66000000, false);
//                    headerLayoutParams.height += StatusBarUtils.getStatusBarHeight(mContext);
//                    header.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
//                    Logger.i("设置statusBar走了这里1");
//                    isFirst = false;
//                }

            } else {
                StatusBarUtils.setStatusBarColor(this, R.color.color_66000000, false);
                Logger.i("设置statusBar走了这里2");
                headerLayoutParams.setMargins(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);
            }

//            设置为白色透明，而不是直接透明色，是为了处理可滑动页面滑动过程中由透明变为不透明的实现
//            header.setBackgroundColor(Color.WHITE);
            setMyViewTitleColor(Color.WHITE, Color.WHITE);
            header.getBackground().setAlpha(0);
        }
    }

    private void setDefaultTitleTheme() {
        FrameLayout.LayoutParams swipeRefreshLayoutLayoutParams = (FrameLayout.LayoutParams) swipeRefreshLayout.getLayoutParams();
        swipeRefreshLayoutLayoutParams.setMargins(0, header.getLayoutParams().height, 0, 0);
        swipeRefreshLayout.setLayoutParams(swipeRefreshLayoutLayoutParams);
    }

    private void setMyViewTitleColor(int color, int backgroundColor) {
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        Drawable drawableBack = ContextCompat.getDrawable(mContext, R.mipmap.merchant_web_v3back);
        drawableBack.setColorFilter(porterDuffColorFilter);
        v3Back.setImageDrawable(drawableBack);
        Drawable drawableFinish = ContextCompat.getDrawable(mContext, R.mipmap.login_web_back);
        drawableFinish.setColorFilter(porterDuffColorFilter);
        v3Left.setImageDrawable(drawableFinish);
        Drawable drawablePoints = ContextCompat.getDrawable(mContext, R.mipmap.v3_more_web_bottom);
        drawablePoints.setColorFilter(porterDuffColorFilter);
        v3Share.setImageDrawable(drawablePoints);
        v3Title.setTextColor(color);
        tv_right.setTextColor(color);
        tv_right2.setTextColor(color);
        header.setBackgroundColor(backgroundColor);
    }

    @Override
    protected void onPause() {
        if (titleThemeBroadcastReceiver != null) {
            unregisterReceiver(titleThemeBroadcastReceiver);
        }
        if (iconThemeBroadcastReceiver != null) {
            unregisterReceiver(iconThemeBroadcastReceiver);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.clearView();
            mRoot.removeView(webView);
        }
        webView = null;
        super.onDestroy();
        if (mShareDialog != null && mShareDialog.isShowing()) {
            mShareDialog.dismiss();
        }
//        if (titleThemeBroadcastReceiver != null) {
//            unregisterReceiver(titleThemeBroadcastReceiver);
//        }
//        if (iconThemeBroadcastReceiver != null) {
//            unregisterReceiver(iconThemeBroadcastReceiver);
//        }
    }

    /**
     * 重置title的样式，目前有bug,对于需要为StatusBar设置颜色时，多次刷新，为StatusBar设置的颜色会叠加，导致透明效果消失
     */
    void resetTitleTheme() {
        header.setPadding(0, 0, 0, 0);
        header.getLayoutParams().height = headerOriginalHeight;
        FrameLayout.LayoutParams swipeRefreshLayoutLayoutParams = (FrameLayout.LayoutParams) swipeRefreshLayout.getLayoutParams();
        swipeRefreshLayoutLayoutParams.setMargins(0, 0, 0, 0);
        swipeRefreshLayout.setLayoutParams(swipeRefreshLayoutLayoutParams);
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        屏蔽下拉刷新
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        header = findViewById(R.id.senderlist_header2);
        headerLayoutParams = (FrameLayout.LayoutParams) header.getLayoutParams();
        headerOriginalHeight = headerLayoutParams.height;
        setTitleTheme();
        imageViewRefresh = (ImageView) findViewById(R.id.iv_refresh);
        mRoot = (ViewGroup) findViewById(R.id.root);
        progressBar = (ProgressBar) findViewById(R.id.sender_list_progress);
        webView = (X5WebView) findViewById(R.id.sender_list_webview);
        String userAgentString = webView.getSettings().getUserAgentString();
//        修改WebView的userAgent  在获取到的后面拼接 空格lefen_native_ android
        webView.getSettings().setUserAgentString(userAgentString + " lefen_native_ android");
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setOnClickListener(this);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setOnClickListener(this);

        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);

        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setOnClickListener(this);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setOnClickListener(this);
        v3Share.setVisibility(View.VISIBLE);
        tv_right2 = (TextView) findViewById(R.id.tv_right2);
        tv_right2.setOnClickListener(this);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setOnClickListener(this);

        initIconColor();
    }

    public int getNavitaionBarHeight() {
        int height = header.getHeight();
        if (height == 0) {
            showToast("高度为0");
        }
        return height;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.i("keyCode:" + keyCode + "  webView.canGoBack():" + webView.canGoBack() + "  isRecharge:" + !isRecharge);
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() && !isRecharge) {
            Logger.i("titlesSize1:" + titles.size());
            if (titles.size() > 1) {
                titles.remove(titles.size() - 1);
                titleText = titles.get(titles.size() - 1);
                v3Title.setText(titleText);
                webView.goBack();
            } else {
                finish();
            }
            return true;
        } else {
            Logger.i("KeyCode:" + keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        String callBack = "javascript:appCancelMusic()";//网页使用原生里面的第三方支付方式，支付成功回调
        if (webView != null) {
            webView.loadUrl(callBack);
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == WelfareLoveMsgActivity.TO_PERSON_DETAIL) {
            setResult(WelfareLoveMsgActivity.TO_PERSON_DETAIL);
            finish();
        }
        if (requestCode == 1 && resultCode == 1) {
            //重新刷新页面
            initData();
        }
    }

    private void initData() {
        String url = getIntent().getStringExtra("url");

        Logger.i("打开的Url" + url);
        AndroidJs obj = new AndroidJs(this);
        webView.addJavascriptInterface(obj, "App");
        webView.loadUrl(url);
        shareUrl = url;
        WebSettings webViewSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            webViewSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        Logger.i("StorageEnabled:"+webViewSettings.getDomStorageEnabled());
        webViewSettings.setJavaScriptEnabled(true);
//        取消网页缓存
        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    initIconColor();
                    webView.loadUrl(url);
                    shareUrl = url;
                    Logger.i("再次打开的URL：" + url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                处理部分机型https证书验证不通过时，页面空白问题
                sslErrorHandler.proceed();
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack() && !isRecharge) {//如果是银联充值页面，点击返回键时会下标越界,所以加上非充值页面判断
                    if (titles.size() > 1) {
                        titles.remove(titles.size() - 1);
                        titleText = titles.get(titles.size() - 1);
                        v3Title.setText(titleText);
                    }
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                swipeRefreshLayout.setRefreshing(false);//网页加载过程中不显示该控件的动画效果，只在下拉时显示
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleText = title;
                v3Title.setText(titleText);
                shareTitle = title;
                titles.add(title);
            }
        });
        webView.setDownloadListener(new MyWebViewDownLoadListener());
    }

    /**
     * 用于重置图片颜色
     * 图片颜色改变后，改变的是图片本身的颜色，再次使用需要重置颜色
     */
    private void initIconColor() {
        Logger.i("WebView:初始化了图标和背景颜色");
        setMyViewTitleColor(ContextCompat.getColor(mContext, R.color.color_333), Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                Logger.i("我点了返回");
                Logger.i("titlesSize2:" + titles.size());
                if (isRecharge) {//如果是充值页面，则点击顶部返回按钮，则直接关闭页面
                    finish();
                    return;
                }
                if (titles.size() > 1 && webView.canGoBack()) {
                    titles.remove(titles.size() - 1);
                    titleText = titles.get(titles.size() - 1);
                    v3Title.setText(titleText);
                    webView.goBack();
                } else {
                    WebViewActivity.this.finish();
                }
                break;
            case R.id.tv_right:
                if (iconType == ICON_TYPE_TWO_ICON) {
                    webView.loadUrl("javascript:appCallTitle()");
                }
                break;
            case R.id.v3Left:
                finish();
                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Share:
                if (isDefaultIcon) {
                    menuShow();
                } else {
                    webView.loadUrl("javascript:appCallMore()");
                }
                break;
            case R.id.v3Shop:
                webView.loadUrl(Constants.JS_APPSHOWRULER);
                break;
            default:
                break;
        }
    }

    /**
     * 右上角更多
     */
    private void menuShow() {
        final PopupMenu popupMenu = new PopupMenu(this);
        popupMenu.showLocation(R.id.v3Share);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            startActivity(new Intent(WebViewActivity.this, InformationActivity.class));
                        } else {
                            startActivity(new Intent(WebViewActivity.this, LeFenPhoneLoginActivity.class));
                        }
                        break;
                    case ITEM2:
                        Intent intentHome = new Intent(WebViewActivity.this, JPMainActivity.class);
                        intentHome.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        startActivity(intentHome);
                        break;
                    case ITEM3:
//                        先获取分享内容，在调用分享sdk
                        webView.evaluateJavascript("javascript:appCallShareParameter('" + titleText + "')", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String callbackStr) {
//                返回值为json字符串 {"title":"益联益家","content":"我是大家猜呀","imageUrl":"","url":""}
                                Logger.i("js返回内容：" + callbackStr);
//                                防止js返回数据不是约定的格式
                                try {
                                    if (callbackStr != null) {
                                        WebShareEntity webShareEntity = new Gson().fromJson(JsStringUtil.getJsonString(callbackStr), WebShareEntity.class);
                                        Logger.i("JS走了这里2 webShareEntity: " + webShareEntity);
                                        if (webShareEntity != null) {
                                            Logger.i("JS走了这里" + webShareEntity.toString());
                                            shareTitle = webShareEntity.title;
                                            shareUrl = webShareEntity.url;
                                            shareContent = webShareEntity.content;
                                            imgPath = webShareEntity.imageUrl;
                                        }
                                    }
                                } catch (JsonSyntaxException e) {
                                    Logger.i("JS走了这里 JsonSyntaxException:" + e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });
                        shareUrl();
                        break;
                    case ITEM4:
                        Intent intentUser = new Intent(WebViewActivity.this, JPMainActivity.class);
                        intentUser.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        startActivity(intentUser);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 分享网页
     */
    public void shareUrl() {
        Logger.i("2018年1月4日 15:22:07-" + shareTitle);
        Logger.i("2018年1月4日 15:22:07-" + shareContent);
        Logger.i("2018年1月4日 15:22:07-" + imgPath);
        Logger.i("2018年1月4日 15:22:07-" + shareUrl);
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            shareTitle, shareContent,
                            imgPath,
                            shareUrl).share();
                }
            });

        }
        mShareDialog.show();
    }

    private void initListener() {

        webView.setOnScrollChangedCallback(new X5WebView.OnScrollChangedCallback() {
            int scrollDistance = 0;

            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                scrollDistance += (t - oldt);
                Logger.i("webview滑动距离：" + scrollDistance + "oldt:" + oldt + " t:" + t);
                if (scrollDistance == 0) {
//                    屏蔽下拉刷新
//                    swipeRefreshLayout.setEnabled(true);
                } else {
//                    swipeRefreshLayout.setEnabled(false);
                }
                switch (themeType) {
                    case 0:
                        break;
                    case 1:
                        if (alpha == 0) {
                            //                        随着页面滑动，改变title的透明度
                            Logger.i("这里正在滑动1");
                            if (scrollDistance == 0) {
                                setMyViewTitleColor(Color.WHITE, Color.WHITE);
                                header.getBackground().setAlpha(0);
                                header.setAlpha(1);
                            } else if (scrollDistance < 255) {
                                Logger.i("这里正在滑动2");
                                setMyViewTitleColor(ContextCompat.getColor(mContext, R.color.color_333), Color.WHITE);
                                header.setAlpha((float) (scrollDistance * 0.1 / 255));
                            } else {
                                Logger.i("这里正在滑动3");
                                header.setAlpha(1);
                            }
                        } else {

                        }

                        break;
                    default:
                        break;
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetTitleTheme();
                webView.reload();
            }
        });
        imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });
    }

    class IconThemeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            iconType = com.yilian.mylibrary.NumberFormat.convertToInt(intent.getStringExtra("iconType"), 0);
            String rightText = intent.getStringExtra("rightText");
            isDefaultIcon = intent.getBooleanExtra("defaul", true);
//                    TODO 处理iconType逻辑
            if (iconType == ICON_TYPE_SHARE) {
                //                            只显示更多图标
                v3Share.setVisibility(View.VISIBLE);
                v3Shop.setVisibility(View.GONE);
                tv_right.setVisibility(View.GONE);
            } else if (iconType == ICON_TYPE_TEXT) {
                //                            显示特殊图标和更多图标
                v3Share.setVisibility(View.VISIBLE);
                v3Shop.setVisibility(View.VISIBLE);
                if (!isDefaultIcon) {
                    int drawableResId = getResources().getIdentifier(rightText, "drawable", getBaseContext().getPackageName());
                    Drawable drawable = getDrawable(drawableResId);
                    v3Shop.setImageResource(drawableResId);
                }
                tv_right.setVisibility(View.GONE);
            } else if (iconType == ICON_TYPE_TWO_ICON) {
                //                            只显示文字
                v3Share.setVisibility(View.GONE);
                v3Shop.setVisibility(View.GONE);
                tv_right.setVisibility(View.VISIBLE);
                tv_right.setText(rightText);
            }
        }
    }

    class TitleThemeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            改变图标颜色前，先初始化图标颜色
            titleTheme = intent.getIntExtra("titleTheme", 0);
            Logger.i("收到了theme广播 titleTheme:" + titleTheme);
            switch (titleTheme) {
                case 0:
                    Logger.i("WebView: 修改了图标和背景颜色");
                    String backgroundColor = intent.getStringExtra("backgroundColor");
                    int themeColor = ContextCompat.getColor(mContext, R.color.white);
                    if (backgroundColor != null) {
                        themeColor = Color.parseColor("#" + backgroundColor);
                    }
                    String tinColor = intent.getStringExtra("tinColor");
                    int iconColor = ContextCompat.getColor(mContext, R.color.color_333);
                    if (tinColor != null) {
                        iconColor = Color.parseColor("#" + tinColor);
                        Logger.i("转变后的iconColor:" + iconColor);
                    }
                    Logger.i("修改的iconColor:" + iconColor);
                    setMyViewTitleColor(iconColor, themeColor);
                    break;
                case 1:
                    alpha = NumberFormat.convertToInt(intent.getStringExtra("alpha"), -1);
                    themeType = NumberFormat.convertToInt(intent.getStringExtra("themeType"), -1);
                    break;
                default:
                    break;
            }
            setTitleTheme();
        }

    }

    class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
