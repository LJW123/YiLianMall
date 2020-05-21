package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import rxfamily.entity.BaseEntity;

/**
 * Created by @author zhaiyaohua on 2018/1/15 0015.
 */

public class WelfareListEntity extends BaseEntity {


    /**
     * data : {"list":[{"img_url":"/20160106/e51fddb55bdb48648102e2f0c6b88a86.png","project_describe":"幸福家家康","project_name":"家庭健康"},{"img_url":"/20180106/e51fddb55bdb48648102e2f0c6b88a86.png","project_describe":"乡村校园美颜计划","project_name":"校园救助"}]}
     */

    @SerializedName("data")
    public Data data;
    @SerializedName("total")
    public String total;


    public static class Data {
        @SerializedName("list")
        public java.util.List<Details> list;
        @SerializedName("totalIntegral")
        public String totalIntegral;

        public static class Details {
            /**
             * img_url : /20160106/e51fddb55bdb48648102e2f0c6b8  8a86.png
             * project_describe : 幸福家家康
             * project_name : 家庭健康
             */

//            @SerializedName("img_url")
//            public String imgUrl;

            @SerializedName("img1")
            public String img1;
            @SerializedName("img2")
            public String img2;
            @SerializedName("project_describe")
            public String projectDescribe;
            @SerializedName("project_name")
            public String projectName;
            @SerializedName("id")
            public String id;
            @SerializedName("count")
            public String count;
        }
    }
}
