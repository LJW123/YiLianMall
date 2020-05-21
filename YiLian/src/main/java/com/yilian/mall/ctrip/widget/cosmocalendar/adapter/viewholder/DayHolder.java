package com.yilian.mall.ctrip.widget.cosmocalendar.adapter.viewholder;

import android.content.res.Resources;
import android.view.View;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.BaseSelectionManager;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.RangeSelectionManager;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.SelectionState;
import com.yilian.mall.ctrip.widget.cosmocalendar.settings.appearance.ConnectedDayIconPosition;
import com.yilian.mall.ctrip.widget.cosmocalendar.utils.CalendarUtils;
import com.yilian.mall.ctrip.widget.cosmocalendar.view.CalendarView;
import com.yilian.mall.ctrip.widget.cosmocalendar.view.customviews.CircleAnimationTextView;

import java.util.Calendar;

//import com.yilian.mall.ctrip.weidget.cosmocalendar.view.customviews.CircleAnimationTextView;

public class DayHolder extends BaseDayHolder {

    private CircleAnimationTextView ctvDay;
    private BaseSelectionManager selectionManager;

    public DayHolder(View itemView, CalendarView calendarView) {
        super(itemView, calendarView);
        ctvDay = (CircleAnimationTextView) itemView.findViewById(R.id.tv_day_number);
    }

    public void bind(Day day, BaseSelectionManager selectionManager) {
        this.selectionManager = selectionManager;
        ctvDay.setText(String.valueOf(day.getDayNumber()));

        boolean isSelected = selectionManager.isDaySelected(day);
        if (isSelected && !day.isDisabled()) {
//            选中
            select(day);
        } else {
//            非选中
            unselect(day);
        }
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE,-1);
        if (day.getCalendar().compareTo(tomorrow) < 0) {
//            已过去的日期 不可点
            day.setDisabled(true);
        } else {
            day.setDisabled(false);
        }
        if (day.isCurrent()) {
            ctvDay.setText("今天");
            addCurrentDayIcon(isSelected);
        }

        if (day.isDisabled()) {
            ctvDay.setTextColor(calendarView.getDisabledDayTextColor());
        }
    }

    private void addCurrentDayIcon(boolean isSelected) {
        ctvDay.setCompoundDrawablePadding(getPadding(getCurrentDayIconHeight(isSelected)) * -1);
        ctvDay.setCompoundDrawablesWithIntrinsicBounds(0, isSelected
                ? calendarView.getCurrentDaySelectedIconRes()
                : calendarView.getCurrentDayIconRes(), 0, 0);
    }

    private int getCurrentDayIconHeight(boolean isSelected) {
        if (isSelected) {
            return CalendarUtils.getIconHeight(calendarView.getContext().getResources(), calendarView.getCurrentDaySelectedIconRes());
        } else {
            return CalendarUtils.getIconHeight(calendarView.getContext().getResources(), calendarView.getCurrentDayIconRes());
        }
    }

    private int getConnectedDayIconHeight(boolean isSelected) {
        if (isSelected) {
            return CalendarUtils.getIconHeight(calendarView.getContext().getResources(), calendarView.getConnectedDaySelectedIconRes());
        } else {
            return CalendarUtils.getIconHeight(calendarView.getContext().getResources(), calendarView.getConnectedDayIconRes());
        }
    }

    private void select(Day day) {
        if (day.isFromConnectedCalendar()) {
            if (day.isDisabled()) {
                ctvDay.setTextColor(day.getConnectedDaysDisabledTextColor());
            } else {
                ctvDay.setTextColor(day.getConnectedDaysSelectedTextColor());
            }
            addConnectedDayIcon(true);
        } else {
            ctvDay.setTextColor(calendarView.getSelectedDayTextColor());
            ctvDay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        SelectionState state;
        if (selectionManager instanceof RangeSelectionManager) {
            state = ((RangeSelectionManager) selectionManager).getSelectedState(day);
        } else {
            state = SelectionState.SINGLE_DAY;
        }
        animateDay(state, day);
    }

    private void addConnectedDayIcon(boolean isSelected) {
        ctvDay.setCompoundDrawablePadding(getPadding(getConnectedDayIconHeight(isSelected)) * -1);

        switch (calendarView.getConnectedDayIconPosition()) {
            case ConnectedDayIconPosition.TOP:
                ctvDay.setCompoundDrawablesWithIntrinsicBounds(0, isSelected
                        ? calendarView.getConnectedDaySelectedIconRes()
                        : calendarView.getConnectedDayIconRes(), 0, 0);
                break;

            case ConnectedDayIconPosition.BOTTOM:
                ctvDay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, isSelected
                        ? calendarView.getConnectedDaySelectedIconRes()
                        : calendarView.getConnectedDayIconRes());
                break;
            default:
                break;
        }
    }

    private void animateDay(SelectionState state, Day day) {
        if (day.getSelectionState() != state) {
            if (day.isSelectionCircleDrawed() && state == SelectionState.SINGLE_DAY) {
                ctvDay.showAsSingleCircle(calendarView);
            } else if (day.isSelectionCircleDrawed() && state == SelectionState.START_RANGE_DAY) {
                ctvDay.showAsStartCircle(calendarView, false);
            } else if (day.isSelectionCircleDrawed() && state == SelectionState.END_RANGE_DAY) {
                ctvDay.showAsEndCircle(calendarView, false);
            } else {
                ctvDay.setSelectionStateAndAnimate(state, calendarView, day);
            }
        } else {
            switch (state) {
                case SINGLE_DAY:
                    if (day.isSelectionCircleDrawed()) {
                        ctvDay.showAsSingleCircle(calendarView);
                    } else {
                        ctvDay.setSelectionStateAndAnimate(state, calendarView, day);
                    }
                    break;

                case RANGE_DAY:
                    ctvDay.setSelectionStateAndAnimate(state, calendarView, day);
                    break;

                case START_RANGE_DAY_WITHOUT_END:
                    if (day.isSelectionCircleDrawed()) {
                        ctvDay.showAsStartCircleWithouEnd(calendarView, false);
                    } else {
                        ctvDay.setSelectionStateAndAnimate(state, calendarView, day);
                    }
                    break;

                case START_RANGE_DAY:
                    if (day.isSelectionCircleDrawed()) {
                        ctvDay.showAsStartCircle(calendarView, false);
                    } else {
                        ctvDay.setSelectionStateAndAnimate(state, calendarView, day);
                    }
                    break;

                case END_RANGE_DAY:
                    if (day.isSelectionCircleDrawed()) {
                        ctvDay.showAsEndCircle(calendarView, false);
                    } else {
                        ctvDay.setSelectionStateAndAnimate(state, calendarView, day);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void unselect(Day day) {
        int textColor;
        if (day.isFromConnectedCalendar()) {
            if (day.isDisabled()) {
                textColor = day.getConnectedDaysDisabledTextColor();
            } else {
                textColor = day.getConnectedDaysTextColor();
            }
            addConnectedDayIcon(false);
        } else if (day.isWeekend()) {
            textColor = calendarView.getWeekendDayTextColor();
            ctvDay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            textColor = calendarView.getDayTextColor();
            ctvDay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        day.setSelectionCircleDrawed(false);
        ctvDay.setTextColor(textColor);
        ctvDay.clearView();
    }

    private int getPadding(int iconHeight) {
        return (int) (iconHeight * Resources.getSystem().getDisplayMetrics().density);
    }
}
