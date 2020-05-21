package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.AreaInfo;
import com.yilian.mall.widgets.wheel.adapters.AbstractWheelTextAdapter;
import com.yilian.mall.widgets.wheel.views.OnWheelChangedListener;
import com.yilian.mall.widgets.wheel.views.OnWheelScrollListener;
import com.yilian.mall.widgets.wheel.views.WheelView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChangeAddressDialog extends Dialog implements View.OnClickListener,OnWheelChangedListener {

	//省市区控件
	private WheelView wvProvince;
	private WheelView wvCitys;
	private WheelView wvArea;
	
	private TextView btnSure;//确定按钮
	private TextView btnCancle;//取消按钮

	private Context context;//上下文对象


	private List<AreaInfo> mArrProvinces;
	private Map<String, List<AreaInfo>> mCitisDatasMap;
	private Map<String, List<AreaInfo>> mAreaDatasMap;


	
	private AddressTextAdapter provinceAdapter;
	private AddressTextAdapter cityAdapter;
	private AddressTextAdapter areaAdapter;

	//选中的省市区信息
	private AreaInfo strProvince;
	private AreaInfo strCity ;
	private AreaInfo strArea ;
	
	//回调方法
	private OnAddressCListener onAddressCListener;

	//显示文字的字体大小
	private int maxsize = 18;
	private int minsize = 14;

	private Handler mHandler=new Handler();

	public ChangeAddressDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changeaddress);

		wvProvince = (WheelView) findViewById(R.id.wv_address_province);
		wvCitys = (WheelView) findViewById(R.id.wv_address_city);
		wvArea = (WheelView) findViewById(R.id.wv_address_area);
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancle = (TextView) findViewById(R.id.btn_myinfo_cancel);

		btnSure.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
		wvProvince.addChangingListener(this);
		wvCitys.addChangingListener(this);
		wvArea.addChangingListener(this);

		wvProvince.setVisibleItems(5);

		wvCitys.setVisibleItems(5);

		wvArea.setVisibleItems(5);
		wvProvince.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
//				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
//				strProvince= (AreaInfo) provinceAdapter.getItemObject(wheel.getCurrentItem());
//				setTextviewSize(currentText, provinceAdapter);
				updateCities();
			}
		});
		wvCitys.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
//				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
//				strCity=(AreaInfo) cityAdapter.getItemObject(wheel.getCurrentItem());
//				setTextviewSize(currentText, cityAdapter);
				updateAreas();
			}
		});
		wvArea.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
				strArea = (AreaInfo) areaAdapter.getItemObject(wheel.getCurrentItem());
				setTextviewSize(currentText, areaAdapter);
			}
		});
	}



	public void setAddressData(List<AreaInfo> arrProvinces,Map<String, List<AreaInfo>> citisDatasMap,Map<String, List<AreaInfo>> areaDatasMap){
		mArrProvinces = arrProvinces;
		mCitisDatasMap = citisDatasMap;
		mAreaDatasMap = areaDatasMap;

		strProvince= mArrProvinces.get(0);
		List<AreaInfo> citys=mCitisDatasMap.get(strProvince.getRegion_id());
		if (citys!=null&&citys.size()>0){
			strCity=citys.get(0);
			List<AreaInfo> areas=mAreaDatasMap.get(strCity.getRegion_id());
			if (areas!=null&&areas.size()>0){
				strArea=areas.get(0);
			}
		}
		provinceAdapter = new AddressTextAdapter(context, mArrProvinces, 0, maxsize, minsize);
		wvProvince.setViewAdapter(provinceAdapter);
		wvProvince.setCurrentItem(0);
		cityAdapter = new AddressTextAdapter(context, mCitisDatasMap.get(strProvince.getRegion_id()), 0, maxsize, minsize);
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(0);
		areaAdapter = new AddressTextAdapter(context, mAreaDatasMap.get(strCity.getRegion_id()), 0, maxsize, minsize);
		wvArea.setViewAdapter(areaAdapter);
		wvArea.setCurrentItem(0);
	}

	
	//根据省来更新wheel的状态
	private void updateCities()
	{
		String currentText = (String) provinceAdapter.getItemText(wvProvince.getCurrentItem());
		strProvince = (AreaInfo) provinceAdapter.getItemObject(wvProvince.getCurrentItem());
		Logger.i("updateCities updateCities 调用--" + strProvince.getRegion_name());
		setTextviewSize(currentText, provinceAdapter);
//		cityAdapter = new AddressTextAdapter(mContext, mCitisDatasMap.get(strProvince.getRegion_id()), 0, maxsize, minsize);
		cityAdapter.update(mCitisDatasMap.get(strProvince.getRegion_id()));
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(0);
		updateAreas();
	}
	
	//根据城市来更新wheel的状态
	private void updateAreas()
	{
		String currentText = (String) cityAdapter.getItemText(wvCitys.getCurrentItem());
		strCity = (AreaInfo) cityAdapter.getItemObject(wvCitys.getCurrentItem());
		Logger.i("updateAreas updateAreas 调用--" + strCity.getRegion_name());
		setTextviewSize(currentText, cityAdapter);
//		areaAdapter = new AddressTextAdapter(mContext, mAreaDatasMap.get(strCity.getRegion_id()), 0, maxsize, minsize);
		areaAdapter.update(mAreaDatasMap.get(strCity.getRegion_id()));
		wvArea.setViewAdapter(areaAdapter);
		wvArea.setCurrentItem(0);
		strArea = (AreaInfo) areaAdapter.getItemObject(wvArea.getCurrentItem());
	}
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
//		if (wheel == wvProvince)
//		{
//			//切换省份的操作
//			updateCities();
//		} else if (wheel == wvCitys)
//		{
//			updateAreas();
//		} else if (wheel == wvArea)
//		{
//			String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
//			strArea = (AreaInfo) areaAdapter.getItemObject(wheel.getCurrentItem());
//			Log.i("onChanged","area_onChanged 调用--"+strArea.getRegion_name());
////			strArea = mAreaDatasMap.get(strCity)[newValue];
//			setTextviewSize(currentText, areaAdapter);
//		}
	}
	
////////////////////////////////////////////////////华丽的分界线	

	public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(18);
			} else {
				textvew.setTextSize(14);
			}
		}
	}

	public void setAddresskListener(OnAddressCListener onAddressCListener) {
		this.onAddressCListener = onAddressCListener;
	}

	@Override
	public void onClick(View v) {
		if (v == btnSure) {
			if (onAddressCListener != null) {
				onAddressCListener.onClick(strProvince, strCity, strArea);
			}
		}
		if (v == btnCancle) {
			dismiss();
		}
		dismiss();
	}


	public interface OnAddressCListener {
		void onClick(AreaInfo province, AreaInfo city, AreaInfo area);
	}

	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		List<AreaInfo> list;

		protected AddressTextAdapter(Context context, List<AreaInfo> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		public void update(List<AreaInfo> list){
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			if (list!=null){
				return list.size();
			}
			return 0;
		}

		@Override
		protected CharSequence getItemText(int index) {
			if (list!=null&&list.size()>0){
				return list.get(index).getRegion_name();
			}
			return "";
		}

		@Override
		protected Object getItemObject(int index) {
			if (list!=null&&list.size()>0){
				return list.get(index);
			}
			return null;
		}
	}
}