package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshGridView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.adapter.AfterSaleAdapter;
import com.yilian.mall.adapter.DefaultAdapter;
import com.yilian.mall.adapter.JPMallGoodsAdapter;
import com.yilian.mall.adapter.MallListAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.entity.AfterSale;
import com.yilian.mall.entity.AreaInfo;
import com.yilian.mall.entity.AreaInfoList;
import com.yilian.mall.entity.JPMallGoodsListEntity;
import com.yilian.mall.entity.KeyWord;
import com.yilian.mall.entity.MerchantList;
import com.yilian.mall.entity.Search;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.http.NearbyNetRequest;
import com.yilian.mall.http.OneBuyNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.LocationUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.FlowLayout;
import com.yilian.mall.widgets.HorizontalListView;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.ShopsSort;
import com.yilian.networkingmodule.entity.SearchListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 搜索activity  搜索城市  搜索 商品 售后列表
 */
public class JPFindActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    ArrayList<MerchantList> merchantLists;
    ArrayList<AfterSale.AfterSaleList> afterSaleList;
    String type;
    /**
     * 商品列表加载过程中显示的占位图
     */
    BitmapDisplayConfig bitmap;
    @ViewInject(R.id.ed_keyword)
    private EditText edKeyWord;
    //    @ViewInject(R.id.lv_find_data)
    @ViewInject(R.id.class_gv)
    private PullToRefreshGridView lvFindData;
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
    @ViewInject(R.id.flowLayout_hot_key)
    private FlowLayout flowLayoutHotKey;//显示热词
    /**
     * 搜索历史记录列表
     */
    @ViewInject(R.id.lv_find_record)
    private ListView lv_find_record;
    private String[] recordList;
    private MallNetRequest mallNetRequest;
    private AccountNetRequest accountNetRequest;
    private NearbyNetRequest nearbyNetRequest;
    private JPMallGoodsAdapter goodsListAdapter;
    private MerchantAdapter merchantAdapter;
    private OneBuyNetRequest oneBuyNetRequest;
    private int page = 0;
    private ArrayList<AreaInfo> cityList;
    private ArrayList<AreaInfo> areaInfos;
    private CityAdapter cityAdapter;
    /**
     * 搜索商品
     */
    private ArrayList<JPMallGoodsListEntity.ListBean> allGoodsLists = new ArrayList<>();
    private ViewHolder1 holder1;
    private TextView tvTag;

    //yhs jfg
    @ViewInject(R.id.tv_seach)
    TextView tvSearch;
    @ViewInject(R.id.rl_yhs_and_jfg)
    RelativeLayout YhsAndJfgLayout;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;
    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;
    private MallListAdapter yhsjfgAdapter;
    private ArrayList<com.yilian.networkingmodule.entity.JPGoodsEntity> yhsjfgList = new ArrayList<>();
    String word;

    private boolean getFirstPageDataFlag = true;
    private View emptyView, errorView;
    private TextView tvRefresh, tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_activity_find);
        ViewUtils.inject(this);

        lv_find_record.setOverScrollMode(View.OVER_SCROLL_NEVER);
        lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
        accountNetRequest = new AccountNetRequest(mContext);
        nearbyNetRequest = new NearbyNetRequest(mContext);
        mallNetRequest = new MallNetRequest(mContext);

        merchantLists = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        Logger.i("传递过来的type值" + type);
        showSearchHistory(type);

        cityList = new ArrayList<>();

        lv_find_record.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(word)) {
                    edKeyWord.setText(word);
                    edKeyWord.setSelection(word.length());
                    switchKeyWord(word);
                }
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
                goodsListAdapter = new JPMallGoodsAdapter(mContext, allGoodsLists, imageLoader);
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
                goodsListAdapter = new JPMallGoodsAdapter(mContext, allGoodsLists, imageLoader);
                lvFindData.setAdapter(goodsListAdapter);
                edKeyWord.setHint("请输入商品名称");
                getMallHotKeyWord();
                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
                lvFindData.setVisibility(View.GONE);
                gridFindVData.setVisibility(View.VISIBLE);
                break;

            case "1":
            case "2":
            case "3":
                initYhsAndJfgView();
                break;

            default:
                edKeyWord.setText("请输入要搜索的关键字");
                break;

        }
        //如果是从webview界面跳转过来，且是带搜索关键字的，则直接显示搜索结果页面
        String from = getIntent().getStringExtra("from");
        if (from != null && from.equals("webView")) {
            String keyWord = getIntent().getStringExtra("keyWord");
            edKeyWord.setFocusable(false);
            edKeyWord.setText(keyWord);
            if ("mallCategory".equals(type)) {
                keyWord = type;
            }
            switchKeyWord(keyWord);
        }
        //显示热搜
        getMallHotKeyWord();

        initListViewRefresh();

        Listener();
    }

    private void initYhsAndJfgView() {
        edKeyWord.setHint("请输入商品名称");
        lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
        lvFindData.setVisibility(View.GONE);
        YhsAndJfgLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        emptyView = View.inflate(mContext, R.layout.merchant_barcode_nothing, null);
        tvNoData = (TextView) emptyView.findViewById(R.id.tv_no_data);
        tvNoData.setText("暂无数据");
        errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                getYhaOrJfgGoodsList(word);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
        recyclerView.addItemDecoration(decor);
        yhsjfgAdapter = new MallListAdapter(yhsjfgList, type);
        yhsjfgAdapter.setOnLoadMoreListener(this, recyclerView);
        yhsjfgAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return yhsjfgList.get(position).getSpanSize();
            }
        });
        recyclerView.setAdapter(yhsjfgAdapter);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(edKeyWord.getText().toString())) {
            word = edKeyWord.getText().toString();
            switchKeyWord(word);
        }
    }

    private void getYhaOrJfgGoodsList(String keyWord) {
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSearchList(keyWord, String.valueOf(page), type, new Callback<SearchListEntity>() {
                    @Override
                    public void onResponse(Call<SearchListEntity> call, Response<SearchListEntity> response) {
                        SearchListEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        ArrayList<com.yilian.networkingmodule.entity.JPGoodsEntity> newList = body.list;
                                        Logger.i("ray-" + newList.size());
                                        if (newList.size() > 0 && newList != null) {
                                            if (getFirstPageDataFlag) {
                                                yhsjfgAdapter.setNewData(newList);
                                                getFirstPageDataFlag = false;
                                                yhsjfgList.clear();
                                            } else {
                                                yhsjfgAdapter.addData(newList);
                                            }
                                            yhsjfgList.addAll(newList);

                                            if (newList.size() < Constants.PAGE_COUNT_20) {
                                                yhsjfgAdapter.loadMoreEnd();
                                            } else {
                                                yhsjfgAdapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
                                                yhsjfgAdapter.setNewData(newList);
                                                yhsjfgAdapter.setEmptyView(emptyView);
                                            }
                                        }

                                        find_record_linear.setVisibility(View.GONE);
                                        saveSearchHistory(keyWord, type);
                                        break;
                                }
                            }
                        }
                        netRequestEnd();
                    }

                    @Override
                    public void onFailure(Call<SearchListEntity> call, Throwable t) {
                        if (page == 0) {
                            yhsjfgAdapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                        }
                        yhsjfgAdapter.loadMoreFail();
                        netRequestEnd();
                        showToast(R.string.net_work_not_available);
                    }
                });
    }

    private void getOneBuyHotKeyWord() {
        if (oneBuyNetRequest == null) {
            oneBuyNetRequest = new OneBuyNetRequest(mContext);
        }

        oneBuyNetRequest.getHotWord(new RequestCallBack<KeyWord>() {
            @Override
            public void onSuccess(ResponseInfo<KeyWord> responseInfo) {

                final KeyWord result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        final ArrayList<String> keyWords = result.keyWord;
                        if (keyWords.size() != 0) {
//                            listHotKeyWord.setVisibility(View.VISIBLE);
                            listHotKeyWord.setAdapter(new DefaultAdapter<String>(mContext, keyWords, R.layout.item_find_key_word) {
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
        Logger.i("新搜索界面从sp中取出的省份公司id" + sp.getString(Constants.SPKEY_LOCATION_PROVINCE_ID, "0"));
        mallNetRequest.mallHotKeyWord("0", new RequestCallBack<KeyWord>() {
            @Override
            public void onSuccess(ResponseInfo<KeyWord> responseInfo) {
                final KeyWord result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        final ArrayList<String> keyWord = result.keyWord;
                        if (keyWord.size() > 0) {
                            flowLayoutHotKey.setVisibility(View.VISIBLE);
                        } else {
                            flowLayoutHotKey.setVisibility(View.GONE);
                        }

                        Logger.i("拿到了热词");
                        for (int i = 0, subCount = keyWord.size(); i < subCount; i++) {//流式布局 TextView 显示标签

                            tvTag = new TextView(mContext);
                            //            使用自定义的流式布局要重写 属性代码 否则他不知道自己的位置 VewGroup.MarginLayoutParams
                            ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            mlp.setMargins(DPXUnitUtil.dp2px(mContext, 15f), DPXUnitUtil.dp2px(mContext, 10f),
                                    DPXUnitUtil.dp2px(mContext, 0f), DPXUnitUtil.dp2px(mContext, 1f));
                            tvTag.setLayoutParams(mlp);
                            tvTag.setPadding(30, 5, 30, 5);
                            tvTag.setBackgroundResource(R.drawable.bg_hotsearch);
                            String text = keyWord.get(i);
                            Logger.i("放置热词" + text);
                            tvTag.setText(text);
                            tvTag.setTextColor(getResources().getColor(R.color.color_666));
                            tvTag.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switchKeyWord(text);
                                    edKeyWord.setText(text);
                                }
                            });
                            flowLayoutHotKey.addView(tvTag);
                        }


                        if (keyWord.size() != 0) {
//                            listHotKeyWord.setVisibility(View.VISIBLE);
                            listHotKeyWord.setAdapter(new DefaultAdapter<String>(mContext, keyWord, R.layout.item_find_key_word) {
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
                        break;

                    case "merchant":
                        intent = new Intent(mContext, MTMerchantDetailActivity.class);
                        intent.putExtra("merchant_id", merchantLists.get(position - 1).merchantId);
                        startActivity(intent);
                        break;

                    case "goods":
                        intent = new Intent(mContext, JPNewCommDetailActivity.class);
                        JPMallGoodsListEntity.ListBean mallGoodsLists = (JPMallGoodsListEntity.ListBean) parent.getItemAtPosition(position);
                        intent.putExtra("goods_id", mallGoodsLists.goodsId);
                        intent.putExtra("filiale_id", "0");
                        startActivity(intent);
                        break;


                    case "afterSale":
                        AfterSale.AfterSaleList afterSaleList = (AfterSale.AfterSaleList) parent.getItemAtPosition(position);
                        switch (afterSaleList.AfterSaleStatus) {
                            case 2:
                                intent = new Intent(mContext, AfterSaleThreeActivity.class);
                                intent.putExtra("type", afterSaleList.AfterSaleType);
                                intent.putExtra("orderId", afterSaleList.orderId);
                                intent.putExtra("filialeId", afterSaleList.goodsFiliale);
                                startActivity(intent);
                                break;

                            default:
                                intent = new Intent(mContext, AfterSaleOneActivity.class);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                if (scrollDy > ScreenUtils.getScreenHeight(mContext) * 3) {
                    iv_return_top.setVisibility(View.VISIBLE);
                } else {
                    iv_return_top.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        iv_return_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                page = 0;
                getFirstPageDataFlag = true;
                getYhaOrJfgGoodsList(word);
                yhsjfgAdapter.setEnableLoadMore(false);
            }
        });

        edKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    find(null);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        yhsjfgAdapter.setEnableLoadMore(true);
    }

    private void switchKeyWord(String keyWord) {
        flowLayoutHotKey.setVisibility(View.GONE);//搜索时，热词隐藏
        Logger.i("2017年7月26日 15:53:00-" + type);
        switch (type) {
            case "city":
                startMyDialog();
                getCityList(keyWord);
                break;

            case "merchant":
                startMyDialog();
                getMerchant(keyWord);
                break;

            case "goods":
                startMyDialog();
                getGoodsList(keyWord);
                break;

            case "afterSale":
                startMyDialog();
                getAfterSale(keyWord);
                break;

            case "mallCategory":
                startMyDialog();
                getmallCategory(keyWord);
                break;

            case "1":
            case "2":
            case "3":
                page = 0;
                getFirstPageDataFlag = true;
                swipeRefreshLayout.setRefreshing(true);
                getYhaOrJfgGoodsList(keyWord);
                yhsjfgAdapter.setEnableLoadMore(false);
                handler.sendEmptyMessage(SEARCH_SUCCESS);
                tvSearch.setClickable(false);
                Logger.i("ray919-" + "走了搜索这里");
                break;

            default:
                stopMyDialog();
                showToast("搜索类型错误");

                break;
        }
    }

    public static final int SEARCH_SUCCESS = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH_SUCCESS:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvSearch.setClickable(true);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
            }
        }
    };

    private void getGoodsList(final String keyWord) {
        mallNetRequest.mallGoodsSearchList(keyWord, page, JPMallGoodsListEntity.class, new RequestCallBack<JPMallGoodsListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPMallGoodsListEntity> responseInfo) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                JPMallGoodsListEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        ArrayList<JPMallGoodsListEntity.ListBean> list = result.list;

                        if (page == 0) {
                            allGoodsLists.clear();
                        } else {
                            if (list.size() == 0) {
                                showToast("没有数据了");
                            }
                        }

                        if (list != null && list.size() != 0) {
                            allGoodsLists.addAll(list);
                            goodsListAdapter.notifyDataSetChanged();
                        }

                        if (allGoodsLists == null || allGoodsLists.size() == 0) {
                            imgView_no_date.setVisibility(View.VISIBLE);
                            imgView_no_date.setImageDrawable(getResources().getDrawable(R.mipmap.mall_list_nodata));
                            text_find_data.setVisibility(View.GONE);// <!--搜索结果框，已经取消，该框即使结果出现后也不再显示，已在代码里把VISIABLE改为GONE-->
                            lvFindData.setVisibility(View.GONE);
                        } else {
                            imgView_no_date.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.GONE); //<!--搜索结果框，已经取消，该框即使结果出现后也不再显示，已在代码里把VISIABLE改为GONE-->
                            lvFindData.setVisibility(View.VISIBLE);
                        }
                        find_record_linear.setVisibility(View.GONE);
//                        saveSearchHistory(keyWord, Constants.SPKEY_GOODS_SEARCH_HISTORY);
                        saveSearchHistory(keyWord, type);

                        break;
                    default:
                        imgView_no_date.setVisibility(View.VISIBLE);
                        lvFindData.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                imgView_no_date.setVisibility(View.VISIBLE);
                lvFindData.setVisibility(View.GONE);
            }
        });
    }

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
                            text_find_data.setVisibility(View.GONE);
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
//        String city = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, mContext, "0");
//        String county = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, mContext, "0");
//        nearbyNetRequest.searchNearbyMerchants(city, county, keyWord, Search.class, new RequestCallBack<Search>() {
        nearbyNetRequest.searchNearbyMerchants("149", "0", keyWord, PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext), PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext), Search.class, new RequestCallBack<Search>() {
            @Override
            public void onSuccess(ResponseInfo<Search> responseInfo) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                Search result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        if (result.getMerchant_list().size() == 0) {
                            imgView_no_date.setVisibility(View.VISIBLE);
                            text_find_data.setVisibility(View.GONE);
                            lvFindData.setVisibility(View.GONE);
                        } else {
                            ArrayList<MerchantList> merchantList = result.merchant_list;
                            ShopsSort.sort(mContext, merchantList);
                            if (page == 0) {
                                merchantLists.clear();
                                merchantLists.addAll(merchantList);
                            } else {
                                merchantLists.addAll(merchantList);
                            }

                            merchantAdapter.notifyDataSetChanged();
                            imgView_no_date.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.GONE);
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

        Intent intent = new Intent(mContext, JPMainActivity.class);
        startActivity(intent);
        AppManager.getInstance().killActivity(AreaCitySelectionAcitivty.class);
        AppManager.getInstance().killActivity(AreaSelectionActivity.class);
    }

    private void initListViewRefresh() {

        lvFindData.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 0;
                String keyWord = edKeyWord.getText().toString();
                if (TextUtils.isEmpty(keyWord)) {
                    lvFindData.onRefreshComplete();
                    return;
                }
                switchKeyWord(keyWord);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                String keyWord = edKeyWord.getText().toString();
                if (TextUtils.isEmpty(keyWord)) {
                    lvFindData.onRefreshComplete();
                    return;
                }
                switchKeyWord(keyWord);
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

    //缓存数据
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

    int findOnclickTime = 0;

    public void find(View view) {
        word = edKeyWord.getText().toString();
        Logger.i("2017年8月17日 09:23:50-" + word);
        page = 0;
        if (word.equals("")) {
            showToast("请输入搜索内容");
            return;
        }
        findOnclickTime++;
        switchKeyWord(word);
    }

    /**
     * 搜索城市
     */
    private void getCityList(final String keyWord) {
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
                            text_find_data.setVisibility(View.GONE);
                            lvFindData.setVisibility(View.VISIBLE);

                        }
                        find_record_linear.setVisibility(View.GONE);
                        cityAdapter = new CityAdapter(mContext, responseInfo.result.cities);
                        lvFindData.setAdapter(cityAdapter);
                        saveSearchHistory(keyWord, type);
                        dismissInputMethod();
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

        mallNetRequest.mallGoodsSearch(sp.getString(Constants.SPKEY_LOCATION_PROVINCE_ID, "0"), keyWord, new RequestCallBack<JPMallGoodsListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPMallGoodsListEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:

                        if (page == 0) {
                            allGoodsLists.clear();
                        }
                        allGoodsLists.addAll(responseInfo.result.list);
                        lvFindData.setAdapter(new JPMallGoodsAdapter(mContext, allGoodsLists, imageLoader));
                        saveSearchHistory(keyWord, type);

                        if (responseInfo.result.list == null || responseInfo.result.list.size() == 0) {
                            text_find_data.setVisibility(View.GONE);
                            imgView_no_date.setVisibility(View.VISIBLE);
                            lvFindData.setVisibility(View.GONE);
                        } else {
                            imgView_no_date.setVisibility(View.GONE);
                            text_find_data.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mallNetRequest != null) {
            mallNetRequest = null;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        Logger.i("ray919-" + "走了LoadMore这里");
        swipeRefreshLayout.setEnabled(false);
        page++;
        getYhaOrJfgGoodsList(word);
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

            if ("1".equals(merchantList.isReserve)) {
                if ("1".equals(merchantList.isDelivery)) {
                    holder.tvIdentityOut.setVisibility(View.VISIBLE);
                    holder.tvIdentityCombo.setVisibility(View.GONE);
                } else {
                    holder.tvIdentityOut.setVisibility(View.GONE);
                    holder.tvIdentityCombo.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tvIdentityOut.setVisibility(View.GONE);
                holder.tvIdentityCombo.setVisibility(View.GONE);
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
        public TextView tvSustainReserve;
        public TextView tvIdentityOut;
        public TextView tvIdentityCombo;

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
            //是否支持在线预订
            tvSustainReserve = (TextView) view.findViewById(R.id.tv_sustain_reserve);

            tvIdentityOut = (TextView) view.findViewById(R.id.tv_identity_out);
            tvIdentityCombo = (TextView) view.findViewById(R.id.tv_identity_combo);
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