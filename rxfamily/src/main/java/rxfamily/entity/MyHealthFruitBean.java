package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class MyHealthFruitBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("tardeRatio")
        public String tardeRatio;

        @SerializedName("APPLES_NUM")
        public String APPLES_NUM;

        @SerializedName("mutualPlan")
        public PlanBean mutualPlan;

        public class PlanBean {
            @SerializedName("IMG_URL")
            public String IMG_URL;
            @SerializedName("ID")
            public String ID;
            @SerializedName("NAME")
            public String NAME;
            @SerializedName("EXCHANGE_RATIO")
            public String EXCHANGE_RATIO;
            @SerializedName("LIMIT")
            public String LIMIT;
        }

        @SerializedName("instructions")
        public ArrayList<MsgBean> instructions;

        public class MsgBean {
            @SerializedName("CREATE_DATE")
            public String CREATE_DATE;
            @SerializedName("DESCRIPTION")
            public String DESCRIPTION;
            @SerializedName("ICON_URL")
            public String ICON_URL;
            @SerializedName("ID")
            public String ID;
            @SerializedName("SUB_TITLE")
            public String SUB_TITLE;
            @SerializedName("TITLE")
            public String TITLE;
            @SerializedName("TYPE")
            public String TYPE;
            @SerializedName("VALIDITY")
            public String VALIDITY;
            @SerializedName("VIEW_TYPE")
            public String VIEW_TYPE;

            @Override
            public String toString() {
                return "MsgBean{" +
                        "CREATE_DATE='" + CREATE_DATE + '\'' +
                        ", DESCRIPTION='" + DESCRIPTION + '\'' +
                        ", ICON_URL='" + ICON_URL + '\'' +
                        ", ID='" + ID + '\'' +
                        ", SUB_TITLE='" + SUB_TITLE + '\'' +
                        ", TITLE='" + TITLE + '\'' +
                        ", TYPE='" + TYPE + '\'' +
                        ", VALIDITY='" + VALIDITY + '\'' +
                        ", VIEW_TYPE='" + VIEW_TYPE + '\'' +
                        '}';
            }
        }
    }

}
