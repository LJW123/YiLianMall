package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshBase.Mode;
import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.GetWinnerListResult;
import com.yilian.mall.entity.PrizeWinnerInfo;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.ImageLoaderUtil;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 活动中奖列表
 */
public class PrizeWinnerListActivity extends BaseActivity {
	
	@ViewInject(R.id.tv_back)
	TextView mTvBack;
	
	@ViewInject(R.id.right_textview)
	TextView mTvRight;
	
	@ViewInject(R.id.sv_list)
	PullToRefreshScrollView mSvList;
	
	@ViewInject(R.id.lv_winners)
	ListView mLvWinners;
	
	private ActivityNetRequest mActivityRequest;
	private WinnerListAdapter mAdapter;
	private int mPageIndex=0;
	private List<PrizeWinnerInfo> mWinnerList;
	private DisplayImageOptions imageOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prize_winner_list);
		ViewUtils.inject(this);
		init();
		getWinnerList();
	}

	
	@Override
	public void rightTextview(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra("title_name", getString(R.string.activity_rule));
		intent.putExtra("url", Constants.ActivityRules);
		startActivity(intent);
	}

	private void init() {
		mTvBack.setText(getIntent().getStringExtra("activity_name"));
		mTvRight.setText("活动规则");
		mWinnerList=new ArrayList<PrizeWinnerInfo>();
		mActivityRequest=new ActivityNetRequest(mContext);
		mAdapter=new WinnerListAdapter();
		mLvWinners.setAdapter(mAdapter);
		mSvList.setMode(Mode.PULL_FROM_END);
		mSvList.getLoadingLayoutProxy(false, true).setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));
		mSvList.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				if (refreshView.getCurrentMode()==Mode.PULL_FROM_END) {
					getWinnerList();
				}
			}
		});
		imageOptions = new DisplayImageOptions.Builder().showStubImage(R.mipmap.login_module_default_jp)
				.showImageForEmptyUri(R.mipmap.login_module_default_jp).showImageOnFail(R.mipmap.login_module_default_jp)
				// 这里的三张状态用一张替代
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	private void getWinnerList() {
		startMyDialog();
		String activityIndex=getIntent().getStringExtra("activity_index");
		String type=getIntent().getStringExtra("activity_type");
		RequestCallBack<GetWinnerListResult> callBack=new RequestCallBack<GetWinnerListResult>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				stopMyDialog();
				mSvList.onRefreshComplete();
				showToast(R.string.net_work_not_available);
			}

			@Override
			public void onSuccess(ResponseInfo<GetWinnerListResult> arg0) {
				// TODO Auto-generated method stub
				mSvList.onRefreshComplete();
				stopMyDialog();
				if (arg0.result==null) {
					return;
				}
				GetWinnerListResult result=arg0.result;
				mWinnerList.addAll(result.list);
				if (mWinnerList.size()==0) {
					showToast("无中奖记录");
					return;
				}
//				mLvWinners.setAdapter(new WinnerListAdapter());
				mAdapter.setDataSource(mWinnerList);
				CommonUtils.setListViewHeightBasedOnChildren(mLvWinners);
				++mPageIndex;
			}
		};
		if (Constants.ACTIVITY_TYPE_SHAKE.equals(type)) {
			mActivityRequest.getShakeWinnerList(activityIndex,mPageIndex, callBack);
		}else if (Constants.ACTIVITY_TYPE_TURN.equals(type)) {
			mActivityRequest.getTurntableWinnerList(activityIndex,mPageIndex, callBack);
		}else if (Constants.ACTIVITY_TYPE_SCRAPING.equals(type)) {
			mActivityRequest.getScrapeWinnerList(activityIndex,mPageIndex, callBack);
		}
	}
	
	
	class WinnerListAdapter extends BaseAdapter{
		
		private List<PrizeWinnerInfo> list;
		private ImageLoaderUtil imageLoaderUtil=new ImageLoaderUtil(mContext);
		
		public void setDataSource(List<PrizeWinnerInfo> list) {
			this.list=list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list==null?0:list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list==null?null:list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView==null) {
				viewHolder=new ViewHolder();
				convertView=getLayoutInflater().inflate(R.layout.item_winner_list, null);
				viewHolder.mTvPrizeName=(TextView) convertView.findViewById(R.id.tv_prize_name);
				viewHolder.mTvUserTel=(TextView) convertView.findViewById(R.id.tv_user_tel);
				viewHolder.mTvPrizeTime=(TextView) convertView.findViewById(R.id.tv_prize_time);
				viewHolder.mImgIcon=(ImageView) convertView.findViewById(R.id.imgView_icon);
				convertView.setTag(viewHolder);
			}else {
				viewHolder=(ViewHolder) convertView.getTag();
			}
			PrizeWinnerInfo winnerInfo=list.get(position);
			viewHolder.mTvPrizeName.setText(winnerInfo.getPrizeName());
			String tel=winnerInfo.getUserPhone();
			viewHolder.mTvUserTel.setText(tel.replace(tel.substring(3, 7), "****"));
			String time="中奖日期  "+new SimpleDateFormat("yyyy/MM/dd",Locale.US).format(new Date(winnerInfo.getPrizeTime()*1000));
			viewHolder.mTvPrizeTime.setText(time);
			ImageLoader.getInstance().displayImage(Constants.ImageUrl+winnerInfo.getPrizeImgUrl(), viewHolder.mImgIcon,imageOptions);
//			imageLoaderUtil.displayImage1(Constants.ImageUrl+winnerInfo.getPrizeImgUrl(), viewHolder.mImgIcon, false);
			return convertView;
		}
		
		class ViewHolder{

			TextView mTvPrizeName;
			
			TextView mTvUserTel;
			
			TextView mTvPrizeTime;
			ImageView mImgIcon;
		}
	}
}
