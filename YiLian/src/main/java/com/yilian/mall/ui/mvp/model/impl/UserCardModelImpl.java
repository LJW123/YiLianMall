package com.yilian.mall.ui.mvp.model.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.inter.IUserCardModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;

/**
 * @author Zg on 2018/11/18.
 */

public class UserCardModelImpl implements IUserCardModel {

    @SuppressWarnings("unchecked")
    @Override
    public Observable getUserCard(Context context) {
        return RetrofitUtils3.getRetrofitService(context)
                .getCardBalance("jd_pay/get_user_card");
    }

}
