package com.yilian.mall.suning.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.suning.module.SuNingCommitOrderGoodsModule;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SnCommitOrderMultiGoodsList extends BaseAppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SnCommitOrderMultiGoodsList";
    public static final String TAG_GOODS_COUNT = "tag_goods_count";
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private RecyclerView recyclerView;
    private ArrayList<SuNingCommitOrderGoodsModule> goodsDatas;
    private int orderGoodsCount = 0;
    private ArrayList<SuNingCommitOrderGoodsModule> goodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sn_commit_order_mulit_goods_list);
        initView();
        initData();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商品清单");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        tvRight2.setVisibility(View.VISIBLE);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        goodsList = (ArrayList<SuNingCommitOrderGoodsModule>) getIntent().getSerializableExtra(TAG);
        orderGoodsCount = getIntent().getIntExtra(TAG_GOODS_COUNT, 0);
        recyclerView.setAdapter(new MyAdapter(goodsList));
        tvRight2.setText(String.format("共%s件", orderGoodsCount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            default:
                break;
        }
    }

    class MyAdapter extends BaseQuickAdapter<SuNingCommitOrderGoodsModule, BaseViewHolder> {

        public MyAdapter(@Nullable List<SuNingCommitOrderGoodsModule> data) {
            super(R.layout.item_sn_commit_order_goods_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SuNingCommitOrderGoodsModule item) {
            helper.setText(R.id.tv_goods_name, item.goodsName);
            GlideUtil.showImage(mContext, item.imgUrl, helper.getView(R.id.iv_goods));
            helper.setText(R.id.tv_goods_price, String.format("¥%s/件", item.price));
            helper.setText(R.id.tv_goods_count, String.format(Locale.getDefault(), "%d", item.count));
        }
    }
}
