package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class HealthMyFamilyBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("homeplans")
        public ArrayList<Map<String, ArrayList<HomeBean.FamilyBean>>> homeplans;

        public class HomeBean {

            public class FamilyBean {
                @SerializedName("APPLY_DATE")//申请时间
                public String APPLY_DATE;
                @SerializedName("JOIN_AMOUNT")//加入金额
                public String JOIN_AMOUNT;
                @SerializedName("PERIOD_TPYE")//期限类型（1.年，2.月，3天）
                public String PERIOD_TPYE;
                @SerializedName("PLAN_PERIOD")//互助期限
                public String PLAN_PERIOD;
                @SerializedName("STATUS")//状态（1.已提交；2.审核通过；3.审核驳回；４.保障中；５.已结算；６.已结束）
                public String STATUS;
                @SerializedName("TAG")//目前有四种类型：本人；配偶；子女；父母
                public String TAG;
                @SerializedName("TAG_ID")//1：本人；2：配偶；3：父母；4：子女；
                public String TAG_ID;
                @SerializedName("WAIT_PERIOD") //等待生效天数
                public String waitTime;
                @SerializedName("joinUserId")//加入人员ID
                public String joinUserId;
                @SerializedName("msg")//描述内容
                public String msg;
                @SerializedName("projectId")//参与项目ID
                public String projectId;
                @SerializedName("project_name") //参与项目名称
                public String project_name;
                @SerializedName("user_name") //加入人员姓名
                public String user_name;
                @SerializedName("birthday")
                public String birthday;
                @SerializedName("id_num")
                public String id_num;
                @SerializedName("statusName")
                public String statusName;

                @Override
                public String toString() {
                    return "FamilyBean{" +
                            "APPLY_DATE='" + APPLY_DATE + '\'' +
                            ", JOIN_AMOUNT='" + JOIN_AMOUNT + '\'' +
                            ", PERIOD_TPYE='" + PERIOD_TPYE + '\'' +
                            ", PLAN_PERIOD='" + PLAN_PERIOD + '\'' +
                            ", STATUS='" + STATUS + '\'' +
                            ", TAG='" + TAG + '\'' +
                            ", TAG_ID='" + TAG_ID + '\'' +
                            ", waitTime='" + waitTime + '\'' +
                            ", joinUserId='" + joinUserId + '\'' +
                            ", msg='" + msg + '\'' +
                            ", projectId='" + projectId + '\'' +
                            ", project_name='" + project_name + '\'' +
                            ", user_name='" + user_name + '\'' +
                            ", birthday='" + birthday + '\'' +
                            ", id_num='" + id_num + '\'' +
                            ", statusName='" + statusName + '\'' +
                            '}';
                }
            }
        }
    }
}
