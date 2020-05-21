package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MyCombatGainsAdapter;

/**
 * @author LYQ 我的战绩
 */
public class ActMyCombatGainsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private LinearLayout llV3title;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private MyCombatGainsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_swip_recyclerview);
        initView();
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("我的战绩");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        llV3title = (LinearLayout) findViewById(R.id.ll_v3title);
        llV3title.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_my_combat_gains));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyCombatGainsAdapter(R.layout.item_my_combat_gains);
        recyclerView.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext,R.color.color_red));
        initHeadView();
        v3Back.setOnClickListener(this);
    }

    private void initHeadView() {
        View headView=View.inflate(mContext,R.layout.item_my_combat_gains_head,null);
        ImageView ivShare= (ImageView) headView.findViewById(R.id.iv_share);
        TextView tvJoinCount= (TextView) headView.findViewById(R.id.tv_join_count);
        TextView tvTotalEarnings= (TextView) headView.findViewById(R.id.tv_total_earnings);
        TextView tvSuccessDays= (TextView) headView.findViewById(R.id.tv_success_days);
        TextView tvFriendGain= (TextView) headView.findViewById(R.id.tv_friend_gain);
        TextView tvDetailRecord= (TextView) headView.findViewById(R.id.tv_detail_record);
        adapter.addHeaderView(headView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
        }
    }
}
