package com.yilian.mall.ctrip.bean;

import java.util.ArrayList;

/**
 * 作者：马铁超 on 2018/10/28 15:23
 */

public class FacilityListBean {
    public String FacilityTitle;
    public ArrayList<FacilityListData> facilityListData;

    public static class FacilityListData{
        public String id;
        public String name;
        public String status;
    }
}
