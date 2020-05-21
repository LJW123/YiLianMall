package com.yilian.mall.ctrip.widget.cosmocalendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.OrientationHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yilian.BaseDialogFragment;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.widget.cosmocalendar.model.Day;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.BaseSelectionManager;
import com.yilian.mall.ctrip.widget.cosmocalendar.selection.RangeSelectionManager;
import com.yilian.mall.ctrip.widget.cosmocalendar.utils.SelectionType;
import com.yilian.mall.ctrip.widget.cosmocalendar.view.CalendarView;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ScreenUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;

import rx.functions.Action1;


/**
 * @author air
 */
public class CalendarViewActivity extends BaseDialogFragment {
    public static final String TAG = "CalendarViewActivity";
    private CalendarView calendarView;
    private OnDaysSelected mOnDaysSelected;

    public static CalendarViewActivity getInstance(OnDaysSelected onDaysSelected) {
        CalendarViewActivity calendarViewActivity = new CalendarViewActivity();
        Bundle args = new Bundle();
        args.putSerializable(TAG, onDaysSelected);
        calendarViewActivity.setArguments(args);
        return calendarViewActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_calendar_view, container);
        initView(inflate);
        Bundle arguments = getArguments();
        mOnDaysSelected = (OnDaysSelected) arguments.getSerializable(TAG);
        return inflate;
    }

    private void initView(View inflate) {
        calendarView = (CalendarView) inflate.findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.VERTICAL);
        calendarView.setSelectionType(SelectionType.RANGE);
//        设置每周从周几开始
        calendarView.setFirstDayOfWeek(1);
//        开始时间选中背景色
        calendarView.setSelectedDayBackgroundStartColor(Color.parseColor("#ff4289FF"));
//        选中日期区间的背景颜色
        calendarView.setSelectedDayBackgroundColor(Color.parseColor("#664289FF"));
//        结束时间选中背景色
        calendarView.setSelectedDayBackgroundEndColor(Color.parseColor("#ff4289FF"));
        calendarView.setCurrentDayTextColor(Color.RED);
//        周末的颜色
        calendarView.setWeekendDayTextColor(Color.RED);
//        是否在页面头部显示星期几
        calendarView.setShowDaysOfWeekTitle(true);
//        是否在每月头部显示星期几
        calendarView.setShowDaysOfWeek(false);
//        设置周末
        calendarView.setWeekendDays(new HashSet() {{
            add(Calendar.SUNDAY);
            add(Calendar.SATURDAY);
        }});
//        设置选择区间间隔最大值
        calendarView.setMaxRange(28);
        calendarView.setOnDaysSelectedListener(new CalendarView.OnDaysSelected() {
            @Override
            public void onDaysSelected() {
                BaseSelectionManager selectionManager = calendarView.getSelectionManager();
                if (selectionManager instanceof RangeSelectionManager) {
                    Pair<Day, Day> days = ((RangeSelectionManager) selectionManager).getDays();
                    if (days != null) {
                        mOnDaysSelected.daysSelected(days);
                        RxUtil.countDown(1)
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        if (aLong == 0) {
                                            dismiss();
                                        }
                                    }
                                });
                    }
                }
            }
        });
        calendarView.setOnCancelListener(new CalendarView.OnCancelListener() {
            @Override
            public void cancelListener() {
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        底部显示
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomToTopAnim);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = (int) (ScreenUtils.getScreenHeight(getContext()) / 1.2);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public interface OnDaysSelected extends Serializable {
        void daysSelected(Pair<Day, Day> days);
    }
}
