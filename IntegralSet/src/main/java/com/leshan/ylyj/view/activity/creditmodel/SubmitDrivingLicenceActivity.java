package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.DateUtils;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.QueryDriverLicenseEntity;
import rxfamily.entity.QueryDrivingEntity;

/**
 * 添加驾驶证（提交成功）
 */
public class SubmitDrivingLicenceActivity extends BaseActivity implements View.OnClickListener {
    private TextView examine_tv, submit_time, submit_name, submit_car_id, submit_car_type, checkDrivingBrandType, checkDrivingCarNo, category_tv;
    //                 姓名       证件号码  准驾车型 品牌型号 车牌号码 发证日期
    private String str, name, certificateNo, fileNo, brandType, carNo, issueDate, vin;
    CreditPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_driving_licence);
        initView();
        initListener();
        //传入驾驶证标识
        str = getIntent().getExtras().getString("str");
        initToolbar();
        //根据上级页面传过来的数据判断驾驶证或行车证
        if ("licence".equals(str)) {
            setToolbarTitle("驾驶证");
            category_tv.setText("准驾车型");
        } else {
            setToolbarTitle("行驶证");
            checkDrivingBrandType.setText("品牌型号");
            checkDrivingCarNo.setText("车牌号码");
            category_tv.setText("发证日期");
        }
        hasBack(true);
        initData();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        examine_tv = findViewById(R.id.examine_tv);
        category_tv = findViewById(R.id.category_tv);
        submit_time = findViewById(R.id.submit_time);
        submit_name = findViewById(R.id.submit_name);
        submit_car_id = findViewById(R.id.submit_car_id);
        submit_car_type = findViewById(R.id.submit_car_type);
        checkDrivingBrandType = (TextView) findViewById(R.id.check_driving_brand_type);
        checkDrivingCarNo = (TextView) findViewById(R.id.check_driving_car_no);
    }

    @Override
    protected void initListener() {
        examine_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //@net 初始化presenter，并绑定网络回调，
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
        if ("licence".equals(str)) {
            //查询驾驶证信息
            startMyDialog(false);
            presenter.queryDriverLicenseInfo();
        } else {
            //查询行驶证信息
            startMyDialog(false);
            presenter.queryDrivingLicenseInfo();
        }

    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("查询驾驶证信息成功");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("查询驾驶证信息异常：" + e.getMessage());
        stopMyDialog();
//        ToastUtil.showMessage(mContext, e.getMessage());
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("查询驾驶证信息结果");
        if (baseEntity instanceof QueryDriverLicenseEntity) {
            //返回驾驶证信息
            if ((QueryDriverLicenseEntity) baseEntity!=null){
                setDriverLicense((QueryDriverLicenseEntity) baseEntity);
            }
        } else if (baseEntity instanceof QueryDrivingEntity) {
            //返回行驶证信息
            if ((QueryDrivingEntity) baseEntity!=null) {
                setDrivingInfo((QueryDrivingEntity) baseEntity);
            }
        }
    }

    /**
     * 设置品牌型号，车牌号码，发证日期的值并展示
     *
     * @param baseEntity
     */
    public void setDrivingInfo(QueryDrivingEntity baseEntity) {
        submit_time.setText("提交时间：" + baseEntity.getData().getCreate_time());
        name = baseEntity.getData().getName().toString();
        vin = baseEntity.getData().getVin().toString();
        brandType = baseEntity.getData().getBrand_type().toString();
        carNo = baseEntity.getData().getCar_no().toString();
        issueDate = baseEntity.getData().getIssue_date().toString();
        submit_name.setText(brandType);
        String start = carNo.substring(0, 2);
        carNo=start+"*****";
        //车牌号码
        submit_car_id.setText(carNo);
        issueDate= DateUtils.convertDate(issueDate);
        Log.i("TAG", "日期-发证issueDate: "+issueDate);
        submit_car_type.setText(issueDate);
    }

    /**
     * 设置提交时间，姓名，证件号码，准驾车型的值并展示
     *
     * @param baseEntity
     */
    public void setDriverLicense(QueryDriverLicenseEntity baseEntity) {
        name = baseEntity.getData().getName().toString();//张振振
        certificateNo = baseEntity.getData().getCertificate_no().toString();
        fileNo = baseEntity.getData().getFile_no().toString();
        submit_time.setText("提交时间：" + baseEntity.getData().getCreate_time());
        StringBuffer buffer = new StringBuffer(name);
        buffer.replace(0, 1, "*");
        submit_name.setText(buffer);
        //截取证件号码
        String start = certificateNo.substring(0, 2);
        String end = certificateNo.substring(certificateNo.length() - 2, certificateNo.length());
        certificateNo = start + "**************" + end;
        submit_car_id.setText(certificateNo);
        submit_car_type.setText(baseEntity.getData().getCar_type().toString());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.examine_tv) {
            if ("licence".equals(str)) {
                finish();
                SkipUtils.toCheckHaveDrivingLicence(this, name, certificateNo, fileNo);
            } else {
                finish();
                SkipUtils.toCheckHaveDrivingLicense(this, name, carNo, vin);
            }

        }
    }

}
