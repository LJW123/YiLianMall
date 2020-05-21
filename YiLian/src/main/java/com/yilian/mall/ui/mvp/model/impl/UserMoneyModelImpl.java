package com.yilian.mall.ui.mvp.model.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.inter.IUserMoneyModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;

/**
 * @author xiaofo on 2018/7/8.
 */

public class UserMoneyModelImpl implements IUserMoneyModel {
    @SuppressWarnings("unchecked")
    @Override
    public Observable getUserMoney(Context context) {
        return RetrofitUtils3.getRetrofitService(context)
                .getMoneyData("get_integral_balance");
    }

}
