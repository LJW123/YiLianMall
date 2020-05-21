package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class SubmitRecordEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean data;
    @SerializedName("desc")
    public String desc;
    @SerializedName("datas")
    public String datas;
    @SerializedName("outputPage")
    public String outputPage;

    public class DataBean{

        /**
         * id : 8b79bde3fcac46a08f207f52e9c4e93f
         * "createDate": "2018-01-22",
         */

        @SerializedName("id")
        private String id;
        @SerializedName("createDate")
        private String createDate;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
