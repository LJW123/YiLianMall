package com.yilian.mall.adapter;/**
 * Created by  on 2017/2/25 0025.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.http.ShoppingCartNetRequest;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.fragment.JPShoppingCartFragment;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mall.widgets.NumberAddSubView;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ShoppingCartListEntity2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by  on 2017/2/25 0025.
 */
public class ShoppingCartExpandableListViewAdapter extends BaseExpandableListAdapter {
    protected static DisplayImageOptions options;
    private final Context context;
    private final ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> list;
    private final ShoppingCartNetRequest shoppingCartNetRequest;
    private final JPShoppingCartFragment jpShoppingCartFragment;
    private CheckInterface checkInterface;

    public ShoppingCartExpandableListViewAdapter(ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> list, Context context,
                                                 ShoppingCartNetRequest shoppingCartNetRequest, JPShoppingCartFragment jpShoppingCartFragment) {
        this.list = list;
        this.context = context;
        this.shoppingCartNetRequest = shoppingCartNetRequest;
        this.jpShoppingCartFragment = jpShoppingCartFragment;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;

    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shoppingcart_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        ArrayList<ShoppingCartListEntity2.ListBean> listBeen = list.get(groupPosition);
        int groupChildrenCount = 0;//单组的商品总数量
        for (int i = 0; i < listBeen.size(); i++) {
            ShoppingCartListEntity2.ListBean listBean = listBeen.get(i);
            groupChildrenCount += listBean.goodsCount;
        }
        if (groupPosition == 0) {
            groupViewHolder.viewGroupDivider.setVisibility(View.GONE);
        } else {
            groupViewHolder.viewGroupDivider.setVisibility(View.VISIBLE);
        }
        groupViewHolder.tvGoodsNum.setText("共" + groupChildrenCount + "件商品");//单组的商品总数量
        if (listBeen.size() > 0) {
            groupViewHolder.tvShopName.setText(listBeen.get(0).shopName);//店铺名称(店铺名称在内部商品的数据中，如果购物车有这个店铺，那么一定最少有一个商品，所以此处取每个店铺第一条商品的店铺名称设置给groupItem)
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shoppingcart_child, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        ShoppingCartListEntity2.ListBean listBean = list.get(groupPosition).get(childPosition);
        if (listBean != null) {
            setChildData(groupPosition, childPosition, childViewHolder, listBean);
        }
        return convertView;
    }

    private void setChildData(int groupPosition, int childPosition, ChildViewHolder childViewHolder, ShoppingCartListEntity2.ListBean listBean) {
        checkInterface.checkAll();

        //type 0普通商品 1易划算 2 奖券购
        childViewHolder.tvGoodsNorms.setVisibility(View.GONE);
        childViewHolder.tvGouwuquan.setVisibility(View.GONE);
        childViewHolder.tvGouwuquanIcon.setVisibility(View.GONE);
        switch (listBean.type) {
            case "0":
                childViewHolder.ivYhsIcon.setVisibility(View.GONE);
                childViewHolder.tvLebi.setVisibility(View.VISIBLE);
                childViewHolder.tvLebi.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(String.valueOf((int) listBean.goodsCost))));
                childViewHolder.tvLefen.setVisibility(View.VISIBLE);
                childViewHolder.tvXianjinquan.setVisibility(View.VISIBLE);
                childViewHolder.tvIntegralCost.setVisibility(View.GONE);
                childViewHolder.tvIntegralPrice.setVisibility(View.GONE);
                childViewHolder.tvLedou.setVisibility(View.GONE);
                break;
            case "1":
                childViewHolder.ivYhsIcon.setVisibility(View.VISIBLE);
                childViewHolder.ivYhsIcon.setImageResource(R.mipmap.icon_yhs);
                childViewHolder.tvLebi.setVisibility(View.GONE);
                childViewHolder.tvLefen.setVisibility(View.GONE);
                childViewHolder.tvXianjinquan.setVisibility(View.GONE);
                childViewHolder.tvIntegralCost.setVisibility(View.GONE);
                childViewHolder.tvIntegralPrice.setVisibility(View.VISIBLE);
                childViewHolder.tvIntegralPrice.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(listBean.goodsIntegral)));
                childViewHolder.tvLedou.setVisibility(View.GONE);
                break;
            case "2":
                childViewHolder.ivYhsIcon.setVisibility(View.VISIBLE);
                childViewHolder.ivYhsIcon.setImageResource(R.mipmap.icon_jfg);
                childViewHolder.tvLebi.setVisibility(View.VISIBLE);
                //奖券购价格
                childViewHolder.tvLebi.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(listBean.goodsRetail)));
                childViewHolder.tvLefen.setVisibility(View.GONE);
                childViewHolder.tvIntegralCost.setVisibility(View.VISIBLE);
                childViewHolder.tvIntegralPrice.setVisibility(View.VISIBLE);
                float integralCost = listBean.goodsPrice - listBean.goodsRetail;
                childViewHolder.tvIntegralPrice.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(integralCost)));
                childViewHolder.tvXianjinquan.setVisibility(View.GONE);
                childViewHolder.tvIntegralCost.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
                childViewHolder.tvIntegralCost.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(listBean.goodsPrice)));
                childViewHolder.tvLedou.setVisibility(View.GONE);
                break;
            case "3":
            case GoodsType.CAL_POWER:
                childViewHolder.ivYhsIcon.setVisibility(View.GONE);
                childViewHolder.tvLebi.setVisibility(View.VISIBLE);
                childViewHolder.tvLebi.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(String.valueOf((int) listBean.goodsCost))));
//                原价显示
                childViewHolder.tvLefen.setVisibility(View.VISIBLE);
                childViewHolder.tvXianjinquan.setVisibility(View.GONE);
                childViewHolder.tvIntegralCost.setVisibility(View.GONE);
                childViewHolder.tvIntegralPrice.setVisibility(View.GONE);
                childViewHolder.tvLedou.setVisibility(View.VISIBLE);
                break;
            case "4":
                childViewHolder.ivYhsIcon.setVisibility(View.GONE);
                childViewHolder.tvLebi.setVisibility(View.VISIBLE);
                childViewHolder.tvLebi.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(String.valueOf((int) listBean.goodsCost))));
//                原价隐藏
                childViewHolder.tvLefen.setVisibility(View.GONE);
                childViewHolder.tvXianjinquan.setVisibility(View.GONE);
                childViewHolder.tvIntegralCost.setVisibility(View.GONE);
                childViewHolder.tvIntegralPrice.setVisibility(View.GONE);
                childViewHolder.tvLedou.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        if (listBean.isChecked) {
            childViewHolder.chkShoppingCartGoodsList.setChecked(true);
        } else {
            childViewHolder.chkShoppingCartGoodsList.setChecked(false);
        }
        childViewHolder.chkShoppingCartGoodsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listBean.isChecked = ((CheckBox) v).isChecked();//点击每个商品前面的单选按钮时，改变该商品对应选中状态
                childViewHolder.chkShoppingCartGoodsList.setChecked(((CheckBox) v).isChecked());
                checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());

            }
        });
        childViewHolder.tvGoodsName.setText(list.get(groupPosition).get(childPosition).goodsName);

        childViewHolder.rlShoppingCartInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listBean.goodsStatus == 1) {
                    Toast.makeText(context, "商品已下架", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, JPNewCommDetailActivity.class);
                    intent.putExtra("filiale_id", listBean.filialeId);
                    intent.putExtra("goods_id", listBean.goodsId);
                    intent.putExtra("classify", listBean.type);
                    context.startActivity(intent);
                    Logger.i("购物车跳转详情时获取的filialeId:" + listBean.filialeId + "  goodId:" + listBean.goodsId);
                }
            }
        });

        String goodsIcon = listBean.goodsIcon;
        GlideUtil.showImageWithSuffix(context, goodsIcon, childViewHolder.ivItemShoppingCart);

        childViewHolder.chkShoppingCartGoodsList.setChecked(listBean.isChecked);//选中框
        childViewHolder.tvGoodsName.setText(listBean.goodsName);//商品名
//            childViewHolder.tvGoodsNorms.setText(listBean.goodsNorms);//商品简介\副标题
        childViewHolder.tvLefen.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(String.valueOf((int) listBean.goodsPrice))));
        childViewHolder.tvLefen.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        childViewHolder.tvLefen.setBackgroundColor(Color.WHITE);
        childViewHolder.tvLefen.setTextColor(context.getResources().getColor(R.color.color_999));
        childViewHolder.tvXianjinquan.setText(context.getResources().getString(R.string.back_score) + MoneyUtil.getLeXiangBi(listBean.returnIntegral));
        childViewHolder.tvLedou.setText("送  " + MoneyUtil.getLeXiangBiNoZero(listBean.returnBean));

        float goodsBuyPrice = listBean.goodsPrice;
        childViewHolder.tvGoodsPrice.setText(String.format("%.2f", goodsBuyPrice / 100));


        Logger.i("goodsStatus:" + listBean.goodsStatus);
        switch (listBean.goodsStatus) {//商品状态 1下架、2抢光
            case 1:
                childViewHolder.ivGoodsStatus.setVisibility(View.VISIBLE);
                childViewHolder.ivGoodsStatus.setImageResource(R.mipmap.status_goods_the_shelves);
                break;

            case 2:
                childViewHolder.ivGoodsStatus.setVisibility(View.VISIBLE);
                childViewHolder.ivGoodsStatus.setImageResource(R.mipmap.status_goods_sold_out);
                break;

            default:
                childViewHolder.ivGoodsStatus.setVisibility(View.GONE);
                break;
        }

        childViewHolder.numberAddSub.setValue(listBean.goodsCount);
        childViewHolder.numberAddSub.setMinValue(1);//限制商品最小数量是1

        childViewHolder.numberAddSub.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            //记录上次修改商品数量时，点击减号，减少商品数量值时，本次减少到的数量，如果是1，那么下次将不再继续减少
            int lastValue = childViewHolder.numberAddSub.getValue();

            @Override
            public void onButtonAddClick(View view, int value) {

                listBean.goodsCount = value;
                modifyGoodsCount(listBean.cartId, value);
                checkInterface.checkChild(groupPosition, childPosition, childViewHolder.chkShoppingCartGoodsList.isChecked());

            }

            @Override
            public void onButtonSubClick(View view, int value) {

                if (lastValue == 1) {
                    return;
                }
                Logger.i("减法修改的值：" + value);
                listBean.goodsCount = value;
                modifyGoodsCount(listBean.cartId, value);
                lastValue = value;//记录上次减少到的数量，如果是1 那么下次将不再继续减少，而是直接return。
                checkInterface.checkChild(groupPosition, childPosition, childViewHolder.chkShoppingCartGoodsList.isChecked());
            }
        });

    }

    /**
     * 修改商品数量
     *
     * @param shoppingCartId
     * @param count
     */
    @SuppressWarnings("unchecked")
    private void modifyGoodsCount(String shoppingCartId, int count) {
        jpShoppingCartFragment.startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).modifyShoppingCartNum(
                "cart/update_cart_quantity", shoppingCartId, String.valueOf(count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShoppingCartListEntity2>() {
                    @Override
                    public void onCompleted() {
                        jpShoppingCartFragment.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        jpShoppingCartFragment.stopMyDialog();
                    }

                    @Override
                    public void onNext(ShoppingCartListEntity2 o) {
                        Toast.makeText(context, "修改商品数量成功", Toast.LENGTH_SHORT).show();
                    }
                });
        jpShoppingCartFragment.addSubscription(subscription);
//        shoppingCartNetRequest.modifyShoppingCartQuantity2(shoppingCartId, String.valueOf(count),
//                new RequestCallBack<ShoppingCartListEntity2>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        jpShoppingCartFragment.startMyDialog();
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<ShoppingCartListEntity2> responseInfo) {
//                        switch (responseInfo.result.code) {
//                            case 1:
//                                Toast.makeText(context, "修改商品数量成功", Toast.LENGTH_SHORT).show();
//                                break;
//
//                            case -4:
//                                Toast.makeText(context, "您的登录状态失效", Toast.LENGTH_SHORT).show();
//
//                                break;
//
//                            default:
//                                Toast.makeText(context, "修改商品数量失败", Toast.LENGTH_SHORT).show();
//
//                                break;
//                        }
//                        jpShoppingCartFragment.stopMyDialog();
//                    }
//
//                    @Override
//                    public void onFailure(HttpException e, String s) {
//                        Toast.makeText(context, "修改商品数量失败", Toast.LENGTH_SHORT).show();
//                        jpShoppingCartFragment.stopMyDialog();
//                    }
//                });


    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);

        /**
         * adapter的getView时，及时全选按钮状态修改
         */
        void checkAll();
    }

    public static class GroupViewHolder {
        private final View viewGroupDivider;
        public View rootView;
        public TextView tvShopName;
        public TextView tvGoodsNum;

        public GroupViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvShopName = (TextView) rootView.findViewById(R.id.tv_shop_name);
            this.tvGoodsNum = (TextView) rootView.findViewById(R.id.tv_goods_num);
            viewGroupDivider = rootView.findViewById(R.id.view_group_divider);
        }

    }

    public static class ChildViewHolder {
        public View rootView;
        public CheckBox chkShoppingCartGoodsList;
        public CircleImageView1 ivItemShoppingCart;
        public CircleImageView1 ivGoodsStatus;
        public TextView tvGoodsName;
        public TextView tvLebi;
        public TextView tvLefen;
        public TextView tvGoodsNorms;
        public TextView tvXianjinquan;
        public TextView tvGouwuquan;
        public TextView tvGouwuquanIcon;
        public NumberAddSubView numberAddSub;
        public TextView tvGoodsPrice;
        public LinearLayout rlShoppingCartInner;
        public ImageView ivYhsIcon;
        public TextView tvIntegralPrice;
        public TextView tvIntegralCost;
        public TextView tvLedou;

        public ChildViewHolder(View rootView) {
            this.rootView = rootView;
            this.chkShoppingCartGoodsList = (CheckBox) rootView.findViewById(R.id.chk_shopping_cart_goods_list);
            this.ivItemShoppingCart = (CircleImageView1) rootView.findViewById(R.id.iv_item_shopping_cart);
            this.ivGoodsStatus = (CircleImageView1) rootView.findViewById(R.id.iv_goods_status);
            this.tvGoodsName = (TextView) rootView.findViewById(R.id.tv_goods_name);
            this.tvLebi = (TextView) rootView.findViewById(R.id.tv_lebi);
            this.tvLefen = (TextView) rootView.findViewById(R.id.tv_lefen);
            this.tvGoodsNorms = (TextView) rootView.findViewById(R.id.tv_goods_norms);
            this.tvXianjinquan = (TextView) rootView.findViewById(R.id.tv_xianjinquan);
            this.tvLedou = (TextView) rootView.findViewById(R.id.tv_ledou);
            this.tvGouwuquan = (TextView) rootView.findViewById(R.id.tv_gouwuquan);
            this.tvGouwuquanIcon = (TextView) rootView.findViewById(R.id.tv_gouwuquan_icon);
            this.numberAddSub = (NumberAddSubView) rootView.findViewById(R.id.number_add_sub);
            this.tvGoodsPrice = (TextView) rootView.findViewById(R.id.tv_goods_price);
            this.rlShoppingCartInner = (LinearLayout) rootView.findViewById(R.id.rl_shopping_cart_inner);
            this.ivYhsIcon = (ImageView) rootView.findViewById(R.id.iv_yhs_icon);
            this.tvIntegralPrice = (TextView) rootView.findViewById(R.id.tv_integral_price);
            this.tvIntegralCost = (TextView) rootView.findViewById(R.id.tv_integral_cost);
        }

    }
}
