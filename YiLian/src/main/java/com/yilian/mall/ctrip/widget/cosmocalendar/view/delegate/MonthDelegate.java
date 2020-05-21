package com.yilian.mall.ctrip.widget.cosmocalendar.view.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.widget.cosmocalendar.adapter.DaysAdapter;
import com.yilian.mall.ctrip.widget.cosmocalendar.adapter.viewholder.MonthHolder;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Month;
import com.yilian.mall.ctrip.widget.cosmocalendar.settings.SettingsManager;

public class MonthDelegate {

    private SettingsManager appearanceModel;

    public MonthDelegate(SettingsManager appearanceModel) {
        this.appearanceModel = appearanceModel;
    }

    public MonthHolder onCreateMonthHolder(DaysAdapter adapter, ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.view_month_item, parent, false);
        final MonthHolder holder = new MonthHolder(view, appearanceModel);
        holder.setDayAdapter(adapter);
        return holder;
    }

    public void onBindMonthHolder(Month month, MonthHolder holder, int position) {
        holder.bind(month);
    }
}
