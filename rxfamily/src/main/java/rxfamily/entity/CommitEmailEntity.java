package rxfamily.entity;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class CommitEmailEntity extends BaseEntity {

    /**
     * data : {"create_time":"2018-01-23 12:57:29.0","email":"xuewenhuina@yeah.net"}
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
         * create_time : 2018-01-23 12:57:29.0
         * email : xuewenhuina@yeah.net
         */

        private String create_time;
        private String email;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
