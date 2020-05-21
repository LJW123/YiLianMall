package com.yilian.mall.adapter;

import android.content.Context;
import android.view.View;

import com.yilian.mall.R;
import com.yilian.mall.entity.MallGoodsListEntity;

import java.util.List;

/**
 * Created by yukun on 2016/4/26.
 */
public class GridLikeGoodsAdapter extends Adapter {

    public GridLikeGoodsAdapter(Context context,List<MallGoodsListEntity.MallGoodsLists> datas){
        super(context,datas);
    }

    @Override
    public int getContentView() {
        return R.layout.item_like_goods;
    }

    @Override
    public void onInitView(View view, Object item, int position) {

    }

//    @Override
//    public void onInitView(View view, int position) {
//
//    }
}
