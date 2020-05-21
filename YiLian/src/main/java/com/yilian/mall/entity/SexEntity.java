package com.yilian.mall.entity;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * @author Created by  on 2018/1/15.
 *         性别
 */

public class SexEntity implements IPickerViewData {
    public SexEntity(String sex, String sexType) {
        this.sex = sex;
        this.sexType = sexType;
    }

    public String sex;
    public String sexType;


    @Override
    public String getPickerViewText() {
        return sex;
    }
}
