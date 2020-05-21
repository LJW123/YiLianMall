package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MTRefundStateAdapter;
import com.yilian.mall.entity.MTRefundStatusEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yilian.mall.R.id.tv_money;


/**
 * Created by Ray_L_Pain on 2016/12/6 0006.
 * 美团-退款详情
 */

public class MTRefundStateActivity extends BaseActivity{
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(tv_money)
    TextView tvMoney;
    @ViewInject(R.id.tv_way)
    TextView tvWay;
    @ViewInject(R.id.tv_time)
    TextView tvTime;
    @ViewInject(R.id.listView)
    ListView listView;

    private MTRefundStateAdapter adapter;
    String id;
    private MTNetRequest mtNetRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_detail_mt);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        id = getIntent().getStringExtra("index_id");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTRefundStateActivity.this.finish();
            }
        });
        tvTitle.setText("退款详情");
    }

    private void initData() {
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.comboRefundStatus(id, new RequestCallBack<MTRefundStatusEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<MTRefundStatusEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        MTRefundStatusEntity info = responseInfo.result;
                        tvMoney.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(info.price)));
                        tvWay.setText(info.refStyle);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String time = sdf.format(new Date(Long.valueOf(info.planTime + "000")));
                        if ("3".equals(info.status)) {
                            tvTime.setVisibility(View.GONE);
                        } else {
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText("预计" + time + "前");
                        }

                        adapter = new MTRefundStateAdapter(mContext, info);
                        listView.setAdapter(adapter);
                        break;
                    default:
                        showToast("" + responseInfo.result.code);
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
}
