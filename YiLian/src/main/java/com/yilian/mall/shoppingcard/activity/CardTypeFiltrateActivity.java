package com.yilian.mall.shoppingcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.shoppingcard.adapter.CardTypeFiltrateAdapter;
import com.yilian.mall.widgets.NoScrollRecyclerView;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.networkingmodule.entity.shoppingcard.CardChangeDetailEntity;
import com.yilian.networkingmodule.entity.shoppingcard.CardTypeFiltrateEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：马铁超 on 2018/11/16 16:08
 * 购物卡类型筛选
 */

public class CardTypeFiltrateActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView v3Back;
    private TextView v3Title;
    private TextView tvRight;
    private NoScrollRecyclerView rvCardFiltrateList;
    private Button tvQuery;
    List<CardTypeFiltrateEntity.DataBean> itemDataBeans = new ArrayList<>();
    private String type = "2";
    private CardTypeFiltrateAdapter filtrateAdapter;
    private int resultCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_type_filtrate);
        initView();
        initData();
        initListener();
    }


    private void initData() {
        requestCardTypeFiltrateData();
    }

    private void initListener() {
        // 返回
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                onBackPressed();
            }
        });

        filtrateAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (itemDataBeans.get(position).isCheck == true) {
                    itemDataBeans.get(position).isCheck = false;
                } else {
                    for (int i = 0; i < itemDataBeans.size(); i++) {
                        itemDataBeans.get(i).isCheck = false;
                    }
                    itemDataBeans.get(position).isCheck = true;
                }
                filtrateAdapter.notifyDataSetChanged();
            }
        });

        RxUtil.clicks(tvQuery, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                String typeKey = "";

                boolean isCheck = false;
                if (itemDataBeans != null) {
                    for (int i = 0; i < itemDataBeans.size(); i++) {
                        if (itemDataBeans.get(i).isCheck == true) {
                            isCheck = true;
                            typeKey = itemDataBeans.get(i).type;
                        }
                    }
                }

                if (isCheck == true) {
                    Intent intent = new Intent();
                    intent.putExtra("typeKey",typeKey);
                    setResult(0, intent);
                    onBackPressed();
                } else {
                    ToastUtil.showMessage(mContext, "请选择筛选内容");
                }
            }
        });

    }

    /**
     * 获取购物卡类型筛选数据
     */
    private void requestCardTypeFiltrateData() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getCardTypeFiltrate("account/getScreenType", type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardTypeFiltrateEntity>() {
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
                    public void onNext(CardTypeFiltrateEntity filtrateEntity) {
                        itemDataBeans = filtrateEntity.data;
                        setFiltrateData(itemDataBeans);
                    }
                });
        addSubscription(subscription);
    }

    private void setFiltrateData(List<CardTypeFiltrateEntity.DataBean> filtrateData) {
        if (filtrateData != null && filtrateData.size() > 0) {
            filtrateAdapter.setNewData(filtrateData);
        }
    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rvCardFiltrateList = (NoScrollRecyclerView) findViewById(R.id.rv_card_filtrate_list);
        tvQuery = (Button) findViewById(R.id.tv_query);
        v3Title.setText("筛选");

        filtrateAdapter = new CardTypeFiltrateAdapter(R.layout.item_card_type_filtrate);
        rvCardFiltrateList.setAdapter(filtrateAdapter);
        filtrateAdapter.bindToRecyclerView(rvCardFiltrateList);

        rvCardFiltrateList.setLayoutManager(new GridLayoutManager(mContext, 3));
        tvRight.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                break;
            case R.id.tv_query:

                break;

        }
    }
}
