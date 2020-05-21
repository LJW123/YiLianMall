package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @author zhaiyaohua on 2018/1/23 0023.
 */

public class AuthenticationInfoEntity extends BaseEntity {

    /**
     * info : {"card_name":"","user_name":"","card_id":"","card_status":""}
     */

    @SerializedName("info")
    public InfoBean info;

    public static class InfoBean {
        /**
         * card_name :
         * user_name :
         * card_id :
         * card_status :
         */

        @SerializedName("card_name")
        public String cardName;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("card_id")
        public String cardId;
        @SerializedName("card_status")
        public String cardStatus;
    }
}
