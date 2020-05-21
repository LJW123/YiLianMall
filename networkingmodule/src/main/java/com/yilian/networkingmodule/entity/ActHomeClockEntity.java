package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2017/11/14 0014.
 *         活动首页打卡数据
 */

public class ActHomeClockEntity extends HttpResultBean {

    /**
     * id : 1
     * cover : admin/images/ffda3d6015f9ea8bee2579eb8721803b3aac11ca_foot_right_bg.png
     * date_no : 20171109
     * title_sub : 早起打卡 养成习惯
     * join_num : 4
     */

    @SerializedName("id")
    public String id;
    @SerializedName("cover")
    public String cover;
    @SerializedName("date_no")
    public String dateNo;
    @SerializedName("title_sub")
    public String titleSub;
    @SerializedName("join_num")
    public int joinNum;
}
