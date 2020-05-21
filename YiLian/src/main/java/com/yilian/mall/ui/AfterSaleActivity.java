package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.AfterSaleAdapter;
import com.yilian.mall.entity.AfterSale;
import com.yilian.mall.http.AfterSaleNetRequest;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 售后服务订单列表界面
 */
public class AfterSaleActivity extends BaseActivity {

    AfterSaleNetRequest netRequest;
    @ViewInject(R.id.tv_back)
    private TextView tvBack;
    @ViewInject(R.id.list)
    private PullToRefreshListView listView;
    private BitmapUtils bitmapUtil;

    private ArrayList<AfterSale.AfterSaleList> list;

    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale);
        ViewUtils.inject(this);
        bitmapUtil = new BitmapUtils(mContext);
        tvBack.setText("售后服务订单列表");

        setListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void setListener() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                getData();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AfterSale.AfterSaleList afterSale = (AfterSale.AfterSaleList) parent.getItemAtPosition(position);

                if (afterSale.AfterSaleStatus == 0)
                    return;
                Intent intent = null;
                switch (afterSale.AfterSaleStatus) {
                    case 2:
                        //审核拒绝
                        intent = new Intent(AfterSaleActivity.this, AfterSaleThreeActivity.class);
                        intent.putExtra("type", afterSale.AfterSaleType);
                        intent.putExtra("orderId", afterSale.orderId);
                        intent.putExtra("filialeId", afterSale.goodsFiliale);
                        startActivity(intent);
                        break;

                    default:
                        intent = new Intent(AfterSaleActivity.this, AfterSaleOneActivity.class);
                        intent.putExtra("type", afterSale.AfterSaleType);
                        intent.putExtra("orderId", afterSale.orderId);
                        intent.putExtra("filialeId", afterSale.goodsFiliale);
                        startActivity(intent);
                        break;
                }


            }
        });
    }

    public void getData() {

        if (netRequest == null)
            netRequest = new AfterSaleNetRequest(mContext);

        netRequest.afterSaleList(page, new RequestCallBack<AfterSale>() {

            @Override
            public void onStart() {
                super.onStart();

                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<AfterSale> responseInfo) {
                stopMyDialog();
                listView.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:

                        list = responseInfo.result.list;

                        if (list == null || list.size() < 1) {
                            showToast(R.string.no_more_data);
                        } else {
                            listView.setAdapter(new AfterSaleAdapter(mContext, list));
                        }

                        break;
                    case -4:

                        showToast("登录状态失效");

                        break;

                    case -3:

                        showToast("系统繁忙");

                        break;

                    default:
                        showToast("异常");
                        break;

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("请检查网络");
            }
        });


    }

    public void find(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        intent.putExtra("type", "afterSale");
        startActivity(intent);
    }
}
