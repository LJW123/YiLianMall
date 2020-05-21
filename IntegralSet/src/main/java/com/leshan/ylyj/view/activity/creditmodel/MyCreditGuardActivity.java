package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.CreditGuardAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.CreditGuardEntity;


/**
 * 信用守护
 */
public class MyCreditGuardActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView credit_guard_rv;
    private List<CreditGuardEntity.ListBean> listdata;
    CreditPresenter creditPresenter;
    private ImageView guard_title_img;
    private TextView guard_title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_credit_guard);
        initToolbar();
        setToolbarTitle("信用守护");
        hasBack(true);
        initView();
        initListener();
        initData();
        getCreditGuard();
        StatusBarUtil.setColor(this, Color.WHITE);

    }

    /**
     * @net 执行网络请求
     */
    private void getCreditGuard(){
        startMyDialog(false);
        //@net 初始化presenter，并绑定网络回调，
        if (creditPresenter==null){
            creditPresenter=new CreditPresenter(mContext,this);
        }
        //@net  进行网络请求
        creditPresenter.getCreditGuard();
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }
    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext,e.getMessage());
        stopMyDialog();
    }
    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("获取数据结果");
        //@net 数据类型判断执行对应请求的逻辑
        if (baseEntity instanceof CreditGuardEntity){
            listdata=((CreditGuardEntity) baseEntity).list;
            GlideUtil.showImage(this, ((CreditGuardEntity) baseEntity).banner_url, guard_title_img);
            final CreditGuardAdapter adapter = new CreditGuardAdapter(this, listdata);
            GridLayoutManager manager = new GridLayoutManager(this,1);
            credit_guard_rv.setLayoutManager(manager);
            credit_guard_rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new CreditGuardAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, String data, int position) {
//          data为内容，position为点击的位置
//                Toast.makeText(MyCreditGuardActivity.this, "----" + position, Toast.LENGTH_SHORT).show();
                    if(position==0){
                        SkipUtils.toMyMallOrderListComment(MyCreditGuardActivity.this,4);
                    }else if(position==1){
                        SkipUtils.toMyMallOrderListComment(MyCreditGuardActivity.this,2);
                    }else if(position==2){
                        SkipUtils.toEssentialInformation(MyCreditGuardActivity.this);
                    }
                }
            });
        }
    }

    @Override
    protected void initView() {
        credit_guard_rv=findViewById(R.id.credit_guard_rv);
        guard_title_img=findViewById(R.id.guard_title_img);
        guard_title_tv=findViewById(R.id.guard_title_tv);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        listdata=new ArrayList<>();

    }

    @Override
    public void onClick(View v) {

    }
}
