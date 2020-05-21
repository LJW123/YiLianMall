package com.yilian.mall.ui;

import android.os.Bundle;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BalanceAdapter;
import com.yilian.mall.entity.AssetsRecordList;
import com.yilian.mall.http.AssetsNetRequest;
import com.yilian.mall.widgets.NoScrollListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 领奖励记录界面
 * Created by Administrator on 2016/4/14.
 */
public class CashRecordActivity extends BaseActivity {

    @ViewInject(R.id.rv)
    private NoScrollListView rv;
    private AssetsNetRequest netRequest;
    private String c="lefen";
    private String type="2";
    private int page=0;
    private ArrayList<AssetsRecordList.AssetsRecord> list = new ArrayList<>(); // 领奖励记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_record);

        ViewUtils.inject(this);
        getData();
    }

    private void getData() {
        if (netRequest == null)
            netRequest = new AssetsNetRequest(mContext);

        netRequest.assetsRecord(c, type, page, new RequestCallBack<AssetsRecordList>() {
            @Override
            public void onSuccess(ResponseInfo<AssetsRecordList> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                            list = responseInfo.result.list;
                            rv.setAdapter(new BalanceAdapter(mContext, c, type, list));
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
