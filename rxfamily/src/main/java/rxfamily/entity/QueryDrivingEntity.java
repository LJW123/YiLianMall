package rxfamily.entity;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class QueryDrivingEntity extends BaseEntity {

    /**
     * data : {"address":null,"back":"455415","birthday":null,"brand_type":"aa","car_no":"a521251","create_time":"2018-01-19","engine_no":"3135","id":"4140305a91b64b63b6a7dc66ccba485e","initial_date":"1992-01-01","issue_date":"2000-01-01","name":"1asfasf","nationality":null,"positive":"241541","register_date":"1992-01-01","sex":0,"update_time":"2018-01-19","user_id":"6292180495306720","vin":"12"}
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
         * address : null
         * back : 455415
         * birthday : null
         * brand_type : aa
         * car_no : a521251
         * create_time : 2018-01-19
         * engine_no : 3135
         * id : 4140305a91b64b63b6a7dc66ccba485e
         * initial_date : 1992-01-01
         * issue_date : 2000-01-01
         * name : 1asfasf
         * nationality : null
         * positive : 241541
         * register_date : 1992-01-01
         * sex : 0
         * update_time : 2018-01-19
         * user_id : 6292180495306720
         * vin : 12
         */

        private String address;
        private String back;
        private String birthday;
        private String brand_type;
        private String car_no;
        private String create_time;
        private String engine_no;
        private String id;
        private String initial_date;
        private String issue_date;
        private String name;
        private String nationality;
        private String positive;
        private String register_date;
        private String sex;
        private String update_time;
        private String user_id;
        private String vin;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBack() {
            return back;
        }

        public void setBack(String back) {
            this.back = back;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBrand_type() {
            return brand_type;
        }

        public void setBrand_type(String brand_type) {
            this.brand_type = brand_type;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getEngine_no() {
            return engine_no;
        }

        public void setEngine_no(String engine_no) {
            this.engine_no = engine_no;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInitial_date() {
            return initial_date;
        }

        public void setInitial_date(String initial_date) {
            this.initial_date = initial_date;
        }

        public String getIssue_date() {
            return issue_date;
        }

        public void setIssue_date(String issue_date) {
            this.issue_date = issue_date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getPositive() {
            return positive;
        }

        public void setPositive(String positive) {
            this.positive = positive;
        }

        public String getRegister_date() {
            return register_date;
        }

        public void setRegister_date(String register_date) {
            this.register_date = register_date;
        }

        public String getSex() {
            return sex;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }
    }
}
