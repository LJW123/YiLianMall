package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.JPCommEvaluateAdapter;
import com.yilian.mall.adapter.JPCommQuestionAdapter;
import com.yilian.mall.entity.AnswerListEntity;
import com.yilian.mall.entity.EvaluateList;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/11/4 0004.
 * 评价列表界面
 */

public class JPAllEvaListActivity extends BaseActivity{
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.listView)
    PullToRefreshListView listView;

    private MallNetRequest evaRequest;
    private JPNetRequest jpNetRequest;
    JPCommEvaluateAdapter adapter;
    JPCommQuestionAdapter answerAdapter;
    private int page = 0;
    ArrayList<EvaluateList.Evaluate> list = new ArrayList<>();
    ArrayList<AnswerListEntity.ListBean> answerList = new ArrayList<>();
    private String goodsId, filialeId, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allevalist);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        goodsId = getIntent().getStringExtra("goods_id");
        filialeId = getIntent().getStringExtra("filiale_id");
        type = getIntent().getStringExtra("type");
        listView.setMode(PullToRefreshBase.Mode.BOTH);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPAllEvaListActivity.this.finish();
            }
        });

        if ("eva".equals(type)) {
            tvTitle.setText("全部评价");
            adapter = new JPCommEvaluateAdapter(mContext, list);
            listView.setAdapter(adapter);
        } else if ("que".equals(type)) {
            tvTitle.setText("全部问答");
            answerAdapter = new JPCommQuestionAdapter(mContext, answerList);
            listView.setAdapter(answerAdapter);
        }
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page ++;
                initData();
            }
        });
    }

    private void initData(){
        if ("eva".equals(type)) {
            if(evaRequest == null){
                evaRequest = new MallNetRequest(mContext);
            }
            evaRequest.getEvaluateList(goodsId, filialeId, page, new RequestCallBack<EvaluateList>() {
                @Override
                public void onStart() {
                    super.onStart();
                    startMyDialog();
                }

                @Override
                public void onSuccess(ResponseInfo<EvaluateList> responseInfo) {
                    Logger.i("goods_id:"+goodsId);
                    Logger.i("filialeId:"+filialeId);
                    Logger.i("page:"+page);
                    listView.onRefreshComplete();
                    stopMyDialog();
                    switch (responseInfo.result.code){
                        case 1:
                            if(page == 0){
                                list.clear();
                            }
                            ArrayList<EvaluateList.Evaluate> moreList = responseInfo.result.list;
                            if(moreList.size() != 0 && moreList != null){
                                list.addAll(moreList);
                                adapter.notifyDataSetChanged();
                            } else {
                                showToast(R.string.no_more_data);
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
        } else if ("que".equals(type)) {
            if(jpNetRequest == null){
                jpNetRequest = new JPNetRequest(mContext);
            }
            jpNetRequest.askAndAnswerList(goodsId, page, new RequestCallBack<AnswerListEntity>() {
                @Override
                public void onStart() {
                    super.onStart();
                    startMyDialog();
                }

                @Override
                public void onSuccess(ResponseInfo<AnswerListEntity> responseInfo) {
                    listView.onRefreshComplete();
                    stopMyDialog();
                    switch (responseInfo.result.code) {
                        case 1:
                            if (page == 0) {
                                answerList.clear();
                            }
                            ArrayList<AnswerListEntity.ListBean> moreList = responseInfo.result.list;
                            if (moreList.size() != 0 && moreList != null) {
                                answerList.addAll(moreList);
                                answerAdapter.notifyDataSetChanged();
                            } else {
                                showToast(R.string.no_more_data);
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
}
