package com.yilian.mall.ctrip.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.AdapterStartLeavel;
import com.yilian.mall.ctrip.adapter.AdapterThirdPrice;
import com.yilian.mall.ctrip.bean.FiltrateEventBean;
import com.yilian.mall.ctrip.bean.PriceAndLevelSelectBean;
import com.yilian.mall.listener.MyItemClickListener;
import com.yilian.mall.widgets.rangeSeekBar.OnRangeChangedListener;
import com.yilian.networkingmodule.entity.SearchFilterBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import rxfamily.utils.StringUtils;

/**
 * 作者：马铁超 on 2018/9/3 10:35
 * 携程_筛选_价格星级
 */

public class ActivityFilterThird {

    int price_range_min, price_range_max, static_min_range = 0, static_max_range = 1000;
    //  进度最小值
    int min_range = 0;
    //  最大范围
    int max_range = 1000;
    //  范围间隔
    int range_interval = 50;
    //    预留间隔
    int range_reserve = 5;
    private String getMinRangeIndex, getMaxRangeIndex;
    private String getMinPrice, getMaxPrice;
    private Context context;
    private MyItemClickListener listener;
    private GridView gv_filtrate_price, gv_filtrate_starlevel;
    private TextView price_range, tv_start_price, tv_max_price;
    private LinearLayout ll_reset, ll_complete;
    private com.yilian.mall.widgets.rangeSeekBar.RangeSeekBar rangeSeekBar;
    private List<SearchFilterBean.PSortBean> pSortBeanList = new ArrayList<>();
    private AdapterThirdPrice third_price;
    private AdapterStartLeavel adapter_startLeavel;
    private String minPrice = "", maxPrice = "";
    List<String>getLevelId;

    public ActivityFilterThird(Context context, List<SearchFilterBean.PSortBean> pSortBeans, @Nullable PriceAndLevelSelectBean priceAndLevelSelectBean) {
        this.context = context;
        pSortBeanList = pSortBeans;
        if (priceAndLevelSelectBean != null) {
            getMinRangeIndex = priceAndLevelSelectBean.getMinRange();
            getMaxRangeIndex = priceAndLevelSelectBean.getMaxRange();
            getMinPrice = priceAndLevelSelectBean.getMinPrice();
            getMaxPrice = priceAndLevelSelectBean.getMaxPrice();
            getLevelId = priceAndLevelSelectBean.getLevelId();
            //       星级
            if (getLevelId.size() > 0 && getLevelId!= null) {
                if (pSortBeans != null && pSortBeans.size() > 0) {
                    if (pSortBeans.get(1).getSorts() != null && pSortBeans.get(1).getSorts().size() > 0) {
                        for (int i = 0; i < pSortBeans.get(1).getSorts().size(); i++) {
                            if (getLevelId!= null && getLevelId.size() > 0){
                                for (int j = 0 ; j <getLevelId.size() ; j++ ){
                                    if(getLevelId.get(j).equals(pSortBeans.get(1).getSorts().get(i).getSorts())){
                                        pSortBeans.get(1).getSorts().get(i).setCheck(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
//      最低 & 最高 价格
            if (!StringUtils.isEmpty(getMinPrice) && !StringUtils.isEmpty(getMaxPrice)) {
                if (pSortBeans != null && pSortBeans.size() > 0) {
                    if (pSortBeans.get(0).getSorts() != null && pSortBeans.get(0).getSorts().size() > 0) {
                        for (int i = 0; i < pSortBeans.get(0).getSorts().size(); i++) {
                            String price = pSortBeans.get(0).getSorts().get(i).getSorts();
                            int indexOf = price.indexOf(",");
                            String minPrice = price.substring(0, indexOf);
                            String maxPrice = price.substring(indexOf + 1, price.length());
                            if (minPrice.equals(getMinPrice) && maxPrice.equals(getMaxPrice)) {
                                pSortBeans.get(0).getSorts().get(i).setCheck(true);
                            }
                        }
                    }
                }
            }

        }
    }

    public void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    public View thirdView() {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_filtrate_third_view, null);
        tv_max_price = view.findViewById(R.id.tv_max_price);
        tv_start_price = view.findViewById(R.id.tv_start_price);
        gv_filtrate_price = (GridView) view.findViewById(R.id.gv_filtrate_price);
        gv_filtrate_starlevel = (GridView) view.findViewById(R.id.gv_filtrate_starlevel);
        rangeSeekBar = (com.yilian.mall.widgets.rangeSeekBar.RangeSeekBar) view.findViewById(R.id.rsb);
        price_range = view.findViewById(R.id.price_range);
        ll_reset = view.findViewById(R.id.ll_reset);
        ll_complete = view.findViewById(R.id.ll_complete);
        rangeSeekBar.setTypeface(Typeface.DEFAULT_BOLD);
//        rangeSeekBar.getLeftSeekBar().setTypeface(Typeface.DEFAULT_BOLD);
        rangeSeekBar.setIndicatorTextDecimalFormat("0");
        ll_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetContent();
            }
        });
        ll_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeContent();
            }
        });
        if (pSortBeanList != null) {
//          获取默认最低最高价格
            int indexOf = pSortBeanList.get(0).getPrice().indexOf(",");
            price_range_min = Integer.parseInt(pSortBeanList.get(0).getPrice().substring(0, indexOf));
            price_range_max = Integer.parseInt(pSortBeanList.get(0).getPrice().substring(indexOf + 1, pSortBeanList.get(0).getPrice().length()));
            static_min_range = price_range_min;
            static_max_range = price_range_max;
            minPrice = price_range_min + "";
            maxPrice = price_range_max + "";
            tv_max_price.setText("￥" + maxPrice + "以上");
            tv_start_price.setText("￥" + minPrice);
        }
//      筛选价格取值范围
        if (!StringUtils.isEmpty(getMinPrice) && !StringUtils.isEmpty(getMaxPrice)) {
            price_range.setText("￥" + getMinPrice + "-" + getMaxPrice);
        } else {
            price_range.setText("￥" + static_min_range + "-" + static_max_range);
        }
        if (pSortBeanList != null && pSortBeanList.size() > 0) {
            third_price = new AdapterThirdPrice(context, pSortBeanList.get(0).getSorts(), R.layout.item_third_price);
            gv_filtrate_price.setAdapter(third_price);
            adapter_startLeavel = new AdapterStartLeavel(context, pSortBeanList.get(1).getSorts(), R.layout.item_third_startleavel);
            gv_filtrate_starlevel.setAdapter(adapter_startLeavel);
        }
        //设置rangeSeekBar 间隔
        if (static_max_range > 50) {
            max_range = static_max_range;
            range_interval = static_max_range / 50;
        }
        rangeSeekBar.setSeekBarMode(2);
        rangeSeekBar.setRange(min_range, max_range, range_reserve, range_interval);
        //如果之前页面有设置 最大小价格 设置进度
        if (!StringUtils.isEmpty(getMinRangeIndex) && !StringUtils.isEmpty(getMaxRangeIndex)) {
            min_range = Integer.parseInt(getMinRangeIndex);
            max_range = Integer.parseInt(getMaxRangeIndex);
            rangeSeekBar.setValue(min_range, max_range);
        } else if (!StringUtils.isEmpty(pSortBeanList.get(0).minRange) && !StringUtils.isEmpty(pSortBeanList.get(0).maxRange)) {
            min_range = Integer.parseInt(pSortBeanList.get(0).minRange);
            max_range = Integer.parseInt(pSortBeanList.get(0).maxRange);
            rangeSeekBar.setValue(min_range, max_range);
        } else {
            rangeSeekBar.setValue(static_min_range, static_max_range);
        }
//      价格筛选列表点击事件
        gv_filtrate_price.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //清空所有选中状态
                for (int i = 0; i < pSortBeanList.get(0).getSorts().size(); i++) {
                    if (position != i) {
                        pSortBeanList.get(0).getSorts().get(i).setCheck(false);
                    }
                }
                // 反选恢复默认值
                if (pSortBeanList.get(0).getSorts().get(position).isCheck() == true) {
                    pSortBeanList.get(0).getSorts().get(position).setCheck(false);
                    price_range.setText("¥" + price_range_min + "-" + price_range_max);
                    rangeSeekBar.setValue(static_min_range, static_max_range);
                } else {
                    //选中
                    pSortBeanList.get(0).getSorts().get(position).setCheck(true);
                    int indexOf = pSortBeanList.get(0).getSorts().get(position).getSorts().indexOf(",");
                    min_range = Integer.parseInt(pSortBeanList.get(0).getSorts().get(position).getSorts().substring(0, indexOf));
                    max_range = Integer.parseInt(pSortBeanList.get(0).getSorts().get(position).getSorts().substring(indexOf + 1, pSortBeanList.get(0).getSorts().get(position).getSorts().length()));
                    if (max_range >= static_max_range) {
                        max_range = static_max_range;
                    }
                    price_range.setText("￥" + min_range + "-" + max_range);
                    rangeSeekBar.setValue(min_range, max_range);
                    minPrice = min_range + "";
                    maxPrice = max_range + "";
                }
                third_price.notifyDataSetChanged();
            }
        });

//    星级筛选列表点击事件
        gv_filtrate_starlevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pSortBeanList.get(1).getSorts().get(position).isCheck() == true) {
                    pSortBeanList.get(1).getSorts().get(position).setCheck(false);
                } else {
                    pSortBeanList.get(1).getSorts().get(position).setCheck(true);
                }
                adapter_startLeavel.notifyDataSetChanged();
            }
        });
//      双向滑动SeekBar  滑动监听
        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(com.yilian.mall.widgets.rangeSeekBar.RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                price_range.setText("¥" + Math.round(leftValue) + "-" + Math.round(rightValue));
                minPrice = Integer.parseInt(new java.text.DecimalFormat("0").format(leftValue)) + "";
                maxPrice = Integer.parseInt(new java.text.DecimalFormat("0").format(rightValue)) + "";
                pSortBeanList.get(0).minRange = minPrice;
                pSortBeanList.get(0).maxRange = maxPrice;
            }

            @Override
            public void onStartTrackingTouch(com.yilian.mall.widgets.rangeSeekBar.RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(com.yilian.mall.widgets.rangeSeekBar.RangeSeekBar view, boolean isLeft) {
            }
        });
        return view;
    }

    //  重置
    public void resetContent() {
        price_range.setText("¥" + price_range_min + "-" + price_range_max);
        rangeSeekBar.setValue(0, static_max_range);
        for (SearchFilterBean.PSortBean.SortsBeanX sortsBeanX : pSortBeanList.get(0).getSorts()) {
            sortsBeanX.setCheck(false);
        }
        for (SearchFilterBean.PSortBean.SortsBeanX sortsBeanX : pSortBeanList.get(1).getSorts()) {
            sortsBeanX.setCheck(false);
        }
        third_price.notifyDataSetChanged();
        adapter_startLeavel.notifyDataSetChanged();
        FiltrateEventBean filtrateEventBean = new FiltrateEventBean("价格/星级", false, 2);
        EventBus.getDefault().post(filtrateEventBean);
    }

    //  完成
    private void completeContent() {
        boolean priceIsCheck = false;
        ArrayList<String> start_Level_list = new ArrayList<>();
        //星级
        String starStr = "";
        for (SearchFilterBean.PSortBean.SortsBeanX sortsBeanX : pSortBeanList.get(1).getSorts()) {
            if (sortsBeanX.isCheck() == true) {
                start_Level_list.add(sortsBeanX.getSorts());
                if (starStr.equals("")) {
                    starStr = sortsBeanX.getTitle();
                } else {
                    starStr += "," + sortsBeanX.getTitle();
                }
            }
        }
        String showStr = "";
        if (TextUtils.isEmpty(starStr)) {
            showStr = "¥" + minPrice + "-" + maxPrice;
        } else {
            showStr = starStr + ",¥" + minPrice + "-" + maxPrice;
        }

        if (!StringUtils.isEmpty(minPrice) && !StringUtils.isEmpty(maxPrice) || start_Level_list.size() > 0) {
            priceIsCheck = true;
        }
        listener.onThereClick(priceIsCheck, minPrice, maxPrice, start_Level_list, showStr);
    }

    public void refreshAdapter(List<SearchFilterBean.PSortBean> pSort){
        if (!StringUtils.isEmpty(pSort.get(0).minRange) && !StringUtils.isEmpty(pSort.get(0).maxRange)) {
            min_range = Integer.parseInt(pSort.get(0).minRange);
            max_range = Integer.parseInt(pSort.get(0).maxRange);
            rangeSeekBar.setValue(min_range, max_range);
        }
        if (third_price != null) {
            third_price.notifyDataSetChanged();
        }
        if (adapter_startLeavel != null) {
            adapter_startLeavel.notifyDataSetChanged();
        }
    }
}
