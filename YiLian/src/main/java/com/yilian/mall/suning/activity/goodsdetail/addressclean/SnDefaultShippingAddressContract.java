package com.yilian.mall.suning.activity.goodsdetail.addressclean;

import android.content.Context;

import com.yilian.mall.suning.activity.goodsdetail.BaseView;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;

/**
 * @author xiaofo on 2018/7/18.
 */

public class SnDefaultShippingAddressContract {
    interface ISnDefaultShippingAddressInfoPresenter{
        void getSnDefaultShippingAddressInfo(Context context);
    }
   public interface IView extends BaseView {
        void getSnDefaultShippingAddressInfoSuccess(SnShippingAddressInfoEntity snShippingAddressInfoEntity);
    }
}
