package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActCommentAdapter;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.networkingmodule.entity.ActCommentRankEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 大家对TA的评价
 * Created by ZYH on 2017/12/15 0015.
 */

public class ActLookCommentsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private MySwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private ActCommentAdapter mAdapter;
    private String id;
    private List<ActCommentRankEntity.Comment> mList=new ArrayList<>();
    private EditText etComment;
    private TextView tvSubmite;
    private boolean notPriseOpter=false;
    private CheckBox cbBigZan;
    private boolean operation=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_look_comments);
        initView();
        iniListner();
    }

    private void iniListner() {
        cbBigZan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){//点赞
                    clockInPraise(id,1);
                }else {
                    clockInPraise(id,2);
                }

            }
        });
        findViewById(R.id.v3Back).setOnClickListener(this);
        tvSubmite.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //点赞功能
                ActCommentRankEntity.Comment item= (ActCommentRankEntity.Comment) adapter.getItem(position);
                String id=item.commentId;
                int type=item.isPraise;
                if (type==1){
                    praiseComment(position,item,id,2);
                }else {
                    praiseComment(position,item,id,1);

                }

            }
        });
    }
    private void initView() {
        int type=getIntent().getIntExtra("type",0);
        cbBigZan= (CheckBox) findViewById(R.id.cb_input_zan);
        if (type==0){
            cbBigZan.setChecked(false);
        }else {
            cbBigZan.setChecked(true);
        }
        id=getIntent().getStringExtra("id");
        etComment= (EditText) findViewById(R.id.et_comment_content);

        tvSubmite= (TextView) findViewById(R.id.tv_submit);
        mRefreshLayout= (MySwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        mRecycleView= (RecyclerView) findViewById(R.id.pull_listView);
        mAdapter=new ActCommentAdapter(R.layout.item_act_look_comment);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleView.setAdapter(mAdapter);
        TextView textView= (TextView) findViewById(R.id.v3Title);
        textView.setText("大家对TA的评价");
        StatusBarUtil.setColor(this, Color.parseColor("#282828"));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.v3Back:
                finish();
                break;
            case R.id.tv_submit:
                //提交评论
                String content=etComment.getText().toString().trim();
                subComment(content);
                break;
        }
    }
    @SuppressWarnings("unchecked")
    private void praiseComment(int position,ActCommentRankEntity.Comment item,String id,int type){
        startMyDialog(false);
         Subscription subscription =RetrofitUtils3.getRetrofitService(mContext)
                    .praiseComments("clockin_comment_praise",id,type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<HttpResultBean>() {
                        @Override
                        public void onCompleted() {
                            stopMyDialog();


                        }

                        @Override
                        public void onError(Throwable e) {
                            stopMyDialog();


                        }

                        @Override
                        public void onNext(HttpResultBean resultBean) {
                            stopMyDialog();
                            operation=true;
                            showToast(resultBean.msg);
                            item.isPraise=type;
                            if (type==1){
                                item.praiseNum++;
                            }else {
                                item.praiseNum--;
                            }
                            mAdapter.notifyItemChanged(position);

                        }
                    });
         addSubscription(subscription);
    }
    @SuppressWarnings("unchecked")
    private void subComment(String content) {
        if (TextUtils.isEmpty(content)){
            showToast("评论内容不能为空");
            return;
        }
        startMyDialog(false);
        notPriseOpter=true;
        Subscription subscription=RetrofitUtils3.getRetrofitService(mContext)
                .subComments("clockin_record_comment",id,content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        notPriseOpter=false;

                    }

                    @Override
                    public void onError(Throwable e) {
                        notPriseOpter=false;
                        stopMyDialog();

                    }

                    @Override
                    public void onNext(HttpResultBean resultBean) {
                        operation=true;
                        stopMyDialog();
                        notPriseOpter=false;
                        showToast(resultBean.msg);
                        etComment.setText("");
                        updateData();
                    }
                });
        addSubscription(subscription);


    }

    private void updateData(){
        getData();
    }
    private boolean isFirst=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus&&isFirst){
            isFirst=false;
            initData();

        }
    }

    private void initData() {
        mRefreshLayout.setRefreshing(true);
        getData();
    }

    @SuppressWarnings("unchecked")
    private void getData(){
           Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).getCommentRanking("clockin_record_comment_list",id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ActCommentRankEntity>() {
                        @Override
                        public void onCompleted() {
                            mRefreshLayout.setRefreshing(false);
                            if (notPriseOpter){
                                stopMyDialog();
                                notPriseOpter=false;
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            mRefreshLayout.setRefreshing(false);
                            if (notPriseOpter){
                                stopMyDialog();
                                notPriseOpter=false;
                            }

                        }

                        @Override
                        public void onNext(ActCommentRankEntity actCommentRankEntity) {
                            if (notPriseOpter){
                                stopMyDialog();
                                notPriseOpter=false;
                            }

                            setData(actCommentRankEntity);

                        }
                    });
           addSubscription(subscription);
    }

    /**
     * 对排行点赞或取消点赞
     *
     * @param recordId
     * @param type     1 点赞   2 取消点赞
     */
    @SuppressWarnings("unchecked")
    private void clockInPraise(String recordId, int type) {
        startMyDialog(false);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .clockInRecordPraise("clockin_record_praise", recordId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        operation=true;

                    }
                });
        addSubscription(subscription);
    }

    private void setData(ActCommentRankEntity actCommentRankEntity) {
        mList.clear();
        mList.addAll(actCommentRankEntity.list);
        mAdapter.setNewData(mList);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void finish() {
        if (operation){
            setResult(1);
        }
        super.finish();
    }
}
