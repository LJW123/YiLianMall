package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取公益分享内容
 * @author zhaiyaohua on 2018/1/19 0019.
 */

public class WelfareShareDataEntivity extends BaseEntity {

    /**
     * data : {"count":1,"create_time":"2018-01-16 09:32:33","foundation_id":"1","img1":"/20180108/e51fddb55bdb48648102e2f0c6b88a86.png","img2":[{"img_url":"/20180109/e51fddb55bdb48648102e2f0c6b88a86.png"},{"img_url":"/20180110/e51fddb55bdb48648102e2f0c6b88a86.png"}],"img3":"/20180111/e51fddb55bdb48648102e2f0c6b88a86.png","integralSum":100,"name":"中国扶贫基金会","project_describe":"乡村校园美颜计划","project_id":"5290256734760802","project_name":"校园救助","project_summary":"说明信息1"}
     * datas : null
     * desc : null
     * outputPage : null
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        /**
         * count : 1
         * create_time : 2018-01-16 09:32:33
         * foundation_id : 1
         * img1 : /20180108/e51fddb55bdb48648102e2f0c6b88a86.png
         * img2 : [{"img_url":"/20180109/e51fddb55bdb48648102e2f0c6b88a86.png"},{"img_url":"/20180110/e51fddb55bdb48648102e2f0c6b88a86.png"}]
         * img3 : /20180111/e51fddb55bdb48648102e2f0c6b88a86.png
         * integralSum : 100
         * name : 中国扶贫基金会
         * project_describe : 乡村校园美颜计划
         * project_id : 5290256734760802
         * project_name : 校园救助
         * project_summary : 说明信息1
         */

        @SerializedName("count")
        public int count;
        @SerializedName("create_time")
        public String createTime;
        @SerializedName("foundation_id")
        public String foundationId;
        @SerializedName("img1")
        public String img1;
        @SerializedName("img3")
        public String img3;
        @SerializedName("integralSum")
        public int integralSum;
        @SerializedName("name")
        public String name;
        @SerializedName("project_describe")
        public String projectDescribe;
        @SerializedName("project_id")
        public String projectId;
        @SerializedName("project_name")
        public String projectName;
        @SerializedName("project_summary")
        public String projectSummary;
        @SerializedName("img2")
        public List<Img2> img2;

        public static class Img2 {
            /**
             * img_url : /20180109/e51fddb55bdb48648102e2f0c6b88a86.png
             */

            @SerializedName("img_url")
            public String imgUrl;
        }
    }
}
