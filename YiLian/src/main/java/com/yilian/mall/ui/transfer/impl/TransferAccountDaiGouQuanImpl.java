package com.yilian.mall.ui.transfer.impl;

import android.content.Context;

import com.yilian.mall.ui.transfer.Donee;
import com.yilian.mall.ui.transfer.inter.ITransferAccount;
import com.yilian.networkingmodule.entity.ContactSperson;
import com.yilian.networkingmodule.entity.MemberGiftEntity1;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author xiaofo on 2018/10/10.
 */

public class TransferAccountDaiGouQuanImpl implements ITransferAccount {

    @Override
    public TransferDaiGouQUanCategoryImpl getTransferCategory() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<ArrayList<Donee>> getTransferAccountList(Context context) {

        return RetrofitUtils3.getRetrofitService(context)
                .getContactsPerson("quan/getContractsQuan")
                .map(new Func1<ContactSperson, ArrayList<Donee>>() {
                    @Override
                    public ArrayList<Donee> call(ContactSperson contactSperson) {
                        ArrayList<Donee> transferAccountBeans = new ArrayList<Donee>();
                        for (ContactSperson.Data datum : contactSperson.data) {
                            transferAccountBeans.add(new Donee(datum.userid, datum.name, datum.phone, datum.photo));
                        }
                        return transferAccountBeans;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<MemberGiftEntity1> checkTransferAccount(Context context, String... strings) {

        return RetrofitUtils3.getRetrofitService(context)
                .getGiveManInfo("user_info1", strings[0])
                ;
    }

    @Override
    public String getTransferRemind() {
        return "注：兑换券将实时转入对方账户，无法退回，请仔细核对。";
    }

    @Override
    public String getTitle() {
        return "兑换券转赠";
    }
}
