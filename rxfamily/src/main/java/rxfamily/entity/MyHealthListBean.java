package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class MyHealthListBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {

        @SerializedName("projects")
        public ArrayList<ListBean> projects;

        public class ListBean {
            @SerializedName("NAME")
            public String NAME;
            @SerializedName("IMG_URL")
            public String IMG_URL;
            @SerializedName("RANGE_TITLE")
            public String RANGE_TITLE;
            @SerializedName("LIMIT")
            public String LIMIT;
            @SerializedName("projectId")
            public String projectId;
        }
    }
}
