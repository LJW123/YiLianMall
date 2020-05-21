package com.yilian.mall.ctrip.widget.cosmocalendar.selection;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.ctrip.widget.cosmocalendar.utils.DateUtils;

public class RangeSelectionManager extends BaseSelectionManager {

    private Pair<Day, Day> days;
    private Day tempDay;
    private int maxRange = 29;

    public RangeSelectionManager(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public Pair<Day, Day> getDays() {
        return days;
    }

    @Override
    public void toggleDay(@NonNull Day day) {
        if (days == null && tempDay == null || tempDay == null) {
            tempDay = day;
            days = null;
        } else {
            if (tempDay == day) {
                return;
            }
//            修改选中日期逻辑，
// 两次点击区间为选中日期 改为：第二次点击在第一次点击之后，且第二次点击和第一次点击最多间隔29天 那么点击区间才为选中日期
            if (tempDay.getCalendar().getTime().before(day.getCalendar().getTime())) {
                long l = (day.getCalendar().getTimeInMillis() - tempDay.getCalendar().getTimeInMillis()) / (24 * 60 * 60 * 1000);
                if (l <= getMaxRange()) {
                    days = Pair.create(tempDay, day);
                    tempDay = null;
                } else {
                    tempDay = day;
                }

            } else {
                tempDay = day;
            }
        }
        onDaySelectedListener.onDaySelected();
    }

    @Override
    public boolean isDaySelected(@NonNull Day day) {
        return isDaySelectedManually(day);
    }

    private boolean isDaySelectedManually(@NonNull Day day) {
        if (tempDay != null) {
            return day.equals(tempDay);
        } else if (days != null) {
            return DateUtils.isDayInRange(day, days.first, days.second);
        } else {
            return false;
        }
    }

    @Override
    public void clearSelections() {
        days = null;
        tempDay = null;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public SelectionState getSelectedState(Day day) {
        if (!isDaySelectedManually(day)) {
            return SelectionState.SINGLE_DAY;
        }

        if (days == null) {
            return SelectionState.START_RANGE_DAY_WITHOUT_END;
        } else if (days.first.equals(day)) {
            return SelectionState.START_RANGE_DAY;
        } else if (days.second.equals(day)) {
            return SelectionState.END_RANGE_DAY;
        } else if (DateUtils.isDayInRange(day, days.first, days.second)) {
            return SelectionState.RANGE_DAY;
        } else {
            return SelectionState.SINGLE_DAY;
        }
    }
}
