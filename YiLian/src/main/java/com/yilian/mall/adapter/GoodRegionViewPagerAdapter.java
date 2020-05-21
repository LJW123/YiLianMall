package com.yilian.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallGoodsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class GoodRegionViewPagerAdapter extends BaseViewPagerAdapter {

    private Context mContext;
    private List<List<MallGoodsList>> mList;
    private List<ListView> mListViews;

    public GoodRegionViewPagerAdapter(Context context,List<List<MallGoodsList>> list){
        this.mContext=context;
        this.mList=list;
        mListViews=new ArrayList<>();
        getItemViews();
    }

    public void setDataView(int position,boolean hasData){
        ListView list=mListViews.get(position);
        ImageView imgNoData= (ImageView) mViewList.get(position).findViewById(R.id.no_data);
        if (hasData){
            list.setVisibility(View.VISIBLE);
            imgNoData.setVisibility(View.GONE);
        }else {
            list.setVisibility(View.GONE);
            imgNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        for (ListView v:mListViews) {
            ((BaseAdapter)v.getAdapter()).notifyDataSetChanged();

        }
    }

    public void notifyDataSetChanged(int position){
        ((BaseAdapter)mListViews.get(position).getAdapter()).notifyDataSetChanged();
    }


    @Override
    public void getItemViews() {
        int count=mList.size();
        for (int i=0;i<count;++i){
            View view= LayoutInflater.from(mContext).inflate(R.layout.item_good_region_viewpager,null);
            ListView list= (ListView) view.findViewById(R.id.lv_filiale_goods);
            MallGoodsListAdapter adapter =new MallGoodsListAdapter(mContext,mList.get(i));
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String goodsId = ((MallGoodsList) adapterView.getItemAtPosition(i)).goodsId;
                    Intent intent = new Intent(mContext, BaseActivity.class);
                    intent.putExtra("good_id", goodsId);
                    mContext.startActivity(intent);
                }
            });
            mViewList.add(view);
            mListViews.add(list);
        }
    }


}
