package com.yilian.mall.suning.fragment.goodsdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilian.mall.R;
import com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity;
import com.yilian.mall.jd.view.ItemWebView;

/**
 * 图文详情里的规格参数的Fragment
 */
public class SnGoodsInfoConfigFragment extends Fragment {
    public static final String TAG = "GoodsInfoConfigFragment";
    public JDGoodsDetailActivity activity;
    public RecyclerView lv_config;
    private ItemWebView webViewConfig;
    private String htmlText;

    static SnGoodsInfoConfigFragment newInstance(String htmlText) {
        SnGoodsInfoConfigFragment goodsInfoConfigFragment = new SnGoodsInfoConfigFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, htmlText);
        goodsInfoConfigFragment.setArguments(bundle);
        return goodsInfoConfigFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (JDGoodsDetailActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jd_fragment_item_config, null);
        webViewConfig = view.findViewById(R.id.web_view_config);
        htmlText = getArguments().getString(TAG);
        webViewConfig.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);
        return view;
    }
}
