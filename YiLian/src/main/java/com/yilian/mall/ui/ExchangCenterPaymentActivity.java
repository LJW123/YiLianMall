package com.yilian.mall.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 兑换中心交易确认页面
 */
public class ExchangCenterPaymentActivity extends BaseActivity {
	
	@ViewInject(R.id.tv_back)
	TextView mTvTitle;
	
	@ViewInject(R.id.webView_pay)
	WebView mWebView;
	
	private String mUrl="";
	private Map<String, String> params=new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exchange_center_payment);
		ViewUtils.inject(this);
		init();
		registerEvents();
	}
	
	private void init() {
        String userAgentString = mWebView.getSettings().getUserAgentString();
//        修改WebView的userAgent  在获取到的后面拼接 空格lefen_native_ android
        mWebView.getSettings().setUserAgentString(userAgentString+" lefen_native_ android");
		WebSettings settings= mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl, params);
	}

	private void registerEvents() {
		mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				startMyDialog();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				stopMyDialog();
			}
		});
		
	}
}
