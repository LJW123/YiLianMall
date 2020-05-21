package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HealthFriendRobAllCountBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("lootApple")
        public String lootApple;

        @SerializedName("lootappleDay")
        public String lootappleDay;
    }
}
