package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.DialogChangeAdress;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.DeleteRecordEntity;
import rxfamily.entity.InquireRecordEntity;


/**
 * 学历提交成功
 */
public class SubmitEducationActivity extends BaseActivity implements View.OnClickListener {

    private TextView delete_tv,check_entrante_times_tv;
    CreditPresenter creditPresenter;
    private TextView check_time_tv,check_region_tv,check_academy_tv,check_state_tv,check_diploma_tv,check_entrante_time_tv,education_background_tv;
    private String state;
    private String record;
    InquireRecordEntity.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_education);
        initToolbar();
        setToolbarTitle("学历");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initListener();
        initData();
        getInquireRecord();
    }

    @Override
    protected void initView() {
        delete_tv = findViewById(R.id.delete_tv);

        check_time_tv = findViewById(R.id.check_time_tv);
        check_region_tv = findViewById(R.id.check_region_tv);
        check_academy_tv = findViewById(R.id.check_academy_tv);
        check_state_tv = findViewById(R.id.check_state_tv);
        check_diploma_tv = findViewById(R.id.check_diploma_tv);
        check_entrante_time_tv = findViewById(R.id.check_entrante_time_tv);
        check_entrante_times_tv=findViewById(R.id.check_entrante_times_tv);
        education_background_tv=findViewById(R.id.education_background_tv);

    }

    @Override
    protected void initListener() {
        delete_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.delete_tv) {//删除学历dialog
            DialogChangeAdress.TwoEditDialog(this);

        } else if (i == R.id.check_car_delete) {//删除学历
            DialogChangeAdress.dialog.dismiss();
            DeleteRecord(dataBean.getId());
        } else if (i == R.id.check_car_cancel) {//取消
            DialogChangeAdress.dialog.dismiss();
        }
    }

    /**
     * 删除学历
     */
    private void DeleteRecord(String id){
        startMyDialog(false);
        //@net 初始化presenter，并绑定网络回调，
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        //@net  进行网络请求
        creditPresenter.getDeleteRecord(id);
        Logger.i("---进行网络请求---");
    }

    /**
     * @net 执行网络请求
     */
    private void getInquireRecord() {
        startMyDialog(false);
        //@net 初始化presenter，并绑定网络回调，
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        //@net  进行网络请求
        creditPresenter.getInquireRecord();
        Logger.i("---进行网络请求---");
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
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
    }
    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("获取数据结果");
        //@net 数据类型判断执行对应请求的逻辑
        if (baseEntity instanceof InquireRecordEntity) {
            dataBean=((InquireRecordEntity) baseEntity).data;
            InquireRecord(dataBean);
//            Toast.makeText(this,((SubmitRecordEntity) baseEntity).desc+"----",Toast.LENGTH_SHORT).show();
        }else if(baseEntity instanceof DeleteRecordEntity){
            SkipUtils.toRecord(this);
            finish();
        }
    }
    private void InquireRecord(InquireRecordEntity.DataBean dataBean){
        check_time_tv.setText("提交时间"+dataBean.getCreate_time());
        Transform(dataBean.getCurrentStatus(),dataBean.getEducation());
        check_region_tv.setText(dataBean.getAreaName());
        check_academy_tv.setText(dataBean.getCollegeName());
        check_state_tv.setText(state);
        check_diploma_tv.setText(record);
        if(dataBean.getCurrentStatus()==1){
//            check_entrante_time_tv.setText(dataBean.getEnrolTime());
            check_entrante_time_tv.setText(dataBean.getGraduateTime());
        }else{
            check_entrante_time_tv.setText(dataBean.getGraduateTime());
        }
    }
    public void Transform(int current_status,int education){
        switch (current_status){
            case 1:
                state="在读";
                check_entrante_times_tv.setText("入学时间");
                education_background_tv.setText("学        历");
                break;
            case 2:
                check_entrante_times_tv.setText("毕业时间");
                education_background_tv.setText("最高学历");
                state="毕业";
                break;
        }
        switch (education){
            case 1:
                record="中专";
                break;
            case 2:
                record="大专";
                break;
            case 3:
                record="本科";
                break;
            case 4:
                record="硕士";
                break;
            case 5:
                record="博士";
                break;
        }
    }
}
