package com.yilianmall.merchant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BarCodeSearchEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.BarcodeSearchRecycleAdapter;
import com.yilianmall.merchant.adapter.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 手输条码搜索
 */
public class MerchantInputBarCodeSearchActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private FrameLayout v3Layout;
    private TextView tvNoData;
    private LinearLayout llNothing;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private LinearLayout llContent;
    private TextView tvRefresh;
    private LinearLayout llLoadError;
    private EditText etSearch;
    private ImageView ivSearch;
    private ArrayList<BarCodeSearchEntity.DataBean> searchList = new ArrayList<>();
    private BarcodeSearchRecycleAdapter adapter;
    private String selectGoodsCode, selectGoodsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_manual_barcode_search);
        initView();
        initListener();
    }

    private void initListener() {

        adapter.setOnRefreshButtonClick(new BarcodeSearchRecycleAdapter.OnRefreshDataListener() {


            @Override
            public void onClick(String goodsCode, String goodsCount) {
                MerchantInputBarCodeSearchActivity.this.selectGoodsCode = goodsCode;
                MerchantInputBarCodeSearchActivity.this.selectGoodsCount = goodsCount;
                Logger.i("selectGoodsCode  :: " + goodsCode + "  selectGoodsCount :: " + goodsCount);
                btnAdd.setEnabled(true);
            }
        });

    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("手输条码搜索");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        etSearch = (EditText) findViewById(R.id.et_search);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        tvNoData.setText("请输入条码搜索");
        llNothing = (LinearLayout) findViewById(R.id.ll_nothing);
        llNothing.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration decor = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST, 1, R.color.merchant_color_bg);
        recyclerView.addItemDecoration(decor);
        adapter = new BarcodeSearchRecycleAdapter(searchList, mContext);
        recyclerView.setAdapter(adapter);
        btnAdd = (Button) findViewById(R.id.btn_add);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        llLoadError = (LinearLayout) findViewById(R.id.ll_load_error);
        llLoadError.setVisibility(View.GONE);

        v3Back.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getGoodsCode();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.iv_search) {
            getGoodsCode();
        } else if (i == R.id.btn_add) {
            addList();
        } else if (i == R.id.tv_refresh) {
            getGoodsCode();
        }
    }

    private void addList() {
        startMyDialog(false);
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .scGoodsAddCart(selectGoodsCode, selectGoodsCount, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        stopMyDialog();//防止finish的时候还没有关闭dialog
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            HttpResultBean bean = response.body();
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        showToast("添加商超列表成功");
                                        finish();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }

    public void getGoodsCode() {
        String goodsCode = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(goodsCode)) {
            showToast("请输入您要搜索的条码");
            return;
        }
        getCodeList(goodsCode);
    }

    public void getCodeList(final String goodsCode) {
        startMyDialog(false);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getBarCodeSearchEntity(goodsCode, new Callback<BarCodeSearchEntity>() {
                    @Override
                    public void onResponse(Call<BarCodeSearchEntity> call, Response<BarCodeSearchEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        searchList.clear();
                                        btnAdd.setEnabled(false);
                                        List<BarCodeSearchEntity.DataBean> searchData = response.body().data;
                                        if (null != searchData && searchData.size() > 0) {
                                            llNothing.setVisibility(View.GONE);
                                            llLoadError.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            initList(searchData);
                                            searchList.addAll(searchData);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            recyclerView.setVisibility(View.GONE);
                                            llNothing.setVisibility(View.VISIBLE);
                                            tvNoData.setText("抱歉，没有查询到此条码商品");
                                        }
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<BarCodeSearchEntity> call, Throwable t) {
                        llContent.setVisibility(View.GONE);
                        llLoadError.setVisibility(View.VISIBLE);
                        stopMyDialog();
                    }
                });
    }

    /**
     * 充值列表的选中状态
     * 默认只有一条的情况下当前条目为选中状态并且有库存
     *
     * @param searchData
     */
    private void initList(List<BarCodeSearchEntity.DataBean> searchData) {
        if (searchData.size() == 1) {
            if (Integer.parseInt(searchData.get(0).skuInventory) > 0) {
                searchData.get(0).isCheck = true;
                btnAdd.setEnabled(true);
                selectGoodsCode = searchData.get(0).goodsCode;
                selectGoodsCount = String.valueOf(searchData.get(0).goodsCount);
                adapter.lastPosition=0;
            }
        } else {
            for (int i = 0; i < searchData.size(); i++) {
                searchData.get(i).isCheck = false;
            }
            adapter.lastPosition=-1;
        }
    }
}
