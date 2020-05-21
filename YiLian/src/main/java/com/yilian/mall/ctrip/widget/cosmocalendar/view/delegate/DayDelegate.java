package com.yilian.mall.ctrip.widget.cosmocalendar.view.delegate;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.widget.cosmocalendar.adapter.MonthAdapter;
import com.yilian.mall.ctrip.widget.cosmocalendar.adapter.viewholder.DayHolder;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.BaseSelectionManager;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.MultipleSelectionManager;
import com.yilian.mall.ctrip.widget.cosmocalendar.view.CalendarView;
import com.yilian.mall.ctrip.widget.cosmocalendar.view.customviews.CircleAnimationTextView;

public class DayDelegate extends BaseDelegate {

    private MonthAdapter monthAdapter;

    public DayDelegate(CalendarView calendarView, MonthAdapter monthAdapter) {
        this.calendarView = calendarView;
        this.monthAdapter = monthAdapter;
    }

    public DayHolder onCreateDayHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_day, parent, false);
        return new DayHolder(view, calendarView);
    }

    public void onBindDayHolder(final RecyclerView.Adapter daysAdapter, final Day day,
                                final DayHolder holder, final int position) {
        final BaseSelectionManager selectionManager = monthAdapter.getSelectionManager();
        holder.bind(day, selectionManager);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleAnimationTextView textView = holder.itemView.findViewById(R.id.tv_day_number);
                Log.i("点击了", textView.getText().toString().trim());
                if (!day.isDisabled()) {
                    selectionManager.toggleDay(day);
                    if (selectionManager instanceof MultipleSelectionManager) {
                        daysAdapter.notifyItemChanged(position);
                    } else {
                        monthAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
