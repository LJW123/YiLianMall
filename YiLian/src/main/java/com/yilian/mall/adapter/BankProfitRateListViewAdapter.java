package com.yilian.mall.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.BankProfitRateEntity;
import com.yilian.mall.utils.NumberFormat;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class BankProfitRateListViewAdapter extends android.widget.BaseAdapter {

    private ArrayList<BankProfitRateEntity.ListBean> list;
    private Context context;

    public BankProfitRateListViewAdapter() {
    }

    public BankProfitRateListViewAdapter(Context context, ArrayList<BankProfitRateEntity.ListBean> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_bank_profit_rate_list_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        setViewHolderData(position,holder);
        return convertView;
    }

    private void setViewHolderData(int position, ViewHolder holder) {
        BankProfitRateEntity.ListBean bankProfitRateEntity = list.get(position);
        holder.tv_bank_profit_rate_section.setText(bankProfitRateEntity.msg);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_bank_profit_rate.getLayoutParams();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getSize(outSize);
        int x = outSize.x;//屏幕宽度
        int y = outSize.y;//屏幕高度
        layoutParams.width=(int)( NumberFormat.convertToDouble(bankProfitRateEntity.percentage,0.0)/100*x);
        holder.iv_bank_profit_rate.setLayoutParams(layoutParams);
        holder.tv_bank_profit_rate.setText(bankProfitRateEntity.percentage+"%");
    }

    public static class ViewHolder {

        public View rootView;
        public ImageView iv_bank_profit_rate;
        public TextView tv_bank_profit_rate_section;
        private  TextView tv_bank_profit_rate;
        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_bank_profit_rate = (ImageView) rootView.findViewById(R.id.iv_bank_profit_rate);
            this.tv_bank_profit_rate_section = (TextView) rootView.findViewById(R.id.tv_bank_profit_rate_section);
            this.tv_bank_profit_rate = (TextView) rootView.findViewById(R.id.tv_bank_profit_rate);
        }

    }
}
