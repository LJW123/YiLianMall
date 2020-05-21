package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.OnlineGoodsSortAdapter;
import com.yilian.mall.adapter.OnlineGoodsTypeAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MenuUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.JPGoodsClassfiyEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 线上商品分类页面
 *
 * @author Created by zhaiyaohua on 2018/5/11.
 */

public class OnlineMallGoodsSortListActivity extends BaseActivity {
    private TextView tvSearch;
    private RecyclerView rvOnlineGoodsType;
    private RecyclerView rvOnlineGoodsSort;
    private List<JPGoodsClassfiyEntity.ListBean> goodsTypeList = new ArrayList<>();
    private List<JPGoodsClassfiyEntity.ListBean.InfoBean> goodsSortList = new ArrayList<>();
    private HashMap<String, List<JPGoodsClassfiyEntity.ListBean.InfoBean>> mMap = new HashMap<>();
    private OnlineGoodsTypeAdapter mOnlineGoodsTypeAdapter;
    private OnlineGoodsSortAdapter mOnlineGoodsSortAdapter;
    private TextView headGoodsType;
    private ImageView v3Back;
    private TextView v3Title;
    private JPNetRequest jpNetRequest;
    private View headView;
    private ImageView v3Share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_goods_sort_list);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white_color), Constants.STATUS_BAR_ALPHA_60);
        v3Share = findViewById(R.id.v3Share);
        v3Share.setImageResource(R.mipmap.setting);
        v3Share.setVisibility(View.VISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商品分类");
        v3Back = (ImageView) findViewById(R.id.v3Back);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        //左边商品分类列表
        rvOnlineGoodsType = (RecyclerView) findViewById(R.id.rv_online_goods_type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 1, R.color.color_white, false);
        rvOnlineGoodsType.setLayoutManager(linearLayoutManager);
        rvOnlineGoodsType.addItemDecoration(decor);
        mOnlineGoodsTypeAdapter = new OnlineGoodsTypeAdapter(R.layout.item_online_goods_type, goodsTypeList);
        rvOnlineGoodsType.setAdapter(mOnlineGoodsTypeAdapter);
        // 右边商品品牌分类列表
        headView = View.inflate(mContext, R.layout.header_online_goods_sort, null);
        headGoodsType = headView.findViewById(R.id.tv_head_goods_type);
        rvOnlineGoodsSort = (RecyclerView) findViewById(R.id.rv_online_goods_sort);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rvOnlineGoodsSort.setLayoutManager(gridLayoutManager);
        mOnlineGoodsSortAdapter = new OnlineGoodsSortAdapter(R.layout.item_online_goods_sort, goodsSortList);
        rvOnlineGoodsSort.setAdapter(mOnlineGoodsSortAdapter);
    }

    private void initListener() {
        RxUtil.clicks(v3Share, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showMenu();
            }
        });
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
        mOnlineGoodsSortAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JPGoodsClassfiyEntity.ListBean.InfoBean infoBean = mOnlineGoodsSortAdapter.getItem(position);
                Intent intent = new Intent(mContext, OnLineMallGoodsListActivity.class);
                intent.putExtra("class_id", infoBean.id);
                intent.putExtra("goods_classfiy", infoBean.name);
                startActivity(intent);
            }
        });
        mOnlineGoodsTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JPGoodsClassfiyEntity.ListBean bean = mOnlineGoodsTypeAdapter.getItem(position);
                headGoodsType.setText(bean.name);
                mOnlineGoodsTypeAdapter.setNotisifySelectedPositon(position);
                mOnlineGoodsSortAdapter.setNewData(mMap.get(bean.id));
            }
        });
        RxUtil.clicks(tvSearch, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent searchIntent = new Intent(mContext, SearchCommodityActivity.class);
                startActivity(searchIntent);
            }
        });
    }

    /**
     * 获取商品分类
     */
    private void initData() {
        if (null == jpNetRequest) {
            jpNetRequest = new JPNetRequest(this);
        }
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getGoodsClassList("goods/app_classify_list", "0", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JPGoodsClassfiyEntity>() {
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
                    public void onNext(JPGoodsClassfiyEntity o) {
                        if (mOnlineGoodsSortAdapter.getHeaderLayoutCount() <= 0) {
                            mOnlineGoodsSortAdapter.addHeaderView(headView);
                        }
                        goodsTypeList.clear();
                        goodsTypeList.addAll(o.list);
                        for (int i = 0; i < goodsTypeList.size(); i++) {
                            JPGoodsClassfiyEntity.ListBean bean = goodsTypeList.get(i);
                            mMap.put(bean.id, bean.info);
                        }
                        mOnlineGoodsTypeAdapter.setNewData(goodsTypeList);
                        mOnlineGoodsSortAdapter.setNewData(goodsTypeList.get(0).info);
                        headGoodsType.setText(goodsTypeList.get(0).name);
                    }
                });
        addSubscription(subscription);
//        jpNetRequest.getGoodsClassList(new RequestCallBack<JPGoodsClassfiyEntity>() {
//            @Override
//            public void onSuccess(ResponseInfo<JPGoodsClassfiyEntity> responseInfo) {
//                stopMyDialog();
//                switch (responseInfo.result.code) {
//                    case 1:
//                        if (mOnlineGoodsSortAdapter.getHeaderLayoutCount() <= 0) {
//                            mOnlineGoodsSortAdapter.addHeaderView(headView);
//                        }
//                        goodsTypeList.clear();
//                        goodsTypeList.addAll(responseInfo.result.list);
//                        for (int i = 0; i < goodsTypeList.size(); i++) {
//                            JPGoodsClassfiyEntity.ListBean bean = goodsTypeList.get(i);
//                            mMap.put(bean.id, bean.info);
//                        }
//                        mOnlineGoodsTypeAdapter.setNewData(goodsTypeList);
//                        mOnlineGoodsSortAdapter.setNewData(goodsTypeList.get(0).info);
//                        headGoodsType.setText(goodsTypeList.get(0).name);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                stopMyDialog();
//                showToast(e.getMessage());
//
//            }
//        });
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        MenuUtil.showMenu(this, R.id.v3Share);
    }
}
