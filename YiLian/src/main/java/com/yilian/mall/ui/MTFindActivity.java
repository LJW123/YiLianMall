package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ComboSearchAdapter;
import com.yilian.mall.entity.MTComboSearchEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.HorizontalListView;
import com.yilian.mylibrary.Constants;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.yilian.mall.R.id.ed_keyword;

/**
 * Created by liuyuqi on 2016/12/14 0014.
 * 套餐商家搜索
 */
public class MTFindActivity extends BaseActivity implements View.OnClickListener {
    private EditText edKeyword;
    private LinearLayout indexTopLayout;
    private HorizontalListView listHotKeyWord;
    private ListView lvFindRecord;
    private TextView clearFindRecord;
    private LinearLayout llayoutClear;
    private LinearLayout findRecordLinear;
    private TextView textFindData;
    private GridView gridFindData;
    private PullToRefreshListView pullListView;
    private ImageView imgViewNoDate;
    private String type;
    private TextView tvBack;
    private MTNetRequest mtNetRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_activity_combo_merchant_search);
        initView();
    }


    private void initView() {
        tvBack = (TextView) findViewById(R.id.tv_back);
        edKeyword = (EditText) findViewById(ed_keyword);
        indexTopLayout = (LinearLayout) findViewById(R.id.index_top_layout);
        listHotKeyWord = (HorizontalListView) findViewById(R.id.list_hot_key_word);
        lvFindRecord = (ListView) findViewById(R.id.lv_find_record);
        lvFindRecord.setOverScrollMode(View.OVER_SCROLL_NEVER);//设置不可滑动
        clearFindRecord = (TextView) findViewById(R.id.clear_find_record);
        llayoutClear = (LinearLayout) findViewById(R.id.llayout_clear);
        findRecordLinear = (LinearLayout) findViewById(R.id.find_record_linear);
        textFindData = (TextView) findViewById(R.id.text_find_data);
        gridFindData = (GridView) findViewById(R.id.grid_find_data);
        pullListView = (PullToRefreshListView) findViewById(R.id.pull_listView);
        pullListView.setMode(PullToRefreshBase.Mode.BOTH);
        imgViewNoDate = (ImageView) findViewById(R.id.iv_no_date);


        type = getIntent().getStringExtra("type");
        Logger.i("传递过来的type值" + type);
        showSerachHistory(type);

        tvBack.setOnClickListener(this);
        clearFindRecord.setOnClickListener(this);
    }


    //初始化数据
    private void initData(final String keyword) {
        final String city = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, mContext, "0");
        String county = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, mContext, "0");
        if ( mtNetRequest== null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        startMyDialog();
        mtNetRequest.getComboSerachList(city, county, keyword, new RequestCallBack<MTComboSearchEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MTComboSearchEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code){
                    case 1:
                        List<MTComboSearchEntity.DataBean> comboList = responseInfo.result.data;
                        if (comboList.size()<=0|| null==comboList){
                            showToast("暂无套餐");
                            pullListView.setVisibility(View.GONE);
                            imgViewNoDate.setVisibility(View.VISIBLE);
                            return;
                        }else{
                            pullListView.setVisibility(View.VISIBLE);
                            //通过高德地图计算位置并返回一个排好序的集合
                            final double latitude = ((MyApplication) mContext.getApplicationContext())
                                    .getLatitude();
                            final double longitude = ((MyApplication) mContext.getApplicationContext())
                                    .getLongitude();
                            final LatLng latLngLocate = new LatLng(latitude, longitude);
                            final DecimalFormat decimalFormat = new DecimalFormat("0.0");

                            if (latitude > 0.0 && longitude > 0.0) {
                                if (comboList.size() > 1) {
                                    Collections.sort(comboList, new Comparator<MTComboSearchEntity.DataBean>() {
                                        @Override
                                        public int compare(MTComboSearchEntity.DataBean lhs, MTComboSearchEntity.DataBean rhs) {
                                            // TODO Auto-generated method stub
                                            double[] latlnglhs = CommonUtils.bd2gcj02(Double.parseDouble(lhs.merchantLatitude),
                                                    Double.parseDouble(lhs.merchantLongitude));
                                            double[] latlngrhs = CommonUtils.bd2gcj02(Double.parseDouble(rhs.merchantLatitude),
                                                    Double.parseDouble(rhs.merchantLongitude));
                                            LatLng latLnglhs = new LatLng(latlnglhs[0], latlnglhs[1]);
                                            LatLng latLngrhs = new LatLng(latlngrhs[0], latlngrhs[1]);
                                            float distancelhs = AMapUtils.calculateLineDistance(latLnglhs,
                                                    latLngLocate);
                                            float distancerhs = AMapUtils.calculateLineDistance(latLngrhs,
                                                    latLngLocate);
                                            lhs.distance = decimalFormat.format(distancelhs / 1000);
                                            rhs.distance = decimalFormat.format(distancerhs / 1000);
                                            float diff = distancelhs - distancerhs;
                                            if (diff > 0) {
                                                return 1;
                                            } else if (diff < 0) {
                                                return -1;
                                            }
                                            return 0;
                                        }

                                    });
                                } else {
                                    double[] latlng = CommonUtils.bd2gcj02(Double.parseDouble(comboList.get(0).merchantLatitude),
                                            Double.parseDouble(comboList.get(0).merchantLongitude));
                                    LatLng latLng = new LatLng(latlng[0], latlng[1]);
                                    float distance = AMapUtils.calculateLineDistance(latLng,
                                            latLngLocate);
                                    comboList.get(0).distance = decimalFormat.format(distance / 1000);
                                }
                            }

                            pullListView.setAdapter(new ComboSearchAdapter(comboList));
                        }
                        saveSearchHistory(keyword);
                        break;
                    case 0:
                        showToast("请求参数异常");
                        break;
                        default:
                            showToast("请求异常码"+responseInfo.result.code);
                            break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    private void saveSearchHistory(String keyword) {
        findRecordLinear.setVisibility(View.GONE);
        listHotKeyWord.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(keyword)){
            String historyStr = sp.getString(type, "");
            String[] historyArr = historyStr.split("\\|");
            for (int i = 0; i < historyArr.length; i++) {
                if (keyword.equals(historyArr[i])){
                    return;
                }
            }
            if (historyStr.length()>0){
                sp.edit().putString("type",keyword+"|"+historyStr).commit();
            }else{
                sp.edit().putString("type",keyword+historyStr).commit();
            }
        }
    }


    private void showSerachHistory(String type) {
        String historyStr=sp.getString(type,"");
        if (TextUtils.isEmpty(historyStr)){
            findRecordLinear.setVisibility(View.GONE);
            return;
        }
        String[] recordList = historyStr.split("\\|");
        if (recordList !=null && recordList.length>0){
            lvFindRecord.setAdapter(new SerachRecordAdapter(recordList));
            findRecordLinear.setVisibility(View.VISIBLE);
        }
    }




    class SerachRecordAdapter extends BaseAdapter {

        private final String[] recordList;

        public SerachRecordAdapter(String[] recordList) {
            this.recordList =recordList;
        }

        @Override
        public int getCount() {
            if (recordList != null) {
                return recordList.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (recordList != null && recordList.length > position) {
                return recordList[position];
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RecordHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_find_record_item, null);
                holder = new RecordHolder();
                holder.find_record_item_tv = (TextView) convertView.findViewById(com.yilian.mall.R.id.find_record_item_tv);
                convertView.setTag(holder);
            } else {
                holder = (RecordHolder) convertView.getTag();
            }

            holder.find_record_item_tv.setText(recordList[position]);

            return convertView;

        }

    }

     class RecordHolder {
        private TextView find_record_item_tv;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.llayout_clear:
                clear(type);
                break;
        }

    }

    //清除缓存历史
    private void clear(String type) {
        sp.edit().putString("type","").commit();
        showSerachHistory(type);
    }


    //搜索
    public void find(View view){
        String  keyword = edKeyword.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "请输入搜索的套餐名称", Toast.LENGTH_SHORT).show();
            return;
        }
        initData(keyword);
    }
}
