package rxfamily.entity;

/**
 * Created by Administrator on 2018/1/17 0017
 * 确认修改驾驶证信息.
 */

public class MyDriverLicenseEntity extends BaseEntity {

    /**
     * data : {"updateDate":"2018-01-18"}
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
         * updateDate : 2018-01-18
         */
        private String updateDate;
        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }
    }
}
