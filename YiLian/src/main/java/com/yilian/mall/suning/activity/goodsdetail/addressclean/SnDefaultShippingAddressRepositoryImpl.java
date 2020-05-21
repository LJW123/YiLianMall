package com.yilian.mall.suning.activity.goodsdetail.addressclean;

import android.content.Context;

import com.yilian.mall.clean.BaseRepository;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;

/**
 * @author xiaofo on 2018/7/18.
 */

public class SnDefaultShippingAddressRepositoryImpl extends BaseRepository implements ISnDefaultShippingAddressInfoRepository {
    public SnDefaultShippingAddressRepositoryImpl(Context context) {
        super(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<SnShippingAddressInfoEntity> getSnDefaultShippingAddress() {
        return RetrofitUtils3.getRetrofitService(mContext)
                .getSnShippingAddress("suning_address/suning_getdefault_address")
                ;
    }
}
