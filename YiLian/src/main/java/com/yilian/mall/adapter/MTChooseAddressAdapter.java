package com.yilian.mall.adapter;/**
 * Created by  on 2016/12/9 0009.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.yilian.mall.R;

import java.util.ArrayList;

/**
 * Created by  on 2016/12/9 0009.
 */
public class MTChooseAddressAdapter extends android.widget.BaseAdapter {
    private final Context context;
    private final ArrayList<PoiItem> list;

    public MTChooseAddressAdapter(Context context, ArrayList<PoiItem> list) {
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_searched_buildings, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PoiItem poiItem = list.get(position);
        if (position == 0) {
            viewHolder.tvBuildName.setTextColor(context.getResources().getColor(R.color.color_red));
            viewHolder.tvBuildName.setText("[推荐位置]" + poiItem.getTitle());
            viewHolder.tvBuildAddress.setText(poiItem.getSnippet());
        } else {
            viewHolder.tvBuildName.setTextColor(context.getResources().getColor(R.color.color_333));
            viewHolder.tvBuildAddress.setText(poiItem.getSnippet());
            viewHolder.tvBuildName.setText(poiItem.getTitle());
        }
        if(TextUtils.isEmpty(poiItem.getSnippet())){
            viewHolder.tvBuildAddress.setVisibility(View.GONE);
        }else{
            viewHolder.tvBuildAddress.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvBuildName;
        public TextView tvBuildAddress;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvBuildName = (TextView) rootView.findViewById(R.id.tv_build_name);
            this.tvBuildAddress = (TextView) rootView.findViewById(R.id.tv_build_address);
        }

    }
}
