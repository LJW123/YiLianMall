package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BannerViewPager;
import com.yilian.mall.listener.onLoadErrorListener;
import com.yilian.mall.ui.SpellGroupDetailsActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.networkingmodule.entity.SpellGroupDetailsEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/5/12 0012.
 */
public class SpellGroupDetailsTopFragment extends BaseFragment {

    private ViewPager viewPager;
    private LinearLayout llPoints;
    private TextView tvName;
    private TextView tvCost;
    private TextView tvPrice;
    private TextView tvTotalCount;
    private TextView tvFlow;
    private SpellGroupDetailsActivity activity;
    private onLoadErrorListener onLoadErrorListener;
    private BannerViewPager viewpagerAdaper;
    private LinearLayout llDrawingProgress;
    public String goodsInfoUrl;
    public String goodsId;
    public int goodsFiliale;
    public SpellGroupDetailsEntity.DataBean groupData;
    private ArrayList<ImageView> dotImageViews = new ArrayList<>();
    private List<String> goodsAlbum;
    public Map<String, SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean> mSelectedProperty = new HashMap<>();
    public ArrayList<String> selectSkuList;
    public String sku, skuCount;
    private TextView tvLabel1;
    private TextView tvLabel2;
    private TextView tvLabel3;
    private TextView tvLabel4;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_group_details_top, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        llPoints = (LinearLayout) view.findViewById(R.id.ll_points);
        tvName = (TextView) view.findViewById(R.id.tv_goods_name);
        tvCost = (TextView) view.findViewById(R.id.tv_goods_cost);
        tvPrice = (TextView) view.findViewById(R.id.tv_goods_price);
        tvTotalCount = (TextView) view.findViewById(R.id.tv_total_sell_group_count);
        tvFlow = (TextView) view.findViewById(R.id.tv_sell_group_flow);
        llDrawingProgress = (LinearLayout) view.findViewById(R.id.ll_drawing_progress);
        tvLabel1 = (TextView) view.findViewById(R.id.tv_label1);
        tvLabel2 = (TextView) view.findViewById(R.id.tv_label2);
        tvLabel3 = (TextView) view.findViewById(R.id.tv_label3);
        tvLabel4 = (TextView) view.findViewById(R.id.tv_label4);
        activity = (SpellGroupDetailsActivity) getActivity();

        initListener();
        return view;
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int finalPosition;
            private int lastPostion;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            private void initChageDot(int position) {
                initAllDots();//改变点位之前先将所有的点置灰
                if (groupData.goodsAlbum.size() != 0) {
                    position = position % groupData.goodsAlbum.size();
                    if (position != 0) {
                        position = position - 1;
                    } else {
                        position = groupData.goodsAlbum.size() - 1;
                    }
                    View childAt = llPoints.getChildAt(lastPostion);
                    if (childAt != null) {
                        childAt.setEnabled(false);
                    }
                    View childAt1 = llPoints.getChildAt(position);
                    if (childAt1 != null) {
                        childAt1.setEnabled(true);
                    }
                    lastPostion = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                finalPosition = position;
                initChageDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // 页面切换   自动的切换到对应的界面    最后一个A----->第一个A
                    if (finalPosition == viewpagerAdaper.getCount() - 1) {
                        //最后一个元素  是否平滑切换
                        viewPager.setCurrentItem(1, false);
                    } else if (finalPosition == 0) {
                        //是第一个元素{D] ----> 倒数第二个元素[D]
                        viewPager.setCurrentItem(viewpagerAdaper.getCount() - 2, false);
                    }
                }
            }
        });
        llDrawingProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constants.spellGroupRefundExplain);
                startActivity(intent);

            }
        });
    }

    @Override
    public void loadData() {
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(activity)).setDeviceIndex(RequestOftenKey.getDeviceIndex(activity))
                .getSpellGroupDetailsData(activity.index, new Callback<SpellGroupDetailsEntity>() {
                    @Override
                    public void onResponse(Call<SpellGroupDetailsEntity> call, Response<SpellGroupDetailsEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(activity, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        groupData = response.body().data;
                                        initViewData(groupData);
                                        if (null != onLoadErrorListener) {
                                            onLoadErrorListener.isError(false);
                                        }
                                        break;
                                    case -17:
                                        if (null != onLoadErrorListener) {
                                            onLoadErrorListener.isError(true);
                                        }
                                        break;
                                    default:
                                        Logger.i(getClass().getSimpleName() + response.body().code + response.body().msg);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SpellGroupDetailsEntity> call, Throwable t) {
                        if (null != onLoadErrorListener) {
                            onLoadErrorListener.isError(true);
                        }
                    }
                });
    }

    public String noskuPropertyTxt(int count) {
        return "已选择 " + count + " 件";
    }

    /**
     * 获取已选择的商品规格
     *
     * @param selectSkuList
     * @return
     */
    public String staticPropertyText(List<String> selectSkuList) {
        String selectedValues = "";
        String selectedId = "";

        if (selectSkuList != null && selectSkuList.size() != 0 && selectSkuList != null) {
            for (int i = 0; i < selectSkuList.size(); i++) {
                String value = selectSkuList.get(i);

                for (SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean property : groupData.goodsSkuValues) {
                    if (null == property.skuIndex && null == property.skuParentId) {
                        selectedValues = "";
                        selectedId = "";
                    } else if (null != property.skuParentId && null != property.skuIndex) {
                        String item = property.skuParentId + ":" + property.skuIndex;
                        if (item.equals(value)) {
                            selectedValues += property.skuName + ", ";
                            selectedId += property.skuParentId + ":" + property.skuIndex + "，";
                        }
                    }
                }
            }
            Logger.i("goodsSkuValues:" + groupData.goodsSkuValues);
            Logger.i("selectedValues:" + selectedValues);
            Logger.i("selectedId:" + selectedId);
            return "已选择 " + selectedValues + "1件";
        }
        return null;
    }

    /**
     * 获取商品属性选择结果字串
     */
    public String getPropertyTxt(int count) {
        String selectedValues = "";

        if (groupData != null) {
            int selectedPropertyCount = mSelectedProperty.size(); //我的选择
            Logger.i("2016-12-15mSelectedProperty:" + mSelectedProperty);
            int propertyCount = groupData.goodsSkuProperty.size(); //商品选择--2
            if (propertyCount > 0) {
                if (selectedPropertyCount == propertyCount) {
                    for (SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean property : groupData.goodsSkuProperty) {
                        selectedValues += mSelectedProperty.get(property.skuIndex).skuName + " ";
                    }
                    return "已选择 " + selectedValues + count + "件";
                } else if (selectedPropertyCount < propertyCount) {
                    for (SpellGroupDetailsEntity.DataBean.GoodsSkuPropertyBean property : groupData.goodsSkuProperty) {
                        if (null == mSelectedProperty.get(property.skuIndex)) {
                            selectedValues += property.skuName + " ";
                        }
                    }
                    return "请选择 " + selectedValues + count + "件";
                }
            }
        }
        return null;
    }

    public void initAllDots() {
        for (int i = 0; i < dotImageViews.size(); i++) {
            dotImageViews.get(i).setEnabled(false);
        }
    }

    private void initViewData(SpellGroupDetailsEntity.DataBean groupData) {
        if (null != groupData.goodsAlbum) {
            initViewPager(groupData.goodsAlbum);
        }
        if (null == groupData.goodsName) {

        } else {
            tvName.setText(groupData.goodsName);
        }
        if (!TextUtils.isEmpty(groupData.amount)) {
            tvCost.setText(MoneyUtil.setNoSmallMoney(MoneyUtil.getLeXiangBi(groupData.goodsPrice)));
            tvCost.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (!TextUtils.isEmpty(groupData.goodsPrice)) {
            tvPrice.setText(MoneyUtil.setNoSmall¥Money(MoneyUtil.getLeXiangBi(groupData.amount)));
        }
        if (!TextUtils.isEmpty(groupData.groupCondition) && !TextUtils.isEmpty(groupData.totalNumber)) {

            tvTotalCount.setText(Html.fromHtml("已有<font color=\"#F76D02\">" + NumberFormat.convertToTenThousand(groupData.groupCondition, groupData.totalNumber) + "</font>人参团"));
        }

        tvFlow.setText("活动时间:" + DateUtils.timeStampToStr(Long.parseLong(groupData.startTime)) + "-" + DateUtils.timeStampToStr(Long.parseLong(groupData.endTime))
                +"\n\n1、活动结束后，拼团成功用户即可获得抽奖资格"
                + "\n\n2、中奖商品为“" + groupData.goodsName + "(共"+groupData.prizeNumber+");未中奖用户将全额退款。成团越多，中奖率越高哦~"
                + "\n\n3、中奖名单将于" + DateUtils.timeStampToStr(Long.parseLong(groupData.endTime)) + "公布，并于次日陆续发货\n\n4、中奖商品为拼团抽奖奖品，不享受退换货服务" +
                "\n\n5、奖品图片仅供参考，以实物为准");

        //商品信息和商品参数的网址拼接
        goodsInfoUrl = Ip.getWechatURL(mContext) + "m/goods/contentPic.php?goods_id=" + groupData.goodsIndex + "&filiale_id=" + groupData.goodsFiliale + "&goods_supplier=" + groupData.goodsSupplier;
        goodsId = groupData.goodsId;
        goodsFiliale = groupData.goodsFiliale;
        llDrawingProgress.setVisibility(View.VISIBLE);

        //获取当前选择的商品规格
        if (!TextUtils.isEmpty(groupData.goodsSku) && groupData.goodsSku instanceof String) {
            sku = groupData.goodsSku;
            String[] arraySku = sku.split(",");
            selectSkuList = new ArrayList<>(Arrays.asList(arraySku));
        }
        skuCount = String.valueOf(groupData.goodsSkuCount);
        staticPropertyText(selectSkuList);
        if (!TextUtils.isEmpty(groupData.descTags.get(0))) {
            tvLabel1.setVisibility(View.VISIBLE);
            tvLabel1.setText(groupData.descTags.get(0));
        }else {
            tvLabel1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(groupData.descTags.get(1))) {
            tvLabel2.setVisibility(View.VISIBLE);
            tvLabel2.setText(groupData.descTags.get(1));
        }else {
            tvLabel2.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(groupData.descTags.get(2))) {
            tvLabel3.setVisibility(View.VISIBLE);
            tvLabel3.setText(groupData.descTags.get(2));
        }else {
            tvLabel3.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(groupData.descTags.get(3))) {
            tvLabel4.setVisibility(View.VISIBLE);
            tvLabel4.setText(groupData.descTags.get(3));
        }else {
            tvLabel4.setVisibility(View.GONE);
        }

    }

    private void initViewPager(List<String> goodsAlbum) {
        this.goodsAlbum = goodsAlbum;
        if (null != this.goodsAlbum && goodsAlbum.size() > 0) {
            viewpagerAdaper = new BannerViewPager(goodsAlbum, imageLoader, options, activity, true);
            viewPager.setAdapter(viewpagerAdaper);
            viewPager.setCurrentItem(1);
        }
        initPionts(goodsAlbum);
    }

    private void initPionts(List<String> goodsAlbum) {
        llPoints.removeAllViews();
        dotImageViews.clear();
        if (goodsAlbum.size()<1){
            llPoints.setVisibility(View.GONE);
        }else{
            for (int i = 0; i < goodsAlbum.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.rightMargin = DPXUnitUtil.dp2px(mContext, 10);
                imageView.setLayoutParams(layoutParams);
                dotImageViews.add(imageView);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.lldot_white_enable));

                if (i == 0) {
                    imageView.setEnabled(true);
                } else {
                    imageView.setEnabled(false);
                }
                llPoints.addView(imageView);
            }
        }

    }

    public void setOnLoadErrorListener(onLoadErrorListener listener) {
        this.onLoadErrorListener = listener;
    }

}
