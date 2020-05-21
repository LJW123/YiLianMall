package com.yilian.mall.ui.mvp.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.mvp.presenter.ILocationPresenter;
import com.yilian.mall.ui.mvp.presenter.LocationPresenterImpl;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.RegionEntity2;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import rx.Subscription;

/**
 * @author
 *         选择居住地和家乡
 */
public class LocationViewImplActivity extends BaseAppCompatActivity implements ILocationView, View.OnClickListener {

    private ILocationPresenter iLocationPresenter;
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
    private TextView tvLocation;
    private TextView tvSelectAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view_impl);
        iLocationPresenter = new LocationPresenterImpl(this);

        initView();
        initData();
        initListener();
    }

    private String provinceId;
    private String cityId;
    private String countyId;

    /**
     * 弹出省市县选择器
     */
    @Override
    public void showArea(ArrayList<RegionEntity2.ProvincesBean> provinceList, ArrayList<ArrayList<RegionEntity2.ProvincesBean.CitysBean>> cityList
            , ArrayList<ArrayList<ArrayList<RegionEntity2.ProvincesBean.CitysBean.CountysBean>>> countyList) {
        if (provinceList == null || provinceList.size() <= 0 || cityList == null || cityList.size() <= 0 || countyList == null || countyList.size() <= 0) {
            showToast("请稍后再试");
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                RegionEntity2.ProvincesBean provinceBean = provinceList.get(options1);
                provinceId = provinceBean.regionId;
                String provinceName = provinceBean.regionName;
                RegionEntity2.ProvincesBean.CitysBean citysBean = cityList.get(options1).get(options2);
                cityId = citysBean.regionId;
                String cityName = citysBean.regionName;
                RegionEntity2.ProvincesBean.CitysBean.CountysBean countysBean = countyList.get(options1).get(options2).get(options3);
                countyId = countysBean.regionId;
                String countyName = countysBean.regionName;
                tvSelectAddress.setText(provinceBean.getPickerViewText() + "-" + citysBean.getPickerViewText() + "-" + countysBean.getPickerViewText());
                Intent data = new Intent();
                data.putExtra("ProvinceId", provinceId);
                data.putExtra("RegionName", provinceName);
                data.putExtra("CityId", cityId);
                data.putExtra("CityName", cityName);
                data.putExtra("CountyId", countyId);
                data.putExtra("CountyName", countyName);
                setResult(0, data);
            }
        })

                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(provinceList, cityList, countyList);
        pvOptions.show();
    }

    @Override
    public void setLocation(com.yilian.networkingmodule.entity.Location location) {
        Intent data = new Intent();
        com.yilian.networkingmodule.entity.Location.location location1 = location.location;
        data.putExtra("ProvinceId", location1.provinceId);
        data.putExtra("RegionName", location1.provinceName);
        data.putExtra("CityId", location1.cityId);
        data.putExtra("CityName", location1.cityName);
        data.putExtra("CountyId", location1.countyId);
        data.putExtra("CountyName", location1.countyName);
        setResult(0, data);
        String address = "";
        if (!TextUtils.isEmpty(location1.provinceName)) {
            address += location1.provinceName;
        }
        if (!TextUtils.isEmpty(location1.cityName)) {
            address += ("-" + location1.cityName);
        }
        if (!TextUtils.isEmpty(location1.countyName)) {
            address += ("-" + location1.countyName);
        }
        tvSelectAddress.setText(TextUtils.isEmpty(address) ? "设置失败,请手动输入" : address);
    }

    private void initListener() {
        RxUtil.clicks(tvSelectAddress, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Subscription subscription = iLocationPresenter.getAddressList(mContext);
                addSubscription(subscription);
            }
        });
        RxUtil.clicks(tvLocation, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Subscription subscription = iLocationPresenter.getLocation(mContext);
                addSubscription(subscription);
            }
        });
    }

    private void initData() {

    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText(getIntent().getStringExtra("title"));
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvSelectAddress = (TextView) findViewById(R.id.tv_select_address);
        String address = getIntent().getStringExtra("address");
        tvSelectAddress.setText(address);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
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
            case R.id.tv_location:

                break;
        }
    }
}
