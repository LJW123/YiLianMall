package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import rxfamily.entity.BaseEntity;

/**
 * 公益爱心成就
 *  @author zhaiyaohua on 2018/1/16 0016.
 */

public class WelfarePersonInfoEntity extends BaseEntity {


    /**
     * data : {"condition":[{"condition":2,"id":"1","mainImgUrl":"主图灰","name":"积善成德","rule":["1","2"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":1},{"condition":4,"id":"2","mainImgUrl":"主图灰","name":"爱心巨匠","rule":["3","4"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":2},{"condition":6,"id":"3","mainImgUrl":"主图灰","name":"博施济众","rule":["5","6"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":3},{"condition":8,"id":"4","mainImgUrl":"主图灰","name":"一呼百应","rule":["7","8"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":4},{"condition":10,"id":"5","mainImgUrl":"主图灰","name":"持之以恒","rule":["9","10"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":5},{"condition":12,"id":"6","mainImgUrl":"主图灰","name":"一心一意","rule":["10","12"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":6}],"donationNumber":0}
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        /**
         * condition : [{"condition":2,"id":"1","mainImgUrl":"主图灰","name":"积善成德","rule":["1","2"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":1},{"condition":4,"id":"2","mainImgUrl":"主图灰","name":"爱心巨匠","rule":["3","4"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":2},{"condition":6,"id":"3","mainImgUrl":"主图灰","name":"博施济众","rule":["5","6"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":3},{"condition":8,"id":"4","mainImgUrl":"主图灰","name":"一呼百应","rule":["7","8"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":4},{"condition":10,"id":"5","mainImgUrl":"主图灰","name":"持之以恒","rule":["9","10"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":5},{"condition":12,"id":"6","mainImgUrl":"主图灰","name":"一心一意","rule":["10","12"],"san_img_url_List":["子图1","子图2","子图3"],"secondary_img_url":"附图亮","sort":6}]
         * donationNumber : 0
         */

        @SerializedName("totalIntegral")
        public String totalIntegral;

        @SerializedName("donationNumber")
        public String donationNumber;
        @SerializedName("completed")
        public int completed;
        @SerializedName("condition")
        public List<Condition> condition;

        public static class Condition {
            /**
             * condition : 2
             * id : 1
             * mainImgUrl : 主图灰
             * name : 积善成德
             * rule : ["1","2"]
             * san_img_url_List : ["子图1","子图2","子图3"]
             * secondary_img_url : 附图亮
             * sort : 1
             */

            @SerializedName("condition")
            public String condition;
            @SerializedName("id")
            public String id;
            @SerializedName("mainImgUrl")
            public String mainImgUrl;
            @SerializedName("name")
            public String name;
            @SerializedName("secondary_img_url")
            public String secondaryImgUrl;
            @SerializedName("sort")
            public int sort;
            @SerializedName("rule")
            public List<String> rule;
            @SerializedName("san_img_url_List")
            public List<String> sanImgUrlList;
        }
    }
}
