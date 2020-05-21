package com.yilian.mall.ctrip.fragment.hoteldetail;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.activity.CtripHotelDetailActivity;
import com.yilian.mall.ctrip.adapter.CtripHomeDetailDialogFacilityAdapter;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripRoomTypeInfo;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author xiaofo on 2018/9/22.
 */

public class CtripHomeDetailDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    public static final String TAG = "CtripHomeDetailDialogFragment";
    public static final String TAG_POSITION = "CtripHomeDetailDialogFragment_POSITION";
    private int mPosition;
    private Banner banner;
    private TextView tvArea;
    private TextView tvPeopleNum;
    private LinearLayout llAreaAndPeopleCount;
    private TextView tvFloorNum;
    private TextView tvBedWidth;
    private TextView tvWifi;
    private View viewLine;
    private RecyclerView recyclerViewFacilities;
    private TextView tvRoomFacilities;
    private LinearLayout llHomeFacilities;
    private View viewHeight10;
    private TextView tvBookRoomNoteTitle;
    private View viewBottom;
    private TextView tvPriceOneNight;
    private TextView tvReturnBean;
    private Button btnBook;
    private CtripRoomTypeInfo.RoomInfosBean mRoomInfosBean;
    private TextView tvName;
    private TextView tvBookNote;

    //服务设施
    private LinearLayout llServiceFacility;
    private ImageView ivServiceFacilityBack;
    private RecyclerView recyclerview1, recyclerview2, recyclerview3, recyclerview4, recyclerview5;
    private CtripHomeDetailDialogFacilityAdapter adapter1, adapter2, adapter3, adapter4, adapter5;
    private List<CtripRoomTypeInfo.FacilitiesBean.facilityItemBean> list1, list2, list3, list4, list5;

    public static CtripHomeDetailDialogFragment getInstance(CtripRoomTypeInfo.RoomInfosBean roomInfosBean, int position) {
        CtripHomeDetailDialogFragment ctripHomeDetailDialogFragment = new CtripHomeDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, roomInfosBean);
        args.putInt(TAG_POSITION, position);
        ctripHomeDetailDialogFragment.setArguments(args);
        return ctripHomeDetailDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.layout_ctrip_home_detail_dialog_fragment, container);
        initView(inflate);
        initData();
        initListener();
        return inflate;
    }

    private void initView(View inflate) {
        tvBookNote = inflate.findViewById(R.id.tv_book_note);
        tvName = inflate.findViewById(R.id.tv_room_name);
        banner = (Banner) inflate.findViewById(R.id.banner);
        tvArea = (TextView) inflate.findViewById(R.id.tv_area);
        tvPeopleNum = (TextView) inflate.findViewById(R.id.tv_people_num);
        llAreaAndPeopleCount = (LinearLayout) inflate.findViewById(R.id.ll_area_and_people_count);
        tvFloorNum = (TextView) inflate.findViewById(R.id.tv_floor_num);
        tvBedWidth = (TextView) inflate.findViewById(R.id.tv_bed_width);
        tvWifi = (TextView) inflate.findViewById(R.id.tv_wifi);
        viewLine = (View) inflate.findViewById(R.id.view_line);
        recyclerViewFacilities = (RecyclerView) inflate.findViewById(R.id.recycler_view_facilities);
        tvRoomFacilities = (TextView) inflate.findViewById(R.id.tv_room_facilities);
        llHomeFacilities = (LinearLayout) inflate.findViewById(R.id.ll_home_facilities);
        viewHeight10 = (View) inflate.findViewById(R.id.view_height_10);
        tvBookRoomNoteTitle = (TextView) inflate.findViewById(R.id.tv_book_room_note_title);
        viewBottom = (View) inflate.findViewById(R.id.view_bottom);
        tvPriceOneNight = (TextView) inflate.findViewById(R.id.tv_price_one_night);
        tvReturnBean = (TextView) inflate.findViewById(R.id.tv_return_bean);
        btnBook = (Button) inflate.findViewById(R.id.btn_book);
        //服务设施
        llServiceFacility = inflate.findViewById(R.id.ll_service_facility);
        llServiceFacility.setVisibility(View.GONE);
        ivServiceFacilityBack = inflate.findViewById(R.id.iv_service_facility_back);
        //便利设施
        recyclerview1 = inflate.findViewById(R.id.recyclerview1);
        recyclerview1.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter1 = new CtripHomeDetailDialogFacilityAdapter();
        adapter1.bindToRecyclerView(recyclerview1);
        //浴室
        recyclerview2 = inflate.findViewById(R.id.recyclerview2);
        recyclerview2.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter2 = new CtripHomeDetailDialogFacilityAdapter();
        adapter2.bindToRecyclerView(recyclerview2);
        //媒体科技
        recyclerview3 = inflate.findViewById(R.id.recyclerview3);
        recyclerview3.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter3 = new CtripHomeDetailDialogFacilityAdapter();
        adapter3.bindToRecyclerView(recyclerview3);
        //食品饮品
        recyclerview4 = inflate.findViewById(R.id.recyclerview4);
        recyclerview4.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter4 = new CtripHomeDetailDialogFacilityAdapter();
        adapter4.bindToRecyclerView(recyclerview4);
        //其他
        recyclerview5 = inflate.findViewById(R.id.recyclerview5);
        recyclerview5.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter5 = new CtripHomeDetailDialogFacilityAdapter();
        adapter5.bindToRecyclerView(recyclerview5);
    }

    private void initData() {
        mRoomInfosBean = (CtripRoomTypeInfo.RoomInfosBean) getArguments().getSerializable(TAG);
        mPosition = getArguments().getInt(TAG_POSITION);
        ArrayList<String> pics = new ArrayList<>();
        for (CtripRoomTypeInfo.PicturesBean picturesBean : mRoomInfosBean.pictures) {
            pics.add(picturesBean.url);
        }
        tvName.setText(mRoomInfosBean.roomName);
        banner.setImages(pics)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new BannerViewGlideUtil())
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        tvArea.setText(getText("面积 ", mRoomInfosBean.areaRange));
        tvPeopleNum.setText(getText("可住 ", mRoomInfosBean.maxOccupancy + "人"));
        tvFloorNum.setText(getText("楼层 ", mRoomInfosBean.floorRange));
        if (mRoomInfosBean.roomBedInfos.size() > 0) {
            CtripRoomTypeInfo.RoomInfosBean.RoomBedInfosBeanX roomBedInfosBeanX = mRoomInfosBean.roomBedInfos.get(0);
            tvBedWidth.setText(getText("床宽 ", roomBedInfosBeanX.name + roomBedInfosBeanX.bedInfo.get(0).bedWidth));
        }
        StringBuilder networkStr = new StringBuilder();
        if (mRoomInfosBean.wiredBroadnet == 0) {
            networkStr.append("无Wi-Fi");
        } else if (mRoomInfosBean.wiredBroadnet == 1 || mRoomInfosBean.wiredBroadnet == 2) {
            networkStr.append("全部房间Wi-Fi");
        } else {
            networkStr.append("部分房间Wi-Fi");
        }
        if (mRoomInfosBean.wiredBroadnet == 0) {
            networkStr.append("、无宽带");
        } else {
            networkStr.append("、有线宽带");

        }
        tvWifi.setText(getText("宽带 ", networkStr.toString()));

//        StringBuilder hotelPolicies = new StringBuilder();
//        for (CtripHotelDetailEntity.DataBean.PoliciesBean policy : mRoomInfosBean.policies) {
//            hotelPolicies.append(policy.text).append("<br/>");
//        }
        StringBuilder hotelPolicies = new StringBuilder();
        for (int i = 0; i < mRoomInfosBean.policies.size(); i++) {
//            入住政策只取前四条不为空的字段
            if (i == 4) {
                break;
            }
            CtripHotelDetailEntity.DataBean.PoliciesBean policiesBean = mRoomInfosBean.policies.get(i);
            if (!TextUtils.isEmpty(policiesBean.text)) {
                hotelPolicies.append(policiesBean.text).append("<br/>");
            }
        }

//      订房必读
        tvBookNote.setText(Html.fromHtml(hotelPolicies.toString()));
        tvPriceOneNight.setText(Html.fromHtml("<font><small><small>¥</small></small>" + mRoomInfosBean.priceInfo.amount));
        tvReturnBean.setText("+" + mRoomInfosBean.priceInfo.dailyExclusiveAmount);
        recyclerViewFacilities.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewFacilities.setAdapter(new MyAdapter(mRoomInfosBean.facilities));

        //服务设施
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        list5 = new ArrayList<>();
        for (int i = 0; i < mRoomInfosBean.facilities.size(); i++) {
            String categoryName = mRoomInfosBean.facilities.get(i).categoryName;
            for (CtripRoomTypeInfo.FacilitiesBean.facilityItemBean itemBean : mRoomInfosBean.facilities.get(i).facilityItem) {
                if (categoryName.equals("便利设施")) {
                    list1.add(itemBean);
                } else if (categoryName.equals("浴室")) {
                    list2.add(itemBean);
                } else if (categoryName.equals("媒体科技")) {
                    list3.add(itemBean);
                } else if (categoryName.equals("食品饮品")) {
                    list4.add(itemBean);
                } else {
                    list5.add(itemBean);
                }
            }
        }
        adapter1.setNewData(list1);
        adapter2.setNewData(list2);
        adapter3.setNewData(list3);
        adapter4.setNewData(list4);
        adapter5.setNewData(list5);
    }

    private void initListener() {
        tvRoomFacilities.setOnClickListener(this);
        ivServiceFacilityBack.setOnClickListener(this);
        btnBook.setOnClickListener(this);
        RxUtil.clicks(tvName, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                dismiss();
            }
        });
    }

    Spanned getText(String before, String end) {
        return Html.fromHtml(before + "<font color='#333333'>" + end + "</font>");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_book:
                ((CtripHotelDetailActivity) getActivity()).bookRoom(mPosition);
                dismiss();
                break;
            case R.id.tv_room_facilities:
                llServiceFacility.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_service_facility_back:
                llServiceFacility.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        底部显示
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomToTopAnim);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = (int) (ScreenUtils.getScreenHeight(getContext()) / 1.2);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private final ArrayList<CtripRoomTypeInfo.FacilitiesBean> facilitiesBeans;

        public MyAdapter(ArrayList<CtripRoomTypeInfo.FacilitiesBean> facilitiesBeans) {
            this.facilitiesBeans = facilitiesBeans;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(new TextView(getContext()));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            CtripRoomTypeInfo.FacilitiesBean facilitiesBean = facilitiesBeans.get(position);
            holder.textView.setText(facilitiesBean.facilityItem.get(0).name);
            holder.textView.setCompoundDrawablePadding(DPXUnitUtil.dp2px(getContext(),5));
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.mipmap.ctrip_icon_f), null, null, null);
        }

        @Override
        public int getItemCount() {
            if (facilitiesBeans.size()>4) {
                return 4;
            }
            return facilitiesBeans.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }

    }
}
