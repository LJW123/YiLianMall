package com.yilian.mylibrary.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.yilian.mylibrary.R;
import com.yilian.mylibrary.view.varyView.VaryViewHelper;


/**
 * 动态显示布局（加载中、显示数据、数据为空、Error等页面）
 * Created by Zg on 2018/5/23.
 */
public class VaryViewUtils {

    public VaryViewHelper mVaryViewHelper;
    public Context mContext;

    public VaryViewUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 初始化 数据请求布局
     *
     * @param id                 数据展示布局
     * @param view               父类布局文件
     * @param ErrorClickListener 点击事件
     */
    public void setVaryViewHelper(int id, View view, View.OnClickListener ErrorClickListener) {//R.id.vary_content
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(view.findViewById(id))//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_loadingview, null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_emptyview, null), R.mipmap.library_module_default_jp, "")//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_errorview, null))//错误页面
                .setRefreshListener(ErrorClickListener)//错误页点击刷新实现
                .build();

        mVaryViewHelper.showLoadingView();
    }

    /**
     * 京东订单 动态布局
     *
     * @param id                 数据展示布局
     * @param view               父类布局文件
     * @param ErrorClickListener 点击事件
     */
    public void setVaryViewByJDOrderList(int id, View view, View.OnClickListener ErrorClickListener) {//R.id.vary_content
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(view.findViewById(id))//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_loadingview, null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_emptyview, null), R.mipmap.library_module_jd_order_empty, "您还没有相关订单")//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_errorview, null))//错误页面
                .setRefreshListener(ErrorClickListener)//错误页点击刷新实现
                .build();

        mVaryViewHelper.showLoadingView();
    }

    /**
     * 京东商品列表页 动态布局
     *
     * @param id
     * @param view
     * @param ErrorClickListener
     */

    public void setJdVaryViewHelper(int id, View view, View.OnClickListener ErrorClickListener) {
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(view.findViewById(id))
                .setLoadingView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_loadingview, null))
                .setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_emptyview, null), R.mipmap.library_module_jd_icon_no_goods, "抱歉，没有找到商品")
                .setErrorView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_errorview, null))
                .setRefreshListener(ErrorClickListener)
                .build();

        mVaryViewHelper.showLoadingView();
    }

    /**
     * 苏宁列表缺省页
     *
     * @param id
     * @param view
     * @param ErrorClickListener
     */
    public void setSnVaryViewHelper(int id, View view, View.OnClickListener ErrorClickListener) {
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(view.findViewById(id))
                .setLoadingView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_loadingview, null))
                .setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_emptyview, null), R.mipmap.library_module_sn_icon_empty, "抱歉，没有找到商品")
                .setErrorView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_errorview, null))
                .setRefreshListener(ErrorClickListener)
                .build();

        mVaryViewHelper.showLoadingView();
    }

    /**
     * 苏宁订单 动态布局
     *
     * @param id                 数据展示布局
     * @param view               父类布局文件
     * @param ErrorClickListener 点击事件
     */
    public void setVaryViewBySnOrderList(int id, View view, View.OnClickListener ErrorClickListener, String msg, View.OnClickListener ToHomeClickListener) {//R.id.vary_content
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(view.findViewById(id))//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_loadingview, null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_emptyview_sn, null), R.mipmap.library_module_sn_order_empty, msg)//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_errorview, null))//错误页面
                .setRefreshListener(ErrorClickListener)//错误页点击刷新实现
                .setToHomeListener(ToHomeClickListener)//去首页
                .build();

        mVaryViewHelper.showLoadingView();
    }

    /**
     * 携程订单 动态布局
     *
     * @param id                 数据展示布局
     * @param view               父类布局文件
     * @param ErrorClickListener 点击事件
     */
    public void setCtripOrderList(int id, View view, View.OnClickListener ErrorClickListener) {//R.id.vary_content
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(view.findViewById(id))//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_loadingview, null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_emptyview, null), R.mipmap.library_module_ctrip_order_empty, "暂无相关订单")//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_errorview, null))//错误页面
                .setRefreshListener(ErrorClickListener)//错误页点击刷新实现
                .build();

        mVaryViewHelper.showLoadingView();
    }

    /**
     * 携程 查询
     *
     * @param id                 数据展示布局
     * @param view               父类布局文件
     * @param ErrorClickListener 点击事件
     */
    public void setCtripSearch(int id, View view, View.OnClickListener ErrorClickListener) {//R.id.vary_content
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(view.findViewById(id))//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_loadingview, null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_jd_layout_emptyview, null), R.mipmap.library_module_ctrip_search_empty, "暂未查到该信息~")//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(mContext).inflate(R.layout.library_module_vary_layout_errorview, null))//错误页面
                .setRefreshListener(ErrorClickListener)//错误页点击刷新实现
                .build();

        mVaryViewHelper.showLoadingView();
    }

    public void showEmptyView() {
        if (mVaryViewHelper != null) {
            mVaryViewHelper.showEmptyView();
        }
    }

    public void showErrorView() {
        if (mVaryViewHelper != null) {
            mVaryViewHelper.showErrorView();
        }
    }

    public void showLoadingView() {
        if (mVaryViewHelper != null) {
            mVaryViewHelper.showLoadingView();
        }
    }

    public void showDataView() {
        if (mVaryViewHelper != null) {
            mVaryViewHelper.showDataView();
        }
    }

}
