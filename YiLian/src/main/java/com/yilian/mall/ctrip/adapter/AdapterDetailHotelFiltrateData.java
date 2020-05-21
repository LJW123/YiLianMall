package com.yilian.mall.ctrip.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.bean.FiltrateDataBean;
import com.yilian.mylibrary.ToastUtil;

import rxfamily.utils.StringUtils;

/**
 * 作者：马铁超 on 2018/10/23 14:10
 */

public class AdapterDetailHotelFiltrateData extends BaseQuickAdapter<FiltrateDataBean, BaseViewHolder> {
    public AdapterDetailHotelFiltrateData(int layoutResId) {
        super(layoutResId);
    }

    public interface Ref {
        void Onclick(String title, String key);
    }

    private Ref ref;


    public void setRef(Ref ref) {
        this.ref = ref;
    }

    @Override
    protected void convert(BaseViewHolder helper, FiltrateDataBean item) {
        RecyclerView recyclerView = helper.getView(R.id.rv_filtrate_item);
        helper.setText(R.id.tv_filtrate, item.filtrateTitle);
        AdapterFiltrateDataContent adapter = new AdapterFiltrateDataContent(R.layout.item_third_price);
//        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(item.getDataBeanContents());
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                for (int i = 0; i < item.getDataBeanContents().size(); i++) {
//                    item.getDataBeanContents().get(i).isCheck = false;
//                }
//                if (item.getDataBeanContents().get(position).isCheck == true) {
//                    item.getDataBeanContents().get(position).isCheck = false;
//                } else {
//                    item.getDataBeanContents().get(position).isCheck = true;
//                }
//                adapter.notifyDataSetChanged();
//                ref.Onclick(item.getDataBeanContents().get(position).name,item.getDataBeanContents().get(position).key);
//            }
//        });
        recyclerView.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                int itemView = view.getId();
                switch (itemView) {
                    case R.id.tv_third_price:
//                      下标越界
                        for (int i = 0; i < item.getDataBeanContents().size(); i++) {
                            item.getDataBeanContents().get(i).isCheck = false;
                        }
                        if (item.getDataBeanContents().get(position).isCheck == true) {
                            item.getDataBeanContents().get(position).isCheck = false;
                        } else {
                            item.getDataBeanContents().get(position).isCheck = true;
                        }
                        adapter.notifyDataSetChanged();
                        ref.Onclick(item.getDataBeanContents().get(position).name, item.getDataBeanContents().get(position).key);
                        break;
                }
            }
        });
    }


}
