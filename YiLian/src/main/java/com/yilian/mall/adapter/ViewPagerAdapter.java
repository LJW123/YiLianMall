package com.yilian.mall.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


public class ViewPagerAdapter extends PagerAdapter {

	private ImageView []imgs;

	public ViewPagerAdapter(ImageView[] imageViews) {
		this.imgs = imageViews;
	}

	public int getDataCount(){
		if (imgs!=null&&imgs.length>0) return imgs.length;
		return 0;
	}

	@Override
	public int getCount() {
		return imgs.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		container.addView(imgs[position%imgs.length],params);
		return imgs[position%imgs.length];
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(imgs[position%imgs.length]);
	}
}
