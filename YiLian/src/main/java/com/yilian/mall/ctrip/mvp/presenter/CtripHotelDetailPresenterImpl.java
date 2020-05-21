package com.yilian.mall.ctrip.mvp.presenter;

import android.content.Context;
import android.widget.TextView;

import com.yilian.mall.ctrip.model.CtripHotelDetailMode;
import com.yilian.mall.ctrip.mvp.CtripHotelDetailContract;
import com.yilian.networkingmodule.entity.Hotel_Rooms_FiltrateBean;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelDetailEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripHotelFilterEntity;

import java.net.SocketTimeoutException;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author xiaofo on 2018/8/31.
 */

public class CtripHotelDetailPresenterImpl implements CtripHotelDetailContract.Presenter {
    private final CtripHotelDetailContract.View mView;
    private TextView textView;

    public CtripHotelDetailPresenterImpl(CtripHotelDetailContract.View view) {
        mView = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getCtripHotelDetailData(Context context, String hotelId, String startDate, String endDate) {
        mView.startMyDialog(false);
        CtripHotelDetailMode ctripHotelDetailMode = new CtripHotelDetailMode();
        return ctripHotelDetailMode
                .getCtritHotelDetai(context, hotelId, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHotelDetailEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            mView.showToast("网络连接超时,请重试");
                        } else {
                            mView.showToast(e.getMessage());
                        }
                        mView.stopMyDialog();
                    }

                    @Override
                    public void onNext(CtripHotelDetailEntity ctripHotelDetailEntity) {
                        mView.showCtripHotelDetail(ctripHotelDetailEntity);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getCtripHotelFilter(Context context) {
        CtripHotelDetailMode ctripHotelDetailMode = new CtripHotelDetailMode();
        return ctripHotelDetailMode
                .getCtripHotelFilter(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripHotelFilterEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            mView.showToast("网络连接超时,请重试");
                        } else {
                            mView.showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(CtripHotelFilterEntity ctripHotelFilter) {
                        if (ctripHotelFilter.code == 1) {
                            mView.showCtripFilter(ctripHotelFilter);
                        }
                    }
                });
    }

    @Override
    public Subscription getFiltrateData(Context context, String HotelID, String Start, String End, String RoomBedInfos, String NumberOfBreakfast, String WirelessBroadnet, String WiredBroadnet, String minPrice, String maxPrice, String IsInstantConfirm, String CancelPolicyInfos) {
        CtripHotelDetailMode ctripHotelDetailMode = new CtripHotelDetailMode();
        return ctripHotelDetailMode
                .getHotelFilterData(context, HotelID, Start, End, RoomBedInfos, NumberOfBreakfast, WirelessBroadnet, WiredBroadnet, minPrice, maxPrice, IsInstantConfirm, CancelPolicyInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Hotel_Rooms_FiltrateBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException) {
                            mView.showToast("网络连接超时,请重试");
                        } else {
                            mView.showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Hotel_Rooms_FiltrateBean ctripRoomTypeInfo) {
                        if (ctripRoomTypeInfo.code == 1) {
                            mView.resetFilterList(ctripRoomTypeInfo);
                        }
                    }
                });
    }
}
