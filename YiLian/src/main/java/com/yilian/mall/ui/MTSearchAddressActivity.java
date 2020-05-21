package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MTChooseAddressAdapter;
import com.yilian.mall.entity.MyPoiItem;
import com.yilian.mall.utils.SharedPreferencesUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * 搜索地址界面
 */
public class MTSearchAddressActivity extends BaseActivity implements View.OnClickListener, PoiSearch.OnPoiSearchListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private EditText tvInputBuildings;
    private ListView lvSearchAddress;
    private LinearLayout activityMtsearchAddress;
    private int currentPage;
    private ArrayList<PoiItem> pois = new ArrayList<>();
    private MTChooseAddressAdapter adapter;
    private String cityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtsearch_address);
        cityCode = getIntent().getStringExtra("cityCode");
        initView();
        initListener();
    }

    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        tvInputBuildings = (EditText) findViewById(R.id.tv_input_buildings);
        lvSearchAddress = (ListView) findViewById(R.id.lv_search_address);
        adapter = new MTChooseAddressAdapter(mContext, pois);
        lvSearchAddress.setAdapter(adapter);
        activityMtsearchAddress = (LinearLayout) findViewById(R.id.activity_mtsearch_address);
        tvInputBuildings.setOnClickListener(this);
//        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("选择地址");
        tvTitle.setVisibility(View.VISIBLE);
        ivRight2.setVisibility(View.GONE);
    }

    private void initListener() {
        ivLeft1.setOnClickListener(this);
        lvSearchAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem itemAtPosition = (PoiItem) parent.getItemAtPosition(position);
                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext,"SearchAddress");
                MyPoiItem myPoiItem = new MyPoiItem();
                myPoiItem.title = itemAtPosition.getTitle();
                myPoiItem.Latitude = itemAtPosition.getLatLonPoint().getLatitude();
                myPoiItem.Longitude = itemAtPosition.getLatLonPoint().getLongitude();
                myPoiItem. cityCode = itemAtPosition.getCityCode();
                myPoiItem. cityName = itemAtPosition.getCityName();
                myPoiItem.provinceName = itemAtPosition.getProvinceName();
                myPoiItem.adName = itemAtPosition.getAdName();

                sharedPreferencesUtils.setObject("SearchAddress",myPoiItem);
//                Intent newIntent = new Intent();
//                newIntent.putExtra("PoiItem",itemAtPosition);
//                setResult(RESULT_OK,newIntent);
                finish();
            }
        });
        tvInputBuildings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.i("改变中");
                PoiSearch.Query query = new PoiSearch.Query(s.toString().trim(),"",cityCode);
                query.setPageSize(20);// 设置每页最多返回多少条poiitem
                query.setPageNum(currentPage);//设置查询页码
                PoiSearch poiSearch = new PoiSearch(mContext, query);
                poiSearch.setOnPoiSearchListener(MTSearchAddressActivity.this);
                poiSearch.searchPOIAsyn();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_input_buildings:

                break;
            case R.id.iv_left1:
                finish();
                break;
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            ArrayList<PoiItem> pois = result.getPois();
            Logger.i("pois.toString():  "+pois.toString());
            if(pois!=null){
                lvSearchAddress.setAdapter(new MTChooseAddressAdapter(mContext,pois));
            }
            if (pois.size()>0) {
                PoiItem poiItem = pois.get(0);
                Logger.i("pois:  poiItem.getAdCode(): "+ poiItem.getAdCode()+"  poiItem.getAdName():"+poiItem.getAdName()+" poiItem.getBusinessArea():" +
                        " "+poiItem.getBusinessArea()+"  poiItem.getCityCode():"+poiItem.getCityCode()
                        +"  poiItem.getCityName():"+poiItem.getCityName()+" "+poiItem.getLatLonPoint());
            }

        }
    }

    @Override
    public void onPoiItemSearched(PoiItem result, int rCode) {

    }
}
