package com.yilian.mall.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.MTComboDetailsEntity;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/14 0014.
 * 套餐详情的套餐信息
 */
public class ComboListAdapter  extends BaseListAdapter<MTComboDetailsEntity.DataBean.PackageInfoBean> {

    public ComboListAdapter(List<MTComboDetailsEntity.DataBean.PackageInfoBean> packageIcon) {
        super(packageIcon);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHoler holer;
        if (view==null){
            view=View.inflate(context, R.layout.mt_list_combo_item,null);
            holer=new ViewHoler(view);
            view.setTag(holer);
        }else{
            holer= (ViewHoler) view.getTag();
        }
        MTComboDetailsEntity.DataBean.PackageInfoBean packageInfoBean = datas.get(position);
        holer.classFood.setText(packageInfoBean.packageName);
        holer.listView.setAdapter(new ListAdapter(packageInfoBean.content));
        //holer.listView.setAdapter(new FoodItemAdapter());

        return view;
    }

    class ViewHoler{
        TextView classFood;
        ListView listView;

        public  ViewHoler(View  contentView) {
            this.classFood= (TextView) contentView.findViewById(R.id.tv_class_food);
            this.listView= (ListView) contentView.findViewById(R.id.lv_item_content);
        }
    }

    class ListAdapter extends BaseListAdapter{

        public ListAdapter(List<MTComboDetailsEntity.DataBean.PackageInfoBean.ContentBean> content) {
            super(content);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ListHolder listHolder;
            if (view==null){
                view=View.inflate(context,R.layout.mt_list_combo_food_list,null);
                listHolder=new ListHolder(view);
                view.setTag(listHolder);
            }else{
                listHolder= (ListHolder) view.getTag();
            }
            listHolder.listView.setAdapter(new FoodItemAdapter(datas));
            return view;
        }
    }


    class FoodItemAdapter extends BaseListAdapter<MTComboDetailsEntity.DataBean.PackageInfoBean.ContentBean>{

        public FoodItemAdapter(List<MTComboDetailsEntity.DataBean.PackageInfoBean.ContentBean> content) {
            super(content);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            FoodHolder holder;
            if (view==null){
                view=View.inflate(context,R.layout.mt_list_item_food_item,null);
                holder=new FoodHolder(view);
                view.setTag(holder);
            }else{
                holder= (FoodHolder) view.getTag();
            }
            holder.foodName.setText(datas.get(position).name);
            holder.foodCost.setText(MoneyUtil.setNoSmall¥Money(datas.get(position).cost));
            holder.foodNumber.setText(datas.get(position).number+"份");
            return view;
        }


    }


    class ListHolder{
        NoScrollListView listView;
        private ListHolder(View view){
            this.listView= (NoScrollListView) view.findViewById(R.id.listview);
        }
    }

    class FoodHolder{
        TextView foodName;
        TextView foodCost;
        TextView foodNumber;

        public FoodHolder(View contentView){
            this.foodName= (TextView) contentView.findViewById(R.id.tv_food_name);
            this.foodCost= (TextView) contentView.findViewById(R.id.tv_food_price);
            this.foodNumber= (TextView) contentView.findViewById(R.id.tv_food_number);
        }
    }
}
