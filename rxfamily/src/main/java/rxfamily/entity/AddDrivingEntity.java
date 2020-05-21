package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @author zhaiyaohua on 2018/1/22 0022.
 */

public class AddDrivingEntity extends BaseEntity {

    /**
     * data : {"createDate":"2010-04-01","id":"6c4f2ba1ce5e4ae18223287f708f07e6"}
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        /**
         * createDate : 2010-04-01
         * id : 6c4f2ba1ce5e4ae18223287f708f07e6
         */

        @SerializedName("createDate")
        public String createDate;
        @SerializedName("id")
        public String id;
    }
}
