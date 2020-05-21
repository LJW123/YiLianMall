package com.yilian.mall.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.NewMerchantListAdapter;
import com.yilian.mall.entity.ComboListEntity;
import com.yilian.mall.entity.CountyList;
import com.yilian.mall.entity.Location;
import com.yilian.mall.entity.MerchantList;
import com.yilian.mall.entity.Nearby;
import com.yilian.mall.entity.StoreClass;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.utils.AMapLocationSuccessListener;
import com.yilian.mall.utils.AMapLocationUtil;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ShopsSort;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by liuyuqi on 2016/12/5 0005.
 * 本地商家列表-入口是送券商家
 */
public class MTNearActivity extends BaseNearActivity {
    static int i;
    @ViewInject(R.id.iv_no_date)
    ImageView mImgNoData;
    ArrayList<CountyList.county> county;
    ArrayList<StoreClass> storeClasses;
    ArrayList<StoreClass> stores;
    MTNetRequest nearbyNetRequest;
    AccountNetRequest netRequest;
    LvAreaAdapter lvAreaAdapter;
    /**
     * 首次进入页面，不进行定位，
     * 文本显示用户选择区县，且根据用户选择的区县获取商家列表，
     * 如果用户在该界面手动定位，那么就获取定位地理位置的商家列表
     */
    boolean isFirst = true;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.mt_radio_area_select)
    private CheckBox checkBoxAreaSelect;
    @ViewInject(R.id.mt_radio_store_class)
    private CheckBox checkBoxStoreClass;
    private PopupWindow storeClassPopupWindow;
    private PopupWindow areaPopupWindow;
    @ViewInject(R.id.mall_title)
    private TextView mall_title;
    @ViewInject(R.id.ll_check_box)
    private LinearLayout llCheckBox;
    private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> mapStores = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> mapStoreClasss = new HashMap<Integer, Integer>();
    private int pageIndex = 0;
    @ViewInject(R.id.lv_near_data)
    private RecyclerView recyclerView;
    @ViewInject(R.id.refreshable_view)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.iv_refresh_address)
    private ImageView mIvRefreshAddress;
    @ViewInject(R.id.txt_address)
    private TextView mTxtAddress;
    @ViewInject(R.id.lv_hard)
    private View lvHard;
    private Animation mRefreshAnim;
    private String mIndustryTop = "0";
    private String mIndustrySon = "0";
    private boolean flag = true;
    private String mCurrCityId;
    private String mCurrCountyId;
    private String mProvinceId;
    private String hasPackage;
    // 商家类型 的背景数组
    private int[] mStoreClass = {R.drawable.near_store_class_onclick_all_class, // 全部分类
            // R.drawable.near_store_class_onclick_duihuan, // 兑换中心
            R.drawable.near_store_class_onclick_food, // 美食
            R.drawable.near_store_class_onclick_play, // 休闲娱乐
            R.drawable.near_store_class_onclick_hotel, // 酒店
            R.drawable.near_store_class_onclick_life_service, // 生活服务
            R.drawable.near_store_class_onclick_meirong, // 美容
            R.drawable.near_store_class_onclick_baihuo, // 百货
            R.drawable.near_store_class_onclick_furniture, // 家具房产
            R.drawable.near_store_class_onclick_education, // 学习培训
            R.drawable.near_store_class_onclick_car, // 汽车
            R.drawable.near_store_class_onclick_other}; // 其他
    private List<MerchantList> storeList = new ArrayList<MerchantList>();
    private List<ComboListEntity.DataBean> copyComboList = new ArrayList<>();
    private String categroy;
    private String lat = PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext);
    private String lng = PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext);
    private NewMerchantListAdapter merchantListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_near_avtivity);
        ViewUtils.inject(this);
        nearbyNetRequest = new MTNetRequest(this.mContext);
        netRequest = new AccountNetRequest(this.mContext);
        mIndustryTop = getIntent().getStringExtra("mIndustryTop");
        mIndustryTop = mIndustryTop == null ? "0" : mIndustryTop;
        //获取需要展示的listView的类型
        categroy = getIntent().getStringExtra("categroy");
        Logger.i("传递的categroy  " + categroy);

        hasPackage = getIntent().getStringExtra("has_package");
        if (TextUtils.isEmpty(hasPackage)) {
            hasPackage = "0";
        }

        county = new ArrayList();
        lvAreaAdapter = new LvAreaAdapter(this.mContext);

        String countyId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, this.mContext, "0");
        if (!countyId.equals(mCurrCountyId)) {
            mCurrCountyId = countyId;
        }
        mRefreshAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.rotate_refresh);
        checkLocationPermission();
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        merchantListAdapter = new NewMerchantListAdapter(R.layout.item_merchant, storeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ivBack.setVisibility(View.VISIBLE);
        initData();
        Listener();
    }

    private void initData() {
        String cityId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, this.mContext, "0");
        mCurrCityId = cityId;
        String provinceId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_PROVINCE_ID, this.mContext, "0");
        mProvinceId = provinceId;
        String provinceName = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_PROVINCE_NAME, this.mContext, "0");
        String cityName = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_NAME, this.mContext, "0");
        String countyName = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_NAME, this.mContext, "0");
        String selectionAddress = provinceName;
        if (!provinceName.equals(cityName)) {
            selectionAddress += cityName;
        }
        if (!cityName.equals(countyName)) {
            selectionAddress += countyName;
        }
        mTxtAddress.setText(selectionAddress);
        getFirstPageData();
        getCityData();
    }

    //使用接口抽出去方法
    public void refreshAddress() {
        checkLocationPermission();
    }

    void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            if (isFirst) {
                isFirst = false;
                return;
            }
            location();
        }
    }

    private void Listener() {
        merchantListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerView);
        RxUtil.clicks(mIvRefreshAddress, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                refreshAddress();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        mImgNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MTNearActivity.this.mContext, JPSignActivity.class));
                Intent intent = new Intent(mContext, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                startActivity(intent);
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // TODO listview 点击事件要区分当前的点击的入口
        if ("merchant".equals(categroy)) { //商家
            merchantListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent;
                    intent = new Intent(mContext, MTMerchantDetailActivity.class);
                    Logger.i("position " + position);
                    intent.putExtra("merchant_id", storeList.get(position).merchantId);
                    Logger.i("传递前的merchant_id" + storeList.get(position).merchantId);
                    Logger.i("商家集合：" + storeList.toString());
                    startActivity(intent);
                }
            });
        }

        // 商家分类
        checkBoxStoreClass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxAreaSelect.setChecked(false);
                    getStoreClassPopupWindow();
                    storeClassPopupWindow.showAsDropDown(llCheckBox);
                }
            }
        });

        //区域的选择
        checkBoxAreaSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkBoxStoreClass.setChecked(false);
                    getAreaPopupWindow(0);
                    areaPopupWindow.showAsDropDown(llCheckBox);
                }
            }
        });
    }

    /**
     * 获取商家分类的popupwindow
     */
    private void getStoreClassPopupWindow() {
        if (null != storeClassPopupWindow) {
            storeClassPopupWindow.dismiss();
        } else {
            initStoreClassPopuptWindow();
        }
    }

    /**
     * 搜索
     */
    public void find(View view) {
        Intent intent = null;
        if ("merchant".equals(categroy)) {
            intent = new Intent(mContext, MerchantSearchActivity.class);//跳转商家搜索页面
            intent.putExtra(Constants.CITYID, mCurrCityId);
            intent.putExtra(Constants.COUNTYID, mCurrCountyId);
            intent.putExtra("type", "merchant");
        } else if ("combo".equals(categroy)) {
            intent = new Intent(mContext, MTFindActivity.class);//跳转套餐搜索页面
            intent.putExtra("type", "combo");
        }
        startActivity(intent);
    }

    /**
     * 商家分类的popupwindow
     */
    private void initStoreClassPopuptWindow() {
        final JSONArray js;
        try {
            // 获取 assets 文件 （商家类型数据源）
            InputStream is = getResources().getAssets().open("config_industry.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "utf-8");

            storeClasses = new ArrayList<StoreClass>();
            js = new JSONArray(json);
            Gson gson = new Gson();
            // 取前12位。判断前12为的getIndustry_name() 是否小于0
            for (int i = 0; i < 12; i++) {
                if (gson.fromJson(js.getJSONObject(i).toString(), StoreClass.class).getIndustry_parent().equals("0")) {
                    storeClasses.add(gson.fromJson(js.getJSONObject(i).toString(), StoreClass.class));
                }
            }

            // 获取自定义布局文件popupwindow_amenduserphoto.xml的视图
            View popupWindow_view = getLayoutInflater().inflate(R.layout.popupwindow_near_two, null, false);

            // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
            ListView lv_storeClasses = (ListView) popupWindow_view.findViewById(R.id.lv_popup_two);
            final ListView lv_stores = (ListView) popupWindow_view.findViewById(R.id.lv_popup_stors);
            final LvStoreClasseAdapter lvStoreClassAdapter = new LvStoreClasseAdapter(mContext, storeClasses,
                    mStoreClass);
            // "定位失败！"));
            lv_storeClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                    RadioHolder holder = new RadioHolder(arg1);
                    holder.radioStoreClasss.setChecked(true);
                    mapStoreClasss.clear();
                    mapStoreClasss.put(arg2, 100);
                    lvStoreClassAdapter.notifyDataSetChanged();
                    pageIndex = 0;
                    mapStores.clear();

                    if (arg2 == 0) {
                        lv_stores.setVisibility(View.INVISIBLE);

                        mIndustryTop = "0";
                        mIndustrySon = "0";
                        getFirstPageData();
                        storeClassPopupWindow.dismiss();
                        checkBoxStoreClass.setChecked(false);
                    } else {

                        lv_stores.setVisibility(View.VISIBLE);
                        Gson gson = new Gson();
                        // 取前12位。判断前12为的getIndustry_name() 是否小于0
                        try {
                            stores = new ArrayList<StoreClass>();
                            stores.clear();
                            for (int i = 11; i < js.length(); i++) {
                                if (gson.fromJson(js.getJSONObject(i).toString(), StoreClass.class).getIndustry_parent()
                                        .equals(storeClasses.get(arg2).getIndustry_id())) {
                                    stores.add(gson.fromJson(js.getJSONObject(i).toString(), StoreClass.class));
                                }
                            }
                            final StoreAdapter lvStoreAdapter = new StoreAdapter(mContext, stores);
                            lv_stores.setAdapter(lvStoreAdapter);
                            lv_stores.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    pageIndex = 0;
                                    mapStores.clear();
                                    mapStores.put(position, 100);
                                    lvStoreAdapter.notifyDataSetChanged();
                                    sp.edit().putString("sotre_type", "1").commit();

                                    mIndustryTop = stores.get(position).getIndustry_parent();
                                    mIndustrySon = stores.get(position).getIndustry_id();
                                    storeClassPopupWindow.dismiss();
                                    checkBoxStoreClass.setChecked(false);
                                    getFirstPageData();

                                }
                            });

                        } catch (JsonSyntaxException | JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            stores.clear();
                        }

                    }
                }
            });

            int height = (int) (llCheckBox.getHeight() * 10);
            storeClassPopupWindow = new PopupWindow(popupWindow_view, llCheckBox.getWidth(), height, true);
            // 设置动画效果
            // popupWindow.setAnimationStyle(R.style.AnimationFade);
            storeClassPopupWindow.setOnDismissListener(new popupDismissListener());

            lv_storeClasses.setAdapter(lvStoreClassAdapter);
            storeClassPopupWindow.setFocusable(true);
            // 点击其他地方消失
            popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (storeClassPopupWindow != null && storeClassPopupWindow.isShowing()) {
                        storeClassPopupWindow.dismiss();
                        checkBoxStoreClass.setChecked(false);
                        checkBoxAreaSelect.setChecked(false);
                        storeClassPopupWindow = null;
                        mapStoreClasss.clear();
                    }
                    return false;
                }
            });

        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /***
     * 获取PopupWindow实例
     */
    private void getAreaPopupWindow(int width) {
        if (null != areaPopupWindow) {
            areaPopupWindow.dismiss();
        } else {
            initAreaPopuptWindow(width);
        }
    }

    /**
     * 创建区域选择 PopupWindow
     */
    protected void initAreaPopuptWindow(int width) {
        // TODO Auto-generated method stub
        // 获取自定义布局文件popupwindow_amenduserphoto.xml的视图
        View areaView = getLayoutInflater().inflate(R.layout.popupwindow_near_store_classs, null, false);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度

        ListView lv_area = (ListView) areaView.findViewById(R.id.lv_popup_one);
        lv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (areaPopupWindow != null) {
                    pageIndex = 0;
                    RadioHolder holder = new RadioHolder(arg1);
                    holder.radio.setChecked(true);
                    map.clear();
                    map.put(arg2, 100);
                    flag = false;
                    lvAreaAdapter.notifyDataSetChanged();
                    areaPopupWindow.dismiss();
                    checkBoxStoreClass.setChecked(false);
                    checkBoxAreaSelect.setChecked(false);
                    areaPopupWindow = null;
                    CountyList.county selectCounty = MTNearActivity.this.county.get(arg2);
                    mCurrCountyId = selectCounty.regionId;
                    mTxtAddress.setText(selectCounty.regionName);
                    stopAnim();
                    getFirstPageData();
                }
            }
        });
        int height = (int) (llCheckBox.getHeight() * 10);
        areaPopupWindow = new PopupWindow(areaView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        areaPopupWindow.showAtLocation(llCheckBox, Gravity.TOP, -(llCheckBox.getWidth() / 2), mall_title.getHeight() + llCheckBox.getHeight() + CommonUtils.getStatusBarHeight(mContext));
        // 设置动画效果
        // popupWindow.setAnimationStyle(R.style.AnimationFade);
        areaPopupWindow.setOnDismissListener(new popupDismissListener());

        lv_area.setAdapter(lvAreaAdapter);
        // 点击其他地方消失
        areaView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (areaPopupWindow != null && areaPopupWindow.isShowing()) {
                    areaPopupWindow.dismiss();
                    checkBoxStoreClass.setChecked(false);
                    checkBoxAreaSelect.setChecked(false);
                    areaPopupWindow = null;
                    map.clear();
                }
                return false;
            }
        });
    }

    private void getNextPageData() {
        pageIndex++;
        getStoreData();
    }

    //获取城市区县的列表
    void getCityData() {
        netRequest.county(sp.getString(Constants.SPKEY_SELECT_CITY_ID, "149"), new RequestCallBack<CountyList>() {
            @Override
            public void onSuccess(ResponseInfo<CountyList> responseInfo) {
                switch (responseInfo.result.code) {
                    case -23:
                    case 1:
                    case -4:
                        county.clear();
                        CountyList c = new CountyList();
                        CountyList.county c1 = c.new county();
                        c1.regionName = "全部";
                        c1.regionId = "0";
                        county.add(c1);
                        county.addAll(responseInfo.result.counties);
                        //map.clear();
                        for (CountyList.county count : responseInfo.result.counties) {
                            String selectCountyId = sp.getString(Constants.SPKEY_SELECT_COUNTY_ID, "");
                            if (selectCountyId.equals(count.regionId) && flag) {
                                map.put(county.indexOf(count), 100);
                            }
                        }
                        lvAreaAdapter.setDataSource(county);
                        break;
                    default:
                        showToast("获取" + sp.getString(Constants.SPKEY_SELECT_CITY_NAME, "郑州") + "区域信息失败");
                        break;
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            switch (requestCode) {
                case LOCATION_REQUEST_CODE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        location();
                    } else {
                        showToast(getString(com.yilianmall.merchant.R.string.merchant_remind_location_permission));
                    }
                    break;
            }
        }

    }

    private void location() {
        startAnim();
        AMapLocationUtil.getInstance()
                .initLocation(mContext)
                .location(new AMapLocationSuccessListener() {
                    @Override
                    public void amapLocationSuccessListener(AMapLocation aMapLocation, Location.location location) {
                        lat = location.lat;
                        lng = location.lng;
                        mCurrCityId = location.cityId;
                        mProvinceId = location.provinceId;
                        mCurrCountyId = location.countyId;
                        mTxtAddress.setText(aMapLocation.getAddress());
                        getFirstPageData();//重新定位后再次获取商家列表数据
                        stopAnim();
                    }

                    @Override
                    public void amapLocationFailureListener(HttpException e, String s) {

                    }
                }).startLocation();
    }

    @Override
    public void startAnim() {
        mRefreshAnim.reset();
        mIvRefreshAddress.clearAnimation();
        mIvRefreshAddress.startAnimation(mRefreshAnim);
    }

    @Override
    public void stopAnim() {
        mRefreshAnim.reset();
        mIvRefreshAddress.clearAnimation();
    }

    /**
     * 获取第一页数据
     */
    private void getFirstPageData() {
        swipeRefreshLayout.setRefreshing(true);
        pageIndex = 0;
        getStoreData();
    }

    private void getStoreData() {
        getMerchantData();
        mall_title.setText("线下商家");
        lvHard.setVisibility(View.GONE);
    }

    //获取商家的列表
    private void getMerchantData() {
        nearbyNetRequest.getNewNearMerchantList(pageIndex, mIndustryTop, mIndustrySon, mCurrCityId, mCurrCountyId, hasPackage, lat, lng,
                new RequestCallBack<Nearby>() {
                    @Override
                    public void onSuccess(ResponseInfo<Nearby> arg0) {
                        switch (arg0.result.code) {
                            case 0:
                                mImgNoData.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                break;
                            case 1:
                                if (pageIndex == 0) {
                                    storeList.clear();
                                }

                                List<MerchantList> merchantLists = arg0.result.merchantList;

                                if (null != merchantLists && merchantLists.size() >= 1) {
                                    storeList.addAll(merchantLists);
                                    ShopsSort.sort(mContext, storeList);
                                    recyclerView.setAdapter(merchantListAdapter);
                                    mImgNoData.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    if (storeList.size() > 0) {
                                        mImgNoData.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        if (pageIndex >= 1) {
                                            showToast(R.string.no_more_data);
                                        }
                                    } else {
                                        mImgNoData.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }
                                swipeRefreshLayout.setRefreshing(false);
                                break;
                            default:
                                showToast(arg0.result.msg);
                                break;
                        }
                       swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                        showToast(R.string.net_work_not_available);
                    }

                });
    }

    class RadioHolder {
        private RadioButton radio;
        private RadioButton radioStoreClasss;
        private RadioButton radioStores;

        public RadioHolder(View view) {
            this.radioStoreClasss = (RadioButton) view.findViewById(R.id.popup_store_class_lv_cb);
            this.radio = (RadioButton) view.findViewById(R.id.popup_near_area_lv_checkbox);
            this.radioStores = (RadioButton) view.findViewById(R.id.popup_near_area_lv_radiobutton);
        }
    }

    class popupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            Logger.i("我是关闭事件");
            checkBoxStoreClass.setChecked(false);
            checkBoxAreaSelect.setChecked(false);
            areaPopupWindow = null;
            //map.clear();

        }
    }

    /**
     * 地区选择
     *
     * @author lauyk
     */
    private class LvAreaAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<CountyList.county> mList;
        private LayoutInflater inflater;

        public LvAreaAdapter(Context context) {
            this.mContext = context;
            this.inflater = LayoutInflater.from(context);
        }

        public void setDataSource(ArrayList<CountyList.county> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mList == null) {
                return 0;
            } else {
                return this.mList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mList == null) {
                return null;
            } else {
                return this.mList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RadioHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.popupwindow_near_area_lv, null);
                holder = new RadioHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RadioHolder) convertView.getTag();
            }
            holder.radio.setChecked(map.get(position) == null ? false : true);
            Logger.i(map.get(position) + "");
            holder.radio.setText(mList.get(position).regionName);
            return convertView;
        }

    }

    /**
     * 商家类型选择
     *
     * @author lauyk
     */
    private class LvStoreClasseAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<StoreClass> mList;
        private LayoutInflater inflater;
        private int[] mStoreClass;

        public LvStoreClasseAdapter(Context context, ArrayList<StoreClass> mList, int[] mStoreClass) {
            this.mContext = context;
            this.mList = mList;
            this.inflater = LayoutInflater.from(context);
            this.mStoreClass = mStoreClass;
        }

        @Override
        public int getCount() {
            if (mList == null) {
                return 0;
            } else {
                return this.mList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mList == null) {
                return null;
            } else {
                return this.mList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            RadioHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.popupwindow_store_class_lv, null);
                holder = new RadioHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RadioHolder) convertView.getTag();
            }
            holder.radioStoreClasss.setChecked(mapStoreClasss.get(position) == null ? false : true);
            if (this.mList != null) {
                holder.radioStoreClasss.setText(mList.get(position).getIndustry_name());

                Drawable rightDrawable = getResources().getDrawable(mStoreClass[position]);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                holder.radioStoreClasss.setCompoundDrawables(rightDrawable, null, null, null);
            }

            return convertView;
        }

    }

    private class StoreAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<StoreClass> mList;
        private LayoutInflater inflater;

        public StoreAdapter(Context context, ArrayList<StoreClass> stores) {
            this.mContext = context;
            this.mList = stores;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (mList == null) {
                return 0;
            } else {
                return this.mList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mList == null) {
                return null;
            } else {
                return this.mList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RadioHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.popupwindow_near_store_lv, null);
                holder = new RadioHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RadioHolder) convertView.getTag();
            }
            holder.radioStores.setChecked(mapStores.get(position) == null ? false : true);
            holder.radioStores.setText(mList.get(position).getIndustry_name());
            return convertView;
        }

    }
}
