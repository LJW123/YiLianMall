package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BankProfitListViewAdapter;
import com.yilian.mall.adapter.BankProfitRateListViewAdapter;
import com.yilian.mall.entity.BankProfitEntity;
import com.yilian.mall.entity.BankProfitRateEntity;
import com.yilian.mall.http.BankRequest;
import com.yilian.mall.utils.NumberFormat;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;

/**
 * 乐分宝增值说明及记录界面
 */

public class BankProfitActivity extends BaseActivity implements View.OnClickListener {

    public static final int WHAT = 1;
    private TextView tv_back;
    private TextView right_textview;
    private ImageView iv_near_title_search;
    private TextView tv_title;
    private TextView tv_data;
    private ListView lv_profit;
    private String pageName;
    private BankRequest request;
    private ArrayList list;
    private BaseAdapter adapter;
    private PullToRefreshScrollView sv_bank_profit;
    private ListView lv_bank_profit_rate;
    private Handler bankProfitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_profit);
        Intent intent = getIntent();
        pageName = intent.getStringExtra("pageName");
        request = new BankRequest(this);
        list = new ArrayList();
        initView();
        initData();
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);

        right_textview = (TextView) findViewById(R.id.right_textview);
        iv_near_title_search = (ImageView) findViewById(R.id.iv_near_title_search);
        tv_title = (TextView) findViewById(R.id.iv_title);
        tv_data = (TextView) findViewById(R.id.tv_data);
        lv_profit = (ListView) findViewById(R.id.lv_profit);
        lv_bank_profit_rate = (ListView) findViewById(R.id.lv_bank_profit_rate);
        sv_bank_profit = (PullToRefreshScrollView) findViewById(R.id.sv_bank_profit);
        sv_bank_profit.setMode(PullToRefreshBase.Mode.DISABLED);
        tv_back.setOnClickListener(this);
        right_textview.setOnClickListener(this);
        switch (pageName) {
            case "profitRate":
                lv_bank_profit_rate.setVisibility(View.VISIBLE);
                sv_bank_profit.setVisibility(View.GONE);
                tv_back.setText("年增值率");
                tv_title.setVisibility(View.GONE);
                tv_data.setVisibility(View.GONE);
                adapter = new BankProfitRateListViewAdapter(this, list);
                lv_bank_profit_rate.setAdapter(adapter);
                break;
            case "accumulatedProfit":
                lv_bank_profit_rate.setVisibility(View.GONE);
                sv_bank_profit.setVisibility(View.VISIBLE);
                tv_back.setText("累计增值");
                tv_title.setVisibility(View.VISIBLE);
                tv_data.setVisibility(View.VISIBLE);
                adapter = new BankProfitListViewAdapter(this, list);
                lv_profit.setAdapter(adapter);
                break;
        }

    }

    private void initData() {
        if (request == null) {
            request = new BankRequest(this);
        }
        switch (pageName) {
            case "profitRate":
                getProfitRate();
                break;
            case "accumulatedProfit":
                getTotalProfit();
                break;
        }

    }

    /**
     * 获取年化领奖励率
     */
    private void getProfitRate() {
        startMyDialog();
        request.getProfitRate(new RequestCallBack<BankProfitRateEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BankProfitRateEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<BankProfitRateEntity.ListBean> listBeen = responseInfo.result.list;
                        list.addAll(listBeen);
                        bankProfitHandler.sendEmptyMessage(WHAT);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 获取累计领奖励数据
     */
    private void getTotalProfit() {
        startMyDialog();
        request.getTotalProfit(new RequestCallBack<BankProfitEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BankProfitEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        double v = NumberFormat.convertToDouble(responseInfo.result.totalProfit, 0.0) / 100;//从服务器获得的乐享币单位是"分",要除以100取小数点后两位
                        String format = String.format("%.2f", v);
                        tv_data.setText(format);
                        ArrayList<BankProfitEntity.ListBean> listBeen = responseInfo.result.list;
                        list.addAll(listBeen);
                        bankProfitHandler.sendEmptyMessage(WHAT);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.right_textview:

                break;
        }
    }
}
