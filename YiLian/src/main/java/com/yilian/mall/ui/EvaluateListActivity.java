package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ImageAdapter;
import com.yilian.mall.adapter.layoutManager.FullyLinearLayoutManager;
import com.yilian.mall.entity.EvaluateList;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * 商品评价列表界面
 * Created by lauyk on 16/3/22.
 */
public class EvaluateListActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    @ViewInject(R.id.rb_evaluate)
    private RatingBar mRb;

    @ViewInject(R.id.tv_evaluate_count)
    private TextView mTvEvaluateCount;

    @ViewInject(R.id.rv)
    private ListView rv;

    @ViewInject(R.id.sv_pull_refresh)
    private PullToRefreshScrollView svPullRefresh;


    MallNetRequest netRequest;
    ArrayList<EvaluateList.Evaluate> list = new ArrayList<>();
    int page;

    String goodsId, filialeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_list);
        ViewUtils.inject(this);
        netRequest = new MallNetRequest(mContext);
        Intent intent = getIntent();
        goodsId = intent.getStringExtra("goods_id");
        filialeId = intent.getStringExtra("filiale_id");
        mTvEvaluateCount.setText("共" + intent.getStringExtra("evaluateCount") + "条评价");
        mRb.setRating(intent.getFloatExtra("totalScore", 5));
        tvBack.setText("评价列表");
        getData();

        svPullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        svPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page += 1;
                getData();
            }
        });
    }


    private void getData() {
        netRequest.getEvaluateList(goodsId, filialeId, page, new RequestCallBack<EvaluateList>() {
            @Override
            public void onSuccess(ResponseInfo<EvaluateList> responseInfo) {
                svPullRefresh.onRefreshComplete();
                switch (responseInfo.result.code) {

                    case 1:

                        if (page == 0) {
                            list.clear();
                        }
                        if (responseInfo.result.list.size() == 0)
                            showToast("没有了");
                        list.addAll(responseInfo.result.list);
                        rv.setAdapter(new adapter(list));

//                        rv.setAdapter(new EvaluateAdapter(mContext, responseInfo.result.list));
//                        rv.setLayoutManager(new FullyLinearLayoutManager(mContext));
//                        rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));


                        break;

                    default:

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                svPullRefresh.onRefreshComplete();
            }
        });

    }

    class adapter extends BaseAdapter {
        BitmapUtils bitmapUtils;
        ArrayList<EvaluateList.Evaluate> list;
        RecyclerView rv;

        public adapter(ArrayList<EvaluateList.Evaluate> list) {
            this.list = list;
            bitmapUtils = new BitmapUtils(mContext);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_evaluate, null);

            if (!TextUtils.isEmpty(list.get(position).icon)) {

                String url = list.get(position).icon;

                if (!TextUtils.isEmpty(url)) {
                    GlideUtil.showImageWithSuffix(mContext, url, (JHCircleView) convertView.findViewById(R.id.iv_user));
                }

            }

            ((TextView) convertView.findViewById(R.id.tv_user_name)).setText(list.get(position).commentConsumer);

            ((TextView) convertView.findViewById(R.id.tv_time)).setText(StringFormat.formatDate(list.get(position).commentTime, "yyyy-MM-dd"));
            ((TextView) convertView.findViewById(R.id.tv_evaluate)).setText(list.get(position).commentContent);
            ((RatingBar) convertView.findViewById(R.id.rb_evaluate)).setRating(list.get(position).commentScore / 10);
            rv = (RecyclerView) convertView.findViewById(R.id.rv_evaluate);
            String[] a = list.get(position).commentImages.split(",");

            ArrayList<String> list = new ArrayList<>();
            for (String b : a)
                list.add(b);

            if (TextUtils.isEmpty(this.list.get(position).commentImages)) {
                rv.setVisibility(View.GONE);
            } else {
                rv.setVisibility(View.VISIBLE);
                rv.setAdapter(new ImageAdapter(mContext, list, bitmapUtils));
                rv.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
            }
            return convertView;
        }
    }
}
