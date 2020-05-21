package com.yilian.mall.jd.dialog;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.jd.adapter.JDLogisticsPickAdapter;
import com.yilian.networkingmodule.entity.ExpressListEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 输入物流信息
 * Created by Zg on 2018/5/29.
 */
public class InputLogisticsPopwindow extends PopupWindow {
    private Context mContext;
    private View view;
    private TextView tv_select_logistics;
    private EditText et_logistics_numbers;
    private TextView tv_cancel, tv_submit;
    //物流选择
    private RecyclerView logistics_pick;
    private JDLogisticsPickAdapter mAdapter;
    private boolean logisticsPickShow = false;

    public InputLogisticsPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.jd_popupwindow_input_logistics, null);
        this.mContext = mContext;


        tv_select_logistics = view.findViewById(R.id.tv_select_logistics);
        et_logistics_numbers = view.findViewById(R.id.et_logistics_numbers);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_submit = view.findViewById(R.id.tv_submit);
        logistics_pick = view.findViewById(R.id.logistics_pick);
        logistics_pick.setVisibility(View.GONE);
        logistics_pick.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new JDLogisticsPickAdapter();
        logistics_pick.setAdapter(mAdapter);
        mAdapter.setNewData(getLogisticsList());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String str = (String) adapter.getItem(position);
                tv_select_logistics.setText(str);
                logisticsPickShow = false;
                logistics_pick.setVisibility(View.GONE);
            }
        });

        tv_select_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logisticsPickShow) {
                    logisticsPickShow = false;
                    logistics_pick.setVisibility(View.GONE);
                } else {
                    logisticsPickShow = true;
                    logistics_pick.setVisibility(View.VISIBLE);
                }
            }
        });

         /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private List<String> getLogisticsList() {
        List<String> list = new ArrayList<>();
        list.add("圆通快递");
        list.add("申通快递");
        list.add("韵达快递");
        list.add("中通快递");
        list.add("宅急送");
        list.add("EMS");
        list.add("顺丰快递");
        return list;
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

    public void showPop(View v) {
        showAtLocation(v, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.3f);
    }

    public String getLogistics() {
        if (TextUtils.isEmpty(tv_select_logistics.getText().toString())) {
            return "";
        } else {
            return tv_select_logistics.getText().toString();
        }
    }

//    public void selectLogistics(View.OnClickListener listener) {
//        tv_select_logistics.setOnClickListener(listener);
//    }

    public void setLogistics(String str) {
        tv_select_logistics.setText(str);
    }

    public String getLogisticsNumbers() {
        if (TextUtils.isEmpty(et_logistics_numbers.getText().toString())) {
            return "";
        } else {
            return et_logistics_numbers.getText().toString();
        }
    }

    public void cancel(View.OnClickListener listener) {
        tv_cancel.setOnClickListener(listener);
    }

    public void submit(View.OnClickListener listener) {
        tv_submit.setOnClickListener(listener);
    }

//    /**
//     * 弹出快递选择器
//     */
//    private void showExpressListPickerView() {
//        List<ExpressListEntity.ListBean> expressList = new ArrayList<>();
//        ExpressListEntity.ListBean bean = new ExpressListEntity.ListBean();
//        bean.expressName = ;
//        expressList.add(bean);
//        bean = new ExpressListEntity.ListBean();
//        bean.expressName = "";
//        expressList.add(bean);
//        bean = new ExpressListEntity.ListBean();
//        bean.expressName = "";
//        expressList.add(bean);
//        bean = new ExpressListEntity.ListBean();
//        bean.expressName = "";
//        expressList.add(bean);
//        bean = new ExpressListEntity.ListBean();
//        bean.expressName = "";
//        expressList.add(bean);
//        bean = new ExpressListEntity.ListBean();
//        bean.expressName = "";
//        expressList.add(bean);
//        bean = new ExpressListEntity.ListBean();
//        bean.expressName = "";
//        expressList.add(bean);
//
//        final OptionsPickerView expressOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                ExpressListEntity.ListBean expressBean = expressList.get(options1);
//               setLogistics(expressBean.expressName);
//            }
//        }).setTitleText("快递选择").setContentTextSize(20)
//                .build();
//        expressOptions.setPicker(expressList);
//        expressOptions.show();
//    }
}