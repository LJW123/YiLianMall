package com.yilian.mall.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActCommEvaluateAdapter;
import com.yilian.mall.adapter.JPCommQuestionAdapter;
import com.yilian.mall.entity.AnswerListEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.ui.ActivityDetailActivity;
import com.yilian.mall.ui.JPAllEvaListActivity;
import com.yilian.mall.widgets.CusRecycleView;
import com.yilian.mall.widgets.CustListView;
import com.yilian.mall.widgets.CustWebView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.networkingmodule.entity.ActGoodsEvaListEntity;
import com.yilian.networkingmodule.entity.GALZanEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2016/11/1 0001.
 */

public class ActivityDetailBottomFragment extends BaseFragment {
    private Context context;
    private ActivityDetailActivity activity;
    View rootView;
    private RelativeLayout paddingLayout;
    private RadioGroup radioGroup;
    private RadioButton buttonOne;
    private RadioButton buttonTwo;
    private RadioButton buttonThree;
    private ScrollView sv;
    private ProgressBar progressBar;
    private CustWebView webView1,webView2;
    private CusRecycleView recycleView;
    private CustListView listViewQuestion;
    View moreView;
    private TextView moreTv, noMoreTv;
    private ImageView ivNoData;
    public ImageView iv_go_top;
    private ActCommEvaluateAdapter adapter;
    public JPCommQuestionAdapter questionAdapter;
    private String urlOne,goodsId,filialeId;
    private MallNetRequest evaRequest;
    private JPNetRequest jpNetRequest;
    private int evaPage = 0;
    ArrayList<ActGoodsEvaListEntity.ListBean> evaList = new ArrayList<>();
    ArrayList<AnswerListEntity.ListBean> queList = new ArrayList<>();


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_act_bottom, null);
        context = getContext();
        activity = (ActivityDetailActivity) getActivity();

        initMore();
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(webView1!=null){
            webView1.destroy();
            webView1=null;
        }
        if(webView2!=null){
            webView2.destroy();
            webView2=null;
        }
    }

    public void initView(int which) {
        Logger.i("ray-" + "走了bottom的initView");
        paddingLayout = (RelativeLayout) rootView.findViewById(R.id.layout_padding);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paddingLayout.setPadding(0, StatusBarUtils.getStatusBarHeight(context), 0, 0);
        }
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_one:
                        Logger.i("ray-" + "点击按钮1");
                        webView1.setVisibility(View.VISIBLE);
                        webView2.setVisibility(View.GONE);
                        recycleView.setVisibility(View.GONE);
                        listViewQuestion.setVisibility(View.GONE);
                        activity.which = 0;
                        if (isWebViewLoadFinishOne) {
                            initWebView(webView1, urlOne, 1);
                        }
                        showNormal();
                        break;
                    case R.id.rb_two:
                        Logger.i("ray-" + "点击按钮2");
                        webView1.setVisibility(View.GONE);
                        webView2.setVisibility(View.VISIBLE);
                        recycleView.setVisibility(View.GONE);
                        listViewQuestion.setVisibility(View.GONE);
                        activity.which = 1;
                        if (isWebViewLoadFinishTwo) {
                            initWebView(webView2, Constants.AFTER_SALE_GOODS, 2);
                        }
                        showNormal();
                        break;
                    case R.id.rb_three:
                        Logger.i("ray-" + "点击按钮3");
                        webView1.setVisibility(View.GONE);
                        webView2.setVisibility(View.GONE);
                        recycleView.setVisibility(View.VISIBLE);
                        listViewQuestion.setVisibility(View.GONE);
                        initListViewEvaluate();
                        activity.which = 2;
                        showNormal();
                        break;
                    case R.id.rb_four:
                        Logger.i("ray-" + "点击按钮4");
                        webView1.setVisibility(View.GONE);
                        webView2.setVisibility(View.GONE);
                        recycleView.setVisibility(View.GONE);
                        listViewQuestion.setVisibility(View.VISIBLE);
                        initListViewQuestion(false);
                        activity.which = 3;
                        showQuestion();
                        break;
                    default:
                        break;
                }
            }
        });
        buttonOne = (RadioButton) rootView.findViewById(R.id.rb_one);
        buttonTwo = (RadioButton) rootView.findViewById(R.id.rb_two);
        buttonThree = (RadioButton) rootView.findViewById(R.id.rb_three);
        sv = (ScrollView) rootView.findViewById(R.id.sv);
        progressBar = (ProgressBar) rootView.findViewById(R.id.sender_list_progress);
        webView1 = (CustWebView) rootView.findViewById(R.id.webView1);
        String userAgentString = webView1.getSettings().getUserAgentString();
//        修改WebView的userAgent  在获取到的后面拼接 空格lefen_native_ android
        webView1.getSettings().setUserAgentString(userAgentString+" lefen_native_ android");
        webView2 = (CustWebView) rootView.findViewById(R.id.webView2);
        String userAgentString2 = webView2.getSettings().getUserAgentString();
//        修改WebView的userAgent  在获取到的后面拼接 空格lefen_native_ android
        webView2.getSettings().setUserAgentString(userAgentString2+" lefen_native_ android");
        recycleView = (CusRecycleView) rootView.findViewById(R.id.listViewEvaluate);
        listViewQuestion = (CustListView) rootView.findViewById(R.id.listViewQuestion);
        ivNoData = (ImageView) rootView.findViewById(R.id.iv_no_data);

        urlOne = activity.urlOne;

        //根据类型来选择加载第几个页面，默认是0 也就是第一个
        switch (which){
            case 0:
                Logger.i("ray-" + "走了1");
                webView1.setVisibility(View.VISIBLE);
                webView2.setVisibility(View.GONE);
                recycleView.setVisibility(View.GONE);
                listViewQuestion.setVisibility(View.GONE);
                if (isWebViewLoadFinishOne) {
                    initWebView(webView1,urlOne, 1);
                }
                showNormal();
                break;
            case 1:
                Logger.i("ray-" + "走了2");
                webView1.setVisibility(View.GONE);
                webView2.setVisibility(View.VISIBLE);
                recycleView.setVisibility(View.GONE);
                listViewQuestion.setVisibility(View.GONE);
                if (isWebViewLoadFinishTwo) {
                    initWebView(webView2, Constants.AFTER_SALE_GOODS, 2);
                }
                activity.which = 1;
                showNormal();
                break;
            case 2:
                Logger.i("ray-" + "走了3");
                webView1.setVisibility(View.GONE);
                webView2.setVisibility(View.GONE);
                recycleView.setVisibility(View.VISIBLE);
                listViewQuestion.setVisibility(View.GONE);
                initListViewEvaluate();
                activity.which = 2;
                showNormal();
                break;
            case 3:
                Logger.i("ray-" + "走了4");
                webView1.setVisibility(View.GONE);
                webView2.setVisibility(View.GONE);
                recycleView.setVisibility(View.GONE);
                listViewQuestion.setVisibility(View.VISIBLE);
                initListViewQuestion(false);
                activity.which = 3;
                showQuestion();
                break;
            default:
                break;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recycleView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new ActCommEvaluateAdapter(R.layout.item_commodity_evaluate_act);
        }
        recycleView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.i("2017年12月13日 10:01:41-走了这里111");
                ActGoodsEvaListEntity.ListBean item = (ActGoodsEvaListEntity.ListBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_zan_count:
                        Logger.i("2017年12月13日 10:01:41-走了这里222");
                        String isParise;
                        if ("0".equals(item.isParise)) {
                            isParise = "1";
                        } else {
                            isParise = "2";
                        }
                        int zanCount = item.commentPraise;
                        RetrofitUtils2.getInstance(mContext).setPraise(item.commentIndex, "3", isParise, new Callback<GALZanEntity>() {
                            @Override
                            public void onResponse(Call<GALZanEntity> call, Response<GALZanEntity> response) {
                                HttpResultBean body = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                        switch (body.code) {
                                            case 1:
                                                GALZanEntity entity = response.body();

                                                int newCount = entity.pariseNum;
                                                item.commentPraise = newCount;
                                                if (zanCount > newCount) {
                                                    //取消赞
                                                    item.isParise = "0";
                                                } else {
                                                    //点赞
                                                    item.isParise = "1";
                                                }
                                                adapter.notifyItemChanged(position, item);
                                                break;
                                            default:
                                                showToast(body.msg);
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GALZanEntity> call, Throwable t) {
                                showToast(R.string.net_work_not_available_zan);
                            }
                        });
                        break;
                    default:
                        Logger.i("2017年12月13日 10:01:41-走了这里333");
                        break;
                }
            }
        });
    }

    private void showNormal() {
        if (activity.layoutNormal.getVisibility() == View.GONE) {
            activity.layoutNormal.setVisibility(View.VISIBLE);
        }
        if (activity.layoutQuestion.getVisibility() == View.VISIBLE) {
            activity.layoutQuestion.setVisibility(View.GONE);
        }
    }

    private void showQuestion() {
        if (activity.layoutNormal.getVisibility() == View.VISIBLE) {
            activity.layoutNormal.setVisibility(View.GONE);
        }
        if (activity.layoutQuestion.getVisibility() == View.GONE) {
            activity.layoutQuestion.setVisibility(View.VISIBLE);
        }
    }

    private void initMore(){
        if (moreView == null) {
            moreView = View.inflate(context, R.layout.listview_footer, null);
            moreTv = (TextView) moreView.findViewById(R.id.tv_more);
            noMoreTv = (TextView) moreView.findViewById(R.id.tv_no_more);
        }
    }

    public boolean isWebViewLoadFinishOne = true;
    public boolean isWebViewLoadFinishTwo = true;

    private void initWebView(WebView webView, String url, int position){
        Logger.i("JP详情   "+url);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    if (position == 1) {
                        isWebViewLoadFinishOne = false;
                    } else if (position == 2) {
                        isWebViewLoadFinishTwo = false;
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
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


    private ArrayList<ActGoodsEvaListEntity.ListBean> list = new ArrayList<>();

    private void initListViewEvaluate() {
        if(evaList.size() != 0){
            return;
        }
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).galCommentList(String.valueOf(evaPage), activity.goodsId, new Callback<ActGoodsEvaListEntity>() {
            @Override
            public void onResponse(Call<ActGoodsEvaListEntity> call, Response<ActGoodsEvaListEntity> response) {
                stopMyDialog();
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        ActGoodsEvaListEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                ArrayList<ActGoodsEvaListEntity.ListBean> newList = entity.list;
                                Logger.i("2017年12月13日 10:01:41-走了这里" + newList.size());
                                if (newList.size() > 0 && newList != null) {
                                    recycleView.setVisibility(View.VISIBLE);
                                    ivNoData.setVisibility(View.GONE);

                                    adapter.setNewData(newList);
                                    list.clear();
                                    list.addAll(newList);

                                    if (adapter.getFooterLayoutCount() == 0) {
                                        adapter.addFooterView(moreView);
                                        if (list.size() >= 20) {
                                            moreTv.setVisibility(View.VISIBLE);
                                            noMoreTv.setVisibility(View.GONE);
                                            moreTv.setText("查看全部评价");
                                            moreTv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(activity, JPAllEvaListActivity.class);
                                                    intent.putExtra("goods_id", activity.goodsId);
                                                    intent.putExtra("filiale_id", activity.filialeId);
                                                    intent.putExtra("type", "eva");
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            moreTv.setVisibility(View.GONE);
                                            noMoreTv.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    adapter.setNewData(newList);
                                    recycleView.setVisibility(View.GONE);
                                    ivNoData.setVisibility(View.VISIBLE);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActGoodsEvaListEntity> call, Throwable t) {
                stopMyDialog();
                adapter.loadMoreFail();
                recycleView.setVisibility(View.GONE);
                ivNoData.setVisibility(View.VISIBLE);
                showToast(R.string.merchant_module_service_exception);
            }
        });
    }

    public void initListViewQuestion(boolean flag) {
        if(jpNetRequest == null){
            jpNetRequest = new JPNetRequest(context);
        }
        if (!flag) {
            if(queList.size() != 0){
                return;
            }
        }

        jpNetRequest.askAndAnswerList(activity.goodsId, 0, new RequestCallBack<AnswerListEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<AnswerListEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code){
                    case 1:
                        if(responseInfo.result.list.size() == 0 || responseInfo.result.list == null){
                            listViewQuestion.setVisibility(View.GONE);
                            ivNoData.setVisibility(View.VISIBLE);
                        } else {
                            listViewQuestion.setVisibility(View.VISIBLE);
                            ivNoData.setVisibility(View.GONE);
                            queList.clear();
                            queList.addAll(responseInfo.result.list);
                            questionAdapter = new JPCommQuestionAdapter(context, queList);
                            Logger.i("ray-" + listViewQuestion.getFooterViewsCount());
                            if (listViewQuestion.getFooterViewsCount() == 0) {
                                listViewQuestion.addFooterView(moreView);
                            }
                            if (queList.size() >= 20) {
                                moreTv.setVisibility(View.VISIBLE);
                                noMoreTv.setVisibility(View.GONE);
                                moreTv.setText("查看全部问答");
                                moreTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(activity, JPAllEvaListActivity.class);
                                        intent.putExtra("goods_id", activity.goodsId);
                                        intent.putExtra("filiale_id", activity.filialeId);
                                        intent.putExtra("type", "que");
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                moreTv.setVisibility(View.GONE);
                                noMoreTv.setVisibility(View.VISIBLE);
                            }

                            listViewQuestion.setAdapter(questionAdapter);
                        }
                        break;
                    default:
                        showToast(responseInfo.result.msg);
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
