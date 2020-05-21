package com.yilian.mall.ui.transfer.inter;

import java.io.Serializable;

/**
 * @author xiaofo on 2018/10/11.
 */

public interface ITransferCategory extends Serializable {
    String getCategoryName();
    String getTitle();
    String getSubTitle();
    String getRate();
    String getMinTransferAmount();
    String getMaxTransferAmount();

}
