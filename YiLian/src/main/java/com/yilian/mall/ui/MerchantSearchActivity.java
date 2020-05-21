package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MTHomePageShopsAdapter;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.entity.ShopsEntity;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ShopsSort;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MerchantSearchActivity extends BaseActivity implements View.OnClickListener {

    private EditText edKeyword;
    private TextView tvSeach;
    private LinearLayout indexTopLayout;
    private ListView lvFindRecord;
    private TextView clearFindRecord;
    private LinearLayout llayoutClear;
    private LinearLayout findRecordLinear;
    private RecyclerView recyclerView;
    private android.support.v4.widget.SwipeRefreshLayout mySwipeRefreshLayout;
    private ImageView ivNoData;
    private String cityId;
    private String countyId;
    private TextView tvFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_search);
        initView();
        initData();
        initListener();
        showSearchHistory(Constants.MERCHNAT_SEARCH_HISTORY);
    }

    private void initListener() {
        lvFindRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchRecordKey = (String) parent.getItemAtPosition(position);
                searchNearByShops(searchRecordKey);
            }
        });
    }

    private void initData() {
        cityId = getIntent().getStringExtra("cityId");
        countyId = getIntent().getStringExtra("countyId");
    }

    /**
     * 搜索附近商家
     *
     * @param keyword
     */
    private void searchNearByShops(String keyword) {
        dismissInputMethod();
        mySwipeRefreshLayout.setRefreshing(true);
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext).searchNearByShops(cityId, countyId, keyword, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), new Callback<ShopsEntity>() {
                @Override
                public void onResponse(Call<ShopsEntity> call, Response<ShopsEntity> response) {
                    ShopsEntity body = response.body();
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,body)){
                        List<ShopsEntity.MerchantListBean> merchantList = body.merchantList;
                        ShopsSort.sort(mContext, merchantList);
                        if (null==merchantList|| merchantList.size()<1){
                            ivNoData.setVisibility(View.VISIBLE);
                            mySwipeRefreshLayout.setVisibility(View.GONE);
                        } else {
                            recyclerView.setAdapter(new MTHomePageShopsAdapter(mContext, merchantList));
                            ivNoData.setVisibility(View.GONE);
                            mySwipeRefreshLayout.setVisibility(View.VISIBLE);

                        }

                        findRecordLinear.setVisibility(View.GONE);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<ShopsEntity> call, Throwable t) {
                    mySwipeRefreshLayout.setRefreshing(false);
                    showToast(R.string.net_work_not_available);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);
        edKeyword = (EditText) findViewById(R.id.ed_keyword);
        tvSeach = (TextView) findViewById(R.id.tv_seach);
        indexTopLayout = (LinearLayout) findViewById(R.id.index_top_layout);
        lvFindRecord = (ListView) findViewById(R.id.lv_find_record);
        clearFindRecord = (TextView) findViewById(R.id.clear_find_record);
        llayoutClear = (LinearLayout) findViewById(R.id.llayout_clear);
        findRecordLinear = (LinearLayout) findViewById(R.id.find_record_linear);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration((new DividerItemDecoration(mContext,1, 1,Color.parseColor("#e7e7e7"))));
        mySwipeRefreshLayout = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.my_swipe_refresh_layout);
        mySwipeRefreshLayout.setColorSchemeColors(Color.RED);
        ivNoData = (ImageView) findViewById(R.id.iv_no_date);

        tvSeach.setOnClickListener(this);
        clearFindRecord.setOnClickListener(this);
        edKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    submit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_seach:
                submit();
                break;
            case R.id.clear_find_record:
                clear();
                break;
            case R.id.tv_finish:
                finish();
                break;
        }
    }

    private void submit() {
        // validate
        String keyword = edKeyword.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "输入搜索内容", Toast.LENGTH_SHORT).show();
            return;
        }
        saveSearchHistory(keyword, Constants.MERCHNAT_SEARCH_HISTORY);
        // TODO validate success, do something
        searchNearByShops(keyword);

    }

    private String[] recordList;
    private ViewHolder1 holder1;

    class SearchReordAdapter extends BaseAdapter {

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
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_find_record_item, null);

                holder1 = new ViewHolder1();
                holder1.find_record_item_tv = (TextView) convertView.findViewById(R.id.find_record_item_tv);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }

            holder1.find_record_item_tv.setText(recordList[position]);

            return convertView;

        }

    }

    private class ViewHolder1 {
        private TextView find_record_item_tv;
    }

    private void showSearchHistory(String type) {

        String historyStr = sp.getString(type, "");
        Logger.i("搜索历史：" + historyStr);
        if (TextUtils.isEmpty(historyStr)) {
            findRecordLinear.setVisibility(View.GONE);
            return;
        }
        recordList = historyStr.split("\\|");
        if (recordList != null && recordList.length > 0) {
            lvFindRecord.setAdapter(new SearchReordAdapter());
            findRecordLinear.setVisibility(View.VISIBLE);
        } else {
            findRecordLinear.setVisibility(View.GONE);
        }
    }

    //缓存数据
    private void saveSearchHistory(String word, String type) {
        findRecordLinear.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(word)) {
            String historyStr = sp.getString(type, "");
            String[] historyArr = historyStr.split("\\|");
            for (int i = 0; i < historyArr.length; i++) {
                if (word.equals(historyArr[i])) {
                    return;
                }
            }
            if (historyStr.length() > 0) {
                sp.edit().putString(type, word + "|" + historyStr).commit();
            } else {
                sp.edit().putString(type, word + historyStr).commit();
            }
        }
    }

    /**
     * 清除历史记录
     */
    public void clear() {
        sp.edit().putString(Constants.MERCHNAT_SEARCH_HISTORY, "").commit();
        showSearchHistory(Constants.MERCHNAT_SEARCH_HISTORY);
    }

}
