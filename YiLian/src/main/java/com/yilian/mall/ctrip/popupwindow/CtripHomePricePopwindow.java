package com.yilian.mall.ctrip.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.yilian.mall.ctrip.activity.ActivityFilterThird;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.networkingmodule.entity.SearchFilterBean;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripPriceModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 价格星级 筛选
 * Created by Zg on 2018/9/11.
 */
public class CtripHomePricePopwindow extends PopupWindow implements MyItemClickListener {
    String starlevel = "";
    private Context mContext;
    private ActivityFilterThird thirdView;
    private View view;

    public CtripHomePricePopwindow(final Context mContext, List<SearchFilterBean.PSortBean> pSortBeans) {
        this.thirdView = new ActivityFilterThird(mContext, pSortBeans,null);
        this.view = thirdView.thirdView();
        this.mContext = mContext;

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        this.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        thirdView.setListener(this);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 弹出对话框
     *
     * @param v
     */
    public void showPop(View v) {
        showAtLocation(v, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.3f);
    }

    @Override
    public void onTwoClick(String title, String distance, String zoneid, String locationid, String key) {

    }


    @Override
    public void onOneClick(View view, int position, String title, String sorts, int page,String keyWord) {

    }

    @Override
    public void onFourClick(String themeTitle, String themeId,String brandid,String facilityid,String bedid,String grade,String keyWord) {

    }

    //   星级价格筛选结果回调
    @Override
    public void onThereClick(boolean priceIsCheck,String minprice, String maxprice, ArrayList<String> startlevelList, String showStr) {
        starlevel = startlevelList.toString();
        starlevel = starlevel.replace("[", "").trim();
        starlevel = starlevel.replace("]", "").trim();
        /**
         * {@link CtripHomePageActivity }
         */
        EventBus.getDefault().post(new CtripPriceModel(minprice, maxprice, startlevelList, showStr));
        dismiss();
    }

    public void resetContent(){
        thirdView.resetContent();
    }
}