package com.yilian.mall.ui.mvp.view.inter;

import com.yilian.mall.ui.mvp.view.IBaseView;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.MyCardEntity;
import com.yilian.networkingmodule.entity.PayTypeEntity;

/**
 * @author xiaofo on 2018/7/8.
 */

public interface IUserMoneyView extends IBaseView {

    void getUserMoneySuccess(MyBalanceEntity2 myBalanceEntity2);

}
