package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ListViewGridViewAdapter;
import com.yilian.mall.entity.AreaInfo;
import com.yilian.mall.entity.CityList;
import com.yilian.mall.http.AccountNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 地区选择：市县
 */
public class AreaCitySelectionAcitivty extends BaseActivity{
	ArrayList<AreaInfo> city ;

	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	
	@ViewInject(R.id.city_text)
	private TextView city_text;

	@ViewInject(R.id.lv_city_county)
	ListView lvCityCounty;


	ListViewGridViewAdapter treeViewAdapter;
	private String provinceName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_city_selection);
		ViewUtils.inject(this);
		provinceName = getIntent().getStringExtra("region_name");
		tv_back.setText(provinceName);
		startMyDialog();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setData();
	}
	private void setData() {
		final String provinceId=getIntent().getStringExtra("region_id");

		new AccountNetRequest(mContext).city(provinceId, new RequestCallBack<CityList>() {
			@Override
			public void onSuccess(ResponseInfo<CityList> responseInfo) {
				stopMyDialog();
				switch (responseInfo.result.code){
					case 1:
					case -4:
						treeViewAdapter = new ListViewGridViewAdapter(AreaCitySelectionAcitivty.this,responseInfo.result.cities,sp,provinceId,provinceName);
						lvCityCounty.setAdapter(treeViewAdapter);
						break;

					default:
						showToast("获取"+tv_back.getText()+"信息失败");
						break;

				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				stopMyDialog();
				showToast("获取"+tv_back.getText()+"信息失败");
			}
		});

	}	
	
	/**
	 * 市区界面的搜索框
	 * @param view
	 */
	public void findcity(View view){
		Intent intent = new Intent(AreaCitySelectionAcitivty.this, FindActivity.class);
		intent.putExtra("type", "city");
		startActivity(intent);
	}

}
