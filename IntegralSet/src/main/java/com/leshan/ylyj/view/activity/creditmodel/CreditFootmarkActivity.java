package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.CreditFootmarkAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.BulgeBgView;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.CreditFootmarkEntity;


/**
 * 信用足迹
 */
public class CreditFootmarkActivity extends BaseActivity {
    private SwipeRefreshLayout swipe_refresh_layout;
    //头部布局
    private BulgeBgView bulge_bg;//头部背景
    private TextView credit_evaluate_tv, abide_by_num_tv;//信用评价，守约次数

    private View headerView;

    private RecyclerView recyclerview;//足迹列表
    private CreditFootmarkAdapter mAdapter;
    CreditPresenter creditPresenter;
    public static String tone1 = "#99F7E8E8";
    public static String tone2 = "#99FBEEE2";
    public static String tone3 = "#99E5F7E5";
    public static String tone4 = "#99E5F6F3";
    public static String tone5 = "#99EFF9FA";

    private int page = 0;//页数
    private int count = 20;//每页展示的内容数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_footmark);

        initToolbar();
        setToolbarTitle("信用足迹");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void initView() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);//刷新控件
        swipe_refresh_layout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipe_refresh_layout.setProgressViewEndTarget(true, 500);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CreditFootmarkAdapter();
        recyclerview.setAdapter(mAdapter);


        headerView = getLayoutInflater().inflate(R.layout.activity_credit_footmark_header, null);
        bulge_bg = headerView.findViewById(R.id.bulge_bg);
        bulge_bg.setLeftHeight(150);
        bulge_bg.setMaxHeight(180);
        credit_evaluate_tv = headerView.findViewById(R.id.credit_evaluate_tv);
        abide_by_num_tv = headerView.findViewById(R.id.abide_by_num_tv);
    }

    @Override
    protected void initListener() {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerview);
    }

    /**
     * @net 执行网络请求
     */
    private void getFirstPageData() {
        page = 0;
        //@net  进行网络请求
//        creditPresenter.getCreditFootmark();
        creditPresenter.getCreditFootmark(String.valueOf(page), String.valueOf(count));
    }

    private void getNextPageData() {
        page++;
//        creditPresenter.getCreditFootmark();
        creditPresenter.getCreditFootmark(String.valueOf(page), String.valueOf(count));
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
        swipe_refresh_layout.setRefreshing(false);
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        if (page == 0) {
            View errorView = View.inflate(mContext, R.layout.library_module_load_error2, null);
            View tvRefresh = errorView.findViewById(R.id.tv_refresh);
            RxUtil.clicks(tvRefresh, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    getFirstPageData();
                }
            });
            mAdapter.setEmptyView(errorView);
        }
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
        swipe_refresh_layout.setRefreshing(false);
    }


    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("获取数据结果");
        //@net 数据类型判断执行对应请求的逻辑
        if (baseEntity instanceof CreditFootmarkEntity) {
//            Toast.makeText(this,((SubmitRecordEntity) baseEntity).desc+"----",Toast.LENGTH_SHORT).show();

            CreditFootmarkEntity.DataBean dataBean = ((CreditFootmarkEntity) baseEntity).data;
            CreditFootmarkEntity.OutBean outBean = ((CreditFootmarkEntity) baseEntity).outputPage;
            List<CreditFootmarkEntity.ContentBean> listBean = ((CreditFootmarkEntity) baseEntity).data.content;


            if (dataBean != null) {
                setData(dataBean);
                if ("1".equals(dataBean.grade)) {
                    mAdapter.setColor(tone1);
                } else if ("2".equals(dataBean.grade)) {
                    mAdapter.setColor(tone2);
                } else if ("3".equals(dataBean.grade)) {
                    mAdapter.setColor(tone3);
                } else if ("4".equals(dataBean.grade)) {
                    mAdapter.setColor(tone4);
                } else if ("5".equals(dataBean.grade)) {
                    mAdapter.setColor(tone5);
                } else {
                    mAdapter.setColor(tone1);
                }
            }
            if (page == 0) {
                if (listBean.size() > 0) {
                    mAdapter.setNewData(listBean);
                } else {
                    mAdapter.setNewData(new ArrayList<CreditFootmarkEntity.ContentBean>());
                    mAdapter.setEmptyView(R.layout.library_module_no_data);
                }
            } else {
                mAdapter.addData(listBean);
            }
            if (listBean.size() > 0 && listBean.size() < count) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
    }

    @Override
    protected void initData() {
        //信用评价 - 较差
//        bulge_bg.resetBg(Color.parseColor("#EB5154"), Color.parseColor("#F66E46"));
//        bulge_bg.resetDrawg();
//        //信用评价 - 一般
//        bulge_bg.resetBg(Color.parseColor("#EB6E07"), Color.parseColor("#F99D04"));
//        //信用评价 - 不错
//        bulge_bg.resetBg(Color.parseColor("#27B12C"), Color.parseColor("#94D342"));
//        //信用评价 - 优秀
//        bulge_bg.resetBg(Color.parseColor("#02BCC2"), Color.parseColor("#02D3AA"));
//        //信用评价 - 非常棒
//        bulge_bg.resetBg(Color.parseColor("#029DEA "), Color.parseColor("#46E3D4"));

        //@net 初始化presenter，并绑定网络回调，
        creditPresenter = new CreditPresenter(mContext, this);
        startMyDialog(false);
        getFirstPageData();

    }

    private void setData(CreditFootmarkEntity.DataBean dataBean) {
        bulge_bg.resetBg(Color.parseColor("#" + dataBean.endColor), Color.parseColor("#" + dataBean.startColor));
        bulge_bg.resetDrawg();
        credit_evaluate_tv.setText(dataBean.title);
        abide_by_num_tv.setText(dataBean.num + "");
        if (mAdapter.getHeaderLayout() == null)//判断是否存在头布局 不存在则添加头布局
            mAdapter.addHeaderView(headerView);
    }
}
