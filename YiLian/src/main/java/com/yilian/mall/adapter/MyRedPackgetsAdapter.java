package com.yilian.mall.adapter;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.utils.DistanceUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.StealMyRedpackgets;

/**
 * Created by ASUS on 2017/12/2 0002.
 */

public class MyRedPackgetsAdapter extends BaseQuickAdapter<StealMyRedpackgets.Data.RedDetails, BaseViewHolder> {
    private boolean isHasHeader = false;
    private int myRedSizes = 0;

    public MyRedPackgetsAdapter(int layoutResId, boolean isHasHeader) {
        super(layoutResId);
        this.isHasHeader = isHasHeader;
    }

    //我的奖励的集合长度
    public void setDateTypes(int myRedSizes) {
        this.myRedSizes = myRedSizes;
    }

    @Override
    protected void convert(BaseViewHolder helper, StealMyRedpackgets.Data.RedDetails item) {
        //获取当前的item的位置
        int currtPostion = helper.getLayoutPosition();
        if (null != item) {
            GlideUtil.showImageWithSuffix(mContext, item.merchantImage, helper.getView(R.id.shop_imge));
            if (!TextUtils.isEmpty(item.distance)){
                if (Double.parseDouble(item.distance)>=1000){
                    double distance=Double.parseDouble(item.distance)/1000;
                    helper.setText(R.id.instance, distance+"km");
                }else {
                    helper.setText(R.id.instance, item.distance+"m");
                }
            }else {
                helper.setText(R.id.instance, "计算距离失败");
            }
            //姓名
            helper.setText(R.id.name, item.merchantName);
            if (item instanceof StealMyRedpackgets.Data.MoreRedDetails) {
                if (currtPostion == myRedSizes + 1) {
                    helper.getView(R.id.dv_other_red).setVisibility(View.VISIBLE);
                }
                helper.getView(R.id.rl_counts).setVisibility(View.GONE);
            } else {
                helper.setText(R.id.redpackgers_counts, "x" + item.count);
            }
        }

    }

    private void setDistace(BaseViewHolder helper, StealMyRedpackgets.Data.RedDetails item) {
        double lat_d = MyApplication.getInstance().getLatitude();
        double log_d = MyApplication.getInstance().getLongitude();
        String lat = item.merchantLatitude;
        String log = item.merchantLongitude;
        if (log == null || lat == null) {
            helper.setText(R.id.instance, "计算距离失败");
        } else {
            int dis = (int) DistanceUtil.getDistanceOfMeter(Double.parseDouble(lat), Double.parseDouble(log), lat_d, log_d);
            if (dis < 1000) {
                helper.setText(R.id.instance, dis + "m");
            } else {
                helper.setText(R.id.instance, dis / 1000 + "km");
            }
        }
    }


}
