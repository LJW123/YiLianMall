package com.leshan.ylyj.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import rxfamily.entity.WelfarePersonInfoEntity;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.GlideUtil;


import java.util.List;

/**
 *
 * @author ZYH
 * @date 2017/12/20 0020
 */

public class DonationAdapter extends BaseQuickAdapter<WelfarePersonInfoEntity.Data.Condition, BaseViewHolder> {
    //占位标记
    private boolean flagOccupy=true;

    public DonationAdapter(List data) {
        super(R.layout.item_donatinon_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WelfarePersonInfoEntity.Data.Condition item) {
        ImageView imageView=helper.getView(R.id.icon_iv);
        if (flagOccupy){
            int position=helper.getLayoutPosition();
            setOccupyIv(imageView,position);
        }else {
            GlideUtil.showImage(mContext,item.mainImgUrl,imageView);
            helper.setText(R.id.title_tv, item.name);
        }
    }
    private void setOccupyIv(ImageView iv,int position){
        int resouceId=0;
        switch (position){
            case 0:
                resouceId=R.mipmap.hear1_greay;
                break;
            case 1:
                resouceId=R.mipmap.heart2_gray;
                break;
            case 2:
                resouceId=R.mipmap.heart3_gray;
                break;
            case 3:
                resouceId=R.mipmap.heart4_gray;
                break;
            case 4:
                resouceId=R.mipmap.heart5_gray;
                break;
            case 5:
                resouceId=R.mipmap.heart6_gray;
                break;
        }
        iv.setImageResource(resouceId);
    }
    public void setFlag(boolean flag){
        this.flagOccupy=flag;
    }
}
