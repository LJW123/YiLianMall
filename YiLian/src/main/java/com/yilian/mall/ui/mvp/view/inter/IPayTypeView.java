package com.yilian.mall.ui.mvp.view.inter;

import com.yilian.mall.ui.mvp.view.IBaseView;
import com.yilian.networkingmodule.entity.PayTypeEntity;

/**
 * @author xiaofo on 2018/7/8.
 */

public interface IPayTypeView extends IBaseView{
    void getPayTypeListSuccess(PayTypeEntity payTypeEntity);
}
