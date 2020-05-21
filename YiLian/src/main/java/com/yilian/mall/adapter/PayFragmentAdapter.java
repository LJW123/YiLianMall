package com.yilian.mall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilian.mall.MyApplication;
import com.yilian.mall.R;
import com.yilian.mall.entity.PayTypeListEntity;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by liuyuqi on 2016/12/10 0010.
 *  请求支付方式的列表
 */
public class PayFragmentAdapter extends android.widget.BaseAdapter {

    private final ArrayList<PayTypeListEntity.DataBean> listData;
    private final Context context;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private final int selectPosition;
    //创建一个map集合来存储选择的状态
    private Map<Integer, Boolean> selectMap=new HashMap<>();


    public PayFragmentAdapter(ArrayList<PayTypeListEntity.DataBean> listData, ImageLoader imageLoader, DisplayImageOptions options,int selectPosition) {
        context = MyApplication.getInstance();
        this.listData =listData;
        this.imageLoader =imageLoader;
        this.options =options;
        this.selectPosition =selectPosition;
    }

    public void initData(){
        for (int i = 0; i < listData.size(); i++) {
            selectMap.put(i,false);
        }
    }

    @Override
    public int getCount() {
        if (listData.size()>0){
            return listData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            view=View.inflate(context,R.layout.item_pay_fragment_adapter,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        PayTypeListEntity.DataBean dataBean = listData.get(position);
        imageLoader.displayImage(Constants.ImageUrl+dataBean.icon+ Constants.ImageSuffix,holder.mIvIcon,options);
        holder.mTvClassName.setText(dataBean.className);
        holder.mTvClassSubTitle.setText(dataBean.classSubtitle);
        if (dataBean.isuse==0){
            holder.mRL.setBackgroundColor(context.getResources().getColor(R.color.bac_color));
        }
        if (selectPosition==0){
            if (position==0){
                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
            }else{
                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
            }
        }else{
            if (selectPosition==position){
                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_select));
            }else{
                holder.selectImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.merchant_big_is_noselect));
            }
        }

        return view;
    }

    public class ViewHolder {
        public RelativeLayout mRL;
        public View rootView;
        public ImageView mIvIcon;
        public TextView mTvClassName;
        public TextView mTvClassSubTitle;
        public ImageView selectImg;

        public ViewHolder(View rootView){
            this.rootView=rootView;
            this.mIvIcon = (ImageView) rootView.findViewById(R.id.iv_icon);
            this.mTvClassName = (TextView) rootView.findViewById(R.id.tv_class_name);
            this.mTvClassSubTitle = (TextView) rootView.findViewById(R.id.tv_class_sub_title);
            this.mRL = (RelativeLayout) rootView.findViewById(R.id.rl);
            this.selectImg = (ImageView) rootView.findViewById(R.id.commit_express_icon);
        }
    }
}
