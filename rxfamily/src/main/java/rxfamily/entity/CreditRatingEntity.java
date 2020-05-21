package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class CreditRatingEntity extends BaseEntity {

    @SerializedName("datas")
    public List<DataBean> datas;

    public class DataBean{
        /**
         * end : 450
         * end_color : eb5154
         * id : 1
         * level : 1
         * name : 较差
         * start : 300
         * start_color : f66e46
         */

        @SerializedName("end")
        private int end;
        @SerializedName("end_color")
        private String endColor;
        @SerializedName("id")
        private String id;
        @SerializedName("level")
        private int level;
        @SerializedName("name")
        private String name;
        @SerializedName("start")
        private int start;
        @SerializedName("start_color")
        private String startColor;

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public String getEndColor() {
            return endColor;
        }

        public void setEndColor(String endColor) {
            this.endColor = endColor;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public String getStartColor() {
            return startColor;
        }

        public void setStartColor(String startColor) {
            this.startColor = startColor;
        }
    }

}
