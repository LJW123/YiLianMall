package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class CreditGuardEntity extends BaseEntity {

    @SerializedName("banner_url")
    public String banner_url;
    @SerializedName("list")
    public List<ListBean> list;

    public class ListBean{

        /**
         * title : 提前收货
         * img : /app/images/icon/20171206/recive.png
         * desc : 收货的速度决定您的信用
         * type : 10
         * content : 2
         */

        @SerializedName("title")
        private String title;
        @SerializedName("img")
        private String img;
        @SerializedName("desc")
        private String desc;
        @SerializedName("type")
        private int type;
        @SerializedName("content")
        private int content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getContent() {
            return content;
        }

        public void setContent(int content) {
            this.content = content;
        }
    }

}
