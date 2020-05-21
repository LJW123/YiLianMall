package com.yilian.mall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.Charge;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/25.
 */
public class ChargePriceAdapter extends SimpleAdapter<Charge>  {

    ArrayList<Charge> list;

    private TextView tvDescripe;
    private LinearLayout ll;


    public ChargePriceAdapter(Context context, ArrayList<Charge> list) {
        super(context, R.layout.item_charge_price, list);
        this.list = list;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Charge item, int position) {
        ll = (LinearLayout) viewHoder.getView(R.id.ll);


        String descripe = item.descripe;

        viewHoder.itemView.setTag(item);

        viewHoder.getTextView(R.id.tv_price).setText(item.price + "元");

        tvDescripe = viewHoder.getTextView(R.id.tv_descripe);

        if (TextUtils.isEmpty(descripe)) {
            tvDescripe.setVisibility(View.GONE);
        } else {
            tvDescripe.setVisibility(View.VISIBLE);
            tvDescripe.setText(descripe);
        }

        if (item.flag) {
            ll.setBackgroundResource(R.mipmap.ic_warning);
        } else {
            ll.setBackgroundResource(R.drawable.bg_charge_blue);
        }
    }

}
