package com.yilian.mall.jd.fragment.goodsdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;


/**
 * 图文详情里的包装售后的Fragment
 */
public class JDGoodsInfoAfterSaleFragment extends Fragment {
    public static final String TAG_PACK_LIST = "GoodsInfoServiceFragmentPackList";
    public static final String TAG_AFTER_SALE_SERVICE = "GoodsInfoServiceFragmentAfterSaleService";
    private View view;
    private TextView tvPackList;
    private LinearLayout llPackList;
    private TextView tvAfterSaleService;
    private LinearLayout llAfterSaleService;
    private String afterSaleService;
    private String packList;

    static JDGoodsInfoAfterSaleFragment newInstance(String packList, String afterSaleService) {
        JDGoodsInfoAfterSaleFragment goodsInfoServiceFragment = new JDGoodsInfoAfterSaleFragment();
        Bundle args = new Bundle();
        args.putString(TAG_PACK_LIST, packList);
        args.putString(TAG_AFTER_SALE_SERVICE, afterSaleService);
        goodsInfoServiceFragment.setArguments(args);
        return goodsInfoServiceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jd_fragment_goods_info_service, null);
        packList = getArguments().getString(TAG_PACK_LIST);
        afterSaleService = getArguments().getString(TAG_AFTER_SALE_SERVICE);
        initView(view);
        initData();
        return view;
    }


    public void initView(View view) {

        tvPackList = (TextView) view.findViewById(R.id.tv_pack_list);
        llPackList = (LinearLayout) view.findViewById(R.id.ll_pack_list);
        tvAfterSaleService = (TextView) view.findViewById(R.id.tv_after_sale_service);
        llAfterSaleService = (LinearLayout) view.findViewById(R.id.ll_after_sale_service);
    }


    public void initData() {
        if (packList==null|| TextUtils.isEmpty(packList.trim())) {
            llPackList.setVisibility(View.GONE);
        }else{
            llPackList.setVisibility(View.VISIBLE);
            tvPackList.setText(packList);
        }
        if (afterSaleService==null|| TextUtils.isEmpty(afterSaleService.trim())) {
            llAfterSaleService.setVisibility(View.GONE);
        }else{
            llAfterSaleService.setVisibility(View.VISIBLE);
            tvAfterSaleService.setText(afterSaleService);
        }
    }

    public void initListener() {

    }


}
