package com.yilian.mall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.CityList;
import com.yilian.mall.entity.GetUserInfoEntity;
import com.yilian.mall.entity.ProvinceList;
import com.yilian.mall.ui.AreaCitySelectionAcitivty;
import com.yilian.mall.ui.AreaSelectionActivity;
import com.yilian.mall.ui.CitySelectionActivity;
import com.yilian.mall.utils.LocationUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

public class ListViewGridViewAdapter extends BaseAdapter {

    private static SharedPreferences sp;
    private  String provinceName;
    StringBuilder citySb;
    CitySelectionActivity activity;
    private LayoutInflater inflater;
    private ArrayList<CityList.province> citys;
    private Context context; // 上下文
    private String provinceId;//省份ID

    public ListViewGridViewAdapter(Context context, ArrayList<CityList.province> cities, SharedPreferences sp) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.citys = cities;
        this.sp = sp;

    }

    public ListViewGridViewAdapter(Context context, ArrayList<CityList.province> cities, StringBuilder citySb, SharedPreferences sp) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.citys = cities;
        this.citySb = citySb;
        this.sp = sp;

    }

    public ListViewGridViewAdapter(AreaCitySelectionAcitivty context, ArrayList<CityList.province> cities, SharedPreferences sp, String provinceId,String provinceName) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.citys = cities;
        this.sp = sp;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        Logger.i("传递过来的定位的省份ID:" + provinceId);


    }

    /**
     * 保存区县选择记录
     *
     * @param provinceId
     * @param name       区域名称
     * @param id         区域id
     * @param cityId     区域所属城市id
     */
    public static void saveLatestCity(String provinceId, String name, String id, String cityId, Context context) {
        if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty
                (cityId)) {
            return;
        }
        String latestCityIdStr = PreferenceUtils.readStrConfig(Constants.SPKEY_LATEST_CITY_ID, context, "");
        if (!TextUtils.isEmpty(latestCityIdStr)) {//如果之前存入的城市ID记录不为空，则使用","分隔，然后遍历，只有新选择的地址和之前记录地址ID不同时才记录
            String[] latestProvinceIds = latestCityIdStr.split(",");
            for (int i = 0; i < latestProvinceIds.length; i++) {
                if (latestProvinceIds[i].equals(cityId)) {//如果新选择的地址和之前记录地址ID相同，则直接结束该方法，不保存新选中地址
                    Logger.i("latestProvinceIds[i]:"+latestProvinceIds[i]+"  latestCityIdStr:"+latestCityIdStr);
                    return;
                }
            }
        }
        int currNum = PreferenceUtils.readIntConfig
                (Constants.SPKEY_LATEST_COUNTY_NUM, context, 0);
        int replaceIndex = currNum % 3;
        String savedProvinceIdStr = PreferenceUtils.readStrConfig
                (Constants.SPKEY_LATEST_PROVINCE_ID, context, "");
        String savedIdStr = PreferenceUtils.readStrConfig
                (Constants.SPKEY_LATEST_COUNTY_ID, context, "");
        String savedNameStr = PreferenceUtils.readStrConfig
                (Constants.SPKEY_LATEST_COUNTY_NAME, context, "");
        String savedCityIdStr = PreferenceUtils.readStrConfig
                (Constants.SPKEY_LATEST_CITY_ID, context, "");
        if (TextUtils.isEmpty(savedProvinceIdStr) && TextUtils.isEmpty(savedIdStr) && TextUtils.isEmpty
                (savedNameStr) && TextUtils.isEmpty(savedCityIdStr)) {

            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_PROVINCE_ID, provinceId, context);
            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_COUNTY_ID, id, context);
            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_COUNTY_NAME, name, context);
            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_CITY_ID, cityId, context);
        } else if (!TextUtils.isEmpty(savedIdStr) && !
                TextUtils.isEmpty(savedNameStr) && !TextUtils.isEmpty(savedCityIdStr)) {
            String[] saveProvinceArr = savedProvinceIdStr.split(",");
            String[] saveIdArr = savedIdStr.split(",");
            String[] saveNameArr = savedNameStr.split
                    (",");
            String[] saveCityIdArr = savedCityIdStr.split
                    (",");
            int len = saveIdArr.length;
            if (len == saveNameArr.length && len == saveCityIdArr.length && len == saveProvinceArr.length) {
                for (int i = 0; i < saveIdArr.length;
                     i++) {
                    if (provinceId.equals(saveProvinceArr[i]) && id.equals(saveIdArr[i])
                            && name.equals(saveNameArr[i])) {
                        return;
                    }
                }

                if (len == 3) {
                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_PROVINCE_ID,
                            savedProvinceIdStr.replace(saveProvinceArr[replaceIndex], id), context);

                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_COUNTY_ID,
                            savedIdStr.replace(saveIdArr[replaceIndex], id), context);

                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_COUNTY_NAME,
                            savedNameStr.replace(saveNameArr[replaceIndex], name), context);

                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_CITY_ID,
                            savedCityIdStr.replace(saveIdArr[replaceIndex], cityId), context);
                } else if (len > 0 && len < 3) {

                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_PROVINCE_ID,
                            savedProvinceIdStr + "," + provinceId, context);

                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_COUNTY_ID,
                            savedIdStr + "," + id, context);

                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_COUNTY_NAME,
                            savedNameStr + "," + name, context);

                    PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_CITY_ID,
                            savedCityIdStr + "," + cityId, context);
                }
                PreferenceUtils.writeIntConfig
                        (Constants.SPKEY_LATEST_COUNTY_NUM, currNum + 1, context);
            } else {
                PreferenceUtils.writeStrConfig
                        (Constants.SPKEY_LATEST_PROVINCE_ID, "", context);
                PreferenceUtils.writeStrConfig
                        (Constants.SPKEY_LATEST_COUNTY_ID, "", context);
                PreferenceUtils.writeStrConfig
                        (Constants.SPKEY_LATEST_COUNTY_NAME, "", context);
                PreferenceUtils.writeStrConfig
                        (Constants.SPKEY_LATEST_CITY_ID, "", context);
            }
        } else {
            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_PROVINCE_ID, "", context);
            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_COUNTY_ID, "", context);
            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_COUNTY_NAME, "", context);
            PreferenceUtils.writeStrConfig
                    (Constants.SPKEY_LATEST_CITY_ID, "", context);
        }

    }

    @Override
    public int getCount() {

        return citys == null ? 0 : citys.size();
    }

    @Override
    public Object getItem(int position) {

        return citys == null ? 0 : citys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.area_city_list, null);
            holder = new ViewHolder();
            holder.city = (TextView) convertView.findViewById(R.id.city);
            holder.noScrollGridView = (NoScrollGridView) convertView.findViewById(R.id.NoScrollGridView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.citys != null) {
            if (holder.city != null) {
                holder.city.setText(citys.get(position).regionName);
            }
            if (holder.noScrollGridView != null) {
                final ArrayList<ProvinceList.province> counts = citys.get(position).counties;
                GridViewAdapter gridViewAdapter = new GridViewAdapter(context, counts, position);
                holder.noScrollGridView.setAdapter(gridViewAdapter);
                holder.noScrollGridView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int p, long id) {

                        ProvinceList.province province = (ProvinceList.province) parent.getItemAtPosition(p);
                        String cityID = citys.get(position).regionId;
                        String countyID = province.regionId;
                        if (citySb != null && citySb.length() != 0) {//citySb不为空,表示用于设置收货地址
                            citySb.append(citys.get(position).regionName + "," + cityID + "," +
                                    province.regionName + "," + countyID);

                            // 数据是使用Intent返回
                            Intent intent = new Intent();
                            intent.putExtra("cityInfo", citySb.toString());
                            ((Activity) context).setResult(Activity.RESULT_OK, intent);

                            // 申请
                            String[] cityInfo = citySb.toString().split(",");
                            sendUserInfo(cityInfo);
                        } else {//citySb为空 ， 表示用于 定位

                            /**
                             * 保存城市区县信息
                             */
                            String countyId = countyID;
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString(Constants.SPKEY_SELECT_CITY_ID, cityID).commit();
                            edit.putString(Constants.SPKEY_SELECT_COUNTY_ID, countyID).commit();
                            edit.putString(Constants.SPKEY_SELECT_CITY_NAME, citys.get(position).regionName).commit();
                            edit.putString(Constants.SPKEY_SELECT_PROVINCE_ID, provinceId).commit();//选择城市所在的省份的ID
                            edit.putString(Constants.SPKEY_SELECT_PROVINCE_NAME, provinceName).commit();//选择城市所在的省份的ID
                            //sp.edit().putString(Constants.SPKEY_LOCATION,"{\"lat\":\"\",\"lng\":\"\",\"city_id\":"+cityID+",\"county_id\":"+countyID+",\"province_id\":"+provinceId+"}").commit();
                            edit.putString(Constants.SPKEY_LOCATION, LocationUtil.getJSLocationInfo(cityID, countyID, provinceId)).commit();
                            Logger.i("返回JS位置信息:" + LocationUtil.getJSLocationInfo(cityID, countyID, provinceId));
                            String areaName;
                            //如果选择的是某市的全部区县,就存入市名称,否则存入区县名称
                            if ("0".equals(countyId)) {
                                areaName = citys.get(position).regionName;
                            } else {
                                areaName = province.regionName;
                            }
                            edit.putString(Constants.SPKEY_SELECT_COUNTY_NAME,
                                    areaName).commit();
                            edit.putBoolean(Constants.SPKEY_USER_SELECT_CITY, true).commit();

                            AreaSelectionActivity.activity.finish();

                            Toast.makeText(context, "当前选中的是:" + areaName, Toast.LENGTH_SHORT).show();

                            //存入最近访问区域
                            saveLatestCity(provinceId, areaName, countyId, cityID, context);
                            //刷新主页面标识
                            sp.edit().putBoolean(Constants.REFRESH_HOME_FRAGMENT, true).commit();
                            ((Activity) context).finish();
                        }


                    }
                });
            }

        }

        return convertView;
    }

    private void sendUserInfo(final String[] strings) {

        if (activity == null)
            activity = (CitySelectionActivity) context;

        activity.startMyDialog();

        GetUserInfoEntity info = new GetUserInfoEntity();
        GetUserInfoEntity.UserInfo in = info.new UserInfo();
        in.city = strings[3];
        in.district = strings[5];
        activity.netRequest.setUserInfo(in, new RequestCallBack<BaseEntity>() {

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> arg0) {
                activity.stopMyDialog();
                switch (arg0.result.code) {
                    case 1:
                        activity.showToast("修改成功");
                        sp.edit().putString("provinceName", strings[0]).commit();
                        sp.edit().putString("provinceId", strings[1]).commit();
                        sp.edit().putString("cityName", strings[2]).commit();
                        sp.edit().putString("cityId", strings[3]).commit();
                        sp.edit().putString("districtName", strings[4]).commit();
                        sp.edit().putString("districtId", strings[5]).commit();


                        ((Activity) context).finish();

                        break;
                    case -23:
                    case -4:
                        activity.showToast("您的登录状态已经失效，请重新登录后再执行操作");
                        break;


                    default:
                        activity.showToast("修改失败");
                        break;
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                activity.stopMyDialog();
                activity.showToast("修改失败，因为网络链接失败");
            }
        });
    }

    private static class ViewHolder {
        public TextView city;
        public TextView area_city_gridview_city;
        public NoScrollGridView noScrollGridView;
    }

    private class GridViewAdapter extends BaseAdapter {
        List<ProvinceList.province> county;
        private int index;

        public GridViewAdapter(Context context, List<ProvinceList.province> distructs, int index) {
            this.county = distructs;
            this.index = index;
        }

        @Override
        public int getCount() {
            if (county == null) {
                return 0;
            } else {
                return this.county.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (county == null) {
                return null;
            } else {
                return this.county.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.area_city_gridview, null);
                holder = new ViewHolder();
                holder.area_city_gridview_city = (TextView) convertView.findViewById(R.id.area_city_gridview_city);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (this.county != null) {

                if (holder.area_city_gridview_city != null) {
                    if (county.get(position) != null) {
//                        if (position == 0) {
//                            holder.area_city_gridview_city.setText(citys.get(index).regionName);
//                        } else {
                        holder.area_city_gridview_city.setText(county.get(position).regionName);
//                        }
                    } else {
                    }

                }
            }
            return convertView;
        }
    }

}