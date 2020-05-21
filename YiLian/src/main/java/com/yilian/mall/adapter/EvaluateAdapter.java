package com.yilian.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.lidroid.xutils.BitmapUtils;
import com.yilian.mall.R;
import com.yilian.mall.adapter.layoutManager.FullyLinearLayoutManager;
import com.yilian.mall.entity.EvaluateList;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/22.
 */
public class EvaluateAdapter extends SimpleAdapter<EvaluateList.Evaluate> {

    private Context context;
    BitmapUtils bitmapUtils;

    RecyclerView rv;

    public EvaluateAdapter(Context context, ArrayList<EvaluateList.Evaluate> list) {
        super(context,R.layout.item_evaluate,list);
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, EvaluateList.Evaluate item, int position) {

        GlideUtil.showImageWithSuffix(context,item.icon,viewHolder.getImageView(R.id.iv_user));
        viewHolder.getTextView(R.id.tv_user_name).setText(item.commentConsumer);
        viewHolder.getTextView(R.id.tv_time).setText(StringFormat.formatDate(item.commentTime,"yyyy-MM-dd"));
        viewHolder.getTextView(R.id.tv_evaluate).setText(item.commentContent);
        ((RatingBar)viewHolder.getView(R.id.rb_evaluate)).setRating(item.commentScore);
        rv = (RecyclerView) viewHolder.getView(R.id.rv_evaluate);

        String [] a = item.commentImages.split(",");

        ArrayList<String>  list = new ArrayList<>();
        for (String b:a)
            list.add(b);

        if (list == null||list.size()<1){
            rv.setVisibility(View.GONE);
        }else{
            rv.setVisibility(View.VISIBLE);
            rv.setAdapter(new ImageAdapter(context,list,bitmapUtils));
            rv.setLayoutManager(new FullyLinearLayoutManager(context, LinearLayout.HORIZONTAL,false));
        }

    }




}
