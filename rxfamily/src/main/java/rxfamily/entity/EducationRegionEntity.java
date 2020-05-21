package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class EducationRegionEntity extends BaseEntity {

    @SerializedName("desc")
    public String desc;
    @SerializedName("data")
    public String data;
    @SerializedName("outputPage")
    public String outputPage;
    @SerializedName("datas")
    public List<DataBean> datas;

    public class DataBean{
        @SerializedName("areas")
        public List<AreasBean> areas;

        @SerializedName("letter")
        public String letter;

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }
    }

    public class AreasBean{

        /**
         * code : 340000
         * initial_letter : A
         * name : 安徽省
         * parent_code : 0
         */

        @SerializedName("code")
        private String code;
        @SerializedName("initial_letter")
        private String initialLetter;
        @SerializedName("name")
        private String name;
        @SerializedName("parent_code")
        private String parentCode;

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

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }
    }
}
