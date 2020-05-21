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
import com.leshan.ylyj.presenter.DringLicensePrensent;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.mylibrary.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rxfamily.entity.AddDriverLicenseEntity;
import rxfamily.entity.AddDrivingEntity;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.DrivingLicenseOutputValueEntity;


/**
 * 核对行驶证
 */
public class CheckDrivingLicenseActivity extends BaseActivity implements View.OnClickListener {

    private TextView check_driving_license_submit_tv,checkDrivingIssueDate,checkDrivingRegisterDate;
    private EditText checkDrivingCarNo, checkDrivingCarName, checkDrivingBrandType, checkDrivingVin, checkDrivingEngineNo;
    CreditPresenter presenter;
    private String carNo, name,brandType, registerDate, issueDate, engineNo, vin, positive, back;

    private DrivingLicenseOutputValueEntity faceValue;
    private DrivingLicenseOutputValueEntity backValue;

    private DringLicensePrensent mPresent;

    private DringLicensePrensent mAddDrivingPresent;

    private Map<String,String> map=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_driving_license);
        initToolbar();
        setToolbarTitle("添加行驶证");
        hasBack(true);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }
    /**
     * 添加行驶证
     * @param map
     */
    private void addDrivingCard(Map<String,String>map){
        startMyDialog(false);
        mAddDrivingPresent.addDringCard(map);
    }

    @Override
    protected void initView() {
        check_driving_license_submit_tv = (TextView) findViewById(R.id.check_driving_license_submit_tv);
        checkDrivingCarNo = (EditText) findViewById(R.id.check_driving_car_no);
        checkDrivingCarName = (EditText) findViewById(R.id.check_driving_car_name);
        checkDrivingBrandType = (EditText) findViewById(R.id.check_driving_brand_type);
        checkDrivingVin = (EditText) findViewById(R.id.check_driving_vin);
        checkDrivingEngineNo = (EditText) findViewById(R.id.check_driving_engine_no);
        checkDrivingRegisterDate = (TextView) findViewById(R.id.check_driving_register_date);
        checkDrivingIssueDate = (TextView) findViewById(R.id.check_driving_issue_date);

        mAddDrivingPresent=new DringLicensePrensent(mContext,this,1);

    }

    @Override
    protected void initListener() {
        checkDrivingRegisterDate.setOnClickListener(this);
        checkDrivingIssueDate.setOnClickListener(this);
        check_driving_license_submit_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
        mPresent=new DringLicensePrensent(mContext,this);
        faceValue=getIntent().getParcelableExtra("value_face");
        backValue=getIntent().getParcelableExtra("value_back");
        setDrivingMap(faceValue);
        setDrivingMap(backValue);

    }
    private void setDrivingMap(DrivingLicenseOutputValueEntity valueEntity){
        if (valueEntity!=null){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyyMMdd");

            if (!TextUtils.isEmpty(valueEntity.plateNum)){
                map.put("carNo",valueEntity.plateNum);
                checkDrivingCarNo.setText(valueEntity.plateNum);
            }
            if (!TextUtils.isEmpty(valueEntity.registerDate)){
                String formTime=formDate(valueEntity.registerDate);
                map.put("registerDate",valueEntity.registerDate);
                checkDrivingRegisterDate.setText(formTime);
            }
            if (!TextUtils.isEmpty(valueEntity.engineNum)){
                map.put("engineNo",valueEntity.engineNum);
                checkDrivingEngineNo.setText(valueEntity.engineNum);
            }
            if (!TextUtils.isEmpty(valueEntity.vin)){
                map.put("vin",valueEntity.vin);
                checkDrivingVin.setText(valueEntity.vin);
            }
            if (!TextUtils.isEmpty(valueEntity.vehicleType)){
                map.put("brandType",valueEntity.vehicleType);
                checkDrivingBrandType.setText(valueEntity.vehicleType);
            }
            if (!TextUtils.isEmpty(valueEntity.owner)){
                map.put("name",valueEntity.owner);
                checkDrivingCarName.setText(valueEntity.owner);
            }
            if (!TextUtils.isEmpty(valueEntity.issueDate)){
                String formTime=formDate(valueEntity.issueDate);
                map.put("issueDate",valueEntity.issueDate);
                checkDrivingIssueDate.setText(formTime);
            }
            String back=getIntent().getStringExtra("back");
            String positive=getIntent().getStringExtra("positive");
            map.put("back",back);
            map.put("positive",positive);
        }
    }

    /**
     * 格式化时间：格式yyyy年MM月dd日
     * @param date
     * @return
     */
    private String formDate(String date){
        String sfTime=null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sf=new java.text.SimpleDateFormat("yyyyMMdd");
        try {
            Date time=sf.parse(date);
            long timeLong=time.getTime();
            sfTime=TimeUtils.millis2String(timeLong,simpleDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sfTime;
    }

    public boolean setDrivingInfo() {
        carNo = checkDrivingCarNo.getText().toString();
        if (TextUtils.isEmpty(carNo)){
            showToast("请输入车牌号");
            return false;
        }else {
            map.put("carNo",carNo);
        }
        name = checkDrivingCarName.getText().toString();
        if (TextUtils.isEmpty(name)){
            showToast("请填写所有人");
            return false;
        }else {
            map.put("name",name);
        }

        brandType = checkDrivingBrandType.getText().toString();
        if (TextUtils.isEmpty(brandType)){
            showToast("请填写车牌型号");
            return false;
        }else {
            map.put("brandType",brandType);
        }
        vin = checkDrivingVin.getText().toString();
        if (TextUtils.isEmpty(vin)){
            showToast("请填写车辆识别代号");
            return false;
        }else {
            map.put("vin",vin);
        }
        engineNo = checkDrivingEngineNo.getText().toString();
        if (TextUtils.isEmpty(engineNo)){
            showToast("请填写发动机号码");
            return false;
        }else {
            map.put("engineNo",engineNo);
        }
        registerDate = checkDrivingRegisterDate.getText().toString();
        if (TextUtils.isEmpty(registerDate)){
            showToast("请选择注册日期");
            return false;
        }else {
            String formTime=submitTime(registerDate);
            map.put("registerDate",formTime);
        }
        issueDate = checkDrivingIssueDate.getText().toString();
        if (TextUtils.isEmpty(issueDate)){
            showToast("请选择发证日期");
            return false;
        }else {
            String formTime=submitTime(issueDate);
            map.put("issueDate",formTime);
        }
        return true;
    }

    /**
     * 转化为服务器要求的时间格式
     * @param formTime
     * @return
     */
    private String submitTime(String formTime){
        String time=null;
        SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        try {
            Date date=sf.parse(formTime);
            long longTime=date.getTime();
            time=TimeUtils.millis2String(longTime,simpleDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();
        } else if (i == R.id.check_driving_license_submit_tv) {//                SkipUtils.toCheckCorrectDrivingLicense(this);
            if (setDrivingInfo()){
                addDrivingCard(map);
            }
        }else if (i ==R.id.check_driving_register_date){
            showDatePicker(checkDrivingRegisterDate);
        }else if (i==R.id.check_driving_issue_date){
            showDatePicker(checkDrivingIssueDate);
        }
    }
    @Override
    public void onCompleted() {
       stopMyDialog();
    }

    @Override
    public void onError(Throwable e) {
        stopMyDialog();

    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("添加结果");
        if (baseEntity instanceof AddDriverLicenseEntity){
            Logger.i("行驶证添加成功，时间："+((AddDriverLicenseEntity) baseEntity).getData().getCreateDate().toString());
            ToastUtil.showMessage(mContext,"行驶证添加成功！");
            finish();
            SkipUtils.toSubmitDrivingLicence(this, "license");
        }
        if (baseEntity instanceof AddDrivingEntity){
            showToast(baseEntity.msg);
            finish();
            AppManager.getInstance().killActivity(UploadingDrivingLicenceActivity.class);
            AppManager.getInstance().killActivity(DrivingLicenseActivity.class);
            SkipUtils.toSubmitDrivingLicence(this, "license");
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
}
