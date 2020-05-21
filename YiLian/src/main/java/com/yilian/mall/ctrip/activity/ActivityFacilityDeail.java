package com.yilian.mall.ctrip.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.AdapterFacilityAndPolicy;
import com.yilian.mall.ctrip.adapter.AdapterFacilityDetail;
import com.yilian.mall.ctrip.bean.FacilityListBean;
import com.yilian.mall.ctrip.fragment.hoteldetail.CtripHotelMapViewFragment;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rxfamily.utils.StringUtils;


/**
 * 作者：马铁超 on 2018/10/27 11:55
 * 设施详情页面
 */

public class ActivityFacilityDeail extends BaseAppCompatActivity {
    private TabLayout tablayout;
    private RecyclerView rvFacilityData;
    private FrameLayout mapview_facility_location;
    private TextView tv_expanded_view, tv_sheshi, tv_hotel_location, tv_tuwen, tv_hotel_feature, v3Title, tv_book_room_note_facility, tv_phone, tvUserRating, tvUserRatingsdec, tvUserRatingdec, tv_address;
    private List<String> tablist = new ArrayList<>();
    private ImageView v3Back;
    private AdapterFacilityDetail adapterFacilityDetail;
    private AdapterFacilityAndPolicy facilityAndPolicyAdapter;
    private View headView;
    private RecyclerView rv_hotel_feature, rv_facility;
    private List<CtripHotelDetailEntity.DataBean.PicturesBean> picturesBean;
    private ArrayList<CtripHotelDetailEntity.DataBean.DescriptionsBean> descriptionDatas;
    private List<CtripHotelDetailEntity.DataBean.FacilitiesBean> facilitiesBeans;
    private List<FacilityListBean> facilityListBeans = new ArrayList<>();
    private CtripHotelDetailEntity.DataBean data;
    FacilityListBean facilityListBean, facilityListBean1, facilityListBean2, facilityListBean3;
    private String cityId;
    CtripHotelMapViewFragment ctripHotelMapViewFragment;
    private com.yilian.mall.ctrip.widget.MyNestedScrollView myscroll;
    private RelativeLayout rl_fility_tag, rl_tuwen, rl_hotel_location, rl_hotel_feature_tag, rl_expanded_view_content;
    private LinearLayout ll_map_cantent;
    private int defaultShowNum = 2;

    /**
     * 是否是ScrollView主动动作
     * false:是ScrollView主动动作
     * true:是TabLayout 主动动作
     */
    private boolean tagFlag = false;
    /**
     * 用于切换内容模块，相应的改变导航标签，表示当一个所处的位置
     */
    private int lastTagIndex = 0;
    /**
     * 用于在同一个内容模块内滑动，锁定导航标签，防止重复刷新标签
     * true: 锁定
     * false ; 没有锁定
     */
    private boolean content2NavigateFlagInnerLock = false, isExpanded;
    private int targetY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detail);
        initHeadView();
        initView();
        initListener();
    }

    /**
     * 列表头部数据
     */
    private void initHeadView() {
        headView = View.inflate(mContext, R.layout.layout_facility_head, null);
        rl_expanded_view_content = headView.findViewById(R.id.rl_expanded_content);
        tv_expanded_view = headView.findViewById(R.id.tv_expanded_view);
        rl_hotel_feature_tag = headView.findViewById(R.id.rl_hotel_feature_tag);
        tv_sheshi = headView.findViewById(R.id.tv_sheshi);
        rl_tuwen = headView.findViewById(R.id.rl_tuwen);
        tv_tuwen = headView.findViewById(R.id.tv_tuwen);
        tv_hotel_feature = headView.findViewById(R.id.tv_hotel_feature);
        rl_fility_tag = headView.findViewById(R.id.rl_fility_tag);
        rv_hotel_feature = (RecyclerView) headView.findViewById(R.id.rv_hotel_feature);
        rv_facility = headView.findViewById(R.id.rv_facility);
        tv_phone = headView.findViewById(R.id.tv_phone);
        tv_book_room_note_facility = headView.findViewById(R.id.tv_book_room_note_facility);
        if (!StringUtils.isEmpty(data.telephone)) {
            tv_phone.setText("联系电话" + data.telephone);
        }
//      酒店政策
        StringBuilder hotelPolicies = new StringBuilder();
        for (CtripHotelDetailEntity.DataBean.PoliciesBean policy : data.policies) {
            hotelPolicies.append(policy.text).append("<br/>");
        }
        tv_book_room_note_facility.setText(Html.fromHtml(hotelPolicies.toString()));

//          填充设施服务数据
//          服务项目
        if (facilitiesBeans != null && facilitiesBeans.size() > 0) {
            facilityListBean = new FacilityListBean();
            facilityListBean.FacilityTitle = "服务项目";
            facilityListBean.facilityListData = new ArrayList<>();

//          活动设施
            facilityListBean1 = new FacilityListBean();
            facilityListBean1.FacilityTitle = "活动设施";
            facilityListBean1.facilityListData = new ArrayList<>();

//          客房设施和服务
            facilityListBean2 = new FacilityListBean();
            facilityListBean2.FacilityTitle = "客房设施和服务";
            facilityListBean2.facilityListData = new ArrayList<>();

//          通用设施
            facilityListBean3 = new FacilityListBean();
            facilityListBean3.FacilityTitle = "通用设施";
            facilityListBean3.facilityListData = new ArrayList<>();

            for (int i = 0; i < facilitiesBeans.size(); i++) {
                if (facilitiesBeans.get(i).facilityItem != null && facilitiesBeans.get(i).facilityItem.size() > 0) {
                    for (int j = 0; j < facilitiesBeans.get(i).facilityItem.size(); j++) {
                        if (facilitiesBeans.get(i).categoryName.equals("服务项目")) {
                            FacilityListBean.FacilityListData data = new FacilityListBean.FacilityListData();
                            data.id = facilitiesBeans.get(i).facilityItem.get(j).id + "";
                            data.name = facilitiesBeans.get(i).facilityItem.get(j).name;
                            data.status = facilitiesBeans.get(i).facilityItem.get(j).status;
                            facilityListBean.facilityListData.add(data);
                        } else if (facilitiesBeans.get(i).categoryName.equals("活动设施")) {
                            FacilityListBean.FacilityListData data = new FacilityListBean.FacilityListData();
                            data.id = facilitiesBeans.get(i).facilityItem.get(j).id + "";
                            data.name = facilitiesBeans.get(i).facilityItem.get(j).name;
                            data.status = facilitiesBeans.get(i).facilityItem.get(j).status;
                            facilityListBean1.facilityListData.add(data);
                        } else if (facilitiesBeans.get(i).categoryName.equals("客房设施和服务")) {
                            FacilityListBean.FacilityListData data = new FacilityListBean.FacilityListData();
                            data.id = facilitiesBeans.get(i).facilityItem.get(j).id + "";
                            data.name = facilitiesBeans.get(i).facilityItem.get(j).name;
                            data.status = facilitiesBeans.get(i).facilityItem.get(j).status;
                            facilityListBean2.facilityListData.add(data);
                        } else if (facilitiesBeans.get(i).categoryName.equals("通用设施")) {
                            FacilityListBean.FacilityListData data = new FacilityListBean.FacilityListData();
                            data.id = facilitiesBeans.get(i).facilityItem.get(j).id + "";
                            data.name = facilitiesBeans.get(i).facilityItem.get(j).name;
                            data.status = facilitiesBeans.get(i).facilityItem.get(j).status;
                            facilityListBean3.facilityListData.add(data);
                        }
                    }
                }
            }
        }
        if (facilityListBean != null && facilityListBean.facilityListData.size() > 0) {
            facilityListBeans.add(facilityListBean);
        }
        if (facilityListBean1 != null && facilityListBean1.facilityListData.size() > 0) {
            facilityListBeans.add(facilityListBean1);
        }
        if (facilityListBean2 != null && facilityListBean2.facilityListData.size() > 0) {
            facilityListBeans.add(facilityListBean2);
        }
        if (facilityListBean3 != null && facilityListBean3.facilityListData.size() > 0) {
            facilityListBeans.add(facilityListBean3);
        }

        rv_facility.setLayoutManager(new LinearLayoutManager(mContext));
        facilityAndPolicyAdapter = new AdapterFacilityAndPolicy(R.layout.layout_facilityandpolicy);
        facilityAndPolicyAdapter.bindToRecyclerView(rv_facility);
        facilityAndPolicyAdapter.setNewData(facilityListBeans);
        // 展开收起 按钮  在列表展示数量大于默认值 时候 显示   相反隐藏
//        if (facilityListBeans.size() > defaultShowNum) {
//            rl_expanded_view_content.setVisibility(View.VISIBLE);
//        } else {
//            rl_expanded_view_content.setVisibility(View.GONE);
//        }

//      酒店特色
        rv_hotel_feature.setLayoutManager(new LinearLayoutManager(mContext));
        if (descriptionDatas != null && descriptionDatas.size() > 0) {
            rv_hotel_feature.setAdapter(
                    new BaseQuickAdapter<CtripHotelDetailEntity.DataBean.DescriptionsBean, BaseViewHolder>(
                            R.layout.item_ctrip_hotel_detail_descriptions, descriptionDatas) {
                        @Override
                        protected void convert(BaseViewHolder helper, CtripHotelDetailEntity.DataBean.DescriptionsBean item) {
                            helper.setText(R.id.tv_content, item.text);
                        }
                    });
        }
    }


    private void initView() {
        ll_map_cantent = findViewById(R.id.ll_map_cantent);
        rl_hotel_location = findViewById(R.id.rl_hotel_location);
        tv_hotel_location = findViewById(R.id.tv_hotel_location);
        myscroll = findViewById(R.id.myscroll);
        tvUserRating = (TextView) findViewById(R.id.tv_user_rating);
        tvUserRatingsdec = (TextView) findViewById(R.id.tv_user_ratingsdec);
        tvUserRatingdec = (TextView) findViewById(R.id.tv_user_ratingdec);
        tv_address = findViewById(R.id.tv_address);
        mapview_facility_location = findViewById(R.id.mapview_facility_location);

        tablayout = (TabLayout) findViewById(R.id.filtrate_tablayout);
        rvFacilityData = (RecyclerView) findViewById(R.id.rv_facility_data);
        v3Title = findViewById(R.id.v3Title);
        v3Back = findViewById(R.id.v3Back);
        tablayout.addTab(tablayout.newTab().setCustomView(tab_icon("推荐理由", R.mipmap.iv_dingwei)));
        tablayout.addTab(tablayout.newTab().setCustomView(tab_icon("设施政策", R.mipmap.iv_dingwei)));
        tablayout.addTab(tablayout.newTab().setCustomView(tab_icon("图文详情", R.mipmap.iv_dingwei)));
        tablayout.addTab(tablayout.newTab().setCustomView(tab_icon("交通娱乐", R.mipmap.iv_dingwei)));
        v3Title.setText("设施详情");
        rvFacilityData.setLayoutManager(new LinearLayoutManager(mContext));
        rvFacilityData.setFocusable(false);
        if (!StringUtils.isEmpty(CtripHomePageActivity.mCtripSite.cityId)) {
            cityId = CtripHomePageActivity.mCtripSite.cityId;
        } else {
            cityId = "0";
        }
        //      地图
        if (data != null) {
            ctripHotelMapViewFragment = new CtripHotelMapViewFragment(mContext, cityId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mapview_facility_location, CtripHotelMapViewFragment.getInstance(new LatLng(data.gdLat, data.gdLng), ctripHotelMapViewFragment))
                    .commit();
            //      地址
            tv_address.setText("地址：" + data.cityName + data.areaName + data.adjacentIntersection + data.address);
            //      住客点评
            tvUserRating.setText(data.ctripUserRating);
            tvUserRatingsdec.setText(data.ctripUserRatingsDec);
            tvUserRatingdec.setText(data.ctripUserRatingDec);
            adapterFacilityDetail = new AdapterFacilityDetail(R.layout.adapter_facility_ivlist);
            adapterFacilityDetail.bindToRecyclerView(rvFacilityData);
            adapterFacilityDetail.addHeaderView(headView);
            adapterFacilityDetail.setNewData(picturesBean);
            rvFacilityData.setAdapter(adapterFacilityDetail);
        }

    }

    @SuppressLint("NewApi")
    private void initListener() {
        //设施政策列表 点击 展开 收起
        RxUtil.clicks(tv_expanded_view, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (facilityListBeans != null && facilityListBeans.size() > 0) {

                }
            }
        });


        // 返回键监听
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                onBackPressed();
            }
        });
//      拨打联系电话  data.telephone
        RxUtil.clicks(tv_phone, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!StringUtils.isEmpty(data.telephone)) {
                    call(data.telephone);
                }
            }
        });
        TabLayout.Tab tab = tablayout.getTabAt(0);
        resetTabChecked(tab, true);
        myscroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //表明当前的动作是由 ScrollView 触发和主导
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tagFlag = true;
                }
                return false;
            }
        });


        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tagFlag = false;
                resetTabChecked(tab, true);
                resetScrollIndex(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                resetTabChecked(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                tagFlag = false;
                resetScrollIndex(tab);
            }
        });
        myscroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (tagFlag == true) {
                    if (scrollY > rl_hotel_feature_tag.getTop()) {
                        resetTablayoutCheck(0);
                    }
                    if (scrollY > rl_fility_tag.getTop()) {
                        resetTablayoutCheck(1);
                    }
                    if (scrollY > rl_tuwen.getTop()) {
                        resetTablayoutCheck(2);
                    }
                    if (scrollY > ll_map_cantent.getTop()) {
                        resetTablayoutCheck(3);
                    }
                }
            }
        });
    }

    /**
     * 重置tab选中状态
     *
     * @param tab     当前选中tab
     * @param isCheck 当前tab 设置选中状态
     */
    private void resetTabChecked(TabLayout.Tab tab, boolean isCheck) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.tabicon);
        TextView txt_title = (TextView) view.findViewById(R.id.tabtext);
        if (isCheck == true) {
            img_title.setVisibility(View.VISIBLE);
            txt_title.setTextColor(Color.parseColor("#FF4289FF"));
        } else {
            img_title.setVisibility(View.GONE);
            txt_title.setTextColor(Color.parseColor("#ff333333"));
        }
    }

    private void resetScrollIndex(TabLayout.Tab tab) {
        //表明当前的动作是由 TabLayout 触发和主导
        tagFlag = false;
        if (tab != null) {
            // 根据点击的位置，使ScrollView 滑动到对应区域
            // 计算点击的导航标签所对应内容区域的高度
            targetY = 0;
            switch (tab.getPosition()) {
                case 0:
                    targetY = rl_hotel_feature_tag.getTop();
                    break;
                case 1:
                    targetY = rl_fility_tag.getTop();
                    break;
                case 2:
                    targetY = rl_tuwen.getTop();
                    break;
                case 3:
                    targetY = ll_map_cantent.getTop();
                default:
                    break;
            }
            myscroll.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myscroll.scrollTo(0, targetY);
                }
            }, 100);
        }
    }

    /**
     * 重置tab选中状态
     * 当由ScroView 触发滑动时 改变tab选中
     *
     * @param currentTagIndex 选中下标
     */
    private void resetTablayoutCheck(int currentTagIndex) {
        // 上一个位置与当前位置不一致是，解锁内部锁，是导航可以发生变化
        if (lastTagIndex != currentTagIndex) {
            content2NavigateFlagInnerLock = false;
        }
        if (!content2NavigateFlagInnerLock) {
            // 锁定内部锁
            content2NavigateFlagInnerLock = true;
            // 动作是由ScrollView触发主导的情况下，导航标签才可以滚动选中
            if (tagFlag) {
//                tablayout.setScrollPosition(currentTagIndex, 0, true);
            }
        }
        lastTagIndex = currentTagIndex;
    }

    /*
    *    for (int i = 0; i < tablayout.getTabCount(); i++) {
                    if (i == currentTagIndex) {
                        TabLayout.Tab tab = tablayout.getTabAt(i);
                        View view = tab.getCustomView();
                        ImageView img_title = (ImageView) view.findViewById(R.id.tabicon);
                        TextView txt_title = (TextView) view.findViewById(R.id.tabtext);
                        img_title.setVisibility(View.VISIBLE);
                        txt_title.setTextColor(Color.parseColor("#FF4289FF"));
                    } else {
                        TabLayout.Tab tab = tablayout.getTabAt(i);
                        View view = tab.getCustomView();
                        ImageView img_title = (ImageView) view.findViewById(R.id.tabicon);
                        TextView txt_title = (TextView) view.findViewById(R.id.tabtext);
                        img_title.setVisibility(View.GONE);
                        txt_title.setTextColor(Color.parseColor("#ff333333"));
                    }
                }*/


    /**
     * 接收上个页面传过来的 酒店详情数据
     *
     * @param mCtripHotelDetailEntity
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void messageEventBus(CtripHotelDetailEntity mCtripHotelDetailEntity) {
//      图文详情列表数据
        picturesBean = mCtripHotelDetailEntity.data.pictures;
        descriptionDatas = mCtripHotelDetailEntity.data.descriptions;
//      设施服务
        facilitiesBeans = mCtripHotelDetailEntity.data.facilities;
        data = mCtripHotelDetailEntity.data;
    }


    /**
     * 设置tablayout 图标
     *
     * @param name
     * @param iconID
     * @return
     */
    private View tab_icon(String name, int iconID) {
        View newTab = LayoutInflater.from(this).inflate(R.layout.tablayout_icon_view, null);
        TextView tv = (TextView) newTab.findViewById(R.id.tabtext);
        tv.setText(name);
        ImageView im = (ImageView) newTab.findViewById(R.id.tabicon);
        im.setImageResource(iconID);
        im.setVisibility(View.GONE);
        return newTab;
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
