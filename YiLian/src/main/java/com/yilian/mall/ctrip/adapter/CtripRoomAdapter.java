package com.yilian.mall.ctrip.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.NumberUtil;
import com.yilian.networkingmodule.entity.ctrip.CtripRoomTypeInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xiaofo on 2018/9/3.
 */

public class CtripRoomAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CtripRoomAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(CtripRoomTypeInfo.TYPE_HEADER, R.layout.item_ctrip_hotel_detail_home_0);
        addItemType(CtripRoomTypeInfo.TYPE_ROOM, R.layout.item_ctrip_hotel_detail_home_1);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case CtripRoomTypeInfo.TYPE_HEADER:
                CtripRoomTypeInfo ctripRoomTypeInfo = (CtripRoomTypeInfo) item;

                //起价
                helper.setText(R.id.tv_unit, "¥");
                helper.setText(R.id.tv_price, String.format("%s", ctripRoomTypeInfo.minPrice));
                helper.setText(R.id.tv_min_price, "起");
                if (ctripRoomTypeInfo.pictures != null && ctripRoomTypeInfo.pictures.size() > 0) {
                    GlideUtil.showImage(mContext, ctripRoomTypeInfo.pictures.get(0).url, helper.getView(R.id.iv_room_pic));
                } else {
                    ((ImageView) (helper.getView(R.id.iv_room_pic))).setImageResource(R.mipmap.zhanwei);
                }
                helper.setText(R.id.tv_room_standard, ctripRoomTypeInfo.roomTypeName);
                helper.setText(R.id.tv_room_size,
                        String.format(
                                "%sm²%s", ctripRoomTypeInfo.areaRange, ctripRoomTypeInfo.typeBedName));
//helper.setText(R.id.tv_evaluate,ctripRoomTypeInfo.)

                helper.getView(R.id.ll_expand).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ctripRoomTypeInfo.isExpanded()) {
                            collapse(helper.getAdapterPosition());
                        } else {
                            expand(helper.getAdapterPosition());
                        }
                    }
                });
                if (ctripRoomTypeInfo.isExpanded()) {
                    ((TextView) (helper.getView(R.id.tv_min_price))).setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(mContext, R.mipmap.icon_ctrip_room_collapse), null
                    );
                } else {
                    ((TextView) (helper.getView(R.id.tv_min_price))).setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(mContext, R.mipmap.icon_ctrip_room_expand), null
                    );
                }


                List<CtripRoomTypeInfo.RoomInfosBean> subItems = ctripRoomTypeInfo.getSubItems();

                int remainingNumber = 0;
//                判断是否订完
                if (subItems != null && subItems.size() > 0) {
                    for (CtripRoomTypeInfo.RoomInfosBean subItem : subItems) {
                        helper.setText(R.id.tv_predict, String.format("预计%s", subItem.priceInfo.dailyReturnBean));
                        if (!NumberUtil.isNumber(subItem.priceInfo.remainingRooms)) {
                            helper.getView(R.id.iv_booked_up).setVisibility(View.INVISIBLE);
                            break;
                        } else {
                            remainingNumber += NumberFormat.convertToInt(subItem.priceInfo.remainingRooms, 0);
                            if (remainingNumber <= 0) {
                                helper.getView(R.id.iv_booked_up).setVisibility(View.VISIBLE);
                            } else {
                                helper.getView(R.id.iv_booked_up).setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                break;
            case CtripRoomTypeInfo.TYPE_ROOM:
                CtripRoomTypeInfo.RoomInfosBean roomInfosBean = (CtripRoomTypeInfo.RoomInfosBean) item;
                helper.setText(R.id.tv_room_category, roomInfosBean.roomName);
                helper.addOnClickListener(R.id.tv_details);
                String breakfast = "无早餐";
                if (roomInfosBean.priceInfo.numberOfBreakfast == 1) {
                    breakfast = "单份早餐";
                } else if (roomInfosBean.priceInfo.numberOfBreakfast == 2) {
                    breakfast = "双份早餐";
                } else if (roomInfosBean.priceInfo.numberOfBreakfast > 2) {
                    breakfast = roomInfosBean.priceInfo.numberOfBreakfast + "份早餐";
                }
                helper.setText(R.id.tv_room_label, String.format("%s  %s", breakfast, roomInfosBean.bedName));
                helper.setText(R.id.tv_unit, "¥");
                if (roomInfosBean.priceInfo.cancelPolicyInfos > 0) {
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        Date cancelDate = fmt.parse(roomInfosBean.priceInfo.start);
                        Date currentDate = new Date(System.currentTimeMillis());
                        Logger.i("cancelDate：" + cancelDate + "  currentDate：" + currentDate);
                        if (cancelDate.compareTo(currentDate) > 0) {
                            helper.setTextColor(R.id.tv_whether_can_cancel, Color.parseColor("#ff2eb000"));
                            helper.setText(R.id.tv_whether_can_cancel, "入住当天" + cancelDate.getHours() + "前可以取消");
                        } else {
                            helper.setText(R.id.tv_whether_can_cancel, "不可取消");
                            helper.setTextColor(R.id.tv_whether_can_cancel, Color.parseColor("#ff999999"));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    helper.setText(R.id.tv_whether_can_cancel, "可免费取消");
                }
                if(roomInfosBean.priceInfo.remainingRooms.equals("10+")){
                    helper.setText(R.id.tv_room_num, "剩余间数"+roomInfosBean.priceInfo.remainingRooms);
                }else{
                    helper.setText(R.id.tv_room_num, "仅剩"+roomInfosBean.priceInfo.remainingRooms+"间");
                }

                if (roomInfosBean.priceInfo.isInstantConfirm) {
                    helper.setText(R.id.tv_whether_confirm_now, "不可立即确认");
                } else {
                    helper.setText(R.id.tv_whether_confirm_now, "立即确认");
                }
                helper.setText(R.id.tv_return_bean, String.format("预计%s", roomInfosBean.priceInfo.dailyReturnBean));
                helper.setText(R.id.tv_price, roomInfosBean.priceInfo.dailyExclusiveAmount);
                helper.addOnClickListener(R.id.iv_book_up);
                break;
            default:
                break;
        }
    }
}
