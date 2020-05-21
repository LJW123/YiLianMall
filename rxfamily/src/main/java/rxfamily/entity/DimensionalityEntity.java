package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class DimensionalityEntity extends BaseEntity {


    @SerializedName("data")
    public DataBean data;

    public class DataBean{
        @SerializedName("creditDimensionalList")
        public List<CreditBean> creditDimensionalList;
        @SerializedName("creditLevelName")
        public String creditLevelName;
        @SerializedName("creditTotal")
        public int creditTotal;
        @SerializedName("creditLevel")
        public int creditLevel;
        @SerializedName("tempCredit")
        public int tempCredit;
        @SerializedName("creditCeiling")
        public int creditCeiling;
        @SerializedName("creditInit")
        public int creditInit;
    }



    public class CreditBean{

        /**
         * auto_id : 1
         * code : identity
         * desc : sdfasdf
         * icon_gray : 维度图标-灰
         * icon_bright : 维度图标-亮
         * id : 1
         * credit : 0
         * name : 身份特质
         */

        @SerializedName("auto_id")
        private int autoId;
        @SerializedName("code")
        private String code;
        @SerializedName("desc")
        private String desc;
        @SerializedName("icon_gray")
        private String iconGray;
        @SerializedName("icon_bright")
        private String iconBright;
        @SerializedName("id")
        private String id;
        @SerializedName("credit")
        private int credit;
        @SerializedName("name")
        private String name;

        public int getAutoId() {
            return autoId;
        }

        public void setAutoId(int autoId) {
            this.autoId = autoId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIconGray() {
            return iconGray;
        }

        public void setIconGray(String iconGray) {
            this.iconGray = iconGray;
        }

        public String getIconBright() {
            return iconBright;
        }

        public void setIconBright(String iconBright) {
            this.iconBright = iconBright;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
