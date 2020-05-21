package com.yilian.mall.ui.transfer.impl;

import com.yilian.mall.ui.transfer.inter.ITransferCategory;

/**
 * @author xiaofo on 2018/10/11.
 *         转赠类目
 */

public class TransferDaiGouQUanCategoryImpl implements ITransferCategory {
    private final String maxTransferAmount;
    /**
     * 手续费比例
     */
    public String transferRate;
    /**
     * 最小转赠金额
     */
    public String minTransferAmount;
    String actualValue;

    public TransferDaiGouQUanCategoryImpl(String transferRate, String minTransferAmount, String maxTransferAmount) {
        this.transferRate = transferRate;
        this.minTransferAmount = minTransferAmount;

        this.maxTransferAmount = maxTransferAmount;
    }    @Override
    public String getCategoryName() {
        return "兑换券";
    }

    @Override
    public String getTitle() {
        return "兑换券转赠";
    }

    @Override
    public String getSubTitle() {
        return "兑换券转赠";
    }




    @Override
    public String getRate() {
        return transferRate;
    }

    @Override
    public String getMinTransferAmount() {
        return minTransferAmount;
    }

    @Override
    public String getMaxTransferAmount() {
        return maxTransferAmount;
    }
}
