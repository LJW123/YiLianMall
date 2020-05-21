package rxfamily.entity;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.BaseEntity;

import java.util.ArrayList;



/**
 * Created by Ray_L_Pain on 2017/8/9 0009.
 */

public class BankEntity extends BaseEntity {

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean implements IPickerViewData {
        /**
         * bank_id : 招商银行
         * bank_name : 招商银行
         */

        @SerializedName("bank_id")
        public String bankId;
        @SerializedName("bank_name")
        public String bankName;

        @Override
        public String getPickerViewText() {
            return bankName;
        }
    }
}
