package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
import com.yilian.mall.entity.JPMyFavoriteStorEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.ui.JPFlagshipActivity;
import com.yilian.mall.widgets.XlistView.XListView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyuqi on 2016/10/22 0022.
 * 收藏-旗舰店
 */

public class JPFavoriteStorFragment extends BaseFragment implements XListView.IXListViewListener {

    public boolean isCompile;
    public boolean isNothing = false;
    StringBuilder sbCollectIds = new StringBuilder();
    List<String> checkCollectIds = new ArrayList<>();
    List<Boolean> isAllSelectFlags = new ArrayList<>();
    private int screenWidth;
    private int height;
    private XListView listView;
    private LinearLayout ll_select;
    private CheckBox tv_select;
    private Button btn_cancle;
    private storAdapter adapter;
    private List<JPMyFavoriteStorEntity.ListBean> copylist = new ArrayList<>();
    private MTNetRequest requestData;
    private ImageView imageView;
    private Map<Integer, Boolean> flags = new HashMap<>();
    private JPNetRequest cancleFavorite;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_favorite_fragment, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        listView = (XListView) view.findViewById(R.id.lv_favorite_content);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        tv_select = (CheckBox) view.findViewById(R.id.tv_all_select);
        btn_cancle = (Button) view.findViewById(R.id.cancel);
        if (adapter == null)
            adapter = new storAdapter(copylist, isCompile);
        listView.setAdapter(adapter);

        //计算屏幕的宽高设置图片的宽高比例
        screenWidth = ScreenUtils.getScreenWidth(getActivity());
        height = (int) (screenWidth * 0.38);
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

    private void initData() {
        if (requestData == null) {
            requestData = new MTNetRequest(mContext);
        }
        startMyDialog();
        requestData.getMyFavoriteStorList(new RequestCallBack<JPMyFavoriteStorEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPMyFavoriteStorEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        List<JPMyFavoriteStorEntity.ListBean> listBeen = responseInfo.result.list;
                        Logger.i("收藏旗舰店"+responseInfo.result.list.toString());
                        copylist.clear();
                        if (listBeen.size() > 0) {
                            isNothing = false;
                            listView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            copylist.addAll(listBeen);

                        }else{
                            imageView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            isNothing = true;
                        }

                        adapter.setData(copylist);
                        listView.stopRefresh();
                        tv_select.setChecked(false);
                        stopMyDialog();
                        break;
                    case -18:
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
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle();
            }
        });
    }


    //取消收藏
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
            showToast("请选择需要取消的旗舰店");
            return;
        }else if (TextUtils.isEmpty(sbCollectIdsStr) && isNothing){
            showToast("亲，没有可取消的旗舰店哦");
            return;
        }
        initCancleData(sbCollectIdsStr);
    }

    private void initCancleData(String sbCollectIdsStr) {
        startMyDialog();
        if (cancleFavorite ==null){
            cancleFavorite =new JPNetRequest(getActivity());
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

    class storAdapter extends BaseAdapter {
        private List<JPMyFavoriteStorEntity.ListBean> Datas;
        private boolean isCompile;
        private DisplayImageOptions options;

        public storAdapter(List<JPMyFavoriteStorEntity.ListBean> listBeen, boolean isCompile) {
            this.Datas = listBeen;
            this.isCompile = isCompile;
            for (int i = 0; i < Datas.size(); i++) {
                flags.put(i, false);
            }
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
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
        public View getView(final int position, View view, ViewGroup viewGroup) {
            StoreHolder holder;
            if (view == null) {
                view = View.inflate(JPFavoriteStorFragment.this.getActivity(), R.layout.item_favorite_store, null);
                holder = new StoreHolder();
                holder.storIcon = (ImageView) view.findViewById(R.id.iv_store_icon);
                holder.storName = (TextView) view.findViewById(R.id.tv_cost_name);
                holder.unSelect = (TextView) view.findViewById(R.id.un_select);
                holder.isSelect = (CheckBox) view.findViewById(R.id.cb_select);
                holder.flContent = (FrameLayout) view.findViewById(R.id.fl_image_content);
                holder.tvDescribe = (TextView) view.findViewById(R.id.tv_tagsName);
                holder.whiteViewBg = view.findViewById(R.id.iv_white_bg);
                holder.bgContent = view.findViewById(R.id.bg_llcontent);
                view.setTag(holder);
            } else {
                holder = (StoreHolder) view.getTag();
            }

            final int finalPosition = position;
            //设置显示图片的宽高
            final JPMyFavoriteStorEntity.ListBean beanList = Datas.get(position);

            holder.flContent.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, height));

            holder.storName.setText(beanList.collectName);
            if (TextUtils.isEmpty(beanList.tagsName)) {
                holder.bgContent.setVisibility(View.GONE);
                holder.whiteViewBg.setVisibility(View.GONE);
                holder.tvDescribe.setText("");
            } else {
                holder.tvDescribe.setText(beanList.tagsName);
                if ("0".equals(beanList.statuts)){
                    holder.tvDescribe.setText("此旗舰店已下架");
                }
                holder.bgContent.setVisibility(View.VISIBLE);
                holder.whiteViewBg.setVisibility(View.VISIBLE);
            }

            holder.unSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flags.put(finalPosition, true);
                    cancle();
                }
            });

            if (isCompile) {
                holder.unSelect.setVisibility(View.GONE);
                holder.isSelect.setVisibility(View.VISIBLE);
            } else {
                holder.unSelect.setVisibility(View.VISIBLE);
                holder.isSelect.setVisibility(View.GONE);
                for (int i = 0; i < Datas.size(); i++) {
                    flags.put(i, false);
                }
            }

            holder.isSelect.setChecked(flags.get(position));
            holder.isSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flags.put(finalPosition, ((CheckBox) view).isChecked());
                    bianli();
                }
            });

            holder.flContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("0".equals(Datas.get(position).statuts)){
                        showToast("此旗舰店已下架");
                        return;
                    }

                    //点击跳转旗舰店 不是商品详情
                    Intent intent = new Intent(getActivity(), JPFlagshipActivity.class);
                    intent.putExtra("index_id", Datas.get(position).collectId);
                    startActivity(intent);
                }
            });

            GlideUtil.showImageWithSuffix(mContext,beanList.collectIcon,holder.storIcon);

            return view;
        }

        public void setData(List<JPMyFavoriteStorEntity.ListBean> data) {
            this.Datas = data;
            for (int i = 0; i < Datas.size(); i++) {
                flags.put(i, false);
            }
            notifyDataSetChanged();
        }
    }

    class StoreHolder {
        ImageView storIcon;
        TextView storName;
        TextView unSelect;
        CheckBox isSelect;
        FrameLayout flContent;
        TextView tvDescribe;
        View whiteViewBg;
        View bgContent;
    }

}
