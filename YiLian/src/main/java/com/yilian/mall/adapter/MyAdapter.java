package com.yilian.mall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yilian.mall.R;
import com.yilian.mall.ui.DialogImageActivity;
import com.yilian.mall.utils.CommonAdapter;
import com.yilian.mall.utils.ViewHolder;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.List;

public class MyAdapter extends CommonAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;
    private Context context;
	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath)
	{
		super(context, mDatas, itemLayoutId);
		this.context = context;
		this.mDirPath = dirPath;
	}

	@Override
	public void convert(final ViewHolder helper, final String item)
	{
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.mipmap.pictures_no);
		//设置no_selected
				/*helper.setImageResource(R.id.id_item_select,
						R.drawable.picture_unselected);*/
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		//final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				/*Intent intent = new Intent(mContext, CropImageActivity.class);
				intent.putExtra(CropImageActivity.IMAGE_PATH, mDirPath + "/" + item);
				intent.putExtra(CropImageActivity.SCALE, true);
				intent.putExtra(CropImageActivity.ASPECT_X, 3);
				intent.putExtra(CropImageActivity.ASPECT_Y, 2);
				mContext.startActivity(intent);
				((DialogImageActivity) mContext).finish();*/
				Bundle extras = new Bundle();
				Intent intent = new Intent(mDirPath + "/" + item);
				Logger.i(mDirPath + "/" + item);
				intent.putExtras(extras);
				intent.putExtra("path", mDirPath + "/" + item);
				((Activity) context).setResult(10, intent);
				((DialogImageActivity) context).finish();
			}
		});

	}
}
