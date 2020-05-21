package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPMyFavoriteMerchantEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.ui.MTMerchantDetailActivity;
import com.yilian.mall.utils.AMapDistanceUtils;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.XlistView.XListView;
import com.yilian.mylibrary.GlideUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyuqi on 2016/10/22 0022.
 * 收藏-本地商家
 */

public class JPFavoriteMerchantFragment extends BaseFragment implements XListView.IXListViewListener {

    public boolean isNothing = false;
    StringBuilder sbCollectIds = new StringBuilder();
    List<String> checkCollectIds = new ArrayList<>();
    List<Boolean> isAllSelectFlags = new ArrayList<>();
    private MerchantAdapter adapter;
    private XListView listView;
    private LinearLayout ll_select;
    private MTNetRequest requestData;
    private boolean isCompile;
    private CheckBox tv_select;
    private Button cancle;
    private List<JPMyFavoriteMerchantEntity.ListBean> copylist = new ArrayList<>();
    private ImageView imageView;
    // 用来存所有 checkbox 的选中状态
    private Map<Integer, Boolean> flags = new HashMap<>();
    private JPMyFavoriteMerchantEntity.ListBean beanList;
    private JPNetRequest cancleFavorite;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_favorite_fragment, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        listView = (XListView) view.findViewById(R.id.lv_favorite_content);
        listView.setXListViewListener(this);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(false);
        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        tv_select = (CheckBox) view.findViewById(R.id.tv_all_select);
        cancle = (Button) view.findViewById(R.id.cancel);
        if (adapter == null)
            adapter = new MerchantAdapter(copylist, isCompile);
        listView.setAdapter(adapter);
        initData();
        initListener();
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

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if ("0".equals(adapter.Datas.get(position - 1).status)) {
                    showToast("此商家已下架");
                    return;
                }
                //跳转商家详情
                Intent intent = new Intent(getActivity(), MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id", adapter.Datas.get(position - 1).collectId);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        if (requestData == null) {
            requestData = new MTNetRequest(mContext);
        }
        startMyDialog();
        requestData.getMyFavoriteMerchantList(new RequestCallBack<JPMyFavoriteMerchantEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPMyFavoriteMerchantEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        List<JPMyFavoriteMerchantEntity.ListBean> listBeen = responseInfo.result.list;
                        copylist.clear();
                        if (listBeen.size() > 0) {
                            isNothing = false;
                            listView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            copylist.addAll(listBeen);
                            adapter.setData(copylist);
                        } else {
                            imageView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            isNothing = true;
                        }
                        listView.stopRefresh();
                        tv_select.setChecked(false);
                        stopMyDialog();

                        break;
                    case -18://没有数据,编辑按钮不可点击
                        imageView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        isNothing = true;
                        listView.stopRefresh();
                        stopMyDialog();
                        break;
                    case -4:
                    case -23:
                    case -24:
                        showToast(R.string.login_failure);
                        listView.stopRefresh();
                        stopMyDialog();
                        if (copylist.size() <= 0) {
                            imageView.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                listView.stopRefresh();
                stopMyDialog();
                Toast.makeText(mContext, "请重新加载数据", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //全选
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    flags.clear();
                    for (int i = 0; i < copylist.size(); i++) {
                        flags.put(i, true);
                    }
                } else {
                    // 取消全选时  改变里面的所有标记为选中
                    for (int i = 0; i < copylist.size(); i++) {
                        flags.put(i, false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle();
            }
        });
    }

    private void cancle() {
        sbCollectIds.delete(0, sbCollectIds.length());
        checkCollectIds.clear();

        for (int i = 0; i < copylist.size(); i++) {
            if (flags.get(i)) {
                checkCollectIds.add(copylist.get(i).collectIndex);
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
            showToast("请选择需要取消的本地商家");
            return;
        } else if (sbCollectIdsStr.length() <= 0 && isNothing) {
            showToast("亲，没有可取消的本地商家哦");
            return;
        }

        initCancleData(sbCollectIdsStr);
    }

    private void initCancleData(String sbCollectIdsStr) {
        if (cancleFavorite == null) {
            cancleFavorite = new JPNetRequest(getActivity());
        }
        startMyDialog();
        cancleFavorite.getCancleFavoriteist(sbCollectIdsStr, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                initActivirtyState(false);
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("取消成功");
                        initData();
                        break;
                    case -3:
                        showToast("系统繁忙请稍后重试");
                        initActivirtyState(false);
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

    private void bianli() {
        isAllSelectFlags.clear();
        for (int i = 0; i < copylist.size(); i++) {
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


    class MerchantAdapter extends BaseAdapter {
        private boolean isCompile;
        private List<JPMyFavoriteMerchantEntity.ListBean> Datas;
        private DisplayImageOptions options;

        public MerchantAdapter(List<JPMyFavoriteMerchantEntity.ListBean> listBeen, boolean isCompile) {
            this.Datas = listBeen;
            this.isCompile = isCompile;
            for (int i = 0; i < Datas.size(); i++) {
                flags.put(i, false);
            }
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                    .cacheOnDisc(false) //设置下载的图片是否缓存在SD卡中
                    .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//              .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
                    .build();
            imageLoader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            if (Datas.size() == 0) {
                return 0;
            }
            return Datas.size();
        }

        @Override
        public Object getItem(int i) {
            return Datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            MerchantHolder holder;
            if (view == null) {
                view = View.inflate(JPFavoriteMerchantFragment.this.getActivity(), R.layout.item_favorite_merchant, null);
                holder = new MerchantHolder();
                holder.llBg = (LinearLayout) view.findViewById(R.id.ll_bg);
                holder.cbIsSelect = (CheckBox) view.findViewById(R.id.cb_select);
                holder.goodsIcon = (ImageView) view.findViewById(R.id.goods_icon);
                holder.goodsName = (TextView) view.findViewById(R.id.tv_goods_name);
                holder.goodsratingbar = (RatingBar) view.findViewById(R.id.goods_ratingbar);
                holder.goodsGrade = (TextView) view.findViewById(R.id.goods_grade);
                holder.industry = (TextView) view.findViewById(R.id.tv_industry);
                holder.location = (TextView) view.findViewById(R.id.tv_location);
                holder.distance = (TextView) view.findViewById(R.id.tv_distance);
                holder.goodsIconNon = (ImageView) view.findViewById(R.id.goods_icon_non);
                holder.tvPrivilege = (TextView) view.findViewById(R.id.tv_privilege);
                view.setTag(holder);
            } else {
                holder = (MerchantHolder) view.getTag();
            }

            final int finalPosition = position;
            beanList = this.Datas.get(position);
            Logger.i("本地商家收藏数据" + beanList.toString());
            if (position == 0) {
                holder.llBg.setVisibility(View.VISIBLE);
            } else {
                holder.llBg.setVisibility(View.GONE);
            }

            if ("0".equals(beanList.status)) {
                holder.goodsIconNon.setVisibility(View.VISIBLE);
            } else {
                holder.goodsIconNon.setVisibility(View.GONE);
            }

            GlideUtil.showImageWithSuffix(mContext, beanList.collectIcon, holder.goodsIcon);
            holder.goodsName.setText(beanList.collectName);
            if (isCompile) {
                holder.cbIsSelect.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < Datas.size(); i++) {
                    flags.put(i, false);
                }
                holder.cbIsSelect.setChecked(flags.get(position));
                holder.cbIsSelect.setVisibility(View.GONE);
            }

            if (null != beanList.gradeCount) {
                float grade = Float.parseFloat(beanList.gradeScore) / 10.0f;
                holder.goodsratingbar.setRating(grade == 0 ? (float) 5.0 : grade);
                holder.goodsGrade.setText(beanList.gradeCount + "赞");
            }
            if (!TextUtils.isEmpty(beanList.merDiscount)) {
                holder.tvPrivilege.setVisibility(View.VISIBLE);
                holder.tvPrivilege.setText(Html.fromHtml(beanList.merDiscount + "<font><small><small>%</small></small></font>"));
            } else {
                holder.tvPrivilege.setVisibility(View.GONE);
            }

            //根据经纬度获得距离差
            float distance = AMapDistanceUtils.getSingleDistance2(beanList.latitude, beanList.longitude);
            final DecimalFormat decimalFormat = new DecimalFormat("0.0");

            if (TextUtils.isEmpty(String.valueOf(distance))) {
                holder.distance.setText("计算距离失败");
            } else {
                if (distance > 1000) {
                    holder.distance.setText(decimalFormat.format(distance / 1000) + "km");
                } else {
                    holder.distance.setText((int)distance + "m");
                }
            }

            holder.industry.setText(beanList.industry);
            String location = beanList.address;
            StringBuffer locationStr = new StringBuffer();
            for (int i = 0; i < location.length(); i++) {
                if (i < 8) {
                    locationStr.append(location.charAt(i));
                } else if (i == 8) {
                    locationStr.append("...");
                }
            }
            holder.location.setText(locationStr);
            holder.cbIsSelect.setChecked(flags.get(position));

            holder.cbIsSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    flags.put(finalPosition, ((CheckBox) view).isChecked());
                    bianli();
                }
            });

            return view;
        }

        public void setData(List<JPMyFavoriteMerchantEntity.ListBean> data) {
            this.Datas = data;
            for (int i = 0; i < Datas.size(); i++) {
                flags.put(i, false);
            }
            notifyDataSetChanged();
        }
    }

    class MerchantHolder {
        LinearLayout llBg;
        CheckBox cbIsSelect;
        ImageView goodsIcon;
        TextView goodsName;
        RatingBar goodsratingbar;
        TextView goodsGrade;
        TextView industry;
        TextView location;
        TextView distance;
        TextView tvPrivilege;
        ImageView goodsIconNon;
    }
}
