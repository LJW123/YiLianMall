package com.yilian.mall.ctrip.widget.cosmocalendar.settings.selection;

import com.yilian.mall.ctrip.widget.cosmocalendar.utils.SelectionType;

public interface SelectionInterface {

    @SelectionType
    int getSelectionType();

    void setSelectionType(@SelectionType int selectionType);
}
