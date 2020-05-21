package com.yilian.mall.ctrip.widget.cosmocalendar.selection;

import android.support.annotation.NonNull;

import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;

/**
 * Created by leonardo2204 on 06/10/17.
 */

public class NoneSelectionManager extends BaseSelectionManager {

    @Override
    public void toggleDay(@NonNull Day day) {

    }

    @Override
    public boolean isDaySelected(@NonNull Day day) {
        return false;
    }

    @Override
    public void clearSelections() {

    }
}
