package com.yilian.mall.suning.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.suning.adapter.SnGoodsClassifyAdapter;
import com.yilian.mall.suning.adapter.SnGoodsSecondClassifyAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.suning.SnGoodsClassifyEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 苏宁首页商品分类
 *
 * @author Created by zhaiyaohua on 2018/5/23.
 */

public class SnGoodsClassifyFragment extends JPBaseFragment {
    public static final int SPAN_COUNT = 3;
    private RecyclerView jdGoodsClassifyRecycleView;
    private RecyclerView jdGoodsSecondClassifyRecycleView;
    private SnGoodsClassifyAdapter snGoodsClassifyAdapter;
    private SnGoodsSecondClassifyAdapter secondClassifyAdapter;
    private List<SnGoodsClassifyEntity.Data> dataList;
    private TextView secondClassifyTitle;
    private TextView tvSearch;
    private VaryViewUtils varyViewUtils;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.sn_fragment_goods_classify, null);
        iniView(view);
        initListener();
        return view;
    }

    private void iniView(View view) {
        StatusBarUtil.setColor(getActivity(), ContextCompat.getColor(mContext, R.color.white_color), Constants.STATUS_BAR_ALPHA_60);
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setSnVaryViewHelper(R.id.vary_content, view, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                loadData();

            }
        });
        tvSearch = (TextView) view.findViewById(R.id.tv_search);


        jdGoodsClassifyRecycleView = (RecyclerView) view.findViewById(R.id.sn_goods_classify);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 1, R.color.color_white, false);
        jdGoodsClassifyRecycleView.setLayoutManager(linearLayoutManager);
        jdGoodsClassifyRecycleView.addItemDecoration(decor);
        snGoodsClassifyAdapter = new SnGoodsClassifyAdapter();
        jdGoodsClassifyRecycleView.setAdapter(snGoodsClassifyAdapter);

        jdGoodsSecondClassifyRecycleView = view.findViewById(R.id.sn_goods_second_classify);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, SPAN_COUNT);
        jdGoodsSecondClassifyRecycleView.setLayoutManager(gridLayoutManager);
        secondClassifyAdapter = new SnGoodsSecondClassifyAdapter();
        jdGoodsSecondClassifyRecycleView.setAdapter(secondClassifyAdapter);
    }

    private void initListener() {
        RxUtil.clicks(tvSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpSnActivityUtils.toSnGoodsSearch(mContext);
            }
        });
        snGoodsClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnGoodsClassifyEntity.Data data = snGoodsClassifyAdapter.getItem(position);
                secondClassifyTitle.setText(data.getTitle());
                secondClassifyAdapter.setNewData(data.getContent());
                snGoodsClassifyAdapter.setNotisifySelectedPositon(position);
                jdGoodsSecondClassifyRecycleView.smoothScrollToPosition(0);
            }
        });
        secondClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnGoodsClassifyEntity.Data.Content content = (SnGoodsClassifyEntity.Data.Content) adapter.getItem(position);
                if (null != content) {
                    String catId = content.catId;
                    String title = content.title;
                    JumpSnActivityUtils.toSnThirdClassifyGoodsList(mContext, catId, title);
                }

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getJdGoodsClassify() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnGoodsClassify("suning_goods/suning_class_list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnGoodsClassifyEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        stopMyDialog();
                        showToast(e.getMessage());

                    }

                    @Override
                    public void onNext(SnGoodsClassifyEntity entity) {
                        setGoodsClassifyData(entity);
                    }
                });
        addSubscription(subscription);
    }

    private void setGoodsClassifyData(SnGoodsClassifyEntity entity) {
        if (null != entity) {
            varyViewUtils.showDataView();
            dataList = entity.getData();
            snGoodsClassifyAdapter.setNewData(dataList);
            if (dataList.size() > 0) {
                if (secondClassifyAdapter.getHeaderLayoutCount() <= 0) {
                    View headView = View.inflate(mContext, R.layout.header_online_goods_sort, null);
                    secondClassifyTitle = headView.findViewById(R.id.tv_head_goods_type);
                    secondClassifyAdapter.addHeaderView(headView);
                }
                secondClassifyAdapter.setNewData(dataList.get(0).getContent());
                secondClassifyTitle.setText(dataList.get(0).getTitle());
            }
        }
    }

    @Override
    protected void loadData() {
        getJdGoodsClassify();
    }
}
