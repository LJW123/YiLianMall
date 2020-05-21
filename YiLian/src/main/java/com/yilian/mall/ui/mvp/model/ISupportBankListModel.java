package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.entity.SupportBankListEntity;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/21.
 */

public interface ISupportBankListModel extends IBaseModel {
    Subscription getSupportBankList(Context context, int type, Observer<SupportBankListEntity> observer);


}
