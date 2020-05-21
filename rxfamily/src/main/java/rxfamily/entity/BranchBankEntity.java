package rxfamily.entity;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;

/**
 * @author Created by  on 2018/1/23.
 */

public class BranchBankEntity extends BaseEntity {
    @SerializedName("list")

    public ArrayList<ListBean> list;

    public static class ListBean {
        public ListBean(String branchName) {
            this.branchName = branchName;
        }

        /**
         * branch_name : 北京北蜂窝路支行
         */

        @SerializedName("branch_name")
        public String branchName;

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ListBean)) {
                return false;
            }

            ListBean listBean = (ListBean) obj;

            return branchName != null ? branchName.equals(listBean.branchName) : listBean.branchName == null;
        }

        @Override
        public int hashCode() {
            return branchName != null ? branchName.hashCode() : 0;
        }
    }


}
