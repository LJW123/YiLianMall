package com.yilian.mall.ctrip.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.RxDeviceTool;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripCommitOrderPriceParticularsAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 创建订单 价格明细
 * Created by Zg on 2018/10/24.
 */
public class CtripCommitOrderPriceParticularsPopwindow extends PopupWindow {
    private Context mContext;
    private View view;
    private TextView tv_describe, tv_total_price;
    private TextView tvPrice, tvReturnBean, tvDetail, tvCommit;

    private RecyclerView recyclerView;
    private CtripCommitOrderPriceParticularsAdapter mAdapter;


    public CtripCommitOrderPriceParticularsPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.ctrip_popupwindow_commit_order_price_particulars, null);
        this.mContext = mContext;
        /* 初始化view */
        initView();
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        DisplayMetrics dm = RxDeviceTool.getDisplayMetrics(mContext);
        this.setHeight((int) (dm.heightPixels * 0.4));
//        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void initView() {
        tv_describe = view.findViewById(R.id.tv_describe);
        tv_total_price = view.findViewById(R.id.tv_total_price);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CtripCommitOrderPriceParticularsAdapter();
        mAdapter.bindToRecyclerView(recyclerView);

        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvReturnBean = (TextView) view.findViewById(R.id.tv_return_bean);
        tvDetail = (TextView) view.findViewById(R.id.tv_detail);
        tvCommit = (TextView) view.findViewById(R.id.tv_commit);

        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
        showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.3f);
    }

    /**
     * 设置内容
     *
     * @param checkIn          入住时间 yyyy-MM-dd
     * @param dateArea         入住晚数
     * @param mSelectRoomCount 所选间数
     * @param totalPrice       总价
     * @param returnBean       益豆
     * @param unitPrice        单价
     */
    public boolean setContent(String checkIn, int dateArea, int mSelectRoomCount, String totalPrice, String returnBean, String unitPrice) {
        this.tv_describe.setText(String.format("%s晚，%s间共", dateArea, mSelectRoomCount));
        this.tv_total_price.setText(String.format("¥%s", totalPrice));
        this.tvPrice.setText(totalPrice);
        this.tvReturnBean.setText(String.format("+%s", returnBean));

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfMD = new SimpleDateFormat("MM-dd");

        try {
            List<String> list = new ArrayList<>();
            for (int i = 0; i <= dateArea; i++) {
                Date in = sdfYMD.parse(checkIn);
                Calendar checkInCalendar = Calendar.getInstance();
                checkInCalendar.setTime(in);
                checkInCalendar.add(Calendar.DAY_OF_MONTH, i);
                String data = sdfMD.format(checkInCalendar.getTime());
                list.add(data);
            }
            mAdapter.setParameter(list.size(),mSelectRoomCount,unitPrice);
            mAdapter.setNewData(list);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 提交订单
     *
     * @param listener 点击事件
     */
    public void setCommit(View.OnClickListener listener) {
        tvCommit.setOnClickListener(listener);
    }
}