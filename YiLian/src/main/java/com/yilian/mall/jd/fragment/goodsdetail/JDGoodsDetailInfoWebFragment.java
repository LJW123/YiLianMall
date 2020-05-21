package com.yilian.mall.jd.fragment.goodsdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.jd.view.ItemWebView;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.ScreenUtils;


/**
 * 商品详情的Fragment
 */
public class JDGoodsDetailInfoWebFragment extends JPBaseFragment {
    public static final String TAG = "GoodsInfoWebFragment";
    public ItemWebView wv_detail;
    private WebSettings webSettings;
    private LayoutInflater inflater;
    private String htmlText;

    static JDGoodsDetailInfoWebFragment newInstance(String htmlText) {
        JDGoodsDetailInfoWebFragment goodsInfoWebFragment = new JDGoodsDetailInfoWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, htmlText);
        goodsInfoWebFragment.setArguments(bundle);
        return goodsInfoWebFragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.jd_fragment_item_info_web, null);
        Bundle arguments = getArguments();
        htmlText = arguments.getString(TAG);
        initWebView(rootView);
        return rootView;
    }

    public void initWebView(View rootView) {
        wv_detail = rootView.findViewById(R.id.wv_detail);
        wv_detail.setFocusable(false);
        webSettings = wv_detail.getSettings();
//        隐藏缩放按钮
        webSettings.setDisplayZoomControls(false);
        wv_detail.setWebViewClient(new GoodsDetailWebViewClient());
        wv_detail.setVerticalScrollBarEnabled(false);
        Logger.i("webData :  " + htmlText);
        wv_detail.loadDataWithBaseURL("http:", htmlText, "text/html", "UTF-8", null);
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        if (screenWidth == 720) {
            wv_detail.setInitialScale(112);
        } else if (screenWidth == 1080) {
            wv_detail.setInitialScale(168);
        } else if (screenWidth == 1440) {
            wv_detail.setInitialScale(225);
        }
//        wv_detail.loadDataWithBaseURL(null, testHtml, "text/html", "UTF-8", null);
    }

    public void scrollToTop() {
        wv_detail.scrollTo(0, 0);
    }

    private class GoodsDetailWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webSettings.setBlockNetworkImage(false);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }
    }
}
