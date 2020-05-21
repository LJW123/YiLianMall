package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * 公益成长体系
 * Created by @author zhaiyaohua on 2018/1/16 0016.
 */

public class WelfareGrowthSystem extends BaseEntity {


    /**
     * data : {"list":[{"end":200,"level":1,"level_name":"爱心新秀","start":0},{"end":300,"level":2,"level_name":"爱心见习生","start":200},{"end":400,"level":3,"level_name":"爱心大人","start":300},{"end":500,"level":4,"level_name":"爱心大使","start":400},{"end":600,"level":5,"level_name":"爱心元老","start":500}]}
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("list")
        public java.util.List<Details> list;

        public static class Details {
            /**
             * end : 200
             * level : 1
             * level_name : 爱心新秀
             * start : 0
             */

            @SerializedName("end")
            public int end;
            @SerializedName("level")
            public int level;
            @SerializedName("level_name")
            public String levelName;
            @SerializedName("start")
            public int start;
        }
    }
}
