package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：马铁超 on 2018/9/3 17:39
 */

public class SearchFilterBean implements Serializable {

    @SerializedName("code")
    public int code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("comSort")
    public List<ComSortBean> comSort;
    @SerializedName("distSort")
    public List<DistSortBean> distSort;
    @SerializedName("pSort")
    public List<PSortBean> pSort;
    @SerializedName("sSort")
    public List<SSortBean> sSort;
    @SerializedName("secSort")
    public List<SecSortBean> secSort;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ComSortBean> getComSort() {
        return comSort;
    }

    public void setComSort(List<ComSortBean> comSort) {
        this.comSort = comSort;
    }

    public List<DistSortBean> getDistSort() {
        return distSort;
    }

    public void setDistSort(List<DistSortBean> distSort) {
        this.distSort = distSort;
    }

    public List<PSortBean> getPSort() {
        return pSort;
    }

    public void setPSort(List<PSortBean> pSort) {
        this.pSort = pSort;
    }

    public List<SSortBean> getSSort() {
        return sSort;
    }

    public void setSSort(List<SSortBean> sSort) {
        this.sSort = sSort;
    }

    public List<SecSortBean> getSecSort() {
        return secSort;
    }

    public void setSecSort(List<SecSortBean> secSort) {
        this.secSort = secSort;
    }

    public static class ComSortBean implements Serializable{
        /**
         * sorts : comSort DESC
         * title : 综合排序
         * key : comsort
         */
        @SerializedName("sorts")
        public String sorts;
        @SerializedName("title")
        public String title;
        @SerializedName("key")
        public String key;
        public boolean isCheck;
        public String getSorts() {
            return sorts;
        }

        public void setSorts(String sorts) {
            this.sorts = sorts;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }

    public static class DistSortBean implements Serializable{
        /**
         * sorts : [{"sorts":"500","title":"500米"},{"sorts":"1000","title":"1公里"},{"sorts":"2000","title":"2公里"},{"sorts":"4000","title":"4公里"},{"sorts":"8000","title":"8公里"},{"sorts":"10000","title":"10公里"}]
         * title : 直线距离
         * key : distance
         */
        @SerializedName("title")
        public String title;
        @SerializedName("key")
        public String key;
        public List<SortsBean> sorts;
        public boolean isCheck;
        public boolean circleShow;



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<SortsBean> getSorts() {
            return sorts;
        }

        public void setSorts(List<SortsBean> sorts) {
            this.sorts = sorts;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public boolean isCircleShow() {
            return circleShow;
        }

        public void setCircleShow(boolean circleShow) {
            this.circleShow = circleShow;
        }

        public static class SortsBean implements Serializable{
            /**
             * sorts : 500
             * title : 500米
             */
            @SerializedName("sorts")
            public String sorts;
            @SerializedName("title")
            public String title;
            public boolean isCheck;
            public boolean saveCheck;


            public String getSorts() {
                return sorts;
            }

            public void setSorts(String sorts) {
                this.sorts = sorts;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public boolean isSaveCheck() {
                return saveCheck;
            }

            public void setSaveCheck(boolean saveCheck) {
                this.saveCheck = saveCheck;
            }
        }
    }

    public static class PSortBean implements Serializable{

        @SerializedName("title")
        public String title;
        @SerializedName("price")
        public String price;
        @SerializedName("key")
        public String key;
        public String minRange;
        public String maxRange;

        public List<SortsBeanX> sorts;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<SortsBeanX> getSorts() {
            return sorts;
        }

        public void setSorts(List<SortsBeanX> sorts) {
            this.sorts = sorts;
        }



        public static class SortsBeanX implements Serializable{
            /**
             * sorts : 0,150
             * title : ¥150以下
             */
            @SerializedName("sorts")
            public String sorts;
            @SerializedName("title")
            public String title;
            public boolean isCheck;

            public String getSorts() {
                return sorts;
            }

            public void setSorts(String sorts) {
                this.sorts = sorts;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }
    }

    public static class SSortBean implements Serializable{

        @SerializedName("title")
        public String title;
        @SerializedName("key")
        public String key;
        public List<SortsBeanXX> sorts;
        public boolean isCheck;
        public boolean isCircleShow;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<SortsBeanXX> getSorts() {
            return sorts;
        }

        public void setSorts(List<SortsBeanXX> sorts) {
            this.sorts = sorts;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public boolean isCircleShow() {
            return isCircleShow;
        }

        public void setCircleShow(boolean circleShow) {
            isCircleShow = circleShow;
        }

        public static class SortsBeanXX implements Serializable{
            /**
             * sorts : 11
             * title : 游乐园周边
             */
            @SerializedName("sorts")
            public String sorts;
            @SerializedName("title")
            public String title;
            public boolean isCheck;

            public String getSorts() {
                return sorts;
            }

            public void setSorts(String sorts) {
                this.sorts = sorts;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }
    }

    public static class SecSortBean implements Serializable{

        @SerializedName("sorts")
        public String sorts;
        @SerializedName("title")
        public String title;
        @SerializedName("key")
        public String key;
        public boolean isCheck;
        public String getSorts() {
            return sorts;
        }

        public void setSorts(String sorts) {
            this.sorts = sorts;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
