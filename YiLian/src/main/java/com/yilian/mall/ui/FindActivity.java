package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.adapter.AfterSaleAdapter;
import com.yilian.mall.adapter.DefaultAdapter;
import com.yilian.mall.adapter.MallGoodsList;
import com.yilian.mall.entity.AfterSale;
import com.yilian.mall.entity.AreaInfo;
import com.yilian.mall.entity.AreaInfoList;
import com.yilian.mall.entity.KeyWord;
import com.yilian.mall.entity.MallGoodsListEntity;
import com.yilian.mall.entity.MerchantList;
import com.yilian.mall.entity.Search;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.http.NearbyNetRequest;
import com.yilian.mall.http.OneBuyNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.LocationUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.HorizontalListView;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ShopsSort;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索activity  搜索城市  搜索 商品 售后列表
 */
public class FindActivity extends BaseActivity {

    ArrayList<MerchantList> merchantLists;
    ArrayList<AfterSale.AfterSaleList> afterSaleList;
    String type;
    /**
     * 商品列表加载过程中显示的占位图
     */
    BitmapDisplayConfig bitmap;
    @ViewInject(R.id.ed_keyword)
    private EditText edKeyWord;
    @ViewInject(R.id.lv_find_data)
    private PullToRefreshListView lvFindData;//城市 商家的listview
    @ViewInject(R.id.grid_find_data)
    private GridView gridFindVData;
    @ViewInject(R.id.iv_no_date)
    private ImageView imgView_no_date;
    @ViewInject(R.id.find_record_linear)
    private LinearLayout find_record_linear;
    @ViewInject(R.id.text_find_data)
    private TextView text_find_data;
    @ViewInject(R.id.list_hot_key_word)
    private HorizontalListView listHotKeyWord; // 显示热词
    /**
     * 搜索历史记录列表
     */
    @ViewInject(R.id.lv_find_record)
    private ListView lv_find_record;
    private String[] recordList;
    private MallNetRequest mallNetRequest;
    private AccountNetRequest accountNetRequest;
    private NearbyNetRequest nearbyNetRequest;
    private MallGoodsList goodsListAdapter;
    private MerchantAdapter merchantAdapter;
    private OneBuyNetRequest oneBuyNetRequest;
    private int page;
    private ArrayList<AreaInfo> cityList;
    private ArrayList<AreaInfo> areaInfos;
    private CityAdapter cityAdapter;
    /**
     * 搜索商品
     */
    private List<MallGoodsListEntity.MallGoodsLists> allGoodsLists = new ArrayList<MallGoodsListEntity.MallGoodsLists>();
    private ViewHolder1 holder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ViewUtils.inject(this);
        initListView();

        lv_find_record.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        lvFindData.setOverScrollMode(View.OVER_SCROLL_NEVER);

        accountNetRequest = new AccountNetRequest(mContext);
        nearbyNetRequest = new NearbyNetRequest(mContext);
        mallNetRequest = new MallNetRequest(mContext);

        merchantLists = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        showSearchHistory(type);

        cityList = new ArrayList<>();

        Listener();

        lv_find_record.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = (String) parent.getItemAtPosition(position);
                edKeyWord.setText(word);
                switchKeyWord(word);

            }
        });

        switch (type) {
            case "city":
                edKeyWord.setHint("请输入城市名称");
                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
                break;

            case "merchant":
                merchantAdapter = new MerchantAdapter(mContext, merchantLists);
                lvFindData.setAdapter(merchantAdapter);
                edKeyWord.setHint("请输入商家名称");
                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
                break;

            case "goods":
                goodsListAdapter = new MallGoodsList(mContext, allGoodsLists);
                lvFindData.setAdapter(goodsListAdapter);
                edKeyWord.setHint("请输入商品名称");
                lvFindData.setMode(PullToRefreshBase.Mode.BOTH);
                break;

            case "oneBuy":
                edKeyWord.setHint("请输入活动名称");
                getOneBuyHotKeyWord();
                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
                lvFindData.setVisibility(View.GONE);
                gridFindVData.setVisibility(View.VISIBLE);
                break;
            case "mallCategory":
                goodsListAdapter = new MallGoodsList(mContext, allGoodsLists);
                lvFindData.setAdapter(goodsListAdapter);
                edKeyWord.setHint("请输入商品名称");
                getMallHotKeyWord();
                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
                lvFindData.setVisibility(View.GONE);
                gridFindVData.setVisibility(View.VISIBLE);
                break;

            default:
                edKeyWord.setHint("请输入要搜索的关键字");
                break;

        }
        //如果是从webview界面跳转过来，且是带搜索关键字的，则直接显示搜索结果页面
        String from = getIntent().getStringExtra("from");
        if (from != null && from.equals("webView")) {
            String keyWord = getIntent().getStringExtra("keyWord");
            edKeyWord.setFocusable(false);
            edKeyWord.setText(keyWord);
            switchKeyWord(keyWord);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(edKeyWord.getText().toString())) {
            switchKeyWord(edKeyWord.getText().toString());
        }
    }

    private void getOneBuyHotKeyWord() {
        if (oneBuyNetRequest == null) {
            oneBuyNetRequest = new OneBuyNetRequest(mContext);
        }

        oneBuyNetRequest.getHotWord(new RequestCallBack<KeyWord>() {
            @Override
            public void onSuccess(ResponseInfo<KeyWord> responseInfo) {

                switch (responseInfo.result.code) {
                    case 1:
                        if (responseInfo.result.keyWord.size() != 0) {
                            listHotKeyWord.setVisibility(View.VISIBLE);
                            listHotKeyWord.setAdapter(new DefaultAdapter<String>(FindActivity.this, responseInfo.result.keyWord, R.layout.item_find_key_word) {
                                @Override
                                public void convert(com.yilian.mall.adapter.ViewHolder helper, String item) {
                                    helper.setText(R.id.txt_key_word, item);
                                }
                            });
                        }
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void getMallHotKeyWord() {
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }
        Logger.i("搜索界面从sp中取出的省份公司id" + sp.getString(Constants.SPKEY_LOCATION_PROVINCE_ID, "0"));
        mallNetRequest.mallHotKeyWord(sp.getString(Constants.SPKEY_LOCATION_PROVINCE_ID, "0"), new RequestCallBack<KeyWord>() {
            @Override
            public void onSuccess(ResponseInfo<KeyWord> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        if (responseInfo.result.keyWord.size() != 0) {
                            listHotKeyWord.setVisibility(View.VISIBLE);
                            listHotKeyWord.setAdapter(new DefaultAdapter<String>(FindActivity.this, responseInfo.result.keyWord, R.layout.item_find_key_word) {
                                @Override
                                public void convert(com.yilian.mall.adapter.ViewHolder helper, String item) {
                                    helper.setText(R.id.txt_key_word, item);
                                }
                            });
                        }
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    private void Listener() {
        lvFindData.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (getIntent().getStringExtra("type")) {
                    case "city":
                        city((AreaInfo) parent.getItemAtPosition(position));
                        //刷新主页面标识
                        sp.edit().putBoolean(Constants.REFRESH_HOME_FRAGMENT, true).commit();
                        break;

                    case "merchant":
                        intent = new Intent(FindActivity.this, MTMerchantDetailActivity.class);
                        intent.putExtra("merchant_id", merchantLists.get(position - 1).merchantId);
                        startActivity(intent);
                        break;


                    case "afterSale":
                        AfterSale.AfterSaleList afterSaleList = (AfterSale.AfterSaleList) parent.getItemAtPosition(position);
                        switch (afterSaleList.AfterSaleStatus) {
                            case 2:
                                intent = new Intent(FindActivity.this, AfterSaleThreeActivity.class);
                                intent.putExtra("type", afterSaleList.AfterSaleType);
                                intent.putExtra("orderId", afterSaleList.orderId);
                                intent.putExtra("filialeId", afterSaleList.goodsFiliale);
                                startActivity(intent);
                                break;

                            default:
                                intent = new Intent(FindActivity.this, AfterSaleOneActivity.class);
                                intent.putExtra("type", afterSaleList.AfterSaleType);
                                intent.putExtra("orderId", afterSaleList.orderId);
                                intent.putExtra("filialeId", afterSaleList.goodsFiliale);
                                startActivity(intent);
                                break;
                        }
                        break;
                    case "2":

                        break;

                }


            }
        });

        gridFindVData.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
                switch (getIntent().getStringExtra("type")) {


                    default:
                        showToast("使点劲");
                        break;

                }
            }
        });

        listHotKeyWord.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switchKeyWord((((String) parent.getItemAtPosition(position)).toString()));
                edKeyWord.setText((((String) parent.getItemAtPosition(position)).toString()));
            }
        });

    }

    private void switchKeyWord(String keyWord) {
        startMyDialog();
        switch (type) {
            case "city":
                getCityList(keyWord);
                break;

            case "merchant":
                getMerchant(keyWord);
                break;

            case "goods":
                getGoodsList(keyWord);
                break;

            case "afterSale":
                getAfterSale(keyWord);
                break;


            case "mallCategory":
                getmallCategory(keyWord);
                break;

            default:
                stopMyDialog();
                showToast("搜索类型错误");
                break;
        }
    }


    private void getGoodsList(final String keyWord) {
        startMyDialog();
        mallNetRequest.mallSearchList(getIntent().getStringExtra("order_Type"), keyWord, page, MallGoodsListEntity.class, new RequestCallBack<MallGoodsListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MallGoodsListEntity> responseInfo) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:

                        if (page == 0) {
                            allGoodsLists.clear();
                        }

                        if (responseInfo.result.list != null && responseInfo.result.list.size() != 0) {
                            allGoodsLists.addAll(responseInfo.result.list);
                            goodsListAdapter.notifyDataSetChanged();
                        }

                        if (responseInfo.result.list.size() == 0) {
                            showToast("没有数据了");
                        }

                        if (allGoodsLists == null || allGoodsLists.size() == 0) {
                            imgView_no_date.setVisibility(View.VISIBLE);
                            imgView_no_date.setImageDrawable(getResources().getDrawable(R.mipmap.mall_list_nodata));
                            text_find_data.setVisibility(View.GONE);
                            lvFindData.setVisibility(View.GONE);
                        } else {
                            imgView_no_date.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.VISIBLE);
                            lvFindData.setVisibility(View.VISIBLE);
                        }
                        find_record_linear.setVisibility(View.GONE);
                        saveSearchHistory(keyWord, Constants.SPKEY_GOODS_SEARCH_HISTORY);

                        page++;

                        break;
                    default:

                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
            }
        });
    }

    /**
     * 售后列表搜索
     *
     * @param keyWord
     */
    private void getAfterSale(final String keyWord) {
        mallNetRequest.keyWordAfterSaleOrderList(keyWord, page, new RequestCallBack<AfterSale>() {

            @Override
            public void onSuccess(ResponseInfo<AfterSale> responseInfo) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:
                        afterSaleList = responseInfo.result.list;
                        if (afterSaleList == null || afterSaleList.size() < 1) {
                            showToast("没有找到数据");
                            lvFindData.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.GONE);
                        } else {
                            lvFindData.setAdapter(new AfterSaleAdapter(mContext, afterSaleList));
                            lvFindData.setVisibility(View.VISIBLE);
                            text_find_data.setVisibility(View.VISIBLE);
                        }

                        saveSearchHistory(keyWord, type);

                        break;
                    case -4:

                        showToast("登录状态失效");

                        break;

                    case -3:

                        showToast("系统繁忙");

                        break;

                    default:
                        showToast("异常");
                        break;

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("请检查网络");
            }
        });
    }

    /**
     * 用户搜索商家
     */
    private void getMerchant(final String keyWord) {
        String city = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, mContext, "0");
        String county = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, mContext, "0");
        nearbyNetRequest.searchNearbyMerchants(city, county, keyWord, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), Search.class, new RequestCallBack<Search>() {
            @Override
            public void onSuccess(ResponseInfo<Search> responseInfo) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:
                        if (responseInfo.result.getMerchant_list().size() == 0) {
                            imgView_no_date.setVisibility(View.VISIBLE);
                            text_find_data.setVisibility(View.GONE);
                            lvFindData.setVisibility(View.GONE);
                        } else {
                            ArrayList<MerchantList> merchantList = responseInfo.result.merchant_list;
                            ShopsSort.sort(mContext, merchantList);
                            if (page == 0) {
                                merchantLists.clear();
                                merchantLists.addAll(merchantList);
                            } else {
                                merchantLists.addAll(merchantList);
                            }

                            merchantAdapter.notifyDataSetChanged();
                            imgView_no_date.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.VISIBLE);
                            lvFindData.setVisibility(View.VISIBLE);


                        }

                        find_record_linear.setVisibility(View.GONE);
                        saveSearchHistory(keyWord, type);
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
                lvFindData.onRefreshComplete();
            }
        });

    }

    /**
     * 用户搜索区县时的选择
     *
     * @param data
     */
    private void city(AreaInfo data) {
        /**
         * 判读数据是城市还是区县。
         * 如果选择的是某市的全部区县,就存入市名称,否则存入区县名称
         */
        String provinceId = data.getProvince_id();
        switch (data.getRegion_type()) {
            case "2":
                sp.edit().putString(Constants.SPKEY_SELECT_CITY_ID, data.getRegion_id()).commit();
                sp.edit().putString(Constants.SPKEY_SELECT_COUNTY_NAME, data.getRegion_name()).commit();
                sp.edit().putString(Constants.SPKEY_SELECT_COUNTY_ID, "0").commit();
                sp.edit().putString(Constants.SPKEY_SELECT_PROVINCE_ID, provinceId).commit();
                sp.edit().putString(Constants.SPKEY_LOCATION, LocationUtil.getJSLocationInfo(data.getRegion_id(), "0", provinceId)).commit();
                break;

            case "3":
                sp.edit().putString(Constants.SPKEY_SELECT_CITY_ID, data.getParent_id()).commit();
                sp.edit().putString(Constants.SPKEY_SELECT_COUNTY_NAME, data.getRegion_name()).commit();
                sp.edit().putString(Constants.SPKEY_SELECT_COUNTY_ID, data.getRegion_id()).commit();
                sp.edit().putString(Constants.SPKEY_SELECT_PROVINCE_ID, provinceId).commit();
                sp.edit().putString(Constants.SPKEY_LOCATION, LocationUtil.getJSLocationInfo(data.getParent_id(), data.getRegion_id(), provinceId)).commit();
                break;
        }


        showToast("当前选中的是:" + data.getRegion_name());
        sp.edit().putBoolean(Constants.SPKEY_USER_SELECT_CITY, true).commit();

        Intent intent = new Intent(FindActivity.this, JPMainActivity.class);
        startActivity(intent);
        AppManager.getInstance().killActivity(AreaCitySelectionAcitivty.class);
        AppManager.getInstance().killActivity(AreaSelectionActivity.class);
    }

    private void initListView() {

        lvFindData.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                switchKeyWord(edKeyWord.getText().toString());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                switchKeyWord(edKeyWord.getText().toString());
            }

        });
    }

    private void showSearchHistory(String type) {

        String historyStr = sp.getString(type, "");
        if (TextUtils.isEmpty(historyStr)) {
            find_record_linear.setVisibility(View.GONE);
            return;
        }
        recordList = historyStr.split("\\|");
        if (recordList != null && recordList.length > 0) {
            lv_find_record.setAdapter(new SearchReordAdapter());
            find_record_linear.setVisibility(View.VISIBLE);
        } else {
            find_record_linear.setVisibility(View.GONE);
        }
    }

    private void saveSearchHistory(String word, String type) {
        find_record_linear.setVisibility(View.GONE);
        listHotKeyWord.setVisibility(View.GONE);

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

    public void find(View view) {

        String keyWord = edKeyWord.getText().toString();
        page = 0;
        if (keyWord.equals("")) {
            showToast("请输入搜索内容");
            return;
        }
        switchKeyWord(keyWord);
    }

    /**
     * 搜索城市
     */
    private void getCityList(final String keyWord) {
        dismissInputMethod();
        cityList.clear();

        mallNetRequest.selectCity(keyWord, new RequestCallBack<AreaInfoList>() {
            @Override
            public void onSuccess(ResponseInfo<AreaInfoList> responseInfo) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:
                        if (responseInfo.result.cities.size() == 0) {
                            imgView_no_date.setVisibility(View.VISIBLE);
                            text_find_data.setVisibility(View.GONE);
                            lvFindData.setVisibility(View.GONE);

                        } else {
                            imgView_no_date.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.VISIBLE);
                            lvFindData.setVisibility(View.VISIBLE);

                        }
                        find_record_linear.setVisibility(View.GONE);
                        cityAdapter = new CityAdapter(mContext, responseInfo.result.cities);
                        lvFindData.setAdapter(cityAdapter);
                        saveSearchHistory(keyWord, type);

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
            }
        });

    }

    private void getmallCategory(final String keyWord) {
        dismissInputMethod();
        if (mallNetRequest == null) {
            mallNetRequest = new MallNetRequest(mContext);
        }

        mallNetRequest.mallSearch(sp.getString(Constants.SPKEY_LOCATION_PROVINCE_ID, "0"), keyWord, new RequestCallBack<MallGoodsListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MallGoodsListEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:

                        if (page == 0) {
                            allGoodsLists.clear();
                        }
                        allGoodsLists.addAll(responseInfo.result.list);
                        lvFindData.setAdapter(new MallGoodsList(mContext, allGoodsLists));
                        saveSearchHistory(keyWord, type);

                        if (responseInfo.result.list == null || responseInfo.result.list.size() == 0) {
                            text_find_data.setVisibility(View.GONE);
                            imgView_no_date.setVisibility(View.VISIBLE);
                            lvFindData.setVisibility(View.GONE);
                        } else {
                            imgView_no_date.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.VISIBLE);
                            lvFindData.setVisibility(View.VISIBLE);
                        }
                        break;

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("网络连接失败，请检查网络设置");
                stopMyDialog();

            }
        });
    }

    /**
     * 清除历史记录
     *
     * @param view
     */
    public void clear(View view) {

        sp.edit().putString(type, "").commit();

        showSearchHistory(type);
    }

    class CityAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<AreaInfo> areaInfos;

        public CityAdapter(Context context, ArrayList<AreaInfo> areaInfos) {
            this.context = context;
            this.areaInfos = areaInfos;
        }

        @Override
        public int getCount() {
            return areaInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return areaInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.city_search_list_item, null);
            Logger.i(areaInfos.get(position).getRegion_name());
            TextView city_name_text_view = (TextView) view.findViewById(R.id.city_name_text_view);
            city_name_text_view.setText(areaInfos.get(position).getRegion_name());
            PreferenceUtils.writeStrConfig(Constants.SPKEY_SELECT_PROVINCE_ID, areaInfos.get(position).getProvince_id(), context);
            return view;
        }

    }

    class MerchantAdapter extends BaseAdapter {

        private ArrayList<MerchantList> merchantLists;
        private Context context;

        public MerchantAdapter(Context context, ArrayList<MerchantList> mList) {

            this.context = context;
            this.merchantLists = mList;
        }

        @Override
        public int getCount() {
            return merchantLists.size();
        }

        @Override
        public Object getItem(int position) {
            if (merchantLists == null) {
                return null;
            } else {
                return this.merchantLists.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            BitmapUtils bitmapUtils = new BitmapUtils(context);
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_merchant, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MerchantList merchantList = merchantLists.get(position);
            holder.merchant_name.setText(merchantList.merchantName);
            holder.merchant_address.setText(merchantList.merchantAddress);
            holder.merchant_ratingBar.setRating(Float.parseFloat(merchantList.graded) / 10);
            GlideUtil.showImageWithSuffix(mContext, merchantList.merchantImage, holder.merchant_iv_prize);
            if (TextUtils.isEmpty(merchantList.praiseCount)) {
                holder.tvPraiseCount.setText("被赞 0 次");
            } else {
                holder.tvPraiseCount.setText("被赞 " + merchantList.praiseCount + " 次");
            }

            double locateLat = MyApplication.getInstance().getLatitude();
            double locateLon = MyApplication.getInstance().getLongitude();
            String latitude = merchantList.merchantLatitude;
            String longitude = merchantList.merchantLongitude;
            if (locateLat > 0 && locateLon > 0 && !TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                double[] gcj02 = CommonUtils.bd2gcj02(Double.parseDouble(latitude), Double.parseDouble(longitude));
                holder.merchant_distance.setText(merchantList.formatServiceMerDistance);
            } else {
                holder.merchant_distance.setText("距离计算失败");
            }
            return convertView;
        }
    }

    class ViewHolder {
        public ImageView merchant_iv_prize = null;
        public TextView merchant_name = null;
        public RatingBar merchant_ratingBar = null;
        public TextView merchant_address = null;
        public TextView merchant_distance = null;
        public TextView title = null;
        public TextView tvPraiseCount;

        public ViewHolder(View view) {
            // 商店地址
            merchant_address = (TextView) view.findViewById(R.id.merchant_address);
            // 图片
            merchant_iv_prize = (ImageView) view.findViewById(R.id.merchant_iv_prize);
            // 评分
            merchant_ratingBar = (RatingBar) view.findViewById(R.id.merchant_ratingBar);
            // 距离
            merchant_distance = (TextView) view.findViewById(R.id.merchant_distance);
            // 名称
            merchant_name = (TextView) view.findViewById(R.id.merchant_name);

            tvPraiseCount = (TextView) view.findViewById(R.id.tv_presence_count);
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
