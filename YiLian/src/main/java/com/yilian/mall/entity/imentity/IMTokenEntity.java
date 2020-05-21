package com.yilian.mall.entity.imentity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mall.entity.BaseEntity;

import java.util.List;

/**
 * author XiuRenLi on 2016/8/16  PRAY NO BUG
 */

public class IMTokenEntity extends BaseEntity {


    /**
     * message : 融云Token
     * data : []
     * request : POST /app/im.php?c=get_rc_token
     */

    @SerializedName("request")
    public String request;
    @SerializedName("data")
    public List<?> data;
}
