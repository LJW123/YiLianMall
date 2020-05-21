package com.yilian.mall.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 作者：马铁超 on 2018/9/4 12:23
 * 动态设置列表高度 工具类
 */
public class SetHeightUtil {
    //为listview动态设置高度（有多少条目就显示多少条目）
    public static void setListViewHeight(ListView listView, Context context) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//       获取每一条占用的高度
        int itemHeight = params.height/listAdapter.getCount();
//       如果超出范围  默认显示5条的高度
        if(listAdapter.getCount()>=6){
            params.height = itemHeight*6;
        }
//     获取当前屏幕高度
//     如果超出屏幕范围  显示范围在屏幕的一半
       /* WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (params.height >= height) {
            params.height = height / 2;
        }*/
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeight(GridView gridview,Context context) {
        // 获取gridview的adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int numColumns = gridview.getNumColumns();
        int totalHeight = 0;
        // 计算每一列的高度之和
        for (int i = 0; i < listAdapter.getCount(); i += numColumns) {
            // 获取gridview的每一个item
            View listItem = listAdapter.getView(i, null, gridview);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }
        // 获取gridview的布局参数
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        params.height = totalHeight;
        if (params.height >= height) {
            params.height = height / 2;
        }
        gridview.setLayoutParams(params);
    }
}
