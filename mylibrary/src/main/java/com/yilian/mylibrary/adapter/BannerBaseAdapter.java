package com.yilian.mylibrary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mylibrary.widget.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2017/6/20 0020.
 */

public abstract class BannerBaseAdapter<T> extends PagerAdapter {

    private Context mContext;
    private List<T> mDatas = new ArrayList<>();
//    private OnPageTouchListener mListener;
    private long mDownTime;
    private View mConvertView;
    private BannerView mBannerView;

    public BannerBaseAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {
        return mDatas == null || mDatas.size() == 0 ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public T getItem(int position) {
        return position >= mDatas.size() ? mDatas.get(0) : mDatas.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mConvertView = LayoutInflater.from(mContext).inflate(getLayoutResID(position), null);
        mConvertView.setClickable(true);

        if (mDatas != null && mDatas.size() != 0) {
            position = position % mDatas.size();
        }
        if (mDatas != null) {
            // 处理视图和数据
            convert(mConvertView, getItem(position),position);
        }

        final int finalPosition = position;

        container.addView(mConvertView);
        return mConvertView;
    }


    public void setData(List<T> datas) {
        if (datas == null) return;
        this.mDatas = new ArrayList<>(datas);
        notifyDataSetChanged();
        if (mBannerView != null) {
            mBannerView.resetCurrentPosition(datas.size());
        }
    }


    protected <K extends View> K getView(@IdRes int id) {
        return (K) mConvertView.findViewById(id);
    }


    protected BannerBaseAdapter setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    protected BannerBaseAdapter setImage(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
        return this;
    }

    protected BannerBaseAdapter setImage(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }


    /**
     * 获取Item对象
     *
     * @return
     */
    protected View getItemView() {
        return mConvertView;
    }

    // 绑定视图和数据
    protected abstract int getLayoutResID(int position);

    protected abstract void convert(View convertView, T data, int position);

//    public void setOnPageTouchListener(OnPageTouchListener<T> listener) {
//        this.mListener = listener;
//    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setBannerView(BannerView bannerView) {
        this.mBannerView = bannerView;
    }

//    /**
//     * 条目页面的触摸事件
//     */
//    public interface OnPageTouchListener<T> {
//        void onPageClick(int position, T t);
//
//        void onPageDown();
//
//        void onPageUp();
//    }
}
