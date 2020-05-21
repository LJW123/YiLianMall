package rxfamily.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class QueryDriverLicenseEntity extends BaseEntity implements Serializable{

    /**
     * data : {"address":"河南省","back":"ll","birthday":"2000-01-01","car_type":"C1","certificate_no":"23513513","effective_date":"2000-01-01","expiry_date":"2001-01-01","file_no":"11111111","id":"a2d432589efe4ebeaf2183a88cb4925d","initial_date":"1996-01-01","name":"nnnn","nationality":"中国","positive":"asdasdf","sex":1,"user_id":"6292180495306720","create_time":"2017-01-01","update_time":"2017-01-01"}
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
         * address : 河南省
         * back : ll
         * birthday : 2000-01-01
         * car_type : C1
         * certificate_no : 23513513
         * effective_date : 2000-01-01
         * expiry_date : 2001-01-01
         * file_no : 11111111
         * id : a2d432589efe4ebeaf2183a88cb4925d
         * initial_date : 1996-01-01
         * name : nnnn
         * nationality : 中国
         * positive : asdasdf
         * sex : 1
         * user_id : 6292180495306720
         * create_time : 2017-01-01
         * update_time : 2017-01-01
         */

        private String address;
        private String back;
        private String birthday;
        private String car_type;
        private String certificate_no;
        private String effective_date;
        private String expiry_date;
        private String file_no;
        private String id;
        private String initial_date;
        private String name;
        private String nationality;
        private String positive;
        private String sex;
        private String user_id;
        private String create_time;
        private String update_time;

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

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public String getCertificate_no() {
            return certificate_no;
        }

        public void setCertificate_no(String certificate_no) {
            this.certificate_no = certificate_no;
        }

        public String getEffective_date() {
            return effective_date;
        }

        public void setEffective_date(String effective_date) {
            this.effective_date = effective_date;
        }

        public String getExpiry_date() {
            return expiry_date;
        }

        public void setExpiry_date(String expiry_date) {
            this.expiry_date = expiry_date;
        }

        public String getFile_no() {
            return file_no;
        }

        public void setFile_no(String file_no) {
            this.file_no = file_no;
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

        public String getSex() {
            return sex;
        }


        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
