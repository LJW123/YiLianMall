package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.yilian.mall.entity.MtMyFavoriteComboEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.ui.MTComboDetailsActivity;
import com.yilian.mall.utils.AMapDistanceUtils;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.XlistView.XListView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liuyuqi on 2016/12/5 0005.
 * 收藏的套餐界面
 */
public class MTFavoriteComboFragment extends BaseFragment implements XListView.IXListViewListener {

    private ImageView ivNothing;
    private XListView listView;
    private LinearLayout ll_select;
    private CheckBox tv_select;
    private Button btn_cancle;
    private FavoriteAdapter adapter;
    private MTNetRequest requestData;
    private boolean isNothing;
    private Map<Integer, Boolean> flags = new HashMap<>();
    StringBuilder sbCollectIds = new StringBuilder();
    List<String> checkCollectIds = new ArrayList<>();
    List<Boolean> isAllSelectFlags = new ArrayList<>();
    private boolean isCompile;
    private JPNetRequest jpNetRequest;
    private MtMyFavoriteComboEntity.ListBean listBean;
    private List<MtMyFavoriteComboEntity.ListBean> comboList=new ArrayList<>();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_favorite_fragment, container, false);
        ivNothing = (ImageView) view.findViewById(R.id.imageView);
        listView = (XListView) view.findViewById(R.id.lv_favorite_content);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);//刷新
        listView.setPullLoadEnable(false);//加载更多
        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        tv_select = (CheckBox) view.findViewById(R.id.tv_all_select);
        btn_cancle = (Button) view.findViewById(R.id.cancel);
        if (adapter == null) {
            adapter = new FavoriteAdapter(comboList, isCompile);
        }
        listView.setAdapter(adapter);

        initListener();
        return view;
    }

    @Override
    protected void loadData() {
        if (requestData == null) {
            requestData = new MTNetRequest(mContext);
        }
        requestData.getMyFavoriteComboList(new RequestCallBack<MtMyFavoriteComboEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MtMyFavoriteComboEntity> responseInfo) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        switch (responseInfo.result.code) {
                            case 1:
                                List<MtMyFavoriteComboEntity.ListBean> list = responseInfo.result.list;
                                Logger.i("请求的套餐请求数据" + comboList.toString());
                                comboList.clear();
                                if (null != list && list.size() > 0) {
                                    isNothing = false;
                                    listView.setVisibility(View.VISIBLE);
                                    ivNothing.setVisibility(View.GONE);
                                    comboList.addAll(list);
                                    adapter.setData(comboList);
                                } else {
                                    isNothing = true;
                                    ivNothing.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }
                                break;
                        }
                    }
                    listView.stopRefresh();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listView.stopRefresh();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isVisble)
            loadData();
    }


    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if ("0".equals(listBean.status)){
//                    showToast("此套餐已下架");
//                    return;
//                }
//
//                Intent intent=new Intent(mContext, MTComboDetailsActivity.class);
//                intent.putExtra("package_id",listBean.collectId);
//                intent.putExtra("merchant_id","0");
//                mContext.startActivity(intent);

                MtMyFavoriteComboEntity.ListBean itemAtPosition = (MtMyFavoriteComboEntity.ListBean) adapterView.getItemAtPosition(position);
                if ("0".equals(itemAtPosition.status)) {
                    showToast("此套餐已下架");
                    return;
                }

                Intent intent = new Intent(mContext, MTComboDetailsActivity.class);
                intent.putExtra("package_id", itemAtPosition.collectId);
                intent.putExtra("merchant_id", "0");
                mContext.startActivity(intent);
            }
        });

    }

    //取消收藏
    private void cancle() {
        sbCollectIds.delete(0, sbCollectIds.length());
        checkCollectIds.clear();

        for (int i = 0; i < comboList.size(); i++) {
            if (flags.get(i)) {
                checkCollectIds.add(comboList.get(i).collectIndex);
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
            showToast("请选择需要取消的套餐");
            return;
        } else if (TextUtils.isEmpty(sbCollectIdsStr) && isNothing) {
            showToast("亲，没有可取消的套餐哦");
            return;
        }
        initCancleData(sbCollectIdsStr);
    }

    private void initCancleData(String sbCollectIdsStr) {
        startMyDialog();
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(getActivity());
        }
        jpNetRequest.getCancleFavoriteist(sbCollectIdsStr, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                initActivirtyState(false);
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("取消成功");
                        loadData();
                        break;
                    case -3:
                        showToast("系统繁忙请稍后重试");
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

        adapter.isComplie = isCompile;
        if (adapter.isComplie) {
            adapter.notifyDataSetChanged();
        }
    }

    private void bianli() {
        isAllSelectFlags.clear();
        for (int i = 0; i < comboList.size(); i++) {
            isAllSelectFlags.add(flags.get(i));
        }
        if (isAllSelectFlags.contains(false)) {
            tv_select.setChecked(false);
        } else {
            tv_select.setChecked(true);
        }
    }

    @Override //给按钮设置点击事件
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    for (int i = 0; i < comboList.size(); i++) {
                        flags.put(i, true);
                    }
                } else {
                    // 取消全选时  改变里面的所有标记为选中
                    for (int i = 0; i < comboList.size(); i++) {
                        flags.put(i, false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onLoadMore() {

    }

    class FavoriteAdapter extends BaseAdapter {

        private boolean isComplie;
        private List<MtMyFavoriteComboEntity.ListBean> data;

        public FavoriteAdapter(List<MtMyFavoriteComboEntity.ListBean> data, boolean isComplie) {
            this.data = data;
            this.isComplie = isComplie;
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
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = View.inflate(mContext, R.layout.mt_item_favorite_combo, null);
                holder = new ViewHolder();
                holder.view = view.findViewById(R.id.view);
                holder.comboIcon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.comboPrice = (TextView) view.findViewById(R.id.tv_price);
                holder.cbSelect = (CheckBox) view.findViewById(R.id.cb_select);
                holder.comboTitle = (TextView) view.findViewById(R.id.tv_combo_title);
                holder.comboDiscounts = (TextView) view.findViewById(R.id.tv_discount);
                holder.cashQuan = (TextView) view.findViewById(R.id.tv_cash_quan);
                holder.nowBuy = (TextView) view.findViewById(R.id.btn_buy);
                holder.ivIconNon = (ImageView) view.findViewById(R.id.iv_icon_non);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final int finalPosition = position;
            listBean = data.get(position);

            //只有第一条才显示
            if (position == 0) {
                holder.view.setVisibility(View.VISIBLE);
            } else {
                holder.view.setVisibility(View.GONE);
            }
            if ("0".equals(listBean.status)) {
                holder.ivIconNon.setVisibility(View.VISIBLE);
            } else {
                holder.ivIconNon.setVisibility(View.GONE);
            }
            //防止每次点击图片都刷新
            if (listBean.collectIcon.equals(holder.comboIcon.getTag())) {
            } else {
                holder.comboIcon.setTag(listBean.collectIcon);
                imageLoader.displayImage(Constants.ImageUrl + listBean.collectIcon + Constants.ImageSuffix, holder.comboIcon);
            }

            holder.comboTitle.setText(listBean.collectName);
            holder.comboPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(listBean.collectPrice)));
            holder.cashQuan.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBiNoZero(listBean.collectPrice)));
            if (isComplie) {
                holder.cbSelect.setVisibility(View.VISIBLE);
            } else {
                holder.cbSelect.setVisibility(View.GONE);
                //gone 默认清空 界面清空，集合清空
                for (int i = 0; i < data.size(); i++) {
                    flags.put(i, false);
                    holder.cbSelect.setClickable(flags.get(position));
                }
            }

            holder.cbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    flags.put(finalPosition, ((CheckBox) view).isChecked());
                    bianli();
                }
            });
            //给距离赋值
            float distance = AMapDistanceUtils.getSingleDistance2(listBean.latitude, listBean.longitude);
            final DecimalFormat decimalFormat = new DecimalFormat("0.0");

            if (TextUtils.isEmpty(String.valueOf(distance))) {
                holder.comboDiscounts.setText("计算距离失败");
            } else {
                if (distance > 1000) {
                    holder.comboDiscounts.setText(decimalFormat.format(distance / 1000) + "km");
                } else {
                    holder.comboDiscounts.setText((int)distance + "m");
                }
            }
            return view;
        }


        public void setData(List<MtMyFavoriteComboEntity.ListBean> data) {
            this.data = data;
            //默认都是未选中的
            for (int i = 0; i < data.size(); i++) {
                flags.put(i, false);
            }
            notifyDataSetChanged();
        }


    }

    class ViewHolder {
        View view;
        ImageView comboIcon;
        CheckBox cbSelect;
        TextView comboTitle;
        TextView comboDiscounts;
        TextView comboPrice;
        TextView cashQuan;
        TextView nowBuy;
        ImageView ivIconNon;
    }
}
