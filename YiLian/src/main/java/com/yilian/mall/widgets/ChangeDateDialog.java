package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yilian.mall.R;
import com.yilian.mall.widgets.wheelview.OnWheelScrollListener;
import com.yilian.mall.widgets.wheelview.WheelView;
import com.yilian.mall.widgets.wheelview.adapter.NumericWheelAdapter;

import java.util.Calendar;


/**
 * Created by Administrator on 2016/3/23.
 */
public class ChangeDateDialog extends Dialog implements View.OnClickListener{


    //时间控件
    private WheelView wvYear;
    private WheelView wvMouth;
    private WheelView wvDay;

    private int mYear=2014;
    private int mMonth=0;
    private int mDay=1;

    private LinearLayout ll;


    private TextView btnSure;//确定按钮
    private TextView btnCancle;//取消按钮

    private Context context;//上下文对象

    //显示文字的字体大小
//    private int maxsize = 18;
//    private int minsize = 14;

    private LayoutInflater inflater;

    View view=null;
    //回调
    private OnDateCListener onDateCListener;


    public ChangeDateDialog(Context context,LayoutInflater inflater,OnDateCListener onDateCListener) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.inflater=inflater;
        this.onDateCListener=onDateCListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myinfo_changedate);

        ll=(LinearLayout) findViewById(R.id.ll);
        ll.addView(getDataPick());

        btnSure= (TextView) findViewById(R.id.btn_myinfo_data_sure);
        btnCancle= (TextView) findViewById(R.id.btn_myinfo_date_cancel);

        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);

        wvMouth = (WheelView) findViewById(R.id.month);
        wvDay = (WheelView) findViewById(R.id.day);
    }

    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);

        int curYear = mYear;
        int curMonth =mMonth+1;
        int curDate = mDay;

        view = inflater.inflate(R.layout.wheel_date_picker, null);

        wvYear = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(context, 2014, norYear);
        numericWheelAdapter1.setLabel("年");
        wvYear.setViewAdapter(numericWheelAdapter1);
        wvYear.setCyclic(false);//是否可循环滑动
        wvYear.addScrollingListener(scrollListener);

        wvMouth= (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(context, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        wvMouth.setViewAdapter(numericWheelAdapter2);
        wvMouth.setCyclic(false);
        wvMouth.addScrollingListener(scrollListener);

        wvDay= (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        wvDay.setCyclic(false);

        wvYear.setVisibleItems(5);//设置显示行数
        wvMouth.setVisibleItems(5);
        wvDay.setVisibleItems(5);

        wvYear.setCurrentItem(curYear - 2014);
        wvMouth.setCurrentItem(curMonth - 1);
        wvDay.setCurrentItem(curDate - 1);

        return view;
    }

    private int n_year,n_month,n_day;
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
             n_year = wvYear.getCurrentItem() + 2014;//年
             n_month = wvMouth.getCurrentItem() + 1;//月
             n_day = getDay(n_year,n_month);

            initDay(n_year, n_month);

//            mYear=wvYear.getCurrentItem()+2014;
//            mMonth=wvMouth.getCurrentItem()+1;
//            mDay=wvDay.getCurrentItem();
        }
    };
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(context,1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        wvDay.setViewAdapter(numericWheelAdapter);
    }
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }



    public interface OnDateCListener {
         void onClick(int mYear, int mMonth, int mDay);
    }

    public void setDatekListener(OnDateCListener onDateCListener) {
        this.onDateCListener = onDateCListener;
    }
    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onDateCListener != null) {
                n_year=wvYear.getCurrentItem()+2014;
                n_month=wvMouth.getCurrentItem()+1;
                n_day=wvDay.getCurrentItem()+1;
                onDateCListener.onClick(n_year, n_month, n_day);
            }
        }
        if (v == btnCancle){
            dismiss();
        }
        dismiss();
    }
}
