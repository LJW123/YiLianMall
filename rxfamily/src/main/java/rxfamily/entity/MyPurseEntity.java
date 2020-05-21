package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class MyPurseEntity extends BaseEntity {

    /**
     * data : {"card_num":3,"cash":"86733608","info":[{"content":"42","image":"admin/images/5fcd97b400bd2b0d344b5999df4fb7ab39cac0c1_1.png","name":"我的公益","type":"9"},{"content":"http://baidu.com?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo2ODY5NDY0MjU3NDk3MDE0LCJleHBpcmVzX2luIjoxNTE5Mjc1MDcxfQ.CfC-XEzKlSMplzPJlOHTrOVoMB3H-TEJgXe5NLCWE48&mobile=18203660535","image":"admin/images/3e6a51b16b59f1c504e8f48ac8bc8ef3babbc76f_1.png","name":"益豆宝","type":"18"},{"content":"41","image":"admin/images/6c226d84262c191fad4840f41a6f1dc839cdb784_1.png","name":"我的健康","type":"9"},{"content":"40","image":"admin/images/11fe174312252de7222f301ad6ef52c8b4d48123_1.png","name":" 我的信用","type":"9"},{"content":"39","image":"admin/images/3392eaa7a21175e1fa0c572547f2b4c33389c82d_1.png","name":"集分宝","type":"9"}],"integral":"1"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * card_num : 3
         * cash : 86733608
         * info : [{"content":"42","image":"admin/images/5fcd97b400bd2b0d344b5999df4fb7ab39cac0c1_1.png","name":"我的公益","type":"9"},{"content":"http://baidu.com?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo2ODY5NDY0MjU3NDk3MDE0LCJleHBpcmVzX2luIjoxNTE5Mjc1MDcxfQ.CfC-XEzKlSMplzPJlOHTrOVoMB3H-TEJgXe5NLCWE48&mobile=18203660535","image":"admin/images/3e6a51b16b59f1c504e8f48ac8bc8ef3babbc76f_1.png","name":"益豆宝","type":"18"},{"content":"41","image":"admin/images/6c226d84262c191fad4840f41a6f1dc839cdb784_1.png","name":"我的健康","type":"9"},{"content":"40","image":"admin/images/11fe174312252de7222f301ad6ef52c8b4d48123_1.png","name":" 我的信用","type":"9"},{"content":"39","image":"admin/images/3392eaa7a21175e1fa0c572547f2b4c33389c82d_1.png","name":"集分宝","type":"9"}]
         * integral : 1
         */

        @SerializedName("card_num")
        public String cardNum;
        @SerializedName("cash")
        public String cash;
        @SerializedName("integral")
        public String integral;
        @SerializedName("info")
        public List<InfoBean> info;

        public static class InfoBean {
            /**
             * content : 42
             * image : admin/images/5fcd97b400bd2b0d344b5999df4fb7ab39cac0c1_1.png
             * name : 我的公益
             * type : 9
             */

            @SerializedName("content")
            public String content;
            @SerializedName("image")
            public String image;
            @SerializedName("name")
            public String name;
            @SerializedName("type")
            public int type;
        }
    }
}
