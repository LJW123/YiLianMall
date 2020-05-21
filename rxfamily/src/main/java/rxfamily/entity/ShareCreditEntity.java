package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class ShareCreditEntity extends BaseEntity {


    @SerializedName("datas")
    public List<DataBean> datas;

    public class DataBean{


        /**
         * content : null
         * deputy_picture : http://img4.imgtn.bdimg.com/it/u=1720759960,2922738202&fm=27&gp=0.jpg
         * id : 1
         * main_picture : http://img4.imgtn.bdimg.com/it/u=1720759960,2922738202&fm=27&gp=0.jpg
         * order : 1
         * title : 12
         */

        @SerializedName("content")
        private Object content;
        @SerializedName("deputy_picture")
        private String deputyPicture;
        @SerializedName("id")
        private String id;
        @SerializedName("main_picture")
        private String mainPicture;
        @SerializedName("order")
        private int order;
        @SerializedName("title")
        private String title;

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getDeputyPicture() {
            return deputyPicture;
        }

        public void setDeputyPicture(String deputyPicture) {
            this.deputyPicture = deputyPicture;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMainPicture() {
            return mainPicture;
        }

        public void setMainPicture(String mainPicture) {
            this.mainPicture = mainPicture;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
