package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class SchoolNameEntity extends BaseEntity {

    @SerializedName("desc")
    public String desc;
    @SerializedName("datas")
    public List<DataBean> datas;

    public class DataBean{
        @SerializedName("schools")
        public List<SchoolsBean> schools;

        @SerializedName("letter")
        public String letter;
    }
    public class SchoolsBean{

        /**
         * area_code_provice : 110000
         * code : 10001
         * initial_letter : B
         * name : 北京大学
         */

        @SerializedName("area_code_provice")
        private String areaCodeProvice;
        @SerializedName("code")
        private String code;
        @SerializedName("initial_letter")
        private String initialLetter;
        @SerializedName("name")
        private String name;

        public String getAreaCodeProvice() {
            return areaCodeProvice;
        }

        public void setAreaCodeProvice(String areaCodeProvice) {
            this.areaCodeProvice = areaCodeProvice;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getInitialLetter() {
            return initialLetter;
        }

        public void setInitialLetter(String initialLetter) {
            this.initialLetter = initialLetter;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
