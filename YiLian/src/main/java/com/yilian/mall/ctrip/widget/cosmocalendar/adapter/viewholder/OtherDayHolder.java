package com.yilian.mall.ctrip.widget.cosmocalendar.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.ctrip.widget.cosmocalendar.view.CalendarView;

public class OtherDayHolder extends BaseDayHolder {

    public OtherDayHolder(View itemView, CalendarView calendarView) {
        super(itemView, calendarView);
        tvDay = (TextView) itemView.findViewById(R.id.tv_day_number);
    }

    public void bind(Day day) {
//        非本月日期不可见
        tvDay.setVisibility(View.INVISIBLE);
        tvDay.setText(String.valueOf(day.getDayNumber()));
        tvDay.setTextColor(calendarView.getOtherDayTextColor());
    }
}
