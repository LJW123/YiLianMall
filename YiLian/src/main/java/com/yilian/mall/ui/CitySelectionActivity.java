package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.AreaListAdapter;
import com.yilian.mall.adapter.ListViewGridViewAdapter;
import com.yilian.mall.entity.CityList;
import com.yilian.mall.entity.ProvinceList;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.widgets.SideBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 城市选择界面
 * 该页面用于收货地址选择，与定位无关
 */
public class CitySelectionActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    @ViewInject(R.id.flayout_province)
    private FrameLayout fLayoutProvince;

    @ViewInject(R.id.lv_province)
    private ListView lvProvince;

    @ViewInject(R.id.sidebar)
    private SideBar sideBar;

    @ViewInject(R.id.lv_city_county)
    private ListView lvCityCounty;

    public AccountNetRequest netRequest;

    ArrayList<ProvinceList.province> provinces;

    public AreaListAdapter adapter;

    StringBuilder citySb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        ViewUtils.inject(this);

        netRequest = new AccountNetRequest(mContext);

        initView();

        Listener();

    }

    public void onBack(){
        Logger.i(tvBack.getText().toString());

        if (tvBack.getText().equals("选择省份")) {
            finish();
        } else {
            showProvince();
        }

    };


    private void initView() {
        tvBack.setText("选择省份");
        getProvinceList();

        sideBar.setListener(new SideBar.setOnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String s) {
                final int position = adapter.getPositionForSelection(s.charAt(0));
                if (position!=-1) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            lvProvince.setSelection(position);//头部位置为0，需要加1

                        }
                    });
                }
            }
        });
    }

    private void Listener() {
        lvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProvinceList.province province = (ProvinceList.province) parent.getItemAtPosition(position);
                citySb.setLength(0);
                citySb.append(province.regionName+","+province.regionId+",");
                getCityList(province.regionName,province.regionId);
                startMyDialog();
            }
        });
    }


    private void getProvinceList() {
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
                        lvProvince.setAdapter(adapter);
                        break;

                    default:
                        showToast("获取信息失败");
                        break;


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("获取信息失败，因为网络链接失败");
            }
        });

    }

    private void getCityList(final String provinceName, String provinceId) {
        netRequest.city(provinceId, new RequestCallBack<CityList>() {
            @Override
            public void onSuccess(ResponseInfo<CityList> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                    case -4:
                        lvCityCounty.setAdapter(new ListViewGridViewAdapter(mContext, responseInfo.result.cities,citySb,sp));
                        showCity();
                        tvBack.setText(provinceName);
                        break;

                    default:
                        showToast("获取" + provinceName + "信息失败，错误代码：" + responseInfo.result.code);
                        break;

                }
                ;
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("获取" + provinceName + "信息失败，因为网络链接失败");
                Logger.i(e.getExceptionCode()+"--"+s);
            }
        });
    }

    /**
     * 显示省份列表隐藏城市列表
     */
    private void showProvince() {
        fLayoutProvince.setVisibility(View.VISIBLE);
        lvCityCounty.setVisibility(View.GONE);
    }

    /**
     * 显示城市列表，隐藏省份列表
     */
    private void showCity() {
        fLayoutProvince.setVisibility(View.GONE);
        lvCityCounty.setVisibility(View.VISIBLE);
    }

}
