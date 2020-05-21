package com.yilian.mall.ctrip.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.ctrip.adapter.AdapterDetailHotelFiltrateCondition;
import com.yilian.mall.ctrip.adapter.AdapterDetailHotelFiltrateData;
import com.yilian.mall.ctrip.adapter.AdapterThirdPrice;
import com.yilian.mall.ctrip.adapter.CtripRoomAdapter;
import com.yilian.mall.ctrip.bean.FiltrateDataBean;
import com.yilian.mall.ctrip.bean.FiltrateListBean;
import com.yilian.mall.ctrip.bean.MapHotelInfoBean;
import com.yilian.mall.ctrip.fragment.hoteldetail.CtripHomeDetailDialogFragment;
import com.yilian.mall.ctrip.fragment.hoteldetail.CtripHotelMapViewFragment;
import com.yilian.mall.ctrip.fragment.hoteldetail.DescriptionsDialogFragment;
import com.yilian.mall.ctrip.mvp.CtripHotelDetailContract;
import com.yilian.mall.ctrip.mvp.presenter.CtripHotelDetailPresenterImpl;
import com.yilian.mall.ctrip.util.CtripHomeUtils;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ctrip.widget.PopupWindow.CommonPopupWindow;
import com.yilian.mall.ctrip.widget.cosmocalendar.CalendarViewActivity;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.entity.BookRoomInfo;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CtripIconUtil;
import com.yilian.mall.widgets.rangeSeekBar.RangeSeekBar;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.widget.ScrollListenerScrollView;
import com.yilian.networkingmodule.entity.Hotel_Rooms_FiltrateBean;
import com.yilian.networkingmodule.entity.SearchFilterBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelFilterEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripRoomTypeInfo;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Consumer;
import rx.Subscription;
import rxfamily.utils.StringUtils;

//import com.yilian.mall.adapter.HotelDetail_HotelFiltrateCondition_Adapter;

/**
 * 酒店详情
 */
public class CtripHotelDetailActivity extends BaseAppCompatActivity implements CtripHotelDetailContract.View, CommonPopupWindow.ViewInterface, AdapterDetailHotelFiltrateData.Ref {
    //此key JumpToOtherPage中 同使用 勿改
    public static final String TAG_HOTEL_ID = "tag_hotel_id";
    boolean filtrateChecked = false;
    //  进度最小值,最大范围，范围间隔，预留间隔
    int min_range = 0, max_range = 1000, range_interval = 50, range_reserve = 5;
    int price_range_min, price_range_max, static_max_range = 1000;
    // 价格筛选
    List<SearchFilterBean.PSortBean.SortsBeanX> pSortBeanList;
    String share_type = "24", share_title, share_content, sub_title, share_img, share_url, shareImg;
    MapHotelInfoBean mapHotelInfoBean;
    private Banner banner;
    private TextView tvBannerHotelName;
    private TextView tvRoad;
    private TextView tvTransportationInfo;
    private View viewLine;
    private TextView tvScore;
    private View viewLine2;
    private TextView tvDetail;
    private LinearLayout llOpeningDateAndLable;
    private LinearLayout llFacilities;
    private RelativeLayout rlLabel;
    private TextView tvDescriptions;
    private View viewLine3;
    private TextView tvStartDate;
    private TextView tvDaysNum;
    private TextView tvEndDate;
    private LinearLayout llCheckInRange;
    private RangeSeekBar rangeSeekBar;
    private com.yilian.mall.ctrip.widget.MyLinearLayout llFilterLabelFilter;
    private com.yilian.mylibrary.NoScrollRecyclerView recyclerViewRoom;
    private TextView tvBookRoomNote;
    private LinearLayout llTitle;
    private CtripHotelDetailContract.Presenter ctripHotelDetailPresenter;
    private CtripRoomAdapter ctripRoomAdapter;
    private ScrollListenerScrollView scrollView;
    private CtripHotelDetailEntity mCtripHotelDetailEntity;
    private TextView tvTitleHotelName;
    private ImageView ivTitleBack;
    private ImageView ivTitleShare;
    private int facilitiesCount = 0;
    private FrameLayout mapView;
    private ArrayList<CtripRoomTypeInfo> ctripRoomTypeInfoArrayList = new ArrayList<>();
    private CtripHotelFilterEntity.DataBean.RoomBedInfosBeanX checkedRoomBed = null;
    private int mEnterDateArea;
    private String hotelAddress = "", mHotelId = "", mStartDate = "", mEndDate = "", cityId, RoomBedInfos = "", NumberOfBreakfast = "0", WirelessBroadnet = "0", WiredBroadnet = "0", minPrice = "", maxPrice = "", IsInstantConfirm = "0", CancelPolicyInfos = "-1";
    private CtripHotelDetailEntity ctripHotelDetailData;
    private PopupWindow hotelFiltratePopuWindow;
    private CommonPopupWindow popupWindow;
    private FiltrateListBean filtrateListBean = new FiltrateListBean();
    private AdapterDetailHotelFiltrateCondition condition_adapter;
    private AdapterDetailHotelFiltrateData filtrateDataAdapter;
    private CtripHotelFilterEntity.DataBean dataBean;
    private List<FiltrateDataBean> dataBeans;
    private View filtratePriceView;
    private AdapterThirdPrice thirdPriceAdapter;
    private JPNetRequest jpNetRequest;
    //是否第一次请求分享
    private boolean isFirstGetShare = true;
    private UmengDialog mShareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrip_hotel_detail);
        ImmersionBar immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true).init();
        initView(savedInstanceState);
        setInitDateData();
        initData();
        initListener();
    }

    private void initView(Bundle savedInstanceState) {
        ivTitleBack = findViewById(R.id.iv_title_back);
        ivTitleShare = findViewById(R.id.iv_title_share);
        tvTitleHotelName = findViewById(R.id.tv_title_hotel_name);
        tvTitleHotelName.setSingleLine(true);
        scrollView = findViewById(R.id.scroll_view);
        banner = (Banner) findViewById(R.id.banner);
        banner.post(new Runnable() {
            @Override
            public void run() {
                ((RelativeLayout.LayoutParams) banner.getLayoutParams()).height = banner.getWidth() / 2;
            }
        });
        tvBannerHotelName = (TextView) findViewById(R.id.tv_banner_hotel_name);
        mapView = (FrameLayout) findViewById(R.id.map_view);
        tvRoad = (TextView) findViewById(R.id.tv_road);
        tvTransportationInfo = (TextView) findViewById(R.id.tv_transportation_info);
        viewLine = (View) findViewById(R.id.view_line);
        tvScore = (TextView) findViewById(R.id.tv_score);
        viewLine2 = (View) findViewById(R.id.view_line_2);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        llOpeningDateAndLable = (LinearLayout) findViewById(R.id.ll_opening_date_and_lable);
        llFacilities = (LinearLayout) findViewById(R.id.ll_facilities);
        rlLabel = (RelativeLayout) findViewById(R.id.rl_label);
        tvDescriptions = (TextView) findViewById(R.id.tv_descriptions);
        viewLine3 = (View) findViewById(R.id.view_line_3);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvDaysNum = (TextView) findViewById(R.id.tv_days_num);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        llCheckInRange = (LinearLayout) findViewById(R.id.ll_check_in_range);
        llFilterLabelFilter = (com.yilian.mall.ctrip.widget.MyLinearLayout) findViewById(R.id.ll_filter_label);
        recyclerViewRoom = (com.yilian.mylibrary.NoScrollRecyclerView) findViewById(R.id.recycler_view_room);
        recyclerViewRoom.setNestedScrollingEnabled(false);
        recyclerViewRoom.addItemDecoration(new DividerItemDecoration(mContext, 1, 1, Color.parseColor("#e7e7e7")));
        recyclerViewRoom.setLayoutManager(new LinearLayoutManager(mContext));
        ctripRoomAdapter = new CtripRoomAdapter(new ArrayList<>());
        recyclerViewRoom.setAdapter(ctripRoomAdapter);
        ctripRoomAdapter.bindToRecyclerView(recyclerViewRoom);
        tvBookRoomNote = (TextView) findViewById(R.id.tv_book_room_note);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        llTitle.getBackground().mutate().setAlpha(0);
        llTitle.post(new Runnable() {
            @Override
            public void run() {
                int oldHeight = llTitle.getHeight();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) llTitle.getLayoutParams();
                int statusBarHeight = StatusBarUtils.getStatusBarHeight(mContext);
                layoutParams.height = oldHeight + statusBarHeight;
                llTitle.setPadding(0, statusBarHeight, 0, 0);
            }
        });
    }

    //http://test.i1170.com/app/xiecheng.php?c=xc_hotel_detail&HotelID=559&Start=2018-11-7&End=2018-11-9
    private void setInitDateData() {
        mHotelId = getIntent().getStringExtra(TAG_HOTEL_ID);
        mStartDate = getIntent().getStringExtra("checkIn");
        mEndDate = getIntent().getStringExtra("checkOut");
        mEnterDateArea = (int) CtripHomeUtils.getTimeDistance(mStartDate, mEndDate);
        setStartAndEndDate(mStartDate, mEndDate);
    }

    private void initData() {
//        setStartAndEndDate(mStartDate, mEndDate);
        if (!StringUtils.isEmpty(CtripHomePageActivity.mCtripSite.cityId)) {
            cityId = CtripHomePageActivity.mCtripSite.cityId;
        } else {
            cityId = "0";
        }
        getHotelDetailData();
    }

    private void initListener() {
//      跳转设施详情
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CtripHotelDetailEntity detailEntity = mCtripHotelDetailEntity;
                if (detailEntity != null) {
                    EventBus.getDefault().postSticky(detailEntity);
                }
                JumpCtripActivityUtils.toFacilityDetailActivity(mContext);
            }
        });

        RxUtil.clicks(ivTitleBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                onBackPressed();
            }
        });

//       分享
        RxUtil.clicks(ivTitleShare, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (TextUtils.isEmpty(share_title)) {
                    getShareUrl();
                } else {
                    share();
                }
            }
        });

        ctripRoomAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_book_up:
//                        预定
                        bookRoom(position);
                        break;
                    case R.id.tv_details:
//                        房间详情
                        CtripRoomTypeInfo.RoomInfosBean item2 = (CtripRoomTypeInfo.RoomInfosBean) adapter.getItem(position);

                        CtripHomeDetailDialogFragment.getInstance(item2, position)
                                .show(getSupportFragmentManager(), CtripHomeDetailDialogFragment.TAG);
                        break;
                    default:
                        break;
                }
            }
        });
//      查看酒店详情
        ctripRoomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object item = adapter.getItem(position);
                if (item instanceof CtripRoomTypeInfo.RoomInfosBean) {
//                    CtripHomeDetailDialogFragment.getInstance(i)
                }
            }
        });
//      选择日期后获取酒店数据
        RxUtil.clicks(llCheckInRange, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                CalendarViewActivity.getInstance(new CalendarViewActivity.OnDaysSelected() {
                    @Override
                    public void daysSelected(Pair<Day, Day> days) {
//                        mStartDate = " 2018-09-29";
//                        mEndDate = " 2018-09-30";

                        Day first = days.first;
                        Day second = days.second;
                        mEnterDateArea = DateUtils.differentDays(first.getCalendar(), second.getCalendar());
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        mStartDate = df.format(first.getCalendar().getTime());
                        mEndDate = df.format(second.getCalendar().getTime());
                        getHotelDetailData();
                    }
                }).show(getSupportFragmentManager(), CalendarViewActivity.TAG);
            }
        });
        scrollView.setOnScrollChanged(new ScrollListenerScrollView.OnScrollChanged() {
            int distance = 0;

            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                distance = t;
                if (distance <= 255) {
                    llTitle.getBackground().mutate().setAlpha(distance);
                    tvTitleHotelName.setText("");
                    ivTitleBack.setImageResource(R.mipmap.icon_ctrip_hotel_detail_back_trans);
                    ivTitleShare.setImageResource(R.mipmap.icon_ctrip_hotel_detail_share_trans);

                } else {
                    llTitle.getBackground().mutate().setAlpha(255);
                    tvTitleHotelName.setText(mCtripHotelDetailEntity.data.hotelName);
                    ivTitleBack.setImageResource(R.mipmap.icon_ctrip_hotel_detail_back_white);
                    ivTitleShare.setImageResource(R.mipmap.icon_ctrip_hotel_detail_share_white);
                }

            }
        });
    }

    void setStartAndEndDate(String startDate, String endDate) {
        tvDaysNum.setText(String.format(Locale.getDefault(), "%d晚", mEnterDateArea));
        setStartDate(startDate);
        setEndDate(endDate);
    }

    //  获取酒店详情数据
    private void getHotelDetailData() {
        if (ctripHotelDetailPresenter == null) {
            ctripHotelDetailPresenter = new CtripHotelDetailPresenterImpl(this);
        }
        Subscription subscription = ctripHotelDetailPresenter.getCtripHotelDetailData(mContext, mHotelId, mStartDate, mEndDate);
        Subscription ctripHotelFilter = ctripHotelDetailPresenter.getCtripHotelFilter(mContext);
        addSubscription(subscription);
        addSubscription(ctripHotelFilter);
    }

    /**
     * 获取分享链接
     */
    private void getShareUrl() {
        startMyDialog();
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.getShareUrl(share_type, mHotelId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        String sub_title = responseInfo.result.subTitle;
                        share_content = responseInfo.result.content;
                        share_img = responseInfo.result.imgUrl;
                        share_url = responseInfo.result.url;
                        if (TextUtils.isEmpty(share_img)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg = Constants.ImageUrl + share_img;
                            }
                        }
                        share();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

    private void share() {
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            share_content,
                            shareImg,
                            share_url).share();
                }
            });
        }
        mShareDialog.show();
    }

    public void bookRoom(int position) {
        CtripRoomTypeInfo.RoomInfosBean item = (CtripRoomTypeInfo.RoomInfosBean) ctripRoomAdapter.getItem(position);
        BookRoomInfo bookRoomInfo = new BookRoomInfo();
        bookRoomInfo.hotelCode = mHotelId;
        if (item != null) {
            bookRoomInfo.hotelId = item.hotelID;
            bookRoomInfo.roomId = item.roomID;
            bookRoomInfo.hotelName = mCtripHotelDetailEntity.data.hotelName;
            bookRoomInfo.roomType = item.roomName;
            bookRoomInfo.startDate = mStartDate;
            bookRoomInfo.endDate = mEndDate;
            bookRoomInfo.dateArea = mEnterDateArea;
            bookRoomInfo.bedName = item.bedName;
            bookRoomInfo.wirelessBroadnet = item.wirelessBroadnet;
            bookRoomInfo.breakfast = item.priceInfo.numberOfBreakfast;
            bookRoomInfo.maxRoomCount = 10;
            bookRoomInfo.hotelPhone = mCtripHotelDetailEntity.data.telephone;
            bookRoomInfo.roomDailyPrice = item.priceInfo.dailyExclusiveAmount;
            bookRoomInfo.returnDailyBean = item.priceInfo.dailyReturnBean;
            bookRoomInfo.maxPeoplePerRoom = item.maxOccupancy;
            bookRoomInfo.latestTime = mCtripHotelDetailEntity.data.latestTime;
            bookRoomInfo.isMustBe = mCtripHotelDetailEntity.data.isMustBe;
            bookRoomInfo.setIsHourlyRoom(BookRoomInfo.ROOM_ORDINARY);
            JumpCtripActivityUtils.toCtripBookRoomActivity(mContext, bookRoomInfo);
        }
    }

    void setStartDate(String startDate) {
        mStartDate = startDate;
        String enter = "入住";
        String format = String.format("%s\n%s  %s", enter, CtripHomeUtils.getDateByMMdd(startDate), CtripHomeUtils.getWeek(startDate));
        SpannableStringBuilder spannableStringBuilder = formatDays(enter, format);
        tvStartDate.setText(spannableStringBuilder);
    }

    void setEndDate(String endDate) {
        mEndDate = endDate;
        String leave = "离店";
        String format = String.format("%s\n%s  %s", leave, CtripHomeUtils.getDateByMMdd(endDate), CtripHomeUtils.getWeek(endDate));
        SpannableStringBuilder spannableStringBuilder = formatDays(leave, format);
        tvEndDate.setText(spannableStringBuilder);
    }

    /**
     * 格式化日期
     *
     * @param frontText
     * @param format
     * @return
     */
    @NonNull
    private SpannableStringBuilder formatDays(String frontText, String format) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_4289FF)),
                frontText.length(), format.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    private String getBreakfastNum() {
        return "";
    }

    /**
     * 获取酒店详情数据
     *
     * @param ctripHotelDetailEntity
     */
    @Override
    public void showCtripHotelDetail(CtripHotelDetailEntity ctripHotelDetailEntity) {
//        ******************************************************************************************
//        测试数据
//        String json = AssetsUtil.getJson(mContext, "hoteltest.json");
//        ctripHotelDetailEntity = GsonUtil.getBeanFromJson(json, CtripHotelDetailEntity.class);
//        ******************************************************************************************
        ctripHotelDetailData = ctripHotelDetailEntity;
        mapHotelInfoBean = new MapHotelInfoBean();
        mapHotelInfoBean.gdLat = ctripHotelDetailData.data.gdLat;
        mapHotelInfoBean.gdLong = ctripHotelDetailData.data.gdLng;
        mapHotelInfoBean.hotelName = ctripHotelDetailData.data.hotelName;
        StringBuilder stringDescription = new StringBuilder();
        for (int i = 0; i < ctripHotelDetailData.data.descriptions.size(); i++) {
            stringDescription.append(ctripHotelDetailData.data.descriptions.get(i).text);
        }
        mapHotelInfoBean.hotelInfo = stringDescription.toString();
        mapHotelInfoBean.hotelGrade = ctripHotelDetailData.data.ctripUserRating;
        mapHotelInfoBean.checkIn = mStartDate;
        mapHotelInfoBean.checkOut = mEndDate;
        mapHotelInfoBean.hotelId = ctripHotelDetailData.data.hotelID;
        mapHotelInfoBean.addRess = hotelAddress;
        setStartAndEndDate(mStartDate, mEndDate);
        mCtripHotelDetailEntity = ctripHotelDetailEntity;
        CtripHotelDetailEntity.DataBean data = ctripHotelDetailEntity.data;
        CtripHotelMapViewFragment ctripHotelMapViewFragment = new CtripHotelMapViewFragment(mContext, cityId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.map_view,
                        CtripHotelMapViewFragment.getInstance(new LatLng(data.gdLat, data.gdLng),
                                ctripHotelMapViewFragment))
                .commit();
//      地图查看酒店信息
        findViewById(R.id.fl_map_view_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpCtripActivityUtils.toMapSearchHotelInfo(mContext, mHotelId);
            }
        });
        setOpenYearAndThemeName(data);
        setFacilities(data);
        List<CtripHotelDetailEntity.DataBean.PicturesBean> pictures = data.pictures;
        ArrayList<String> bannerList = new ArrayList<>();
        for (CtripHotelDetailEntity.DataBean.PicturesBean picture : pictures) {
            bannerList.add(picture.url);
        }
        banner.setImages(bannerList)
                .setImageLoader(new BannerViewGlideUtil())
                .start();
        tvBannerHotelName.setText(data.hotelName);
        tvRoad.setText("地址：" + data.cityName + data.areaName + data.adjacentIntersection + data.address);
        hotelAddress = data.cityName + data.areaName + data.adjacentIntersection + data.address;
        if (data.transportationInfos != null && data.transportationInfos.size() > 0) {
            CtripHotelDetailEntity.DataBean.TransportationInfosBean transportationInfosBean = data.transportationInfos.get(0);
            tvTransportationInfo.setText(String.format("距%s%s", transportationInfosBean.name, transportationInfosBean.directions));
        }
        String format;
        if (TextUtils.isEmpty(data.ctripUserRating)) {
            format = String.format("%s   \"%s\"", data.ctripUserRatingsDec, data.ctripUserRatingDec);
        } else {
            format = String.format("%s 分   %s   \"%s\"", data.ctripUserRating, data.ctripUserRatingsDec, data.ctripUserRatingDec);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
        AbsoluteSizeSpan absoluteSizeSpanScore = new AbsoluteSizeSpan(25, true);
        AbsoluteSizeSpan absoluteSizeSpanLable = new AbsoluteSizeSpan(15, true);
        spannableStringBuilder.setSpan(absoluteSizeSpanScore, 0, data.ctripUserRating.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(absoluteSizeSpanLable, data.ctripUserRating.length() + 4, data.ctripUserRating.length() + 4 + data.ctripUserRatingsDec.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvScore.setText(spannableStringBuilder);
        ArrayList<CtripHotelDetailEntity.DataBean.DescriptionsBean> descriptions = data.descriptions;
        if (descriptions != null && descriptions.size() > 0) {
            tvDescriptions.setText(descriptions.get(0).text);
//          酒店特色弹出框
            tvDescriptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DescriptionsDialogFragment.getInstance(descriptions).show(getSupportFragmentManager(), DescriptionsDialogFragment.TAG);
                }
            });
        }
        ctripRoomTypeInfoArrayList.clear();
        for (CtripRoomTypeInfo roomTypeInfo : data.roomTypeInfos) {
            for (CtripRoomTypeInfo.RoomInfosBean roomInfosBean : roomTypeInfo.roomInfos) {
                roomInfosBean.pictures = roomTypeInfo.pictures;
                roomInfosBean.policies = data.policies;
                roomInfosBean.facilities = roomTypeInfo.facilities;
                roomTypeInfo.addSubItem(roomInfosBean);
            }
            ctripRoomTypeInfoArrayList.add(roomTypeInfo);
        }
        if (ctripRoomTypeInfoArrayList != null && ctripRoomTypeInfoArrayList.size() > 0) {
            ctripRoomAdapter.getData().clear();
            ctripRoomAdapter.notifyDataSetChanged();
            ctripRoomAdapter.addData(ctripRoomTypeInfoArrayList);
        } else {
            //放置空页面
            ctripRoomAdapter.getData().clear();
            ctripRoomAdapter.setNewData(ctripRoomAdapter.getData());
            ctripRoomAdapter.setEmptyView(R.layout.view_hotel_no_data);
            ctripRoomAdapter.notifyDataSetChanged();
        }

        StringBuilder hotelPolicies = new StringBuilder();
        for (int i = 0; i < data.policies.size(); i++) {
//            入住政策只取前四条不为空的字段
            if (i == 4) {
                break;
            }
            CtripHotelDetailEntity.DataBean.PoliciesBean policiesBean = data.policies.get(i);
            if (!TextUtils.isEmpty(policiesBean.text)) {
                hotelPolicies.append(policiesBean.text).append("<br/>");
            }

        }
//      订房必读
        tvBookRoomNote.setText(Html.fromHtml(hotelPolicies.toString()));
    }

    /**
     * 设置开店时间和主题信息
     *
     * @param data
     */
    private void setOpenYearAndThemeName(CtripHotelDetailEntity.DataBean data) {
        llOpeningDateAndLable.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        TextView openYear = new TextView(mContext);
        openYear.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        DateFormat instance = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = instance.parse(data.openYear);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            openYear.setText(String.format(Locale.getDefault(), "%d年开业", calendar.get(Calendar.YEAR)));
        } catch (Exception e) {
            e.printStackTrace();
            openYear.setText(data.openYear);
        }
        openYear.setLayoutParams(params);
//      开业时间
        llOpeningDateAndLable.addView(openYear);
        String[] themesNames = data.themesName.split(",");
        for (int i = 0; i < themesNames.length; i++) {
            // 1、设置固定大小
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 100);
            //设置margin值
            lp.setMargins(20, 0, 20, 0);
            TextView themeName = new TextView(mContext);
            themeName.setLayoutParams(lp);
            themeName.setTextColor(ContextCompat.getColor(mContext, R.color.color_4289FF));
            themeName.setBackgroundResource(R.drawable.bg_ctrip_hotel_theme);
            themeName.setText(themesNames[i]);
            themeName.setPadding(2, 2, 2, 2);
            themeName.setLayoutParams(params);
            llOpeningDateAndLable.addView(themeName);
            if (i > 2) {
                break;
            }
        }
    }

    /**
     * 设置设施信息
     *
     * @param data
     */
    private void setFacilities(CtripHotelDetailEntity.DataBean data) {
        if (data.facilities != null && data.facilities.size() > 0) {
            for (CtripHotelDetailEntity.DataBean.FacilitiesBean facility : data.facilities) {
                if (facility.facilityItem != null && facility.facilityItem.size() > 0) {
                    for (CtripHotelDetailEntity.DataBean.FacilitiesBean.FacilityItemBean facilityItemBean : facility.facilityItem) {
                        TextView textView = new TextView(mContext);
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
                        textView.setTextSize(10);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.rightMargin = 2;
                        textView.setLayoutParams(params);
                        textView.setText(facilityItemBean.name);
//                        id是从1开始，而图片资源的获取是从0开始的，所以此处需要修正
                        try {
                            Bitmap imageFromAssetsFile = CtripIconUtil.getImageFromAssetsFile(--facilityItemBean.id, mContext);
                            ImageView imageView = new ImageView(mContext);
                            imageView.setImageBitmap(imageFromAssetsFile);
//                        imageView.setLayoutParams(new LinearLayout.LayoutParams(DPXUnitUtil.dp2px(mContext, 12), DPXUnitUtil.dp2px(mContext, 12)));
                            llFacilities.addView(imageView);
                            LinearLayout.LayoutParams imageViewParams
                                    = new LinearLayout.LayoutParams(
                                    DPXUnitUtil.dp2px(mContext, 12), ViewGroup.LayoutParams.MATCH_PARENT);
                            imageViewParams.gravity = Gravity.CENTER_VERTICAL;
                            imageView.setLayoutParams(imageViewParams);
                        } catch (Exception e) {
                            continue;
                        }
                        llFacilities.addView(textView);
                        facilitiesCount++;
                        if (facilitiesCount > 2) {
                            break;
                        }
                    }

                }
                if (facilitiesCount > 2) {
                    break;
                }
            }
        }
    }

    //  酒店详情筛选卡
    @Override
    public void showCtripFilter(CtripHotelFilterEntity ctripHotelFilterEntity) {
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dataBean = ctripHotelFilterEntity.data;
        CtripHotelFilterEntity.DataBean.OpeartionsBean opeartions = ctripHotelFilterEntity.data.opeartions;
        CtripHotelFilterEntity.DataBean.PSortBean pSortBean = ctripHotelFilterEntity.data.pSort;
        setFilterView(opeartions);
        initFiltratePriceView(pSortBean);
    }

    //  设置酒店详情筛选卡数据和样式
    private void setFilterView(CtripHotelFilterEntity.DataBean.OpeartionsBean opeartions) {
        llFilterLabelFilter.removeAllViews();
        FiltrateListBean.listData = new ArrayList<>();
//        取消条件
        CtripHotelFilterEntity.DataBean.OpeartionsBean.CancelPolicyInfosBean mCancelPolicyInfos = opeartions.CancelPolicyInfos;
        FiltrateListBean.ListItemData itemData = new FiltrateListBean().new ListItemData();
        itemData.setCheck(mCancelPolicyInfos.ischecked);
        itemData.setTitle(mCancelPolicyInfos.Name);
        itemData.setKey("CancelPolicyInfos");
        FiltrateListBean.listData.add(itemData);
        TextView cancelTextView = setTextView(mCancelPolicyInfos.Name);
        mCancelPolicyInfos.textView = cancelTextView.getText().toString();
        llFilterLabelFilter.addView(cancelTextView);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelPolicyInfos.ischecked == false) {
                    mCancelPolicyInfos.ischecked = true;
                    CancelPolicyInfos = "0";
                } else {
                    mCancelPolicyInfos.ischecked = false;
                    CancelPolicyInfos = "-1";
                }
                resetTextView(mCancelPolicyInfos.ischecked, cancelTextView, true);
            }
        });

//        是否立即确认
        CtripHotelFilterEntity.DataBean.OpeartionsBean.IsInstantConfirmBean isInstantConfirm = opeartions.IsInstantConfirm;
        FiltrateListBean.ListItemData itemData1 = new FiltrateListBean().new ListItemData();
        itemData1.setCheck(isInstantConfirm.ischecked);
        itemData1.setTitle(isInstantConfirm.Name);
        itemData1.setKey("IsInstantConfirm");
        FiltrateListBean.listData.add(itemData1);
        TextView confirmTextView = setTextView(isInstantConfirm.Name);
        isInstantConfirm.textView = confirmTextView.getText().toString();
        llFilterLabelFilter.addView(confirmTextView);
        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstantConfirm.ischecked == true) {
                    isInstantConfirm.ischecked = false;
                    IsInstantConfirm = "0";
                } else {
                    isInstantConfirm.ischecked = true;
                    IsInstantConfirm = "1";
                }
                resetTextView(isInstantConfirm.ischecked, confirmTextView, true);
            }
        });
//        早餐数量
        CtripHotelFilterEntity.DataBean.OpeartionsBean.NumberOfBreakfastBean numberOfBreakfast = opeartions.NumberOfBreakfast;
        FiltrateListBean.ListItemData itemData2 = new FiltrateListBean().new ListItemData();
        itemData2.setCheck(numberOfBreakfast.ischecked);
        itemData2.setTitle(numberOfBreakfast.Name);
        itemData2.setKey("NumberOfBreakfast");
        FiltrateListBean.listData.add(itemData2);
        TextView breakfastTextView = setTextView(numberOfBreakfast.Name);
        numberOfBreakfast.textView = breakfastTextView.getText().toString();
        llFilterLabelFilter.addView(breakfastTextView);
        breakfastTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfBreakfast.ischecked == true) {
                    numberOfBreakfast.ischecked = false;
                    NumberOfBreakfast = "0";
                } else {
                    numberOfBreakfast.ischecked = true;
                    if (!StringUtils.isEmpty(numberOfBreakfast.Name)) {
                        if (numberOfBreakfast.Name.equals("含早餐")) {
                            NumberOfBreakfast = "-1";
                        } else if (numberOfBreakfast.Name.equals("单餐")) {
                            NumberOfBreakfast = "1";
                        } else if (numberOfBreakfast.Name.equals("双餐")) {
                            NumberOfBreakfast = "2";
                        }
                    }
                }
                resetTextView(numberOfBreakfast.ischecked, breakfastTextView, true);
            }
        });

//        床型
        List<CtripHotelFilterEntity.DataBean.OpeartionsBean.RoomBedInfosBean> roomBedInfos = opeartions.RoomBedInfos;
        CtripHotelFilterEntity.DataBean.OpeartionsBean.RoomBedInfosBean roomBedInfosBean1 = roomBedInfos.get(0);
        FiltrateListBean.ListItemData itemData3 = new FiltrateListBean().new ListItemData();
        itemData3.setCheck(roomBedInfosBean1.ischecked);
        itemData3.setTitle(roomBedInfosBean1.Name);
        itemData3.setKey("RoomBedInfos");
        FiltrateListBean.listData.add(itemData3);
        TextView roomBedInfoTextView1 = setTextView(roomBedInfosBean1.Name);
        roomBedInfosBean1.textView = roomBedInfoTextView1.getText().toString();
        llFilterLabelFilter.addView(roomBedInfoTextView1);
        roomBedInfoTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomBedInfosBean1.ischecked == true) {
                    roomBedInfosBean1.ischecked = false;
                    RoomBedInfos = "";
                } else {
                    roomBedInfosBean1.ischecked = true;
                    RoomBedInfos = roomBedInfos.get(0).ID + "";
                }
                resetTextView(roomBedInfosBean1.ischecked, roomBedInfoTextView1, true);
            }
        });
        CtripHotelFilterEntity.DataBean.OpeartionsBean.RoomBedInfosBean roomBedInfosBean2 = roomBedInfos.get(1);
        FiltrateListBean.ListItemData itemData4 = new FiltrateListBean().new ListItemData();
        itemData4.setCheck(roomBedInfosBean2.ischecked);
        itemData4.setTitle(roomBedInfosBean2.Name);
        itemData4.setKey("RoomBedInfos");
        FiltrateListBean.listData.add(itemData4);
        TextView roomBedInfoTextView2 = setTextView(roomBedInfosBean2.Name);
        roomBedInfosBean2.textView = roomBedInfoTextView2.getText().toString();
        llFilterLabelFilter.addView(roomBedInfoTextView2);
        roomBedInfoTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomBedInfosBean2.ischecked == true) {
                    roomBedInfosBean2.ischecked = false;
                    RoomBedInfos = "";
                } else {
                    roomBedInfosBean2.ischecked = true;
                    RoomBedInfos = roomBedInfos.get(1).ID + "";
                }
                resetTextView(roomBedInfosBean2.ischecked, roomBedInfoTextView2, true);
            }
        });
//      综合筛选
        TextView filtrateText = setTextView("筛选");
        filtrateText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.arrow_down_filtrate), null);
        llFilterLabelFilter.addView(filtrateText);
        filtrateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownPop(llTitle);
            }
        });

        dataBeans = new ArrayList<>();
        FiltrateDataBean filtrateDataBean = new FiltrateDataBean();
        filtrateDataBean.filtrateTitle = "床型";
        filtrateDataBean.dataBeanContents = new ArrayList<>();
        for (int i = 0; i < dataBean.RoomBedInfos.size(); i++) {
            FiltrateDataBean.FiltrateDataBeanContent filtrateDataBeanContent = new FiltrateDataBean.FiltrateDataBeanContent();
            filtrateDataBeanContent.id = dataBean.RoomBedInfos.get(i).ID + "";
            filtrateDataBeanContent.name = dataBean.RoomBedInfos.get(i).Name;
            filtrateDataBeanContent.key = dataBean.RoomBedInfos.get(i).key;
            filtrateDataBean.dataBeanContents.add(filtrateDataBeanContent);
        }
        dataBeans.add(filtrateDataBean);

        FiltrateDataBean filtrateDataBean1 = new FiltrateDataBean();
        filtrateDataBean1.filtrateTitle = "早餐";
        filtrateDataBean1.dataBeanContents = new ArrayList<>();
        for (int i = 0; i < dataBean.NumberOfBreakfast.size(); i++) {
            FiltrateDataBean.FiltrateDataBeanContent filtrateDataBeanContent = new FiltrateDataBean.FiltrateDataBeanContent();
            filtrateDataBeanContent.id = dataBean.NumberOfBreakfast.get(i).NumberOfBreakfast + "";
            filtrateDataBeanContent.name = dataBean.NumberOfBreakfast.get(i).Name;
            filtrateDataBeanContent.key = dataBean.NumberOfBreakfast.get(i).key;
            filtrateDataBean1.dataBeanContents.add(filtrateDataBeanContent);
        }
        dataBeans.add(filtrateDataBean1);

        FiltrateDataBean filtrateBroadnetDataBean2 = new FiltrateDataBean();
        filtrateBroadnetDataBean2.filtrateTitle = "宽带";
        filtrateBroadnetDataBean2.dataBeanContents = new ArrayList<>();
        for (int i = 0; i < dataBean.Broadnet.size(); i++) {
            FiltrateDataBean.FiltrateDataBeanContent filtrateDataBeanContent = new FiltrateDataBean.FiltrateDataBeanContent();
            filtrateDataBeanContent.id = dataBean.Broadnet.get(i).WirelessBroadnet + "";
            filtrateDataBeanContent.name = dataBean.Broadnet.get(i).Name;
            if (dataBean.Broadnet.get(i).WiredBroadnet != null) {
                filtrateDataBeanContent.key = "WiredBroadnet";
            }
            if (dataBean.Broadnet.get(i).WirelessBroadnet != null) {
                filtrateDataBeanContent.key = "WirelessBroadnet";
            }
            filtrateBroadnetDataBean2.dataBeanContents.add(filtrateDataBeanContent);
        }
        dataBeans.add(filtrateBroadnetDataBean2);

    }

    /**
     * 筛选popuWindow 底部 价格筛选
     *
     * @param pSortBean
     */
    private void initFiltratePriceView(CtripHotelFilterEntity.DataBean.PSortBean pSortBean) {
//        filtratePriceView = View.inflate(mContext, R.layout.layout_filtrate_price, null);
        filtratePriceView = LayoutInflater.from(this).inflate(R.layout.layout_filtrate_price, null,
                false);
        TextView price_range = filtratePriceView.findViewById(R.id.price_range);
        pSortBeanList = new ArrayList<>();
        rangeSeekBar = filtratePriceView.findViewById(R.id.rsb);
        GridView gv_filtrate_price = filtratePriceView.findViewById(R.id.gv_filtrate_price);
        rangeSeekBar.setTypeface(Typeface.DEFAULT_BOLD);
        rangeSeekBar.setIndicatorTextDecimalFormat("0");
        rangeSeekBar.setSeekBarMode(2);
        int indexOf = pSortBean.price.indexOf(",");
        min_range = Integer.parseInt(pSortBean.price.substring(0, indexOf));
        max_range = Integer.parseInt(pSortBean.price.substring(indexOf + 1, pSortBean.price.length()));
        static_max_range = max_range;
        price_range.setText("¥" + min_range + "-" + max_range);
        rangeSeekBar.setRange(min_range, max_range, range_reserve, range_interval);
        rangeSeekBar.setValue(0, static_max_range);
        if (pSortBean.sorts != null && pSortBean.sorts.size() > 0) {
            for (int i = 0; i < pSortBean.sorts.size(); i++) {
                SearchFilterBean.PSortBean.SortsBeanX bean = new SearchFilterBean.PSortBean.SortsBeanX();
                bean.sorts = pSortBean.sorts.get(i).sorts;
                bean.title = pSortBean.sorts.get(i).title;
                pSortBeanList.add(bean);
            }
        }
        thirdPriceAdapter = new AdapterThirdPrice(mContext, pSortBeanList, R.layout.item_third_price);
        gv_filtrate_price.setAdapter(thirdPriceAdapter);
        gv_filtrate_price.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < pSortBeanList.size(); i++) {
                    pSortBeanList.get(i).setCheck(false);
                }
                if (pSortBeanList.get(position).isCheck() == true) {
                    pSortBeanList.get(position).setCheck(false);
                    price_range.setText("¥" + price_range_min + "-" + price_range_max);
                    rangeSeekBar.setValue(0, static_max_range);
                } else {
                    pSortBeanList.get(position).setCheck(true);
                    int indexOf = pSortBeanList.get(position).getSorts().indexOf(",");
                    min_range = Integer.parseInt(pSortBeanList.get(position).getSorts().substring(0, indexOf));
                    max_range = Integer.parseInt(pSortBeanList.get(position).getSorts().substring(indexOf + 1, pSortBeanList.get(position).getSorts().length()));
                    if (max_range >= static_max_range) {
                        max_range = static_max_range;
                    }
                    price_range.setText(min_range + "-" + max_range);
                    rangeSeekBar.setValue(min_range, max_range);
                }
                filtrateDataAdapter.notifyDataSetChanged();
                thirdPriceAdapter.notifyDataSetChanged();
            }
        });

    }

    TextView setTextView(String text) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mlp.rightMargin = DPXUnitUtil.dp2px(mContext, 6);
        mlp.topMargin = DPXUnitUtil.dp2px(mContext, 6);
        textView.setLayoutParams(mlp);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        textView.setBackgroundResource(R.drawable.bg_gray_radius_90);
        int i = DPXUnitUtil.dp2px(mContext, 10);
        textView.setPadding(DPXUnitUtil.dp2px(mContext, 10), DPXUnitUtil.dp2px(mContext, 8), DPXUnitUtil.dp2px(mContext, 10), DPXUnitUtil.dp2px(mContext, 8));
        return textView;
    }

    private void resetTextView(boolean isCheck, TextView textView, boolean isRequest) {
        if (isCheck == true) {
            textView.setBackgroundResource(R.drawable.bg_white_border_blue_radius_90);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.cff4289ff));
        } else {
            textView.setBackgroundResource(R.drawable.bg_gray_radius_90);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        }
        if (isRequest == true) {
            Subscription ctripHotelFilterData = ctripHotelDetailPresenter.getFiltrateData(mContext, mHotelId, mStartDate, mEndDate, RoomBedInfos, NumberOfBreakfast, WirelessBroadnet, WiredBroadnet, minPrice, maxPrice, IsInstantConfirm, CancelPolicyInfos);
            addSubscription(ctripHotelFilterData);
        }
    }

    //向下弹出
    public void showDownPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popuwindow_hotel_filtrate)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.TopToButtomAnim)
                .setOutsideTouchable(true)
                .setViewOnclickListener(this)
                .setBackGroundLevel(0.8f)
                .create();
        llTitle.setBackgroundColor(Color.WHITE);
        popupWindow.showAsDropDown(view);
        popupWindow.setOnDismissListener(new popupDismissListener());
    }

    //  刷新列表
    @Override
    public void resetFilterList(Hotel_Rooms_FiltrateBean ctripRoomTypeInfo) {
        if (ctripRoomTypeInfo.roomTypeInfos.size() > 0) {
            ctripRoomTypeInfoArrayList.clear();
            if (ctripHotelDetailData.data.roomTypeInfos.size() > 0) {
                ctripHotelDetailData.data.roomTypeInfos.clear();
            }
            ctripHotelDetailData.data.roomTypeInfos.addAll(ctripRoomTypeInfo.roomTypeInfos);
            for (CtripRoomTypeInfo roomTypeInfo : ctripHotelDetailData.data.roomTypeInfos) {
                for (CtripRoomTypeInfo.RoomInfosBean roomInfosBean : roomTypeInfo.roomInfos) {
                    roomInfosBean.policies = ctripHotelDetailData.data.policies;
                    roomInfosBean.pictures = roomTypeInfo.pictures;
                    roomInfosBean.facilities = roomTypeInfo.facilities;
                    roomTypeInfo.addSubItem(roomInfosBean);
                }
                ctripRoomTypeInfoArrayList.add(roomTypeInfo);
            }
            if (ctripRoomTypeInfoArrayList != null && ctripRoomTypeInfoArrayList.size() > 0) {
                ctripRoomAdapter.getData().clear();
                ctripRoomAdapter.addData(ctripRoomTypeInfoArrayList);
                ctripRoomAdapter.notifyDataSetChanged();
            }
        } else {
            //放置空页面
            ctripRoomAdapter.getData().clear();
            ctripRoomAdapter.setNewData(ctripRoomAdapter.getData());
            ctripRoomAdapter.setEmptyView(R.layout.view_hotel_no_data);
            ctripRoomAdapter.notifyDataSetChanged();
        }
    }

    //  填充下拉popuWindow 数据
    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popuwindow_hotel_filtrate:
                TextView tv_filtrate_switch = view.findViewById(R.id.tv_filtrate_switch);
                RecyclerView rv_filtrate_listdata = view.findViewById(R.id.rv_filtrate_listdata);
                LinearLayout ll_reset = view.findViewById(R.id.ll_reset);
                LinearLayout ll_complete = view.findViewById(R.id.ll_complete);
                RecyclerView rv_filtrate_condition = view.findViewById(R.id.rv_filtrate_condition);
//              关闭筛选popuWindow
                RxUtil.clicks(tv_filtrate_switch, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            filtrateDataAdapter.removeAllFooterView();
                            popupWindow.dismiss();
                        }
                    }
                });
                rv_filtrate_condition.setFocusable(false);
                rv_filtrate_condition.setNestedScrollingEnabled(false);
                rv_filtrate_condition.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//                rv_filtrate_condition.addItemDecoration(new MarginDecoration(mContext));
                condition_adapter = new AdapterDetailHotelFiltrateCondition(R.layout.item_detail_filtrate_condition);
                condition_adapter.bindToRecyclerView(rv_filtrate_condition);
                rv_filtrate_condition.setAdapter(condition_adapter);
                condition_adapter.setNewData(FiltrateListBean.listData);
                condition_adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        resetFiltrateListMutual(FiltrateListBean.listData, FiltrateListBean.listData.get(position).getKey(), position);
                        resetFiltrateContent(FiltrateListBean.listData.get(position).getTitle(), FiltrateListBean.listData.get(position).isCheck, filtrateListBean.listData.get(position).key);
                        condition_adapter.notifyDataSetChanged();
                        filtrateDataAdapter.notifyDataSetChanged();
                    }
                });
                rv_filtrate_listdata.setFocusable(false);
                rv_filtrate_listdata.setNestedScrollingEnabled(false);
                rv_filtrate_listdata.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                filtrateDataAdapter = new AdapterDetailHotelFiltrateData(R.layout.item_filtrate_data);
                filtrateDataAdapter.setRef(this);
                filtrateDataAdapter.addFooterView(filtratePriceView);
                filtrateDataAdapter.setNewData(dataBeans);
                rv_filtrate_listdata.setAdapter(filtrateDataAdapter);
                ll_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetFiltrateData();
                    }
                });
                ll_complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeFiltrateDat();
                    }
                });
                break;
        }
    }

    /**
     * 处理筛选卡 选中 互斥逻辑
     *
     * @param listData
     * @param key
     * @param position
     */
    private void resetFiltrateListMutual(ArrayList<FiltrateListBean.ListItemData> listData, String key, int position) {
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).key.equals(key)) {
                if (i == position) {
                    if (listData.get(i).isCheck == true) {
                        listData.get(i).setCheck(false);
                    } else {
                        listData.get(i).setCheck(true);
                    }
                } else {
                    listData.get(i).setCheck(false);
                }
            }
        }
    }

    /**
     * 重置筛选内容选中状态
     *
     * @param title
     * @param isCheck
     * @param key
     */
    private void resetFiltrateContent(String title, boolean isCheck, String key) {
        for (int i = 0; i < dataBeans.size(); i++) {
            for (int j = 0; j < dataBeans.get(i).dataBeanContents.size(); j++) {
                if (dataBeans.get(i).dataBeanContents.get(j).name.equals(title)) {
                    if (isCheck == true) {
                        dataBeans.get(i).dataBeanContents.get(j).isCheck = true;
                    } else {
                        dataBeans.get(i).dataBeanContents.get(j).isCheck = false;
                    }
                } else if (!dataBeans.get(i).dataBeanContents.get(j).name.equals(title) && dataBeans.get(i).dataBeanContents.get(j).key.equals(key)) {
                    dataBeans.get(i).dataBeanContents.get(j).isCheck = false;
                }
            }
        }
    }

    // 重置筛选数据
    private void resetFiltrateData() {
//      清空筛选条件选中状态
        for (FiltrateDataBean filtrateDataBean : dataBeans) {
            if (filtrateDataBean.dataBeanContents != null && filtrateDataBean.dataBeanContents.size() > 0) {
                for (int i = 0; i < filtrateDataBean.dataBeanContents.size(); i++) {
                    filtrateDataBean.dataBeanContents.get(i).isCheck = false;
                }
            }
        }
//      清空筛选数据状态
        if (FiltrateListBean.listData != null && FiltrateListBean.listData.size() > 0) {
            for (int i = 0; i < FiltrateListBean.listData.size(); i++) {
                FiltrateListBean.listData.get(i).isCheck = false;
            }
        }
//      清空筛选价格选中状态
        if (pSortBeanList != null && pSortBeanList.size() > 0) {
            for (int i = 0; i < pSortBeanList.size(); i++) {
                pSortBeanList.get(i).isCheck = false;
            }
        }
//        rangeSeekBar  恢复默认值
        rangeSeekBar.setValue(0, static_max_range);
        if (thirdPriceAdapter != null) {
            thirdPriceAdapter.notifyDataSetChanged();
        }
        if (condition_adapter != null) {
            condition_adapter.notifyDataSetChanged();
        }
        if (filtrateDataAdapter != null) {
            filtrateDataAdapter.notifyDataSetChanged();
        }
    }


/*
* http://test.i1170.com/app/xiecheng.php?c=xc_hotel_rooms&HotelID=346401&Start=2018-10-29&End=2018-10-30&RoomBedInfos=1&NumberOfBreakfast=1&WirelessBroadnet=2&WiredBroadnet=2&minPrice=0&maxPrice=10000&IsInstantConfirm=0&CancelPolicyInfos=-1
* */

    //  完成筛选
    private void completeFiltrateDat() {
        if (dataBeans != null && dataBeans.size() > 0) {
            for (int i = 0; i < dataBeans.size(); i++) {
                //         床型
                if (dataBeans.get(0) != null && dataBeans.get(0).dataBeanContents.size() > 0) {
                    for (int j = 0; j < dataBeans.get(0).dataBeanContents.size(); j++) {
                        if (dataBeans.get(0).dataBeanContents.get(j).isCheck == true) {
                            RoomBedInfos = dataBean.RoomBedInfos.get(i).ID + "";
                        }
                    }
                }
                //          早餐
                if (dataBeans.get(1) != null && dataBeans.get(1).dataBeanContents.size() > 0) {
                    for (int j = 0; j < dataBeans.get(1).dataBeanContents.size(); j++) {
                        if (dataBeans.get(1).dataBeanContents.get(j).isCheck == true) {
                            NumberOfBreakfast = dataBean.NumberOfBreakfast.get(j).NumberOfBreakfast + "";
                        }
                    }
                }
                //         宽带
                if (dataBeans.get(2) != null && dataBeans.get(2).dataBeanContents.size() > 0) {
                    for (int j = 0; j < dataBeans.get(2).dataBeanContents.size(); j++) {
                        if (dataBeans.get(2).dataBeanContents.get(j).isCheck == true) {
                            if (!StringUtils.isEmpty(dataBeans.get(2).dataBeanContents.get(j).key)) {
                                // 免费无线
                                if (dataBeans.get(2).dataBeanContents.get(j).key.equals("WirelessBroadnet")) {
                                    WirelessBroadnet = "2";
                                    WiredBroadnet = "0";
                                }
                                //免费有线
                                if (dataBeans.get(2).dataBeanContents.get(j).key.equals("WiredBroadnet")) {
                                    WirelessBroadnet = "0";
                                    WiredBroadnet = "2";
                                }
                            }
                        }
                    }
                }
            }
        }
//      最大 最小 价格
        if (pSortBeanList != null && pSortBeanList.size() > 0) {
            for (int i = 0; i < pSortBeanList.size(); i++) {
                if (pSortBeanList.get(i).isCheck == true) {
                    String price = pSortBeanList.get(i).getSorts();
                    int indexOf = price.indexOf(",");
                    minPrice = price.substring(0, indexOf);
                    maxPrice = price.substring(indexOf + 1, price.length());
                }
            }
        }

//      立即确认  免费取消
        if (FiltrateListBean.listData != null && FiltrateListBean.listData.size() > 0) {
            for (int i = 0; i < FiltrateListBean.listData.size(); i++) {
                if (FiltrateListBean.listData.get(i).key.equals("IsInstantConfirm")) {
                    if (FiltrateListBean.listData.get(i).isCheck == true) {
                        IsInstantConfirm = "1";
                    } else {
                        IsInstantConfirm = "0";
                    }
                }
                if (FiltrateListBean.listData.get(i).key.equals("CancelPolicyInfos")) {
                    if (FiltrateListBean.listData.get(i).key.equals("CancelPolicyInfos")) {
                        if (FiltrateListBean.listData.get(i).isCheck == true) {
                            CancelPolicyInfos = "0";
                        } else {
                            CancelPolicyInfos = "-1";
                        }
                    }
                }

            }
        }
        Subscription ctripHotelFilterData = ctripHotelDetailPresenter.getFiltrateData(mContext, mHotelId, mStartDate, mEndDate, RoomBedInfos, NumberOfBreakfast, WirelessBroadnet, WiredBroadnet, minPrice, maxPrice, IsInstantConfirm, CancelPolicyInfos);
        addSubscription(ctripHotelFilterData);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 刷新筛选条件选中状态
     *
     * @param title
     * @param key
     */
    @Override
    public void Onclick(String title, String key) {
        if (FiltrateListBean.listData != null && FiltrateListBean.listData.size() > 0) {
            for (int i = 0; i < FiltrateListBean.listData.size(); i++) {
                if (FiltrateListBean.listData.get(i).getTitle().equals(title)) {
                    FiltrateListBean.listData.get(i).isCheck = true;
                } else if (!FiltrateListBean.listData.get(i).getTitle().equals(title) && FiltrateListBean.listData.get(i).key.equals(key)) {
                    FiltrateListBean.listData.get(i).isCheck = false;
                }
            }
        }
       /* for (int i = 0; i < item.getDataBeanContents().size(); i++) {
            item.getDataBeanContents().get(i).isCheck = false;
        }
        if (item.getDataBeanContents().get(position).isCheck == true) {
            item.getDataBeanContents().get(position).isCheck = false;
        } else {
            item.getDataBeanContents().get(position).isCheck = true;
        }
        adapter.notifyDataSetChanged();*/
        condition_adapter.notifyDataSetChanged();
    }

    class popupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            Logger.i("我是关闭事件");
            filtrateDataAdapter.removeAllFooterView();
        }
    }
}
