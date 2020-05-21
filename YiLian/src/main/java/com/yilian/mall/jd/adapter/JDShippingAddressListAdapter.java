package com.yilian.mall.jd.adapter;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.jd.utils.JDDetailAddressUtil;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;

import static com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity.DataBean.DEFAULT_JD_ADDRESS;

/**
 * @author Created by  on 2018/5/26.
 */

public class JDShippingAddressListAdapter extends BaseQuickAdapter<JDShippingAddressInfoEntity.DataBean, BaseViewHolder> {

    public JDShippingAddressListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JDShippingAddressInfoEntity.DataBean data) {
        helper.setText(R.id.tv_jd_name_phone, data.name + "    " + PhoneUtil.formatPhoneMiddle4Asterisk(data.mobile));
        helper.setText(R.id.tv_jd_address, JDDetailAddressUtil.getJDShippingDetailAddressStr(data.province,data.city,
                data.county,data.town,data.detailAddress));
        helper.setChecked(R.id.check_box_jd_default_address, data.defaultAddress == 1);
        helper.setText(R.id.check_box_jd_default_address, data.defaultAddress == 1 ? "默认地址" : "设为默认");
        helper.addOnClickListener(R.id.tv_edit_jd_address);
        helper.addOnClickListener(R.id.tv_delete_jd_address);
        helper.addOnClickListener(R.id.check_box_jd_default_address);
        CheckBox view = helper.getView(R.id.check_box_jd_default_address);

        view.setButtonDrawable(data.defaultAddress == DEFAULT_JD_ADDRESS ?
                R.mipmap.icon_jd_check_default_address : R.mipmap.icon_jd_check_undefault_address);
    }


}
