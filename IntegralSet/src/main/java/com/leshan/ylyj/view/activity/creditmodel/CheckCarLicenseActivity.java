package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rxfamily.entity.ALiCarSuccessDataInfo;
import rxfamily.entity.AddDriverLicenseEntity;
import rxfamily.entity.BaseEntity;


/**
 * 核对驾驶证信息，添加驾驶证
 */
public class CheckCarLicenseActivity extends BaseActivity implements View.OnClickListener {
    private TextView check_car_license_submit_tv, checkEffectivedate, checkExpirydate;//确定
    //                          证号          姓名      准驾车型      生效日期          失效日期         档案编号
    private EditText checkCertificateNo, checkName, checkCartype, checkFileno;
    CreditPresenter presenter;
    private String cerNo, name, carType, effDate, expirDate, fileNo, positive, back;
    ALiCarSuccessDataInfo info;
    private Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_car_license);
        initToolbar();
        setToolbarTitle("添加驾驶证");
        hasBack(true);
        info = (ALiCarSuccessDataInfo) getIntent().getSerializableExtra("bean");
        positive = getIntent().getStringExtra("positive");
        back = getIntent().getStringExtra("back");
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        check_car_license_submit_tv = (TextView) findViewById(R.id.check_car_license_submit_tv);
        checkCertificateNo = (EditText) findViewById(R.id.check_certificate_no);
        checkName = (EditText) findViewById(R.id.check_name);
        checkCartype = (EditText) findViewById(R.id.check_cartype);
        checkEffectivedate = (TextView) findViewById(R.id.check_effectivedate);
        checkExpirydate = (TextView) findViewById(R.id.check_expirydate);
        checkFileno = (EditText) findViewById(R.id.check_fileno);
        //设置识别后传来的值
        checkCertificateNo.setText(info.getNum());
        checkName.setText(info.getName().toString());
        checkCartype.setText(info.getVehicleType());
        //生效日期  20180112
        String startDate = info.getStartDate();
        //转成—>yyyy年MM月dd日
        startDate = DateUtils.formDate(startDate);
        Logger.i("生效日期:" + startDate);
        checkEffectivedate.setText(startDate);
        //失效日期  20180102
        String endDate = info.getEndDate();
        //转成—>yyyy年MM月dd日
        endDate = DateUtils.formDate(endDate);
        checkExpirydate.setText(endDate);

        checkFileno.setText(info.getArchiveNo());
    }

    @Override
    protected void initListener() {
        check_car_license_submit_tv.setOnClickListener(this);
        checkEffectivedate.setOnClickListener(this);
        checkExpirydate.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //@net 初始化presenter，并绑定网络回调，
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("添加驾驶证信息成功");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        stopMyDialog();
        Logger.i("添加驾驶证信息异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, "添加驾驶证信息失败");
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("添加驾驶证信息结果:" + ((AddDriverLicenseEntity) baseEntity).getData().toString());
        if (baseEntity instanceof AddDriverLicenseEntity) {
            if (((AddDriverLicenseEntity) baseEntity).getData() != null) {
                String createDate = ((AddDriverLicenseEntity) baseEntity).getData().getCreateDate();
                ToastUtil.showMessage(mContext, "驾驶证信息添加成功!");
                finish();
                AppManager.getInstance().killActivity(UploadingDrivingLicenceActivity.class);
                AppManager.getInstance().killActivity(MyCarLicenseActivity.class);
                SkipUtils.toSubmitDrivingLicence(this, "licence");//传入驾驶证标识

            }
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.check_car_license_submit_tv) {
            //添加驾驶证信息
            if (setDrivingInfo()) {
                addDriverCard(map);
            }
        } else if (i == R.id.check_effectivedate) {
            showDatePicker(checkEffectivedate);
        } else if (i == R.id.check_expirydate) {
            showDatePicker(checkExpirydate);
        }
    }
    /**
     * 时间选择的滚轮
     */
    private void showDatePicker(final TextView textView) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(1900,0 , 1);
        endDate.set(2200, 11, 31);

        new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中时间回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                textView.setText(format.format(date));
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setContentSize(20)
                .setDividerColor(Color.DKGRAY)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)//设置起止时间
                .build()
                .show();
    }


    public boolean setDrivingInfo() {
        cerNo = checkCertificateNo.getText().toString();
        if (TextUtils.isEmpty(cerNo)) {
            showToast("请输入证号");
            return false;
        } else {
            map.put("certificateNo", cerNo);
        }
        name = checkName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("请输入姓名");
            return false;
        } else {
            map.put("name", name);
        }
        carType = checkCartype.getText().toString();
        if (TextUtils.isEmpty(carType)) {
            showToast("请输入准驾车型");
            return false;
        } else {
            map.put("carType", carType);
        }
        fileNo = checkFileno.getText().toString().trim();
        boolean result = fileNo.matches("[0-9]+");
        if (TextUtils.isEmpty(fileNo)) {
            showToast("请输入档案编号");
            return false;
        } else if (result == false) {
            ToastUtil.showMessage(mContext, "档案编号格式不正确，请重新输入");
            return false;
        } else {
            map.put("fileNo", fileNo);
        }
        //生效日期      年 月 日 —> 20140101
        effDate = checkEffectivedate.getText().toString();
        if (TextUtils.isEmpty(effDate)) {
            showToast("请输入生效日期");
            return false;
        } else {
            effDate = DateUtils.submitTime(effDate);
            Logger.i("日期：effDate:" + effDate);
            map.put("effectiveDate", effDate);
        }
        //失效日期
        expirDate = checkExpirydate.getText().toString();
        if (TextUtils.isEmpty(expirDate)) {
            showToast("请输入失效日期");
            return false;
        } else {
            expirDate = DateUtils.submitTime(expirDate);
            Logger.i("日期：expirDate:" + expirDate);
            map.put("expiryDate", expirDate);
        }
        map.put("back", back);
        map.put("positive", positive);
        return true;
    }

    /**
     * 添加驾驶证
     *
     * @param map
     */
    private void addDriverCard(Map<String, String> map) {
        startMyDialog(false);
        presenter.addDriver(map);
    }


}
