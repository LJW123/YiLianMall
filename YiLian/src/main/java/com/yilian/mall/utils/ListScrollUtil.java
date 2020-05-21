package com.yilian.mall.utils;/**
 * Created by  on 2017/1/11 0011.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.yilian.mylibrary.ScreenUtils;

/**
 * Created by  on 2017/1/11 0011.
 */
public class ListScrollUtil {
    private  final Context context;

    public ListScrollUtil(Context context) {
        this.context = context;
    }

    /**
     * 动态设置listview的高度
     *
     * @param listView
     */
    public  void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        View view;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, null, listView);
            //宽度为屏幕宽度
            int i1 = View.MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth(context),
                    View.MeasureSpec.EXACTLY);
            //根据屏幕宽度计算高度
            int i2 = View.MeasureSpec.makeMeasureSpec(i1, View.MeasureSpec.UNSPECIFIED);
            view.measure(i1, i2);
            totalHeight += view.getMeasuredHeight();
        }
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
