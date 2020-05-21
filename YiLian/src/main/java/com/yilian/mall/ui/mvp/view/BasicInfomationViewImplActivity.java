package com.yilian.mall.ui.mvp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.common.base.Objects;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.SexEntity;
import com.yilian.mall.ui.mvp.presenter.BasicInformationPresenterImpl;
import com.yilian.mall.ui.mvp.presenter.IBasicInformationPresenter;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.BasicInformationEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.functions.Consumer;
import rx.Subscription;

/**
 * @author
 *         基础信息界面
 */
public class BasicInfomationViewImplActivity extends BaseAppCompatActivity implements IBasicInfomationView, View.OnClickListener {
    /**
     * 选择职业
     */
    public static final int PROFESSION_REQUEST_CODE = 77;
    /**
     * 选择家乡地址
     */
    public static final int HOME_TOWN_REQUEST_CODE = 0;
    /**
     * 选择现居住地地址
     */
    public static final int LOCATION_REQUEST_CODE = 1;

    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvTitle;
    private TextView tvContent;
    private IBasicInformationPresenter basicInformationPresenter;
    private TextView tvSexContent;
    private TextView tvAgeContent;
    private TextView tvBirthContent;
    private TextView tvBloodTypeContent;
    private EditText etSchoolContent;
    private EditText etCompanyContent;
    private TextView tvJobContent;
    private TextView tvHomeTownContent;
    private TextView tvLocationContent;
    private Button btnSaveData;
    private String sexType = "0";
    private long birthTimestamp;
    private String professionName = "";
    private int profession;
    private String homeProvinceId = "";
    private String homeCityId = "";
    private String homeCountyId = "";
    private String homeProvinceName = "";
    private String homeCityName = "";
    private String homeCountyName = "";
    private String locationProvinceId = "";
    private String locationCityId = "";
    private String locationCountyId = "";
    private String locationProvinceName = "";
    private String locationCityName = "";
    private String locationCountyName = "";
    private View includeSex;
    private View includeBirth;
    private View includeAge;
    private View includeBloodType;
    private View includeJob;
    private View includeHomeTown;
    private View includeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_infomation);
        basicInformationPresenter = new BasicInformationPresenterImpl(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        RxUtil.clicks(btnSaveData, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Subscription subscription = basicInformationPresenter.saveData(mContext, getData());
                addSubscription(subscription);
            }
        });
        RxUtil.clicks(includeBloodType, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showBloodPickerView();
            }
        });
        RxUtil.clicks(includeSex, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showSexPickerView();
            }
        });
        RxUtil.clicks(includeBirth, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showBirthdayPicker(tvBirthContent);
            }
        });
        RxUtil.clicks(includeJob, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, ProfessionViewImplActivity.class);
                intent.putExtra("pro_name", professionName);
                intent.putExtra("pro_id", profession);
                startActivityForResult(intent, PROFESSION_REQUEST_CODE);
            }
        });
        RxUtil.clicks(includeHomeTown, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, LocationViewImplActivity.class);
                intent.putExtra("address", tvHomeTownContent.getText().toString().trim());
                intent.putExtra("title", "家乡");
                startActivityForResult(intent, HOME_TOWN_REQUEST_CODE);
            }
        });
        RxUtil.clicks(includeLocation, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, LocationViewImplActivity.class);
                intent.putExtra("address", tvLocationContent.getText().toString().trim());
                intent.putExtra("title", "所在地");
                startActivityForResult(intent, LOCATION_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case PROFESSION_REQUEST_CODE:
                professionName = data.getStringExtra("pro_name");
                profession = data.getIntExtra("pro_id", 0);
                tvJobContent.setText(professionName);
                tvJobContent.setGravity(Gravity.LEFT);
                break;
            case HOME_TOWN_REQUEST_CODE:
                homeProvinceId = data.getStringExtra("ProvinceId");
                homeProvinceName = data.getStringExtra("RegionName");
                homeCityId = data.getStringExtra("CityId");
                homeCityName = data.getStringExtra("CityName");
                homeCountyId = data.getStringExtra("CountyId");
                homeCountyName = data.getStringExtra("CountyName");
                String homeAddress = "";
                if (!TextUtils.isEmpty(homeProvinceName)) {
                    homeAddress = homeProvinceName;
                }
                if (!TextUtils.isEmpty(homeCityName)) {
                    homeAddress += ("-" + homeCityName);
                }
                if (!TextUtils.isEmpty(homeCountyName)) {
                    homeAddress += ("-" + homeCountyName);
                }
                if (!TextUtils.isEmpty(homeAddress)) {
                    tvHomeTownContent.setText(homeAddress);
                }
                break;
            case LOCATION_REQUEST_CODE:
                locationProvinceId = data.getStringExtra("ProvinceId");
                locationProvinceName = data.getStringExtra("RegionName");
                locationCityId = data.getStringExtra("CityId");
                locationCityName = data.getStringExtra("CityName");
                locationCountyId = data.getStringExtra("CountyId");
                locationCountyName = data.getStringExtra("CountyName");
                String locationAddress = "";
                if (!TextUtils.isEmpty(locationProvinceName)) {
                    locationAddress = locationProvinceName;
                }
                if (!TextUtils.isEmpty(locationCityName)) {
                    locationAddress += ("-" + locationCityName);
                }
                if (!TextUtils.isEmpty(locationCountyName)) {
                    locationAddress += ("-" + locationCountyName);
                }
                if (!TextUtils.isEmpty(locationAddress)) {
                    tvLocationContent.setText(locationAddress);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 时间选择的滚轮
     *
     * @param view
     */
    private void showBirthdayPicker(TextView view) {
//        开始时间限制2017年7月1日
        Calendar calendarStart = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendarStart.set(Calendar.MONTH, 1 - 1);
        calendarStart.set(Calendar.YEAR, 1950);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
//        默认和截止时间限制 当天
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendarEnd = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendarEnd.set(Calendar.MINUTE, 0);
        //设置默认当前的时间
        String title = "选择时间";

        new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中时间回调
                Timestamp timestamp = new Timestamp(date.getTime());
                birthTimestamp = timestamp.getTime() / 1000;
                view.setText(DateUtils.timeStampToStr2(birthTimestamp));
                Calendar selectDay = Calendar.getInstance();
                selectDay.setTime(date);
                tvAgeContent.setText(String.valueOf(calendarEnd.get(Calendar.YEAR) - selectDay.get(Calendar.YEAR)));
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDividerColor(Color.DKGRAY)
                .setTitleText(title)
                .setContentSize(20)
                .setDate(calendarEnd)
                .setRangDate(calendarStart, calendarEnd)//设置起止时间
                .build()
                .show();
    }

    @Override
    public void onBackPressed() {
        Logger.i("lastSex:" + lastSex + "tvSexContent.getText().toString().trim():" + tvSexContent.getText().toString().trim() + "\n" +
                "  lastBirth:" + lastBirth + "tvBirthContent.getText().toString().trim():" + tvBirthContent.getText().toString().trim() + "\n" +
                "  lastAge:" + lastAge + "tvAgeContent.getText().toString().trim():" + tvAgeContent.getText().toString().trim() + "\n" +
                "  lastBlood:" + lastBlood + "tvBloodTypeContent.getText().toString().trim():" + tvBloodTypeContent.getText().toString().trim() + "\n" +
                "  lastSchool:" + lastSchool + "etSchoolContent.getText().toString().trim():" + etSchoolContent.getText().toString().trim() + "\n" +
                "  lastCompany:" + lastCompany + "etCompanyContent.getText().toString().trim()" + etCompanyContent.getText().toString().trim() + "\n" +
                "  lastJob:" + lastJob + "tvJobContent.getText().toString().trim():" + tvJobContent.getText().toString().trim() + "\n" +
                "  lastHome:" + lastHome + "tvHomeTownContent.getText().toString().trim():" + tvHomeTownContent.getText().toString().trim() + "\n" +
                "  lastLocation:" + lastLocation + "tvLocationContent.getText().toString().trim():" + tvLocationContent.getText().toString().trim()
        );
        if (
                !lastSex.equals(tvSexContent.getText().toString().trim()) ||
                        !lastBirth.equals(tvBirthContent.getText().toString().trim()) ||
                        !lastAge.equals(tvAgeContent.getText().toString().trim()) ||
                        !lastBlood.equals(tvBloodTypeContent.getText().toString().trim()) ||
                        !lastSchool.equals(etSchoolContent.getText().toString().trim()) ||
                        !lastCompany.equals(etCompanyContent.getText().toString().trim()) ||
                        !lastJob.equals(tvJobContent.getText().toString().trim()) ||
                        !lastHome.equals(tvHomeTownContent.getText().toString().trim()) ||
                        !lastLocation.equals(tvLocationContent.getText().toString().trim())
                ) {
            showSystemV7Dialog("温馨提示", "您修改的信息还没有保存，是否保存？", "保存", "放弃",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            switch (i) {
                                case DialogInterface.BUTTON_NEGATIVE:
                                    finish();
                                    break;
                                case DialogInterface.BUTTON_POSITIVE:
                                    basicInformationPresenter.saveData(mContext, getData());
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 血型选择器
     */
    private void showBloodPickerView() {
        ArrayList<String> bloodSts = new ArrayList<>();
        bloodSts.add("A");
        bloodSts.add("B");
        bloodSts.add("AB");
        bloodSts.add("O");
        OptionsPickerView pickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String bloodType = bloodSts.get(options1);
                tvBloodTypeContent.setText(bloodType);
            }
        }).setContentTextSize(20).build();
        pickerView.setPicker(bloodSts);
        pickerView.show();
    }

    /**
     * 性别选择器
     */
    private void showSexPickerView() {
        ArrayList<SexEntity> sexEntities = new ArrayList<>();
        sexEntities.add(new SexEntity("男", "1"));
        sexEntities.add(new SexEntity("女", "2"));
        OptionsPickerView pickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                SexEntity sexEntity = sexEntities.get(options1);
                sexType = sexEntity.sexType;
                tvSexContent.setText(sexEntity.sex);
            }
        }).setContentTextSize(20).build();
        pickerView.setPicker(sexEntities);
        pickerView.show();
    }

    private void initData() {
        Subscription subscription = basicInformationPresenter.getData(mContext);
        addSubscription(subscription);
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("基本信息");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);

        includeSex = findViewById(R.id.include_sex);
        TextView tvSexTitle = includeSex.findViewById(R.id.tv_title);
        tvSexTitle.setText("性别");
        tvSexContent = includeSex.findViewById(R.id.tv_content);
//        tvSexContent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tvSexContent.setText("");
        includeAge = findViewById(R.id.include_age);
        TextView tvAgeTitle = includeAge.findViewById(R.id.tv_title);
        tvAgeTitle.setText("年龄");
        tvAgeContent = includeAge.findViewById(R.id.tv_content);
        tvAgeContent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tvAgeContent.setText("");
        includeBirth = findViewById(R.id.include_birth);
        TextView tvBirthTitle = includeBirth.findViewById(R.id.tv_title);
        tvBirthTitle.setText("生日");
        tvBirthContent = includeBirth.findViewById(R.id.tv_content);
//        tvBirthContent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        tvBirthContent.setText("");
        includeBloodType = findViewById(R.id.include_blood_type);
        TextView tvBloodTypeTitle = includeBloodType.findViewById(R.id.tv_title);
        tvBloodTypeTitle.setText("血型");
        tvBloodTypeContent = includeBloodType.findViewById(R.id.tv_content);
        tvBloodTypeContent.setText("");
        View includeSchool = findViewById(R.id.include_school);
        TextView tvSchoolTitle = includeSchool.findViewById(R.id.tv_title);
        tvSchoolTitle.setText("学校");
        etSchoolContent = includeSchool.findViewById(R.id.et_content);
        etSchoolContent.setText("");
        View includeCompany = findViewById(R.id.include_company);
        TextView tvCompanyTitle = includeCompany.findViewById(R.id.tv_title);
        tvCompanyTitle.setText("公司");
        etCompanyContent = includeCompany.findViewById(R.id.et_content);
        etCompanyContent.setText("");
        includeJob = findViewById(R.id.include_job);
        TextView tvJobTitle = includeJob.findViewById(R.id.tv_title);
        tvJobTitle.setText("职业");
        tvJobContent = includeJob.findViewById(R.id.tv_content);
        tvJobContent.setText("");
        includeHomeTown = findViewById(R.id.include_hometown);
        TextView tvHomeTownTitle = includeHomeTown.findViewById(R.id.tv_title);
        tvHomeTownTitle.setText("家乡");
        tvHomeTownContent = includeHomeTown.findViewById(R.id.tv_content);
        tvHomeTownContent.setText("");
        includeLocation = findViewById(R.id.include_location);
        TextView tvLocationTitle = includeLocation.findViewById(R.id.tv_title);
        tvLocationTitle.setText("所在地");
        tvLocationContent = includeLocation.findViewById(R.id.tv_content);
        tvLocationContent.setText("");

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnSaveData = (Button) findViewById(R.id.btn_save_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_save_data:
                break;
            default:
                break;
        }
    }

    @Override
    public BasicInformationEntity.DataBean getData() {
        BasicInformationEntity basicInformationEntity = new BasicInformationEntity();
        BasicInformationEntity.DataBean data = basicInformationEntity.new DataBean();
        data.sex = sexType;
        data.age = tvAgeContent.getText().toString().trim();
        data.birthday = String.valueOf(birthTimestamp);
        data.bloodType = tvBloodTypeContent.getText().toString().trim();
        data.school = etSchoolContent.getText().toString().trim();
        data.company = etCompanyContent.getText().toString().trim();
        data.profession = String.valueOf(profession);
        data.professionName = professionName;
        data.homeProvice = homeProvinceId;
        data.homeProviceCn = homeProvinceName;
        data.homeCounty = homeCountyId;
        data.homeCountyCn = homeCountyName;
        data.homeCity = homeCityId;
        data.homeCityCn = homeCityName;
        data.currentProvince = locationProvinceId;
        data.currentProvinceCn = locationProvinceName;
        data.currentCity = locationCityId;
        data.currentCityCn = locationCityName;
        data.currentCounty = locationCountyId;
        data.currentCountyCn = locationCountyName;
        return data;

    }

    String lastSex = "";
    String lastAge = "";
    String lastBirth = "";
    String lastBlood = "";
    String lastSchool = "";
    String lastCompany = "";
    String lastJob = "";
    String lastHome = "";
    String lastLocation = "";

    @Override
    public void setData(BasicInformationEntity basicInformationEntity) {
        BasicInformationEntity.DataBean data = basicInformationEntity.data;
        sexType = data.sex;
        if (Objects.equal(data.sex, "1")) {
            tvSexContent.setText("男");
            lastSex = "男";
        } else if (Objects.equal(data.sex, "2")) {
            tvSexContent.setText("女");
            lastSex = "女";
        } else {
            tvSexContent.setHint("未设置");
            lastSex = "";
        }
        if (TextUtils.isEmpty(data.age)) {
            tvAgeContent.setHint("0");
            lastAge = "";
        } else {
            tvAgeContent.setText(data.age);
            lastAge = data.age;
        }
        if (TextUtils.isEmpty(data.birthday)) {
            tvBirthContent.setHint("未设置");
            lastBirth = "";
        } else {
            Logger.i("birthday:" + data.birthday);
            String birth = DateUtils.timeStampToStr2(NumberFormat.convertToLong(data.birthday, 0L));
            tvBirthContent.setText(birth);
            lastBirth = birth;
            birthTimestamp = NumberFormat.convertToLong(data.birthday, 0L);
        }
        if (TextUtils.isEmpty(data.bloodType)) {
            tvBloodTypeContent.setHint("未设置");
            lastBlood = "";
        } else {
            tvBloodTypeContent.setText(data.bloodType);
            lastBlood = data.bloodType;
        }
        if (TextUtils.isEmpty(data.school)) {
            etSchoolContent.setHint("填写学校");
            lastSchool = "";
        } else {
            etSchoolContent.setText(data.school);
            lastSchool = data.school;
        }
        if (TextUtils.isEmpty(data.company)) {
            etCompanyContent.setHint("填写公司");
            lastCompany = "";
        } else {
            etCompanyContent.setText(data.company);
            lastCompany = data.company;
        }
        if (TextUtils.isEmpty(data.professionName)) {
            tvJobContent.setText("待完善");
            lastJob = "待完善";
            tvJobContent.setGravity(Gravity.RIGHT);
        } else {
            professionName = data.professionName;
            profession = NumberFormat.convertToInt(data.profession, 0);
            tvJobContent.setText(professionName);
            lastJob = professionName;
            tvJobContent.setGravity(Gravity.LEFT);
        }
        homeProvinceName = data.homeProviceCn;
        homeProvinceId = data.homeProvice;
        homeCityName = data.homeCityCn;
        homeCityId = data.homeCity;
        homeCountyName = data.homeCountyCn;
        homeCountyId = data.homeCounty;
        String homeTown = "";
        if (!TextUtils.isEmpty(data.homeProviceCn)) {
            homeTown = data.homeProviceCn;
        }
        if (!TextUtils.isEmpty(data.homeCityCn)) {
            homeTown += ("-" + data.homeCityCn);
        }
        if (!TextUtils.isEmpty(data.homeCountyCn)) {
            homeTown += ("-" + data.homeCountyCn);
        }
        if (TextUtils.isEmpty(homeTown)) {
            tvHomeTownContent.setText("待完善");
            lastHome = "待完善";
            tvHomeTownContent.setGravity(Gravity.RIGHT);
        } else {
            tvHomeTownContent.setText(homeTown);
            lastHome = homeTown;
            tvHomeTownContent.setGravity(Gravity.LEFT);
        }
        locationProvinceName = data.currentProvinceCn;
        locationProvinceId = data.currentProvince;
        locationCityName = data.currentCityCn;
        locationCityId = data.currentCity;
        locationCountyName = data.currentCountyCn;
        locationCountyId = data.currentCounty;
        String location = "";
        if (!TextUtils.isEmpty(data.currentProvinceCn)) {
            location += data.currentProvinceCn;
        }
        if (!TextUtils.isEmpty(data.currentCityCn)) {
            location += ("-" + data.currentCityCn);
        }
        if (!TextUtils.isEmpty(data.currentCountyCn)) {
            location += ("-" + data.currentCountyCn);
        }
        if (TextUtils.isEmpty(location)) {
            tvLocationContent.setText("待完善");
            lastLocation = "待完善";
            tvLocationContent.setGravity(Gravity.RIGHT);
        } else {
            tvLocationContent.setText(location);
            lastLocation = location;
            tvLocationContent.setGravity(Gravity.LEFT);
        }
    }


}
