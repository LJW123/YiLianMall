package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class ALiCarBackDataValue {

    /**
     * config_str :
     * archive_no : 370211375349
     * success : true
     */
    @SerializedName("config_str")
    public String configStr;
    @SerializedName("archive_no")
    public String archiveNo;
    @SerializedName("success")
    public boolean success;

}
