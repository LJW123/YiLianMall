package com.leshan.ylyj.view.activity.bankmodel;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leshan.ylyj.adapter.MyPurseAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.MyPurseEntity;


/**
 * 我的钱包
 */
public class MyPurseActivity extends BaseActivity implements View.OnClickListener {

    public static final int CARD_LIST_REQUEST_CODE = 0;
    private TextView toolbar_right;//交易记录

    private LinearLayout my_integration_ll, balance_ll, my_bank_cards_ll;

    private BankPresenter myPursePresenter;
    /**
     * 0.00
     */
    private TextView mIntegralTv;
    /**
     * 0.00
     */
    private TextView mBalanceTv;
    /**
     * 0
     */
    private TextView mCardNumTv;
    private RecyclerView recyclerView;
    private MyPurseAdapter myPurseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purse);

        myPursePresenter = new BankPresenter(this, this);

        initToolbar();
        setToolbarTitle("钱包");
        hasBack(true);

        initView();
        initData();
        initListener();

    }

    @Override
    protected void initView() {
        toolbar_right = findViewById(R.id.toolbar_right);//交易记录
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
//        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
//        recyclerView.addItemDecoration(decor);
        myPurseAdapter = new MyPurseAdapter(R.layout.item_my_pure);
        recyclerView.setAdapter(myPurseAdapter);
        toolbar_right.setText("交易记录");
        toolbar_right.setTextColor(getResources().getColor(R.color.main_black_text));

        my_integration_ll = (LinearLayout) findViewById(R.id.my_integration_ll);
        balance_ll = (LinearLayout) findViewById(R.id.balance_ll);
        my_bank_cards_ll = (LinearLayout) findViewById(R.id.my_bank_cards_ll);
        mIntegralTv = (TextView) findViewById(R.id.integral_tv);

        mBalanceTv = (TextView) findViewById(R.id.balance_tv);
        mCardNumTv = (TextView) findViewById(R.id.card_num_tv);
    }

    private void setData(MyPurseEntity myPurseEntity) {

        mIntegralTv.setText(MoneyUtil.getTwoDecimalPlaces(myPurseEntity.data.integral));
        mBalanceTv.setText(MoneyUtil.getTwoDecimalPlaces(myPurseEntity.data.cash));
        mCardNumTv.setText(myPurseEntity.data.cardNum);

    }


    @Override
    protected void initListener() {
        toolbar_right.setOnClickListener(this);
        my_integration_ll.setOnClickListener(this);
        balance_ll.setOnClickListener(this);
        my_bank_cards_ll.setOnClickListener(this);
        myPurseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyPurseEntity.DataBean.InfoBean item = (MyPurseEntity.DataBean.InfoBean) adapter.getItem(position);
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(item.type, item.content);
            }
        });
    }

    @Override
    protected void initData() {
        getData();

    }

    private void getData() {
        startMyDialog(false);
        myPursePresenter.getPurse();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.toolbar_right) {//交易记录
            SkipUtils.toTransactionRecord(this);
        } else if (i == R.id.my_integration_ll) {
            SkipUtils.toMyIntegration(this);
        } else if (i == R.id.balance_ll) {
            SkipUtils.toMyMoney(this);
        } else if (i == R.id.my_bank_cards_ll) {
//            SkipUtils.toMyBankCards(this);
            if (isCert()) {
                Intent intent = new Intent(mContext, MyBankCardsActivity.class);
                startActivityForResult(intent, CARD_LIST_REQUEST_CODE);
            } else {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationViewImplActivity"));
                startActivity(intent);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
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
        Logger.e(e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof MyPurseEntity) {

            myPurseAdapter.setNewData(((MyPurseEntity) baseEntity).data.info);
            setData((MyPurseEntity) baseEntity);
            Logger.i("获取数据成功");
        } else {
            Logger.i("获取数据失败");
        }
    }
}