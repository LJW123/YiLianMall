package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPMyFavoriteGoodsEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.ui.JPMyFavoriteActivity;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.XlistView.XListView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liuyuqi on 2016/10/21 0021.
 * 收藏-商品
 */

public class JPFavoriteGooodsFragment extends JPBaseFragment implements XListView.IXListViewListener {

    public boolean isCompile = false;
    public boolean isNothing = false;
    StringBuilder sbCollectIds = new StringBuilder();
    List<String> checkCollectIds = new ArrayList<>();
    List<Boolean> isAllSelectFlags = new ArrayList<>();
    private CollectGoodsAdapter adapter;
    private XListView listView;
    private MTNetRequest requestData;
    private LinearLayout ll_select;
    private CheckBox tv_select;
    private Button btn_cancle;
    private CollectGoodsHolder holder;
    private ImageView imageView;
    // 用来存所有 checkbox 的选中状态
    private Map<Integer, Boolean> flags = new HashMap<>();
    private JPMyFavoriteActivity context;
    private JPNetRequest cancleFavorite;
    private ArrayList<JPMyFavoriteGoodsEntity.ListBean> goodsList;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_favorite_fragment, container, false);
        context = (JPMyFavoriteActivity) getActivity();
        imageView = (ImageView) view.findViewById(R.id.imageView);
        listView = (XListView) view.findViewById(R.id.lv_favorite_content);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);//刷新
        listView.setPullLoadEnable(false);//加载更多
        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        tv_select = (CheckBox) view.findViewById(R.id.tv_all_select);
        btn_cancle = (Button) view.findViewById(R.id.cancel);

        initData();
        initListenr();
        return view;
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initListenr() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JPMyFavoriteGoodsEntity.ListBean item = (JPMyFavoriteGoodsEntity.ListBean) adapter.getItem(position - 1);
                if ("0".equals(item.status)) {
                    showToast("当前商品已下架");
                    return;
                }

                Intent intent = new Intent(getActivity(), JPNewCommDetailActivity.class);
                intent.putExtra("goods_id", item.collectId);
                intent.putExtra("filiale_id", item.filialeId);
                intent.putExtra("classify", item.type);
                startActivity(intent);
            }
        });
    }


    @Override //给按钮设置点击事件
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    for (int i = 0; i < goodsList.size(); i++) {
                        flags.put(i, true);
                    }
                } else {
                    for (int i = 0; i < goodsList.size(); i++) {
                        flags.put(i, false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_select.setChecked(false);
                cancle();
            }
        });
    }

    //取消收藏
    private void cancle() {
        sbCollectIds.delete(0, sbCollectIds.length());
        checkCollectIds.clear();

        for (int i = 0; i < goodsList.size(); i++) {
            if (flags.get(i)) {
                checkCollectIds.add(goodsList.get(i).collectIndex);
            }
        }

        for (int i = 0; i < checkCollectIds.size(); i++) {
            if (i == checkCollectIds.size() - 1) {
                sbCollectIds.append(checkCollectIds.get(i));
            } else {
                sbCollectIds.append(checkCollectIds.get(i) + ",");
            }
        }

        String sbCollectIdsStr = sbCollectIds.toString();
        if (sbCollectIdsStr.length() <= 0 && !isNothing) {
            showToast("请选择需要取消的商品");
            return;
        } else if (sbCollectIdsStr.length() <= 0 && isNothing) {
            showToast("亲，没有可取消的商品哦");
            return;
        }
        initCancleData(sbCollectIdsStr);
    }

    //请求服务器取消收藏
    private void initCancleData(String sbCollectIdsStr) {
        startMyDialog();
        if (cancleFavorite == null) {
            cancleFavorite = new JPNetRequest(getActivity());
        }
        cancleFavorite.getCancleFavoriteist(sbCollectIdsStr, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                initActivirtyState(false);
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("取消成功");
                        //刷新个人页面标识
                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                        initData();
                        break;
                    case -3:
                        showToast("系统繁忙，请稍后重试");
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("取消收藏失败");
            }
        });
    }

    public void initActivirtyState(boolean isChecked) {
        this.isCompile = isChecked;
        if (isCompile) {
            ll_select.setVisibility(View.VISIBLE);
        } else if (!isCompile) {
            ll_select.setVisibility(View.GONE);
            tv_select.setChecked(false);
        } else {
            isCompile = false;
        }

        adapter.isCompile = isCompile;
        if (adapter.isCompile) {
            adapter.notifyDataSetChanged();
        }
    }

    private void initData() {
        if (requestData == null) {
            requestData = new MTNetRequest(mContext);
        }
        startMyDialog();
        //商品新增type字段
        requestData.getMtFavoriteGoodsList(new RequestCallBack<JPMyFavoriteGoodsEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPMyFavoriteGoodsEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        goodsList = (ArrayList<JPMyFavoriteGoodsEntity.ListBean>) responseInfo.result.list;
                        if (goodsList.size() > 0) {
                            isNothing = false;
                            imageView.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            if (adapter == null)
                                adapter = new CollectGoodsAdapter(goodsList, isCompile);
                            listView.setAdapter(adapter);
                        } else {
                            imageView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            isNothing = true;
                        }

                        adapter.setData(goodsList);
                        tv_select.setChecked(false);
                        stopMyDialog();
                        listView.stopRefresh();
                        break;
                    //新的接口不处理-18这样的返回值
                    case -18://没有该种类的收藏
                        imageView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        isNothing = true;
                        stopMyDialog();
                        listView.stopRefresh();
                        break;
                    case -4:
                    case -23:
                    case -24:
                        showToast(R.string.login_failure);
                        stopMyDialog();
                        listView.stopRefresh();
                        if (goodsList.size() <= 0) {
                            imageView.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                listView.stopRefresh();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void bianli() {
        isAllSelectFlags.clear();
        for (int i = 0; i < goodsList.size(); i++) {
            isAllSelectFlags.add(flags.get(i));
        }
        if (isAllSelectFlags.contains(false)) {
            tv_select.setChecked(false);
        } else {
            tv_select.setChecked(true);
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
    }

    class CollectGoodsAdapter extends BaseAdapter {

        private List<JPMyFavoriteGoodsEntity.ListBean> datas;
        private Boolean isCompile;
        private DisplayImageOptions options;

        public CollectGoodsAdapter(List<JPMyFavoriteGoodsEntity.ListBean> datas, Boolean isCompile) {
            this.datas = datas;
            this.isCompile = isCompile;
            for (int i = 0; i < datas.size(); i++) {
                flags.put(i, false);
            }

            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                    .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
                    .build();
            imageLoader = ImageLoader.getInstance();

        }

        @Override
        public int getCount() {
            if (datas.size() == 0) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            Log.e("adapter的getItem", datas.get(i) + "datasSIZE" + datas.size());
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(JPFavoriteGooodsFragment.this.getActivity(), R.layout.item_favorite_goods, null);
                holder = new CollectGoodsHolder();
                holder.llBg = (LinearLayout) view.findViewById(R.id.ll_bg);
                holder.iv_select = (CheckBox) view.findViewById(R.id.cb_select);
                holder.goodsIcon = (ImageView) view.findViewById(R.id.imageView);
                holder.goodsName = (TextView) view.findViewById(R.id.tv_goods_name);
                holder.goodsCostPrice = (TextView) view.findViewById(R.id.tv_cost_price);
                holder.goodsMarketPrice = (TextView) view.findViewById(R.id.tv_market_price);
                holder.goodsUserQuan = (TextView) view.findViewById(R.id.tv_use_quan);
                holder.toLook = (TextView) view.findViewById(R.id.tv_to_look);
                holder.imageViewNon = (ImageView) view.findViewById(R.id.imageView_non);
                holder.ivYhsIcon = (ImageView) view.findViewById(R.id.iv_yhs_icon);//易划算货奖券购的小图标
                holder.tvGoodsIntegral = (TextView) view.findViewById(R.id.tv_goods_integral);
                holder.tvIntegralCost = (TextView) view.findViewById(R.id.tv_integral_cost);
                view.setTag(holder);
            } else {
                holder = (CollectGoodsHolder) view.getTag();
            }

            final int finalPosition = position;
            JPMyFavoriteGoodsEntity.ListBean listBean = datas.get(position);

            if (position == 0) {
                holder.llBg.setVisibility(View.VISIBLE);
            } else {
                holder.llBg.setVisibility(View.GONE);
            }
            holder.goodsName.setText(listBean.collectName);
//            listBean.collectType="8";
            int disPrice;
            Logger.i("listBean.collectType  " + listBean.collectType);
            if (!TextUtils.isEmpty(listBean.type)) {
                switch (listBean.type) {
                    case "0"://普通商品
                        holder.ivYhsIcon.setVisibility(View.GONE);
                        holder.goodsCostPrice.setVisibility(View.VISIBLE);
                        holder.goodsMarketPrice.setVisibility(View.VISIBLE);
                        holder.tvGoodsIntegral.setVisibility(View.GONE);
                        holder.tvIntegralCost.setVisibility(View.INVISIBLE);
                        holder.goodsCostPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(listBean.collectCost)));
                        holder.goodsMarketPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(listBean.collectPrice)));
                        //价格设置中划线
                        holder.goodsMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        holder.goodsUserQuan.setVisibility(View.VISIBLE);
                        holder.goodsUserQuan.setText(getString(R.string.back_score) + MoneyUtil.getLeXiangBi(listBean.returnIntegral));
                        break;
                    case "1"://易划算
                        holder.goodsCostPrice.setVisibility(View.GONE);
                        holder.ivYhsIcon.setVisibility(View.VISIBLE);
                        holder.ivYhsIcon.setImageResource(R.mipmap.icon_yhs);
                        holder.tvGoodsIntegral.setVisibility(View.VISIBLE);
                        holder.goodsUserQuan.setVisibility(View.GONE);
                        holder.tvIntegralCost.setVisibility(View.GONE);
                        holder.goodsMarketPrice.setVisibility(View.GONE);
                        holder.tvIntegralCost.setVisibility(View.INVISIBLE);
                        holder.tvGoodsIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(listBean.goodsIntegral)));
                        //零购券
                        break;
                    case "2"://奖券购
                        holder.ivYhsIcon.setVisibility(View.VISIBLE);
                        holder.goodsUserQuan.setVisibility(View.GONE);
                        holder.ivYhsIcon.setImageResource(R.mipmap.icon_jfg);
                        holder.goodsMarketPrice.setVisibility(View.GONE);
                        holder.goodsCostPrice.setVisibility(View.VISIBLE);
                        holder.tvGoodsIntegral.setVisibility(View.VISIBLE);
                        holder.goodsCostPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(listBean.collectCost)));
                        float hasIntegral = Float.parseFloat(listBean.collectPrice) - Float.parseFloat(listBean.collectCost);
                        holder.tvGoodsIntegral.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(hasIntegral)));
                        holder.tvIntegralCost.setVisibility(View.VISIBLE);
                        holder.tvIntegralCost.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.tvIntegralCost.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(listBean.collectPrice)));
                        break;
                }
            }
            if (adapter.isCompile) {
                holder.iv_select.setVisibility(View.VISIBLE);
            } else {
                holder.iv_select.setVisibility(View.GONE);
                for (int i = 0; i < datas.size(); i++) {
                    flags.put(i, false);
                }
                holder.iv_select.setChecked(flags.get(position));
            }


            holder.iv_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    flags.put(finalPosition, ((CheckBox) view).isChecked());
                    bianli();
                }
            });

            //防止刷新的时候重新加载图片
            if ("0".equals(listBean.status)) {
                holder.imageViewNon.setVisibility(View.VISIBLE);
            } else {
                holder.imageViewNon.setVisibility(View.GONE);
            }
            GlideUtil.showImageWithSuffix(mContext, listBean.collectIcon, holder.goodsIcon);

            return view;
        }

        public void setData(ArrayList<JPMyFavoriteGoodsEntity.ListBean> data) {
            this.datas = data;
            for (int i = 0; i < datas.size(); i++) {
                flags.put(i, false);
            }
            notifyDataSetChanged();
        }
    }

    class CollectGoodsHolder {
        LinearLayout llBg;
        CheckBox iv_select;
        ImageView goodsIcon;
        TextView goodsName;
        TextView goodsCostPrice;
        TextView goodsMarketPrice;
        TextView goodsUserQuan;
        TextView toLook;
        ImageView imageViewNon;
        ImageView ivYhsIcon;
        TextView tvGoodsIntegral;
        TextView tvIntegralCost;


    }

}