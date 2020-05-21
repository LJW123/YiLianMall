package com.yilian.mall.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.AreaListAdapter;
import com.yilian.mall.adapter.ListViewGridViewAdapter;
import com.yilian.mall.entity.ProvinceList;
import com.yilian.mall.entity.User_info;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.utils.LocationUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.SideBar;
import com.yilian.mall.widgets.SideBar.setOnTouchLetterChangedListener;
import com.yilian.mylibrary.Constants;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 地区选择 ：省列表界面
 */
public class AreaSelectionActivity extends BaseActivity implements OnClickListener {

    /**
     * 返回键
     */
    @ViewInject(R.id.tv_back)
    TextView mTxtBack;

//	@ViewInject(R.id.right_textview)
//	private TextView right_textview;

    @ViewInject(R.id.list_province_2)
    private ListView listProvince;

    @ViewInject(R.id.sidebar)
    SideBar sidebar;

    public static AreaSelectionActivity activity;

    private AreaListAdapter adapter;
    private TextView positionAddress;
    private LinearLayout viewLatestCountyTitle;
    private LinearLayout viewLatestCounty;
    private TextView tvLatestCounty1;
    private TextView tvLatestCounty2;
    private TextView tvLatestCounty3;

    //	String locationCity;
    private String[] latestCountyNames;
    private String[] latestCountyIds;
    private String[] latestProvinceIds;
    private String[] latestCityIds;
    private AccountNetRequest netRequest;

    ArrayList<ProvinceList.province> provinces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_province_listview);

        netRequest = new AccountNetRequest(mContext);

        activity = this;

        ViewUtils.inject(this);
        initHeader();
        init();
        initSideBar();
        setListener();
        setData();
    }

    /**
     * 初始化listView的头部
     */
    private void initHeader() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.activity_area_province_listview_head, null);
        positionAddress = (TextView) view.findViewById(R.id.positionAddress);
        viewLatestCountyTitle = (LinearLayout) view.findViewById(R.id.ll_zuijinfangwen);
        viewLatestCounty = (LinearLayout) view.findViewById(R.id.ll_zuijinfangwen_county);
        tvLatestCounty1 = (TextView) view.findViewById(R.id.tv_latest_county1);
        tvLatestCounty2 = (TextView) view.findViewById(R.id.tv_latest_county2);
        tvLatestCounty3 = (TextView) view.findViewById(R.id.tv_latest_county3);

        listProvince.addHeaderView(view);
    }

    /**
     * 右侧字母导航初始化
     */
    private void initSideBar() {
        sidebar.setListener(new setOnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String s) {
                final int position = adapter.getPositionForSelection(s.charAt(0));
                if (position != -1) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            listProvince.setSelection(position + 1);//头部位置为0，需要加1

                        }
                    });
                }
            }
        });
    }

    /**
     * 最近三个访问记录
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_latest_county1:
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_ID, latestProvinceIds[0], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, latestCountyIds[0], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, latestCountyNames[0], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_CITY_ID, latestCityIds[0], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION, LocationUtil.getJSLocationInfo(latestCityIds[0],
                        latestCountyIds[0], latestProvinceIds[0]), mContext);
                showToast(latestCountyNames[0]);
                //刷新主页面标识
                sp.edit().putBoolean(Constants.REFRESH_HOME_FRAGMENT, true).commit();
                break;
            case R.id.tv_latest_county2:
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_ID, latestProvinceIds[1], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, latestCountyIds[1], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, latestCountyNames[1], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_CITY_ID, latestCityIds[1], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION, LocationUtil.getJSLocationInfo(latestCityIds[1],
                        latestCountyIds[1], latestProvinceIds[1]), mContext);
                showToast(latestCountyNames[1]);
                //刷新主页面标识
                sp.edit().putBoolean(Constants.REFRESH_HOME_FRAGMENT, true).commit();
                break;
            case R.id.tv_latest_county3:
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_ID, latestProvinceIds[2], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, latestCountyIds[2], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, latestCountyNames[2], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_CITY_ID, latestCityIds[2], mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_LOCATION, LocationUtil.getJSLocationInfo(latestCityIds[2],
                        latestCountyIds[2], latestProvinceIds[2]), mContext);
                showToast(latestCountyNames[2]);
                //刷新主页面标识
                sp.edit().putBoolean(Constants.REFRESH_HOME_FRAGMENT, true).commit();
                break;

            default:
                break;
        }
        finish();
    }

    private void init() {
        mTxtBack.setText("地区选择");
        String address = PreferenceUtils.readStrConfig(Constants.SPKEY_LOCATION_ADDRESS, mContext);
        positionAddress.setText(address);
        String latestProvinceIdStr = PreferenceUtils.readStrConfig(Constants.SPKEY_LATEST_PROVINCE_ID, mContext, "");
        String latestCountyIdStr = PreferenceUtils.readStrConfig(Constants.SPKEY_LATEST_COUNTY_ID, mContext, "");
        String latestCountyNameStr = PreferenceUtils.readStrConfig(Constants.SPKEY_LATEST_COUNTY_NAME, mContext, "");
        String latestCityIdStr = PreferenceUtils.readStrConfig(Constants.SPKEY_LATEST_CITY_ID, mContext, "");
        if (!TextUtils.isEmpty(latestProvinceIdStr) && !TextUtils.isEmpty(latestCountyIdStr) && !TextUtils.isEmpty(latestCountyNameStr) && !TextUtils.isEmpty(latestCityIdStr)) {
            latestProvinceIds = latestProvinceIdStr.split(",");
            latestCountyIds = latestCountyIdStr.split(",");
            latestCountyNames = latestCountyNameStr.split(",");
            latestCityIds = latestCityIdStr.split(",");
            viewLatestCountyTitle.setVisibility(View.VISIBLE);
            viewLatestCounty.setVisibility(View.VISIBLE);
            for (int i = 0; i < latestCountyNames.length; i++) {
                TextView tv = (TextView) viewLatestCounty.getChildAt(i);
                tv.setText(latestCountyNames[i]);
                tv.setVisibility(View.VISIBLE);
                tv.setOnClickListener(this);
            }
        } else {
            viewLatestCountyTitle.setVisibility(View.GONE);
            viewLatestCounty.setVisibility(View.GONE);
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void positionAddress(View view) throws JSONException {

        if (!sp.getString(Constants.SPKEY_LOCATION_CITY_ID, "0").equals("0")) {

            SharedPreferences.Editor edit = sp.edit();
            edit.putString(Constants.SPKEY_SELECT_CITY_ID,
                    sp.getString(Constants.SPKEY_LOCATION_CITY_ID, "0"));
            edit.putString(Constants.SPKEY_SELECT_CITY_NAME,
                    sp.getString(Constants.SPKEY_LOCATION_CITY_NAME, "未定位"));

            edit.putString(Constants.SPKEY_SELECT_COUNTY_NAME,
                    sp.getString(Constants.SPKEY_LOCATION_COUNTY_NAME, "0"));
            edit.putString(Constants.SPKEY_SELECT_COUNTY_ID,
                    sp.getString(Constants.SPKEY_LOCATION_COUNTY_ID, "0"));
            edit.commit();
            String provinceId = sp.getString(Constants.SPKEY_SELECT_PROVINCE_ID, "0");
            String cityId = sp.getString(Constants.SPKEY_SELECT_CITY_ID, "0");
            String countyId = sp.getString(Constants.SPKEY_SELECT_COUNTY_ID, "0");
            if (!cityId.equals("0")) {
                User_info info = new User_info();
                info.setCity(cityId);
                info.setDistrict(countyId);

                ListViewGridViewAdapter.saveLatestCity(provinceId, PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, mContext), countyId, cityId, mContext);
                finish();
                //刷新主页面标识
                sp.edit().putBoolean(Constants.REFRESH_HOME_FRAGMENT, true).commit();
            }
        }
    }

    private void setListener() {
        tvLatestCounty1.setOnClickListener(this);
        tvLatestCounty2.setOnClickListener(this);
        tvLatestCounty3.setOnClickListener(this);
        listProvince.setOnItemClickListener(new OnItemClickListener() {

            @SuppressLint("CommitPrefEdits")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                ProvinceList.province province = (ProvinceList.province) parent.getItemAtPosition(position);
                Intent intent = new Intent(AreaSelectionActivity.this, AreaCitySelectionAcitivty.class);
                intent.putExtra("region_id", province.regionId);
                intent.putExtra("region_name", province.regionName);
                sp.edit().putString("province_name", province.regionName).commit();
                sp.edit().putString(Constants.SPKEY_SELECT_PROVINCE_ID, province.regionId).commit();

                startActivity(intent);

            }
        });
    }

    private void setData() {
        startMyDialog();
        netRequest.provinceList(new RequestCallBack<ProvinceList>() {
            @Override
            public void onSuccess(ResponseInfo<ProvinceList> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                    case -4:
                        provinces = responseInfo.result.provinces;
                        Collections.sort(provinces, new Comparator<ProvinceList.province>() {
                            @Override
                            public int compare(ProvinceList.province lhs, ProvinceList.province rhs) {
                                return lhs.agencyId.compareTo(rhs.agencyId);
                            }

                        });


                        adapter = new AreaListAdapter(mContext, provinces);
                        listProvince.setAdapter(adapter);
                        stopMyDialog();

                        break;

                    default:
                        showToast("获取省份列表信息失败");
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("获取省份列表信息失败");
                stopMyDialog();
            }
        });

    }

    /**
     * 省界面的搜索框
     *
     * @param view
     */
    public void findcity(View view) {
        Intent intent = new Intent(AreaSelectionActivity.this, FindActivity.class);
        intent.putExtra("type", "city");
        startActivity(intent);
    }

}
