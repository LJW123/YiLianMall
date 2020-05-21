package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HelpOtherFindBAIBean extends BaseEntity {

    @SerializedName("data")
    public DataBean datas;

    public class DataBean {
        @SerializedName("BIRTHDAY")
        public String BIRTHDAY;
        @SerializedName("ID_NUM")
        public String ID_NUM;
    }
}
