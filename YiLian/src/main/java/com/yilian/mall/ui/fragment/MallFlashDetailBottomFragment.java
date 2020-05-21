package com.yilian.mall.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.adapter.JPCommEvaluateAdapter;
import com.yilian.mall.entity.EvaluateList;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.ui.JPAllEvaListActivity;
import com.yilian.mall.ui.MallFlashSaleDetailActivity;
import com.yilian.mall.widgets.CustListView;
import com.yilian.mall.widgets.CustWebView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;

/**
 * Created by Ray_L_Pain on 2016/11/1 0001.
 */

public class MallFlashDetailBottomFragment extends BaseFragment{
    private Context context;
    private MallFlashSaleDetailActivity activity;
    View rootView;
    private RadioGroup radioGroup;
    private RadioButton buttonOne;
    private RadioButton buttonThree;
    private ScrollView sv;
    private ProgressBar progressBar;
    private CustWebView webView1;
    private CustListView listView;
    View moreView;
    private TextView moreTv;
    private ImageView ivNoData;
    public ImageView iv_go_top;
    private JPCommEvaluateAdapter adapter;
    private String urlOne;
    private MallNetRequest evaRequest;
    private int evaPage = 0;
    ArrayList<EvaluateList.Evaluate> evaList = new ArrayList<>();


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_commdetail_bottom, null);
        context = getContext();
        activity = (MallFlashSaleDetailActivity) getActivity();

        initView(activity.which);
        initMore();
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    public void initView(int which) {
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_one:
                        webView1.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        activity.which = 0;
                        initWebView(webView1,urlOne);
                        break;
                    case R.id.rb_three:
                        webView1.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        initListView();
                        activity.which = 2;
                        break;
                }
            }
        });
        buttonOne = (RadioButton) rootView.findViewById(R.id.rb_one);
        RadioButton buttonTwo= (RadioButton) radioGroup.findViewById(R.id.rb_two);
        buttonTwo.setVisibility(View.GONE);
        buttonThree = (RadioButton) rootView.findViewById(R.id.rb_three);
        sv = (ScrollView) rootView.findViewById(R.id.sv);
        progressBar = (ProgressBar) rootView.findViewById(R.id.sender_list_progress);
        webView1 = (CustWebView) rootView.findViewById(R.id.webView1);
      //        修改WebView的userAgent  在获取到的后面拼接 空格lefen_native_ android
        String userAgentString = webView1.getSettings().getUserAgentString();

        webView1.getSettings().setUserAgentString(userAgentString+" lefen_native_ android");
        WebView webView2= (WebView) rootView.findViewById(R.id.webView2);
        webView2.setVisibility(View.GONE);
        listView = (CustListView) rootView.findViewById(R.id.listView);
        ivNoData = (ImageView) rootView.findViewById(R.id.iv_no_data);

        urlOne = activity.urlOne;//请求商品详情的时候获取的数据之后拼接

        Logger.i("限时抢购详情  oneUrl  "+urlOne);

        //默认加载
        switch (which){
            case 0:
                radioGroup.check(R.id.rb_one);
                progressBar.setVisibility(View.VISIBLE);
                webView1.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                initWebView(webView1, urlOne);
                break;
            case 2:
                webView1.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                activity.which = 2;
                break;
        }


//        iv_go_top = (ImageView) rootView.findViewById(R.id.iv_go_top);
//        iv_go_top.setVisibility(View.VISIBLE);
//        iv_go_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iv_go_top.setVisibility(View.INVISIBLE);
//            }
//        });
    }


    private void initMore(){
        moreView = View.inflate(context, R.layout.listview_footer, null);
        moreTv = (TextView) moreView.findViewById(R.id.tv_more);
    }

    private void initWebView(WebView webView, String url){
        webView.loadUrl(url);
        Logger.i("本地限时抢购   "+url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(LOAD_CACHE_ELSE_NETWORK);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
        webView.setDownloadListener(new MyWebViewDownLoadListener());
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

    private void initListView(){
        if(evaRequest == null){
            evaRequest = new MallNetRequest(context);
        }
        if(evaList.size() != 0){
            return;
        }
        evaRequest.getEvaluateList(activity.picId, activity.filialeId, evaPage, new RequestCallBack<EvaluateList>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<EvaluateList> responseInfo) {
                Logger.i("pic_id: "+activity.picId);
                Logger.i("filialeId:"+activity.filialeId);
                Logger.i("evaPage:"+evaPage);
                stopMyDialog();
                switch (responseInfo.result.code){
                    case 1:
                        if(responseInfo.result.list.size() == 0 || responseInfo.result.list == null){
                            listView.setVisibility(View.GONE);
                            ivNoData.setVisibility(View.VISIBLE);
                        } else {
                            evaList.addAll(responseInfo.result.list);
                            adapter = new JPCommEvaluateAdapter(context, evaList);
                            listView.addFooterView(moreView);
                            moreTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, JPAllEvaListActivity.class);
                                    intent.putExtra("goods_id", activity.picId);//从限时抢购跳转
                                    intent.putExtra("filiale_id", activity.filialeId);
                                    startActivity(intent);
                                }
                            });

                            listView.setAdapter(adapter);
                        }
                        break;
                    default:
                        showToast("返回码："+responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }
}
