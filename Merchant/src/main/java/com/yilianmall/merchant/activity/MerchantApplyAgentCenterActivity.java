package com.yilianmall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.RegionEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantApplyAgentCenterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvCounty;
    private EditText merchantEdittext;
    private Button btnApply;
    private ArrayList<RegionEntity.ProvincesBean> provincesBeanArrayList;
    private boolean getAddressFailure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_apply_agent_center);
        initView();
        getAddressInfo();
    }

    private void getAddressInfo() {
        startMyDialog(false);
        RetrofitUtils.getInstance(mContext).getRegion(new Callback<RegionEntity>() {
            @Override
            public void onResponse(Call<RegionEntity> call, Response<RegionEntity> response) {
                stopMyDialog();
                RegionEntity body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                provincesBeanArrayList = body.list;
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RegionEntity> call, Throwable t) {
                stopMyDialog();
                getAddressFailure = true;
            }
        });
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("申请服务中心");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        tvProvince = (TextView) findViewById(R.id.tv_province);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvCounty = (TextView) findViewById(R.id.tv_county);
        merchantEdittext = (EditText) findViewById(R.id.merchant_edittext);
        btnApply = (Button) findViewById(R.id.btn_apply);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvProvince.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvCounty.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
        } else if (i == R.id.btn_apply) {
            submit();
        } else if (i == R.id.tv_province) {
            showProvinceOptionPicker();
        } else if (i == R.id.tv_city) {
            showCityOptionPicker();
        } else if (i == R.id.tv_county) {
            showCountyOptionPicker();
        }
    }

    int provinceOption;//选择省份的option 用来确定市列表数据
    int cityOption;//选择市的option，用来确定区县的列表数据
    int countOption;

    /**
     * 省 选择器
     */
    void showProvinceOptionPicker() {
        if (getAddressFailure) {
            showToast("获取地址列表失败");
            return;
        }
        if (provincesBeanArrayList == null || provincesBeanArrayList.size() <= 0) {
            showToast("正在加载数据,请稍后再试");
            return;
        }
        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                RegionEntity.ProvincesBean provinceBeans = provincesBeanArrayList.get(options1);
                provinceOption = options1;
                tvProvince.setText(provinceBeans.getPickerViewText());
                if (provinceId != provinceBeans.regionId) {//省份改变了，市、县都要重置
                    provinceId = provinceBeans.regionId;
                    cityId = null;
                    countyId = null;
                }
            }
        }).setContentTextSize(20).build();
        optionsPickerView.setPicker(provincesBeanArrayList);
        optionsPickerView.show();
    }

    void showCityOptionPicker() {
        if (provinceId == null) {
            showToast("请选择省份");
            return;
        }
        RegionEntity.ProvincesBean ProvinceBeans = provincesBeanArrayList.get(provinceOption);
        final ArrayList<RegionEntity.ProvincesBean.CitysBean> citys = ProvinceBeans.citys;

        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                RegionEntity.ProvincesBean.CitysBean citysBean = citys.get(options1);
                cityOption = options1;
                tvCity.setText(citysBean.getPickerViewText());
                if (cityId != citysBean.regionId) {//市区改变了，县要重置
                    cityId = citysBean.regionId;
                    countyId = null;
                }
            }
        }).setContentTextSize(20).build();
        optionsPickerView.setPicker(citys);
        optionsPickerView.show();
    }

    void showCountyOptionPicker() {
        if (cityId == null) {
            showToast("请选择市区");
            return;
        }
        RegionEntity.ProvincesBean.CitysBean citysBean = provincesBeanArrayList.get(provinceOption).citys.get(cityOption);
        final ArrayList<RegionEntity.ProvincesBean.CitysBean.CountysBean> countys = citysBean.countys;
        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                RegionEntity.ProvincesBean.CitysBean.CountysBean countysBean = countys.get(options1);
                tvCounty.setText(countysBean.getPickerViewText());
                countyId = countysBean.regionId;
            }
        }).setContentTextSize(20).build();
        optionsPickerView.setPicker(countys);
        optionsPickerView.show();

    }

    private void submit() {
        // validate
        String edittext = merchantEdittext.getText().toString().trim();
        if (TextUtils.isEmpty(edittext)) {
            Toast.makeText(this, "简单描述一下您的申请意愿吧~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (provinceId == null) {
            showToast("至少选择一个省份");
            return;
        }

        // TODO validate success, do something
        applyAgent();

    }

    String provinceId;
    String cityId;
    String countyId;
    String remark;

    /**
     * 申请服务中心
     */
    private void applyAgent() {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext))
                .applyAgent(provinceId, cityId, countyId, remark, new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        BaseEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (com.yilian.mylibrary.CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        showToast("申请提交成功");
                                        //刷新个人页面标识
                                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                        //刷新主页面标识
                                        sp.edit().putBoolean(Constants.REFRESH_HOME_FRAGMENT, true).commit();
                                        com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.AGENT_STATUS, "1", mContext);
                                        Intent intent = new Intent();
                                        intent.setAction("com.yilianmall.merchant.changeAgentStatus");
                                        intent.putExtra("changeAgentStatus", body.code);
                                        mContext.sendBroadcast(intent);
                                        finish();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }
}
