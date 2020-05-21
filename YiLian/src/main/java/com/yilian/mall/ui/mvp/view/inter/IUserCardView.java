package com.yilian.mall.ui.mvp.view.inter;

import com.yilian.mall.ui.mvp.view.IBaseView;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.MyCardEntity;

/**
 * @author Zg on 2018/11/18.
 */

public interface IUserCardView extends IBaseView {

    void getUserCardSuccess(MyCardEntity myCardEntity);

    void getUserCardError(String errorMsg);

}
