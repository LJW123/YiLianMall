package rxfamily.entity;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class AddDriverLicenseEntity extends BaseEntity{


    /**
     * data : {"createDate":"2010-01-01","id":"3d32bc2a8ebe4538b1ba81aa5538a6ba"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * createDate : 2010-01-01
         * id : 3d32bc2a8ebe4538b1ba81aa5538a6ba
         */

        private String createDate;
        private String id;

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
