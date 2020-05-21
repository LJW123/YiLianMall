package com.yilian.mall.ui.transfer.inter;

import android.content.Context;

import com.yilian.mall.ui.transfer.Donee;
import com.yilian.mall.ui.transfer.impl.TransferDaiGouQUanCategoryImpl;
import com.yilian.networkingmodule.entity.MemberGiftEntity1;

import java.io.Serializable;
import java.util.ArrayList;

import rx.Observable;


/**
 * @author xiaofo on 2018/10/10.
 * 转赠账号
 */

public interface ITransferAccount extends Serializable {
    TransferDaiGouQUanCategoryImpl getTransferCategory();
    /**
     * 获取转赠历史数据
     *
     * @return
     */
    Observable<ArrayList<Donee>> getTransferAccountList(Context context);

    /**
     * 检查转增人是否合法
     *
     */
    Observable<MemberGiftEntity1> checkTransferAccount(Context context, String... strings);

    String getTransferRemind();

    String getTitle();

    public interface GetTransferAccountListener {
        void getTransferAccountSuccess(ArrayList<Donee> transferAccountBeans);
    }

}
