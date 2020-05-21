package com.yilian.mall.ctrip.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.FiltrateOneAdapter;
import com.yilian.mall.ctrip.bean.FiltrateOneContentBean;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.mall.utils.SetHeightUtil;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：马铁超 on 2018/9/3 09:39
 * 酒店查询_筛选_直线距离
 */

public class ActivityFiltrateOne {
    RecyclerView rvAddressFiltrate;
    private Context context;
    private MyItemClickListener listener;
    private List<SearchFilterBean.ComSortBean> this_comSortBeans;
    FiltrateOneAdapter adapter;
    String keyWord="";
    ActivityFiltrateOne(Context context, List<SearchFilterBean.ComSortBean> comSortBeans) {
        this.context = context;
        this_comSortBeans = comSortBeans;
    }

    void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    View oneView() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_filtrate_one, null);
        rvAddressFiltrate = (RecyclerView) view.findViewById(R.id.rv_address_filtrate);
        rvAddressFiltrate.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        if (this_comSortBeans != null && this_comSortBeans.size() > 0) {
            adapter = new FiltrateOneAdapter();
            rvAddressFiltrate.setAdapter(adapter);
            adapter.setNewData(this_comSortBeans);
            adapter.bindToRecyclerView(rvAddressFiltrate);
        }
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.onOneClick(view, 1, this_comSortBeans.get(position).getTitle(), this_comSortBeans.get(position).getSorts(), 0,keyWord);
                for (SearchFilterBean.ComSortBean comSortBean : this_comSortBeans) {
                    comSortBean.setCheck(false);
                }
                this_comSortBeans.get(position).setCheck(true);
                keyWord = this_comSortBeans.get(position).getKey();
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public void setDefaultSelect(String keyWord){
        for (int i = 0 ; i<this_comSortBeans.size() ; i ++ ){
            if(this_comSortBeans.get(i).getSorts().equals(keyWord)){
                this_comSortBeans.get(i).setCheck(true);
            }
        }
    }

    public  void refreshAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
