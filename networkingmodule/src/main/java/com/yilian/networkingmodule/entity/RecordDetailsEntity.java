package com.yilian.networkingmodule.entity;


import java.util.List;

/**
 * @author Created by  on 2017/12/13.
 *         奖励、奖券记录单笔详情
 */

public class RecordDetailsEntity {

    public List<DataBean> data;

    public static class DataBean {
        /**
         * content : 通过微信扫码商家收入
         * title : 交易类型
         * line_type:是否显示分割线   1 显示
         */

        public String content;
        public String title;
        public int lineType;

        public DataBean(String title,String content,int lineType){
            this.content =content;
            this.title =title;
            this.lineType =lineType;
        }
    }
}
