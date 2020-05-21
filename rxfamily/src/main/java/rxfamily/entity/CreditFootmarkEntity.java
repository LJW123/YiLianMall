package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class CreditFootmarkEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean data;
    @SerializedName("datas")
    public String datas;
    @SerializedName("desc")
    public String desc;
    @SerializedName("outputPage")
    public OutBean outputPage;

    public class DataBean{
        @SerializedName("content")
        public List<ContentBean> content;

        @SerializedName("endColor")
        public String endColor;
        @SerializedName("grade")
        public String grade;
        @SerializedName("num")
        public int num;
        @SerializedName("startColor")
        public String startColor;
        @SerializedName("title")
        public String title;
    }

    public class OutBean{

        /**
         "currentPage": 1,					// 当前页
         "currentResult": 0,				// 当前记录起始索引
         "showCount": 15,					// 每页显示记录数
         "totalPage": 1,						// 总页数
         "totalResult": 11					// 总记录数
         */

        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("currentResult")
        private int currentResult;
        @SerializedName("showCount")
        private int showCount;
        @SerializedName("totalPage")
        private int totalPage;
        @SerializedName("totalResult")
        private int totalResult;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getCurrentResult() {
            return currentResult;
        }

        public void setCurrentResult(int currentResult) {
            this.currentResult = currentResult;
        }

        public int getShowCount() {
            return showCount;
        }

        public void setShowCount(int showCount) {
            this.showCount = showCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalResult() {
            return totalResult;
        }

        public void setTotalResult(int totalResult) {
            this.totalResult = totalResult;
        }
    }

    public class ContentBean{
        /**
         * create_time : 2018-01-23
         * desc : 添加所在地信息
         * dimensional_code : identity
         * img : safasf
         * name : 身份特性
         * type : 1
         */

        @SerializedName("create_time")
        private String createTime;
        @SerializedName("desc")
        private String desc;
        @SerializedName("dimensional_code")
        private String dimensionalCode;
        @SerializedName("img")
        private String img;
        @SerializedName("name")
        private String name;
        @SerializedName("type")
        private int type;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDimensionalCode() {
            return dimensionalCode;
        }

        public void setDimensionalCode(String dimensionalCode) {
            this.dimensionalCode = dimensionalCode;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        /**
         * "create_time": 1516332067000,		//时间
         *"desc": "年龄",									//描述
         *"dimensional_code": "identity",			//不用管
         *"img": null,										//图标
         *"name": "身份特性",							//维度名称
         *"type": 1											//1:增加   2:删除
         */
    }

}
