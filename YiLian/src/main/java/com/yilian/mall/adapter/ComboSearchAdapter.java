package com.yilian.mall.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.MTComboSearchEntity;
import com.yilian.mall.ui.MTComboDetailsActivity;
import com.yilian.mall.ui.MTMerchantDetailActivity;
import com.yilian.mall.utils.AMapDistanceUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by liuyuqi on 2016/12/14 0014.
 */
public class ComboSearchAdapter extends BaseListAdapter<MTComboSearchEntity.DataBean> {

    private int finalPosition;

    public ComboSearchAdapter(List<MTComboSearchEntity.DataBean> datas) {
        super(datas);
    }

    @Override
    public int getCount() {
//        return super.getCount();
        if (datas.size()>=2){
            return 2;
        }else{
            return datas.size();
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ParentHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.combo_lv_item, null);
            holder = new ParentHolder(view);
            view.setTag(holder);
        } else {
            holder = (ParentHolder) view.getTag();
        }

        final MTComboSearchEntity.DataBean parentBean = datas.get(position);

        finalPosition = position;
        Logger.i("parentBean.evaluate" + "dataBean" + parentBean.toString() + "</br> dataBean.merchantName" + parentBean.merchantName);
        holder.merchantName.setText(parentBean.merchantName + "(" + parentBean.merchantAddress + ")");
        //计算点赞数量
        if (null != parentBean.praiseCount) {
            if ("0".equals(parentBean.praiseCount)) {
                holder.merchantRb.setRating(5.0f);
            } else {
                float grade = (Float.parseFloat(parentBean.praiseCount) / 10);
                Logger.i("套餐搜索grade" + grade);
                holder.merchantRb.setRating(grade);
            }
        }
        holder.comboLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent= new Intent(context, MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id",parentBean.merchantId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.ratingCount.setText("被赞" + parentBean.praiseCount + "次");
        //外卖
        if ("1".equals(parentBean.isDelivery)) {
            holder.takeOut.setVisibility(View.VISIBLE);
        } else {
            holder.takeOut.setVisibility(View.GONE);
        }

        //计算距离
        float distance = AMapDistanceUtils.getSingleDistance2(parentBean.merchantLatitude, parentBean.merchantLongitude);
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");
        if (TextUtils.isEmpty(String.valueOf(distance))) {
            holder.discount.setText("计算距离失败");
        } else {
            if (distance > 1000) {
                holder.discount.setText(decimalFormat.format(distance / 1000) + "km");
            } else {
                holder.discount.setText((int)distance + "m");
            }
        }

        holder.listView.setAdapter(new ListItemAdapter(parentBean.packages));

        //点击不同的条目跳转不同的套餐详情
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(context, MTComboDetailsActivity.class);
                intent.putExtra("package_id", parentBean.packages.get(position).packageId);
//                activity继承了context重载了startActivity方法,如果使用acitvity中的startActivity，不会有任何限制。
//                而如果直接使用context的startActivity则会报上面的错误，根据错误提示信息,可以得知,如果要使用这种方式需要打开新的TASK。
//                故,解决方法:
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return view;
    }


    class ListItemAdapter extends BaseListAdapter<MTComboSearchEntity.DataBean.PackagesBean>{

        public ListItemAdapter(List<MTComboSearchEntity.DataBean.PackagesBean> packages) {
            super(packages);
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ItemHolder holder;
            if (view==null){
                view=View.inflate(context,R.layout.mt_item_merchant_body,null);
                holder=new ItemHolder(view);
                view.setTag(holder);
            }else{
                holder= (ItemHolder) view.getTag();
            }
            MTComboSearchEntity.DataBean.PackagesBean comboBean = datas.get(position);
            holder.comboName.setText(comboBean.name);
            imageLoader.displayImage(Constants.ImageUrl+comboBean.packageIcon+ Constants.ImageSuffix,holder.comboIcon,options);
            Logger.i("套餐价格"+comboBean.price);
            holder.comboPrice.setText(MoneyUtil.getLeXiangBiNoZero(comboBean.price));
            holder.sellCount.setText("已售出:"+comboBean.sellCount);
            Logger.i("已售出"+comboBean.sellCount);//null

            return view;
        }
    }


    class ParentHolder {
        RelativeLayout comboLayout;
        TextView merchantName;
        RatingBar merchantRb;
        TextView ratingCount;
        TextView takeOut;//外卖
        TextView discount;
        NoScrollListView listView;
//        TextView lookMore;
        private final View viewIncludeMerchantHead;

        public ParentHolder(View cotentView) {
            this.comboLayout = (RelativeLayout) cotentView.findViewById(R.id.rl_combo_title);
            this.merchantName = (TextView) cotentView.findViewById(R.id.tv_merchant_title);
            this.merchantRb = (RatingBar) cotentView.findViewById(R.id.merchant_ratingBar);
            this.ratingCount = (TextView) cotentView.findViewById(R.id.tv_rating_count);
            this.discount = (TextView) cotentView.findViewById(R.id.tv_discount);
            this.listView = (NoScrollListView) cotentView.findViewById(R.id.lv_combo_item);
            this.takeOut = (TextView) cotentView.findViewById(R.id.tv_take_out);
//            this.lookMore = (TextView) cotentView.findViewById(R.id.tv_look_more);
            viewIncludeMerchantHead = cotentView.findViewById(R.id.view_include_merchant_head);
        }
    }

    class ItemHolder {
        ImageView comboIcon;
        TextView comboName;
        TextView comboPrice;
        TextView sellCount;

        public ItemHolder(View contentView) {
            this.comboIcon = (ImageView) contentView.findViewById(R.id.iv_merchant_icon);
            this.comboName = (TextView) contentView.findViewById(R.id.tv_merchant_name);
            this.comboPrice = (TextView) contentView.findViewById(R.id.tv_merchant_price);
            this.sellCount = (TextView) contentView.findViewById(R.id.tv_sell_count);
        }
    }
}
