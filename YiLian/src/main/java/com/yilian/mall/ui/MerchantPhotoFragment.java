package com.yilian.mall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.AlbumGridViewAdapter;
import com.yilian.mall.listener.onLoadErrorListener;
import com.yilian.mall.ui.fragment.BaseFragment;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.networkingmodule.entity.MerchantPhotoAlbum;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/5/8 0008.
 */
public class MerchantPhotoFragment extends BaseFragment implements View.OnClickListener {


    public String groupId, merchantId;
    public int page;
    private GridView gridView;
    private ImageView ivNothing;
    private LinearLayout llError;
    private TextView tvRefresh;
    private ArrayList<String> mAlbumList;
    private onLoadErrorListener onLoadErrorListener;
    private MySwipeRefreshLayout mySwipeRefreshLayout;
    private AlbumGridViewAdapter adapter;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_photo_fragment, container, false);
        mySwipeRefreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.my_swipe_refresh_layout);
        mySwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.BOTH);
        mySwipeRefreshLayout.setColorSchemeColors(Color.RED);
        gridView = (GridView) view.findViewById(R.id.gridView);
        ivNothing = (ImageView) view.findViewById(R.id.iv_nothing);
        llError = (LinearLayout) view.findViewById(R.id.ll_error_view);
        llError.setVisibility(View.GONE);
        tvRefresh = (TextView) view.findViewById(R.id.tv_update_error);
        page = 0;
        mAlbumList = new ArrayList<>();
        initListener();
        return view;
    }

    private void initListener() {
        tvRefresh.setOnClickListener(this);

        mySwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                loadData();
            }
        });

        mySwipeRefreshLayout.setOnPullUpRefreshListener(new MySwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                page++;
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        String count = "30";
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantPackageAlbum(String.valueOf(page), count, merchantId, groupId, new Callback<MerchantPhotoAlbum>() {

                    @Override
                    public void onResponse(Call<MerchantPhotoAlbum> call, Response<MerchantPhotoAlbum> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        List<String> albumList = response.body().data.album;
                                        Logger.i("albumList  "+albumList+"  adapter  "+adapter);
                                        if (null != albumList && albumList.size() > 0) {
                                            gridView.setVisibility(View.VISIBLE);
                                            if (page > 0) {
                                                mAlbumList.addAll(albumList);
                                            } else {
                                                mAlbumList.clear();
                                                mAlbumList.addAll(albumList);
                                                if (adapter==null){
                                                    adapter = new AlbumGridViewAdapter(mAlbumList);
                                                }
                                                gridView.setAdapter(adapter);
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            if (page > 0) {
                                                showToast("暂无更多图片");
                                            } else {
                                                ivNothing.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        stopMyDialog();
                                        mySwipeRefreshLayout.setPullUpRefreshing(false);
                                        mySwipeRefreshLayout.setRefreshing(false);
                                        break;
                                    default:
                                        showToast(response.body().code);
                                        llError.setVisibility(View.VISIBLE);
                                        stopMyDialog();
                                        mySwipeRefreshLayout.setPullUpRefreshing(false);
                                        mySwipeRefreshLayout.setRefreshing(false);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantPhotoAlbum> call, Throwable t) {
                        Logger.e("onFailure" + t);
                        llError.setVisibility(View.VISIBLE);
                        mySwipeRefreshLayout.setPullUpRefreshing(false);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update_error:
                loadData();
                break;
        }
    }
}
