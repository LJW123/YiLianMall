package com.leshan.ylyj.view.activity.creditmodel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.DialogChangeAdress;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxImageTool;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.QueryDriverLicenseEntity;

import static com.leshan.ylyj.view.activity.creditmodel.EditCarLicenseActivity.EDIT_SUCCESS_RESULT_CODE;


/**
 * 驾驶证核对无误
 */
public class CheckCorrectCarLicenseActivity extends BaseActivity implements View.OnClickListener {
    public static final int EDIT_REQUEST_CODE = 1;
    //                      编辑                    姓名      证件号     准驾车型      生效日期         失效日期         证件编号
    private TextView check_driving_licence_edit_tv, checkName, checkCarId, checkCartype, checkEffectiveDate, checkExpiryDate, checkFileNo;
    //                证件照主页       证件照副页
    private ImageView checkPositiveImg, checkBackImg;
    CreditPresenter presenter;
    private String id, checkPositiveUrl, checkBackUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_correct_car_license);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }


    @Override
    protected void initView() {
        check_driving_licence_edit_tv = (TextView) findViewById(R.id.check_driving_licence_edit_tv);
        checkName = (TextView) findViewById(R.id.check_name);
        checkCarId = (TextView) findViewById(R.id.check_car_id);
        checkCartype = (TextView) findViewById(R.id.check_cartype);
        checkEffectiveDate = (TextView) findViewById(R.id.check_effective_date);
        checkExpiryDate = (TextView) findViewById(R.id.check_expiry_date);
        checkFileNo = (TextView) findViewById(R.id.check_file_no);
        checkPositiveImg = (ImageView) findViewById(R.id.check_positive_img);
        checkBackImg = (ImageView) findViewById(R.id.check_back_img);
    }

    @Override
    protected void initListener() {
        check_driving_licence_edit_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getCarLicenseData();

    }

    private void getCarLicenseData() {
        //@net 初始化presenter，并绑定网络回调，
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
        startMyDialog();
        //查询驾驶证信息
        presenter.queryDriverLicenseInfo();
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("查询驾驶证详情核对成功");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("查询驾驶证详情核对异常：" + e.getMessage());
        stopMyDialog();
        ToastUtil.showMessage(mContext, e.getMessage());
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("查询驾驶证详情核对结果");
        if (baseEntity instanceof QueryDriverLicenseEntity) {
            //返回驾驶证信息进行赋值
            if ((QueryDriverLicenseEntity) baseEntity != null) {
                setDriverLicense((QueryDriverLicenseEntity) baseEntity);
            }
        } else {
            ToastUtil.showMessage(mContext, "删除驾驶证成功");
            finish();
            DialogChangeAdress.dialog.dismiss();
            SkipUtils.toUploadingDrivingLicence(this, "licence");//传入驾驶证标识
        }
    }

    //展示 姓名      证件号     准驾车型      生效日期         失效日期         证件编号
    public void setDriverLicense(QueryDriverLicenseEntity baseEntity) {
        checkName.setText(baseEntity.getData().getName().toString());
        checkCarId.setText(baseEntity.getData().getCertificate_no().toString());
        checkCartype.setText(baseEntity.getData().getCar_type().toString());
//        生效日期
        Logger.i("生效日期:" + DateUtils.convertDate(baseEntity.getData().getEffective_date().toString()));

        checkEffectiveDate.setText(DateUtils.convertDate(baseEntity.getData().getEffective_date().toString()));
//        失效日期
        Logger.i("失效日期:" + DateUtils.convertDate(baseEntity.getData().getExpiry_date().toString()));
        checkExpiryDate.setText(DateUtils.convertDate(baseEntity.getData().getExpiry_date().toString()));

        checkFileNo.setText(baseEntity.getData().getFile_no().toString());
        id = baseEntity.getData().getId().toString();
        checkPositiveUrl = baseEntity.getData().getPositive().toString();
        checkBackUrl = baseEntity.getData().getBack().toString();
        //证件照主页
        GlideUtil.showImageRadius(mContext, checkPositiveUrl, checkPositiveImg, RxImageTool.dp2px(3));
        //证件照副页
        GlideUtil.showImageRadius(mContext, checkBackUrl, checkBackImg, RxImageTool.dp2px(3));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EDIT_SUCCESS_RESULT_CODE) {
            getCarLicenseData();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.check_driving_licence_edit_tv) {
            DialogChangeAdress.EditDialog(this);

        } else if (i == R.id.check_car_edit) {
//            finish();
            //修改界面
            Intent intent = new Intent(mContext, EditCarLicenseActivity.class);
            startActivityForResult(intent, EDIT_REQUEST_CODE);
            DialogChangeAdress.dialog.dismiss();
        } else if (i == R.id.check_car_delete) {
            if (id != null) {
                //传入驾驶证id，删除驾驶证信息
                startMyDialog();
                presenter.deleteDriverLincenseInfo(id);
            }

        } else if (i == R.id.check_car_cancel) {
            DialogChangeAdress.dialog.dismiss();

        }
    }
}
