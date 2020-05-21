package com.yilian.mall.adapter;

import android.widget.EditText;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ProfessionEntity;

import java.util.List;

/**
 * @author Created by  on 2018/1/16.
 */

public class ProfessionMultiAdapter extends BaseMultiItemQuickAdapter<ProfessionEntity.DataBean, BaseViewHolder> {


    public int editTextPosition;
    public EditText etProfession;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ProfessionMultiAdapter(List data) {
        super(data);
        addItemType(ProfessionEntity.TEXT_TYPE, R.layout.item_profession_text);
        addItemType(ProfessionEntity.EDIT_TYPE, R.layout.item_profession_edit);
    }


    @Override
    protected void convert(BaseViewHolder helper, ProfessionEntity.DataBean item) {
        GlideUtil.showImage(mContext, item.tagPic, helper.getView(R.id.iv_profession_icon));
        int itemViewType = helper.getItemViewType();
        Logger.i("itemViewType:" + itemViewType);
        switch (itemViewType) {
            case ProfessionEntity.TEXT_TYPE:
                Logger.i("行业走了这里1");
                helper.setText(R.id.tv_profession, item.name);
                break;
            case ProfessionEntity.EDIT_TYPE:
                Logger.i("行业走了这里2");
                ((EditText) helper.getView(R.id.et_profession)).setHint("可手动输入其他行业");
                editTextPosition = helper.getLayoutPosition();
                etProfession = helper.getView(R.id.et_profession);
                break;
            default:
                break;
        }
    }
}
