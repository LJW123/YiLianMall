package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.BankProfitEntity;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.StringFormat;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/27.
 */
public class BankProfitListViewAdapter extends android.widget.BaseAdapter {

    private ArrayList<BankProfitEntity.ListBean> list;
    private Context context;
    private double maxProfit = 0.0;

    public BankProfitListViewAdapter() {
    }

    public BankProfitListViewAdapter(Context context) {
        this.context = context;
    }

    public BankProfitListViewAdapter(Context context, ArrayList<BankProfitEntity.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holer = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_bank_profit_list_view, null);
            holer = new ViewHolder(convertView);
            convertView.setTag(holer);
        } else {
            holer = (ViewHolder) convertView.getTag();
        }
        setViewHolderData(position, holer);
        return convertView;
    }

    private void setViewHolderData(int position, ViewHolder holer) {
        double temp;
        for (BankProfitEntity.ListBean listBean1 : list) {
            temp = NumberFormat.convertToDouble(listBean1.dailyProfit, 0.0);//临时值等于每笔领奖励
            if (maxProfit < temp) {
                maxProfit = temp;//获得所有领奖励中的最大领奖励
            }
        }

        BankProfitEntity.ListBean listBean = list.get(position);
        {//设置每个条目的宽度，以集合内最大领奖励对应屏幕宽度为准，其他数值按照对应比例设置宽度
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Point outSize = new Point();
            windowManager.getDefaultDisplay().getSize(outSize);
            int x = outSize.x/2;//屏幕宽度的一半
            int y = outSize.y;//屏幕高度
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holer.rl_bank_profit_detail.getLayoutParams();
            //以所有领奖励中最大领奖励对应屏幕一半为基准计算出该笔领奖励对应的宽度
            if(maxProfit!=0){
                //最大领奖励不为0时
                layoutParams.width = (int) ((x * NumberFormat.convertToDouble(listBean.dailyProfit, 0.0)) / maxProfit)+x;
            }else{
                //最大领奖励为0时（即 所有领奖励都是0时），所有宽度都是屏幕的一半
                layoutParams.width = x;
            }
            holer.rl_bank_profit_detail.setLayoutParams(layoutParams);
        }
        if(position==0){
            holer.rl_bank_profit_detail.setBackgroundResource(R.color.cfe9c29);
        }else{
            holer.rl_bank_profit_detail.setBackgroundResource(R.color.ccdd0d3);
        }
        holer.tv_bank_profit_date.setText(StringFormat.formatDate(listBean.profitTime, "yyyy-MM-dd"));//每笔领奖励时间
        holer.tv_bank_profit_money.setText(String.format("%.2f", NumberFormat.convertToDouble(listBean.dailyProfit, 0.0) / 100));//每笔领奖励金额
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tv_bank_profit_date;
        public TextView tv_bank_profit_money;
        public LinearLayout rl_bank_profit_detail;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_bank_profit_date = (TextView) rootView.findViewById(R.id.tv_bank_profit_date);
            this.tv_bank_profit_money = (TextView) rootView.findViewById(R.id.tv_bank_profit_money);
            this.rl_bank_profit_detail = (LinearLayout) rootView.findViewById(R.id.rl_bank_profit_detail);
        }

    }
}
