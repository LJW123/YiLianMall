package rxfamily.entity;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class UploadDriverImgEntity extends BaseEntity {

    /**
     * data : {"picUrl":"oss-cn-beijing.aliyuncs.com/credit-driver/44e453af26284ce5a0ea679432a18588.png"}
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
         * picUrl : oss-cn-beijing.aliyuncs.com/credit-driver/44e453af26284ce5a0ea679432a18588.png
         */

        private String picUrl;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}
