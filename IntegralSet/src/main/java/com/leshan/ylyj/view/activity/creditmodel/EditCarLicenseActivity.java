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

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.MyDriverLicenseEntity;
import rxfamily.entity.QueryDriverLicenseEntity;


/**
 * 驾驶证编辑
 */
public class EditCarLicenseActivity extends BaseActivity implements View.OnClickListener {
    public static final int EDIT_SUCCESS_RESULT_CODE = 1;
    //                    确认修改                  生效日期        失效日期
    private TextView edit_card_license_confirm, lose_efficacy_tv, take_effect_tv, birth_tv, sex_tv, get_date_tv;
    private DialogPopup8 dialogPopup8;
    private String[] strings1 = new String[]{"男", "女"};
    CreditPresenter presenter;
    //                     证号         姓名    准驾车型  档案编号
    private EditText car_certificateNo, car_name, car_type, car_fileNo, edit_nationality, edit_address;
    private String id, certificateNo, name, sex, nationality, address, birthday, initialDate, carType, effectiveDate, expiryDate, fileNo, checkPositiveUrl, checkBackUrl, updateTime;
    private ImageView checkPositiveImg, checkBackImg;
    SimpleDateFormat sfToYear, sfToNum;
    private Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car_license);
        //接收上个页面传来的驾驶证信息
//        entity = (QueryDriverLicenseEntity) getIntent().getSerializableExtra("entity");
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        sfToYear = new SimpleDateFormat("yyyy年MM月dd日");
        sfToNum = new SimpleDateFormat("yyyyMMdd");
        car_fileNo = findViewById(R.id.car_fileNo);
        car_certificateNo = findViewById(R.id.car_certificateno);
        car_name = findViewById(R.id.car_name);
        car_type = findViewById(R.id.car_type);
        edit_card_license_confirm = (TextView) findViewById(R.id.edit_card_license_confirm);
        lose_efficacy_tv = (TextView) findViewById(R.id.lose_efficacy_tv);
        take_effect_tv = (TextView) findViewById(R.id.take_effect_tv);
        birth_tv = (TextView) findViewById(R.id.birth_tv);
        sex_tv = (TextView) findViewById(R.id.sex_tv);
        edit_nationality = findViewById(R.id.edit_nationality);
        edit_address = findViewById(R.id.edit_address);
        get_date_tv = (TextView) findViewById(R.id.get_date_tv);
        checkPositiveImg = (ImageView) findViewById(R.id.check_positive_img);
        checkBackImg = (ImageView) findViewById(R.id.check_back_img);

    }

    @Override
    protected void initListener() {
        lose_efficacy_tv.setOnClickListener(this);
        take_effect_tv.setOnClickListener(this);
        birth_tv.setOnClickListener(this);
        edit_card_license_confirm.setOnClickListener(this);
        sex_tv.setOnClickListener(this);
        get_date_tv.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        //@net 初始化presenter，并绑定网络回调，
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
        startMyDialog();
        presenter.queryDriverLicenseInfo();
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

        } else if (i == R.id.edit_card_license_confirm) {
            //确认修改信息
            if (updateDriver()) {
                updateDriverCard(map);
            }

        } else if (i == R.id.lose_efficacy_tv) {
            DateUtils.showDatePickerToYear(mContext, lose_efficacy_tv);

        } else if (i == R.id.take_effect_tv) {
            DateUtils.showDatePickerToYear(mContext, take_effect_tv);

        } else if (i == R.id.birth_tv) {
            DateUtils.showDatePickerToYear(mContext, birth_tv);

        } else if (i == R.id.sex_tv) {
            DialogPopupShow(strings1);

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
    private void updateDriverCard(Map<String, String> map) {
        startMyDialog(false);
        presenter.updateDriver(map);
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("修改成功");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("修改异常：" + e.getMessage());
        stopMyDialog();
        ToastUtil.showMessage(mContext, "修改失败！" + e.getMessage());
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("修改结果");
        if (baseEntity instanceof QueryDriverLicenseEntity) {
            //返回驾驶证信息进行赋值
            setDriverLicense((QueryDriverLicenseEntity) baseEntity);
        } else if (baseEntity instanceof MyDriverLicenseEntity) {
            updateTime = ((MyDriverLicenseEntity) baseEntity).getData().getUpdateDate().toString();
            ToastUtil.showMessage(mContext, "修改成功!");
            setResult(EDIT_SUCCESS_RESULT_CODE);
            finish();
//            SkipUtils.toCheckCorrectCarLicense(this);
        }
    }

    /**
     * 对修改页面进行初次查询赋值
     *
     * @param entity
     */
    public void setDriverLicense(QueryDriverLicenseEntity entity) {
        Logger.i("" + entity.getData().toString());
        id = entity.getData().getId().toString();
        //证件号
        certificateNo = entity.getData().getCertificate_no();
        if (!TextUtils.isEmpty(certificateNo)) {
            car_certificateNo.setText(certificateNo);
        }
        //姓名
        name = entity.getData().getName();
        if (!TextUtils.isEmpty(name)) {
            car_name.setText(name);
        }
        //性别
        String sex = entity.getData().getSex();
        if (("0").equals(sex)) {
            sex_tv.setText("女");
        } else if ("1".equals(sex)) {
            sex_tv.setText("男");
        } else {
            sex_tv.setText("");
        }
        //国籍
        nationality = entity.getData().getNationality();
        if (!TextUtils.isEmpty(nationality)) {
            edit_nationality.setText(nationality);
        }
        //地址
        address = entity.getData().getAddress();
        if (!TextUtils.isEmpty(address)) {
            edit_address.setText(address);
        }
        //生日
        birthday = entity.getData().getBirthday();
        if (!TextUtils.isEmpty(birthday)) {
            birthday = DateUtils.convertDate(birthday);
            birth_tv.setText(birthday);
        }
        //准驾车型
        carType = entity.getData().getCar_type();
        Logger.i("查询驾驶证-准驾车型-Before" + carType);
        if (!TextUtils.isEmpty(carType)) {
            car_type.setText(carType);
            Logger.i("查询驾驶证-准驾车型-after" + car_type.getText().toString());
        }
        //生效日期
        effectiveDate = entity.getData().getEffective_date();
        Logger.i("查询驾驶证-生效日期-Before" + effectiveDate);
        if (!TextUtils.isEmpty(effectiveDate)) {
            lose_efficacy_tv.setText(DateUtils.convertDate(effectiveDate));
            Logger.i("查询驾驶证-生效日期-after" + lose_efficacy_tv.getText().toString());
        }
        //失效日期
        expiryDate = entity.getData().getExpiry_date();
        Logger.i("查询驾驶证-失效日期-Before" + expiryDate);
        if (!TextUtils.isEmpty(expiryDate)) {
            take_effect_tv.setText(DateUtils.convertDate(expiryDate));
            Logger.i("查询驾驶证-失效日期-after" + take_effect_tv.getText().toString());
        }
        //档案编号
        fileNo = entity.getData().getFile_no();
        Logger.i("查询驾驶证-档案编号-Before" + fileNo);
        if (!TextUtils.isEmpty(fileNo)) {
            car_fileNo.setText(fileNo);
            Logger.i("查询驾驶证-档案编号-after" + car_fileNo.getText().toString());
        }
        //初次领证日期
        initialDate = entity.getData().getInitial_date();
        Logger.i("查询驾驶证-初次领证日期-Before" + initialDate);
        if (!TextUtils.isEmpty(initialDate)) {
            get_date_tv.setText(DateUtils.convertDate(initialDate));
            Logger.i("查询驾驶证-初次领证日期-after" + get_date_tv.getText().toString());
        }
        //证件照主页
        checkPositiveUrl = entity.getData().getPositive();
        Logger.i("查询驾驶证-证件照主页-Before" + checkPositiveUrl);
        if (!TextUtils.isEmpty(checkPositiveUrl)) {
            GlideUtil.showImageRadius(mContext, checkPositiveUrl, checkPositiveImg, RxImageTool.dp2px(3));
        }
        //证件照副页
        checkBackUrl = entity.getData().getBack();
        Logger.i("查询驾驶证-证件照副页-Before" + checkBackUrl);
        if (!TextUtils.isEmpty(checkBackUrl)) {
            //证件照副页
            GlideUtil.showImageRadius(mContext, checkBackUrl, checkBackImg, RxImageTool.dp2px(3));
        }

    }

    /**
     * 确认修改
     *
     * @return
     */
    public boolean updateDriver() {
        map.put("id", id);
        certificateNo = car_certificateNo.getText().toString();
        if (TextUtils.isEmpty(certificateNo)) {
            showToast("请输入证号");
            return false;
        } else {
            map.put("certificateNo", certificateNo);
        }

        name = car_name.getText().toString();
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

        nationality = edit_nationality.getText().toString();
        map.put("nationality", nationality);

        address = edit_address.getText().toString();
        map.put("address", address);

        birthday = birth_tv.getText().toString();
//        if (!TextUtils.isEmpty(birthday)) {
//            birthday = DateUtils.submitTime(birthday);
//            map.put("birthday", birthday);
//        }
        if (TextUtils.isEmpty(birthday)) {
            showToast("请输入出生日期");
            return false;
        } else {
            birthday = DateUtils.submitTime(birthday);
            map.put("birthday", birthday);
        }
        initialDate = get_date_tv.getText().toString();
        if (TextUtils.isEmpty(initialDate)) {
            showToast("请输入初次领证日期");
            return false;
        } else {
            initialDate = DateUtils.submitTime(initialDate);
            map.put("initialDate", initialDate);
        }

        carType = car_type.getText().toString();
        if (TextUtils.isEmpty(carType)) {
            showToast("请输入准驾车型");
            return false;
        } else {
            map.put("carType", carType);
        }

        effectiveDate = lose_efficacy_tv.getText().toString();
        if (TextUtils.isEmpty(effectiveDate)) {
            showToast("请输入生效日期");
            return false;
        } else {
            effectiveDate = DateUtils.submitTime(effectiveDate);
            map.put("effectiveDate", effectiveDate);
        }

        expiryDate = take_effect_tv.getText().toString();
        if (TextUtils.isEmpty(expiryDate)) {
            showToast("请输入失效日期");
            return false;
        } else {
            expiryDate = DateUtils.submitTime(expiryDate);
            map.put("expiryDate", expiryDate);
        }

        fileNo = car_fileNo.getText().toString();
        boolean result = fileNo.matches("[0-9]+");
        if (TextUtils.isEmpty(fileNo)) {
            showToast("请输入档案编号");
            return false;
        } else if (result == false) {
            showToast("档案编号格式不正确，请重新输入");
            return false;
        } else {
            map.put("fileNo", fileNo);
        }
        map.put("positive", checkPositiveUrl);
        map.put("back", checkBackUrl);
        if (TextUtils.isEmpty(certificateNo) || TextUtils.isEmpty(name) || TextUtils.isEmpty(sex) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(initialDate) || TextUtils.isEmpty(carType) || TextUtils.isEmpty(effectiveDate) || TextUtils.isEmpty(expiryDate) || TextUtils.isEmpty(fileNo)) {
            showToast("您输入的证件信息不完整，请检查后再确认");
            return false;
        }
        return true;
    }

}
