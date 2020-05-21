package com.yilian.mall.ui.mvp.view;

import com.yilian.networkingmodule.entity.SupportBankListEntity;

/**
 * @author Created by  on 2018/1/21.
 */

public interface ISupportBankListView extends IBaseView {
    /**
     * 获取支持银行卡类型 0私有的 1对公的
     *
     * @return
     */
    int getType();

    void setData(SupportBankListEntity supportBankListEntity);
}
