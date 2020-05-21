package com.leshan.ylyj.view.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leshan.ylyj.adapter.TwoTypeCardAdpter;
import com.leshan.ylyj.eventmodel.FirstEvent;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.RxBus;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.mylibrary.widget.MyLoading;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.TwoTypeEntity;

/**
 * 对公银行卡
 */
public class PublicFragment extends BaseFragment {

    private RecyclerView public_rv;
    private TwoTypeCardAdpter adpter;
    public MyLoading myloading;
    private BankPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_public, null);

        initView(view, null);
        initListener();
        initData();

        return view;

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        public_rv = view.findViewById(R.id.public_rv);
        public_rv.setLayoutManager(new LinearLayoutManager(getHoldingActivity()));
        public_rv.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        presenter = new BankPresenter(this.getContext(), this);
        presenter.getTwoTypeList("1");
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }


    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
    }

    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(getHoldingActivity(), e.getMessage());
    }

    @Override
    public void onErrors(int flag, Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(getHoldingActivity(), e.getMessage());
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("获取数据结果");
        if (baseEntity instanceof TwoTypeEntity) {
            setData(((TwoTypeEntity) baseEntity));
            Logger.i("获取数据成功");

            RxBus.getDefault().post(new FirstEvent("GETPUBLIC", "SUCCESS"));

        } else {
            ToastUtil.showMessage(getHoldingActivity(), "获取数据失败");
            Logger.i("获取数据失败");
        }
    }

    public void setData(TwoTypeEntity twoTypeEntity) {

        adpter = new TwoTypeCardAdpter(getHoldingActivity(), twoTypeEntity.data);
        public_rv.setAdapter(adpter);

    }

}
