package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import rxfamily.entity.BaseEntity;

/**
 * Created by @author zhaiyaohua on 2018/1/16 0016.
 */

public class WelfareExpericeEntity extends BaseEntity {

    /**
     * data : {"end":200,"level":1,"level_name":"爱心新秀","name":"xh","photo":"app/images/head/20171109/7435908194499807_9657332_.jpg","rank":2,"supLevel":2,"supName":"爱心见习生","totalCredit":40,"totalIntegral":130}
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
//        {
//            "code": 1,
//                "data": {
//            "end": 200,//结束经验值
//                    "level": 1,//当前级别
//                    "level_name": "爱心新秀",//等级名称
//                    "name": "xh",//消费者用户昵称
//                    "photo": "app/images/head/20171109/7435908194499807_9657332_.jpg",//用户头像图片存储路径
//                    "rank": 2,//会员当前级别
//                    "supLevel": 2,//下一级别
//                    "supName": "爱心见习生",//下一等级名称
//                    "totalCredit": 40,//当前总经验值
//                    "totalIntegral": 130//累积捐赠奖券
//        },
//            "msg": "执行成功"
//        }

        @SerializedName("end")
        public int end;
        @SerializedName("level")
        public int level;
        @SerializedName("level_name")
        public String levelName;
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("rank")
        public int rank;
        @SerializedName("supLevel")
        public int supLevel;
        @SerializedName("supName")
        public String supName;
        @SerializedName("totalCredit")
        public int totalCredit;
        @SerializedName("totalIntegral")
        public String totalIntegral;
        @SerializedName("count")
        public String count;
    }
}
