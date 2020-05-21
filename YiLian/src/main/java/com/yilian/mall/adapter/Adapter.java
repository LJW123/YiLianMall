package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 2016/4/26.
 */
public abstract  class Adapter<T> extends android.widget.BaseAdapter {
    private List<T> list;

    protected Context context;

    public Adapter(Context context) {
        init(context, new ArrayList());
    }

    public Adapter(Context context, List<T> list) {
        init(context, list);
    }

    private void init(Context context, List<T> list) {
        this.list = list;
        this.context = context;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        if (list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflate(getContentView());
        }
        T item = getItem(position);
        onInitView(convertView, item,position);
        return convertView;
    }

    /** 加载布局 */
    private View inflate(int layoutResID) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layoutResID, null);
        return view;
    }

    public abstract int getContentView();

    public abstract void onInitView(View view,T item, int position);

    /**
     *
     * @param view
     *            converView
     * @param id
     *            控件的id
     * @return 返回
     */
//    @SuppressWarnings("unchecked")
//    protected E get(View view, int id) {
//        SparseArray viewHolder = (SparseArray) view.getTag();
//        if (null == viewHolder) {
//            viewHolder = new SparseArray();
//            view.setTag(viewHolder);
//        }
//        View childView = (View) viewHolder.get(id);
//        if (null == childView) {
//            childView = view.findViewById(id);
//            viewHolder.put(id, childView);
//
//        }
//        return (E) childView;
//    }
}
