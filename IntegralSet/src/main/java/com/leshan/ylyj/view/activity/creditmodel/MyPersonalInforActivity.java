package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.UploadInformationEntity;


/**
 * 信用个人信息
 */
public class MyPersonalInforActivity extends BaseActivity implements View.OnClickListener {

    //各个行点击的LinearLayout
    private LinearLayout credit_education_rl, credit_mainbox_rl, credit_driving_licence, credit_driving_license, credit_id_card;
    //学历，邮箱，驾驶证，行车证，身份证，上传状态
    private TextView credit_education_tv, credit_mainbox_tv, credit_driving_licence_tv, credit_driving_license_tv, credit_id_card_tv;
    private String identification;

    CreditPresenter creditPresenter;
    UploadInformationEntity.DataBean dataBean;
    private String credit_education = "0";
    private String credit_email = "0";
    private String driving_licence = "0";
    private String vehicle_license = "0";
    private String realname = "";
    private boolean refresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_personal_infor);
        initToolbar();
        setToolbarTitle("个人信息");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initListener();
        getSubmitRecord();
//        Bundle bundle = getIntent().getExtras();
//        identification = bundle.getString("identification");
//        if (identification.equals("1")) {
//            credit_education_tv.setText("已上");//学历
//            credit_mainbox_tv.setText("已上");
//            credit_driving_licence_tv.setText("已上");//驾驶证
//            credit_driving_license_tv.setText("已上");//行驶证
//            credit_id_card_tv.setText("已上");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refresh) {
            getSubmitRecord();
        }
    }

    @Override
    protected void initView() {
        credit_education_rl = findViewById(R.id.credit_education_rl);
        credit_mainbox_rl = findViewById(R.id.credit_mainbox_rl);
        credit_driving_licence = findViewById(R.id.credit_driving_licence);
        credit_driving_license = findViewById(R.id.credit_driving_license);
        credit_education_tv = findViewById(R.id.credit_education_tv);
        credit_mainbox_tv = findViewById(R.id.credit_mainbox_tv);
        credit_driving_licence_tv = findViewById(R.id.credit_driving_licence_tv);
        credit_driving_license_tv = findViewById(R.id.credit_driving_license_tv);
        credit_id_card_tv = findViewById(R.id.credit_id_card_tv);
        credit_id_card = findViewById(R.id.credit_id_card);
    }

    @Override
    protected void initListener() {
        credit_education_rl.setOnClickListener(this);
        credit_mainbox_rl.setOnClickListener(this);
        credit_driving_licence.setOnClickListener(this);
        credit_driving_license.setOnClickListener(this);
        credit_id_card.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * @net 执行网络请求
     */
    private void getSubmitRecord() {
        startMyDialog(false);
        //@net 初始化presenter，并绑定网络回调，
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        //@net  进行网络请求
        creditPresenter.getUploadInformation();
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
        if (baseEntity instanceof UploadInformationEntity) {
//            Toast.makeText(this,((SubmitRecordEntity) baseEntity).desc+"----",Toast.LENGTH_SHORT).show();
            dataBean = ((UploadInformationEntity) baseEntity).data;
            if (dataBean.getDriverStatus().equals("1")) {
                credit_driving_licence_tv.setText("已上传");//驾驶证
                driving_licence = "1";
            }else{
                driving_licence = "0";
                credit_driving_licence_tv.setText("");//驾驶证
            }
            if (dataBean.getEducationStatus().equals("1")) {
                credit_education_tv.setText("已上传");//学历
                credit_education = "1";
            }else{
                credit_education = "0";
                credit_education_tv.setText("");//学历
            }
            if (dataBean.getVehicleStatus().equals("1")) {
                credit_driving_license_tv.setText("已上传");//行驶证
                vehicle_license = "1";
            }else{
                vehicle_license = "0";
                credit_driving_license_tv.setText("");//行驶证
            }
            if (dataBean.getEmailStatus().equals("1")) {//邮箱
                credit_mainbox_tv.setText("已上传");
                credit_email="1";
            }else{
                credit_email = "0";
                credit_mainbox_tv.setText("");
            }
            realname = dataBean.getRealNameStatus();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        refresh = true;
        if (("1".equals(realname))) {//已实名认证
            if (i == R.id.credit_education_rl) {//学历
                if (credit_education.equals("1")) {
                    SkipUtils.toSubmitEducation(this);//已上传过学历，查看学历
                } else {
                    SkipUtils.toRecord(this);
                }
            } else if (i == R.id.credit_mainbox_rl) {//邮箱
                if (credit_email.equals("1")) {
                    SkipUtils.toMyEmailSuccess(this);//已上传
                } else {
                    SkipUtils.toMyEmail(this);
                }
            } else if (i == R.id.credit_driving_licence) {
                if (driving_licence.equals("1")) {
                    SkipUtils.toSubmitDrivingLicence(this, "licence");//已上传
                } else {
                    SkipUtils.toUploadingDrivingLicence(this, "licence");//传入驾驶证标识
                }
            } else if (i == R.id.credit_driving_license) {
                if (vehicle_license.equals("1")) {
                    SkipUtils.toSubmitDrivingLicence(this, "license");//已上传
                } else {
                    SkipUtils.toUploadingDrivingLicence(this, "license");//传入行驶证标识
                }
            } else if (i == R.id.credit_id_card) {//身份证
                SkipUtils.toIDInformationVerification(this);

            }
        } else if ("0".equals(realname)) {
            Toast.makeText(this, "请先实名认证", Toast.LENGTH_SHORT).show();
            SkipUtils.toCertificationViewImpl(this);
        } else {
            Toast.makeText(this, "网络异常,正在重新加载", Toast.LENGTH_SHORT).show();
            getSubmitRecord();
        }
    }
}
