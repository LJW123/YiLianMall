package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.presenter.IntegralTreasurePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.adapter.ExchangeIndexRecordAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.ExchangeIndexRecordEntity;


/**
 * 兑换指数记录
 */
public class ExchangeIndexRecordActivity extends BaseActivity implements View.OnClickListener {
    private IntegralTreasurePresenter mPresent;
    private RecyclerView record_rv;
    private ExchangeIndexRecordAdapter indexRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_index_record);
        initToolbar();
        setToolbarTitle("近7日兑换指数");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();
        initListener();
        initData();
    }


    @Override
    protected void initView() {
        record_rv = findViewById(R.id.record_rv);
        record_rv.setLayoutManager(new LinearLayoutManager(this));
        record_rv.setNestedScrollingEnabled(false);
        indexRecordAdapter = new ExchangeIndexRecordAdapter(this);
        record_rv.setAdapter(indexRecordAdapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mPresent = new IntegralTreasurePresenter(mContext, this);
        startMyDialog(false);
        mPresent.getExchangeIndexRecord();

//        List<Map<String, String>> list = new ArrayList<>();
//        Map<String, String> map = new HashMap<>();
//        map.put("date", "2017-09-20");
//        map.put("range", "0.01");
//        map.put("earnings", "0.2400%");
//        map.put("upsSndDowns", "1");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("date", "2017-09-19");
//        map.put("range", "0.01");
//        map.put("earnings", "0.2400%");
//        map.put("upsSndDowns", "-1");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("date", "2017-09-18");
//        map.put("range", "0.01");
//        map.put("earnings", "0.2400%");
//        map.put("upsSndDowns", "1");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("date", "2017-09-17");
//        map.put("range", "0.01");
//        map.put("earnings", "0.2400%");
//        map.put("upsSndDowns", "-1");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("date", "2017-09-16");
//        map.put("range", "0.01");
//        map.put("earnings", "0.2400%");
//        map.put("upsSndDowns", "1");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("date", "2017-09-15");
//        map.put("range", "0.01");
//        map.put("earnings", "0.2400%");
//        map.put("upsSndDowns", "1");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("date", "2017-09-14");
//        map.put("range", "0.01");
//        map.put("earnings", "0.2400%");
//        map.put("upsSndDowns", "1");
//        list.add(map);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.date_picker_ll://年月选择
//
//                break;
            default:
                break;
        }
    }

    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof rxfamily.entity.ExchangeIndexRecordEntity) {
            Logger.i("获取数据成功");
            indexRecordAdapter.setNewData(((ExchangeIndexRecordEntity) baseEntity).data.getLastSeven());
        } else {
            Logger.i("获取数据失败");
        }
    }
}
