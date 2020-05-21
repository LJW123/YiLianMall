package com.yilian.mall.ctrip.widget.cosmocalendar.settings.lists;


import java.util.Set;

import com.yilian.mall.ctrip.widget.cosmocalendar.settings.lists.connected_days.ConnectedDays;
import com.yilian.mall.ctrip.widget.cosmocalendar.settings.lists.connected_days.ConnectedDaysManager;

//import com.yilian.mall.ctrip.weidget.cosmocalendar.settings.lists.connected_days.ConnectedDays;
public interface CalendarListsInterface {

    Set<Long> getDisabledDays();

    ConnectedDaysManager getConnectedDaysManager();

    Set<Long> getWeekendDays();

    DisabledDaysCriteria getDisabledDaysCriteria();

    void setDisabledDays(Set<Long> disabledDays);

    void setWeekendDays(Set<Long> weekendDays);

    void setDisabledDaysCriteria(DisabledDaysCriteria criteria);

    void addConnectedDays(ConnectedDays connectedDays);
}
