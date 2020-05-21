package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HealthFriendRobListBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public class ListBean {
            @SerializedName("name")
            public String name;
            @SerializedName("apples_num")
            public String apples_num;
            @SerializedName("photo")
            public String photo;
            @SerializedName("user_id")
            public String user_id;

            public String state;

            @Override
            public String toString() {
                return "ListBean{" +
                        "name='" + name + '\'' +
                        ", apples_num='" + apples_num + '\'' +
                        ", photo='" + photo + '\'' +
                        ", user_id='" + user_id + '\'' +
                        ", state='" + state + '\'' +
                        '}';
            }
        }
    }

}
