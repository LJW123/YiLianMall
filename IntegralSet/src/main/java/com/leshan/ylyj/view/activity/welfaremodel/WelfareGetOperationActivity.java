package com.leshan.ylyj.view.activity.welfaremodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.WelfareGetOperationAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.WelfarePresenter;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.MenuUtil;

import java.util.ArrayList;
import java.util.List;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.WelfareLoveOperationEntity;

/**
 *
 * 如何获取经验值
 * @author zhaiyaohua on 2018/1/18 0018.
 */

public class WelfareGetOperationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mLlTitle;
    private ImageView mLeftBack;
    private TextView mMiddleTitile;
    private ImageView mRightImage;
    private ImageView mIvIocnShare;
    private ImageView mIvCancel;
    private RecyclerView mRyView;
    private WelfarePresenter mPresenter;
    private WelfareGetOperationAdapter mAdapter;
    private List<WelfareLoveOperationEntity.Data> mDataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_get_operation);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
        mMiddleTitile = (TextView) findViewById(R.id.middle_titile);
        mRightImage = (ImageView) findViewById(R.id.right_image);
        mRightImage.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(R.mipmap.setting);
        mIvIocnShare = (ImageView) findViewById(R.id.iv_iocn_share);
        mLeftBack=findViewById(R.id.left_back);
        mRyView = (RecyclerView) findViewById(R.id.ry_view);
        mIvCancel=(ImageView)findViewById(R.id.iv_cancel);
        mRyView.setNestedScrollingEnabled(false);

        mAdapter=new WelfareGetOperationAdapter(R.layout.item_welfare_operation);
        View headerView=View.inflate(mContext,R.layout.header_welfare_operation,null);
        mAdapter.setHeaderView(headerView);
        mRyView.setLayoutManager(new LinearLayoutManager(mContext));
        mRyView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mIvCancel.setOnClickListener(this);
        findViewById(R.id.left_back).setOnClickListener(this);
        mRightImage.setOnClickListener(this);
        findViewById(R.id.tv_know).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getWelfareOperation();
    }

    /**
     * 如何获取公益爱心经验值
     */
    private void getWelfareOperation(){
        startMyDialog(false);
        if (mPresenter==null){
            mPresenter=new WelfarePresenter(mContext,this);
        }
        mPresenter.getWelfareOperation();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id==R.id.iv_cancel||id==R.id.left_back||id==R.id.tv_know){
            finish();
        }else if (id==R.id.right_image){
            showMenu();
        }
    }
    @Override
    public void onCompleted() {
        stopMyDialog();

    }

    @Override
    public void onError(Throwable e) {
        showToast(e.getMessage());
        stopMyDialog();

    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        WelfareLoveOperationEntity entity= (WelfareLoveOperationEntity) baseEntity;
        mDataList.clear();
        if (entity.data!=null){
            mDataList.add(entity.data);
        }
        mAdapter.setNewData(mDataList);
    }

    private void showMenu() {
        MenuUtil.showMenu(WelfareGetOperationActivity.this, R.id.right_image);
    }
}
