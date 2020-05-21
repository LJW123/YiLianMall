package com.yilian.networkingmodule.entity.jd;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 京东or苏宁 政企意见反馈类型
 *
 * @author Created by Zg on 2018/6/20.
 */

public class FeedbackTypeEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean implements IPickerViewData {

        /**
         * "id": "",//主键ID
         * name: "价格问题"
         */

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        @Override
        public String getPickerViewText() {
            return name;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
