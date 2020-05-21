package rxfamily.entity;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class MyEmailInfoEntity extends BaseEntity{


    /**
     * data : {"create_time":"2017-04-07 12:41:11","email":"sdfasdf@qq.com"}
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
         * create_time : 2017-04-07 12:41:11
         * email : sdfasdf@qq.com
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
