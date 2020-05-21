package com.leshan.ylyj.view.activity.creditmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxImageTool;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.DeleteDrivingInfo;
import rxfamily.entity.QueryDrivingEntity;

import static com.leshan.ylyj.view.activity.creditmodel.EdutDrivingLicenseActivity.EDIT_DRIVING_LICENSE_RESULT_CODE;


/**
 * 行车证核对无误 编辑界面
 */
public class CheckCorrectDrivingLicenseActivity extends BaseActivity implements View.OnClickListener {

    public static final int EDIT_DRIVING_LICENSE_REQUEST_CODE = 1;
    private TextView check_driving_license_edit_tv, checkDrivingCarNo, checkDrivingName, checkDrivingBrandType, checkDrivingVin, checkDrivingEngineNo, checkDrivingRegisterDate, checkDrivingIssueDate;
    //编辑dialog
    private AlertDialog dialog;
    private Window window;
    CreditPresenter presenter;
    private ImageView checkDrivingPositiveImg, checkDrivingBackImg;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_correct_driving_license);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        check_driving_license_edit_tv = (TextView) findViewById(R.id.check_driving_license_edit_tv);
        checkDrivingCarNo = (TextView) findViewById(R.id.check_driving_car_no);
        checkDrivingName = (TextView) findViewById(R.id.check_driving_name);
        checkDrivingBrandType = (TextView) findViewById(R.id.check_driving_brand_type);
        checkDrivingVin = (TextView) findViewById(R.id.check_driving_vin);
        checkDrivingEngineNo = (TextView) findViewById(R.id.check_driving_engine_no);
        checkDrivingRegisterDate = (TextView) findViewById(R.id.check_driving_register_date);
        checkDrivingIssueDate = (TextView) findViewById(R.id.check_driving_issue_date);
        checkDrivingPositiveImg = (ImageView) findViewById(R.id.check_driving_positive_img);
        checkDrivingBackImg = (ImageView) findViewById(R.id.check_driving_back_img);
    }

    @Override
    protected void initListener() {
        check_driving_license_edit_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getDrivingLicenseData();
    }

    private void getDrivingLicenseData() {
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
        startMyDialog();
        presenter.queryDrivingLicenseInfo();
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("查询行驶证信息成功");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("查询行驶证信息异常：" + e.getMessage());
        stopMyDialog();
        ToastUtil.showMessage(mContext, e.getMessage());
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("查询行驶证信息结果");
        if (baseEntity instanceof QueryDrivingEntity) {
            //返回行驶证信息
            if ((QueryDrivingEntity) baseEntity != null) {
                setDrivingInfo((QueryDrivingEntity) baseEntity);
            }
        } else if (baseEntity instanceof DeleteDrivingInfo) {
            Logger.i("行驶证删除：" + baseEntity.msg);
            ToastUtil.showMessage(mContext, "行驶证删除成功！");
            finish();
            dialog.dismiss();
            //跳转到个人信息页面
            SkipUtils.toUploadingDrivingLicence(this, "license");//传入行驶证标识
        }
    }

    public void setDrivingInfo(QueryDrivingEntity baseEntity) {
        id = baseEntity.getData().getId();
        checkDrivingCarNo.setText(baseEntity.getData().getCar_no().toString());
        checkDrivingName.setText(baseEntity.getData().getName().toString());
        checkDrivingBrandType.setText(baseEntity.getData().getBrand_type());
        checkDrivingVin.setText(baseEntity.getData().getVin().toString());
        checkDrivingEngineNo.setText(baseEntity.getData().getEngine_no().toString());
        //注册日期
        checkDrivingRegisterDate.setText(DateUtils.convertDate(baseEntity.getData().getRegister_date().toString()));
        //发证日期
        checkDrivingIssueDate.setText(DateUtils.convertDate(baseEntity.getData().getIssue_date().toString()));
        GlideUtil.showImageRadius(this, baseEntity.getData().getPositive(), checkDrivingPositiveImg, RxImageTool.dp2px(3));
        GlideUtil.showImageRadius(this, baseEntity.getData().getBack(), checkDrivingBackImg, RxImageTool.dp2px(3));
    }

    /**
     * 编辑dialog
     */
    private void EditDialog() {
        dialog = new AlertDialog.Builder(this, R.style.transverse_bespread_dialog).create();
        window = dialog.getWindow();
        dialog.show();
        View view = getLayoutInflater().inflate(R.layout.dialog_check_correct_car_license, null, false);
        TextView check_car_edit = (TextView) view.findViewById(R.id.check_car_edit);
        TextView check_car_delete = (TextView) view.findViewById(R.id.check_car_delete);
        TextView check_car_cancel = (TextView) view.findViewById(R.id.check_car_cancel);
        check_car_edit.setOnClickListener(this);
        check_car_delete.setOnClickListener(this);
        check_car_cancel.setOnClickListener(this);
//        AddressSelector address_edit_as=view.findViewById(R.id.address_edit_as);
        window.setContentView(view);
        //这句设置我们dialog在底部
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = 1200;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.check_driving_license_edit_tv) {
            EditDialog();

        } else if (i == R.id.check_car_edit) {
            Intent intent = new Intent(mContext, EdutDrivingLicenseActivity.class);
            startActivityForResult(intent, EDIT_DRIVING_LICENSE_REQUEST_CODE);
            dialog.dismiss();

        } else if (i == R.id.check_car_delete) {
            //删除行驶证
            presenter.deleteDrivingInfo(id);


        } else if (i == R.id.check_car_cancel) {
            dialog.dismiss();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EDIT_DRIVING_LICENSE_RESULT_CODE) {
            getDrivingLicenseData();
        }
    }
}
