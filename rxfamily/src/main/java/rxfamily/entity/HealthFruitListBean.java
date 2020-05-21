package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HealthFruitListBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("list")
        public List<ListBean> list;

        public class ListBean {
            @SerializedName("APPLES_NUM")
            public String APPLES_NUM;
            @SerializedName("CREATE_DATE")
            public String CREATE_DATE;
            @SerializedName("ID")
            public String ID;
            @SerializedName("NAME")
            public String NAME;
            @SerializedName("TYPE")
            public String TYPE;
            @SerializedName("describe")
            public String describe;
        }
    }

}
