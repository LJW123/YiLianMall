package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class PublicBenefitHomeBageEntity extends BaseEntity {

    @SerializedName("datas")
    public String datas;
    @SerializedName("desc")
    public String desc;
    @SerializedName("outputPage")
    public String outputPage;
    @SerializedName("data")
    public DataBean data;

    public class DataBean{
        @SerializedName("list")
        public List<ListBean> list;
        @SerializedName("total")
        public String total;
        @SerializedName("totalDonation")
        public String totalDonation;
        @SerializedName("totalIntegral")
        public String totalIntegral;
    }
    public class ListBean{
        /**
         "id": "6290256734760801",//公益项目ID
         "count": 13,//爱心捐赠份数
         "img1": "/20160106/e51fddb55bdb48648102e2f0c6b88a86.png",//(主图)图片路径
         "img2": null,//（缩略图）图片路径
         "project_describe": "幸福家家康",//项目描述
         "project_name": "家庭健康"//项目名
         */

        @SerializedName("id")
        private String id;
        @SerializedName("count")
        private int count;
        @SerializedName("img1")
        private String img1;
        @SerializedName("img2")
        private String img2;
        @SerializedName("project_describe")
        private String projectDescribe;
        @SerializedName("project_name")
        private String projectName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getImg1() {
            return img1;
        }

        public void setImg1(String img1) {
            this.img1 = img1;
        }

        public String getImg2() {
            return img2;
        }

        public void setImg2(String img2) {
            this.img2 = img2;
        }

        public String getProjectDescribe() {
            return projectDescribe;
        }

        public void setProjectDescribe(String projectDescribe) {
            this.projectDescribe = projectDescribe;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }
    }

}
