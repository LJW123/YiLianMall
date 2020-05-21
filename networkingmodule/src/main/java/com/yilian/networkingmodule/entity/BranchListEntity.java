package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/10/20 0020.
 */

public class BranchListEntity extends HttpResultBean {

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean {
        public ListBean(String branchName) {
            this.branchName = branchName;
        }

        @SerializedName("branch_name")
        public String branchName;

        @Override
        public String toString() {
            return "ListBean{" +
                    "branchName='" + branchName + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ListBean)) {
                return false;
            }

            ListBean listBean = (ListBean) o;

            return branchName != null ? branchName.equals(listBean.branchName) : listBean.branchName == null;

        }

        @Override
        public int hashCode() {
            return branchName != null ? branchName.hashCode() : 0;
        }
    }


}
