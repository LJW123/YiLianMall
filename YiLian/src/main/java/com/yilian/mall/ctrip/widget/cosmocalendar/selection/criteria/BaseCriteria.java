package com.yilian.mall.ctrip.widget.cosmocalendar.selection.criteria;


import java.util.Set;

import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;

public abstract class BaseCriteria {

    protected Set<Integer> weekendDays;
    protected Set<Integer> disabledDays;

    public abstract boolean isCriteriaPassed(Day day);

    public void setWeekendDays(Set<Integer> weekendDays) {
        this.weekendDays = weekendDays;
    }

    public void setDisabledDays(Set<Integer> disabledDays) {
        this.disabledDays = disabledDays;
    }
}
