package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.ProvinceList;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AreaListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<ProvinceList.province> list;

	public AreaListAdapter(Context context, ArrayList<ProvinceList.province> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.area_list, null);
			holder = new ViewHolder();
			holder.province = (TextView) convertView.findViewById(R.id.list_province);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.alpha_bottom_line = convertView.findViewById(R.id.alpha_bottom_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.province.setText(list.get(position).regionName);
	
		// 当前字母
		String currentStr = getAlpha(list.get(position).agencyId);

		// 前面的字母
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).agencyId) : " ";
		
		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha_bottom_line.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
			holder.alpha_bottom_line.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	public int getPositionForSelection(int selection) {
		for (int i = 0; i < getCount(); i++) {
			char c = list.get(i).agencyId.charAt(0);
			if (c == selection) {
				return i;
			}
		}
		return -1;
	}
	
	private static class ViewHolder {
		public TextView province;
		public TextView alpha;
		public View alpha_bottom_line;
	}

	/**
	 * 获取首字母
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式匹配
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 将小写字母转换为大写
		} else {
			return "#";
		}
	}
}
