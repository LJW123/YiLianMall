package rxfamily.entity;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class MyEmailEntity extends BaseEntity {


    /**
     * data : {}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
