package com.yilian.mall.ctrip.widget.cosmocalendar.selection.criteria.month;


import java.util.Calendar;

import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.criteria.BaseCriteria;

public abstract class BaseMonthCriteria extends BaseCriteria {

    protected abstract int getMonth();

    protected abstract int getYear();

    @Override
    public boolean isCriteriaPassed(Day day) {
        return day.getCalendar().get(Calendar.MONTH) == getMonth()
                && day.getCalendar().get(Calendar.YEAR) == getYear();
    }
}
