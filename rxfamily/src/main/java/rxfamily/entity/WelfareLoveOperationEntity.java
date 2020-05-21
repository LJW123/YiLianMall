package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 如何获取公益爱心经验值
 * @author zhaiyaohua on 2018/1/19 0019.
 */

public class WelfareLoveOperationEntity extends BaseEntity {

    /**
     * data : {"value_double":10,"value_string":"捐赠1奖券"}
     * desc : null
     * outputPage : null
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        /**
         * value_double : 10
         * value_string : 捐赠1奖券
         */

        @SerializedName("value_double")
        public String valueDouble;
        @SerializedName("value_string")
        public String valueString;
    }
}
