package com.yilian.mall.ui.transfer.inter;

import android.content.Context;

import com.yilian.mall.ui.transfer.Donee;
import com.yilian.mall.ui.transfer.bean.TransferSuccessBean;
import com.yilian.mall.ui.transfer.impl.TransferDaiGouQUanCategoryImpl;

import java.io.Serializable;

import rx.Observable;

/**
 * @author xiaofo on 2018/10/11.
 * 转赠
 */

public interface ITransfer extends Serializable{

    /**
     * 受赠人信息
     */
    Donee getDonee();

    /**
     * 转赠类目
     * @return
     */
    TransferDaiGouQUanCategoryImpl getTransferCategory();
    Observable<TransferSuccessBean> transfer(Context context, String transferAmount, String password, String remark);
}
