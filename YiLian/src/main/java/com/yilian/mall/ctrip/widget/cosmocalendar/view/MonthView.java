package com.yilian.mall.ctrip.widget.cosmocalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yilian.mall.ctrip.widget.cosmocalendar.adapter.DaysAdapter;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Month;
import com.yilian.mall.ctrip.widget.cosmocalendar.utils.Constants;

public class MonthView extends FrameLayout {

    private RecyclerView rvDays;

    public MonthView(@NonNull Context context) {
        super(context);
        init();
    }

    public MonthView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthView(@NonNull Context context,
                     @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MonthView(@NonNull Context context,
                     @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        rvDays = new RecyclerView(getContext());
        rvDays.setHasFixedSize(true);
        rvDays.setNestedScrollingEnabled(false);
        rvDays.setLayoutParams(generateLayoutParams());
        final GridLayoutManager manager = new GridLayoutManager(getContext(), Constants.DAYS_IN_WEEK);
        rvDays.setLayoutManager(manager);
        addView(rvDays);
    }

    public void setAdapter(DaysAdapter adapter) {
        rvDays.setAdapter(adapter);
    }

    public DaysAdapter getAdapter() {
        return (DaysAdapter) rvDays.getAdapter();
    }

    public void initAdapter(Month month) {
        getAdapter().setMonth(month);
    }

    private LayoutParams generateLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
