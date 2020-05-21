package com.yilian.mall.ui.mvp.model.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.inter.IPayTypeModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;

/**
 * @author xiaofo on 2018/7/8.
 */

public class PayTypeModelImpl implements IPayTypeModel {
    @Override
    public Observable getPayType(Context context) {
        return RetrofitUtils3.getRetrofitService(context)
                .getPayTypeList("recharge/paytype_v3");
    }
}
