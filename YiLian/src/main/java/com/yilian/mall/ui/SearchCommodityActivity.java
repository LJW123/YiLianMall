package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.LeDouListAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.DataBean;
import com.yilian.networkingmodule.entity.SearchCommodityEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品搜索
 *
 * @author Zg
 * @date 2018/5/10
 */
public class SearchCommodityActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    /**
     * 默认所搜类型
     * 0-- 全部商品
     */
    public static final String TYPE_ALL_GOODS = "0";
    private EditText edKeyword;
    private TextView tvSeach;
    private LinearLayout indexTopLayout;
    private ListView lvFindRecord;
    private TextView clearFindRecord;
    private LinearLayout llayoutClear;
    private LinearLayout findRecordLinear;
    private RecyclerView recyclerView;
    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivNoData;
    private TextView tvFinish;
    private String[] recordList;
    private ViewHolder1 holder1;
    private int page = 0;
    /**
     * 搜索出来的商品类型
     * 0--所有商品
     * 3--区块实验区的商品
     */
    private String type;

    private List<DataBean> data;
    private LeDouListAdapter mAdapter;
    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_commodity);
        initView();
        initData();
        initListener();
        showSearchHistory(Constants.MERCHNAT_SEARCH_COMMODITY);
    }

    private void initView() {
        type = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(type)) {
            //默认搜索出来全部的商品
            type = TYPE_ALL_GOODS;
        }

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
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_line, false);
        recyclerView.addItemDecoration(decor);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2) {
            @Override
            public boolean canScrollVertically() {
                return !swipeRefreshLayout.isRefreshing();
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LeDouListAdapter(R.layout.item_gride_cal_power,new ArrayList<DataBean>());
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);

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

    private void initData() {
        //如果是从webview界面跳转过来，且是带搜索关键字的，则直接显示搜索结果页面
        String from = getIntent().getStringExtra("from");
        if (from != null && from.equals("webView")) {
            keyword = getIntent().getStringExtra("keyWord");
            if (!TextUtils.isEmpty(keyword)) {
                edKeyword.setText(keyword);
                edKeyword.setSelection(edKeyword.getText().length());
                submit();
            }
        }
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
                mAdapter.setEnableLoadMore(false);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                DataBean item = (DataBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);
                intent = new Intent(mContext, JPNewCommDetailActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JP_GOODS_ID, item.goodsId);
                intent.putExtra("classify", item.goodsType);
                mContext.startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        lvFindRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edKeyword.setText((String) parent.getItemAtPosition(position));
                edKeyword.setSelection(edKeyword.getText().length());
                submit();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        Logger.i("getShopsList：onLoadMoreRequested");
        getNextPageData();
        swipeRefreshLayout.setEnabled(false);
    }

    private void getNextPageData() {
        page++;
        getCommodityList();
    }

    /**
     * 搜索商品
     */
    private void getCommodityList() {
        com.yilian.networkingmodule.retrofitutil.RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSearchCommodityList(keyword, String.valueOf(page), type, new Callback<SearchCommodityEntity>() {
                    @Override
                    public void onResponse(Call<SearchCommodityEntity> call, Response<SearchCommodityEntity> response) {
                        data = response.body().list;
                        if (page == 0) {
                            if (data.size() <= 0) {
                                swipeRefreshLayout.setVisibility(View.GONE);
                                ivNoData.setVisibility(View.VISIBLE);
                            } else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                ivNoData.setVisibility(View.GONE);
                                mAdapter.setNewData(data);
                                isLoadMore(data);
                            }
                        } else {
                            if (data.size() <= 0) {
                                mAdapter.loadMoreEnd();
                            } else {
                                mAdapter.addData(data);
                                isLoadMore(data);
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<SearchCommodityEntity> call, Throwable e) {
                        showToast(e.getMessage());
                        netRequestEnd();
                    }
                });
    }

    /**
     * 列表是否加载下一页
     *
     * @param data
     */
    private void isLoadMore(List<DataBean> data) {
        if (data.size() < Constants.PAGE_COUNT) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
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
            default:
                break;
        }
    }

    private void submit() {
        // validate
        keyword = edKeyword.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "输入搜索内容", Toast.LENGTH_SHORT).show();
            return;
        }
        findRecordLinear.setVisibility(View.GONE);
        saveSearchHistory(keyword, Constants.MERCHNAT_SEARCH_COMMODITY);
        // TODO validate success, do something
        getFirstPageData();

    }

    /**
     * 清除历史记录
     */
    public void clear() {
        sp.edit().putString(Constants.MERCHNAT_SEARCH_COMMODITY, "").commit();
        showSearchHistory(Constants.MERCHNAT_SEARCH_COMMODITY);
    }

    //缓存数据
    private void saveSearchHistory(String word, String type) {
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

    private void getFirstPageData() {
        page = 0;
        getCommodityList();
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

}
