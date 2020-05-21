package com.yilian.mall.ctrip.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.AdapterSearchAddressDistanceLeft;
import com.yilian.mall.ctrip.adapter.AdapterSearchAddressDistanceRight;
import com.yilian.mall.ctrip.bean.FiltrateEventBean;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import rxfamily.utils.StringUtils;

/**
 * 作者：马铁超 on 2018/9/3 10:19
 * 位置距离
 */

public class ActivityFilterSec {
    private Context context;
    private MyItemClickListener listener;
    private  RecyclerView rv_filtrate_distance_left;
    private  RecyclerView rv_filtrate_distance_right;
    private  List<SearchFilterBean.DistSortBean> distSortBeans = new ArrayList<>();
    private  List<SearchFilterBean.DistSortBean.SortsBean> sortsBeans = new ArrayList<>();
    AdapterSearchAddressDistanceRight distanceRight;
    AdapterSearchAddressDistanceLeft distanceLeft;
    private LinearLayout ll_reset, ll_complete;
    private String title = "", distanceId = "", zoneid = "", locationid = "", primary_ClassificationId = "";
    private int index = -1;
    private String getTag;

    /**
     * @param context
     * @param beanList 数据源
     *
     */
    ActivityFilterSec(Context context, List<SearchFilterBean.DistSortBean> beanList, @Nullable String tag) {
        this.context = context;
        distSortBeans = beanList;
        getTag = tag;
    }

    void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    View secView() {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_filtrate_sec, null);
        rv_filtrate_distance_left = (RecyclerView) view.findViewById(R.id.lv_filtrate_distance_left);
        rv_filtrate_distance_right = (RecyclerView) view.findViewById(R.id.lv_filtrate_distance_right);
        ll_reset = view.findViewById(R.id.ll_reset);
        ll_complete = view.findViewById(R.id.ll_complete);
        rv_filtrate_distance_left.setFocusable(false);
        rv_filtrate_distance_left.setNestedScrollingEnabled(false);
        rv_filtrate_distance_left.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_filtrate_distance_right.setFocusable(false);
        rv_filtrate_distance_right.setNestedScrollingEnabled(false);
        rv_filtrate_distance_right.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        //默认选择第一条如果tag不为空根据tag传过来的key判断当前左侧列表哪个选中
        if (distSortBeans.size() > 0) {
            for (int i = 0; i < distSortBeans.size(); i++) {
                distSortBeans.get(i).setCheck(false);
            }
            if (sortsBeans != null && sortsBeans.size() > 0) {
                sortsBeans.clear();
            }
            if (!StringUtils.isEmpty(getTag)) {
                if (getTag.equals("1")) {
                    sortsBeans.addAll(distSortBeans.get(1).getSorts());
                    distSortBeans.get(1).setCheck(true);
                } else if (getTag.equals("2")) {
                    sortsBeans.addAll(distSortBeans.get(2).getSorts());
                    distSortBeans.get(2).setCheck(true);
                }
            } else {
                sortsBeans.addAll(distSortBeans.get(0).getSorts());
                distSortBeans.get(0).setCheck(true);
            }
        }
        if (distSortBeans != null && distSortBeans.size() > 0) {
            primary_ClassificationId = distSortBeans.get(0).getKey();
            if(distanceLeft == null){
                distanceLeft = new AdapterSearchAddressDistanceLeft();
                rv_filtrate_distance_left.setAdapter(distanceLeft);
                distanceLeft.bindToRecyclerView(rv_filtrate_distance_left);
            }
            distanceLeft.setNewData(distSortBeans);
        }
        if (sortsBeans != null && sortsBeans.size() > 0) {
            if(distanceRight == null){
                distanceRight = new AdapterSearchAddressDistanceRight();
                rv_filtrate_distance_right.setAdapter(distanceRight);
                distanceRight.bindToRecyclerView(rv_filtrate_distance_right);
            }
            distanceRight.setNewData(sortsBeans);
        }
        ll_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetContent();
            }
        });
        ll_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comPlete();
            }
        });
        //      右侧列表点击
        distanceRight.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean circle = false;
                title = "";
                distanceId = "";
//              清空右侧列表选中状态 设置当前下标选中
                for (int i = 0; i < sortsBeans.size(); i++) {
                    if (i != position) {
                        sortsBeans.get(i).setCheck(false);
                    }
                }
                if (sortsBeans.get(position).isCheck() == true) {
                    sortsBeans.get(position).setCheck(false);
                    if (!StringUtils.isEmpty(primary_ClassificationId)) {
                        if (primary_ClassificationId.equals("distance")) {
                            distanceId = "";
                        } else if (primary_ClassificationId.equals("zoneid")) {
                            zoneid = "";
                        } else if (primary_ClassificationId.equals("locationid")) {
                            locationid = "";
                        }
                    }
                } else {
                    sortsBeans.get(position).setCheck(true);
                    title = sortsBeans.get(position).getTitle();
                    if (!StringUtils.isEmpty(primary_ClassificationId)) {
                        if (primary_ClassificationId.equals("distance")) {
                            distanceId = sortsBeans.get(position).getSorts();
                        } else if (primary_ClassificationId.equals("zoneid")) {
                            zoneid = sortsBeans.get(position).getSorts();
                        } else if (primary_ClassificationId.equals("locationid")) {
                            locationid = sortsBeans.get(position).getSorts();
                        }
                    }
                }
//              右侧有内容选中时 更新左侧列表小红点
                for (SearchFilterBean.DistSortBean distSortBean : distSortBeans) {
                    if (distSortBean.getSorts().size() > 0) {
                        for (int i = 0; i < distSortBean.getSorts().size(); i++) {
                            if (distSortBean.getSorts().get(i).isCheck() == true) {
                                circle = true;
                            }
                        }
                        if (circle == true) {
                            distSortBean.setCircleShow(true);
                        } else {
                            distSortBean.setCircleShow(false);
                        }
                        circle = false;
                    }
                }
                if (primary_ClassificationId.equals("zoneid")) {
                    circulationList("locationid");
                } else if (primary_ClassificationId.equals("locationid")) {
                    circulationList("zoneid");
                }
                distanceLeft.notifyDataSetChanged();
                distanceRight.notifyDataSetChanged();
            }
        });
//      左侧列表点击
        distanceLeft.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                primary_ClassificationId = "";
                if (distSortBeans != null && distSortBeans.size() > 0 && !StringUtils.isEmpty(distSortBeans.get(position).getKey())) {
                    primary_ClassificationId = distSortBeans.get(position).getKey();
                }
//              清空选中状态
                if (distSortBeans != null && distSortBeans.size() > 0) {
                    for (SearchFilterBean.DistSortBean distSortBean : distSortBeans) {
                        distSortBean.setCheck(false);
                    }
                    distSortBeans.get(position).setCheck(true);
                    if (sortsBeans != null && sortsBeans.size() > 0) {
                        sortsBeans.clear();
                        sortsBeans.addAll(distSortBeans.get(position).getSorts());
                    }
                    distanceRight.setNewData(sortsBeans);
                    if (distanceRight != null) {
                        distanceRight.notifyDataSetChanged();
                    }
                    if (distanceLeft != null) {
                        distanceLeft.notifyDataSetChanged();
                    }
                }

            }
        });
        return view;
    }

    // 行政区 与 商业区为互斥关系
    private void circulationList(String name) {
        for (SearchFilterBean.DistSortBean distSortBean : distSortBeans) {
            if (distSortBean.getKey().equals(name)) {
                distSortBean.setCircleShow(false);
                for (int i = 0; i < distSortBean.getSorts().size(); i++) {
                    distSortBean.getSorts().get(i).setCheck(false);
                }
            }
        }
    }

    //  完成
    private void comPlete() {
        List<SearchFilterBean.DistSortBean> data = distSortBeans;
        listener.onTwoClick(title, distanceId, zoneid, locationid, primary_ClassificationId);
    }

    //  重置
    private void reSetContent() {
        distanceId = "";
        zoneid = "";
        locationid = "";
        if (distSortBeans != null && distSortBeans.size() > 0) {
            primary_ClassificationId = distSortBeans.get(0).getKey();
        }
        for (SearchFilterBean.DistSortBean bean : distSortBeans) {
            bean.setCircleShow(false);
            bean.setCheck(false);
            for (int j = 0; j < bean.getSorts().size(); j++) {
                bean.getSorts().get(j).setCheck(false);
            }
        }
        if (distSortBeans != null && distSortBeans.size() > 0) {
            distSortBeans.get(0).setCheck(true);
            if (sortsBeans != null && sortsBeans.size() > 0) {
                sortsBeans.clear();
                sortsBeans.addAll(distSortBeans.get(0).getSorts());
            }
        }
        if (distanceLeft != null) {
            distanceLeft.notifyDataSetChanged();
        }
        if (distanceRight != null) {
            distanceRight.notifyDataSetChanged();
        }
        FiltrateEventBean filtrateEventBean = new FiltrateEventBean("位置距离", false, 1);
        EventBus.getDefault().post(filtrateEventBean);
    }

    public  void refreshAdapter(String key) {
        //右侧有内容选中时 更新左侧列表小红点
        boolean circle = false;
        int index = 0;
        for (SearchFilterBean.DistSortBean distSortBean : distSortBeans) {
            distSortBean.setCheck(false);
            if (distSortBean.getSorts().size() > 0) {
                for (int i = 0; i < distSortBean.getSorts().size(); i++) {
                    if (distSortBean.getSorts().get(i).isCheck() == true) {
                        circle = true;
//                        index = i;
                    }
                }
                if (circle == true) {
                    distSortBean.setCircleShow(true);
                } else {
                    distSortBean.setCircleShow(false);
                }
                circle = false;
            }
            if (distSortBean.getKey().equals(key)) {
                distSortBean.setCheck(true);
                sortsBeans.clear();
                sortsBeans.addAll(distSortBean.getSorts());
            }
        }
//        listview.setSelection(index);
        if (distanceLeft != null) {
            distanceLeft.notifyDataSetChanged();
        }
        if (distanceRight != null) {
            distanceRight.notifyDataSetChanged();
        }
    }
}
