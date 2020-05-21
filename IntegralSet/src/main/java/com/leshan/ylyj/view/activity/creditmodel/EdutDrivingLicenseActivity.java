package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.DialogPopup8;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxImageTool;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.MyDriverLicenseEntity;
import rxfamily.entity.QueryDrivingEntity;


/**
 * 编辑行车证
 */
public class EdutDrivingLicenseActivity extends BaseActivity implements View.OnClickListener {

    public static final int EDIT_DRIVING_LICENSE_RESULT_CODE = 1;
    private TextView edut_driving_license_confirm, register_tv, certificate_tv, sex_tv, birth_tv, get_date_tv;
    private DialogPopup8 dialogPopup8;
    private String[] strings1 = new String[]{"男", "女"};
    private EditText edutDrivingCarNo, edutDrivingName, edutDrivingBrandType, edutDrivingCarVin, edutDrivingEngineNo, edutAddress, edutNationality;
    private ImageView edutDrivingPositiveImg, edutDrivingBackImg;
    CreditPresenter presenter;
    private String id, carNo, name, sex, nationality, address, birthday, initialDate, brandType, registerDate, issueDate, engineNo, vin, positive, back;
    private Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edut_driving_license);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        //确认修改
        edut_driving_license_confirm = (TextView) findViewById(R.id.edut_driving_license_confirm);
        //注册
        register_tv = (TextView) findViewById(R.id.register_tv);
        //发证
        certificate_tv = (TextView) findViewById(R.id.certificate_tv);
        //车牌号
        edutDrivingCarNo = (EditText) findViewById(R.id.edut_driving_car_no);
        //姓名
        edutDrivingName = (EditText) findViewById(R.id.edut_driving_name);
        //性别
        sex_tv = (TextView) findViewById(R.id.sex_tv);
        //生日
        birth_tv = (TextView) findViewById(R.id.birth_tv);
        //初次领证日期
        get_date_tv = (TextView) findViewById(R.id.get_date_tv);
        //车牌型号
        edutDrivingBrandType = (EditText) findViewById(R.id.edut_driving_car_type);
        // vin 车辆识别代号
        edutDrivingCarVin = (EditText) findViewById(R.id.edut_driving_car_vin);
        //engineNo 发动机编号
        edutDrivingEngineNo = (EditText) findViewById(R.id.edut_driving_engine_no);
        //住址
        edutAddress = findViewById(R.id.address);
        //国籍
        edutNationality = findViewById(R.id.nationality_tv);
        edutDrivingPositiveImg = (ImageView) findViewById(R.id.edut_driving_positive_img);
        edutDrivingBackImg = (ImageView) findViewById(R.id.edut_driving_back_img);

    }


    @Override
    protected void initListener() {
        edut_driving_license_confirm.setOnClickListener(this);
        register_tv.setOnClickListener(this);
        certificate_tv.setOnClickListener(this);
        sex_tv.setOnClickListener(this);
        birth_tv.setOnClickListener(this);
        get_date_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //@net 初始化presenter，并绑定网络回调，
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
        ToastUtil.showMessage(mContext, "修改失败！");
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("查询行驶证信息结果");
        if (baseEntity instanceof QueryDrivingEntity) {
            //返回行驶证信息
            setDrivingInfo((QueryDrivingEntity) baseEntity);
        } else if (baseEntity instanceof MyDriverLicenseEntity) {
            ToastUtil.showMessage(this, "修改成功!");
            setResult(EDIT_DRIVING_LICENSE_RESULT_CODE);
            finish();
//            SkipUtils.toCheckCorrectDrivingLicense(mContext);
        }

    }

    /**
     * 查询行驶证未修改前的信息
     * carNo车牌号 name姓名 sex性别 nationality国籍 address地birthday出生日期	 initialDate初次领证日期	 brandTyp车牌型号
     * registerDate注册日期	issueDate发证日期	engineNo 发动机编号 vin 车辆识别代号 positive 证件照片主页 back 证件照片副页
     *
     * @param baseEntity
     */
    public void setDrivingInfo(QueryDrivingEntity baseEntity) {
        id = baseEntity.getData().getId().toString();
        carNo = baseEntity.getData().getCar_no();
        if (!TextUtils.isEmpty(carNo)) {
            edutDrivingCarNo.setText(carNo);
        }

        name = baseEntity.getData().getName();
        if (!TextUtils.isEmpty(name)) {
            edutDrivingName.setText(name);
        }
        //性别
        String sex = baseEntity.getData().getSex();
        if (("0").equals(sex)) {
            sex_tv.setText("女");
        } else if ("1".equals(sex)) {
            sex_tv.setText("男");
        } else {
            sex_tv.setText("");
        }

        nationality = baseEntity.getData().getNationality();
        if (!TextUtils.isEmpty(nationality)) {
            edutNationality.setText(nationality);
        }

        address = baseEntity.getData().getAddress();
        if (!TextUtils.isEmpty(address)) {
            edutAddress.setText(address);
        }

        birthday = baseEntity.getData().getBirthday();
        if (!TextUtils.isEmpty(birthday)) {
            birthday = DateUtils.convertDate(birthday);
            birth_tv.setText(birthday);
        }
        //初次领证日期
        initialDate = baseEntity.getData().getInitial_date();
        if (!TextUtils.isEmpty(initialDate)) {
            //转换年月日
            initialDate = DateUtils.convertDate(initialDate);
            get_date_tv.setText(initialDate);
        }
        //车牌型号
        brandType = baseEntity.getData().getBrand_type();
        Logger.i("查询行驶证-车牌型号-Before" + brandType);
        if (!TextUtils.isEmpty(brandType)) {
            edutDrivingBrandType.setText(brandType);
            Logger.i("查询行驶证-车牌型号-after" + edutDrivingBrandType.getText().toString());
        }

        //注册日期
        registerDate = baseEntity.getData().getRegister_date();
        Logger.i("查询行驶证-注册日期-Before" + registerDate);
        if (!TextUtils.isEmpty(registerDate)) {
            registerDate = DateUtils.convertDate(registerDate);
            register_tv.setText(registerDate);
            Logger.i("查询行驶证-注册日期-after" + register_tv.getText().toString());
        }

        //发证日期
        issueDate = baseEntity.getData().getIssue_date();
        Logger.i("查询行驶证-发证日期-Before" + issueDate);
        if (!TextUtils.isEmpty(issueDate)) {
            issueDate = DateUtils.convertDate(issueDate);
            Logger.i("查询行驶证-发证日期-after:issueDate" + issueDate);
            certificate_tv.setText(issueDate);
            Logger.i("查询行驶证-发证日期-after" + certificate_tv.getText().toString());
        }

        //发动机编号
        engineNo = baseEntity.getData().getEngine_no();
        Logger.i("查询行驶证-发动机编号-Before" + engineNo);
        if (!TextUtils.isEmpty(engineNo)) {
            edutDrivingEngineNo.setText(engineNo);
            Logger.i("查询行驶证-发动机编号-after" + edutDrivingEngineNo.getText().toString());
        }
        //车辆识别代号
        vin = baseEntity.getData().getVin();
        Logger.i("查询行驶证-车辆识别代号-Before" + vin);
        if (!TextUtils.isEmpty(vin)) {
            edutDrivingCarVin.setText(vin);
            Logger.i("查询行驶证-车辆识别代号-after" + edutDrivingCarVin.getText().toString());
        }
        //证件照片
        positive = baseEntity.getData().getPositive();
        if (!TextUtils.isEmpty(positive)) {
            GlideUtil.showImageRadius(this, positive, edutDrivingPositiveImg, RxImageTool.dp2px(3));
        }
        back = baseEntity.getData().getBack();
        if (!TextUtils.isEmpty(back)) {
            GlideUtil.showImageRadius(this, back, edutDrivingBackImg, RxImageTool.dp2px(3));
        }


    }

    /**
     * 弹出dialogPopup8选择框
     * 性别选择
     *
     * @param
     * @param strings1
     */
    private void DialogPopupShow(String[] strings1) {
        dialogPopup8 = new DialogPopup8(this, strings1);
        dialogPopup8.showPopupWindow();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.edut_driving_license_confirm) {
            //确认修改信息
            if (updateDrivingInfo()) {
                updateDriving(map);
            }
        } else if (i == R.id.register_tv) {
            DateUtils.showDatePickerToYear(mContext, register_tv);

        } else if (i == R.id.certificate_tv) {
            DateUtils.showDatePickerToYear(mContext, certificate_tv);

        } else if (i == R.id.sex_tv) {
            DialogPopupShow(strings1);

        } else if (i == R.id.birth_tv) {
            DateUtils.showDatePickerToYear(mContext, birth_tv);

        } else if (i == R.id.get_date_tv) {
            DateUtils.showDatePickerToYear(mContext, get_date_tv);

        } else if (i == R.id.record_sure_tv) {
            sex_tv.setText(dialogPopup8.picker.getContentByCurrValue());
            dialogPopup8.dismiss();

        } else if (i == R.id.record_cancel_tv) {
            dialogPopup8.dismiss();

        }
    }

    /**
     * 添加驾驶证
     *
     * @param map
     */
    private void updateDriving(Map<String, String> map) {
        startMyDialog(false);
        presenter.updateDriving(map);
    }

    /**
     * 修改行驶证信息
     * carNo车牌号 name姓名 sex性别 nationality国籍 address地birthday出生日期	 initialDate初次领证日期	 brandTyp车牌型号
     * registerDate注册日期	issueDate发证日期	engineNo 发动机编号 vin 车辆识别代号 positive 证件照片主页 back 证件照片副页
     */
    private boolean updateDrivingInfo() {
        map.put("id", id);
        carNo = edutDrivingCarNo.getText().toString();
        if (TextUtils.isEmpty(carNo)) {
            showToast("请输入车牌号");
            return false;
        } else {
            map.put("carNo", carNo);
        }
        name = edutDrivingName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("请输入姓名");
            return false;
        } else {
            map.put("name", name);
        }
        sex = sex_tv.getText().toString();
        if ("男".equals(sex)) {
            map.put("sex", "1");
        } else if ("女".equals(sex)) {
            map.put("sex", "0");
        } else {
            map.put("sex", "");
        }
        nationality = edutNationality.getText().toString();
        map.put("nationality", nationality);
        address = edutAddress.getText().toString();
        map.put("address", address);
        //生日
        birthday = birth_tv.getText().toString();
        if (!TextUtils.isEmpty(birthday)) {
            birthday = DateUtils.submitTime(birthday);
            map.put("birthday", birthday);
        }
        //初次
        initialDate = get_date_tv.getText().toString();
        if (!TextUtils.isEmpty(initialDate)) {
            initialDate = DateUtils.submitTime(initialDate);
            map.put("initialDate", initialDate);
        }
        registerDate = register_tv.getText().toString();
        if (TextUtils.isEmpty(registerDate)) {
            showToast("请输入注册日期");
            return false;
        } else {
            registerDate = DateUtils.submitTime(registerDate);
            map.put("registerDate", registerDate);
        }
        issueDate = certificate_tv.getText().toString();
        if (TextUtils.isEmpty(issueDate)) {
            showToast("请输入发证日期");
            return false;
        } else {
            issueDate = DateUtils.submitTime(issueDate);
            map.put("issueDate", issueDate);
        }
        brandType = edutDrivingBrandType.getText().toString();
        if (TextUtils.isEmpty(brandType)) {
            showToast("请输入车牌型号");
            return false;
        } else {
            map.put("brandType", brandType);
        }
        engineNo = edutDrivingEngineNo.getText().toString();
        if (TextUtils.isEmpty(engineNo)) {
            showToast("请输入发动机编号");
            return false;
        } else {
            map.put("engineNo", engineNo);
        }
        vin = edutDrivingCarVin.getText().toString();
        if (TextUtils.isEmpty(vin)) {
            showToast("请输入车辆识别代号");
            return false;
        } else {
            map.put("vin", vin);
        }
        if (!TextUtils.isEmpty(positive)) {
            map.put("positive", positive);
        }
        if (!TextUtils.isEmpty(back)) {
            map.put("back", back);
        }
        if (TextUtils.isEmpty(carNo) || TextUtils.isEmpty(name) || TextUtils.isEmpty(sex) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(initialDate) || TextUtils.isEmpty(registerDate) || TextUtils.isEmpty(issueDate) || TextUtils.isEmpty(brandType) || TextUtils.isEmpty(engineNo) || TextUtils.isEmpty(vin)) {
            showToast("您输入的证件信息不完整，请检查后再确认");
            return false;
        }
        return true;
    }

}
