package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.DefaultAdapter;
import com.yilian.mall.adapter.ShopsConsumeAdapter;
import com.yilian.mall.adapter.ViewHolder;
import com.yilian.mall.entity.ShopsConsumeEntity;
import com.yilian.mall.entity.ShopsPresentEntity;
import com.yilian.mall.http.NearbyNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StringFormat;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;

public class ShopsPresentOrConsumeDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int HAVEDATA = 1;
    private TextView tv_back;
    private TextView right_textview;
    private ImageView iv_near_title_search;
    private PullToRefreshGridView ptrlv_record;
    private NearbyNetRequest request;
    private String merchantId;
    private int page;
    private ArrayList list;
    private BaseAdapter adapter;
    private Intent intent;
    private String style;
    private RelativeLayout rl_shops_record;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_present_or_consume_detail);
        request = new NearbyNetRequest(mContext);
        list = new ArrayList<>();
        intent = getIntent();
        merchantId = intent.getStringExtra("merchantId");
        style = intent.getStringExtra("style");
        type = intent.getIntExtra("type", 0);
        initView();
        inidData();
        initListener();
    }


    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        right_textview = (TextView) findViewById(R.id.right_textview);
        iv_near_title_search = (ImageView) findViewById(R.id.iv_near_title_search);
        ptrlv_record = (PullToRefreshGridView) findViewById(R.id.ptrlv_record);
        ptrlv_record.setMode(PullToRefreshBase.Mode.BOTH);

        tv_back.setOnClickListener(this);
        right_textview.setOnClickListener(this);
        tv_back.setText("赠送详情");
        switch (style) {

            case "present":
                initPresentPage();
                break;
            case "consume":
                initConsumePage();
                break;
        }
        ptrlv_record.setAdapter(adapter);
        rl_shops_record = (RelativeLayout) findViewById(R.id.rl_shops_record);
        rl_shops_record.setOnClickListener(this);
    }


    private void inidData() {
        getDetails();

    }

    /**
     * 根据跳转来源不同，获得对应的数据
     */
    private void getDetails() {
        switch (style) {
            case "present":
                getShopsPresentDetail();
                break;
            case "consume":
                getShopsConsumeDetail();
                break;
        }
    }

    private void initListener() {
        ptrlv_record.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 0;
                list.clear();
                getDetails();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                getDetails();
            }
        });
    }

    /**
     * 初始化消费记录界面
     */
    private void initConsumePage() {
//        adapter = new DefaultAdapter<ShopsConsumeEntity.LogBean>(mContext, list, R.layout.item_shops_consume) {
//            @Override
//            public void convert(OuterViewHolder helper, ShopsConsumeEntity.LogBean item) {
//                helper.setText(R.id.tv_consumer_name, TextUtils.isEmpty(item.userName) ? "暂无昵称" : item.userName);// 消费者昵称
//                Spanned spanned = Html.fromHtml("消费币种<font color=\"#f1604f\">乐享币</font>");
//                helper.setText(R.id.tv_consume_style, spanned);//到店消费币种
//                Spanned spanned1 = Html.fromHtml("到店消费<font color=\"#f1604f\">1</font>次");
//                helper.setText(R.id.tv_consume_times, spanned1);//到店消费次数
//                helper.setText(R.id.tv_consume_time, StringFormat.formatDate(item.dealTime));//消费时间
//                helper.setImageUrl(R.id.cv_head_photo, item.photo, null, R.mipmap.ic_evaluate_user);
//
//
//            }
//        };
            adapter = new ShopsConsumeAdapter(mContext,list);
    }

    /**
     * 初始化赠送记录界面
     */
    private void initPresentPage() {
        adapter = new DefaultAdapter<ShopsPresentEntity.LogBean>(mContext, list, R.layout.item_shops_present) {
            @Override
            public void convert(ViewHolder helper, ShopsPresentEntity.LogBean item) {
                String source = "赠送给<font color=\"#247df7\">" + (TextUtils.isEmpty(item.userName) ? "暂无昵称" : item.userName) + "</font>";
                Spanned spanned = Html.fromHtml(source);
                helper.setText(R.id.tv_shops_present_people, spanned);//赠送对象名称
//                String text = item.sendCount + "乐分币";
                Spanned fromHtml=null;
                switch (type){
                    case 0:
                    case 1:
                         fromHtml = Html.fromHtml("<font color = \"#ffa351\">" + MoneyUtil.getLeXiangBiNoZero(item.sendCount) + "</font> "+getResources().getString(R.string.dikouquan));
                        break;
                }
                helper.setText(R.id.tv_shops_present_number, fromHtml);//赠送数量
                helper.setText(R.id.tv_shops_present_time, StringFormat.formatDate(item.sendTime));//赠送时间
            }
        };

    }

    private Handler shopsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HAVEDATA:
                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    };


    /**
     * 获得消费记录详情
     */
    private void getShopsConsumeDetail() {
        startMyDialog();
        if (request == null) {
            request = new NearbyNetRequest(mContext);
        }
        request.getShopsConsumeDetail(merchantId, page, new RequestCallBack<ShopsConsumeEntity>() {
            @Override
            public void onSuccess(ResponseInfo<ShopsConsumeEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<ShopsConsumeEntity.LogBean> listBean = responseInfo.result.log;
                        list.addAll(listBean);
                        if (listBean.size() <= 0 && list.size() <= 0) {//完全没有数据
                            //显示没有记录界面
                            ptrlv_record.setVisibility(View.GONE);
                            rl_shops_record.setVisibility(View.VISIBLE);
                        } else if (listBean.size() <= 0 && list.size() > 0) {//没有 最新（更多） 数据
                            showToast(R.string.no_more_data);
                        } else {//加载到了最新数据
                            shopsHandler.sendEmptyMessage(HAVEDATA);
                        }
                        break;
                }
                stopMyDialog();
                ptrlv_record.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.no_more_data);
                stopMyDialog();
                ptrlv_record.onRefreshComplete();
            }
        });
    }

    /**
     * 获取赠送记录详情
     */
    private void getShopsPresentDetail() {
        startMyDialog();
        if (request == null) {
            request = new NearbyNetRequest(mContext);
        }
        request.getShopsPresentDetail(merchantId, page, new RequestCallBack<ShopsPresentEntity>() {
            @Override
            public void onSuccess(ResponseInfo<ShopsPresentEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<ShopsPresentEntity.LogBean> listBean = responseInfo.result.log;
                        list.addAll(listBean);
                        if (listBean.size() <= 0 && list.size() <= 0) {//完全没有数据
                            //显示没有记录界面
                            //显示没有记录界面
                            ptrlv_record.setVisibility(View.GONE);
                            rl_shops_record.setVisibility(View.VISIBLE);
                        } else if (listBean.size() <= 0 && list.size() > 0) {//没有 最新（更多） 数据
                            showToast(R.string.no_more_data);
                        } else {//加载到了最新数据
                            shopsHandler.sendEmptyMessage(HAVEDATA);
                        }

                        break;


                }
                stopMyDialog();
                ptrlv_record.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.no_more_data);
                stopMyDialog();
                ptrlv_record.onRefreshComplete();
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
