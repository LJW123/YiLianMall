package com.yilian.mylibrary;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by  on 2018/1/21.
 */

public class AliIdCardBackOutputValue {

    /**
     * dataType : 50
     * dataValue : {"config_str":"{\"side\":\"back\"}","end_date":"20190902","issue":"***公安局","request_id":"20180121134714_393c9abfc85ae14f6778a7b2e5c62869","start_date":"20090902","success":true}
     */

    @SerializedName("dataType")
    public int dataType;
    @SerializedName("dataValue")
    public AliIdCardBackDataValue dataValue;
}
