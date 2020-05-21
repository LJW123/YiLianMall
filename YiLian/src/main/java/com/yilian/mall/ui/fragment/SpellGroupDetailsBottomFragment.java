package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.adapter.JPCommEvaluateAdapter;
import com.yilian.mall.entity.EvaluateList;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.ui.JPAllEvaListActivity;
import com.yilian.mall.ui.SpellGroupDetailsActivity;
import com.yilian.mall.widgets.CustListView;
import com.yilian.mall.widgets.CustWebView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/5/12 0012.
 */
public class SpellGroupDetailsBottomFragment extends BaseFragment {

    private RadioGroup rgContent;
    private RadioButton rbGoodsInfo;
    private RadioButton rbEvaluate;
    private CustWebView webView;
    private CustListView listView;
    private ImageView ivNothing;
    private SpellGroupDetailsActivity activity;
    private ProgressBar progressBar;
    private MallNetRequest netRequest;
    private int page;
    private ArrayList<EvaluateList.Evaluate> evaluateList = new ArrayList<>();
    private View moreView;
    private TextView moreTv;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_group_details_bottom, container, false);
        rgContent = (RadioGroup) view.findViewById(R.id.rg_content);
        rbGoodsInfo = (RadioButton) view.findViewById(R.id.rb_goods_info);
        rbGoodsInfo.setChecked(true);

        rbEvaluate = (RadioButton) view.findViewById(R.id.rb_goods_evaluate);
        progressBar = (ProgressBar) view.findViewById(R.id.sender_list_progress);
        webView = (CustWebView) view.findViewById(R.id.webview);
        String userAgentString = webView.getSettings().getUserAgentString();
//        修改WebView的userAgent  在获取到的后面拼接 空格lefen_native_ android
        webView.getSettings().setUserAgentString(userAgentString + " lefen_native_ android");
        listView = (CustListView) view.findViewById(R.id.listView);
        ivNothing = (ImageView) view.findViewById(R.id.iv_nothing);
        activity = (SpellGroupDetailsActivity) getActivity();
        page = 0;
        initMore();
        return view;
    }

    private void initMore() {
        moreView = View.inflate(mContext, R.layout.listview_footer, null);
        moreTv = (TextView) moreView.findViewById(R.id.tv_more);
    }

    public void initWiew(int which) {
        rgContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_goods_info:
                        ivNothing.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        activity.which = 0;
                        initWebView(activity.goodsInfoUrl);
                        break;
//                    case R.id.rb_goods_parameter:
//                        webView.setVisibility(View.VISIBLE);
//                        initWebView(activity.goodsParamenterUrl);
//                        break;
                    case R.id.rb_goods_evaluate:
                        activity.which = 2;
                        webView.setVisibility(View.GONE);
                        initListData();
                        break;
                }
            }
        });
        //activity中记录的下滑到哪个页面
        switch (which) {
            case 0:
                webView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                initWebView(activity.goodsInfoUrl);
                activity.which = 0;
                break;
//            case 1:
//                listView.setVisibility(View.GONE);
//                webView.setVisibility(View.VISIBLE);
//                initWebView(activity.goodsParamenterUrl);
//                activity.which = 1;
//                break;
            case 2:
                listView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                initListData();
                activity.which = 2;
                break;
        }
    }

    private void initListData() {
        if (netRequest == null) {
            netRequest = new MallNetRequest(activity);
        }
        startMyDialog();
        netRequest.getEvaluateList(activity.goodsIndex, activity.filialeId, page, new RequestCallBack<EvaluateList>() {

            private JPCommEvaluateAdapter adapter;

            @Override
            public void onSuccess(ResponseInfo<EvaluateList> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<EvaluateList.Evaluate> evaluates = responseInfo.result.list;
                        ivNothing.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        if (null != evaluates && evaluates.size() > 0) {
                            if (page > 1) {
                                evaluateList.addAll(evaluates);
                            } else {
                                evaluateList.clear();
                                evaluateList.addAll(evaluates);
                                if (null == adapter) {
                                    adapter = new JPCommEvaluateAdapter(activity, evaluateList);
                                }
                                listView.setAdapter(adapter);
                            }
                            adapter.notifyDataSetChanged();

                            moreTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, JPAllEvaListActivity.class);
                                    intent.putExtra("goods_id", activity.goodsId);//从限时抢购跳转
                                    intent.putExtra("filiale_id", activity.filialeId);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            if (page > 0) {
                                showToast("暂无更多");
                            } else {
                                ivNothing.setVisibility(View.VISIBLE);
                            }
                        }

                        break;
                    default:
                        Logger.i(getClass().getSimpleName() + responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listView.setVisibility(View.GONE);
                ivNothing.setVisibility(View.VISIBLE);
                stopMyDialog();
            }
        });

    }

    @Override
    protected void loadData() {

    }

    private void initWebView(String url) {
        Logger.i("拼团的webView  Url  " + url);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setDownloadListener(new SpellWebViewDownLoadListener());
    }

    class SpellWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


}
