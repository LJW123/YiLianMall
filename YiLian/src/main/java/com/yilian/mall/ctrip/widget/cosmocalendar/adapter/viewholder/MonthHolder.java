package com.yilian.mall.ctrip.widget.cosmocalendar.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.widget.cosmocalendar.adapter.DaysAdapter;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Month;
import com.yilian.mall.ctrip.widget.cosmocalendar.settings.SettingsManager;
import com.yilian.mall.ctrip.widget.cosmocalendar.view.MonthView;

public class MonthHolder extends RecyclerView.ViewHolder {

    private LinearLayout llMonthHeader;
    private TextView tvMonthName;
    private View viewLeftLine;
    private View viewRightLine;
    private MonthView monthView;
    private SettingsManager appearanceModel;

    public MonthHolder(View itemView, SettingsManager appearanceModel) {
        super(itemView);
        llMonthHeader = (LinearLayout) itemView.findViewById(R.id.ll_month_header);
        monthView = (MonthView) itemView.findViewById(R.id.month_view);
        tvMonthName = (TextView) itemView.findViewById(R.id.tv_month_name);
        viewLeftLine = itemView.findViewById(R.id.view_left_line);
        viewRightLine = itemView.findViewById(R.id.view_right_line);
        this.appearanceModel = appearanceModel;
    }

    public void setDayAdapter(DaysAdapter adapter) {
        getMonthView().setAdapter(adapter);
    }

    public MonthView getMonthView() {
        return monthView;
    }

    public void bind(Month month) {
//        月份title文字
        tvMonthName.setText(month.getMonthName());
        tvMonthName.setTextColor(appearanceModel.getMonthTextColor());
//隐藏两条横线
//        viewLeftLine.setVisibility(appearanceModel.getCalendarOrientation() == OrientationHelper.HORIZONTAL ? View.INVISIBLE : View.VISIBLE);
//        viewRightLine.setVisibility(appearanceModel.getCalendarOrientation() == OrientationHelper.HORIZONTAL ? View.INVISIBLE : View.VISIBLE);
        viewLeftLine.setVisibility(View.INVISIBLE);
        viewRightLine.setVisibility(View.INVISIBLE);
//        设置月份title背景色
        llMonthHeader.setBackgroundResource(R.drawable.bg_month_title);
        monthView.initAdapter(month);
    }
}
