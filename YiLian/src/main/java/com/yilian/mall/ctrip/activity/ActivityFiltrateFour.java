package com.yilian.mall.ctrip.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.AdapterFiltrateRight;
import com.yilian.mall.ctrip.adapter.AdapterSearchAddressDistance;
import com.yilian.mall.ctrip.bean.FiltrateEventBean;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rxfamily.utils.StringUtils;

/**
 * 作者：马铁超 on 2018/8/31 14:43
 * 综合筛选
 */

public class ActivityFiltrateFour {
    private Context context;
    private MyItemClickListener listener;
    private ListView location_filtrate_left;
    private ListView location_filtrate_right;
    private List<SearchFilterBean.SSortBean> sortBeanList = new ArrayList<>();
    private List<SearchFilterBean.SSortBean.SortsBeanXX> beanXXList = new ArrayList<>();
    private AdapterSearchAddressDistance adapter_searchAddress_distance;
    private AdapterFiltrateRight adapter_filtrate_right;
    private LinearLayout ll_reset, ll_complete;
    private String themeId="", themeTitle="", brandid="", facilityid="", bedid="", grade="", primary_ClassificationId="";
    static int index = 0;

    ActivityFiltrateFour(Context context, List<SearchFilterBean.SSortBean> secSortBeans) {
        this.context = context;
        sortBeanList = secSortBeans;

    }

    void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    View fourView() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_filtrate_four, null);
        location_filtrate_left = (ListView) view.findViewById(R.id.lv_filtrate_four_left);
        location_filtrate_right = (ListView) view.findViewById(R.id.lv_filtrate_four_right);
        ll_reset = view.findViewById(R.id.ll_reset);
        ll_complete = view.findViewById(R.id.ll_complete);
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
        if (sortBeanList != null) {
            adapter_searchAddress_distance = new AdapterSearchAddressDistance(context, sortBeanList, R.layout.item_filtrate_distance_left);
            location_filtrate_left.setAdapter(adapter_searchAddress_distance);
            primary_ClassificationId = sortBeanList.get(0).getKey();
        }
        if (sortBeanList.size() > 0) {
            for (int i = 0; i < sortBeanList.size(); i++) {
                sortBeanList.get(i).setCheck(false);
            }
            if (beanXXList != null && beanXXList.size() > 0) {
                beanXXList.clear();
            }
            sortBeanList.get(0).setCheck(true);
            beanXXList.addAll(sortBeanList.get(0).getSorts());
        }


        if (adapter_filtrate_right == null) {
            adapter_filtrate_right = new AdapterFiltrateRight(context, beanXXList, R.layout.item_filtrate_distance_right);
            location_filtrate_right.setAdapter(adapter_filtrate_right);
        }
        sortBeanList.get(0).setCheck(true);


        location_filtrate_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                primary_ClassificationId = "";
                if (sortBeanList != null && sortBeanList.size() > 0 && !StringUtils.isEmpty(sortBeanList.get(position).getKey())) {
                    primary_ClassificationId = sortBeanList.get(position).getKey();
                }
                for (SearchFilterBean.SSortBean sSortBean : sortBeanList) {
                    sSortBean.setCheck(false);
                }
//              左侧列表点击选中状态
                sortBeanList.get(position).setCheck(true);
                if (adapter_filtrate_right != null) {
                    if (beanXXList.size() > 0) {
                        beanXXList.clear();
                    }
                    beanXXList.addAll(sortBeanList.get(position).getSorts());
                }
                adapter_searchAddress_distance.notifyDataSetChanged();
                adapter_filtrate_right.notifyDataSetChanged();
            }
        });
        location_filtrate_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean circle = false;
                for (int i = 0; i < beanXXList.size(); i++) {
                    if (position != i) {
                        beanXXList.get(i).setCheck(false);
                    }
                }
                if (beanXXList.get(position).isCheck() == true) {
                    beanXXList.get(position).setCheck(false);
                    themeTitle = "";
                    if (primary_ClassificationId.equals("themeid")) {
                        themeId = "";
                    } else if (primary_ClassificationId.equals("brandid")) {
                        brandid = "";
                    } else if (primary_ClassificationId.equals("facilityid")) {
                        facilityid = "";
                    } else if (primary_ClassificationId.equals("bedid")) {
                        bedid = "";
                    } else if (primary_ClassificationId.equals("grade")) {
                        grade = "";
                    }
                } else {
                    beanXXList.get(position).setCheck(true);
                    themeTitle = beanXXList.get(position).getTitle();
                    if (primary_ClassificationId.equals("themeid")) {
                        themeId = beanXXList.get(position).getSorts();
                    } else if (primary_ClassificationId.equals("brandid")) {
                        brandid = beanXXList.get(position).getSorts();
                    } else if (primary_ClassificationId.equals("facilityid")) {
                        facilityid = beanXXList.get(position).getSorts();
                    } else if (primary_ClassificationId.equals("bedid")) {
                        bedid = beanXXList.get(position).getSorts();
                    } else if (primary_ClassificationId.equals("grade")) {
                        grade = beanXXList.get(position).getSorts();
                    }
                }
                // 右侧有内容选中时 更新左侧列表小红点
                for (SearchFilterBean.SSortBean bean : sortBeanList) {
                    if (bean.getSorts().size() > 0) {
                        for (int i = 0; i < bean.getSorts().size(); i++) {
                            if (bean.getSorts().get(i).isCheck() == true) {
                                circle = true;
                            }
                        }
                        if (circle == true) {
                            bean.setCircleShow(true);
                        } else {
                            bean.setCircleShow(false);
                        }
                        circle = false;
                    }
                }
                if (adapter_searchAddress_distance != null) {
                    adapter_searchAddress_distance.notifyDataSetChanged();
                }
                if (adapter_filtrate_right != null) {
                    adapter_filtrate_right.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    //  完成
    private void comPlete() {
        listener.onFourClick(themeTitle, themeId, brandid, facilityid, bedid, grade, primary_ClassificationId);
    }

    //  重置
    private void reSetContent() {

        if (sortBeanList != null && sortBeanList.size() > 0) {
            primary_ClassificationId = sortBeanList.get(0).getKey();
        }
        for (SearchFilterBean.SSortBean bean : sortBeanList) {
            bean.setCheck(false);
            bean.setCircleShow(false);
            for (int i = 0; i < bean.getSorts().size(); i++) {
                bean.getSorts().get(i).setCheck(false);
            }
        }
//              左侧列表点击选中状态
        if (sortBeanList != null && sortBeanList.size() > 0) {
            sortBeanList.get(0).setCheck(true);
            if (beanXXList != null && beanXXList.size() > 0) {
                beanXXList.clear();
                beanXXList.addAll(sortBeanList.get(0).getSorts());
            }
        }
        if (adapter_searchAddress_distance != null) {
            adapter_searchAddress_distance.notifyDataSetChanged();
        }
        if (adapter_filtrate_right != null) {
            adapter_filtrate_right.notifyDataSetChanged();
        }
        FiltrateEventBean filtrateEventBean = new FiltrateEventBean("筛选", false, 3);
        EventBus.getDefault().post(filtrateEventBean);
    }

    public void refreshAdapter(String key) {
        //右侧有内容选中时 更新左侧列表小红点
        //        location_filtrate_right.setSelection(index);
        moveToIndex(key);
        if (adapter_searchAddress_distance != null) {
            adapter_searchAddress_distance.notifyDataSetChanged();
        }
        if (adapter_filtrate_right != null) {
            adapter_filtrate_right.notifyDataSetChanged();
        }
    }

    //  根据最后一条选中的key值   更新左侧列表选中状态
    private void moveToIndex(String key) {
        boolean circle = false;
        index = 0;
        for (SearchFilterBean.SSortBean sSortBean : sortBeanList) {
            sSortBean.setCheck(false);
            if (sSortBean.getSorts().size() > 0) {
                for (int i = 0; i < sSortBean.getSorts().size(); i++) {
                    if (sSortBean.getSorts().get(i).isCheck() == true) {
                        circle = true;
                        index = i;
                    }
                }
                if (circle == true) {
                    sSortBean.setCircleShow(true);
                } else {
                    sSortBean.setCircleShow(false);
                }
                circle = false;
            }
            if (sSortBean.getKey().equals(key)) {
                sSortBean.setCheck(true);
                if (beanXXList != null) {
                    beanXXList.clear();
                    beanXXList.addAll(sSortBean.getSorts());
                }
            }
        }
        if (location_filtrate_right != null) {
            location_filtrate_right.post(new Runnable() {
                @Override
                public void run() {
                    location_filtrate_right.smoothScrollToPosition(index);
                }
            });
        }
    }
}
