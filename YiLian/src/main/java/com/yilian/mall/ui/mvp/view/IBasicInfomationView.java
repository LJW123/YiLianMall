package com.yilian.mall.ui.mvp.view;

import com.yilian.networkingmodule.entity.BasicInformationEntity;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IBasicInfomationView extends IBaseView {

    void setData(BasicInformationEntity basicInformationEntity);

    void finish();
    BasicInformationEntity.DataBean getData();
}
